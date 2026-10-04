package com.devflux.deenone.features.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemIslamicShortBinding;
import com.devflux.deenone.features.home.model.ShortItem;

import java.util.List;

public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortViewHolder> {

    public interface OnShortClickListener {
        void onShortClick(ShortItem item);
    }

    private final List<ShortItem> shortsList;
    private final OnShortClickListener listener;

    public ShortsAdapter(List<ShortItem> shortsList, OnShortClickListener listener) {
        this.shortsList = shortsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIslamicShortBinding binding = ItemIslamicShortBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ShortViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortViewHolder holder, int position) {
        ShortItem item = shortsList.get(position);
        holder.binding.tvShortsTitle.setText(item.getTitle());
        holder.binding.tvShortsViews.setText(item.getViews());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShortClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shortsList.size();
    }

    public static class ShortViewHolder extends RecyclerView.ViewHolder {
        final ItemIslamicShortBinding binding;

        public ShortViewHolder(ItemIslamicShortBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
