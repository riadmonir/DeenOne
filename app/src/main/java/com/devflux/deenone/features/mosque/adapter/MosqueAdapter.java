package com.devflux.deenone.features.mosque.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemMosqueCardBinding;
import com.devflux.deenone.features.mosque.model.MosqueItem;

import java.util.ArrayList;
import java.util.List;

public class MosqueAdapter extends RecyclerView.Adapter<MosqueAdapter.MosqueViewHolder> {

    public interface OnMosqueActionClickListener {
        void onWalkingClick(MosqueItem item);
        void onDrivingClick(MosqueItem item);
        void onShareClick(MosqueItem item);
    }

    private final List<MosqueItem> items = new ArrayList<>();
    private final OnMosqueActionClickListener listener;

    public MosqueAdapter(OnMosqueActionClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<MosqueItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MosqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMosqueCardBinding binding = ItemMosqueCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MosqueViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MosqueViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MosqueViewHolder extends RecyclerView.ViewHolder {
        private final ItemMosqueCardBinding binding;

        MosqueViewHolder(ItemMosqueCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MosqueItem item, OnMosqueActionClickListener listener) {
            binding.tvMosqueName.setText(item.getName());
            binding.tvMosqueAddress.setText(item.getAddress());
            binding.tvMosqueDistance.setText(item.getDistanceFormatted());
            binding.tvMosqueWalkingTime.setText(item.getWalkingTimeFormatted());
            binding.tvMosqueDrivingTime.setText(item.getDrivingTimeFormatted());
            binding.tvMosqueStatus.setText("● " + item.getStatus());

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onWalkingClick(item);
            });

            binding.btnWalkingDirections.setOnClickListener(v -> {
                if (listener != null) listener.onWalkingClick(item);
            });

            binding.btnDrivingDirections.setOnClickListener(v -> {
                if (listener != null) listener.onDrivingClick(item);
            });

            binding.btnShareMosque.setOnClickListener(v -> {
                if (listener != null) listener.onShareClick(item);
            });
        }
    }
}
