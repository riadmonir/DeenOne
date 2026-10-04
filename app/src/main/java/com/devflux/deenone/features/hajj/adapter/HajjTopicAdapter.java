package com.devflux.deenone.features.hajj.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.databinding.ItemHajjTopicCardBinding;
import com.devflux.deenone.features.hajj.HajjAcceptancePageDialog;
import com.devflux.deenone.features.hajj.HajjArafatPageDialog;
import com.devflux.deenone.features.hajj.HajjHistoryPageDialog;
import com.devflux.deenone.features.hajj.HajjIhramPageDialog;
import com.devflux.deenone.features.hajj.HajjMuzdalifahPageDialog;
import com.devflux.deenone.features.hajj.HajjTawafPageDialog;
import com.devflux.deenone.features.hajj.HajjTopicArticlePageDialog;
import com.devflux.deenone.features.hajj.HajjTopicDetailDialog;
import com.devflux.deenone.features.hajj.data.HajjArticleContentRepository;
import com.devflux.deenone.features.hajj.model.HajjTopicItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class HajjTopicAdapter extends RecyclerView.Adapter<HajjTopicAdapter.TopicViewHolder> {

    public static class HajjPalette {
        public final int tintColor;
        public final int darkBgColor;
        public final int lightBgColor;

        public HajjPalette(String tintHex, String darkBgHex, String lightBgHex) {
            this.tintColor = android.graphics.Color.parseColor(tintHex);
            this.darkBgColor = android.graphics.Color.parseColor(darkBgHex);
            this.lightBgColor = android.graphics.Color.parseColor(lightBgHex);
        }
    }

    public static final HajjPalette[] PALETTES = new HajjPalette[]{
        new HajjPalette("#34D399", "#0E3827", "#E6F9F0"), // Emerald Green
        new HajjPalette("#FBBF24", "#382810", "#FEF3C7"), // Warm Amber / Gold
        new HajjPalette("#22D3EE", "#103338", "#E0F7FA"), // Vivid Cyan
        new HajjPalette("#F472B6", "#381028", "#FCE7F3"), // Rose / Ruby
        new HajjPalette("#38BDF8", "#102738", "#E0F2FE"), // Sky Blue
        new HajjPalette("#C084FC", "#291238", "#F3E8FF"), // Purple / Amethyst
        new HajjPalette("#FB923C", "#381F0A", "#FFEDD5"), // Sunset Orange
        new HajjPalette("#2DD4BF", "#103833", "#CCFBF1"), // Mint / Teal
        new HajjPalette("#F59E0B", "#38260E", "#FEF3C7"), // Radiant Gold
        new HajjPalette("#818CF8", "#102B38", "#EEF2FF"), // Indigo / Iris
        new HajjPalette("#35D99B", "#122E2B", "#D1FAE5"), // Spring Teal
        new HajjPalette("#FB7185", "#38121A", "#FFE4E6")  // Coral Pink
    };

    public static HajjPalette getPaletteForTopic(int topicId, int position) {
        int idx = (topicId > 0 ? (topicId - 1) : position) % PALETTES.length;
        if (idx < 0) idx = 0;
        return PALETTES[idx];
    }

    private final Context context;
    private final List<HajjTopicItem> topics;
    private final boolean isBn;

    public HajjTopicAdapter(Context context, List<HajjTopicItem> topics, boolean isBn) {
        this.context = context;
        this.topics = topics;
        this.isBn = isBn;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHajjTopicCardBinding binding = ItemHajjTopicCardBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new TopicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        HajjTopicItem item = topics.get(position);
        holder.binding.ivHajjTopicIcon.setImageResource(item.getIconResId());
        holder.binding.tvHajjTopicTitle.setText(item.getTitle(isBn));

        boolean isNight = (context.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES;
        HajjPalette palette = getPaletteForTopic(item.getId(), position);

        holder.binding.ivHajjTopicIcon.setImageTintList(android.content.res.ColorStateList.valueOf(palette.tintColor));

        android.graphics.drawable.GradientDrawable circleBg = new android.graphics.drawable.GradientDrawable();
        circleBg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        circleBg.setColor(isNight ? palette.darkBgColor : palette.lightBgColor);
        holder.binding.flHajjTopicIconContainer.setBackground(circleBg);

        holder.binding.cardHajjTopic.setOnClickListener(v -> {
            if (context instanceof Activity && item.getId() == 3) {
                HajjHistoryPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 5) {
                com.devflux.deenone.features.hajj.HajjFardWajibPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 7) {
                com.devflux.deenone.features.hajj.HajjHolyPlacesPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 8) {
                com.devflux.deenone.features.hajj.HajjDuaPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 9) {
                HajjAcceptancePageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 10) {
                HajjIhramPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 11) {
                HajjArafatPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 13) {
                HajjMuzdalifahPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 15) {
                HajjTawafPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 16) {
                com.devflux.deenone.features.hajj.HajjMiqatPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 19) {
                com.devflux.deenone.features.hajj.HajjDhulHijjahAmalPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 21) {
                com.devflux.deenone.features.hajj.HajjUmrahPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 23) {
                com.devflux.deenone.features.hajj.HajjZiyaratMadinaPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 24) {
                com.devflux.deenone.features.hajj.HajjMistakesPageDialog.show((Activity) context);
            } else if (context instanceof Activity && item.getId() == 29) {
                com.devflux.deenone.features.hajj.HajjJourneyPageDialog.show((Activity) context);
            } else if (context instanceof Activity && HajjArticleContentRepository.hasArticleCardsForTopic(item.getId())) {
                HajjTopicArticlePageDialog.show((Activity) context, item.getId(), item.getTitle(isBn));
            } else {
                HajjTopicDetailDialog.show(context, item, isBn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics != null ? topics.size() : 0;
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        final ItemHajjTopicCardBinding binding;

        TopicViewHolder(ItemHajjTopicCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
