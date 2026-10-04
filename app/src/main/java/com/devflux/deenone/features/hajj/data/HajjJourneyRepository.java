package com.devflux.deenone.features.hajj.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.features.hajj.model.HajjJourneyStage;
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
 * High-performance Hajj Journey Data & Glide Offline Caching Manager.
 * Features:
 *  - Automatic offline pre-caching via Glide DiskCacheStrategy.ALL
 *  - Zero-lag instant memory & local cache fallback
 *  - Remote synchronization with PHP backend / MySQL database
 */
public class HajjJourneyRepository {

    private static final String TAG = "HajjJourneyRepo";
    private static final String PREF_NAME = "deanone_hajj_journey_cache";
    private static final String KEY_CACHE_JSON = "cached_hajj_stages_json";

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();

    public static final String SUMMARY_TITLE_BN = "হজ যাত্রা নির্দেশিকা";
    public static final String SUMMARY_TITLE_EN = "Hajj Journey Overview";

    public static final String SUMMARY_CARD_BN =
            "হজ ইসলাম ধর্মের পঞ্চম স্তম্ভ। যারা শারীরিক ও আর্থিকভাবে সক্ষম, অর্থাৎ যাদের নিসাব পরিমাণ সম্পদ রয়েছে, তাদের ওপর জীবনে অন্তত একবার হজ করা ফরজ। হজের প্রতিটি ধাপ সুনির্দিষ্ট নিয়ম ও আহকামে আবদ্ধ।";

    public static final String SUMMARY_CARD_EN =
            "Hajj is the fifth pillar of Islam. It is an obligatory duty once in a lifetime for every physically and financially able Muslim who possesses wealth above the Nisab threshold. Each rite and stage of Hajj is bound by prescribed divine rules and sacred etiquettes.";

    public static final String SUMMARY_EXTENDED_BN =
            "পবিত্র হজের মূল পর্ব জিলহজ মাসের ৮ তারিখ থেকে ১২ বা ১৩ তারিখ পর্যন্ত অনুষ্ঠিত হয়। এই মহান সফরে হাজি সাহেবগণ মক্কা মুকাররমা, মিনা, আরাফাতের ময়দান এবং মুজদালিফায় অবস্থান করে আল্লাহর সন্তুষ্টি লাভের জন্য বিভিন্ন ঐতিহাসিক ও আধ্যাত্মিক রোকন পালন করেন।\n\n" +
            "• ৮ই জিলহজ: মিনা যাত্রা ও রাত্রিযাপন\n" +
            "• ৯ই জিলহজ: উকুফে আরাফা (মূল হজ) এবং রাতে মুজদালিফায় অবস্থান ও পাথর সংগ্রহ\n" +
            "• ১০ই জিলহজ: বড় জামরাতে পাথর নিক্ষেপ, দমে শোকর (কোরবানি), হলক (চুল কামানো/কাটা) ও তাওয়াফুল ইফাদাহ\n" +
            "• ১১ ও ১২ই জিলহজ: মিনায় অবস্থান ও তিন জামরাতে পাথর নিক্ষেপ\n" +
            "• মক্কা ত্যাগের পূর্বে: বিদায়ী তাওয়াফ সম্পাদন";

    public static final String SUMMARY_EXTENDED_EN =
            "The primary rituals of the blessed Hajj take place between the 8th and 12th or 13th of Dhul Hijjah. During this sacred journey, pilgrims stay in Makkah al-Mukarramah, Mina, the plains of Arafat, and Muzdalifah to fulfill historical and spiritual rites seeking Allah's pleasure.\n\n" +
            "• 8th Dhul Hijjah: Departure to Mina and overnight stay\n" +
            "• 9th Dhul Hijjah: Wuquf at Arafah (the core pillar of Hajj), evening at Muzdalifah and collecting pebbles\n" +
            "• 10th Dhul Hijjah: Stoning the large pillar (Jamarat al-Aqabah), offering sacrifice (Hady), shaving/trimming hair (Halq/Taqseer), and Tawaf al-Ifadah\n" +
            "• 11th & 12th Dhul Hijjah: Staying in Mina and stoning all three Jamarat pillars\n" +
            "• Before leaving Makkah: Performing Tawaf al-Wida (Farewell Circumambulation)";

    private static List<HajjJourneyStage> stagesCache = null;

    public static synchronized List<HajjJourneyStage> getStages() {
        if (stagesCache != null) {
            return stagesCache;
        }
        stagesCache = Collections.unmodifiableList(getDefaultStages());
        return stagesCache;
    }

    public static synchronized List<HajjJourneyStage> getStages(Context context) {
        if (stagesCache != null) {
            return stagesCache;
        }
        List<HajjJourneyStage> cached = getCachedStages(context);
        if (cached != null && !cached.isEmpty()) {
            stagesCache = Collections.unmodifiableList(cached);
            return stagesCache;
        }
        stagesCache = Collections.unmodifiableList(getDefaultStages());
        return stagesCache;
    }

    public static synchronized List<HajjJourneyStage> getJourneyStages() {
        return getStages();
    }

    public static synchronized List<HajjJourneyStage> getJourneyStages(Context context) {
        return getStages(context);
    }

    /**
     * Preloads all Hajj Journey stage images in background using Glide.
     * Called when user enters the Hajj Journey page dialog.
     */
    public static void preloadAllJourneyStages(@NonNull Context context) {
        final Context appContext = context.getApplicationContext();
        EXECUTOR.execute(() -> {
            try {
                List<HajjJourneyStage> list = getStages(appContext);
                for (HajjJourneyStage stage : list) {
                    String imgUrl = stage.getImageUrl(appContext);
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        MAIN_HANDLER.post(() -> {
                            try {
                                Glide.with(appContext)
                                        .load(imgUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .preload();
                            } catch (Exception ignored) {}
                        });
                    }
                }
                // Automatically fetch latest remote updates from PHP backend
                fetchRemoteStages(appContext, null);
            } catch (Exception e) {
                Log.e(TAG, "Preload Hajj stages error: " + e.getMessage());
            }
        });
    }

