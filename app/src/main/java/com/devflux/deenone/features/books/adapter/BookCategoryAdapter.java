package com.devflux.deenone.features.books.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.books.model.BookCategoryItem;

import java.util.ArrayList;
import java.util.List;

public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(BookCategoryItem category, int position);
    }

    private final List<BookCategoryItem> items = new ArrayList<>();
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public BookCategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<BookCategoryItem> list) {
        items.clear();
        if (list != null) {
            items.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setSelectedCategory(String categoryKey) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getKey().equalsIgnoreCase(categoryKey)) {
                int prev = selectedPosition;
                selectedPosition = i;
                notifyItemChanged(prev);
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_category_chip, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        BookCategoryItem item = items.get(position);
        holder.bind(item, position == selectedPosition);
        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION && currentPos != selectedPosition) {
                int prev = selectedPosition;
                selectedPosition = currentPos;
                notifyItemChanged(prev);
                notifyItemChanged(selectedPosition);
                if (listener != null) {
                    listener.onCategoryClick(item, selectedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvChip;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChip = itemView.findViewById(R.id.tvCategoryChip);
        }

        public void bind(BookCategoryItem item, boolean isSelected) {
            tvChip.setText(item.getIconEmoji() + " " + item.getTitleBengali());

            if (isSelected) {
                tvChip.setBackgroundResource(R.drawable.bg_btn_mint_pill);
                tvChip.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.bg_main));
                tvChip.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                tvChip.setBackgroundResource(R.drawable.bg_badge_pill);
                tvChip.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_primary));
                tvChip.setTypeface(null, android.graphics.Typeface.NORMAL);
            }
        }
    }
}
