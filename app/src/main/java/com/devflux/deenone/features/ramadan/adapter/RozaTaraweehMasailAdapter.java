package com.devflux.deenone.features.ramadan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemRozaTaraweehMasailCardBinding;
import com.devflux.deenone.features.ramadan.model.RozaTaraweehMasalaItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

/**
 * 60 FPS Ultra-Fast, Memory-Efficient Adapter for "তারাবীহ নিয়ে মাসআলা-মাসায়েল" cards.
 * Exactly matches the user screenshot layout with smooth spring touches and expand/collapse.
 */
public class RozaTaraweehMasailAdapter extends RecyclerView.Adapter<RozaTaraweehMasailAdapter.MasailViewHolder> {

    private final List<RozaTaraweehMasalaItem> items;
    private final boolean isBn;

    public RozaTaraweehMasailAdapter(List<RozaTaraweehMasalaItem> items, boolean isBn) {
        this.items = items;
        this.isBn = isBn;
    }

    @NonNull
    @Override
    public MasailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaTaraweehMasailCardBinding binding = ItemRozaTaraweehMasailCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new MasailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MasailViewHolder holder, int position) {
        holder.bind(items.get(position), isBn);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class MasailViewHolder extends RecyclerView.ViewHolder {
        private final ItemRozaTaraweehMasailCardBinding binding;

        public MasailViewHolder(ItemRozaTaraweehMasailCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            TouchAnimationUtil.attachTouchSpring(binding.layoutMasailToggleExpand);
        }

        public void bind(RozaTaraweehMasalaItem item, boolean isBn) {
            binding.tvMasailCardTitle.setText(item.getTitle(isBn));
            binding.tvMasailCardPreview.setText(item.getPreview(isBn));
            binding.tvMasailCardFullContent.setText(item.getContent(isBn));

            updateState(item.isExpanded(), isBn);

            View.OnClickListener toggleListener = v -> {
                boolean nextState = !item.isExpanded();
                item.setExpanded(nextState);
                binding.ivMasailToggleChevron.animate()
                        .rotation(nextState ? 180f : 0f)
                        .setDuration(200)
                        .start();
                updateState(nextState, isBn);
            };

            binding.layoutMasailToggleExpand.setOnClickListener(toggleListener);
            binding.cardMasailItem.setOnClickListener(toggleListener);
        }

        private void updateState(boolean isExpanded, boolean isBn) {
            if (isExpanded) {
                binding.tvMasailCardPreview.setVisibility(View.GONE);
                binding.tvMasailCardFullContent.setVisibility(View.VISIBLE);
                binding.tvMasailToggleText.setText(isBn ? "সংক্ষেপ করুন" : "Collapse");
                binding.ivMasailToggleChevron.setRotation(180f);
            } else {
                binding.tvMasailCardPreview.setVisibility(View.VISIBLE);
                binding.tvMasailCardFullContent.setVisibility(View.GONE);
                binding.tvMasailToggleText.setText(isBn ? "বিস্তারিত" : "Details");
                binding.ivMasailToggleChevron.setRotation(0f);
            }
        }
    }
}
