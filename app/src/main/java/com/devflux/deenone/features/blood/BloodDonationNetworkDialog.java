package com.devflux.deenone.features.blood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.backend.BackendRepository;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.BloodDonorEntity;
import com.devflux.deenone.databinding.DialogBloodDonorRegistrationBinding;
import com.devflux.deenone.databinding.PageBloodDonationNetworkBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BloodDonationNetworkDialog {

  private static final String PREF_PROFILE = "user_profile_prefs";
  private static final String PREF_BLOOD = "blood_donation_prefs";
  private static final String KEY_USER_ID = "user_uid";
  private static final String KEY_USER_NAME = "user_full_name";
  private static final String KEY_USER_LOCATION = "user_location";

  private static final String[] BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

  public static void show(Activity activity) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    PageBloodDonationNetworkBinding binding = PageBloodDonationNetworkBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    SharedPreferences profilePrefs = activity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
    SharedPreferences bloodPrefs = activity.getSharedPreferences(PREF_BLOOD, Context.MODE_PRIVATE);
    AppDatabase db = AppDatabase.getInstance(activity);

    boolean isBn = LocaleManager.isBengali(activity);

    // 1. Hero Header Texts (DeenOne Blood Hub)
    binding.tvBloodHeroBadge.setText(isBn ? "দ্বীন ওয়ান ব্লাড হাব" : "DEENONE BLOOD HUB");
    binding.tvBloodHeroTitle.setText(isBn
        ? "রক্তদান করুন, মানুষের পাশে\nথাকুন"
        : "Donate Blood, Stand by\nHumanity");

    // 2. 2x2 Feature Grid Titles & Subtitles (Verbatim Match)
    binding.tvFindDonorTitle.setText(isBn ? "ডোনার খুঁজুন" : "Find Donors");
    binding.tvFindDonorSubtitle.setText(isBn ? "আশেপাশে ডোনার সন্ধান করুন" : "Search for donors nearby");

    binding.tvEmergencyNeedTitle.setText(isBn ? "জরুরী রক্ত প্রয়োজন" : "Emergency Blood");
    binding.tvEmergencyNeedSubtitle.setText(isBn ? "রক্তের আবেদন পোস্ট করুন" : "Post blood request");

    binding.tvBecomeDonorTitle.setText(isBn ? "ডোনার হন" : "Become a Donor");
    binding.tvBecomeDonorSubtitle.setText(isBn ? "রক্তদাতা হিসেবে নিবন্ধন করুন" : "Register as a donor");

    binding.tvAllRequestsTitle.setText(isBn ? "সব আবেদন" : "All Requests");
    binding.tvAllRequestsSubtitle.setText(isBn ? "রক্তের সব রিকোয়েস্ট দেখুন" : "View all requests");

    // 3. 3 Full-Width Action Rows Titles & Subtitles (Verbatim Match)
    binding.tvDonorCommunityTitle.setText(isBn ? "রক্তদাতা কমিউনিটি" : "Blood Donor Community");
    binding.tvDonorCommunitySubtitle.setText(isBn
        ? "আপনার কাছাকাছি রক্তদাতা সংগঠন ও স্বেচ্ছাসেবী গ্রুপসমূহ"
        : "Blood donor organizations and volunteer groups near you");

    binding.tvDonationGuidelineTitle.setText(isBn ? "রক্তদান নির্দেশিকা" : "Donation Guidelines");
    binding.tvDonationGuidelineSubtitle.setText(isBn ? "রক্তদানের নিয়ম ও যোগ্যতা জানুন" : "Learn rules and eligibility for donation");

    binding.tvSafetyCautionTitle.setText(isBn ? "নিরাপত্তা ও সতর্কতা" : "Safety & Caution");
    binding.tvSafetyCautionSubtitle.setText(isBn ? "প্রতারণা রোধ করতে নিয়মাবলি পড়ুন" : "Read guidelines to prevent fraud");

    // 4. Theme & Notification Icons
    boolean isBloodDark = ThemeManager.getSavedThemeMode(activity) == ThemeManager.THEME_DARK;
    binding.ivBloodThemeIcon.setImageResource(isBloodDark ? R.drawable.ic_sun : R.drawable.ic_moon);

    // STRICT RULE 7: Touch animation attached ONLY to actionable buttons, ZERO animation on card views!
    TouchAnimationUtil.attachTouchSpring(binding.btnCloseBloodNetwork);
    TouchAnimationUtil.attachTouchSpring(binding.btnBloodThemeToggle);
    TouchAnimationUtil.attachTouchSpring(binding.btnBloodNotification);

    // Top Bar Actions
    binding.btnCloseBloodNetwork.setOnClickListener(v -> dialog.dismiss());

    binding.btnBloodThemeToggle.setOnClickListener(v -> {
      if (activity instanceof MainActivity mainActivity) {
        mainActivity.toggleAppTheme();
        dialog.dismiss();
      }
    });

    binding.btnBloodNotification.setOnClickListener(v -> {
      if (activity instanceof MainActivity mainActivity) {
        mainActivity.showNotificationHistorySheet();
      }
    });

    // 5. 2x2 Grid Card Clicks (Direct actions without touch animation on cards as per Rule 7)
    binding.cardFindDonor.setOnClickListener(v -> {
      BloodDonorSearchDialog.show(activity);
    });

    binding.cardEmergencyNeed.setOnClickListener(v -> {
      BloodEmergencyRequestDialog.show(activity, null);
    });

    binding.cardBecomeDonor.setOnClickListener(v -> {
      showDonorRegistrationDialog(activity, db, profilePrefs, bloodPrefs, null);
    });

    binding.cardAllRequests.setOnClickListener(v -> {
      BloodAllRequestsDialog.show(activity);
    });

    // 6. 3 Full-Width Action Rows Clicks
    binding.cardDonorCommunity.setOnClickListener(v -> {
      BloodCommunityOrganizationsDialog.show(activity);
    });

    binding.cardDonationGuideline.setOnClickListener(v -> {
      BloodDonationFaqDialog.show(activity);
    });

    binding.cardSafetyCaution.setOnClickListener(v -> {
      BloodFraudPreventionDialog.show(activity);
    });

    // 7. Background initial sync with remote backend
    syncRemoteDonors(activity, db);

    dialog.show();
  }

  // =========================================================================
  // Donor Registration / Profile Update Dialog (Production-Ready)
  // =========================================================================
  public static void showDonorRegistrationDialog(Activity activity, AppDatabase db,
                                                SharedPreferences profilePrefs, SharedPreferences bloodPrefs,
                                                Runnable onCompleted) {
    if (activity == null || activity.isFinishing()) return;
    BloodDonorRegistrationDialog.show(activity, onCompleted);
  }

  private static void syncRemoteDonors(Activity activity, AppDatabase db) {
    if (activity == null || !com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(activity)) return;
    AppDatabase.databaseWriteExecutor.execute(() -> {
      try {
        db.bloodDonorDao().deleteDummyDonors();
      } catch (Exception ignored) {}

      String url = BackendConfigManager.getPhpApiEndpoint(activity, "blood_donors.php?view=donors");
      try {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
            .url(url)
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(activity))
            .build();
        try (Response response = client.newCall(request).execute()) {
          if (response.isSuccessful() && response.body() != null) {
            String jsonStr = response.body().string();
            JsonObject obj = new Gson().fromJson(jsonStr, JsonObject.class);
            if (obj != null && obj.has("donors")) {
              JsonArray arr = obj.getAsJsonArray("donors");
              for (JsonElement el : arr) {
                JsonObject d = el.getAsJsonObject();
                String id = d.has("id") ? "donor_php_" + d.get("id").getAsString() : "donor_" + System.currentTimeMillis();
                String uid = d.has("user_id") && !d.get("user_id").isJsonNull() ? d.get("user_id").getAsString() : id;
                String name = d.has("name") ? d.get("name").getAsString() : "";
                String group = d.has("blood_group") ? d.get("blood_group").getAsString() : "O+";
                String phone = d.has("phone_number") ? d.get("phone_number").getAsString() : "";
                String district = d.has("district") ? d.get("district").getAsString() : "ঢাকা";
                String upazila = d.has("upazila") && !d.get("upazila").isJsonNull() ? d.get("upazila").getAsString() : "";
                String location = upazila.isEmpty() ? district : (district + ", " + upazila);
                boolean isAvail = !d.has("is_available") || d.get("is_available").getAsInt() == 1;

                if (!name.isEmpty() && !phone.isEmpty()) {
                  db.bloodDonorDao().insertOrUpdateDonor(new BloodDonorEntity(
                      id, uid, name, group, phone, location, 0, System.currentTimeMillis(), isAvail
                  ));
                }
              }
            }
          }
        }
      } catch (Exception ignored) {}
    });
  }
}
