package com.devflux.deenone.features.battle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.features.battle.model.BattleCategory;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 2-Column Grid Category Adapter for Knowledge Battle.
 * Formatted matching the 100% Visual Fidelity screenshot:
 * - Circular Golden Glow Badge with study icon
 * - Centered bold Category Title
 * - Soft pill badge showing real-time question count (e.g. "১০০ টি প্রশ্ন")
 */
public class BattleCategoryAdapter extends RecyclerView.Adapter<BattleCategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategorySelected(BattleCategory category);
    }

    private final List<BattleCategory> categories = new ArrayList<>();
    private final OnCategoryClickListener listener;

    public BattleCategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<BattleCategory> newCategories) {
        this.categories.clear();
        if (newCategories != null) {
            this.categories.addAll(newCategories);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_battle_category_grid, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryTitleGrid;
        private final TextView tvCategoryQuestionCountGrid;
        private final ImageView ivCategoryIconGrid;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryTitleGrid = itemView.findViewById(R.id.tvCategoryTitleGrid);
            tvCategoryQuestionCountGrid = itemView.findViewById(R.id.tvCategoryQuestionCountGrid);
            ivCategoryIconGrid = itemView.findViewById(R.id.ivCategoryIconGrid);
        }

        void bind(BattleCategory category) {
            Context ctx = itemView.getContext();
            boolean isBn = LocaleManager.isBengali(ctx);

            tvCategoryTitleGrid.setText(isBn ? category.getTitleBn() : category.getTitleEn());

            tvCategoryQuestionCountGrid.setText(isBn
                    ? (BengaliNumberUtil.toBengali(category.getTotalQuestions()) + " টি প্রশ্ন")
                    : (category.getTotalQuestions() + " Questions"));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategorySelected(category);
                }
            });
        }
    }
}
