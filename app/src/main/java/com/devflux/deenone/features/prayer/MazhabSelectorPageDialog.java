package com.devflux.deenone.features.prayer;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.prayer.PrayerSettingsManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageMazhabSelectorBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;

public class MazhabSelectorPageDialog {

    public interface OnMazhabSavedListener {
        void onMazhabSaved(PrayerSettingsManager.JuristicMethod juristicMethod);
    }

    public static void show(@NonNull Context context) {
        show(context, null);
    }

    public static void show(@NonNull Context context, OnMazhabSavedListener listener) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        PageMazhabSelectorBinding binding = PageMazhabSelectorBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        if (isBn) {
            binding.tvMazhabSelectorTitle.setText("মাযহাব নির্বাচন");
            binding.tvMazhabSelectorSubtitle.setText("আসরের ওয়াক্ত ও মাযহাব পদ্ধতি নির্ধারণ");
            binding.tvAsrCalculationHeader.setText("আসরের ছায়ার পদ্ধতি");
            binding.tvAsrCalculationDesc.setText("হানাফি মাযহাবে কোনো বস্তুর মূল ছায়া বাদে তার ছায়া দ্বিগুণ (২ গুণ) হলে আসরের ওয়াক্ত শুরু হয়।\n\nশাফেয়ী, মালেকী ও হাম্বলী মাযহাবে মূল ছায়া বাদে বস্তুর ছায়া সমান (১ গুণ) হলেই আসরের ওয়াক্ত শুরু হয়। এই নির্বাচনটি আপনার পুরো অ্যাপে আসরের ওয়াক্তের সময় স্বয়ংক্রিয়ভাবে নির্ধারণ করবে।");
            binding.tvOptionShafiTitle.setText("১ গুণ ছায়া পদ্ধতি");
            binding.tvOptionShafiSubtitle.setText("শাফেয়ী, মালেকী ও হাম্বলী");
            binding.tvOptionHanafiTitle.setText("২ গুণ ছায়া পদ্ধতি");
            binding.tvOptionHanafiSubtitle.setText("হানাফি মাযহাব");
            binding.btnCancelMazhab.setText("বাতিল");
            binding.btnSaveMazhab.setText("সংরক্ষণ করুন");
            binding.btnBackMazhab.setContentDescription("ফিরে যান");
        } else {
            binding.tvMazhabSelectorTitle.setText("Juristic Method");
            binding.tvMazhabSelectorSubtitle.setText("Asr Prayer & Juristic Method Setup");
            binding.tvAsrCalculationHeader.setText("Asr Calculation System");
            binding.tvAsrCalculationDesc.setText("In the Hanafi school, Asr begins when the shadow of an object becomes twice its length plus the noon shadow.\n\nIn the Shafi'i, Maliki, and Hanbali schools, Asr begins when the shadow of an object equals its length plus the noon shadow. This selection automatically updates Asr prayer times throughout the app.");
            binding.tvOptionShafiTitle.setText("1x Shadow System");
            binding.tvOptionShafiSubtitle.setText("Shafi'i, Maliki & Hanbali");
            binding.tvOptionHanafiTitle.setText("2x Shadow System");
            binding.tvOptionHanafiSubtitle.setText("Hanafi Madhhab");
            binding.btnCancelMazhab.setText("Cancel");
            binding.btnSaveMazhab.setText("Save");
            binding.btnBackMazhab.setContentDescription("Back");
        }

        TouchAnimationUtil.attachTouchSpring(binding.btnBackMazhab);
        TouchAnimationUtil.attachTouchSpring(binding.btnCancelMazhab);
        TouchAnimationUtil.attachTouchSpring(binding.btnSaveMazhab);

        PrayerSettingsManager.JuristicMethod currentJuristic = PrayerSettingsManager.getJuristicMethod(context);
        final PrayerSettingsManager.JuristicMethod[] selectedJuristic = new PrayerSettingsManager.JuristicMethod[]{currentJuristic};

        Runnable updateUI = () -> {
            boolean isHanafi = selectedJuristic[0] == PrayerSettingsManager.JuristicMethod.HANAFI;
            binding.ivRadioHanafi.setImageResource(isHanafi ? R.drawable.bg_radio_selected : R.drawable.bg_radio_unselected);
            binding.ivRadioShafi.setImageResource(!isHanafi ? R.drawable.bg_radio_selected : R.drawable.bg_radio_unselected);
        };

        updateUI.run();

        binding.cardMazhabHanafi.setOnClickListener(v -> {
            selectedJuristic[0] = PrayerSettingsManager.JuristicMethod.HANAFI;
            updateUI.run();
        });

        binding.cardMazhabShafi.setOnClickListener(v -> {
            selectedJuristic[0] = PrayerSettingsManager.JuristicMethod.SHAFI;
            updateUI.run();
        });

        binding.btnBackMazhab.setOnClickListener(v -> dialog.dismiss());
        binding.btnCancelMazhab.setOnClickListener(v -> dialog.dismiss());

        binding.btnSaveMazhab.setOnClickListener(v -> {
            PrayerSettingsManager.setJuristicMethod(context, selectedJuristic[0]);

            // Notify app-wide prayer settings change
            PrayerSettingsManager.notifyPrayerSettingsChanged(context);

            if (listener != null) {
                listener.onMazhabSaved(selectedJuristic[0]);
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
