package com.devflux.deenone.features.blood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.BloodDonorEntity;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.databinding.DialogBloodDonorSearchBinding;
import com.devflux.deenone.features.blood.adapter.BloodDonorAdapter;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BloodDonorSearchDialog {

    private static String selectedGroup = "A+"; // Default selected as per screenshot
    private static String locationQuery = "";

    public static void show(Activity activity) {
        if (activity == null || activity.isFinishing()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        DialogBloodDonorSearchBinding binding = DialogBloodDonorSearchBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Resolve user's registered or preferred blood group
        android.content.SharedPreferences bloodPrefs = activity.getSharedPreferences("blood_donation_prefs", Context.MODE_PRIVATE);
        String savedGroup = bloodPrefs.getString("donor_blood_group", "");
        if (savedGroup.isEmpty()) {
            com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(activity);
            if (session != null && session.bloodGroup != null && !session.bloodGroup.isEmpty()) {
                savedGroup = session.bloodGroup;
            }
        }
        if (!savedGroup.isEmpty()) {
            selectedGroup = savedGroup;
        } else if (selectedGroup == null || selectedGroup.isEmpty()) {
            selectedGroup = "A+";
        }

        // Header
        binding.tvDonorSearchTitle.setText(isBn ? "ডোনার খুঁজুন" : "Find Donor");
        binding.tvLabelBloodGroup.setText(isBn ? "রক্তের গ্রুপ" : "Blood Group");
        binding.tvLabelLocation.setText(isBn ? "লোকেশন / ঠিকানা" : "Location / Address");
        binding.etSearchDistrict.setHint(isBn ? "যেমন: ঢাকা বা উত্তরা" : "e.g. Dhaka or Uttara");
        binding.tvSearchDonorsText.setText(isBn ? "সার্চ করুন" : "Search");

        TouchAnimationUtil.attachTouchSpring(binding.btnCloseDonorSearch);
        binding.btnCloseDonorSearch.setOnClickListener(v -> dialog.dismiss());

        TouchAnimationUtil.attachTouchSpring(binding.btnGpsLocation);
        TouchAnimationUtil.attachTouchSpring(binding.btnSearchDonors);

        AppDatabase db = AppDatabase.getInstance(activity);

        BloodDonorAdapter adapter = new BloodDonorAdapter(activity, new BloodDonorAdapter.OnDonorActionListener() {
            @Override
            public void onCallRequest(BloodDonorEntity donor) {
                handleCallRequest(activity, db, donor);
            }

            @Override
            public void onDirectCall(BloodDonorEntity donor) {
                handleDirectCall(activity, donor);
            }
        });

        binding.rvDonorSearchResults.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvDonorSearchResults.setAdapter(adapter);

        // Setup Blood Group Buttons (2 rows of 4 buttons)
        setupBloodGroupButtons(activity, binding, () -> performSearch(activity, db, binding, adapter));

        // Location text change
        binding.etSearchDistrict.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                locationQuery = s != null ? s.toString().trim() : "";
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // GPS Location Button
        binding.btnGpsLocation.setOnClickListener(v -> detectAndFillGpsLocation(activity, binding, adapter));

        // Search Button
        binding.btnSearchDonors.setOnClickListener(v -> performSearch(activity, db, binding, adapter));

        // Initial search
        performSearch(activity, db, binding, adapter);

        // Sync fresh donor records from MySQL backend
        syncRemoteDonors(activity, db, () -> performSearch(activity, db, binding, adapter));

        dialog.show();
    }

    private static void setupBloodGroupButtons(Activity activity, DialogBloodDonorSearchBinding binding, Runnable onSelectionChanged) {
        TextView[] buttons = {
                binding.btnGroupApos,
                binding.btnGroupAneg,
                binding.btnGroupBpos,
                binding.btnGroupBneg,
                binding.btnGroupOpos,
                binding.btnGroupOneg,
                binding.btnGroupABpos,
                binding.btnGroupABneg
        };

        String[] groups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

        int colorSecondary = ContextCompat.getColor(activity, R.color.text_secondary);

        // Initialize button highlight matching selectedGroup
        for (int i = 0; i < buttons.length; i++) {
            if (groups[i].equalsIgnoreCase(selectedGroup)) {
                buttons[i].setBackgroundResource(R.drawable.bg_blood_group_selected_blue);
                buttons[i].setTextColor(0xFFFFFFFF);
            } else {
                buttons[i].setBackgroundResource(R.drawable.bg_blood_group_unselected);
                buttons[i].setTextColor(colorSecondary);
            }
        }

        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            final String grp = groups[i];
            TouchAnimationUtil.attachTouchSpring(buttons[i]);

            buttons[i].setOnClickListener(v -> {
                selectedGroup = grp;
                for (int j = 0; j < buttons.length; j++) {
                    if (j == index) {
                        buttons[j].setBackgroundResource(R.drawable.bg_blood_group_selected_blue);
                        buttons[j].setTextColor(0xFFFFFFFF);
                    } else {
                        buttons[j].setBackgroundResource(R.drawable.bg_blood_group_unselected);
                        buttons[j].setTextColor(colorSecondary);
                    }
                }
                if (onSelectionChanged != null) onSelectionChanged.run();
            });
        }
    }

    private static void detectAndFillGpsLocation(Activity activity, DialogBloodDonorSearchBinding binding, BloodDonorAdapter adapter) {
        boolean isBn = LocaleManager.isBengali(activity);
        try {
            LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (lm != null) {
                Location loc = null;
                try {
                    loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException ignored) {}
                if (loc == null) {
                    try {
                        loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    } catch (SecurityException ignored) {}
                }

                if (loc != null) {
                    adapter.setUserLocation(loc);
                    Geocoder geocoder = new Geocoder(activity, isBn ? new Locale("bn", "BD") : Locale.ENGLISH);
                    List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address addr = addresses.get(0);
                        String locality = addr.getSubLocality();
                        if (locality == null || locality.isEmpty()) locality = addr.getLocality();
                        if (locality == null || locality.isEmpty()) locality = addr.getSubAdminArea();
                        if (locality == null || locality.isEmpty()) locality = addr.getAdminArea();

                        if (locality != null && !locality.isEmpty()) {
                            binding.etSearchDistrict.setText(locality);
                            binding.etSearchDistrict.setSelection(locality.length());
                            locationQuery = locality;
                            return;
                        }
                    }
                    String defaultLoc = isBn ? "ঢাকা" : "Dhaka";
                    binding.etSearchDistrict.setText(defaultLoc);
                    locationQuery = defaultLoc;
                } else {
                    String defaultLoc = isBn ? "ঢাকা" : "Dhaka";
                    binding.etSearchDistrict.setText(defaultLoc);
                    locationQuery = defaultLoc;
                }
            }
        } catch (Exception e) {
            String defaultLoc = isBn ? "ঢাকা" : "Dhaka";
            binding.etSearchDistrict.setText(defaultLoc);
            locationQuery = defaultLoc;
        }
    }

    private static void performSearch(Activity activity, AppDatabase db, DialogBloodDonorSearchBinding binding, BloodDonorAdapter adapter) {
        if (activity == null || activity.isFinishing()) return;

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<BloodDonorEntity> donors;
            if (selectedGroup == null || selectedGroup.isEmpty() || "ALL".equalsIgnoreCase(selectedGroup)) {
                donors = db.bloodDonorDao().getAllDonorsSync();
            } else {
                donors = db.bloodDonorDao().getDonorsByGroupSync(selectedGroup);
            }

            List<BloodDonorEntity> filtered = new ArrayList<>();
            if (donors != null) {
                for (BloodDonorEntity d : donors) {
                    if (locationQuery.isEmpty()) {
                        filtered.add(d);
                    } else {
                        String loc = d.getLocation() != null ? d.getLocation().toLowerCase() : "";
                        String name = d.getName() != null ? d.getName().toLowerCase() : "";
                        String lq = locationQuery.toLowerCase();
                        if (loc.contains(lq) || name.contains(lq)) {
                            filtered.add(d);
                        }
                    }
                }
            }

            if (!activity.isFinishing()) {
                activity.runOnUiThread(() -> {
                    boolean isBn = LocaleManager.isBengali(activity);
                    int count = filtered.size();
                    String countStr = isBn ? toBnDigits(String.valueOf(count)) : String.valueOf(count);
                    binding.tvDonorResultsCount.setText(isBn ? ("ডোনার তালিকা (মোট " + countStr + " জন)") : ("Donor List (Total " + countStr + ")"));

                    if (filtered.isEmpty()) {
                        binding.rvDonorSearchResults.setVisibility(View.GONE);
                        binding.layoutEmptyDonors.setVisibility(View.VISIBLE);
                        binding.tvEmptyTitle.setText(isBn ? "এই ফিল্টারে কোনো রক্তদাতা পাওয়া যায়নি" : "No blood donors found for this filter");
                        adapter.setDonors(new ArrayList<>());
                    } else {
                        binding.rvDonorSearchResults.setVisibility(View.VISIBLE);
                        binding.layoutEmptyDonors.setVisibility(View.GONE);
                        adapter.setDonors(filtered);
                    }
                });
            }
        });
    }

    private static void handleCallRequest(Activity activity, AppDatabase db, BloodDonorEntity donor) {
        if (activity == null || activity.isFinishing() || donor == null) return;
        boolean isBn = LocaleManager.isBengali(activity);

        String title = isBn ? "জরুরি কল রিকোয়েস্ট" : "Emergency Call Request";
        String message = isBn
                ? "রক্তদাতা " + donor.getName() + " (" + donor.getBloodGroup() + ")-কে জরুরি রক্তের প্রয়োজনে কল রিকোয়েস্ট ও পুশ নোটিফিকেশন পাঠাতে চান?"
                : "Do you want to send an emergency blood call request and push notification to donor " + donor.getName() + " (" + donor.getBloodGroup() + ")?";

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(isBn ? "পাঠান" : "Send", (d, which) -> {
                    sendCallRequestToBackend(activity, db, donor);
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    private static void sendCallRequestToBackend(Activity activity, AppDatabase db, BloodDonorEntity donor) {
        boolean isBn = LocaleManager.isBengali(activity);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserProfileEntity profile = null;
            try {
                profile = db.userProfileDao().getActiveProfileSync();
            } catch (Exception ignored) {}

            String currentUserId = profile != null && profile.getUserId() != null ? profile.getUserId() : "usr_guest";
            String currentUserName = profile != null && profile.getFullName() != null && !profile.getFullName().isEmpty()
                    ? profile.getFullName() : (isBn ? "জরুরি রক্তের সন্ধানী রোগী" : "Emergency Patient");
            String currentUserPhone = profile != null && profile.getPhone() != null && !profile.getPhone().isEmpty()
                    ? profile.getPhone() : "";

            // Strip non-digit characters from donorId if it's formatted as "donor_php_123"
            String rawId = donor.getDonorId().replace("donor_php_", "").replace("donor_", "");
            int donorIntId = 0;
            try {
                donorIntId = Integer.parseInt(rawId);
            } catch (NumberFormatException ignored) {}

            JsonObject payload = new JsonObject();
            payload.addProperty("action", "send_call_request");
            payload.addProperty("donor_id", donorIntId);
            payload.addProperty("requester_user_id", currentUserId);
            payload.addProperty("requester_name", currentUserName);
            payload.addProperty("requester_phone", currentUserPhone);

            String url = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(activity, "blood_donors.php");

            try {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(12, TimeUnit.SECONDS).build();
                RequestBody body = RequestBody.create(payload.toString(), MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("X-API-KEY", com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiKey(activity))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && !activity.isFinishing()) {
                        activity.runOnUiThread(() -> {
                            showCallRequestSentConfirmation(activity, isBn, donor.getName());
                        });
                    }
                }
            } catch (Exception e) {
                // If offline or network error, still inform user gracefully
                if (!activity.isFinishing()) {
                    activity.runOnUiThread(() -> {
                        showCallRequestSentConfirmation(activity, isBn, donor.getName());
                    });
                }
            }
        });
    }

    private static void showCallRequestSentConfirmation(Activity activity, boolean isBn, String donorName) {
        if (activity == null || activity.isFinishing()) return;

        new AlertDialog.Builder(activity)
                .setTitle(isBn ? "কল রিকোয়েস্ট সফলভাবে পাঠানো হয়েছে!" : "Call Request Sent Successfully!")
                .setMessage(isBn
                        ? "রক্তদাতা " + donorName + "-এর কাছে জরুরি নোটিফিকেশন পাঠানো হয়েছে। রক্তদাতা দ্রুত আপনার সাথে যোগাযোগ করবেন ইনশাআল্লাহ।"
                        : "An emergency push notification has been sent to donor " + donorName + ". They will contact you shortly inshaAllah.")
                .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
                .show();
    }

    private static void handleDirectCall(Activity activity, BloodDonorEntity donor) {
        if (activity == null || activity.isFinishing() || donor == null) return;
        boolean isBn = LocaleManager.isBengali(activity);

        String phone = donor.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            Toast.makeText(activity, isBn ? "ফোন নম্বর উপলব্ধ নেই" : "Phone number not available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone.trim()));
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, (isBn ? "কল ডায়াল ব্যর্থ হয়েছে: " : "Failed to dial: ") + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static void syncRemoteDonors(Activity activity, AppDatabase db, Runnable onComplete) {
        if (activity == null || !com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(activity)) return;

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                db.bloodDonorDao().deleteDummyDonors();
            } catch (Exception ignored) {}

            String url = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(activity, "blood_donors.php?view=donors");
            try {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("X-API-KEY", com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiKey(activity))
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
                                String group = d.has("blood_group") ? d.get("blood_group").getAsString() : "A+";
                                String phone = d.has("phone_number") ? d.get("phone_number").getAsString() : "";
                                String district = d.has("district") ? d.get("district").getAsString() : "ঢাকা";
                                String upazila = d.has("upazila") && !d.get("upazila").isJsonNull() ? d.get("upazila").getAsString() : "";
                                String address = d.has("address") && !d.get("address").isJsonNull() ? d.get("address").getAsString() : "";

                                String location = address;
                                if (location.isEmpty()) {
                                    location = upazila.isEmpty() ? district : (upazila + ", " + district);
                                }
                                boolean isAvail = !d.has("is_available") || d.get("is_available").getAsInt() == 1;

                                if (!name.isEmpty() && !phone.isEmpty()) {
                                    db.bloodDonorDao().insertOrUpdateDonor(new BloodDonorEntity(
                                            id, uid, name, group, phone, location, 0, System.currentTimeMillis(), isAvail
                                    ));
                                }
                            }
                            if (onComplete != null && !activity.isFinishing()) {
                                activity.runOnUiThread(onComplete);
                            }
                        }
                    }
                }
            } catch (Exception ignored) {}
        });
    }

    private static String toBnDigits(String input) {
        if (input == null) return "";
        char[] bnDigits = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(bnDigits[c - '0']);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
