package com.devflux.deenone.features.battle.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleRoom;

import java.util.ArrayList;
import java.util.List;

public class BattleLobbyPlayerAdapter extends RecyclerView.Adapter<BattleLobbyPlayerAdapter.ViewHolder> {

    public interface OnPlayerReadyToggleListener {
        void onToggleReady(BattlePlayer player);
    }

    private final List<BattlePlayer> players = new ArrayList<>();
    private int totalCapacity = 2;
    private OnPlayerReadyToggleListener toggleListener;

    public void setRoomData(BattleRoom room, OnPlayerReadyToggleListener listener) {
        this.players.clear();
        if (room != null) {
            this.players.addAll(room.getPlayers());
            this.totalCapacity = room.getConfig().getTotalPlayers();
        }
        this.toggleListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_battle_lobby_player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context ctx = holder.itemView.getContext();
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(ctx);
        if (position < players.size()) {
            BattlePlayer p = players.get(position);
            String displayName = p.getName();
            if (displayName != null) {
                displayName = displayName.replace("(আপনি)", "").replace("(You)", "").trim();
            }
            if (displayName == null || displayName.isEmpty() || displayName.equalsIgnoreCase("Anonymous")) {
                displayName = isBn ? "আপনি" : "You";
            }

            holder.tvAvatar.setText(displayName.length() > 0 ? String.valueOf(displayName.charAt(0)).toUpperCase() : "P");
            holder.tvName.setText(displayName);

            boolean isReady = p.isReady();
            boolean isHost = position == 0;
            String statusText;
            if (isHost) {
                statusText = isBn ? (isReady ? "হোস্ট • প্রস্তুত" : "হোস্ট • অপেক্ষমাণ...") : (isReady ? "Host • Ready" : "Host • Waiting...");
            } else {
                statusText = isBn ? (isReady ? "প্রস্তুত" : "অপেক্ষমাণ...") : (isReady ? "Ready" : "Waiting...");
            }
            holder.tvStatus.setText(statusText);
            holder.tvBadge.setText(isBn ? p.getStatus().getLabelBn() : p.getStatus().getLabelEn());

            if (isReady) {
                holder.tvBadge.setBackgroundTintList(androidx.core.content.ContextCompat.getColorStateList(ctx, R.color.primary_green));
                holder.tvBadge.setTextColor(androidx.core.content.ContextCompat.getColor(ctx, R.color.white));
            } else {
                holder.tvBadge.setBackgroundTintList(androidx.core.content.ContextCompat.getColorStateList(ctx, R.color.bg_card_active));
                holder.tvBadge.setTextColor(androidx.core.content.ContextCompat.getColor(ctx, R.color.text_secondary));
            }

            holder.itemView.setOnClickListener(v -> {
                if (toggleListener != null) {
                    toggleListener.onToggleReady(p);
                }
            });
        } else {
            // Empty waiting slot
            holder.tvAvatar.setText("+");
            holder.tvName.setText(isBn ? "খেলোয়াড়ের জন্য অপেক্ষা..." : "Waiting for player...");
            holder.tvStatus.setText(isBn ? "কোড দিয়ে যোগ দেওয়ার জন্য প্রস্তুত" : "Ready to join with code");
            holder.tvBadge.setText(isBn ? "অপেক্ষমাণ" : "Waiting");
            holder.tvBadge.setBackgroundTintList(androidx.core.content.ContextCompat.getColorStateList(ctx, R.color.bg_card_active));
            holder.tvBadge.setTextColor(androidx.core.content.ContextCompat.getColor(ctx, R.color.text_muted));
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(players.size(), totalCapacity);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvName, tvStatus, tvBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvLobbyPlayerAvatarInitial);
            tvName = itemView.findViewById(R.id.tvLobbyPlayerName);
            tvStatus = itemView.findViewById(R.id.tvLobbyPlayerStatus);
            tvBadge = itemView.findViewById(R.id.tvLobbyPlayerReadyBadge);
        }
    }
}
