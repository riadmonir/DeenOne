package com.devflux.deenone.features.prayer;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.prayer.PrayerSettingsManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemSalahAdjustmentCardBinding;
import com.devflux.deenone.databinding.PageSalahTimeAdjustmentBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.PrayerCalculator;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SalahTimeAdjustmentPageDialog {

    public interface OnSalahAdjustmentSavedListener {
        void onSalahAdjustmentSaved();
    }

    public static void show(@NonNull Context context) {
        show(context, null);
    }

    public static void show(@NonNull Context context, OnSalahAdjustmentSavedListener listener) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        PageSalahTimeAdjustmentBinding binding = PageSalahTimeAdjustmentBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        binding.tvAdjustmentTitle.setText(isBn ? "সালাত সময় সমন্বয়" : "Salah Time Adjustment");
        binding.tvAdjustmentSubtitle.setText(isBn ? "নামাজের সময়সূচি ম্যানুয়াল সমন্বয়" : "Manual Prayer Schedule Adjustment");
        binding.btnResetAllOffsets.setText(isBn ? "রিসেট" : "Reset");
        binding.tvInfoCardTitle.setText(isBn ? "সালাত সময় সমন্বয়" : "Salah Time Adjustment");
        binding.tvInfoCardDesc.setText(isBn 
            ? "আপনার স্থানীয় মসজিদ বা এলাকার আজান ও জামাতের সময়ের সাথে হুবহু মেলাতে প্রতিটি ওয়াক্তের সময় মিনিট হিসেবে (+/- ৬০ মিনিট) আগে বা পরে নির্ধারণ করতে পারেন। এটি অ্যাপের ড্যাশবোর্ড, উইজেট, লকস্ক্রিন ও আজান অ্যালার্মে সরাসরি প্রযোজ্য হবে।"
            : "You can adjust each prayer time (+/- 60 minutes) to precisely match your local mosque or jamaat times. This applies directly across the dashboard, widgets, lock screen, and adhan alarms.");
        binding.btnCancelSalahAdjustment.setText(isBn ? "বাতিল" : "Cancel");
        binding.btnSaveSalahAdjustment.setText(isBn ? "পরিবর্তন সংরক্ষণ করুন" : "Save Changes");

        // Load base times (with 0 offsets) for live preview calculation
        LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
        Calendar cal = Calendar.getInstance();
        PrayerSettingsManager.CalculationMethod method = PrayerSettingsManager.getCalculationMethod(context);
        PrayerSettingsManager.JuristicMethod juristic = PrayerSettingsManager.getJuristicMethod(context);

        PrayerCalculator.PrayerTimesResult baseTimes = PrayerCalculator.calculateForLocationWithSettings(
                coords.latitude, coords.longitude, coords.timezone, cal,
                method, juristic,
                0, 0, 0, 0, 0, 0, 0
        );

        // Offsets state: [fajr, sunrise, dhuhr, jummah, asr, maghrib, isha]
        final int[] offsets = new int[]{
                PrayerSettingsManager.getOffsetMinutes(context, "fajr"),
                PrayerSettingsManager.getOffsetMinutes(context, "sunrise"),
                PrayerSettingsManager.getOffsetMinutes(context, "dhuhr"),
                PrayerSettingsManager.getOffsetMinutes(context, "jummah"),
                PrayerSettingsManager.getOffsetMinutes(context, "asr"),
                PrayerSettingsManager.getOffsetMinutes(context, "maghrib"),
                PrayerSettingsManager.getOffsetMinutes(context, "isha")
        };

        // Setup individual cards with glossy circular waqt artwork icons
        WaqtCardController fajrCard = new WaqtCardController(
                binding.layoutFajrAdjustment, isBn ? "ফজর" : "Fajr",
                R.drawable.ic_waqt_fajr, baseTimes.fajrMillis, offsets[0], isBn,
                newOffset -> offsets[0] = newOffset
        );

        WaqtCardController sunriseCard = new WaqtCardController(
                binding.layoutSunriseAdjustment, isBn ? "সূর্যোদয়" : "Sunrise",
                R.drawable.ic_waqt_sunrise, baseTimes.sunriseMillis, offsets[1], isBn,
                newOffset -> offsets[1] = newOffset
        );

        WaqtCardController dhuhrCard = new WaqtCardController(
                binding.layoutDhuhrAdjustment, isBn ? "যোহর" : "Dhuhr",
                R.drawable.ic_waqt_dhuhr, baseTimes.zohrMillis, offsets[2], isBn,
                newOffset -> offsets[2] = newOffset
        );

        WaqtCardController jummahCard = new WaqtCardController(
                binding.layoutJummahAdjustment, isBn ? "জুম'আ" : "Jummah",
                R.drawable.ic_waqt_jummah, baseTimes.jummahMillis, offsets[3], isBn,
                newOffset -> offsets[3] = newOffset
        );

        WaqtCardController asrCard = new WaqtCardController(
                binding.layoutAsrAdjustment, isBn ? "আসর" : "Asr",
                R.drawable.ic_waqt_asr, baseTimes.asrMillis, offsets[4], isBn,
                newOffset -> offsets[4] = newOffset
        );

        WaqtCardController maghribCard = new WaqtCardController(
                binding.layoutMaghribAdjustment, isBn ? "মাগরিব" : "Maghrib",
                R.drawable.ic_waqt_maghrib, baseTimes.maghribMillis, offsets[5], isBn,
                newOffset -> offsets[5] = newOffset
        );

        WaqtCardController ishaCard = new WaqtCardController(
                binding.layoutIshaAdjustment, isBn ? "এশা" : "Isha",
                R.drawable.ic_waqt_isha, baseTimes.ishaMillis, offsets[6], isBn,
                newOffset -> offsets[6] = newOffset
        );

        // Attach touch spring physics to top navigation and bottom action buttons
        TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahAdjustment);
        TouchAnimationUtil.attachTouchSpring(binding.btnResetAllOffsets);
        TouchAnimationUtil.attachTouchSpring(binding.btnCancelSalahAdjustment);
        TouchAnimationUtil.attachTouchSpring(binding.btnSaveSalahAdjustment);

        // Reset All Button
        binding.btnResetAllOffsets.setOnClickListener(v -> {
            for (int i = 0; i < offsets.length; i++) {
                offsets[i] = 0;
            }
            fajrCard.setOffset(0);
            sunriseCard.setOffset(0);
            dhuhrCard.setOffset(0);
            jummahCard.setOffset(0);
            asrCard.setOffset(0);
            maghribCard.setOffset(0);
            ishaCard.setOffset(0);
        });

        binding.btnBackSalahAdjustment.setOnClickListener(v -> dialog.dismiss());
        binding.btnCancelSalahAdjustment.setOnClickListener(v -> dialog.dismiss());

        binding.btnSaveSalahAdjustment.setOnClickListener(v -> {
            PrayerSettingsManager.setAllOffsets(
                    context,
                    offsets[0], offsets[1], offsets[2],
                    offsets[3], offsets[4], offsets[5], offsets[6]
            );

            if (listener != null) {
                listener.onSalahAdjustmentSaved();
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

    private interface OnOffsetChanged {
        void onOffsetChanged(int newOffset);
    }

    private static class WaqtCardController {
        private final ItemSalahAdjustmentCardBinding binding;
        private final long baseMillis;
        private final boolean isBn;
        private final OnOffsetChanged callback;
        private int currentOffset;
        private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        WaqtCardController(
                ItemSalahAdjustmentCardBinding binding,
                String title,
                int iconRes,
                long baseMillis,
                int initialOffset,
                boolean isBn,
                OnOffsetChanged callback
        ) {
            this.binding = binding;
            this.baseMillis = baseMillis;
            this.currentOffset = initialOffset;
            this.isBn = isBn;
            this.callback = callback;

            binding.tvWaqtTitle.setText(title);
            binding.ivWaqtIcon.setImageResource(iconRes);
            binding.tvOriginalTime.setText(isBn ? ("মূল সময়: " + formatTime(baseMillis)) : ("Original: " + formatTime(baseMillis)));
            binding.chipMinus10.setText(isBn ? "-১০মি" : "-10m");
            binding.chipMinus5.setText(isBn ? "-৫মি" : "-5m");
            binding.chipZero.setText(isBn ? "০মি" : "0m");
            binding.chipPlus5.setText(isBn ? "+৫মি" : "+5m");
            binding.chipPlus10.setText(isBn ? "+১০মি" : "+10m");
            binding.chipPlus15.setText(isBn ? "+১৫মি" : "+15m");
            binding.chipPlus20.setText(isBn ? "+২০মি" : "+20m");

            TouchAnimationUtil.attachTouchSpring(binding.btnMinus);
            TouchAnimationUtil.attachTouchSpring(binding.btnPlus);
            TouchAnimationUtil.attachTouchSpring(binding.chipMinus10);
            TouchAnimationUtil.attachTouchSpring(binding.chipMinus5);
            TouchAnimationUtil.attachTouchSpring(binding.chipZero);
            TouchAnimationUtil.attachTouchSpring(binding.chipPlus5);
            TouchAnimationUtil.attachTouchSpring(binding.chipPlus10);
            TouchAnimationUtil.attachTouchSpring(binding.chipPlus15);
            TouchAnimationUtil.attachTouchSpring(binding.chipPlus20);

            binding.btnMinus.setOnClickListener(v -> {
                if (currentOffset > -60) {
                    currentOffset--;
                    updateUI();
                    callback.onOffsetChanged(currentOffset);
                }
            });

            binding.btnPlus.setOnClickListener(v -> {
                if (currentOffset < 60) {
                    currentOffset++;
                    updateUI();
                    callback.onOffsetChanged(currentOffset);
                }
            });

            binding.chipMinus10.setOnClickListener(v -> setOffset(-10));
            binding.chipMinus5.setOnClickListener(v -> setOffset(-5));
            binding.chipZero.setOnClickListener(v -> setOffset(0));
            binding.chipPlus5.setOnClickListener(v -> setOffset(5));
            binding.chipPlus10.setOnClickListener(v -> setOffset(10));
            binding.chipPlus15.setOnClickListener(v -> setOffset(15));
            binding.chipPlus20.setOnClickListener(v -> setOffset(20));

            updateUI();
        }

        public void setOffset(int offset) {
            this.currentOffset = Math.max(-60, Math.min(60, offset));
            updateUI();
            callback.onOffsetChanged(currentOffset);
        }

        private void updateUI() {
            long adjustedMillis = baseMillis + ((long) currentOffset * 60 * 1000);
            binding.tvAdjustedTimePreview.setText(formatTime(adjustedMillis));
            binding.tvOriginalTime.setText(isBn ? ("মূল সময়: " + formatTime(baseMillis)) : ("Original: " + formatTime(baseMillis)));

            Context context = binding.getRoot().getContext();
            if (currentOffset == 0) {
                binding.tvOffsetBadge.setText(isBn ? "০ মিনিট (ডিফল্ট)" : "0 min (Default)");
                binding.tvOffsetBadge.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            } else if (currentOffset > 0) {
                String val = isBn ? ("+" + BengaliNumberUtil.toBengali(currentOffset) + " মিনিট") : ("+" + currentOffset + " min");
                binding.tvOffsetBadge.setText(val);
                binding.tvOffsetBadge.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
            } else {
                String val = isBn ? (BengaliNumberUtil.toBengali(currentOffset) + " মিনিট") : (currentOffset + " min");
                binding.tvOffsetBadge.setText(val);
                binding.tvOffsetBadge.setTextColor(ContextCompat.getColor(context, R.color.accent_gold));
            }
        }

        private String formatTime(long millis) {
            String str = timeFormat.format(new Date(millis));
            return isBn ? BengaliNumberUtil.toBengali(str) : str;
        }
    }
}
