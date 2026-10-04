package com.devflux.deenone.features.community.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunitySyncManager {

  private static final String TAG = "CommunitySyncManager";
  private static final String PREF_SYNC = "community_offline_sync_queue";
  private static final String KEY_PENDING_ACTIONS = "pending_actions_json";
  private static final String KEY_PROCESSED_OPS = "processed_operation_ids";
  private static final int MAX_RETRIES = 5;

  public static final String ACTION_CREATE_POST = "CREATE_POST";
  public static final String ACTION_DELETE_POST = "DELETE_POST";
  public static final String ACTION_ADD_COMMENT = "ADD_COMMENT";
  public static final String ACTION_DELETE_COMMENT = "DELETE_COMMENT";
  public static final String ACTION_TOGGLE_LIKE = "TOGGLE_LIKE";

  public static class PendingAction {
    public String operationId;
    public String actionType;
    public String targetId;
    public String secondaryId;
    public String payload;
    public String authorName;
    public long timestamp;
    public int retryCount;

    public PendingAction() {}

    public PendingAction(String actionType, String targetId, String secondaryId, String payload, String authorName, long timestamp) {
      this.operationId = UUID.randomUUID().toString();
      this.actionType = actionType;
      this.targetId = targetId;
      this.secondaryId = secondaryId;
      this.payload = payload;
      this.authorName = authorName;
      this.timestamp = timestamp;
      this.retryCount = 0;
    }
  }

  private static CommunitySyncManager instance;
  private final Context context;
  private final SharedPreferences prefs;
  private final Gson gson = new Gson();
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final OkHttpClient httpClient;
  private boolean isSyncing = false;

  private CommunitySyncManager(Context context) {
    this.context = context.getApplicationContext();
    this.prefs = this.context.getSharedPreferences(PREF_SYNC, Context.MODE_PRIVATE);
    this.httpClient = new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build();
    observeNetworkState();
  }

  public static synchronized CommunitySyncManager getInstance(Context context) {
    if (instance == null) {
      instance = new CommunitySyncManager(context);
    }
    return instance;
  }

  private void observeNetworkState() {
    NetworkConnectivityHelper.getNetworkStatusLiveData(context).observeForever(isOnline -> {
      if (isOnline != null && isOnline) {
        triggerSyncPendingActions();
        // Also refresh latest community posts
        CommunityRepository.getInstance(context).syncFromRemote(true, null);
      }
    });
  }

  public synchronized void enqueueAction(PendingAction action) {
    if (action == null) return;
    List<PendingAction> list = getPendingActions();
    list.add(action);
    savePendingActions(list);

    // Enqueue WorkManager background task for OS-level guarantee
    CommunitySyncWorker.enqueueSyncWork(context);
  }

  public synchronized List<PendingAction> getPendingActions() {
    String json = prefs.getString(KEY_PENDING_ACTIONS, null);
    if (json == null || json.isEmpty()) {
      return new ArrayList<>();
    }
    try {
      Type type = new TypeToken<ArrayList<PendingAction>>() {}.getType();
      List<PendingAction> list = gson.fromJson(json, type);
      return list != null ? list : new ArrayList<>();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private synchronized void savePendingActions(List<PendingAction> list) {
    String json = gson.toJson(list);
    prefs.edit().putString(KEY_PENDING_ACTIONS, json).apply();
  }

  private synchronized Set<String> getProcessedOperationIds() {
    return new HashSet<>(prefs.getStringSet(KEY_PROCESSED_OPS, Collections.emptySet()));
  }

  private synchronized void markOperationProcessed(String operationId) {
    Set<String> set = getProcessedOperationIds();
    set.add(operationId);
    prefs.edit().putStringSet(KEY_PROCESSED_OPS, set).apply();
  }

  public int getPendingActionsCount() {
    return getPendingActions().size();
  }

  public synchronized void triggerSyncPendingActions() {
    if (isSyncing || !NetworkConnectivityHelper.isOnline(context)) return;

    executor.execute(this::syncPendingActionsSynchronously);
  }

  public String getActiveUserId() {
    if (!AuthManager.isLoggedIn(context)) {
      SharedPreferences dPrefs = context.getSharedPreferences("device_identity_prefs", Context.MODE_PRIVATE);
      String devId = dPrefs.getString("device_client_id", null);
      if (devId == null) {
        devId = "dev_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        dPrefs.edit().putString("device_client_id", devId).apply();
      }
      return devId;
    }
    AuthManager.UserSession session = AuthManager.getCurrentSession(context);
    if (session != null && !TextUtils.isEmpty(session.userId)) {
      return session.userId;
    }
    SharedPreferences pPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
    return pPrefs.getString("profile_user_id", pPrefs.getString("user_uid", "usr_main"));
  }

  private String getActiveUserAvatar() {
    SharedPreferences prefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
    return prefs.getString("profile_avatar", "avatar_1");
  }

  /**
   * Executes synchronous sync of pending actions with full conflict resolution,
   * duplicate prevention, and partial failure handling.
   */
  public synchronized boolean syncPendingActionsSynchronously() {
    if (!NetworkConnectivityHelper.isOnline(context)) return false;

    List<PendingAction> pendingList = getPendingActions();
    if (pendingList.isEmpty()) return true;

    isSyncing = true;
    boolean allSuccessful = true;
    Set<String> processedIds = getProcessedOperationIds();
    List<PendingAction> remainingList = new ArrayList<>();

    for (PendingAction action : pendingList) {
      if (processedIds.contains(action.operationId)) {
        continue;
      }

      boolean success = false;
      try {
        if (ACTION_CREATE_POST.equals(action.actionType)) {
          success = sendCreatePostToApi(action);
        } else if (ACTION_ADD_COMMENT.equals(action.actionType)) {
          success = sendAddCommentToApi(action);
        } else if (ACTION_TOGGLE_LIKE.equals(action.actionType)) {
          success = sendToggleLikeToApi(action);
        } else if (ACTION_DELETE_POST.equals(action.actionType)) {
          success = sendDeletePostToApi(action);
        } else if (ACTION_DELETE_COMMENT.equals(action.actionType)) {
          success = sendDeleteCommentToApi(action);
        }

        if (success) {
          markOperationProcessed(action.operationId);
        }
      } catch (Exception e) {
        Log.e(TAG, "Failed to sync action: " + action.actionType, e);
        success = false;
      }

      if (!success) {
        allSuccessful = false;
        action.retryCount++;
        if (action.retryCount < MAX_RETRIES) {
          remainingList.add(action);
        }
      }
    }

    savePendingActions(remainingList);
    isSyncing = false;

    // Refresh latest feed after syncing pending operations
    CommunityRepository.getInstance(context).syncFromRemote(true, null);

    return allSuccessful;
  }

  private boolean sendCreatePostToApi(PendingAction action) {
    try {
      String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
      String activeUserId = getActiveUserId();
      JsonObject json = new JsonObject();
      json.addProperty("action", "create_post");
      json.addProperty("user_id", activeUserId);
      json.addProperty("author_name", action.authorName);
      json.addProperty("category", action.secondaryId != null ? action.secondaryId : "dua_request");

      String payload = action.payload != null ? action.payload : "";
      if (payload.contains("\n")) {
        String[] parts = payload.split("\n", 2);
        json.addProperty("title", parts[0]);
        json.addProperty("content", parts[1]);
      } else {
        json.addProperty("title", payload);
        json.addProperty("content", payload);
      }

      RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
      Request request = new Request.Builder()
          .url(url)
          .addHeader("User-Agent", "DeenOne-App/1.0")
          .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
          .post(body)
          .build();

      try (Response response = httpClient.newCall(request).execute()) {
        if (response.isSuccessful()) {
          return true;
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "sendCreatePostToApi error: " + e.getMessage());
    }
    return false;
  }

  private boolean sendAddCommentToApi(PendingAction action) {
    try {
      String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
      String activeUserId = getActiveUserId();
      JsonObject json = new JsonObject();
      json.addProperty("action", "add_comment");
      json.addProperty("user_id", activeUserId);
      json.addProperty("author_name", action.authorName);
      json.addProperty("author_avatar", getActiveUserAvatar());
      json.addProperty("post_id", action.targetId);
      json.addProperty("comment_text", action.payload);

      if (action.secondaryId != null && action.secondaryId.contains("::")) {
        String[] replyParts = action.secondaryId.split("::", 2);
        json.addProperty("parent_comment_id", replyParts[0]);
        if (replyParts.length > 1 && !replyParts[1].isEmpty()) {
          json.addProperty("reply_to_author", replyParts[1]);
        }
      }

      RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
      Request request = new Request.Builder()
          .url(url)
          .addHeader("User-Agent", "DeenOne-App/1.0")
          .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
          .post(body)
          .build();

      try (Response response = httpClient.newCall(request).execute()) {
        return response.isSuccessful();
      }
    } catch (Exception e) {
      Log.e(TAG, "sendAddCommentToApi error: " + e.getMessage());
    }
    return false;
  }

  private boolean sendToggleLikeToApi(PendingAction action) {
    try {
      String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
      String activeUserId = getActiveUserId();
      JsonObject json = new JsonObject();
      json.addProperty("action", "toggle_like");
      json.addProperty("user_id", activeUserId);
      json.addProperty("author_name", action.authorName);
      json.addProperty("post_id", action.targetId);
      json.addProperty("liked", Boolean.parseBoolean(action.payload));

      RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
      Request request = new Request.Builder()
          .url(url)
          .addHeader("User-Agent", "DeenOne-App/1.0")
          .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
          .post(body)
          .build();

      try (Response response = httpClient.newCall(request).execute()) {
        return response.isSuccessful();
      }
    } catch (Exception e) {
      Log.e(TAG, "sendToggleLikeToApi error: " + e.getMessage());
    }
    return false;
  }

  private boolean sendDeletePostToApi(PendingAction action) {
    try {
      String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
      String activeUserId = getActiveUserId();
      JsonObject json = new JsonObject();
      json.addProperty("action", "delete_post");
      json.addProperty("user_id", activeUserId);
      json.addProperty("post_id", action.targetId);

      RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
      Request request = new Request.Builder()
          .url(url)
          .addHeader("User-Agent", "DeenOne-App/1.0")
          .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
          .post(body)
          .build();

      try (Response response = httpClient.newCall(request).execute()) {
        return response.isSuccessful();
      }
    } catch (Exception e) {
      Log.e(TAG, "sendDeletePostToApi error: " + e.getMessage());
    }
    return false;
  }

  private boolean sendDeleteCommentToApi(PendingAction action) {
    try {
      String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
      String activeUserId = getActiveUserId();
      JsonObject json = new JsonObject();
      json.addProperty("action", "delete_comment");
      json.addProperty("user_id", activeUserId);
      json.addProperty("comment_id", action.targetId);
      json.addProperty("post_id", action.secondaryId);

      RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
      Request request = new Request.Builder()
          .url(url)
          .addHeader("User-Agent", "DeenOne-App/1.0")
          .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
          .post(body)
          .build();

      try (Response response = httpClient.newCall(request).execute()) {
        return response.isSuccessful();
      }
    } catch (Exception e) {
      Log.e(TAG, "sendDeleteCommentToApi error: " + e.getMessage());
    }
    return false;
  }

  public void handlePostCreated(String postId, String title, String content, String category, String authorName) {
    PendingAction action = new PendingAction(
        ACTION_CREATE_POST, postId, category, title + "\n" + content, authorName, System.currentTimeMillis()
    );

    if (NetworkConnectivityHelper.isOnline(context)) {
      executor.execute(() -> {
        boolean ok = sendCreatePostToApi(action);
        if (ok) {
          markOperationProcessed(action.operationId);
          CommunityRepository.getInstance(context).syncFromRemote(true, null);
        } else {
          enqueueAction(action);
        }
      });
    } else {
      enqueueAction(action);
    }
  }

  public void handlePostDeleted(String postId, String authorName) {
    PendingAction action = new PendingAction(
        ACTION_DELETE_POST, postId, null, null, authorName, System.currentTimeMillis()
    );

    if (NetworkConnectivityHelper.isOnline(context)) {
      executor.execute(() -> {
        boolean ok = sendDeletePostToApi(action);
        if (ok) {
          markOperationProcessed(action.operationId);
        } else {
          enqueueAction(action);
        }
      });
    } else {
      enqueueAction(action);
    }
  }

  public void handleCommentAdded(String postId, String commentId, String commentText, String authorName, String parentCommentId, String replyToAuthor) {
    PendingAction action = new PendingAction(
        ACTION_ADD_COMMENT, postId, commentId, commentText, authorName, System.currentTimeMillis()
    );
    if (!TextUtils.isEmpty(parentCommentId)) {
      action.secondaryId = parentCommentId + "::" + (replyToAuthor != null ? replyToAuthor : "");
    }

    if (NetworkConnectivityHelper.isOnline(context)) {
      executor.execute(() -> {
        boolean ok = sendAddCommentToApi(action);
        if (ok) {
          markOperationProcessed(action.operationId);
          CommunityRepository.getInstance(context).fetchCommentsForPost(postId, (Runnable) null);
        } else {
          enqueueAction(action);
        }
      });
    } else {
      enqueueAction(action);
    }
  }

  public void handleCommentDeleted(String postId, String commentId, String authorName) {
    PendingAction action = new PendingAction(
        ACTION_DELETE_COMMENT, commentId, postId, null, authorName, System.currentTimeMillis()
    );

    if (NetworkConnectivityHelper.isOnline(context)) {
      executor.execute(() -> {
        boolean ok = sendDeleteCommentToApi(action);
        if (ok) {
          markOperationProcessed(action.operationId);
        } else {
          enqueueAction(action);
        }
      });
    } else {
      enqueueAction(action);
    }
  }

  public void handleLikeToggled(String postId, boolean isLiked, String authorName) {
    PendingAction action = new PendingAction(
        ACTION_TOGGLE_LIKE, postId, null, String.valueOf(isLiked), authorName, System.currentTimeMillis()
    );

    if (NetworkConnectivityHelper.isOnline(context)) {
      executor.execute(() -> {
        boolean ok = sendToggleLikeToApi(action);
        if (ok) {
          markOperationProcessed(action.operationId);
        } else {
          enqueueAction(action);
        }
      });
    } else {
      enqueueAction(action);
    }
  }
}
