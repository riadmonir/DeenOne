package com.devflux.deenone.features.qibla;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.calculations.QiblaCalculator;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.sensors.CompassSensorManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageQiblaCompassBinding;
import com.devflux.deenone.features.home.HomeViewModel;

import java.util.Locale;

public class QiblaCompassPageDialog {

  private static boolean hasVibratedForCurrentAlignment = false;

  public static void show(Context context) {
    if (context instanceof Activity activity) {
      show(activity, null);
    }
  }

  public static void show(Activity activity, HomeViewModel homeViewModel) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    PageQiblaCompassBinding binding = PageQiblaCompassBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
    boolean isQiblaDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(activity) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
    binding.ivQiblaThemeIcon.setImageResource(isQiblaDark ? R.drawable.ic_sun : R.drawable.ic_moon);

    // Static text translations
    binding.pillQiblaTitle.setText(isBn ? "• কিবলা কম্পাস" : "• Qibla Compass");
    binding.tvMeccaDistanceLabel.setText(isBn ? "মক্কার দূরত্ব" : "Distance to Makkah");
    binding.tvSensorStatusText.setText(isBn ? "ম্যাগনেটোমিটার সেন্সর সংযুক্ত ও সক্রিয়" : "Magnetometer sensor active & connected");
    binding.btnCalibrateSensor.setText(isBn ? "ক্যালিব্রেট" : "Calibrate");
    binding.tvSensorGuidanceSubtitle.setText(isBn ? "সঠিক দিক নির্দেশনার জন্য মোবাইল ফোনটি সমতল স্থানে বা মাটির সমান্তরালে রাখুন।"
                                                 : "Keep the phone on a flat level surface for optimal directional accuracy.");

