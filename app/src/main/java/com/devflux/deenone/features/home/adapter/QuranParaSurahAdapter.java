package com.devflux.deenone.features.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.databinding.ItemQuranParaSurahBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class QuranParaSurahAdapter extends RecyclerView.Adapter<QuranParaSurahAdapter.ParaSurahViewHolder> {

    public interface OnSurahClickListener {
        void onSurahClick(QuranSurahEntity surah);
    }

    private final List<QuranSurahEntity> surahList = new ArrayList<>();
    private final OnSurahClickListener listener;

    public QuranParaSurahAdapter(OnSurahClickListener listener) {
        this.listener = listener;
    }

    public void setSurahs(List<QuranSurahEntity> list) {
        surahList.clear();
        if (list != null) {
            surahList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ParaSurahViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuranParaSurahBinding binding = ItemQuranParaSurahBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ParaSurahViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParaSurahViewHolder holder, int position) {
        QuranSurahEntity surah = surahList.get(position);
        holder.bind(surah, listener);
    }

    @Override
    public int getItemCount() {
        return surahList.size();
    }

    static class ParaSurahViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuranParaSurahBinding binding;

        public ParaSurahViewHolder(@NonNull ItemQuranParaSurahBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(QuranSurahEntity surah, OnSurahClickListener listener) {
            Context context = binding.getRoot().getContext();
            boolean isBn = LocaleManager.isBengali(context);

            binding.tvSurahNumber.setText(isBn ? BengaliNumberUtil.toBengali(surah.getNumber()) : String.valueOf(surah.getNumber()));
            binding.tvSurahName.setText(isBn ? surah.getNameBengali() : surah.getNameEnglish());

            // Revelation Badge (Makki / Madani)
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

            binding.tvSurahAyahsCount.setText(isBn
                    ? (BengaliNumberUtil.toBengali(surah.getNumberOfAyahs()) + " আয়াত")
                    : (surah.getNumberOfAyahs() + " Verses"));

            binding.tvSurahNameArabic.setText(surah.getNameArabic());

            binding.cardParaSurahItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSurahClick(surah);
                }
            });
        }
    }
}
