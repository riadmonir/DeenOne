package com.devflux.deenone.features.ramadan.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for সিয়ামের প্রকারভেদ, মাসের বৈশিষ্ট্য ও তার রোযার মাহাত্ম্য
 * (Types of Fasting, Ramadan's Characteristics, and the Greatness of its Fast).
 * Pre-seeded with 4 complete verbatim cards matching user requirements.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaFastingTypesRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaFastingTypesRepository() {
        // Utility class
    }

    public interface DataCallback {
        void onDataLoaded(List<HajjHistoryCardItem> items);
    }

    public static List<HajjHistoryCardItem> getAllCards(Context context, DataCallback callback) {
        if (context != null && NetworkConnectivityHelper.isOnline(context)) {
            final Context appContext = context.getApplicationContext();
            executor.execute(() -> fetchRemoteCards(appContext, callback));
        }
        return new ArrayList<>(cachedList);
    }

    public static List<HajjHistoryCardItem> getAllCards() {
        return new ArrayList<>(cachedList);
    }

    private static void fetchRemoteCards(Context context, DataCallback callback) {
        HttpURLConnection conn = null;
        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_fasting_types.php");
            URL url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    JsonObject root = JsonParser.parseString(sb.toString()).getAsJsonObject();
                    if (root.has("success") && root.get("success").getAsBoolean() && root.has("cards")) {
                        JsonArray arr = root.getAsJsonArray("cards");
                        List<HajjHistoryCardItem> remoteItems = new ArrayList<>();

                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int id = obj.has("id") ? obj.get("id").getAsInt() : (remoteItems.size() + 1);
                            String titleBn = obj.has("title_bn") ? obj.get("title_bn").getAsString() : "";
                            String titleEn = obj.has("title_en") ? obj.get("title_en").getAsString() : "";
                            String previewBn = obj.has("preview_bn") ? obj.get("preview_bn").getAsString() : "";
                            String previewEn = obj.has("preview_en") ? obj.get("preview_en").getAsString() : "";
                            String fullContentBn = obj.has("full_content_bn") ? obj.get("full_content_bn").getAsString() : "";
                            String fullContentEn = obj.has("full_content_en") ? obj.get("full_content_en").getAsString() : "";

                            remoteItems.add(new HajjHistoryCardItem(
                                    id, titleBn, titleEn, previewBn, previewEn, fullContentBn, fullContentEn
                            ));
                        }

                        if (!remoteItems.isEmpty()) {
                            cachedList.clear();
                            cachedList.addAll(remoteItems);
                            if (callback != null) {
                                mainHandler.post(() -> callback.onDataLoaded(new ArrayList<>(remoteItems)));
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Fallback to pre-seeded static data gracefully
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: রোযার প্রকারভেদ
        list.add(new HajjHistoryCardItem(
                1,
                "রোযার প্রকারভেদ",
                "Categories of Fasting",
                "রোযা হল দুই প্রকার; ফরয (বাধ্যতামূলক) ও নফল (অতিরিক্ত)। ফরয রোযা আবার ৩ প্রকার; রমাযানের রোযা, কাফ্ফারার রোযা এবং নযর মানা রোযা...",
                "Fasting is divided into two primary categories: Obligatory (Fard) and Voluntary (Nafl). Obligatory fasting is further sub-divided into three types...",
                "রোযা হল দুই প্রকার; ফরয (বাধ্যতামূলক) ও নফল (অতিরিক্ত)। ফরয রোযা আবার ৩ প্রকার; রমাযানের রোযা, কাফ্ফারার রোযা এবং নযর মানা রোযা।",
                "Fasting is divided into two primary categories: Obligatory (Fard) and Voluntary (Nafl). Obligatory fasting is further subdivided into three types: the fasts of Ramadan, expiation (Kaffarah) fasts, and fulfillment of vows (Nadhr)."
        ));

        // Card 2: রমাযানের রোযার মান
        list.add(new HajjHistoryCardItem(
                2,
                "রমাযানের রোযার মান",
                "Status of Fasting in Ramadan",
                "রমাযানের রোযা আল্লাহর কিতাব, তাঁর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর হাদীস ও মুসলিম উম্মাহর ইজমা’ (সর্বসম্মতি) মতে চিরকালের জন্য ফরয...",
                "The fast of Ramadan is an eternal divine obligation upon every Muslim, established firmly through the Quran, prophetic Sunnah, and unanimous consensus...",
                "রমাযানের রোযা আল্লাহর কিতাব, তাঁর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর হাদীস ও মুসলিম উম্মাহর ইজমা’ (সর্বসম্মতি) মতে চিরকালের জন্য ফরয।\n\n"
                        + "কুরআন কারীমে মহান আল্লাহ বলেন,\n\n"
                        + "﴿يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِنْ قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ- أَيَّاماً مَعْدُودَاتٍ﴾\n\n"
                        + "‘‘হে ঈমানদারগণ! তোমাদের উপর রোযা ফরয করা হল, যেমন তোমাদের পূর্ববর্তী উম্মতের উপর ফরয করা হয়েছিল। যাতে তোমরা পরহেযগার হতে পার। তা নির্ধারিত কয়েক দিন--।’’(কুরআনুল কারীম ২/১৮৩-১৮৪)\n\n"
                        + "তিনি আরো বলেন,\n\n"
                        + "﴿شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدىً لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ﴾\n\n"
                        + "‘‘রমাযান মাস; যে মাসে মানুষের দিশারী এবং সৎপথের স্পষ্ট নিদর্শন ও সত্যাসত্যের পার্থক্যকারীরূপে কুরআন অবতীর্ণ হয়েছে। অতএব তোমাদের মধ্যে যে কেউ এ মাস পাবে সে যেন এ মাসে রোযা রাখে।’ (কুরআনুল কারীম ২/১৮৫)\n\n"
                        + "হাদীসে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘ইসলামের ভিত্তি হল ৫টি কর্ম; আল্লাহ ছাড়া কেউ সত্যিকারে উপাস্য নেই এবং মুহাম্মাদ (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁর রসূল (দূত) -এই কথার সাক্ষ্য দেওয়া, নামায কায়েম করা, যাকাৎ প্রদান করা, রমাযানের রোযা রাখা এবং সামর্থ্য থাকলে কা’বাগৃহের হজ্জ করা।’’[1]\n\n"
                        + "তালহা বিন উবাইদুল্লাহ (রাঃ) বলেন, এক ব্যক্তি নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-কে জিজ্ঞাসা করল, ‘হে আল্লাহর রসূল! আমার উপর আল্লাহ কি কি রোযা ফরয করেছেন তা আমাকে বলে দিন।’ উত্তরে তিনি বললেন, ‘‘রমাযান মাসের রোযা।’’ লোকটি বলল, ‘এ ছাড়া অন্য কিছু কি আমার কর্তব্য আছে?’ তিনি বললেন, ‘‘না, তবে যদি তুমি নফল রোযা রাখ, তাহলে ভিন্ন কথা।’’[2]\n\n"
                        + "আর মুসলিম উম্মাহ এ বিষয়ে একমত যে, রমাযানের রোযা ফরয। তা ইসলামের অন্যতম রুকন, খুঁটি বা ভিত্তি। এ ফরয হওয়ার কথা সহজ উপায়ে সকলের জানা। সুতরাং যে কেউ তা অস্বীকার করবে সে মুরতাদ্দ্ কাফের। তাকে তওবা করার জন্য আহবান করা হবে। তাতে সে তওবা করলে এবং রোযা ফরয বলে মেনে নিলে উত্তম। নচেৎ, (সরকার) তাকে কাফের অবস্থায় হত্যা করবে।[3]\n\n"
                        + "[1] (বুখারী ৮, মুসলিম ১৬নং, তিরমিযী, নাসাঈ)\n\n"
                        + "[2] (বুখারী, ১৮৯১, মুসলিম)\n\n"
                        + "[3] (ফিকহুস সুন্নাহ ১/৩৮৩, ফুসূল ৪-৫পৃঃ)",
                "Fasting in the month of Ramadan is an eternal and indisputable obligation upon every accountable Muslim, firmly established through the Book of Allah, the authentic Sunnah of His Messenger (peace and blessings be upon him), and the unanimous consensus (Ijma') of the Muslim Ummah.\n\n"
                        + "Allah the Almighty states in the Glorious Quran:\n\n"
                        + "﴿يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِنْ قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ- أَيَّاماً مَعْدُودَاتٍ﴾\n\n"
                        + "'O you who believe! Fasting is prescribed for you as it was prescribed for those before you, that you may attain Taqwa (piety and righteousness)—for a specified number of days.' (Surah Al-Baqarah 2:183-184)\n\n"
                        + "He also says:\n\n"
                        + "﴿شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدىً لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ﴾\n\n"
                        + "'The month of Ramadan in which was revealed the Quran, a guidance for the people and clear proofs of guidance and criterion. So whoever sights the month, let him fast it.' (Surah Al-Baqarah 2:185)\n\n"
                        + "In the Hadith, the Prophet (peace and blessings be upon him) said: ''Islam is built upon five pillars: testifying that none has the right to be worshipped but Allah and that Muhammad is the Messenger of Allah, establishing the prayer, paying Zakah, fasting the month of Ramadan, and performing Hajj to the House for whoever has the means.''[1]\n\n"
                        + "Talhah ibn Ubaydullah (may Allah be pleased with him) narrated: A man approached the Prophet (peace and blessings be upon him) and asked: 'O Messenger of Allah! Tell me what fasting Allah has obligated upon me.' The Prophet replied: ''The month of Ramadan.'' The man asked: 'Is there any other fast incumbent upon me?' He replied: ''No, unless you choose to fast voluntarily.''[2]\n\n"
                        + "The Muslim Ummah is entirely unanimous that fasting during Ramadan is an absolute obligation and a foundational pillar of Islam. This obligation is known of necessity by all Muslims, common and scholar alike. Therefore, whoever denies its obligation becomes an apostate (Murtadd) disbeliever. He is called upon to repent; if he repents and acknowledges fasting as an obligation, he is restored to Islam. Otherwise, the lawful authority executes him as an apostate.[3]\n\n"
                        + "[1] (Sahih Bukhari #8; Sahih Muslim #16; Jami' at-Tirmidhi; Sunan an-Nasa'i)\n\n"
                        + "[2] (Sahih Bukhari #1891; Sahih Muslim)\n\n"
                        + "[3] (Fiqh us-Sunnah 1/383; Fusool, pp. 4-5)"
        ));

        // Card 3: রমাযান মাসের বৈশিষ্ট্য ও তার রোযার মাহাত্ম্য
        list.add(new HajjHistoryCardItem(
                3,
                "রমাযান মাসের বৈশিষ্ট্য ও তার রোযার মাহাত্ম্য",
                "Virtues of Ramadan and its Fast",
                "রমাযান শব্দটি ‘রম্য’ ধাতু থেকে উৎপত্তি। এর মানে হল কঠিন গরম, জ্বালিয়ে দেওয়া। রমাযানের একাধিক এমন বৈশিষ্ট্য রয়েছে, যা অন্যান্য মাসে নেই...",
                "The name Ramadan originates from 'Ramad', signifying intense scorching heat. Ramadan possesses numerous unique distinctions unmatched by any other month...",
                "রমাযান শব্দটি ‘রম্য’ ধাতু থেকে উৎপত্তি। এর মানে হল কঠিন গরম, জ্বালিয়ে দেওয়া। চান্দ্র মাসগুলোর যখন প্রাচীন নাম বাদ দিয়ে আরবী ভাষায় নতুন নাম দেওয়া হয়, তখন রমাযান মাসটি পরে কঠিন গরমের সময়। আর তাকেই ভিত্তি করে তার ‘রামাযান’ নামকরণ করা হয়। অবশ্য রমাযান মাসের রোযা ফরয হওয়ার পর তার নাম সার্থক হয়। যেহেতু উক্ত মাসে ক্ষুৎপিপাসায় রোযাদারের পেট জ্বলে থাকে।\n\n"
                        + "রমাযানের মাসের একাধিক এমন বৈশিষ্ট্য রয়েছে, যা অন্যান্য মাসে নেই। যেমনঃ-\n\n"
                        + "১। এই মাসের নাম কুরআন মাজীদে উল্লেখিত হয়েছে। (দ্রষ্টব্য কুরআনুল কারীম ২/১৮৫)\n\n"
                        + "পক্ষান্তরে অন্য মাসের নাম উল্লেখ করা হয়নি।\n\n"
                        + "২। এই মাস আসার সময় মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁর সাহাবাগণকে সুসংবাদ দিতেন।\n\n"
                        + "৩। রমাযান হল বর্কতময় পবিত্র মাস। এ মাসে বর্কত অবতীর্ণ হয়।\n\n"
                        + "৪। মহান আল্লাহ এই মাসের রোযা ফরয করেছেন।\n\n"
                        + "৫। এই মাসে জান্নাতের দ্বারসমূহ উন্মুক্ত করা হয় এবং একটা দ্বারও বন্ধ থাকে না।\n\n"
                        + "৬। এই মাসে রহমতের সকল দরজা খুলে দেওয়া হয়।[1]\n\n"
                        + "৭। এই মাসে জাহান্নামের দ্বারসমূহ রুদ্ধ করা হয় এবং একটা দ্বারও খোলা থাকে না।\n\n"
                        + "উল্লেখ্য যে, বেহেশ্তের সকল দরজা খুলে দেওয়ার কারণ হল, যাতে করে আমলকারী তা শুনে আমলে আগ্রহ ও উৎসাহ পায় এবং তাতে প্রবেশ করার জন্য প্রস্ত্ততি গ্রহণ করে। আর দোযখের সকল দরজা বন্ধ করার কারণ হল, যাতে আমলকারী এই মাসে পাপে লিপ্ত না হয় এবং তাতে প্রবেশ না করে বসে। এর অর্থ এই নয় যে, যে ব্যক্তি রমাযান মাসে মারা যাবে সে বিনা হিসাবে সোজা বেহেশ্তে যাবে।[2]\n\n"
                        + "৮। উচ্ছৃঙ্খল শয়তান দলকে এই মাসে বন্দী করে রাখা হয়।[3] অর্থাৎ, তাদেরকে শিকল ও বেড়ি দিয়ে বেঁধে আটকে রাখা হয়। ফলে তারা রমাযানে সেই পাপাচরণ ঘটাতে সক্ষম হয় না, যতটা অন্য মাসে সক্ষম হয়। এ জন্য দেখা যায় যে, অন্যান্য মাসের তুলনায় এই মাসে শয়তানের কুমন্ত্রণা, চক্রান্ত এবং মানুষকে বিভ্রান্ত করার কাজ কম ঘটে থাকে। বরং শয়তান রমাযান মাসকে ভয় করে, যেমন ভয় করে আযান ও ইকামতকে এবং তার শব্দ শুনে পাদতে পাদতে পলায়ন করে।\n\n"
                        + "কিন্তু আমরা এ মাসেও যে পাপাচরণ ও শয়তানী কর্মকান্ড ঘটতে দেখে থাকি তা উক্ত কথার বিরোধী নয়। কারণ, পাপ কেবল শয়তানই ঘটায় না। বরং মন্দপ্রবণ মানুষের মনও এমনিতেই পাপ করে থাকে। যে মন শয়তানের কুমন্ত্রণা সত্বর গ্রহণ করে থাকে এবং শয়তানের তাসীর কম বা বন্ধ হয়ে গেলেও সেই মন নিজেই পাপ সৃষ্টি করে। এটি হল মানুষের ‘নাফ্সে আম্মারাহ।’ যে নাফ্স বা মন শয়তানের প্রতিনিধিত্ব করে এবং সেই পাপাচরণ ঘটিয়ে থাকে। নাঊযু বিল্লাহি মিন যালিক।[4]\n\n"
                        + "৯। রমাযান মাসে আসমানের দরজাসমূহ উন্মুক্ত করা হয়।[5]\n\n"
                        + "১০। এই মাসে দুআ কবুল হয়। মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘(রমাযান মাসের) প্রত্যেক রাতে ও দিনে প্রত্যেক মুসলিমের দুআ কবুল করা হয়।’’[6]\n\n"
                        + "১১। এই মাসে রয়েছে এমন একটি রাত্রি, যা হাজার মাস অপেক্ষা শ্রেষ্ঠ। যে ব্যক্তি এই রাতের মঙ্গল থেকে বঞ্চিত হয়, সে আসলে সকল মঙ্গল থেকে বঞ্চিত। আর একান্ত বঞ্চিত ব্যক্তি ছাড়া সে মঙ্গল থেকে কেউ বঞ্চিত হয় না।[7]\n\n"
                        + "১২। এই মাসের প্রত্যেক রাত্রে একজন আহবানকারী (ফিরিশ্তা) আহবান করে বলেন, ‘ওহে কল্যাণকামী! তুমি অগ্রসর হও। আর ওহে মন্দকামী! তুমি ক্ষান্ত হও।’\n\n"
                        + "১৩। এই মাসের প্রত্যেক রাত্রে মহান আল্লাহ দোযখ থেকে মুসলিম মুক্ত করে থাকেন।[8]\n\n"
                        + "১৪। রমাযান মাস হল সবর ও ধৈর্যের মাস। যেহেতু রোযা ছাড়া অন্য ইবাদতে সেইরূপ ধৈর্যের পরীক্ষা দিতে হয় না। মুসলিম এই মাসে পূর্ণ ৩০ বা ২৯টি দিনই পানাহার, স্ত্রী-মিলন এবং অন্যান্য রোযাবিরোধী সকল কর্ম থেকে ধৈর্যের সাথে বিরত থাকে। তাই মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) এই মাসকে ‘ধৈর্যের মাস’ বলে অভিহিত করেছেন।[9] আর তিনি বলেছেন, ‘‘ধৈর্যের (রমাযান) মাসে রোযা আর প্রত্যেক মাসের তিনটি রোযা অন্তরের বিদ্বেষ ও খট্কা দূর করে দেয়।’’[10]\n\n"
                        + "১৫। রমাযান হল কুরআনের মাস। কুরআন পঠন-পাঠন ও তেলাঅতের মাস। প্রশংসার অধিকারী বিজ্ঞানময় আল্লাহর তরফ থেকে কুরআন অবতীর্ণ হওয়ার মাস। এই মাসে কুরআন ‘লাওহে মাহফূয’ থেকে দুনিয়ার আসমানের প্রতি অবতীর্ণ হয়, অথবা কুরআন অবতীর্ণ হতে শুরু হয় এই পবিত্র মাসে। মহান আল্লাহ বলেন,\n\n"
                        + "﴿شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدىً لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ﴾\n\n"
                        + "‘‘রমাযান মাস; যে মাসে মানুষের দিশারী এবং সৎপথের স্পষ্ট নিদর্শন ও সত্যাসত্যের পার্থক্যকারীরূপে কুরআন অবতীর্ণ হয়েছে। অতএব তোমাদের মধ্যে যে কেউ এ মাস পাবে সে যেন এ মাসে রোযা রাখে।’’\n\n"
                        + "(কুরআনুল কারীম ২/১৮৫)\n\n"
                        + "আর কেবল কুরআনই নয়; বরং অন্যান্য আসমানী গ্রন্থসমূহও অবতীর্ণ হয়েছে এই বর্কতময় মাসেই। ‘‘ইবরাহীমের সহীফাসমূহ অবতীর্ণ হয়েছে রমাযান মাসের প্রথম রাতে, তাওরাত অবতীর্ণ হয়েছে রমাযানের সপ্তম রাতে, ইঞ্জীল অবতীর্ণ হয়েছে রমাযানের ১৪তম রাতে, যাবূর অবতীর্ণ হয়েছে রমাযানের ১৯শের রাতে এবং কুরআন অবতীর্ণ হয়েছে রমাযানের ২৫শের রাতে।’’[11]\n\n"
                        + "১৬। রমাযান মাসে বিস্ময়কর বড় বড় বিজয় দানের মাধ্যমে মহান আল্লাহ ইসলামকে সাহায্য করেছেন, মুসলিমদেরকে মর্যাদা দিয়েছেন। এই মাসেই বদর যুদ্ধে বদর প্রান্তরে মুসলিমদের আধ্যাত্মিক শক্তিমত্তা, ঈমানী ভিত্তির সুদৃঢ়তা এবং প্রতীতির অবিচলতা প্রকাশ পায়। আল্লাহ তাআলা এই দিনে তাঁদেরকে সাহায্য করেন এবং শত্রুর উপর বিজয়ী করেন।\n\n"
                        + "অষ্টম হিজরীর রমাযান মাসে মক্কা বিজয়ের মাধ্যমে আল্লাহ তাঁদেরকে সাহায্য করেন। যার পর মুসলিমরা স্থিতিশীলতা পেলেন এবং ইসলাম পূর্ণরূপে প্রতিষ্ঠা লাভ করল।[12]\n\n"
                        + "১৭। রমাযান মাসে কোন কোন আমলের বহুগুণ সওয়াব লাভ হয়। এ মাসে উমরাহ আদায় আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর সাথে হজ্জ করার সমান।[13]\n\n"
                        + "পক্ষান্তরে এ মাসের রোযা রাখার সওয়াব প্রসঙ্গে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন,\n\n"
                        + "(ক) ‘‘যে ব্যক্তি ঈমান ও বিÇবাসের সাথে এবং সওয়াবের আশা রেখে রমাযানের রোযা রাখবে তারও পূর্বেকার পাপরাশি মাফ হয়ে যাবে।’’[14]\n\n"
                        + "(খ) ‘‘পাঁচ ওয়াক্ত নামায, এক জুমআহ হতে অপর জুমআহ পর্যন্ত ও এক রমাযান অপর রমাযান পর্যন্ত উভয়ের মধ্যবর্তীকালের সংঘটিত পাপরাশিকে মোচন করে দেয়; যদি কাবীরা গোনাহ থেকে দূরে থাকা হয় তবে।’’[15]\n\n"
                        + "(গ) এক ব্যক্তি মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর কাছে হাজির হয়ে আরজ করল, ‘হে আল্লাহর রসূল! আপনার অভিমত কি? যদি আমি সাক্ষ্য দিই যে, আল্লাহ ছাড়া কোন সত্য উপাস্য নেই এবং আপনি আল্লাহর রসূল, পাঁচ-ওয়াক্ত নামায পড়ি, যাকাত আদায় করি এবং রমাযানের রোযা পালন করি, তাহলে আমি কাদের দলভুক্ত হব?’ উত্তরে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘তুমি সিদ্দীক ও শহীদগণের দলভুক্ত হবে।’’[16]\n\n"
                        + "[1] (সিলসিলাহ সহীহাহ, আলবানী ১৩০৭, সহীহুল জামেইস সাগীর, আলবানী ৪৭১নং)\n\n"
                        + "[2] (ফাসিঃ ২২পৃঃ)\n\n"
                        + "[3] (বুখারী ১৮০, মুসলিম ১০৭৯নং)\n\n"
                        + "[4] (দুরুসু রামাযান অকাফাত লিস্-সায়েমীন ২১পৃঃ)\n\n"
                        + "[5] (বুখারী ১৮৯৯, আহমাদ, মুসনাদ, নাসাঈ, সিলসিলাহ সহীহাহ, আলবানী ১৮৬৮নং)\n\n"
                        + "[6] (বাযযার, সহীহ তারগীব, আলবানী ৯৮৮নং)\n\n"
                        + "[7] (সহীহ ইবনে মাজাহ, আলবানী ১৩৩৩, সহীহুল জামেইস সাগীর, আলবানী ৩৫১৯নং)\n\n"
                        + "[8] (আহমাদ, মুসনাদ ৪/৩১২, ৫/৪১১, নাসাঈ, সহীহ ইবনে মাজাহ, আলবানী ১৩৩১নং)\n\n"
                        + "[9] (সহীহুল জামেইস সাগীর, আলবানী ৩৮০৩নং)\n\n"
                        + "[10] (বাযযার, ত্বাবারানী, মু’জাম, ইবনে হিববান, সহীহ, সহীহুল জামেইস সাগীর, আলবানী ৩৮০৪নং)\n\n"
                        + "[11] (আহমাদ, মুসনাদ, ত্বাবারানী, মু’জাম, সহীহুল জামেইস সাগীর, আলবানী ১৪৯৭, সিলসিলাহ সহীহাহ, আলবানী ১৫৭৫নং)\n\n"
                        + "[12] (সাওমু রামাযান ৮-১৩পৃঃ)\n\n"
                        + "[13] (বুখারী ১৮৬৩, মুসলিম ১২৫৬, আবূ দাঊদ, তিরমিযী, ইবনে মাজাহ, দারেমী, সুনান)\n\n"
                        + "[14] (বুখারী ৩৮, ১৯০৮, মুসলিম ৭৬০, আবূ দাঊদ, নাসাঈ, ইবনে মাজাহ)\n\n"
                        + "[15] (আহমাদ, মুসনাদ ২/৩৫৯, ৪০০, ৪১৪, মুসলিম ২৩৩নং)\n\n"
                        + "[16] (বাযযার, ইবনে খুযাইমাহ, সহীহ, ইবনে হিববান, সহীহ, সহীহ তারগীব, আলবানী ৯৮৯নং)",
                "The word 'Ramadan' derives from the Arabic root 'Ramad', which signifies intense scorching heat or burning. When the ancient names of lunar months were replaced in the Arabic language, Ramadan fell during extreme desert heat, and it was thus named Ramadan. Following the divine obligation of fasting, this name attained profound spiritual fulfillment, as the faster's stomach burns with thirst and hunger for the sake of Allah.\n\n"
                        + "The month of Ramadan possesses several distinct characteristics found in no other month:\n\n"
                        + "1. Its name is explicitly mentioned in the Holy Quran (Surah Al-Baqarah 2:185), whereas no other month's name is specified.\n\n"
                        + "2. The Messenger of Allah (peace and blessings be upon him) used to give glad tidings to his noble companions upon its arrival.\n\n"
                        + "3. Ramadan is a blessed and sacred month, during which divine barakah descends abundantly.\n\n"
                        + "4. Allah the Almighty made fasting its entire month an absolute obligation.\n\n"
                        + "5. The gates of Paradise are wide opened, and not a single gate remains closed.\n\n"
                        + "6. All gates of divine mercy are thrown open.[1]\n\n"
                        + "7. The gates of Hellfire are firmly bolted shut, and not a single gate remains open.\n\n"
                        + "Note: The wisdom behind opening the gates of Paradise is to inspire and motivate the believer to eagerly hasten towards righteous deeds and prepare for entry into Paradise. The closing of Hell's gates aims to deter the believer from sinning during this sacred time. It does not imply that anyone dying in Ramadan enters Paradise without reckoning.[2]\n\n"
                        + "8. The rebellious devils and evil jinn are shackled and chained.[3] Consequently, they cannot instigate the magnitude of corruption and sin that they normally perpetrate in other months. Hence, evil whispers and conspiracies decrease notably. Indeed, Satan dreads Ramadan just as he dreads the Adhan and Iqamah, fleeing frantically upon hearing them.\n\n"
                        + "However, sins that occur during Ramadan do not contradict this reality. Sins are not caused solely by Satan; the human ego inclined towards evil (Nafs al-Ammarah) also commits sins independently, especially when accustomed to sinful habits. Thus, the soul acts as a proxy for Satan even when devils are chained. We seek refuge in Allah from that.[4]\n\n"
                        + "9. The gates of heaven are opened wide.[5]\n\n"
                        + "10. Supplications are answered without fail. The Prophet (peace and blessings be upon him) said: ''In every night and day of Ramadan, the supplication of every Muslim is granted.''[6]\n\n"
                        + "11. It contains a night greater than a thousand months (Laylatul Qadr). Whoever is deprived of its goodness is truly deprived of all good, and none is denied its blessings except the utterly forsaken.[7]\n\n"
                        + "12. In every night of Ramadan, an angelic herald calls out: 'O seeker of good, step forward! O seeker of evil, desist!'\n\n"
                        + "13. In every night of Ramadan, Allah emancipates numerous Muslims from the Hellfire.[8]\n\n"
                        + "14. Ramadan is the month of patience (Sabr). No act of worship tests endurance like fasting. For 29 or 30 days, the Muslim restrains himself from food, drink, marital relations, and foul deeds. Hence, the Prophet (peace and blessings be upon him) designated it the 'Month of Patience.'[9] And he said: ''Fasting the month of patience and three days of every month removes malice and enmity from the heart.''[10]\n\n"
                        + "15. Ramadan is the month of the Quran—a month of recitation, study, and reflection. It is the month in which the Quran was revealed from the Preserved Tablet (Lawh al-Mahfudh) to the heaven of the earth, or its initial revelation began. Allah says:\n\n"
                        + "﴿شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدىً لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ﴾\n\n"
                        + "'The month of Ramadan in which was revealed the Quran, a guidance for mankind and clear proofs for guidance and the criterion. So whoever sights the month, let him fast it.' (Surah Al-Baqarah 2:185)\n\n"
                        + "Not only the Quran, but other divine scriptures were also revealed in this blessed month: ''The scriptures of Ibrahim were revealed on the first night of Ramadan, the Torah on the 6th of Ramadan, the Gospel on the 13th of Ramadan, the Psalms (Zabur) on the 18th of Ramadan, and the Quran on the 24th night of Ramadan.''[11]\n\n"
                        + "16. Through monumental historical victories in Ramadan, Allah granted triumph to Islam and glory to the believers. In this month, the decisive Battle of Badr took place, manifesting the spiritual fortitude and conviction of the companions. On this day, Allah granted them victory over the polytheists. In Ramadan of 8 AH, the Conquest of Makkah occurred, bringing lasting stability and complete establishment to Islam.[12]\n\n"
                        + "17. Righteous deeds are rewarded manifold. Performing Umrah during Ramadan is equivalent in reward to performing Hajj in company of the Prophet (peace and blessings be upon him).[13]\n\n"
                        + "Regarding the sublime rewards of fasting Ramadan, the Prophet (peace and blessings be upon him) stated:\n\n"
                        + "(A) ''Whoever fasts Ramadan out of sincere faith and hoping for reward, all his past sins will be forgiven.''[14]\n\n"
                        + "(B) ''The five daily prayers, from one Friday prayer to the next, and from one Ramadan to the next, are expiations for the sins committed between them, provided major sins are avoided.''[15]\n\n"
                        + "(C) A man approached the Prophet (peace and blessings be upon him) and asked: 'O Messenger of Allah! What do you think if I testify that none has the right to be worshipped but Allah and that you are the Messenger of Allah, observe the five daily prayers, pay Zakah, and fast Ramadan—among whom will I be?' The Prophet replied: ''You will be among the Siddiqeen (the truthful) and the Shuhada (the martyrs).''[16]\n\n"
                        + "[1] (Silsilah Sahihah #1307; Sahih al-Jami' #471 by Al-Albani)\n\n"
                        + "[2] (Fatawa as-Siyam, p. 22)\n\n"
                        + "[3] (Sahih Bukhari #180; Sahih Muslim #1079)\n\n"
                        + "[4] (Duroos Ramadan wa-Waqafat lis-Sa'imeen, p. 21)\n\n"
                        + "[5] (Sahih Bukhari #1899; Musnad Ahmad; Sunan an-Nasa'i; Silsilah Sahihah #1868)\n\n"
                        + "[6] (Al-Bazzar; Sahih at-Targhib #988 by Al-Albani)\n\n"
                        + "[7] (Sahih Ibn Majah #1333; Sahih al-Jami' #3519 by Al-Albani)\n\n"
                        + "[8] (Musnad Ahmad 4/312, 5/411; Sunan an-Nasa'i; Sahih Ibn Majah #1331)\n\n"
                        + "[9] (Sahih al-Jami' #3803 by Al-Albani)\n\n"
                        + "[10] (Al-Bazzar; At-Tabarani; Sahih Ibn Hibban; Sahih al-Jami' #3804)\n\n"
                        + "[11] (Musnad Ahmad; At-Tabarani; Sahih al-Jami' #1497; Silsilah Sahihah #1575)\n\n"
                        + "[12] (Sawmu Ramadan, pp. 8-13)\n\n"
                        + "[13] (Sahih Bukhari #1863; Sahih Muslim #1256; Abu Dawud; At-Tirmidhi; Ibn Majah; Sunan ad-Darimi)\n\n"
                        + "[14] (Sahih Bukhari #38, #1908; Sahih Muslim #760; Abu Dawud; An-Nasa'i; Ibn Majah)\n\n"
                        + "[15] (Musnad Ahmad 2/359, 400, 414; Sahih Muslim #233)\n\n"
                        + "[16] (Al-Bazzar; Sahih Ibn Khuzaymah; Sahih Ibn Hibban; Sahih at-Targhib #989 by Al-Albani)"
        ));

        // Card 4: বিনা ওযরে রোযা ত্যাগ করার সাজা
        list.add(new HajjHistoryCardItem(
                4,
                "বিনা ওযরে রোযা ত্যাগ করার সাজা",
                "Punishment for Abandoning Fasting",
                "যে ব্যক্তি বিনা ওজরে রমাযানের রোযা ত্যাগ করে সে ব্যক্তির দুটি কারণ হতে পারে; হয় সে তা ফরয বলে অস্বীকার করছে, আর না হয় সে আলসেমি করে তা রাখছে না...",
                "Whoever deliberately abandons the fast of Ramadan without a valid excuse faces dire divine consequences, either through apostasy or severe chastisement...",
                "যে ব্যক্তি বিনা ওজরে রমাযানের রোযা ত্যাগ করে সে ব্যক্তির দুটি কারণ হতে পারে; হয় সে তা ফরয বলে অস্বীকার করছে এবং তাকে একটি ইবাদত বলে স্বীকৃতি দিচ্ছে না, (নাঊযু বিল্লাহি মিন যালিক।) আর না হয় সে আলসেমি করে তা রাখছে না।\n\n"
                        + "সুতরাং যদি সে রোযা ফরয বলে অস্বীকার করে ও বলে যে, রোযা শরীয়তে ফরয নয়, তাহলে সে কাফের ও মুরতাদ্দ্; যেমন এ কথা পূর্বে বলা হয়েছে। কারণ, সে দ্বীনের সর্ববাদিসম্মত এমন একটি ব্যাপারকে অস্বীকার করে যা আম-খাস সকলের পক্ষে জানা সহজ এবং যা ইসলামের একটি রুকন।\n\n"
                        + "আর তার এই মুরতাদ্দ্ হওয়ার ফলে একজন মুরতাদ্দের মাল ও পরিবারের ব্যাপারে যা বিধান আছে তা কার্যকর হবে। সরকারের কাছে সে হত্যাযোগ্য অপরাধী বলে গণ্য হবে। তার গোসল-কাফন ও জানাযা হবে না এবং মুসলিমদের গোরস্থানে তাকে দাফন করাও যাবে না। অবশ্য যদি কেউ নওমুসলিম হওয়ার ফলে অথবা ইসলামী পরিবেশ ও উলামা থেকে দূরে থাকার ফলে এ ধরনের কথা বলে থাকে, তাহলে তার কথা ভিন্ন।\n\n"
                        + "পক্ষান্তরে যদি কেউ আলসেমি করে রোযা না রাখে, তাহলে ভয়ানক কঠিন শাস্তি তার জন্য অপেক্ষা করছে।\n\n"
                        + "আবূ উমামাহ বাহেলী (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আমি শুনেছি আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন যে, ‘‘একদা আমি ঘুমিয়ে ছিলাম; এমন সময় (স্বপ্নে) আমার নিকট দুই ব্যক্তি উপস্থিত হলেন। তাঁরা আমার উভয় বাহুর ঊর্ধ্বাংশে ধরে আমাকে এক দুর্গম পাহাড়ের নিকট উপস্থিত করলেন এবং বললেন, ‘আপনি এই পাহাড়ে চড়ুন।’ আমি বললাম, ‘এ পাহাড়ে চড়তে আমি অক্ষম।’ তাঁরা বললেন, ‘আমরা আপনার জন্য চড়া সহজ করে দেব।’ সুতরাং আমি চড়ে গেলাম। অবশেষে যখন পাহাড়ের চূড়ায় গিয়ে পৌঁছলাম তখন বেশ কিছু চিৎকার-ধ্বনি শুনতে পেলাম। আমি জিজ্ঞাসা করলাম ‘এ চিৎকার-ধ্বনি কাদের?’ তাঁরা বললেন, ‘এ হল জাহান্নামবাসীদের চীৎকার-ধ্বনি।’ পুনরায় তাঁরা আমাকে নিয়ে চলতে লাগলেন। হঠাৎ দেখলাম একদল লোক তাদের পায়ের গোড়ালির উপর মোটা শিরায় (বাঁধা অবস্থায়) লটকানো আছে, তাদের কশগুলো কেটে ও ছিঁড়ে আছে এবং কশ বেয়ে রক্তও ঝরছে। নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, আমি বললাম, ‘ওরা কারা?’ তাঁরা বললেন, ‘ওরা হল তারা; যারা সময় হওয়ার পূর্বে-পূর্বেই ইফতার করে নিত---।’’[1]\n\n"
                        + "‘ওরা হল তারা; যারা সময় হওয়ার পূর্বে-পূর্বেই ইফতার করে নিত---।’ রোযা রাখার পরেও তাদের যদি ঐ অবস্থা হয়, তাহলে যারা পূর্ণ দিন মূলেই রোযা রাখে না, তাদের অবস্থা এবং যারা পূর্ণ মাসই রোযা রাখে না, তাদের অবস্থা যে কত করুণ, কত সঙ্গিন তা অনুমেয়!\n\n"
                        + "মুস্তাফা মুহাম্মাদ আম্মারাহ তাঁর তারগীবের টীকায়[2] বলেন, ‘উক্ত হাদীসের ভাবার্থ এই যে, মহান আল্লাহ তাঁর নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-কে রোযা ভঙ্গকারীদের আযাব সম্বন্ধে ওয়াকেফহাল করেছেন। তিনি দেখেছেন, তাদের সেই দুরবস্থা; তাদের আকার-আকৃতি ছিল বড় মর্মান্তিক ও নিকৃষ্ট। কঠিন যন্ত্রণায় তারা কুকুর ও নেকড়ের মত চিৎকার করছে। তারা সাহায্য প্রার্থনা করছে অথচ কোন সাহায্যকারী নেই। তাদের পায়ের শেষ প্রান্তে (গোড়ালির উপর মোটা শিরায়) জাহান্নামের আঁকুশি দিয়ে কসাইখানার যবাই করা ছাগলের মত তাদেরকে নিম্নমুখে ঝুলিয়ে রাখা হয়েছে। আর তাদের কশ বেয়ে মুখভর্তি রক্ত ঝরছে! আশা করি নাফরমান বেরোযাদার মুসলিম সম্প্রদায় এই আযাবের কথা জেনে আল্লাহর নিকট তওবা করবে এবং তাঁর সেই আযাবকে ভয় করে যথানিয়মে রোযা পালন করবে।’\n\n"
                        + "ইমাম যাহাবী (রঃ) বলেন, ‘মুমিনদের নিকটে এ কথা স্থির-সিদ্ধান্ত যে, যে ব্যক্তি কোন রোগ ও ওজর না থাকা সত্ত্বেও রমাযানের রোযা ত্যাগ করে, সে ব্যক্তি একজন ব্যভিচারী ও মদ্যপায়ী থেকেও নিকৃষ্ট। বরং মুসলিমরা তার ইসলামে সন্দেহ পোষণ করে এবং ধারণা করে যে, সে একজন নাস্তিক ও নৈতিক শৈথিল্যপূর্ণ মানুষ।[3]\n\n"
                        + "[1] (ইবনে খুযাইমাহ, সহীহ, ইবনে হিববান, সহীহ, বাইহাকী ৪/২১৬, হাকেম, মুস্তাদ্রাক ১/৪৩০, সহীহ তারগীব, আলবানী ৯৯১নং)\n\n"
                        + "[2] (২/১০৯)\n\n"
                        + "[3] (মাজমূ’ ফাতাওয়া, ইবনে তাইমিয়্যাহ ২৫/২২৫, আল-কাবায়ের, যাহাবী ৪৯পৃঃ, ফিকহুস সুন্নাহ ১/৩৮৪, ফাইযুর রাহীমির রাহমান, ফী আহকামি অমাওয়াইযি রামাযান, আব্দুল্লাহ ত্বাইয়ার ২০-২১পৃঃ, তাওজীহাতুন অফাওয়াএদ লিসসা-য়েমীনা অসসায়েমাত ৭৪পৃঃ)",
                "Whoever deliberately abandons the fast of Ramadan without a legitimate religious excuse falls into one of two categories: either he denies that fasting is an obligation and refuses to acknowledge it as an act of worship (we seek refuge in Allah from that), or he abandons it out of sheer laziness and heedlessness.\n\n"
                        + "If he denies its obligation, asserting that fasting is not ordained in Islamic law, he is a disbeliever and apostate (Murtadd), as established previously. This is because he denies a universally acknowledged tenet of the religion known by necessity and repudiates a fundamental pillar of Islam.\n\n"
                        + "As an apostate, the legal rulings regarding an apostate's estate and marriage apply to him. In an Islamic state, he is subject to capital punishment for high treason against faith. He is not washed, shrouded, prayed over in Janazah, nor buried in a Muslim cemetery. However, if the person is a brand new convert to Islam or lived in isolated areas far from Islamic scholars, he is excused until properly instructed.\n\n"
                        + "Conversely, if someone abandons fasting purely out of laziness and sinful negligence while believing in its obligation, a terrifying and agonizing punishment awaits him in the Hereafter.\n\n"
                        + "Abu Umamah al-Bahili (may Allah be pleased with him) narrated: I heard the Messenger of Allah (peace and blessings be upon him) say: ''While I was sleeping, two men came to me (in a vision) and took hold of my upper arms, bringing me to a rugged mountain. They said: 'Climb.' I said: 'I am unable to climb it.' They said: 'We will make it easy for you.' So I climbed until I reached the mountain peak, where I heard intense shrieks and cries. I asked: 'Whose cries are these?' They replied: 'These are the shrieks of the dwellers of Hell.' Then they took me further, and suddenly I saw people suspended upside down by their Achilles tendons (the thick tendon above their heels), with their mouth corners torn and bleeding profusely. The Prophet said: I asked: 'Who are these people?' They replied: 'These are those who used to break their fasts prematurely before the proper time had arrived...'''[1]\n\n"
                        + "SubhanAllah! If this gruesome torment is the penalty for people who actually observed fasting yet broke it slightly before sunset, imagine how horrific and unimaginable will be the agony of those who never fast a day at all, or abandon the entire month in shameless arrogance!\n\n"
                        + "Mustafa Muhammad Ammarah wrote in his commentary on At-Targhib wa At-Tarhib[2]: 'The core implication of this hadith is that Allah revealed to His Prophet (peace and blessings be upon him) the terrifying reality of the punishment reserved for those who violate fasting. He saw their grotesque, miserable state—howling in agonizing torment like dogs and wolves, pleading desperately for relief with none to help. They were suspended upside down by hooks pierced through the tendons above their heels, like slaughtered goats in an abattoir, with blood streaming from their torn lips! May the rebellious Muslims who neglect fasting take heed of this horrific torment, sincerely repent to Allah, and diligently observe their fasts in reverence of His punishment.'\n\n"
                        + "Imam Adh-Dhahabi (may Allah have mercy on him) stated: 'It is firmly established among the believers that whoever deliberately abandons the fast of Ramadan without illness or excuse is worse than a fornicator and a drunkard. Rather, Muslims doubt his very faith and suspect him of atheism and profound moral dissolution.'[3]\n\n"
                        + "[1] (Sahih Ibn Khuzaymah; Sahih Ibn Hibban; Al-Bayhaqi 4/216; Al-Hakim in Al-Mustadrak 1/430; Sahih at-Targhib #991 by Al-Albani)\n\n"
                        + "[2] (At-Targhib wa At-Tarhib 2/109)\n\n"
                        + "[3] (Majmu' al-Fatawa of Ibn Taymiyyah 25/225; Al-Kaba'ir by Imam Adh-Dhahabi, p. 49; Fiqh us-Sunnah 1/384; Faydul Rahimi ar-Rahman, pp. 20-21; Tawjihat wa Fawa'id lis-Sa'imeen, p. 74)"
        ));

        return list;
    }
}
