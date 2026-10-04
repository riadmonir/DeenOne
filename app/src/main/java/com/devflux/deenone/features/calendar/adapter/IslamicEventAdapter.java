package com.devflux.deenone.features.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemIslamicEventCardBinding;
import com.devflux.deenone.features.calendar.model.IslamicEventItem;

import java.util.ArrayList;
import java.util.List;

public class IslamicEventAdapter extends RecyclerView.Adapter<IslamicEventAdapter.EventViewHolder> {

    private final List<IslamicEventItem> items = new ArrayList<>();

    public void setItems(List<IslamicEventItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIslamicEventCardBinding binding = ItemIslamicEventCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private final ItemIslamicEventCardBinding binding;

        EventViewHolder(ItemIslamicEventCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(IslamicEventItem item) {
            binding.tvEventTitle.setText(item.getTitle());
            binding.tvEventDateBadge.setText(item.getHijriDateString());

            if (item.getArabicName() != null && !item.getArabicName().isEmpty()) {
                binding.tvEventArabicName.setText(item.getArabicName());
                binding.tvEventArabicName.setVisibility(View.VISIBLE);
            } else {
                binding.tvEventArabicName.setVisibility(View.GONE);
            }

            binding.tvEventDescription.setText(item.getDescription());

            if (item.getReference() != null && !item.getReference().isEmpty()) {
                binding.layoutEventDalil.setVisibility(View.VISIBLE);
                binding.tvEventDalil.setText(item.getReference());
            } else {
                binding.layoutEventDalil.setVisibility(View.GONE);
            }
        }
    }
}
