package com.devflux.deenone.ui.dashboard.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemFeatureGridBinding;
import com.devflux.deenone.ui.dashboard.model.FeatureItem;

import java.util.List;

public class FeatureGridAdapter extends RecyclerView.Adapter<FeatureGridAdapter.FeatureViewHolder> {

    public interface OnFeatureClickListener {
        void onFeatureClick(FeatureItem item);
    }

    private final List<FeatureItem> featureList;
    private final OnFeatureClickListener listener;

    public FeatureGridAdapter(List<FeatureItem> featureList, OnFeatureClickListener listener) {
        this.featureList = featureList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeatureGridBinding binding = ItemFeatureGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new FeatureViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        FeatureItem item = featureList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    public static class FeatureViewHolder extends RecyclerView.ViewHolder {
        private final ItemFeatureGridBinding binding;

        public FeatureViewHolder(ItemFeatureGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FeatureItem item, OnFeatureClickListener listener) {
            binding.tvFeatureTitle.setText(item.getTitle());
            binding.ivFeatureIcon.setImageResource(item.getIconResId());

            // Set dynamic circular background color
            GradientDrawable circleBg = new GradientDrawable();
            circleBg.setShape(GradientDrawable.OVAL);
            circleBg.setColor(item.getCircleBgColor());
            binding.iconContainer.setBackground(circleBg);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFeatureClick(item);
                }
            });
        }
    }
}
