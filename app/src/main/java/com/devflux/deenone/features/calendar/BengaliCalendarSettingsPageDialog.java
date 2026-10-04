package com.devflux.deenone.features.calendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.core.calendar.CalendarSettingsManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageBengaliCalendarSettingsBinding;
import com.devflux.deenone.utils.BengaliCalendarUtil;

import java.util.Calendar;

/**
 * Controller for Bengali Calendar (বঙ্গাব্দ) Settings page dialog.
 * Allows toggling Bengali Calendar ON/OFF and selecting Calculation Method:
 * 1. Bangla Academy (Bangladesh)
 * 2. Indian Drik Siddhanta (West Bengal, India)
 */
public class BengaliCalendarSettingsPageDialog {

    public interface OnBengaliCalendarSettingsSavedListener {
        void onSaved(boolean enabled, BengaliCalendarUtil.CalculationMethod method);
    }

    public static void show(@NonNull Activity activity) {
        show(activity, null);
    }

    public static void show(@NonNull Activity activity, OnBengaliCalendarSettingsSavedListener listener) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageBengaliCalendarSettingsBinding binding = PageBengaliCalendarSettingsBinding.inflate(
                LayoutInflater.from(activity)
        );
        dialog.setContentView(binding.getRoot());

        // Load current persisted values
        final boolean[] isEnabled = {CalendarSettingsManager.isBengaliCalendarEnabled(activity)};
        final BengaliCalendarUtil.CalculationMethod[] selectedMethod = {
                CalendarSettingsManager.getBengaliCalculationMethod(activity)
        };

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
        binding.tvBengaliCalendarTopTitle.setText(isBn ? "বঙ্গাব্দ ক্যালেন্ডার" : "Bengali Calendar");
        binding.tvToggleBengaliCalendarTitle.setText(isBn ? "বঙ্গাব্দ ক্যালেন্ডার" : "Bengali Calendar");
        binding.tvToggleBengaliCalendarSubtitle.setText(isBn ? "বাংলা ক্যালেন্ডার প্রদর্শন চালু বা বন্ধ করুন" : "Enable or disable Bengali calendar");
        binding.tvCalculationMethodSectionTitle.setText(isBn ? "গণনা পদ্ধতি নির্বাচন" : "Choose Calculation Method");
        binding.tvCalculationMethodSectionSubtitle.setText(isBn ? "আপনার অঞ্চল অনুযায়ী বাংলা ক্যালেন্ডার গণনা পদ্ধতি নির্বাচন করুন।" : "Select Bengali calendar calculation method based on your region.");
        binding.tvLivePreviewLabel.setText(isBn ? "আজকের বাংলা তারিখ" : "Today's Bengali Date");
        binding.tvMethodBanglaAcademyTitle.setText(isBn ? "বাংলা একাডেমি" : "Bangla Academy");
        binding.tvMethodBanglaAcademySubtitle.setText(isBn ? "বাংলাদেশ" : "Bangladesh");
        binding.tvMethodIndianDrikTitle.setText(isBn ? "ভারতীয় দৃক সিদ্ধান্ত" : "Indian Drik Siddhanta");
        binding.tvMethodIndianDrikSubtitle.setText(isBn ? "পশ্চিমবঙ্গ, ভারত" : "West Bengal, India");
        binding.btnCancelBengaliCalendar.setText(isBn ? "বাতিল" : "Cancel");
        binding.btnSaveBengaliCalendar.setText(isBn ? "সংরক্ষণ করুন" : "Save");

        // Initialize UI State
        binding.switchBengaliCalendar.setChecked(isEnabled[0]);
        updateMethodSelectionUi(binding, activity, isEnabled[0], selectedMethod[0]);

