package com.devflux.deenone.features.marriage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.data.model.MarriageTopicItem;
import com.devflux.deenone.features.marriage.MuslimMarriageContentFormatter;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MarriageTopicAdapter extends RecyclerView.Adapter<MarriageTopicAdapter.TopicViewHolder> {

    public interface OnTopicClickListener {
        void onTopicClick(MarriageTopicItem item, int position);
    }

    private final List<MarriageTopicItem> items = new ArrayList<>();
    private final OnTopicClickListener listener;

    public MarriageTopicAdapter(OnTopicClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<MarriageTopicItem> newItems) {
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_marriage_topic_card, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        MarriageTopicItem item = items.get(position);
        Context context = holder.itemView.getContext();
        boolean isBn = LocaleManager.isBengali(context);

        holder.tvTopicTitle.setText(isBn ? item.getTitleBn() : item.getTitleEn());

        if (item.isExpanded()) {
            holder.tvTopicSummary.setVisibility(View.GONE);
            holder.tvTopicFullContent.setVisibility(View.VISIBLE);
            holder.tvTopicFullContent.setText(MuslimMarriageContentFormatter.format(item, context));
            holder.tvToggleText.setText(isBn ? "সংক্ষিপ্ত করুন" : "Show Less");
            holder.ivToggleChevron.setRotation(180f);
        } else {
            holder.tvTopicSummary.setVisibility(View.VISIBLE);
            holder.tvTopicSummary.setText(isBn ? item.getSummaryBn() : item.getSummaryEn());
            holder.tvTopicFullContent.setVisibility(View.GONE);
            holder.tvToggleText.setText(isBn ? "বিস্তারিত দেখুন" : "View Details");
            holder.ivToggleChevron.setRotation(0f);
        }

        TouchAnimationUtil.attachTouchSpring(holder.layoutToggleExpand);

        View.OnClickListener toggleListener = v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && pos < items.size()) {
                MarriageTopicItem current = items.get(pos);
                current.setExpanded(!current.isExpanded());
                notifyItemChanged(pos);
            }
        };

        holder.cardMarriageTopic.setOnClickListener(toggleListener);
        holder.layoutToggleExpand.setOnClickListener(toggleListener);

        holder.cardMarriageTopic.setOnLongClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && pos < items.size() && listener != null) {
                listener.onTopicClick(items.get(pos), pos);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCardView cardMarriageTopic;
        public final TextView tvTopicTitle;
        public final TextView tvTopicSummary;
        public final TextView tvTopicFullContent;
        public final LinearLayout layoutToggleExpand;
        public final TextView tvToggleText;
        public final ImageView ivToggleChevron;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            cardMarriageTopic = itemView.findViewById(R.id.cardMarriageTopic);
            tvTopicTitle = itemView.findViewById(R.id.tvTopicTitle);
            tvTopicSummary = itemView.findViewById(R.id.tvTopicSummary);
            tvTopicFullContent = itemView.findViewById(R.id.tvTopicFullContent);
            layoutToggleExpand = itemView.findViewById(R.id.layoutToggleExpand);
            tvToggleText = itemView.findViewById(R.id.tvToggleText);
            ivToggleChevron = itemView.findViewById(R.id.ivToggleChevron);
        }
    }
}

