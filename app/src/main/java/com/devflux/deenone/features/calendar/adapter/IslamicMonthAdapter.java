package com.devflux.deenone.features.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemTwelveMonthCardBinding;
import com.devflux.deenone.features.calendar.model.IslamicMonthItem;

import java.util.ArrayList;
import java.util.List;

public class IslamicMonthAdapter extends RecyclerView.Adapter<IslamicMonthAdapter.MonthViewHolder> {

    private final List<IslamicMonthItem> items = new ArrayList<>();

    public void setItems(List<IslamicMonthItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTwelveMonthCardBinding binding = ItemTwelveMonthCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MonthViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MonthViewHolder extends RecyclerView.ViewHolder {
        private final ItemTwelveMonthCardBinding binding;

        MonthViewHolder(ItemTwelveMonthCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(IslamicMonthItem item) {
            Context context = binding.getRoot().getContext();
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            binding.tvMonthIndex.setText(isBn ? com.devflux.deenone.utils.BengaliNumberUtil.toBengali(item.getMonthNumber()) : String.valueOf(item.getMonthNumber()));
            String title = isBn ? (item.getNameBengali() + " • " + item.getNameArabic())
                                : (item.getNameEnglish() + " • " + item.getNameArabic());
            binding.tvMonthTitle.setText(title);

            if (item.isSacredMonth()) {
                binding.tvMonthSacredBadge.setVisibility(View.VISIBLE);
                binding.tvMonthSacredBadge.setText(isBn ? "পবিত্র ৪ মাস" : "Sacred Month");
            } else {
                binding.tvMonthSacredBadge.setVisibility(View.GONE);
            }

            binding.tvMonthDescription.setText(item.getSpiritualSignificance());

            if (item.getKeyEvents() != null && !item.getKeyEvents().isEmpty()) {
                binding.tvMonthObservances.setVisibility(View.VISIBLE);
                binding.tvMonthObservances.setText(item.getKeyEvents());
            } else {
                binding.tvMonthObservances.setVisibility(View.GONE);
            }
        }
    }
}
