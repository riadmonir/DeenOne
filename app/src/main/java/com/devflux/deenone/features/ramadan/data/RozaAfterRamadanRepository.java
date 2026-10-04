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
 * Repository for রমাযান পরে কি? (What After Ramadan? & Rulings of Qadha Fasts).
 * Pre-seeded with all 7 cards verbatim matching user prompt and screenshot.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaAfterRamadanRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaAfterRamadanRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_after_ramadan.php");
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
            // Fallback gracefully to pre-seeded local data
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: রমাযান পরে কি?
        list.add(new HajjHistoryCardItem(
                1,
                "রমাযান পরে কি?",
                "What After Ramadan?",
                "রমাযান বিদায় নিল। ফিরে আসবে আবার প্রায় এক বছর পর। রমাযান চলে গেল। কিন্তু রমাযান পরে মুসলিমের অবস্থা কি, কর্তব্য কি? রমাযানের আমল ভরা দিনগুলি শেষ হয়ে গেল, কিন্তু মুমিনের আমল তো কোন দিনকার জন্য শেষ হওয়ার নয়...",
                "Ramadan has departed, to return again after almost a year. But what is the state and duty of a Muslim after Ramadan? The days filled with deeds have ended, but a believer's deeds never cease...",
                "রমাযান বিদায় নিল। ফিরে আসবে আবার প্রায় এক বছর পর। রমাযান চলে গেল। কিন্তু রমাযান পরে মুসলিমের অবস্থা কি, কর্তব্য কি? রমাযানের আমল ভরা দিনগুলি শেষ হয়ে গেল, কিন্তু মুমিনের আমল তো কোন দিনকার জন্য শেষ হওয়ার নয়। যেহেতু যিনি রমাযানের প্রভু, তিনিই শা’বান ও শওয়াল তথা বাকী মাসসমূহ ও সারা বছরের প্রভু।<br><br>"
                        + "মুসলিমের কর্তব্য হল, রমাযান মাসে যে সব নেক আমলে অভ্যাসী হয়েছে সেই সব আমল বন্ধ না করে একটানা নিয়মিত করে যাওয়া। মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(وَاعْبُدْ رَبَّكَ حَتَّى يَأْتِيَكَ الْيَقِيْن)</b></font><br><br>"
                        + "অর্থাৎ, মৃত্যু আসা অবধি তুমি তোমার প্রতিপালকের ইবাদত করতে থাক। (কুরআনুল কারীম ১৫/৯৯)<br><br>"
                        + "আর মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘আল্লাহর নিকট সবচেয়ে সেই আমল অধিক পছন্দনীয়, যা লাগাতার করে যাওয়া হয়; যদিও বা তা পরিমাণে কম হয়।’’[1]<br><br>"
                        + "শেষ হয়ে গেল সবুরের মাস। আর সবুর হল পরহেযগার মানুষদের সম্বল। বলা বাহুল্য, যেমন সে এ মাসে বড় প্রচেষ্টা ও শ্রম দিয়ে আমল-ইবাদত করেছে, তেমনি পরের মাসগুলিতেও যেন সেই প্রচেষ্টা ও পরিশ্রম বাকী থাকে। তার সমস্ত আমল এমন হওয়া উচিত, যেন সে তা রমাযানেই করছে।<br><br>"
                        + "রোযার মাস বিদায় নিয়ে চলে গেল। কিন্তু রোযা বিদায় নেয় নি। যেহেতু রমাযানের রোযা ছাড়াও অন্যান্য সুন্নত ও নফল রোযা রয়েছে; তা যেন তার আমলের খাতা থেকে বাদ না পড়ে।<br><br>"
                        + "নামায ও কিয়ামের মাস ফুরিয়ে গেল। কিন্তু নফল নামায ও কিয়াম কেবল রোযার মাসেই সীমাবদ্ধ নয়। বরং প্রত্যেক রাতের একটি অংশ তাহাজ্জুদের জন্য মুসলিমের খাস হওয়া উচিত।<br><br>"
                        + "এই মাসে সকল মসজিদের ইমাম ও মুসল্লীগণ তাঁদের অন্যান্য (বেনামাযী) ভাইদেরকে হেদায়াতপ্রাপ্ত হয়ে মসজিদে দেখে শত খুশী হয়েছেন, যারা রমাযানভর পাঁচ অক্ত্ ফরয নামায যথা নিয়মে আদায় করেছে, বরং তারা নিয়মিতভাবে তারাবীহ ও তাহাজ্জুদের নামাযও পড়েছে। পরন্তু তাঁদের আনন্দ আরো বৃদ্ধি হতে থাকে; যদি ঐ ভায়েরা উক্ত অবস্থাতেই প্রতিষ্ঠিত থেকে যায়। (অর্থাৎ বার মাসই নামায পড়ে।) নচেৎ ‘‘কিয়ামতে বান্দার নিকট থেকে অন্যান্য আমলের পূর্বে সর্বপ্রথম নামাযের হিসাব নেওয়া হবে। সুতরাং নামায সঠিক হলে তার অন্যান্য আমলও সঠিক বিবেচিত হবে। (নচেৎ না।)’’[2]<br><br>"
                        + "নেক আমল বা কোনও সৎকর্ম আল্লাহর দরবারে কবুল হওয়ার অন্যতম লক্ষণ এই যে, আমলকারী ঐ কর্মের পরে পুনরায় অন্যান্য সৎকর্ম করে থাকে। সুতরাং রমাযানের পর আপনার অন্যান্য নেক আমল করতে থাকা এই কথারই লক্ষণ যে, আপনার রোযা ও তারাবীহ মহান আল্লাহ কবুল করে নিয়েছেন। আর হ্যাঁ, খবরদার পুনরায় আপনি আপনার সেই অবস্থায় ফিরে যাবেন না, যে অবস্থা ছিল রমাযানের পূর্বে। মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(وَلاَ تَكُوْنُوْا كَالَّتِيْ نَقَضَتْ غَزْلَهَا مِنْ بَعْدِ قُوَّةٍ أَنْكَاثاً)</b></font><br><br>"
                        + "অর্থাৎ, তোমরা সে নারীর মত হয়ো না, যে তার সুতা মজবুত করে পাকাবার পর ওর পাক খুলে নষ্ট করে দেয়।’’ (কুরআনুল কারীম ১৬/৯২)<br><br>"
                        + "ভাই মুসলিম! আপনি আপনার ঔদাস্য বর্জন করুন। আরামের নিদ্রা থেকে জেগে উঠুন। আপনার সফরের জন্য কিছু পথের সম্বল সংগ্রহ করে নিন। আল্লাহ রববুল আলামীনের প্রতি প্রত্যাবর্তন ও রুজু করুন। হয়তো বা আপনি তাঁর সাড়া পাবেন। তাঁর রহমত ও করুণা অর্জন করে আপনি সৌভাগ্যবান হবেন। আল্লাহর অলীদের পথের পথিক হয়ে তাঁদের সাথে গিয়ে মিলিত হতে পারবেন।<br><br>"
                        + "আল্লাহ ও তাঁর জাহান্নামের ভয়ে নয়নাশ্রু বিগলিত করে রমাযান মাসকে বিদায় জানান। যে ছিল প্রিয়তম, সে বিদায় নিল। তাতে তো আপনার চোখে পানির স্রোত নামারই কথা। কি জানি আবার আগামী বছরে সেই প্রিয়তমের সাথে আপনার সাক্ষাৎ হবে কি না? আবার আপনি ঐ ফরয রোযা পালন করার তওফীক লাভ করবেন কি না? পুনরায় ঐ উদ্দীপনার সাথে জামাআতে ঐ তারাবীহর নামায পড়তে সুযোগ পাবেন কি না?<br><br>"
                        + "কাজ শেষে মজুরকে তার মজুরী দিয়ে দেওয়া হয়। আমরা আমাদের কাজ তো শেষ করলাম। কিন্তু হায়! যদি আমরা আমাদের মধ্যে কার কাজ মহান আল্লাহর দরবারে গৃহীত হয়েছে তা জানতে পারতাম, তাহলে তাকে তার উপর মোবারকবাদ জানাতাম। আর কার কাজ গৃহীত নয়, তা জানতে পারলে তার সাথে বসে সমবেদনা ও শোক প্রকাশ করতাম।[3]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (বুখারী ৬৪৬৫, মুসলিম ৭৮৩নং প্রমুখ)<br>"
                        + "[2] (আহমাদ, মুসনাদ, সুনানে আরবাআহ; আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ, ত্বাবারানী, মু’জাম, প্রমুখ, সিলসিলাহ সহীহাহ, আলবানী ১৩৫৮নং)<br>"
                        + "[3] (তাওজীহাতুন অফাওয়াএদ লিসসা-য়েমীনা অসসায়েমাত ১০১-১০৬পৃঃ থেকে সংক্ষেপিত)</font>",
                "Ramadan has departed, to return again after almost a year. But what is the state and duty of a Muslim after Ramadan? The days filled with deeds have ended, but a believer's deeds never cease. For the Lord of Ramadan is the Lord of Sha'ban, Shawwal, and all the months of the entire year.<br><br>"
                        + "The duty of a Muslim is to consistently maintain the good habits established during Ramadan. Allah Almighty says: 'And worship your Lord until certainty (death) comes to you.' (Surah Al-Hijr: 99).<br><br>"
                        + "The Prophet (ﷺ) said: 'The most beloved deeds to Allah are those that are consistent, even if they are small.' (Sahih al-Bukhari 6465, Sahih Muslim 783).<br><br>"
                        + "The month of fasting has passed, but fasting itself has not ended. There are Sunnah and voluntary fasts throughout the year. The month of prayers has passed, but Tahajjud and night prayers remain open. Let us steadfastly continue our righteousness and not dismantle what we have built.<br><br>"
                        + "<b>References:</b> Sahih al-Bukhari 6465, Sahih Muslim 783; Musnad Ahmad, Sunan Arba'ah, Silsilah Sahihah 1358; Tawjihat wa Fawa'id lis-Sa'imeen pp. 101-106."
        ));

        // Card 2: রমাযানের রোযা কাযা করার বিবরণ
        list.add(new HajjHistoryCardItem(
                2,
                "রমাযানের রোযা কাযা করার বিবরণ",
                "Details of Making Up Missed Ramadan Fasts (Qadha)",
                "কারো রমাযান মাসের রোযা ছুটে গেলে তা সত্বর কাযা করা ওয়াজেব নয়। বরং এ ব্যাপারে প্রশস্ততা আছে; সুযোগ ও সময় মত তা কাযা করতে পারা যায়। তদনুরূপ কাফ্ফারাও সত্বর আদায় করা ওয়াজেব নয়...",
                "If someone misses days of Ramadan fasting, it is not strictly obligatory to make them up immediately; there is flexibility to make them up as opportunity permits...",
                "কারো রমাযান মাসের রোযা ছুটে গেলে তা সত্বর কাযা করা ওয়াজেব নয়। বরং এ ব্যাপারে প্রশস্ততা আছে; সুযোগ ও সময় মত তা কাযা করতে পারা যায়। তদনুরূপ কাফ্ফারাও সত্বর আদায় করা ওয়াজেব নয়। যেহেতু মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(فَمَنْ كَانَ مِنْكُمْ مَرِيْضاً أَوْ عَلَى سَفَرٍ فَعِدَّةٌ مِّنْ أَيَّامٍ أُخَر)</b></font><br><br>"
                        + "অর্থাৎ, কিন্তু তোমাদের মধ্যে কেউ অসুস্থ বা মুসাফির হলে সে অপর কোন দিন গণনা করবে। (কুরআনুল কারীম ২/১৮৪)<br><br>"
                        + "অর্থাৎ, সে অপর কোন দিনে রোযা রেখে নেবে। এখানে মহান আল্লাহ লাগাতার বা সাথে সাথে রাখার শর্ত আরোপ করেন নি। সে শর্তের কথা উল্লেখ থাকলে অবশ্যই তা সত্বর পালনীয় হত। অতএব বুঝা গেল যে, এ ব্যাপারে প্রশস্ততা আছে।[1] বলা বাহুল্য, যদি কেউ তার ছুটে যাওয়া রোযা পিছিয়ে দিয়ে শীতের ছোট ছোট দিনে রাখে, তাহলে তাও তার জন্য বৈধ এবং যথেষ্ট। তাতেও মহান আল্লাহর ঐ ঋণ পরিশোধ হয়ে যাবে।[2]<br><br>"
                        + "তবে ঈদের পরে ওজর দূর হয়ে গেলে সুযোগ হওয়ার সাথে সাথে সত্বর কাযা রেখে নেওয়াই উচিত। কারণ, তাতে সত্বর দায়িত্ব পালন হয়ে যায় এবং পূর্বসতর্কতামূলক কর্ম সম্পাদন করা হয়।[3]<br><br>"
                        + "মা আয়েশা (রাঃ) বলেন, ‘আমার রমাযানের রোযা কাযা থাকত। কিন্তু সে রোযা শা’বান ছাড়া তার আগে কাযা করতে সক্ষম হতাম না।’ এই কথার এক বর্ণনাকারী ইয়াহয়্যা বিন সাঈদ বলেন, ‘এর কারণ এই যে, নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর খিদমত তাঁকে ব্যস্ত করে রাখত। আর তাঁর কাছে তাঁর পৃথক মর্যাদাও ছিল।’[4]<br><br>"
                        + "এখানে মা আয়েশার বাহ্যিক উক্তি এই কথাই দাবী করে যে, তাঁর ব্যস্ততা না থাকলে ছুটে যাওয়া রোযা সত্বরই কাযা করতেন। এ থেকে বুঝা যায় যে, যার কোন ওজর-অসুবিধা নেই, তার জন্য দেরী না করে সত্বর কাযা রেখে নেওয়াই উচিত।[5]<br><br>"
                        + "কাযা রোযা ছুটে যাওয়া রোযার মতই। অর্থাৎ, যত দিনকার রোযা ছুটে গেছে, ঠিক তত দিনকারই কাযা করবে; তার বেশী নয়। অবশ্য উভয়ের মাঝে পার্থক্য এই যে, কাযা রোযা লাগাতার রাখা জরুরী নয়। যেহেতু মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(فَمَنْ كَانَ مِنْكُمْ مَرِيضاً أَوْ عَلَى سَفَرٍ فَعِدَّةٌ مِنْ أَيَّامٍ أُخَرَ)</b></font><br><br>"
                        + "‘‘কিন্তু তোমাদের মধ্যে কেউ অসুস্থ বা মুসাফির হলে সে অপর কোন দিন গণনা করবে।’’ (কুরআনুল কারীম ২/১৮৪)<br><br>"
                        + "অর্থাৎ, যে দিনের রোযা সে ছেড়ে দিয়েছে, সেই দিনগুলোই যেন অন্য দিনে কাযা করে নেয়; নিরবচ্ছিন্নভাবে অথবা বিচ্ছিন্নভাবে। যেহেতু মহান আল্লাহ এখানে রোযা কাযা করার কথাই বলেছেন এবং তার সাথে কোন ধরনের শর্ত আরোপ করেন নি।[6]<br><br>"
                        + "পক্ষান্তরে কাযা রোযাসমূহকে ছেড়ে ছেড়ে অথবা লাগাতার রাখার ব্যাপারে কোন মরফূ’ হাদীস বিশুদ্ধরূপে বর্ণিত হয় নি। সঠিক হল, উভয় প্রকার বৈধ। যেমন আবূ হুরাইরা (রাঃ) বলেন, ‘ইচ্ছা করলে একটানা রাখবে।’[7]<br><br>"
                        + "অবশ্য এতে কোন সন্দেহ নেই যে, তিনটি কারণে কাযা রোযাগুলিকে একটানা -অর্থাৎ মাঝে এক দিনও বাদ না দিয়ে- রেখে নেওয়াই উত্তমঃ-<br><br>"
                        + "প্রথমতঃ একটানা রোযা কাযা রাখাটা আসল রোযার সাথে অধিকতর সামঞ্জস্যপূর্ণ। কারণ, আসল রোযা একটানাই রাখতে হয়।<br><br>"
                        + "দ্বিতীয়তঃ লাগাতার রাখলে অতি সত্বর দায়িত্ব পালন হয়ে যায়। যেহেতু একদিন রোযা রেখে মাঝে ২/১ দিন বাদ দিয়ে আবার রাখলে কাযা পূর্ণ করতে বিলম্ব হয়ে যায়। অথচ লাগাতার রেখে নিলে তাড়াতাড়ি শেষ হয়ে যায়।<br><br>"
                        + "তৃতীয়তঃ একটানা রোযা রেখে নেওয়াটাই পূর্বসতর্কতামূলক কর্ম। কারণ, মানুষ জানে না যে, আগামীতে তার কি ঘটবে। আজ সুস্থ আছে, কিন্তু কাল হয়তো অসুস্থ হয়ে পড়বে। আজ জীবিত আছে, কিন্তু কাল হয়তো মরণের আহবানে সাড়া দিতে হবে।[8]<br><br>"
                        + "অনুরূপভাবে আসল রোযা থেকে কাযা রোযার একটি পার্থক্য এই যে, কাযা রোযা রাখা অবস্থায় দিনের বেলায় সঙ্গম করে ফেললে কোন কাফ্ফারা লাগে না। যেহেতু সেটা রমাযান মাসের বাইরে ঘটে তাই।[9]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আশ্শারহুল মুমতে’ ৬/৪৪৯)<br>"
                        + "[2] (ফাসিঃ মুসনিদ ৮১পৃঃ)<br>"
                        + "[3] (আশ্শারহুল মুমতে’ ৬/৪৪৬)<br>"
                        + "[4] (বুখারী ১৯৫০, মুসলিম ১১৪৬, আবূ দাঊদ ২৩৯৯, ইবনে মাজাহ ১৬৬৯, ইবনে খুযাইমাহ, সহীহ ২০৪৬-২০৪৮নং, বাইহাকী ৪/২৫২)<br>"
                        + "[5] (তামামুল মিন্নাহ, আল্লামা আলবানী ৪২২পৃঃ দ্রঃ)<br>"
                        + "[6] (ফিকহুস সুন্নাহ ১/৪১৬)<br>"
                        + "[7] (ইরওয়াউল গালীল, আলবানী ৪/৯৪-৯৭, তামামুল মিন্নাহ, আল্লামা আলবানী ৪২৪পৃঃ)<br>"
                        + "[8] (আশ্শারহুল মুমতে’ ৬/৪৪৬)<br>"
                        + "[9] (ঐ ৬/৪১৩)</font>",
                "Making up missed Ramadan fasts (Qadha) is not immediately mandatory; there is broad leeway to fast as time and capacity permit before the next Ramadan. However, it is preferable to hasten making them up once excuses cease.<br><br>"
                        + "Aisha (may Allah be pleased with her) reported that she could only make up her missed fasts in Sha'ban due to serving the Prophet (ﷺ). Consecutive fasting is not obligatory but recommended for prompt fulfillment, caution, and alignment with the original fast.<br><br>"
                        + "<b>References:</b> Surah Al-Baqarah: 184; Sahih al-Bukhari 1950, Sahih Muslim 1146; Ash-Sharh al-Mumti' 6/446-449; Fiqh as-Sunnah 1/416; Irwa' al-Ghalil 4/94-97."
        ));

        // Card 3: আগামী রমাযান পর্যন্ত কাযা রোযা রাখতে না পারলে
        list.add(new HajjHistoryCardItem(
                3,
                "আগামী রমাযান পর্যন্ত কাযা রোযা রাখতে না পারলে",
                "Failing to Make Up Missed Fasts Before Next Ramadan",
                "কোন ওযর ব্যতীত রমাযানের কাযা রোযা না রেখে পরবর্তী রমাযান পার করে দেওয়া বৈধ নয়। কার্যক্ষেত্রে কাযা পালন না করতে পারা অবস্থায় দ্বিতীয় রমাযান এসে উপস্থিত হলে বর্তমান রমাযানের রোযা পালন করতে হবে...",
                "Delaying make-up fasts past the next Ramadan without a valid excuse is not permissible. If the second Ramadan arrives, one must fast the present Ramadan first...",
                "কোন ওযর ব্যতীত রমাযানের কাযা রোযা না রেখে পরবর্তী রমাযান পার করে দেওয়া বৈধ নয়। কার্যক্ষেত্রে কাযা পালন না করতে পারা অবস্থায় দ্বিতীয় রমাযান এসে উপস্থিত হলে বর্তমান রমাযানের রোযা পালন করতে হবে। তারপর (প্রথম সুযোগে) ঐ কাযা রোযা রেখে নিতে হবে। আর এ ক্ষেত্রে কোন ফিদ্য়্যাহ বা দন্ড-জরিমানা নেই।[1]<br><br>"
                        + "পক্ষান্তরে বিনা ওযরে কাযা না তুলে পরবর্তী রমাযান পার করে দিলে গোনাহগার হতে হবে। আর এ ক্ষেত্রে কাযা করার সাথে সাথে প্রত্যেক দিনের বিনিময়ে একটি করে মিসকীনকে খানা দান করতে হবে। এই মত হল কিছু উলামার।[2] যেহেতু এই মত পোষণ করতেন সাহাবী আবূ হুরাইরা ও ইবনে আববাস (রাযি.)।[3]<br><br>"
                        + "অন্য দিকে অন্য কিছু উলামা বলেন যে, কেবল কাযাই করতে হবে; মিসকীনকে খাদ্য দান করতে হবে না। আর উভয় সাহাবী থেকে যে আসার বর্ণনা করা হয়েছে, তা উক্ত দাবীর দলীল নয়। কারণ, অকাট্য দলীল কেবল কিতাব ও সুন্নাহ থেকেই গৃহীত হবে। পক্ষান্তরে সাহাবাগণের উক্তি দলীল হওয়ার ব্যাপারটা বিবেচনাধীন; বিশেষ করে যখন তাঁদের কথা কুরআনের বাহ্যিক উক্তির প্রতিকূল হয়। আর এখানে কাযা রাখার সাথে মিসকীনকে খানা দান করা ওয়াজেব করার বিধান কুরআনের বাহ্যিক উক্তির বিরোধী। যেহেতু মহান আল্লাহ ভিন্ন দিনে কাযা করা ছাড়া অন্য কিছু ওয়াজেব করেন নি। অতএব এই যুক্তিতে আমরা আল্লাহর বান্দাদেরকে সেই জিনিস পালন করতে বাধ্য করতে পারি না, যে জিনিস পালন করতে তিনি তাদেরকে বাধ্য করেন নি। অবশ্য এ বিষয়ে যদি কোন দায়মুক্তকারী দলীল থাকত, তাহলে সে কথা ভিন্ন ছিল।<br><br>"
                        + "পক্ষান্তরে আবূ হুরাইরা ও ইবনে আববাস (রাযি.) কর্তৃক যে উক্তি বর্ণিত হয়েছে, সে ব্যাপারে বলা যেতে পারে যে, কাযা রাখার সাথে সাথে একটি করে মিসকীন খাইয়ে দেওয়া উত্তম; ওয়াজেব নয়। সুতরাং এ ব্যাপারে সঠিক মত এই যে, পরবর্তী রমাযান অতিবাহিত করে কাযা রাখলে রোযা ছাড়া অন্য কিছু ওয়াজেব নয়। তবে এ কথা ঠিক যে, এই দেরী করার জন্য সে গোনাহগার হবে।[4]<br><br>"
                        + "গত কয়েক বছরের হলেও রমাযানের রোযা কাযা করা ওয়াজেব। সুতরাং কেউ যদি ২০ বছর বয়সে রোযা রাখতে শুরু করে, তাহলে তাকে সাবালক হওয়ার পর থেকে ৫ বছরের ছাড়া রোযা কাযা করতে হবে। আর সেই সাথে তাকে লজ্জিত হয়ে তওবাও করতে হবে এবং এই সংকল্পবদ্ধ হতে হবে যে, জীবনে পুনঃ কোন দিন রোযা ছাড়বে না।[5]<br><br>"
                        + "মা আয়েশা (রাঃ) বলেন, ‘আমরা রোযা কাযা করতে আদিষ্ট হতাম এবং নামায কাযা করতে আদিষ্ট হতাম না।’[6]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (ফিকহুস সুন্নাহ ১/৪১৬)<br>"
                        + "[2] (ফাসিঃ মুসনিদ, ৮২পৃঃ, আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ৯৯পৃঃ)<br>"
                        + "[3] (দারাকুত্বনী, সুনান ২৩১৮, ২৩১৯, ২৩২২নং, বাইহাকী ৪/২৫৩)<br>"
                        + "[4] (আশ্শারহুল মুমতে’ ৬/৪৫১)<br>"
                        + "[5] (মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ ৩০/১০৯)<br>"
                        + "[6] (মুসলিম ৩৩৫নং)</font>",
                "It is impermissible to delay making up missed fasts until the following Ramadan without a valid excuse. If the next Ramadan arrives, one must fast that Ramadan first, then make up the missed fasts afterward.<br><br>"
                        + "If the delay was without excuse, the person incurs sin. Some scholars view that feeding one poor person per day is required alongside Qadha, while the stronger view is that Qadha alone is obligatory, though feeding is recommended. Past years' missed fasts must be made up along with sincere repentance.<br><br>"
                        + "<b>References:</b> Sahih Muslim 335; Sunan ad-Daraqutni 2318-2322, Sunan al-Bayhaqi 4/253; Ash-Sharh al-Mumti' 6/451; Fiqh as-Sunnah 1/416; Majallat al-Buhuth al-Islamiyyah 30/109."
        ));

        // Card 4: ইচ্ছাকৃত ছাড়া রোযার কাযা
        list.add(new HajjHistoryCardItem(
                4,
                "ইচ্ছাকৃত ছাড়া রোযার কাযা",
                "Making Up Deliberately Abandoned Fasts",
                "কিছু সংখ্যক উলামার মত এই যে, যে ব্যক্তি বিনা ওযরে ইচ্ছাকৃত রমাযানের রোযা ত্যাগ করবে, সে ব্যক্তির পাপ হবে মহাপাপ (কাবীরা)। তাকে সে রোযা কাযা করতে হবে এবং ঐ অপরাধের জন্য আল্লাহর কাছে তওবা করতে হবে...",
                "Regarding one who deliberately abandons fasting without a valid reason, scholars differ: some hold that it is a major sin requiring Qadha, sincere repentance, and voluntary worship...",
                "কিছু সংখ্যক উলামার মত এই যে, যে ব্যক্তি বিনা ওযরে ইচ্ছাকৃত রমাযানের রোযা ত্যাগ করবে, সে ব্যক্তির পাপ হবে মহাপাপ (কাবীরা)। তাকে সে রোযা কাযা করতে হবে এবং ঐ অপরাধের জন্য আল্লাহর কাছে তওবা করতে হবে। বেশী বেশী করে নফল রোযা ও অন্যান্য ইবাদত করতে হবে, যাতে ফরয ইবাদতের ঐ ক্ষতিপূরণ সম্ভব হয়। আর সম্ভবতঃ আল্লাহ তাআলা তার তওবা কবুল করে তাকে ক্ষমা করে দেবেন।[1]<br><br>"
                        + "উক্ত অভিমত সেই রমাযানের দিনের বেলায় স্ত্রী-সঙ্গমকারীর হাদীসকে ভিত্তি করে প্রকাশ করা হয়েছে; যাতে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাকে ঐ দিনকে কাযা করতে আদেশ করে বলেছিলেন, ‘‘একদিন রোযা রাখ এবং আল্লাহর কাছে ক্ষমা প্রার্থনা কর।’’[2]<br><br>"
                        + "এ ছাড়া তিনি বলেছেন, ‘‘রোযা অবস্থায় যে ব্যক্তি বমনকে দমন করতে সক্ষম হয় না, তার জন্য কাযা নেই। পক্ষান্তরে যে ইচ্ছাকৃতভাবে বমি করে, সে যেন ঐ রোযা কাযা করে।’’[3]<br><br>"
                        + "অন্য কিছু সংখ্যক উলামা বলেন যে, বিনা ওযরে ইচ্ছাকৃত নামায-রোযা ত্যাগকারীর কোন কাযা নেই। আর না-ই তার তা শুদ্ধ হবে। অবশ্য যা কাযা করার ব্যাপারে দলীল আছে তার কথা সবতন্ত্র। যেমন রোযা রেখে ইচ্ছাকৃত স্ত্রী-সঙ্গমকারী এবং বমনকারী ব্যক্তি দলীলের ভিত্তিতে রোযা কাযা করবে।[4]<br><br>"
                        + "পক্ষান্তরে যে ইচ্ছাকৃতভাবে মোটেই রোযা রাখে না সে ব্যক্তি কিছু উলামার মতে কাফের ও মুর্তাদ হয়ে যাবে। তার জন্য তওবা জরুরী এবং বেশী বেশী নফল ইবাদত করা উচিত। যেমন জরুরী দ্বীনের সকল বিধানকে ঘাড় পেতে মান্য করা। আর উলামাদের সঠিক মতানুসারে তার জন্য কাযা নেই। যেহেতু তার অপরাধ বড় যে, কাযা করে তার খন্ডন হবে না।[5]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৪৩নং)<br>"
                        + "[2] (আবু দাউদ ২৩৯৩, ইবনে খুযাইমাহ, সহীহ ১৯৫৪, দারাকুত্বনী, বাইহাকী ৪/২২৬-২২৭, ইরওয়াউল গালীল ৯৪০নং)<br>"
                        + "[3] (আহমাদ ২/৪৯৮, আবু দাউদ ২৩৮০, তিরমিযী ৭১৬, ইবনে মাজাহ ১৬৭৬, দারেমী ১৬৮০, ইবনে খুযাইমাহ ১৯৬০, ইবনে হিববান,সহীহ, মাওয়ারিদ ৯০৭নং, হাঃ ১/৪২৭, দারাকুত্বনী, বাইহাকী ৪/২১৯ প্রমুখ, ইরওয়াউল গালীল ৯৩০, সহীহুল জামেইস সাগীর ৬২৪৩নং)<br>"
                        + "[4] (তামামুল মিন্নাহ, আল্লামা আলবানী ৪২৫-৪২৬পৃঃ)<br>"
                        + "[5] (ফাতাওয়া ইসলামিয়্যাহ ২/১৫৪, ফাসিঃ মুসনিদ ৮৪পৃঃ)</font>",
                "Scholars hold varying views regarding one who deliberately neglects fasting without excuse. Some view that it is a grave major sin requiring Qadha alongside sincere repentance and abundant voluntary deeds.<br><br>"
                        + "Others (such as Ibn Taymiyyah and Ibn Hazm) argue that Qadha is neither valid nor effective for one who deliberately abandons an obligatory time-bound duty without excuse; rather, turning to Allah in profound repentance and multiplying good deeds is essential.<br><br>"
                        + "<b>References:</b> Sunan Abi Dawud 2380, 2393; Jami' at-Tirmidhi 716; Sunan Ibn Majah 1676; Irwa' al-Ghalil 930, 940; Tamam al-Minnah pp. 425-426; Fatawa Islamiyyah 2/154."
        ));

        // Card 5: চিররোগা খাদ্যদানের পর সুস্থ হলে
        list.add(new HajjHistoryCardItem(
                5,
                "চিররোগা খাদ্যদানের পর সুস্থ হলে",
                "Chronically Ill Who Feeds the Poor and Then Recovers",
                "কোন চিররোগা লোক প্রত্যেক দিনের পরিবর্তে মিসকীনকে খাদ্য দান করার পর আল্লাহ তাকে আরোগ্য দান করলে তার জন্য ঐ দিনগুলিকে কাযা রাখা জরুরী নয়। যেহেতু রোযার বদলে খাদ্য দান করার ফলে তার দায়িত্ব যথাসময়ে পালন হয়ে গেছে...",
                "If a chronically ill person feeds a poor person for each missed day and Allah subsequently cures him, he is not obligated to make up those fasts...",
                "কোন চিররোগা লোক প্রত্যেক দিনের পরিবর্তে মিসকীনকে খাদ্য দান করার পর আল্লাহ তাকে আরোগ্য দান করলে তার জন্য ঐ দিনগুলিকে কাযা রাখা জরুরী নয়। যেহেতু রোযার বদলে খাদ্য দান করার ফলে তার দায়িত্ব যথাসময়ে পালন হয়ে গেছে।[1]<br><br>"
                        + "কোন রোগী রোগের কারণে রোযা ছেড়ে দিল। তারপর কাযা করার জন্য আরোগ্য লাভের আশায় ছিল। কিন্তু তার রোগ ভালো হল না। বরং জানতে পারল যে, তার রোগ চিরস্থায়ী। এমন লোকের জন্য রোযা কাযা করার বদলে প্রত্যেক দিনের পরিবর্তে এক একটি মিসকীনকে খাদ্য দান করা জরুরী।[2]<br><br>"
                        + "একজন রোগী চিররোগ থাকার ফলে রোযা রাখে নি। কিন্তু মিসকীনকে খাদ্যও দান করে নি। অতঃপর কয়েক বছর পার হওয়ার পর সে সুস্থ হয়ে উঠল। এমন রোগীর জন্যও গত রমাযানসমূহের রোযা কাযা করতে বাধ্য নয়। বরং সে প্রত্যেক দিনের বিনিময়ে একটি করে মিসকীন খাওয়াতে বাধ্য। অবশ্য আগামী রমাযানে তার জন্য রোযা রাখা অবশ্যই ফরয।[3]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (সামানিয়া ওয়া আরবাঊন সুআলান ফিস্-সিয়াম ৪১পৃঃ)<br>"
                        + "[2] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ২৭নং)<br>"
                        + "[3] (ইবনে বায, মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ ৩০/১১২, আশ্শারহুল মুমতে’ ৬/৪৫৩)</font>",
                "If a chronically ill person provides food to the poor in place of fasting and Allah subsequently grants recovery, making up those past fasts is not required, because the religious obligation was discharged in its due time according to divine law.<br><br>"
                        + "If a person awaited recovery but found the condition chronic, they feed a poor person for each day. If one neglected feeding and later recovered after years, feeding the poor for past missed days remains binding, while future Ramadan fasts become obligatory.<br><br>"
                        + "<b>References:</b> Sab'una Mas'alah fis-Siyam No. 27, 41; Ash-Sharh al-Mumti' 6/453; Majallat al-Buhuth al-Islamiyyah 30/112."
        ));

        // Card 6: কাযা রাখার পূর্বে কি নফল রাখা চলবে?
        list.add(new HajjHistoryCardItem(
                6,
                "কাযা রাখার পূর্বে কি নফল রাখা চলবে?",
                "Is Voluntary (Nafl) Fasting Permissible Before Qadha?",
                "রমাযানের কাযা রোযা রাখার জন্য সময় সংকীর্ণ না হলে তার পূর্বে নফল রোযা রাখা বৈধ ও শুদ্ধ। অতএব সময় যথেষ্ট থাকলে ফরয রোযা কাযা করার আগে মুসলিম নফল রোযা রাখতে পারে...",
                "If time is not restricted, it is valid to observe voluntary fasts before making up missed Ramadan fasts. However, completing obligatory Qadha first is more virtuous...",
                "রমাযানের কাযা রোযা রাখার জন্য সময় সংকীর্ণ না হলে তার পূর্বে নফল রোযা রাখা বৈধ ও শুদ্ধ। অতএব সময় যথেষ্ট থাকলে ফরয রোযা কাযা করার আগে মুসলিম নফল রোযা রাখতে পারে। যেমন ফরয নামায আদায় করার আগে নফল নামায পড়তে পারে। আর এতে কোন গোনাহ নেই। উভয়ের মধ্যে অনুমিতির কথা সুস্পষ্ট। তবে উত্তম হল প্রথমে ফরয রোযা কাযা রেখে নেওয়া। এমন কি যুলহজ্জের প্রথম ৮ দিন, আরাফার দিন, আশূরার দিন এসে উপস্থিত হলে সে দিনগুলিতেও কাযা রোযা রাখবে। সম্ভবতঃ তাতে কাযা রাখার সওয়াব ও ঐ দিনগুলির ফযীলত উভয়ই লাভ করবে। আর যদি ধরে নেওয়াই যায় যে, কাযা রাখলে ঐ দিনগুলির ফযীলত পাবে না, তাহলেও নফল রাখা থেকে ফরয কাযা করার গুরুত্ব ও মাহাত্ম্য অধিক।[1] তা ছাড়া কিছু সংখ্যক উলামা রমাযানের রোযা কাযা রাখার পূর্বে নফল রোযা রাখা মকরূহ মনে করেছেন।[2]<br><br>"
                        + "পক্ষান্তরে শওয়ালের ছয় রোযা রমাযানের রোযা কাযা করার আগে রাখা যাবে না। রাখলে তা সাধারণ নফলের মান পাবে; শওয়ালের রোযার ফযীলত পাওয়া যাবে না। কেননা, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি রমাযানের রোযা রাখার পর পর শওয়ালের ছয়টি রোযা রাখে, সে ব্যক্তির সারা বছর রোযা রাখা হয়।’’[3] কিন্তু যার রমাযানের রোযা অবশিষ্ট থাকবে, তার ব্যাপারে এ কথা যথাযথ হবে না যে, সে রমাযানের রোযা রেখেছে।[4]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আশ্শারহুল মুমতে’ ৬/৪৪৮)<br>"
                        + "[2] (ইবনে আবী শাইবাহ, মুসান্নাফ ২/৩০৬ দ্রঃ)<br>"
                        + "[3] (মুসলিম ১১৬৪, আবূ দাঊদ ২৪৩৩, তিরমিযী, ইবনে মাজাহ ১৭১৬নং, দারেমী, প্রমুখ)<br>"
                        + "[4] (আশ্শারহুল মুমতে’ ৬/৪৪৯, ফইঃ ২/১৬৬)</font>",
                "As long as time permits before the next Ramadan, voluntary fasts are permissible before completing missed Qadha fasts, just as voluntary prayers are allowed before obligatory ones. However, prioritizing Qadha is far more virtuous and urgent.<br><br>"
                        + "Critically, the special reward of the Six Fasts of Shawwal cannot be attained until missed Ramadan fasts are fully made up, because the Prophet (ﷺ) explicitly stated: 'Whoever fasts Ramadan and then follows it with six days of Shawwal...' One with pending fasts has not yet completed Ramadan.<br><br>"
                        + "<b>References:</b> Sahih Muslim 1164; Sunan Abi Dawud 2433; Sunan Ibn Majah 1716; Musannaf Ibn Abi Shaybah 2/306; Ash-Sharh al-Mumti' 6/448-449."
        ));

        // Card 7: রোযা কাযা রেখে মারা গেলে
        list.add(new HajjHistoryCardItem(
                7,
                "রোযা কাযা রেখে মারা গেলে",
                "If Someone Dies with Missed (Qadha) Fasts Pending",
                "যে ব্যক্তি নামায কাযা রেখে মারা যায়, সে ব্যক্তির অভিভাবক বা অন্য কেউ তার পক্ষ থেকে সে নামায আদায় করে দিতে পারে না। তদনুরূপ যে ব্যক্তি রোযা রাখতে অক্ষম, সে ব্যক্তির তরফ থেকে তার জীবনে কেউ তার সেই রোযা রেখে দিতে পারে না...",
                "If a person dies with missed fasts, no one can pray on their behalf, but rulings on fasting differ based on whether they had the opportunity to make up the fasts or if it was a vowed fast...",
                "যে ব্যক্তি নামায কাযা রেখে মারা যায়, সে ব্যক্তির অভিভাবক বা অন্য কেউ তার পক্ষ থেকে সে নামায আদায় করে দিতে পারে না। আর তার জন্য কোন কাফ্ফারা বা কোন ফিদ্য়্যাহ-জরিমানা নেই। তদনুরূপ যে ব্যক্তি রোযা রাখতে অক্ষম, সে ব্যক্তির তরফ থেকে তার জীবনে কেউ তার সেই রোযা রেখে দিতে পারে না। মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(وَأَنْ لَّيْسَ لِلإِنْسَانِ إِلاَّ مَا سَعَى)</b></font><br><br>"
                        + "অর্থাৎ, আর এই যে, মানুষ যা চেষ্টা করে, তাই সে পেয়ে থাকে। (কুরআনুল কারীম ৫৩/৩৯)<br><br>"
                        + "যদি কোন ব্যক্তি রমাযান মাস চলা অবস্থায় মারা যায়, তাহলে মাসের অবশিষ্ট দিনগুলির ব্যাপারে তার উপরে অথবা তার অভিভাবকের উপরে কোন কিছু ওয়াজেব নয়।[1]<br><br>"
                        + "কিন্তু কোন রোগী রোগে থাকা অবস্থায় (কিছু বা সম্পূর্ণ) রমাযান পার হয়ে মারা গেলে তার ব্যাপারে বিস্তারিত বিবরণ আছেঃ-<br><br>"
                        + "১। যে রোগীর আরোগ্যের আশা আছে, আরোগ্য পর্যন্ত তার উপর রোযা ওয়াজেব থাকবে। কিন্তু রোগ যদি থেকেই যায় এবং কাযা করার সুযোগ হওয়ার আগেই সে মারা যায়, তাহলে তার উপর কিছুই ওয়াজেব নয়। কারণ, তার উপর ওয়াজেব ছিল কাযা, আর তা করতে সে সুযোগই পায় নি。<br><br>"
                        + "২। এমন রোগী যার আরোগ্যের কোন আশা নেই। তার তরফ থেকে শুরু থেকেই মিসকীন খাওয়ানো ওয়াজেব; রোযা কাযার পরিবর্তে নয়। সে মারা গেলে তার তরফ থেকে প্রত্যেক রোযার বিনিময়ে একটি করে মিসকীন খাইয়ে দিলেই ওয়াজেব আদায় হয়ে যাবে। যেহেতু যার ওযর দূর হওয়ার মত নয় -যেমন, অথর্ব বৃদ্ধ এবং চিররোগা, তার তরফ থেকে ১টি রোযার বদলে ১টি মিসকীন খাওয়ানোই ওয়াজেব。<br><br>"
                        + "৩। এমন রোগী যার আরোগ্যের আশা ছিল, অতঃপর রমাযান পরে সে সুস্থও হয়েছে এবং কাযা করার সুযোগও পেয়েছে। কিন্তু কাযা করার আগেই সে মারা গেছে। এমন রোগীর তরফ থেকে তার নিকটাত্মীয় প্রত্যেক রোযার পরিবর্তে একটি করে মিসকীনকে খাদ্য দান করবে।[2]<br><br>"
                        + "অবশ্য যদি ঐ মৃতব্যক্তির কোন আত্মীয় তার তরফ থেকে রোযাগুলি কাযা রেখে দিতে চায়, তাহলে তাও কিছু উলামার নিকট শুদ্ধ হয়ে যাবে।[3] যেহেতু মা আয়েশা (রাঃ) কর্তৃক বর্ণিত, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি নিজের দায়িত্বে রোযা থাকা অবস্থায় মারা যাবে, তার তরফ থেকে তার অভিভাবক (ওয়ারেস) রোযা রাখবে।’’[4]<br><br>"
                        + "অন্য কিছু উলামা এই মতকে প্রাধান্য দেন যে, উক্ত হাদীস নযরের রোযার ব্যাপারে বিবৃত হয়েছে। পক্ষান্তরে রমাযানের ফরয রোযা বাকী রাখা অবস্থায় মারা গেলে, তার তরফ থেকে কারো রোযা রাখা চলবে না। বরং তার অভিভাবক বা ওয়ারেস তার তরফ থেকে প্রত্যেক দিনের পরিবর্তে ১টি করে মিসকীনকে খাদ্য দান করবে।[5] যেহেতু আমরাহ কর্তৃক বর্ণিত, তাঁর আম্মা রমাযানের রোযা বাকী রাখা অবস্থায় মারা যান। তিনি আয়েশা (রাঃ)কে জিজ্ঞাসা করলেন, ‘আমি কি তাঁর তরফ থেকে কাযা রেখে দেব?’ উত্তরে তিনি বললেন, ‘না। বরং তাঁর তরফ থেকে প্রত্যেক দিনের পরিবর্তে ১টি করে মিসকীনকে অর্ধ সা’ (মোটামুটি সওয়া ১ কিলো) খাদ্য দান কর।’[6]<br><br>"
                        + "ইবনে আববাস (রাঃ) কর্তৃক বর্ণিত যে, এক মহিলা আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর নিকট এসে জিজ্ঞাসা করল, ‘হে আল্লাহর রসূল! আমার মা মারা গেছেন। কিন্তু তাঁর যিম্মায় নযরের রোযা বাকী আছে। এখন আমি কি তাঁর তরফ থেকে রোযা রেখে দোব?’ উত্তরে তিনি বললেন, ‘‘তোমার মায়ের যিম্মায় কোন ঋণ বাকী থাকলে তা কি তুমি পরিশোধ করতে? তা কি তার তরফ থেকে আদায় করা হত?’’ মহিলাটি বলল, ‘জী হ্যাঁ।’ তিনি বললেন, ‘‘অতএব তুমি তোমার মায়ের তরফ থেকে রোযা রেখে দাও।’’[7]<br><br>"
                        + "ইবনে আববাস (রাঃ) বলেন, ‘যদি কোন লোক রমাযানে ব্যাধিগ্রস্ত হয়, অতঃপর সে মারা যায় এবং রোযা (কাযা করার সুযোগ পাওয়া সত্ত্বেও) রোযা না রেখে থাকে, তাহলে তার তরফ থেকে মিসকীন খাইয়ে দিতে হবে; তার জন্য রোযা কাযা নেই। কিন্তু যদি সে নযরের রোযা না রেখে মারা যায়, তাহলে তার অভিভাবক (বা ওয়ারেস) তার তরফ থেকে সেই রোযা কাযা করে দেবে।’[8]<br><br>"
                        + "বলা বাহুল্য, যে ব্যক্তি নযরের রোযা না রেখে মারা যাবে, তার তরফ থেকে তার ওয়ারেস রোযা রেখে দেবে। আর এই রোযা রেখে দেওয়ার মান হল মুস্তাহাব। কারণ, মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(وَلاَ تَزِرُ وَازِرَةٌ وِّزْرَ أُخْرَى)</b></font><br><br>"
                        + "অর্থাৎ, কেউ অপরের ভার বহন করবে না। (কুরআনুল কারীম ৬/১৬৪)<br><br>"
                        + "মৃতব্যক্তির তরফ থেকে রোযা রাখার সময় একাধিক রোযা হলে ওয়ারেসরা যদি আপোসে ভাগ করে রাখে, তাহলে তা বৈধ। কিন্তু এই ভাগাভাগি ‘যিহার’ কিংবা রমাযানের দিনে সঙ্গম করার কাফ্ফারার রোযায় চলবে না। কারণ, তাতে লাগাতার রোযা হওয়ার শর্ত আছে। আর ভাগাভাগি করে রাখলে নিরবচ্ছিন্নতা থাকে না। অতএব হয় মাত্র একজনই লাগাতার ৬০ রোযা রেখে দেবে। নতুবা ৬০টি মিসকীন খাইয়ে দেবে।[9]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ১৪নং, তাযকীরু ইবাদির রাহমান, ফীমা অরাদা বিসিয়ামি শাহরি রামাযান ৪৯পৃঃ)<br>"
                        + "[2] (আশ্শারহুল মুমতে’ ৬/৪৫২-৪৫৩)<br>"
                        + "[3] (আশ্শারহুল মুমতে’ ৬/৪৫৫-৪৫৬, সাবঊনা মাসআলাহ ফিস্-সিয়াম ২৯নং)<br>"
                        + "[4] (বুখারী ১৯৫২, মুসলিম ১১৪৭নং, প্রমুখ)<br>"
                        + "[5] (আহকামুল জানায়েয, আলবানী ১৭০পৃঃ, তামামুল মিন্নাহ, আল্লামা আলবানী ৪২৭-৪২৮পৃঃ দ্রঃ)<br>"
                        + "[6] (ত্বাহাবী, ইবনে হায্ম, আহকামুল জানায়েয, আলবানী ১৭০পৃঃ দ্রঃ)<br>"
                        + "[7] (আহমাদ, মুসনাদ ২/২১৬, বুখারী ১৯৫৩, মুসলিম ১১৪৮, আবূ দাঊদ ৩৩০৮নং প্রমুখ)<br>"
                        + "[8] (সহীহ আবূ দাঊদ ২১০১নং প্রমুখ)<br>"
                        + "[9] (আশ্শারহুল মুমতে’ ৬/৪৫৭-৪৫৮)</font>",
                "If someone dies with missed fasts: If they passed away during illness without ever having the opportunity to make them up, nothing is required on their behalf. If they recovered and had opportunity but delayed until death, their heirs feed one poor person per missed day.<br><br>"
                        + "Regarding vowed fasts (Nadhr), the Prophet (ﷺ) allowed the guardian/heir to fast on their behalf. For expiations requiring consecutive fasting (such as Zihar or daytime intercourse Kaffarah), dividing days among heirs is impermissible.<br><br>"
                        + "<b>References:</b> Surah An-Najm: 39, Surah Al-An'am: 164; Sahih al-Bukhari 1952, 1953; Sahih Muslim 1147, 1148; Sunan Abi Dawud 2101, 3308; Ahkam al-Jana'iz p. 170; Ash-Sharh al-Mumti' 6/452-458."
        ));

        return list;
    }
}
