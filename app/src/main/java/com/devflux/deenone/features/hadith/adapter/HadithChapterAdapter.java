package com.devflux.deenone.features.hadith.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.HadithChapterEntity;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HadithChapterAdapter extends RecyclerView.Adapter<HadithChapterAdapter.ChapterViewHolder> implements Filterable {

    public interface OnChapterClickListener {
        void onChapterClick(HadithChapterEntity chapter);
    }

    private final Context context;
    private final OnChapterClickListener listener;
    private List<HadithChapterEntity> chapterList = new ArrayList<>();
    private List<HadithChapterEntity> filteredList = new ArrayList<>();

    public HadithChapterAdapter(Context context, OnChapterClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setChapters(List<HadithChapterEntity> list) {
        this.chapterList = list != null ? new ArrayList<>(list) : new ArrayList<>();
        this.filteredList = new ArrayList<>(this.chapterList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hadith_chapter_card, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        if (position < 0 || position >= filteredList.size()) return;
        HadithChapterEntity chapter = filteredList.get(position);

        boolean isBn = LocaleManager.isBengali(context);

        // 1. Chapter Number in Hexagon Badge
        String badgeNum;
        if (isBn) {
            badgeNum = chapter.getChapterNumberBn();
            if (badgeNum == null || badgeNum.isEmpty()) {
                badgeNum = BengaliNumberUtil.toBengali(chapter.getChapterNumber());
            }
        } else {
            badgeNum = String.valueOf(chapter.getChapterNumber());
        }
        holder.tvChapterNumberBadge.setText(badgeNum);

        // 2. Chapter Title
        String title;
        if (isBn) {
            title = chapter.getTitleBn();
            if (title == null || title.isEmpty()) {
                title = chapter.getTitleEn();
            }
        } else {
            title = chapter.getTitleEn();
            if (title == null || title.isEmpty()) {
                title = chapter.getTitleBn();
            }
        }
        holder.tvChapterTitle.setText(title != null ? title : "");

        // 3. Hadith Range
        String rangeStr;
        if (isBn) {
            String rangeVal = chapter.getHadithRangeBn();
            if (rangeVal == null || rangeVal.isEmpty()) {
                rangeVal = BengaliNumberUtil.toBengali(chapter.getStartHadith()) + " - " + BengaliNumberUtil.toBengali(chapter.getEndHadith());
            }
            rangeStr = "হাদিসের রেঞ্জ: " + rangeVal;
        } else {
            String rangeVal = chapter.getHadithRange();
            if (rangeVal == null || rangeVal.isEmpty()) {
                rangeVal = chapter.getStartHadith() + " - " + chapter.getEndHadith();
            }
            rangeStr = "Hadith Range: " + rangeVal;
        }
        holder.tvChapterRange.setText(rangeStr);

        // 4. Click handler (Touch animation excluded on cards)
        holder.cardHadithChapter.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChapterClick(chapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<HadithChapterEntity> matched = new ArrayList<>();
                if (constraint == null || constraint.toString().trim().isEmpty()) {
                    matched.addAll(chapterList);
                } else {
                    String query = constraint.toString().trim().toLowerCase();
                    for (HadithChapterEntity item : chapterList) {
                        String titleBn = item.getTitleBn() != null ? item.getTitleBn().toLowerCase() : "";
                        String titleEn = item.getTitleEn() != null ? item.getTitleEn().toLowerCase() : "";
                        String chapBn = item.getChapterNumberBn() != null ? item.getChapterNumberBn() : "";
                        String chapEn = String.valueOf(item.getChapterNumber());
                        String range = item.getHadithRangeBn() != null ? item.getHadithRangeBn() : "";

                        if (titleBn.contains(query) || titleEn.contains(query) || chapBn.contains(query) || chapEn.contains(query) || range.contains(query)) {
                            matched.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = matched;
                results.count = matched.size();
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<HadithChapterEntity>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardHadithChapter;
        TextView tvChapterNumberBadge;
        TextView tvChapterTitle;
        TextView tvChapterRange;

        ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            cardHadithChapter = itemView.findViewById(R.id.cardHadithChapter);
            tvChapterNumberBadge = itemView.findViewById(R.id.tvChapterNumberBadge);
            tvChapterTitle = itemView.findViewById(R.id.tvChapterTitle);
            tvChapterRange = itemView.findViewById(R.id.tvChapterRange);
        }
    }
}
