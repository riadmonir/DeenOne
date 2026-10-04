package com.devflux.deenone.features.hadith.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.HadithEntity;
import com.devflux.deenone.databinding.ItemHadithCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.List;

public class HadithAdapter extends RecyclerView.Adapter<HadithAdapter.HadithViewHolder> {

  public interface OnBookmarkClickListener {
    void onBookmarkClick(HadithEntity item, int position);
  }

  private List<HadithEntity> items;
  private String currentLanguage = "bn"; // "bn", "en", "ur", "ar"
  private final OnBookmarkClickListener bookmarkListener;

  public HadithAdapter(List<HadithEntity> items, OnBookmarkClickListener bookmarkListener) {
    this.items = items;
    this.bookmarkListener = bookmarkListener;
  }

  public void updateData(List<HadithEntity> newItems) {
    this.items = newItems;
    notifyDataSetChanged();
  }

  public void setLanguage(String langCode) {
    this.currentLanguage = langCode;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public HadithViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemHadithCardBinding binding = ItemHadithCardBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );
    return new HadithViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull HadithViewHolder holder, int position) {
    HadithEntity item = items.get(position);
    holder.bind(item, currentLanguage, bookmarkListener, position);
  }

  @Override
  public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  public static class HadithViewHolder extends RecyclerView.ViewHolder {
    private final ItemHadithCardBinding binding;

    public HadithViewHolder(ItemHadithCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnBookmarkHadith);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCopyHadith);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnShareHadith);
    }

    public void bind(
        HadithEntity item,
        String currentLang,
        OnBookmarkClickListener bookmarkListener,
        int position
    ) {
      Context context = itemView.getContext();
      boolean isBengali = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

      String numStr = isBengali ? BengaliNumberUtil.toBengali(item.getHadithNumber()) : String.valueOf(item.getHadithNumber());
      binding.tvHadithCollectionPill.setText(item.getDisplayCollectionName(isBengali) + ": " + numStr);
      binding.tvHadithChapter.setText((isBengali ? "অধ্যায়: " : "Chapter: ") + item.getDisplayChapterTitle(isBengali));
      binding.tvHadithNarrator.setText((isBengali ? "বর্ণনাকারী: " : "Narrator: ") + item.getDisplayNarrator(isBengali));
      if (item.getArabicText() != null && !item.getArabicText().trim().isEmpty()) {
        binding.tvHadithArabic.setVisibility(View.VISIBLE);
        binding.tvHadithArabic.setText(item.getArabicText());
      } else {
        binding.tvHadithArabic.setVisibility(View.GONE);
      }

      // Pure localized translation based on selected language
      String translationText = item.getDisplayTranslation(isBengali);
      binding.tvHadithTranslation.setText(translationText);

      // Grade & Source Reference
      binding.tvHadithGrade.setText(item.getDisplayGrade(isBengali));
      binding.tvHadithSource.setText(item.getDisplaySourceReference(isBengali));

      binding.btnCopyHadith.setContentDescription(isBengali ? "হাদিস কপি করুন" : "Copy Hadith");
      binding.btnShareHadith.setContentDescription(isBengali ? "হাদিস শেয়ার করুন" : "Share Hadith");
      binding.btnBookmarkHadith.setContentDescription(isBengali ? "বুকমার্ক করুন" : "Bookmark Hadith");

      // Bookmark icon state
      if (item.isBookmarked()) {
        binding.btnBookmarkHadith.setColorFilter(ContextCompat.getColor(context, R.color.accent_mint));
      } else {
        binding.btnBookmarkHadith.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
      }

      binding.btnBookmarkHadith.setOnClickListener(v -> {
        if (bookmarkListener != null) {
          bookmarkListener.onBookmarkClick(item, position);
        }
        Toast.makeText(context, isBengali ? (!item.isBookmarked() ? "হাদিসটি বুকমার্ক করা হয়েছে" : "বুকমার্ক সরানো হয়েছে") : (!item.isBookmarked() ? "Hadith bookmarked" : "Bookmark removed"), Toast.LENGTH_SHORT).show();
      });

      // Copy to clipboard
      final String textToCopy = (item.getArabicText() != null ? item.getArabicText() + "\n\n" : "") + translationText + "\n\n" + item.getDisplaySourceReference(isBengali);
      binding.btnCopyHadith.setOnClickListener(v -> {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Hadith", textToCopy);
        if (clipboard != null) {
          clipboard.setPrimaryClip(clip);
          Toast.makeText(context, isBengali ? "সহীহ হাদিসটি কপি করা হয়েছে" : "Hadith copied to clipboard", Toast.LENGTH_SHORT).show();
        }
      });

      // Share Hadith
      binding.btnShareHadith.setOnClickListener(v -> {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToCopy + (isBengali ? "\n— দ্বীনওয়ান" : "\n— DeenOne"));
        context.startActivity(Intent.createChooser(shareIntent, isBengali ? "হাদিস শেয়ার করুন" : "Share Hadith"));
      });
    }
  }
}
