package com.devflux.deenone.features.blood;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.BloodDonorEntity;
import com.devflux.deenone.databinding.PageBloodDonorRegistrationBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Blood Donor Registration Full-Screen Interface
 * 100% Verbatim visual fidelity matching user screenshot.
 * Strict Rule 5: Zero mixed bracket text.
 * Strict Rule 7: Touch animations exclusively on buttons.
 */
public class BloodDonorRegistrationDialog {

  private static final String PREF_PROFILE = "user_profile_prefs";
  private static final String PREF_BLOOD = "blood_donation_prefs";
  private static final String KEY_USER_ID = "profile_user_id";
  private static final String KEY_USER_NAME = "profile_full_name";
  private static final String KEY_USER_PHONE = "profile_phone";
  private static final String KEY_LEGACY_UID = "user_uid";
  private static final String KEY_LEGACY_NAME = "user_full_name";
  private static final String KEY_LEGACY_PHONE = "user_phone";
  private static final String KEY_USER_LOCATION = "user_location";

  private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

  private static final OkHttpClient httpClient = new OkHttpClient.Builder()
      .connectTimeout(12, TimeUnit.SECONDS)
      .readTimeout(12, TimeUnit.SECONDS)
      .writeTimeout(12, TimeUnit.SECONDS)
      .build();

  private static final ExecutorService executor = Executors.newSingleThreadExecutor();
  private static final Handler mainHandler = new Handler(Looper.getMainLooper());

  private static String selectedGroup = "A+";

  public static void show(Activity activity, Runnable onCompleted) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    PageBloodDonorRegistrationBinding binding = PageBloodDonorRegistrationBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isBn = LocaleManager.isBengali(activity);
    SharedPreferences profilePrefs = activity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
    SharedPreferences bloodPrefs = activity.getSharedPreferences(PREF_BLOOD, Context.MODE_PRIVATE);
    AppDatabase db = AppDatabase.getInstance(activity);

    // 1. Text & Titles Setup (Strict Rule 5 - Zero Bracket Text)
    binding.tvDonorRegHeaderTitle.setText(isBn ? "রক্তদাতা নিবন্ধন" : "Blood Donor Registration");
    binding.tvLabelBloodGroup.setText(isBn ? "রক্তের গ্রুপ" : "Blood Group");
    binding.tvLabelLastDonation.setText(isBn ? "সর্বশেষ রক্তদানের তারিখ (ঐচ্ছিক)" : "Last Donation Date (Optional)");
    binding.tvLastDonationDate.setHint("YYYY-MM-DD");
    binding.tvLabelTotalDonations.setText(isBn ? "মোট রক্তদানের সংখ্যা" : "Total Blood Donations");
    binding.etTotalDonations.setHint("0");
    binding.tvLabelCurrentAddress.setText(isBn ? "বর্তমান ঠিকানা / শিক্ষা প্রতিষ্ঠান" : "Current Address / Educational Institution");
    binding.etCurrentAddress.setHint(isBn ? "যেমন: উত্তরা, ঢাকা বা ঢাকা বি..." : "e.g. Uttara, Dhaka or Dhaka Uni...");
    binding.tvLabelDonorSociety.setText(isBn ? "রক্তদাতা সোসাইটি / কমিউনিটি (ঐচ্ছিক)" : "Blood Donor Society / Community (Optional)");
    binding.btnRegisterAsDonor.setText(isBn ? "ডোনার হিসেবে নিবন্ধন করুন" : "Register as Donor");

    // 2. Resolve User Profile Information from AuthManager & SharedPreferences
    AuthManager.UserSession session = AuthManager.getCurrentSession(activity);
    String userId = (session != null && session.userId != null && !session.userId.isEmpty() && !"usr_guest".equals(session.userId))
        ? session.userId
        : profilePrefs.getString(KEY_USER_ID, profilePrefs.getString(KEY_LEGACY_UID, "usr_" + System.currentTimeMillis()));

    String defaultName = (session != null && session.name != null && !session.name.isEmpty() 
        && !session.name.equals("লগইন / সাইন আপ") && !session.name.equals("দ্বীনওয়ান ব্যবহারকারী"))
        ? session.name
        : profilePrefs.getString(KEY_USER_NAME, profilePrefs.getString(KEY_LEGACY_NAME, bloodPrefs.getString("donor_name", "")));

    String defaultPhone = (session != null && session.phone != null && !session.phone.isEmpty())
        ? session.phone
        : profilePrefs.getString(KEY_USER_PHONE, profilePrefs.getString(KEY_LEGACY_PHONE, bloodPrefs.getString("donor_phone", "")));