    /**
     * Asynchronously fetches stages from PHP API and pre-caches new images into disk cache.
     */
    public static void fetchRemoteStages(Context context, Runnable onComplete) {
        String apiUrl = BackendConfigManager.getPhpApiEndpoint(context, "get_hajj_journey.php");
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

                        List<HajjJourneyStage> parsed = parseCachedStages(json);
                        if (parsed != null && !parsed.isEmpty()) {
                            synchronized (HajjJourneyRepository.class) {
                                stagesCache = Collections.unmodifiableList(parsed);
                            }
                            // Pre-cache all images via Glide
                            for (HajjJourneyStage stage : parsed) {
                                String fullImg = stage.getImageUrl(context);
                                if (fullImg != null && !fullImg.isEmpty()) {
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

    private static List<HajjJourneyStage> getCachedStages(Context context) {
        try {
            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String json = sp.getString(KEY_CACHE_JSON, null);
            if (json == null || json.isEmpty()) return null;
            return parseCachedStages(json);
        } catch (Exception e) {
            return null;
        }
    }

    private static List<HajjJourneyStage> parseCachedStages(String json) {
        try {
            JsonObject root = new Gson().fromJson(json, JsonObject.class);
            if (root == null || !root.has("stages")) return null;

            JsonArray arr = root.getAsJsonArray("stages");
            List<HajjJourneyStage> list = new ArrayList<>();

            for (JsonElement elem : arr) {
                JsonObject o = elem.getAsJsonObject();
                int stageNum = o.has("stage_number") ? o.get("stage_number").getAsInt() : 1;
                String snBn = o.has("stage_number_bn") ? o.get("stage_number_bn").getAsString() : String.valueOf(stageNum);
                String snEn = o.has("stage_number_en") ? o.get("stage_number_en").getAsString() : String.format("%02d", stageNum);
                String titleBn = o.has("title_bn") ? o.get("title_bn").getAsString() : "";
                String titleEn = o.has("title_en") ? o.get("title_en").getAsString() : "";
                String descBn = o.has("description_bn") ? o.get("description_bn").getAsString() : "";
                String descEn = o.has("description_en") ? o.get("description_en").getAsString() : "";
                String imgUrl = o.has("full_image_url") ? o.get("full_image_url").getAsString() : (o.has("image_url") ? o.get("image_url").getAsString() : "");
                String starColor = o.has("star_color_hex") ? o.get("star_color_hex").getAsString() : "#2FB68E";
                String detailsBn = o.has("details_bn") && !o.get("details_bn").isJsonNull() ? o.get("details_bn").getAsString() : "";
                String detailsEn = o.has("details_en") && !o.get("details_en").isJsonNull() ? o.get("details_en").getAsString() : "";
                String duaArabic = o.has("dua_arabic") && !o.get("dua_arabic").isJsonNull() ? o.get("dua_arabic").getAsString() : "";
                String duaPronBn = o.has("dua_pronunciation_bn") && !o.get("dua_pronunciation_bn").isJsonNull() ? o.get("dua_pronunciation_bn").getAsString() : "";
                String duaPronEn = o.has("dua_pronunciation_en") && !o.get("dua_pronunciation_en").isJsonNull() ? o.get("dua_pronunciation_en").getAsString() : "";
                String duaMeanBn = o.has("dua_meaning_bn") && !o.get("dua_meaning_bn").isJsonNull() ? o.get("dua_meaning_bn").getAsString() : "";
                String duaMeanEn = o.has("dua_meaning_en") && !o.get("dua_meaning_en").isJsonNull() ? o.get("dua_meaning_en").getAsString() : "";
                String ref = o.has("reference") && !o.get("reference").isJsonNull() ? o.get("reference").getAsString() : "";

                list.add(new HajjJourneyStage(
                        stageNum, snBn, snEn, titleBn, titleEn, descBn, descEn,
                        imgUrl, getImageResForStage(stageNum), getLineDrawableForStage(stageNum),
                        starColor, detailsBn, detailsEn, duaArabic, duaPronBn, duaPronEn,
                        duaMeanBn, duaMeanEn, ref
                ));
            }
            return list.isEmpty() ? null : list;
        } catch (Exception e) {
            return null;
        }
    }

    public static int getLineDrawableForStage(int stageNumber) {
        switch (stageNumber) {
            case 1: return R.drawable.hajj_line_right1;
            case 2: return R.drawable.hajj_line_left2;
            case 3: return R.drawable.hajj_line_right3;
            case 4: return R.drawable.hajj_line_left3;
            case 5: return R.drawable.hajj_line_right;
            case 6: return R.drawable.hajj_line_left1;
            case 7: return R.drawable.hajj_line_right2;
            case 8: return R.drawable.hajj_line_left4;
            case 9: return R.drawable.hajj_line_right4;
            case 10: return R.drawable.hajj_line_left;
            case 11: return R.drawable.hajj_line_right3;
            case 12: default: return R.drawable.hajj_line_left4;
        }
    }

    public static int getImageResForStage(int stageNumber) {
        switch (stageNumber) {
            case 1: return R.drawable.img_hajj_journey_stage_1;
            case 2: return R.drawable.img_hajj_journey_stage_2;
            case 3: return R.drawable.img_hajj_journey_stage_3;
            case 4: return R.drawable.img_hajj_journey_stage_4;
            case 5: return R.drawable.img_hajj_journey_stage_5;
            case 6: return R.drawable.img_hajj_journey_stage_6;
            case 7: return R.drawable.img_hajj_journey_stage_7;
            case 8: return R.drawable.img_hajj_journey_stage_8;
            case 9: return R.drawable.img_hajj_journey_stage_9;
            case 10: return R.drawable.img_hajj_journey_stage_10;
            case 11: return R.drawable.img_hajj_journey_stage_11;
            case 12: default: return R.drawable.img_hajj_journey_stage_12;
        }
    }

    private static List<HajjJourneyStage> getDefaultStages() {
        List<HajjJourneyStage> list = new ArrayList<>(12);

        // Stage 01: Ihram (Yellow, curves right)
        list.add(new HajjJourneyStage(
                1,
                "০১",
                "01",
                "ইহরাম",
                "Ihram",
                "হজের যাত্রা শুরু করুন একটি শুদ্ধ নিয়তের মাধ্যমে। ইহরাম অবস্থায় প্রবেশের জন্য নিয়ত করুন এবং তলবিয়াহ পাঠ করে হজের নিয়মাবলীতে নিজেকে যুক্ত করুন। এরপর ইহরামের কাপড় পরিধান করুন।",
                "Begin your sacred pilgrimage journey with a pure and sincere intention. Make the Niyyah to enter the state of Ihram, recite the Talbiyah to devote yourself to the rites of Hajj, and put on the prescribed Ihram garments.",
                "uploads/hajj_journey/img_hajj_journey_stage_1.png",
                R.drawable.img_hajj_journey_stage_1,
                R.drawable.hajj_line_right1,
                "#EBCA28",
                "ইহরামের আহকাম ও করণীয়:\n• শারীরিক পরিচ্ছন্নতা অর্জন করুন, গোসল বা অজু করুন।\n• পুরুষরা দুটি সেলাইবিহীন সাদা চাদর পরিধান করবেন (ইজার ও রিদা)। নারীরা তাদের স্বাভাবিক শালীন পোশাক পরিধান করবেন।\n• দুই রাকাত নফল সালাত আদায় করুন।\n• মনে মনে বা মুখে হজের স্পষ্ট নিয়ত করুন।\n• নিয়তের পরপরই তালবিয়াহ পাঠ শুরু করুন।",
                "Ihram Guidelines & Protocols:\n• Perform personal hygiene, Ghusl (ritual bath) or Wudu.\n• Men wear two unstitched white sheets (Izar and Rida). Women wear modest, loose clothing with hijab.\n• Offer two Rak'ahs of Nafl prayer.\n• Make sincere intention for Hajj.\n• Begin reciting the Talbiyah earnestly upon setting intention.",
                "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لاَ شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ وَالنِّعْمَةَ لَكَ وَالْمُلْكَ، لاَ شَرِيكَ لَكَ",
                "লাব্বাইক আল্লাহুম্মা লাব্বাইক, লাব্বাইকা লা শারীকা লাকা লাব্বাইক, ইন্নাল হামদা ওয়ান নি'মাতা লাকা ওয়াল মুল্ক, লা শারীকা লাক।",
                "Labbayk Allahumma labbayk, labbayka la shareeka laka labbayk, innal-hamda wan-ni'mata laka wal-mulk, la shareeka lak.",
                "আমি আপনার দরবারে হাজির হে আল্লাহ! আমি হাজির! আপনার কোনো শরিক নেই, আমি হাজির! নিশ্চয় সমস্ত প্রশংসা, নিয়ামত ও সার্বভৌমত্ব আপনারই; আপনার কোনো শরিক নেই।",
                "Here I am, O Allah, here I am. Here I am, You have no partner, here I am. Verily all praise, grace, and sovereignty belong to You. You have no partner.",
                "সহীহ বুখারী: ১৫৪৯, সহীহ মুসলিম: ১১৮৪"
        ));

        // Stage 02: Tawaf al-Qudum (Light Green, curves left)
        list.add(new HajjJourneyStage(
                2,
                "০২",
                "02",
                "তাওয়াফুল কুদুম",
                "Tawaf al-Qudum",
                "মক্কায় প্রবেশের পর, ৭ চক্কর দিয়ে তাওয়াফুল কুদুম সম্পন্ন করুন। এরপর মাকাম-ই-ইব্রাহিমের পেছনে দুই রাক'আত সুন্নত নামাজ পড়ুন। তবে যদি সেখানে ভিড় থাকে বা নামাজে অসুবিধা হয়, তাহলে মসজিদুল হারামের অন্য কোনো স্থানে নামাজ পড়া যাবে।",
                "Upon arriving in Makkah, complete the Tawaf al-Qudum (Arrival Circumambulation) by making 7 circuits around the Ka'bah. Afterwards, pray two Rak'ahs of Sunnah prayer behind Maqam Ibrahim. However, if crowded, you may pray anywhere inside the Sacred Mosque.",
                "uploads/hajj_journey/img_hajj_journey_stage_2.png",
                R.drawable.img_hajj_journey_stage_2,
                R.drawable.hajj_line_left2,
                "#A3DE7C",
                "তাওয়াফুল কুদুমের নিয়মাবলী:\n• হাজরে আসওয়াদের কোণা থেকে তাওয়াফ শুরু ও শেষ করুন।\n• পুরুষরা প্রথম তিন চক্করে 'রমল' (বীরদর্পে দ্রুত হাঁটা) করবেন এবং ডান কাঁধ উন্মুক্ত রাখবেন (ইজতিবা)।\n• প্রতি চক্করে রুকনে ইয়ামানী অতিক্রমকালে 'রাব্বানা আতিনা ফিদ-দুনিয়া' দোয়া পাঠ করা সুন্নত।\n• সাত চক্কর শেষে মাকামে ইবরাহীমের পেছনে অথবা মসজিদের যেকোনো সুবিধাজনক স্থানে দুই রাকাত সালাত আদায় করুন।",
                "Tawaf al-Qudum Protocols:\n• Begin and conclude each circuit at the Black Stone (Hajar al-Aswad).\n• For men, perform Ramal (brisk, purposeful pace) during the first three circuits and Idtiba (uncovering the right shoulder).\n• Recite 'Rabbana Atina fid-Dunya' between the Yemeni Corner and Hajar al-Aswad.\n• Pray two Rak'ahs behind Maqam Ibrahim or anywhere feasible in Masjid al-Haram.",
                "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                "রব্বানা আতিনা ফিদ্ দুনয়া হাসানাতাও ওয়া ফিল আখিরাতি হাসানাতাও ওয়া ক্বিনা 'আযাবান নার।",
                "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
                "হে আমাদের প্রতিপালক! আমাদের ইহকালে কল্যাণ দান করুন এবং পরকালেও কল্যাণ দান করুন, আর আমাদেরকে জাহান্নামের আজাব থেকে রক্ষা করুন।",
                "Our Lord, give us in this world that which is good and in the Hereafter that which is good and protect us from the punishment of the Fire.",
                "সূরা আল-বাকারা: ২০১, সুনানে আবু দাউদ: ১৮৯২"
        ));

        // Stage 03: Safa and Marwa (Teal, curves right)
        list.add(new HajjJourneyStage(
                3,
                "০৩",
                "03",
                "সাফা ও মারওয়া",
                "Safa & Marwa",
                "সাফা ও মারওয়া পাহাড়ের মধ্যে সাঈ করুন, সাতবার হাঁটুন, শুরু করুন সাফা থেকে এবং শেষ করুন মারওয়া পর্যন্ত। সাঈটি তাওয়াফের পরেই করা উচিত।",
                "Perform Sa'i between the hills of Safa and Marwa by walking 7 laps, commencing at Safa and culminating at Marwa. Sa'i should ideally be performed following Tawaf.",
                "uploads/hajj_journey/img_hajj_journey_stage_3.png",
                R.drawable.img_hajj_journey_stage_3,
                R.drawable.hajj_line_right3,
                "#2FB68E",
                "সাঈর বিধান ও আদব:\n• সাফা পাহাড়ে উঠে ক্বিবলামুখী হয়ে তাকবীর, তাহলীল ও দোয়া করুন।\n• সাফা থেকে মারওয়ায় পৌঁছালে ১ চক্কর গণনা হবে। মারওয়া থেকে সাফায় এলে ২য় চক্কর হবে। এভাবে মোট ৭ চক্কর পূর্ণ হলে সাঈ সম্পন্ন হবে মারওয়ায়।\n• দুই সবুজ বাতির মধ্যবর্তী অংশে পুরুষদের দ্রুত কদমে দৌড়ানো সুন্নত।\n• সাঈ চলাকালীন কুরআন তিলাওয়াত ও ইস্তিগফার অব্যাহত রাখুন।",
                "Sa'i Protocols & Etiquettes:\n• Ascend Mount Safa, face the Qiblah, and make Takbeer, Tahleel, and heartfelt supplication.\n• Walking from Safa to Marwa constitutes 1 lap; Marwa to Safa is lap 2. The 7th lap finishes at Marwa.\n• Men should jog briskly between the two green light markers.\n• Spend the entire Sa'i in constant Dhikr, Quranic recitation, and sincere Istighfar.",
                "إِنَّ الصَّفَا وَالْمَرْوَةَ مِن شَعَائِرِ اللَّهِ ۖ فَمَنْ حَجَّ الْبَيْتَ أَوِ اعْتَمَرَ فَلَا جُنَاحَ عَلَيْهِ أَن يَطَّوَّفَ بِهِمَا",
                "ইন্নাস সাফা ওয়াল মারওয়াতা মিন শা'আইরিল্লাহ, ফামান হাজ্জাল বাইতা আওয়ি'তামারা ফালা জুনা-হা 'আলাইহি আই ইয়াত্তাওওয়াফা বিহিমা।",
                "Innas-Safa wal-Marwata min sha'a'irillah, faman hajjal-bayta awi'tamara fala junaha 'alayhi ay yattawwafa bihima.",
                "নিশ্চয়ই সাফা ও মারওয়া আল্লাহর নিদর্শনসমূহের অন্তর্ভুক্ত। অতএব যে ব্যক্তি বায়তুল্লাহর হজ বা উমরাহ পালন করে, উভয়ের মাঝে প্রদক্ষিণ (সাঈ) করাতে তার কোনো পাপ নেই।",
                "Indeed, Safa and Marwah are among the symbols of Allah. So whoever makes Hajj to the House or performs Umrah, there is no blame upon him for walking between them.",
                "সূরা আল-বাকারা: ১৫৮, সহীহ মুসলিম: ১২১৮"
        ));

        // Stage 04: Mina (Teal, curves left)
        list.add(new HajjJourneyStage(
                4,
                "০৪",
                "04",
                "মিনা",
                "Mina",
                "৮ই জিলহজ্জের সকালে মক্কা থেকে মিনা রওনা হন এবং সেখানেই রাত যাপন করুন। এই সময়টুকু দোয়া ও ইবাদতে ব্যস্ত থাকুন, যাতে আগামীকাল আরাফাত দিবসের প্রস্তুতি নিতে পারেন।",
                "On the morning of the 8th of Dhul Hijjah, depart from Makkah to Mina and spend the night there. Dedicate these hours to supplication and worship to prepare spiritually for the Day of Arafah tomorrow.",
                "uploads/hajj_journey/img_hajj_journey_stage_4.png",
                R.drawable.img_hajj_journey_stage_4,
                R.drawable.hajj_line_left3,
                "#2FB68E",
                "মিনায় অবস্থানের নিয়মাবলী (৮ই জিলহজ - ইয়াওমুত তারবিয়াহ):\n• জোহরের পূর্বেই মিনায় পৌঁছানোর চেষ্টা করুন।\n• মিনায় জোহর, আসর, মাগরিব, ইশা এবং ৯ই জিলহজের ফজর—এই পাঁচ ওয়াক্ত নামাজ কসর সহকারে আদায় করুন।\n• এই রাতে মিনায় অবস্থান করা সুন্নাত মুয়াক্কাদা।\n• বেশি বেশি তালবিয়া, তাওবাহ এবং ইস্তিগফারে রত থাকুন।",
                "Rites of Mina (8th Dhul Hijjah - Yawm at-Tarwiyah):\n• Aim to arrive in Mina before Dhuhr prayer.\n• Pray five obligatory prayers in Mina: Dhuhr, Asr, Maghrib, Isha, and Fajr of the 9th Dhul Hijjah, shortening 4-Rak'ah prayers.\n• Staying overnight in Mina on this night is an established Sunnah.\n• Engage consistently in Talbiyah, sincere repentance, and heartfelt contemplation.",
                "اللَّهُمَّ إِنِّي أَسْأَلُكَ رِضَاكَ وَالْجَنَّةَ وَأَعُوذُ بِكَ مِنْ سَخَطِكَ وَالنَّارِ",
                "আল্লাহুম্মা ইন্নী আসআলুকা রিদাকা ওয়াল জান্নাহ, ওয়া আ'ঊযু বিকা মিন সাখাত্বিকা ওয়ান নার।",
                "Allahumma inni as'aluka ridaka wal-jannah, wa a'oodhu bika min sakhatika wan-nar.",
                "হে আল্লাহ! আমি আপনার সন্তুষ্টি এবং জান্নাত প্রার্থনা করছি, আর আপনার ক্রোধ এবং জাহান্নামের আগুন থেকে আশ্রয় প্রার্থনা করছি।",
                "O Allah, I ask You for Your good pleasure and Paradise, and I seek refuge in You from Your wrath and the Fire.",
                "জামে আত-তিরমিযী: ২৫৫৮"
        ));

        // Stage 05: Arafat (Light Green, curves right)
        list.add(new HajjJourneyStage(
                5,
                "০৫",
                "05",
                "আরাফাত",
                "Arafat",
                "৯ই জিলহজ্জের সকালে মিনা থেকে আরাফাতে রওনা হন এবং সূর্যাস্ত পর্যন্ত সেখানে অবস্থান করুন। এই দিনটি \"হজই আরাফাত\" নামে পরিচিত। সূর্যাস্তের পূর্বে আরাফাত ত্যাগ করা উচিত নয়; এটি হজের শর্ত।",
                "On the morning of the 9th of Dhul Hijjah, journey from Mina to the plains of Arafat and remain there until sunset. This sacred day embodies the essence of pilgrimage ('Hajj is Arafah'). Do not depart before sunset as remaining is an essential condition.",
                "uploads/hajj_journey/img_hajj_journey_stage_5.png",
                R.drawable.img_hajj_journey_stage_5,
                R.drawable.hajj_line_right,
                "#A3DE7C",
                "উকুফে আরাফার শর্ত ও ফজিলত (৯ই জিলহজ - মূল হজ):\n• ৯ই জিলহজ দুপুর (জাওয়াল) থেকে সূর্যাস্ত পর্যন্ত আরাফাত ময়দানে অবস্থান করা হজের প্রধানতম ফরজ রুকন।\n• জোহর ও আসরের সালাত একত্রে জাম'আ তাকদীম করে আদায় করুন।\n• সূর্যাস্ত পর্যন্ত হাত তুলে ক্বিবলামুখী হয়ে অবিরাম রোনাজারি, ক্ষমা প্রার্থনা ও দোয়া করুন।\n• সূর্যাস্তের পূর্বে কোনো অবস্থাতেই আরাফাত ময়দানের সীমানা ত্যাগ করা যাবে না।",
                "Wuquf at Arafah Rites & Virtues (9th Dhul Hijjah - Core of Hajj):\n• Standing in the plain of Arafat from midday (Zawal) until sunset is the paramount obligatory pillar of Hajj.\n• Combine and shorten Dhuhr and Asr prayers together at the time of Dhuhr.\n• Spend the precious afternoon facing the Qiblah with raised hands, begging for forgiveness and mercy.\n• Pilgrims must strictly remain within Arafah boundaries until complete sunset.",
                "لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
                "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়া লাহুল হামদু, ওয়া হুয়া 'আলা কুল্লি শাইয়িন ক্বাদীর।",
                "La ilaha illallahu wahdahu la shareeka lah, lahul-mulku wa lahul-hamdu, wa huwa 'ala kulli shay'in qadeer.",
                "একমাত্র আল্লাহ ব্যতীত সত্য কোনো উপাস্য নেই, তাঁর কোনো শরিক নেই, রাজত্ব একমাত্র তাঁরই এবং সমস্ত প্রশংসাও তাঁরই, আর তিনি সর্ববিষয়ে সর্বশক্তিমান।",
                "There is no deity worthy of worship except Allah alone, with no partner. His is the sovereignty and all praise, and He is over all things competent.",
                "জামে আত-তিরমিযী: ৩৫৮৫, মুসনাদে আহমাদ: ৬৯৬১"
        ));

        // Stage 06: Muzdalifah (Yellow, curves left)
        list.add(new HajjJourneyStage(
                6,
                "০৬",
                "06",
                "মুজদালিফা",
                "Muzdalifah",
                "৯ই জিলহজ্জের সূর্যাস্তের পর মিনা থেকে মুজদালিফার উদ্দেশ্যে রওনা হন। সেখানে পৌঁছে মাগরিব ও ইশা নামাজ একত্রে কসর করে আদায় করুন। আগামী তিন দিন শয়তানকে পাথর ছোঁড়ার জন্য কমপক্ষে ৭০টি ছোট পাথর সংগ্রহ করুন। প্রতিদিন ২১টি করে পাথর প্রয়োজন।",
                "After sunset on the 9th of Dhul Hijjah, depart towards Muzdalifah. Upon arrival, perform Maghrib and Isha prayers together shortened. Collect at least 70 small pebbles for the next three days of stoning the Jamarat (21 pebbles required per day).",
                "uploads/hajj_journey/img_hajj_journey_stage_6.png",
                R.drawable.img_hajj_journey_stage_6,
                R.drawable.hajj_line_left1,
                "#EBCA28",
                "মুজদালিফায় অবস্থানের বিধানাবলী:\n• আরাফাত থেকে মাগরিব না পড়ে সরাসরি মুজদালিফায় এসে মাগরিব ও ইশা এক আজান ও দুই ইকামতে আদায় করুন।\n• মুজদালিফায় খোলা আকাশের নিচে রাত যাপন করা ওয়াজিব বা সুন্নাতে মুয়াক্কাদা।\n• জামরাতে পাথর মারার জন্য ছোলা বা মটরদানার সমান ৭০টি কঙ্কর সংগ্রহ করুন ও পরিষ্কার করে নিন।\n• ফজরের সালাত প্রথম ওয়াক্তে আদায় করে সূর্যোদয়ের পূর্ব পর্যন্ত মশ'আরুল হারামে দাঁড়িয়ে দোয়া করুন।",
                "Muzdalifah Rites & Directives:\n• Depart Arafat without praying Maghrib; combine Maghrib and Isha at Muzdalifah with one Adhan and two Iqamahs.\n• Spending the night under the open sky in Muzdalifah is an obligatory rite (Wajib).\n• Gather 70 chickpea-sized pebbles and rinse them carefully for stoning at the Jamarat.\n• Perform Fajr at dawn and make earnest supplication at al-Mash'ar al-Haram until sunrise.",
                "فَإِذَا أَفَضْتُم مِّنْ عَرَفَاتٍ فَاذْكُرُوا اللَّهَ عِندَ الْمَشْعَرِ الْحَرَامِ ۖ وَاذْكُرُوهُ كَمَا هَدَاكُمْ",
                "ফা ইযা আফাদতুম মিন 'আরাফাতিন ফাযকুরুল্লাহা 'ইন্দাল মাশ'আরিল হারাম, ওয়াযকুরূহু কামা হাদাকুম।",
                "Fa-idha afadtum min 'Arafatin fadh-kurullaha 'indal-Mash'aril-Haram, wadh-kuroohu kama hadakum.",
                "অতঃপর যখন তোমরা আরাফাত থেকে প্রত্যাবর্তন করবে, তখন মাশ'আরুল হারামের কাছে এসে আল্লাহকে স্মরণ করো এবং তাঁকে স্মরণ করো যেভাবে তিনি তোমাদের পথ প্রদর্শন করেছেন।",
                "Then when you depart from Arafat, remember Allah at al-Mash'ar al-Haram. And remember Him as He has guided you.",
                "সূরা আল-বাকারা: ১৯৮, সহীহ মুসলিম: ১২১৮"
        ));

        // Stage 07: Jamarat al-Aqabah (Light Green, curves right)
        list.add(new HajjJourneyStage(
                7,
                "০৭",
                "07",
                "জামরাত আল-আকাবায় পাথর নিক্ষেপ",
                "Stoning Jamarat al-Aqabah",
                "১০ই জিলহজ্জ (ঈদের দিন) সকালে মিনা ফিরে এসে জামরাত আল-আকাবাতে ৭টি পাথর নিক্ষেপ করুন। প্রতি পাথর নিক্ষেপের সময় \"আল্লাহু আকবার\" বলুন।",
                "On the morning of the 10th of Dhul Hijjah (Eid Day), return to Mina and cast 7 pebbles at Jamarat al-Aqabah (the Big Pillar). Recite 'Allahu Akbar' with each pebble thrown.",
                "uploads/hajj_journey/img_hajj_journey_stage_7.png",
                R.drawable.img_hajj_journey_stage_7,
                R.drawable.hajj_line_right2,
                "#A3DE7C",
                "১০ই জিলহজ রমি করার নিয়ম:\n• এ দিন শুধুমাত্র বড় স্তম্ভ (জামরাত আল-আকাবাহ)-তে ৭টি পাথর নিক্ষেপ করতে হবে।\n• প্রথম পাথর নিক্ষেপের সাথে সাথে পূর্বের তালবিয়া পাঠ বন্ধ করবেন।\n• প্রতিটি কঙ্কর নিক্ষেপকালে ডান হাত তুলে সজোরে 'আল্লাহু আকবার' বলুন।\n• পাথর সরাসরি হাউজের ভেতরে পতিত হতে হবে। অন্যকে আঘাত না করে শান্তভাবে আমলটি করুন।",
                "Stoning Rites on the 10th of Dhul Hijjah:\n• On this day, stone only the largest pillar (Jamarat al-Aqabah) with 7 pebbles.\n• Cease reciting the Talbiyah upon throwing the very first pebble.\n• Raise your right hand with each throw, proclaiming 'Allahu Akbar' clearly.\n• Ensure each pebble lands safely inside the basin without jostling others.",
                "بِسْمِ اللَّهِ، اللَّهُ أَكْبَرُ، رَغْمًا لِلشَّيْطَانِ وَرِضًا لِلرَّحْمَنِ",
                "বিসমিল্লাহি, আল্লাহু আকবার, রাগমান লিশ-শাইত্বানি ওয়া রিদান লির-রাহমান।",
                "Bismillahi, Allahu Akbar, raghman lish-shaytani wa ridan lir-Rahman.",
                "আল্লাহর নামে, আল্লাহ মহান! শয়তানের অবমাননাস্বরূপ এবং পরম করুণাময় আল্লাহর সন্তুষ্টির উদ্দেশ্যে।",
                "In the name of Allah, Allah is the Greatest! In defiance of Satan and seeking the pleasure of the Most Merciful.",
                "মুসনাদে আহমাদ: ১৫১৮, সুনানে বায়হাকী: ৯৬২৬"
        ));

        // Stage 08: Adhi / Qurbani (Dark Teal, curves left)
        list.add(new HajjJourneyStage(
                8,
                "০৮",
                "08",
                "আদহি (কোরবানির পশু)",
                "Sacrificial Animal (Adhi)",
                "আল্লাহর নৈকট্য লাভের উদ্দেশ্যে কোরবানির পশু উৎসর্গ করুন। কোরবানির মাংসের এক-তৃতীয়াংশ গরিবদের মধ্যে বিতরণ করা সুন্নত।",
                "Sacrifice a prescribed animal seeking nearness and acceptance from Allah. It is Sunnah to distribute one-third of the sacrificial meat among the needy.",
                "uploads/hajj_journey/img_hajj_journey_stage_8.png",
                R.drawable.img_hajj_journey_stage_8,
                R.drawable.hajj_line_left4,
                "#1E8787",
                "কোরবানি (দমে শোকর) সংক্রান্ত বিধান:\n• তামাত্তু ও ক্বেরান হজ আদায়কারীদের জন্য কোরবানি করা ওয়াজিব।\n• পাথর নিক্ষেপের পর এবং চুল কাটার পূর্বে কোরবানি সম্পন্ন করা মুস্তাহাব।\n• বর্তমানে সৌদি সরকারের অনুমোদিত 'আদাহী' ইসলামিক ডেভেলপমেন্ট ব্যাংক কুপনের মাধ্যমে কোরবানি সম্পন্ন করা নিরাপদ ও সুশৃঙ্খল।",
                "Qurbani (Hady) Directives:\n• Offering animal sacrifice is obligatory (Wajib) for pilgrims performing Tamattu and Qiran.\n• It is recommended to sacrifice after casting pebbles and before cutting/shaving hair.\n• Utilizing the authorized Saudi Adahi (Islamic Development Bank) voucher ensures hygienic distribution to the world's destitute.",
                "إِنَّ صَلَاتِي وَنُسُكِي وَمَحْيَايَ وَمَمَاتِي لِلَّهِ رَبِّ الْعَالَمِينَ ۝ لَا شَرِيكَ لَهُ",
                "ইন্না সালাতী ওয়া নুসুকী ওয়া মাহ্ইয়ায়া ওয়া মামাতী লিল্লাহি রব্বিল 'আলামীন, লা শারীকা লাহ।",
                "Inna salati wa nusuki wa mahyaya wa mamati lillahi Rabbil-'Alameen, la shareeka lah.",
                "নিশ্চয় আমার সালাত, আমার কোরবানি, আমার জীবন ও আমার মরণ একমাত্র বিশ্বজগতের প্রতিপালক আল্লাহর জন্য, যাঁর কোনো শরিক নেই।",
                "Indeed, my prayer, my rites of sacrifice, my living and my dying are for Allah, Lord of the worlds, with no partner.",
                "সূরা আল-আন'আম: ১৬২-১৬৩"
        ));

        // Stage 09: Hair Cutting / Shaving (Dark Teal, curves right)
        list.add(new HajjJourneyStage(
                9,
                "০৯",
                "09",
                "চুল কাটা বা কামানো",
                "Shaving or Trimming Hair",
                "১০ই জিলহজ্জের ঈদের দিনে জামরাত আল-আকাবাতে পাথর নিক্ষেপ ও কোরবানি শেষে আপনার চুল কাটা বা কামানো করুন। পুরুষদের জন্য পুরো মাথা কামানো (হলক) সুন্নত। মহিলাদের জন্য মাথা কামানো নিষিদ্ধ; তারা অবশ্যই চুল কেটে (তাকসীর) বের হবেন।",
                "On the day of Eid (10th Dhul Hijjah), after casting pebbles and offering sacrifice, shave or trim your hair. Shaving the entire head (Halq) is superior Sunnah for men. Shaving is prohibited for women; they must trim a fingertip's length (Taqseer) from their hair.",
                "uploads/hajj_journey/img_hajj_journey_stage_9.png",
                R.drawable.img_hajj_journey_stage_9,
                R.drawable.hajj_line_right4,
                "#1E8787",
                "চুল কাটা ও তাহাল্লুল সংক্রান্ত বিধান:\n• পুরুষদের জন্য মাথা কামানো (হলক) সর্বোত্তম; তবে চতুর্দিক থেকে সমানভাবে চুল ছোট করলেও চলবে।\n• মহিলারা চুলের প্রান্ত থেকে আঙুলের এক কর (প্রায় ১ ইঞ্চি) পরিমাণ কেটে নেবেন।\n• চুল কাটা সম্পন্ন হওয়ার মাধ্যমে হাজী সাহেব 'তাহাল্লুলে আসগর' (প্রথম হালাল) অর্জন করেন; ফলে স্ত্রী মিলন ব্যতীত ইহরামের সমস্ত নিষেধাজ্ঞা উঠে যায়।",
                "Halq/Taqseer Rites & First Deconsecration (Tahallul al-Asghar):\n• Complete head shaving (Halq) is three times more rewarded for men, though uniform trimming is valid.\n• Women gather their hair tips and trim approximately one fingertip length (approx 1 inch).\n• Upon completion, the pilgrim attains partial deconsecration; all Ihram prohibitions are lifted except marital relations.",
                "اللَّهُمَّ اغْفِرْ لِلْمُحَلِّقِينَ، اللَّهُمَّ ارْحَمِ الْمُحَلِّقِينَ، وَلِلْمُقَصِّরِينَ",
                "আল্লাহুম্মাগফির লিল মুহাল্লিক্বীন, আল্লাহুম্মারহামিল মুহাল্লিক্বীন, ওয়া লিল মুক্বাসসিরীন।",
                "Allahummagh-fir lil-muhalliqeen, Allahummar-hamil-muhalliqeen, wa lil-muqassireen.",
                "হে আল্লাহ! যারা মাথা কামিয়েছে তাদের ক্ষমা করুন, হে আল্লাহ! যারা মাথা কামিয়েছে তাদের ওপর রহম করুন, এবং যারা চুল ছোট করেছে তাদেরও ক্ষমা করুন।",
                "O Allah, forgive those who shave their heads. O Allah, have mercy on those who shave their heads, and also forgive those who trim their hair.",
                "সহীহ বুখারী: ১৭২৭, সহীহ মুসলিম: ১৩০১"
        ));

        // Stage 10: Tawaf al-Ifadah (Teal, curves left)
        list.add(new HajjJourneyStage(
                10,
                "১০",
                "10",
                "তাওয়াফুল ইফাদাহ",
                "Tawaf al-Ifadah",
                "১০ই জিলহজ্জ মক্কায় ফিরে এসে ৭ চক্কর দিয়ে কা'বা তাওয়াফ করুন। এরপর, সম্ভব হলে, মাকাম-ই-ইব্রাহিমের পেছনে দুই রাক'আত সুন্নত নামাজ পড়ুন।",
                "On the 10th of Dhul Hijjah, return to Makkah to circumambulate the Ka'bah 7 times. Afterwards, if feasible, pray two Rak'ahs of Sunnah prayer behind Maqam Ibrahim.",
                "uploads/hajj_journey/img_hajj_journey_stage_10.png",
                R.drawable.img_hajj_journey_stage_10,
                R.drawable.hajj_line_left,
                "#2FB68E",
                "তাওয়াফুল জিয়ারাহ/ইফাদাহ-এর আহকাম:\n• এটি হজের অন্যতম ফরজ রুকন। এটি না করলে কারো হজ পূর্ণ হবে না।\n• ১০ই জিলহজ থেকে ১২ই জিলহজের সূর্যাস্তের পূর্ব পর্যন্ত যেকোনো সময় এটি আদায় করা ওয়াজিব।\n• তাওয়াফ শেষে মাকামে ইবরাহীমে দুই রাকাত সালাত আদায় করুন এবং জমজমের পানি পান করুন।\n• তামাত্তু হজকারী এবং যারা পূর্বে হজের সাঈ করেননি, তারা তাওয়াফুল ইফাদাহর পর সাফা-মারওয়ায় সাঈ করবেন।\n• এই তাওয়াফের পর 'তাহাল্লুলে আকবর' সম্পন্ন হয় এবং স্ত্রী সহবাস সহ সব কিছু হালাল হয়ে যায়।",
                "Tawaf al-Ifadah (Tawaf az-Ziyarah) Directives:\n• This is an indispensable pillar of Hajj without which the pilgrimage remains incomplete.\n• Must be performed between 10th Dhul Hijjah and before sunset of 12th Dhul Hijjah.\n• Pray two Rak'ahs behind Maqam Ibrahim and drink pure Zamzam water to fulfillment.\n• Perform Sa'i between Safa and Marwa if not performed during Arrival Tawaf.\n• Upon completing this Tawaf, full deconsecration (Tahallul al-Akbar) is achieved.",
                "ثُمَّ لْيَقْضُوا تَفَثَهُمْ وَلْيُوفُوا نُذُورَهُمْ وَلْيَطَّوَّفُوا بِالْبَيْتِ الْعَتِيقِ",
                "সুম্মা লিয়াক্বদূ তাফাসা-হুম ওয়াল ইয়ূফূ নুযূরাহুম ওয়াল ইয়াত্বাত্বাওওয়াফূ বিল বাইতিল 'আতীক্ব।",
                "Thumma lyaqdoo tafathahum wal-yoofoo nudhoorahum wal-yattawwafoo bil-Baytil-'Ateeq.",
                "অতঃপর তারা যেন তাদের শারীরিক ময়লা দূর করে, তাদের মানত পূর্ণ করে এবং এই প্রাচীন ঘরের (পবিত্র কাবা) তাওয়াফ সম্পন্ন করে।",
                "Then let them end their untidiness and fulfill their vows and perform Tawaf around the Ancient House.",
                "সূরা আল-হাজ্জ: ২৯, সহীহ মুসলিম: ১২১৮"
        ));

        // Stage 11: Stoning at Jamarat (Teal, curves right)
        list.add(new HajjJourneyStage(
                11,
                "১১",
                "11",
                "জামরাতে পাথর নিক্ষেপ",
                "Stoning at Jamarat",
                "১১, ১২ ও ১৩ জিলহজ্জ মিনা ফিরে এসে জামরাতের তিনটি স্তম্ভে (জামরাত আল-আকাবাহ, জামরাত আল-উস্তা, ও জামরাত আল-সুগরা) ৭টি করে পাথর নিক্ষেপ করুন। পাথর নিক্ষেপের সময় \"আল্লাহু আকবার\" বলুন।",
                "On the 11th, 12th, and optionally 13th of Dhul Hijjah, return to Mina and cast 7 pebbles at each of the three Jamarat pillars (al-Aqabah, al-Wusta, and al-Sughra). Proclaim 'Allahu Akbar' with each throw.",
                "uploads/hajj_journey/img_hajj_journey_stage_11.png",
                R.drawable.img_hajj_journey_stage_11,
                R.drawable.hajj_line_right3,
                "#2FB68E",
                "আইয়ামে তাশরীক্বে পাথর নিক্ষেপের নিয়মাবলী:\n• ১১ ও ১২ই জিলহজ সূর্য পশ্চিমে ঢলে যাওয়ার (জাওয়াল/দুপুর) পর পাথর নিক্ষেপের ওয়াক্ত শুরু হয়।\n• ধারাবাহিকতা রক্ষা করা ওয়াজিব: প্রথমে ছোট জামরাত (সুগরা), এরপর মধ্যম জামরাত (উস্তা), এবং শেষে বড় জামরাত (আকাবাহ)।\n• প্রতিটি স্তম্ভে ৭টি করে মোট ২১টি পাথর নিক্ষেপ করতে হবে।\n• ছোট ও মধ্যম জামরাতে রমি শেষে কিবলামুখী হয়ে দীর্ঘ সময় দুই হাত তুলে প্রাণখুলে দোয়া করা সুন্নত।",
                "Stoning Directives during Ayyam at-Tashreeq:\n• On the 11th and 12th of Dhul Hijjah, the stoning window begins after midday (Zawal).\n• Order is mandatory: begin with Small Jamarat (Sughra), then Middle (Wusta), concluding with Large (Aqabah).\n• Cast 7 pebbles at each pillar (totaling 21 pebbles daily).\n• After stoning the Small and Middle Jamarat, step aside facing the Qiblah for prolonged supplication.",
                "اللَّهُمَّ اجْعَلْهُ حَجًّا مَبْرُورًا، وَذَنْبًا مَغْفُورًا، وَسَعْيًا مَشْكُورًا",
                "আল্লাহুম্মাজ'আলহু হাজ্জাম মাবরূরা, ওয়া যামবাম মাগফূরা, ওয়া সা'ইয়াম মাশকূরা।",
                "Allahummaj-'alhu hajjan mabrooran, wa dhanban maghfooran, wa sa'yan mashkooran.",
                "হে আল্লাহ! আমার এই হজকে মাবরুর (কবুল) হজ বানিয়ে দিন, সমস্ত পাপ ক্ষমা করে দিন এবং প্রচেষ্টাকে পুরস্কৃত করুন।",
                "O Allah, make this an accepted pilgrimage (Hajj Mabrur), forgiven sin, and appreciated effort.",
                "মুসান্নাফ ইবনে আবি শায়বা: ১৩২২৮"
        ));

        // Stage 12: Farewell Tawaf (Dark Teal, curves left)
        list.add(new HajjJourneyStage(
                12,
                "১২",
                "12",
                "বিদায়ী তাওয়াফ",
                "Farewell Tawaf",
                "মক্কা ত্যাগের পূর্বে তাওয়াফুল বিদা করুন। এটি ৭ চক্কর দিয়ে কা'বা তাওয়াফ করা। এ সময় \"বিসমিল্লাহ, আল্লাহু আকবার\" বলা সুন্নত। শেষে মাকাম-ই-ইব্রাহিমের পেছনে দুই রাক'আত সুন্নত নামাজ পড়া সুন্নত।",
                "Before departing from the holy city of Makkah, perform the Farewell Circumambulation (Tawaf al-Wida). Circumambulate the Ka'bah 7 circuits, reciting 'Bismillah, Allahu Akbar'. Conclude by offering two Rak'ahs of Sunnah prayer behind Maqam Ibrahim.",
                "uploads/hajj_journey/img_hajj_journey_stage_12.png",
                R.drawable.img_hajj_journey_stage_12,
                R.drawable.hajj_line_left4,
                "#1E8787",
                "বিদায়ী তাওয়াফ (তাওয়াফুল বিদা)-এর বিধান:\n• বহিরাগত সমস্ত হাজীর জন্য মক্কা ত্যাগ করার ঠিক পূর্বে এটি আদায় করা ওয়াজিব।\n• এতে রমল বা ইজতিবা নেই এবং এর পরে সাঈ করতে হয় না।\n• সাত চক্কর শেষে মাকামে ইবরাহীমে দুই রাকাত সালাত আদায় করুন।\n• মুলতাযামে বুক-কপাল ঠেকিয়ে অশ্রুসজল নয়নে কা'বা শরীফের উদ্দেশ্যে শেষ সালাম ও দোয়া পেশ করুন এবং বিদায়ের মুহূর্তে বেশি বেশি দরুদ পড়ুন।",
                "Farewell Tawaf (Tawaf al-Wida) Protocols:\n• Obligatory (Wajib) for all out-of-town pilgrims just prior to leaving Makkah.\n• There is no Ramal, Idtiba, or Sa'i following this circumambulation.\n• Pray two Rak'ahs of Sunnah behind Maqam Ibrahim and drink Zamzam with gratitude.\n• Stand humbly at the Multazam, pouring out your soul in farewell supplications, wishing to return to Allah's Sacred House.",
                "اللَّهُمَّ لَا تَجْعَلْ هَذَا آخِرَ الْعَهْدِ بِبَيْتِكَ الْحَرَامِ، وَإِنْ جَعَلْتَهُ فَاعْوِضْنِي عَنْهُ الْجَنَّةَ",
                "আল্লাহুম্মা লা তাজ'আল হাযা আখিরাল 'আহদি বি-বাইতিকাল হারাম, ওয়া ইন জা'আলতাহু ফা'বিদনী 'আনহুল জান্নাহ।",
                "Allahumma la taj'al hadha akhiral-'ahdi bi-baytikal-haram, wa in ja'altahu fa-'awidnee 'anhul-jannah.",
                "হে আল্লাহ! আপনার সম্মানিত পবিত্র ঘরের সাথে এটিই যেন আমার শেষ সাক্ষাৎ না হয়; আর যদি শেষ সময় হয়েই থাকে, তবে এর বিনিময়ে আমাকে জান্নাত দান করবেন।",
                "O Allah, do not make this my final visit to Your Sacred House; and if You have decreed it so, compensate me with Paradise in return.",
                "সহীহ বুখারী: ১৭৫৫, সহীহ মুসলিম: ১৩২৮"
        ));

        return list;
    }
}
