package com.devflux.deenone.features.calendar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.calendar.CalendarSettingsManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageHijriDateSourceBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;

public class HijriDateSourcePageDialog {

    public interface OnHijriSourceSavedListener {
        void onHijriSourceSaved(CalendarSettingsManager.HijriDateSource source, int offsetDays);
    }

    public static void show(@NonNull Context context) {
        show(context, null);
    }

    public static void show(@NonNull Context context, OnHijriSourceSavedListener listener) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        PageHijriDateSourceBinding binding = PageHijriDateSourceBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        CalendarSettingsManager.HijriDateSource currentSource = CalendarSettingsManager.getHijriDateSource(context);
        // Default to DEENONE_MANAGED if not set or legacy
        if (currentSource != CalendarSettingsManager.HijriDateSource.ESTIMATED_MOON_CALCULATION
                && currentSource != CalendarSettingsManager.HijriDateSource.ASTRONOMICAL) {
            currentSource = CalendarSettingsManager.HijriDateSource.DEENONE_MANAGED;
        }

        final CalendarSettingsManager.HijriDateSource[] selectedSource = new CalendarSettingsManager.HijriDateSource[]{currentSource};

        int currentOffset = HijriCalendarUtil.getHijriAdjustment(context);
        final int[] selectedOffset = new int[]{currentOffset};

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

        // Top header and section titles
        binding.tvHijriDateSourceTopTitle.setText(isBn ? "হিজরি তারিখ" : "Hijri Date");
        binding.tvHijriDateSourceSectionTitle.setText(isBn ? "হিজরি তারিখ উৎস নির্বাচন" : "Choose Hijri Date Source");
        binding.tvHijriDateSourceSectionSubtitle.setText(isBn
                ? "সর্বোচ্চ নির্ভুলতার জন্য 'দীনওয়ান নিয়ন্ত্রিত' নির্বাচন করুন। অন্য অপশনটি গাণিতিক হিসাবের ওপর নির্ভরশীল।"
                : "Select 'Managed by DeenOne' for the most accurate Hijri dates. The other option uses mathematical calculations and may vary from actual moon sightings.");
        binding.tvTitleDeenone.setText(isBn ? "দীনওয়ান নিয়ন্ত্রিত" : "Managed by DeenOne");
        binding.tvSubtitleDeenone.setText(isBn ? "প্রস্তাবিত" : "Recommended");
        binding.tvTitleMoonPosition.setText(isBn ? "চাঁদের আনুমানিক অবস্থান" : "Estimated Moon Position");
        binding.tvSubtitleMoonPosition.setText(isBn ? "গণনাভিত্তিক" : "Calculation-based");
        binding.tvManualOffsetLabel.setText(isBn ? "ম্যানুয়াল হিজরি তারিখ সমন্বয়" : "Manual Hijri Date Adjustment");
        binding.tvManualOffsetDesc.setText(isBn
                ? "প্রয়োজনে হিজরি তারিখ ১ বা ২ দিন ম্যানুয়ালি যোগ বা বিয়োগ করতে পারেন।"
                : "You can manually adjust the Hijri date by +1/-1 or +2/-2 days if needed.");
        binding.btnCancelHijriSource.setText(isBn ? "বাতিল" : "Cancel");
        binding.btnSaveHijriSource.setText(isBn ? "সংরক্ষণ করুন" : "Save");

        int activeColor = ContextCompat.getColor(context, R.color.accent_mint);
        int inactiveColor = ContextCompat.getColor(context, R.color.border_card);

        Runnable updateUI = () -> {
            boolean isManaged = (selectedSource[0] == CalendarSettingsManager.HijriDateSource.DEENONE_MANAGED
                    || selectedSource[0] == CalendarSettingsManager.HijriDateSource.DEANONE_MANAGED);

            // 1. Managed by DeenOne Card
            binding.cardSourceDeenone.setStrokeColor(ColorStateList.valueOf(isManaged ? activeColor : inactiveColor));
            binding.cardSourceDeenone.setStrokeWidth(isManaged ? (int) (1.5f * context.getResources().getDisplayMetrics().density) : (int) (1.0f * context.getResources().getDisplayMetrics().density));
            binding.ivRadioDeenone.setImageResource(isManaged ? R.drawable.ic_radio_circle_checked : R.drawable.ic_radio_circle_empty);

            // 2. Estimated Moon Position Card
            binding.cardSourceMoonPosition.setStrokeColor(ColorStateList.valueOf(!isManaged ? activeColor : inactiveColor));
            binding.cardSourceMoonPosition.setStrokeWidth(!isManaged ? (int) (1.5f * context.getResources().getDisplayMetrics().density) : (int) (1.0f * context.getResources().getDisplayMetrics().density));
            binding.ivRadioMoonPosition.setImageResource(!isManaged ? R.drawable.ic_radio_circle_checked : R.drawable.ic_radio_circle_empty);

            // 3. Offset display
            int off = selectedOffset[0];
            String offsetText;
            if (isBn) {
                if (off == 0) {
                    offsetText = "০ দিন";
                } else if (off > 0) {
                    offsetText = "+" + BengaliNumberUtil.toBengali(off) + " দিন এগিয়ে";
                } else {
                    offsetText = "-" + BengaliNumberUtil.toBengali(Math.abs(off)) + " দিন পিছিয়ে";
                }
            } else {
                if (off == 0) {
                    offsetText = "0 Days";
                } else if (off > 0) {
                    offsetText = "+" + off + " Days Ahead";
                } else {
                    offsetText = "-" + Math.abs(off) + " Days Behind";
                }
            }
            binding.tvHijriOffsetDisplay.setText(offsetText);
        };

        updateUI.run();

        binding.cardSourceDeenone.setOnClickListener(v -> {
            selectedSource[0] = CalendarSettingsManager.HijriDateSource.DEENONE_MANAGED;
            updateUI.run();
        });

        binding.cardSourceMoonPosition.setOnClickListener(v -> {
            selectedSource[0] = CalendarSettingsManager.HijriDateSource.ESTIMATED_MOON_CALCULATION;
            updateUI.run();
        });

        binding.btnHijriOffsetMinus.setOnClickListener(v -> {
            if (selectedOffset[0] > -2) {
                selectedOffset[0]--;
                updateUI.run();
            }
        });

        binding.btnHijriOffsetPlus.setOnClickListener(v -> {
            if (selectedOffset[0] < 2) {
                selectedOffset[0]++;
                updateUI.run();
            }
        });

        binding.btnBackHijriSource.setOnClickListener(v -> dialog.dismiss());
        binding.btnCancelHijriSource.setOnClickListener(v -> dialog.dismiss());

        binding.btnSaveHijriSource.setOnClickListener(v -> {
            CalendarSettingsManager.setHijriDateSource(context, selectedSource[0]);
            HijriCalendarUtil.setHijriAdjustment(context, selectedOffset[0]);

            if (listener != null) {
                listener.onHijriSourceSaved(selectedSource[0], selectedOffset[0]);
            }

            if (context instanceof MainActivity activity) {
                if (activity.getViewModel() != null) {
                    activity.getViewModel().updateRealTimeCalculations();
                }
            }

            dialog.dismiss();
        });

        dialog.show();
    }
}