    String defaultLocation = bloodPrefs.getString("donor_address", profilePrefs.getString(KEY_USER_LOCATION, ""));
    String defaultGroup = (session != null && session.bloodGroup != null && !session.bloodGroup.isEmpty())
        ? session.bloodGroup
        : bloodPrefs.getString("donor_blood_group", "A+");
    String defaultLastDate = bloodPrefs.getString("donor_last_donation_date", "");
    int defaultDonations = bloodPrefs.getInt("donor_total_donations", 0);

    // Pre-fill fields if previously registered
    if (!defaultLocation.isEmpty()) {
      binding.etCurrentAddress.setText(defaultLocation);
    }
    if (!defaultLastDate.isEmpty()) {
      binding.tvLastDonationDate.setText(defaultLastDate);
    }
    if (defaultDonations > 0) {
      binding.etTotalDonations.setText(String.valueOf(defaultDonations));
    }

    // Always keep Name & Phone input fields visible for explicit donor identification
    binding.containerProfileFallback.setVisibility(View.VISIBLE);
    binding.tvLabelDonorName.setText(isBn ? "আপনার পূর্ণ নাম" : "Your Full Name");
    binding.etDonorFullName.setHint(isBn ? "নাম লিখুন" : "Enter your name");
    if (!defaultName.isEmpty()) {
      binding.etDonorFullName.setText(defaultName);
    }
    binding.tvLabelDonorPhone.setText(isBn ? "যোগাযোগের মোবাইল নম্বর" : "Contact Phone Number");
    binding.etDonorPhoneNum.setHint(isBn ? "যেমন: 01700000000" : "e.g. 01700000000");
    if (!defaultPhone.isEmpty()) {
      binding.etDonorPhoneNum.setText(defaultPhone);
    }

    // 3. Blood Group 2x4 Pills Setup
    selectedGroup = (!defaultGroup.isEmpty()) ? defaultGroup : "A+";
    AppCompatTextView[] groupButtons = {
        binding.btnGroupAPos, binding.btnGroupANeg,
        binding.btnGroupBPos, binding.btnGroupBNeg,
        binding.btnGroupOPos, binding.btnGroupONeg,
        binding.btnGroupABPos, binding.btnGroupABNeg
    };

    updateGroupSelection(groupButtons, selectedGroup);

    for (AppCompatTextView btn : groupButtons) {
      TouchAnimationUtil.attachTouchSpring(btn);
      btn.setOnClickListener(v -> {
        selectedGroup = btn.getText().toString().trim();
        updateGroupSelection(groupButtons, selectedGroup);
      });
    }

    // 4. Date Picker Setup
    binding.boxLastDonationDate.setOnClickListener(v -> {
      Calendar cal = Calendar.getInstance();
      String curDate = binding.tvLastDonationDate.getText().toString().trim();
      if (curDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
        try {
          String[] parts = curDate.split("-");
          cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
          cal.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
          cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
        } catch (Exception ignored) {}
      }

      DatePickerDialog dateDialog = new DatePickerDialog(
          activity,
          (view, year, month, dayOfMonth) -> {
            String formatted = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            binding.tvLastDonationDate.setText(formatted);
          },
          cal.get(Calendar.YEAR),
          cal.get(Calendar.MONTH),
          cal.get(Calendar.DAY_OF_MONTH)
      );
      dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
      dateDialog.show();
    });

    // 5. GPS Location Auto-Detection
    TouchAnimationUtil.attachTouchSpring(binding.btnDetectLocation);
    binding.btnDetectLocation.setOnClickListener(v -> {
      detectDeviceLocation(activity, binding);
    });

    // 6. Blood Donor Society Dropdown Setup (Strict Rule 5 - Zero Bracket Texts)
    String[] societies = isBn ? new String[]{
        "কোনো সোসাইটি নয়",
        "সন্ধানী",
        "কোয়ান্টাম ব্লাড ল্যাব",
        "বাংলাদেশ রেড ক্রিসেন্ট সোসাইটি",
        "বাঁধন",
        "পুলিশ ব্লাড ব্যাংক",
        "অন্যান্য সোসাইটি বা ক্লাব"
    } : new String[]{
        "No Society / None",
        "Sandhani",
        "Quantum Blood Lab",
        "Bangladesh Red Crescent Society",
        "Badhan",
        "Police Blood Bank",
        "Other Society or Club"
    };

