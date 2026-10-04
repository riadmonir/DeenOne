package com.devflux.deenone.features.dua.adapter;

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
import com.devflux.deenone.data.local.entity.DuaEntity;
import com.devflux.deenone.databinding.ItemDuaCardBinding;

import java.util.List;

public class DuaAdapter extends RecyclerView.Adapter<DuaAdapter.DuaViewHolder> {

  public interface OnFavoriteClickListener {
    void onFavoriteClick(DuaEntity item, int position);
  }

  public interface OnMarkReadClickListener {
    void onMarkReadClick(DuaEntity item, int position);
  }

  private List<DuaEntity> items;
  private String currentLanguage = "bn";
  private final OnFavoriteClickListener favListener;
  private final OnMarkReadClickListener markReadListener;

  public DuaAdapter(List<DuaEntity> items, OnFavoriteClickListener favListener, OnMarkReadClickListener markReadListener) {
    this.items = items;
    this.favListener = favListener;
    this.markReadListener = markReadListener;
  }

  public void updateData(List<DuaEntity> newItems) {
    this.items = newItems;
    notifyDataSetChanged();
  }

  public void setLanguage(String langCode) {
    this.currentLanguage = langCode;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public DuaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemDuaCardBinding binding = ItemDuaCardBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent, false
    );
    return new DuaViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull DuaViewHolder holder, int position) {
    DuaEntity item = items.get(position);
    holder.bind(item, currentLanguage, favListener, markReadListener, position);
  }

  @Override
  public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  public static class DuaViewHolder extends RecyclerView.ViewHolder {
    private final ItemDuaCardBinding binding;

    public DuaViewHolder(ItemDuaCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(
        DuaEntity item,
        String currentLang,
        OnFavoriteClickListener favListener,
        OnMarkReadClickListener markReadListener,
        int position
    ) {
      Context context = itemView.getContext();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

      binding.tvDuaCategoryPill.setText(item.getDisplayCategory(isBn));
      binding.tvDuaTitle.setText(item.getDisplayTitle(isBn));

      if (item.getArabic() != null && !item.getArabic().trim().isEmpty()) {
        binding.tvDuaArabic.setVisibility(View.VISIBLE);
        binding.tvDuaArabic.setText(item.getArabic().trim());
      } else {
        binding.tvDuaArabic.setVisibility(View.GONE);
      }

      if (item.getTransliteration() != null && !item.getTransliteration().trim().isEmpty()) {
        binding.tvDuaTransliteration.setVisibility(View.VISIBLE);
        binding.tvDuaTransliteration.setText((isBn ? "উচ্চারণ: " : "Pronunciation: ") + item.getTransliteration().trim());
      } else {
        binding.tvDuaTransliteration.setVisibility(View.GONE);
      }

      // Language Translation
      String meaning = item.getDisplayMeaning(isBn);
      if (!meaning.isEmpty()) {
        binding.tvDuaMeaning.setVisibility(View.VISIBLE);
        binding.tvDuaMeaning.setText((isBn ? "অর্থ: " : "Meaning: ") + meaning);
      } else {
        binding.tvDuaMeaning.setVisibility(View.GONE);
      }

      // When & Why
      String when = item.getDisplayWhenToRead(isBn);
      if (!when.isEmpty()) {
        binding.tvWhenToRead.setVisibility(View.VISIBLE);
        binding.tvWhenToRead.setText((isBn ? "কখন পড়বেন: " : "When to recite: ") + when);
      } else {
        binding.tvWhenToRead.setVisibility(View.GONE);
      }

      String why = item.getDisplayWhyToRead(isBn);
      if (!why.isEmpty()) {
        binding.tvWhyToRead.setVisibility(View.VISIBLE);
        binding.tvWhyToRead.setText((isBn ? "ফজিলত: " : "Virtue: ") + why);
      } else {
        binding.tvWhyToRead.setVisibility(View.GONE);
      }

      String ref = item.getDisplayReference(isBn);
      if (!ref.isEmpty()) {
        binding.tvDuaReference.setVisibility(View.VISIBLE);
        binding.tvDuaReference.setText((isBn ? "সূত্র: " : "Source: ") + ref);
      } else {
        binding.tvDuaReference.setVisibility(View.GONE);
      }

      // Attach touch animations
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnFavDua);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnMarkDuaRead);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCopyDua);
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnShareDua);

      // Favorite Icon
      if (item.isFavorite()) {
        binding.btnFavDua.setImageResource(R.drawable.ic_favorite_filled);
        binding.btnFavDua.setColorFilter(android.graphics.Color.parseColor("#EF4444"));
      } else {
        binding.btnFavDua.setImageResource(R.drawable.ic_favorite_outline);
        binding.btnFavDua.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
      }

      binding.btnFavDua.setOnClickListener(v -> {
        if (favListener != null) {
          favListener.onFavoriteClick(item, position);
        }
      });

      // Mark as Read Action
      binding.btnMarkDuaRead.setOnClickListener(v -> {
        if (markReadListener != null) {
          markReadListener.onMarkReadClick(item, position);
          binding.tvMarkDuaReadLabel.setText(isBn ? "সম্পন্ন" : "Done");
          binding.btnMarkDuaRead.setBackgroundResource(R.drawable.bg_badge_pill);
        }
      });

      // Copy
      final String textToCopy = item.getDisplayTitle(isBn) + "\n\n" + item.getArabic() + "\n\n" +
          (isBn ? "উচ্চারণ: " : "Pronunciation: ") + item.getTransliteration() + "\n\n" +
          (isBn ? "অর্থ: " : "Meaning: ") + meaning + "\n\n" +
          (isBn ? "সূত্র: " : "Source: ") + ref;

      binding.btnCopyDua.setOnClickListener(v -> {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Dua", textToCopy);
        if (clipboard != null) {
          clipboard.setPrimaryClip(clip);
          Toast.makeText(context, isBn ? "দোয়াটি কপি করা হয়েছে" : "Dua copied to clipboard", Toast.LENGTH_SHORT).show();
        }
      });

      // Share
      binding.btnShareDua.setOnClickListener(v -> {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToCopy + (isBn ? "\n— দ্বীনওয়ান" : "\n— DeenOne"));
        context.startActivity(Intent.createChooser(shareIntent, isBn ? "দোয়া শেয়ার করুন" : "Share Dua"));
      });
    }

    public static String getBengaliCategoryName(String category) {
      if (category == null || category.trim().isEmpty()) return "মাসনুন দোয়া";
      switch (category.trim()) {
        case "Morning Dua": return "সকালের দোয়া";
        case "Evening Dua": return "সন্ধ্যার দোয়া";
        case "Before Sleeping": return "ঘুমানোর দোয়া";
        case "Waking Up": return "ঘুম থেকে ওঠার দোয়া";
        case "Before Eating": case "Eating": return "খাওয়ার দোয়া";
        case "After Eating": return "খাওয়ার পরের দোয়া";
        case "Leaving Home": return "ঘর থেকে বের হওয়ার দোয়া";
        case "Entering Home": return "ঘরে প্রবেশের দোয়া";
        case "Entering Mosque": case "Mosque": return "মসজিদে প্রবেশের দোয়া";
        case "Leaving Mosque": return "মসজিদ থেকে বের হওয়ার দোয়া";
        case "Salah-related Duas": case "Salah": return "সালাতের দোয়া";
        case "Forgiveness": return "ক্ষমা ও তওবা";
        case "Protection": return "সুরক্ষা ও হেফাজত";
        case "Anxiety/Worry": return "দুশ্চিন্তা ও পেরেশানি";
        case "Parents": return "পিতামাতা";
        case "Rizq": return "রিজিক ও বরকত";
        case "Health": return "রোগমুক্তি ও সুস্থতা";
        case "Travel": return "সফর ও ভ্রমণ";
        case "Ramadan": return "রমজান ও রোজা";
        case "Hajj": return "হজ";
        case "Umrah": return "উমরাহ";
        case "Daily Life": return "দৈনন্দিন জীবন";
        case "Rabbana": return "রব্বানা দোয়া";
        case "Favorites": return "প্রিয় দোয়া";
        default: return category;
      }
    }
  }
}