        // Toggle Switch Listener
        binding.switchBengaliCalendar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isEnabled[0] = isChecked;
            updateMethodSelectionUi(binding, activity, isEnabled[0], selectedMethod[0]);
        });

        // Top Card Click also toggles the switch
        binding.cardToggleBengaliCalendar.setOnClickListener(v -> {
            binding.switchBengaliCalendar.setChecked(!binding.switchBengaliCalendar.isChecked());
        });

        // Method 1: Bangla Academy
        binding.cardBanglaAcademy.setOnClickListener(v -> {
            if (!isEnabled[0]) return;
            selectedMethod[0] = BengaliCalendarUtil.CalculationMethod.BANGLA_ACADEMY;
            updateMethodSelectionUi(binding, activity, isEnabled[0], selectedMethod[0]);
        });

        // Method 2: Indian Drik Siddhanta
        binding.cardIndianDrikSiddhanta.setOnClickListener(v -> {
            if (!isEnabled[0]) return;
            selectedMethod[0] = BengaliCalendarUtil.CalculationMethod.INDIAN_DRIK_SIDDHANTA;
            updateMethodSelectionUi(binding, activity, isEnabled[0], selectedMethod[0]);
        });

        // Back / Cancel Buttons
        binding.btnBackBengaliCalendar.setOnClickListener(v -> dialog.dismiss());
        binding.btnCancelBengaliCalendar.setOnClickListener(v -> dialog.dismiss());

        // Save Button
        binding.btnSaveBengaliCalendar.setOnClickListener(v -> {
            CalendarSettingsManager.setBengaliCalendarEnabled(activity, isEnabled[0]);
            CalendarSettingsManager.setBengaliCalculationMethod(activity, selectedMethod[0]);

            if (listener != null) {
                listener.onSaved(isEnabled[0], selectedMethod[0]);
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private static void updateMethodSelectionUi(
            PageBengaliCalendarSettingsBinding binding,
            Context context,
            boolean isEnabled,
            BengaliCalendarUtil.CalculationMethod selectedMethod
    ) {
        // Update Section Enabled/Disabled UI
        if (isEnabled) {
            binding.layoutCalculationMethodSection.setAlpha(1.0f);
            binding.cardBanglaAcademy.setEnabled(true);
            binding.cardIndianDrikSiddhanta.setEnabled(true);
            binding.cardBanglaAcademy.setFocusable(true);
            binding.cardIndianDrikSiddhanta.setFocusable(true);
        } else {
            binding.layoutCalculationMethodSection.setAlpha(0.38f);
            binding.cardBanglaAcademy.setEnabled(false);
            binding.cardIndianDrikSiddhanta.setEnabled(false);
            binding.cardBanglaAcademy.setFocusable(false);
            binding.cardIndianDrikSiddhanta.setFocusable(false);
        }

        // Update Radio Indicators
        boolean isBanglaAcademy = (selectedMethod == BengaliCalendarUtil.CalculationMethod.BANGLA_ACADEMY);
        binding.ivRadioBanglaAcademy.setImageResource(
                isBanglaAcademy ? R.drawable.bg_radio_selected : R.drawable.bg_radio_unselected
        );
        binding.ivRadioIndianDrikSiddhanta.setImageResource(
                !isBanglaAcademy ? R.drawable.bg_radio_selected : R.drawable.bg_radio_unselected
        );

        // Update Live Preview Date with Authentic Online Sync Engine
        Calendar now = Calendar.getInstance();
        BengaliCalendarUtil.BengaliDateResult preview =
                com.devflux.deenone.core.calendar.BengaliCalendarOnlineSyncEngine.getAuthenticBengaliDate(context, now, selectedMethod);
        binding.tvBengaliDateLivePreview.setText(preview.getFormattedDateWithSeason());

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        binding.tvBengaliOnlineSyncStatus.setText(
                selectedMethod == BengaliCalendarUtil.CalculationMethod.BANGLA_ACADEMY
                        ? (isBn ? "● বাংলা একাডেমি অনলাইন ডাটাবেজ দ্বারা যাচাইকৃত" : "● Verified by Bangla Academy online database")
                        : (isBn ? "● ভারতীয় দৃক সিদ্ধান্ত অনলাইন পঞ্জিকা দ্বারা যাচাইকৃত" : "● Verified by Indian Drik Siddhanta online panjika")
        );
    }
}
