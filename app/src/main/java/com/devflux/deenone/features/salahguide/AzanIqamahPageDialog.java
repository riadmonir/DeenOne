package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.view.LayoutInflater;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageAzanIqamahHubBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AzanIqamahPageDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageAzanIqamahHubBinding binding = PageAzanIqamahHubBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "আজান ও ইকামত" : "Adhan & Iqamah");

        // Card Titles
        binding.tvTitleAzanMeaning.setText(isBn ? "আজান ও ইকামতের অর্থ" : "Meaning of Adhan & Iqamah");
        binding.tvTitleAzanDuaRules.setText(isBn ? "আজানের দোয়া ও নিয়ম" : "Duas & Rules of Adhan");
        binding.tvTitleAzanAnswer.setText(isBn ? "আজানের উত্তর" : "Responding to Adhan");
        binding.tvTitleAzanSunnahMustahab.setText(isBn ? "আজানের সুন্নত ও মুস্তাহাব সমূহ" : "Sunnahs & Recommended Acts of Adhan");

        // Back Button with Spring Touch
        binding.btnBackAzanIqamah.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackAzanIqamah);

        // Settings Action Button
        binding.btnSettingsAzanIqamah.setOnClickListener(v -> {
            showInfoDialog(activity, isBn);
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnSettingsAzanIqamah);

        // 1. আজান ও ইকামতের অর্থ
        binding.cardAzanMeaning.setOnClickListener(v -> {
            AzanMeaningPageDialog.show(activity);
        });

        // 2. আজানের দোয়া ও নিয়ম
        binding.cardAzanDuaRules.setOnClickListener(v -> {
            AzanDuaRulesPageDialog.show(activity);
        });

        // 3. আজানের উত্তর
        binding.cardAzanAnswer.setOnClickListener(v -> {
            AzanAnswerPageDialog.show(activity);
        });

        // 4. আজানের সুন্নত ও মুস্তাহাব সমূহ
        binding.cardAzanSunnahMustahab.setOnClickListener(v -> {
            AzanSunnahMustahabPageDialog.show(activity);
        });

        dialog.show();
    }

    private static void showInfoDialog(Activity activity, boolean isBn) {
        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "আজান ও ইকামত" : "Adhan & Iqamah")
                .setMessage(isBn ? "আজান হলো নামাজের জন্য আল্লাহর পক্ষ থেকে শ্রেষ্ঠ আহ্বান। আজানের জবাব দেওয়া এবং আজানের পর দোয়া পাঠ করা বিশেষ সওয়াবের কাজ।" : "Adhan is the noble call to prayer from Allah. Responding to Adhan and supplicating afterward carries immense spiritual reward.")
                .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                .show();
    }
}
