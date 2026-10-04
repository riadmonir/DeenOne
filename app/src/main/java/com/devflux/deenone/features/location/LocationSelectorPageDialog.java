package com.devflux.deenone.features.location;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.alarms.AlarmRescheduler;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.DialogIslamicPickerModalBinding;
import com.devflux.deenone.databinding.ItemIslamicSelectionRowBinding;
import com.devflux.deenone.databinding.PageLocationSelectorBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * LocationSelectorPageDialog — 100% Exact Dynamic Implementation.
 * Fully supports Light/Dark theme and Dual-Language (Bengali/English).
 */
public class LocationSelectorPageDialog {

  private static final String PREF_NAME = "deanone_location_prefs";
  private static final String KEY_MODE = "key_location_mode";
  private static final String KEY_TIMEZONE_ID = "key_timezone_id";

  public enum Mode {
    AUTO_GPS,
    TIMEZONE_ONLY,
    MANUAL
  }

  public static class CityItem {
    public final String nameBn;
    public final String nameEn;
    public final double latitude;
    public final double longitude;
    public final double timezoneOffset;
    public final String timezoneId;

    public CityItem(String nameBn, String nameEn, double latitude, double longitude, double timezoneOffset, String timezoneId) {
      this.nameBn = nameBn;
      this.nameEn = nameEn;
      this.latitude = latitude;
      this.longitude = longitude;
      this.timezoneOffset = timezoneOffset;
      this.timezoneId = timezoneId;
    }

    public String getName(boolean isBn) {
      return isBn ? nameBn : nameEn;
    }
  }

  public static class TimezoneItem {
    public final String displayNameBn;
    public final String displayNameEn;
    public final String id;
    public final double offsetHours;
    public final double latitude;
    public final double longitude;

    public TimezoneItem(String displayNameBn, String displayNameEn, String id, double offsetHours, double latitude, double longitude) {
      this.displayNameBn = displayNameBn;
      this.displayNameEn = displayNameEn;
      this.id = id;
      this.offsetHours = offsetHours;
      this.latitude = latitude;
      this.longitude = longitude;
    }

    public String getDisplayName(boolean isBn) {
      return isBn ? displayNameBn : displayNameEn;
    }
  }

  public static void show(@NonNull Context context) {
    show(context, null);
  }

  public static void show(@NonNull Context context, @Nullable Runnable onDismissCallback) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    PageLocationSelectorBinding binding = PageLocationSelectorBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    if (onDismissCallback != null) {
      dialog.setOnDismissListener(d -> onDismissCallback.run());
    }

    boolean isBn = LocaleManager.isBengali(context);

