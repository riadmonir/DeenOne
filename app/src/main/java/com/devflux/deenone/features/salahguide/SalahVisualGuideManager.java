package com.devflux.deenone.features.salahguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * High-performance Salah Visual Guide Data & Glide Preloader Manager.
 * Features:
 *  - Automatic offline pre-caching via Glide DiskCacheStrategy.ALL
 *  - Zero-lag instant memory & local cache fallback
 *  - Remote synchronization with PHP backend / MySQL database
 */
public class SalahVisualGuideManager {

    private static final String TAG = "SalahVisualGuide";
    private static final String PREF_NAME = "deanone_salah_visual_cache";
    private static final String KEY_CACHE_JSON = "cached_visual_steps_json";

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();

    public static class StepItem {
        public final int stepNumber;
        public final String title;
        public final String description;
        public final String dua;
        public final String notes;
        public final String imageUrl;
        public final float verticalBias;

        public StepItem(int stepNumber, String title, String description, String dua, String notes, String imageUrl, float verticalBias) {
            this.stepNumber = stepNumber;
            this.title = title;
            this.description = description;
            this.dua = dua;
            this.notes = notes;
            this.imageUrl = imageUrl;
            this.verticalBias = verticalBias;
        }
    }

    /**
     * Preloads all visual guide images in background using Glide.
     * Called when the user enters the Namaz Learning Hub.
     */
    public static void preloadAllVisualSteps(@NonNull Context context) {
        final Context appContext = context.getApplicationContext();
        EXECUTOR.execute(() -> {
            try {
                List<StepItem> maleSteps = getMaleSteps(appContext);
                List<StepItem> femaleSteps = getFemaleSteps(appContext);

                List<StepItem> all = new ArrayList<>(maleSteps.size() + femaleSteps.size());
                all.addAll(maleSteps);
                all.addAll(femaleSteps);

                for (StepItem step : all) {
                    if (step.imageUrl != null && !step.imageUrl.isEmpty()) {
                        MAIN_HANDLER.post(() -> {
                            try {
                                Glide.with(appContext)
                                        .load(step.imageUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .preload();
                            } catch (Exception ignored) {}
                        });
                    }
                }

                // Fetch latest dynamic steps from PHP API if connected
                fetchRemoteSteps(appContext, null);
            } catch (Exception e) {
                Log.e(TAG, "Preload visual steps error: " + e.getMessage());
            }
        });
    }

    /**
     * Resolves full image URL from backend base.
     */
    public static String getFullImageUrl(Context context, String relPath) {
        if (relPath == null || relPath.isEmpty()) return "";
        if (relPath.startsWith("http://") || relPath.startsWith("https://")) {
            return relPath;
        }
        String baseUrl = BackendConfigManager.getPhpBaseUrl(context);
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        if (relPath.startsWith("/")) {
            relPath = relPath.substring(1);
        }
        return baseUrl + relPath;
    }

    /**
     * Returns Male Salah Visual Steps.
     */
    public static List<StepItem> getMaleSteps(Context context) {
        boolean isBn = LocaleManager.isBengali(context);
        List<StepItem> cached = getCachedSteps(context, "male", isBn);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }
        return getDefaultMaleSteps(context, isBn);
    }

