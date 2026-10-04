package com.devflux.deenone.features.blood;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.databinding.DialogBloodAllRequestsBinding;
import com.devflux.deenone.features.blood.adapter.BloodRequestAdapter;
import com.devflux.deenone.features.blood.model.BloodRequestModel;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BloodAllRequestsDialog {

  private static String currentFilterGroup = "ALL";

  public static void show(Activity activity) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    DialogBloodAllRequestsBinding binding = DialogBloodAllRequestsBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isBn = LocaleManager.isBengali(activity);
    currentFilterGroup = "ALL";

    binding.tvAllRequestsHeaderTitle.setText(isBn ? "রক্তের সব আবেদন" : "All Blood Requests");

    TouchAnimationUtil.attachTouchSpring(binding.btnCloseAllRequests);
    binding.btnCloseAllRequests.setOnClickListener(v -> dialog.dismiss());

    final BloodRequestAdapter[] adapterHolder = new BloodRequestAdapter[1];
    BloodRequestAdapter adapter = new BloodRequestAdapter(activity, model -> {
      BloodRequestDetailDialog.show(activity, model, () -> {
        if (adapterHolder[0] != null) {
          fetchRemoteRequests(activity, binding, adapterHolder[0]);
        }
      });
    });
    adapterHolder[0] = adapter;

    binding.rvAllBloodRequests.setLayoutManager(new LinearLayoutManager(activity));
    binding.rvAllBloodRequests.setAdapter(adapter);

    setupFilterChips(activity, binding, () -> fetchRemoteRequests(activity, binding, adapter));

    fetchRemoteRequests(activity, binding, adapter);

    dialog.show();
  }

  private static void setupFilterChips(Activity activity, DialogBloodAllRequestsBinding binding, Runnable onFilterChanged) {
    boolean isBn = LocaleManager.isBengali(activity);
    String allText = isBn ? "সকল" : "All";

    TextView[] chips = {
        binding.chipReqAll,
        binding.chipReqAPos,
        binding.chipReqBPos,
        binding.chipReqOPos,
        binding.chipReqABPos
    };

    String[] groupValues = {"ALL", "A+", "B+", "O+", "AB+"};
    int colorUnselectedBg = ContextCompat.getColor(activity, R.color.bg_card);
    int colorUnselectedText = ContextCompat.getColor(activity, R.color.text_primary);

    for (int i = 0; i < chips.length; i++) {
      final int index = i;
      chips[i].setText(groupValues[i].equals("ALL") ? allText : groupValues[i]);
      TouchAnimationUtil.attachTouchSpring(chips[i]);
      chips[i].setOnClickListener(v -> {
        currentFilterGroup = groupValues[index];

        for (int j = 0; j < chips.length; j++) {
          if (j == index) {
            chips[j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DC2626")));
            chips[j].setTextColor(Color.parseColor("#FFFFFF"));
          } else {
            chips[j].setBackgroundTintList(ColorStateList.valueOf(colorUnselectedBg));
            chips[j].setTextColor(colorUnselectedText);
          }
        }
        if (onFilterChanged != null) onFilterChanged.run();
      });
    }
  }

  private static void fetchRemoteRequests(Activity activity, DialogBloodAllRequestsBinding binding, BloodRequestAdapter adapter) {
    if (activity == null || activity.isFinishing()) return;

    AppDatabase.databaseWriteExecutor.execute(() -> {
      String groupQuery = "ALL".equalsIgnoreCase(currentFilterGroup) ? "" : ("&group=" + currentFilterGroup);
      String url = BackendConfigManager.getPhpApiEndpoint(activity, "blood_donors.php?view=requests" + groupQuery);

      List<BloodRequestModel> list = new ArrayList<>();
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
            if (obj != null && obj.has("requests")) {
              JsonArray arr = obj.getAsJsonArray("requests");
              for (JsonElement el : arr) {
                JsonObject r = el.getAsJsonObject();
                int id = r.has("id") ? r.get("id").getAsInt() : 0;
                String uid = r.has("requester_user_id") && !r.get("requester_user_id").isJsonNull() ? r.get("requester_user_id").getAsString() : "";
                String patient = r.has("patient_name") ? r.get("patient_name").getAsString() : "";
                String group = r.has("blood_group") ? r.get("blood_group").getAsString() : "A+";
                int units = r.has("units_needed") ? r.get("units_needed").getAsInt() : 1;
                String hospital = r.has("hospital_name") ? r.get("hospital_name").getAsString() : "";
                String district = r.has("district") ? r.get("district").getAsString() : "ঢাকা";
                String upazila = r.has("upazila") && !r.get("upazila").isJsonNull() ? r.get("upazila").getAsString() : "";
                String phone = r.has("contact_phone") ? r.get("contact_phone").getAsString() : "";
                String whatsapp = r.has("whatsapp_number") && !r.get("whatsapp_number").isJsonNull() ? r.get("whatsapp_number").getAsString() : phone;
                String urgency = r.has("urgency_level") ? r.get("urgency_level").getAsString() : "EMERGENCY";
                String neededDate = r.has("needed_date") && !r.get("needed_date").isJsonNull() ? r.get("needed_date").getAsString() : "";
                String status = r.has("status") ? r.get("status").getAsString() : "OPEN";

                list.add(new BloodRequestModel(
                    id, uid, patient, group, units, hospital, district, upazila, phone, whatsapp, urgency, neededDate, status, System.currentTimeMillis()
                ));
              }
            }
          }
        }
      } catch (Exception ignored) {}

      if (!activity.isFinishing()) {
        activity.runOnUiThread(() -> {
          boolean isBn = LocaleManager.isBengali(activity);
          binding.tvAllReqResultCount.setText((isBn ? "চলতি রক্তের আবেদন: " : "Active Blood Requests: ") + list.size() + (isBn ? " টি" : ""));

          if (list.isEmpty()) {
            binding.rvAllBloodRequests.setVisibility(View.GONE);
            binding.layoutAllReqEmptyState.setVisibility(View.VISIBLE);
            binding.tvAllReqEmptyMsg.setText(isBn ? "বর্তমানে কোনো জরুরি রক্তের আবেদন নেই।" : "No blood requests available.");
            adapter.setRequests(new ArrayList<>());
          } else {
            binding.rvAllBloodRequests.setVisibility(View.VISIBLE);
            binding.layoutAllReqEmptyState.setVisibility(View.GONE);
            adapter.setRequests(list);
          }
        });
      }
    });
  }
}
