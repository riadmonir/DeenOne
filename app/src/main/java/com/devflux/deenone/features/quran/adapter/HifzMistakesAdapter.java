package com.devflux.deenone.features.quran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.HifzProgressManager;
import com.devflux.deenone.core.quran.QuranPageDataHelper;
import com.devflux.deenone.databinding.ItemHifzMistakeCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Marked Mistakes and Mutashabihat in Hifz Hub.
 */
public class HifzMistakesAdapter extends RecyclerView.Adapter<HifzMistakesAdapter.MistakeViewHolder> {

    public interface OnMistakeActionListener {
        void onPlayLoop(HifzProgressManager.HifzMistakeItem item);
        void onJumpPage(int pageNumber);
        void onRemove(HifzProgressManager.HifzMistakeItem item);
    }

    private List<HifzProgressManager.HifzMistakeItem> list = new ArrayList<>();
    private final OnMistakeActionListener listener;

    public HifzMistakesAdapter(OnMistakeActionListener listener) {
        this.listener = listener;
    }

    public void setMistakes(List<HifzProgressManager.HifzMistakeItem> items) {
        this.list = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public MistakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHifzMistakeCardBinding binding = ItemHifzMistakeCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MistakeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MistakeViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    class MistakeViewHolder extends RecyclerView.ViewHolder {
        private final ItemHifzMistakeCardBinding binding;

        public MistakeViewHolder(@NonNull ItemHifzMistakeCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            TouchAnimationUtil.attachTouchSpring(binding.btnRemoveMistake);
            TouchAnimationUtil.attachTouchSpring(binding.btnMistakePlayLoop);
            TouchAnimationUtil.attachTouchSpring(binding.btnMistakeJumpPage);
        }

        public void bind(HifzProgressManager.HifzMistakeItem item) {
            Context context = binding.getRoot().getContext();
            boolean isBn = LocaleManager.isBengali(context);

            String surahName = isBn ? item.surahNameBn : item.surahNameEn;
            String ayahNum = isBn ? BengaliNumberUtil.toBengali(item.ayahNumber) : String.valueOf(item.ayahNumber);
            binding.tvMistakeSurahAyahBadge.setText((isBn ? "সূরা " : "Surah ") + surahName + ": " + (isBn ? "আয়াত " : "Ayah ") + ayahNum);

            binding.tvMistakeArabicText.setText(item.textArabic);

            int page = QuranPageDataHelper.getPageForAyah(item.surahNumber, item.ayahNumber);
            binding.btnMistakeJumpPage.setText(isBn ? ("পৃষ্ঠা " + BengaliNumberUtil.toBengali(page) + " এ যান →") : ("Go to Page " + page + " →"));

            binding.btnMistakePlayLoop.setOnClickListener(v -> {
                if (listener != null) listener.onPlayLoop(item);
            });

            binding.btnMistakeJumpPage.setOnClickListener(v -> {
                if (listener != null) listener.onJumpPage(page);
            });

            binding.btnRemoveMistake.setOnClickListener(v -> {
                if (listener != null) listener.onRemove(item);
            });
        }
    }
}
