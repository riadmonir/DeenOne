package com.devflux.deenone.core.halal;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.devflux.deenone.core.backend.BackendConfigManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HalalFoodManager {

    private static final String TAG = "HalalFoodManager";
    private static final String PREF_NAME = "deanone_halal_food_prefs";
    private static final String KEY_LAST_SYNC = "key_halal_last_sync";
    private static final String CACHE_FILE_NAME = "halal_foods_catalog_v2.json";

    private static volatile HalalFoodManager instance;
    private final List<HalalFoodItem> inMemoryCatalog = new ArrayList<>();
    private boolean isInitialized = false;

    public static HalalFoodManager getInstance() {
        if (instance == null) {
            synchronized (HalalFoodManager.class) {
                if (instance == null) {
                    instance = new HalalFoodManager();
                }
            }
        }
        return instance;
    }

    public synchronized List<HalalFoodItem> getAllItems(Context context) {
        if (!isInitialized || inMemoryCatalog.isEmpty()) {
            loadCatalog(context);
        }
        return new ArrayList<>(inMemoryCatalog);
    }

    private synchronized void loadCatalog(Context context) {
        inMemoryCatalog.clear();
        Map<String, HalalFoodItem> itemMap = new LinkedHashMap<>();

        // Load strictly from Local Cached DB (Synced from PHP MySQL Backend)
        if (context != null) {
            try {
                File cacheFile = new File(context.getFilesDir(), CACHE_FILE_NAME);
                if (cacheFile.exists()) {
                    FileInputStream fis = new FileInputStream(cacheFile);
                    byte[] buffer = new byte[(int) cacheFile.length()];
                    fis.read(buffer);
                    fis.close();

                    String jsonStr = new String(buffer, StandardCharsets.UTF_8);
                    JSONArray array = new JSONArray(jsonStr);
                    for (int i = 0; i < array.length(); i++) {
                        HalalFoodItem item = HalalFoodItem.fromJson(array.getJSONObject(i));
                        if (item != null && item.getId() != null && !item.getId().isEmpty()) {
                            itemMap.put(item.getId(), item);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading local halal cache: " + e.getMessage());
            }
        }

        inMemoryCatalog.addAll(itemMap.values());
        isInitialized = true;
    }

    public void syncWithRemoteSource(Context context, Runnable onComplete) {
        if (context == null) {
            if (onComplete != null) onComplete.run();
            return;
        }

        // Run network sync in background thread
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "halal_foods.php");
                URL url = new URL(endpoint + "?limit=1000");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("Accept", "application/json");

                int respCode = conn.getResponseCode();
                if (respCode == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(sb.toString());
                    if (jsonResponse.optBoolean("success", false) && jsonResponse.has("items")) {
                        JSONArray itemsArray = jsonResponse.getJSONArray("items");
                        Map<String, HalalFoodItem> itemMap = new LinkedHashMap<>();

                        for (int i = 0; i < itemsArray.length(); i++) {
                            HalalFoodItem item = HalalFoodItem.fromJson(itemsArray.getJSONObject(i));
                            if (item != null && item.getId() != null && !item.getId().isEmpty()) {
                                itemMap.put(item.getId(), item);
                            }
                        }

                        synchronized (HalalFoodManager.this) {
                            inMemoryCatalog.clear();
                            inMemoryCatalog.addAll(itemMap.values());
                            isInitialized = true;
                        }

                        // Save synced records to persistent local cache
                        JSONArray saveArray = new JSONArray();
                        for (HalalFoodItem itm : itemMap.values()) {
                            saveArray.put(itm.toJson());
                        }
                        File cacheFile = new File(context.getFilesDir(), CACHE_FILE_NAME);
                        FileOutputStream fos = new FileOutputStream(cacheFile);
                        fos.write(saveArray.toString().getBytes(StandardCharsets.UTF_8));
                        fos.close();

                        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                        prefs.edit().putLong(KEY_LAST_SYNC, System.currentTimeMillis()).apply();
                        Log.d(TAG, "Halal Food database synced successfully from PHP backend: " + itemMap.size() + " items.");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Remote halal sync error: " + e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        }).start();
    }

    public List<HalalFoodItem> searchAndFilter(Context context, String query, String categoryFilter, String statusFilter) {
        List<HalalFoodItem> all = getAllItems(context);
        List<HalalFoodItem> result = new ArrayList<>();

        String cleanQuery = query != null ? query.trim().toLowerCase() : "";
        String normQueryCode = cleanQuery.replace(" ", "").replace("-", "").replace("_", "");

        for (HalalFoodItem item : all) {
            // Category Filter
            if (categoryFilter != null && !categoryFilter.equals("ALL")) {
                if (!categoryFilter.equalsIgnoreCase(item.getCategory())) {
                    continue;
                }
            }

            // Status Filter
            if (statusFilter != null && !statusFilter.equals("ALL")) {
                if (!statusFilter.equalsIgnoreCase(item.getStatus())) {
                    continue;
                }
            }

            // Query Search (Bilingual search support)
            if (!cleanQuery.isEmpty()) {
                boolean matchTitle = (item.getTitle() != null && item.getTitle().toLowerCase().contains(cleanQuery))
                        || (item.getTitle(true) != null && item.getTitle(true).toLowerCase().contains(cleanQuery))
                        || (item.getTitle(false) != null && item.getTitle(false).toLowerCase().contains(cleanQuery));
                boolean matchArabic = item.getArabicName() != null && item.getArabicName().toLowerCase().contains(cleanQuery);
                boolean matchSci = (item.getScientificName(true) != null && item.getScientificName(true).toLowerCase().contains(cleanQuery))
                        || (item.getScientificName(false) != null && item.getScientificName(false).toLowerCase().contains(cleanQuery));
                boolean matchDesc = (item.getDescription(true) != null && item.getDescription(true).toLowerCase().contains(cleanQuery))
                        || (item.getDescription(false) != null && item.getDescription(false).toLowerCase().contains(cleanQuery));
                boolean matchSource = (item.getSourceOrigin(true) != null && item.getSourceOrigin(true).toLowerCase().contains(cleanQuery))
                        || (item.getSourceOrigin(false) != null && item.getSourceOrigin(false).toLowerCase().contains(cleanQuery));
                boolean matchBenefits = (item.getNutritionBenefits(true) != null && item.getNutritionBenefits(true).toLowerCase().contains(cleanQuery))
                        || (item.getNutritionBenefits(false) != null && item.getNutritionBenefits(false).toLowerCase().contains(cleanQuery));
                boolean matchAlt = (item.getHalalAlternative(true) != null && item.getHalalAlternative(true).toLowerCase().contains(cleanQuery))
                        || (item.getHalalAlternative(false) != null && item.getHalalAlternative(false).toLowerCase().contains(cleanQuery));

                boolean matchECode = false;
                if (item.getECode() != null && !item.getECode().isEmpty()) {
                    String normItemCode = item.getECode().toLowerCase().replace(" ", "").replace("-", "").replace("_", "");
                    if (normItemCode.contains(normQueryCode) || normQueryCode.contains(normItemCode)) {
                        matchECode = true;
                    }
                }

                if (!matchTitle && !matchArabic && !matchSci && !matchDesc && !matchSource && !matchBenefits && !matchAlt && !matchECode) {
                    continue;
                }
            }

            result.add(item);
        }

        return result;
    }
}