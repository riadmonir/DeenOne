package com.devflux.deenone.features.blood;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.databinding.DialogBloodEmergencyRequestBinding;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BloodEmergencyRequestDialog {

  private static String selectedBloodGroup = "A+";
  private static String selectedUrgency = "EMERGENCY";
  private static String selectedProblemReason = "অপারেশন";
  private static String selectedUnitsNeeded = "১ ব্যাগ";
  private static String selectedNeededDate = "";

  public static void show(Activity activity, Runnable onCompleted) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    DialogBloodEmergencyRequestBinding binding =
        DialogBloodEmergencyRequestBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isBn = LocaleManager.isBengali(activity);

    // Initial State
    selectedBloodGroup = "A+";
    selectedUrgency = "EMERGENCY";
    selectedProblemReason = isBn ? "অপারেশন" : "Surgery";
    selectedUnitsNeeded = isBn ? "১ ব্যাগ" : "1 Bag";

    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    selectedNeededDate = sdf.format(cal.getTime());

    // 1. Language Localization as per Rule 5 (Zero Mixed-Language Bracket Pollution)
    binding.tvEmergencyReqHeaderTitle.setText(isBn ? "রক্তের আবেদন" : "Blood Request");
    binding.lblReqBloodGroup.setText(isBn ? "রক্তের গ্রুপ" : "Blood Group");
    binding.lblUrgencyLevel.setText(isBn ? "জরুরিতা" : "Urgency");
    binding.tvUrgencyEmergencyText.setText("Emergency");
    binding.tvUrgencyNormalText.setText("Normal");

    binding.lblProblemReason.setText(isBn ? "রোগীর সমস্যা" : "Patient's Condition");
    binding.tvProblemReason.setText(selectedProblemReason);

    binding.lblReqUnits.setText(isBn ? "রক্তের পরিমাণ" : "Blood Quantity");
    binding.tvUnitsNeeded.setText(selectedUnitsNeeded);

    binding.lblNeededDate.setText(isBn ? "রক্তদানের তারিখ" : "Donation Date");
    binding.tvNeededDate.setText(selectedNeededDate);

    binding.lblDonationTime.setText(isBn ? "রক্তদানের সময়" : "Donation Time");
    binding.etDonationTime.setHint(isBn ? "যেমন: সকাল ১০টা" : "e.g. 10:00 AM");

    binding.lblHospitalAddress.setText(isBn ? "হাসপাতাল ও ঠিকানা" : "Hospital & Address");
    binding.etHospitalAddress.setHint(isBn ? "যেমন: ঢাকা মেডিকেল কলেজ হাস..." : "e.g. Dhaka Medical College Hos...");

    binding.lblContactPhone.setText(isBn ? "যোগাযোগের নম্বর" : "Contact Phone");
    binding.etContactPhone.setHint("01XXXXXXXXX");

    binding.lblNotes.setText(isBn ? "অতিরিক্ত নোট (ঐচ্ছিক)" : "Additional Notes (Optional)");
    binding.etNotes.setHint(isBn ? "জরুরী যোগাযোগের জন্য বিস্তারিত লিখুন" : "Enter details for emergency contact");

    binding.tvConfirmRequestText.setText(isBn ? "আবেদন নিশ্চিত করুন" : "Confirm Request");

    // 2. Pre-fill user saved phone & district
    com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(activity);
    SharedPreferences profilePrefs = activity.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
    String savedPhone = (session != null && session.phone != null && !session.phone.isEmpty())
        ? session.phone : profilePrefs.getString("profile_phone", profilePrefs.getString("user_phone", ""));
    String savedDistrict = profilePrefs.getString("user_location", "");
    if (!savedPhone.isEmpty()) {
      binding.etContactPhone.setText(savedPhone);
    }
    if (!savedDistrict.isEmpty()) {
      binding.etHospitalAddress.setText(savedDistrict);
    }

    // 3. Attach Touch Animations strictly on interactive buttons as per Rule 7
    TouchAnimationUtil.attachTouchSpring(binding.btnCloseEmergencyReq);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupAPos);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupANeg);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupBPos);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupBNeg);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupOPos);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupONeg);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupABPos);
    TouchAnimationUtil.attachTouchSpring(binding.btnGroupABNeg);
    TouchAnimationUtil.attachTouchSpring(binding.btnUrgencyEmergency);
    TouchAnimationUtil.attachTouchSpring(binding.btnUrgencyNormal);
    TouchAnimationUtil.attachTouchSpring(binding.btnGpsLocation);
    TouchAnimationUtil.attachTouchSpring(binding.btnConfirmRequest);

    binding.btnCloseEmergencyReq.setOnClickListener(v -> dialog.dismiss());

    // 4. Blood Group Chip Selection Setup
    TextView[] bloodChips = {
        binding.btnGroupAPos, binding.btnGroupANeg,
        binding.btnGroupBPos, binding.btnGroupBNeg,
        binding.btnGroupOPos, binding.btnGroupONeg,
        binding.btnGroupABPos, binding.btnGroupABNeg
    };
    String[] groups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

    updateBloodGroupChips(bloodChips, groups, selectedBloodGroup);

    for (int i = 0; i < bloodChips.length; i++) {
      final String group = groups[i];
      bloodChips[i].setOnClickListener(v -> {
        selectedBloodGroup = group;
        updateBloodGroupChips(bloodChips, groups, selectedBloodGroup);
      });
    }

    // 5. Urgency Selection Setup (Emergency vs Normal)
    updateUrgencyDisplay(binding, selectedUrgency);

    binding.btnUrgencyEmergency.setOnClickListener(v -> {
      selectedUrgency = "EMERGENCY";
      updateUrgencyDisplay(binding, selectedUrgency);
    });

    binding.btnUrgencyNormal.setOnClickListener(v -> {
      selectedUrgency = "NORMAL";
      updateUrgencyDisplay(binding, selectedUrgency);
    });

    // 6. Patient Condition / Problem Picker Dialog
    String[] problemReasons = isBn
        ? new String[]{"অপারেশন", "থ্যালাসেমিয়া", "দুর্ঘটনা", "রক্তক্ষরণ", "প্রসূতি / সিজার", "ক্যান্সার / ডায়ালাইসিস", "অন্যান্য"}
        : new String[]{"Surgery", "Thalassemia", "Accident", "Severe Bleeding", "Maternity / C-Section", "Cancer / Dialysis", "Other"};

    binding.layoutProblemReason.setOnClickListener(v -> {
      new MaterialAlertDialogBuilder(activity)
          .setTitle(isBn ? "রোগীর সমস্যা নির্বাচন করুন" : "Select Patient Condition")
          .setItems(problemReasons, (d, which) -> {
            selectedProblemReason = problemReasons[which];
            binding.tvProblemReason.setText(selectedProblemReason);
          })
          .show();
    });

    // 7. Blood Quantity / Units Picker Dialog
    String[] unitsOptions = isBn
        ? new String[]{"১ ব্যাগ", "২ ব্যাগ", "৩ ব্যাগ", "৪ ব্যাগ", "৫ ব্যাগ", "জরুরি প্রয়োজনমতো"}
        : new String[]{"1 Bag", "2 Bags", "3 Bags", "4 Bags", "5 Bags", "As needed urgently"};

    binding.layoutUnitsNeeded.setOnClickListener(v -> {
      new MaterialAlertDialogBuilder(activity)
          .setTitle(isBn ? "রক্তের পরিমাণ নির্বাচন করুন" : "Select Quantity")
          .setItems(unitsOptions, (d, which) -> {
            selectedUnitsNeeded = unitsOptions[which];
            binding.tvUnitsNeeded.setText(selectedUnitsNeeded);
          })
          .show();
    });

    // 8. Needed Date Picker Dialog
    binding.layoutNeededDate.setOnClickListener(v -> {
      Calendar c = Calendar.getInstance();
      DatePickerDialog datePicker = new DatePickerDialog(
          activity,
          (view, year, month, dayOfMonth) -> {
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            selectedNeededDate = sdf.format(c.getTime());
            binding.tvNeededDate.setText(selectedNeededDate);
          },
          c.get(Calendar.YEAR),
          c.get(Calendar.MONTH),
          c.get(Calendar.DAY_OF_MONTH)
      );
      datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
      datePicker.show();
    });

    // 9. GPS Target Button Auto-Location
    binding.btnGpsLocation.setOnClickListener(v -> {
      String loc = profilePrefs.getString("user_location", "");
      if (!loc.isEmpty()) {
        String current = binding.etHospitalAddress.getText().toString().trim();
        if (current.isEmpty()) {
          binding.etHospitalAddress.setText(loc);
        } else if (!current.contains(loc)) {
          binding.etHospitalAddress.setText(current + ", " + loc);
        }
      } else {
        binding.etHospitalAddress.setText(isBn ? "ঢাকা" : "Dhaka");
      }
    });

    // 10. Confirm & Submit Action
    binding.btnConfirmRequest.setOnClickListener(v -> {
      String hospital = binding.etHospitalAddress.getText().toString().trim();
      String phone = binding.etContactPhone.getText().toString().trim();
      String donationTime = binding.etDonationTime.getText().toString().trim();
      String notes = binding.etNotes.getText().toString().trim();

      if (hospital.isEmpty()) {
        binding.etHospitalAddress.setError(isBn ? "অনুগ্রহ করে হাসপাতাল ও ঠিকানা লিখুন" : "Please enter hospital and address");
        binding.etHospitalAddress.requestFocus();
        return;
      }
      if (phone.isEmpty()) {
        binding.etContactPhone.setError(isBn ? "অনুগ্রহ করে যোগাযোগের নম্বর দিন" : "Please enter contact phone");
        binding.etContactPhone.requestFocus();
        return;
      }

      binding.btnConfirmRequest.setEnabled(false);
      binding.tvConfirmRequestText.setText(isBn ? "জমা হচ্ছে..." : "Submitting...");

      submitRequestToBackend(
          activity,
          selectedBloodGroup,
          selectedUrgency,
          selectedProblemReason,
          selectedUnitsNeeded,
          selectedNeededDate,
          donationTime,
          hospital,
          phone,
          notes,
          () -> {
            dialog.dismiss();
            if (onCompleted != null) onCompleted.run();
          },
          () -> {
            binding.btnConfirmRequest.setEnabled(true);
            binding.tvConfirmRequestText.setText(isBn ? "আবেদন নিশ্চিত করুন" : "Confirm Request");
          }
      );
    });

    dialog.show();
  }

  private static void updateBloodGroupChips(TextView[] chips, String[] groups, String selected) {
    for (int i = 0; i < chips.length; i++) {
      if (groups[i].equals(selected)) {
        chips[i].setBackgroundResource(R.drawable.bg_blood_group_pill_red);
        chips[i].setTextColor(Color.WHITE);
      } else {
        chips[i].setBackgroundResource(R.drawable.bg_blood_group_pill_dark);
        chips[i].setTextColor(Color.parseColor("#94A3B8"));
      }
    }
  }

  private static void updateUrgencyDisplay(DialogBloodEmergencyRequestBinding binding, String urgency) {
    if ("EMERGENCY".equalsIgnoreCase(urgency)) {
      binding.btnUrgencyEmergency.setBackgroundResource(R.drawable.bg_blood_group_pill_red);
      binding.tvUrgencyEmergencyText.setTextColor(Color.WHITE);
      binding.ivUrgencyWarning.setImageTintList(ColorStateList.valueOf(Color.WHITE));

      binding.btnUrgencyNormal.setBackgroundResource(R.drawable.bg_blood_group_pill_dark);
      binding.tvUrgencyNormalText.setTextColor(Color.parseColor("#94A3B8"));
    } else {
      binding.btnUrgencyEmergency.setBackgroundResource(R.drawable.bg_blood_group_pill_dark);
      binding.tvUrgencyEmergencyText.setTextColor(Color.parseColor("#94A3B8"));
      binding.ivUrgencyWarning.setImageTintList(ColorStateList.valueOf(Color.parseColor("#94A3B8")));

      binding.btnUrgencyNormal.setBackgroundResource(R.drawable.bg_blood_group_pill_red);
      binding.tvUrgencyNormalText.setTextColor(Color.WHITE);
    }
  }

  private static void submitRequestToBackend(
      Activity activity,
      String bloodGroup,
      String urgency,
      String problemReason,
      String unitsNeeded,
      String neededDate,
      String donationTime,
      String hospitalAddress,
      String contactPhone,
      String notes,
      Runnable onSuccess,
      Runnable onError) {

    boolean isBn = LocaleManager.isBengali(activity);
    com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(activity);
    SharedPreferences profilePrefs = activity.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
    String userId = (session != null && session.userId != null && !session.userId.isEmpty())
        ? session.userId : profilePrefs.getString("profile_user_id", profilePrefs.getString("user_id", ""));
    String district = profilePrefs.getString("user_location", isBn ? "ঢাকা" : "Dhaka");

    com.devflux.deenone.data.local.AppDatabase.databaseWriteExecutor.execute(() -> {
      String url = BackendConfigManager.getPhpApiEndpoint(activity, "blood_donors.php");
      try {
        JsonObject json = new JsonObject();
        json.addProperty("action", "create_request");
        json.addProperty("user_id", userId);
        json.addProperty("blood_group", bloodGroup);
        json.addProperty("urgency_level", urgency);
        json.addProperty("problem_reason", problemReason);
        json.addProperty("units_needed", unitsNeeded);
        json.addProperty("needed_date", neededDate);
        json.addProperty("donation_time", donationTime);
        json.addProperty("hospital_name", hospitalAddress);
        json.addProperty("district", district);
        json.addProperty("contact_phone", contactPhone);
        json.addProperty("notes", notes);

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(activity))
            .build();

        try (Response response = client.newCall(request).execute()) {
          boolean ok = response.isSuccessful();
          String respBody = response.body() != null ? response.body().string() : "";

          if (!activity.isFinishing()) {
            activity.runOnUiThread(() -> {
              if (ok) {
                // If emergency, show prominent notification confirmation
                String successMsg = "EMERGENCY".equalsIgnoreCase(urgency)
                    ? (isBn
                        ? "জরুরি রক্তের আবেদন সফলভাবে প্রচারিত হয়েছে! সকল ব্যবহারকারীর নিকট নোটিফিকেশন পাঠানো হয়েছে।"
                        : "Emergency blood request broadcasted successfully! Notification dispatched to all app users.")
                    : (isBn
                        ? "রক্তের আবেদনটি সফলভাবে সিস্টেমে যুক্ত হয়েছে।"
                        : "Blood request registered successfully.");

                new MaterialAlertDialogBuilder(activity)
                    .setTitle(isBn ? "আবেদন সফল হয়েছে" : "Request Submitted")
                    .setMessage(successMsg)
                    .setPositiveButton(isBn ? "ঠিক আছে" : "OK", (d, w) -> {
                      if (onSuccess != null) onSuccess.run();
                    })
                    .setCancelable(false)
                    .show();

              } else {
                if (onError != null) onError.run();
                new MaterialAlertDialogBuilder(activity)
                    .setTitle(isBn ? "ত্রুটি" : "Error")
                    .setMessage(isBn ? "আবেদন জমা দিতে সমস্যা হয়েছে। অনুগ্রহ করে পুনরায় চেষ্টা করুন।" : "Failed to post request. Please try again.")
                    .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                    .show();
              }
            });
          }
        }
      } catch (Exception e) {
        if (!activity.isFinishing()) {
          activity.runOnUiThread(() -> {
            if (onError != null) onError.run();
            Toast.makeText(activity, (isBn ? "নেটওয়ার্ক ত্রুটি: " : "Network error: ") + e.getMessage(), Toast.LENGTH_SHORT).show();
          });
        }
      }
    });
  }
}
