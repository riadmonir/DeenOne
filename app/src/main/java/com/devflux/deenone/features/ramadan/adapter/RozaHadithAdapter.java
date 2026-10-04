package com.devflux.deenone.features.ramadan.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.databinding.ItemRozaHadithCardBinding;
import com.devflux.deenone.features.ramadan.data.RozaHadithFavoritesManager;
import com.devflux.deenone.features.ramadan.model.RozaHadithItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

/**
 * 60 FPS Ultra-Fast, Memory-efficient Adapter for Roza Hadith Cards.
 * Supports dynamic spring touch effects, instant favorite toggle, and dual language mode.
 */
public class RozaHadithAdapter extends RecyclerView.Adapter<RozaHadithAdapter.RozaHadithViewHolder> {

    private List<RozaHadithItem> items;
    private final boolean isBn;

    public RozaHadithAdapter(List<RozaHadithItem> items, boolean isBn) {
        this.items = items;
        this.isBn = isBn;
    }

    public void updateData(List<RozaHadithItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RozaHadithViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRozaHadithCardBinding binding = ItemRozaHadithCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new RozaHadithViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RozaHadithViewHolder holder, int position) {
        RozaHadithItem item = items.get(position);
        holder.bind(item, isBn);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class RozaHadithViewHolder extends RecyclerView.ViewHolder {

        private final ItemRozaHadithCardBinding binding;

        public RozaHadithViewHolder(ItemRozaHadithCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            TouchAnimationUtil.attachTouchSpring(binding.btnFavoriteAction);
        }

        public void bind(RozaHadithItem item, boolean isBn) {
            Context context = itemView.getContext();

            // 1. Badge (e.g. "হাদিস- ১৮৯১" / "Hadith- 1891")
            binding.tvHadithBadge.setText(item.getBadgeText(isBn));

            // 2. Arabic Text
            binding.tvHadithArabic.setText(item.getArabicText());

            // 3. Bengali / English Translation
            binding.tvHadithTranslation.setText(item.getTranslation(isBn));

            // 4. Favorite Action State
            updateFavoriteUi(item.isFavorite(), isBn, context);

            // 5. Toggle Favorite Listener
            binding.btnFavoriteAction.setOnClickListener(v -> {
                boolean newState = RozaHadithFavoritesManager.toggleFavorite(context, item.getHadithNumber());
                item.setFavorite(newState);
                updateFavoriteUi(newState, isBn, context);
            });
        }

        private void updateFavoriteUi(boolean isFav, boolean isBn, Context context) {
            String label = isBn ? "প্রিয় তালিকা" : "Favorite";
            binding.tvFavoriteLabel.setText(label);

            if (isFav) {
                binding.ivFavoriteIcon.setImageResource(R.drawable.ic_heart_filled);
                int favRed = Color.parseColor("#EF4444");
                ImageViewCompat.setImageTintList(binding.ivFavoriteIcon, ColorStateList.valueOf(favRed));
                binding.tvFavoriteLabel.setTextColor(favRed);
            } else {
                binding.ivFavoriteIcon.setImageResource(R.drawable.ic_heart_outline);
                int secondaryColor = ContextCompat.getColor(context, R.color.text_secondary);
                ImageViewCompat.setImageTintList(binding.ivFavoriteIcon, ColorStateList.valueOf(secondaryColor));
                binding.tvFavoriteLabel.setTextColor(secondaryColor);
            }
        }
    }
}
