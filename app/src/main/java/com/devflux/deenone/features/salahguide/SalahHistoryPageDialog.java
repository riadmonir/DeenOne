package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.view.LayoutInflater;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageSalahHistoryBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SalahHistoryPageDialog {

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahHistoryBinding binding = PageSalahHistoryBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title
        binding.tvHeaderTitle.setText(isBn ? "নামাজের ইতিহাস" : "History of Salah");

        // Card Titles
        binding.tvTitleFiveWaqts.setText(isBn ? "পাঁচ ওয়াক্ত নামাজের ইতিহাস" : "History of Five Daily Prayers");
        binding.tvTitleWhenStarted.setText(isBn ? "মুসলমানরা কখন থেকে নামাজ পড়া শুরু করে" : "When Muslims Started Praying");
        binding.tvTitleWhyFiveTimes.setText(isBn ? "নামাজের জন্য পাঁচটি সময় কেন নির্ধারিত" : "Why Five Prayer Times Are Set");
        binding.tvTitleFiftyToFive.setText(isBn ? "পঞ্চাশ ওয়াক্ত নামাজের ইতিহাস" : "History of Fifty to Five Prayers");
        binding.tvTitleFiftyReward.setText(isBn ? "পাঁচ ওয়াক্ত নামাজ পড়লেই পঞ্চাশ ওয়াক্তের সাওয়াব" : "Reward of Fifty Prayers by Praying Five");
        binding.tvTitleProphetsSalah.setText(isBn ? "কোন নবীর উপর কোন নামাজ ফরজ হয়েছিল" : "Which Prayer Was Prescribed to Which Prophet");

        // Back Button
        binding.btnBackSalahHistory.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahHistory);

        // Info / Settings Button
        binding.btnInfoSalahHistory.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(activity)
                    .setTitle(isBn ? "নামাজের ইতিহাস" : "History of Salah")
                    .setMessage(isBn ? "নামাজের প্রামাণ্য ইতিহাস ও তাৎপর্য বিষয়ক নির্ভরযোগ্য ইসলামিক তথ্যকোষ।" : "Authentic and reliable Islamic compendium on the history and significance of Salah.")
                    .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                    .show();
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnInfoSalahHistory);

        // 1. পাঁচ ওয়াক্ত নামাজের ইতিহাস
        binding.cardFiveWaqtsHistory.setOnClickListener(v -> {
            FiveWaqtHistoryPageDialog.show(activity);
        });

        // 2. মুসলমানরা কখন থেকে নামাজ পড়া শুরু করে
        binding.cardWhenStartedHistory.setOnClickListener(v -> {
            WhenStartedHistoryPageDialog.show(activity);
        });

        // 3. নামাজের জন্য পাঁচটি সময় কেন নির্ধারিত
        binding.cardWhyFiveTimesHistory.setOnClickListener(v -> {
            WhyFiveTimesHistoryPageDialog.show(activity);
        });

        // 4. পঞ্চাশ ওয়াক্ত নামাজের ইতিহাস
        binding.cardFiftyToFiveHistory.setOnClickListener(v -> {
            FiftyToFiveHistoryPageDialog.show(activity);
        });

        // 5. পাঁচ ওয়াক্ত নামাজ পড়লেই পঞ্চাশ ওয়াক্তের সাওয়াব
        binding.cardFiftyRewardHistory.setOnClickListener(v -> {
            FiftyRewardHistoryPageDialog.show(activity);
        });

        // 6. কোন নবীর উপর কোন নামাজ ফরজ হয়েছিল
        binding.cardProphetsSalahHistory.setOnClickListener(v -> {
            ProphetsSalahHistoryPageDialog.show(activity);
        });

        dialog.show();
    }
}
