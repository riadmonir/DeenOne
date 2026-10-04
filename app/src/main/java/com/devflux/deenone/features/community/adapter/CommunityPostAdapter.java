package com.devflux.deenone.features.community.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.features.community.data.CommunityRepository;
import com.devflux.deenone.features.community.model.CommunityPostItem;
import com.devflux.deenone.features.profile.ProfileImageUploadManager;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.PostViewHolder> {

  public interface OnPostActionListener {
    void onAmeenClick(CommunityPostItem post, int position);
    void onCommentsClick(CommunityPostItem post, int position);
    void onShareClick(CommunityPostItem post, int position);
    void onEditClick(CommunityPostItem post, int position);
    void onDeleteClick(CommunityPostItem post, int position);
    void onReportClick(CommunityPostItem post, int position);
    void onPostDeleted(CommunityPostItem post, int position);
  }

  private final List<CommunityPostItem> items = new ArrayList<>();
  private final OnPostActionListener listener;
  private static final LruCache<String, Bitmap> avatarCache = new LruCache<>(50);
  private static final ExecutorService imageExecutor = Executors.newFixedThreadPool(2);
  private static final Handler mainHandler = new Handler(Looper.getMainLooper());

  public CommunityPostAdapter(OnPostActionListener listener) {
    this.listener = listener;
  }

  public void submitList(List<CommunityPostItem> newItems) {
    items.clear();
    if (newItems != null) items.addAll(newItems);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community_post, parent, false);
    return new PostViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
    holder.bind(items.get(position), listener, position);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class PostViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvAuthorInitial, tvAuthorName, tvAuthorMeta, tvCategoryBadge, tvTitle, tvContent, tvAmeenCount, tvCommentsCount, tvViewsCount, tvShareActionText;
    private final ImageView ivVerified, ivAmeenIcon, ivPostAuthorAvatar, ivCommentsIcon, ivShareActionIcon;
    private final LinearLayout btnAmeen, btnComments, btnShareAction;
    private final ImageButton btnPostMoreOptions;

    public PostViewHolder(@NonNull View itemView) {
      super(itemView);
      tvAuthorInitial = itemView.findViewById(R.id.tvPostAuthorInitial);
      ivPostAuthorAvatar = itemView.findViewById(R.id.ivPostAuthorAvatar);
      tvAuthorName = itemView.findViewById(R.id.tvPostAuthorName);
      tvAuthorMeta = itemView.findViewById(R.id.tvPostAuthorMeta);
      tvCategoryBadge = itemView.findViewById(R.id.tvPostCategoryBadge);
      btnPostMoreOptions = itemView.findViewById(R.id.btnPostMoreOptions);
      tvTitle = itemView.findViewById(R.id.tvPostTitle);
      tvContent = itemView.findViewById(R.id.tvPostContent);
      tvAmeenCount = itemView.findViewById(R.id.tvAmeenCount);
      tvCommentsCount = itemView.findViewById(R.id.tvCommentsCount);
      tvViewsCount = itemView.findViewById(R.id.tvPostViewsCount);
      ivVerified = itemView.findViewById(R.id.ivPostVerified);
      ivAmeenIcon = itemView.findViewById(R.id.ivAmeenIcon);
      ivCommentsIcon = itemView.findViewById(R.id.ivCommentsIcon);
      btnAmeen = itemView.findViewById(R.id.btnPostAmeen);
      btnComments = itemView.findViewById(R.id.btnPostComments);
      btnShareAction = itemView.findViewById(R.id.btnPostShareAction);
      tvShareActionText = itemView.findViewById(R.id.tvShareActionText);
      ivShareActionIcon = itemView.findViewById(R.id.ivShareActionIcon);
    }

    public void bind(CommunityPostItem item, OnPostActionListener listener, int position) {
      boolean isBn = LocaleManager.isBengali(itemView.getContext());

      // 1. Author Name & Initial
      String authorName = !TextUtils.isEmpty(item.getAuthorName()) ? item.getAuthorName() : (isBn ? "সহযাত্রী" : "Fellow Muslim");
      tvAuthorName.setText(authorName);
      String initial = authorName.substring(0, 1).toUpperCase();
      tvAuthorInitial.setText(initial);

      // 2. Author Avatar Loading
      bindAvatar(item.getAuthorAvatar(), initial);

      // 3. Time Ago & Category
      tvAuthorMeta.setText(item.getTimeAgo(isBn));
      if (tvCategoryBadge != null) {
        tvCategoryBadge.setText(item.getCategoryBadgeText(isBn));
      }

      // 4. Title, Content & Views
      tvTitle.setText(item.getTitle());
      tvContent.setText(item.getContent());
      tvViewsCount.setText(String.valueOf(item.getViewCount()));
      if (ivVerified != null) {
        ivVerified.setVisibility(item.isVerified() ? View.VISIBLE : View.GONE);
      }

      // 5. Like / Ameen status
      tvAmeenCount.setText(String.valueOf(item.getAmeenCount()));
      tvCommentsCount.setText(String.valueOf(item.getCommentCount()));

      if (item.isAmeenGiven()) {
        ivAmeenIcon.setImageResource(R.drawable.ic_favorite);
        ivAmeenIcon.setColorFilter(Color.parseColor("#34D399"), PorterDuff.Mode.SRC_IN);
        tvAmeenCount.setTextColor(Color.parseColor("#34D399"));
      } else {
        ivAmeenIcon.setImageResource(R.drawable.ic_favorite_border);
        ivAmeenIcon.setColorFilter(Color.parseColor("#94A3B8"), PorterDuff.Mode.SRC_IN);
        tvAmeenCount.setTextColor(Color.parseColor("#94A3B8"));
      }

      if (tvShareActionText != null) {
        tvShareActionText.setText(isBn ? "শেয়ার" : "Share");
      }

      // 6. Strict Rule 7: Touch Animation ONLY on buttons (ZERO on CardView)
      TouchAnimationUtil.attachTouchSpring(btnAmeen);
      TouchAnimationUtil.attachTouchSpring(btnComments);
      if (btnShareAction != null) {
        TouchAnimationUtil.attachTouchSpring(btnShareAction);
      }
      if (btnPostMoreOptions != null) {
        TouchAnimationUtil.attachTouchSpring(btnPostMoreOptions);
      }

      // 7. Click Listeners
      btnAmeen.setOnClickListener(v -> {
        if (listener != null) listener.onAmeenClick(item, position);
      });

      btnComments.setOnClickListener(v -> {
        if (listener != null) listener.onCommentsClick(item, position);
      });

      if (btnShareAction != null) {
        btnShareAction.setOnClickListener(v -> {
          if (listener != null) listener.onShareClick(item, position);
        });
      }

      // 8. Three-Dot Menu Options (Author vs Non-Author)
      if (btnPostMoreOptions != null) {
        btnPostMoreOptions.setOnClickListener(v -> {
          boolean isAuthor = CommunityRepository.getInstance(v.getContext()).isCurrentUserAuthor(item);
          PopupMenu popup = new PopupMenu(v.getContext(), v);

          if (isAuthor) {
            popup.getMenu().add(0, 1, 0, isBn ? "পোস্ট এডিট করুন" : "Edit Post");
            popup.getMenu().add(0, 2, 1, isBn ? "মুছে ফেলুন" : "Delete Post");
          } else {
            popup.getMenu().add(0, 3, 0, isBn ? "পোস্ট রিপোর্ট করুন" : "Report Post");
          }

          popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == 1) {
              if (listener != null) listener.onEditClick(item, position);
            } else if (menuItem.getItemId() == 2) {
              if (listener != null) listener.onDeleteClick(item, position);
            } else if (menuItem.getItemId() == 3) {
              if (listener != null) listener.onReportClick(item, position);
            }
            return true;
          });
          popup.show();
        });
      }
    }

    private void bindAvatar(String avatarUrl, String fallbackInitial) {
      if (ivPostAuthorAvatar == null) return;

      if (TextUtils.isEmpty(avatarUrl) || avatarUrl.equalsIgnoreCase("avatar_1")) {
        ivPostAuthorAvatar.setVisibility(View.GONE);
        tvAuthorInitial.setVisibility(View.VISIBLE);
        tvAuthorInitial.setText(fallbackInitial);
        return;
      }

      String fullUrl = avatarUrl;
      if (!fullUrl.startsWith("http://") && !fullUrl.startsWith("https://")) {
        fullUrl = BackendConfigManager.getPhpBaseUrl(itemView.getContext()) + "/" + avatarUrl;
      }

      Bitmap cached = avatarCache.get(fullUrl);
      if (cached != null && !cached.isRecycled()) {
        ivPostAuthorAvatar.setImageBitmap(cached);
        ivPostAuthorAvatar.setVisibility(View.VISIBLE);
        tvAuthorInitial.setVisibility(View.GONE);
        return;
      }

      // Fetch avatar in background thread
      final String finalUrl = fullUrl;
      ivPostAuthorAvatar.setTag(finalUrl);
      ivPostAuthorAvatar.setVisibility(View.GONE);
      tvAuthorInitial.setVisibility(View.VISIBLE);
      tvAuthorInitial.setText(fallbackInitial);

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
                Bitmap circular = ProfileImageUploadManager.getCircularCroppedBitmap(raw, 120);
                avatarCache.put(finalUrl, circular);
                mainHandler.post(() -> {
                  if (finalUrl.equals(ivPostAuthorAvatar.getTag())) {
                    ivPostAuthorAvatar.setImageBitmap(circular);
                    ivPostAuthorAvatar.setVisibility(View.VISIBLE);
                    tvAuthorInitial.setVisibility(View.GONE);
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
