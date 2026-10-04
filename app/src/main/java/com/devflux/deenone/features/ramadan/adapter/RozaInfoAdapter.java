package com.devflux.deenone.features.ramadan.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemRozaInfoRowBinding;
import com.devflux.deenone.features.ramadan.model.RozaInfoItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class RozaInfoAdapter extends RecyclerView.Adapter<RozaInfoAdapter.ViewHolder> {

    public interface OnRozaInfoClickListener {
        void onRozaInfoClick(RozaInfoItem item);
    }

    private final List<RozaInfoItem> items;
    private final boolean isBn;
    private final OnRozaInfoClickListener listener;

    public RozaInfoAdapter(List<RozaInfoItem> items, boolean isBn, OnRozaInfoClickListener listener) {
        this.items = items;
        this.isBn = isBn;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaInfoRowBinding binding = ItemRozaInfoRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RozaInfoItem item = items.get(position);
        holder.binding.tvInfoTitle.setText(item.getTitle(isBn));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRozaInfoClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemRozaInfoRowBinding binding;

        ViewHolder(ItemRozaInfoRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
