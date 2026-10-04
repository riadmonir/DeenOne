package com.devflux.deenone.features.hajj.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devflux.deenone.core.ui.loading.DeenOneLoadingDrawable;
import com.devflux.deenone.databinding.ItemHajjJourneyCardBinding;
import com.devflux.deenone.features.hajj.HajjArafatPageDialog;
import com.devflux.deenone.features.hajj.HajjHolyPlacesPageDialog;
import com.devflux.deenone.features.hajj.HajjIhramPageDialog;
import com.devflux.deenone.features.hajj.HajjJourneyDetailDialog;
import com.devflux.deenone.features.hajj.HajjTopicArticlePageDialog;
import com.devflux.deenone.features.hajj.model.HajjJourneyStage;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class HajjJourneyTimelineAdapter extends RecyclerView.Adapter<HajjJourneyTimelineAdapter.StageViewHolder> {

    private final Context context;
    private final List<HajjJourneyStage> stages;
    private final boolean isBn;

    public HajjJourneyTimelineAdapter(Context context, List<HajjJourneyStage> stages, boolean isBn) {
        this.context = context;
        this.stages = stages;
        this.isBn = isBn;
    }

    @NonNull
    @Override
    public StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHajjJourneyCardBinding binding = ItemHajjJourneyCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new StageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StageViewHolder holder, int position) {
        HajjJourneyStage stage = stages.get(position);
        int starColor = Color.parseColor(stage.getStarColorHex());

        // Top Connector line is always visible to maintain continuous timeline down the 50% center
        holder.binding.lineConnectorTop.setVisibility(View.VISIBLE);

        if (stage.isEven()) {
            // Even Stage: Text on Left (0-50%), Spine in Center (50%), Image on Right (50-100%)
            holder.binding.layoutEvenStage.setVisibility(View.VISIBLE);
            holder.binding.layoutOddStage.setVisibility(View.GONE);

            holder.binding.tvStageTitleEven.setText(stage.getTitle(isBn));
            holder.binding.tvStageDescEven.setText(stage.getDescription(isBn));
            
            // Ultra-smooth Glide loading with 60 FPS DeenOne spinner & automatic offline disk caching
            String imgUrlEven = stage.getImageUrl(context);
            DeenOneLoadingDrawable placeholderEven = new DeenOneLoadingDrawable(context, false, true);
            Glide.with(context)
                    .load(imgUrlEven)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholderEven)
                    .error(stage.getImageResId())
                    .into(holder.binding.ivStageImageEven);

            holder.binding.tvStageNumberEven.setText(stage.getStageNumber(isBn));

            holder.binding.ivStarRosetteEven.setImageTintList(ColorStateList.valueOf(starColor));
            holder.binding.lineVerticalTopEven.setBackgroundColor(starColor);
            holder.binding.lineVerticalMiddleEven.setBackgroundColor(starColor);

            // Left curve line asset: hajj_line_left*.png
            holder.binding.ivHajjLineLeft.setImageResource(stage.getLineDrawableResId());

            holder.binding.tvBtnDetailsEven.setText(isBn ? "বিস্তারিত" : "Details");

            TouchAnimationUtil.attachTouchSpring(holder.binding.btnDetailsEven);
            holder.binding.btnDetailsEven.setOnClickListener(v -> navigateToStageDetails(stage));
        } else {
            // Odd Stage: Image on Left (0-50%), Spine in Center (50%), Text on Right (50-100%)
            holder.binding.layoutOddStage.setVisibility(View.VISIBLE);
            holder.binding.layoutEvenStage.setVisibility(View.GONE);

            holder.binding.tvStageTitleOdd.setText(stage.getTitle(isBn));
            holder.binding.tvStageDescOdd.setText(stage.getDescription(isBn));
            
            // Ultra-smooth Glide loading with 60 FPS DeenOne spinner & automatic offline disk caching
            String imgUrlOdd = stage.getImageUrl(context);
            DeenOneLoadingDrawable placeholderOdd = new DeenOneLoadingDrawable(context, false, true);
            Glide.with(context)
                    .load(imgUrlOdd)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholderOdd)
                    .error(stage.getImageResId())
                    .into(holder.binding.ivStageImageOdd);

            holder.binding.tvStageNumberOdd.setText(stage.getStageNumber(isBn));

            holder.binding.ivStarRosetteOdd.setImageTintList(ColorStateList.valueOf(starColor));
            holder.binding.lineVerticalTopOdd.setBackgroundColor(starColor);
            holder.binding.lineVerticalMiddleOdd.setBackgroundColor(starColor);

            // Right curve line asset: hajj_line_right*.png
            holder.binding.ivHajjLineRight.setImageResource(stage.getLineDrawableResId());

            holder.binding.tvBtnDetailsOdd.setText(isBn ? "বিস্তারিত" : "Details");

            TouchAnimationUtil.attachTouchSpring(holder.binding.btnDetailsOdd);
            holder.binding.btnDetailsOdd.setOnClickListener(v -> navigateToStageDetails(stage));
        }

        holder.binding.cardStageItem.setOnClickListener(v -> navigateToStageDetails(stage));
    }

    /**
     * Seamlessly routes the user to existing project features/dialogs if available for the stage;
     * otherwise opens the comprehensive HajjJourneyDetailDialog with authentic dua, rituals, and references.
     */
    private void navigateToStageDetails(HajjJourneyStage stage) {
        Activity activity = (context instanceof Activity) ? (Activity) context : null;
        int stageNum = stage.getStageNumber();

        if (activity != null) {
            if (stageNum == 1) {
                // Stage 01: ইহরাম (Ihram) -> Existing HajjIhramPageDialog
                HajjIhramPageDialog.show(activity);
                return;
            } else if (stageNum == 2) {
                // Stage 02: তাওয়াফ (Tawaf al-Qudum) -> HajjTawafPageDialog
                com.devflux.deenone.features.hajj.HajjTawafPageDialog.show(activity);
                return;
            } else if (stageNum == 3) {
                // Stage 03: সাফা ও মারওয়া (Safa & Marwa) -> Existing HajjHolyPlacesPageDialog (covers Safa, Marwah, Mas'a)
                HajjHolyPlacesPageDialog.show(activity);
                return;
            } else if (stageNum == 4) {
                // Stage 04: মিনা (Mina) -> Existing Mina Article Cards in HajjTopicArticlePageDialog (topicId 12)
                HajjTopicArticlePageDialog.show(activity, 12, stage.getTitle(isBn));
                return;
            } else if (stageNum == 5) {
                // Stage 05: আরাফাত (Arafat) -> Existing HajjArafatPageDialog
                HajjArafatPageDialog.show(activity);
                return;
            } else if (stageNum == 6) {
                // Stage 06: মুজদালিফা (Muzdalifah) -> HajjMuzdalifahPageDialog
                com.devflux.deenone.features.hajj.HajjMuzdalifahPageDialog.show(activity);
                return;
            } else if (stageNum == 8) {
                // Stage 08: আদহি (কোরবানির পশু) -> HajjTopicArticlePageDialog
                HajjTopicArticlePageDialog.show(activity, 8, stage.getTitle(isBn));
                return;
            } else if (stageNum == 9) {
                // Stage 09: চুল কাটা বা কামানো -> HajjTopicArticlePageDialog
                HajjTopicArticlePageDialog.show(activity, 9, stage.getTitle(isBn));
                return;
            } else if (stageNum == 10) {
                // Stage 10: তাওয়াফুল ইফাদাহ -> HajjTopicArticlePageDialog
                HajjTopicArticlePageDialog.show(activity, 10, stage.getTitle(isBn));
                return;
            } else if (stageNum == 11) {
                // Stage 11: জামরাতে পাথর নিক্ষেপ -> HajjTopicArticlePageDialog
                HajjTopicArticlePageDialog.show(activity, 11, stage.getTitle(isBn));
                return;
            } else if (stageNum == 12) {
                // Stage 12: বিদায়ী তাওয়াফ -> HajjTawafPageDialog
                com.devflux.deenone.features.hajj.HajjTawafPageDialog.show(activity, 13, stage.getTitle(isBn));
                return;
            }
        }

        // Standard / Default Detail Dialog for stage with authentic Dua, Transliteration, Meaning, and Hadith references
        HajjJourneyDetailDialog.show(context, stage);
    }

    @Override
    public int getItemCount() {
        return stages != null ? stages.size() : 0;
    }

    static class StageViewHolder extends RecyclerView.ViewHolder {
        final ItemHajjJourneyCardBinding binding;

        StageViewHolder(ItemHajjJourneyCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
