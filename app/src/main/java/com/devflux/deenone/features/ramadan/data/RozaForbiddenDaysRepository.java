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
 * Repository for যে দিনগুলিতে রোযা রাখা নিষিদ্ধ (Days on Which Fasting is Forbidden).
 * Pre-seeded with all 11 unique cards verbatim matching user prompt.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaForbiddenDaysRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaForbiddenDaysRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_forbidden_days.php");
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

        // Card 1: দুই ঈদের দিন
        list.add(new HajjHistoryCardItem(
                1,
                "দুই ঈদের দিন",
                "The Two Days of Eid",
                "মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) কোন হিকমত ও যুক্তির ফলে মুসলিমকে কতকগুলি দিনে রোযা রাখতে নিষেধ করেছেন। সমস্ত উলামা এ ব্যাপারে একমত যে, উভয় ঈদের দিন রোযা রাখা হারাম...",
                "Fasting on either of the two Eid days (Eid al-Fitr and Eid al-Adha) is strictly prohibited and unlawful by consensus of all scholars...",
                "মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) কোন হিকমত ও যুক্তির ফলে মুসলিমকে কতকগুলি দিনে রোযা রাখতে নিষেধ করেছেন। সেই দিনগুলি পরবর্তীতে আলোচিত হল।<br><br>"
                        + "সমস্ত উলামা এ ব্যাপারে একমত যে, উভয় ঈদের দিন রোযা রাখা হারাম। তাতে সে রোযা ফরয হোক; যেমন রমাযানের কাযা বা নযরের রোযা, অথবা নফল হোক। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) ঐ দিনে রোযা রাখতে নিষেধ করেছেন।[1]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (দ্রঃ বুখারী ১৯৯০, ১৯৯১, ১৯৯৩, ১৯৯৫, মুসলিম ৮২৭, ১১৩৭, ১১৩৮, ১১৪০)</font>",
                "All Islamic scholars are unanimous that fasting on the two days of Eid—Eid al-Fitr and Eid al-Adha—is strictly forbidden (Haram), whether as an obligatory fast, a make-up fast, a vowed fast, or a voluntary fast, because the Prophet (ﷺ) explicitly forbade fasting on these days.<br><br>"
                        + "<b>References:</b> Sahih al-Bukhari 1990, 1991, 1993, 1995; Sahih Muslim 827, 1137, 1138, 1140."
        ));

        // Card 2: তাশরীকের তিন দিন
        list.add(new HajjHistoryCardItem(
                2,
                "তাশরীকের তিন দিন",
                "The Three Days of Tashreeq",
                "ঈদুল আযহার পরবর্তী ৩ দিন রোযা রাখা বৈধ নয়। কেননা, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তাশরীকের দিনগুলো পানাহার ও আল্লাহর যিক্র করার দিন।’’...",
                "Fasting during the three days following Eid al-Adha (11th, 12th, and 13th of Dhul Hijjah) is forbidden, as they are days of eating, drinking, and remembering Allah...",
                "ঈদুল আযহার পরবর্তী ৩ দিন রোযা রাখা বৈধ নয়। কেননা, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তাশরীকের দিনগুলো পানাহার ও আল্লাহর যিক্র করার দিন।’’[1]<br><br>"
                        + "যে ব্যক্তির প্রত্যেক সোম ও বৃহস্পতিবার রোযা রাখা অভ্যাস আছে এবং তা যদি তাশরীকের কোন দিন পড়ে, তাহলে তার জন্যও ঐ রোযা রাখা বৈধ নয়। কারণ, সুন্নত কাজ করে হারাম-বিধান লংঘন করা যাবে না।[2]<br><br>"
                        + "অবশ্য যে (অমক্কাবাসী) হাজী মিনায় হজ্জের হাদ্ই (কুরবানী) দিতে সক্ষম হয় না, তার জন্য ঐ দিনগুলিতে বিনিমেয় রোযা রাখা বৈধ।<br><br>"
                        + "মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(فَمَنْ تَمَتَّعَ بِالْعُمْرَةِ إِلَى الْحَجِّ فَمَا اسْتَيْسَرَ مِنَ الْهَدْيِ، فَمَنْ لَّمْ يَجِدْ فَصِيَامُ ثَلاَثَةِ أَيَّامٍ فِي الْحَجِّ وَسَبْعَةٍ إِذَا رَجَعْتُمْ، تِلْكَ عَشَرَةٌ كَامِلَةٌ)</b></font><br><br>"
                        + "অর্থাৎ, সুতরাং তোমাদের মধ্যে যে ব্যক্তি হজ্জের আগে উমরাহ করে হালাল হয়ে লাভবান হতে (তামাত্তু হজ্জ করতে) চায় সে সহজলভ্য কুরবানী পেশ করবে। কিন্তু যদি কেউ কুরবানী না পায়, তাহলে তাকে হজ্জের সময় ৩দিন এবং ঘরে ফিরে ৭দিন এই পূর্ণ ১০দিন রোযা পালন করতে হবে। (কুরআনুল কারীম ২/১৯৬)<br><br>"
                        + "আয়েশা ও ইবনে উমার (রাঃ) বলেন, ‘যে হাজী হাদ্ই দিতে অপারগ সে ছাড়া আর কারো জন্য তাশরীকের দিনগুলিতে রোযা রাখার অনুমতি নেই।’[3]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আহমাদ, মুসনাদ ৪/১৫২, ৫/৭৫, ৭৬, ২২৪, মুসলিম ১১৪১, ১১৪২, সুনানে আরবাআহ; আবূ দাঊদ, তিরমিযী, নাসাঈ ও ইবনে মাজাহ)<br>"
                        + "[2] (আসইলাতুন অআজবিবাতুন ফী সবলাতিল ঈদাঈন ২৩পৃঃ)<br>"
                        + "[3] (বুখারী ১৯৯৭, ১৯৯৮নং)</font>",
                "Fasting on the three days following Eid al-Adha (Days of Tashreeq: 11th, 12th, and 13th of Dhul Hijjah) is impermissible, as the Prophet (ﷺ) declared that they are days of eating, drinking, and remembering Allah.<br><br>"
                        + "The only exception granted by Shariah is for the pilgrim performing Tamattu' or Qiran Hajj who cannot afford sacrificial livestock (Hady); such a pilgrim may fast these three days during Hajj.<br><br>"
                        + "<b>References:</b> Musnad Ahmad 4/152, Sahih Muslim 1141-1142, Sahih al-Bukhari 1997-1998, Quran 2:196."
        ));

        // Card 3: কেবল জুমআর দিন রোযা
        list.add(new HajjHistoryCardItem(
                3,
                "কেবল জুমআর দিন রোযা",
                "Fasting Exclusively on Friday",
                "জুমআর দিন হল মুসলিমদের সাপ্তাহিক ঈদ। তা ছাড়া এ দিন হল যিক্র ও ইবাদতের দিন। তাই তাতে সাহায্য নিতে এ দিনে রোযা না রাখা মুস্তাহাব...",
                "Singling out Friday specifically for voluntary fasting is disliked (Makruh) unless paired with a day before (Thursday) or after (Saturday)...",
                "জুমআর দিন হল মুসলিমদের সাপ্তাহিক ঈদ। তা ছাড়া এ দিন হল যিক্র ও ইবাদতের দিন। তাই তাতে সাহায্য নিতে এ দিনে রোযা না রাখা মুস্তাহাব। পক্ষান্তরে যদি কেউ জুমআর আগে একদিন অথবা পরে একদিন রোযা রাখে, অথবা তার অভ্যাসের কোন রোযা (যেমন শুক্লপক্ষের শেষ দিন) পড়ে, অথবা ঐ দিনে আরাফা বা আশূরার রোযা পড়ে, তাহলে তার জন্য সেদিনকার রোযা রাখা মকরূহ নয়।<br><br>"
                        + "এক জুমআর দিনে জুয়াইরিয়াহ বিনতে হারেষ রোযা রেখেছিলেন। মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁর নিকট এসে বললেন, ‘‘তুমি কি গতকাল রোযা রেখেছ?’’ তিনি বললেন, ‘জী না।’ নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘আগামী কাল রোযা রাখার ইচ্ছা আছে কি?’’ তিনি বললেন, ‘জী না।’ নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘তাহলে তুমি রোযা ভেঙ্গে ফেল।’’[1]<br><br>"
                        + "আবূ হুরাইরা (রাঃ) কর্তৃক বর্ণিত, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তোমাদের মধ্যে কেউ যেন জুমআর দিন রোযা না রাখে। অবশ্য যদি তার একদিন আগে অথবা পরে একটি রোযা রাখে, তাহলে তা রাখতে পারে।’’[2]<br><br>"
                        + "অন্য এক বর্ণনায় বলেন, ‘‘অন্যান্য রাত ছেড়ে জুমআর রাতকে কিয়ামের জন্য খাস করো না এবং অন্যান্য দিন ছেড়ে জুমআর দিনকে রোযার জন্য খাস করো না। অবশ্য কেউ তার অভ্যাসগত রোযা রাখলে ভিন্ন কথা।’’[3]<br><br>"
                        + "কাইস বিন সাকান বলেন, ‘আব্দুল্লাহর কিছু সঙ্গী-সাথী জুমআর দিনে রোযা রেখে আবূ যার্র (রাঃ)-এর নিকট গেলে তিনি তাদেরকে বললেন, ‘তোমাদের উপর কসম রইল! তোমরা অবশ্যই রোযা ভেঙ্গে ফেল। কারণ, জুমআহ হল ঈদের দিন।’[4]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আহমাদ, মুসনাদ, বুখারী ১৯৮৬, আবূ দাঊদ ২৪২২, নাসাঈ)<br>"
                        + "[2] (আহমাদ, মুসনাদ ২/৪৯৫, বুখারী ১৯৮৫, মুসলিম ১১৪৪, আবূ দাঊদ ২৪২০, তিরমিযী, ইবনে মাজাহ ১৭৭৩, ইবনে আবী শাইবাহ, মুসান্নাফ ৯২৪০নং, ইবনে খুযাইমাহ, সহীহ, বাইহাকী)<br>"
                        + "[3] (মুসলিম ১১৪৪নং)<br>"
                        + "[4] (ইবনে আবী শাইবাহ, মুসান্নাফ ৯২৪৪নং)</font>",
                "Friday is the weekly Eid for Muslims and a day devoted to supplication and remembrance. Singling it out specifically for voluntary fasting without pairing it with Thursday before it or Saturday after it is disliked (Makruh), unless it coincides with one's regular habit, Arafah, or Ashura.<br><br>"
                        + "<b>References:</b> Sahih al-Bukhari 1985-1986, Sahih Muslim 1144, Sunan Abi Dawud 2420."
        ));

        // Card 4: কেবল শনিবার রোযা রাখা
        list.add(new HajjHistoryCardItem(
                4,
                "কেবল শনিবার রোযা রাখা",
                "Fasting Exclusively on Saturday",
                "ফরয বা নির্দিষ্ট নফল (যেমনঃ অভ্যাসগত শুক্লপক্ষের দিন, আরাফা বা আশূরার) রোযা ছাড়া কেবল শনিবার সাধারণ অনির্দিষ্ট নফল রোযা রাখা বৈধ নয়...",
                "Singling out Saturday for unspecified voluntary fasting is impermissible or disliked unless it is an obligatory fast, habitual sunnah fast, or paired with Friday or Sunday...",
                "ফরয বা নির্দিষ্ট নফল (যেমনঃ অভ্যাসগত শুক্লপক্ষের দিন, আরাফা বা আশূরার) রোযা ছাড়া কেবল শনিবার সাধারণ অনির্দিষ্ট নফল রোযা রাখা বৈধ নয়। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তোমরা ফরয ছাড়া শনিবার রোযা রেখো না। তোমাদের কেউ যদি ঐ দিন আঙ্গুরের লতা বা গাছের ডাল ছাড়া অন্য কোন খাবার নাও পায়, তাহলে সে যেন তাই চিবিয়ে খায়।’’[1]<br><br>"
                        + "ত্বীবী বলেন, ‘ফরয’ বলতে রমাযানের ফরয রোযা, নযর মানা রোযা, কাযা রোযা, কাফ্ফারার রোযা এবং একই অর্থে সুন্নাতে মুআক্কাদাহ রোযা, যেমনঃ আরাফা, আশূরা এবং অভ্যাসগত (শুক্লপক্ষের দিনের) রোযা শামিল।[2] অর্থাৎ ঐসব রোযা অন্যান্য দলীলের ভিত্তিতে শনিবারে রাখতে নিষেধ নয়। যেহেতু তারীখের সাথে নির্দিষ্ট সুন্নত রোযাসমূহ যে কোন দিনেই রাখা যাবে।<br><br>"
                        + "যেমন তার আগে বা পরে একদিন রোযা রাখলে শনিবার রাখা বৈধ। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) জুয়াইরিয়াকে বললেন, ‘‘তুমি কি আগামী দিন (অর্থাৎ, শনিবার) রোযা রাখবে?’’ আর তার মানেই হল, শুক্র ও শনিবার রোযা রাখলে মকরূহ হবে না।[3]<br><br>"
                        + "এই দিনে রোযা রাখা নিষেধ হওয়ার পশ্চাতে যুক্তি ও হিকমত এই যে, ইয়াহুদীরা এই দিনের তা’যীম করত, এই দিন উপবাস করত এবং কাজ-কর্ম ছেড়ে ছুটি পালন করত। সুতরাং সেদিন রোযা রাখলে তাদের সাদৃশ্য প্রকাশ পায়। পক্ষান্তরে আগে বা পরে একদিন মিলিয়ে অথবা নযর বা কাযা রোযা রাখলে সেদিন রোযা রাখা মকরূহ হবে না।[4]<br><br>"
                        + "কিন্তু উম্মে সালামাহ (রাঃ) বলেন যে, ‘নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) শনিবার রোযা রাখতেন।’[5] বাহ্যতঃ এই হাদীসটি পূর্ববর্ণিত আমলের বিরোধী। তবুও সামঞ্জস্য সাধনের জন্য বলা যায় যে, যখন অবৈধকারী ও বৈধকারী দুটি হাদীস পরস্পর-বিরোধী হয়, তখন অবৈধকারী হাদীসকেই প্রাধান্য দেওয়া হয়। তদনুরূপ মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর কথা ও আমল পরস্পর-বিরোধী হলে তাঁর কথাকেই অগ্রাধিকার দেওয়া হবে। অতএব এই নীতির ভিত্তিতে কেবল শনিবার রোযা রাখা মকরূহ হবে।[6]<br><br>"
                        + "অথবা উম্মু সালামাহ (রাঃ) তাঁকে কোন অভ্যাসগত রোযা রাখতে দেখেছেন。<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আহমাদ, মুসনাদ ৪/১৮৯, ৬/৩৬৮, সহীহ আবূ দাঊদ ২১১৬, তিরমিযী ৫৯৪, সহীহ ইবনে মাজাহ, আলবানী ১৪০৩, ইবনে খুযাইমাহ, সহীহ ২১৬৪, দারেমী, সুনান ১৬৯৮নং)<br>"
                        + "[2] (তুহফাতুল আহওয়াযী ৩/৩৭২)<br>"
                        + "[3] (আশ্শারহুল মুমতে’ ৬/৪৬৬)<br>"
                        + "[4] (ফাইযুর রাহীমির রাহমান, ফী আহকামি অমাওয়াইযি রামাযান ৭৯পৃঃ)<br>"
                        + "[5] (আহমাদ, মুসনাদ ৬/৩২৩, ৩২৪, ইবনে খুযাইমাহ, সহীহ ২১৬৭, ইবনে হিববান, সহীহ ৯৪১নং, হাকেম, মুস্তাদ্রাক ১/৪৩৬, বাইহাকী ৪/৩১৩)<br>"
                        + "[6] (তামামুল মিন্নাহ, আল্লামা আলবানী ৪০৭পৃঃ)</font>",
                "Singling out Saturday solely for general voluntary fasting without reason is disliked because Jews venerate Saturday. However, if paired with Friday or Sunday, or if it coincides with Ramadan, Qadha, Kaffarah, Arafah, or Ashura, it is entirely permissible.<br><br>"
                        + "<b>References:</b> Musnad Ahmad 4/189, Sunan Abi Dawud 2116, Jami' at-Tirmidhi 594, Ash-Sharh al-Mumti' 6/466."
        ));

        // Card 5: সওমে বিসাল
        list.add(new HajjHistoryCardItem(
                5,
                "সওমে বিসাল",
                "Continuous Fasting (Sawm al-Wisal)",
                "মাঝে ইফতারী না করে এবং সেহরীও না খেয়ে একটানা দুই অথবা ততোধিক দিন রোযা রাখাকে ‘সওমে বিসাল’ বলা হয়। এই শ্রেণীর রোযা রাখতে আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) নিষেধ করেছেন...",
                "Sawm al-Wisal refers to continuous fasting for two or more consecutive days without breaking fast at sunset or eating Suhoor. The Prophet (ﷺ) strictly prohibited this practice...",
                "মাঝে ইফতারী না করে এবং সেহরীও না খেয়ে একটানা দুই অথবা ততোধিক দিন রোযা রাখাকে ‘সওমে বিসাল’ বলা হয়। এই শ্রেণীর রোযা রাখতে আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) নিষেধ করেছেন। যেহেতু তাতে রয়েছে অতিরঞ্জন এবং আত্মপীড়ন।<br><br>"
                        + "মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তোমরা ‘সওমে বিসাল’ থেকে দূরে থাক।’’ এ কথা তিনি ৩ বার পুনরাবৃত্তি করলেন। সাহাবাগণ বললেন, ‘কিন্তু হে আল্লাহর রসূল! আপনি তো বিসাল করে থাকেন?’ তিনি বললেন, ‘‘এ ব্যাপারে তোমরা আমার মত নও। কারণ, আমি রাত্রি যাপন করি, আর আমার প্রতিপালক আমাকে পানাহার করিয়ে থাকেন। সুতরাং তোমরা সেই আমল করতে উদ্বুদ্ধ হও, যা করতে তোমরা সক্ষম।’’[1]<br><br>"
                        + "অবশ্য ইফতারী না করে সেহরী খাওয়া পর্যন্ত ‘বিসাল’ করা চলে; যদি তাতে রোযাদারের কোন কষ্ট না হয়। যেহেতু আবূ সাঈদ খুদরী (রাঃ) কর্তৃক বর্ণিত, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘তোমরা ‘বিসাল’ করো না। কিন্তু যদি তোমাদের মধ্যে কেউ তা করতেই চায়, তাহলে সে সেহরী পর্যন্ত করুক।’’[2]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (বুখারী ১৯৬৬, মুসলিম ১১০৩নং, প্রমুখ)<br>"
                        + "[2] (বুখারী ১৯৬৭নং)</font>",
                "Continuous fasting without breaking fast in the evening or having Suhoor across consecutive days (Sawm al-Wisal) is forbidden because it causes self-harm and extremism in worship. If one wishes to extend, it is only permitted up to the pre-dawn meal (Suhoor).<br><br>"
                        + "<b>References:</b> Sahih al-Bukhari 1966-1967, Sahih Muslim 1103."
        ));

        // Card 6: কেবল রবিবার রোযা রাখা
        list.add(new HajjHistoryCardItem(
                6,
                "কেবল রবিবার রোযা রাখা",
                "Fasting Exclusively on Sunday",
                "কিছু উলামা কেবল রবিবার রোযা রাখাকে মকরূহ মনে করেছেন। কারণ, রবিবার হল খৃষ্টানদের ঈদ...",
                "Some scholars consider singling out Sunday alone for fasting to be disliked because Sunday is the sacred day of Christians...",
                "কিছু উলামা কেবল রবিবার রোযা রাখাকে মকরূহ মনে করেছেন। কারণ, রবিবার হল খৃষ্টানদের ঈদ। যেহেতু রোযা রাখাতে এক ধরনের দিনের তা’যীম প্রকাশ পায়। আর কাফেররা তাদের প্রতীক হিসাবে যার তা’যীম করে তার তা’যীম কোন মুসলিমের জন্য বৈধ নয়। পক্ষান্তরে তার সাথে তার পরের দিন একটি রোযা রাখলে আর মকরূহ থাকে না।[1] যেমন ঐ দিনে কোন নযর, কাযা, আরাফা বা আশূরার রোযা রাখা নিষেধ নয়。<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আশ্শারহুল মুমতে’ ৬/৪৬৭)</font>",
                "Singling out Sunday alone for voluntary fasting is considered disliked by some scholars because it resembles the Christian day of veneration. However, pairing it with Saturday or Monday, or observing Qadha/Arafah on it, removes any dislike.<br><br>"
                        + "<b>References:</b> Ash-Sharh al-Mumti' 6/467."
        ));

        // Card 7: সন্দেহের দিন রোযা
        list.add(new HajjHistoryCardItem(
                7,
                "সন্দেহের দিন রোযা",
                "Fasting on the Day of Doubt (Yawm ash-Shakk)",
                "সন্দেহের দিন হল ৩০শে শা’বান; যখন ২৯ তারিখে আকাশ ধূম্র বা মেঘাচ্ছন্ন থাকার ফলে চাঁদ দেখা সম্ভব হয় না। ১লা রমাযান কি না তা সন্দেহ করে পূর্বসতর্কতামূলক ভেবে ঐ দিন রোযা রাখা হারাম...",
                "The Day of Doubt (30th of Sha'ban when clouds obscure the moon) is forbidden to fast as a precaution for Ramadan...",
                "সন্দেহের দিন হল ৩০শে শা’বান; যখন ২৯ তারিখে আকাশ ধূম্র বা মেঘাচ্ছন্ন থাকার ফলে চাঁদ দেখা সম্ভব হয় না। পক্ষান্তরে ২৯ তারীখে আকাশ পরিষ্কার থাকলে ৩০ তারিখ সন্দেহের দিন থাকে না।<br><br>"
                        + "বলা বাহুল্য, ১লা রমাযান কি না তা সন্দেহ করে পূর্বসতর্কতামূলক কাজ ভেবে ঐ দিন রোযা রাখা হারাম। এ কথার দলীল হল আম্মার বিন ইয়াসের (রাঃ)-এর উক্তি, ‘যে ব্যক্তি সন্দেহের দিন রোযা রাখল, সে আসলে আবুল কাসেম (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর নাফরমানী করল।’[1]<br><br>"
                        + "মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তোমরা রমাযানের আগে আগে এক অথবা দুই দিনের রোযা রেখো না। অবশ্য তার অভ্যাসগত কোন রোযা হলে সে রাখতে পারে।’’[2]<br><br>"
                        + "আর যেহেতু সন্দেহের দিন রোযা রাখা মহান আল্লাহর শরীয়ত-গন্ডীর এক প্রকার সীমালংঘন। কারণ, মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ)</b></font><br><br>"
                        + "অর্থাৎ, তোমাদের মধ্যে যে কেউ এ মাস পাবে সে যেন এ মাসে রোযা রাখে। (কুরআনুল কারীম ২/১৮৫)<br><br>"
                        + "আর মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তোমরা চাঁদ দেখে রোযা রাখ এবং চাঁদ দেখে ঈদ কর। কিন্তু আকাশে মেঘ থাকলে শা’বানের গুনতি ৩০ পূর্ণ করে নাও।’’[3]<br><br>"
                        + "যে ব্যক্তি সন্দেহের সাথে ৩০শে শা’বান রোযা রাখে, অতঃপর বুঝতে পারে যে, সেদিন সত্য সত্যই ১লা রমাযান ছিল, সে ব্যক্তি এতদ্সত্ত্বেও ঐ দিনকার রোযা কাযা করবে। কারণ, সে আসলে ভিত্তিহীন রোযা রেখেছে। আর যে ব্যক্তি ভিত্তিহীন রোযা রাখে, তার রোযা যথেষ্ট নয়। সে তো আসলে চাঁদ না দেখে, চাঁদের অস্তিত্বের প্রমাণ না নিয়ে রোযা রেখেছে; যদিও প্রকৃতপক্ষে চাঁদ মেঘের আড়ালে বিদ্যমান ছিল।[4]<br><br>"
                        + "অবশ্য ঐ সন্দেহের দিন ৩০শে শা’বান যদি কেউ তার অভ্যাসগত রোযা (যেমন সোম অথবা বৃহস্পতিবার বলে) রাখে, তাহলে তা দূষণীয় নয়; যেমন সে কথা হাদীসেও স্পষ্টভাবে উল্লেখিত হয়েছে。<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (বুখারী বিনা সনদে ৩৭৬পৃঃ, আবূ দাঊদ ২৩৩৪নং, তিরমিযী, নাসাঈ, দারেমী, ইবনে হিববান, সহীহ, দারেমী, হাকেম, মুস্তাদ্রাক ১/৪২৪, বাইহাকী ৪/২০৮, ইরওয়াউল গালীল, আলবানী ৯৬১নং)<br>"
                        + "[2] (বুখারী ১৯১৪, মুসলিম ১০৮২নং)<br>"
                        + "[3] (বুখারী ১৯০০, মুসলিম ১০৮০নং)<br>"
                        + "[4] (ফিকহুস সুন্নাহ ১/৩৯৬, তাযকীরু ইবাদির রাহমান, ফীমা অরাদা বিসিয়ামি শাহরি রামাযান ৩৯পৃঃ)</font>",
                "Fasting on the Day of Doubt (the 30th of Sha'ban when the crescent is concealed by clouds on the 29th) out of precaution for Ramadan is strictly forbidden. The Prophet (ﷺ) prohibited preempting Ramadan with a day or two of fasting, except for someone observing their regular habitual fast.<br><br>"
                        + "<b>References:</b> Sunan Abi Dawud 2334, Sahih al-Bukhari 1900, 1914, Sahih Muslim 1080, 1082."
        ));

        // Card 8: বছরের প্রতিদিন রোযা রাখা
        list.add(new HajjHistoryCardItem(
                8,
                "বছরের প্রতিদিন রোযা রাখা",
                "Perpetual Fasting (Sawm ad-Dahr)",
                "নিষিদ্ধ দিনগুলি ছাড়া বছরের প্রতি দিন রোযা রাখা মকরূহ অথবা হারাম। কারণ, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘সে রোযা রাখল না, যে সমস্ত দিনগুলিতে রোযা রাখল।’’...",
                "Fasting continuously every single day of the year (Sawm ad-Dahr) is disliked or forbidden because it contradicts the Sunnah and constitutes undue hardship...",
                "নিষিদ্ধ দিনগুলি ছাড়া বছরের প্রতি দিন রোযা রাখা মকরূহ অথবা হারাম। কারণ, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘সে রোযা রাখল না, যে সমস্ত দিনগুলিতে রোযা রাখল।’’[1]<br><br>"
                        + "তিনি আরো বলেন, ‘‘যে ব্যক্তি প্রতিদিন রোযা রাখে, তার রোযা হয় না এবং সে পানাহারও করে না।’’[2]<br><br>"
                        + "তিনি আরো বলেন, ‘‘যে ব্যক্তি প্রতিদিন রোযা রাখে, তার প্রতি জাহান্নামকে এত সংকীর্ণ করা হয়, পরিশেষে তা এতটুকু হয়ে যায়।’’ আর এ কথা বলার সাথে সাথে তিনি তাঁর হাতের মুঠোকে বন্ধ করলেন।[3]<br><br>"
                        + "এখানে জাহান্নাম সংকীর্ণ হওয়ার অর্থ এই যে, জাহান্নামে তার বাসস্থান সংকীর্ণ হবে। যেহেতু সে নিজের জন্য কাঠিন্য পছন্দ করে, কষ্ট সত্ত্বেও তাতে নিজের আত্মাকে উদ্বুদ্ধ করে, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর আদর্শ থেকে বিমুখতা প্রকাশ করে এবং এই মনে করে যে, সে যা করছে তা তাঁর আদর্শ থেকে উত্তম![4]<br><br>"
                        + "পক্ষান্তরে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘শোন! আমি তোমাদের সবার চাইতে বেশী আল্লাহকে ভয় করে থাকি, তোমাদের সবার চাইতে আমার তাকওয়া বেশী। কিন্তু আমি রোযা রাখি, আবার তা ত্যাগও করি। রাতে নামায পড়ি, আবার ঘুমিয়েও থাকি। বিবাহ করে স্ত্রী-মিলনও করি। সুতরাং যে ব্যক্তি আমার সুন্নত-বিমুখ হবে, সে আমার দলভুক্ত নয়।’’[5]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (বুখারী ১৯৭৭, মুসলিম ১১৫৯নং প্রমুখ)<br>"
                        + "[2] (আহমাদ, মুসনাদ ৪/২৪, নাসাঈ, ইবনে মাজাহ ১৭০৫, ইবনে খুযাইমাহ, সহীহ ২১৫০নং, হাকেম, মুস্তাদ্রাক ১/৪৩৫, সহীহুল জামেইস সাগীর, আলবানী ৬৩২৩নং)<br>"
                        + "[3] (আহমাদ, মুসনাদ ৪/৪১৪, বাইহাকী ৪/৩০০, ইবনে খুযাইমাহ, সহীহ ২১৫৪, ২১৫৫নং)<br>"
                        + "[4] (দ্রঃ ফাতহুল বারী ৪/১৯৩, যামাঃ ২/৮৩)<br>"
                        + "[5] (বুখারী ৫০৬৩, মুসলিম ১৪০১নং, প্রমুখ)</font>",
                "Perpetual fasting throughout the entire year (Sawm ad-Dahr) is prohibited or severely disliked in Islam. The Prophet (ﷺ) warned: \"Whoever fasts perpetually has neither fasted nor broken his fast,\" cautioning against turning away from the moderate prophetic Sunnah.<br><br>"
                        + "<b>References:</b> Sahih al-Bukhari 1977, 5063, Sahih Muslim 1159, 1401, Musnad Ahmad 4/24."
        ));

        // Card 9: স্বামীর বর্তমানে স্ত্রীর রোযা রাখা
        list.add(new HajjHistoryCardItem(
                9,
                "স্বামীর বর্তমানে স্ত্রীর রোযা রাখা",
                "Voluntary Fasting by a Wife in Husband's Presence",
                "স্বামী-স্ত্রীর জীবন বড় মধুর, বড় যৌনসুখময় রোমাঞ্চকর। স্ত্রীর তুলনায় স্বামীই এ সুখ বেশী উপভোগ করে থাকে। তাই মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) মহিলাকে নিষেধ করলেন, যাতে স্বামী ঘরে থাকলে তার বিনা অনুমতিতে স্ত্রী রোযা না রাখে...",
                "A married woman may not observe voluntary fasts in the presence of her resident husband without his permission...",
                "স্বামী-স্ত্রীর জীবন বড় মধুর, বড় যৌনসুখময় রোমাঞ্চকর। স্ত্রীর তুলনায় স্বামীই এ সুখ বেশী উপভোগ করে থাকে। তাই মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) মহিলাকে নিষেধ করলেন, যাতে স্বামী ঘরে থাকলে তার বিনা অনুমতিতে স্ত্রী রোযা না রাখে।<br><br>"
                        + "আবূ হুরাইরা কর্তৃক বর্ণিত, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘মহিলা যেন স্বামীর বর্তমানে তার বিনা অনুমতিতে রমাযানের রোযা ছাড়া একটি দিনও রোযা না রাখে।’’[1]<br><br>"
                        + "উলামাগণ উক্ত নিষেধকে হারামের অর্থে ব্যবহার করেন। আর সে জন্যই বিনা অনুমতিতে স্ত্রী নফল রোযা রাখলে স্বামীর জন্য তা নষ্ট করে দেওয়া বৈধ মনে করেন। যেহেতু এটা স্বামীর প্রাপ্য হক এবং স্ত্রীর তরফ থেকে তার অধিকার হরণ। অবশ্য এ অধিকার কেবল নফল রোযায়, রমাযানের ফরয রোযার ক্ষেত্রে স্বামীর সে অধিকার থাকবে না। আর ফরয রোযা রাখতে স্ত্রীও স্বামীর অনুমতির অপেক্ষা করবে না।<br><br>"
                        + "পক্ষান্তরে স্বামী ঘরে না থাকলে তার বিনা অনুমতিতে স্ত্রী নফল রোযা রাখতে পারে। রোযা রাখার পর দিনের বেলায় স্বামী ঘরে ফিরলে, তার অধিকার আছে, সে স্ত্রীর রোযা নষ্ট করতে পারে।<br><br>"
                        + "অনুরূপভাবে স্বামী অসুস্থ অথবা সঙ্গমে অক্ষম হলেও স্ত্রী তার বিনা অনুমতিতে রোযা রাখতে পারে।[2]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আহমাদ, মুসনাদ ২/২৪৫, ৩১৬, বুখারী ৫১৯৫, মুসলিম ১০২৬, আবূ দাঊদ ২৪৫৮, তিরমিযী ৭৮২, ইবনে মাজাহ ১৭৬১, দারেমী, সুনান ১৬৭১নং, ইবনে হিববান, সহীহ ৯৫৪নং, হাকেম, মুস্তাদ্রাক ৪/১৭৩ প্রমুখ)<br>"
                        + "[2] (ফিকহুস সুন্নাহ ১/৩৯৭)</font>",
                "A married woman is not permitted to observe voluntary (Nafl) fasts while her husband is present at home without obtaining his permission, as marital intimacy is a spousal right. However, for obligatory Ramadan fasts, no spousal permission is required.<br><br>"
                        + "<b>References:</b> Sahih al-Bukhari 5195, Sahih Muslim 1026, Sunan Abi Dawud 2458, Fiqh as-Sunnah 1/397."
        ));

        // Card 10: রজব মাসের রোযা
        list.add(new HajjHistoryCardItem(
                10,
                "রজব মাসের রোযা",
                "Singling Out the Month of Rajab for Fasting",
                "খাস রজব মাসে রোযা রাখা মকরূহ। কারণ, তা জাহেলিয়াতের এক প্রতীক। জাহেলী যুগের লোকেরাই এ মাসের তা’যীম করত...",
                "Singling out the entire month of Rajab specifically for fasting is disliked (Makruh) because it was a custom of Jahiliyyah...",
                "খাস রজব মাসে রোযা রাখা মকরূহ। কারণ, তা জাহেলিয়াতের এক প্রতীক। জাহেলী যুগের লোকেরাই এ মাসের তা’যীম করত। পক্ষান্তরে সুন্নাহতে এর তা’যীমের ব্যাপারে কিছু বর্ণিত হয় নি। আর এ মাসের নামায ও রোযার ব্যাপারে যা কিছু বর্ণনা করা হয়ে থাকে, তার সবটাই মিথ্যা।[1]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আশ্শারহুল মুমতে’ ৬/৪৭৬)</font>",
                "Singling out Rajab specifically with fasting or unique night prayers is disliked (Makruh) because it was a venerated custom in pre-Islamic Jahiliyyah, and no authentic Hadiths establish specific fasts for Rajab.<br><br>"
                        + "<b>References:</b> Ash-Sharh al-Mumti' 6/476."
        ));

        // Card 11: শবেবরাতের রোযা
        list.add(new HajjHistoryCardItem(
                11,
                "শবেবরাতের রোযা",
                "Fasting on the 15th of Sha'ban (Shab-e-Barat)",
                "১৫ই শা’বানকে শবেবরাত বলা ভুল। যেমন তার রোযাও বিদআত। কারণ, এ ব্যাপারে কোন সহীহ হাদীস বর্ণিত হয় নি...",
                "Singling out the 15th of Sha'ban for a dedicated fast is an innovation (Bid'ah) unsupported by authentic Sunnah, unless part of habitual Ayyam al-Beed fasts...",
                "১৫ই শা’বানকে শবেবরাত বলা ভুল। যেমন তার রোযাও বিদআত। কারণ, এ ব্যাপারে কোন সহীহ হাদীস বর্ণিত হয় নি। অবশ্য অভ্যাসগতভাবে মাসের ৩টি রোযার ১টি ঐ দিনে হলে দোষাবহ নয়।",
                "Designating the 15th of Sha'ban exclusively for fasting with belief in special virtues is not established by any authentic Hadith and is considered an innovation (Bid'ah). However, observing it as part of the normal three white days (Ayyam al-Beed: 13th, 14th, 15th) is permissible."
        ));

        return list;
    }
}