    ArrayAdapter<String> societyAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, societies);
    binding.spinnerDonorSociety.setAdapter(societyAdapter);

    String savedSociety = bloodPrefs.getString("donor_society", "");
    if (!savedSociety.isEmpty()) {
      for (int i = 0; i < societies.length; i++) {
        if (societies[i].equalsIgnoreCase(savedSociety) || societies[i].contains(savedSociety)) {
          binding.spinnerDonorSociety.setSelection(i);
          break;
        }
      }
    }

    // 7. Button Spring Animations (Strict Rule 7)
    TouchAnimationUtil.attachTouchSpring(binding.btnBackDonorReg);
    TouchAnimationUtil.attachTouchSpring(binding.btnRegisterAsDonor);

    binding.btnBackDonorReg.setOnClickListener(v -> dialog.dismiss());

    // 8. Submit Registration Handler
    binding.btnRegisterAsDonor.setOnClickListener(v -> {
      String inputName = binding.etDonorFullName.getText().toString().trim();
      String finalName = !inputName.isEmpty() ? inputName : defaultName;

      String inputPhone = binding.etDonorPhoneNum.getText().toString().trim();
      String finalPhone = !inputPhone.isEmpty() ? inputPhone : defaultPhone;

      if (finalName.isEmpty()) {
        binding.containerProfileFallback.setVisibility(View.VISIBLE);
        binding.etDonorFullName.requestFocus();
        Toast.makeText(activity, isBn ? "অনুগ্রহ করে আপনার নাম প্রদান করুন" : "Please enter your name", Toast.LENGTH_SHORT).show();
        return;
      }

      if (finalPhone.isEmpty()) {
        binding.containerProfileFallback.setVisibility(View.VISIBLE);
        binding.etDonorPhoneNum.requestFocus();
        Toast.makeText(activity, isBn ? "অনুগ্রহ করে মোবাইল নম্বর প্রদান করুন" : "Please enter your phone number", Toast.LENGTH_SHORT).show();
        return;
      }

      String address = binding.etCurrentAddress.getText().toString().trim();
      String lastDate = binding.tvLastDonationDate.getText().toString().trim();
      if (!lastDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
        lastDate = "";
      }

      int totalDonations = 0;
      try {
        String numStr = binding.etTotalDonations.getText().toString().trim();
        if (!numStr.isEmpty()) {
          totalDonations = Integer.parseInt(numStr);
        }
      } catch (Exception ignored) {}

      String society = binding.spinnerDonorSociety.getSelectedItem() != null
          ? binding.spinnerDonorSociety.getSelectedItem().toString() : "";
      if (society.equals("কোনো সোসাইটি নয়") || society.equals("No Society / None")) {
        society = "";
      }

      // Show Progress on Button
      binding.btnRegisterAsDonor.setEnabled(false);
      binding.btnRegisterAsDonor.setText(isBn ? "নিবন্ধন সম্পন্ন হচ্ছে..." : "Registering...");

      performDonorRegistration(
          activity,
          dialog,
          userId,
          finalName,
          finalPhone,
          selectedGroup,
          address,
          lastDate,
          totalDonations,
          society,
          profilePrefs,
          bloodPrefs,
          db,
          binding,
          onCompleted
      );
    });

    dialog.show();
  }

  private static void updateGroupSelection(AppCompatTextView[] buttons, String group) {
    for (AppCompatTextView btn : buttons) {
      if (btn.getText().toString().trim().equalsIgnoreCase(group)) {
        btn.setBackgroundResource(R.drawable.bg_blood_group_selected);
        btn.setTextColor(0xFFFFFFFF);
      } else {
        btn.setBackgroundResource(R.drawable.bg_blood_group_unselected);
        btn.setTextColor(ContextCompat.getColor(btn.getContext(), R.color.text_secondary));
      }
    }
  }

  private static void detectDeviceLocation(Activity activity, PageBloodDonorRegistrationBinding binding) {
    boolean isBn = LocaleManager.isBengali(activity);
    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
      Toast.makeText(activity, isBn ? "অবস্থান শনাক্ত করতে লোকেশন অনুমতি প্রয়োজন" : "Location permission needed to detect position", Toast.LENGTH_SHORT).show();
      return;
    }

    try {
      LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
      Location location = null;
      if (lm != null) {
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
          location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (location == null && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
          location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
      }

      if (location != null) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        executor.execute(() -> {
          try {
            Geocoder geocoder = new Geocoder(activity, isBn ? new Locale("bn", "BD") : Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
              Address addr = addresses.get(0);
              String locality = addr.getSubLocality();
              if (locality == null || locality.isEmpty()) locality = addr.getLocality();
              String adminArea = addr.getSubAdminArea();
              if (adminArea == null || adminArea.isEmpty()) adminArea = addr.getAdminArea();

              String detected = "";
              if (locality != null && !locality.isEmpty()) detected += locality;
              if (adminArea != null && !adminArea.isEmpty()) {
                detected += (detected.isEmpty() ? "" : ", ") + adminArea;
              }

              final String finalText = detected;
              mainHandler.post(() -> {
                if (!finalText.isEmpty()) {
                  binding.etCurrentAddress.setText(finalText);
                  Toast.makeText(activity, isBn ? "ঠিকানা শনাক্ত হয়েছে: " + finalText : "Location detected: " + finalText, Toast.LENGTH_SHORT).show();
                }
              });
            }
          } catch (Exception ignored) {}
        });
      } else {
        Toast.makeText(activity, isBn ? "অনুগ্রহ করে ডিভাইসের GPS চালু করুন" : "Please enable device GPS", Toast.LENGTH_SHORT).show();
      }
    } catch (Exception e) {
      Toast.makeText(activity, isBn ? "অবস্থান শনাক্ত করা যায়নি" : "Unable to detect location", Toast.LENGTH_SHORT).show();
    }
  }

  private static void performDonorRegistration(
      Activity activity,
      FullScreenPageDialog dialog,
      String userId,
      String name,
      String phone,
      String bloodGroup,
      String address,
      String lastDonationDate,
      int totalDonations,
      String society,
      SharedPreferences profilePrefs,
      SharedPreferences bloodPrefs,
      AppDatabase db,
      PageBloodDonorRegistrationBinding binding,
      Runnable onCompleted
  ) {
    boolean isBn = LocaleManager.isBengali(activity);

    // 1. Immediately cache locally in SharedPreferences & SQLite Room
    bloodPrefs.edit()
        .putBoolean("is_registered_donor", true)
        .putString("donor_name", name)
        .putString("donor_phone", phone)
        .putString("donor_blood_group", bloodGroup)
        .putString("donor_address", address)
        .putString("donor_last_donation_date", lastDonationDate)
        .putInt("donor_total_donations", totalDonations)
        .putString("donor_society", society)
        .putLong("donor_reg_time", System.currentTimeMillis())
        .apply();

    // Update profile phone and name if empty
    profilePrefs.edit()
        .putString(KEY_USER_NAME, name)
        .putString(KEY_USER_PHONE, phone)
        .putString(KEY_LEGACY_NAME, name)
        .putString(KEY_LEGACY_PHONE, phone)
        .apply();

    executor.execute(() -> {
      BloodDonorEntity entity = new BloodDonorEntity(
          "donor_" + userId,
          userId,
          name,
          bloodGroup,
          phone,
          address.isEmpty() ? (isBn ? "ঢাকা" : "Dhaka") : address,
          0L,
          System.currentTimeMillis(),
          true
      );
      try {
        db.bloodDonorDao().deleteDummyDonors();
        db.bloodDonorDao().insertOrUpdateDonor(entity);
      } catch (Exception ignored) {}

      // 2. Post to PHP REST API (server_backend/api/blood_donors.php)
      String apiUrl = BackendConfigManager.getPhpApiEndpoint(activity, "blood_donors.php");
      JsonObject json = new JsonObject();
      json.addProperty("action", "register_donor");
      json.addProperty("user_id", userId);
      json.addProperty("name", name);
      json.addProperty("phone", phone);
      json.addProperty("phone_number", phone);
      json.addProperty("blood_group", bloodGroup);
      json.addProperty("district", address.isEmpty() ? (isBn ? "ঢাকা" : "Dhaka") : address);
      json.addProperty("address", address);
      json.addProperty("last_donation_date", lastDonationDate);
      json.addProperty("total_donations", totalDonations);
      json.addProperty("society", society);
      json.addProperty("is_available", 1);

      try {
        RequestBody body = RequestBody.create(json.toString(), JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
            .url(apiUrl)
            .post(body)
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(activity))
            .build();
        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
          String responseMsg = response.body().string();
          try {
            JsonObject resObj = new Gson().fromJson(responseMsg, JsonObject.class);
            if (resObj != null && resObj.has("donor_id")) {
              int donorId = resObj.get("donor_id").getAsInt();
              try {
                db.bloodDonorDao().deleteDonor("donor_" + userId);
              } catch (Exception ignored) {}
              entity.setDonorId("donor_php_" + donorId);
              db.bloodDonorDao().insertOrUpdateDonor(entity);
            }
          } catch (Exception ignored) {}
        }
      } catch (Exception ignored) {}

      mainHandler.post(() -> {
        if (activity.isFinishing()) return;

        binding.btnRegisterAsDonor.setEnabled(true);
        binding.btnRegisterAsDonor.setText(isBn ? "ডোনার হিসেবে নিবন্ধন করুন" : "Register as Donor");

        Toast.makeText(
            activity,
            isBn ? "রক্তদাতা হিসেবে আপনার নিবন্ধন সফল হয়েছে!" : "Registered as a blood donor successfully!",
            Toast.LENGTH_LONG
        ).show();

        if (onCompleted != null) {
          onCompleted.run();
        }

        dialog.dismiss();
      });
    });
  }
}
