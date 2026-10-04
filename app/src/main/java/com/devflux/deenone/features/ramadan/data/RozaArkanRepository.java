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
 * Repository for রোযার আরকান (Pillars of Fasting).
 * Instant 0ms cached local data with verbatim text matching the user's provided content,
 * and asynchronous background sync with the PHP backend REST API (Rule 11).
 */
public final class RozaArkanRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaArkanRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_arkan.php");
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
                                mainHandler.post(() -> callback.onDataLoaded(new ArrayList<>(cachedList)));
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Offline resiliency: keep using in-memory pre-seeded cache seamlessly
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: রোযার আরকান
        list.add(new HajjHistoryCardItem(
                1,
                "রোযার আরকান",
                "Pillars of Fasting (Arkan as-Sawm)",
                "রোযার হল দুটি রুকন; যদ্দবারা তার প্রকৃতত্ব সংগঠিতঃ- ১। ফজর উদয় হওয়ার পর থেকে নিয়ে সূর্য অস্ত যাওয়া পর্যন্ত সময় ধরে যাবতীয় রোযা নষ্টকারী জিনিস থেকে বিরত থাকা এবং ২। নিয়ত...",
                "Fasting consists of two essential pillars through which its true reality is fulfilled: 1. Abstaining from all invalidators of fasting from dawn until sunset, and 2. Sincere intention (Niyyah)...",
                "রোযার হল দুটি রুকন; যদ্দবারা তার প্রকৃতত্ব সংগঠিতঃ-<br><br>"
                        + "<b>১। ফজর উদয় হওয়ার পর থেকে নিয়ে সূর্য অস্ত যাওয়া পর্যন্ত সময় ধরে যাবতীয় রোযা নষ্টকারী জিনিস থেকে বিরত থাকা।</b> মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(وَكُلُوْا وَاشْرَبُوْا حَتَّى يَتَبَيَّنَ لَكُمُ الْخَيْطُ الأَبْيَضُ مِنَ الْخَيْطِ الأَسْوَدِ مِنَ الْفَجْرِ، ثُمَّ أَتِمُّوا الصِّيَامَ إِلَى اللَّيْلِ)</b></font><br><br>"
                        + "অর্থাৎ, আর তোমরা পানাহার কর, যতক্ষণ পর্যন্ত না কালো সুতা থেকে ফজরের সাদা সুতা তোমাদের নিকট স্পষ্ট হয়েছে। অতঃপর তোমরা রাত পর্যন্ত রোযা পূর্ণ কর। (কুরআনুল কারীম ২/১৮৭)<br><br>"
                        + "উক্ত আয়াতে উল্লেখিত কালো সুতা ও সাদা সুতা বলে রাতের অন্ধকার ও দিনের শুভ্রতাকে বুঝানো হয়েছে। বুখারী ও মুসলিমে আদী বিন হাতেম কর্তৃক বর্ণিত, উক্ত আয়াত অবতীর্ণ হলে তিনি (মাথায় রুমালের উপর ব্যবহার্য) একটি সাদা ও একটি কালো মোটা রশি (বালিশের নিচে) রাখলেন। রাত্রি হলে তিনি লক্ষ্য করলেন, কিন্তু (কোন্টা সাদা ও কোন্টা কালো) তা স্পষ্ট হল না। সকাল হলে তিনি এ কথা আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর কাছে উল্লেখ করলেন। তিনি (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁকে বললেন, ‘‘তোমার বালিশ তাহলে খুবই বিশাল! কালো সুতা ও সাদা সুতা তোমার বালিশের নিচে ছিল?!’’[1] অন্য এক বর্ণনায় আছে, ‘‘তার মানে হল, রাতের অন্ধকার ও দিনের শুভ্রতা।’’[2]<br><br>"
                        + "<b>২। নিয়ত ;</b> আর তা হল, মহান আল্লাহর আদেশ পালন করার উদ্দেশ্যে রোযা রাখার জন্য হৃদয়ের সংকল্প। আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(وَمَا أُمِرُوْا إِلاَّ لِيَعْبُدُوا اللهَ مُخْلِصِيْنَ لَهُ الدِّيْنَ)</b></font><br><br>"
                        + "অর্থাৎ, তারা তো আল্লাহর আনুগত্যে বিশুদ্ধচিত্ত হয়ে একনিষ্ঠভাবে তাঁর ইবাদত করতে আদিষ্ট হয়েছিল। (কুরআনুল কারীম ৯৮/৫)<br><br>"
                        + "আর মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘সমস্ত কর্ম নিয়তের উপর নির্ভরশীল এবং মানুষের তাই প্রাপ্য হয় যার সে নিয়ত করে।’’[3]<br><br>"
                        + "সুতরাং যে ব্যক্তি ফরয (যেমন রমাযান, কাযা, নযর অথবা কাফ্ফারার) রোযা রাখবে, সে ব্যক্তির জন্য নিয়ত ও সংকল্প করা ওয়াজেব। আর নিয়ত হল, হৃদয়ের কাজ; তার সাথে মুখের কোন সম্পর্ক নেই। তার প্রকৃতত্ব হল, মহান আল্লাহর আদেশ পালন এবং তাঁর সন্তুষ্টি লাভ করার উদ্দেশ্যে কোন কাজের সংকল্প করা। বলা বাহুল্য, ‘নাওয়াইতু আন আসূমা গাদাম মিন শাহরি রামাযান’ বলে নিয়ত পড়া বিদআত।<br><br>"
                        + "আসলে যে ব্যক্তি মনে মনে এ কথা জানবে যে, আগামী কাল রোযা, অতঃপর রোযা রাখার উদ্দেশ্যে সে সেহরী খাবে, তার এমনিই নিয়ত হয়ে যাবে। তদনুরূপ যে ব্যক্তি আল্লাহর জন্য বিশুদ্ধচিত্তে দিনের বেলায় (ফজর উদয়কাল থেকে সূর্য অস্তকাল পর্যন্ত) সকল প্রকার রোযা নষ্টকারী জিনিস থেকে বিরত থাকার সংকল্প করবে, তার নিয়ত হয়ে যাবে, যদিও সে সেহরী খেতে সুযোগ না পেয়েছে।[4]<br><br>"
                        + "অবশ্য নিয়ত ফজরের পূর্বে হওয়া জরুরী। তবে রাত্রের যে কোন অংশে করলে যথেষ্ট ও বৈধ। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি রাত থেকে রোযা রাখার সংকল্প না করে, তার রোযা নেই।’’[5] তিনি আরো বলেন, ‘‘যে ব্যক্তি ফজর উদয় হওয়ার পূর্বে রোযা রাখার সংকল্প না করে, তার রোযা নেই।’’[6]<br><br>"
                        + "এই জন্যই যে ব্যক্তি ফজর উদয় হওয়ার পর ছাড়া রমাযান মাস আগত হওয়ার কথা তার আগে না জানতে পারে, তার জন্য জরুরী হল, বাকী দিন রোযা নষ্টকারী জিনিস থেকে বিরত থাকা এবং রমাযান পরে সেই দিনের রোযা কাযা করা।[7] আর এ হল অধিকাংশ উলামাদের মত - যেমন পূর্বে আলোচিত হয়েছে।<br><br>"
                        + "পক্ষান্তরে সাধারণ নফল রোযার ক্ষেত্রে রাত থেকে নিয়ত করা শর্ত নয়। বরং ফজর উদয় হওয়ার পর কিছু না খেয়ে থাকলে দিনের বেলায় নিয়ত করলেও তা যথেষ্ট হবে। কেননা, মা আয়েশা (রাঃ) বলেন, এক দিন আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) আমার নিকট এলেন এবং বললেন, ‘‘তোমাদের কিছু আছে কি?’’ আমরা বললাম, ‘না।’ তিনি বললেন, ‘‘তাহলে আমি রোযা রাখলাম।’’[8]<br><br>"
                        + "পরন্তু নির্দিষ্ট নফল (যেমন আরাফা ও আশূরার) রোযার ক্ষেত্রে পূর্বসতর্কতামূলক আমল হল, রাত থেকেই তার নিয়ত করে নেওয়া।[9]<br><br>"
                        + "রমাযানের রোযাদারের জন্য রমাযানের প্রত্যেক রাতে নিয়ত নবায়ন করার প্রয়োজন নেই। বরং রমাযান আসার শুরুতে সারা মাস রোযা রাখার একবার নিয়ত করে নিলেই যথেষ্ট। সুতরাং যদি ধরে নেওয়া হয় যে, এক ব্যক্তি রমাযানের কোন দিনে সূর্য ডোবার আগে ঘুমিয়ে গেল। অতঃপর পরের দিন ফজর উদয় হওয়ার পর তার চেতন হল। অর্থাৎ, সে রাতে এই দিনের রোযা রাখার নিয়ত করার সুযোগ পেল না। কিন্তু তবুও তার রোযা শুদ্ধ হবে। কারণ, মাসের শুরুতে সারা মাস রোযা রাখার নিয়ত তার ছিল।<br><br>"
                        + "হ্যাঁ, তবে যদি কেউ সফর, রোগ অথবা অন্য কোন ওযরের ফলে মাঝে রোযা না রেখে নিয়ত ছিন্ন করে ফেলেছে তার জন্য অবশ্য ওযর দূর হওয়ার পর নতুন করে রোযা রাখার জন্য নিয়ত নবায়ন করা জরুরী।[10]<br><br>"
                        + "যে ব্যক্তি খাওয়া অথবা পান করার সংকল্প করার পর পুনরায় স্থির করল যে, সে ধৈর্য ধরবে। অতএব সে পানাহার করল না। এমন ব্যক্তির রোযা কেবলমাত্র পানাহার করার ইচ্ছা ও সংকল্প হওয়ার জন্য নষ্ট হবে না। আর এ কাজ হল সেই ব্যক্তির মত, যে নামাযে কথা বলতে ইচ্ছা করার পর কথা বলে না, অথবা (হাওয়া ছেড়ে) ওযূ নষ্ট করার ইচ্ছা হওয়ার পর ওযূ নষ্ট করে না। যেমন এই নামাযীর ঐ ইচ্ছার ফলে নামায বাতিল হবে না এবং তার ওযূও শুদ্ধ থাকবে, অনুরূপ ঐ রোযাদারের পানাহার করার ইচ্ছা হওয়ার পর পানাহার না করে তার রোযাও বাতিল না হয়ে শুদ্ধ থাকবে। যেহেতু নীতি হল, যে ব্যক্তি ইবাদতে কোন নিষিদ্ধ (ইবাদত নষ্টকারী) কর্ম করার সংকল্প করে, কিন্তু কার্যতঃ তা করে না, সে ব্যক্তির ইবাদত নষ্ট হয় না।[11]<br><br>"
                        + "পক্ষান্তরে যে (রোযা নেই বা রাখলাম না মনে করে) নিয়ত ছিন্ন করে দেয়, তার রোযা বাতিল। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘সমস্ত কর্ম নিয়তের উপর নির্ভরশীল এবং মানুষের তাই প্রাপ্য হয় যার সে নিয়ত করে।’’[12]<br><br>"
                        + "যদি কোন রোযাদার মুরতাদ্দ্ হয়ে যায় তাহলে সাথে সাথে তওবা করলেও তার রোযা নষ্ট হয়ে যায়। কারণ, মুরতাদ্দের কাজ ইবাদতের নিয়ত বাতিল ও বিচ্ছিন্ন করে ফেলে। আর এতে কারো কোন প্রকারের দ্বিমত নেই।[13]<br><br>"
                        + "নিয়ত প্রসঙ্গে আলোচনায় একটি সতর্কতা জরুরী এই যে, প্রত্যেক মুসলিমের জন্য ওয়াজেব হল, আল্লাহর প্রতি ঈমান, তাঁর সওয়াবের আশা এবং কেবল তাঁরই সন্তুষ্টি বিধানের উদ্দেশ্যে রোযা রাখা। কাউকে দেখাবার বা শোনাবার উদ্দেশ্যে, অথবা কেবলমাত্র লোকেদের দেখাদেখি অন্ধ অনুকরণ করে, অথবা দেশ বা পরিবারের পরিবেশের অনুকরণ করে রোযা রাখা উচিৎ নয়। (যেমন রোযা শরীর ও সবাস্থ্যের পক্ষে উপকারী বলে সেই উপকার লাভের উদ্দেশ্যেই রোযা রাখা।) বরং ওয়াজেব হল, তাকে যেন তার এই ঈমান রোযা রাখতে উদ্বুদ্ধ করে যে, মহান আল্লাহ তার উপর এই রোযা ফরয করেছেন এবং সে তা পালন করে তাঁর কাছে প্রতিদানের আশা করে। আর এ জন্যই মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি ঈমান এবং সওয়াবের আশা রেখে রমাযানের রোযা রাখে, তার পূর্বেকার সকল গোনাহ মাফ হয়ে যায়।’’[14]<br><br>"
                        + "সুতরাং রোযার উদ্দেশ্য ক্ষুৎ-পিপাসা ও কষ্ট সহ্য করার উপর শরীর-চর্চা বা সবাস্থ্য-অনুশীলন নয়, বরং তা হল প্রিয়তমের সন্তুষ্টি লাভের উদ্দেশ্যে প্রিয়তম বস্ত্ত ত্যাগ করার উপর আত্মার অনুশীলন।<br><br>"
                        + "রোযায় পরিত্যাজ্য প্রিয়তম বস্ত্ত হল, পানাহার ও স্ত্রী-সঙ্গম। আর তা হল আত্মার কামনা। প্রিয়তমের সন্তুষ্টি হল, মহান আল্লাহর সন্তুষ্টি। সুতরাং নিয়তে আমরা যেন এ কথা স্মরণে রাখি যে, আমরা মহান আল্লাহর সন্তুষ্টি লাভের আশায় উক্ত রোযা নষ্টকারী (কামনার) বস্ত্ত ত্যাগ করব।[15]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (বুখারী ৪৫০৯নং)<br>"
                        + "[2] (বুখারী ১৯১৬, মুসলিম ১০৯০নং)<br>"
                        + "[3] (বুখারী ১, মুসলিম ১৩নং)<br>"
                        + "[4] (দ্রঃ ফিকহুস সুন্নাহ আরাবী ১/৩৮৭, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৩৩নং, তাইঃ ১৩পৃঃ)<br>"
                        + "[5] (নাসাঈ, সহীহুল জামেইস সাগীর, আলবানী ৬৫৩৫নং)<br>"
                        + "[6] (দারাকুত্বনী, সুনান, বাইহাকী, আয়েশা কর্তৃক এবং আহমাদ, মুসনাদ, আবূ দাঊদ, তিরমিযী, নাসাঈ হাফসা কর্তৃক, ইরওয়াউল গালীল, আলবানী ৯১৪নং)<br>"
                        + "[7] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৩৬নং)<br>"
                        + "[8] (মুসলিম ১১৫৪নং)<br>"
                        + "[9] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৩৪নং)<br>"
                        + "[10] (আশ্শারহুল মুমতে’ ৬/৩৬৯, সামানিয়া ওয়া আরবাঊন সুআলান ফিস্-সিয়াম ৪৬পৃঃ, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৩৩নং)<br>"
                        + "[11] (ইবনে উষাইমীন, আহকামুন মিনাস সিয়াম, ক্যাসেট)<br>"
                        + "[12] (বুখারী ১, মুসলিম ১৩নং) (দ্রঃ আশ্শারহুল মুমতে’ ৬/৩৭৬, ইবনে জিবরীন ফাসিঃ মুসনিদ ৯৯পৃঃ, সাওমু রামাযান ২৪পৃঃ)<br>"
                        + "[13] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৩৩নং)<br>"
                        + "[14] (বুখারী ৩৮, মুসলিম ৭৬০নং) (রিসালাতানি মু’জাযাতানি ফিয যাকাতি অস্সিয়াম, ইবনে বায ২২পৃঃ)<br>"
                        + "[15] (সামানিয়া ওয়া আরবাঊন সুআলান ফিস্-সিয়াম ১১পৃঃ)</font>",
                "Fasting comprises two fundamental pillars (Arkan) without which it is invalid:<br><br>"
                        + "<b>1. Abstaining from all invalidators of fasting from the break of true dawn until sunset.</b> Allah says: <i>'And eat and drink until the white thread of dawn becomes distinct to you from the black thread of night. Then complete the fast until nightfall.'</i> (Surah Al-Baqarah 2:187).<br><br>"
                        + "<b>2. Sincere Intention (Niyyah):</b> The firm resolve of the heart to fast purely in obedience to Allah and seeking His pleasure. The Prophet ﷺ said: <i>'Actions are judged by intentions, and every person will get what they intended.'</i> (Bukhari 1, Muslim 13).<br><br>"
                        + "For obligatory fasts (Ramadan, Qadha, vows, or expiations), the intention must be established before dawn (Fajr). For voluntary (Nafl) fasts, intending during daytime is permissible provided nothing invalidating was consumed.<br><br>"
                        + "<b>References:</b><br>"
                        + "[1] Bukhari 4509, [2] Bukhari 1916, Muslim 1090, [3] Bukhari 1, Muslim 13, [5] Nasa'i 6535, [6] Irwa al-Ghaleel 914, [14] Bukhari 38, Muslim 760."
        ));

        return list;
    }
}
