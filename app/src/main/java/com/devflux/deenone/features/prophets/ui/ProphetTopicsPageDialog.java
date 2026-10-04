package com.devflux.deenone.features.prophets.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.PageProphetsOverviewBinding;
import com.devflux.deenone.features.prophets.adapter.ProphetOverviewAdapter;
import com.devflux.deenone.features.prophets.data.ProphetsContentRepository;
import com.devflux.deenone.features.prophets.model.ProphetOverviewTopicItem;
import com.devflux.deenone.features.prophets.model.ProphetStoryItem;
import com.devflux.deenone.utils.TouchAnimationUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ProphetTopicsPageDialog {

    public static void show(@NonNull Activity activity, ProphetStoryItem prophetItem) {
        if (prophetItem == null || activity.isFinishing() || activity.isDestroyed()) return;

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageProphetsOverviewBinding binding = PageProphetsOverviewBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Title matching Screenshot: "← হযরত আদম (আ:)" / "← Prophet Adam (AS)"
        String title = isBn ? prophetItem.getBengaliName() : prophetItem.getEnglishName();
        binding.tvOverviewTitle.setText(title);

        // Back Button with Touch Spring
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseOverview);
        binding.btnCloseOverview.setOnClickListener(v -> dialog.dismiss());

        // Offline-first load from Repository
        List<ProphetOverviewTopicItem> list = new ArrayList<>(
                ProphetsContentRepository.getTopicsForProphet(prophetItem.getId())
        );
        ProphetOverviewAdapter adapter = new ProphetOverviewAdapter(activity, list);

        binding.rvOverviewCards.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvOverviewCards.setAdapter(adapter);

        // Background server sync for this prophet's topics
        syncWithServer(activity, prophetItem.getId(), list, adapter);

        dialog.show();
    }

    private static void syncWithServer(Context context, int prophetId, List<ProphetOverviewTopicItem> localList, ProphetOverviewAdapter adapter) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_prophet_topics.php") + "?prophet_id=" + prophetId;
                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject response = new JSONObject(sb.toString());
                    if (response.optBoolean("success", false) && response.has("data")) {
                        JSONArray data = response.getJSONArray("data");
                        List<ProphetOverviewTopicItem> serverList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            serverList.add(new ProphetOverviewTopicItem(
                                    obj.optInt("id", i + 1),
                                    obj.optString("title_bn", ""),
                                    obj.optString("title_en", ""),
                                    obj.optString("content_bn", ""),
                                    obj.optString("content_en", "")
                            ));
                        }

                        if (!serverList.isEmpty()) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                localList.clear();
                                localList.addAll(serverList);
                                adapter.setItems(serverList);
                            });
                        }
                    }
                }
            } catch (Exception ignored) {}
        });
    }
}
