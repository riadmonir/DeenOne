package com.devflux.deenone.core.travel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.databinding.ItemTravelSearchResultBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TravelLocationSearchAdapter extends RecyclerView.Adapter<TravelLocationSearchAdapter.LocationViewHolder> {

    public interface OnLocationSelectedListener {
        void onLocationSelected(TravelLocationSearchManager.TravelLocationItem item);
    }

    private List<TravelLocationSearchManager.TravelLocationItem> items = new ArrayList<>();
    private final OnLocationSelectedListener listener;

    public TravelLocationSearchAdapter(OnLocationSelectedListener listener) {
        this.listener = listener;
    }

    public void setItems(List<TravelLocationSearchManager.TravelLocationItem> newItems) {
        this.items = newItems != null ? new ArrayList<>(newItems) : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTravelSearchResultBinding binding = ItemTravelSearchResultBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        private final ItemTravelSearchResultBinding binding;

        public LocationViewHolder(ItemTravelSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TravelLocationSearchManager.TravelLocationItem item, OnLocationSelectedListener listener) {
            binding.tvLocationTitle.setText(item.title);
            binding.tvLocationSubtitle.setText(item.subtitle);

            // Icon according to category
            if ("ISLAMIC_HOLY".equalsIgnoreCase(item.category)) {
                binding.ivLocationCategoryIcon.setImageResource(R.drawable.ic_mosque);
            } else if ("INTERNATIONAL".equalsIgnoreCase(item.category)) {
                binding.ivLocationCategoryIcon.setImageResource(R.drawable.ic_globe);
            } else {
                binding.ivLocationCategoryIcon.setImageResource(R.drawable.ic_location_pin);
            }

            // Distance
            if (item.distanceKmFromOrigin > 0) {
                binding.tvLocationDistance.setVisibility(View.VISIBLE);
                boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(binding.getRoot().getContext());
                String distStr = String.format(Locale.US, "%.0f", item.distanceKmFromOrigin);
                if (isBn) {
                    binding.tvLocationDistance.setText(BengaliNumberUtil.toBengali(distStr) + " কিমি");
                } else {
                    binding.tvLocationDistance.setText(distStr + " km");
                }
            } else {
                binding.tvLocationDistance.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLocationSelected(item);
                }
            });
        }
    }
}
