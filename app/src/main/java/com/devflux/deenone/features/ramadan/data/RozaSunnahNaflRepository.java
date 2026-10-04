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
 * Repository for সুন্নত ও নফল রোযা (Sunnah and Voluntary Fasting).
 * Pre-seeded with 12 complete verbatim cards matching user requirements.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaSunnahNaflRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaSunnahNaflRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_sunnah_nafl.php");
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

        // Card 1: সুন্নত ও নফল রোযা
        list.add(new HajjHistoryCardItem(
                1,
                "সুন্নত ও নফল রোযা",
                "Sunnah and Voluntary Fasts",
                "রোযার দ্বিতীয় প্রকার হল সুন্নত, নফল (অতিরিক্ত) রোযা; যা পালন করা মুসলিমের জন্য ওয়াজেব নয়। কিন্তু পালন করলে ফযীলত ও সওয়াবের অধিকারী হওয়া যায়...",
                "The second category of fasting comprises Sunnah and voluntary (Nafl) fasts, which are not obligatory upon a Muslim but carry immense virtue and reward...",
                "রোযার দ্বিতীয় প্রকার হল সুন্নত, নফল (অতিরিক্ত) রোযা; যা পালন করা মুসলিমের জন্য ওয়াজেব নয়। কিন্তু পালন করলে ফযীলত ও সওয়াবের অধিকারী হওয়া যায়।\n\n"
                        + "মহান আল্লাহর একটি হিকমত ও অনুগ্রহ এই যে, তিনি ফরয ইবাদতের মতই নফল ইবাদতও বিধিবদ্ধ করেছেন। তিনি যে আমল ফরয করেছেন, অনুরূপ সেই আমল নফলও করেছেন বান্দার জন্য। ফরয ইবাদতের মাঝে একদিকে যেমন ঘটিত ত্রুটি নফল ইবাদত দ্বারা পূরণ হয়ে যায়। তেমনি অপর দিকে তারই মাধ্যমে আমলকারীর নেকী ও সওয়াব বৃদ্ধি হয়। পক্ষান্তরে তা বিধিবদ্ধ না হলে তা পালন করা ভ্রষ্টকারী বিদআত বলে গণ্য হত। আর হাদীসে বলা হয়েছে যে, ‘‘কিয়ামতের দিন নফল ইবাদত দ্বারা ফরয ইবাদতের অসম্পূর্ণতা পূর্ণ করা হবে।’’[1]\n\n"
                        + "[1] (আহমাদ, মুসনাদ ২/৪২৫, আবূ দাঊদ ৮৬৪নং, তিরমিযী, নাসাঈ, ইবনে মাজাহ, হাকেম, মুস্তাদ্রাক ১/২৬২, সহীহ আবূ দাঊদ ৭৭০নং)",
                "The second category of fasting is Sunnah and voluntary (Nafl) fasting; observing it is not obligatory upon a Muslim, but observing it earns immense virtues and rewards.\n\n"
                        + "It is part of the divine wisdom and grace of Allah the Almighty that He legislated voluntary acts of worship alongside obligatory ones. For every obligation He ordained, He also established a voluntary counterpart for His servants. On one hand, shortcomings occurring in obligatory worship are compensated by voluntary acts. On the other hand, the servant's righteousness and rewards are multiplied. Furthermore, had they not been divinely legislated, performing them would have been deemed straying innovation (Bid'ah). It is stated in the Hadith: ''On the Day of Resurrection, deficiencies in obligatory deeds will be completed through voluntary deeds.''[1]\n\n"
                        + "[1] (Musnad Ahmad 2/425; Sunan Abi Dawud #864; Jami' at-Tirmidhi; Sunan an-Nasa'i; Sunan Ibn Majah; Al-Hakim in Al-Mustadrak 1/262; Sahih Abi Dawud #770)"
        ));

        // Card 2: নফল রোযার জন্য নিয়ত
        list.add(new HajjHistoryCardItem(
                2,
                "নফল রোযার জন্য নিয়ত",
                "Intention for Voluntary Fasts",
                "নফল রোযার নিয়ত ফজরের আগে থেকে হওয়া জরুরী নয়। বরং দিনের বেলায় সূর্য ঢলার আগে বা পরে নিয়ত করলেই রোযা শুদ্ধ হয়ে যায়...",
                "For voluntary fasts, it is not strictly required to make the intention before dawn; making the intention during daytime validates the fast...",
                "নফল রোযার নিয়ত ফজরের আগে থেকে হওয়া জরুরী নয়। বরং দিনের বেলায় সূর্য ঢলার আগে বা পরে নিয়ত করলেই রোযা শুদ্ধ হয়ে যায়। অবশ্য ফরয রোযার বেলায় তা হয় না- যেমন পূর্বেই এ কথা আলোচিত হয়েছে। তবে নফল রোযার ক্ষেত্রেও শর্ত হল, যেন নিয়ত করার আগে ফজর উদয় হওয়ার পর কোন রোযা নষ্টকারী জিনিস ব্যবহার না করা হয়। বলা বাহুল্য, যদি তা (পানাহার বা অন্য কিছু) ব্যবহার করে থাকে তাহলে রোযা হবে না।\n\n"
                        + "এ কথার দলীল মা আয়েশার হাদীস; তিনি বলেন, একদা নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁর নিকট এসে জিজ্ঞাসা করলেন, ‘‘তোমাদের কাছে (খাবার) কিছু আছে কি?’’ বললেন, ‘জী না।’ মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তখন বললেন, ‘‘তাহলে আজকে আমি রোযা থাকলাম।’’[1]\n\n"
                        + "আর এই আমল ছিল সাহাবা (রাযি.)-দের।[2]\n\n"
                        + "কিন্তু জানার কথা যে, দিনের বেলায় নিয়ত করলে, কেবল নিয়ত করার পর থেকেই সওয়াবের অধিকারী হবে। সুতরাং কেউ সূর্য ঢলার সময় নিয়ত করলে সে কেবল অর্ধেক রোযার সওয়াব প্রাপ্ত হবে; তার বেশী নয়।[3] যেহেতু প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যাবতীয় আমল নিয়তের উপর নির্ভরশীল। সুতরাং প্রত্যেক ব্যক্তির তা-ই প্রাপ্য হয়, যার সে নিয়ত করে থাকে।’’[4]\n\n"
                        + "[1] (মুসলিম ১১৫৪নং, প্রমুখ)\n\n"
                        + "[2] (দ্রঃ বুখারী ৩৭৯পৃঃ)\n\n"
                        + "[3] (আশ্শারহুল মুমতে’ ৬/৩৭২-৩৭৪)\n\n"
                        + "[4] (বুখারী ১নং, মুসলিম ১৯০৭নং)",
                "For voluntary fasts, it is not mandatory to make the intention before dawn (Fajr). Rather, making the intention during daytime—whether before or after the sun reaches its zenith—renders the fast valid. This is unlike obligatory fasts, where prior intention before dawn is strictly required, as previously discussed. However, the condition for voluntary fasts remains that after dawn, the person must not have engaged in any act that invalidates fasting. Needless to say, if one has eaten, drunk, or done anything that breaks the fast, the fast will not be valid.\n\n"
                        + "The proof for this is the Hadith of Mother Aisha (may Allah be pleased with her), who narrated that the Prophet (peace and blessings be upon him) once entered her home and asked: ''Do you have anything (to eat)?'' She replied: 'No.' The Prophet (peace and blessings be upon him) said: ''Then I am fasting today.''[1]\n\n"
                        + "This was also the practice of the noble Companions (may Allah be pleased with them).[2]\n\n"
                        + "However, it should be noted that if one makes the intention during daytime, reward is counted only from the moment the intention was made. Therefore, if someone makes the intention around the sun's zenith, he receives the reward of only half a day's fast, not more.[3] For the beloved Prophet (peace and blessings be upon him) said: ''All actions depend upon intentions, and every person will attain only what he intended.''[4]\n\n"
                        + "[1] (Sahih Muslim #1154, and others)\n\n"
                        + "[2] (See Sahih Bukhari, p. 379)\n\n"
                        + "[3] (Ash-Sharh al-Mumti' 6/372-374)\n\n"
                        + "[4] (Sahih Bukhari #1; Sahih Muslim #1907)"
        ));

        // Card 3: শওয়ালের ছয় রোযা
        list.add(new HajjHistoryCardItem(
                3,
                "শওয়ালের ছয় রোযা",
                "Six Fasts of Shawwal",
                "যে ব্যক্তির রমাযানের রোযা পূর্ণ হয়ে যাবে, তার জন্য শওয়াল মাসের ৬টি রোযা রাখা মুস্তাহাব। আর এতে তার জন্য রয়েছে বৃহৎ সওয়াব...",
                "Whoever completes the fasts of Ramadan, it is recommended (Mustahabb) for them to fast six days in the month of Shawwal, earning immense reward...",
                "যে ব্যক্তির রমাযানের রোযা পূর্ণ হয়ে যাবে, তার জন্য শওয়াল মাসের ৬টি রোযা রাখা মুস্তাহাব। আর এতে তার জন্য রয়েছে বৃহৎ সওয়াব। মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি রমাযানের রোযা রাখার পরে-পরেই শওয়াল মাসে ছয়টি রোযা পালন করে সে ব্যক্তির পূর্ণ বৎসরের রোযা রাখার সমতুল্য সওয়াব লাভ হয়।’’[1]\n\n"
                        + "এই সওয়াব এই জন্য হবে যে, আল্লাহর অনুগ্রহে ১টি কাজের সওয়াব ১০টি করে পাওয়া যায়। অতএব সেই ভিত্তিতে ১ মাসের (৩০ দিনের) রোযা ১০ মাসের (৩০০ দিনের) সমান এবং ৬ দিনের রোযা ২ মাসের (৬০ দিনের) সমান; সর্বমোট ১২ মাস (৩৬০ দিন) বা এক বছরের সওয়াব লাভ হয়ে থাকে। আর এই ভাবে সেই রোযাদারের জীবনের প্রত্যেকটি দিন রোযা রাখা হয়! দয়াময় আল্লাহ বলেন,\n\n"
                        + "(مَنْ جَاءَ بِالْحَسَنَةِ فَلَهُ عَشْرُ أَمْثَالِهَا)\n\n"
                        + "অর্থাৎ, কেউ কোন ভাল কাজ করলে, সে তার ১০ গুণ প্রতিদান পাবে। (কুরআনুল কারীম ৬/১৬০)\n\n"
                        + "এই রোযা বিধিবদ্ধ হওয়ার কারণ -আর আল্লাহই ভাল জানেন - তা হল ফরয নামাযের পর সুন্নাতে মুআক্কাদার মত। যা ফরয নামাযের উপকারিতা ও তার অসম্পূর্ণতা সম্পূর্ণ করে। অনুরূপ এই ছয় রোযা রমাযানের ফরয রোযার অসম্পূর্ণতা সম্পূর্ণ করে এবং তাতে কোন ত্রুটি ঘটে থাকলে তা দূর করে থাকে। সে অসম্পূর্ণতা ও ত্রুটির কথা রোযাদার জানতে পারুক অথবা না পারুক।[2]\n\n"
                        + "তা ছাড়া রমাযানের ফরয রোযা রাখার পর পুনরায় রোযা রাখা রমাযানের রোযা কবুল হওয়ার একটি লক্ষণ। যেহেতু মহান আল্লাহ যখন কোন বান্দার নেক আমল কবুল করেন, তখন তার পরেই তাকে আরো নেক আমল করার তওফীক দান করে থাকেন। যেমন উলামাগণ বলে থাকেন, ‘নেক কাজের সওয়াব হল, তার পরে পুনঃ নেক কাজ করা।’[3]\n\n"
                        + "এই রোযার উত্তম সময় হল, ঈদের সরাসরি পরের ৬ দিন। কারণ, তাতেই রয়েছে নেক আমলের দিকে সত্বর ধাবমান হওয়ার দলীল। আর এ কথা মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর এই উক্তি ‘‘যে ব্যক্তি রমাযানের রোযা রাখার পরে-পরেই শওয়াল মাসে ছয়টি রোযা পালন করে---’’ থেকে বুঝা যায়।\n\n"
                        + "তদনুরূপ উত্তম হল, উক্ত ছয় রোযাকে লাগাতার রাখা। কেননা, এমনটি করা রমাযানের অভ্যাস অনুযায়ী সহজসাধ্য। আর তাতে হবে বিধিবদ্ধ নেক আমল করার প্রতি সাগ্রহে ধাবমান হওয়ার পরিচয়।\n\n"
                        + "অবশ্য তা লাগাতার না রেখে বিচ্ছিন্নভাবেও রাখা চলে। যেহেতু হাদীসের অর্থ ব্যাপক। কিন্তু শওয়াল মাস অতিবাহিত হয়ে গেলে তা কাযা করা বিধেয় নয়। যেহেতু তা সুন্নত এবং তার যথাসময় পার হয়ে গেছে। তাতে তা কোন ওযরের ফলে পার হোক অথবা বিনা ওযরে।[4]\n\n"
                        + "রমাযানের রোযা কাযা না করে শওয়ালের রোযা রাখা বিধেয় নয়। যেমন কাফ্ফারার রোযা থাকলে তা না রেখে শওয়ালের রোযা রাখা চলে না। আর শওয়াল মাসে রমাযানের কাযা রাখলে তাই শওয়ালের রোযা বলে যথেষ্ট হবে না।[5]\n\n"
                        + "[1] (মুসলিম ১১৬৪নং, সুনানে আরবাআহ; আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ)\n\n"
                        + "[2] (ফাইযুর রাহীমির রাহমান, ফী আহকামি অমাওয়াইযি রামাযান ৭৬পৃঃ)\n\n"
                        + "[3] (ইতহাফু আহলিল ইসলাম বিআহকামিস সিয়াম ৯২পৃঃ)\n\n"
                        + "[4] (ইবনে বায : ফাতাওয়া ইসলামিয়্যাহ ২/১৬৫-১৬৬)\n\n"
                        + "[5] (ইবনে জিবরীন, ফাসিঃ ১০৭পৃঃ)",
                "Whoever has completed the fasts of Ramadan, it is recommended (Mustahabb) for them to fast six days in the month of Shawwal, which carries immense reward. The Prophet (peace and blessings be upon him) said: ''Whoever fasts Ramadan and follows it with six days of Shawwal, it will be as if he fasted for a lifetime (an entire year).''[1]\n\n"
                        + "This great reward is because, by the grace of Allah, each righteous deed is multiplied tenfold. On this basis, 1 month (30 days) of fasting equals 10 months (300 days), and 6 days of fasting equals 2 months (60 days); together totaling 12 months (360 days) or a complete year. Thus, every single day of the person's life is credited as fasting! The Most Merciful Allah says:\n\n"
                        + "(مَنْ جَاءَ بِالْحَسَنَةِ فَلَهُ عَشْرُ أَمْثَالِهَا)\n\n"
                        + "Meaning: 'Whoever comes with a good deed will have ten times the like thereof.' (Surah Al-An'am 6:160)\n\n"
                        + "The wisdom behind legislating these six fasts—and Allah knows best—is like the confirmed Sunnah (Sunnah Mu'akkadah) prayers after obligatory prayers. Just as they repair deficiencies and enhance the benefits of the obligatory prayer, likewise these six fasts make up for any shortcomings in Ramadan's obligatory fasts and rectify inadvertent flaws, whether the faster was aware of them or not.[2]\n\n"
                        + "Moreover, fasting again after concluding the obligatory fast of Ramadan is a sign of acceptance of Ramadan's fasts. For when Allah accepts a servant's good deed, He grants him the divine enablement (Tawfeeq) to perform further righteous deeds after it. As scholars say: 'The true reward of a righteous deed is the blessing to perform another good deed following it.'[3]\n\n"
                        + "The best time to observe these fasts is the six consecutive days immediately following Eid al-Fitr, as this demonstrates hastening towards righteous deeds. This is inferred from the Prophet's phrasing: ''Whoever fasts Ramadan and follows it immediately with six days of Shawwal...''\n\n"
                        + "Likewise, observing these six fasts consecutively is best, because continuing immediately from Ramadan makes it easier and proves eager devotion to divine worship.\n\n"
                        + "However, fasting them intermittently throughout Shawwal is also permissible, as the Hadith wording is broad. But once Shawwal has passed, making them up (Qadha) is not legislated, because it is a voluntary Sunnah tied to a specific time that has elapsed, whether missed due to an excuse or without one.[4]\n\n"
                        + "It is not permissible to fast the six days of Shawwal before making up missed obligatory fasts of Ramadan. Just as one cannot perform voluntary fasts while having unfulfilled expiation (Kaffarah) fasts, similarly, observing missed Ramadan fasts during Shawwal does not suffice as the six days of Shawwal.[5]\n\n"
                        + "[1] (Sahih Muslim #1164; Sunan Arba'ah: Abu Dawud, At-Tirmidhi, An-Nasa'i, and Ibn Majah)\n\n"
                        + "[2] (Faydul Rahimi ar-Rahman fi Ahkami wa Mawa'idzi Ramadan, p. 76)\n\n"
                        + "[3] (Ithaf Ahlil Islam bi-Ahkamis Siyam, p. 92)\n\n"
                        + "[4] (Ibn Baz, Fatawa Islamiyyah 2/165-166)\n\n"
                        + "[5] (Ibn Jibreen, Fatawa as-Siyam, p. 107)"
        ));

        // Card 4: আরাফার রোযা
        list.add(new HajjHistoryCardItem(
                4,
                "আরাফার রোযা",
                "Fasting on the Day of Arafah",
                "যুল-হজ্জ মাসের ৯ তারীখ (সৌদি আরবের) হল আরাফার দিন। এই দিনে হাজীগণ আরাফার ময়দানে উপস্থিত হন বলে এই নামকরণ হয়েছে...",
                "The 9th of Dhul-Hijjah is the Day of Arafah. Fasting on this day expiates the sins of the preceding year and the coming year...",
                "যুল-হজ্জ মাসের ৯ তারীখ (সৌদি আরবের) হল আরাফার দিন। এই দিনে হাজীগণ আরাফার ময়দানে উপস্থিত হন বলে এই নামকরণ হয়েছে। এই দিনের রোযা রাখার মাহাত্ম্য প্রসঙ্গে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) জিজ্ঞাসিত হলে তিনি বলেছিলেন, ‘‘(উক্ত রোযা) গত এক বছরের এবং আগামী এক বছরের কৃত পাপরাশিকে মোচন করে দেয়।’’[1]\n\n"
                        + "সাহ্ল বিন সা’দ (রাঃ) হতে বর্ণিত, তিনি বলেন আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি আরাফার দিন রোযা রাখে তার উপর্যুপরি দুই বৎসরের পাপরাশি মাফ হয়ে যায়।’’[2]\n\n"
                        + "অবশ্য এই রোযা গৃহবাসীর জন্য বিধেয়; আরাফাতে অবস্থানরত হাজীর জন্য তা বিধেয় নয়। কেননা, ঐ দিনে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) রোযা রেখেছেন কি না লোকেরা তা নিয়ে সন্দেহ করলে, তাঁর নিকট এক পাত্র দুধ পাঠানো হল। তিনি ঐ দিনের চাশ্তের সময় তা পান করলেন। সে সময় লোকেরা তাঁর দিকে তাকিয়ে দেখছিল।[3]\n\n"
                        + "আরাফার ময়দানে ঐ রোযা বিধেয় না হওয়ার কারণ এই যে, ঐ দিন হল দুআ ও যিক্রের দিন। আর রোযা রাখলে তাতে দুর্বলতা দেখা দিতে পারে। তা ছাড়া সেটা হল সফর। আর সফরে রোযা না রাখাটাই উত্তম।[4]\n\n"
                        + "পক্ষান্তরে অহাজীদের জন্য ঐ রোযা বিধেয় হওয়ার পশ্চাতে হিকমত হল, ঐ রোযা রেখে রোযাদার হাজীদের সাদৃশ্য বরণ করতে পারে, তাঁদের কর্মের প্রতি আকাঙ্ক্ষী হয় এবং তাঁদের উপর আল্লাহর যে রহমত অবতীর্ণ হয় তাতে শামিল হতে ও সেই রহমতের দরিয়ায় আপ্লুত হতে পারে।[5]\n\n"
                        + "প্রকাশ থাকে যে, রমাযানের কাযা রোযার নিয়তে কেউ আরাফা অথবা আশূরার দিন রোযা রাখলে তার উভয় সওয়াব লাভ হবে ইন শাআল্লাহ।\n\n"
                        + "[1] (আহমাদ, মুসনাদ ৫/২৯৭, মুসলিম ১১৬২নং, আবূ দাঊদ ২৪২৫নং, তিরমিযী, নাসাঈ, ইবনে মাজাহ, বাইহাকী ৪/২৮৬)\n\n"
                        + "[2] (আবু য়্যা’লা, সহীহ তারগীব ৯৯৮নং)\n\n"
                        + "[3] (বুখারী ১৯৮৮, মুসলিম ১১২৩নং)\n\n"
                        + "[4] (দ্রঃ যামাঃ ২/৭৭, আশ্শারহুল মুমতে’ ৬/৪৭৩)\n\n"
                        + "[5] (ফাইযুর রাহীমির রাহমান, ফী আহকামি অমাওয়াইযি রামাযান ৭৬পৃঃ)",
                "The 9th day of Dhul-Hijjah (according to Saudi Arabia / pilgrimage calendar) is the Day of Arafah. It is named such because pilgrims gather on the plains of Arafat on this day. When the Prophet (peace and blessings be upon him) was asked about the virtues of fasting on this day, he said: ''(This fast) expiates the sins of the preceding year and the coming year.''[1]\n\n"
                        + "Narrated by Sahl ibn Sa'd (may Allah be pleased with him), the Messenger of Allah (peace and blessings be upon him) said: ''Whoever fasts on the Day of Arafah, his sins of two consecutive years are forgiven.''[2]\n\n"
                        + "However, this fast is legislated for non-pilgrims residing in their homes; it is not legislated for pilgrims standing at Arafat. When the people doubted whether the Prophet (peace and blessings be upon him) was fasting on that day at Arafat, a bowl of milk was sent to him, and he drank it during the forenoon (Chasht) while the people watched him.[3]\n\n"
                        + "The reason it is not prescribed at Arafat is that the day is devoted to intense supplication (Dua) and remembrance (Dhikr); fasting could cause weakness and fatigue. Moreover, the pilgrim is a traveler, and not fasting during travel is better.[4]\n\n"
                        + "Conversely, the wisdom of prescribing it for non-pilgrims is that by fasting, they emulate the noble pilgrims, yearn for their righteous deeds, and partake in the divine mercy and blessings descending upon the pilgrims.[5]\n\n"
                        + "It is worth noting that if someone fasts on the Day of Arafah or Ashura with the joint intention of making up a missed Ramadan fast (Qadha), he will attain both rewards In Sha Allah.\n\n"
                        + "[1] (Musnad Ahmad 5/297; Sahih Muslim #1162; Sunan Abi Dawud #2425; Jami' at-Tirmidhi; Sunan an-Nasa'i; Sunan Ibn Majah; Al-Bayhaqi 4/286)\n\n"
                        + "[2] (Abu Ya'la; Sahih at-Targhib #998)\n\n"
                        + "[3] (Sahih Bukhari #1988; Sahih Muslim #1123)\n\n"
                        + "[4] (See Zad al-Ma'ad 2/77; Ash-Sharh al-Mumti' 6/473)\n\n"
                        + "[5] (Faydul Rahimi ar-Rahman fi Ahkami wa Mawa'idzi Ramadan, p. 76)"
        ));

        // Card 5: মুহাররম মাসের রোযা
        list.add(new HajjHistoryCardItem(
                5,
                "মুহাররম মাসের রোযা",
                "Fasting in Muharram and Ashura",
                "সুন্নত রোযাসমূহের মধ্যে মুহার্রাম মাসের (অধিকাংশ দিনের) রোযা অন্যতম। রমাযানের পর পর রয়েছে এই রোযার মান...",
                "Among the Sunnah fasts, fasting most days of Muharram holds great preeminence, especially the 10th of Muharram (Ashura) paired with the 9th...",
                "সুন্নত রোযাসমূহের মধ্যে মুহার্রাম মাসের (অধিকাংশ দিনের) রোযা অন্যতম। রমাযানের পর পর রয়েছে এই রোযার মান। আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘রমাযানের পর সর্বশ্রেষ্ঠ রোযা হল আল্লাহর মাস মুহার্রামের রোযা। আর ফরয নামাযের পর সর্বশ্রেষ্ঠ নামায হল রাতের (তাহাজ্জুদের) নামায।’’[1]\n\n\n\n"
                        + "[1] (মুসলিম ১১৬৩নং, সুনানে আরবাআহ; আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ, ইবনে খুযাইমাহ, সহীহ)\n\n"
                        + "আশূরার রোযা\n"
                        + "মুহার্রাম মাসের রোযার মধ্যে সবচেয়ে বেশী তাকীদপ্রাপ্ত হল ঐ মাসের ১০ তারীখ আশূরার দিনের রোযা। রমাযানের রোযা ফরয হওয়ার আগে এই রোযা ওয়াজেব ছিল। রুবাইয়ে’ বিন্তে মুআউবিয বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) আশূরার সকালে মদ্বীনার আশেপাশে আনসারদের বস্তিতে বস্তিতে খবর পাঠিয়ে দিলেন যে, ‘‘যে রোযা অবস্থায় সকাল করেছে, সে যেন তার রোযা পূর্ণ করে নেয়। আর যে ব্যক্তি রোযা না রাখা অবস্থায় সকাল করেছে সেও যেন তার বাকী দিন পূর্ণ করে নেয়।’’\n\n"
                        + "রুবাইয়ে’ বলেন, ‘আমরা তার পর হতে ঐ রোযা রাখতাম এবং আমাদের ছোট ছোট বাচ্চাদেরকেও রাখাতাম। তাদের জন্য তুলোর খেলনা তৈরী করতাম এবং তাদেরকে মসজিদে নিয়ে যেতাম। অতঃপর তাদের মধ্যে কেউ খাবারের জন্য কাঁদতে শুরু করলে তাকে ঐ খেলনা দিতাম। আর এইভাবে ইফতারের সময় এসে পৌঁছত।’[1]\n\n"
                        + "মা আয়েশা (রাঃ) বলেন, ‘কুরাইশরা জাহেলিয়াতের যুগে আশূরার রোযা পালন করত। আর আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-ও জাহেলিয়াতে ঐ রোযা রাখতেন। (ঐ দিন ছিল কাবায় গিলাফ চড়াবার দিন।) অতঃপর তিনি যখন মদ্বীনায় এলেন, তখনও তিনি ঐ রোযা রাখলেন এবং সকলকে রাখতে আদেশ দিলেন। কিন্তু পরবর্তীতে যখন রমাযানের রোযা ফরয হল, তখন আশূরার রোযা ছেড়ে দিলেন। তখন অবস্থা এই হল যে, যার ইচ্ছা হবে সে রাখবে এবং যার ইচ্ছা হবে সে রাখবে না।’[2]\n\n"
                        + "ইবনে আববাস (রাঃ) বলেন, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) যখন মক্কা থেকে হিজরত করে মদ্বীনায় এলেন, তখন দেখলেন, ইয়াহুদীরা আশূরার দিনে রোযা পালন করছে। তিনি তাদেরকে জিজ্ঞাসা করলেন, ‘‘এটা কি এমন দিন যে, তোমরা এ দিনে রোযা রাখছ?’’ ইয়াহুদীরা বলল, ‘এ এক উত্তম দিন। এ দিনে আল্লাহ বানী ইসরাঈলকে তাদের শত্রু থেকে পরিত্রাণ দিয়েছিলেন। তাই মূসা এরই কৃতজ্ঞতা জ্ঞাপনের উদ্দেশ্যে এই দিনে রোযা পালন করেছিলেন। (আর সেই জন্যই আমরাও এ দিনে রোযা রেখে থাকি।)’\n\n"
                        + "এ কথা শুনে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘মূসার স্মৃতি পালন করার ব্যাপারে তোমাদের চাইতে আমি অধিক হকদার।’’ সুতরাং তিনি ঐ দিনে রোযা রাখলেন এবং সকলকে রোযা রাখতে আদেশ দিলেন।[3]\n\n"
                        + "বলাই বাহুল্য যে, উক্ত আদেশ ছিল মুস্তাহাব। যেমন মা আয়েশার উক্তিতে তা স্পষ্ট। তা ছাড়া মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘আজকে আশূরার দিন; এর রোযা আল্লাহ তোমাদের উপর ফরয করেন নি। তবে আমি রোযা রেখেছি। সুতরাং যার ইচ্ছা সে রোযা রাখবে, যার ইচ্ছা সে রাখবে না।’’[4]\n\n"
                        + "আবু কাতাদাহ (রাঃ) হতে বর্ণিত তিনি বলেন, আল্লার রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) আশূরার দিন রোযা রাখা প্রসঙ্গে জিজ্ঞাসিত হলে তিনি বললেন, ‘‘আমি আশা করি যে, (উক্ত রোযা) বিগত এক বছরের পাপরাশি মোচন করে দেবে।’’[5]\n\n"
                        + "ইবনে আববাস (রাঃ) প্রমুখাৎ বর্ণিত, তিনি বলেন, ‘আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) রমাযানের রোযার পর আশূরার দিন ছাড়া কোন দিনকে অন্য দিন অপেক্ষা মাহাত্ম্যপূর্ণ মনে করতেন না।’[6] অনুরূপ বর্ণিত আছে বুখারী ও মুসলিম শরীফে।[7]\n\n"
                        + "এক বর্ণনায় আছে, এই রোযা এক বছরের রোযার সমান।[8]\n\n"
                        + "অবশ্য যে ব্যক্তি আশূরার রোযা রাখবে তার জন্য তার একদিন আগে (৯ তারীখে)ও একটি রোযা রাখা সুন্নত। যেহেতু ইবনে আববাস (রাঃ) বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) যখন আশূরার রোযা রাখলেন এবং সকলকে রাখার আদেশ দিলেন, তখন লোকেরা বলল, ‘হে আল্লাহর রসূল! এ দিনটিকে তো ইয়াহুদ ও নাসারারা তা’যীম করে থাকে।’ তিনি বললেন, ‘‘তাহলে আমরা আগামী বছরে ৯ তারীখেও রোযা রাখব ইনশাআল্লাহ।’’ কিন্তু আগামী বছর আসার আগেই আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর ইন্তিকাল হয়ে গেল।[9]\n\n"
                        + "ইবনে আববাস (রাঃ) বলেন, ‘তোমরা ৯ ও ১০ তারীখে রোযা রাখ।’[10]\n\n"
                        + "পক্ষান্তরে ‘‘তোমরা এর একদিন আগে বা একদিন পরে একটি রোযা রাখ’’ -এই হাদীস সহীহ নয়।[11] তদনুরূপ সহীহ নয় ‘‘তোমরা এর একদিন আগে একটি এবং একদিন পরেও একটি রোযা রাখ’’ -এই হাদীস।[12]\n\n"
                        + "বলা বাহুল্য, ৯ ও ১০ তারীখেই রোযা রাখা সুন্নত। পক্ষান্তরে কেবল ১০ তারীখে রোযা রাখা মকরূহ।[13] যেহেতু তাতে ইয়াহুদীদের সাদৃশ্য সাধন হয় এবং তা মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর আশার প্রতিকূল। অবশ্য কেউ কেউ বলেন, ‘মকরূহ নয়। তবে কেউ একদিন (কেবল আশূরার দিন) রোযা রাখলে পূর্ণ সওয়াবের অধিকারী হবে না।’\n\n"
                        + "জ্ঞাতব্য যে, হুসাইন (রাঃ)-এর এই দিনে শহীদ হওয়ার সাথে এ রোযার কোন নিকট অথবা দূরতম কোন সম্পর্ক নেই। কারণ, তার পূর্বে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম); বরং তাঁর পূর্বে মূসা নবী u এই দিনে রোযা রেখে গেছেন। আর এই দিনে শিয়া সম্প্রদায় যে মাতম ও শোক পালন, মুখ ও বুক চিরে, গালে থাপর মেরে, চুল-জামা ছিঁড়ে, পিঠে চাবুক মেরে আত্মপ্রহার ইত্যাদি করে থাকে, তা জঘন্যতম বিদআত। সুন্নাহতে এ সবের কোন ভিত্তি নেই।\n\n"
                        + "তদনুরূপ এই দিনে নিজ পরিবার-পরিজনের উপর খরচ বৃদ্ধি করা, বিশেষ কোন নামায পড়া, দান-খয়রাত করা, বিশেষ করে শরবত-পানি দান করা, কলফ ব্যবহার করা, তেল মাখা, সুরমা ব্যবহার করা প্রভৃতি বিদআত। এ সকল বিদআত হুসাইন (রাঃ)-এর খুনীরাই আবিষ্কার করে গেছে।[14]\n\n"
                        + "[1] (আহমাদ, মুসনাদ ৬/৩৫৯, বুখারী ১৯৬০, মুসলিম ১১৩৬, ইবনে খুযাইমাহ, সহীহ ২০৮৮নং, বাইহাকী ৪/২৮৮)\n\n"
                        + "[2] (বুখারী ১৯৫২, ২০০২, মুসলিম ১১২৫নং প্রমুখ)\n\n"
                        + "[3] (বুখারী ২০০৪, মুসলিম ১১৩০নং)\n\n"
                        + "[4] (বুখারী ২০০৩, মুসলিম ১১২৯নং)\n\n"
                        + "[5] (আহমাদ, মুসনাদ ৫/২৯৭, মুসলিম ১১৬২, আবূ দাঊদ ২৪২৫, বাইহাকী ৪/২৮৬)\n\n"
                        + "[6] (ত্বাবারানী, মু’জাম আওসাত্ব, সহীহ তারগীব, আলবানী ১০০৬ নং)\n\n"
                        + "[7] (বুখারী ২০০৬, মুসলিম ১১৩২নং)\n\n"
                        + "[8] (ইবনে হিববান, সহীহ ৩৬৩১নং)\n\n"
                        + "[9] (মুসলিম ১১৩৪, আবূ দাঊদ ২৪৪৫নং)\n\n"
                        + "[10] (বাইহাকী ৪/২৮৭, আব্দুর রায্যাক, মুসান্নাফ ৭৮৩৯নং)\n\n"
                        + "[11] (ইবনে খুযাইমাহ, সহীহ ২০৯৫নং, আলবানীর টীকা দ্রঃ)\n\n"
                        + "[12] (যামাঃ ২/৭৬ টীকা দ্রঃ)\n\n"
                        + "[13] (ইবনে বায, ফাতাওয়া ইসলামিয়্যাহ ২/১৭০)\n\n"
                        + "[14] (তামামুল মিন্নাহ, আল্লামা আলবানী ৪১২পৃঃ দ্রঃ)",
                "Among voluntary fasts, fasting most days of the sacred month of Muharram holds supreme status right after Ramadan. The Messenger of Allah (peace and blessings be upon him) said: ''The best fast after the month of Ramadan is fasting in Allah's month, al-Muharram. And the best prayer after the obligatory prayer is the night prayer (Tahajjud).''[1]\n\n"
                        + "[1] (Sahih Muslim #1163; Sunan Arba'ah: Abu Dawud, At-Tirmidhi, An-Nasa'i, and Ibn Majah; Sahih Ibn Khuzaymah)\n\n"
                        + "Fasting on the Day of Ashura\n"
                        + "Among the fasts of Muharram, the most emphatically emphasized is the fast of the 10th of Muharram, the Day of Ashura. Before Ramadan was ordained, this fast was obligatory. Rubayyi' bint Mu'awwidh narrated that the Messenger of Allah (peace and blessings be upon him) sent messengers on the morning of Ashura to the villages of the Ansar around Madinah announcing: ''Whoever woke up fasting, let him complete his fast; and whoever broke their fast this morning, let him fast the remainder of the day.''\n\n"
                        + "Rubayyi' said: 'Thereafter, we used to observe this fast and would make our young children fast too. We made woollen toys for them and took them to the mosque. When one of them cried for food, we gave them that toy until it was time to break the fast.'[1]\n\n"
                        + "Mother Aisha (may Allah be pleased with her) said: 'The Quraysh used to fast on Ashura in the pre-Islamic period of ignorance (Jahiliyyah), and the Messenger of Allah (peace and blessings be upon him) also observed it during Jahiliyyah (it was the day the Ka'bah was draped with its cloth). When he migrated to Madinah, he continued to fast it and commanded everyone to fast. But when the fasting of Ramadan was made obligatory, he ceased enforcing it. It then became optional: whoever wished to fast did so, and whoever wished not to refrained.'[2]\n\n"
                        + "Ibn Abbas (may Allah be pleased with him) narrated: When the Prophet (peace and blessings be upon him) migrated from Makkah to Madinah, he found the Jews fasting on the Day of Ashura. He asked them: ''What is this day on which you fast?'' They replied: 'This is an auspicious day on which Allah delivered the Children of Israel from their enemy, so Moses fasted on this day out of gratitude to Allah (and thus we fast it too).' Upon hearing this, the Prophet (peace and blessings be upon him) said: ''I am closer to Moses and have more right to honor his memory than you.'' So he fasted that day and instructed everyone to fast.[3]\n\n"
                        + "This command was recommended (Mustahabb), as evident from Aisha's narration. Furthermore, the Prophet (peace and blessings be upon him) declared: ''Today is the Day of Ashura; Allah has not made its fast obligatory upon you, but I am fasting. Whoever wishes may fast, and whoever wishes may refrain.''[4]\n\n"
                        + "Abu Qatadah (may Allah be pleased with him) narrated that when asked about fasting on the Day of Ashura, the Prophet (peace and blessings be upon him) said: ''I hope that Allah will expiate thereby the sins of the year that came before it.''[5]\n\n"
                        + "Ibn Abbas (may Allah be pleased with him) narrated: 'I never saw the Messenger of Allah (peace and blessings be upon him) eagerly seek a day to fast, preferring it above all others, except this day—the Day of Ashura—and this month—the month of Ramadan.'[6] Similar narrations are recorded in Sahih Bukhari and Sahih Muslim.[7]\n\n"
                        + "In another narration, this fast is likened to an entire year's fasting.[8]\n\n"
                        + "Whoever observes the fast of Ashura is strongly encouraged by Sunnah to fast the day before it (the 9th of Muharram) as well. Ibn Abbas narrated that when the Prophet fasted Ashura and commanded the people to fast, they said: 'O Messenger of Allah! This is a day venerated by the Jews and Christians.' The Prophet (peace and blessings be upon him) replied: ''If I live until next year, In Sha Allah, we will surely fast on the ninth as well.'' However, the Messenger of Allah (peace and blessings be upon him) passed away before the next year arrived.[9]\n\n"
                        + "Ibn Abbas said: 'Fast on the 9th and 10th.'[10]\n\n"
                        + "On the other hand, the narration saying ''Fast a day before it or a day after it'' is not authentic (Da'eef).[11] Likewise, the narration saying ''Fast a day before it and a day after it'' is unauthentic.[12]\n\n"
                        + "It is Sunnah to combine the 9th and 10th. Fasting solely on the 10th without the 9th is disliked (Makruh) according to many scholars,[13] because it resembles the Jewish practice and opposes what the Prophet intended. Other scholars held: 'It is not forbidden or disliked, but one who fasts only the 10th does not attain the complete reward.'\n\n"
                        + "Important note: Fasting on this day has no connection whatsoever to the martyrdom of Imam Husayn (may Allah be pleased with him). For the Prophet (peace and blessings be upon him) fasted it decades before Husayn's martyrdom, and Prophet Moses (peace be upon him) fasted it centuries earlier. The practices carried out by the Shi'a sect on this day—mourning, wailing, chest-beating, cheek-slapping, tearing clothes, and self-flagellation with chains or blades—are vile innovations (Bid'ah) with zero foundation in the Sunnah.\n\n"
                        + "Similarly, exaggerating household expenses, designating special ritual prayers, distributing sweet drinks or syrups, applying henna/kohl, or treating it as a festive celebration are also innovations invented originally by the perpetrators of Husayn's martyrdom.[14]\n\n"
                        + "[1] (Musnad Ahmad 6/359; Sahih Bukhari #1960; Sahih Muslim #1136; Sahih Ibn Khuzaymah #2088; Al-Bayhaqi 4/288)\n\n"
                        + "[2] (Sahih Bukhari #1952, #2002; Sahih Muslim #1125, and others)\n\n"
                        + "[3] (Sahih Bukhari #2004; Sahih Muslim #1130)\n\n"
                        + "[4] (Sahih Bukhari #2003; Sahih Muslim #1129)\n\n"
                        + "[5] (Musnad Ahmad 5/297; Sahih Muslim #1162; Sunan Abi Dawud #2425; Al-Bayhaqi 4/286)\n\n"
                        + "[6] (At-Tabarani in Al-Mu'jam al-Awsat; Sahih at-Targhib #1006 by Al-Albani)\n\n"
                        + "[7] (Sahih Bukhari #2006; Sahih Muslim #1132)\n\n"
                        + "[8] (Sahih Ibn Hibban #3631)\n\n"
                        + "[9] (Sahih Muslim #1134; Sunan Abi Dawud #2445)\n\n"
                        + "[10] (Al-Bayhaqi 4/287; Abdur-Razzaq in Al-Musannaf #7839)\n\n"
                        + "[11] (Sahih Ibn Khuzaymah #2095, see notes by Al-Albani)\n\n"
                        + "[12] (See Zad al-Ma'ad 2/76)\n\n"
                        + "[13] (Ibn Baz, Fatawa Islamiyyah 2/170)\n\n"
                        + "[14] (Tamam al-Minnah by Shaykh al-Albani, p. 412)"
        ));

        // Card 6: যুলহজ্জের প্রথম নয় দিনের রোযা
        list.add(new HajjHistoryCardItem(
                6,
                "যুলহজ্জের প্রথম নয় দিনের রোযা",
                "Fasting First Nine Days of Dhul-Hijjah",
                "যুলহজ্জ মাসের প্রথম নয় দিন রোযা রাখা মুস্তাহাব। যেহেতু আল্লাহ আয্যা অজাল্ল যুলহজ্জের প্রথম দশ দিনকে অন্যান্য দিনের উপর শ্রেষ্ঠত্ব ও মর্যাদা দান করেছেন...",
                "Fasting the first nine days of Dhul-Hijjah is recommended (Mustahabb), as Allah has elevated the first ten days above all other days...",
                "যুলহজ্জ মাসের প্রথম নয় দিন রোযা রাখা মুস্তাহাব। যেহেতু আল্লাহ আয্যা অজাল্ল যুলহজ্জের প্রথম দশ দিনকে অন্যান্য দিনের উপর শ্রেষ্ঠত্ব ও মর্যাদা দান করেছেন। আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন। ‘‘এই দশদিনের মধ্যে কৃত নেক আমলের চেয়ে আল্লাহর নিকট অধিক পছন্দনীয় আর কোন আমল নেই।’’ (সাহাবাগণ) বললেন, ‘আল্লাহর পথে জিহাদও নয় কি?’ তিনি বললেন, ‘‘আল্লাহর পথে জিহাদও নয়। তবে এমন কোন ব্যক্তি (এর আমল) যে নিজের জান-মাল সহ বের হয় এবং তারপর কিছুও সঙ্গে নিয়ে আর ফিরে আসে না।[1]\n\n"
                        + "আর রোযা হল একটি নেক আমল। সুতরাং তা পালন করাও এ দিনগুলিতে মুস্তাহাব।[2]\n\n"
                        + "প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) ও এই নয় দিনে রোযা পালন করতেন। তাঁর পত্নী ( হাফসাহ রাঃ) বলেন, ‘‘নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) যুল হজ্জের নয় দিন, আশূরার দিন এবং প্রত্যেক মাসের তিন দিন; মাসের প্রথম সোমবার এবং বৃহস্পতিবার রোযা রাখতেন।’’[3]\n\n"
                        + "বাইহাকী ‘ফাযায়েলুল আওকাত’ এ বলেন, এই হাদীসটি আয়েশা (রাঃ) এর ঐ হাদীস অপেক্ষা উত্তম যাতে তিনি বলেন, ‘রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) কে (যুলহজ্জের) দশ দিনে কখনো রোযা রাখতে দেখিনি।’[4] কারণ, এ হাদীসটি ঘটনসূচক এবং তা আয়েশার ঐ অঘটনসূচক হাদীস হতে উত্তম। আর মুহাদ্দেসীনদের একটি নীতি এই যে, যখন ঘটনসূচক ও অঘটনসূচক দু’টি হাদীস পরস্পর-বিরোধী হয় তখন সমন্বয় সাধনের অন্যান্য উপায় না থাকলে ঘটনসূচক হাদীসটিকে প্রাধান্য দেওয়া হয়। কারণ কেউ যদি কিছু ঘটতে না দেখে তবে তার অর্থ এই নয় যে, তা ঘটেই নি। তাই যে ঘটতে দেখেছে তার কথাটিকে ঘটার প্রমাণস্বরূপ গ্রহণ করা হয়।\n\n"
                        + "মোট কথা, যুলহজ্জ মাসের এই নয় দিনে রোযা রাখা মুস্তাহাব। ইমাম নওবী বলেন, ‘ঐ দিনগুলিতে রোযা রাখা পাকা মুস্তাহাব।’[5]\n\n"
                        + "[1] (আহমাদ, মুসনাদ ৩/২৯৮, বুখারী ৯৬৯, আবূ দাঊদ ২৪৩৮, তিরমিযী ৭৫৭, ইবনে মাজাহ ১৭২৭নং)\n\n"
                        + "[2] (আশ্শারহুল মুমতে’ ৬/৪৭১)\n\n"
                        + "[3] (আবূ দাঊদ ২৪৩৭, সহীহ আবূ দাঊদ ২১২৯নং, নাসাঈ)\n\n"
                        + "[4] (মুসলিম ১১৭৬, আবূ দাঊদ ২৪৩৯, তিরমিযী ৭৫৬, ইবনে মাজাহ ১৭২৯নং)\n\n"
                        + "[5] (শরহুন নওবী ৮/৩২০)",
                "Fasting the first nine days of Dhul-Hijjah is recommended (Mustahabb), because Allah, the Exalted and Majestic, has bestowed supreme distinction and virtue upon the first ten days of Dhul-Hijjah over all other days of the year. The Messenger of Allah (peace and blessings be upon him) said: ''There are no days during which good deeds are more beloved to Allah than these ten days.'' The Companions asked: 'Not even Jihad in the cause of Allah?' He replied: ''Not even Jihad in the cause of Allah, except a person who goes out with his life and his wealth, and returns with neither of them.''[1]\n\n"
                        + "And fasting is among the greatest of righteous deeds; hence, fasting during these days is highly recommended.[2]\n\n"
                        + "The beloved Prophet (peace and blessings be upon him) himself used to fast these nine days. His wife Hafsah (may Allah be pleased with her) narrated: ''The Prophet (peace and blessings be upon him) used to fast the first nine days of Dhul-Hijjah, the Day of Ashura, and three days of each month: the first Monday of the month and two Thursdays.''[3]\n\n"
                        + "Imam Al-Bayhaqi stated in 'Fada'il al-Awqat' that this narration is given precedence over Mother Aisha's statement where she said: 'I never saw the Messenger of Allah (peace and blessings be upon him) fasting during the ten days.'[4] This is because Hafsah's narration establishes positive affirmation (Ithbat), which takes precedence over negation (Nafy) according to the established principles of Hadith methodology. If one person did not witness an event, it does not prove that it never occurred; therefore, the affirmative testimony of the one who witnessed it is accepted as conclusive proof.\n\n"
                        + "In summary, fasting during these first nine days of Dhul-Hijjah is thoroughly recommended. Imam An-Nawawi declared: 'Fasting these days is emphatically recommended (Mustahabb Mu'akkad).' [5]\n\n"
                        + "[1] (Musnad Ahmad 3/298; Sahih Bukhari #969; Sunan Abi Dawud #2438; Jami' at-Tirmidhi #757; Sunan Ibn Majah #1727)\n\n"
                        + "[2] (Ash-Sharh al-Mumti' 6/471)\n\n"
                        + "[3] (Sunan Abi Dawud #2437; Sahih Abi Dawud #2129; Sunan an-Nasa'i)\n\n"
                        + "[4] (Sahih Muslim #1176; Sunan Abi Dawud #2439; Jami' at-Tirmidhi #756; Sunan Ibn Majah #1729)\n\n"
                        + "[5] (Sharh an-Nawawi 8/320)"
        ));

        // Card 7: শা’বান মাসের অধিকাংশ দিনের রোযা
        list.add(new HajjHistoryCardItem(
                7,
                "শা’বান মাসের অধিকাংশ দিনের রোযা",
                "Fasting Most Days of Sha'ban",
                "আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) শা’বান মাসের অধিকাংশ দিনগুলিতে রোযা রাখতেন। মা আয়েশা (রাঃ) বলেন...",
                "The Messenger of Allah (peace and blessings be upon him) used to fast most days of Sha'ban, when deeds are raised to Allah...",
                "আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) শা’বান মাসের অধিকাংশ দিনগুলিতে রোযা রাখতেন। মা আয়েশা (রাঃ) বলেন, ‘আমি আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-কে রমাযান ছাড়া অন্য কোন মাস সম্পূর্ণ রোযা রাখতে দেখি নি। আর শা’বান মাস ছাড়া অন্য কোন মাসের অধিকাংশ দিনগুলিতে তাঁকে রোযা রাখতে দেখি নি।’[1]\n\n"
                        + "উসামাহ বিন যায়দ (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, ‘একদা আমি বললাম, হে আল্লাহর রসূল! আপনাকে শা’বান মাসে যত রোযা রাখতে দেখি তত অন্য কোন মাসে তো রাখতে দেখি না, (এর রহস্য কি)?’ উত্তরে তিনি বললেন, ‘‘এটা তো সেই মাস, যে মাস সম্বন্ধে মানুষ উদাসীন, যা হল রজব ও রমাযানের মাঝে। আর এটা তো সেই মাস; যাতে বিশব জাহানের প্রতিপালকের নিকট আমলসমূহ পেশ করা হয়। তাই আমি পছন্দ করি যে, আমার রোযা রাখা অবস্থায় আমার আমল (আল্লাহর নিকট) পেশ করা হোক।[2]\n\n"
                        + "মা আয়েশা (রাঃ) বলেন, ‘আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর নিকট রোযা রাখার জন্য পছন্দনীয় মাস ছিল শা’বান। তিনি সে মাসের রোযাকে রমাযানের সাথে মিলিত করতেন।’[3]\n\n"
                        + "এখানে তাঁর শা’বানের অধিকাংশ দিনগুলিতে রোযা এবং এই মাসের রোযার সাথে রমাযানের রোযাকে মিলিত করার হাদীসের সাথে রমাযানের ২/১ দিন আগে রোযা রাখতে নিষেধকারী হাদীসের[4] অথবা তার কৃষ্ণপক্ষের দিনগুলিতে রোযা রাখতে নিষেধকারী হাদীসের[5] কোন সংঘর্ষ বা পরস্পর-বিরোধিতা নেই। কেননা, উভয় শ্রেণীর হাদীসের মাঝে সম¦বয় সাধন সম্ভব। আর তা এইভাবে যে, ঐ দিনগুলিতে রোযা রাখা নিষিদ্ধ; যদি অভ্যাসগতভাবে কোন রোযা না পড়ে তাহলে। পক্ষান্তরে অভ্যাসগতভাবে ঐ দিনগুলিতে রোযা পড়লে রাখা বৈধ। আর সেটাই ছিল মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর আমল।[6] অর্থাৎ, তিনি অভ্যাসগতভাবে ঐ দিনগুলিতে রোযা রাখতেন। এখতিয়ার করে নয়।\n\n"
                        + "অন্য দিকে এই মাসের ১৫ তারীখের রোযা রাখা এবং তাতে পৃথক কোন বৈশিষ্ট্য বা মাহাত্ম্য আছে মনে করা বিদআত; যেমন এ কথা পূর্বেও আলোচিত হয়েছে। কেননা, এ ব্যাপারে বর্ণিত কোন হাদীস সহীহ নয়।\n\n"
                        + "[1] (আহমাদ, মুসনাদ, বুখারী ১৯৬৯, মুসলিম ১১৫৬নং, তিরমিযী, ইবনে মাজাহ)\n\n"
                        + "[2] (নাসাঈ, সহীহ তারগীব, আলবানী ১০০৮নং, তামামুল মিন্নাহ, আল্লামা আলবানী ৪১২পৃঃ)\n\n"
                        + "[3] (সহীহ আবূ দাঊদ ২১২৪নং)\n\n"
                        + "[4] (বুখারী ১৯১৪, মুসলিম ১০৮২নং)\n\n"
                        + "[5] (সহীহ আবূ দাঊদ ২০৪৯, সহীহ তিরমিযী, আলবানী ৫৯০নং)\n\n"
                        + "[6] (আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ১৭৪পৃঃ)",
                "The Messenger of Allah (peace and blessings be upon him) used to fast most days of the month of Sha'ban. Mother Aisha (may Allah be pleased with her) said: 'I never saw the Messenger of Allah (peace and blessings be upon him) complete a full month of fasting except Ramadan, and I never saw him fast more days in any month than in Sha'ban.'[1]\n\n"
                        + "Usamah ibn Zayd (may Allah be pleased with him) narrated: 'I once asked: O Messenger of Allah! I do not see you fasting in any month as much as you fast in Sha'ban, what is the reason? He replied: ''That is a month between Rajab and Ramadan that people neglect, and it is a month in which deeds are raised to the Lord of the worlds. Therefore, I love for my deeds to be presented while I am fasting.''\n\n"
                        + "Mother Aisha (may Allah be pleased with her) also said: 'The most beloved month for the Messenger of Allah (peace and blessings be upon him) to fast was Sha'ban, and he would connect it with Ramadan.'[3]\n\n"
                        + "There is no contradiction between these Hadiths and the prophetic prohibition against fasting one or two days right before Ramadan,[4] or the prohibition against fasting after the middle of Sha'ban for those without prior habit.[5] Reconciling them is straightforward: prohibition applies to those who innovate or start fasting spontaneously right before Ramadan without prior habit. If someone has an established regular habit of fasting (such as Mondays, Thursdays, or alternate days), continuing to fast is completely permissible. That was indeed the Prophet's regular practice.[6]\n\n"
                        + "On the other hand, singling out the 15th of Sha'ban (Shab-e-Barat) for a dedicated fast, attributing special scriptural merit specifically to that individual day's fast, is an unproven innovation (Bid'ah), as no authentic Sahih Hadith establishes a specific fast for the 15th day alone.\n\n"
                        + "[1] (Musnad Ahmad; Sahih Bukhari #1969; Sahih Muslim #1156; Jami' at-Tirmidhi; Sunan Ibn Majah)\n\n"
                        + "[2] (Sunan an-Nasa'i; Sahih at-Targhib #1008 by Al-Albani; Tamam al-Minnah, p. 412)\n\n"
                        + "[3] (Sahih Abi Dawud #2124)\n\n"
                        + "[4] (Sahih Bukhari #1914; Sahih Muslim #1082)\n\n"
                        + "[5] (Sahih Abi Dawud #2049; Sahih at-Tirmidhi #590 by Al-Albani)\n\n"
                        + "[6] (Ahkamus Sawmi wal-I'tikaf by Abu Sari M. Abdul Hadi, p. 174)"
        ));

        // Card 8: সোম ও বৃহস্পতিবারের রোযা
        list.add(new HajjHistoryCardItem(
                8,
                "সোম ও বৃহস্পতিবারের রোযা",
                "Fasting on Mondays and Thursdays",
                "প্রত্যেক সপ্তাহের সোম ও বৃহস্পতিবার রোযা রাখা সুন্নত ও মুস্তাহাব। যেহেতু তা ছিল মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর আমল...",
                "Fasting every Monday and Thursday of each week is Sunnah and Mustahabb, as deeds are presented to Allah on these two days...",
                "প্রত্যেক সপ্তাহের সোম ও বৃহস্পতিবার রোযা রাখা সুন্নত ও মুস্তাহাব। যেহেতু তা ছিল মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর আমল। আর দিন দুটিতে বিশবাধিপতি আল্লাহর নিকট বান্দার আমল পেশ করা হয়।\n\n"
                        + "মা আয়েশা (রাঃ) বলেন, ‘আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) সোম ও বৃহস্পতিবারে রোযা রাখাকে প্রাধান্য দিতেন।’[1]\n\n"
                        + "আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘সোম ও বৃহস্পতিবার (মানুষের) সকল আমল (আল্লাহর দরবারে) পেশ করা হয়। তাই আমি এটা পছন্দ করি যে, আমার রোযা রাখা অবস্থায় আমার আমল (তাঁর নিকট) পেশ করা হোক।’’[2]\n\n"
                        + "উক্ত আবু হুরাইরা (রাঃ) হতেই বর্ণিত, তিনি বলেন আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘প্রত্যেক সোম ও বৃহস্পতিবারে (মানুষের) সকল আমল (আল্লাহর নিকট) পেশ করা হয়। (এবং বেহেশ্তের দ্বারসমূহ উ¦মুক্ত করা হয়।) আর (ঐ উভয় দিনে) আল্লাহ আয্যা অজাল্ল্ প্রত্যেক সেই ব্যক্তিকে মার্জনা করে দেন যে কোন কিছুকে তাঁর অংশী স্থাপন করে না। তবে সেই ব্যক্তিকে ক্ষমা করেন না যার নিজ ভায়ের সাথে বিদ্বেষ থাকে; এই দুই ব্যক্তির জন্য (ফিরিশ্তার উদ্দেশ্যে) তিনি বলেন, উভয়ের মিলন না হওয়া পর্যন্ত ওদেরকে অবকাশ দাও। উভয়ের মিলন না হওয়া পর্যন্ত ওদেরকে অবকাশ দাও।’’[3]\n\n"
                        + "আবূ কাতাদাহ (রাঃ) বলেন, ‘সোমবার রোযা রাখার ব্যাপারে নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) জিজ্ঞাসিত হলে তিনি বললেন, ‘‘এটা হল সেই দিন, যেদিনে আমার জ¦ম হয়েছে এবং আমার উপর সর্বপ্রথম কুরআন অবতীর্ণ হয়েছে।’’ অন্য এক বর্ণনায় আছে, ‘‘ঐ দিনে আমি (নবীরূপে) প্রেরিত হয়েছি।’’[4]\n\n"
                        + "[1] (আহমাদ, মুসনাদ ৬/৮০, ৮৯, ১০৬, তিরমিযী, নাসাঈ, ইবনে মাজাহ ১৭৩৯নং, ইরওয়াউল গালীল, আলবানী ৪/১০৫-১০৬)\n\n"
                        + "[2] (তিরমিযী, সহীহ তারগীব, আলবানী ১০২৭নং)\n\n"
                        + "[3] (আহমাদ, মুসনাদ ২/৩২৯, মুসলিম ২৫৬৫ নং, প্রমুখ)\n\n"
                        + "[4] (আহমাদ, মুসনাদ ৫/২৯৭, ২৯৯, মুসলিম ১১৬২, আবূ দাঊদ ২৪২৫নং)",
                "Fasting every Monday and Thursday of each week is Sunnah and recommended (Mustahabb), as it was the continuous practice of the Prophet (peace and blessings be upon him). Moreover, deeds of servants are presented before the Lord of the worlds on these two days.\n\n"
                        + "Mother Aisha (may Allah be pleased with her) said: 'The Messenger of Allah (peace and blessings be upon him) was keen to fast on Mondays and Thursdays.'[1]\n\n"
                        + "Abu Hurairah (may Allah be pleased with him) narrated that the Messenger of Allah (peace and blessings be upon him) said: ''Deeds are presented (before Allah) on Mondays and Thursdays, and I love that my deeds be presented while I am fasting.''[2]\n\n"
                        + "Also narrated from Abu Hurairah (may Allah be pleased with him), the Messenger of Allah (peace and blessings be upon him) said: ''The deeds of people are presented every Monday and Thursday (and the gates of Paradise are opened). On both days, Allah, the Mighty and Majestic, forgives every person who does not associate any partner with Him, except a person who holds mutual enmity and grudge against his brother. Regarding these two, Allah instructs the angels: Leave these two until they reconcile, leave these two until they reconcile!''[3]\n\n"
                        + "Abu Qatadah (may Allah be pleased with him) narrated that when the Prophet (peace and blessings be upon him) was asked about fasting on Mondays, he said: ''That is the day on which I was born and on which the Quran was first revealed to me.'' In another narration: ''On that day, I was commissioned as a Prophet.''[4]\n\n"
                        + "[1] (Musnad Ahmad 6/80, 89, 106; Jami' at-Tirmidhi; Sunan an-Nasa'i; Sunan Ibn Majah #1739; Irwa' al-Ghalil 4/105-106 by Al-Albani)\n\n"
                        + "[2] (Jami' at-Tirmidhi; Sahih at-Targhib #1027 by Al-Albani)\n\n"
                        + "[3] (Musnad Ahmad 2/329; Sahih Muslim #2565, and others)\n\n"
                        + "[4] (Musnad Ahmad 5/297, 299; Sahih Muslim #1162; Sunan Abi Dawud #2425)"
        ));

        // Card 9: প্রত্যেক মাসে তিনটি রোযা
        list.add(new HajjHistoryCardItem(
                9,
                "প্রত্যেক মাসে তিনটি রোযা",
                "Fasting Three Days of Each Month",
                "প্রত্যেক (চান্দ্র) মাসে ৩টি করে রোযা রাখা মুস্তাহাব। আব্দুল্লাহ বিন আম্র বিন আস (রাঃ) বলেন, আল্লাহর রসূল...",
                "Fasting three days in each lunar month is equivalent to fasting the entire year, ideally on the 13th, 14th, and 15th (White Days)...",
                "প্রত্যেক (চান্দ্র) মাসে ৩টি করে রোযা রাখা মুস্তাহাব। আব্দুল্লাহ বিন আম্র বিন আস (রাঃ) বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘প্রত্যেক মাসে তিনটি রোযা রাখা সারা বছর রোযা রাখার সমতুল্য।’’[1]\n\n"
                        + "আবূ যার্র (রাঃ) বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘যে ব্যক্তি প্রত্যেক মাসে ৩টি করে রোযা রাখবে, তার সারা বছর রোযা রাখা হবে। আল্লাহ আয্যা অজাল্ল্ এর সত্যায়ন অবতীর্ণ করে বলেন, কেউ কোন ভাল কাজ করলে, সে তার ১০ গুণ প্রতিদান পাবে। (কুরআনুল কারীম ৬/১৬০) এক দিন ১০ দিনের সমান।’’[2]\n\n"
                        + "ইবনে আববাস (রাঃ) হতে বর্ণিত, তিনি বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘ধৈর্যের (রমাযান) মাসে রোযা আর প্রত্যেক মাসের তিনটি রোযা অন্তরের বিদ্বেষ ও খট্কা দূর করে দেয়।’’[3]\n\n"
                        + "পক্ষান্তরে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁর একান্ত ভক্ত আবূ হুরাইরা (রাঃ)-কে এই রোযা রাখতে অসিয়ত (বিশেষ উপদেশ) করেছেন।[4]\n\n"
                        + "অবশ্য এই তিন রোযা প্রত্যেক চান্দ্র মাসের শুক½পক্ষের শেষ দিনগুলিতে; অর্থাৎ, ১৩, ১৪ ও ১৫ তারীখে হওয়া মুস্তাহাব। যেহেতু আবূ যার্র (রাঃ) বলেন, আল্লাহর রসূল তাঁকে বলেছেন, ‘‘হে আবূ যার্র! মাসে ৩টি রোযা রাখলে ১৩, ১৪ ও ১৫ তারীখে রাখ।’’[5]\n\n"
                        + "আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) রাখতেন প্রত্যেক মাসের প্রথম সোমবার, অতঃপর তার পরের বৃহস্পতিবার, অতঃপর তার পরবর্তী বৃহস্পতিবার।[6] কোন কোন বর্ণনা মতে মাসের শুক্লপক্ষের শেষ তিনদিন রোযা রাখতেন।[7] আর কোন কোন বর্ণনা মতে তিনি কোন নির্দিষ্ট দিনের খেয়াল না করেই যে কোন দিনে ৩টি রোযা রাখতেন।[8]\n\n"
                        + "[1] (বুখারী ১৯৭৯নং, মুসলিম ১১৫৯ নং)\n\n"
                        + "[2] (তিরমিযী, ইবনে মাজাহ ১৭০৮নং, ইরওয়াউল গালীল, আলবানী ৪/১০২)\n\n"
                        + "[3] (বাযযার, সহীহ তারগীব, আলবানী ১০১৮নং)\n\n"
                        + "[4] (দ্রঃ আহমাদ, মুসনাদ ২/৪৫৯, বুখারী ১১৭৮, মুসলিম ৭২১, দারেমী, বাইহাকী ৪/২৯৩ প্রমুখ)\n\n"
                        + "[5] (আহমাদ, মুসনাদ ৫/১৬২, ১৭৭, তিরমিযী, নাসাঈ, বাইহাকী ৪/২৯৪, ইরওয়াউল গালীল, আলবানী ৯৪৭নং)\n\n"
                        + "[6] (আহমাদ, মুসনাদ, আবূ দাঊদ, নাসাঈ, তামামুল মিন্নাহ, আল্লামা আলবানী ৪১৫পৃঃ দ্রঃ)\n\n"
                        + "[7] (সহীহ আবূ দাঊদ ২১৪০নং)\n\n"
                        + "[8] (মুসলিম ১১৬০, সহীহ আবূ দাঊদ ২১৪২নং)",
                "Fasting three days in each lunar month is recommended (Mustahabb). Abdullah ibn Amr ibn al-Aas (may Allah be pleased with him) narrated that the Messenger of Allah (peace and blessings be upon him) said: ''Fasting three days every month is equivalent to fasting perpetually (the entire year).''[1]\n\n"
                        + "Abu Dharr (may Allah be pleased with him) narrated that the Messenger of Allah (peace and blessings be upon him) said: ''Whoever fasts three days every month, it is counted as fasting for the entire year. Allah, the Mighty and Majestic, confirmed this when He revealed: 'Whoever comes with a good deed will have ten times the like thereof' (Surah Al-An'am 6:160); one day is equal to ten days.''[2]\n\n"
                        + "Ibn Abbas (may Allah be pleased with him) narrated that the Messenger of Allah (peace and blessings be upon him) said: ''Fasting the month of patience (Ramadan) and three days of every month removes malice and rancor from the heart.''[3]\n\n"
                        + "Furthermore, the Prophet (peace and blessings be upon him) specifically advised his beloved companion Abu Hurairah (may Allah be pleased with him) never to leave these three fasts throughout his life.[4]\n\n"
                        + "It is preferable that these three fasts be observed during the bright moonlit nights (Ayyam al-Beed), namely the 13th, 14th, and 15th of each lunar month. Abu Dharr narrated that the Prophet (peace and blessings be upon him) said to him: ''O Abu Dharr! If you fast three days of the month, fast on the 13th, 14th, and 15th.''[5]\n\n"
                        + "In some narrations, the Prophet (peace and blessings be upon him) used to fast the first Monday of the month, followed by the next Thursday, and then the following Thursday.[6] Other narrations state he fasted the last three days of the bright phase,[7] and in other accounts, he fasted three days in whichever part of the month suited him without specifying particular days.[8]\n\n"
                        + "[1] (Sahih Bukhari #1979; Sahih Muslim #1159)\n\n"
                        + "[2] (Jami' at-Tirmidhi; Sunan Ibn Majah #1708; Irwa' al-Ghalil 4/102 by Al-Albani)\n\n"
                        + "[3] (Al-Bazzar; Sahih at-Targhib #1018 by Al-Albani)\n\n"
                        + "[4] (See Musnad Ahmad 2/459; Sahih Bukhari #1178; Sahih Muslim #721; Sunan ad-Darimi; Al-Bayhaqi 4/293)\n\n"
                        + "[5] (Musnad Ahmad 5/162, 177; Jami' at-Tirmidhi; Sunan an-Nasa'i; Al-Bayhaqi 4/294; Irwa' al-Ghalil #947)\n\n"
                        + "[6] (Musnad Ahmad; Sunan Abi Dawud; Sunan an-Nasa'i; Tamam al-Minnah, p. 415)\n\n"
                        + "[7] (Sahih Abi Dawud #2140)\n\n"
                        + "[8] (Sahih Muslim #1160; Sahih Abi Dawud #2142)"
        ));

        // Card 10: দাঊদী রোযা
        list.add(new HajjHistoryCardItem(
                10,
                "দাঊদী রোযা",
                "Fast of Prophet Dawud (AS)",
                "যার সামর্থ্য আছে তার জন্য একদিন রোযা থাকা ও তার পরের দিন রোযা না থাকা; ভিন্ন কথায় একদিন পরপর রোযা রাখা মুস্তাহাব...",
                "For whoever possesses physical capacity, fasting alternate days (the fast of Prophet Dawud AS) is the most beloved fast to Allah...",
                "যার সামর্থ্য আছে তার জন্য একদিন রোযা থাকা ও তার পরের দিন রোযা না থাকা; ভিন্ন কথায় একদিন পরপর রোযা রাখা মুস্তাহাব। আব্দুল্লাহ বিন আম্র বিন আস (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘আল্লাহর নিকট সর্বাপেক্ষা পছন্দনীয় রোযা হল দাঊদ (আঃ) -এর রোযা। আর আল্লাহর নিকট সর্বাপেক্ষা পছন্দনীয় নামায হল দাঊদ (আঃ) -এর নামায। তিনি অর্ধ রাত্রি ঘুমাতেন। অতঃপর তৃতীয় প্রহরে নামায পড়ে পুনরায় ষষ্ঠভাগে ঘুমাতেন, আর তিনি একদিন পানাহার করতেন ও পরদিন রোযা রাখতেন।’’[1]\n\n"
                        + "পরন্তু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) ইবনে আম্রকে বলেছেন, ‘‘তুমি একদিন রোযা থাক এবং একদিন পানাহার কর। এটাই হল দাঊদ u-এর রোযা; যা সর্বশ্রেষ্ঠ রোযা।’’ ইবনে আমর বললেন, ‘কিন্তু আমি তার থেকেও উত্তম (প্রত্যেক দিন রোযা রাখতে) পারি।’ তা শুনে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘(আমি যা বললাম) তার চাইতে উত্তম কিছুই নেই।’’[2] অন্য এক বর্ণনায় আছে, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁকে বললেন, ‘‘দাঊদ (আঃ) -এর রোযার উপর কোন রোযা নেই। অর্ধ বছর রোযা; একদিন রোযা রাখ এবং তার পরের দিন পানাহার কর।’’[3]\n\n"
                        + "বলা বাহুল্য, এ রোযা সামর্থ্যের সাথে সম্পর্ক রাখে। অতএব শর্ত হল, যেন এ রোযা রাখতে গিয়ে সবাস্থ্য এমন দুর্বল না হয়ে যায়, যাতে নফল রোযা থেকে উত্তম বা গুরুত্বপূর্ণ আমল পালনে ত্রুটি পরিলক্ষিত হয়। যেমন, আল্লাহর অন্যান্য হক এবং বান্দার যাবতীয় অধিকার আদায়ে যেন কোন প্রকার ত্রুটি প্রকাশ না পায়। নচেৎ, তা বর্জন করাই উত্তম।[4]\n\n"
                        + "[1] (বুখারী১১৩১, মুসলিম ১১৫৯নং, আবূ দাঊদ, নাসাঈ, ইবনে মাজাহ)\n\n"
                        + "[2] (বুখারী ১৯৭৬নং)\n\n"
                        + "[3] (ঐ ১৯৮০নং)\n\n"
                        + "[4] (দ্রঃ আশ্শারহুল মুমতে’ ৬/৪৭৪, আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ১৭৬পৃঃ)",
                "For whoever possesses the physical capacity, fasting one day and breaking fast the next day—in other words, fasting on alternate days—is recommended (Mustahabb). Abdullah ibn Amr ibn al-Aas (may Allah be pleased with him) narrated that the Messenger of Allah (peace and blessings be upon him) said: ''The most beloved fast to Allah is the fast of Dawud (peace be upon him), and the most beloved prayer to Allah is the prayer of Dawud (peace be upon him). He used to sleep half the night, pray for a third of it, and sleep for a sixth of it. And he used to fast one day and eat the next day.''[1]\n\n"
                        + "Furthermore, the Prophet (peace and blessings be upon him) told Ibn Amr: ''Fast one day and eat the next day; that is the fast of Dawud (peace be upon him), which is the best of fasts.'' Ibn Amr said: 'I can do better than that (fasting every day).' The Prophet (peace and blessings be upon him) said: ''There is nothing better than that!''[2] In another narration, the Prophet (peace and blessings be upon him) said: ''There is no fast superior to the fast of Dawud (peace be upon him): half of the year; fast one day and eat the next day.''[3]\n\n"
                        + "Needless to say, this intense voluntary worship is contingent upon physical capability. The crucial condition is that fasting alternate days must not weaken health so severely that duties of greater or equal importance are neglected—such as fulfilling obligations towards Allah, family responsibilities, and rights of people. Otherwise, refraining from such rigorous voluntary fasting is better.[4]\n\n"
                        + "[1] (Sahih Bukhari #1131; Sahih Muslim #1159; Sunan Abi Dawud; Sunan an-Nasa'i; Sunan Ibn Majah)\n\n"
                        + "[2] (Sahih Bukhari #1976)\n\n"
                        + "[3] (Sahih Bukhari #1980)\n\n"
                        + "[4] (See Ash-Sharh al-Mumti' 6/474; Ahkamus Sawmi wal-I'tikaf, p. 176)"
        ));

        // Card 11: সাধারণ নফল রোযা
        list.add(new HajjHistoryCardItem(
                11,
                "সাধারণ নফল রোযা",
                "General Voluntary (Nafl) Fasting",
                "সাধারণ নফল রোযা; যা কোন নির্দিষ্ট কারণ বা দিনের সাথে সীমাবদ্ধ নয়। এমন রোযা নিষিদ্ধ দিন ছাড়া যে কোনও দিনে অনির্দিষ্টভাবে রাখা যাবে...",
                "General voluntary fasts not restricted to specific dates may be observed on any day outside forbidden days, especially in winter...",
                "সাধারণ নফল রোযা; যা কোন নির্দিষ্ট কারণ বা দিনের সাথে সীমাবদ্ধ নয়। এমন রোযা নিষিদ্ধ দিন ছাড়া যে কোনও দিনে অনির্দিষ্টভাবে রাখা যাবে। মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘শীতকালের রোযা ঠান্ডা গনীমত (যুদ্ধজয়ে লব্ধ সম্পদ)।’’\n\n"
                        + "অন্য এক বর্ণনায় তিনি বলেন, ‘‘শীতকাল মুমিনের বসন্তকাল। তার রাত্রি লম্বা হওয়ার ফলে সে তাহাজ্জুদ পড়ে এবং তার দিন ছোট হওয়ার ফলে সে রোযা রাখে।’’[1]\n\n"
                        + "বলা বাহুল্য, এমন রোযা দ্বীনের সহজ-সরল নিয়ম-নীতির ভিত্তিতেই বিধিবদ্ধ।\n\n"
                        + "[1] (আহমাদ, মুসনাদ প্রমুখ, সিলসিলাহ সহীহাহ, আলবানী ১৯২২নং)",
                "General voluntary (Nafl) fasting is that which is not restricted to a specific occasion, season, or day of the week. Such fasts may be observed on any permissible day outside the strictly prohibited days (such as the two Eids and the days of Tashreeq). The Prophet (peace and blessings be upon him) said: ''Fasting in winter is cold booty (effortless spiritual gain gained without battle).''\n\n"
                        + "In another narration, he said: ''Winter is the spring of the believer: its long nights allow him to pray Tahajjud, and its short days make it easy for him to fast.''[1]\n\n"
                        + "Needless to say, such voluntary fasting is legislated upon the foundational principles of ease and mercy in Islam.\n\n"
                        + "[1] (Musnad Ahmad, and others; Silsilah Sahihah #1922 by Al-Albani)"
        ));

        // Card 12: নফল রোযা ভাঙ্গা বৈধ
        list.add(new HajjHistoryCardItem(
                12,
                "নফল রোযা ভাঙ্গা বৈধ",
                "Permissibility of Breaking Voluntary Fasts",
                "যে ব্যক্তি নফল রোযা রাখে, তার জন্য তা ভাঙ্গা বা দিনের যে কোন অংশে তা ছেড়ে দেওয়া বৈধ। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন...",
                "Whoever observes a voluntary fast is free to break it or discontinue it at any point during the day, as a voluntary faster is his own master...",
                "যে ব্যক্তি নফল রোযা রাখে, তার জন্য তা ভাঙ্গা বা দিনের যে কোন অংশে তা ছেড়ে দেওয়া বৈধ। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘নফল রোযাদার নিজের আমীর। ইচ্ছা হলে সে রোযা থাকতে পারে, আবার ইচ্ছা না হলে সে তা ভাঙ্গতেও পারে।’’[1]\n\n"
                        + "মা আয়েশা (রাঃ) বলেন, একদা নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁর নিকট এসে জিজ্ঞাসা করলেন, ‘‘তোমাদের কাছে (খাবার) কিছু আছে কি?’’ বললেন, ‘জী না।’ মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তখন বললেন, ‘‘তাহলে আজকে আমি রোযা থাকলাম।’’ অতঃপর আর একদিন আমাদেরকে হাইস (খেজুর, পনীর ও ঘি একত্রিত করে প্রস্ত্তত খাদ্য বিশেষ) উপহার দেওয়া হয়েছিল। আমি তার থেকে কিছু অংশ তাঁর জন্য লুকিয়ে রেখেছিলাম। আর তিনি হাইস ভালোবাসতেন। আমি বললাম, ‘হে আল্লাহর রসূল! আজ আমাদেরকে হাইস উপহার দেওয়া হয়েছে। আর আমি আপনার জন্য কিছুটা লুকিয়ে রেখেছি।’ তিনি বললেন, ‘‘আমার কাছে নিয়ে এস। আমি সকাল থেকে রোযা অবস্থায় ছিলাম।’’ এ কথা বলে তিনি তা খেলেন এবং বললেন, ‘‘নফল রোযাদারের উদাহরণ ঐ লোকের মত যে নিজ মাল থেকে (নফল) সাদকাহ বের করে। অতঃপর সে চাইলে তা দান করে, না চাইলে রেখে নেয়।’’[2]\n\n"
                        + "রোযা ভেঙ্গে ফেললে তা কাযা করা ওয়াজেব নয়। আবূ সাঈদ খুদরী (রাঃ) বলেন, একদা আমি আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর জন্য খাবার তৈরী করলাম। তিনি তাঁর অন্যান্য সহচর সহ আমার বাড়িতে এলেন। অতঃপর যখন খাবার সামনে রাখা হল, তখন দলের মধ্যে একজন বলল, ‘আমার রোযা আছে।’ তা শুনে আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘তোমাদের ভাই তোমাদেরকে দাওয়াত দিয়ে খরচ (বা কষ্ট) করেছে।’’ অতঃপর তিনি তার উদ্দেশ্যে বললেন, ‘‘রোযা ভেঙ্গে দাও। আর চাইলে তার বিনিময়ে অন্য একদিন রোযা রাখ।’’[3]\n\n"
                        + "[1] (আহমাদ, মুসনাদ ৬/৩৪১, তিরমিযী, হাকেম, মুস্তাদ্রাক ১/৪৩৯, বাইহাকী ৪/২৭৬ প্রমুখ, সহীহুল জামে’ ৩৮৫৪নং)\n\n"
                        + "[2] (আহমাদ, মুসনাদ ৬/৪৯, ২০৭, মুসলিম ১১৫৪, আবূ দাঊদ ২৪৫৫, নাসাঈ ২৩২১, ইবনে মাজাহ ১৭০১, ইবনে খুযাইমাহ, সহীহ ২১৪১নং, দারাকুত্বনী, সুনান, বাইহাকী ৪/২৭৫)\n\n"
                        + "[3] (বাইহাকী ৪/২৭৯, ত্বাবারানী, মু’জাম, ইরওয়াউল গালীল, আলবানী ১৯৫২নং)",
                "Whoever observes a voluntary (Nafl) fast is permitted to break it or discontinue it at any point during the day without sin. For the Prophet (peace and blessings be upon him) said: ''The voluntary faster is his own master: if he wishes, he may continue fasting; and if he wishes, he may break it.''[1]\n\n"
                        + "Mother Aisha (may Allah be pleased with her) narrated: Once the Prophet (peace and blessings be upon him) entered my home and asked: ''Do you have anything (to eat)?'' I said: 'No.' The Prophet (peace and blessings be upon him) said: ''Then I am fasting today.'' On another day, we were gifted Hais (a sweet delicacy made from dates, dried cheese/curd, and clarified butter). I had kept some hidden for him, as he was fond of Hais. I said: 'O Messenger of Allah! We were gifted Hais today, and I set aside a portion for you.' He said: ''Bring it to me; I began my morning fasting.'' He then ate it and remarked: ''The similitude of the voluntary faster is like a man who brings out voluntary charity (Sadaqah) from his wealth: if he wishes, he gives it away; and if he wishes, he withholds it.''[2]\n\n"
                        + "If one breaks a voluntary fast, making it up (Qadha) is not obligatory. Abu Sa'id al-Khudri (may Allah be pleased with him) narrated: 'Once I prepared a meal for the Messenger of Allah (peace and blessings be upon him). He visited my home along with some companions. When food was served, one of the men in the company said: I am fasting. Hearing this, the Messenger of Allah (peace and blessings be upon him) said: ''Your brother has invited you and expended expense and effort for you.'' Then he told the fasting man: ''Break your fast, and if you wish, fast another day in its place.''\n\n"
                        + "[1] (Musnad Ahmad 6/341; Jami' at-Tirmidhi; Al-Hakim in Al-Mustadrak 1/439; Al-Bayhaqi 4/276; Sahih al-Jami' #3854 by Al-Albani)\n\n"
                        + "[2] (Musnad Ahmad 6/49, 207; Sahih Muslim #1154; Sunan Abi Dawud #2455; Sunan an-Nasa'i #2321; Sunan Ibn Majah #1701; Sahih Ibn Khuzaymah #2141; Sunan ad-Daraqutni; Al-Bayhaqi 4/275)\n\n"
                        + "[3] (Al-Bayhaqi 4/279; At-Tabarani; Irwa' al-Ghalil #1952 by Al-Albani)"
        ));

        return list;
    }
}
