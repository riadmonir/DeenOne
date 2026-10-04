package com.devflux.deenone.features.salahguide.adapter;

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

public class EssentialSurahsAdapter extends RecyclerView.Adapter<EssentialSurahsAdapter.EssentialSurahViewHolder> {

    public interface OnSurahClickListener {
        void onSurahClick(QuranSurahEntity surah);
    }

    public interface OnSurahBookmarkListener {
        void onBookmarkClick(QuranSurahEntity surah, int position);
    }

    private final List<QuranSurahEntity> surahs = new ArrayList<>();
    private final OnSurahClickListener clickListener;
    private final OnSurahBookmarkListener bookmarkListener;

    public EssentialSurahsAdapter(OnSurahClickListener clickListener, OnSurahBookmarkListener bookmarkListener) {
        this.clickListener = clickListener;
        this.bookmarkListener = bookmarkListener;
    }

    public void setSurahs(List<QuranSurahEntity> newSurahs) {
        surahs.clear();
        if (newSurahs != null) {
            surahs.addAll(newSurahs);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EssentialSurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuranSurahBinding binding = ItemQuranSurahBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new EssentialSurahViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EssentialSurahViewHolder holder, int position) {
        holder.bind(surahs.get(position));
    }

    @Override
    public int getItemCount() {
        return surahs.size();
    }

    public class EssentialSurahViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuranSurahBinding binding;

        public EssentialSurahViewHolder(@NonNull ItemQuranSurahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            TouchAnimationUtil.attachTouchSpring(binding.btnBookmarkSurah);
        }

        public void bind(QuranSurahEntity surah) {
            Context context = binding.getRoot().getContext();
            boolean isBn = LocaleManager.isBengali(context);

            int num = surah.getNumber();

            // 1. Serial Badge: Islamic Star Rosette + Surah Number
            binding.tvSurahNumber.setText(isBn ? BengaliNumberUtil.toBengali(num) : String.valueOf(num));

            // 2. Surah Display Name
            String nameBn = getVerbatimSurahNameBn(num, surah.getNameBengali());
            String nameEn = surah.getNameEnglish();
            binding.tvSurahNameBengali.setText(isBn ? nameBn : nameEn);

            // 3. Meaning / Subtitle (verbatim from screenshot)
            String meaning = isBn ? getVerbatimMeaningBn(num, surah.getMeaningBengali()) : getVerbatimMeaningEn(num, surah.getMeaningEnglish());
            if (meaning != null && !meaning.trim().isEmpty()) {
                binding.tvSurahMeaning.setVisibility(View.VISIBLE);
                binding.tvDot1.setVisibility(View.VISIBLE);
                binding.tvSurahMeaning.setText(meaning.trim());
            } else {
                binding.tvSurahMeaning.setVisibility(View.GONE);
                binding.tvDot1.setVisibility(View.GONE);
            }

            // 4. Revelation Badge (Makki / Madani)
            String revType = surah.getRevelationType();
            boolean isMakki = revType == null || "মাক্কী".equals(revType) || "Meccan".equalsIgnoreCase(revType);
            if (isMakki) {
                binding.tvSurahRevelationBadge.setText(isBn ? "মাক্কী" : "Makki");
                binding.tvSurahRevelationBadge.setBackgroundResource(R.drawable.bg_badge_makki);
                binding.tvSurahRevelationBadge.setTextColor(ContextCompat.getColor(context, R.color.text_surah_makki));
            } else {
                binding.tvSurahRevelationBadge.setText(isBn ? "মাদানী" : "Madani");
                binding.tvSurahRevelationBadge.setBackgroundResource(R.drawable.bg_badge_madani);
                binding.tvSurahRevelationBadge.setTextColor(ContextCompat.getColor(context, R.color.text_surah_madani));
            }

            // 5. Ayah Count Details
            if (isBn) {
                binding.tvSurahDetails.setText(BengaliNumberUtil.toBengali(surah.getNumberOfAyahs()) + " আয়াত");
            } else {
                binding.tvSurahDetails.setText(surah.getNumberOfAyahs() + " Verses");
            }

            // 6. Arabic Calligraphy Name (100% authentic)
            binding.tvSurahNameArabic.setText(getAuthenticArabicName(num, surah.getNameArabic()));

            // 7. Bookmark State
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
                if (clickListener != null) {
                    clickListener.onSurahClick(surah);
                }
            });
        }
    }

    private static String getVerbatimSurahNameBn(int num, String fallback) {
        switch (num) {
            case 1: return "আল ফাতিহা";
            case 105: return "আল ফীল";
            case 106: return "আল কুরাইশ";
            case 107: return "আল মাউন";
            case 108: return "আল কাওসার";
            case 109: return "আল কাফিরুন";
            case 110: return "আন নাসর";
            case 111: return "আল লাহাব";
            case 112: return "আল-ইখলাস";
            case 113: return "আল-ফালাক";
            case 114: return "আন-নাস";
            default: return fallback != null ? fallback : "সূরা";
        }
    }

    private static String getVerbatimMeaningBn(int num, String fallback) {
        switch (num) {
            case 1: return "সূচনা";
            case 105: return "হাতি";
            case 106: return "কুরাইশ গোত্র";
            case 107: return "সাহায্য সহায়তা";
            case 108: return "কাউসার/প্রাচুর্য";
            case 109: return "অবিশ্বাসী";
            case 110: return "সাহায্য";
            case 111: return "খেজুরের পাকানো (রশি)";
            case 112: return "আন্তরিকতা";
            case 113: return "নিশিভোর";
            case 114: return "মানুষ জাতি";
            default: return fallback;
        }
    }

    private static String getVerbatimMeaningEn(int num, String fallback) {
        switch (num) {
            case 1: return "The Opening";
            case 105: return "The Elephant";
            case 106: return "Quraysh Tribe";
            case 107: return "Small Kindnesses";
            case 108: return "The Abundance";
            case 109: return "The Disbelievers";
            case 110: return "Divine Support";
            case 111: return "Palm Fiber";
            case 112: return "Sincerity";
            case 113: return "The Daybreak";
            case 114: return "Mankind";
            default: return fallback;
        }
    }

    private static String getAuthenticArabicName(int num, String fallback) {
        switch (num) {
            case 1: return "الفاتحة";
            case 105: return "الفيل";
            case 106: return "قريش";
            case 107: return "الماعون";
            case 108: return "الكوثر";
            case 109: return "الكافرون";
            case 110: return "النصر";
            case 111: return "المسد";
            case 112: return "الإخلاص";
            case 113: return "الفلق";
            case 114: return "الناس";
            default: return fallback != null ? fallback : "سورة";
        }
    }
}
