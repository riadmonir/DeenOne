package com.devflux.deenone.features.hadith.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.features.hadith.model.HadithBookCategory;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class HadithCategoryAdapter extends RecyclerView.Adapter<HadithCategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(HadithBookCategory category);
    }

    private final Context context;
    private final boolean isBn;
    private final OnCategoryClickListener listener;
    private final List<HadithBookCategory> originalList = new ArrayList<>();
    private final List<HadithBookCategory> displayedList = new ArrayList<>();

    public HadithCategoryAdapter(Context context, OnCategoryClickListener listener) {
        this.context = context;
        this.isBn = LocaleManager.isBengali(context);
        this.listener = listener;
    }

    public void setCategories(List<HadithBookCategory> categories) {
        originalList.clear();
        displayedList.clear();
        if (categories != null) {
            originalList.addAll(categories);
            displayedList.addAll(categories);
        }
        notifyDataSetChanged();
    }

    public void filter(String query) {
        displayedList.clear();
        if (query == null || query.trim().isEmpty()) {
            displayedList.addAll(originalList);
        } else {
            String lower = query.toLowerCase().trim();
            for (HadithBookCategory item : originalList) {
                if (item.getNameBn().toLowerCase().contains(lower)
                        || item.getNameEn().toLowerCase().contains(lower)
                        || item.getAuthorBn().toLowerCase().contains(lower)
                        || item.getAuthorEn().toLowerCase().contains(lower)
                        || item.getInitials().toLowerCase().contains(lower)
                        || item.getSlug().toLowerCase().contains(lower)) {
                    displayedList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hadith_category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        HadithBookCategory category = displayedList.get(position);
        holder.bind(category, isBn, listener);
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivHexagonBg;
        private final TextView tvCategoryInitials;
        private final TextView tvBookName;
        private final TextView tvAuthorName;
        private final TextView tvHadithCount;
        private final TextView tvHadithLabel;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHexagonBg = itemView.findViewById(R.id.ivHexagonBg);
            tvCategoryInitials = itemView.findViewById(R.id.tvCategoryInitials);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvHadithCount = itemView.findViewById(R.id.tvHadithCount);
            tvHadithLabel = itemView.findViewById(R.id.tvHadithLabel);
        }

        public void bind(HadithBookCategory category, boolean isBn, OnCategoryClickListener listener) {
            // Hexagon Color Tint
            try {
                int color = Color.parseColor(category.getColorHex());
                ivHexagonBg.setColorFilter(color);
            } catch (Exception e) {
                ivHexagonBg.setColorFilter(Color.parseColor("#10B981"));
            }

            // Category Initials
            tvCategoryInitials.setText(category.getInitials());

            // Adjust font size slightly if initials length >= 3 (e.g. "100")
            if (category.getInitials().length() >= 3) {
                tvCategoryInitials.setTextSize(11.5f);
            } else {
                tvCategoryInitials.setTextSize(14f);
            }

            // Names
            tvBookName.setText(category.getDisplayName(isBn));
            tvAuthorName.setText(category.getDisplayAuthor(isBn));

            // Counts
            tvHadithCount.setText(category.getFormattedCount(isBn));
            tvHadithLabel.setText(isBn ? "হাদিস" : "Hadith");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category);
                }
            });
        }
    }
}
