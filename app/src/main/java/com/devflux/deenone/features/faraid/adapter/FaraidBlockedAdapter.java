package com.devflux.deenone.features.faraid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;

import java.util.ArrayList;
import java.util.List;

public class FaraidBlockedAdapter extends RecyclerView.Adapter<FaraidBlockedAdapter.ViewHolder> {

    private final List<BlockedHeirInfo> items = new ArrayList<>();

    public FaraidBlockedAdapter() {
    }

    public FaraidBlockedAdapter(List<BlockedHeirInfo> initialItems) {
        if (initialItems != null) {
            this.items.addAll(initialItems);
        }
    }

    public void setItems(List<BlockedHeirInfo> newItems) {
        updateData(newItems);
    }

    public void updateData(List<BlockedHeirInfo> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faraid_blocked_heir, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BlockedHeirInfo item = items.get(position);
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(holder.itemView.getContext());


        holder.tvBlockedHeirTitle.setText(item.getLocalizedHeirTitle(holder.itemView.getContext()));
        if (holder.tvBlockedBadge != null) {
            holder.tvBlockedBadge.setText(isBn ? "মাহজুব" : "Blocked");
        }
        holder.tvBlockedReason.setText((isBn ? "কারণ: " : "Reason: ") + item.getLocalizedLegalReason(isBn));
        holder.tvBlockedReference.setText((isBn ? "দলিল: " : "Reference: ") + item.getLocalizedShariahReference(isBn));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBlockedHeirTitle, tvBlockedBadge, tvBlockedReason, tvBlockedReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBlockedHeirTitle = itemView.findViewById(R.id.tvBlockedHeirTitle);
            tvBlockedBadge = itemView.findViewById(R.id.tvBlockedBadge);
            tvBlockedReason = itemView.findViewById(R.id.tvBlockedReason);
            tvBlockedReference = itemView.findViewById(R.id.tvBlockedReference);
        }
    }
}
