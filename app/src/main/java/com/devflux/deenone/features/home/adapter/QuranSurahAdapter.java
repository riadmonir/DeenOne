package com.devflux.deenone.features.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.databinding.ItemQuranSurahBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class QuranSurahAdapter extends RecyclerView.Adapter<QuranSurahAdapter.SurahViewHolder> {

    public interface OnSurahClickListener {
        void onSurahClick(QuranSurahEntity surah);
    }

    public interface OnSurahBookmarkListener {
        void onBookmarkClick(QuranSurahEntity surah, int position);
    }

    private List<QuranSurahEntity> surahList = new ArrayList<>();
    private final OnSurahClickListener listener;
    private OnSurahBookmarkListener bookmarkListener;

    public QuranSurahAdapter(OnSurahClickListener listener) {
        this(listener, null);
    }

    public QuranSurahAdapter(OnSurahClickListener listener, OnSurahBookmarkListener bookmarkListener) {
        this.listener = listener;
        this.bookmarkListener = bookmarkListener;
    }

    public void setOnSurahBookmarkListener(OnSurahBookmarkListener bookmarkListener) {
        this.bookmarkListener = bookmarkListener;
    }

    public void setSurahs(List<QuranSurahEntity> list) {
        this.surahList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuranSurahBinding binding = ItemQuranSurahBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new SurahViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SurahViewHolder holder, int position) {
        holder.bind(surahList.get(position));
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    class SurahViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuranSurahBinding binding;

        public SurahViewHolder(@NonNull ItemQuranSurahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            TouchAnimationUtil.attachTouchSpring(binding.btnBookmarkSurah);
        }

        public void bind(QuranSurahEntity surah) {
            Context context = binding.getRoot().getContext();
            boolean isBengali = LocaleManager.isBengali(context);

            binding.tvSurahNumber.setText(isBengali ? BengaliNumberUtil.toBengali(surah.getNumber()) : String.valueOf(surah.getNumber()));
            binding.tvSurahNameBengali.setText(isBengali ? surah.getNameBengali() : surah.getNameEnglish());
            
            // Surah Short Meaning (e.g. সূচনা, গাভী, ইমরানের পরিবার...)
            String meaning = isBengali ? surah.getMeaningBengali() : surah.getMeaningEnglish();
            if (meaning != null && !meaning.trim().isEmpty()) {
                binding.tvSurahMeaning.setVisibility(View.VISIBLE);
                binding.tvDot1.setVisibility(View.VISIBLE);
                binding.tvSurahMeaning.setText(meaning.trim());
            } else {
                binding.tvSurahMeaning.setVisibility(View.GONE);
                binding.tvDot1.setVisibility(View.GONE);
            }

            // Revelation Badge (Makki / Madani)
            String revType = surah.getRevelationType();
            boolean isMakki = revType == null || "মাক্কী".equals(revType) || "Meccan".equalsIgnoreCase(revType);
            if (isMakki) {
                binding.tvSurahRevelationBadge.setText(isBengali ? "মাক্কী" : "Makki");
                binding.tvSurahRevelationBadge.setBackgroundResource(R.drawable.bg_badge_makki);
                binding.tvSurahRevelationBadge.setTextColor(ContextCompat.getColor(context, R.color.text_surah_makki));
            } else {
                binding.tvSurahRevelationBadge.setText(isBengali ? "মাদানী" : "Madani");
                binding.tvSurahRevelationBadge.setBackgroundResource(R.drawable.bg_badge_madani);
                binding.tvSurahRevelationBadge.setTextColor(ContextCompat.getColor(context, R.color.text_surah_madani));
            }

            // Ayah count details
            if (isBengali) {
                binding.tvSurahDetails.setText(BengaliNumberUtil.toBengali(surah.getNumberOfAyahs()) + " আয়াত");
            } else {
                binding.tvSurahDetails.setText(surah.getNumberOfAyahs() + " Verses");
            }

            binding.tvSurahNameArabic.setText(surah.getNameArabic());
            binding.tvSurahEnglishName.setText(surah.getNameEnglish());

            // Bookmark State
            boolean isBookmarked = surah.isFavorite();
            if (isBookmarked) {
                binding.ivBookmarkSurah.setImageResource(R.drawable.ic_bookmark_filled);
                binding.ivBookmarkSurah.setColorFilter(ContextCompat.getColor(context, R.color.accent_mint));
            } else {
                binding.ivBookmarkSurah.setImageResource(R.drawable.ic_bookmark);
                binding.ivBookmarkSurah.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
            }

            binding.btnBookmarkSurah.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && bookmarkListener != null) {
                    bookmarkListener.onBookmarkClick(surah, pos);
                }
            });

            binding.cardSurahItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSurahClick(surah);
                }
            });
        }
    }
}
