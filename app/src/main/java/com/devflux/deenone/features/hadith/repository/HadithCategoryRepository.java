package com.devflux.deenone.features.hadith.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.hadith.model.HadithBookCategory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HadithCategoryRepository {

    private static final String TAG = "HadithCategoryRepository";
    private static final String PREF_HADITH_CAT = "pref_hadith_category_cache";
    private static final String KEY_CACHED_CATEGORIES = "key_hadith_categories_json";

    private static volatile HadithCategoryRepository instance;
    private static volatile List<HadithBookCategory> memoryCache = null;

    private final ExecutorService diskDbExecutor = Executors.newFixedThreadPool(2);
    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Gson gson = new Gson();

    public interface CategoryCallback {
        void onLoaded(List<HadithBookCategory> categories);
    }

    private HadithCategoryRepository() {
        if (memoryCache == null) {
            memoryCache = Collections.synchronizedList(new ArrayList<>(getDefaultCategories()));
        }
    }

    public static HadithCategoryRepository getInstance() {
        if (instance == null) {
            synchronized (HadithCategoryRepository.class) {
                if (instance == null) {
                    instance = new HadithCategoryRepository();
                }
            }
        }
        return instance;
    }

    /**
     * Get 25 Hadith Categories:
     * 1. Returns instant cached memory data (0ms, 60 FPS, lag-free).
     * 2. Asynchronously refreshes local Room database counts in the background.
     * 3. Asynchronously syncs remote server counts on a separate network thread.
     */
    public void getCategories(Context context, CategoryCallback callback) {
        if (context == null) return;
        final Context appContext = context.getApplicationContext();

        // 1. Instant 0ms memory cache callback for smooth, zero-delay UI rendering
        if (memoryCache != null && !memoryCache.isEmpty()) {
            final List<HadithBookCategory> instantList = new ArrayList<>(memoryCache);
            if (Looper.myLooper() == Looper.getMainLooper()) {
                if (callback != null) callback.onLoaded(instantList);
            } else {
                mainHandler.post(() -> {
                    if (callback != null) callback.onLoaded(instantList);
                });
            }
        }

        // 2. Refresh from local database & disk cache asynchronously
        diskDbExecutor.execute(() -> {
            List<HadithBookCategory> list = new ArrayList<>();
            SharedPreferences prefs = appContext.getSharedPreferences(PREF_HADITH_CAT, Context.MODE_PRIVATE);
            String cachedJson = prefs.getString(KEY_CACHED_CATEGORIES, null);

            if (cachedJson != null && !cachedJson.trim().isEmpty()) {
                try {
                    Type listType = new TypeToken<List<HadithBookCategory>>(){}.getType();
                    list = gson.fromJson(cachedJson, listType);
                } catch (Exception ignored) {}
            }

            if (list == null || list.isEmpty()) {
                list = getDefaultCategories();
            }

            // Trigger background SQLite Hadith Database Manager initialization from GitHub CDN
            HadithDatabaseManager.getInstance(appContext).ensureDatabaseAvailable(null);

            // Synchronize Room database count without overwriting canonical authentic totals
            try {
                com.devflux.deenone.data.local.AppDatabase db = com.devflux.deenone.data.local.AppDatabase.getInstance(appContext);
                List<com.devflux.deenone.data.local.dao.HadithDao.CollectionCount> countList = db.hadithDao().getAllCollectionCounts();
                java.util.Map<String, Integer> countMap = new java.util.HashMap<>();
                if (countList != null) {
                    for (com.devflux.deenone.data.local.dao.HadithDao.CollectionCount item : countList) {
                        if (item.collectionId != null) {
                            countMap.put(item.collectionId.toLowerCase(), item.count);
                        }
                    }
                }

                for (HadithBookCategory cat : list) {
                    String slug = cat.getSlug() != null ? cat.getSlug().toLowerCase() : "";
                    int count = countMap.getOrDefault(slug, 0);
                    if (count == 0) {
                        if ("abu_dawood".equals(slug) && countMap.containsKey("abudawud")) {
                            count = countMap.get("abudawud");
                        } else if ("nawawi_40".equals(slug) && countMap.containsKey("nawawi40")) {
                            count = countMap.get("nawawi40");
                        }
                    }
                    if (count > cat.getTotalHadith()) {
                        cat.setTotalHadith(count);
                    }
                }
            } catch (Exception ignored) {}

            memoryCache = Collections.synchronizedList(new ArrayList<>(list));
            final List<HadithBookCategory> updatedList = new ArrayList<>(list);

            mainHandler.post(() -> {
                if (callback != null) {
                    callback.onLoaded(updatedList);
                }
            });

            // 3. Fetch live data from remote on dedicated network thread
            networkExecutor.execute(() -> fetchRemoteCategories(appContext, callback));
        });
    }

    private void fetchRemoteCategories(Context context, CategoryCallback callback) {
        if (!NetworkConnectivityHelper.isOnline(context)) {
            return;
        }

        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_hadith_categories.php");
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3500);
            conn.setReadTimeout(3500);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);
                if (json != null && json.has("success") && json.get("success").getAsBoolean()) {
                    JsonArray arr = json.has("categories") ? json.getAsJsonArray("categories") : new JsonArray();
                    List<HadithBookCategory> remoteList = new ArrayList<>();

                    for (JsonElement el : arr) {
                        JsonObject obj = el.getAsJsonObject();
                        String slug = obj.has("slug") ? obj.get("slug").getAsString() : "";
                        String nameBn = obj.has("name_bn") ? obj.get("name_bn").getAsString() : "";
                        String nameEn = obj.has("name_en") ? obj.get("name_en").getAsString() : "";
                        String authorBn = obj.has("author_bn") ? obj.get("author_bn").getAsString() : "";
                        String authorEn = obj.has("author_en") ? obj.get("author_en").getAsString() : "";
                        String initials = obj.has("initials") ? obj.get("initials").getAsString() : "";
                        String colorHex = obj.has("color_hex") ? obj.get("color_hex").getAsString() : "#10B981";
                        int total = obj.has("total_hadith") ? obj.get("total_hadith").getAsInt() : 0;
                        int order = obj.has("display_order") ? obj.get("display_order").getAsInt() : remoteList.size() + 1;

                        remoteList.add(new HadithBookCategory(slug, nameBn, nameEn, authorBn, authorEn, initials, colorHex, total, order));
                    }

                    if (!remoteList.isEmpty()) {
                        SharedPreferences prefs = context.getSharedPreferences(PREF_HADITH_CAT, Context.MODE_PRIVATE);
                        prefs.edit().putString(KEY_CACHED_CATEGORIES, gson.toJson(remoteList)).apply();
                        memoryCache = Collections.synchronizedList(new ArrayList<>(remoteList));

                        final List<HadithBookCategory> finalList = new ArrayList<>(remoteList);
                        mainHandler.post(() -> {
                            if (callback != null) {
                                callback.onLoaded(finalList);
                            }
                        });
                    }
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.w(TAG, "fetchRemoteCategories note: " + e.getMessage());
        }
    }

    public static List<HadithBookCategory> getDefaultCategories() {
        List<HadithBookCategory> list = new ArrayList<>();

        // 1 - 7: Kutub as-Sittah & Muwatta
        list.add(new HadithBookCategory("bukhari", "সহীহ বুখারী", "Sahih Bukhari", "ইমাম বুখারি", "Imam Bukhari", "B", "#22C55E", 7563, 1));
        list.add(new HadithBookCategory("muslim", "সহীহ মুসলিম", "Sahih Muslim", "ইমাম মুসলিম", "Imam Muslim", "M", "#0284C7", 7500, 2));
        list.add(new HadithBookCategory("nasai", "সুনানে আন-নাসায়ী", "Sunan an-Nasa'i", "ইমাম নাসায়ী", "Imam Nasa'i", "N", "#0EA5E9", 5760, 3));
        list.add(new HadithBookCategory("abu_dawood", "সুনানে আবু দাউদ", "Sunan Abu Dawood", "ইমাম আবু দাউদ", "Imam Abu Dawood", "AD", "#9333EA", 5274, 4));
        list.add(new HadithBookCategory("tirmidhi", "জামে' আত-তিরমিযী", "Jami' at-Tirmidhi", "ইমাম তিরমিজি", "Imam Tirmidhi", "T", "#3B82F6", 3956, 5));
        list.add(new HadithBookCategory("ibn_majah", "সুনানে ইবনে মাজাহ", "Sunan Ibn Majah", "ইমাম ইবনে মাজাহ", "Imam Ibn Majah", "IM", "#F97316", 4341, 6));
        list.add(new HadithBookCategory("muwatta_malik", "মুয়াত্তা ইমাম মালিক", "Muwatta Imam Malik", "ইমাম মালিক", "Imam Malik", "MI", "#38BDF8", 1858, 7));

        // 8 - 13: Classical Collections
        list.add(new HadithBookCategory("riyadus_salihin", "রিয়াদুস সালেহীন", "Riyadus Salihin", "ইমাম নববী", "Imam Nawawi", "RS", "#EC4899", 1905, 8));
        list.add(new HadithBookCategory("bulughul_maram", "বুলুগুল মারাম", "Bulughul Maram", "ইবনে হাজার আসকালানী", "Ibn Hajar al-Asqalani", "BM", "#FB923C", 1568, 9));
        list.add(new HadithBookCategory("lulu_wal_marjan", "আল-লু'লু ওয়াল মারজান", "Al-Lu'lu wal Marjan", "আল্লামা ফুয়াদ আল বাকী", "Allama Fuad Al-Baqi", "LM", "#84CC16", 1906, 10));
        list.add(new HadithBookCategory("hadith_sambhar", "হাদীস সম্ভার", "Hadith Sambhar", "আব্দুল হামিদ ফাইযি", "Abdul Hamid Faizi", "HS", "#14B8A6", 3000, 11));
        list.add(new HadithBookCategory("silsila_sahiha", "সিলসিলা সহিহা", "Silsila Sahiha", "আল্লামা নাসিরুদ্দিন আলবানী", "Allama Nasiruddin Albani", "SS", "#F59E0B", 4035, 12));
        list.add(new HadithBookCategory("jal_o_daif_series", "জাল ও যঈফ হাদীস সিরিজ", "Jal o Daif Hadith Series", "আল্লামা নাসিরুদ্দিন আলবানী", "Allama Nasiruddin Albani", "JH", "#FCA5A5", 4000, 13));

        // 14 - 20: Famous Anthologies
        list.add(new HadithBookCategory("mishkatul_masabih", "মিশকাতুল মাসাবীহ", "Mishkat al-Masabih", "আল্লামা খতীব তাবরেযী", "Allama Khatib Tabrizi", "MM", "#4B5563", 6294, 14));
        list.add(new HadithBookCategory("nawawi_40", "আন্-নওয়াবীর চল্লিশ হাদীস", "An-Nawawi's 40 Hadith", "ইমাম নববী", "Imam Nawawi", "40", "#78716C", 42, 15));
        list.add(new HadithBookCategory("adabul_mufrad", "আল-আদাবুল মুফরাদ", "Al-Adab al-Mufrad", "ইমাম বুখারি", "Imam Bukhari", "AM", "#0F766E", 1322, 16));
        list.add(new HadithBookCategory("rafayel_yadain", "জুয'উল রাফায়েল ইয়াদাইন", "Juz'ul Raf'ul Yadayn", "ইমাম বুখারি", "Imam Bukhari", "JR", "#06B6D4", 114, 17));
        list.add(new HadithBookCategory("hadithe_qudsi", "সহীহ হাদীসে কুদসী", "Sahih Hadithe Qudsi", "আল্লামা নাসিরুদ্দিন আলবানী", "Allama Nasiruddin Albani", "HK", "#10B981", 396, 18));
        list.add(new HadithBookCategory("100_susabbasto_hadith", "১০০ সুসাব্যস্ত হাদীস", "100 Susabbasto Hadith", "সঙ্কলিত হাদিস", "Compiled Hadith", "100", "#4F46E5", 100, 19));
        list.add(new HadithBookCategory("mishkate_daif_hadith", "মিশকাতে যঈফ হাদীস", "Mishkate Daif Hadith", "মুযাফফার বিন মুহসিন", "Muzaffar Bin Muhsin", "MJ", "#E11D48", 1150, 20));

        // 21 - 25: Specialized & Moral Themes
        list.add(new HadithBookCategory("shamayele_tirmidhi", "শামায়েলে তিরমিযি", "Shama'il al-Tirmidhi", "ইমাম তিরমিজি", "Imam Tirmidhi", "ST", "#EF4444", 397, 21));
        list.add(new HadithBookCategory("sahih_at_targib", "সহীহ আত-তারগিব ওয়াত তাহরিব", "Sahih at-Targhib wat-Tahrib", "আল্লামা নাসিরুদ্দিন আলবানী", "Allama Nasiruddin Albani", "TW", "#A855F7", 3775, 22));
        list.add(new HadithBookCategory("sahih_fazayele_amal", "সহিহ ফাযায়েলে আমল", "Sahih Fazayele Amal", "আহসানুল্লাহ বিন সানাউল্লাহ", "Ahsanullah Bin Sanaullah", "FA", "#475569", 172, 23));
        list.add(new HadithBookCategory("upodesh", "উপদেশ", "Upodesh", "আব্দুর রাজ্জাক বিন ইউসুফ", "Abdur Razzak Bin Yousuf", "UP", "#22C55E", 125, 24));
        list.add(new HadithBookCategory("ramadaner_durbol_hadith", "রমজানের দুর্বল হাদিস", "Ramadaner Durbol Hadith", "সানাউল্লাহ নজির আহমদ", "Sanaullah Nazir Ahmad", "RH", "#78350F", 45, 25));

        return list;
    }
}
