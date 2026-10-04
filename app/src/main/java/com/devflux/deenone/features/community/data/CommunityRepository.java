package com.devflux.deenone.features.community.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.community.model.CommunityCommentItem;
import com.devflux.deenone.features.community.model.CommunityPostItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunityRepository {

  private static final String TAG = "CommunityRepository";
  private static CommunityRepository instance;
  private final List<CommunityPostItem> postList = new ArrayList<>();
  private final SharedPreferences prefs;
  private final Context context;
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final OkHttpClient httpClient;
  private final Gson gson = new Gson();

  public static class SocialStats {
    public final int postCount;
    public final int commentCount;
    public final int likeCount;

    public SocialStats(int postCount, int commentCount, int likeCount) {
      this.postCount = postCount;
      this.commentCount = commentCount;
      this.likeCount = likeCount;
    }
  }

  private final MutableLiveData<SocialStats> socialStatsLiveData = new MutableLiveData<>(new SocialStats(0, 0, 0));

  private CommunityRepository(Context context) {
    this.context = context.getApplicationContext();
    this.prefs = this.context.getSharedPreferences("community_prefs", Context.MODE_PRIVATE);
    this.httpClient = new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build();

    loadPersistedCommunityPosts();
    recalculateAndNotifyStats();

    // Auto-sync in background on init if network is available
    if (NetworkConnectivityHelper.isOnline(this.context)) {
      syncFromRemote(false, null);
    }
  }

  public static synchronized CommunityRepository getInstance(Context context) {
    if (instance == null) {
      instance = new CommunityRepository(context);
    }
    return instance;
  }

  public LiveData<SocialStats> getSocialStatsLiveData() {
    return socialStatsLiveData;
  }

  public String getActiveUserName() {
    if (!AuthManager.isLoggedIn(context)) {
      return "সহযাত্রী";
    }
    AuthManager.UserSession session = AuthManager.getCurrentSession(context);
    if (session != null && !TextUtils.isEmpty(session.name)) {
      return session.name;
    }
    SharedPreferences userPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
    String name = userPrefs.getString("user_full_name", "");
    return !TextUtils.isEmpty(name) ? name : "সহযাত্রী";
  }

  public String getActiveUserId() {
    if (!AuthManager.isLoggedIn(context)) {
      return "guest";
    }
    AuthManager.UserSession session = AuthManager.getCurrentSession(context);
    if (session != null && !TextUtils.isEmpty(session.userId)) {
      return session.userId;
    }
    SharedPreferences pPrefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE);
    return pPrefs.getString("user_id", "usr_main");
  }

  public String getActiveUserAvatar() {
    if (AuthManager.isLoggedIn(context)) {
      AuthManager.UserSession session = AuthManager.getCurrentSession(context);
      if (session != null && !TextUtils.isEmpty(session.avatar)) {
        return session.avatar;
      }
    }
    SharedPreferences userPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
    return userPrefs.getString("user_avatar_uri", "avatar_1");
  }

  public boolean isCurrentUserAuthor(CommunityPostItem item) {
    if (item == null) return false;
    if (!AuthManager.isLoggedIn(context)) return false;
    String activeUid = getActiveUserId();
    if (!TextUtils.isEmpty(activeUid) && !TextUtils.isEmpty(item.getUserId()) && !"guest".equalsIgnoreCase(activeUid)) {
      if (activeUid.equalsIgnoreCase(item.getUserId())) return true;
    }
    String activeName = getActiveUserName();
    return isUserAuthor(item.getAuthorName(), activeName);
  }

  private synchronized void loadPersistedCommunityPosts() {
    postList.clear();
    String json = prefs.getString("persisted_community_posts", null);
    if (!TextUtils.isEmpty(json)) {
      try {
        TypeToken<List<CommunityPostItem>> typeToken = new TypeToken<List<CommunityPostItem>>() {};
        List<CommunityPostItem> saved = gson.fromJson(json, typeToken.getType());
        if (saved != null) {
          postList.addAll(saved);
        }
      } catch (Exception ignored) {}
    }

    for (CommunityPostItem p : postList) {
      p.setAmeenGiven(prefs.getBoolean("post_ameen_" + p.getId(), p.isAmeenGiven()));
    }
  }

  private synchronized void savePersistedCommunityPosts() {
    try {
      String json = gson.toJson(postList);
      prefs.edit().putString("persisted_community_posts", json).apply();
    } catch (Exception ignored) {}
  }

  public synchronized List<CommunityPostItem> getPostsByCategory(String category) {
    if (category == null || category.isEmpty() || "all".equalsIgnoreCase(category)) {
      return new ArrayList<>(postList);
    }
    List<CommunityPostItem> filtered = new ArrayList<>();
    for (CommunityPostItem item : postList) {
      if (category.equalsIgnoreCase(item.getCategory())) {
        filtered.add(item);
      }
    }
    return filtered;
  }

  /**
   * Syncs community posts from remote server.
   * Public feed is open to all users (both guests and logged-in).
   * Fully caches all posts & comments locally for offline browsing.
   */
  public void syncFromRemote(boolean forceRemote, Runnable onComplete) {
    if (!NetworkConnectivityHelper.isOnline(context)) {
      if (onComplete != null) {
        mainHandler.post(onComplete);
      }
      return;
    }

    executor.execute(() -> {
      String activeUserId = getActiveUserId();
      String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php?action=feed&limit=50&user_id=" + activeUserId);
      try {
        Request request = new Request.Builder()
            .url(url)
            .addHeader("User-Agent", "DeenOne-App/1.0")
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
          if (response.isSuccessful() && response.body() != null) {
            String jsonStr = response.body().string();
            JsonObject root = gson.fromJson(jsonStr, JsonObject.class);
            if (root != null && root.has("posts")) {
              JsonArray postsArr = root.getAsJsonArray("posts");
              List<CommunityPostItem> remotePosts = new ArrayList<>();

              for (JsonElement el : postsArr) {
                JsonObject p = el.getAsJsonObject();
                String id = p.has("id") ? p.get("id").getAsString() : "";
                String authorName = p.has("author_name") && !p.get("author_name").isJsonNull()
                    ? p.get("author_name").getAsString() : "সহযাত্রী";
                String authorLocation = p.has("author_location") && !p.get("author_location").isJsonNull()
                    ? p.get("author_location").getAsString() : "বাংলাদেশ";
                String category = p.has("category") && !p.get("category").isJsonNull()
                    ? p.get("category").getAsString() : "dua_request";
                String title = p.has("title") && !p.get("title").isJsonNull()
                    ? p.get("title").getAsString() : "";
                String content = p.has("content") && !p.get("content").isJsonNull()
                    ? p.get("content").getAsString() : "";
                int ameenCount = p.has("likes_count") ? p.get("likes_count").getAsInt() : 0;
                int commentCount = p.has("comments_count") ? p.get("comments_count").getAsInt() : 0;
                boolean isAmeenGiven = p.has("user_has_liked") && p.get("user_has_liked").getAsBoolean();
                boolean isPinned = p.has("is_pinned") && p.get("is_pinned").getAsBoolean();
                long createdAt = parseCreatedAt(p.has("created_at") && !p.get("created_at").isJsonNull()
                    ? p.get("created_at").getAsString() : null);

                // If local like state was recorded, respect it
                if (prefs.getBoolean("post_ameen_" + id, false)) {
                  isAmeenGiven = true;
                }

                String postUserId = p.has("user_id") && !p.get("user_id").isJsonNull()
                    ? p.get("user_id").getAsString() : "";
                String postAuthorAvatar = p.has("author_avatar") && !p.get("author_avatar").isJsonNull()
                    ? p.get("author_avatar").getAsString() : "";

                CommunityPostItem item = new CommunityPostItem(
                    id, authorName, authorLocation, category,
                    title, content, ameenCount, commentCount, 1,
                    isPinned, createdAt
                );
                item.setUserId(postUserId);
                item.setAuthorAvatar(postAuthorAvatar);
                item.setAmeenGiven(isAmeenGiven);

                // Parse attached comments
                if (p.has("comments") && p.get("comments").isJsonArray()) {
                  JsonArray commArr = p.getAsJsonArray("comments");
                  List<CommunityCommentItem> cList = new ArrayList<>();
                  for (JsonElement cEl : commArr) {
                    JsonObject cObj = cEl.getAsJsonObject();
                    String cId = cObj.has("id") ? cObj.get("id").getAsString() : "";
                    String cPostId = cObj.has("post_id") ? cObj.get("post_id").getAsString() : id;
                    String cAuthor = cObj.has("author_name") && !cObj.get("author_name").isJsonNull()
                        ? cObj.get("author_name").getAsString() : "সহযাত্রী";
                    String cText = cObj.has("comment_text") && !cObj.get("comment_text").isJsonNull()
                        ? cObj.get("comment_text").getAsString() : "";
                    long cCreated = parseCreatedAt(cObj.has("created_at") && !cObj.get("created_at").isJsonNull()
                        ? cObj.get("created_at").getAsString() : null);
                    String cParent = cObj.has("parent_comment_id") && !cObj.get("parent_comment_id").isJsonNull()
                        ? cObj.get("parent_comment_id").getAsString() : null;
                    String cReplyTo = cObj.has("reply_to_author") && !cObj.get("reply_to_author").isJsonNull()
                        ? cObj.get("reply_to_author").getAsString() : null;
                    String cAvatar = cObj.has("author_avatar") && !cObj.get("author_avatar").isJsonNull()
                        ? cObj.get("author_avatar").getAsString() : "avatar_1";

                    cList.add(new CommunityCommentItem(cId, cPostId, cAuthor, cText, cCreated, cParent, cReplyTo, cAvatar));
                  }
                  item.setComments(cList);
                  item.setCommentCount(Math.max(commentCount, cList.size()));
                }

                remotePosts.add(item);
              }

              synchronized (this) {
                postList.clear();
                postList.addAll(remotePosts);
                savePersistedCommunityPosts();
              }

              recalculateAndNotifyStats();
            }
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "syncFromRemote error: " + e.getMessage());
      } finally {
        if (onComplete != null) {
          mainHandler.post(onComplete);
        }
      }
    });
  }

  public interface OnCommentsLoadedListener {
    void onCommentsLoaded(List<CommunityCommentItem> comments);
  }

  public interface OnOperationCallback {
    void onSuccess(String message);
    void onError(String error);
  }

  /**
   * Fetches latest comments for a specific post from the server.
   */
  public void fetchCommentsForPost(String postId, OnCommentsLoadedListener callback) {
    if (!NetworkConnectivityHelper.isOnline(context)) {
      if (callback != null) {
        synchronized (this) {
          for (CommunityPostItem p : postList) {
            if (p.getId().equals(postId)) {
              mainHandler.post(() -> callback.onCommentsLoaded(new ArrayList<>(p.getComments())));
              return;
            }
          }
        }
        mainHandler.post(() -> callback.onCommentsLoaded(new ArrayList<>()));
      }
      return;
    }

    executor.execute(() -> {
      String cleanId = postId != null ? postId.replaceAll("\\D", "") : "";
      if (cleanId.isEmpty()) {
        if (callback != null) mainHandler.post(() -> callback.onCommentsLoaded(new ArrayList<>()));
        return;
      }
      String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php?action=comments&post_id=" + cleanId);
      try {
        Request request = new Request.Builder()
            .url(url)
            .addHeader("User-Agent", "DeenOne-App/1.0")
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
          if (response.isSuccessful() && response.body() != null) {
            String jsonStr = response.body().string();
            JsonObject root = gson.fromJson(jsonStr, JsonObject.class);
            if (root != null && root.has("comments")) {
              JsonArray commArr = root.getAsJsonArray("comments");
              List<CommunityCommentItem> cList = new ArrayList<>();
              for (JsonElement cEl : commArr) {
                JsonObject cObj = cEl.getAsJsonObject();
                String cId = cObj.has("id") ? cObj.get("id").getAsString() : "";
                String cPostId = cObj.has("post_id") ? cObj.get("post_id").getAsString() : postId;
                String cAuthor = cObj.has("author_name") && !cObj.get("author_name").isJsonNull()
                    ? cObj.get("author_name").getAsString() : "সহযাত্রী";
                String cText = cObj.has("comment_text") && !cObj.get("comment_text").isJsonNull()
                    ? cObj.get("comment_text").getAsString() : "";
                long cCreated = parseCreatedAt(cObj.has("created_at") && !cObj.get("created_at").isJsonNull()
                    ? cObj.get("created_at").getAsString() : null);
                String cParent = cObj.has("parent_comment_id") && !cObj.get("parent_comment_id").isJsonNull()
                    ? cObj.get("parent_comment_id").getAsString() : null;
                String cReplyTo = cObj.has("reply_to_author") && !cObj.get("reply_to_author").isJsonNull()
                    ? cObj.get("reply_to_author").getAsString() : null;
                String cAvatar = cObj.has("author_avatar") && !cObj.get("author_avatar").isJsonNull()
                    ? cObj.get("author_avatar").getAsString() : "avatar_1";

                cList.add(new CommunityCommentItem(cId, cPostId, cAuthor, cText, cCreated, cParent, cReplyTo, cAvatar));
              }

              synchronized (this) {
                for (CommunityPostItem p : postList) {
                  if (p.getId().equals(postId) || p.getId().equals(cleanId)) {
                    p.setComments(cList);
                    p.setCommentCount(cList.size());
                    break;
                  }
                }
                savePersistedCommunityPosts();
              }

              if (callback != null) {
                mainHandler.post(() -> callback.onCommentsLoaded(cList));
              }
              return;
            }
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "fetchCommentsForPost error: " + e.getMessage());
      }
      if (callback != null) {
        mainHandler.post(() -> callback.onCommentsLoaded(new ArrayList<>()));
      }
    });
  }

  public void fetchCommentsForPost(String postId, Runnable onComplete) {
    fetchCommentsForPost(postId, comments -> {
      if (onComplete != null) onComplete.run();
    });
  }

  private long parseCreatedAt(String dateStr) {
    if (TextUtils.isEmpty(dateStr)) return System.currentTimeMillis();
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
      Date date = sdf.parse(dateStr);
      if (date != null) return date.getTime();
    } catch (Exception ignored) {}
    try {
      return Long.parseLong(dateStr);
    } catch (Exception ignored) {}
    return System.currentTimeMillis();
  }

  public void addNewPost(String title, String content, String category) {
    String activeUser = getActiveUserName();
    String activeUserId = getActiveUserId();
    String activeAvatar = getActiveUserAvatar();
    long now = System.currentTimeMillis();
    String newId = "post_" + now;
    CommunityPostItem newPost = new CommunityPostItem(
        newId, activeUser, "বাংলাদেশ", category,
        title, content, 0, 0, 1, false, now
    );
    newPost.setUserId(activeUserId);
    newPost.setAuthorAvatar(activeAvatar);
    synchronized (this) {
      postList.add(0, newPost);
      savePersistedCommunityPosts();
    }
    recalculateAndNotifyStats();

    // Offline-First Sync Integration
    CommunitySyncManager.getInstance(context).handlePostCreated(newId, title, content, category, activeUser);
  }

  public void editPost(String postId, String newTitle, String newContent, String newCategory, OnOperationCallback callback) {
    String cleanId = postId != null ? postId.replaceAll("\\D", "") : "";
    String activeUserId = getActiveUserId();

    synchronized (this) {
      for (CommunityPostItem p : postList) {
        if (p.getId().equals(postId) || (!cleanId.isEmpty() && p.getId().equals(cleanId))) {
          p.setTitle(newTitle);
          p.setContent(newContent);
          if (newCategory != null && !newCategory.isEmpty()) {
            p.setCategory(newCategory);
          }
          break;
        }
      }
      savePersistedCommunityPosts();
    }
    recalculateAndNotifyStats();

    executor.execute(() -> {
      if (!NetworkConnectivityHelper.isOnline(context)) {
        if (callback != null) mainHandler.post(() -> callback.onSuccess("অফলাইনে সংরক্ষিত হয়েছে"));
        return;
      }
      try {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
        JsonObject json = new JsonObject();
        json.addProperty("action", "edit_post");
        json.addProperty("user_id", activeUserId);
        json.addProperty("post_id", !cleanId.isEmpty() ? cleanId : postId);
        json.addProperty("title", newTitle);
        json.addProperty("content", newContent);
        json.addProperty("category", newCategory);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
            .url(url)
            .addHeader("User-Agent", "DeenOne-App/1.0")
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
            .post(body)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
          String resStr = response.body() != null ? response.body().string() : "";
          JsonObject resObj = gson.fromJson(resStr, JsonObject.class);
          boolean success = response.isSuccessful() && resObj != null && resObj.has("success") && resObj.get("success").getAsBoolean();
          if (callback != null) {
            mainHandler.post(() -> {
              if (success) {
                callback.onSuccess(resObj.has("message") ? resObj.get("message").getAsString() : "পোস্ট সফলভাবে আপডেট হয়েছে");
              } else {
                String err = (resObj != null && resObj.has("error")) ? resObj.get("error").getAsString() : "পোস্ট এডিট করা যায়নি";
                callback.onError(err);
              }
            });
          }
        }
      } catch (Exception e) {
        if (callback != null) {
          mainHandler.post(() -> callback.onError("সার্ভার সংযোগ ত্রুটি: " + e.getMessage()));
        }
      }
    });
  }

  public void reportPost(String postId, String reason, String details, OnOperationCallback callback) {
    String cleanId = postId != null ? postId.replaceAll("\\D", "") : "";
    String activeUserId = getActiveUserId();
    String activeUserName = getActiveUserName();

    executor.execute(() -> {
      if (!NetworkConnectivityHelper.isOnline(context)) {
        if (callback != null) mainHandler.post(() -> callback.onError("ইন্টারনেট সংযোগ নেই।"));
        return;
      }
      try {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
        JsonObject json = new JsonObject();
        json.addProperty("action", "report_post");
        json.addProperty("user_id", activeUserId);
        json.addProperty("post_id", !cleanId.isEmpty() ? cleanId : postId);
        json.addProperty("reason", reason);
        json.addProperty("details", details);
        json.addProperty("reporter_name", activeUserName);

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
            .url(url)
            .addHeader("User-Agent", "DeenOne-App/1.0")
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
            .post(body)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
          String resStr = response.body() != null ? response.body().string() : "";
          JsonObject resObj = gson.fromJson(resStr, JsonObject.class);
          boolean success = response.isSuccessful() && resObj != null && resObj.has("success") && resObj.get("success").getAsBoolean();
          if (callback != null) {
            mainHandler.post(() -> {
              if (success) {
                callback.onSuccess(resObj.has("message") ? resObj.get("message").getAsString() : "রিপোর্ট গ্রহণ করা হয়েছে");
              } else {
                String err = (resObj != null && resObj.has("error")) ? resObj.get("error").getAsString() : "রিপোর্ট পাঠানো যায়নি";
                callback.onError(err);
              }
            });
          }
        }
      } catch (Exception e) {
        if (callback != null) {
          mainHandler.post(() -> callback.onError("সার্ভার ত্রুটি: " + e.getMessage()));
        }
      }
    });
  }

  public void deletePost(String postId) {
    String activeUser = getActiveUserName();
    synchronized (this) {
      Iterator<CommunityPostItem> iterator = postList.iterator();
      while (iterator.hasNext()) {
        CommunityPostItem item = iterator.next();
        if (item.getId().equals(postId)) {
          prefs.edit().remove("post_ameen_" + postId).apply();
          iterator.remove();
          break;
        }
      }
      savePersistedCommunityPosts();
    }
    recalculateAndNotifyStats();

    // Offline-First Sync Integration
    CommunitySyncManager.getInstance(context).handlePostDeleted(postId, activeUser);
  }

  public void toggleAmeen(CommunityPostItem item) {
    if (item == null) return;
    boolean cur = item.isAmeenGiven();
    boolean next = !cur;
    item.setAmeenGiven(next);
    item.setAmeenCount(Math.max(0, item.getAmeenCount() + (next ? 1 : -1)));
    prefs.edit().putBoolean("post_ameen_" + item.getId(), next).apply();
    savePersistedCommunityPosts();
    recalculateAndNotifyStats();

    // Offline-First Sync Integration
    CommunitySyncManager.getInstance(context).handleLikeToggled(item.getId(), next, getActiveUserName());
  }

  public void addComment(String postId, String commentText) {
    addComment(postId, commentText, null, null);
  }

  public void addComment(String postId, String commentText, String parentCommentId, String replyToAuthorName) {
    String activeUser = getActiveUserName();
    String activeAvatar = getActiveUserAvatar();
    long now = System.currentTimeMillis();
    String commentId = "c_" + now;

    synchronized (this) {
      for (CommunityPostItem item : postList) {
        if (item.getId().equals(postId)) {
          CommunityCommentItem newComment = new CommunityCommentItem(
              commentId, postId, activeUser, commentText, now, parentCommentId, replyToAuthorName, activeAvatar
          );
          item.getComments().add(newComment);
          item.setCommentCount(item.getComments().size());
          break;
        }
      }
      savePersistedCommunityPosts();
    }
    recalculateAndNotifyStats();

    // Offline-First Sync Integration
    CommunitySyncManager.getInstance(context).handleCommentAdded(postId, commentId, commentText, activeUser, parentCommentId, replyToAuthorName);
  }

  public void deleteComment(String postId, String commentId) {
    String activeUser = getActiveUserName();
    synchronized (this) {
      for (CommunityPostItem item : postList) {
        if (item.getId().equals(postId) && item.getComments() != null) {
          Iterator<CommunityCommentItem> iterator = item.getComments().iterator();
          while (iterator.hasNext()) {
            CommunityCommentItem c = iterator.next();
            if (c.getId().equals(commentId)) {
              iterator.remove();
              item.setCommentCount(item.getComments().size());
              break;
            }
          }
          break;
        }
      }
      savePersistedCommunityPosts();
    }
    recalculateAndNotifyStats();

    // Offline-First Sync Integration
    CommunitySyncManager.getInstance(context).handleCommentDeleted(postId, commentId, activeUser);
  }

  public synchronized SocialStats calculateUserSocialStats() {
    if (!AuthManager.isLoggedIn(context)) {
      return new SocialStats(0, 0, 0);
    }
    String activeUser = getActiveUserName();
    if (TextUtils.isEmpty(activeUser)) {
      return new SocialStats(0, 0, 0);
    }
    int postCount = 0;
    int commentCount = 0;
    int likeCount = 0;

    for (CommunityPostItem post : postList) {
      if (isUserAuthor(post.getAuthorName(), activeUser)) {
        postCount++;
      }
      if (post.isAmeenGiven()) {
        likeCount++;
      }
      if (post.getComments() != null) {
        for (CommunityCommentItem comment : post.getComments()) {
          if (isUserAuthor(comment.getAuthorName(), activeUser)) {
            commentCount++;
          }
        }
      }
    }

    return new SocialStats(postCount, commentCount, likeCount);
  }

  private boolean isUserAuthor(String author, String activeUser) {
    if (TextUtils.isEmpty(author) || TextUtils.isEmpty(activeUser)) return false;
    return author.trim().equalsIgnoreCase(activeUser.trim());
  }

  public void recalculateAndNotifyStats() {
    SocialStats stats = calculateUserSocialStats();
    socialStatsLiveData.postValue(stats);
  }
}