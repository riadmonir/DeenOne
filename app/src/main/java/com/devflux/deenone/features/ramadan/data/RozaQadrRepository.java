package com.devflux.deenone.features.ramadan.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.ramadan.model.RozaQadrTopicItem;
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
 * 3-Tier Production Repository for: section: লাইলাতুল কদর (Laylatul Qadr).
 * 0ms instant memory cache with background async REST API sync (Rule 11).
 * 100% matches screenshot topics:
 * 1. লাইলাতুল কদর
 * 2. লাইলাতুল কদরের বৈশিষ্ট্য
 * 3. লাইলাতুল কদর খুঁজে পাওয়ার গুরুত্ব
 * 4. লাইলাতুল কদরের ইবাদত
 * 5. লাইলাতুল কদরের ফজিলত
 * 6. কদরের রাতকে ফলপ্রসূ করার উপায়
 * 7. লাইলাতুল কদরের সাথে সম্পর্কিত ইসলামিক বিষয়
 */
public final class RozaQadrRepository {

    private static final List<RozaQadrTopicItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultTopics());
    }

    private RozaQadrRepository() {}

    public interface DataCallback {
        void onDataLoaded(List<RozaQadrTopicItem> items);
    }

    public static List<RozaQadrTopicItem> getAllTopics(Context context, DataCallback callback) {
        if (context != null && NetworkConnectivityHelper.isOnline(context)) {
            final Context appContext = context.getApplicationContext();
            executor.execute(() -> fetchRemoteTopics(appContext, callback));
        }
        return new ArrayList<>(cachedList);
    }

    public static List<RozaQadrTopicItem> getAllTopics() {
        return new ArrayList<>(cachedList);
    }

    public static List<RozaQadrTopicItem> getTopics(Context context, DataCallback callback) {
        return getAllTopics(context, callback);
    }

    public static RozaQadrTopicItem getTopicBySlug(String slug) {
        if (slug == null) return null;
        synchronized (cachedList) {
            for (RozaQadrTopicItem item : cachedList) {
                if (slug.equalsIgnoreCase(item.getSlug())) {
                    return item;
                }
            }
        }
        return null;
    }

    public static List<RozaQadrTopicItem> getDefaultTopics() {
        List<RozaQadrTopicItem> list = new ArrayList<>();

        // 1. লাইলাতুল কদর
        list.add(new RozaQadrTopicItem(
                1,
                "qadr_intro",
                1,
                "লাইলাতুল কদর",
                "Laylatul Qadr",
                "লাইলাতুল কদর",
                "Laylatul Qadr",
                "লাইলাতুল কদর বা শবে কদর হলো রমজান মাসের সবচেয়ে বরকতময় ও গুরুত্বপূর্ণ রাত, যা আল্লাহ তাআলা বিশেষ ফজিলত ও মর্যাদা দিয়েছেন। এটি কুরআনে এবং হাদিসে অসংখ্যবার উল্লেখিত হয়েছে এবং মুসলিম উম্মাহর জন্য এটি আত্মশুদ্ধি ও ইবাদতের জন্য ...",
                "Laylatul Qadr or Shab-e-Qadr is the most blessed and significant night of the month of Ramadan, which Allah Almighty has granted extraordinary virtue and honor. It is mentioned abundantly in the Quran and Hadith...",
                "লাইলাতুল কদর বা শবে কদর হলো রমজান মাসের সবচেয়ে বরকতময় ও গুরুত্বপূর্ণ রাত, যা আল্লাহ তাআলা বিশেষ ফজিলত ও মর্যাদা দিয়েছেন। এটি কুরআনে এবং হাদিসে অসংখ্যবার উল্লেখিত হয়েছে এবং মুসলিম উম্মাহর জন্য এটি আত্মশুদ্ধি ও ইবাদতের জন্য এক বিশেষ সুযোগ।\n\n"
                        + "লাইলাতুল কদর শব্দটির অর্থ:\nলাইলাতুল: আরবি শব্দ, যার অর্থ রাত।\nকদর: অর্থ মর্যাদা, তাকদীর বা সম্মান।\nএটি এমন এক রাত, যখন আল্লাহ তাআলা কুরআন নাযিল করেন এবং এই রাতে বান্দার জন্য বিশেষ রহমত, মাগফিরাত (ক্ষমা), এবং বরকত প্রেরণ করেন।\n\n"
                        + "লাইলাতুল কদর সম্পর্কে কুরআনের বর্ণনা:\nলাইলাতুল কদর সম্পর্কে কুরআনে একটি সম্পূর্ণ সূরা নাযিল হয়েছে, যার নাম সূরা আল-কদর (৯৭)। আল্লাহ তাআলা এর মধ্যে এর ফজিলত বর্ণনা করেছেন,\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ  وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ  لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ  تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ  سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "অর্থ:\nনিশ্চয় আমরা কুরআন নাযিল করেছি লাইলাতুল কদরে। এবং আপনি কি জানেন লাইলাতুল কদর কী? লাইলাতুল কদর এক হাজার মাসের চেয়েও উত্তম। এই রাতে ফেরেশতারা ও জিবরাইল (আ.) তাদের প্রভুর আদেশে নেমে আসেন প্রতিটি বিষয়ে। শান্তি, যা ফজর পর্যন্ত বিরাজ করে।\n[সূরা আল-কদর: ১-৫]\n\n"
                        + "এক হাজার মাসের চেয়েও উত্তম:\nআল্লাহ তাআলা বলেন,\n\n"
                        + "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "অর্থ:\nলাইলাতুল কদর এক হাজার মাসের চেয়েও উত্তম।\n[সূরা আল-কদর: ৩]\n\n"
                        + "১,০০০ মাস মানে প্রায় ৮৩ বছর ৪ মাস। এ রাতে ইবাদত করলে এত দীর্ঘ সময়ের ইবাদতের সাওয়াব পাওয়া যায়।\n\n"
                        + "কুরআন নাযিলের রাত:\nলাইলাতুল কদর এমন একটি রাত, যখন আল্লাহ তাআলা পবিত্র কুরআন নাযিল করেছেন।\nআল্লাহ বলেন, \n\n"
                        + "اِنَّاۤ اَنۡزَلۡنٰهُ فِیۡ لَیۡلَۃٍ مُّبٰرَكَۃٍ اِنَّا كُنَّا مُنۡذِرِیۡنَ ﴿۳﴾\n\n"
                        + "অর্থ:\nনিশ্চয় আমি এ (কুরআন) অবতীর্ণ করেছি এক বরকতময় (আশিসপূত শবেকদর) রাতে\n[সূরা আদ-দুখান: ৩]\n\n"
                        + "ফেরেশতাদের অবতরণ ও রহমত:\nএই রাতে অসংখ্য ফেরেশতা, বিশেষত প্রধান ফেরেশতা জিবরাইল (আ.), পৃথিবীতে অবতরণ করেন।\n\n"
                        + "تَنَزَّلُ الۡمَلٰٓئِكَۃُ وَ الرُّوۡحُ فِیۡهَا بِاِذۡنِ رَبِّهِمۡ ۚ مِنۡ كُلِّ اَمۡرٍ ۙ﴿ۛ৪﴾\n\n"
                        + "অর্থ:\nঐ রাত্রিতে ফিরিশতাগণ ও রূহ (জিবরীল) অবতীর্ণ হয় প্রত্যেক কাজে তাদের প্রতিপালকের অনুমতিক্রমে।\n[সূরা আল-কদর: ৪]\n\n"
                        + "মাগফিরাতের সুযোগ রাসুলুল্লাহ ﷺ বলেছেন:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ.\n\n"
                        + "অর্থ: \nযে ব্যক্তি লাইলাতুল কদরে ঈমান ও সওয়াবের আশায় ইবাদত করবে, তার পূর্ববর্তী সমস্ত গুনাহ মাফ করে দেওয়া হবে।\n[সহিহ বুখারি: ১৯০১, সহিহ মুসলিম: ৭৬০]\n\n"
                        + "আল্লাহর বিশেষ রহমত ও শান্তি:\nএ রাতে পৃথিবীতে আল্লাহর রহমত ছড়িয়ে পড়ে এবং এটি ফজর পর্যন্ত স্থায়ী থাকে",
                "Laylatul Qadr (or Shab-e-Qadr) is the most blessed and significant night of the month of Ramadan, which Allah Almighty has endowed with exceptional virtue and honor. It is mentioned repeatedly in the Quran and Hadith, providing a unique opportunity for spiritual purification and devoted worship for the Muslim Ummah.\n\n"
                        + "Meaning of the Term Laylatul Qadr:\nLaylatul: An Arabic word meaning night.\nQadr: Meaning honor, destiny, or dignity.\nIt is a night wherein Allah Almighty revealed the Quran and sends forth special mercy, forgiveness (maghfirah), and divine blessings upon His servants.\n\n"
                        + "Description of Laylatul Qadr in the Quran:\nAn entire chapter of the Quran was revealed concerning Laylatul Qadr, named Surah Al-Qadr (97). In it, Allah Almighty depicts its immense glory:\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ  وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ  لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ  تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ  سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "Meaning:\n\"Indeed, We revealed the Quran on the Night of Decree. And what can make you know what is the Night of Decree? The Night of Decree is better than a thousand months. The angels and the Spirit descend therein by permission of their Lord for every matter. Peace it is until the emergence of dawn.\"\n[Surah Al-Qadr: 1-5]\n\n"
                        + "Better Than a Thousand Months:\nAllah Almighty states:\n\n"
                        + "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "Meaning:\n\"The Night of Decree is better than a thousand months.\"\n[Surah Al-Qadr: 3]\n\n"
                        + "One thousand months equals approximately 83 years and 4 months. Performing devotion on this night earns the recompense of continuous worship across this lengthy duration.\n\n"
                        + "The Night of the Quran's Descent:\nLaylatul Qadr is the auspicious night on which Allah Almighty revealed the Noble Quran. Allah states:\n\n"
                        + "اِنَّاۤ اَنۡزَلۡنٰهُ فِیۡ لَیۡلَۃٍ مُّبٰرَكَۃٍ اِنَّا كُنَّا مُنۡذِرِیۡنَ ﴿۳﴾\n\n"
                        + "Meaning:\n\"Indeed, We sent it down during a blessed night. Indeed, We were to warn.\"\n[Surah Ad-Dukhan: 3]\n\n"
                        + "Descent of the Angels and Divine Mercy:\nOn this night, countless angels descend to earth, particularly the chief archangel Gabriel (Jibreel AS):\n\n"
                        + "تَنَزَّلُ الۡمَلٰٓئِكَۃُ وَ الرُّوۡحُ فِیۡهَا بِاِذۡنِ رَبِّهِمۡ ۚ مِنۡ كُلِّ اَمۡرٍ ۙ﴿ۛ৪﴾\n\n"
                        + "Meaning:\n\"The angels and the Spirit descend therein by permission of their Lord for every matter.\"\n[Surah Al-Qadr: 4]\n\n"
                        + "Immense Opportunity for Forgiveness:\nThe Messenger of Allah ﷺ stated:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "Meaning:\n\"Whoever stands in prayer during Laylatul Qadr with faith and seeking reward, all his previous sins will be forgiven.\"\n[Sahih al-Bukhari: 1901, Sahih Muslim: 760]\n\n"
                        + "Allah's Special Mercy and Tranquility:\nThroughout this glorious night, Allah's abundant mercy cascades over the world and abides continuously until dawn",
                "সূরা আল-কদর: ১-৫; সূরা আদ-দুখান: ৩; সহীহ বুখারী: ১৯০১; সহীহ মুসলিম: ৭৬০",
                "Surah Al-Qadr: 1-5; Surah Ad-Dukhan: 3; Sahih al-Bukhari: 1901; Sahih Muslim: 760"
        ));

        // 2. লাইলাতুল কদরের বৈশিষ্ট্য
        list.add(new RozaQadrTopicItem(
                2,
                "qadr_characteristics",
                2,
                "লাইলাতুল কদরের বৈশিষ্ট্য",
                "Characteristics of Laylatul Qadr",
                "লাইলাতুল কদরের বৈশিষ্ট্য",
                "Characteristics of Laylatul Qadr",
                "লাইলাতুল কদর (শবে কদর) হলো রমজান মাসের এমন একটি বিশেষ রাত, যা আল্লাহ তাআলা অসাধারণ ফজিলত, বরকত, এবং রহমতে ভরপুর করেছেন। এটি এমন একটি রাত, যা হাজার মাসের চেয়েও উত্তম। কুরআন ও হাদিসে এই রাতের অনেক বৈশিষ্ট্য উল্লে...",
                "Laylatul Qadr (Shab-e-Qadr) is such a special night in the month of Ramadan that Allah Almighty has filled with extraordinary virtue, blessing, and mercy. It is a night superior to a thousand months. Quran and Hadith mention numerous...",
                "লাইলাতুল কদর (শবে কদর) হলো রমজান মাসের এমন একটি বিশেষ রাত, যা আল্লাহ তাআলা অসাধারণ ফজিলত, বরকত, এবং রহমতে ভরপুর করেছেন। এটি এমন একটি রাত, যা হাজার মাসের চেয়েও উত্তম। কুরআন ও হাদিসে এই রাতের অনেক বৈশিষ্ট্য উল্লেখিত হয়েছে।\n\n"
                        + "নিচে লাইলাতুল কদরের প্রধান বৈশিষ্ট্যগুলো বর্ণনা করা হলো:\n\n"
                        + "কুরআন নাজিলের রাত:\nলাইলাতুল কদরের সবচেয়ে বড় বৈশিষ্ট্য হলো এই রাতে পবিত্র কুরআন নাজিল হয়েছে। আল্লাহ তাআলা বলেন:\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ\n\n"
                        + "অর্থ:\nনিশ্চয় আমরা কুরআন নাজিল করেছি লাইলাতুল কদরে।\n[সূরা আল-কদর: ১]\n\n"
                        + "এটি এমন এক রাত, যখন কুরআনের প্রথম ওহি নাজিলের মাধ্যমে মানবজাতির জন্য হেদায়াতের দরজা উন্মুক্ত হয়। এটি মানব জীবনের জন্য সবচেয়ে বড় আশীর্বাদ।\n\n"
                        + "হাজার মাসের চেয়েও উত্তম:\nলাইলাতুল কদরকে এক হাজার মাসের চেয়েও উত্তম বলা হয়েছে।\nআল্লাহ তাআলা বলেন:\n\n"
                        + "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "অর্থ:\nলাইলাতুল কদর এক হাজার মাসের চেয়েও উত্তম।\n[সূরা আল-কদর: ৩]\n\n"
                        + "১,০০০ মাস প্রায় ৮৩ বছর ৪ মাস, যা একটি দীর্ঘ সময়। এ রাতে ইবাদত করলে এত বছরের ইবাদতের সাওয়াব অর্জন করা যায়।\n\n"
                        + "ফেরেশতাদের অবতরণ:\nলাইলাতুল কদরের আরেকটি বৈশিষ্ট্য হলো, এ রাতে অসংখ্য ফেরেশতা পৃথিবীতে অবতরণ করেন।\nআল্লাহ তাআলা বলেন:\n\n"
                        + "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ\n\n"
                        + "অর্থ:\nএই রাতে ফেরেশতারা এবং জিবরাইল (আ.) তাদের প্রভুর আদেশে অবতরণ করেন প্রতিটি বিষয়ে।\n[সূরা আল-কদর: ৪]\n\n"
                        + "এই ফেরেশতারা পৃথিবীতে রহমত, বরকত, এবং শান্তি নিয়ে আসেন এবং মুমিনদের জন্য বিশেষ দোয়া করেন।\n\n"
                        + "তাকদীর নির্ধারণের রাত:\nলাইলাতুল কদরের অর্থ \"তাকদীরের রাত\"।\nআল্লাহ তাআলা এই রাতে এক বছরের জন্য মানবজাতির জীবন, রিজিক, মৃত্যু ও অন্যান্য গুরুত্বপূর্ণ বিষয়ে সিদ্ধান্ত দেন। আল্লাহ বলেন:\n\n"
                        + "فِيهَا يُفْرَقُ كُلُّ أَمْرٍ حَكِيمٍ\n\n"
                        + "অর্থ:\nএই রাতে প্রত্যেক গুরুত্বপূর্ণ বিষয় নির্ধারণ করা হয়।\n[সূরা আদ-দুখান: ৪]\n\n"
                        + "এটি এমন এক রাত, যখন আল্লাহর অশেষ জ্ঞান এবং কুদরতের আলোকে মানবজীবনের নির্ধারিত পরিকল্পনা গৃহীত হয়।\n\n"
                        + "শান্তি ও বরকতে পূর্ণ রাত:\nলাইলাতুল কদর এমন এক রাত, যা শুরু থেকে শেষ পর্যন্ত শান্তি এবং বরকতে পরিপূর্ণ। আল্লাহ তাআলা বলেন:\n\n"
                        + "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "অর্থ:\nএটি এমন শান্তিময় রাত, যা ফজর পর্যন্ত অব্যাহত থাকে।\n[সূরা আল-কদর: ৫]\n\n"
                        + "এ রাতে আল্লাহর রহমত, শান্তি, এবং ক্ষমা পৃথিবীতে বর্ষিত হয়। এটি এমন এক সময়, যখন মুমিন বান্দারা আল্লাহর সান্নিধ্য লাভের বিশেষ সুযোগ পান।\n\n"
                        + "গুনাহ মাফের বিশেষ সুযোগ:\nলাইলাতুল কদর এমন একটি রাত, যখন আল্লাহ তাআলা বান্দার সমস্ত গুনাহ ক্ষমা করে দেন, যদি বান্দা আন্তরিকতার সঙ্গে ইবাদত করে। রাসুলুল্লাহ ﷺ বলেছেন:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "অর্থ:\nযে ব্যক্তি ঈমান ও সওয়াবের প্রত্যাশায় লাইলাতুল কদরে ইবাদত করবে, তার পূর্ববর্তী গুনাহ মাফ করে দেওয়া হবে।\n[সহিহ বুখারি: ১৯০১, সহিহ মুসলিম: ৭৬০]\n\n"
                        + "ইবাদতের রাত:\nলাইলাতুল কদর ইবাদতের জন্য সর্বোত্তম রাত। এ রাতে কুরআন তিলাওয়াত, দোয়া, জিকির, নফল নামাজ এবং তওবা করা বিশেষ গুরুত্বপূর্ণ। আল্লাহর নৈকট্য অর্জন এবং রহমত লাভের জন্য এই রাতকে ইবাদতে কাটানো সুন্নত।\n\n"
                        + "বিশেষ দোয়ার রাত:\nএই রাত আল্লাহর কাছে প্রার্থনার জন্য বিশেষ সুযোগ। রাসুলুল্লাহ ﷺ এই রাতে বিশেষ দোয়া করতে বলেছেন। আয়েশা (রা.) জিজ্ঞাসা করেছিলেন, \"ইয়া রাসুলুল্লাহ! যদি আমি লাইলাতুল কদর পাই, তাহলে কী দোয়া করবো?\" তিনি ﷺ বললেন:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "অর্থ:\nহে আল্লাহ! আপনি ক্ষমাশীল, আপনি ক্ষমাকে ভালোবাসেন, সুতরাং আমাকে ক্ষমা করুন।\n[তিরমিজি: ৩৫১৩, ইবন মাজাহ: ৩৮৫০]\n\n"
                        + "রমজানের শেষ দশকের একটি রাত:\nলাইলাতুল কদর রমজানের শেষ দশকের বিজোড় রাতগুলোর মধ্যে একটি। রাসুলুল্লাহ ﷺ বলেছেন:\n\n"
                        + "تَحَرَّوْا لَيْلَةَ الْقَدْرِ فِي الْوِتْرِ مِنَ الْعَشْرِ الْأَوَاخِرِ مِنْ رَمَضَانَ\n\n"
                        + "অর্থ:\nতোমরা লাইলাতুল কদরকে রমজানের শেষ দশকের বিজোড় রাতগুলোতে অনুসন্ধান করো।\n[সহিহ বুখারি: ২০১৭, সহিহ মুসলিম: ১১৬৯]\n\n"
                        + "২৭তম রাতকে লাইলাতুল কদর হওয়ার সবচেয়ে সম্ভাব্য দিন হিসেবে ধরা হয়। তবে এটি ২১, ২৩, ২৫, ২৭, বা ২৯ তারিখের রাতেও হতে পারে",
                "Laylatul Qadr (Shab-e-Qadr) is such a special night in the blessed month of Ramadan that Allah Almighty has enriched with supreme virtues, blessings, and divine mercy. It is a night superior to a thousand months. The Quran and Hadith mention numerous profound characteristics of this night.\n\n"
                        + "The primary characteristics of Laylatul Qadr are detailed below:\n\n"
                        + "Night of the Quran's Revelation:\nThe greatest attribute of Laylatul Qadr is that the Holy Quran was revealed on this auspicious night. Allah Almighty states:\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ\n\n"
                        + "Meaning:\n\"Indeed, We sent the Quran down during the Night of Decree.\"\n[Surah Al-Qadr: 1]\n\n"
                        + "It is the night when the descent of the initial revelation inaugurated the decisive doorway of divine guidance for all humanity—the greatest blessing upon human life.\n\n"
                        + "Superior to a Thousand Months:\nLaylatul Qadr is declared superior to a thousand months. Allah Almighty states:\n\n"
                        + "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "Meaning:\n\"The Night of Decree is better than a thousand months.\"\n[Surah Al-Qadr: 3]\n\n"
                        + "One thousand months equals approximately 83 years and 4 months. Devotion on this single night earns the reward of worship spanning that extensive duration.\n\n"
                        + "Descent of the Angels:\nAnother wondrous distinction of Laylatul Qadr is that myriad angels descend upon the earth. Allah Almighty says:\n\n"
                        + "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ\n\n"
                        + "Meaning:\n\"The angels and the Spirit descend therein by permission of their Lord for every matter.\"\n[Surah Al-Qadr: 4]\n\n"
                        + "These noble angels bring down divine mercy, barakah, and tranquility, supplicating specifically for the devout believers.\n\n"
                        + "Night of Determining Destiny:\nLaylatul Qadr signifies the \"Night of Divine Decree\". On this night, Allah decrees the provisions, lifespans, deaths, and major affairs of humankind for the coming year. Allah states:\n\n"
                        + "فِيهَا يُفْرَقُ كُلُّ أَمْرٍ حَكِيمٍ\n\n"
                        + "Meaning:\n\"Therein every wise matter is made distinct.\"\n[Surah Ad-Dukhan: 4]\n\n"
                        + "It is the profound night when human destinies are dispatched according to Allah's infinite wisdom and sovereignty.\n\n"
                        + "Night Filled with Peace and Blessings:\nLaylatul Qadr remains completely enveloped in peace, tranquility, and divine grace from sunset until dawn. Allah states:\n\n"
                        + "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "Meaning:\n\"Peace it is until the emergence of dawn.\"\n[Surah Al-Qadr: 5]\n\n"
                        + "Allah's mercy and forgiveness cascade continuously upon creation throughout this night, granting believers unmatched closeness to their Creator.\n\n"
                        + "Special Occasion for Forgiveness of Sins:\nLaylatul Qadr is a night wherein Allah pardons all sins of the servant who worships with sincere faith. The Messenger of Allah ﷺ said:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "Meaning:\n\"Whoever stands in prayer during Laylatul Qadr with faith and seeking reward, all his previous sins will be forgiven.\"\n[Sahih al-Bukhari: 1901, Sahih Muslim: 760]\n\n"
                        + "The Ultimate Night of Devotion:\nLaylatul Qadr is the supreme night for worship. Engaging in Quranic recitation, earnest supplications, Dhikr, voluntary prayers, and heartfelt repentance holds profound virtue.\n\n"
                        + "Night of Special Supplication:\nThis night is a prime occasion to beseech Allah. The Prophet ﷺ taught a special dua. Aisha (RA) asked: \"O Messenger of Allah! If I encounter Laylatul Qadr, what should I supplicate?\" He ﷺ replied:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "Meaning:\n\"O Allah! Indeed You are Most Forgiving; You love to forgive, so forgive me.\"\n[Jami' at-Tirmidhi: 3513, Sunan Ibn Majah: 3850]\n\n"
                        + "A Night Among the Odd Nights of the Last Ten Days:\nLaylatul Qadr occurs among the odd nights of Ramadan's final ten days. The Messenger of Allah ﷺ said:\n\n"
                        + "تَحَرَّوْا لَيْلَةَ الْقَدْرِ فِي الْوِتْرِ مِنَ الْعَشْرِ الْأَوَاخِرِ مِنْ رَمَضَانَ\n\n"
                        + "Meaning:\n\"Seek Laylatul Qadr among the odd-numbered nights of the last ten nights of Ramadan.\"\n[Sahih al-Bukhari: 2017, Sahih Muslim: 1169]\n\n"
                        + "The 27th night is often regarded as the most probable, yet it may occur on any of the 21st, 23rd, 25th, 27th, or 29th nights",
                "সূরা আল-কদর: ১, ৩, ৪, ৫; সূরা আদ-দুখান: ৪; সহীহ বুখারী: ১৯০১, ২০১৭; সহীহ মুসলিম: ৭৬০, ১১৬৯; জামে আত-তিরমিযী: ৩৫১৩; সুনানে ইবনে মাজাহ: ৩৮৫০",
                "Surah Al-Qadr: 1, 3, 4, 5; Surah Ad-Dukhan: 4; Sahih al-Bukhari: 1901, 2017; Sahih Muslim: 760, 1169; Jami' at-Tirmidhi: 3513; Sunan Ibn Majah: 3850"
        ));

        // 3. লাইলাতুল কদর খুঁজে পাওয়ার গুরুত্ব
        list.add(new RozaQadrTopicItem(
                3,
                "qadr_seeking_importance",
                3,
                "লাইলাতুল কদর খুঁজে পাওয়ার গুরুত্ব",
                "Importance of Seeking Laylatul Qadr",
                "লাইলাতুল কদর খুঁজে পাওয়ার গুরুত্ব",
                "Importance of Seeking Laylatul Qadr",
                "লাইলাতুল কদর (শবে কদর) রমজানের শেষ দশকের বিজোড় রাতগুলোর মধ্যে এমন একটি রাত, যা হাজার মাসের চেয়েও উত্তম। এটি আত্মশুদ্ধি, ক্ষমা এবং জান্নাতের পথে অগ্রসর হওয়ার এক অনন্য সুযোগ। এই রাতের সঠিক ফজিলত বুঝে তা খুঁজে পাওয়া এবং ...",
                "Laylatul Qadr (Shab-e-Qadr) is such a night among the odd nights of the last ten days of Ramadan that is better than a thousand months. It is an extraordinary opportunity for self-purification, forgiveness, and advancing toward Paradise...",
                "লাইলাতুল কদর (শবে কদর) রমজানের শেষ দশকের বিজোড় রাতগুলোর মধ্যে এমন একটি রাত, যা হাজার মাসের চেয়েও উত্তম। এটি আত্মশুদ্ধি, ক্ষমা এবং জান্নাতের পথে অগ্রসর হওয়ার এক অনন্য সুযোগ। এই রাতের সঠিক ফজিলত বুঝে তা খুঁজে পাওয়া এবং এর ইবাদতে মনোনিবেশ করা ইসলামের দৃষ্টিতে অত্যন্ত গুরুত্বপূর্ণ। লাইলাতুল কদর নিয়ে আল্লাহ তাআলা পবিত্র কুরআনে একটি সম্পূর্ণ সূরা নাজিল করেছেন। সূরা আল-কদর-এ এ রাতের গুরুত্ব বর্ণিত হয়েছে:\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ ۝ وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ ۝ لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "অর্থ:\nনিশ্চয় আমরা কুরআন নাজিল করেছি লাইলাতুল কদরে। এবং আপনি কি জানেন লাইলাতুল কদর কী? লাইলাতুল কদর এক হাজার মাসের চেয়েও উত্তম।\n[সূরা আল-কদর: ১-৩]\n\n"
                        + "এই আয়াতগুলো থেকে স্পষ্ট হয় যে, এ রাতের ইবাদত ও আমল হাজার মাসের ইবাদত থেকে বেশি মর্যাদাপূর্ণ।\n\n"
                        + "লাইলাতুল কদর খুঁজে পাওয়ার গুরুত্ব:\n\n"
                        + "এক হাজার মাসের ইবাদতের সওয়াব লাভের সুযোগ:\n• লাইলাতুল কদর এমন এক রাত, যা ৮৩ বছর ৪ মাসের ইবাদতের চেয়েও বেশি সওয়াব দেয়। এটি এমন এক দয়া ও রহমতের রাত, যা আল্লাহ তাআলা আমাদের জীবনে বারবার প্রদান করেন।\n• এ রাতে ইবাদতকারীর জন্য জান্নাতের পথ খুলে দেওয়া হয়।\n• এক রাতে এত সওয়াব অর্জন করা সম্ভব, যা আমাদের দীর্ঘ জীবনের ইবাদতের সমান বা তার থেকেও বেশি।\n\n"
                        + "গুনাহ মাফের সুযোগ:\nলাইলাতুল কদর গুনাহ মাফের রাত। যে ব্যক্তি এই রাতে আল্লাহর কাছে ক্ষমা প্রার্থনা করে, আল্লাহ তার সমস্ত গুনাহ ক্ষমা করে দেন। রাসুলুল্লাহ ﷺ বলেন:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "অর্থ:\nযে ব্যক্তি ঈমান ও সওয়াবের প্রত্যাশায় লাইলাতুল কদরে ইবাদত করবে, তার পূর্ববর্তী সমস্ত গুনাহ মাফ করে দেওয়া হবে।\n[সহিহ বুখারি: ১৯০১, সহিহ মুসলিম: ৭৬০]\n\n"
                        + "দুনিয়া ও আখিরাতের ভাগ্য নির্ধারণের রাত:\nলাইলাতুল কদর হলো তাকদীরের রাত, যখন আল্লাহ তাআলা এক বছরের জীবনের সমস্ত পরিকল্পনা এবং সিদ্ধান্ত নেন। আল্লাহ বলেন:\n\n"
                        + "فِيهَا يُفْرَقُ كُلُّ أَمْرٍ حَكِيمٍ\n\n"
                        + "অর্থ:\nএই রাতে প্রত্যেক গুরুত্বপূর্ণ বিষয় নির্ধারণ করা হয়।\n[সূরা আদ-দুখান: ৪]\n\n"
                        + "নির্দিষ্ট তারিখ লুকানো থাকার কারণ:\nলাইলাতুল কদরের সুনির্দিষ্ট দিন সম্পর্কে রাসুলুল্লাহ ﷺ বলেছেন যে এটি রমজানের শেষ দশকের বিজোড় রাতগুলোর মধ্যে একটি (২১, ২৩, ২৫, ২৭, বা ২৯)। রাসুলুল্লাহ ﷺ বলেন:\n\n"
                        + "تَحَرَّوْا لَيْلَةَ الْقَدْرِ فِي الْوِتْرِ مِنَ الْعَشْرِ الْأَوَاخِرِ مِنْ رَمَضَانَ\n\n"
                        + "অর্থ:\nতোমরা লাইলাতুল কদরকে রমজানের শেষ দশকের বিজোড় রাতগুলোতে অনুসন্ধান করো।\n[সহিহ বুখারি: ২০১৭, সহিহ মুসলিম: ১১৬৯]\n\n"
                        + "এটি রহমত ও বরকতের সন্ধানে মুমিনদের আত্মনিয়োগে উদ্বুদ্ধ করার জন্য।\n\n"
                        + "বিশেষ দোয়া করার নির্দেশ:\nআয়েশা (রা.) রাসুলুল্লাহ ﷺ কে জিজ্ঞাসা করেছিলেন: \"ইয়া রাসুলুল্লাহ! যদি আমি লাইলাতুল কদর পাই, তাহলে কী দোয়া করবো?\" তিনি ﷺ বললেন:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "অর্থ:\nহে আল্লাহ! নিশ্চয় আপনি ক্ষমাশীল, আপনি ক্ষমাকে ভালোবাসেন, সুতরাং আমাকে ক্ষমা করুন।\n[তিরমিজি: ৩৫১৩, ইবন মাজাহ: ৩৮৫০",
                "Laylatul Qadr (the Night of Decree) is such a night among the odd nights of the last ten days of Ramadan that is superior to a thousand months. It is an extraordinary opportunity for spiritual purification, divine forgiveness, and advancing toward Paradise. Comprehending the genuine virtues of this night and earnestly seeking it in worship is of supreme importance in Islam. Allah Almighty revealed an entire Surah in the Quran concerning Laylatul Qadr. In Surah Al-Qadr, the immense status of this night is described:\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ ۝ وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ ۝ لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "Meaning:\n\"Indeed, We sent the Quran down during the Night of Decree. And what can make you know what is the Night of Decree? The Night of Decree is better than a thousand months.\"\n[Surah Al-Qadr: 1-3]\n\n"
                        + "These verses clarify that worship and righteous deeds performed on this night are far more honorable than devotion across a thousand months.\n\n"
                        + "Significance of Seeking Laylatul Qadr:\n\n"
                        + "Opportunity to Attain Rewards of Devotion Spanning a Thousand Months:\n• Laylatul Qadr is a night that yields reward greater than continuous worship of 83 years and 4 months. It is a divine blessing and mercy that Allah bestows upon our lives repeatedly.\n• The gates of Paradise are opened wide for worshippers on this night.\n• Within a single night, it is possible to acquire merits equivalent to or surpassing a full lifetime of continuous worship.\n\n"
                        + "Immense Opportunity for Forgiveness of Sins:\nLaylatul Qadr is the supreme night of pardon. When a servant implores Allah's forgiveness on this night, Allah pardons all their sins. The Messenger of Allah ﷺ stated:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "Meaning:\n\"Whoever stands in prayer during Laylatul Qadr with faith and seeking reward, all his previous sins will be forgiven.\"\n[Sahih al-Bukhari: 1901, Sahih Muslim: 760]\n\n"
                        + "Night of Determining Worldly and Spiritual Destinies:\nLaylatul Qadr is the night of divine decrees, when Allah determines the provisions, lifespans, and significant affairs of mankind for the forthcoming year. Allah states:\n\n"
                        + "فِيهَا يُفْرَقُ كُلُّ أَمْرٍ حَكِيمٍ\n\n"
                        + "Meaning:\n\"Therein every wise matter is made distinct.\"\n[Surah Ad-Dukhan: 4]\n\n"
                        + "Wisdom Behind Concealing the Exact Date:\nRegarding the exact timing of Laylatul Qadr, the Messenger of Allah ﷺ explained that it lies among the odd-numbered nights of the final ten days of Ramadan (21st, 23rd, 25th, 27th, or 29th). The Prophet ﷺ said:\n\n"
                        + "تَحَرَّوْا لَيْلَةَ الْقَدْرِ فِي الْوِتْرِ مِنَ الْعَشْرِ الْأَوَاخِرِ مِنْ رَمَضَانَ\n\n"
                        + "Meaning:\n\"Seek Laylatul Qadr among the odd-numbered nights of the last ten nights of Ramadan.\"\n[Sahih al-Bukhari: 2017, Sahih Muslim: 1169]\n\n"
                        + "This intentional concealment motivates believers to dedicate themselves wholeheartedly throughout multiple nights in pursuit of divine mercy and grace.\n\n"
                        + "Injunction for Special Supplication:\nAisha (RA) inquired of the Messenger of Allah ﷺ: \"O Messenger of Allah! If I encounter Laylatul Qadr, what should I supplicate?\" He ﷺ replied:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "Meaning:\n\"O Allah! Indeed You are Most Forgiving; You love to forgive, so forgive me.\"\n[Jami' at-Tirmidhi: 3513, Sunan Ibn Majah: 3850",
                "সূরা আল-কদর: ১-৩; সূরা আদ-দুখান: ৪; সহীহ বুখারী: ১৯০১, ২০১৭; সহীহ মুসলিম: ৭৬০, ১১৬৯; জামে আত-তিরমিযী: ৩৫১৩; সুনানে ইবনে মাজাহ: ৩৮৫০",
                "Surah Al-Qadr: 1-3; Surah Ad-Dukhan: 4; Sahih al-Bukhari: 1901, 2017; Sahih Muslim: 760, 1169; Jami' at-Tirmidhi: 3513; Sunan Ibn Majah: 3850"
        ));

        // 4. লাইলাতুল কদরের ইবাদত
        list.add(new RozaQadrTopicItem(
                4,
                "qadr_worship",
                4,
                "লাইলাতুল কদরের ইবাদত",
                "Worship in Laylatul Qadr",
                "লাইলাতুল কদরের ইবাদত",
                "Worship in Laylatul Qadr",
                "লাইলাতুল কদর (শবে কদর) এমন একটি রাত, যা আল্লাহ তাআলা মুসলিম উম্মাহর জন্য বিশেষ রহমত, বরকত এবং মাগফিরাতের রাত হিসেবে নির্ধারণ করেছেন। এই রাতের ইবাদত হাজার মাসের ইবাদতের চেয়েও উত্তম। তাই এই রাতের ইবাদত অত্যন্ত গুরুত্বপূ...",
                "Laylatul Qadr (Shab-e-Qadr) is such a night that Allah Almighty has ordained as a night of special mercy, blessing, and forgiveness for the Muslim Ummah. Worship on this night is superior to devotion over a thousand months...",
                "লাইলাতুল কদর (শবে কদর) এমন একটি রাত, যা আল্লাহ তাআলা মুসলিম উম্মাহর জন্য বিশেষ রহমত, বরকত এবং মাগফিরাতের রাত হিসেবে নির্ধারণ করেছেন। এই রাতের ইবাদত হাজার মাসের ইবাদতের চেয়েও উত্তম। তাই এই রাতের ইবাদত অত্যন্ত গুরুত্বপূর্ণ। লাইলাতুল কদরে কীভাবে ইবাদত করা উচিত, সে সম্পর্কে নিচে বিস্তারিত আলোচনা করা হলো:\n\n"
                        + "লাইলাতুল কদরের ইবাদতের উদ্দেশ্য:\n• আল্লাহর সন্তুষ্টি অর্জন: এই রাতের ইবাদতের মূল লক্ষ্য হলো আল্লাহর নৈকট্য লাভ করা এবং তাঁর সন্তুষ্টি অর্জন করা।\n• গুনাহ থেকে মুক্তি চাওয়া: এই রাত হলো পাপ থেকে মুক্তি লাভের এবং আল্লাহর ক্ষমা অর্জনের এক বিশেষ সুযোগ।\n• আত্মশুদ্ধি ও তাকওয়া বৃদ্ধি: এই রাতে বেশি বেশি ইবাদত করে অন্তরকে পরিশুদ্ধ এবং আল্লাহর প্রতি ভক্তি আরও গভীর করা উচিত।\n\n"
                        + "বেশি বেশি নফল নামাজ আদায় করা:\nএই রাতে যত বেশি সম্ভব নফল নামাজ আদায় করা উচিত। রাসুলুল্লাহ ﷺ বলেছেন:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "অর্থ:\nযে ব্যক্তি ঈমান ও সওয়াবের প্রত্যাশায় লাইলাতুল কদরে ইবাদত করবে, তার পূর্ববর্তী সমস্ত গুনাহ মাফ করে দেওয়া হবে।\n[সহিহ বুখারি: ১৯০১, সহিহ মুসলিম: ৭৬০]\n\n"
                        + "নফল নামাজের নিয়ম:\n• দুই রাকাত করে যত ইচ্ছা পড়া যেতে পারে।\n• নামাজে দীর্ঘ কুরআন তিলাওয়াত করা সুন্নত।\n• সেজদায় বেশি বেশি দোয়া করা।\n\n"
                        + "বিশেষ দোয়া করা:\nলাইলাতুল কদরের ইবাদতে দোয়া করা অত্যন্ত গুরুত্বপূর্ণ। আয়েশা (রা.) জিজ্ঞাসা করেছিলেন:\n\"ইয়া রাসুলুল্লাহ! যদি আমি লাইলাতুল কদর পাই, তাহলে কী দোয়া করবো?\" তিনি ﷺ বলেন:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "অর্থ:\nহে আল্লাহ! নিশ্চয় আপনি ক্ষমাশীল, আপনি ক্ষমাকে ভালোবাসেন, সুতরাং আমাকে ক্ষমা করুন।\n[তিরমিজি: ৩৫১৩, ইবন মাজাহ: ৩৮৫০]\n\n"
                        + "এ দোয়া ছাড়াও নিজের এবং পরিবারের জন্য, মুসলিম উম্মাহর জন্য এবং আখিরাতের কল্যাণের জন্য দোয়া করা উচিত।\n\n"
                        + "কুরআন তিলাওয়াত করা:\nএই রাতে বেশি বেশি কুরআন তিলাওয়াত করা অন্যতম গুরুত্বপূর্ণ আমল।\n• কুরআন নাজিলের রাত হওয়ায় এই ইবাদতের বিশেষ তাৎপর্য রয়েছে।\n• তিলাওয়াতের পাশাপাশি কুরআনের অর্থ ও মর্ম বোঝার চেষ্টা করা উচিত।\n\n"
                        + "জিকির ও তাসবিহ পাঠ করা:\nএই রাতে বেশি বেশি জিকির করা আল্লাহর নৈকট্য লাভের একটি মাধ্যম। জিকির হিসেবে পড়তে পারেন:\n\n"
                        + "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ\n(সুবহানাল্লাহি ওয়া বিহামদিহি, সুবহানাল্লাহিল আজিম)\n\n"
                        + "لَا إِلَهَ إِلَّا اللَّهُ\n(লা ইলাহা ইল্লাল্লাহ)\n\n"
                        + "الْحَمْدُ لِلَّهِ\n(আলহামদুলিল্লাহ)\n\n"
                        + "اللَّهُ أَكْبَرُ\n(আল্লাহু আকবার)\n\n"
                        + "তওবা করা ও গুনাহের জন্য ক্ষমা প্রার্থনা করা:\nএই রাতে আন্তরিকতার সাথে আল্লাহর কাছে তওবা করুন। অতীতের সকল গুনাহের জন্য আল্লাহর কাছে ক্ষমা চান এবং ভবিষ্যতে গুনাহ না করার সংকল্প করুন। আল্লাহ তাআলা ক্ষমাশীল, তিনি তাঁর বান্দাদের প্রতি অত্যন্ত দয়ালু।\n\n"
                        + "অন্যদের জন্য দোয়া করা:\nনিজের জন্য দোয়া করার পাশাপাশি পরিবারের সদস্য, আত্মীয়স্বজন এবং মুসলিম উম্মাহর জন্য দোয়া করুন। মুসলিম উম্মাহর ঐক্য, শান্তি এবং কল্যাণের জন্য বিশেষ দোয়া করা উচিত।\n\n"
                        + "দরিদ্রদের সাহায্য করা (সদকা বা দান করা):\nলাইলাতুল কদরের বরকতময় রাতে গরিব-দুঃখীদের সাহায্য করা একটি উত্তম আমল। সদকা এবং খরচ করার মাধ্যমে আল্লাহর সন্তুষ্টি অর্জন করা সম্ভব।\n\n"
                        + "রাত জেগে ইবাদত করা:\nরাসুলুল্লাহ ﷺ রমজানের শেষ দশকে রাত জেগে ইবাদত করতেন। হাদিসে এসেছে:\n\n"
                        + "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا دَخَلَ الْعَشْرُ شَدَّ مِئْزَرَهُ، وَأَحْيَا لَيْلَهُ، وَأَيْقَظَ أَهْلَهُ\n\n"
                        + "অর্থ:\nযখন রমজানের শেষ দশক আসত, তখন তিনি কোমর বেঁধে (ইবাদতে মনোনিবেশ করতেন), রাত জাগতেন এবং পরিবারকেও জাগিয়ে তুলতেন।\n[সহিহ বুখারি: ২০২৪, সহিহ মুসলিম: ১১৭৪]\n\n"
                        + "মসজিদে ইতিকাফ করা:\nইতিকাফ করা একটি অত্যন্ত ফজিলতপূর্ণ আমল। যারা ইতিকাফ করেন, তারা পুরো সময় ইবাদতে ব্যস্ত থাকেন এবং লাইলাতুল কদর খুঁজে পান।\n\n"
                        + "পরিবারকে ইবাদতে উদ্বুদ্ধ করা:\nনিজের পরিবারের সদস্যদের জাগিয়ে তুলুন এবং তাদের লাইলাতুল কদরের ফজিলত সম্পর্কে অবগত করুন। পরিবারকে একসঙ্গে ইবাদতে অংশগ্রহণ করানো বরকতপূর্ণ।\n\n"
                        + "লাইলাতুল কদরের ইবাদত সহজ ও ফলপ্রসূ করার জন্য কিছু টিপস:\n• পরিকল্পনা: ইবাদতের জন্য আগে থেকেই পরিকল্পনা করুন।\n• শরীর সতেজ রাখা: রাত জেগে ইবাদত করার জন্য দিনের বেলায় কিছু বিশ্রাম নিন।\n• সুনির্দিষ্ট আমল নির্ধারণ: কোন ইবাদত কতক্ষণ করবেন, তা নির্ধারণ করে নিন (নফল নামাজ, কুরআন তিলাওয়াত, দোয়া ইত্যাদি)।\n• আন্তরিকতা: সব ইবাদত আন্তরিকতার সঙ্গে আল্লাহর সন্তুষ্টি অর্জনের উদ্দেশ্যে করুন।\n\n"
                        + "লাইলাতুল কদরের ফজিলত অর্জনের জন্য আল্লাহর প্রতি আশা ও ভালোবাসা:\nলাইলাতুল কদর এমন এক রাত, যা আল্লাহর দয়ার নিদর্শন। এই রাতের ইবাদত বান্দাকে আল্লাহর নৈকট্যে নিয়ে যায়। তাই ইবাদত করার সময় নিজের প্রতি আন্তরিকতা ও আল্লাহর দয়ার প্রতি আশাবাদী হওয়া জরুরি।\n\n"
                        + "উপসংহার:\nলাইলাতুল কদর হলো আল্লাহর রহমত, বরকত এবং ক্ষমা পাওয়ার রাত। এ রাতের ইবাদত শুধুমাত্র আমাদের আখিরাতের পাথেয় নয়, বরং আমাদের অন্তরের পরিশুদ্ধি এবং আল্লাহর সন্তুষ্টি অর্জনের একটি মাধ্যম",
                "Laylatul Qadr (the Night of Decree) is such a blessed night that Allah Almighty has designated as a night of special mercy, blessings, and forgiveness for the Muslim Ummah. Worship on this night is superior to devotion across a thousand months. Therefore, utilizing this night in devotion is exceptionally momentous. A comprehensive guide on how to observe worship in Laylatul Qadr is outlined below:\n\n"
                        + "Objectives of Worship in Laylatul Qadr:\n• Attaining the Pleasure of Allah: The core goal of worship on this night is to achieve nearness to Allah and attain His divine pleasure.\n• Seeking Absolution from Sins: This night represents an extraordinary opportunity to achieve freedom from sins and receive Allah's forgiveness.\n• Self-Purification and Spiritual Growth: By increasing acts of worship on this night, one should purify the heart and deepen reverence toward Allah.\n\n"
                        + "Performing Abundant Voluntary (Nafl) Prayers:\nOne should perform as many voluntary prayers as possible during this night. The Messenger of Allah ﷺ said:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "Meaning:\n\"Whoever stands in prayer during Laylatul Qadr with faith and seeking reward, all his previous sins will be forgiven.\"\n[Sahih al-Bukhari: 1901, Sahih Muslim: 760]\n\n"
                        + "Manners of Voluntary Prayers:\n• May be prayed in sets of two rak'ahs according to one's capacity.\n• Reciting lengthy Quranic passages during prayer is a Sunnah.\n• Offering abundant, heartfelt supplications in prostration (Sujood).\n\n"
                        + "Offering Special Supplications:\nSupplication is of prime significance in the worship of Laylatul Qadr. Aisha (RA) inquired:\n\"O Messenger of Allah! If I encounter Laylatul Qadr, what should I supplicate?\" He ﷺ said:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "Meaning:\n\"O Allah! Indeed You are Most Forgiving; You love to forgive, so forgive me.\"\n[Jami' at-Tirmidhi: 3513, Sunan Ibn Majah: 3850]\n\n"
                        + "In addition to this supplication, one should pray for oneself, family, the Muslim Ummah, and goodness in the Hereafter.\n\n"
                        + "Recitation of the Holy Quran:\nAbundant recitation of the Holy Quran on this night is among the most rewarded deeds.\n• Since the Quran was revealed on this night, this act of devotion carries profound significance.\n• Alongside recitation, one should endeavor to understand its meaning and inner message.\n\n"
                        + "Reciting Dhikr and Tasbih:\nFrequent remembrance of Allah on this night is an effective path to closeness with Him. Beneficial Dhikr includes:\n\n"
                        + "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ\n(Subhanallahi wa bihamdihi, Subhanallahil Azeem)\n\n"
                        + "لَا إِلَهَ إِلَّا اللَّهُ\n(La ilaha illallah)\n\n"
                        + "الْحَمْدُ লِلَّهِ\n(Alhamdulillah)\n\n"
                        + "اللَّهُ أَكْبَرُ\n(Allahu Akbar)\n\n"
                        + "Sincere Repentance and Seeking Forgiveness:\nRepent sincerely to Allah during this night. Seek forgiveness for all past misdeeds and make a firm resolve to avoid future sins. Allah is Most Forgiving and boundless in mercy.\n\n"
                        + "Supplicating for Others:\nAlong with praying for oneself, supplicate for family members, relatives, and the entire Muslim Ummah. Make special prayers for the unity, peace, and welfare of the Ummah.\n\n"
                        + "Helping the Needy (Giving Charity):\nAssisting the poor and distressed during the blessed night of Laylatul Qadr is an excellent virtue. Divine pleasure is attained through charitable contributions.\n\n"
                        + "Vigilant Nighttime Worship:\nThe Prophet ﷺ consistently observed night vigils during the last ten days of Ramadan. Hadith states:\n\n"
                        + "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا دَخَلَ الْعَشْرُ شَدَّ مِئْزَرَهُ، وَأَحْيَا لَيْلَهُ، وَأَيْقَظَ أَهْلَهُ\n\n"
                        + "Meaning:\n\"When the last ten days arrived, he tightened his waist belt, spent the night in prayer, and awakened his household.\"\n[Sahih al-Bukhari: 2024, Sahih Muslim: 1174]\n\n"
                        + "Observing Itikaf in the Mosque:\nItikaf is a deeply rewarded spiritual retreat. Those in Itikaf remain engaged in pure devotion throughout the period and attain Laylatul Qadr.\n\n"
                        + "Encouraging the Family Toward Worship:\nAwaken your household members and enlighten them regarding the virtues of Laylatul Qadr. Engaging the family collectively in worship is full of blessings.\n\n"
                        + "Practical Tips to Make Worship Easy and Fruitful:\n• Planning: Organize an actionable schedule for worship beforehand.\n• Physical Vitality: Take some daytime rest to stay refreshed during night vigils.\n• Scheduled Routine: Allocate specific time slots for voluntary prayer, Quran recitation, and supplication.\n• Sincerity: Perform every act of devotion purely for Allah's divine pleasure.\n\n"
                        + "Hope and Devotion Toward Allah:\nLaylatul Qadr exemplifies divine mercy. Worship on this night draws the servant near to Allah. Therefore, maintain deep sincerity and optimistic reliance on Allah's grace.\n\n"
                        + "Conclusion:\nLaylatul Qadr is a night for receiving divine mercy, blessings, and forgiveness. Worship on this night is not only provision for the Hereafter, but a transformative means of inner purification and divine acceptance",
                "সহীহ বুখারী: ১৯০১, ২০২৪; সহীহ মুসলিম: ৭৬০, ১১৭৪; জামে আত-তিরমিযী: ৩৫১৩; সুনানে ইবনে মাজাহ: ৩৮৫০",
                "Sahih al-Bukhari: 1901, 2024; Sahih Muslim: 760, 1174; Jami' at-Tirmidhi: 3513; Sunan Ibn Majah: 3850"
        ));

        // 5. লাইলাতুল কদরের ফজিলত
        list.add(new RozaQadrTopicItem(
                5,
                "qadr_virtues",
                5,
                "লাইলাতুল কদরের ফজিলত",
                "Virtues of Laylatul Qadr",
                "লাইলাতুল কদরের ফজিলত",
                "Virtues of Laylatul Qadr",
                "লাইলাতুল কদর (শবে কদর) হলো রমজান মাসের এমন এক বরকতময় রাত, যা আল্লাহ তাআলা মানবজাতিকে এক বিশেষ নিয়ামত হিসেবে প্রদান করেছেন। এটি হাজার মাসের চেয়েও উত্তম, এবং এই রাতে ইবাদতকারী বান্দাদের জন্য রয়েছে অশেষ সওয়াব, রহম...",
                "Laylatul Qadr (Shab-e-Qadr) is such a blessed night of Ramadan that Allah Almighty has bestowed upon humanity as an exceptional favor. It is better than a thousand months, offering boundless rewards, mercy, and forgiveness...",
                "লাইলাতুল কদর (শবে কদর) হলো রমজান মাসের এমন এক বরকতময় রাত, যা আল্লাহ তাআলা মানবজাতিকে এক বিশেষ নিয়ামত হিসেবে প্রদান করেছেন। এটি হাজার মাসের চেয়েও উত্তম, এবং এই রাতে ইবাদতকারী বান্দাদের জন্য রয়েছে অশেষ সওয়াব, রহমত, এবং ক্ষমার প্রতিশ্রুতি। কুরআন ও হাদিসে লাইলাতুল কদরের অসংখ্য ফজিলত বর্ণিত হয়েছে।\n\n"
                        + "লাইলাতুল কদর সম্পর্কে কুরআনের বর্ণনা:\n\n"
                        + "লাইলাতুল কদর সম্পর্কে আল্লাহ তাআলা সূরা আল-কদর (৯৭)-এ বলেন:\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ ۝ وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ ۝ لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ ۝ تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ ۝ سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "অর্থ:\nনিশ্চয় আমরা কুরআন নাজিল করেছি লাইলাতুল কদরে। এবং আপনি কি জানেন লাইলাতুল কদর কী? লাইলাতুল কদর এক হাজার মাসের চেয়েও উত্তম। এই রাতে ফেরেশতারা ও জিবরাইল (আ.) তাদের প্রভুর আদেশে নেমে আসেন প্রতিটি বিষয়ে। শান্তি, যা ফজর পর্যন্ত স্থায়ী থাকে।\n[সূরা আল-কদর: ১-৫]\n\n"
                        + "এই আয়াতগুলো থেকে বোঝা যায় যে, লাইলাতুল কদর অত্যন্ত মর্যাদাপূর্ণ একটি রাত, যা আল্লাহর বিশেষ রহমত ও বরকতে পরিপূর্ণ।\n\n"
                        + "এক হাজার মাসের চেয়েও উত্তম:\n\n"
                        + "লাইলাতুল কদরের প্রধান বৈশিষ্ট্য হলো, এ রাতের ইবাদত এক হাজার মাসের ইবাদতের চেয়েও বেশি সওয়াব দেয়। আল্লাহ তাআলা বলেন:\n\n"
                        + "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "অর্থ:\nলাইলাতুল কদর এক হাজার মাসের চেয়েও উত্তম।\n[সূরা আল-কদর: ৩]\n\n"
                        + "১,০০০ মাস মানে প্রায় ৮৩ বছর ৪ মাস। মানুষের জীবনের পুরো সময় ইবাদতে কাটানো কঠিন, কিন্তু এই এক রাতের ইবাদতের মাধ্যমে এত দীর্ঘ সময়ের সওয়াব অর্জন করা সম্ভব।\n\n"
                        + "তাকদীর নির্ধারণের রাত:\n\n"
                        + "লাইলাতুল কদর হলো এমন এক রাত, যখন মানুষের এক বছরের জীবন, রিজিক, মৃত্যু, এবং অন্যান্য গুরুত্বপূর্ণ বিষয় নির্ধারণ করা হয়। আল্লাহ তাআলা বলেন:\n\n"
                        + "فِيهَا يُفْرَقُ كُلُّ أَمْرٍ حَكِيمٍ\n\n"
                        + "অর্থ:\nএই রাতে প্রত্যেক গুরুত্বপূর্ণ বিষয় নির্ধারণ করা হয়।\n[সূরা আদ-দুখান: ৪]\n\n"
                        + "এটি এমন একটি রাত, যখন আল্লাহ তাআলা তাঁর অসীম জ্ঞান অনুযায়ী জীবনের যাবতীয় পরিকল্পনা ও সিদ্ধান্ত গ্রহণ করেন।\n\n"
                        + "শান্তি এবং বরকতে পূর্ণ রাত:\n\n"
                        + "লাইলাতুল কদর হলো শান্তি এবং বরকতের রাত, যা ফজর পর্যন্ত স্থায়ী থাকে। আল্লাহ তাআলা বলেন:\n\n"
                        + "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "অর্থ:\nশান্তি, যা ফজর পর্যন্ত স্থায়ী থাকে।\n[সূরা আল-কদর: ৫]\n\n"
                        + "এই রাতে আল্লাহর রহমত, ক্ষমা, এবং শান্তি বান্দার উপর বর্ষিত হয়। এটি এমন একটি রাত, যখন শয়তান কোনো ক্ষতি করতে পারে না এবং পুরো রাত আল্লাহর রহমত পরিব্যাপ্ত থাকে।\n\n"
                        + "গুনাহ মাফের সুযোগ:\n\n"
                        + "লাইলাতুল কদর হলো ক্ষমা এবং গুনাহ মাফের রাত। এই রাতে আন্তরিকতার সাথে ইবাদত করলে আল্লাহ তাআলা বান্দার সমস্ত গুনাহ ক্ষমা করে দেন। রাসুলুল্লাহ ﷺ বলেছেন:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "অর্থ:\nযে ব্যক্তি ঈমান ও সওয়াবের প্রত্যাশায় লাইলাতুল কদরে ইবাদত করবে, তার পূর্ববর্তী সমস্ত গুনাহ মাফ করে দেওয়া হবে।\n[সহিহ বুখারি: ১৯০১, সহিহ মুসলিম: ৭৬০]\n\n"
                        + "বিশেষ দোয়া এবং ইবাদতের সুযোগ:\n\n"
                        + "এই রাত বিশেষ দোয়া এবং ইবাদতের জন্য গুরুত্বপূর্ণ। আয়েশা (রা.) জিজ্ঞাসা করেছিলেন, \"ইয়া রাসুলুল্লাহ! যদি আমি লাইলাতুল কদর পাই, তাহলে কী দোয়া করবো?\" তিনি ﷺ বললেন:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "অর্থ:\nহে আল্লাহ! নিশ্চয় আপনি ক্ষমাশীল, আপনি ক্ষমাকে ভালোবাসেন, সুতরাং আমাকে ক্ষমা করুন।\n[তিরমিজি: ৩৫১৩, ইবন মাজাহ: ৩৮৫০]\n\n"
                        + "লাইলাতুল কদর একটি বিশেষ বরকতময় রাত, যা আল্লাহর রহমত, ক্ষমা, এবং দয়া লাভের জন্য এক অসাধারণ সুযোগ। এটি এক হাজার মাসের চেয়েও উত্তম, যেখানে ইবাদত ও দোয়ার মাধ্যমে বান্দা জান্নাতের পথে অগ্রসর হতে পারে",
                "Laylatul Qadr (the Night of Decree) is such a blessed night of Ramadan that Allah Almighty has bestowed upon humanity as an exceptional divine gift. It is superior to a thousand months, and for the worshippers of this night, there is the promise of boundless reward, mercy, and complete forgiveness. Numerous virtues of Laylatul Qadr are detailed in the Holy Quran and Hadith.\n\n"
                        + "Quranic Description of Laylatul Qadr:\n\n"
                        + "Regarding Laylatul Qadr, Allah Almighty states in Surah Al-Qadr (97):\n\n"
                        + "إِنَّا أَنزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ ۝ وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ ۝ لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ ۝ تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِم مِّن كُلِّ أَمْرٍ ۝ سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "Meaning:\n\"Indeed, We sent the Quran down during the Night of Decree. And what can make you know what is the Night of Decree? The Night of Decree is better than a thousand months. The angels and the Spirit descend therein by permission of their Lord for every matter. Peace it is until the emergence of dawn.\"\n[Surah Al-Qadr: 1-5]\n\n"
                        + "These verses elucidate that Laylatul Qadr is a profoundly honorable night, filled with Allah's special mercy and divine barakah.\n\n"
                        + "Better Than a Thousand Months:\n\n"
                        + "The primary distinction of Laylatul Qadr is that worship performed on this night brings greater reward than devotion spanning a thousand months. Allah Almighty states:\n\n"
                        + "لَيْلَةُ الْقَدْرِ خَيْرٌ مِّنْ أَلْفِ شَهْرٍ\n\n"
                        + "Meaning:\n\"The Night of Decree is better than a thousand months.\"\n[Surah Al-Qadr: 3]\n\n"
                        + "1,000 months equals roughly 83 years and 4 months. Spending an entire human lifespan continuously in worship is difficult, but through the worship of this single night, the merit of such an immense duration can be attained.\n\n"
                        + "Night of Determining Destinies:\n\n"
                        + "Laylatul Qadr is a night when the annual sustenance, life, death, and all critical affairs of mankind are apportioned. Allah Almighty states:\n\n"
                        + "فِيهَا يُفْرَقُ كُلُّ أَمْرٍ حَكِيمٍ\n\n"
                        + "Meaning:\n\"Therein every wise matter is made distinct.\"\n[Surah Ad-Dukhan: 4]\n\n"
                        + "It is a monumental night when Allah Almighty executes all annual plans and decrees in accordance with His infinite divine knowledge.\n\n"
                        + "A Night Filled with Peace and Blessing:\n\n"
                        + "Laylatul Qadr is a sanctuary of peace and blessing that endures until the dawn. Allah Almighty states:\n\n"
                        + "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ\n\n"
                        + "Meaning:\n\"Peace it is until the emergence of dawn.\"\n[Surah Al-Qadr: 5]\n\n"
                        + "On this night, Allah's mercy, forgiveness, and tranquility descend upon the servants. Satan is restrained from causing harm, and divine serenity envelops the entire night.\n\n"
                        + "Immense Opportunity for Expiation of Sins:\n\n"
                        + "Laylatul Qadr is a paramount night of forgiveness and redemption. When one worships with sincere devotion on this night, Allah forgives all their sins. The Messenger of Allah ﷺ said:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "Meaning:\n\"Whoever stands in prayer during Laylatul Qadr with faith and seeking reward, all his previous sins will be forgiven.\"\n[Sahih al-Bukhari: 1901, Sahih Muslim: 760]\n\n"
                        + "Opportunity for Special Supplication and Devotion:\n\n"
                        + "This night is momentous for earnest supplication and prayer. Aisha (RA) asked: \"O Messenger of Allah! If I encounter Laylatul Qadr, what should I supplicate?\" He ﷺ replied:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "Meaning:\n\"O Allah! Indeed You are Most Forgiving; You love to forgive, so forgive me.\"\n[Jami' at-Tirmidhi: 3513, Sunan Ibn Majah: 3850]\n\n"
                        + "Laylatul Qadr is a supremely blessed night presenting an extraordinary opportunity to attain Allah's mercy, forgiveness, and favor. It is better than a thousand months, paving the direct path for believers toward Paradise",
                "সূরা আল-কদর: ১-৫; সূরা আদ-দুখান: ৩-৪; সহীহ বুখারী: ১৯০১; সহীহ মুসলিম: ৭৬০; জামে আত-তিরমিযী: ৩৫১৩; সুনানে ইবনে মাজাহ: ৩৮৫০",
                "Surah Al-Qadr: 1-5; Surah Ad-Dukhan: 3-4; Sahih al-Bukhari: 1901; Sahih Muslim: 760; Jami' at-Tirmidhi: 3513; Sunan Ibn Majah: 3850"
        ));

        // 6. কদরের রাতকে ফলপ্রসূ করার উপায়
        list.add(new RozaQadrTopicItem(
                6,
                "qadr_fruitful_ways",
                6,
                "কদরের রাতকে ফলপ্রসূ করার উপায়",
                "Ways to Make the Night of Qadr Fruitful",
                "কদরের রাতকে ফলপ্রসূ করার উপায়",
                "Ways to Make the Night of Qadr Fruitful",
                "লাইলাতুল কদর (শবে কদর) হলো রমজানের শেষ দশকের বিজোড় রাতগুলোর মধ্যে একটি, যা হাজার মাসের ইবাদতের চেয়েও উত্তম। এটি আল্লাহ তাআলার রহমত, বরকত, এবং ক্ষমা লাভের বিশেষ সুযোগ। তাই এই রাতকে সঠিকভাবে ইবাদতে কাজে লাগা...",
                "Laylatul Qadr (Shab-e-Qadr) is one of the odd-numbered nights of the final ten days of Ramadan, which is superior to a thousand months of worship. It is a special divine opportunity to attain Allah's mercy, blessings, and forgive...",
                "লাইলাতুল কদর (শবে কদর) হলো রমজানের শেষ দশকের বিজোড় রাতগুলোর মধ্যে একটি, যা হাজার মাসের ইবাদতের চেয়েও উত্তম। এটি আল্লাহ তাআলার রহমত, বরকত, এবং ক্ষমা লাভের বিশেষ সুযোগ। তাই এই রাতকে সঠিকভাবে ইবাদতে কাজে লাগানো প্রত্যেক মুসলিমের জন্য অত্যন্ত গুরুত্বপূর্ণ। নিচে লাইলাতুল কদরের রাতকে ফলপ্রসূ করার কয়েকটি উপায় বর্ণনা করা হলো:\n\n"
                        + "পরিকল্পনা করে ইবাদতের জন্য প্রস্তুতি নেওয়া:\nলাইলাতুল কদর ফলপ্রসূ করতে আগে থেকে পরিকল্পনা করা জরুরি।\n\n"
                        + "সঠিক সময়ে বিশ্রাম নিন:\nরাতে জাগার জন্য দিনের বেলায় কিছুক্ষণ বিশ্রাম নিন।\n\n"
                        + "কোন ইবাদত করবেন তা ঠিক করুন:\nযেমন নফল নামাজ, কুরআন তিলাওয়াত, জিকির, দোয়া, এবং তওবা।\n\n"
                        + "মন স্থির রাখুন:\nইবাদতের জন্য নিজেকে মানসিক ও শারীরিকভাবে প্রস্তুত করুন।\n\n"
                        + "রাত জেগে ইবাদত করা:\nলাইলাতুল কদরের রাতে ইবাদত করা সুন্নত। রাসুলুল্লাহ ﷺ বলেন:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "অর্থ:\nযে ব্যক্তি ঈমান এবং সওয়াবের আশায় লাইলাতুল কদরে ইবাদত করবে, তার পূর্বের সমস্ত গুনাহ ক্ষমা করে দেওয়া হবে।\n[সহিহ বুখারি: ১৯০১, সহিহ মুসলিম: ৭৬০]\n\n"
                        + "নফল নামাজ:\n• দুই রাকাত করে যত বেশি সম্ভব নফল নামাজ পড়ুন।\n• নামাজে দীর্ঘ সূরা তিলাওয়াত করুন।\n• সেজদায় বেশি সময় কাটিয়ে আল্লাহর কাছে নিজের পাপের জন্য ক্ষমা চান।\n\n"
                        + "বিশেষ দোয়া করা:\nলাইলাতুল কদর দোয়ার রাত। রাসুলুল্লাহ ﷺ আয়েশা (রা.)-কে এই রাতে পড়ার জন্য বিশেষ দোয়া শিখিয়েছেন:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "অর্থ:\nহে আল্লাহ! নিশ্চয় আপনি ক্ষমাশীল, আপনি ক্ষমাকে ভালোবাসেন, সুতরাং আমাকে ক্ষমা করুন।\n[তিরমিজি: ৩৫১৩, ইবন মাজাহ: ৩৮৫০]\n\n"
                        + "• নিজের জন্য, পরিবারের জন্য এবং উম্মাহর জন্য দোয়া করুন।\n• আখিরাতের মুক্তি এবং জান্নাতের জন্য আল্লাহর কাছে আবেদন করুন।\n\n"
                        + "কুরআন তিলাওয়াত করা:\nলাইলাতুল কদর হলো কুরআন নাজিলের রাত। তাই এই রাতে বেশি করে কুরআন তিলাওয়াত করুন।\n• কুরআন পড়ার পাশাপাশি এর অর্থ ও ব্যাখ্যা বোঝার চেষ্টা করুন।\n• কুরআনের মাধ্যমে আল্লাহর কাছাকাছি যাওয়ার চেষ্টা করুন।\n\n"
                        + "তওবা করা এবং গুনাহের জন্য ক্ষমা চাওয়া:\nএই রাত হলো গুনাহ থেকে মুক্তি পাওয়ার জন্য বিশেষ সুযোগ।\n• আন্তরিকভাবে তওবা করুন এবং ভবিষ্যতে গুনাহ না করার সংকল্প করুন।\n• নিজের অতীতের ভুলগুলো নিয়ে আল্লাহর কাছে ক্ষমা চান।\n\n"
                        + "তওবার শব্দ:\n\n"
                        + "رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ\n\n"
                        + "অর্থ:\nহে আমার প্রতিপালক! আমাকে ক্ষমা করুন এবং আমার প্রতি রহম করুন। আপনি তো ক্ষমাশীল ও দয়ালু।\n\n"
                        + "জিকির ও তাসবিহ পাঠ করা:\nএই রাতে আল্লাহর নাম বেশি বেশি জপ করুন। কিছু গুরুত্বপূর্ণ জিকির:\nسُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ\n(সুবহানাল্লাহি ওয়া বিহামদিহি, সুবহানাল্লাহিল আজিম)\nلَا إِلَهَ إِلَّا اللَّهُ (লা ইলাহা ইল্লাল্লাহ)\nالْحَمْدُ لِلَّهِ (আলহামদুলিল্লাহ)\nاللَّهُ أَكْبَرُ (আল্লাহু আকবার)\nأَسْتَغْفِرُ اللَّهَ (আস্তাগফিরুল্লাহ)\nএছাড়াও আল্লাহর গুণবাচক নামগুলো (আসমাউল হুসনা) বারবার স্মরণ করুন।\n\n"
                        + "দান-সদকা করা:\nলাইলাতুল কদর হলো সদকা ও দানের জন্য গুরুত্বপূর্ণ রাত।\n• গরিব ও অসহায়দের সাহায্য করুন।\n• মসজিদ, মাদ্রাসা, বা অন্যান্য ইসলামিক প্রতিষ্ঠানে দান করুন।\n• দানের মাধ্যমে নিজের সম্পদকে পরিশুদ্ধ এবং আল্লাহর রহমত লাভের চেষ্টা করুন।\n\n"
                        + "পরিবারের সদস্যদের ইবাদতে যুক্ত করা:\nরাসুলুল্লাহ ﷺ রমজানের শেষ দশকে ইবাদতের জন্য নিজ পরিবারকে উৎসাহ দিতেন। হাদিসে এসেছে:\n\n"
                        + "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا دَخَلَ الْعَشْرُ شَدَّ مِئْزَرَهُ، وَأَحْيَا لَيْلَهُ، وَأَيْقَظَ أَهْلَهُ\n\n"
                        + "অর্থ:\nযখন রমজানের শেষ দশক আসত, তিনি কোমর বেঁধে ইবাদতে মনোনিবেশ করতেন, রাত জাগতেন এবং পরিবারের সদস্যদের জাগিয়ে তুলতেন।\n[সহিহ বুখারি: ২০২৪, সহিহ মুসলিম: ১১৭৪]\n\n"
                        + "ইতিকাফের মাধ্যমে ইবাদত করা:\nইতিকাফ হলো রমজানের শেষ দশকে মসজিদে অবস্থান করে ইবাদতে মনোনিবেশ করা। এটি লাইলাতুল কদর পাওয়ার একটি নিশ্চিত উপায়। যারা ইতিকাফ করেন, তারা রাত জেগে ইবাদত করেন এবং সম্পূর্ণ মনোযোগ আল্লাহর দিকে রাখেন।\n\n"
                        + "আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশ করা:\nলাইলাতুল কদর হলো আল্লাহর দেয়া এক বিশেষ নিয়ামত। এই রাতে আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশ করুন। তাঁর নিয়ামত ও দয়া স্মরণ করে শুকরিয়া আদায় করুন।\n\n"
                        + "রাতকে ফলপ্রসূ করার জন্য নিয়মিততা বজায় রাখা:\nলাইলাতুল কদরের নির্দিষ্ট রাত লুকিয়ে রাখা হয়েছে, যাতে মুমিনরা রমজানের শেষ দশকের সব বিজোড় রাতে ইবাদতে মনোযোগী হয়। ২১, ২৩, ২৫, ২৭ এবং ২৯ তারিখের রাতে বিশেষ ইবাদতের চেষ্টা করুন। ধৈর্য ধরে ইবাদতে লিপ্ত থাকুন",
                "Laylatul Qadr (the Night of Decree) is one of the odd-numbered nights of the final ten days of Ramadan, which is superior to a thousand months of worship. It is a special divine opportunity to attain Allah's mercy, blessings, and forgiveness. Therefore, utilizing this night properly in devotion is exceedingly important for every Muslim. Several ways to make the Night of Qadr fruitful are outlined below:\n\n"
                        + "Planning and Preparing for Worship:\nPlanning ahead is essential to make Laylatul Qadr truly fruitful.\n\n"
                        + "Rest at the Right Time:\nTake some rest during the daytime to prepare for staying awake at night.\n\n"
                        + "Decide Which Acts of Worship to Perform:\nSuch as voluntary prayers, Quranic recitation, Dhikr, supplications, and sincere repentance.\n\n"
                        + "Maintain Focus and Composure:\nPrepare yourself mentally and physically for dedicated devotion.\n\n"
                        + "Staying Awake at Night for Worship:\nVigilant worship during the night of Laylatul Qadr is a revered Sunnah. The Messenger of Allah ﷺ said:\n\n"
                        + "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n"
                        + "Meaning:\nWhoever stands in prayer during Laylatul Qadr with faith and seeking reward, all his past sins will be forgiven.\n[Sahih al-Bukhari: 1901, Sahih Muslim: 760]\n\n"
                        + "Voluntary (Nafl) Prayers:\n• Pray as many two-rak'ah voluntary prayers as possible.\n• Recite long Surahs in prayer with measured reflection.\n• Spend extended time in prostration (Sujood), imploring Allah for forgiveness.\n\n"
                        + "Offering Special Supplications:\nLaylatul Qadr is a night of supplication. The Messenger of Allah ﷺ taught Aisha (RA) a special dua to recite on this night:\n\n"
                        + "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي\n\n"
                        + "Meaning:\nO Allah! Indeed You are Most Forgiving, You love to forgive, so forgive me.\n[Jami' at-Tirmidhi: 3513, Sunan Ibn Majah: 3850]\n\n"
                        + "• Pray for yourself, your family, and the global Ummah.\n• Ask Allah for salvation in the Hereafter and admittance into Paradise.\n\n"
                        + "Reciting the Holy Quran:\nLaylatul Qadr is the night when the Quran was revealed. Hence, recite the Quran abundantly during this night.\n• Along with reading, endeavor to understand its meaning and contemplation.\n• Strive to draw closer to Allah through His divine words.\n\n"
                        + "Repenting and Seeking Forgiveness:\nThis night is a prime opportunity to obtain freedom from sins.\n• Repent sincerely and make a firm resolve not to repeat past sins.\n• Ask Allah's forgiveness for your past shortcomings.\n\n"
                        + "Words of Repentance:\n\n"
                        + "رَبِّ اغْفِرْ لِي وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ\n\n"
                        + "Meaning:\nO my Lord! Forgive me and accept my repentance; indeed You are the Accepter of Repentance, the Most Merciful.\n\n"
                        + "Reciting Dhikr and Tasbih:\nGlorify the names of Allah abundantly throughout this night. Essential Dhikr formulas:\nسُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ\n(Subhanallahi wa bihamdihi, Subhanallahil Azeem)\nلَا إِلَهَ إِلَّا اللَّهُ (La ilaha illallah)\nالْحَمْدُ لِلَّهِ (Alhamdulillah)\nاللَّهُ أَكْبَرُ (Allahu Akbar)\nأَسْتَغْفِرُ اللَّهَ (Astaghfirullah)\nAlso frequently contemplate and recite the Beautiful Names of Allah (Asmaul Husna).\n\n"
                        + "Giving in Charity (Sadaqah):\nLaylatul Qadr is a momentous night for charity and giving.\n• Support the poor and destitute.\n• Contribute to mosques, madrasahs, and Islamic welfare initiatives.\n• Purify your wealth and seek divine mercy through benevolent spending.\n\n"
                        + "Involving Family Members in Worship:\nThe Prophet ﷺ vigorously encouraged his family toward worship during the final ten days. It is narrated:\n\n"
                        + "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا دَخَلَ الْعَشْرُ شَدَّ مِئْزَرَهُ، وَأَحْيَا لَيْلَهُ، وَأَيْقَظَ أَهْلَهُ\n\n"
                        + "Meaning:\nWhen the last ten days entered, he tightened his waist belt, spent the night in prayer, and awakened his household.\n[Sahih al-Bukhari: 2024, Sahih Muslim: 1174]\n\n"
                        + "Worship Through Itikaf:\nItikaf involves secluding oneself in the mosque during the last ten days of Ramadan. It is the most reliable way to attain Laylatul Qadr. Those observing Itikaf devote their complete focus solely to Allah.\n\n"
                        + "Expressing Gratitude to Allah:\nLaylatul Qadr is an extraordinary divine blessing. Express profound gratitude to Allah by remembering His boundless gifts and mercy.\n\n"
                        + "Maintaining Consistency to Make the Night Fruitful:\nThe exact date of Laylatul Qadr has been concealed so that believers remain devoted across all odd nights of the final ten days. Strive particularly on the 21st, 23rd, 25th, 27th, and 29th nights with perseverance",
                "সহীহ বুখারী: ১৯০১, ২০২৪; সহীহ মুসলিম: ৭৬০, ১১৭৪; জামে আত-তিরমিযী: ৩৫১৩; সুনানে ইবনে মাজাহ: ৩৮৫০",
                "Sahih al-Bukhari: 1901, 2024; Sahih Muslim: 760, 1174; Jami' at-Tirmidhi: 3513; Sunan Ibn Majah: 3850"
        ));

        // 7. লাইলাতুল কদরের সাথে সম্পর্কিত ইসলামিক বিষয়
        list.add(new RozaQadrTopicItem(
                7,
                "qadr_islamic_aspects",
                7,
                "লাইলাতুল কদরের সাথে সম্পর্কিত ইসলামিক বিষয়",
                "Islamic Aspects Related to Laylatul Qadr",
                "লাইলাতুল কদরের সাথে সম্পর্কিত ইসলামিক বিষয়",
                "Islamic Aspects Related to Laylatul Qadr",
                "লাইলাতুল কদর (শবে কদর) ইসলামের অন্যতম গুরুত্বপূর্ণ এবং ফজিলতপূর্ণ রাত, যা কুরআন এবং হাদিসে বিশেষ মর্যাদা ও গুরুত্বের সাথে উল্লেখিত হয়েছে। এটি একটি বরকতময় রাত, যা রহমত, মাগফিরাত, এবং নাজাতের রাত হিসেবে পরিচিত। লাইলাতুল কদ...",
                "Laylatul Qadr (Shab-e-Qadr) is one of the most important and virtuous nights in Islam, mentioned with special honor and significance in the Quran and Hadith. It is a blessed night renowned as a night of mercy, forgiveness, and sal...",
                "লাইলাতুল কদর (শবে কদর) ইসলামের অন্যতম গুরুত্বপূর্ণ এবং ফজিলতপূর্ণ রাত, যা কুরআন এবং হাদিসে বিশেষ মর্যাদা ও গুরুত্বের সাথে উল্লেখিত হয়েছে। এটি একটি বরকতময় রাত, যা রহমত, মাগফিরাত, এবং নাজাতের রাত হিসেবে পরিচিত। লাইলাতুল কদরের সাথে সম্পর্কিত কিছু গুরুত্বপূর্ণ ইসলামিক বিষয় নিচে উল্লেখ করা হলো:\n\n"
                        + "কুরআন নাজিলের রাত\n"
                        + "হাজার মাসের ইবাদতের সমান ফজিলত\n"
                        + "ফেরেশতাদের অবতরণ\n"
                        + "তাকদীর নির্ধারণের রাত\n"
                        + "শান্তি ও বরকতের রাত\n"
                        + "গুনাহ মাফের রাত\n"
                        + "বিশেষ দোয়া করার রাত\n"
                        + "ইতিকাফের সাথে সম্পর্ক\n"
                        + "নির্দিষ্ট রাতের অনুসন্ধান\n\n"
                        + "ইবাদতের জন্য গুরুত্বপূর্ণ বিষয়:\n\n"
                        + "লাইলাতুল কদরে নিম্নোক্ত ইবাদতগুলো করা সুন্নত ও ফজিলতপূর্ণ:\n\n"
                        + "নফল নামাজ পড়া।\n"
                        + "কুরআন তিলাওয়াত করা।\n"
                        + "তওবা করা এবং গুনাহের জন্য ক্ষমা চাওয়া।\n"
                        + "জিকির ও তাসবিহ পাঠ করা।\n"
                        + "দরিদ্রদের জন্য দান করা।\n"
                        + "নিজের এবং মুসলিম উম্মাহর জন্য দোয়া করা।",
                "Laylatul Qadr (Shab-e-Qadr) is one of the most important and virtuous nights in Islam, mentioned with special honor and significance in the Quran and Hadith. It is a blessed night renowned as a night of mercy, forgiveness, and salvation. Important Islamic aspects related to Laylatul Qadr are mentioned below:\n\n"
                        + "Night of the Quran's revelation\n"
                        + "Virtue equivalent to a thousand months of worship\n"
                        + "Descent of the angels\n"
                        + "Night of decree and destiny\n"
                        + "Night of peace and blessings\n"
                        + "Night of forgiveness of sins\n"
                        + "Night for making special supplications\n"
                        + "Relationship with Itikaf\n"
                        + "Seeking the specific night\n\n"
                        + "Important aspects for worship:\n\n"
                        + "Performing the following acts of worship on Laylatul Qadr is Sunnah and highly virtuous:\n\n"
                        + "Performing voluntary (Nafl) prayers.\n"
                        + "Reciting the Holy Quran.\n"
                        + "Repenting and seeking forgiveness for sins.\n"
                        + "Reciting Dhikr and Tasbih.\n"
                        + "Giving charity to the poor.\n"
                        + "Supplicating for oneself and the Muslim Ummah.",
                "সূরা আল-কদর: ১-৫; সূরা আদ-দুখান: ১-৪; সহীহ বুখারী: ১৯০১, ২০১৪; জামে আত-তিরমিযী: ৩৫১৩",
                "Surah Al-Qadr: 1-5; Surah Ad-Dukhan: 1-4; Sahih al-Bukhari: 1901, 2014; Jami' at-Tirmidhi: 3513"
        ));

        return list;
    }

    private static void fetchRemoteTopics(Context context, DataCallback callback) {
        HttpURLConnection conn = null;
        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_qadr_topics.php");
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
                    if (root.has("success") && root.get("success").getAsBoolean() && root.has("topics")) {
                        JsonArray arr = root.getAsJsonArray("topics");
                        List<RozaQadrTopicItem> remoteList = new ArrayList<>();
                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int id = obj.get("id").getAsInt();
                            String slug = obj.get("slug").getAsString();
                            int orderIndex = obj.has("sort_order") ? obj.get("sort_order").getAsInt() : id;
                            String titleBn = obj.get("title_bn").getAsString();
                            String titleEn = obj.get("title_en").getAsString();
                            String cardTitleBn = obj.has("card_title_bn") ? obj.get("card_title_bn").getAsString() : titleBn;
                            String cardTitleEn = obj.has("card_title_en") ? obj.get("card_title_en").getAsString() : titleEn;
                            String previewBn = obj.get("preview_bn").getAsString();
                            String previewEn = obj.get("preview_en").getAsString();
                            String detailsBn = obj.get("details_bn").getAsString();
                            String detailsEn = obj.get("details_en").getAsString();
                            String referenceBn = obj.has("reference_bn") && !obj.get("reference_bn").isJsonNull() ? obj.get("reference_bn").getAsString() : "";
                            String referenceEn = obj.has("reference_en") && !obj.get("reference_en").isJsonNull() ? obj.get("reference_en").getAsString() : "";

                            remoteList.add(new RozaQadrTopicItem(
                                    id, slug, orderIndex, titleBn, titleEn, cardTitleBn, cardTitleEn,
                                    previewBn, previewEn, detailsBn, detailsEn, referenceBn, referenceEn
                            ));
                        }

                        if (!remoteList.isEmpty()) {
                            synchronized (cachedList) {
                                cachedList.clear();
                                cachedList.addAll(remoteList);
                            }
                            if (callback != null) {
                                mainHandler.post(() -> callback.onDataLoaded(new ArrayList<>(cachedList)));
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
