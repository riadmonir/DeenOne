package com.devflux.deenone.features.home.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranParaItem;
import com.devflux.deenone.databinding.ItemQuranParaBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuranParaAdapter extends RecyclerView.Adapter<QuranParaAdapter.ParaViewHolder> {

    public interface OnParaClickListener {
        void onParaClick(QuranParaItem para);
    }

    public interface OnParaBookmarkListener {
        void onBookmarkClick(QuranParaItem para, int position);
    }

    private final List<QuranParaItem> paraList = new ArrayList<>();
    private final OnParaClickListener listener;
    private OnParaBookmarkListener bookmarkListener;

    public QuranParaAdapter(OnParaClickListener listener) {
        this(listener, null);
    }

    public QuranParaAdapter(OnParaClickListener listener, OnParaBookmarkListener bookmarkListener) {
        this.listener = listener;
        this.bookmarkListener = bookmarkListener;
    }

    public void setOnParaBookmarkListener(OnParaBookmarkListener bookmarkListener) {
        this.bookmarkListener = bookmarkListener;
    }

    public void setParas(List<QuranParaItem> items) {
        paraList.clear();
        if (items != null) {
            paraList.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuranParaBinding binding = ItemQuranParaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ParaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParaViewHolder holder, int position) {
        QuranParaItem item = paraList.get(position);
        holder.bind(item, listener, bookmarkListener, this);
    }

    @Override
    public int getItemCount() {
        return paraList.size();
    }

    static class ParaViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuranParaBinding binding;

        public ParaViewHolder(@NonNull ItemQuranParaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            TouchAnimationUtil.attachTouchSpring(binding.btnBookmarkPara);
        }

        public void bind(QuranParaItem item, OnParaClickListener listener,
                         OnParaBookmarkListener bookmarkListener, QuranParaAdapter adapter) {
            Context context = binding.getRoot().getContext();
            boolean isBn = LocaleManager.isBengali(context);

            int num = item.getJuzNumber();
            binding.tvParaNumber.setText(isBn ? BengaliNumberUtil.toBengali(num) : String.valueOf(num));
            binding.tvParaName.setText(isBn ? item.getBengaliName() : item.getEnglishName());

            // Subtitle Info
            binding.tvParaIndexText.setText(isBn ? ("পারা " + BengaliNumberUtil.toBengali(num)) : ("Juz " + num));

            // Pill Badge for Surah count
            int surahCount = item.getEndSurahNumber() - item.getStartSurahNumber() + 1;
            if (isBn) {
                binding.tvParaBadge.setText(surahCount == 1 ? "১ সূরা" : (BengaliNumberUtil.toBengali(surahCount) + " সূরা"));
            } else {
                binding.tvParaBadge.setText(surahCount == 1 ? "1 Surah" : (surahCount + " Surahs"));
            }

            if (surahCount > 3) {
                binding.tvParaBadge.setBackgroundResource(R.drawable.bg_badge_makki);
                binding.tvParaBadge.setTextColor(ContextCompat.getColor(context, R.color.text_surah_makki));
            } else {
                binding.tvParaBadge.setBackgroundResource(R.drawable.bg_badge_madani);
                binding.tvParaBadge.setTextColor(ContextCompat.getColor(context, R.color.text_surah_madani));
            }

            // Verse / Surah range
            binding.tvParaRange.setText(isBn ? item.getRangeBengali() : item.getRangeEnglish());

            // Arabic calligraphy name in accent mint
            binding.tvParaNameArabic.setText(item.getArabicName());

            // Persistent Bookmark state
            SharedPreferences prefs = context.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
            Set<String> bookmarkedSet = prefs.getStringSet("bookmarked_paras", null);
            boolean isBookmarked = bookmarkedSet != null && bookmarkedSet.contains(String.valueOf(num));

            if (isBookmarked) {
                binding.ivBookmarkPara.setImageResource(R.drawable.ic_bookmark_filled);
                binding.ivBookmarkPara.setColorFilter(ContextCompat.getColor(context, R.color.accent_mint));
            } else {
                binding.ivBookmarkPara.setImageResource(R.drawable.ic_bookmark);
                binding.ivBookmarkPara.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
            }

            binding.btnBookmarkPara.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                SharedPreferences sp = context.getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
                Set<String> oldSet = sp.getStringSet("bookmarked_paras", null);
                Set<String> newSet = new HashSet<>(oldSet != null ? oldSet : new HashSet<>());

                String key = String.valueOf(num);
                if (newSet.contains(key)) {
                    newSet.remove(key);
                } else {
                    newSet.add(key);
                }
                sp.edit().putStringSet("bookmarked_paras", newSet).apply();
                adapter.notifyItemChanged(pos);

                if (bookmarkListener != null) {
                    bookmarkListener.onBookmarkClick(item, pos);
                }
            });

            binding.cardParaItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onParaClick(item);
                }
            });
        }
    }
}
