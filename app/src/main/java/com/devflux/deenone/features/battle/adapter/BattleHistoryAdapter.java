package com.devflux.deenone.features.battle.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.local.entity.BattleHistoryEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BattleHistoryAdapter extends RecyclerView.Adapter<BattleHistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private final List<BattleHistoryEntity> items = new ArrayList<>();

    public BattleHistoryAdapter(Context context) {
        this.context = context;
    }

    public void submitList(List<BattleHistoryEntity> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_battle_match_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        BattleHistoryEntity item = items.get(position);
        boolean isBn = LocaleManager.isBengali(context);

        // 1. Category Title
        String categoryTitle = item.getCategoryTitle();
        if (categoryTitle == null || categoryTitle.isEmpty() || categoryTitle.equalsIgnoreCase("category")) {
            categoryTitle = isBn ? "সাধারণ জ্ঞান (ইসলাম)" : "General Islamic Knowledge";
        }
        holder.tvMatchCategoryTitle.setText(categoryTitle);

        // 2. Room Code & Date
        String dateStr = DateFormat.format("dd MMM yyyy, hh:mm a", new Date(item.getTimestamp())).toString();
        if (isBn) {
            dateStr = BengaliNumberUtil.toBengali(dateStr);
        }
        String roomCode = item.getBattleCode() != null ? item.getBattleCode() : "";
        holder.tvMatchRoomCodeDate.setText(isBn
                ? ("রুম: " + (roomCode.isEmpty() ? "---" : roomCode) + " • " + dateStr)
                : ("Room: " + (roomCode.isEmpty() ? "---" : roomCode) + " • " + dateStr));

        // 3. Rank & Winner Badge
        if (item.isWinner() || item.getRank() == 1) {
            holder.tvMatchRankBadge.setText(isBn ? "১ম স্থান 🏆" : "1st Place 🏆");
            holder.tvMatchRankBadge.setBackgroundResource(R.drawable.bg_badge_gold_pill);
            holder.tvMatchRankBadge.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (item.getRank() == 2) {
            holder.tvMatchRankBadge.setText(isBn ? "২য় স্থান 🥈" : "2nd Place 🥈");
            holder.tvMatchRankBadge.setBackgroundResource(R.drawable.bg_badge_pill_teal);
            holder.tvMatchRankBadge.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else if (item.getRank() == 3) {
            holder.tvMatchRankBadge.setText(isBn ? "৩য় স্থান 🥉" : "3rd Place 🥉");
            holder.tvMatchRankBadge.setBackgroundResource(R.drawable.bg_badge_pill);
            holder.tvMatchRankBadge.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
        } else {
            holder.tvMatchRankBadge.setText(isBn ? (BengaliNumberUtil.toBengali(item.getRank()) + "ম স্থান") : ("Rank " + item.getRank()));
            holder.tvMatchRankBadge.setBackgroundResource(R.drawable.bg_badge_pill);
            holder.tvMatchRankBadge.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        }

        // 4. Score
        holder.tvMatchScoreLabel.setText(isBn ? "স্কোর" : "Score");
        holder.tvMatchScoreValue.setText(isBn ? BengaliNumberUtil.toBengali(item.getScore()) : String.valueOf(item.getScore()));

        // 5. Accuracy / Correct vs Wrong
        holder.tvMatchAccuracyLabel.setText(isBn ? "সঠিক / ভুল" : "Correct / Wrong");
        int correct = item.getCorrectAnswers();
        int wrong = item.getWrongAnswers();
        if (isBn) {
            holder.tvMatchAccuracyValue.setText(BengaliNumberUtil.toBengali(correct) + " সঠিক • " + BengaliNumberUtil.toBengali(wrong) + " ভুল");
        } else {
            holder.tvMatchAccuracyValue.setText(correct + " Correct • " + wrong + " Wrong");
        }

        // 6. XP
        holder.tvMatchXpLabel.setText(isBn ? "অর্জিত XP" : "Earned XP");
        int xp = item.getDeenXpEarned();
        holder.tvMatchXpValue.setText(isBn ? ("+" + BengaliNumberUtil.toBengali(xp) + " XP") : ("+" + xp + " XP"));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivMatchCategoryIcon;
        final TextView tvMatchCategoryTitle;
        final TextView tvMatchRoomCodeDate;
        final TextView tvMatchRankBadge;
        final TextView tvMatchScoreLabel;
        final TextView tvMatchScoreValue;
        final TextView tvMatchAccuracyLabel;
        final TextView tvMatchAccuracyValue;
        final TextView tvMatchXpLabel;
        final TextView tvMatchXpValue;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMatchCategoryIcon = itemView.findViewById(R.id.ivMatchCategoryIcon);
            tvMatchCategoryTitle = itemView.findViewById(R.id.tvMatchCategoryTitle);
            tvMatchRoomCodeDate = itemView.findViewById(R.id.tvMatchRoomCodeDate);
            tvMatchRankBadge = itemView.findViewById(R.id.tvMatchRankBadge);
            tvMatchScoreLabel = itemView.findViewById(R.id.tvMatchScoreLabel);
            tvMatchScoreValue = itemView.findViewById(R.id.tvMatchScoreValue);
            tvMatchAccuracyLabel = itemView.findViewById(R.id.tvMatchAccuracyLabel);
            tvMatchAccuracyValue = itemView.findViewById(R.id.tvMatchAccuracyValue);
            tvMatchXpLabel = itemView.findViewById(R.id.tvMatchXpLabel);
            tvMatchXpValue = itemView.findViewById(R.id.tvMatchXpValue);
        }
    }
}
