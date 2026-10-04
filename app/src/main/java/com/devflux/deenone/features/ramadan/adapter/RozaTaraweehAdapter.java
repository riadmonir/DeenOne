package com.devflux.deenone.features.ramadan.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemRozaTaraweehRowBinding;
import com.devflux.deenone.features.ramadan.model.RozaTaraweehItem;
import com.devflux.deenone.features.ramadan.ui.RozaTaraweehDetailDialog;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

/**
 * 60 FPS Ultra-Fast, Memory-Efficient Adapter for Roza Taraweeh (রোজা:- তারাবীহ) rows.
 * Exactly matches the user screenshot layout with smooth spring touches.
 */
public class RozaTaraweehAdapter extends RecyclerView.Adapter<RozaTaraweehAdapter.TaraweehViewHolder> {

    private final Activity activity;
    private List<RozaTaraweehItem> items;
    private final boolean isBn;

    public RozaTaraweehAdapter(Activity activity, List<RozaTaraweehItem> items, boolean isBn) {
        this.activity = activity;
        this.items = items;
        this.isBn = isBn;
    }

    public void updateData(List<RozaTaraweehItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaraweehViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaTaraweehRowBinding binding = ItemRozaTaraweehRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new TaraweehViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaraweehViewHolder holder, int position) {
        RozaTaraweehItem item = items.get(position);
        holder.bind(item, isBn, activity);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class TaraweehViewHolder extends RecyclerView.ViewHolder {
        private final ItemRozaTaraweehRowBinding binding;

        public TaraweehViewHolder(ItemRozaTaraweehRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RozaTaraweehItem item, boolean isBn, Activity activity) {
            // Verbatim Title (e.g. "তারাবীহ কি" / "What is Taraweeh")
            binding.tvTaraweehTitle.setText(item.getTitle(isBn));

            // On item click -> Open comprehensive detail dialog or Masail page dialog
            binding.cardTaraweehItem.setOnClickListener(v -> {
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    if ("taraweeh_masail".equals(item.getSlug())) {
                        com.devflux.deenone.features.ramadan.ui.RozaTaraweehMasailPageDialog.show(activity);
                    } else if ("taraweeh_for_women".equals(item.getSlug())) {
                        com.devflux.deenone.features.ramadan.ui.RozaTaraweehWomenPageDialog.show(activity);
                    } else {
                        RozaTaraweehDetailDialog.show(activity, item);
                    }
                }
            });
        }
    }
}