    // 1. Top Bar Navigation & Springs
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnBackLocationSelector);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseLocationSelector);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnLocationThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnLocationNotification);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.layoutTimezoneSelectorPill);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.layoutDistrictSelectorPill);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.layoutManualTimezonePill);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnSaveLocationSettings);

    binding.btnBackLocationSelector.setOnClickListener(v -> dialog.dismiss());
    binding.btnCloseLocationSelector.setOnClickListener(v -> dialog.dismiss());

    boolean isLocationDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(context) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
    binding.ivLocationThemeIcon.setImageResource(isLocationDark ? R.drawable.ic_sun : R.drawable.ic_moon);

    binding.btnLocationThemeToggle.setOnClickListener(v -> {
      if (context instanceof MainActivity) {
        ((MainActivity) context).toggleAppTheme();
        dialog.dismiss();
      }
    });

    binding.btnLocationNotification.setOnClickListener(v -> {
      if (context instanceof MainActivity) {
        ((MainActivity) context).showNotificationHistorySheet();
      }
    });

    // Dynamic Localization of Layout Headers & Labels
    binding.tvLivePreviewBadge.setText(isBn ? "● লাইভ প্রিভিউ" : "● LIVE PREVIEW");
    binding.tvSectionLocationModeTitle.setText(isBn ? "লোকেশন মোড সিলেক্ট করুন" : "Select Location Mode");
    binding.tvModeAutoTitle.setText(isBn ? "অটো ডিটেকশন" : "Auto Detection");
    binding.tvModeAutoSubtitle.setText(isBn ? "জিপিএস এবং রিয়েল-টাইম লোকেশন ব্যবহার করবে" : "Uses GPS and real-time location");
    binding.tvModeTimezoneTitle.setText(isBn ? "শুধুমাত্র টাইমজোন" : "Timezone Only");
    binding.tvModeTimezoneSubtitle.setText(isBn ? "আপনার এলাকার টাইমজোন অনুযায়ী আনুমানিক সময়" : "Approximate prayer times based on timezone");
    binding.tvModeManualTitle.setText(isBn ? "ম্যানুয়াল সিলেকশন" : "Manual Selection");
    binding.tvModeManualSubtitle.setText(isBn ? "নিজে থেকে স্থানাঙ্ক বা টাইমজোন নির্বাচন করবেন" : "Set custom coordinates or timezone manually");
    binding.tvGpsCoordinatesLabel.setText(isBn ? "জি-পি-এস স্থানাঙ্ক" : "GPS Coordinates");
    binding.tvGpsAutoTimezoneLabel.setText(isBn ? "টাইমজোন ড্রপডাউন" : "Timezone Dropdown");
    binding.tvTimezoneOnlyLabel.setText(isBn ? "টাইমজোন ড্রপডাউন" : "Timezone Dropdown");
    binding.tvPopularDistrictsLabel.setText(isBn ? "জনপ্রিয় জেলাসমূহ" : "Popular Districts");
    binding.tvSelectedDistrictName.setText(isBn ? "একটি জেলা সিলেক্ট করুন" : "Select a District");
    binding.tvGoogleMapsSearchLabel.setText(isBn ? "গুগল ম্যাপে লোকেশন খুঁজুন" : "Search Location on Map");
    binding.etManualSearchLocation.setHint(isBn ? "শহর বা এলাকার নাম লিখে অনুসন্ধান করুন..." : "Search city or area name...");
    binding.tvManualLatLabel.setText(isBn ? "অক্ষাংশ" : "Latitude");
    binding.tvManualLngLabel.setText(isBn ? "দ্রাঘিমাংশ" : "Longitude");
    binding.tvManualTimezoneLabel.setText(isBn ? "টাইমজোন ড্রপডাউন" : "Timezone Dropdown");
    binding.btnSaveLocationSettings.setText(isBn ? "সেটিংস সেভ করুন" : "Save Settings");
    binding.tvProTipsTitle.setText(isBn ? "প্রো টিপস" : "Pro Tips");
    binding.tvProTipsDesc.setText(isBn
        ? "সঠিক নামাজের সময়ের জন্য 'অটো ডিটেকশন' মোড সবচেয়ে কার্যকর। আপনি যদি দেশের বাইরে ভ্রমণে থাকেন, তবে এই মোড আপনার লোকেশন অনুযায়ী সময় অটো আপডেট করবে।"
        : "For the most accurate prayer times, 'Auto Detection' is recommended. If traveling abroad, it automatically updates prayer times for your current location.");

    // 2. Load Active Location & State
    LocationProvider.Coordinates savedCoords = LocationProvider.getSavedOrCurrentLocation(context);
    String savedModeStr = LocationProvider.getLocationMode(context);
    Mode currentMode;
    try {
      currentMode = Mode.valueOf(savedModeStr);
    } catch (Exception e) {
      currentMode = Mode.AUTO_GPS;
    }

    String activeTzId = LocationProvider.getSavedTimezoneId(context);

    final Mode[] selectedModeHolder = {currentMode};
    final double[] latHolder = {savedCoords.latitude};
    final double[] lngHolder = {savedCoords.longitude};
    final double[] tzOffsetHolder = {savedCoords.timezone};
    final String[] tzIdHolder = {activeTzId};
    final String[] locationNameHolder = {savedCoords.locationName};

    // 3. Setup Live 1-Second Ticking Clock
    Handler clockHandler = new Handler(Looper.getMainLooper());
    Runnable clockRunnable = new Runnable() {
      @Override
      public void run() {
        updateLiveClock(binding, tzIdHolder[0], isBn);
        clockHandler.postDelayed(this, 1000);
      }
    };
    clockHandler.post(clockRunnable);
    dialog.setOnDismissListener(d -> clockHandler.removeCallbacks(clockRunnable));

    // 4. Populate Initial Coordinate Displays
    updateGpsCoordinateCard(binding, latHolder[0], lngHolder[0]);
    binding.tvGpsAutoTimezoneName.setText(tzIdHolder[0]);
    binding.tvSelectedTimezoneName.setText(tzIdHolder[0]);
    binding.tvManualTimezoneName.setText(tzIdHolder[0]);

    String defaultLocName = isBn ? "বর্তমান অবস্থান" : "Current Location";
    String cleanLoc = savedCoords.locationName != null ? savedCoords.locationName.replace("", "").trim() : defaultLocName;
    if (currentMode == Mode.TIMEZONE_ONLY) {
      binding.tvLiveTimezoneBadge.setText(cleanLoc);
      binding.tvSelectedTimezoneName.setText(cleanLoc);
    } else {
      binding.tvLiveTimezoneBadge.setText(cleanLoc + " • " + tzIdHolder[0]);
    }

    binding.etManualLatitude.setText(String.format(Locale.US, "%.5f", latHolder[0]));
    binding.etManualLongitude.setText(String.format(Locale.US, "%.5f", lngHolder[0]));

    // 5. Setup Mode Switching UI
    applyModeSelection(binding, selectedModeHolder[0]);

    binding.cardModeAutoGps.setOnClickListener(v -> {
      selectedModeHolder[0] = Mode.AUTO_GPS;
      applyModeSelection(binding, Mode.AUTO_GPS);
      triggerGpsAutoDetection(context, binding, latHolder, lngHolder, tzOffsetHolder, tzIdHolder, locationNameHolder);
    });

    binding.cardModeTimezoneOnly.setOnClickListener(v -> {
      selectedModeHolder[0] = Mode.TIMEZONE_ONLY;
      applyModeSelection(binding, Mode.TIMEZONE_ONLY);
      binding.tvLiveTimezoneBadge.setText(locationNameHolder[0] != null ? locationNameHolder[0].replace("", "").trim() : tzIdHolder[0]);
      updateLiveClock(binding, tzIdHolder[0], isBn);
    });

    binding.cardModeManualSelection.setOnClickListener(v -> {
      selectedModeHolder[0] = Mode.MANUAL;
      applyModeSelection(binding, Mode.MANUAL);
      String manualLoc = locationNameHolder[0] != null ? locationNameHolder[0].replace("", "").trim() : (isBn ? "ম্যানুয়াল অবস্থান" : "Manual Location");
      binding.tvLiveTimezoneBadge.setText(manualLoc + " • " + tzIdHolder[0]);
      updateLiveClock(binding, tzIdHolder[0], isBn);
    });

    // 6. Timezone Only Dropdown Click
    binding.layoutTimezoneSelectorPill.setOnClickListener(v -> {
      showTimezonePickerDialog(context, tzIdHolder[0], selectedTz -> {
        tzIdHolder[0] = selectedTz.id;
        tzOffsetHolder[0] = selectedTz.offsetHours;
        latHolder[0] = selectedTz.latitude;
        lngHolder[0] = selectedTz.longitude;
        locationNameHolder[0] = selectedTz.getDisplayName(isBn);

        binding.tvSelectedTimezoneName.setText(selectedTz.getDisplayName(isBn));
        binding.tvLiveTimezoneBadge.setText(selectedTz.getDisplayName(isBn));

        updateLiveClock(binding, selectedTz.id, isBn);
      });
    });

    // 7. Manual Mode: District Dropdown Click
    List<CityItem> popularCities = getPopularCities();
    binding.layoutDistrictSelectorPill.setOnClickListener(v -> {
      showDistrictPickerDialog(context, popularCities, locationNameHolder[0], selectedCity -> {
        binding.tvSelectedDistrictName.setText(selectedCity.getName(isBn));
        binding.tvSelectedDistrictName.setTextColor(ContextCompat.getColor(context, R.color.text_primary));

        latHolder[0] = selectedCity.latitude;
        lngHolder[0] = selectedCity.longitude;
        tzOffsetHolder[0] = selectedCity.timezoneOffset;
        tzIdHolder[0] = selectedCity.timezoneId;
        locationNameHolder[0] = selectedCity.getName(isBn);

        binding.etManualLatitude.setText(String.format(Locale.US, "%.5f", selectedCity.latitude));
        binding.etManualLongitude.setText(String.format(Locale.US, "%.5f", selectedCity.longitude));
        binding.tvManualTimezoneName.setText(selectedCity.timezoneId);
        binding.tvLiveTimezoneBadge.setText(selectedCity.getName(isBn) + " • " + selectedCity.timezoneId);

        updateLiveClock(binding, selectedCity.timezoneId, isBn);
      });
    });

    // 8. Manual Mode: Search Box
    binding.etManualSearchLocation.setOnEditorActionListener((tv, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
        performGeocodingSearch(context, binding.etManualSearchLocation.getText().toString().trim(), binding,
            latHolder, lngHolder, tzOffsetHolder, tzIdHolder, locationNameHolder);
        return true;
      }
      return false;
    });

    // 9. Manual Mode: Timezone Dropdown Click
    binding.layoutManualTimezonePill.setOnClickListener(v -> {
      showTimezonePickerDialog(context, tzIdHolder[0], selectedTz -> {
        tzIdHolder[0] = selectedTz.id;
        tzOffsetHolder[0] = selectedTz.offsetHours;
        binding.tvManualTimezoneName.setText(selectedTz.id);
        String manualLoc = locationNameHolder[0] != null ? locationNameHolder[0].replace("", "").trim() : (isBn ? "ম্যানুয়াল অবস্থান" : "Manual Location");
        binding.tvLiveTimezoneBadge.setText(manualLoc + " • " + selectedTz.id);

        updateLiveClock(binding, selectedTz.id, isBn);
      });
    });

    // 10. Auto GPS Detection on First Launch ONLY if in Auto GPS mode
    if (currentMode == Mode.AUTO_GPS) {
      triggerGpsAutoDetection(context, binding, latHolder, lngHolder, tzOffsetHolder, tzIdHolder, locationNameHolder);
    }

    // 11. Save Button Logic
    binding.btnSaveLocationSettings.setOnClickListener(v -> {
      saveAndApplySettings(context, dialog, selectedModeHolder[0], binding,
          latHolder, lngHolder, tzOffsetHolder, tzIdHolder, locationNameHolder);
    });

    dialog.show();
  }

  private static void applyModeSelection(PageLocationSelectorBinding binding, Mode mode) {
    Context context = binding.getRoot().getContext();
    int activeBg = ContextCompat.getColor(context, R.color.bg_card_active);
    int inactiveBg = ContextCompat.getColor(context, R.color.bg_card);
    int activeStroke = ContextCompat.getColor(context, R.color.border_active);
    int inactiveStroke = ContextCompat.getColor(context, R.color.border_card);

    // Auto GPS Card
    boolean isAuto = (mode == Mode.AUTO_GPS);
    binding.cardModeAutoGps.setCardBackgroundColor(isAuto ? activeBg : inactiveBg);
    binding.cardModeAutoGps.setStrokeColor(isAuto ? activeStroke : inactiveStroke);
    binding.cardModeAutoGps.setStrokeWidth(isAuto ? 4 : 3);
    binding.ivModeAutoCheck.setVisibility(isAuto ? View.VISIBLE : View.GONE);
    binding.layoutAutoGpsSubView.setVisibility(isAuto ? View.VISIBLE : View.GONE);

    // Timezone Only Card
    boolean isTz = (mode == Mode.TIMEZONE_ONLY);
    binding.cardModeTimezoneOnly.setCardBackgroundColor(isTz ? activeBg : inactiveBg);
    binding.cardModeTimezoneOnly.setStrokeColor(isTz ? activeStroke : inactiveStroke);
    binding.cardModeTimezoneOnly.setStrokeWidth(isTz ? 4 : 3);
    binding.ivModeTimezoneCheck.setVisibility(isTz ? View.VISIBLE : View.GONE);
    binding.layoutTimezoneOnlySubView.setVisibility(isTz ? View.VISIBLE : View.GONE);

    // Manual Selection Card
    boolean isManual = (mode == Mode.MANUAL);
    binding.cardModeManualSelection.setCardBackgroundColor(isManual ? activeBg : inactiveBg);
    binding.cardModeManualSelection.setStrokeColor(isManual ? activeStroke : inactiveStroke);
    binding.cardModeManualSelection.setStrokeWidth(isManual ? 4 : 3);
    binding.ivModeManualCheck.setVisibility(isManual ? View.VISIBLE : View.GONE);
    binding.layoutManualSelectionSubView.setVisibility(isManual ? View.VISIBLE : View.GONE);
  }

  private static void triggerGpsAutoDetection(Context context, PageLocationSelectorBinding binding,
                        double[] latHolder, double[] lngHolder,
                        double[] tzOffsetHolder, String[] tzIdHolder,
                        String[] locationNameHolder) {
    boolean isBn = LocaleManager.isBengali(context);
    binding.tvLocationStatusMessage.setText(isBn ? "লাইভ জিপিএস লোকেশন সনাক্ত করা হচ্ছে..." : "Detecting live GPS location...");
    binding.ivStatusCheckIcon.setImageResource(R.drawable.ic_refresh);

    LocationProvider.requestCurrentGpsLocation(context, new LocationProvider.LocationCallback() {
      @Override
      public void onLocationResolved(LocationProvider.Coordinates coordinates) {
        new Handler(Looper.getMainLooper()).post(() -> {
          latHolder[0] = coordinates.latitude;
          lngHolder[0] = coordinates.longitude;
          tzOffsetHolder[0] = coordinates.timezone;
          locationNameHolder[0] = coordinates.locationName;

          String detectedTzId = TimeZone.getDefault().getID();
          if (detectedTzId != null && !detectedTzId.isEmpty()) {
            tzIdHolder[0] = detectedTzId;
          }

          updateGpsCoordinateCard(binding, coordinates.latitude, coordinates.longitude);
          binding.tvGpsAutoTimezoneName.setText(tzIdHolder[0]);

          String defaultLoc = isBn ? "বর্তমান অবস্থান" : "Current Location";
          String clean = coordinates.locationName != null ? coordinates.locationName.replace("", "").trim() : defaultLoc;
          binding.tvLiveTimezoneBadge.setText(clean + " • " + tzIdHolder[0]);

          binding.tvLocationStatusMessage.setText((isBn ? "জিপিএস লোকেশন সফলভাবে সনাক্ত করা হয়েছে: " : "GPS location detected successfully: ") + clean);
          binding.ivStatusCheckIcon.setImageResource(R.drawable.ic_check_circle);

          updateLiveClock(binding, tzIdHolder[0], isBn);
        });
      }

      @Override
      public void onLocationFailed(String errorReason, LocationProvider.Coordinates fallbackCoordinates) {
        new Handler(Looper.getMainLooper()).post(() -> {
          latHolder[0] = fallbackCoordinates.latitude;
          lngHolder[0] = fallbackCoordinates.longitude;
          tzOffsetHolder[0] = fallbackCoordinates.timezone;
          locationNameHolder[0] = fallbackCoordinates.locationName;

          updateGpsCoordinateCard(binding, fallbackCoordinates.latitude, fallbackCoordinates.longitude);
          String defaultLoc = isBn ? "ডিফল্ট অবস্থান" : "Default Location";
          String clean = fallbackCoordinates.locationName != null ? fallbackCoordinates.locationName.replace("", "").trim() : defaultLoc;
          binding.tvLocationStatusMessage.setText((isBn ? "লোকেশন সক্রিয়: " : "Location active: ") + clean);
          binding.tvLiveTimezoneBadge.setText(clean + " • " + tzIdHolder[0]);
          binding.ivStatusCheckIcon.setImageResource(R.drawable.ic_check_circle);

          updateLiveClock(binding, tzIdHolder[0], isBn);
        });
      }
    });
  }

  private static void updateGpsCoordinateCard(PageLocationSelectorBinding binding, double lat, double lng) {
    boolean isBn = LocaleManager.isBengali(binding.getRoot().getContext());
    if (isBn) {
      String latBn = BengaliNumberUtil.toBengali(String.format(Locale.US, "%.5f", lat));
      String lngBn = BengaliNumberUtil.toBengali(String.format(Locale.US, "%.5f", lng));
      binding.tvGpsLatVal.setText("অক্ষাংশ: " + latBn);
      binding.tvGpsLngVal.setText("দ্রাঘিমাংশ: " + lngBn);
    } else {
      String latEn = String.format(Locale.US, "%.5f", lat);
      String lngEn = String.format(Locale.US, "%.5f", lng);
      binding.tvGpsLatVal.setText("Latitude: " + latEn);
      binding.tvGpsLngVal.setText("Longitude: " + lngEn);
    }
  }

  private static void updateLiveClock(PageLocationSelectorBinding binding, String timezoneId, boolean isBn) {
    Calendar cal = Calendar.getInstance();
    if (timezoneId != null && !timezoneId.isEmpty()) {
      try {
        cal.setTimeZone(TimeZone.getTimeZone(timezoneId));
      } catch (Exception ignored) {}
    }

    int hour24 = cal.get(Calendar.HOUR_OF_DAY);
    int hour12 = cal.get(Calendar.HOUR);
    if (hour12 == 0) hour12 = 12;
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);

    if (isBn) {
      String period = (hour24 >= 12) ? "অপরাহ্ন" : "পূর্বাহ্ন";
      String timeStr = String.format(Locale.US, "%s %02d:%02d:%02d", period, hour12, min, sec);
      binding.tvLiveClockTime.setText(BengaliNumberUtil.toBengali(timeStr));

      String[] dayNames = {"রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার"};
      String[] monthNames = {"জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"};

      int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
      int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
      int month = cal.get(Calendar.MONTH);
      int year = cal.get(Calendar.YEAR);

      String dateStr = dayNames[dayOfWeek] + ", " + BengaliNumberUtil.toBengali(dayOfMonth) + " " + monthNames[month] + " " + BengaliNumberUtil.toBengali(year);
      binding.tvLiveClockDate.setText(dateStr);
    } else {
      String period = (hour24 >= 12) ? "PM" : "AM";
      String timeStr = String.format(Locale.US, "%02d:%02d:%02d %s", hour12, min, sec, period);
      binding.tvLiveClockTime.setText(timeStr);

      SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH);
      sdf.setTimeZone(cal.getTimeZone());
      binding.tvLiveClockDate.setText(sdf.format(cal.getTime()));
    }
  }

  private static void performGeocodingSearch(Context context, String query, PageLocationSelectorBinding binding,
                        double[] latHolder, double[] lngHolder, double[] tzOffsetHolder,
                        String[] tzIdHolder, String[] locationNameHolder) {
    boolean isBn = LocaleManager.isBengali(context);
    if (query.isEmpty()) {
      Toast.makeText(context, isBn ? "অনুগ্রহ করে শহর বা এলাকার নাম লিখুন" : "Please enter a city or area name", Toast.LENGTH_SHORT).show();
      return;
    }

    Toast.makeText(context, (isBn ? "'" + query + "' অনুসন্ধান করা হচ্ছে..." : "Searching for '" + query + "'..."), Toast.LENGTH_SHORT).show();

    new Thread(() -> {
      try {
        Geocoder geocoder = new Geocoder(context, isBn ? new Locale("bn", "BD") : Locale.ENGLISH);
        List<Address> addresses = geocoder.getFromLocationName(query, 1);
        if (addresses != null && !addresses.isEmpty()) {
          Address addr = addresses.get(0);
          double foundLat = addr.getLatitude();
          double foundLng = addr.getLongitude();
          String locality = addr.getLocality() != null ? addr.getLocality() : addr.getSubAdminArea();
          String country = addr.getCountryName() != null ? addr.getCountryName() : "";
          String fullName = (locality != null ? locality + ", " : "") + country;

          new Handler(Looper.getMainLooper()).post(() -> {
            latHolder[0] = foundLat;
            lngHolder[0] = foundLng;
            locationNameHolder[0] = (fullName.isEmpty() ? query : fullName);

            binding.etManualLatitude.setText(String.format(Locale.US, "%.5f", foundLat));
            binding.etManualLongitude.setText(String.format(Locale.US, "%.5f", foundLng));
            binding.tvSelectedDistrictName.setText(locationNameHolder[0]);
            binding.tvSelectedDistrictName.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
            binding.tvLiveTimezoneBadge.setText(locationNameHolder[0] + " • " + tzIdHolder[0]);

            updateLiveClock(binding, tzIdHolder[0], isBn);
            Toast.makeText(context, isBn ? "লোকেশন পাওয়া গেছে: " + locationNameHolder[0] : "Location found: " + locationNameHolder[0], Toast.LENGTH_SHORT).show();
          });
        } else {
          new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, isBn ? "কোনো লোকেশন পাওয়া যায়নি" : "No location found", Toast.LENGTH_SHORT).show();
          });
        }
      } catch (Exception e) {
        new Handler(Looper.getMainLooper()).post(() -> {
          Toast.makeText(context, isBn ? "অনুসন্ধান ব্যর্থ হয়েছে" : "Search failed", Toast.LENGTH_SHORT).show();
        });
      }
    }).start();
  }

  private static void showDistrictPickerDialog(Context context, List<CityItem> cities, String currentCityName, java.util.function.Consumer<CityItem> onSelect) {
    BottomSheetDialog bottomSheet = new BottomSheetDialog(context);
    DialogIslamicPickerModalBinding sheetBinding = DialogIslamicPickerModalBinding.inflate(LayoutInflater.from(context));
    bottomSheet.setContentView(sheetBinding.getRoot());

    try {
      View sheetView = bottomSheet.findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (sheetView != null) {
        sheetView.setBackgroundResource(android.R.color.transparent);
      }
    } catch (Exception ignored) {}

    boolean isBn = LocaleManager.isBengali(context);
    sheetBinding.tvPickerTitle.setText(isBn ? "জনপ্রিয় জেলাসমূহ" : "Popular Districts");
    sheetBinding.etPickerSearch.setHint(isBn ? "অনুসন্ধান করুন..." : "Search district...");
    sheetBinding.btnClosePicker.setOnClickListener(v -> bottomSheet.dismiss());

    List<CityItem> filteredList = new ArrayList<>(cities);

    DistrictPickerAdapter adapter = new DistrictPickerAdapter(filteredList, currentCityName, isBn, selected -> {
      onSelect.accept(selected);
      bottomSheet.dismiss();
    });

    sheetBinding.rvPickerItems.setLayoutManager(new LinearLayoutManager(context));
    sheetBinding.rvPickerItems.setAdapter(adapter);

    sheetBinding.etPickerSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString().toLowerCase().trim();
        filteredList.clear();
        if (query.isEmpty()) {
          filteredList.addAll(cities);
        } else {
          for (CityItem item : cities) {
            if (item.nameBn.toLowerCase().contains(query) || item.nameEn.toLowerCase().contains(query) || item.timezoneId.toLowerCase().contains(query)) {
              filteredList.add(item);
            }
          }
        }
        adapter.notifyDataSetChanged();
      }

      @Override
      public void afterTextChanged(Editable s) {}
    });

    bottomSheet.show();
  }

  private static void showTimezonePickerDialog(Context context, String currentId, java.util.function.Consumer<TimezoneItem> onSelect) {
    BottomSheetDialog bottomSheet = new BottomSheetDialog(context);
    DialogIslamicPickerModalBinding sheetBinding = DialogIslamicPickerModalBinding.inflate(LayoutInflater.from(context));
    bottomSheet.setContentView(sheetBinding.getRoot());

    try {
      View sheetView = bottomSheet.findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (sheetView != null) {
        sheetView.setBackgroundResource(android.R.color.transparent);
      }
    } catch (Exception ignored) {}

    boolean isBn = LocaleManager.isBengali(context);
    sheetBinding.tvPickerTitle.setText(isBn ? "টাইমজোন নির্বাচন করুন" : "Select Timezone");
    sheetBinding.etPickerSearch.setHint(isBn ? "অনুসন্ধান করুন..." : "Search timezone...");
    sheetBinding.btnClosePicker.setOnClickListener(v -> bottomSheet.dismiss());

    List<TimezoneItem> fullList = getTimezoneList();
    List<TimezoneItem> filteredList = new ArrayList<>(fullList);

    TimezonePickerAdapter adapter = new TimezonePickerAdapter(filteredList, currentId, isBn, selected -> {
      onSelect.accept(selected);
      bottomSheet.dismiss();
    });

    sheetBinding.rvPickerItems.setLayoutManager(new LinearLayoutManager(context));
    sheetBinding.rvPickerItems.setAdapter(adapter);

    sheetBinding.etPickerSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString().toLowerCase().trim();
        filteredList.clear();
        if (query.isEmpty()) {
          filteredList.addAll(fullList);
        } else {
          for (TimezoneItem item : fullList) {
            if (item.displayNameBn.toLowerCase().contains(query) || item.displayNameEn.toLowerCase().contains(query) || item.id.toLowerCase().contains(query)) {
              filteredList.add(item);
            }
          }
        }
        adapter.notifyDataSetChanged();
      }

      @Override
      public void afterTextChanged(Editable s) {}
    });

    bottomSheet.show();
  }

  private static void saveAndApplySettings(Context context, FullScreenPageDialog dialog, Mode mode,
                        PageLocationSelectorBinding binding,
                        double[] latHolder, double[] lngHolder,
                        double[] tzOffsetHolder, String[] tzIdHolder,
                        String[] locationNameHolder) {
    boolean isBn = LocaleManager.isBengali(context);
    if (mode == Mode.MANUAL) {
      try {
        String latStr = binding.etManualLatitude.getText().toString().trim();
        String lngStr = binding.etManualLongitude.getText().toString().trim();
        if (!latStr.isEmpty()) latHolder[0] = Double.parseDouble(latStr);
        if (!lngStr.isEmpty()) lngHolder[0] = Double.parseDouble(lngStr);
      } catch (Exception ignored) {}
    } else if (mode == Mode.TIMEZONE_ONLY) {
      for (TimezoneItem tz : getTimezoneList()) {
        if (tz.id.equals(tzIdHolder[0])) {
          latHolder[0] = tz.latitude;
          lngHolder[0] = tz.longitude;
          tzOffsetHolder[0] = tz.offsetHours;
          locationNameHolder[0] = tz.getDisplayName(isBn);
          break;
        }
      }
    }

    LocationProvider.setLocationMode(context, mode.name());
    LocationProvider.setSavedTimezoneId(context, tzIdHolder[0]);
    com.devflux.deenone.core.amal.UserLocationTimezoneHelper.setCustomTimeZone(context, tzIdHolder[0]);

    String defaultLoc = isBn ? "বর্তমান অবস্থান" : "Current Location";
    String rawName = locationNameHolder[0] != null ? locationNameHolder[0].replace("", "").trim() : defaultLoc;
    LocationProvider.Coordinates coords = new LocationProvider.Coordinates(
        "" + rawName, latHolder[0], lngHolder[0], tzOffsetHolder[0]
    );
    LocationProvider.saveLocation(context, coords);

    if (com.devflux.deenone.core.prayer.PrayerSettingsManager.isAutoMethod(context)) {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.AutoRecommendation rec =
          com.devflux.deenone.core.prayer.PrayerSettingsManager.getRecommendedMethodForCurrentLocation(context);
      if (rec != null && rec.recommendedMethod != null) {
        if (rec.recommendedMethod == com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION ||
            rec.recommendedMethod == com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.KARACHI ||
            rec.recommendedMethod == com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.TURKEY_DIYANET) {
          com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.HANAFI);
        } else {
          com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.SHAFI);
        }
      }
    }

    if (context instanceof MainActivity) {
      MainActivity activity = (MainActivity) context;
      activity.getViewModel().updateLocation(coords.locationName, coords.latitude, coords.longitude, coords.timezone);
    }

    new com.devflux.deenone.data.repository.PrayerRepository(context).refreshRealtimeSchedule();
    AlarmRescheduler.rescheduleAll(context);

    Toast.makeText(context, isBn ? "লোকেশন ও সময়সূচী সফলভাবে সংরক্ষিত হয়েছে" : "Location & prayer schedule saved successfully", Toast.LENGTH_SHORT).show();
    dialog.dismiss();
  }

  private static List<CityItem> getPopularCities() {
    List<CityItem> list = new ArrayList<>();
    list.add(new CityItem("ঢাকা", "Dhaka", 23.8103, 90.4125, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("চট্টগ্রাম", "Chittagong", 22.3569, 91.7832, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("সিলেট", "Sylhet", 24.8949, 91.8687, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("রাজশাহী", "Rajshahi", 24.3745, 88.6042, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("খুলনা", "Khulna", 22.8456, 89.5403, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("বরিশাল", "Barisal", 22.7010, 90.3535, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("রংপুর", "Rangpur", 25.7439, 89.2752, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("ময়মনসিংহ", "Mymensingh", 24.7471, 90.4203, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("যশোর", "Jessore", 23.1664, 89.2081, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("কুমিল্লা", "Comilla", 23.4682, 91.1788, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("গাজীপুর", "Gazipur", 23.9999, 90.4203, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("বগুড়া", "Bogra", 24.8465, 89.3777, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("কক্সবাজার", "Cox's Bazar", 21.4272, 92.0058, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("নারায়ণগঞ্জ", "Narayanganj", 23.6238, 90.5000, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("দিনাজপুর", "Dinajpur", 25.6217, 88.6355, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("ফরিদপুর", "Faridpur", 23.6070, 89.8429, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("পাবনা", "Pabna", 24.0116, 89.2505, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("টাঙ্গাইল", "Tangail", 24.2513, 89.9167, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("নোয়াখালী", "Noakhali", 22.8696, 91.0995, 6.0, "Asia/Dhaka"));
    list.add(new CityItem("কুষ্টিয়া", "Kushtia", 23.9013, 89.1204, 6.0, "Asia/Dhaka"));

    // Global & Islamic Cities
    list.add(new CityItem("মক্কা মুকাররমা", "Makkah", 21.4225, 39.8262, 3.0, "Asia/Riyadh"));
    list.add(new CityItem("মদিনা মুনাওয়ারা", "Madinah", 24.4672, 39.6024, 3.0, "Asia/Riyadh"));
    list.add(new CityItem("তাবুক", "Tabuk", 28.3997, 36.5775, 3.0, "Asia/Riyadh"));
    list.add(new CityItem("রিয়াদ", "Riyadh", 24.7136, 46.6753, 3.0, "Asia/Riyadh"));
    list.add(new CityItem("জেদ্দা", "Jeddah", 21.5433, 39.1728, 3.0, "Asia/Riyadh"));
    list.add(new CityItem("আল-কুদস", "Jerusalem", 31.7683, 35.2137, 2.0, "Asia/Jerusalem"));
    list.add(new CityItem("দুবাই", "Dubai", 25.2048, 55.2708, 4.0, "Asia/Dubai"));
    list.add(new CityItem("কুয়ালালামপুর", "Kuala Lumpur", 3.1390, 101.6869, 8.0, "Asia/Kuala_Lumpur"));
    list.add(new CityItem("লন্ডন", "London", 51.5074, -0.1278, 0.0, "Europe/London"));
    list.add(new CityItem("নিউইয়র্ক", "New York", 40.7128, -74.0060, -5.0, "America/New_York"));
    list.add(new CityItem("টরন্টো", "Toronto", 43.6532, -79.3832, -5.0, "America/Toronto"));
    return list;
  }

  private static List<TimezoneItem> getTimezoneList() {
    List<TimezoneItem> list = new ArrayList<>();
    list.add(new TimezoneItem("বাংলাদেশ • GMT+6", "Bangladesh • GMT+6", "Asia/Dhaka", 6.0, 23.8103, 90.4125));
    list.add(new TimezoneItem("সৌদি আরব • GMT+3", "Saudi Arabia • GMT+3", "Asia/Riyadh", 3.0, 24.7136, 46.6753));
    list.add(new TimezoneItem("সংযুক্ত আরব আমিরাত • GMT+4", "United Arab Emirates • GMT+4", "Asia/Dubai", 4.0, 25.2048, 55.2708));
    list.add(new TimezoneItem("ভারত • GMT+5:30", "India • GMT+5:30", "Asia/Kolkata", 5.5, 22.5726, 88.3639));
    list.add(new TimezoneItem("পাকিস্তান • GMT+5", "Pakistan • GMT+5", "Asia/Karachi", 5.0, 24.8607, 67.0011));
    list.add(new TimezoneItem("মালয়েশিয়া • GMT+8", "Malaysia • GMT+8", "Asia/Kuala_Lumpur", 8.0, 3.1390, 101.6869));
    list.add(new TimezoneItem("সিঙ্গাপুর • GMT+8", "Singapore • GMT+8", "Asia/Singapore", 8.0, 1.3521, 103.8198));
    list.add(new TimezoneItem("ইন্দোনেশিয়া • GMT+7", "Indonesia • GMT+7", "Asia/Jakarta", 7.0, -6.2088, 106.8456));
    list.add(new TimezoneItem("যুক্তরাজ্য • GMT+0", "United Kingdom • GMT+0", "Europe/London", 0.0, 51.5074, -0.1278));
    list.add(new TimezoneItem("ফ্রান্স • GMT+1", "France • GMT+1", "Europe/Paris", 1.0, 48.8566, 2.3522));
    list.add(new TimezoneItem("তুরস্ক • GMT+3", "Turkey • GMT+3", "Europe/Istanbul", 3.0, 41.0082, 28.9784));
    list.add(new TimezoneItem("মিশর • GMT+2", "Egypt • GMT+2", "Africa/Cairo", 2.0, 30.0444, 31.2357));
    list.add(new TimezoneItem("নিউইয়র্ক • GMT-5", "New York • GMT-5", "America/New_York", -5.0, 40.7128, -74.0060));
    list.add(new TimezoneItem("লস এঞ্জেলেস • GMT-8", "Los Angeles • GMT-8", "America/Los_Angeles", -8.0, 34.0522, -118.2437));
    list.add(new TimezoneItem("শিকাগো • GMT-6", "Chicago • GMT-6", "America/Chicago", -6.0, 41.8781, -87.6298));
    list.add(new TimezoneItem("টরন্টো • GMT-5", "Toronto • GMT-5", "America/Toronto", -5.0, 43.6532, -79.3832));
    list.add(new TimezoneItem("সিডনি • GMT+10", "Sydney • GMT+10", "Australia/Sydney", 10.0, -33.8688, 151.2093));
    list.add(new TimezoneItem("টোকিও • GMT+9", "Tokyo • GMT+9", "Asia/Tokyo", 9.0, 35.6762, 139.6503));
    return list;
  }

  private static class DistrictPickerAdapter extends RecyclerView.Adapter<DistrictPickerAdapter.ViewHolder> {
    private final List<CityItem> items;
    private final String currentCityName;
    private final boolean isBn;
    private final java.util.function.Consumer<CityItem> onSelect;

    public DistrictPickerAdapter(List<CityItem> items, String currentCityName, boolean isBn, java.util.function.Consumer<CityItem> onSelect) {
      this.items = items;
      this.currentCityName = currentCityName;
      this.isBn = isBn;
      this.onSelect = onSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemIslamicSelectionRowBinding binding =
          ItemIslamicSelectionRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      CityItem item = items.get(position);
      String name = item.getName(isBn);
      holder.binding.tvItemTitle.setText(name);
      holder.binding.tvItemSubtitle.setVisibility(View.VISIBLE);
      String offsetStr = (item.timezoneOffset == (int) item.timezoneOffset)
          ? String.valueOf((int) item.timezoneOffset)
          : String.valueOf(item.timezoneOffset);
      holder.binding.tvItemSubtitle.setText(item.timezoneId + " (GMT+" + offsetStr + ")");

      boolean isSelected = (currentCityName != null && (currentCityName.contains(item.nameBn) || currentCityName.contains(item.nameEn)));
      holder.binding.ivItemCheck.setVisibility(isSelected ? View.VISIBLE : View.GONE);
      int activeBg = ContextCompat.getColor(holder.itemView.getContext(), R.color.bg_card_active);
      holder.binding.layoutSelectionItemRoot.setBackgroundColor(isSelected ? activeBg : 0x00000000);

      holder.itemView.setOnClickListener(v -> onSelect.accept(item));
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
      final ItemIslamicSelectionRowBinding binding;

      ViewHolder(ItemIslamicSelectionRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
      }
    }
  }

  private static class TimezonePickerAdapter extends RecyclerView.Adapter<TimezonePickerAdapter.ViewHolder> {
    private final List<TimezoneItem> items;
    private final String currentId;
    private final boolean isBn;
    private final java.util.function.Consumer<TimezoneItem> onSelect;

    public TimezonePickerAdapter(List<TimezoneItem> items, String currentId, boolean isBn, java.util.function.Consumer<TimezoneItem> onSelect) {
      this.items = items;
      this.currentId = currentId;
      this.isBn = isBn;
      this.onSelect = onSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemIslamicSelectionRowBinding binding =
          ItemIslamicSelectionRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      TimezoneItem item = items.get(position);
      holder.binding.tvItemTitle.setText(item.id);
      holder.binding.tvItemSubtitle.setVisibility(View.VISIBLE);
      holder.binding.tvItemSubtitle.setText(item.getDisplayName(isBn));

      boolean isSelected = item.id.equals(currentId);
      holder.binding.ivItemCheck.setVisibility(isSelected ? View.VISIBLE : View.GONE);
      int activeBg = ContextCompat.getColor(holder.itemView.getContext(), R.color.bg_card_active);
      holder.binding.layoutSelectionItemRoot.setBackgroundColor(isSelected ? activeBg : 0x00000000);

      holder.itemView.setOnClickListener(v -> onSelect.accept(item));
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
      final ItemIslamicSelectionRowBinding binding;

      ViewHolder(ItemIslamicSelectionRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
      }
    }
  }
}
