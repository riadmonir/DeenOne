package com.devflux.deenone.features.prophets.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemProphetMenuRowBinding;
import com.devflux.deenone.features.prophets.model.ProphetMenuItem;

import java.util.ArrayList;
import java.util.List;

public class ProphetsAdapter extends RecyclerView.Adapter<ProphetsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ProphetMenuItem item);
    }

    private final List<ProphetMenuItem> fullList;
    private final List<ProphetMenuItem> displayedList;
    private final OnItemClickListener listener;
    private boolean isBn = true;

    public ProphetsAdapter(List<ProphetMenuItem> list, boolean isBn, OnItemClickListener listener) {
        this.fullList = list != null ? new ArrayList<>(list) : new ArrayList<>();
        this.displayedList = new ArrayList<>(this.fullList);
        this.isBn = isBn;
        this.listener = listener;
    }

    public void setLanguage(boolean isBn) {
        this.isBn = isBn;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        displayedList.clear();
        if (query == null || query.trim().isEmpty()) {
            displayedList.addAll(fullList);
        } else {
            String lower = query.toLowerCase().trim();
            for (ProphetMenuItem item : fullList) {
                if (item.getTitleBn().toLowerCase().contains(lower) ||
                    item.getTitleEn().toLowerCase().contains(lower)) {
                    displayedList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateData(List<ProphetMenuItem> newList) {
        fullList.clear();
        if (newList != null) {
            fullList.addAll(newList);
        }
        displayedList.clear();
        displayedList.addAll(fullList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProphetMenuRowBinding binding = ItemProphetMenuRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProphetMenuItem item = displayedList.get(position);
        holder.binding.tvProphetMenuTitle.setText(item.getTitle(isBn));

        holder.binding.cardProphetMenuItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
        // Strict Rule 7: ZERO touch animation on cardProphetMenuItem
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemProphetMenuRowBinding binding;

        public ViewHolder(@NonNull ItemProphetMenuRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
