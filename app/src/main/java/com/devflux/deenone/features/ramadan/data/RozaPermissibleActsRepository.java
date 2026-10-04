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
 * Repository for রোযা অবস্থায় যা বৈধ (Things Permissible While Fasting).
 * Pre-seeded with all 24 cards verbatim matching user prompt and screenshot.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaPermissibleActsRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaPermissibleActsRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_permissible_acts.php");
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
            // High reliability: seamlessly fallback to in-memory pre-seeded cache
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: পানিতে নামা, ডুব দেয়া ও সাঁতার কাটা
        list.add(new HajjHistoryCardItem(
                1,
                "পানিতে নামা, ডুব দেয়া ও সাঁতার কাটা",
                "Entering Water, Diving & Swimming",
                "রোযাদারের জন্য পানিতে নামা, ডুব দেওয়া ও সাঁতার কাটা, একাধিক বার গোসল করা, এসির হাওয়াতে বসা এবং কাপড় ভিজিয়ে গায়ে-মাথায় জড়ানো বৈধ...",
                "It is permissible for a fasting person to enter water, dive, swim, take multiple baths, sit in air conditioning, and wrap a wet cloth around themselves...",
                "কিছু কাজ আছে, যা রোযা অবস্থায় করা বৈধ নয় বলে অনেকের মনে হতে পারে, অথচ তা রোযাদারের জন্য করা বৈধ। সেই ধরনের কিছু কাজের কথা নিম্নে আলোচনা করা হচ্ছেঃ-\n\n"
                        + "১। পানিতে নামা, ডুব দেয়া ও সাঁতার কাটা\n\n"
                        + "রোযাদারের জন্য পানিতে নামা, ডুব দেওয়া ও সাঁতার কাটা, একাধিক বার গোসল করা, এসির হাওয়াতে বসা এবং কাপড় ভিজিয়ে গায়ে-মাথায় জড়ানো বৈধ। যেমন পিপাসা ও গরমের তাড়নায় মাথায় পানি ঢালা, বরফ বা আইসক্রিম চাপানো দোষাবহ নয়।[1]\n\n"
                        + "মা আয়েশা (রাঃ) বলেন, রোযা রেখে নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর অপবিত্র অবস্থায় ফজর হত। অতঃপর তিনি গোসল করতেন।[2]\n\n"
                        + "আবূ বাক্র বিন আব্দুর রহমান নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর কিছু সাহাবী থেকে বর্ণনা করেন যে, তিনি (সাহাবী) বলেন, আমি আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-কে দেখেছি, তিনি রোযা রেখে পিপাসা অথবা গরমের কারণে নিজ মাথায় পানি ঢেলেছেন।[3]\n\n"
                        + "রোযা রাখা অবস্থায় ইবনে উমার একটি কাপড় ভিজিয়ে নিজের দেহের উপর রেখেছেন।[4]\n\n"
                        + "অবশ্য সাঁতার কেটে খেলা করা মকরূহ। কারণ, তাতে রোযা নষ্ট হয়ে যাওয়ার সম্ভাবনা আছে তাই। কিন্তু যার কাজই হল ডুবরীর অথবা প্রয়োজনের তাকীদে পানিতে বারবার ডুব দিতে হয়, সে ব্যক্তি পেটে পানি পৌঁছনো থেকে সাবধান থাকতে পারলে তার রোযার কোন ক্ষতি হবে না।[5]\n\n"
                        + "[1] (ফুসূলুন ফিস্-সিয়ামি অত্-তারাবীহি অয্-যাকাহ ১৬পৃঃ, তাযকীরু ইবাদির রাহমান, ফীমা অরাদা বিসিয়ামি শাহরি রামাযান ৪৬পৃঃ, ফাতাওয়া ইসলামিয়্যাহ ২/১৩০, ফাসিঃ ৪৭পৃঃ, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৫৮নং)\n\n"
                        + "[2] (বুখারী ১৯২৫, মুসলিম ১১০৯নং)\n\n"
                        + "[3] (আহমাদ ৩/৪৭৫, আবূ দাউদ ২৩৬৫নং, মালেক, মুঅত্তা)\n\n"
                        + "[4] (ইবনে আবী শাইবাহঃ ৯২১২নং)\n\n"
                        + "[5] (৭০ মাসআলাহ ফিস্-সিয়ামঃ ৫৮নং)",
                "There are certain actions that many people might mistakenly believe are impermissible while fasting, whereas they are entirely permissible for the fasting person:\n\n"
                        + "1. Entering Water, Diving, and Swimming\n\n"
                        + "It is permissible for a fasting person to enter water, dive, swim, take multiple baths, sit in air-conditioned rooms, and wrap wet clothes around their body or head. Pouring water over the head or applying ice due to intense thirst or scorching heat is not blameworthy.\n\n"
                        + "Mother Aisha (may Allah be pleased with her) narrated that the Prophet (peace be upon him) would wake up at dawn in a state of ritual impurity while fasting, and then he would perform Ghusl.\n\n"
                        + "Abu Bakr bin Abdur Rahman narrated from Companions of the Prophet (peace be upon him) who said: 'I saw the Messenger of Allah (peace be upon him) pouring water over his head while fasting due to extreme thirst or intense heat.'\n\n"
                        + "Ibn Umar (may Allah be pleased with him) used to soak a garment and place it over his body while fasting.\n\n"
                        + "Swimming merely for play or amusement is disliked (Makruh) because of the risk of water entering the stomach. However, for a diver or someone who needs to dive out of necessity, as long as they take strict care that water does not reach the stomach, their fast is unharmed.\n\n"
                        + "References: Sahih Bukhari 1925, Sahih Muslim 1109, Musnad Ahmad 3/475, Sunan Abi Dawud 2365."
        ));

        // Card 2: মিসওয়াক বা দাঁতন করা
        list.add(new HajjHistoryCardItem(
                2,
                "মিসওয়াক বা দাঁতন করা",
                "Using Miswak or Tooth-Stick",
                "দাঁতন করা রোযাদার-অরোযাদার সকলের জন্য এবং দিনের শুরু ও শেষ ভাগে সব সময়কার জন্য সুন্নত...",
                "Using the tooth-stick (Miswak) is Sunnah for both fasting and non-fasting persons throughout the entire day...",
                "দাঁতন করা রোযাদার-অরোযাদার সকলের জন্য এবং দিনের শুরু ও শেষ ভাগে সব সময়কার জন্য সুন্নত। এ ব্যাপারে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর নির্দেশ ব্যাপক; তিনি বলেন, ‘‘দাঁতন করায় রয়েছে মুখের পবিত্রতা এবং প্রতিপালক আল্লাহর সন্তুষ্টি।[1]\n\n"
                        + "প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘আমি উম্মতের জন্য কষ্টকর না জানলে তাদেরকে প্রত্যেক নামাযের সময় দাঁতন করতে আদেশ দিতাম।’’[2] অন্য এক বর্ণনায় আছে, ‘‘---তাদেরকে প্রত্যেক ওযূর সময় দাঁতন করতে আদেশ দিতাম।’’[3]\n\n"
                        + "ইমাম ত্বাবারানী উত্তম সনদ দ্বারা বর্ণনা করেছেন যে, আব্দুর রহমান বিন গুন্ম বলেন, আমি মুআয বিন জাবাল (রাঃ)-কে জিজ্ঞাসা করলাম, ‘আমি কি রোযা অবস্থায় দাঁতন করব?’ উত্তরে তিনি বললেন, ‘হ্যাঁ।’ আমি বললাম, ‘দিনের কোন্ ভাগে?’ তিনি বললেন, ‘সকাল অথবা বিকালে।’ আমি বললাম, ‘লোকে তো রোযার বিকালে দাঁতন করাকে অপছন্দনীয় মনে করে। তারা বলে, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘নিশ্চয়ই রোযাদারের মুখের দুর্গন্ধ আল্লাহর নিকট কস্ত্তরীর সুবাস অপেক্ষা অধিকতর সুগন্ধময়।’’ মুআয (রাঃ) বললেন, ‘সুবহানাল্লাহ! তিনি তাদেরকে দাঁতন করতে আদেশ দিয়েছেন। আর যে জিনিস তিনি পরিষ্কার করতে আদেশ দিয়েছেন, সে জিনিসকে ইচ্ছাকৃতভাবে দুর্গন্ধময় করা উত্তম হতে পারে না। তাতে কোন প্রকারের মঙ্গল নেই; বরং তাতে অমঙ্গলই আছে।’[4]\n\n"
                        + "কিন্তু যদি কোন দাঁতনে বিশেষ স্বাদ থাকে এবং তা তার থুথুকে প্রভাবান্বিত করে, তাহলে তার স্বাদ বা থুথু গিলে নেওয়া উচিৎ নয়।[5] পরন্তু সেই দাঁতন করা থেকে দূরে থাকা উচিৎ, যার দ্রবণশীল উপাদান (ও রস) আছে। যেমন কাঁচা (গাছের ডালের বা শিকড়ের) দাঁতন। তদনুরূপ সেই দাঁতন, যাতে তার নিজস্ব স্বাদ ছাড়া ভিন্ন স্বাদ; যেমন লেবু বা পুদ্বীনা (পেপারমে¦ট্, মেনথল) ইত্যাদির স্বাদ অতিরিক্ত করা হয়েছে এবং যা মুখের ভিতরে গিয়ে দ্রবীভূত হয়ে মুখগহ্বরে ছড়িয়ে পড়ে। আর ইচ্ছা করে তা গিলে ফেলা বৈধ নয়। তবে যদি অনিচ্ছাকৃত কারো গিলা যায়, তাহলে তাতে কোন ক্ষতি হয় না।[6]\n\n"
                        + "পক্ষান্তরে রোযার দিনে দাঁতের মাজন (টুথ পেষ্ট্ বা পাওডার) ব্যবহার না করাই উত্তম। বরং তা রাত্রে এবং ফজরের আগে ব্যবহার করা উচিৎ। কারণ, মাজনের এমন প্রতিক্রিয়া ও সঞ্চার ক্ষমতা আছে, যার ফলে তা গলা ও পাকস্থলীতে নেমে যাওয়ার আশঙ্কা থাকে। অনুরূপ আশঙ্কার ফলেই মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) লাকীত্ব বিন সাবরাহকে বলেছিলেন, ‘‘(ওযূ করার সময়) তুমি নাকে খুব অতিরঞ্জিতভাবে পানি টেনে নিও। কিন্তু তোমার রোযা থাকলে নয়।’’[7]\n\n"
                        + "বলা বাহুল্য, রোযাদারের জন্য মাজন ব্যবহার না করাই উত্তম। আর এ ব্যাপারে সংকীর্ণতা নেই। কারণ, সে ইফতার করে নেওয়া পর্যন্ত সময় অপেক্ষা করে যদি তা ব্যবহার করে, তাহলে সে এমন এক জিনিস থেকে দূরে থাকতে পারবে, যার দ্বারা তার রোযা নষ্ট হয়ে যাওয়ার আশঙ্কা রয়েছে।[8]\n\n"
                        + "পক্ষান্তরে নেশাদার ও দেহে অবসন্ন আনয়নকারী মাজন; যেমন, গুল-গুরাকু প্রভৃতি; যা ব্যবহারের ফলে মাথা ঘোরে অথবা ব্যবহারকারী জ্ঞানশূন্য হয়ে যায়, তা ব্যবহার করা বৈধ নয়; না রোযা অবস্থায় এবং না অন্য সময়। কারণ, তা মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর এই বাণীর আওতাভুক্ত হতে পারে, যাতে তিনি বলেন, ‘‘প্রত্যেক মাদকতা আনয়নকারী দ্রব্য হারাম।’’[9]\n\n"
                        + "জ্ঞাতব্য যে, দাঁতের মাড়িতে ক্ষত থাকার ফলে অথবা দাঁতন করতে গিয়ে রক্ত বের হলে তা গিলে ফেলা বৈধ নয়; বরং তা বের করে ফেলা জরুরী। অবশ্য যদি তা নিজের ইচ্ছা ও এখতিয়ার ছাড়াই গলায় নেমে যায়, তাহলে তাতে কোন ক্ষতি হবে না।[10]\n\n"
                        + "[1] (আহমাদ, মুসনাদ ৬/৪৭, দারেমী, নাসাঈ ৫নং, ইবনে খুযাইমাহ, সহীহ, বাইহাকী ১/৩৪, ইবনে হিববান, সহীহ, বুখারী (বিনা সনদে), মিশকাতুল মাসাবীহ ৩৮১, ইরওয়াউল গালীল, আলবানী ৬৬নং)\n\n"
                        + "[2] (বুখারী ৮৮৭, মুসলিম ২৫২, সুনানে আরবাআহ; আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ)\n\n"
                        + "[3] (আহমাদ, মুসনাদ ২/৪৬০, ৫১৭, প্রমুখ)\n\n"
                        + "[4] (দ্রঃ ইরওয়াউল গালীল, আলবানী ১/১০৬)\n\n"
                        + "[5] (ইবনে উষাইমীন, ফাসিঃ মুসনিদ ৩৯পৃঃ)\n\n"
                        + "[6] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৫৪নং)\n\n"
                        + "[7] (আহমাদ, মুসনাদ ৪/৩৩, আবূ দাঊদ ১৪২, তিরমিযী, নাসাঈ, সহীহ ইবনে মাজাহ, আলবানী ৩২৮নং)\n\n"
                        + "[8] (আশ্শারহুল মুমতে’ ৬/৪০৭, ৪৩২, সামানিয়া ওয়া আরবাঊন সুআলান ফিস্-সিয়াম, ইবনে উষাইমীন ৬৩পৃঃ)\n\n"
                        + "[9] (বুখারী, মুসলিম, সুনানে আরবাআহ; আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ, সজাঃ ৪৫৫০নং)\n\n"
                        + "[10] (ইবনে উষাইমীন, ফাসিঃ ৩৯পৃঃ, ৭০ঃ ৫৩নং)",
                "Using a tooth-stick (Miswak) is Sunnah for everyone, both fasting and non-fasting, at the beginning and end of the day. The Prophet (peace be upon him) said: 'The Miswak cleanses the mouth and pleases the Lord.'\n\n"
                        + "One should avoid tooth-sticks with artificial flavoring (such as mint or lemon) if particles dissolve and mix with saliva, and one must never swallow the juice intentionally.\n\n"
                        + "Using toothpaste is better avoided during the day and used before dawn or after sunset to prevent swallowing. Intoxicating tobacco pastes (such as Gul) are strictly prohibited at all times. Any bleeding from gums should be spat out.\n\n"
                        + "References: Sahih Bukhari 887, Sahih Muslim 252, Sunan Abi Dawud 142, Irwa al-Ghaleel 66."
        ));

        // Card 3: সুরমা লাগানো এবং চোখে ও কানে ওষুধ ব্যবহার
        list.add(new HajjHistoryCardItem(
                3,
                "সুরমা লাগানো এবং চোখে ও কানে ওষুধ ব্যবহার",
                "Applying Kohl and Using Eye & Ear Drops",
                "রোযা অবস্থায় সুরমা লাগানো এবং চোখে ও কানে ওষুধ ব্যবহার বৈধ...",
                "Applying kohl (Surma) and using eye and ear drops are permissible while fasting...",
                "রোযা অবস্থায় সুরমা লাগানো এবং চোখে ও কানে ওষুধ ব্যবহার বৈধ। কিন্তু ব্যবহার করার পর যদি গলায় সুরমা বা ওষুধের স্বাদ অনুভূত হয়, তাহলে (কিছু উলামার নিকট রোযা ভেঙ্গে যাবে এবং সে রোযা) কাযা রেখে নেওয়াই হল পূর্বসতর্কতামূলক কর্ম।[1] কারণ, চোখ ও কান খাদ্য ও পানীয় পেটে যাওয়ার পথ নয় এবং সুরমা বা ওষুধ লাগানোকে খাওয়া বা পান করাও বলা যায় না; না সাধারণ প্রচলিত কথায় এবং না-ই শরয়ী পরিভাষায়। অবশ্য রোযাদার যদি চোখে বা কানে ওষুধ দিনে ব্যবহার না করে রাতে করে, তাহলে সেটাই হবে পূর্বসাবধানতামূলক কর্ম।[2]\n\n"
                        + "আনাস (রাঃ) রোযা থাকা অবস্থায় সুরমা ব্যবহার করতেন।[3]\n\n"
                        + "পক্ষান্তরে রোযা থাকা অবস্থায় নাকে ওষুধ ব্যবহার বৈধ নয়। কারণ, নাকের মাধ্যমে পানাহার পেটে পৌঁছে থাকে। আর এ জন্যই মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘(ওযূ করার সময়) তুমি নাকে খুব অতিরঞ্জিতভাবে পানি টেনে নিও। কিন্তু তোমার রোযা থাকলে নয়।’’[4]\n\n"
                        + "বলা বাহুল্য উক্ত হাদীস এবং অনুরূপ অর্থের অন্যান্য হাদীসের ভিত্তিতেই নাকে ওষুধ ব্যবহার করার পর যদি গলাতে তার স্বাদ অনুভূত হয়, তাহলে রোযা নষ্ট হয়ে যাবে এবং সে রোযা কাযা করতে হবে।[5]\n\n"
                        + "[1] (ইবনে বায, ফাতাওয়া মুহিম্মাহ, তাতাআল্লাকু বিস্সিয়াম, ইবনে বায ২৮পৃঃ)\n\n"
                        + "[2] (আশ্শারহুল মুমতে’ ৬/৩৮২, সঊদী স্থায়ী উলামা কমিটি, ফাসিঃ মুসনিদ ৪৪পৃঃ, ফাতাওয়া ইসলামিয়্যাহ ২/১২৯)\n\n"
                        + "[3] (সহীহ আবূ দাঊদ ২০৮২নং)\n\n"
                        + "[4] (আহমাদ, মুসনাদ ৪/৩৩, আবূ দাঊদ ১৪২, তিরমিযী, নাসাঈ, সহীহ ইবনে মাজাহ, আলবানী ৩২৮নং)\n\n"
                        + "[5] (ইবনে বায, ফাতাওয়া মুহিম্মাহ, তাতাআল্লাকু বিস্সিয়াম ২৮পৃঃ)",
                "Applying Kohl (Surma) and using eye or ear drops while fasting is permissible because the eyes and ears are not normal conduits to the stomach for food or drink.\n\n"
                        + "Anas (may Allah be pleased with him) used to apply kohl while fasting.\n\n"
                        + "However, nasal drops reach the throat and stomach directly. The Prophet (peace be upon him) instructed: 'Sniff water deeply into your nose during ablution, unless you are fasting.' Therefore, if nasal medicine reaches the throat, the fast is nullified and must be made up.\n\n"
                        + "References: Sunan Abi Dawud 142, 2082, Fatawa Islamiyyah 2/129."
        ));

        // Card 4: পেটে (এন্ডোসকপি মেশিন) নল সঞ্চালন
        list.add(new HajjHistoryCardItem(
                4,
                "পেটে (এন্ডোসকপি মেশিন) নল সঞ্চালন",
                "Endoscopy and Stomach Tube Insertion",
                "পেটের ভিতর কোন পরীক্ষার জন্য (এন্ডোসকপি মেশিন) নল বা স্টমাক টিউব সঞ্চালন করার ফলে রোযার কোন ক্ষতি হয় না...",
                "Inserting an endoscopy tube or gastric scope for diagnostic examination does not invalidate the fast...",
                "পেটের ভিতর কোন পরীক্ষার জন্য (এন্ডোসকপি মেশিন) নল বা স্টমাক টিউব সঞ্চালন করার ফলে রোযার কোন ক্ষতি হয় না। তবে হ্যাঁ, যদি পাইপের সাথে কোন (তৈলাক্ত) পদার্থ থাকে এবং তা তার সাথে পেটে গিয়ে পৌঁছে, তাহলে তাতে রোযা নষ্ট হয়ে যাবে। অতএব একান্ত প্রয়োজন ছাড়া এ কাজ ফরয বা ওয়াজেব রোযায় করা বৈধ নয়।[1]\n\n"
                        + "[1] (আশ্শারহুল মুমতে’ ৬/৩৮৩-৩৮৪)",
                "Inserting a stomach probe or endoscopy tube for diagnostic examination does not invalidate the fast in itself. However, if the tube is coated with lubricants, medicinal gels, or fluids that enter the stomach, the fast is invalidated. Hence, this should be avoided during obligatory fasts unless strictly necessary.\n\n"
                        + "Reference: Ash-Sharh al-Mumti' 6/383-384."
        ));

        // Card 5: বাহ্যিক শরীরে তেল, মলম, পাওডার বা ক্রিম ব্যবহার
        list.add(new HajjHistoryCardItem(
                5,
                "বাহ্যিক শরীরে তেল, মলম, পাওডার বা ক্রিম ব্যবহার",
                "Applying Oil, Ointment, Powder, or Cream",
                "বাহ্যিক শরীরের চামড়ায় পাওডার বা মলম ব্যবহার করা রোযাদারের জন্য বৈধ। কারণ, তা পেটে পৌঁছে না...",
                "Applying external ointments, powders, moisturizers, or oils to the skin is permissible while fasting...",
                "বাহ্যিক শরীরের চামড়ায় পাওডার বা মলম ব্যবহার করা রোযাদারের জন্য বৈধ। কারণ, তা পেটে পৌঁছে না।\n\n"
                        + "তদনুরূপ প্রয়োজনে ত্বককে নরম রাখার জন্য কোন তেল, ভ্যাসলিন বা ক্রিম ব্যবহার করাও রোযা অবস্থায় অবৈধ নয়। কারণ, এ সব কিছু কেবল চামড়ার বাহিরের অংশ নরম করে থাকে এবং শরীরের ভিতরে প্রবেশ করে না। পরন্তু যদিও লোমকূপে তা প্রবেশ হওয়ার কথা ধরেই নেওয়া যায়, তবুও তাতে রোযা নষ্ট হবে না।[1]\n\n"
                        + "তদনুরূপ রোযা অবস্থায় মহিলাদের জন্য হাতে মেহেন্দী, পায়ে আলতা অথবা চুলে (কালো ছাড়া অন্য রঙের) কলফ ব্যবহার বৈধ। এ সবে রোযা বা রোযাদারের উপর কোন (মন্দ) প্রভাব ফেলে না।[2]\n\n"
                        + "[1] (ইবনে জিবরীন, ফাতাওয়া ইসলামিয়্যাহ ২/১২৭, ফাসিঃ মুসনিদ ৪১পৃঃ)\n\n"
                        + "[2] (ফাসিঃ মুসনিদ ৪৫পৃঃ, ফাতাওয়া ইসলামিয়্যাহ ২/১২৭)",
                "Applying powders, creams, lotions, or medicinal ointments to the skin is permissible for the fasting person because they do not enter the digestive tract. Even if absorbed through pores, it does not nullify the fast.\n\n"
                        + "Similarly, applying henna (Mehndi) or non-black hair dye is permissible for women and does not affect the validity of the fast.\n\n"
                        + "References: Fatawa Islamiyyah 2/127."
        ));

        // Card 6: স্বামী-স্ত্রীর আপোষের চুম্বন ও প্রেমকেলি
        list.add(new HajjHistoryCardItem(
                6,
                "স্বামী-স্ত্রীর আপোষের চুম্বন ও প্রেমকেলি",
                "Marital Kissing and Affection",
                "যে রোযাদার স্বামী-স্ত্রী মিলনে ধৈর্য রাখতে পারে; অর্থাৎ সঙ্গম বা বীর্যপাত ঘটে যাওয়ার আশঙ্কা না করে, তাদের জন্য আপোসে চুম্বন ও প্রেমকেলি করা বৈধ...",
                "For spouses who can exercise self-restraint and avoid ejaculation or intercourse, kissing and affection are permissible...",
                "যে রোযাদার স্বামী-স্ত্রী মিলনে ধৈর্য রাখতে পারে; অর্থাৎ সঙ্গম বা বীর্যপাত ঘটে যাওয়ার আশঙ্কা না করে, তাদের জন্য আপোসে চুম্বন ও প্রেমকেলি বা কোলাকুলি করা বৈধ এবং তা তাদের জন্য মকরূহ নয়। কারণ, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) রোযা রাখা অবস্থায় স্ত্রী-চুম্বন করতেন এবং রোযা অবস্থায় প্রেমকেলিও করতেন। আর তিনি ছিলেন যৌন ব্যাপারে বড় সংযমী।[1] অন্য এক বর্ণনায় আছে যে, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) স্ত্রী-চুম্বন করতেন রমাযানে রোযা রাখা অবস্থায়;[2] রোযার মাসে।[3]\n\n"
                        + "আর এক বর্ণনায় আছে, আয়েশা (রাঃ) বলেন, ‘আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) আমাকে চুম্বন দিতেন। আর সে সময় আমরা উভয়ে রোযা অবস্থায় থাকতাম।’[4]\n\n"
                        + "উম্মে সালামাহ (রাঃ) বলেন, তিনি তাঁর সাথেও অনুরূপ করতেন।[5] আর তদ্রূপ বলেন হাফসা (রাঃ)ও।[6]\n\n"
                        + "উমার (রাঃ) বলেন, একদা স্ত্রীকে খুশী করতে গিয়ে রোযা অবস্থায় আমি তাকে চুম্বন দিয়ে ফেললাম। অতঃপর নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর নিকট উপস্থিত হয়ে বললাম, ‘আজ আমি একটি বিরাট ভুল করে ফেলেছি; রোযা অবস্থায় স্ত্রী-চুম্বন করে ফেলেছি।’ আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘যদি রোযা রেখে পানি দ্বারা কুল্লি করতে, তাহলে তাতে তোমার অভিমত কি?’’ আমি বললাম, ‘তাতে কোন ক্ষতি নেই।’ মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘তাহলে ভুল কিসের?’’[7]\n\n"
                        + "পক্ষান্তরে রোযাদার যদি আশঙ্কা করে যে, প্রেমকেলি বা চুম্বনের ফলে তার বীর্যপাত ঘটে যেতে পারে অথবা (স্বামী-স্ত্রী) উভয়ের উত্তেজনার ফলে সহসায় মিলন ঘটে যেতে পারে, কারণ সে সময় সে হয়তো তাদের উদগ্র কাম-লালসাকে সংযত করতে পারবে না, তাহলে সে কাজ তাদের জন্য হারাম। আর তা হারাম এই জন্য যে, যাতে পাপের ছিদ্রপথ বন্ধ থাকে এবং তাদের রোযা নষ্ট হওয়া থেকে রক্ষা পায়।\n\n"
                        + "এ ক্ষেত্রে বৃদ্ধ ও যুবকের মাঝে কোন পার্থক্য নেই; যদি উভয়ের কামশক্তি এক পর্যায়ের হয়। সুতরাং দেখার বিষয় হল, কাম উত্তেজনা সৃষ্টি এবং বীর্যস্খলনের আশঙ্কা। অতএব সে কাজ যদি যুবক বা কামশক্তিসম্পন্ন বৃদ্ধের উত্তেজনা সৃষ্টি করে, তাহলে তা উভয়ের জন্য মকরূহ। আর যদি তা না করে তাহলে তা বৃদ্ধ, যৌন-দুর্বল এবং সংযমী যুবকের জন্য মকরূহ নয়। পক্ষান্তরে উভয়ের মাঝে পার্থক্য করার ব্যাপারে যে হাদীস বর্ণিত হয়েছে[8] তা আসলে কামশক্তি বেশী থাকা ও না থাকার কারণে। যেহেতু সাধারণতঃ বৃদ্ধ যৌন ব্যাপারে শান্ত হয়ে থাকে। পক্ষান্তরে যুবক তার বিপরীত।\n\n"
                        + "ফলকথা, সকল শ্রেণীর দম্পতির জন্য উত্তম হল রোযা রেখে প্রেমকেলি, কোলাকুলি ও চুম্বন বিনিময় প্রভৃতি যৌনাচারের ভূমিকা পরিহার করা। কারণ, যে গরু সবুজ ফসল-জমির আশেপাশে চরে, আশঙ্কা থাকে যে, সে কিছু পরে ফসল খেতে শুরু করে দেবে। সুতরাং স্বামী যদি ইফতার করা অবধি ধৈর্য ধারণ করে, তাহলে সেটাই হল সর্বোত্তম। আর রাত্রি তো অতি নিকটে এবং তাতো যথেষ্ট লম্বা। অল্-হামদু লিল্লাহ। মহান আল্লাহ বলেন, ‘‘রোযার রাতে তোমাদের জন্য স্ত্রী-সম্ভোগ হালাল করা হয়েছে।’’ (কুরআনুল কারীম ২/১৮৭)\n\n"
                        + "চুম্বনের ক্ষেত্রে চুম্বন গালে হোক অথবা ঠোঁটে উভয় অবস্থাই সমান। তদনুরূপ সঙ্গমের সকল প্রকার ভূমিকা ও শৃঙ্গারাচার; সকাম স্পর্শ, ঘর্ষণ, দংশন, মর্দন, প্রচাপন, আলিঙ্গন প্রভৃতির মানও চুম্বনের মতই। এ সবের মাঝে কোন পার্থক্য নেই। আর এ সব করতে গিয়ে যদি কারো মযী (বা উত্তেজনার সময় আঠালো তরল পানি) নিঃসৃত হয়, তাহলে তাতে রোযার কোন ক্ষতি হয় না।[9]\n\n"
                        + "জ্ঞাতব্য যে, জিভ চোষার ফলে একে অন্যের জিহবারস গিলে ফেললে রোযা ভেঙ্গে যাবে। যেমন স্তনবৃন্ত চোষণের ফলে মুখে দুগ্ধ এসে গলায় নেমে গেলেও রোযা ভেঙ্গে যাবে।\n\n"
                        + "স্ত্রীর দেহাঙ্গের যে কোন অংশ দেখা রোযাদার স্বামীর জন্যও বৈধ। অবশ্য একবার দেখার ফলেই চরম উত্তেজিত হয়ে কারো মযী বা বীর্যপাত ঘটলে কোন ক্ষতি হবে না।[10] কারণ, অবৈধ নজরবাজীর ব্যাপারে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘প্রথম দৃষ্টি তোমার জন্য বৈধ। কিন্তু দ্বিতীয় দৃষ্টি বৈধ নয়।’’[11] তাছাড়া দ্রুতপতনগ্রস্ত এমন দুর্বল স্বামীর এমন ওযর গ্রহণযোগ্য।\n\n"
                        + "পক্ষান্তরে কেউ বারবার দেখার ফলে মযী নির্গত করলে রোযার কোন ক্ষতি হয় না। কিন্তু বারবার দেখার ফলে বীর্যপাত করে ফেললে রোযা নষ্ট হয়ে যাবে।\n\n"
                        + "অবশ্য স্ত্রী-দেহ নিয়ে কল্পনা করার ফলে কারো মযী বা বীর্যপাত হলে রোযা নষ্ট হয় না। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর ব্যাপক নির্দেশ এ কথার প্রতি ইঙ্গিত করে। তিনি বলেন, ‘‘নিশ্চয় আল্লাহ আমার উম্মতের মনের কল্পনা উপেক্ষা করেন, যতক্ষণ কেউ তা কাজে পরিণত অথবা কথায় প্রকাশ না করে।’’[12]\n\n"
                        + "[1] (বুখারী ১৯২৭, মুসলিম ১১০৬, আবূ দাঊদ ২৩৮২, তিরমিযী ৭২৯, ইবনে আবী শাইবাহ, মুসান্নাফ ৯৩৯২নং)\n\n"
                        + "[2] (মুসলিম ১১০৬নং)\n\n"
                        + "[3] (আবূ দাঊদ ২৩৮৩, ইবনে আবী শাইবাহ, মুসান্নাফ ৯৩৯০নং)\n\n"
                        + "[4] (আবূ দাঊদ ২৩৮৪, ইবনে আবী শাইবাহ, মুসান্নাফ ৯৩৯৭নং)\n\n"
                        + "[5] (মুসলিম ১১০৮নং)\n\n"
                        + "[6] (মুসলিম ১১০৭নং)\n\n"
                        + "[7] (আহমাদ, মুসনাদ ১/২১, ৫২, সহীহ আবূ দাঊদ ২০৮৯, দারেমী, সুনান ১৬৭৫, ইবনে আবী শাইবাহ, মুসান্নাফ ৯৪০৬নং)\n\n"
                        + "[8] (সহীহ আবূ দাঊদ ২০৯০নং)\n\n"
                        + "[9] (আশ্শারহুল মুমতে’ ৬/৩৯০, ৪৩২-৪৩৩, ফাসিঃ ৪৮পৃঃ, তাসিঃ ৪৩-৪৪পৃঃ)\n\n"
                        + "[10] (বুখারী ১৯২৭নং দ্রঃ)\n\n"
                        + "[11] (আবূ দাঊদ ২১৪৯, তিরমিযী ২৭৭৮, সহীহ আবূ দাঊদ ১৮৮১নং)\n\n"
                        + "[12] (বুখারী ২৫২৮, মুসলিম ১২৭, দ্রঃ আশ্শারহুল মুমতে’ ৬/৩৯০-৩৯১)",
                "For spouses who have self-restraint and do not fear ejaculation or falling into intercourse, kissing and embracing are permissible, as proven by multiple narrations of the Prophet (peace be upon him).\n\n"
                        + "However, if one fears falling into sin or emission, it becomes prohibited. Emission of prostatic fluid (Madhi) does not invalidate the fast according to the correct scholarly view, but ejaculation breaks the fast.\n\n"
                        + "References: Sahih Bukhari 1927, 2528, Sahih Muslim 1106, Sunan Abi Dawud 2089."
        ));

        // Card 7: দেহের দূষিত রক্ত বহিষ্করণ
        list.add(new HajjHistoryCardItem(
                7,
                "দেহের দূষিত রক্ত বহিষ্করণ",
                "Cupping and Blood Extraction",
                "দেহ থেকে দূষিত রক্ত বের করলে (হিজামা) কারো রোযা নষ্ট হবে না; তবে দুর্বলতার আশঙ্কা থাকলে তা পরিহার করা উত্তম...",
                "Cupping (Hijama) and medical blood extraction do not invalidate the fast according to the soundest opinion...",
                "রোযা অবস্থায় কোন যন্ত্র দ্বারা অথবা যন্ত্র ছাড়াই, পা থেকে অথবা মাথার কোন শিরা থেকে, মুখে করে চুষে অথবা যে কোন প্রকারে দেহ থেকে দূষিত রক্ত বের করলে রোযা নষ্ট হবে কি না, সে নিয়ে উলামাদের মাঝে মতভেদ রয়েছে। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) কর্তৃক উভয় শ্রেণীর বর্ণনা মজুদ রয়েছে। তিনি বলেন, ‘‘দেহ থেকে দূষিত রক্ত যে বের করে তার এবং যার বের করা হয় তারও রোযা নষ্ট হয়ে যায়।’’[1] এ কথাও বর্ণিত আছে যে, তিনি রোযা অবস্থায় নিজ দেহ থেকে দূষিত রক্ত বের করেছেন।[2] আর তিনি বলেছেন, ‘‘যে (অনিচ্ছাকৃত) বমি করে, যার স্বপ্নদোষ হয় এবং যে দেহ থেকে দূষিত রক্ত বের করে, তার রোযা নষ্ট হয় না।’’[3]\n\n"
                        + "পরস্পর বিরোধী উক্ত সকল বর্ণনা দেখে কিছু উলামা মনে করেন যে, দেহ থেকে দূষিত রক্ত বের করলে রোযা নষ্ট হয়ে যাওয়ার হাদীস আজও নাসেখ (কার্যকর) এবং এর বিরোধী সকল হাদীস মনসূখ (রহিত)। পক্ষান্তরে অন্যান্য কিছু সত্য-সন্ধানী গবেষক উলামা মনে করেন যে, বরং প্রথম হাদীসটাই মনসূখ।\n\n"
                        + "দেহ থেকে দূষিত রক্ত বের করলে রোযা নষ্ট হয়ে যাওয়ার হাদীস যে মনসূখ (রহিত) সে ব্যাপারে সাক্ষ্য বহন করে আনাস (রাঃ)-এর হাদীস। তিনি বলেন, ‘শুরু শুরু রোযাদারের জন্য দেহ থেকে দূষিত রক্ত বের করা মকরূহ ছিল। একদা জা’ফর বিন আবী তালেব রোযা অবস্থায় দেহ থেকে দূষিত রক্ত বের করলেন। তা দেখে তিনি বললেন, ‘‘এদের উভয়ের রোযা নষ্ট।’’ অতঃপর পরবর্তীকালে তিনি রোযাদারের জন্য দেহ থেকে দূষিত রক্ত বের করার অনুমতি দিলেন।’ আর সবয়ং আনাস রোযা অবস্থায় দেহ থেকে দূষিত রক্ত বের করতেন।[4]\n\n"
                        + "একদা তাঁকে প্রশ্ন করা হল, ‘আপনারা আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর যুগে কি রোযা অবস্থায় দেহ থেকে দূষিত রক্ত বের করাকে মকরূহ মনে করতেন?’ উত্তরে তিনি বললেন, ‘না। অবশ্য দুর্বল হয়ে পড়ার আশঙ্কা করলে মকরূহ মনে করা হত।’[5]\n\n"
                        + "তদনুরূপ আবূ সাঈদ খুদরী (রাঃ) বলেন, ‘আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) রোযাদারকে স্ত্রী-চুম্বন ও দেহ থেকে দূষিত রক্ত বের করার ব্যাপারে অনুমতি দিয়েছেন।’[6]\n\n"
                        + "ইবনে আববাস (রাঃ) বলেন, ‘(পেটের ভিতরে) কিছু প্রবেশ করলে রোযা ভাঙ্গে, কিছু বাহির হলে নয়।’[7]\n\n"
                        + "উপরোক্ত কিছু বর্ণনায় ‘অনুমতি’ দেওয়ার অর্থই হল যে, প্রথমে সে কাজ অবৈধ ছিল এবং পরে তা বৈধ ঘোষণা করা হয়েছে। অতএব সঠিক মত এই যে, দেহ থেকে দূষিত রক্ত বের করলে কারো রোযা নষ্ট হবে না; যে বের করাবে তার এবং যে বের করে দেবে তারও নয়।[8]\n\n"
                        + "বলা বাহুল্য, যদিও সঠিক মত এই যে, রোযা অবস্থায় দেহ থেকে দূষিত রক্ত বের করলে রোযা নষ্ট হবে না, তবুও উত্তম ও পূর্বসতর্কতামূলক আমল এই যে, রোযাদার তা বর্জন করবে। এর ফলে সে মতভেদের বেড়াজাল থেকে নিষ্কৃতি পাবে, খুন বের করার পর সে দৈহিক দুর্বলতার শিকার হবে না এবং যে ব্যক্তি মুখে টেনে খুন বের করে সে ব্যক্তির গলায় কিছু রক্ত চলে গিয়ে তারও রোযা নষ্ট না হয়ে যায়। অবশ্য একান্ত তা করার দরকার হলে দিনে না করে রাত্রে করবে। আর সেটাই হবে উভয়ের জন্য উত্তম।[9]\n\n"
                        + "[1] (আহমাদ, মুসনাদ, আবূ দাঊদ ২৩৬৭, ইবনে মাজাহ ১৬৮০, দারেমী, সুনান ১৬৮১-১৬৮২, ইবনে খুযাইমাহ, সহীহ ১৯৬২-১৯৬৩নং, ইবনে হিববান, সহীহ, হাকেম, মুস্তাদ্রাক ১/৪২৭, বাইহাকী ৪/২৬৫, প্রমুখ)\n\n"
                        + "[2] (বুখারী ১৯৩৮-১৯৩৯, আবূ দাউদ ২৩৭২, তিরমিযী, ইবনে আবী শাইবাহ, মুসান্নাফ, বাইহাকী ৪/২৬৩)\n\n"
                        + "[3] (আবূ দাঊদ, সহীহুল জামেইস সাগীর, আলবানী ৭৭৪২নং)\n\n"
                        + "[4] (দারাকুত্বনী, সুনান ২৩৯নং, বাইহাকী ৪/২৬৮)\n\n"
                        + "[5] (বুখারী : ১৯৪০নং)\n\n"
                        + "[6] (ত্ববারানী, দারাকুত্বনী, ইরওয়াউল গালীল ৪/৭৪)\n\n"
                        + "[7] (ইবনু আবী শাইবাহ ৯৩১৯নং, ইরওয়াউল গালীল ৪/৭৫)\n\n"
                        + "[8] (দ্রঃ মুহাল্লা ৬/২০৪-২০৫, ইরওয়াউল গালীল ৪/৭৪)\n\n"
                        + "[9] (দ্রঃ আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ১৩৬পৃঃ, সাবঊনা মাসআলাহ ফিস্-সিয়াম, মুহাম্মাদ বিন সালেহ আল-মুনাজ্জিদ ৫৬নং)",
                "Scholars have discussed whether cupping (Hijama) breaks the fast. The established authentic view is that cupping does not invalidate the fast, as the Prophet (peace be upon him) underwent cupping while fasting, and the earlier prohibition was abrogated.\n\n"
                        + "However, if it causes physical weakness, it is disliked during daytime and best performed at night.\n\n"
                        + "References: Sahih Bukhari 1938-1940, Sunan al-Daraqutni 239, Irwa al-Ghaleel 4/74."
        ));

        // Card 8: নাক অথবা কোন কাটা-ফাটা থেকে রক্ত বের হওয়া
        list.add(new HajjHistoryCardItem(
                8,
                "নাক অথবা কোন কাটা-ফাটা থেকে রক্ত বের হওয়া",
                "Nosebleeds or Bleeding from Wounds",
                "দেহের কোন কাটা-ফাটা অঙ্গ থেকে রক্ত পড়লে রোযা নষ্ট হয় না। অনুরূপ নাক থেকে রক্ত পড়লেও রোযা নষ্ট নয়...",
                "Involuntary bleeding from a wound, cut, or nosebleed does not invalidate the fast...",
                "দেহের কোন কাটা-ফাটা অঙ্গ থেকে রক্ত পড়লে রোযা নষ্ট হয় না। বরং তা দেহ থেকে দূষিত রক্ত বের করার মতই। অনুরূপ নাক থেকে রক্ত পড়লেও রোযা নষ্ট নয়। কারণ, তাতে মানুষের কোন এখতিয়ার থাকে না। আর ইচ্ছা করে বের করলে তাও দেহ থেকে দূষিত রক্ত বের করার মত।[1] তদনুরূপ মাথায় বা দেহের অন্য কোন জায়গায় পাথর বা অন্য কিছুর আঘাত লেগে রক্ত ঝরলে রোযা নষ্ট হয় না।[2]\n\n"
                        + "[1] (দ্রঃ আহকামুস সাওম ১৩৬-১৩৮পৃঃ)\n\n"
                        + "[2] (মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ ৩০/১২৬)",
                "Bleeding from wounds, injuries, surgery, or sudden nosebleeds does not invalidate the fast because it is involuntary and outside human control.\n\n"
                        + "References: Ahkam as-Sawm 136-138, Majallat al-Buhuth al-Islamiyyah 30/126."
        ));

        // Card 9: রক্তদান করা
        list.add(new HajjHistoryCardItem(
                9,
                "রক্তদান করা",
                "Donating Blood or Blood Tests",
                "পরীক্ষার জন্য কিছু রক্ত দেওয়া এবং রোগীর প্রাণ বাঁচানোর উদ্দেশ্যে রক্তদান করা রোযাদারের জন্য বৈধ...",
                "Giving blood samples for diagnostic tests or donating blood to save a life is permissible...",
                "পরীক্ষার জন্য কিছু রক্ত দেওয়া রোযাদারের জন্য বৈধ। এতে তার রোযার কোন ক্ষতি হয় না।[1]\n\n"
                        + "তদনুরূপ কোন রোগীর প্রাণ বাঁচানোর উদ্দেশ্যে রক্তদান করাও বৈধ এবং তা দেহ থেকে দূষিত রক্ত বের করার মতই। এতেও রোযার কোন ক্ষতি হয় না।[2]\n\n"
                        + "[1] (রিসালাতানি মু’জাযাতানি ফিয যাকাতি অস্সিয়াম ২৪পৃঃ, ফাসিঃ মুসনিদ ৫৩পৃঃ, ফাতাওয়া মুহিম্মাহ, তাতাআল্লাকু বিস্সিয়াম ৩৪পৃঃ)\n\n"
                        + "[2] (দ্রঃ আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ১৩৮পৃঃ)",
                "Giving blood for medical lab tests or donating blood to save an emergency patient is permissible and does not invalidate the fast.\n\n"
                        + "References: Risalatani Mu'jazatani 24, Ahkam as-Sawm 138."
        ));

        // Card 10: দাঁত তোলা
        list.add(new HajjHistoryCardItem(
                10,
                "দাঁত তোলা",
                "Dental Treatment & Tooth Extraction",
                "রোযাদারের জন্য দাঁত পরিষ্কার করা, ডাক্তারী ভরণ ব্যবহার করা এবং যন্ত্রণায় দাঁত তুলে ফেলা বৈধ...",
                "Cleaning teeth, dental fillings, and extracting painful teeth are permissible...",
                "রোযাদারের জন্য দাঁত (স্টোন ইত্যাদি থেকে) পরিষ্কার করা, ডাক্তারী ভরণ (ইনলেই) ব্যবহার করা এবং যন্ত্রণায় দাঁত তুলে ফেলা বৈধ। তবে এ সব ক্ষেত্রে তাকে একান্ত সাবধানতা অবলম্বন করা উচিৎ, যাতে কোন প্রকার ওষুধ বা রক্ত গিলা না যায়।[1]\n\n"
                        + "[1] (ইবনে বায, ফাতাওয়া মুহিম্মাহ, তাতাআল্লাকু বিস্সিয়াম ২৯পৃঃ)",
                "Undergoing dental procedures such as plaque removal, cavity fillings, and tooth extraction is permissible provided no blood or medication is swallowed.\n\n"
                        + "Reference: Fatawa Muhimmah Tata'allaqu bis-Siyam 29."
        ));

        // Card 11: কিড্নী (বৃক্ক বা মূত্রগ্রন্থি) অচল অবস্থায় দেহের রক্ত শোধন
        list.add(new HajjHistoryCardItem(
                11,
                "কিড্নী অচল অবস্থায় দেহের রক্ত শোধন",
                "Kidney Dialysis During Fasting",
                "রোযাদারের কিড্নী অচল হলে রোযা অবস্থায় প্রয়োজনে দেহের রক্ত পরিষ্কার ও শোধন করা বৈধ...",
                "Undergoing kidney dialysis to purify blood does not invalidate the fast...",
                "রোযাদারের কিড্নী অচল হলে রোযা অবস্থায় প্রয়োজনে দেহের রক্ত পরিষ্কার ও শোধন (Dialysis) করা বৈধ। পরিশুদ্ধ করার পর পুনরায় দেহে ফিরিয়ে দিতে যদিও রক্ত দেহ থেকে বের হয়, তবুও তাতে রোযার কোন ক্ষতি হবে না।[1]\n\n"
                        + "[1] (ইবনে উষাইমীন, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৪২নং)",
                "When a patient suffers kidney failure, undergoing blood dialysis to cleanse impurities and return the blood into the body does not invalidate the fast.\n\n"
                        + "Reference: Sab'una Mas'alah fis-Siyam 42."
        ));

        // Card 12: আহারের কাজ দেয় না এমন (ওষুধ) ইঞ্জেকশন ব্যবহার করা
        list.add(new HajjHistoryCardItem(
                12,
                "আহারের কাজ দেয় না এমন ইঞ্জেকশন ব্যবহার করা",
                "Using Non-Nutritive Medical Injections",
                "রোযাদারের জন্য চিকিৎসার ক্ষেত্রে সেই ইঞ্জেকশন ব্যবহার করা বৈধ, যা পানাহারের কাজ করে না; যেমন পেনিসিলিন, ইনসুলিন বা অ্যান্টিবায়োটিক...",
                "Medical injections that provide no nutrition, such as insulin or penicillin, do not break the fast...",
                "রোযাদারের জন্য চিকিৎসার ক্ষেত্রে সেই ইঞ্জেকশন ব্যবহার করা বৈধ, যা পানাহারের কাজ করে না। যেমন, পেনিসিলিন বা ইন্সুলিন ইঞ্জেকশন অথবা অ্যান্টিবায়োটিক বা টনিক কিংবা ভিটামিন ইঞ্জেকশন অথবা ভ্যাক্সিন ইঞ্জেকশন প্রভৃতি হাতে, কোমরে বা অন্য জায়গায়, দেহের পেশী অথবা শিরায় ব্যবহার করলে রোযার ক্ষতি হয় না। তবুও নিতান্ত জরুরী না হলে তা দিনে ব্যবহার না করে রাত্রে ব্যবহার করাই উত্তম ও পূর্বসাবধানতামূলক কর্ম। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে বিষয়ে সন্দেহ আছে সে বিষয় বর্জন করে তাই কর যাতে সন্দেহ নেই।’’[1] ‘‘সুতরাং যে সন্দিহান বিষয়াবলী থেকে দূরে থাকবে, সে তার দ্বীন ও ইজ্জতকে বাঁচিয়ে নেবে।’’[2]\n\n"
                        + "[1] (আহমাদ, মুসনাদ, তিরমিযী ২৫১৮, নাসাঈ, ইবনে হিববান, সহীহ, ত্বাবারানী, মু’জাম প্রমুখ, ইরওয়াউল গালীল, আলবানী ২০৭৪, সহীহুল জামেইস সাগীর, আলবানী ৩৩৭৭, ৩৩৭৮নং)\n\n"
                        + "[2] (আহমাদ, মুসনাদ ৪/২৬৯, ২৭০, বুখারী ৫২, মুসলিম ১৫৯৯নং, আবূ দাঊদ, তিরমিযী, ইবনে মাজাহ, দারেমী) (দ্রঃ রিসালাতানি মু’জাযাতানি ফিয যাকাতি অস্সিয়াম ২৪পৃঃ, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৪২নং)",
                "Therapeutic intramuscular or intravenous injections that do not serve as food or nourishment (such as insulin, penicillin, antibiotics, vaccines) do not invalidate the fast. Nutritive glucose or saline drips, however, nullify the fast.\n\n"
                        + "References: Sahih Bukhari 52, Sahih Muslim 1599, Jami' at-Tirmidhi 2518."
        ));

        // Card 13: ক্ষতস্থানে ওষুধ ব্যবহার
        list.add(new HajjHistoryCardItem(
                13,
                " ক্ষতস্থানে ওষুধ ব্যবহার",
                "Applying Medicine to Wounds",
                "রোযাদারের জন্য নিজ দেহের ক্ষতস্থানে ওষুধ দিয়ে ব্যান্ডেজ ইত্যাদি করা দূষণীয় নয়...",
                "Applying antiseptic medicine and bandages to wounds is completely permissible...",
                "রোযাদারের জন্য নিজ দেহের ক্ষতস্থানে ওষুধ দিয়ে ব্যান্ডেজ ইত্যাদি করা দূষণীয় নয়। তাতে সে ক্ষত গভীর হোক অথবা অগভীর। কারণ, এ কাজকে না কিছু খাওয়া বলা যাবে, আর না পান করা। তা ছাড়া ক্ষতস্থান স্বাভাবিক পানাহারের পথ নয়।[1]\n\n"
                        + "[1] (আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ১৪০পৃঃ)",
                "Applying ointments, medicine, and surgical dressing to superficial or deep body wounds does not break the fast because wounds are not pathways for food or beverage.\n\n"
                        + "Reference: Ahkam as-Sawm wal-I'tikaf 140."
        ));

        // Card 14: মাথা ইত্যাদি নেড়া করা
        list.add(new HajjHistoryCardItem(
                14,
                "মাথা ইত্যাদি নেড়া করা",
                "Shaving Head and Trimming Hair",
                "রোযাদারের জন্য নিজ মাথার চুল বা নাভির নিচের লোম ইত্যাদি চাঁছা বৈধ...",
                "Shaving the head or trimming body hair is permissible while fasting...",
                "রোযাদারের জন্য নিজ মাথার চুল বা নাভির নিচের লোম ইত্যাদি চাঁছা বৈধ। তাতে যদি কোন স্থান কেটে রক্ত পড়লেও রোযার কোন ক্ষতি হয় না। পক্ষান্তরে দাড়ি চাঁছা সব সময়কার জন্য হারাম; রোযা অবস্থায় অথবা অন্য কোন অবস্থায়।[1]\n\n"
                        + "[1] (মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ ১৯/১৬৫)",
                "Shaving or trimming hair on the head or hygienic hair is permissible while fasting and does not affect the fast even if minor nick bleeding occurs.\n\n"
                        + "Reference: Majallat al-Buhuth al-Islamiyyah 19/165."
        ));

        // Card 15: কুল্লি করা ও নাকে পানি নেওয়া
        list.add(new HajjHistoryCardItem(
                15,
                "কুল্লি করা ও নাকে পানি নেওয়া",
                "Rinsing Mouth and Nose",
                "রোযাদারের ঠোঁট শুকিয়ে গেলে পানি দ্বারা ভিজিয়ে নেওয়া এবং মুখ বা জিভ শুকিয়ে গেলে কুল্লি করা বৈধ...",
                "Rinsing the mouth and sniffing water into the nose during ablution are permissible...",
                "রোযাদারের ঠোঁট শুকিয়ে গেলে পানি দ্বারা ভিজিয়ে নেওয়া এবং মুখ বা জিভ শুকিয়ে গেলে কুল্লি করা বৈধ। অবশ্য গড়গড়া করা বৈধ নয়। আর এ ক্ষেত্রে মুখ থেকে পানি বের করে দেওয়ার পর ভিতরে পানির যে আর্দ্রতা বা স্বাদ থেকে যাবে, তাতে রোযার কোন ক্ষতি হবে না। কেননা, তা থেকে বাঁচা সম্ভব নয়।[1]\n\n"
                        + "অতি প্রয়োজনে গড়গড়ার ওষুধ ব্যবহার করা বৈধ। তবে শর্ত হল, যেন কোন প্রকারে পানি বা ওষুধ গলার নিচে নেমে না যায়। (নচেৎ, তাতে রোযা নষ্ট হয়ে যাবে।) তাই পূর্বসতর্কতামূলক আমল হল, তা দিনে ব্যবহার না করে রাতে করা।[2]\n\n"
                        + "নাকে পানি টেনে নিয়ে নাক ঝাড়াও রোযাদারের জন্য বৈধ। অবশ্য তাতে অতিরঞ্জন করা যাবে না। কারণ, তাতে গলার নিচে পানি নেমে যাওয়ার আশঙ্কা থাকে। মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘(ওযূ করার সময়) তুমি নাকে খুব অতিরঞ্জিতভাবে পানি টেনে নিও। কিন্তু তোমার রোযা থাকলে নয়।’’[3]\n\n"
                        + "অবশ্য ওযূ ইত্যাদি করার সময় কুল্লি করতে গিয়ে বা নাকি পানি নিতে গিয়ে সাবধানতা সত্ত্বেও যদি অনিচ্ছাকৃতভাবে গলার নিচে চলে যায়, তাহলে তাতে রোযা ভাঙ্গবে না। কেননা, তা ইচ্ছা করে গিলা হয় না। আর মহান আল্লাহ বলেন,\n\n"
                        + "(وَلَكِنْ مَّا تَعَمَّدَتْ قُلُوْبُكُمْ)\n\n"
                        + "অর্থাৎ, (কোন ব্যাপারে তোমরা ভুল করলে তোমাদের কোন অপরাধ নেই;) কিন্তু ইচ্ছাকৃত করলে অপরাধ আছে। (কুরআনুল কারীম ৩৩/৫)[4]\n\n"
                        + "[1] (ইতহাফু আহলিল ইসলাম বিআহকামিস সিয়াম, জারুল্লাহ আলি জারুল্লাহঃ ২৪পৃঃ, মাসআলাহ ফিস্-সিয়াম, মুহাম্মাদ বিন সালেহ আল-মুনাজ্জিদ ৫৩নং)\n\n"
                        + "[2] (ফাসিঃ জিরাইসী ২১পৃঃ)\n\n"
                        + "[3] (আহমাদ, মুসনাদ ৪/৩৩, আবূ দাঊদ ১৪২, সহীহ তিরমিযী, আলবানী ৬৩১, সহীহ নাসাঈ, আলবানী ৮৫, ইবনে মাজাহ ৪০৭নং)\n\n"
                        + "[4] (ইবনে উষাইমীন, ফাসিঃ মুসনিদ ৩৮পৃঃ)",
                "Moistening dry lips with water and gently rinsing the mouth or nose during ablution are permissible. Deep gargling or exaggerated nasal inhalation is prohibited while fasting. Involuntary swallowing of water without intention does not break the fast.\n\n"
                        + "References: Surah Al-Ahzab: 5, Sunan Abi Dawud 142, Jami' at-Tirmidhi 631."
        ));

        // Card 16: সুগন্ধির সুঘ্রাণ নেওয়া
        list.add(new HajjHistoryCardItem(
                16,
                "সুগন্ধির সুঘ্রাণ নেওয়া",
                "Applying and Inhaling Perfumes",
                "রোযা রাখা অবস্থায় আতর বা অন্য প্রকার সুগন্ধি ব্যবহার করা এবং সর্বপ্রকার সুঘ্রাণ নাকে নেওয়া রোযাদারের জন্য বৈধ...",
                "Applying perfumes and inhaling pleasant scents are permissible while fasting...",
                "রোযা রাখা অবস্থায় আতর বা অন্য প্রকার সুগন্ধি ব্যবহার করা এবং সর্বপ্রকার সুঘ্রাণ নাকে নেওয়া রোযাদারের জন্য বৈধ। তবে ধুঁয়া জাতীয় সুগন্ধি (যেমন আগরবাতি, চন্দন-ধুঁয়া প্রভৃতি) ইচ্ছাকৃত নাকে নেওয়া বৈধ নয়। কারণ, এই শ্রেণীর সুগন্ধির ঘনত্ব আছে; যা পাকস্থলিতে গিয়ে পৌঁছে।[1]\n\n"
                        + "বলা বাহুল্য, রান্নাঘরের যে ধোঁয়া অনিচ্ছা সত্ত্বেও নাকে এসে প্রবেশ করে, তাতে রোযার কোন ক্ষতি হবে না। কারণ, তা থেকে বাঁচার উপায় নেই।[2]\n\n"
                        + "প্রকাশ থাকে যে নস্যি ব্যবহার করলে রোযা নষ্ট হয়ে যাবে। কারণ, তারও ঘনত্ব আছে এবং তার গুঁড়া পেটের ভিতরে পৌঁছে থাকে। তা ছাড়া তা মাদকদ্রব্যের শ্রেণীভুক্ত হলে ব্যবহার করা যে কোন সময়ে এমনিতেই হারাম।\n\n"
                        + "[1] (দ্রঃ ফাতাওয়া ইসলামিয়্যাহ ২/১২৮, ফাসিঃ মুসনিদ ৪৩পৃঃ, তাযকীরু ইবাদির রাহমান, ফীমা অরাদা বিসিয়ামি শাহরি রামাযান ৪৭পৃঃ)\n\n"
                        + "[2] (ইবনে উষাইমীন, মাজমূ’ ফাতাওয়া, ইবনে তাইমিয়্যাহ ১/৫০৮)",
                "Applying Attar, floral scents, and perfumes is permissible. However, inhaling incense smoke (Bukhoor) deliberately is forbidden because smoke particles have mass that reaches the stomach.\n\n"
                        + "Involuntary cooking smoke does not invalidate the fast.\n\n"
                        + "References: Fatawa Islamiyyah 2/128, Majmu' Fatawa Ibn Taymiyyah 1/508."
        ));

        // Card 17: নাকে বা মুখে স্প্রে ব্যবহার
        list.add(new HajjHistoryCardItem(
                17,
                "নাকে বা মুখে স্প্রে ব্যবহার",
                "Using Inhalers and Sprays",
                "বাষ্প বা গ্যাস জাতীয় হাঁফানির ইনহেলার স্প্রে রোযা নষ্ট করে না, কারণ তা পাকস্থলীতে পৌঁছে না...",
                "Using pressurized vapor or inhaler sprays for asthma does not break the fast...",
                "স্প্রে দুই প্রকার; প্রথম প্রকার হল ক্যাপসুল স্প্রে পাওডার জাতীয়। যা পিস্তলের মত কোন পাত্রে রেখে পুশ করে স্প্রে করা হয় এবং ধূলোর মত উড়ে গিয়ে গলায় পৌঁছলে রোগী তা গিলতে থাকে। এই প্রকার স্প্রেতে রোযা নষ্ট হয়ে যাবে। রোযাদারকে যদি এমন স্প্রে বছরের সব মাসে এবং দিনেও ব্যবহার করতেই হয়, তাহলে তাকে এমন রোগী গণ্য করা হবে, যার রোগ সারার কোন আশা নেই। সুতরাং সে রোযা না রেখে প্রত্যেক দিনের বিনিময়ে একটি করে মিসকীন খাইয়ে দেবে।\n\n"
                        + "দ্বিতীয় প্রকার স্প্রে হল বাষ্প জাতীয়। এই প্রকার স্প্রেতে রোযা ভাঙ্গবে না। কেননা, তা পাকস্থলীতে পৌঁছে না।[1] কারণ, তা হল এক প্রকার কমপ্রেস্ড্ গ্যাস; যার ডিববায় প্রেসার পড়লে উড়ে গিয়ে (নিঃশ্বাসের বাতাসের সাথে) ফুসফুসে পৌঁছে এবং শ্বাসকষ্ট দূর করে। এমন গ্যাস কোন প্রকার খাদ্য নয়। আর রমাযান অরমাযান এবং দিনে রাতে সব সময়ে (বিশেষ করে শ্বাসরোধ বা শ্বাসকষ্ট জাতীয় যেমন হাঁফানির রোগী) এর মুখাপেক্ষী থাকে।[2]\n\n"
                        + "অনুরূপভাবে মুখের দুর্গন্ধ দূরীকরণের উদ্দেশ্যে ব্যবহার্য স্প্রে রোযাদারের জন্য ব্যবহার করা দোষাবহ নয়। তবে শর্ত হল, সে স্প্রে পবিত্র ও হালাল হতে হবে।[3]\n\n"
                        + "[1] (ইবনে উষাইমীন, ক্যাসেটঃ আহকামুন মিনাস সিয়াম)\n\n"
                        + "[2] (ইবনে বায, ফাতাওয়া মুহিম্মাহ, তাতাআল্লাকু বিস্সিয়াম ৩৬পৃঃ, সামানিয়া ওয়া আরবাঊন সুআলান ফিস্-সিয়াম ৬২পৃঃ, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৪২নং)\n\n"
                        + "[3] (মাজাল্লাতুল বুহূসিল ইসলামিয়্যাহ ৩০/১১২)",
                "Medical inhalers fall into two categories: powder capsules swallowed into the throat nullify the fast; pressurized aerosol gas for asthma reaches the respiratory lungs and does not break the fast because it is not nourishment.\n\n"
                        + "References: Fatawa Muhimmah 36, Sab'una Mas'alah 42."
        ));

        // Card 18: থুথু ও গয়ের
        list.add(new HajjHistoryCardItem(
                18,
                "থুথু ও গয়ের",
                "Swallowing Saliva and Phlegm",
                "মুখের স্বাভাবিক লালা বা থুথু গিলাতে কোন ক্ষতি নেই; যা থেকে বাঁচা দুঃসাধ্য তাতে রোযা নষ্ট নয়...",
                "Swallowing natural saliva does not invalidate the fast because it cannot be avoided...",
                "যা থেকে বাঁচা দুঃসাধ্য তাতে রোযা নষ্ট নয়\n\n"
                        + "থুথু ও গয়ের থেকে বাঁচা দুঃসাধ্য। কারণ, তা মুখে বা গলার গোড়ায় জমা হয়ে নিচে এমনিতেই চলে যায়। অতএব এতে রোযা নষ্ট হবে না এবং বারবার থুথু ফেলারও দরকার হবে না।\n\n"
                        + "অবশ্য যে কফ, গয়ের, খাঁকার বা শ্লেমা বেশী মোটা এবং যা কখনো মানুষের বুক (শ্বাসযন্ত্র) থেকে, আবার কখনো মাথা (Sinuses) থেকে বের হয়ে আসে, তা গলা ঝেড়ে বের করে বাইরে ফেলা ওয়াজেব এবং তা গিলে ফেলা বৈধ নয়। যেহেতু তা ঘৃণিত; সম্ভবতঃ তাতে শরীর থেকে বেরিয়ে আসা কোন রোগজীবাণুও থাকতে পারে। সুতরাং তা গিলে ফেলাতে স্বাস্থ্যের ক্ষতিও হতে পারে। তবে যদি কেউ ফেলতে না পেরে গিলেই ফেলে, তাহলে তাতে রোযা নষ্ট হবে না।\n\n"
                        + "পক্ষান্তরে মুখের ভিতরকার স্বাভাবিক লালা গিলাতে কোন ক্ষতি নেই। রোযাতেও কোন প্রভাব পড়ে না।[1] এই লালা বের করে ফেলা জরুরী নয়; এমনকি ফজরের আযানের সামান্য পূর্বে পানি পান করার পরেও নয়। কারণ, আমাদের জানা মতে সাহাবাবর্গ কর্তৃক এমন কোন নির্দেশ বর্ণিত হয় নি, যাতে বুঝা যায় যে, রোযাদার ফজর উদয় (সেহরীর সময় শেষ) হওয়ার একটু পূর্বে পানি পান করলে ততক্ষণ পর্যন্ত থুথু ফেলতে হবে, যতক্ষণ পর্যন্ত জিব থেকে পানির স্বাদ দূরীভূত না হয়েছে। বরং এতটুকু অবশ্যই ক্ষমার্হ। তবে হ্যাঁ, যদি কোন খাবারের স্বাদ; যেমন খেজুর, চা বা অনুরূপ কোন মিষ্টি জাতীয় খাবারের মিষ্টতা জিবে অবশিষ্ট থেকে যায়, তাহলে তা অবশ্যই থুথু ফেলার সাথে (বা পানি দ্বারা কুল্লি করে) দূর করা জরুরী এবং সেহরীর সময় শেষ হয়ে গেছে জানার পর তা গিলা বৈধ নয়।\n\n"
                        + "দাঁতে লেগে থাকা মাংস বা অন্য কোন খাবার ফজর উদয় হওয়ার পরে অনিচ্ছাকৃতভাবে গিলে ফেললে, অথবা তা অতি সামান্য হওয়ার ফলে মুখে বুঝতে পারা এবং বের করে ফেলা সম্ভব না হলে তা মুখের স্বাভাবিক লালার মতই। তাতে রোযার কোন ক্ষতি হবে না। কিন্তু বেশী হলে এবং তা বের করে ফেলা সম্ভব হলে, বের করে দিলে আর কোন ক্ষতি হবে না। পরন্তু তা ইচ্ছাকৃতভাবে গিলে ফেললে রোযা নষ্ট হয়ে যাবে।[2]\n\n"
                        + "[1] (আশ্শারহুল মুমতে’ ৬/৪২৮-৪২৯, ফাতাওয়া ইসলামিয়্যাহ ২/১২৫, ফাসিঃ ৩৮পৃঃ)\n\n"
                        + "[2] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৫৩নং)",
                "Swallowing normal saliva does not invalidate the fast. Thick mucus from sinuses or the chest should be spat out when possible. Small residual food bits stuck between teeth that are swallowed without intention do not break the fast.\n\n"
                        + "References: Ash-Sharh al-Mumti' 6/428-429, Fatawa Islamiyyah 2/125."
        ));

        // Card 19: রাস্তার ধূলা
        list.add(new HajjHistoryCardItem(
                19,
                "রাস্তার ধূলা",
                "Inhaling Road Dust or Flour",
                "রাস্তার ধূলা রোযাদারের নিঃশ্বাসের সাথে পেটে গেলে রোযার কোন ক্ষতি হয় না...",
                "Involuntary inhalation of street dust or flour dust does not harm the fast...",
                "রাস্তার ধূলা রোযাদারের নিঃশ্বাসের সাথে পেটে গেলে রোযার কোন ক্ষতি হয় না। তদনুরূপ যে ব্যক্তি আটাচাকিতে কাজ করে অথবা তার কাছে যায় সে ব্যক্তির পেটে আটার গুঁড়ো গেলেও রোযার কোন ক্ষতি হবে না।[1] কারণ, এ সব থেকে বাঁচার উপায় নেই। অবশ্য মুখে মুখোশ ব্যবহার করে বা কাপড় বেঁধে কাজ করাই উত্তম।\n\n"
                        + "[1] (আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ১৪৫-১৪৬পৃঃ)",
                "Inhaling road dust or airborne flour dust in a bakery does not invalidate the fast because it is unavoidable.\n\n"
                        + "Reference: Ahkam as-Sawm wal-I'tikaf 145-146."
        ));

        // Card 20: লবণ বা মিষ্টি চাখা
        list.add(new HajjHistoryCardItem(
                20,
                "লবণ বা মিষ্টি চাখা",
                "Tasting Food for Salt or Seasoning",
                "রান্না করতে করতে প্রয়োজনে খাবারের লবণ বা মিষ্টি সঠিক হয়েছে কি না তা চেখে দেখা রোযাদারের জন্য বৈধ...",
                "Tasting food with the tip of the tongue when necessary and spitting it out is permissible...",
                "রোযা অবস্থায় যা করা চলে\n\n"
                        + "এমন কিছু কাজ আছে যা আপাতদৃষ্টিতে রোযাদারের জন্য করা অবৈধ মনে হলেও আসলে তা বৈধ। সেরূপ কিছু কাজ নিম্নরূপঃ-\n\n"
                        + "রান্না করতে করতে প্রয়োজনে খাবারের লবণ বা মিষ্টি সঠিক হয়েছে কি না তা চেখে দেখা রোযাদারের জন্য বৈধ। তদনুরূপ কোন কিছু কেনার সময় চেখে পরীক্ষা করার দরকার হলে তা করতে পারে। ইবনে আববাস (রাঃ) বলেন, ‘কোন খাদ্য, সির্কা এবং কোন কিছু কিনতে হলে তা চেখে দেখাতে কোন দোষ নেই।’[1]\n\n"
                        + "অনুরূপভাবে অতি প্রয়োজনে মা তার শিশুর জন্য কোন শক্ত খাবার চিবিয়ে নরম করে দিতে পারে, ধান শুকিয়েছে কি না এবং মুড়ির চাল হয়েছে কি না তা চিবিয়ে দেখতে পারে। অবশ্য এ সকল ক্ষেত্রে শর্ত হল, যেন চর্বিত কোন অংশ রোযাদারের পেটে না চলে যায়। বরং অতি সাবধানতার সাথে কেবল দাঁতে চিবিয়ে এবং জিভে তার স্বাদ চেখে সঙ্গে সঙ্গে বাইরে ফেলা জরুরী।[2]\n\n"
                        + "[1] (দ্রঃ বুখারী ৩৮০পৃঃ, ইবনে আবী শাইবাহ, মুসান্নাফ ২/৩০৫, বাইহাকী ৪/২৬১, ইরওয়াউল গালীল, আলবানী ৯৩৭নং)\n\n"
                        + "[2] (ফাতাওয়া ইসলামিয়্যাহ ২/১২৮, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৫৩নং, সাওমু রামাযান, আব্দুর রাযযাক নাওফাল ২৬পৃঃ, ফিকহুস সুন্নাহ ১/৪০৯)",
                "Tasting food on the tip of the tongue to check seasoning or sweetness is permissible for the cook, provided none of it is swallowed and it is spat out immediately. Ibn Abbas said: 'There is no harm in tasting vinegar or food items.'\n\n"
                        + "References: Sahih Bukhari (Ta'leeq), Musannaf Ibn Abi Shaybah 2/305, Irwa al-Ghaleel 937."
        ));

        // Card 21: সেহরীর শেষ সময় পর্যন্ত পানাহার করা
        list.add(new HajjHistoryCardItem(
                21,
                "সেহরীর শেষ সময় পর্যন্ত পানাহার করা",
                "Eating and Drinking Until Dawn",
                "সেহরীর শেষ সময় পর্যন্ত পানাহার ও স্ত্রী-সহবাস করা রোযাদারের জন্য বৈধ...",
                "Eating, drinking, and intimacy are permissible until the arrival of true dawn...",
                "সেহরীর শেষ সময় পর্যন্ত পানাহার ও স্ত্রী-সহবাস করা রোযাদারের জন্য বৈধ। কিন্তু ফজর উদয় (সময় বা আযান) হওয়ার সাথে সাথে মুখের খাবার উগলে ফেলা ওয়াজেব। (এ ব্যাপারে মতভেদ পূর্বে আলোচিত হয়েছে।) অনুরূপ সহবাস করতে থাকলে সঙ্গে সঙ্গে স্বামী-স্ত্রী পৃথক হয়ে যাওয়া জরুরী। এরূপ করলে রোযা শুদ্ধ হয়ে যাবে। পক্ষান্তরে সেহরীর সময় শেষ হয়ে গেছে বা ফজরের আযান শুরু হয়ে গেছে জেনে বা শুনেও যদি কেউ পানাহার বা স্ত্রী-সঙ্গমে মত্ত থাকে, তাহলে তার রোযা হবে না। মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘বিলাল রাতে আযান দেয়। সুতরাং তোমরা ততক্ষণ পর্যন্ত পানাহার করতে থাক, যতক্ষণ পর্যন্ত না ইবনে উম্মে মাকতূম আযান দেয়।’’[1]\n\n"
                        + "[1] (বুখারী ৬১৭নং, মুসলিম)",
                "Eating, drinking, and intimate relations are permissible up until the moment true dawn breaks (Subh Sadiq). Once dawn appears or the true Adhan sounds, one must immediately stop eating and spit out remaining morsels.\n\n"
                        + "References: Sahih Bukhari 617, Sahih Muslim."
        ));

        // Card 22: ফজর উদয় হওয়ার পরেও নাপাক থাকা
        list.add(new HajjHistoryCardItem(
                22,
                "ফজর উদয় হওয়ার পরেও নাপাক থাকা",
                "Waking up in State of Janabah",
                "স্ত্রী-সঙ্গম অথবা স্বপ্নদোষ হওয়ার পরেও সময় অভাবে গোসল না করে নাপাক অবস্থাতেই রোযাদার রোযার নিয়ত করতে পারে...",
                "Waking up in a state of ritual impurity (Janabah) and taking a bath after dawn does not invalidate the fast...",
                "স্ত্রী-সঙ্গম অথবা স্বপ্নদোষ হওয়ার পরেও সময় অভাবে গোসল না করে নাপাক অবস্থাতেই রোযাদার রোযার নিয়ত করতে এবং সেহরী খেতে পারে। এমন কি সেহরীর সময় শেষ হয়ে গেলেও আযানের পর গোসল করতে পারে। এ ক্ষেত্রে রোযার শুরুর কিছু অংশ নাপাকে অতিবাহিত হলেও রোযার কোন ক্ষতি হবে না। অবশ্য নামাযের জন্য গোসল জরুরী।\n\n"
                        + "মা আয়েশা ও উম্মে সালামাহ (রাঃ) বলেন, ‘আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর (কখনো কখনো) স্ত্রী-মিলন করে অপবিত্র অবস্থায় ফজর হয়ে যেত। তারপর তিনি গোসল করতেন এবং রোযা রাখতেন।’[1]\n\n"
                        + "তাছাড়া মহান আল্লাহ ফজর উদয় হওয়ার সময় পর্যন্ত স্ত্রী-মিলনের অনুমতি দিয়েছেন এবং তার পর থেকে রোযা রাখার আদেশ দিয়েছেন। আর তার মানেই হল যে, রোযাদারের জন্য (ফজর উদয়ের পূর্বে) মিলনের পর (ফজর উদয়ের পরে) নাপাকীর গোসল করা বৈধ।[2]\n\n"
                        + "তদনুরূপ নিফাস ও ঋতুমতী মহিলার রাত্রে খুন বন্ধ হলে (রোযার নিয়ত করে এবং সেহরী খেয়ে) ফজরের পর রোযায় থেকে পরে গোসল করে নামায পড়তে পারে। উপর্যুক্ত নাপাক পুরুষ ও মহিলার জন্য নাপাকীর গোসলকে সকাল বা দুপুরের সময় পর্যন্ত বিলম্বিত করা বৈধ নয়। বরং সূর্য উদয়ের পূর্বেই গোসল করে যথাসময়ে নামায আদায় করা তাদের জন্য ওয়াজেব। যেমন পুরুষের জন্য ওয়াজেব এমন সময়ের ভিতরে সত্বর গোসল করা, যাতে ফজরের নামায জামাআত সহকারে মসজিদে আদায় করতে সক্ষম হয়।[3]\n\n"
                        + "জ্ঞাতব্য যে, রমাযানের দিনের বেলায় রোযাদারের স্বপ্নদোষ হয়ে গেলে তার রোযা বাতিল নয়। কেননা, তা তার এখতিয়ারকৃত নয়। অতএব তার জন্য জরুরী হল, নাপাকীর গোসল করা। অবশ্য ফজরের নামায পড়ার পর ঘুমাতে গিয়ে স্বপ্নদোষ হলে, সঙ্গে সঙ্গে গোসল না করে যদি যোহরের আগে পর্যন্ত বিলম্ব করে গোসল করে, তাহলে তাতে দোষ হবে না।[4] অবশ্য উত্তম হল, নাপাকে না থেকে সম্ভব হলে সঙ্গে সঙ্গে গোসল করে বিভিন্নভাবে আল্লাহর যিক্র করা।\n\n"
                        + "[1] (বুখারী ১৯২৫, মুসলিম ১১০৯, সুনানে আরবাআহ; আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ)\n\n"
                        + "[2] (দ্রঃ মুহাল্লা ৬/২২০, ফাইযুর রাহীমির রাহমান, ফী আহকামি অমাওয়াইযি রামাযান ৬১পৃঃ)\n\n"
                        + "[3] (ফিকহুস সুন্নাহ ১/৪১১, ইবনে বায, ফাসিঃ মুসনিদ ৫১পৃঃ, রিসালাতানি মু’জাযাতানি ফিয যাকাতি অস্সিয়াম ২৩পৃঃ)\n\n"
                        + "[4] (ইবনে বায, ফাসিঃ মুসনিদ ৫১ পৃঃ)",
                "Waking up in a state of ritual impurity (Janabah) after intimacy or wet dreams does not invalidate the fast. One can eat Suhoor and perform Ghusl after dawn to pray Fajr on time. Wet dreams during daytime do not break the fast.\n\n"
                        + "References: Sahih Bukhari 1925, Sahih Muslim 1109."
        ));

        // Card 23: দিনে ঘুমানো
        list.add(new HajjHistoryCardItem(
                23,
                "দিনে ঘুমানো",
                "Sleeping During the Day While Fasting",
                "রোযাদারের জন্য দিনে ঘুমানো বৈধ। কিন্তু সকল নামায যথাসময়ে জামাআত সহকারে আদায় করা আবশ্যক...",
                "Sleeping or taking a nap during the day is permissible while fasting...",
                "রোযাদারের জন্য দিনে ঘুমানো বৈধ। কিন্তু সকল নামায তার যথাসময়ে জামাআত সহকারে আদায় করতে অবহেলা প্রদর্শন করা বৈধ নয়। যেমন, বিভিন্ন ইবাদতের কল্যাণ থেকে নিজেকে বঞ্চিত করা উচিৎ নয়। বরং উচিৎ হল, ঘুমিয়ে সময় নষ্ট না করে রমাযানের সেই মাহাত্ম্যপূর্ণ সময়কে নফল নামায, যিক্র-আযকার ও কুরআন কারীম তেলাঅত দ্বারা আবাদ করা। যাতে তার রোযার ভিতরে নানা প্রকার ইবাদতের সমাবেশ ঘটে।[1]\n\n"
                        + "[1] (ইবনে উষাইমীন, ফাসিঃ মুসনিদ ৩১-৩২পৃঃ)",
                "Sleeping during the daytime while fasting is permissible. However, it is not permissible to neglect mandatory congregational prayers or squander the blessed hours of Ramadan in excessive sleep.\n\n"
                        + "Reference: Fatawa Ibn Uthaymeen 31-32."
        ));

        // Card 24: সফর করা
        list.add(new HajjHistoryCardItem(
                24,
                "সফর করা",
                "Traveling While Fasting",
                "রোযাদারের জন্য এমন দেশে সফর করে রোযা রাখা বৈধ, যেখানের দিন ঠান্ডা ও ছোট...",
                "Traveling to cooler regions or countries with shorter days while fasting is permissible...",
                "রোযাদারের জন্য এমন দেশে সফর করে রোযা রাখা বৈধ, যেখানের দিন ঠান্ডা ও ছোট।[1]\n\n"
                        + "[1] (ইবনে উষাইমীন, মাজমূ’ ফাতাওয়া ১/৫০৬)",
                "Traveling to a country where daylight hours are shorter or cooler while continuing to observe the fast is permissible.\n\n"
                        + "Reference: Majmu' Fatawa Ibn Uthaymeen 1/506."
        ));

        return list;
    }
}
