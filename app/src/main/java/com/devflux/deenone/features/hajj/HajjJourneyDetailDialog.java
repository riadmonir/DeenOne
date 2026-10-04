package com.devflux.deenone.features.hajj;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.loading.DeenOneLoadingDrawable;
import com.devflux.deenone.databinding.DialogHajjJourneyDetailBinding;
import com.devflux.deenone.features.hajj.model.HajjJourneyStage;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class HajjJourneyDetailDialog {

    public static void show(@NonNull Context context, @NonNull HajjJourneyStage stage) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        DialogHajjJourneyDetailBinding binding = DialogHajjJourneyDetailBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        // Header & Badge
        String stageLabel = isBn ? ("ধাপ " + stage.getStageNumber(true)) : ("Stage " + stage.getStageNumber(false));
        binding.tvDetailStageNumber.setText(stageLabel);
        try {
            binding.tvDetailStageNumber.setBackgroundColor(Color.parseColor(stage.getStarColorHex()));
        } catch (Throwable ignored) {}

        binding.tvDetailTitle.setText(stage.getTitle(isBn));

        // Ultra-smooth Glide loading with 60 FPS DeenOne spinner & automatic offline disk caching
        String heroImageUrl = stage.getImageUrl(context);
        DeenOneLoadingDrawable placeholderHero = new DeenOneLoadingDrawable(context, true, true);
        Glide.with(context)
                .load(heroImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholderHero)
                .error(stage.getImageResId())
                .into(binding.ivDetailHeroImage);

        binding.tvDetailCoreDesc.setText(stage.getDescription(isBn));

        // Rituals Section
        binding.tvSectionRitualsHeader.setText(isBn ? "শরিয়ত সম্মত নিয়মাবলী ও আহকাম" : "Prescribed Rituals & Shariah Guidelines");
        binding.tvDetailRitualsContent.setText(stage.getDetails(isBn));

        // Dua Section
        if (stage.getDuaArabic() != null && !stage.getDuaArabic().isEmpty()) {
            binding.cardDetailDua.setVisibility(View.VISIBLE);
            binding.tvDuaHeader.setText(isBn ? "এই ধাপের গুরুত্বপূর্ণ দোয়া ও যিকির" : "Essential Supplication & Dhikr");
            binding.tvDetailDuaArabic.setText(stage.getDuaArabic());

            String pron = stage.getDuaPronunciation(isBn);
            if (pron != null && !pron.isEmpty()) {
                binding.tvDetailDuaPronunciation.setVisibility(View.VISIBLE);
                binding.tvDetailDuaPronunciation.setText((isBn ? "উচ্চারণ: " : "Pronunciation: ") + pron);
            } else {
                binding.tvDetailDuaPronunciation.setVisibility(View.GONE);
            }

            binding.tvDetailDuaMeaning.setText((isBn ? "অর্থ: " : "Meaning: ") + stage.getDuaMeaning(isBn));
            binding.tvDetailDuaReference.setText(stage.getReference());
        } else {
            binding.cardDetailDua.setVisibility(View.GONE);
        }

        // Action Buttons
        binding.btnDismissDetail.setText(isBn ? "সম্পন্ন" : "Done");
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseDetail);
        TouchAnimationUtil.attachTouchSpring(binding.btnDismissDetail);

        binding.btnCloseDetail.setOnClickListener(v -> dialog.dismiss());
        binding.btnDismissDetail.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
