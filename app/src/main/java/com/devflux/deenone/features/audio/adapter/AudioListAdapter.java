package com.devflux.deenone.features.audio.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.audio.model.IslamicAudioItem;

import java.util.ArrayList;
import java.util.List;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

  public interface OnAudioActionListener {
    void onPlay(IslamicAudioItem item);
    void onFavoriteToggle(IslamicAudioItem item);
    void onDownloadClick(IslamicAudioItem item);
    boolean isFavorite(String id);
    boolean isDownloaded(String id);
    boolean isDownloading(String id);
  }

  private final List<IslamicAudioItem> items = new ArrayList<>();
  private final OnAudioActionListener listener;

  public AudioListAdapter(OnAudioActionListener listener) {
    this.listener = listener;
  }

  public void submitList(List<IslamicAudioItem> newItems) {
    items.clear();
    if (newItems != null) items.addAll(newItems);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_track, parent, false);
    return new AudioViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
    holder.bind(items.get(position), listener);
  }

  @Override
  public int getItemCount() { return items.size(); }

  static class AudioViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvTitle, tvSpeaker, tvDuration, tvCategory;
    private final ImageButton btnFavorite, btnDownload;
    private final ProgressBar pbDownload;
    private final ImageView ivDownloadedCheck;

    AudioViewHolder(View itemView) {
      super(itemView);
      tvTitle = itemView.findViewById(R.id.tvAudioTitle);
      tvSpeaker = itemView.findViewById(R.id.tvAudioSpeaker);
      tvDuration = itemView.findViewById(R.id.tvAudioDuration);
      tvCategory = itemView.findViewById(R.id.tvAudioCategory);
      btnFavorite = itemView.findViewById(R.id.btnAudioFavorite);
      btnDownload = itemView.findViewById(R.id.btnAudioDownload);
      pbDownload = itemView.findViewById(R.id.pbAudioDownloadProgress);
      ivDownloadedCheck = itemView.findViewById(R.id.ivDownloadedCheck);

      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(btnFavorite);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(btnDownload);
    }

    void bind(IslamicAudioItem item, OnAudioActionListener listener) {
      Context context = itemView.getContext();
      boolean isBengali = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
      tvTitle.setText(item.getTitle().isEmpty() ? (isBengali ? "ইসলামিক অডিও" : "Islamic Audio") : item.getTitle());
      tvSpeaker.setText(item.getSpeaker().isEmpty() ? (isBengali ? "বিশ্ববিখ্যাত স্কলার" : "Islamic Scholar") : item.getSpeaker());
      tvDuration.setText(item.getFormattedDuration());

      String cat = item.getCategory();
      if (isBengali) {
        if ("bangla_waz".equalsIgnoreCase(cat)) tvCategory.setText("ওয়াজ ও বয়ান");
        else if ("dhikr".equalsIgnoreCase(cat)) tvCategory.setText("জিকির ও দোয়া");
        else if ("hadith".equalsIgnoreCase(cat)) tvCategory.setText("হাদিস আলোচনা");
        else if ("seerah".equalsIgnoreCase(cat)) tvCategory.setText("সীরাতুন্নবী ﷺ");
        else if ("khutbah".equalsIgnoreCase(cat)) tvCategory.setText("জুমার খুতবা");
        else if ("fiqh".equalsIgnoreCase(cat)) tvCategory.setText("ইসলামিক ফিকহ");
        else if ("nasheed".equalsIgnoreCase(cat)) tvCategory.setText("ইসলামিক নাশিদ");
        else tvCategory.setText(item.getCategoryLabel() != null && !item.getCategoryLabel().isEmpty() ? item.getCategoryLabel() : "ইসলামিক অডিও");
      } else {
        if ("bangla_waz".equalsIgnoreCase(cat)) tvCategory.setText("Waz & Lectures");
        else if ("dhikr".equalsIgnoreCase(cat)) tvCategory.setText("Dhikr & Dua");
        else if ("hadith".equalsIgnoreCase(cat)) tvCategory.setText("Hadith Discussion");
        else if ("seerah".equalsIgnoreCase(cat)) tvCategory.setText("Prophetic Seerah");
        else if ("khutbah".equalsIgnoreCase(cat)) tvCategory.setText("Friday Khutbah");
        else if ("fiqh".equalsIgnoreCase(cat)) tvCategory.setText("Islamic Fiqh");
        else if ("nasheed".equalsIgnoreCase(cat)) tvCategory.setText("Islamic Nasheed");
        else tvCategory.setText("Islamic Audio");
      }

      // Favorite status
      boolean fav = listener.isFavorite(item.getId());
      btnFavorite.setImageResource(fav ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
      if (fav) {
        btnFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(0xFFEF4444));
      } else {
        int secColor = androidx.core.content.ContextCompat.getColor(context, R.color.text_secondary);
        btnFavorite.setImageTintList(android.content.res.ColorStateList.valueOf(secColor));
      }
      btnFavorite.setContentDescription(fav 
          ? (isBengali ? "পছন্দের তালিকা থেকে সরান" : "Remove from favorites") 
          : (isBengali ? "পছন্দের তালিকায় যোগ করুন" : "Add to favorites"));
      btnFavorite.setOnClickListener(v -> listener.onFavoriteToggle(item));

      // Download & Offline Status
      boolean downloaded = listener.isDownloaded(item.getId()) || item.isDownloaded();
      boolean downloading = listener.isDownloading(item.getId()) || item.isDownloading();

      if (downloaded) {
        btnDownload.setVisibility(View.GONE);
        pbDownload.setVisibility(View.GONE);
        ivDownloadedCheck.setVisibility(View.VISIBLE);
      } else if (downloading) {
        btnDownload.setVisibility(View.GONE);
        pbDownload.setVisibility(View.VISIBLE);
        ivDownloadedCheck.setVisibility(View.GONE);
      } else {
        btnDownload.setVisibility(View.VISIBLE);
        pbDownload.setVisibility(View.GONE);
        ivDownloadedCheck.setVisibility(View.GONE);
        btnDownload.setContentDescription(isBengali ? "ডাউনলোড করুন" : "Download");
      }

      btnDownload.setOnClickListener(v -> listener.onDownloadClick(item));

      // Play track on card click
      itemView.setOnClickListener(v -> listener.onPlay(item));
    }
  }
}