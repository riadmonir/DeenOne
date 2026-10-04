package com.devflux.deenone.features.quran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranPageDataHelper;
import com.devflux.deenone.core.quran.QuranParaItem;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.databinding.ItemHifzIndexCardBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for 30 Paras and 114 Surahs Index Directory in Hifz Hub.
 */
public class HifzIndexAdapter extends RecyclerView.Adapter<HifzIndexAdapter.IndexViewHolder> {

    public interface OnIndexSelectListener {
        void onPageSelected(int targetPageNumber);
    }

    public static final int MODE_PARAS = 0;
    public static final int MODE_SURAHS = 1;

    private int currentMode = MODE_PARAS;
    private List<QuranParaItem> paraList = new ArrayList<>();
    private List<QuranSurahEntity> surahList = new ArrayList<>();
    private final OnIndexSelectListener listener;

    public HifzIndexAdapter(OnIndexSelectListener listener) {
        this.listener = listener;
        this.paraList = QuranParaItem.getAll30Paras();
    }

    public void setMode(int mode) {
        this.currentMode = mode;
        notifyDataSetChanged();
    }

    public void setSurahs(List<QuranSurahEntity> surahs) {
        this.surahList = surahs != null ? surahs : new ArrayList<>();
        if (currentMode == MODE_SURAHS) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return (currentMode == MODE_PARAS) ? paraList.size() : surahList.size();
    }

    @NonNull
    @Override
    public IndexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHifzIndexCardBinding binding = ItemHifzIndexCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new IndexViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IndexViewHolder holder, int position) {
        if (currentMode == MODE_PARAS) {
            holder.bindPara(paraList.get(position));
        } else {
            holder.bindSurah(surahList.get(position));
        }
    }

    class IndexViewHolder extends RecyclerView.ViewHolder {
        private final ItemHifzIndexCardBinding binding;

        public IndexViewHolder(@NonNull ItemHifzIndexCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Rule 7: ZERO touch animation on CardView/List item.
        }

        public void bindPara(QuranParaItem para) {
            Context context = binding.getRoot().getContext();
            boolean isBn = LocaleManager.isBengali(context);

            binding.tvIndexNumber.setText(isBn ? BengaliNumberUtil.toBengali(para.getJuzNumber()) : String.valueOf(para.getJuzNumber()));
            binding.tvIndexArabicName.setText(para.getArabicName());

            String title = isBn
                    ? ("পারা " + BengaliNumberUtil.toBengali(para.getJuzNumber()) + ": " + para.getBengaliName())
                    : ("Para " + para.getJuzNumber() + ": " + para.getEnglishName());
            binding.tvIndexTitleBn.setText(title);

            String range = isBn ? para.getRangeBengali() : para.getRangeEnglish();
            int startPage = QuranPageDataHelper.getPageForAyah(para.getStartSurahNumber(), para.getStartAyahNumber());
            int endPage = QuranPageDataHelper.getPageForAyah(para.getEndSurahNumber(), para.getEndAyahNumber());

            String pageInfo = (isBn ? "পৃষ্ঠা " + BengaliNumberUtil.toBengali(startPage) + " - " + BengaliNumberUtil.toBengali(endPage) : "Page " + startPage + " - " + endPage)
                    + " • " + range;
            binding.tvIndexSubtitle.setText(pageInfo);

            binding.tvIndexPageRangeBadge.setText(isBn ? ("পৃষ্ঠা " + BengaliNumberUtil.toBengali(startPage)) : ("Page " + startPage));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPageSelected(startPage);
                }
            });
        }

        public void bindSurah(QuranSurahEntity surah) {
            Context context = binding.getRoot().getContext();
            boolean isBn = LocaleManager.isBengali(context);

            binding.tvIndexNumber.setText(isBn ? BengaliNumberUtil.toBengali(surah.getNumber()) : String.valueOf(surah.getNumber()));
            binding.tvIndexArabicName.setText(surah.getNameArabic());

            String title = isBn ? surah.getNameBengali() : surah.getNameEnglish();
            binding.tvIndexTitleBn.setText((isBn ? "সূরা " : "Surah ") + title);

            int startPage = QuranPageDataHelper.getPageForAyah(surah.getNumber(), 1);
            String rev = surah.getRevelationType() != null ? surah.getRevelationType() : "";
            String count = isBn ? BengaliNumberUtil.toBengali(surah.getNumberOfAyahs()) : String.valueOf(surah.getNumberOfAyahs());

            String sub = rev + " • " + count + (isBn ? " আয়াত" : " Verses");
            binding.tvIndexSubtitle.setText(sub);

            binding.tvIndexPageRangeBadge.setText(isBn ? ("পৃষ্ঠা " + BengaliNumberUtil.toBengali(startPage)) : ("Page " + startPage));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPageSelected(startPage);
                }
            });
        }
    }
}
