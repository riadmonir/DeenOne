package com.devflux.deenone.features.blood;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.databinding.DialogBloodCommunityOrganizationsBinding;
import com.devflux.deenone.features.blood.adapter.BloodOrganizationAdapter;
import com.devflux.deenone.features.blood.model.BloodOrganizationModel;
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

public class BloodCommunityOrganizationsDialog {

  public static void show(Activity activity) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    DialogBloodCommunityOrganizationsBinding binding = DialogBloodCommunityOrganizationsBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isBn = LocaleManager.isBengali(activity);
    binding.tvCommunityOrgsHeaderTitle.setText(isBn ? "রক্তদাতা কমিউনিটি" : "Blood Donor Community");
    binding.tvCommunitySubNote.setText(isBn
        ? "আপনার কাছাকাছি রক্তদাতা সংগঠন, স্বেচ্ছাসেবী গ্রুপ ও জরুরি ব্লাড ব্যাংক হটলাইনসমূহ"
        : "Blood donor organizations, volunteer groups, and emergency blood bank hotlines near you");

    TouchAnimationUtil.attachTouchSpring(binding.btnCloseCommunityOrgs);
    binding.btnCloseCommunityOrgs.setOnClickListener(v -> dialog.dismiss());

    BloodOrganizationAdapter adapter = new BloodOrganizationAdapter(activity);
    binding.rvCommunityOrgs.setLayoutManager(new LinearLayoutManager(activity));
    binding.rvCommunityOrgs.setAdapter(adapter);

    // Initial default verified organizations
    adapter.setOrganizations(getDefaultOrganizations());

    // Sync from MySQL backend
    fetchRemoteOrganizations(activity, adapter);

    dialog.show();
  }

  private static List<BloodOrganizationModel> getDefaultOrganizations() {
    List<BloodOrganizationModel> list = new ArrayList<>();
    list.add(new BloodOrganizationModel(1, "সন্ধানী (কেন্দ্রীয় পরিষদ)", "Sandhani Central Committee", "স্বেচ্ছাসেবী রক্তদান সংস্থা", "ঢাকা", "ঢাকা মেডিকেল কলেজ, ঢাকা", "01711-000000", "02-9668690", "https://sandhani.org", true));
    list.add(new BloodOrganizationModel(2, "কোয়ান্টাম রক্তদান কার্যক্রম", "Quantum Blood Lab", "ব্লাড ল্যাব ও সেবা", "ঢাকা", "শান্তিনগর, ঢাকা", "01714-010869", "02-9351969", "https://quantummethod.org.bd", true));
    list.add(new BloodOrganizationModel(3, "বাংলাদেশ রেড ক্রিসেন্ট সোসাইটি ব্লাড সেন্টার", "Bangladesh Red Crescent Blood Center", "জাতীয় রক্তদান সংস্থা", "ঢাকা", "৭/৫ আওরঙ্গজেব রোড, মোহাম্মদপুর, ঢাকা", "01811-458524", "02-9116563", "https://redcrescent.org.bd", true));
    list.add(new BloodOrganizationModel(4, "বাঁধন (স্বেচ্ছায় রক্তদাতাদের সংগঠন)", "Badhan (A Voluntary Blood Donors Organization)", "স্বেচ্ছাসেবী ছাত্র সংগঠন", "ঢাকা", "টিএসসি, ঢাকা বিশ্ববিদ্যালয়, ঢাকা", "01534-982674", "01711-234567", "https://badhan.org.bd", true));
    list.add(new BloodOrganizationModel(5, "পুলিশ ব্লাড ব্যাংক", "Police Blood Bank", "সরকারি জরুরি ব্লাড ব্যাংক", "ঢাকা", "কেন্দ্রীয় পুলিশ হাসপাতাল, রাজারবাগ, ঢাকা", "01713-398386", "02-9350020", null, true));
    list.add(new BloodOrganizationModel(6, "সন্ধানী (চট্টগ্রাম মেডিকেল কলেজ ইউনিট)", "Sandhani CMC Unit", "স্বেচ্ছাসেবী রক্তদান সংস্থা", "চট্টগ্রাম", "চট্টগ্রাম মেডিকেল কলেজ হাসপাতাল", "01819-123456", "031-619400", null, true));
    list.add(new BloodOrganizationModel(7, "বাঁধন (রাজশাহী বিশ্ববিদ্যালয় ইউনিট)", "Badhan RU Unit", "স্বেচ্ছাসেবী ছাত্র সংগঠন", "রাজশাহী", "টিএসসিসি, রাজশাহী বিশ্ববিদ্যালয়", "01720-987654", null, null, true));
    list.add(new BloodOrganizationModel(8, "ব্লাডম্যান বাংলাদেশ", "Bloodman Bangladesh", "ডিজিটাল রক্তদাতা প্ল্যাটফর্ম", "ঢাকা", "বনানী, ঢাকা", "01755-667788", null, "https://bloodman.org", true));
    return list;
  }

  private static void fetchRemoteOrganizations(Activity activity, BloodOrganizationAdapter adapter) {
    if (activity == null || !com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(activity)) return;

    AppDatabase.databaseWriteExecutor.execute(() -> {
      String url = BackendConfigManager.getPhpApiEndpoint(activity, "blood_donors.php?view=organizations");
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
            if (obj != null && obj.has("organizations")) {
              JsonArray arr = obj.getAsJsonArray("organizations");
              List<BloodOrganizationModel> fetched = new ArrayList<>();
              for (JsonElement el : arr) {
                JsonObject o = el.getAsJsonObject();
                int id = o.has("id") ? o.get("id").getAsInt() : 0;
                String nameBn = o.has("name_bn") ? o.get("name_bn").getAsString() : "";
                String nameEn = o.has("name_en") ? o.get("name_en").getAsString() : nameBn;
                String cat = o.has("category") ? o.get("category").getAsString() : "স্বেচ্ছাসেবী সংস্থা";
                String dist = o.has("district") ? o.get("district").getAsString() : "ঢাকা";
                String addr = o.has("address") && !o.get("address").isJsonNull() ? o.get("address").getAsString() : "";
                String phone = o.has("hotline_phone") ? o.get("hotline_phone").getAsString() : "";
                String alt = o.has("alt_phone") && !o.get("alt_phone").isJsonNull() ? o.get("alt_phone").getAsString() : null;
                String web = o.has("website") && !o.get("website").isJsonNull() ? o.get("website").getAsString() : null;
                boolean ver = !o.has("is_verified") || o.get("is_verified").getAsInt() == 1;

                fetched.add(new BloodOrganizationModel(id, nameBn, nameEn, cat, dist, addr, phone, alt, web, ver));
              }

              if (!fetched.isEmpty() && !activity.isFinishing()) {
                activity.runOnUiThread(() -> adapter.setOrganizations(fetched));
              }
            }
          }
        }
      } catch (Exception ignored) {}
    });
  }
}
