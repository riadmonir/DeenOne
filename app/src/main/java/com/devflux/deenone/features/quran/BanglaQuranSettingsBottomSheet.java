package com.devflux.deenone.features.quran;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.BanglaQuranManager;
import com.devflux.deenone.databinding.BottomSheetBanglaQuranSettingsBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BanglaQuranSettingsBottomSheet {

    public interface OnSettingsChangeListener {
        void onSettingsChanged();
    }

    public static void show(Context context, OnSettingsChangeListener listener) {
        if (context == null) return;

        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.DeenOneBottomSheetDialog);
        BottomSheetBanglaQuranSettingsBinding binding = BottomSheetBanglaQuranSettingsBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);
        BanglaQuranManager manager = BanglaQuranManager.getInstance(context);

        // Titles
        binding.tvSettingsSheetTitle.setText(isBn ? "কুরআন বাংলা সেটিংস" : "Bangla Quran Settings");
        binding.tvSettingsSheetSubtitle.setText(isBn ? "ফন্ট সাইজ ও আরবি দৃশ্যমানতা নির্ধারণ করুন" : "Adjust font sizes and Arabic visibility");
        binding.tvShowArabicTitle.setText(isBn ? "উসমানী আরবি টেক্সট দেখুন" : "Show Uthmani Arabic Text");
        binding.tvShowArabicSubtitle.setText(isBn
                ? "অনুবাদ পড়ার সাথে সাথে মূল আরবি আয়াত প্রদর্শিত থাকবে"
                : "Display original Arabic verse above the Bengali translation");
        binding.tvArabicFontLabel.setText(isBn ? "আরবি ফন্ট সাইজ" : "Arabic Font Size");
        binding.tvBanglaFontLabel.setText(isBn ? "বাংলা অনুবাদ ফন্ট সাইজ" : "Bangla Translation Font Size");

        // Initial Values
        binding.switchShowArabic.setChecked(manager.isArabicShown());
        binding.cardArabicFontSize.setVisibility(manager.isArabicShown() ? View.VISIBLE : View.GONE);

        int currentArabicPct = Math.round(manager.getArabicFontScale() * 100);
        binding.sliderArabicFontSize.setValue(Math.max(70, Math.min(160, currentArabicPct)));
        binding.tvArabicFontPercentage.setText(isBn ? (BengaliNumberUtil.toBengali(currentArabicPct) + "%") : (currentArabicPct + "%"));
        binding.tvSampleArabicPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0f * manager.getArabicFontScale());

        int currentBanglaSp = manager.getBanglaFontSizeSp();
        binding.sliderBanglaFontSize.setValue(Math.max(12, Math.min(24, currentBanglaSp)));
        binding.tvBanglaFontSizeBadge.setText(isBn ? (BengaliNumberUtil.toBengali(currentBanglaSp) + " sp") : (currentBanglaSp + " sp"));
        binding.tvSampleBanglaPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentBanglaSp);

        // Arabic Switch Listener
        binding.switchShowArabic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            manager.setArabicShown(isChecked);
            binding.cardArabicFontSize.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (listener != null) listener.onSettingsChanged();
        });

        // Arabic Font Slider
        binding.sliderArabicFontSize.addOnChangeListener((slider, value, fromUser) -> {
            int pct = (int) value;
            float scale = pct / 100.0f;
            manager.setArabicFontScale(scale);
            binding.tvArabicFontPercentage.setText(isBn ? (BengaliNumberUtil.toBengali(pct) + "%") : (pct + "%"));
            binding.tvSampleArabicPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0f * scale);
            if (listener != null) listener.onSettingsChanged();
        });

        // Bangla Font Slider
        binding.sliderBanglaFontSize.addOnChangeListener((slider, value, fromUser) -> {
            int sp = (int) value;
            manager.setBanglaFontSizeSp(sp);
            binding.tvBanglaFontSizeBadge.setText(isBn ? (BengaliNumberUtil.toBengali(sp) + " sp") : (sp + " sp"));
            binding.tvSampleBanglaPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
            if (listener != null) listener.onSettingsChanged();
        });

        // Touch spring ONLY on buttons
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseSettings);
        binding.btnCloseSettings.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