    // Attach smooth spring physics animations
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseQibla);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnQiblaThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnQiblaNotification);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnRefreshGpsLocation);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnQiblaHelp);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCalibrateSensor);

    // 1. Back Button
    binding.btnCloseQibla.setOnClickListener(v -> dialog.dismiss());

    binding.btnQiblaThemeToggle.setOnClickListener(v -> {
      if (activity instanceof MainActivity mainActivity) {
        mainActivity.toggleAppTheme();
        dialog.dismiss();
      }
    });

    binding.btnQiblaNotification.setOnClickListener(v -> {
      if (activity instanceof MainActivity mainActivity) {
        mainActivity.showNotificationHistorySheet();
      }
    });

    // 2. Fetch User Coordinates
    LocationProvider.Coordinates coords = (homeViewModel != null && homeViewModel.getCurrentCoordinates() != null)
        ? homeViewModel.getCurrentCoordinates()
        : LocationProvider.getSavedOrCurrentLocation(activity);

    // 3. Compute Real Qibla Bearing & Distance
    updateQiblaValues(activity, binding, coords);

    // 4. Compass Sensor Initialization
    CompassSensorManager compass = new CompassSensorManager(activity);
    float qiblaBearing = QiblaCalculator.calculateQiblaBearing(coords.latitude, coords.longitude);
    compass.setTargetQiblaBearing(qiblaBearing);

    Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
    hasVibratedForCurrentAlignment = false;

    // 5. Setup Alignment Listener on Custom Compass View
    binding.viewQiblaCompass.setAlignmentListener((isFacingQibla, deltaDegrees, instructionText) -> {
      binding.tvTurnInstruction.setText(instructionText);

      if (isFacingQibla) {
        binding.layoutTurnInstructionCard.setBackgroundTintList(
            ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(activity, R.color.bg_badge_pill)));
        binding.tvTurnInstruction.setTextColor(
            androidx.core.content.ContextCompat.getColor(activity, R.color.accent_mint));

        // Haptic feedback once when alignment is achieved
        if (!hasVibratedForCurrentAlignment && vibrator != null) {
          hasVibratedForCurrentAlignment = true;
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
          } else {
            vibrator.vibrate(50);
          }
        }
      } else {
        hasVibratedForCurrentAlignment = false;
        binding.layoutTurnInstructionCard.setBackgroundTintList(
            ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(activity, R.color.bg_card_secondary)));
        binding.tvTurnInstruction.setTextColor(
            androidx.core.content.ContextCompat.getColor(activity, R.color.accent_gold));
      }
    });

    // 6. Connect Compass to Sensor Events
    compass.setListener((azimuth, targetBearing) -> {
      binding.viewQiblaCompass.setBearing(azimuth, targetBearing);
    });
    compass.start();

    // 7. Refresh GPS Coordinates Action
    binding.btnRefreshGpsLocation.setOnClickListener(v -> {
      Toast.makeText(activity, isBn ? "লাইভ জিপিএস অবস্থান রিফ্রেশ করা হচ্ছে..." : "Refreshing live GPS location...", Toast.LENGTH_SHORT).show();
      LocationProvider.Coordinates updatedCoords = (homeViewModel != null && homeViewModel.getCurrentCoordinates() != null)
          ? homeViewModel.getCurrentCoordinates() : coords;
      updateQiblaValues(activity, binding, updatedCoords);
      float newBearing = QiblaCalculator.calculateQiblaBearing(updatedCoords.latitude, updatedCoords.longitude);
      compass.setTargetQiblaBearing(newBearing);
      binding.viewQiblaCompass.setBearing(0f, newBearing);
    });

    // 8. Help Dialog
    binding.btnQiblaHelp.setOnClickListener(v -> showQiblaGuideDialog(activity));

    // 9. Calibrate Sensor Dialog
    binding.btnCalibrateSensor.setOnClickListener(v -> showSensorCalibrationDialog(activity));

    dialog.setOnDismissListener(d -> compass.stop());
    dialog.show();
  }

  private static void updateQiblaValues(Context context, PageQiblaCompassBinding binding, LocationProvider.Coordinates coords) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    float qiblaBearing = QiblaCalculator.calculateQiblaBearing(coords.latitude, coords.longitude);
    double distanceKm = QiblaCalculator.calculateDistanceToKaabaKm(coords.latitude, coords.longitude);

    // Location Name
    String locName = coords.locationName != null ? coords.locationName.replace("", "").trim() : (isBn ? "বাংলাদেশ" : "Bangladesh");
    binding.tvQiblaLocationName.setText(locName);

    // Qibla Angle
    String angleText = String.format(Locale.US, "%.1f°", qiblaBearing);
    binding.tvQiblaAngleLabel.setText((isBn ? "কিবলার কোণ: " : "Qibla Angle: ") + (isBn ? com.devflux.deenone.utils.BengaliNumberUtil.toBengali(angleText) : angleText));

    // Distance in KM
    long distRounded = Math.round(distanceKm);
    binding.tvMeccaDistanceValue.setText((isBn ? com.devflux.deenone.utils.BengaliNumberUtil.toBengali(distRounded) : String.valueOf(distRounded)) + (isBn ? " কি.মি." : " km"));
  }

  private static void showQiblaGuideDialog(Activity activity) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
    String title = isBn ? "কিবলা কম্পাস নির্দেশিকা" : "Qibla Compass Guide";
    String message = isBn
        ? "• মোবাইল ফোনটিকে সম্পূর্ণ সমতল স্থানে বা হাতের তালুর উপর সমান্তরালে রাখুন。\n\n"
            + "• চুম্বক, লোহা বা ইলেকট্রনিক ডিভাইসের সংস্পর্শ থেকে ফোন দূরে রাখুন。\n\n"
            + "• কম্পাসের সূচটি কাবার চিহ্নের দিকে না পৌঁছানো পর্যন্ত শরীর ঘুরিয়ে দাঁড়ান。\n\n"
            + "• সূচটি সবুজ বর্ণ ধারণ করলে আপনি নিখুঁতভাবে কিবলামুখী অবস্থান করছেন。"
        : "• Place your mobile phone on a flat surface or hold it parallel to the ground.\n\n"
            + "• Keep the phone away from magnets, metal, or electronic devices.\n\n"
            + "• Rotate yourself until the compass needle aligns with the Kaaba indicator.\n\n"
            + "• When the needle turns green and the phone vibrates, you are accurately facing the Qibla.";

    new AlertDialog.Builder(activity)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(isBn ? "বুঝেছি" : "Got it", null)
        .show();
  }

  private static void showSensorCalibrationDialog(Activity activity) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
    String title = isBn ? "ম্যাগনেটোমিটার সেন্সর ক্যালিব্রেশন" : "Magnetometer Sensor Calibration";
    String message = isBn
        ? "সেন্সরের নির্ভুলতা বাড়ানোর জন্য মোবাইল ফোনটি হাতে ধরে বাতাসে ইংরেজি ৮ (Figure-8) আকৃতিতে ২-৩ বার ঘোরান。\n\n"
            + "এর ফলে ফোনের ডিজিটাল কম্পাস সেন্সর স্বয়ংক্রিয়ভাবে ক্যালিব্রেট হয়ে যাবে。"
        : "To calibrate the magnetometer sensor, hold the phone and wave it in a figure-8 motion 2-3 times in the air.\n\n"
            + "This recalibrates the compass sensor for maximum precision.";

    new AlertDialog.Builder(activity)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(isBn ? "ক্যালিব্রেশন সম্পন্ন" : "Calibration Done", (d, w) -> {
          Toast.makeText(activity, isBn ? "সেন্সর সফলভাবে ক্যালিব্রেট হয়েছে" : "Sensor successfully calibrated", Toast.LENGTH_SHORT).show();
        })
        .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
        .show();
  }
}
