package com.devflux.deenone.features.community.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.community.data.CommunityRepository;
import com.devflux.deenone.features.community.model.CommunityCommentItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.features.profile.ProfileImageUploadManager;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommunityCommentAdapter extends RecyclerView.Adapter<CommunityCommentAdapter.CommentViewHolder> {

  private static final LruCache<String, Bitmap> avatarCache = new LruCache<>(50);
  private static final ExecutorService imageExecutor = Executors.newFixedThreadPool(4);
  private static final Handler mainHandler = new Handler(Looper.getMainLooper());

  public interface OnCommentDeletedListener {
    void onCommentDeleted();
  }

  public interface OnCommentReplyListener {
    void onReplyRequested(CommunityCommentItem comment);
  }

  private final List<CommunityCommentItem> items = new ArrayList<>();
  private OnCommentDeletedListener deleteListener;
  private OnCommentReplyListener replyListener;

  public void setOnCommentDeletedListener(OnCommentDeletedListener listener) {
    this.deleteListener = listener;
  }

  public void setOnCommentReplyListener(OnCommentReplyListener listener) {
    this.replyListener = listener;
  }

  public void submitList(List<CommunityCommentItem> newItems) {
    items.clear();
    if (newItems != null) {
      items.addAll(buildThreadedComments(newItems));
    }
    notifyDataSetChanged();
  }

  public static List<CommunityCommentItem> buildThreadedComments(List<CommunityCommentItem> rawList) {
    if (rawList == null || rawList.isEmpty()) {
      return new ArrayList<>();
    }

    List<CommunityCommentItem> result = new ArrayList<>();
    List<CommunityCommentItem> topLevelComments = new ArrayList<>();
    java.util.Map<String, List<CommunityCommentItem>> repliesMap = new java.util.LinkedHashMap<>();
    java.util.Set<String> allIds = new java.util.HashSet<>();

    for (CommunityCommentItem item : rawList) {
      if (item.getId() != null && !item.getId().isEmpty()) {
        allIds.add(item.getId());
      }
      if (item.isReply()) {
        String parentId = item.getParentCommentId();
        if (!repliesMap.containsKey(parentId)) {
          repliesMap.put(parentId, new ArrayList<>());
        }
        repliesMap.get(parentId).add(item);
      } else {
        topLevelComments.add(item);
      }
    }

    // Attach replies under their corresponding top-level comment
    for (CommunityCommentItem root : topLevelComments) {
      result.add(root);
      attachRepliesRecursively(root.getId(), repliesMap, result);
    }

    // Attach any remaining orphaned replies whose parent is missing
    for (java.util.Map.Entry<String, List<CommunityCommentItem>> entry : repliesMap.entrySet()) {
      if (!allIds.contains(entry.getKey())) {
        for (CommunityCommentItem orphan : entry.getValue()) {
          if (!result.contains(orphan)) {
            result.add(orphan);
            attachRepliesRecursively(orphan.getId(), repliesMap, result);
          }
        }
      }
    }

    return result;
  }

  private static void attachRepliesRecursively(String parentId, java.util.Map<String, List<CommunityCommentItem>> repliesMap, List<CommunityCommentItem> result) {
    List<CommunityCommentItem> children = repliesMap.get(parentId);
    if (children != null) {
      for (CommunityCommentItem child : children) {
        result.add(child);
        attachRepliesRecursively(child.getId(), repliesMap, result);
      }
    }
  }

  @NonNull
  @Override
  public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community_comment, parent, false);
    return new CommentViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
    holder.bind(items.get(position), deleteListener, replyListener);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class CommentViewHolder extends RecyclerView.ViewHolder {
    private final LinearLayout layoutCommentRoot;
    private final TextView tvAuthorName, tvTimeAgo, tvReplyToBadge, tvCommentContent, tvBtnReplyComment, tvAuthorInitial;
    private final ImageView ivAuthorAvatar;

    public CommentViewHolder(@NonNull View itemView) {
      super(itemView);
      layoutCommentRoot = itemView.findViewById(R.id.layoutCommentRoot);
      tvAuthorName = itemView.findViewById(R.id.tvCommentAuthorName);
      tvTimeAgo = itemView.findViewById(R.id.tvCommentTimeAgo);
      tvReplyToBadge = itemView.findViewById(R.id.tvReplyToBadge);
      tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
      tvBtnReplyComment = itemView.findViewById(R.id.tvBtnReplyComment);
      ivAuthorAvatar = itemView.findViewById(R.id.ivCommentAuthorAvatar);
      tvAuthorInitial = itemView.findViewById(R.id.tvCommentAuthorInitial);
    }

    public void bind(CommunityCommentItem item, OnCommentDeletedListener deleteListener, OnCommentReplyListener replyListener) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(itemView.getContext());
      tvAuthorName.setText(item.getAuthorName());
      tvTimeAgo.setText(item.getTimeAgo(isBn));
      tvCommentContent.setText(item.getContent());

      String fallbackInitial = !TextUtils.isEmpty(item.getAuthorName())
          ? item.getAuthorName().substring(0, 1).toUpperCase()
          : "ম";

      bindAvatar(item.getAuthorAvatar(), fallbackInitial);

      // Check if this comment is a reply
      if (item.isReply()) {
        String replyTarget = item.getReplyToAuthorName();
        if (replyTarget.isEmpty()) replyTarget = isBn ? "ব্যবহারকারী" : "User";
        tvReplyToBadge.setVisibility(View.VISIBLE);
        tvReplyToBadge.setText("↳ @" + replyTarget);

        // Indent reply slightly
        int indentPx = (int) (24 * itemView.getContext().getResources().getDisplayMetrics().density);
        layoutCommentRoot.setPadding(indentPx, 0, 0, 0);
      } else {
        tvReplyToBadge.setVisibility(View.GONE);
        layoutCommentRoot.setPadding(0, 0, 0, 0);
      }

      if (tvBtnReplyComment != null) {
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(tvBtnReplyComment);
        tvBtnReplyComment.setText(isBn ? "উত্তর দিন" : "Reply");
        tvBtnReplyComment.setOnClickListener(v -> {
          if (replyListener != null) {
            replyListener.onReplyRequested(item);
          }
        });
      }

      // Long Click to Delete
      itemView.setOnLongClickListener(v -> {
        new AlertDialog.Builder(v.getContext())
            .setTitle(isBn ? "মন্তব্য মুছে ফেলবেন?" : "Delete Comment?")
            .setMessage(isBn ? "আপনি কি এই মন্তব্যটি মুছে ফেলতে চান?" : "Are you sure you want to delete this comment?")
            .setPositiveButton(isBn ? "মুছুন" : "Delete", (d, w) -> {
              CommunityRepository.getInstance(v.getContext()).deleteComment(item.getPostId(), item.getId());
              Toast.makeText(v.getContext(), isBn ? "মন্তব্য মুছে ফেলা হয়েছে" : "Comment deleted successfully", Toast.LENGTH_SHORT).show();
              if (deleteListener != null) {
                deleteListener.onCommentDeleted();
              }
            })
            .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
            .show();
        return true;
      });
    }

    private void bindAvatar(String avatarUrl, String fallbackInitial) {
      if (ivAuthorAvatar == null) return;

      if (TextUtils.isEmpty(avatarUrl) || avatarUrl.equalsIgnoreCase("avatar_1")) {
        ivAuthorAvatar.setVisibility(View.GONE);
        if (tvAuthorInitial != null) {
          tvAuthorInitial.setVisibility(View.VISIBLE);
          tvAuthorInitial.setText(fallbackInitial);
        }
        return;
      }

      String fullUrl = avatarUrl;
      if (!fullUrl.startsWith("http://") && !fullUrl.startsWith("https://")) {
        fullUrl = BackendConfigManager.getPhpBaseUrl(itemView.getContext()) + "/" + avatarUrl;
      }

      Bitmap cached = avatarCache.get(fullUrl);
      if (cached != null && !cached.isRecycled()) {
        ivAuthorAvatar.setImageTintList(null);
        ivAuthorAvatar.setImageBitmap(cached);
        ivAuthorAvatar.setVisibility(View.VISIBLE);
        if (tvAuthorInitial != null) tvAuthorInitial.setVisibility(View.GONE);
        return;
      }

      final String finalUrl = fullUrl;
      ivAuthorAvatar.setTag(finalUrl);
      ivAuthorAvatar.setVisibility(View.GONE);
      if (tvAuthorInitial != null) {
        tvAuthorInitial.setVisibility(View.VISIBLE);
        tvAuthorInitial.setText(fallbackInitial);
      }

      imageExecutor.execute(() -> {
        try {
          URL url = new URL(finalUrl);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 10; Mobile)");
          conn.setInstanceFollowRedirects(true);
          conn.setConnectTimeout(8000);
          conn.setReadTimeout(8000);
          conn.setDoInput(true);
          conn.connect();
          if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream is = conn.getInputStream()) {
              Bitmap raw = BitmapFactory.decodeStream(is);
              if (raw != null) {
                Bitmap circular = ProfileImageUploadManager.getCircularCroppedBitmap(raw, 100);
                avatarCache.put(finalUrl, circular);
                mainHandler.post(() -> {
                  if (finalUrl.equals(ivAuthorAvatar.getTag())) {
                    ivAuthorAvatar.setImageTintList(null);
                    ivAuthorAvatar.setImageBitmap(circular);
                    ivAuthorAvatar.setVisibility(View.VISIBLE);
                    if (tvAuthorInitial != null) tvAuthorInitial.setVisibility(View.GONE);
                  }
                });
              }
            }
          }
        } catch (Exception ignored) {}
      });
    }
  }
}
