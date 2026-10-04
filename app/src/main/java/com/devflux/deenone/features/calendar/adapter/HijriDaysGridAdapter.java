package com.devflux.deenone.features.calendar.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemHijriDayGridBinding;
import com.devflux.deenone.features.calendar.model.HijriDayGridItem;

import java.util.ArrayList;
import java.util.List;

public class HijriDaysGridAdapter extends RecyclerView.Adapter<HijriDaysGridAdapter.DayViewHolder> {

    public interface OnDayClickListener {
        void onDayClick(HijriDayGridItem item);
    }

    private final List<HijriDayGridItem> items = new ArrayList<>();
    private final OnDayClickListener listener;

    public HijriDaysGridAdapter(OnDayClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<HijriDayGridItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHijriDayGridBinding binding = ItemHijriDayGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new DayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private final ItemHijriDayGridBinding binding;

        DayViewHolder(ItemHijriDayGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(HijriDayGridItem item) {
            Context context = binding.getRoot().getContext();
            binding.tvHijriDayNumber.setText(item.dayNumberBn);
            binding.tvHijriDayLabel.setText(item.badgeLabel);

            int colorPrimaryGreen = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.primary_green);
            int colorAccentMint = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.accent_mint);
            int colorBgCard = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.bg_card);
            int colorBorderCard = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.border_card);
            int colorTextPrimary = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.text_primary);
            int colorTextSecondary = androidx.core.content.ContextCompat.getColor(context, com.devflux.deenone.R.color.text_secondary);

            if (item.isToday) {
                // Highlighted solid mint background for TODAY
                binding.cardHijriDay.setCardBackgroundColor(ColorStateList.valueOf(colorAccentMint));
                binding.cardHijriDay.setStrokeColor(ColorStateList.valueOf(colorPrimaryGreen));
                binding.tvHijriDayNumber.setTextColor(Color.parseColor("#02140E"));
                binding.tvHijriDayLabel.setTextColor(Color.parseColor("#02140E"));
            } else if (item.isAyyamAlBeed) {
                // Ayyam al-Beed highlighted border & label (13, 14, 15)
                binding.cardHijriDay.setCardBackgroundColor(ColorStateList.valueOf(colorBgCard));
                binding.cardHijriDay.setStrokeColor(ColorStateList.valueOf(colorAccentMint));
                binding.cardHijriDay.setStrokeWidth(3);
                binding.tvHijriDayNumber.setTextColor(colorTextPrimary);
                binding.tvHijriDayLabel.setTextColor(colorAccentMint);
            } else {
                // Standard Day Card
                binding.cardHijriDay.setCardBackgroundColor(ColorStateList.valueOf(colorBgCard));
                binding.cardHijriDay.setStrokeColor(ColorStateList.valueOf(colorBorderCard));
                binding.cardHijriDay.setStrokeWidth(2);
                binding.tvHijriDayNumber.setTextColor(colorTextPrimary);
                binding.tvHijriDayLabel.setTextColor(colorTextSecondary);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDayClick(item);
                }
            });
        }
    }
}