    /**
     * Returns Female Salah Visual Steps.
     */
    public static List<StepItem> getFemaleSteps(Context context) {
        boolean isBn = LocaleManager.isBengali(context);
        List<StepItem> cached = getCachedSteps(context, "female", isBn);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }
        return getDefaultFemaleSteps(context, isBn);
    }

    /**
     * Asynchronously fetches steps from PHP API and pre-caches new images.
     */
    public static void fetchRemoteSteps(Context context, Runnable onComplete) {
        String apiUrl = BackendConfigManager.getPhpApiEndpoint(context, "get_namaz_visual_steps.php");
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Accept", "application/json")
                .build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (onComplete != null) {
                    MAIN_HANDLER.post(onComplete);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String json = response.body().string();
                        saveCacheJson(context, json);

                        // Preload any newly returned images
                        try {
                            JsonObject root = new Gson().fromJson(json, JsonObject.class);
                            if (root != null && root.has("steps")) {
                                JsonArray arr = root.getAsJsonArray("steps");
                                for (JsonElement elem : arr) {
                                    JsonObject obj = elem.getAsJsonObject();
                                    String fullImg = obj.has("full_image_url") ? obj.get("full_image_url").getAsString() : "";
                                    if (!fullImg.isEmpty()) {
                                        MAIN_HANDLER.post(() -> {
                                            try {
                                                Glide.with(context.getApplicationContext())
                                                        .load(fullImg)
                                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .preload();
                                            } catch (Exception ignored) {}
                                        });
                                    }
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                } catch (Exception ignored) {
                } finally {
                    response.close();
                    if (onComplete != null) {
                        MAIN_HANDLER.post(onComplete);
                    }
                }
            }
        });
    }

    private static void saveCacheJson(Context context, String json) {
        try {
            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            sp.edit().putString(KEY_CACHE_JSON, json).apply();
        } catch (Exception ignored) {}
    }

    private static List<StepItem> getCachedSteps(Context context, String targetGender, boolean isBn) {
        try {
            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String json = sp.getString(KEY_CACHE_JSON, null);
            if (json == null || json.isEmpty()) return null;

            JsonObject root = new Gson().fromJson(json, JsonObject.class);
            if (root == null || !root.has("steps")) return null;

            JsonArray arr = root.getAsJsonArray("steps");
            List<StepItem> list = new ArrayList<>();

            for (JsonElement elem : arr) {
                JsonObject o = elem.getAsJsonObject();
                String gender = o.has("gender") ? o.get("gender").getAsString() : "male";
                if (!gender.equalsIgnoreCase(targetGender)) continue;

                int stepNum = o.has("step_number") ? o.get("step_number").getAsInt() : 1;
                String title = isBn
                        ? (o.has("step_title_bn") ? o.get("step_title_bn").getAsString() : "")
                        : (o.has("step_title_en") ? o.get("step_title_en").getAsString() : "");

                String desc = isBn
                        ? (o.has("description_bn") ? o.get("description_bn").getAsString() : "")
                        : (o.has("description_en") ? o.get("description_en").getAsString() : "");

                String dua = isBn
                        ? (o.has("dua_bn") && !o.get("dua_bn").isJsonNull() ? o.get("dua_bn").getAsString() : null)
                        : (o.has("dua_en") && !o.get("dua_en").isJsonNull() ? o.get("dua_en").getAsString() : null);

                String notes = isBn
                        ? (o.has("notes_bn") && !o.get("notes_bn").isJsonNull() ? o.get("notes_bn").getAsString() : null)
                        : (o.has("notes_en") && !o.get("notes_en").isJsonNull() ? o.get("notes_en").getAsString() : null);

                String imgUrl = o.has("full_image_url") ? o.get("full_image_url").getAsString() : "";
                if (imgUrl.isEmpty() && o.has("image_url")) {
                    imgUrl = getFullImageUrl(context, o.get("image_url").getAsString());
                }

                float bias = o.has("vertical_bias") ? o.get("vertical_bias").getAsFloat() : 0.5f;

                list.add(new StepItem(stepNum, title, desc, dua, notes, imgUrl, bias));
            }

            return list.isEmpty() ? null : list;
        } catch (Exception e) {
            return null;
        }
    }

    // =========================================================================
    // Built-in Instant Fallback Steps (Offline & Zero Network Latency)
    // =========================================================================

    private static List<StepItem> getDefaultMaleSteps(Context context, boolean isBn) {
        List<StepItem> list = new ArrayList<>();

        // ধাপ ১: কিয়াম ও নিয়ত
        list.add(new StepItem(
                1,
                isBn ? "কিয়াম ও নিয়ত" : "Qiyam & Intention",
                isBn ? "নামাজ পড়ার আগে মন দিয়ে আল্লাহর কাছে নামাজের উদ্দেশ্য ঠিক করতে হবে। মনোযোগ সহকারে নামাজের জন্য নিয়ত করতে হবে।"
                     : "Before starting prayer, sincerely set your intention in your heart purely for Allah.",
                null,
                isBn ? "উদাহরণ: 'আমি দুই রাকাত ফরজ নামাজ আদায় করছি আল্লাহর জন্য।' প্রাথমিক নিয়ত করে নামাজ শুরু করতে হয়। (হাদিস: সুনানে আবু দাউদ: ৭৫৮)"
                     : "Example: 'I intend to offer two Rak'ahs of Fard prayer for Allah.' The intention is made in the heart. (Hadith: Sunan Abi Dawud: 758)",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_1.jpg"),
                0.08f
        ));

        // ধাপ ২: তাকবীরে তাহরীমা
        list.add(new StepItem(
                2,
                isBn ? "তাকবীরে তাহরীমা" : "Takbeer-e-Tahreema",
                isBn ? "কিবলামুখী হয়ে আপনার উভয় হাত কান বরাবর ওপরে তুলুন এবং বলুন:"
                     : "Facing the Qiblah, raise both hands up to ear level and recite:",
                isBn ? "আরবি: اللَّهُ أَكْبَرُ\n\nউচ্চারণ: আল্লাহু আকবার।\nঅর্থ: আল্লাহ সর্বশ্রেষ্ঠ।"
                     : "Arabic: اللَّهُ أَكْبَرُ\n\nTransliteration: Allahu Akbar.\nMeaning: Allah is the Greatest.",
                isBn ? "আপনার নামাজ এখান থেকে শুরু হলো।"
                     : "Your prayer begins here.",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_2.jpg"),
                0.02f
        ));

        // ধাপ ৩: হাত বাঁধা ও ছানা পাঠ
        list.add(new StepItem(
                3,
                isBn ? "হাত বাঁধা ও ছানা পাঠ" : "Folding Hands & Reciting Sana",
                isBn ? "ডান হাত বাম হাতের ওপর রেখে নাভির ওপর বাঁধুন এবং দৃষ্টি সিজদার স্থানে রাখুন। তাকবীরে তাহরীমার পরেই পড়ুন ছানা:"
                     : "Place your right hand over the left over your navel and keep your gaze at the place of prostration. Recite Sana:",
                isBn ? "আরবি: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nউচ্চারণ: সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা-ইলাহা গাইরুক।"
                     : "Arabic: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nTransliteration: Subhanak Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
                isBn ? "তা’য়াউজঃ আউ’যুবিল্লাহি মিনাশশাইত্বানির রাজীম\n\nতাসমিয়াহ্ঃ বিসমিল্লাহির রাহমানির রাহীম\n\nসুরা ফাতিহা পড়া ফরজ। (কুরআন: ১: ১-৭), প্রত্যেক নামাজে সুরা ফাতিহা পড়ুন এবং অন্য যেকোনো একটি সূরা তেলাওয়াত করুন।"
                     : "Ta'awwudh: A'udhu billahi minash-shaytanir-rajim\n\nTasmiyah: Bismillahir-Rahmanir-Rahim\n\nReciting Surah Al-Fatiha is obligatory (Quran 1:1-7). Recite Surah Al-Fatiha in every Rak'ah followed by any other Surah.",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_3.jpg"),
                0.35f
        ));

        // ধাপ ৪: রুকু
        list.add(new StepItem(
                4,
                isBn ? "রুকু" : "Ruku",
                isBn ? "এবার আল্লাহু আকবার বলে রুকুতে যান। রুকুর মুহূর্তে আপনার হাত আপনার হাঁটুতে এবং আপনার চোখের দৃষ্টি সিজদার স্থানে হওয়া উচিত। আপনার শরীরটি মাটির সাথে সমান্তরাল রাখুন।"
                     : "Say 'Allahu Akbar' and bow into Ruku. Place your hands on your knees and look at the place of Sajdah. Keep your back parallel to the ground.",
                isBn ? "রুকুর দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الْعَظِيمِ\nউচ্চারণ: সুবহানা রব্বিয়াল আযীম। (৩ বার)\nঅর্থ: আমি আমার মহান প্রভুর পবিত্রতা বর্ণনা করছি।"
                     : "Ruku Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الْعَظِيمِ\nTransliteration: Subhana Rabbiyal Azeem (3 times)\nMeaning: Glory be to my Lord, the Almighty.",
                isBn ? "রুকুতে এই দোয়া ৩, ৫, ৭ বা বিজোড় সংখ্যক বার পাঠ করুন। (হাদিস: সুনানে আবু দাউদ)"
                     : "Recite this in odd numbers (3, 5, or 7 times). (Hadith: Sunan Abi Dawud)",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_4.jpg"),
                0.30f
        ));

        // ধাপ ৫: কওমা - রুকু থেকে উঠে দাঁড়ানো
        list.add(new StepItem(
                5,
                isBn ? "কওমা - রুকু থেকে উঠে দাঁড়ানো" : "Qawmah (Standing from Ruku)",
                isBn ? "রুকু থেকে উঠে দাঁড়াতে দাঁড়াতে পড়ুন:"
                     : "Rise straight from Ruku into standing position (Qawmah) and recite:",
                isBn ? "আরবিঃ سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ\nউচ্চারণ: সামিআল্লাহু লিমান হামিদাহ্।\nঅর্থ: আল্লাহ তার কথা শোনেন, যে তার প্রশংসা করে।"
                     : "Arabic: سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ\nTransliteration: Sami' Allahu liman hamidah.\nMeaning: Allah hears whoever praises Him.",
                isBn ? "সোজা হয়ে দাঁড়িয়ে পড়ুন:\n\nআরবিঃ رَبَّنَا لَكَ الْحَمْدُ، حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ\nউচ্চারণ: রব্বানা লাকাল হামদ, হামদান কাসীরন তাইয়্যিবান মুবারকান ফীহ।\nঅর্থ: হে আমাদের রব! আপনার জন্যই সমস্ত প্রশংসা—এমন প্রশংসা যা প্রচুর, পবিত্র ও বরকতময়।"
                     : "Standing upright, recite:\n\nArabic: رَبَّنَا لَكَ الْحَمْدُ، حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ\nTransliteration: Rabbana lakal hamd, hamdan kaseeran tayyiban mubarakan feeh.\nMeaning: Our Lord, to You belongs all praise, an abundant, beautiful and blessed praise.",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_5.jpg"),
                0.12f
        ));

        // ধাপ ৬: প্রথম সিজদাহ
        list.add(new StepItem(
                6,
                isBn ? "প্রথম সিজদাহ" : "First Sajdah",
                isBn ? "আল্লাহু আকবার বলে প্রথমে হাঁটু, তারপর হাত, অতঃপর নাক ও কপাল মাটিতে রেখে সিজদাহ করুন। সিজদাহর সময়ে দুই পায়ের আঙুল কিবলামুখী করে রাখুন এবং পড়ুন:"
                     : "Saying 'Allahu Akbar', place knees, hands, nose and forehead on the ground in Sajdah. Direct toes toward Qiblah and recite:",
                isBn ? "সিজদার দোয়াঃ\n\nআরবিঃ سُبْحَانَ رَبِّيَ الْأَعْلَى\nউচ্চারণ: সুবহানা রব্বিয়াল আ'লা। (৩ বার)\nঅর্থ: আমি আমার সর্বশ্রেষ্ঠ মহান প্রতিপালকের পবিত্রতা বর্ণনা করছি।"
                     : "Sajdah Dua:\n\nArabic: سُبْحَانَ رَبِّيَ الْأَعْلَى\nTransliteration: Subhana Rabbiyal A'la (3 times)\nMeaning: Glory be to my Lord, the Most High.",
                isBn ? "সিজদায় ৩, ৫ বা ৭ বার তাসবীহ পাঠ করুন।"
                     : "Recite this in odd counts (3, 5, or 7 times).",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_6.jpg"),
                0.50f
        ));

        // ধাপ ৭: দুই সিজদার মাঝের বৈঠক
        list.add(new StepItem(
                7,
                isBn ? "দুই সিজদার মাঝের বৈঠক" : "Jalsah (Sitting between Sajdahs)",
                isBn ? "আল্লাহু আকবার বলে সিজদাহ থেকে সোজা হয়ে বসুন। বাম পা বিছিয়ে তার ওপর বসুন এবং ডান পা খাড়া রাখুন। উভয় হাত উরুর ওপর রাখুন এবং দোয়া পড়ুন:"
                     : "Rise with 'Allahu Akbar' and sit calmly between Sajdahs. Lay left foot flat to sit on, keep right foot upright, place hands on thighs, and recite:",
                isBn ? "আরবিঃ رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\nউচ্চারণ: রব্বিগফির লী, রব্বিগফির লী।\nঅর্থ: হে আমার রব! আমাকে ক্ষমা করুন, হে আমার রব! আমাকে ক্ষমা করুন।"
                     : "Arabic: رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\nTransliteration: Rabbighfir lee, Rabbighfir lee.\nMeaning: O my Lord, forgive me; O my Lord, forgive me.",
                isBn ? "অথবা পড়ুন: 'আল্লাহুম্মাগফির লী ওয়ারহামনী ওয়াহদিনী ওয়া আফিনী ওয়ারযুক্বনী।'"
                     : "Or recite: 'Allahummaghfir lee warhamnee wahdinee wa 'aafinee warzuqnee.'",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_7.jpg"),
                0.50f
        ));

        // ধাপ ৮: দ্বিতীয় সিজদাহ ও দ্বিতীয় রাকাত
        list.add(new StepItem(
                8,
                isBn ? "দ্বিতীয় সিজদাহ ও দ্বিতীয় রাকাত" : "Second Sajdah & 2nd Rak'ah",
                isBn ? "আল্লাহু আকবার বলে আবার দ্বিতীয় সিজদায় যান এবং ৩ বার 'সুবহানা রব্বিয়াল আ'লা' পাঠ করুন। এরপর তাকবীর বলে সোজা হয়ে দাঁড়িয়ে দ্বিতীয় রাকাত একইভাবে আদায় করুন।"
                     : "Go into the second Sajdah saying 'Allahu Akbar' and recite 'Subhana Rabbiyal A'la' 3 times. Rise for the second Rak'ah.",
                isBn ? "আরবিঃ سُبْحَانَ رَبِّيَ الْأَعْلَى\nউচ্চারণ: সুবহানা রব্বিয়াল আ'লা।"
                     : "Arabic: سُبْحَانَ رَبِّيَ الْأَعْلَى\nTransliteration: Subhana Rabbiyal A'la.",
                isBn ? "দ্বিতীয় রাকাতে ছানা পড়তে হবে না, সরাসরি সূরা ফাতিহা দিয়ে শুরু করবেন।"
                     : "In the 2nd Rak'ah, do not recite Sana; start directly from Surah Al-Fatiha.",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_8.jpg"),
                0.50f
        ));

        // ধাপ ৯: তাশাহহুদ ও শেষ বৈঠক
        list.add(new StepItem(
                9,
                isBn ? "তাশাহহুদ ও শেষ বৈঠক" : "Tashahhud & Final Sitting",
                isBn ? "দ্বিতীয় রাকাতের সিজদাহ শেষে তাশাহহুদের জন্য বসুন। তাশাহহুদ, দরুদ ইব্রাহিম ও দোয়া মাসূরা পাঠ করুন:"
                     : "After the 2nd Sajdah of the final Rak'ah, sit for Tashahhud, Durood Ibrahim and Dua Masura:",
                isBn ? "তাশাহহুদ:\n\nআরবিঃ التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nদরুদ ইব্রাহিম:\n\nআরবিঃ اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَىٰ إِبْرَاهِيمَ وَعَلَىٰ آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ\n\nদোয়া মাসূরা:\n\nআরবিঃ اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ"
                     : "Recite At-Tahiyyat, Durood Ibrahim and Dua Masura with complete devotion and humility.",
                isBn ? "তাশাহহুদে 'আশহাদু আল্লা ইলাহা' বলার সময় শাহাদাত আঙুল উঁচিয়ে ইশারা করুন এবং 'ইল্লাল্লাহ' বলে নামিয়ে নিন।"
                     : "Raise your index finger during the Shahadah testimony in Tashahhud.",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_9.jpg"),
                0.50f
        ));

        // ধাপ ১০: সালাম ফিরানো
        list.add(new StepItem(
                10,
                isBn ? "সালাম ফিরানো" : "Tasleem (Salam)",
                isBn ? "দোয়া মাসূরা পাঠ শেষে প্রথমে ডান কাঁধের দিকে মুখ ফিরিয়ে বলুন:"
                     : "After Dua Masura, turn your face to the right shoulder and say:",
                isBn ? "আরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্\n\nএরপর বাম কাঁধের দিকে মুখ ফিরিয়ে আবার বলুন:\n\nআরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্"
                     : "Right:\nAs-salamu 'alaykum wa rahmatullah\n\nLeft:\nAs-salamu 'alaykum wa rahmatullah",
                isBn ? "সালাম ফিরানোর মাধ্যমে নামাজ সমাপ্ত হয়।"
                     : "The prayer concludes with the completion of Tasleem.",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_10.jpg"),
                0.50f
        ));

        // ধাপ ১১: সালাত পরবর্তী দোয়া ও মোনাজাত
        list.add(new StepItem(
                11,
                isBn ? "সালাত পরবর্তী দোয়া ও মোনাজাত" : "Supplication & Duas after Salah",
                isBn ? "সালাম ফিরানোর পর ৩ বার ইস্তিগফার (আস্তাগফিরুল্লাহ) পড়ুন, আয়াতুল কুরসি পড়ুন এবং আল্লাহর কাছে নিজের ও পরিবারসহ সমগ্র মুসলিম উম্মাহর জন্য মোনাজাত করুন।"
                     : "After Salam, recite Astaghfirullah 3 times, Ayat al-Kursi, and make sincere dua to Allah for forgiveness and mercy.",
                isBn ? "আরবিঃ أَسْتَغْفِرُ اللَّهَ (৩ বার)\n\nআরবিঃ اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ\nউচ্চারণ: আল্লাহুম্মা আনতাস সালামু ওয়া মিনকাস সালামু, তাবারাকতা ইয়া যাল জালালি ওয়াল ইকরাম।"
                     : "Recite Astaghfirullah (3 times), followed by 'Allahumma Antas Salamu wa minkas salamu, tabarakta ya Dhal Jalali wal Ikram.'",
                isBn ? "রাসূলুল্লাহ (ﷺ) প্রত্যেক ফরজ নামাজের পর এই দোয়াগুলো পাঠ করতেন। (সহীহ মুসলিম)"
                     : "The Prophet (ﷺ) used to recite these supplications after every obligatory prayer. (Sahih Muslim)",
                getFullImageUrl(context, "uploads/namaz_learning/male/img_salah_male_step_11.jpg"),
                0.50f
        ));

        return list;
    }

    private static List<StepItem> getDefaultFemaleSteps(Context context, boolean isBn) {
        List<StepItem> list = new ArrayList<>();

        // ধাপ ১: কিয়াম ও নিয়ত
        list.add(new StepItem(
                1,
                isBn ? "কিয়াম ও নিয়ত" : "Qiyam & Intention",
                isBn ? "পবিত্রতা অর্জন করে শালীন পোশাক ও হিজাব পরিধান করে কিবলামুখী হয়ে দাঁড়ান এবং অন্তরে নির্ধারিত নামাজের নিয়ত করুন।"
                     : "Ensure purity, wear modest covering/hijab, face the Qiblah and make sincere intention in your heart.",
                null,
                isBn ? "মহিলাদের নামাজের শারীরিক ভঙ্গি অত্যন্ত বিনম্র, সংকুচিত ও মার্জিত হবে।"
                     : "Women's posture in Salah is characterized by modesty and compact positioning.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_1.jpg"),
                0.35f
        ));

        // ধাপ ২: তাকবীরে তাহরীমা
        list.add(new StepItem(
                2,
                isBn ? "তাকবীরে তাহরীমা" : "Takbeer-e-Tahreema",
                isBn ? "উভয় হাত কাঁধ বা বুক বরাবর ওপরে তুলুন (কান পর্যন্ত নয়) এবং বলুন:"
                     : "Raise both hands up to shoulder/chest level (not ear level) and recite:",
                isBn ? "আরবি: اللَّهُ أَكْبَرُ\n\nউচ্চারণ: আল্লাহু আকবার।\nঅর্থ: আল্লাহ সর্বশ্রেষ্ঠ।"
                     : "Arabic: اللَّهُ أَكْبَرُ\n\nTransliteration: Allahu Akbar.\nMeaning: Allah is the Greatest.",
                isBn ? "হাতের তালু কিবলামুখী থাকবে।"
                     : "Palms facing toward Qiblah.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_2.jpg"),
                0.08f
        ));

        // ধাপ ৩: বুকের উপর হাত বাঁধা ও ছানা
        list.add(new StepItem(
                3,
                isBn ? "বুকের উপর হাত বাঁধা ও ছানা" : "Folding Hands on Chest & Sana",
                isBn ? "ডান হাত বাম হাতের পাতার ওপর রেখে বুকের ওপর হাত বাঁধুন। তাকবীরে তাহরীমার পর ছানা পাঠ করুন:"
                     : "Place right hand over left palm over the chest and recite Sana:",
                isBn ? "আরবি: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nউচ্চারণ: সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা, ওয়া তা'আলা জাদ্দুকা, ওয়া লা-ইলাহা গাইরুক।"
                     : "Arabic: سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَىٰ جَدُّكَ وَلَا إِلٰهَ غَيْرُكَ\n\nTransliteration: Subhanak Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
                isBn ? "এরপর আউযুবিল্লাহ, বিসমিল্লাহসহ সূরা ফাতিহা ও অন্য একটি সূরা তিলাওয়াত করুন।"
                     : "Then recite Ta'awwudh, Tasmiyah, Surah Al-Fatiha and another Surah.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_3.jpg"),
                0.08f
        ));

        // ধাপ ৪: রুকু
        list.add(new StepItem(
                4,
                isBn ? "রুকু" : "Ruku",
                isBn ? "আল্লাহু আকবার বলে সামান্য ঝুঁকে রুকু করুন যাতে হাত সহজে হাঁটু পর্যন্ত পৌঁছায় (পুরুষদের মতো সম্পূর্ণ সোজা সমান্তরাল নয়)। রুকুতে পড়ুন:"
                     : "Say 'Allahu Akbar' and bend modestly into Ruku until hands reach knees. Recite:",
                isBn ? "আরবিঃ سُبْحَانَ رَبِّيَ الْعَظِيمِ\nউচ্চারণ: সুবহানা রব্বিয়াল আযীম। (৩ বার)\nঅর্থ: আমি আমার মহান প্রতিপালকের পবিত্রতা বর্ণনা করছি।"
                     : "Arabic: سُبْحَانَ رَبِّيَ الْعَظِيمِ\nTransliteration: Subhana Rabbiyal Azeem (3 times)\nMeaning: Glory be to my Lord, the Almighty.",
                isBn ? "হাতের আঙুলগুলো মিলিয়ে হাঁটুর ওপর আলতোভাবে রাখুন।"
                     : "Keep fingers together and placed gently on knees.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_4.jpg"),
                0.50f
        ));

        // ধাপ ৫: কওমা - রুকু থেকে উঠে দাঁড়ানো
        list.add(new StepItem(
                5,
                isBn ? "কওমা - রুকু থেকে উঠে দাঁড়ানো" : "Qawmah",
                isBn ? "রুকু থেকে সোজা হয়ে দাঁড়াতে দাঁড়াতে পড়ুন 'সামিআল্লাহু লিমান হামিদাহ্' এবং সোজা হয়ে দাঁড়িয়ে পড়ুন 'রব্বানা লাকাল হামদ'।"
                     : "Rise straight from Ruku reciting 'Sami' Allahu liman hamidah' and standing upright say 'Rabbana lakal hamd'.",
                isBn ? "আরবিঃ سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ\nআরবিঃ رَبَّنَا لَكَ الْحَمْدُ"
                     : "Arabic: سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ\nArabic: رَبَّنَا لَكَ الْحَمْدُ",
                isBn ? "শান্ত হয়ে স্থিরভাবে দাঁড়ান।"
                     : "Stand still and calm.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_5.jpg"),
                0.10f
        ));

        // ধাপ ৬: প্রথম সিজদাহ
        list.add(new StepItem(
                6,
                isBn ? "প্রথম সিজদাহ" : "First Sajdah",
                isBn ? "আল্লাহু আকবার বলে সংকুচিত হয়ে সিজদা করুন। পেট উরুর সাথে এবং বাহু পাঁজরের সাথে মিলিয়ে মাটিতে রেখে পড়ুন:"
                     : "Say 'Allahu Akbar' and perform a modest, compact Sajdah keeping limbs close to the body. Recite:",
                isBn ? "আরবিঃ سُبْحَانَ رَبِّيَ الْأَعْلَى\nউচ্চারণ: সুবহানা রব্বিয়াল আ'লা। (৩ বার)\nঅর্থ: আমি আমার সর্বশ্রেষ্ঠ মহান প্রতিপালকের পবিত্রতা বর্ণনা করছি।"
                     : "Arabic: سُبْحَانَ رَبِّيَ الْأَعْلَى\nTransliteration: Subhana Rabbiyal A'la (3 times)\nMeaning: Glory be to my Lord, the Most High.",
                isBn ? "কপাল ও নাক মাটিতে দৃঢ়ভাবে রাখুন।"
                     : "Keep forehead and nose firmly on the ground.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_6.jpg"),
                0.50f
        ));

        // ধাপ ৭: দুই সিজদার মাঝের বৈঠক
        list.add(new StepItem(
                7,
                isBn ? "দুই সিজদার মাঝের বৈঠক" : "Jalsah",
                isBn ? "আল্লাহু আকবার বলে সিজদা থেকে উঠে বসুন। উভয় পা ডান দিক দিয়ে বের করে মাটির ওপর বসুন (তাওয়াররুক পদ্ধতি) এবং দোয়া পড়ুন:"
                     : "Rise with 'Allahu Akbar' and sit with feet shifted to the right side on the ground. Recite:",
                isBn ? "আরবিঃ رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\nউচ্চারণ: রব্বিগফির লী, রব্বিগফির লী।"
                     : "Arabic: رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي\nTransliteration: Rabbighfir lee, Rabbighfir lee.",
                isBn ? "এরপর দ্বিতীয় সিজদাহ আদায় করুন।"
                     : "Then perform the second Sajdah.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_7.jpg"),
                0.50f
        ));

        // ধাপ ৮: তাশাহহুদ ও শেষ বৈঠক
        list.add(new StepItem(
                8,
                isBn ? "তাশাহহুদ ও শেষ বৈঠক" : "Tashahhud & Final Sitting",
                isBn ? "শেষ বৈঠকে বসে তাশাহহুদ, দরুদ ইব্রাহিম ও দোয়া মাসূরা পাঠ করুন:"
                     : "Sit for the final Tashahhud and recite At-Tahiyyat, Durood Ibrahim and Dua Masura:",
                isBn ? "তাশাহহুদ:\n\nالتَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَىٰ عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ\n\nদরুদ ইব্রাহিম ও দোয়া মাসূরা পূর্ণ মনোযোগ সহকারে পড়ুন।"
                     : "Recite At-Tahiyyat, Durood Ibrahim and Dua Masura attentively.",
                isBn ? "তাশাহহুদে শাহাদাত আঙুল দিয়ে ইশারা করুন।"
                     : "Point index finger during the testimony of faith.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_8.jpg"),
                0.50f
        ));

        // ধাপ ৯: ডান দিকে সালাম ফিরানো
        list.add(new StepItem(
                9,
                isBn ? "ডান দিকে সালাম ফিরানো" : "Salam to Right",
                isBn ? "আপনার ডান দিকে কাঁধ পর্যন্ত মুখ ঘুরিয়ে সালাম দিন:"
                     : "Turn your face to the right shoulder and say:",
                isBn ? "আরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্"
                     : "Arabic: السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
                null,
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_9.jpg"),
                0.50f
        ));

        // ধাপ ১০: বাম দিকে সালাম ফিরানো ও সমাপ্তি
        list.add(new StepItem(
                10,
                isBn ? "বাম দিকে সালাম ফিরানো ও সমাপ্তি" : "Salam to Left & Completion",
                isBn ? "বাম কাঁধের দিকে মুখ ঘুরিয়ে সালাম দিন:"
                     : "Turn your face to the left shoulder and say:",
                isBn ? "আরবিঃ السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nউচ্চারণ: আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ্"
                     : "Arabic: السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ\nTransliteration: As-salamu 'alaykum wa rahmatullah",
                isBn ? "এখানে আপনার নামাজ সম্পন্ন হলো।"
                     : "Your Salah is now completed.",
                getFullImageUrl(context, "uploads/namaz_learning/female/img_salah_female_step_10.jpg"),
                0.50f
        ));

        return list;
    }
}
