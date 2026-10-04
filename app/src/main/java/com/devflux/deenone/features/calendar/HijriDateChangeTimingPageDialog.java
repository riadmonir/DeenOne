package com.devflux.deenone.features.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.calendar.CalendarSettingsManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageHijriDateChangeTimingBinding;

public class HijriDateChangeTimingPageDialog {

    public interface OnHijriTimingSavedListener {
        void onHijriTimingSaved(CalendarSettingsManager.HijriChangeTiming timing);
    }

    public static void show(@NonNull Context context) {
        show(context, null);
    }

    public static void show(@NonNull Context context, OnHijriTimingSavedListener listener) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        PageHijriDateChangeTimingBinding binding = PageHijriDateChangeTimingBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        CalendarSettingsManager.HijriChangeTiming currentTiming = CalendarSettingsManager.getHijriChangeTiming(context);
        final CalendarSettingsManager.HijriChangeTiming[] selectedTiming = new CalendarSettingsManager.HijriChangeTiming[]{currentTiming};

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

        // Language-adaptive text labels
        binding.tvHijriTimingTopTitle.setText(isBn ? "হিজরি রোলওভার" : "Hijri Rollover");
        binding.tvHijriTimingSectionTitle.setText(isBn ? "হিজরি তারিখ পরিবর্তনের সময়" : "Hijri Date Change Timing");
        binding.tvHijriTimingSectionSubtitle.setText(isBn ? "প্রতিদিন কোন সময়ে হিজরি তারিখ পরিবর্তিত হবে তা নির্বাচন করুন।" : "This setting controls when the Hijri date rolls over daily.");
        binding.tvTitleMaghrib.setText(isBn ? "সূর্যাস্তের পর" : "After Sunset");
        binding.tvSubtitleMaghrib.setText(isBn ? "স্থানীয় মাগরিবের ওয়াক্তে দিন পরিবর্তন" : "Advances at local Maghrib");
        binding.tvTitleMidnight.setText(isBn ? "মধ্যরাতে" : "At Midnight");
        binding.tvSubtitleMidnight.setText(isBn ? "রাত ১২:০০ টায় দিন পরিবর্তন" : "Advances at 00:00 local time");
        binding.btnCancelHijriTiming.setText(isBn ? "বাতিল" : "Cancel");
        binding.btnSaveHijriTiming.setText(isBn ? "সংরক্ষণ করুন" : "Save");

        int activeStrokeColor = ContextCompat.getColor(context, R.color.accent_mint);
        int defaultStrokeColor = ContextCompat.getColor(context, R.color.border_card);

        Runnable updateSelectionUI = () -> {
            boolean isMidnight = selectedTiming[0] == CalendarSettingsManager.HijriChangeTiming.MIDNIGHT_12AM;

            // Midnight option
            binding.ivCheckMidnight.setVisibility(isMidnight ? View.VISIBLE : View.GONE);
            binding.cardTimingMidnight.setStrokeColor(isMidnight ? activeStrokeColor : defaultStrokeColor);

            // Maghrib option
            binding.ivCheckMaghrib.setVisibility(!isMidnight ? View.VISIBLE : View.GONE);
            binding.cardTimingMaghrib.setStrokeColor(!isMidnight ? activeStrokeColor : defaultStrokeColor);
        };

        updateSelectionUI.run();

        binding.cardTimingMidnight.setOnClickListener(v -> {
            selectedTiming[0] = CalendarSettingsManager.HijriChangeTiming.MIDNIGHT_12AM;
            updateSelectionUI.run();
        });

        binding.cardTimingMaghrib.setOnClickListener(v -> {
            selectedTiming[0] = CalendarSettingsManager.HijriChangeTiming.MAGHRIB_SUNSET;
            updateSelectionUI.run();
        });

        binding.btnBackHijriTiming.setOnClickListener(v -> dialog.dismiss());
        binding.btnCancelHijriTiming.setOnClickListener(v -> dialog.dismiss());

        binding.btnSaveHijriTiming.setOnClickListener(v -> {
            CalendarSettingsManager.setHijriChangeTiming(context, selectedTiming[0]);

            if (listener != null) {
                listener.onHijriTimingSaved(selectedTiming[0]);
            }

            if (context instanceof MainActivity) {
                MainActivity activity = (MainActivity) context;
                if (activity.getViewModel() != null) {
                    activity.getViewModel().updateRealTimeCalculations();
                }
            }

            dialog.dismiss();
        });

        dialog.show();
    }
}
