package com.devflux.deenone.features.ramadan.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.ramadan.model.RozaEidTopicItem;
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
 * 3-Tier Production Repository for: section: ঈদ (Eid Section).
 * 0ms instant memory cache with background async REST API sync (Rule 11).
 */
public final class RozaEidRepository {

    private static final List<RozaEidTopicItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultTopics());
    }

    private RozaEidRepository() {}

    public interface DataCallback {
        void onDataLoaded(List<RozaEidTopicItem> items);
    }

    public static List<RozaEidTopicItem> getAllTopics(Context context, DataCallback callback) {
        if (context != null && NetworkConnectivityHelper.isOnline(context)) {
            final Context appContext = context.getApplicationContext();
            executor.execute(() -> fetchRemoteTopics(appContext, callback));
        }
        return new ArrayList<>(cachedList);
    }

    public static List<RozaEidTopicItem> getAllTopics() {
        return new ArrayList<>(cachedList);
    }

    public static List<RozaEidTopicItem> getTopics(Context context, DataCallback callback) {
        return getAllTopics(context, callback);
    }

    public static List<RozaEidTopicItem> getTopics() {
        return getAllTopics();
    }

    private static void fetchRemoteTopics(Context context, DataCallback callback) {
        HttpURLConnection conn = null;
        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_eid_topics.php");
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
                        List<RozaEidTopicItem> remoteItems = new ArrayList<>();

                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int id = obj.has("id") ? obj.get("id").getAsInt() : (remoteItems.size() + 1);
                            String slug = obj.has("slug") ? obj.get("slug").getAsString() : ("eid_topic_" + id);
                            int orderIndex = obj.has("order_index") ? obj.get("order_index").getAsInt() : id;
                            String titleBn = obj.has("title_bn") ? obj.get("title_bn").getAsString() : "";
                            String titleEn = obj.has("title_en") ? obj.get("title_en").getAsString() : "";
                            String cardTitleBn = obj.has("card_title_bn") ? obj.get("card_title_bn").getAsString() : titleBn;
                            String cardTitleEn = obj.has("card_title_en") ? obj.get("card_title_en").getAsString() : titleEn;
                            String previewBn = obj.has("preview_bn") ? obj.get("preview_bn").getAsString() : "";
                            String previewEn = obj.has("preview_en") ? obj.get("preview_en").getAsString() : "";
                            String detailsBn = obj.has("details_bn") ? obj.get("details_bn").getAsString() : "";
                            String detailsEn = obj.has("details_en") ? obj.get("details_en").getAsString() : "";
                            String referenceBn = obj.has("reference_bn") ? obj.get("reference_bn").getAsString() : "";
                            String referenceEn = obj.has("reference_en") ? obj.get("reference_en").getAsString() : "";

                            remoteItems.add(new RozaEidTopicItem(
                                    id, slug, orderIndex, titleBn, titleEn, cardTitleBn, cardTitleEn,
                                    previewBn, previewEn, detailsBn, detailsEn, referenceBn, referenceEn
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
            // Gracefully fallback to pre-seeded static data
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static List<RozaEidTopicItem> getDefaultTopics() {
        List<RozaEidTopicItem> list = new ArrayList<>();

        // 1. ঈদ
        list.add(new RozaEidTopicItem(
                1,
                "eid_concept",
                1,
                "ঈদ",
                "Eid",
                "ঈদের বিবরণ",
                "Description of Eid",
                "ঈদ শব্দটি আরবি \"عيد\" থেকে এসেছে, যার অর্থ \"বারবার ফিরে আসা\" বা \"উৎসব।\" ইসলামে ঈদ হলো আনন্দ ও ইবাদতের একটি দিন, যা আল্লাহর নির্দেশ অনুসারে পালিত হয়। এটি রোজা, ত্যাগ, ইবাদত, এবং তাকওয়ার ফলস্বরূপ আল্লাহর দেয়া একটি বিশেষ নিয়াম...",
                "The word Eid originates from Arabic meaning recurring return or festival. In Islam, Eid is a day of joy and devotion celebrated according to Allah's command...",
                "ঈদ শব্দটি আরবি \"عيد\" থেকে এসেছে, যার অর্থ \"বারবার ফিরে আসা\" বা \"উৎসব।\" ইসলামে ঈদ হলো আনন্দ ও ইবাদতের একটি দিন, যা আল্লাহর নির্দেশ অনুসারে পালিত হয়। এটি রোজা, ত্যাগ, ইবাদত, এবং তাকওয়ার ফলস্বরূপ আল্লাহর দেয়া একটি বিশেষ নিয়ামত।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "কুরআনে ঈদ:\n" + 
                        "\n" + 
                        "কুরআনে ঈদের দিন সরাসরি উল্লেখ করা হয়নি, তবে ঈদের সাথে সম্পর্কিত বিষয়গুলো স্পষ্টভাবে এসেছে। নিচে কিছু গুরুত্বপূর্ণ আয়াত উল্লেখ করা হলো। রমজানের শেষে তাকওয়া ও শোকরগুজারি করার নির্দেশ (ঈদুল ফিতরের নির্দেশনা),\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "وَلِتُكْمِلُوا ٱلْعِدَّةَ وَلِتُكَبِّرُوا ٱللَّهَ عَلَىٰ مَا هَدَىٰكُمْ وَلَعَلَّكُمْ تَشْكُرُونَ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "যেন তোমরা সংখ্যা পূর্ণ করতে পারো (রমজানের রোজা), এবং যেন তোমরা আল্লাহর মহত্ত্ব ঘোষণা করো, যে তিনি তোমাদের হেদায়াত দিয়েছেন, এবং যেন তোমরা কৃতজ্ঞতা প্রকাশ করো।\n" + 
                        "\n" + 
                        "[সূরা আল-বাকারা: ১৮৫]\n" + 
                        "\n" + 
                        "এই আয়াতে আল্লাহ তাআলা ঈদুল ফিতরের নির্দেশ দিয়েছেন। রোজা শেষ করার পর ঈদে তাকবির বলা (আল্লাহু আকবার) এবং তাঁর প্রতি কৃতজ্ঞতা প্রকাশ করার কথা উল্লেখ করা হয়েছে।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "আল্লাহর দেয়া নেয়ামত উদযাপন করার কথা:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "قُلْ بِفَضْلِ ٱللَّهِ وَبِرَحْمَتِهِۦ فَبِذَٰلِكَ فَلْইَفْرَحُوا۟ ۚ هُوَ خَيْرٌ مِّمَّا يَجْمَعُونَ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "বলুন, এটি আল্লাহর অনুগ্রহ এবং তাঁর রহমত; সুতরাং এতে তারা আনন্দ প্রকাশ করুক। এটি তাদের সঞ্চিত সম্পদের চেয়ে উত্তম।\n" + 
                        "\n" + 
                        "[সূরা ইউনুস: ৫৮]\n" + 
                        "\n" + 
                        "ঈদ আল্লাহর রহমত এবং অনুগ্রহের একটি অংশ। রোজা ও হজ্বের পর ঈদ হল সেই নেয়ামত উদযাপনের উপলক্ষ।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "হাদিসে ঈদ:\n" + 
                        "\n" + 
                        "আনাস ইবনে মালিক (রা.) থেকে বর্ণিত, তিনি বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "قَدِمَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ الْمَدِينَةَ وَلَهُمْ يَوْمَانِ يَلْعَبُونَ فِيهِمَا فَقَالَ: مَا هَذَانِ الْيَوْمَانِ؟ قَالُوا: كُنَّا نَلْعَبُ فِيهِمَا فِي الْجَاهِلِيَّةِ، فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: إِنَّ اللَّهَ قَدْ أَبْدَلَكُمْ بِهِمَا خَيْرًا مِنْهُمَا: يَوْمَ الْأَضْحَىٰ وَيَوْمَ الْفِطْرِ.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ মদিনায় এসে দেখলেন, সেখানকার লোকেরা দুইটি দিন উৎসব পালন করত। তিনি জিজ্ঞাসা করলেন, 'এই দুই দিনের কী অবস্থা?' তারা বলল, 'আমরা এই দুই দিনে জাহেলিয়াতের যুগে আনন্দ করতাম।' তখন রাসুলুল্লাহ ﷺ বললেন, 'আল্লাহ তোমাদের জন্য এর পরিবর্তে দুটি উত্তম দিন দিয়েছেন: ঈদুল আযহা এবং ঈদুল ফিতর।\n" + 
                        "\n" + 
                        "[সহিহ আবু দাউদ: ১১৩৪, নাসায়ি: ১৫৫৬]\n" + 
                        "\n" + 
                        "এই হাদিস থেকে স্পষ্ট হয় যে, ইসলামে বছরে দুটি ঈদ আল্লাহর পক্ষ থেকে বিশেষ পুরস্কার হিসেবে নির্ধারিত হয়েছে।",
                "The word 'Eid' originates from the Arabic word 'Eid' meaning 'recurring return' or 'festival.' In Islam, Eid is a day of joy and devotion, celebrated strictly in accordance with Allah's command. It is a divine blessing bestowed as the fruit of fasting, sacrifice, worship, and Taqwa.\n" + 
                        "\n" + 
                        "Eid in the Qur'an:\n" + 
                        "While the exact festival day of Eid is not mentioned by name in the Qur'an, the foundational obligations and virtues of Eid are clearly stated. Regarding completion of Ramadan and gratitude:\n" + 
                        "\n" + 
                        "وَلِتُكْمِلُوا ٱلْعِدَّةَ وَلِتُكَبِّرُوا ٱللَّهَ عَلَىٰ مَا هَدَىٰكُمْ وَلَعَلَّكُمْ تَشْكُرُونَ\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "''And that you should complete the number (of days of fasting), and that you should magnify Allah for having guided you so that you may be thankful.''\n" + 
                        "[Surah Al-Baqarah: 185]\n" + 
                        "\n" + 
                        "Rejoicing in Allah's Bounty:\n" + 
                        "قُلْ بِفَضْلِ ٱللَّهِ وَبِرَحْمَتِهِۦ فَبِذَٰلِكَ فَلْيَفْرَحُوا۟ ۚ هُوَ خَيْرٌ مِّمَّا يَجْمَعُونَ\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "Say: ''In the bounty of Allah and in His mercy - in that let them rejoice; it is better than what they accumulate.''\n" + 
                        "[Surah Yunus: 58]\n" + 
                        "\n" + 
                        "Eid in the Hadith:\n" + 
                        "Anas ibn Malik (may Allah be pleased with him) reported:\n" + 
                        "''The Messenger of Allah ﷺ arrived in Madinah while the people had two days of festivities. He asked: 'What are these two days?' They said: 'We used to celebrate them during Jahiliyyah.' The Prophet ﷺ said: 'Allah has replaced them with something superior: the Day of Eid al-Adha and the Day of Eid al-Fitr.''\n" + 
                        "[Sahih Abi Dawud #1134, Sunan an-Nasa'i #1556]",
                "সহিহ আবু দাউদ ১১৩৪, নাসায়ি ১৫৫৬",
                "Sahih Abi Dawud 1134, Sunan an-Nasa'i 1556"
        ));

        // 2. ঈদুল ফিতর
        list.add(new RozaEidTopicItem(
                2,
                "eid_ul_fitr",
                2,
                "ঈদুল ফিতর",
                "Eid-ul-Fitr",
                "ঈদুল ফিতর",
                "Eid-ul-Fitr",
                "ঈদুল ফিতর হলো রমজান মাসের রোজা সমাপ্তির পর পালিত একটি আনন্দময় দিন। এটি আল্লাহর দেয়া একটি নিয়ামত, যা রোজা, তাকওয়া এবং আত্মশুদ্ধির মাধ্যমে অর্জিত হয়। ঈদুল ফিতর এমন একটি দিন, যা মুসলিম উম্মাহর ভ্রাতৃত্ব, ঐক্য, এবং আল্লাহর ...",
                "Eid-ul-Fitr is a joyous day celebrated after concluding the fasting of the month of Ramadan. It is a divine blessing attained through fasting, Taqwa, and self-purification. Eid-ul-Fitr expresses the brotherhood, unity, and deep gratitude of the Muslim Ummah to Allah...",
                "ঈদুল ফিতর হলো রমজান মাসের রোজা সমাপ্তির পর পালিত একটি আনন্দময় দিন। এটি আল্লাহর দেয়া একটি নিয়ামত, যা রোজা, তাকওয়া এবং আত্মশুদ্ধির মাধ্যমে অর্জিত হয়। ঈদুল ফিতর এমন একটি দিন, যা মুসলিম উম্মাহর ভ্রাতৃত্ব, ঐক্য, এবং আল্লাহর প্রতি কৃতজ্ঞতার প্রকাশ ঘটায়।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের অর্থ:\n" + 
                        "\n" + 
                        "ঈদ (عيد): শব্দটি আরবি, যার অর্থ \"উৎসব\" বা \"বারবার ফিরে আসা।\"\n" + 
                        "ফিতর (فطر): অর্থ \"রোজা ভাঙা\" বা \"খাওয়া।\"\n" + 
                        "অর্থাৎ, ঈদুল ফিতর হলো রমজানের এক মাসের রোজার সমাপ্তির পর আনন্দ ও আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশের উৎসব।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ইসলামে ঈদুল ফিতরের সূচনা:\n" + 
                        "\n" + 
                        "ইসলামে ঈদুল ফিতরের সূচনা মদিনায় হিজরতের পর হয়। রাসুলুল্লাহ ﷺ মদিনায় এসে দেখেন যে, সেখানকার লোকেরা দুইটি দিন আনন্দ করত। তখন তিনি বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "إِنَّ اللَّهَ قَدْ أَبْدَلَكُمْ بِهِمَا خَيْرًا مِنْهُمَا: يَوْمَ الْفِطْرِ وَيَوْمَ النَّحْرِ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "নিশ্চয়ই আল্লাহ তোমাদের জন্য ওই দুই দিনের পরিবর্তে দুইটি উত্তম দিন নির্ধারণ করেছেন: ঈদুল ফিতর এবং ঈদুল আযহা।\n" + 
                        "\n" + 
                        "[সহিহ আবু দাউদ: ১১৩৪, নাসায়ি: ১৫৫৬]\n" + 
                        "\n" + 
                        "কুরআনে ঈদুল ফিতরের নাম সরাসরি উল্লেখ নেই, তবে রমজান এবং এর সমাপ্তি নিয়ে আয়াত রয়েছে।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "হাদিসে ঈদুল ফিতর:\n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদুল ফিতরের দিনে আল্লাহর ইবাদত এবং আনন্দ করার নির্দেশ দিয়েছেন। আবু হুরাইরা (রা.) থেকে বর্ণিত, রাসুলুল্লাহ ﷺ বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "صُومُوا لِرُؤْيَتِهِ وَأَفْطِرُوا لِرُؤْيَتِهِ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "তোমরা চাঁদ দেখে রোজা শুরু করো এবং চাঁদ দেখে রোজা ভাঙো।\n" + 
                        "\n" + 
                        "[সহিহ বুখারি: ১৯০৯, সহিহ মুসলিম: ১০৮১]",
                "Eid-ul-Fitr is a joyous day celebrated after concluding the fasting of the month of Ramadan. It is a divine blessing attained through fasting, Taqwa, and self-purification. Eid-ul-Fitr is a day that manifests the brotherhood, solidarity, and profound gratitude of the Muslim Ummah towards Allah the Almighty.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "The Meaning of Eid-ul-Fitr:\n" + 
                        "\n" + 
                        "• Eid (عيد): An Arabic word meaning \"festival\" or \"recurring return.\"\n" + 
                        "• Fitr (فطر): Signifies \"breaking the fast\" or \"nourishment.\"\n" + 
                        "Thus, Eid-ul-Fitr is the blessed festival marking the completion of thirty days of Ramadan fasting in joyful gratitude to Allah.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Inception of Eid-ul-Fitr in Islam:\n" + 
                        "\n" + 
                        "The observance of Eid-ul-Fitr began following the Prophet's migration (Hijrah) to Madinah. Upon arriving in Madinah, the Messenger of Allah ﷺ found that the people celebrated two specific festive days. He remarked:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "إِنَّ اللَّهَ قَدْ أَبْدَلَكُمْ بِهِمَا خَيْرًا مِنْهُمَا: يَوْمَ الْفِطْرِ وَيَوْمَ النَّحْرِ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''Indeed, Allah has substituted for you two days superior to them: the Day of Fitr and the Day of Nahr (Adha).''\n" + 
                        "\n" + 
                        "[Sahih Abi Dawud: 1134, Sunan an-Nasa'i: 1556]\n" + 
                        "\n" + 
                        "The name 'Eid-ul-Fitr' is not mentioned explicitly in the Qur'an, though there are pivotal verses commanding the completion of the count of Ramadan.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Eid-ul-Fitr in the Hadith:\n" + 
                        "\n" + 
                        "The Messenger of Allah ﷺ enjoined devotions and permissible celebration on the Day of Fitr. Abu Hurairah (may Allah be pleased with him) reported that the Messenger of Allah ﷺ said:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "صُومُوا لِرُؤْيَتِهِ وَأَفْطِرُوا لِرُؤْيَتِهِ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''Fast upon the sighting of the crescent moon and break your fast upon its sighting.''\n" + 
                        "\n" + 
                        "[Sahih Bukhari: 1909, Sahih Muslim: 1081]",
                "",
                ""
        ));

        // 3. ঈদের নামাজ
        list.add(new RozaEidTopicItem(
                3,
                "eid_namaz",
                3,
                "ঈদের নামাজ",
                "Eid Prayer",
                "ঈদের নামাজ",
                "Eid Prayer",
                "ঈদের নামাজ হলো মুসলিমদের জন্য একটি গুরুত্বপূর্ণ ইবাদত, যা বছরে দুইবার ঈদুল ফিতর এবং ঈদুল আযহার দিনে আদায় করা হয়। এটি একটি বিশেষ নামাজ, যা একত্রে জামাআতে আদায় করা হয় এবং এটি ইসলামের সামাজিক ঐক্য ও ভ্রাতৃত্বের নিদর্শন ঈদুল ফি...",
                "The Eid prayer is a momentous act of worship for Muslims, performed twice a year on the days of Eid al-Fitr and Eid al-Adha. It is a distinctive communal prayer that epitomizes Islamic social unity and fraternal solidarity...",
                "ঈদের নামাজ হলো মুসলিমদের জন্য একটি গুরুত্বপূর্ণ ইবাদত, যা বছরে দুইবার ঈদুল ফিতর এবং ঈদুল আযহার দিনে আদায় করা হয়। এটি একটি বিশেষ নামাজ, যা একত্রে জামাআতে আদায় করা হয় এবং এটি ইসলামের সামাজিক ঐক্য ও ভ্রাতৃত্বের নিদর্শন।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতর নামাজের গুরুত্ব ও বিধান:\n" + 
                        "\n" + 
                        "ঈদের নামাজ ইসলামের একটি গুরুত্বপূর্ণ আমল, যা রাসুলুল্লাহ ﷺ এর সুন্নত এবং মুসলিম উম্মাহর জন্য অত্যন্ত গুরুত্বপূর্ণ।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদের নামাজ ফরজ নয়, তবে ওয়াজিব বা সুন্নতে মুয়াক্কাদা:\n" + 
                        "\n" + 
                        "মুসলিম উম্মাহর অধিকাংশ স্কলারের মতে, ঈদের নামাজ ওয়াজিব (আবশ্যকীয়) বা সুন্নতে মুয়াক্কাদা। এটি রাসুলুল্লাহ ﷺ সবসময় আদায় করেছেন এবং উম্মাহকে আদায় করতে নির্দেশ দিয়েছেন।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "কুরআনে ঈদুল ফিতর নামাজের নির্দেশনা:\n" + 
                        "\n" + 
                        "কুরআনে ঈদের নামাজ সরাসরি উল্লেখ নেই, তবে ঈদের দিন সালাত আদায় করার ইঙ্গিত পাওয়া যায়। আল্লাহ তাআলা বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "فَصَلِّ لِرَبِّكَ وَٱنْحَرْ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "তাহলে তোমার প্রভুর উদ্দেশ্যে সালাত আদায় করো এবং কোরবানি করো।\n" + 
                        "\n" + 
                        "[সূরা আল-কাওসার: ২]\n" + 
                        "\n" + 
                        "স্কলারদের মতে, এখানে \"সালাত\" দ্বারা ঈদের নামাজ বোঝানো হয়েছে এবং \"নাহর\" দ্বারা কোরবানি বোঝানো হয়েছে।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের নামাজ আদায়ের আদেশ:\n" + 
                        "\n" + 
                        "উম্মু আতিয়া (রা.) থেকে বর্ণিত, তিনি বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "أَمَرَنَا، يَعْنِي النَّبِيَّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ، أَنْ نُخْرِجَ فِي الْعِيدَيْنِ الْعَوَاتِقَ، وَذَوَاتِ الْخُدُورِ، وَأَمَرَ الْحُيَّضَ أَنْ يَعْتَزِلْنَ مُصَلَّى الْمُسْلِمِينَ.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "নবী ﷺ আমাদের আদেশ দিয়েছেন ঈদের দিন তরুণী, পর্দানশীন নারী এবং ঋতুমতীদের বের হতে, তবে তারা মসল্লার (নামাজের স্থানের) বাইরে থাকবে।\n" + 
                        "\n" + 
                        "[সহিহ বুখারি: ৯২৪, সহিহ মুসলিম: ৮৯০]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদগাহে গিয়ে নামাজ আদায় করা:\n" + 
                        "\n" + 
                        "আবু সাঈদ খুদরি (রা.) বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَخْرُجُ يَوْمَ الْفِطْرِ وَيَوْمَ الْأَضْحَىٰ إِلَى الْمُصَلَّى، فَأَوَّلُ شَيْءٍ يَبْدَأُ بِهِ الصَّلَاةُ.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ: \n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদুল ফিতর এবং ঈদুল আযহার দিনে ঈদগাহে যেতেন এবং প্রথম কাজ হিসেবে সালাত আদায় করতেন।\n" + 
                        "\n" + 
                        "[সহিহ বুখারি: ৯৫৬, সহিহ মুসলিম: ৮৮৯]\n" + 
                        "\n" + 
                        "এই হাদিস থেকে বোঝা যায় যে, রাসুলুল্লাহ ﷺ ঈদের নামাজ ঈদগাহে আদায় করতেন এবং এটি উম্মাহর জন্য গুরুত্বপূর্ণ সুন্নত।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের দিন আল্লাহর প্রশংসা করা:\n" + 
                        "\n" + 
                        "আবদুল্লাহ ইবনে আবু বকর (রা.) বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يُكَبِّرُ يَوْمَ الْفِطْرِ مِنْ حِينَ يَخْرُجُ مِنْ بَيْتِهِ حَتَّىٰ يَأْتِيَ الْمُصَلَّى. \n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদের দিনে তাঁর বাড়ি থেকে বের হওয়া থেকে ঈদগাহে পৌঁছানো পর্যন্ত তাকবির বলতে থাকতেন।\n" + 
                        "\n" + 
                        "[মুসনাদে আহমদ: ১৯৭৮১]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতর নামাজের সংক্ষিপ্ত বিবরণ:\n" + 
                        "\n" + 
                        "ঈদের নামাজ দুই রাকাত, যা জামাআতে আদায় করা হয়।\n" + 
                        "এই নামাজে কোনো আজান বা ইকামত নেই।\n" + 
                        "নামাজের খুতবা নামাজের পরে হয়।\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতর নামাজের নিয়ত:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "نَوَيْتُ أنْ أصَلِّي للهِ تَعَالىَ رَكْعَتَيْنِ صَلَاةِ الْعِيْدِ الْفِطْرِ مَعَ سِتِّ التَكْبِيْرَاتِ وَاجِبُ اللهِ تَعَالَى اِقْتَضَيْتُ بِهَذَا الْاِمَامِ مُتَوَجِّهًا اِلَى جِهَةِ الْكَعْبَةِ الشَّرِيْفَةِ اللهُ اَكْبَرْ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "উচ্চারণ: \n" + 
                        "\n" + 
                        "নাওয়াইতু আন উসাল্লিয়া লিল্লাহি তাআলা রাকাআতাইন সালাতিল ইদিল ফিতরি মাআ সিত্তাতিত তাকবিরাতি ওয়াঝিবুল্লাহি তাআলা ইকতাদাইতু বিহাজাল ইমামি মুতাওয়াঝঝিহান ইলা ঝিহাতিল কাবাতিশ শারিফাতি 'আল্লাহু আকবার।\n" + 
                        "\n" + 
                        "অর্থ: \n" + 
                        "\n" + 
                        "আমি ঈদুল ফিতরের দুই রাকাত ওয়াজিব নামাজ অতিরিক্ত ৬ তাকবিরের সঙ্গে এই ইমামের পেছনে কেবলামুখী হয়ে আল্লাহর জন্য আদায় করছি- 'আল্লাহু আকবার'।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের নামাজ পড়ার নিয়ম:\n" + 
                        "\n" + 
                        "প্রথম রাকাতে ঈমামের সঙ্গে তাকবিরে তাহরিমা ‘আল্লাহু আকবার’ বলে উভয় হাত বাঁধা। তাকবিরে তাহরিমার পর ছানা পড়া (সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা ওয়া তাবারাকাসমুকা ওয়াতাআলা যাদ্দুকা ওয়া লা ইলাহা গাইরুকা)।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "এরপর অতিরিক্ত ৩ তাকবির দেওয়া। প্রথম ও দ্বিতীয় তাকবিরে উভয় হাত উঠিয়ে তা ছেড়ে দেওয়া। তৃতীয় তাকবির দিয়ে উভয় হাত তাকবিরে তাহরিমার মতো বেঁধে নেওয়া। এরপর নিয়মিত নামাজের মতো রুকু ও সেজদার মাধ্যমে প্রথম রাকাত শেষ করা।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "দ্বিতীয় রাকাতে সুরা মিলানোর পর অতিরিক্ত ৩ তাকবির দেওয়া। প্রথম ও দ্বিতীয় তাকবিরে উভয় হাত উঠিয়ে তা ছেড়ে দেওয়া।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "তৃতীয় তাকবির দিয়ে উভয় হাত তাকবিরে তাহরিমার মতো বেঁধে নেওয়া। এরপর রুকুর তাকবির দিয়ে রুকুতে যাওয়া। সেজদা আদায় করা। বৈঠকে বসা; তাশাহহুদ, দরূদ, দোয়া মাসুরা পড়ে সালাম ফেরানোর মাধ্যমে নামাজ সম্পন্ন করা। সালাম ফেরানোর পর তাকবির পড়া:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        " اَللهُ اَكْبَر اَللهُ اَكْبَر لَا اِلَهَ اِلَّا اللهُ وَاللهُ اَكْبَر اَللهُ اَكْبَروَلِلهِ الْحَمْد\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "উচ্চারণ: \n" + 
                        "\n" + 
                        "আল্লাহু আকবর, আল্লাহু আকবার, লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার আল্লাহু আকবর ওয়া লিল্লাহিল হামদ।’\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতর নামাজের খুতবা:\n" + 
                        "\n" + 
                        "ঈদের নামাজের পরে খুতবা দেওয়া সুন্নত।\n" + 
                        "খুতবায় আল্লাহর প্রশংসা, তাকওয়া এবং ঈদের শিক্ষা সম্পর্কে আলোচনা করা হয়।\n" + 
                        "খুতবার সময় সবাই বসে মনোযোগ দিয়ে শুনবে।\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতর নামাজের সময়:\n" + 
                        "\n" + 
                        "ঈদের নামাজ সুর্যোদয়ের ১৫-২০ মিনিট পরে থেকে শুরু করে জোহরের আগ পর্যন্ত আদায় করা যায়। ঈদুল ফিতরের নামাজ দ্রুত আদায় করা হয়, যাতে গরিবদের মাঝে জাকাতুল ফিতর পৌঁছে যায়। ঈদুল আযহার নামাজ ধীরস্থিরভাবে আদায় করা হয়, যাতে কোরবানি দ্রুত শুরু করা যায়।",
                "The Eid prayer is a momentous act of worship for Muslims, performed twice a year on the days of Eid al-Fitr and Eid al-Adha. It is a distinctive prayer observed communally in congregation, epitomizing the social harmony, brotherhood, and solidarity of Islam.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Significance and Legal Status of Eid al-Fitr Prayer:\n" + 
                        "\n" + 
                        "The Eid prayer is a vital Islamic practice, established as the Sunnah of the Messenger of Allah ﷺ and immensely momentous for the Muslim Ummah.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Eid Prayer is Not Fard, but Wajib or Sunnah Muakkadah:\n" + 
                        "\n" + 
                        "According to the overwhelming majority of Islamic scholars, the Eid prayer is Wajib (obligatory) or Sunnah Muakkadah (strongly emphasized Sunnah). The Messenger of Allah ﷺ continually observed it and enjoined the Ummah to perform it.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Guidance on Eid Prayer in the Qur'an:\n" + 
                        "\n" + 
                        "While the Eid prayer is not mentioned explicitly by name in the Qur'an, there are clear indications regarding prayer on the day of Eid. Allah the Almighty says:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "فَصَلِّ لِرَبِّكَ وَٱنْحَرْ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''So pray to your Lord and sacrifice [to Him alone].''\n" + 
                        "\n" + 
                        "[Surah Al-Kawthar: 2]\n" + 
                        "\n" + 
                        "Scholars explain that \"Salah\" in this verse refers to the Eid prayer and \"Nahr\" refers to the Qurbani (sacrifice).\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Command to Attend the Eid al-Fitr Prayer:\n" + 
                        "\n" + 
                        "Umm Atiyyah (may Allah be pleased with her) narrated:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "أَمَرَنَا، يَعْنِي النَّبِيَّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ، أَنْ نُخْرِجَ فِي الْعِيدَيْنِ الْعَوَاتِقَ، وَذَوَاتِ الْخُدُورِ، وَأَمَرَ الْحُيَّضَ أَنْ يَعْتَزِلْنَ مُصَلَّى الْمُسْلِمِينَ.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Prophet ﷺ commanded us to bring out on the two Eids the adolescent girls, the secluded women, and the menstruating women, but he ordered the menstruating women to stay away from the prayer area of the Muslims.''\n" + 
                        "\n" + 
                        "[Sahih Bukhari: 924, Sahih Muslim: 890]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Performing the Prayer in the Eidgah (Open Ground):\n" + 
                        "\n" + 
                        "Abu Sa'id al-Khudri (may Allah be pleased with him) reported:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَخْرُجُ يَوْمَ الْفِطْرِ وَيَوْمَ الْأَضْحَىٰ إِلَى الْمُصَلَّى، فَأَوَّلُ شَيْءٍ يَبْدَأُ بِهِ الصَّلَاةُ.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Messenger of Allah ﷺ used to go out to the prayer ground (Musalla) on the day of Fitr and the day of Adha, and the very first thing he began with was the prayer.''\n" + 
                        "\n" + 
                        "[Sahih Bukhari: 956, Sahih Muslim: 889]\n" + 
                        "\n" + 
                        "This Hadith elucidates that the Messenger of Allah ﷺ regularly offered the Eid prayer in an open prayer ground (Eidgah), establishing an essential Sunnah for the Ummah.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Praising Allah on the Day of Eid al-Fitr:\n" + 
                        "\n" + 
                        "Abdullah ibn Abi Bakr (may Allah be pleased with him) reported:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يُكَبِّرُ يَوْمَ الْفِطْرِ مِنْ حِينَ يَخْرُجُ مِنْ بَيْتِهِ حَتَّىٰ يَأْتِيَ الْمُصَلَّى. \n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Messenger of Allah ﷺ used to proclaim the Takbeer on the day of Fitr from the moment he left his house until he reached the prayer ground.''\n" + 
                        "\n" + 
                        "[Musnad Ahmad: 19781]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Brief Outline of Eid al-Fitr Prayer:\n" + 
                        "\n" + 
                        "• The Eid prayer comprises two Rak'ahs performed in congregation.\n" + 
                        "• There is neither Adhan nor Iqamah for this prayer.\n" + 
                        "• The sermon (Khutbah) is delivered following the prayer.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Intention (Niyyah) for Eid al-Fitr Prayer:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "نَوَيْتُ أنْ أصَلِّي للهِ تَعَالىَ رَكْعَتَيْنِ صَلَاةِ الْعِيْدِ الْفِطْرِ مَعَ سِتِّ التَكْبِيْرَاتِ وَاجِبُ اللهِ تَعَالَى اِقْتَضَيْتُ بِهَذَا الْاِمَامِ مُتَوَجِّهًا اِلَى جِهَةِ الْكَعْبَةِ الشَّرِيْفَةِ اللهُ اَكْبَرْ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Transliteration:\n" + 
                        "\n" + 
                        "Nawaytu an usalliya lillahi ta'ala rak'atayni salatal 'Idil-Fitri ma'a sittatit-takbirati wajibullahi ta'ala iqtadaytu bihadhal-imami mutawajjihan ila jihatil-Ka'batish-sharifati Allahu Akbar.\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''I intend to perform two Rak'ahs of Wajib Eid al-Fitr prayer for Allah the Almighty, with six additional Takbeers, following this Imam, facing towards the Noble Ka'bah - Allahu Akbar.''\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Method of Performing the Eid al-Fitr Prayer:\n" + 
                        "\n" + 
                        "In the first Rak'ah, fold both hands after uttering Takbeer al-Tahrimah ('Allahu Akbar') alongside the Imam. Recite the Sana (Subhanakallahumma wa bihamdika wa tabarakasmuka wa ta'ala jadduka wa la ilaha ghayruka).\n" + 
                        "\n" + 
                        "Then proclaim three additional Takbeers: raise both hands to the ears and release them at the first and second Takbeers. At the third Takbeer, raise the hands and clasp them in the customary manner. Conclude the first Rak'ah with normal Ruku and Sujud after Qira'at.\n" + 
                        "\n" + 
                        "In the second Rak'ah, following the recitation of Surah, pronounce three additional Takbeers: raise and release both hands at the first and second Takbeers.\n" + 
                        "\n" + 
                        "Raise and fold hands or proceed directly to Ruku with the next Takbeer according to Madhhab. Perform Sujud and sit for Tashahhud, Durood, and Dua Masura, concluding with Salam. After Salam, proclaim the Takbeer:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        " اَللهُ اَكْبَر اَللهُ اَكْبَر لَا اِلَهَ اِلَّا اللهُ وَاللهُ اَكْبَر اَللهُ اَكْبَروَلِلهِ الْحَمْد\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Transliteration:\n" + 
                        "\n" + 
                        "Allahu Akbar, Allahu Akbar, La Ilaha Illallahu Wallahu Akbar, Allahu Akbar Wa Lillahil-Hamd.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "The Eid al-Fitr Khutbah:\n" + 
                        "\n" + 
                        "• Listening to the Khutbah after the prayer is Sunnah.\n" + 
                        "• The Khutbah encompasses praise of Allah, Taqwa, and teachings of Eid.\n" + 
                        "• The congregation should sit respectfully and listen attentively.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Timing of Eid al-Fitr Prayer:\n" + 
                        "\n" + 
                        "The Eid prayer may be performed from 15-20 minutes after sunrise until before zenith (Zawwal/Zuhr). The Eid al-Fitr prayer is preferably performed early so that Zakat al-Fitr reaches the poor promptly.",
                "",
                ""
        ));

        // 4. ঈদ প্রস্তুতি
        list.add(new RozaEidTopicItem(
                4,
                "eid_preparation",
                4,
                "ঈদ প্রস্তুতি",
                "Eid Preparation",
                "ঈদ প্রস্তুতি",
                "Eid Preparation",
                "ঈদ হলো মুসলিম উম্মাহর জন্য আনন্দ ও ইবাদতের বিশেষ দিন। এটি আল্লাহর দেয়া একটি নিয়ামত এবং রোজা (ঈদুল ফিতর) কিংবা কোরবানি (ঈদুল আযহা)-এর পর কৃতজ্ঞতা প্রকাশের দিন। ঈদ উদযাপনের আগে সঠিকভাবে প্রস্তুতি নেওয়া সুন্নাহ ও ইসলামের শি...",
                "Eid is a special day of joy and devotion for the Muslim Ummah. It is a divine blessing and an occasion to express gratitude following Ramadan (Eid al-Fitr) or Qurbani (Eid al-Adha)...",
                "ঈদ হলো মুসলিম উম্মাহর জন্য আনন্দ ও ইবাদতের বিশেষ দিন। এটি আল্লাহর দেয়া একটি নিয়ামত এবং রোজা (ঈদুল ফিতর) কিংবা কোরবানি (ঈদুল আযহা)-এর পর কৃতজ্ঞতা প্রকাশের দিন। ঈদ উদযাপনের আগে সঠিকভাবে প্রস্তুতি নেওয়া সুন্নাহ ও ইসলামের শিক্ষা। ঈদের প্রস্তুতি শুধু বাহ্যিক নয়, বরং আধ্যাত্মিক, সামাজিক এবং অর্থনৈতিক দিকেও হওয়া উচিত। নিচে ঈদের প্রস্তুতির বিস্তারিত দিক নির্দেশনা প্রদান করা হলো,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের প্রস্তুতি:\n" + 
                        "\n" + 
                        "রমজানের শেষ দশকের ইবাদতে মনোযোগ দিন, বিশেষ করে লাইলাতুল কদর খুঁজতে এবং বেশি বেশি তওবা ও দোয়া করুন। রাসুলুল্লাহ ﷺ বলেছেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "যে ব্যক্তি লাইলাতুল কদরের রাতে ঈমান ও সওয়াবের প্রত্যাশায় ইবাদত করে, তার পূর্ববর্তী সমস্ত গুনাহ মাফ করে দেওয়া হবে।\n" + 
                        "\n" + 
                        "[সহিহ বুখারি: ১৯০১, সহিহ মুসলিম: ৭৬০]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "জাকাতুল ফিতর প্রদান করা:\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের আগে জাকাতুল ফিতর প্রদান করতে হবে। এটি গরিবদের জন্য ঈদের আনন্দ নিশ্চিত করে এবং রোজার ভুলত্রুটি পূরণ করে। জাকাতুল ফিতর ঈদের নামাজের আগে প্রদান করা উত্তম।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        " আত্মশুদ্ধি এবং দোয়া:\n" + 
                        "\n" + 
                        "ঈদের আগের দিন ও রাতে বেশি বেশি দোয়া করুন এবং আল্লাহর রহমত ও ক্ষমা প্রার্থনা করুন। ঈদের দিন নতুন জীবনের সূচনা হিসেবে আত্মশুদ্ধির ইচ্ছা রাখুন।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ব্যক্তিগত প্রস্তুতি, গোসল এবং পরিচ্ছন্নতা:\n" + 
                        "\n" + 
                        "ঈদের দিনে গোসল করা সুন্নত। রাসুলুল্লাহ ﷺ ঈদের দিনে গোসল করার জন্য উৎসাহিত করেছেন।\n" + 
                        "\n" + 
                        "ইবনে আব্বাস (রা.) থেকে বর্ণিত,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَغْتَسِلُ يَوْمَ الْفِطْرِ وَيَوْمَ الْأَضْحَىٰ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ: \n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদুল ফিতর এবং ঈদুল আযহার দিনে গোসল করতেন।\n" + 
                        "\n" + 
                        "[ইবনে মাজাহ: ১৩১৫]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "সুন্দর পোশাক পরিধান:\n" + 
                        "\n" + 
                        "ঈদের দিন পরিষ্কার ও উত্তম পোশাক পরা সুন্নত। রাসুলুল্লাহ ﷺ ঈদের জন্য সুন্দর পোশাক পরিধান করতেন। হাসান ইবনে আলী (রা.) থেকে বর্ণিত,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ يَتَزَيَّنُ لِلْعِيدَيْنِ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদের দিনে নিজেকে সাজাতেন।\n" + 
                        "\n" + 
                        "[বায়হাকি: ৬০৫৮]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "খাওয়া-দাওয়া করার সুন্নত:\n" + 
                        "\n" + 
                        "ঈদের নামাজে যাওয়ার আগে কিছু খাওয়া সুন্নত। আনাস ইবনে মালিক (রা.) বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ لَا يَغْدُو يَوْمَ الْفِطْرِ حَتَّى يَأْكُلَ تَمَرَاتٍ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ: \n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদুল ফিতরের দিনে নামাজে যাওয়ার আগে কয়েকটি খেজুর খেতেন।\n" + 
                        "\n" + 
                        "[সহিহ বুখারি: ৯৫৩]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদগাহে যাওয়া:\n" + 
                        "\n" + 
                        "ঈদুল ফিতর নামাজের জন্য ঈদগাহে যাওয়া সুন্নত। রাসুলুল্লাহ ﷺ সবসময় ঈদুল ফিতরের নামাজ খোলা ময়দানে (ঈদগাহে) আদায় করতেন। আবু সাঈদ খুদরি (রা.) বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَخْرُجُ يَوْمَ الْفِطْرِ وَيَوْمَ الْأَضْحَى إِلَى الْمُصَلَّى\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদুল ফিতর এবং ঈদুল আযহার দিনে ঈদগাহে যেতেন।\n" + 
                        "\n" + 
                        "[সহিহ বুখারি: ৯৫৬]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "তাকবির বলা:\n" + 
                        "\n" + 
                        "ঈদের দিন ফজরের নামাজ থেকে শুরু করে ঈদুল ফিতরের নামাজ পর্যন্ত তাকবির বলা সুন্নত।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ لَا إِلَهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ وَلِلَّهِ الْحَمْدُ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "আল্লাহ মহান, আল্লাহ মহান। আল্লাহ ছাড়া আর কোনো উপাস্য নেই। আল্লাহ মহান, আল্লাহ মহান। এবং সমস্ত প্রশংসা আল্লাহর জন্য।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "নতুন পথে ঈদগাহে যাতায়াত করা:\n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদগাহে যাওয়ার এক পথ ব্যবহার করতেন এবং ফিরতেন আরেক পথ দিয়ে। জাবির ইবনে আবদুল্লাহ (রা.) থেকে বর্ণিত,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا كَانَ يَوْمُ عِيدٍ خَالَفَ الطَّرِيقَ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "রাসুলুল্লাহ ﷺ ঈদের দিন (যাতায়াতে) ভিন্ন রাস্তা ব্যবহার করতেন।\n" + 
                        "\n" + 
                        "[সহিহ বুখারি: ৯৮৬]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "গরিব ও অসহায়দের জন্য জাকাতুল ফিতর প্রদান (ঈদুল ফিতর):\n" + 
                        "\n" + 
                        "ঈদের আনন্দ গরিবদের সঙ্গে ভাগাভাগি করার জন্য জাকাতুল ফিতর প্রদান অত্যন্ত গুরুত্বপূর্ণ।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "আত্মীয়স্বজন ও প্রতিবেশীদের খোঁজখবর নেওয়া:\n" + 
                        "\n" + 
                        "ঈদ হলো ভ্রাতৃত্ব ও সম্পর্ক মজবুত করার একটি সুযোগ। আত্মীয়স্বজন ও প্রতিবেশীদের খোঁজখবর নিন এবং তাদের সঙ্গে শুভেচ্ছা বিনিময় করুন।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "একে অপরকে শুভেচ্ছা জানানো:\n" + 
                        "\n" + 
                        "ঈদের দিনে একে অপরকে শুভেচ্ছা জানানো ইসলামের একটি সুন্নত। মুসলিমরা সাধারণত বলে,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "تَقَبَّلَ اللَّهُ مِنَّا وَمِنْكُمْ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "আল্লাহ আমাদের এবং আপনাদের থেকে আমল কবুল করুন।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "গরিবদের সাহায্য করুন এবং আল্লাহর সন্তুষ্টি অর্জনের চেষ্টা করুন। ঈদ হলো ইবাদত, আনন্দ এবং আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশের দিন। ঈদের প্রস্তুতি শুধু বাহ্যিক নয়, বরং আধ্যাত্মিক, সামাজিক, এবং অর্থনৈতিক দিকেও হওয়া উচিত। সঠিক প্রস্তুতির মাধ্যমে ঈদকে আরও অর্থবহ এবং আনন্দময় করা যায়।",
                "Eid is a special day of joy and devotion for the Muslim Ummah. It is a divine blessing and an occasion to express gratitude following Ramadan (Eid al-Fitr) or Qurbani (Eid al-Adha). Preparing diligently for Eid beforehand is Sunnah and core Islamic teaching. Eid preparation should not be merely external, but spiritual, social, and financial as well. Below are comprehensive guidelines for Eid preparation:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Preparation for Eid al-Fitr:\n" + 
                        "\n" + 
                        "Devote heightened focus to worship during the last ten days of Ramadan, particularly seeking Laylatul Qadr, with abundant repentance and supplication. The Messenger of Allah ﷺ said:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''Whoever stands in prayer on the Night of Qadr out of faith and expectation of divine reward, all his previous sins will be forgiven.''\n" + 
                        "\n" + 
                        "[Sahih Bukhari: 1901, Sahih Muslim: 760]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Discharging Zakat al-Fitr:\n" + 
                        "\n" + 
                        "Zakat al-Fitr must be discharged prior to Eid al-Fitr. This ensures the joy of Eid for the poor and cleanses any shortcomings from fasting. It is optimal to discharge Zakat al-Fitr before the Eid prayer.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Spiritual Purification and Supplication:\n" + 
                        "\n" + 
                        "Engage in fervent prayer on the eve and day of Eid, seeking Allah's mercy and forgiveness. Approach Eid day with a sincere intention of spiritual renewal.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Personal Preparation, Ceremonial Bath (Ghusl), and Cleanliness:\n" + 
                        "\n" + 
                        "Taking a bath (Ghusl) on the day of Eid is Sunnah. The Messenger of Allah ﷺ encouraged performing Ghusl on the day of Eid.\n" + 
                        "\n" + 
                        "Narrated by Ibn Abbas (may Allah be pleased with him):\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَغْتَسِلُ يَوْمَ الْفِطْرِ وَيَوْمَ الْأَضْحَىٰ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Messenger of Allah ﷺ used to perform Ghusl on the day of Fitr and the day of Adha.''\n" + 
                        "\n" + 
                        "[Sunan Ibn Majah: 1315]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Adorning Fine Garments:\n" + 
                        "\n" + 
                        "Wearing clean and fine clothing on Eid is Sunnah. The Messenger of Allah ﷺ wore beautiful attire for Eid. Narrated by Hasan ibn Ali (may Allah be pleased with him):\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ يَتَزَيَّنُ لِلْعِيدَيْنِ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Messenger of Allah ﷺ used to adorn himself for the two Eids.''\n" + 
                        "\n" + 
                        "[Sunan al-Bayhaqi: 6058]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "The Sunnah of Eating Before Departing:\n" + 
                        "\n" + 
                        "It is Sunnah to eat something before departing for the Eid prayer. Anas ibn Malik (may Allah be pleased with him) reported:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ لَا يَغْدُو يَوْمَ الْفِطْرِ حَتَّى يَأْكُلَ تَمَرَاتٍ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Messenger of Allah ﷺ would not depart on the morning of Eid al-Fitr until he had eaten some dates.''\n" + 
                        "\n" + 
                        "[Sahih Bukhari: 953]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Proceeding to the Eidgah (Prayer Ground):\n" + 
                        "\n" + 
                        "Proceeding to the open ground (Eidgah) for the Eid al-Fitr prayer is Sunnah. The Messenger of Allah ﷺ always performed the Eid al-Fitr prayer in an open ground. Abu Sa'id al-Khudri (may Allah be pleased with him) narrated:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَخْرُجُ يَوْمَ الْفِطْرِ وَيَوْمَ الْأَضْحَى إِلَى الْمُصَلَّى\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Prophet ﷺ would go out to the prayer place (Musalla) on the day of Fitr and the day of Adha.''\n" + 
                        "\n" + 
                        "[Sahih Bukhari: 956]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Reciting Takbeerat:\n" + 
                        "\n" + 
                        "Proclaiming Takbeer from Fajr prayer on Eid day until the Eid prayer begins is Sunnah:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ لَا إِلَهَ إِلَّا اللَّهُ وَاللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ وَلِلَّهِ الْحَمْدُ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "Allah is greatest, Allah is greatest. There is no deity worthy of worship except Allah. Allah is greatest, Allah is greatest, and to Allah belongs all praise.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Taking Alternate Routes to the Eidgah:\n" + 
                        "\n" + 
                        "The Messenger of Allah ﷺ used one route when heading to the Eid prayer and returned via a different route. Jabir ibn Abdullah (may Allah be pleased with him) reported:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا كَانَ يَوْمُ عِيدٍ خَالَفَ الطَّرِيقَ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "''The Prophet ﷺ, on the day of Eid, used to alter his route.''\n" + 
                        "\n" + 
                        "[Sahih Bukhari: 986]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Providing Zakat al-Fitr for the Impoverished:\n" + 
                        "\n" + 
                        "Distributing Zakat al-Fitr to share the happiness of Eid with the underprivileged is vital.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Inquiring After Relatives and Neighbors:\n" + 
                        "\n" + 
                        "Eid is a blessed occasion to solidify ties of kinship and brotherhood. Inquire about the well-being of relatives and neighbors, exchanging felicitations.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Exchanging Mutual Greetings:\n" + 
                        "\n" + 
                        "Congratulating one another on Eid is an established Sunnah. Muslims traditionally say:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "تَقَبَّلَ اللَّهُ مِنَّا وَمِنْكُمْ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\n" + 
                        "May Allah accept good deeds from us and from you.\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "Assist the underprivileged and strive to achieve Allah's pleasure. Eid is a day of devotion, joy, and gratitude to Allah. Preparation for Eid must encompass spiritual, social, and economic aspects. Through proper preparation, Eid becomes significantly more meaningful and blessed.",
                "",
                ""
        ));

        // 5. ঈদ উদযাপন
        list.add(new RozaEidTopicItem(
                5,
                "eid_celebration",
                5,
                "ঈদ উদযাপন",
                "Eid Celebration",
                "ঈদ উদযাপন",
                "Eid Celebration",
                "ঈদ হলো আনন্দ, ইবাদত, এবং আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশের একটি বিশেষ দিন। তবে মুসলিমদের ঈদ উদযাপন কেবলমাত্র বাহ্যিক আনন্দ বা উৎসবে সীমাবদ্ধ নয়; বরং এটি আত্মিক, সামাজিক, এবং পারিবারিক দিকগুলোতে ভারসাম্যপূর্ণ হতে হবে। ঈদের প্রতিটি ...",
                "Eid is a special day of joy, worship, and gratitude to Allah. However, Muslim celebration is not confined merely to external festivities; it must be balanced spiritually, socially, and within the family...",
                "ঈদ হলো আনন্দ, ইবাদত, এবং আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশের একটি বিশেষ দিন। তবে মুসলিমদের ঈদ উদযাপন কেবলমাত্র বাহ্যিক আনন্দ বা উৎসবে সীমাবদ্ধ নয়; বরং এটি আত্মিক, সামাজিক, এবং পারিবারিক দিকগুলোতে ভারসাম্যপূর্ণ হতে হবে। ঈদের প্রতিটি দিক রাসুলুল্লাহ ﷺ এর সুন্নাহ অনুযায়ী উদযাপন করলে তা আরও অর্থবহ হয় এবং আল্লাহর সন্তুষ্টি অর্জন করা যায়।\n" + 
                        "\n" + 
                        "নিচে ইসলামের নির্দেশনা অনুসারে ঈদ উদযাপনের আদর্শ পদ্ধতি বর্ণনা করা হলো:\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদ উদযাপনের উদ্দেশ্য:\n" + 
                        "\n" + 
                        "আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশ করা।\n" + 
                        "আত্মশুদ্ধি এবং তাকওয়ার ফলস্বরূপ আত্মিক আনন্দ অনুভব করা।\n" + 
                        "গরিব ও অসহায়দের সঙ্গে ঈদের আনন্দ ভাগাভাগি করা।\n" + 
                        "পরিবার, প্রতিবেশী এবং উম্মাহর মধ্যে ভ্রাতৃত্ব ও ঐক্য প্রতিষ্ঠা করা।\n" + 
                        "আল্লাহ তাআলা বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "قُلْ بِفَضْلِ ٱللَّهِ وَبِرَحْمَتِهِۦ فَبِذَٰلِكَ فَلْيَفْرَحُوا۟ ۚ هُوَ خَيْرٌ مِّمَّا يَجْمَعُونَ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "বলুন, এটি আল্লাহর অনুগ্রহ এবং তাঁর রহমত; সুতরাং এতে তারা আনন্দ প্রকাশ করুক। এটি তাদের সঞ্চিত সম্পদের চেয়ে উত্তম।\n" + 
                        "\n" + 
                        "[সূরা ইউনুস: ৫৮]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ইসলামি সীমারেখা মেনে আনন্দ উদযাপন করা:\n" + 
                        "\n" + 
                        "গান-বাজনা বা অপচয় ছাড়া ইসলামের সীমারেখার মধ্যে আনন্দ উদযাপন করা উচিত।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অপচয় এবং গুনাহ থেকে বাঁচা:\n" + 
                        "\n" + 
                        "ঈদ উদযাপনে অপচয় এবং গুনাহের কাজ থেকে বিরত থাকতে হবে। আল্লাহ তাআলা বলেন,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "إِنَّ ٱلْمُبَذِّরِينَ كَانُوٓا۟ إِخْوَٰنَ ٱلشَّيَٰطِينِ\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "অপচয়কারী শয়তানের ভাই।\n" + 
                        "\n" + 
                        "[সূরা আল-ইসরা: ২৭]\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদ হলো ইবাদত, আনন্দ, এবং ভ্রাতৃত্বের দিন। এটি শুধুমাত্র খাওয়া-দাওয়া বা পোশাক পরিধানে সীমাবদ্ধ নয়, বরং আল্লাহর সন্তুষ্টি অর্জন, গরিবদের সাহায্য করা, এবং পরিবার ও সমাজের সঙ্গে সম্পর্ক মজবুত করার একটি সুযোগ।",
                "Eid is a special day of joy, devotion, and expressing gratitude to Allah. However, the celebration of Eid for Muslims is not restricted solely to external festivities or amusement; rather, it must be balanced spiritually, socially, and within the family. When every facet of Eid is celebrated in accordance with the Sunnah of the Messenger of Allah ﷺ, it becomes vastly more meaningful and earns the pleasure of Allah.\n" + 
                        "\n" + 
                        "Below is the ideal Islamic methodology for celebrating Eid:\n" + 
                        "\n" + 
                        "Objectives of Eid Celebration:\n" + 
                        "• Expressing gratitude to Allah.\n" + 
                        "• Experiencing spiritual contentment as a fruit of self-purification and Taqwa.\n" + 
                        "• Sharing the joy of Eid with the destitute and vulnerable.\n" + 
                        "• Fostering brotherhood and unity among family, neighbors, and the global Ummah.\n" + 
                        "\n" + 
                        "Allah the Almighty says:\n" + 
                        "قُلْ بِفَضْلِ ٱللَّهِ وَبِرَحْمَتِهِۦ فَبِذَٰلِكَ فَلْيَفْرَحُوا۟ ۚ هُوَ خَيْرٌ مِّمَّا يَجْمَعُونَ\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "Say, \"In the bounty of Allah and in His mercy - in that let them rejoice; it is better than what they accumulate.\"\n" + 
                        "[Surah Yunus: 58]\n" + 
                        "\n" + 
                        "Celebrating Joy Within Islamic Boundaries:\n" + 
                        "Rejoicing should remain strictly within the permissible boundaries of Islam, free from musical instruments, forbidden entertainment, or extravagance.\n" + 
                        "\n" + 
                        "Guarding Against Extravagance and Sins:\n" + 
                        "One must abstain from squandering wealth and indulgence in sins during Eid. Allah the Almighty says:\n" + 
                        "إِنَّ ٱلْمُবَذِّরِينَ كَانُوٓا۟ إِخْوَٰنَ ٱلشَّيَٰطِينِ\n" + 
                        "\n" + 
                        "Meaning:\n" + 
                        "\"Indeed, the wasteful are brothers of the devils.\"\n" + 
                        "[Surah Al-Isra: 27]\n" + 
                        "\n" + 
                        "Eid is truly a day of worship, rejoicing, and brotherhood. It is not limited to food, drinks, or fine garments, but rather an invaluable opportunity to attain Allah's pleasure, support the needy, and strengthen bonds with family and society.",
                "",
                ""
        ));

        // 6. ঈদ সংক্রান্ত সুন্নাত, নফল, ফরজ ও ওয়াজিব বিষয়
        list.add(new RozaEidTopicItem(
                6,
                "eid_ahkam_categories",
                6,
                "ঈদ সংক্রান্ত সুন্নাত, নফল, ফরজ ও ওয়াজিব বিষয়",
                "Sunnah, Nafl, Fard and Wajib Acts of Eid",
                "ঈদ সংক্রান্ত সুন্নাত, নফল, ফরজ ও ওয়াজিব বিষয়",
                "Sunnah, Nafl, Fard and Wajib Acts of Eid",
                "ঈদ হলো মুসলিম উম্মাহর জন্য আল্লাহর পক্ষ থেকে একটি বিশেষ নিয়ামত এবং আনন্দের দিন। এই দিনে ইবাদত, কৃতজ্ঞতা প্রকাশ, এবং সামাজিক ঐক্য প্রতিষ্ঠার জন্য ইসলামে বিশেষ নির্দেশনা রয়েছে। ঈদ উদযাপনের সময় রাসুলুল্লাহ ﷺ এর দেখানো সুন্নত এ...",
                "Eid is a special blessing and day of joy bestowed by Allah upon the Muslim Ummah. There are specific Islamic guidelines for worship, gratitude, and communal unity on this day...",
                "ঈদ হলো মুসলিম উম্মাহর জন্য আল্লাহর পক্ষ থেকে একটি বিশেষ নিয়ামত এবং আনন্দের দিন। এই দিনে ইবাদত, কৃতজ্ঞতা প্রকাশ, এবং সামাজিক ঐক্য প্রতিষ্ঠার জন্য ইসলামে বিশেষ নির্দেশনা রয়েছে। ঈদ উদযাপনের সময় রাসুলুল্লাহ ﷺ এর দেখানো সুন্নত এবং ইসলামের বিধান অনুযায়ী আমল করলে এটি সওয়াবের কাজ হয়ে যায়। এখানে ঈদ সংক্রান্ত সুন্নাত, নফল, ফরজ ও ওয়াজিব বিষয়ে বিস্তারিত আলোচনা করা হলো,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ফরজ বিষয়:\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের দিন রোজা ভাঙা\n" + 
                        "জাকাতুল ফিতর প্রদান (ঈদুল ফিতরে ফরজ)\n" + 
                        "\n" + 
                        "\n" + 
                        "ওয়াজিব বিষয়:\n" + 
                        "\n" + 
                        "ঈদের নামাজ আদায় করা\n" + 
                        "তাকবির বলা (ঈদের আগের দিন থেকে ঈদের নামাজ পর্যন্ত)\n" + 
                        "তাকবির তাশরিক বলা ঈদুল আযহার দিনগুলোতে ওয়াজিব।\n" + 
                        "ঈদের আগের রাতে ইবাদত করা\n" + 
                        "\n" + 
                        "\n" + 
                        "সুন্নত:\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের দিন, ঈদের নামাজে যাওয়ার আগে খেজুর বা অন্য কিছু খাওয়া সুন্নত।\n" + 
                        "ভিন্ন পথে ঈদগাহে যাতায়াত করা\n" + 
                        "একে অপরকে শুভেচ্ছা জানানো\n" + 
                        "গোসল ও উত্তম পোশাক পরা\n" + 
                        "\n" + 
                        "\n" + 
                        "নফল বিষয়:\n" + 
                        "\n" + 
                        "ঈদের রাতে নফল ইবাদত করা\n" + 
                        "দান-সদকা করা\n" + 
                        "বেশি বেশি জিকির ও দোয়া করা",
                "Eid is a special blessing and a day of joy bestowed by Allah upon the Muslim Ummah. There are distinct Islamic guidelines for worship, expressing gratitude, and establishing social solidarity on this blessed day. Observing Eid in accordance with the Sunnah demonstrated by the Messenger of Allah ﷺ and the commandments of Islam transforms this celebration into an act of immense divine reward. Below is a detailed elaboration of the Fard, Wajib, Sunnah, and Nafl acts concerning Eid:\n" + 
                        "\n" + 
                        "Fard (Obligatory) Matters:\n" + 
                        "• Breaking the fast on the day of Eid al-Fitr\n" + 
                        "• Discharging Zakat al-Fitr (obligatory on Eid al-Fitr)\n" + 
                        "\n" + 
                        "Wajib (Essential) Matters:\n" + 
                        "• Performing the Eid prayer\n" + 
                        "• Proclaiming the Takbeer (from the eve of Eid until the Eid prayer)\n" + 
                        "• Reciting Takbeer al-Tashreeq during the days of Eid al-Adha\n" + 
                        "• Engaging in devotional worship on the eve of Eid\n" + 
                        "\n" + 
                        "Sunnah Acts:\n" + 
                        "• Eating dates or something sweet prior to departing for Eid prayer on Eid al-Fitr\n" + 
                        "• Taking different routes to and from the Eidgah\n" + 
                        "• Exchanging mutual greetings and congratulations\n" + 
                        "• Performing ceremonial bath (Ghusl) and wearing fine apparel\n" + 
                        "\n" + 
                        "Nafl (Voluntary) Matters:\n" + 
                        "• Performing voluntary night prayers on the night of Eid\n" + 
                        "• Giving generous voluntary charity (Sadaqah)\n" + 
                        "• Abundantly engaging in Dhikr and supplication (Dua)",
                "",
                ""
        ));

        // 7. ঈদ সম্পর্কিত মাসআলা-মাসায়েল
        list.add(new RozaEidTopicItem(
                7,
                "eid_masayel",
                7,
                "ঈদ সম্পর্কিত মাসআলা-মাসায়েল",
                "Fiqh Rulings and Masayel of Eid",
                "ঈদ সম্পর্কিত মাসআলা-মাসায়েল",
                "Fiqh Rulings and Masayel of Eid",
                "যাদের উপর জুমার নামায ফরয তাদের উপর ঈদের নামায ওয়াজিব কি, মহিলাদের উপর ঈদের নামাযের হুকুম, মুসাফিরের নামায ও তায়াম্মুমের মাসআলা...",
                "Practical Fiqh rulings regarding missed Eid prayers, women's attendance, overlapping Eid and Friday, and recitation of Takbeers...",
                "ঈদ সম্পর্কিত ১০টি অত্যন্ত গুরুত্বপূর্ণ ফিকহী মাসআলা-মাসায়েল বিস্তারিতভাবে সংকলন করা হয়েছে।",
                "Detailed compilation of 10 essential Fiqh rulings and masayel regarding Eid prayer.",
                "আলমুহীতুল বুরহানী, বাদায়েউস সানায়ে, সহীহ বুখারী, সহীহ মুসলিম",
                "Al-Muheet al-Burhani, Bada'i' as-Sana'i', Sahih Bukhari, Sahih Muslim"
        ));

        // 8. ঈদ সংক্রান্ত ইসলামিক বিষয়
        list.add(new RozaEidTopicItem(
                8,
                "eid_islamic_aspects",
                8,
                "ঈদ সংক্রান্ত ইসলামিক বিষয়",
                "Islamic Aspects of Eid",
                "ঈদ সংক্রান্ত ইসলামিক বিষয়",
                "Islamic Aspects of Eid",
                "ঈদ হলো মুসলিম উম্মাহর জন্য আল্লাহর পক্ষ থেকে বিশেষ নিয়ামত ও আনন্দের দিন। ইসলামে ঈদ উদযাপনকে ইবাদতের অংশ হিসেবে গণ্য করা হয়...",
                "Eid is a special blessing and joyous day granted by Allah to the Muslim Ummah. Celebrating Eid is regarded as a sacred act of worship...",
                "ঈদ হলো মুসলিম উম্মাহর জন্য আল্লাহর পক্ষ থেকে বিশেষ নিয়ামত ও আনন্দের দিন। ইসলামে ঈদ উদযাপনকে ইবাদতের অংশ হিসেবে গণ্য করা হয়, যা আল্লাহর সন্তুষ্টি অর্জন, কৃতজ্ঞতা প্রকাশ এবং সমাজের ভ্রাতৃত্ব ও ঐক্য সুদৃঢ় করার মাধ্যম। ইসলামে দুটি ঈদ পালনের বিধান রয়েছে। এখানে ঈদ সংক্রান্ত ইসলামিক বিষয়গুলো বিস্তারিতভাবে ব্যাখ্যা করা হলো,\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদের দিন ইসলামে বিশেষ দিন:\n" + 
                        "\n" + 
                        "ঈদের দিন মুসলিমদের জন্য কেবল একটি সাধারণ ছুটির দিন নয়, বরং এটি একটি ইবাদত এবং শুকরিয়া আদায়ের দিন।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদের নামায:\n" + 
                        "\n" + 
                        "ঈদের দিনে জামাতে নামায আদায় করা ওয়াজিব (কিছু ফিকহের মতে সুন্নাতে মুয়াক্কাদাহ)। এটি মুসলমানদের ঐক্য ও ভ্রাতৃত্বের প্রতীক।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "তাকবীর বলা:\n" + 
                        "\n" + 
                        "ঈদের দিন তাকবীর বলা একটি গুরুত্বপূর্ণ সুন্নত।\n" + 
                        "\n" + 
                        "তাকবীরে তাশরীক:\n" + 
                        "\n" + 
                        "اللهُ أَكْبَرُ اللهُ أَكْبَرُ، لَا إِلٰهَ إِلَّا اللهُ، وَاللهُ أَكْبَرُ اللهُ أَكْبَرُ، وَلِلّٰهِ الْحَمْدُ\n" + 
                        "\n" + 
                        "অর্থ:\n" + 
                        "\n" + 
                        "আল্লাহ মহান, আল্লাহ মহান; আল্লাহ ব্যতীত কোনো উপাস্য নেই; আল্লাহ মহান, আল্লাহ মহান; সমস্ত প্রশংসা কেবল আল্লাহর জন্যই নির্ধারিত।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "সাদাকাতুল ফিতর (ফিতরা):\n" + 
                        "\n" + 
                        "ঈদুল ফিতরের দিন দরিদ্রদের জন্য ফিতরা প্রদান করা ওয়াজিব, যাতে সমাজের সকল মানুষ ঈদের আনন্দ উপভোগ করতে পারে।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "পারস্পরিক সম্প্রীতি ও ভ্রাতৃত্ব:\n" + 
                        "\n" + 
                        "ঈদের দিন একে অপরকে শুভেচ্ছা জানানো এবং আত্মীয়-স্বজনদের খোঁজখবর নেওয়া সুন্নাত। সালাম ও মুসাফাহার মাধ্যমে পারস্পরিক ভালোবাসা বৃদ্ধি পায়।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "আনন্দ প্রকাশ ও শালীনতা:\n" + 
                        "\n" + 
                        "ইসলামে ঈদের আনন্দ প্রকাশের অনুমতি রয়েছে, তবে তা অবশ্যই শরীয়তের সীমার মধ্যে হতে হবে। গান-বাজনা, অপচয় বা হারাম কোনো কার্যকলাপ থেকে বিরত থাকা আবশ্যক।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ক্ষমা ও সহমর্মিতা:\n" + 
                        "\n" + 
                        "ঈদের দিনটি অতীত ভুলত্রুটি ভুলে গিয়ে একে অপরকে ক্ষমা করার একটি চমৎকার সুযোগ। এটি সমাজের পারস্পরিক সম্পর্কের বন্ধনকে মজবুত করে।\n" + 
                        "\n" + 
                        "\n" + 
                        "\n" + 
                        "ঈদ মুসলিমদের জন্য আনন্দ ও ইবাদতের একটি পরিপূর্ণ সংমিশ্রণ। এটি আল্লাহর রহমত ও নিয়ামতের শুকরিয়া আদায় করার একটি বিশেষ উপলক্ষ।",
                "Eid is a unique day of divine blessing and supreme celebration granted by Allah to the Muslim Ummah. In Islam, celebrating Eid is considered an integral act of devotion aimed at attaining the pleasure of Allah, articulating heartfelt gratitude, and reinforcing fraternal harmony across the community. Islam ordains two primary celebrations: Eid-ul-Fitr and Eid-ul-Adha. Below is a comprehensive exposition of the profound Islamic dimensions surrounding Eid:\n" + 
                        "\n" + 
                        "A Sacred Day in Islam:\n" + 
                        "Eid is not merely a secular holiday; it is an elevated occasion of communal devotion and thanksgiving to Allah.\n" + 
                        "\n" + 
                        "The Eid Congregational Prayer:\n" + 
                        "Performing the Eid prayer in congregation is an essential communal duty symbolizing unity and devotion.\n" + 
                        "\n" + 
                        "Recitation of Takbeer:\n" + 
                        "Proclaiming the greatness of Allah through the Takbeerat is a treasured Sunnah:\n" + 
                        "Allahu Akbar, Allahu Akbar, La Ilaha Illallah, Wallahu Akbar, Allahu Akbar, Wa Lillahil Hamd.\n" + 
                        "\n" + 
                        "Sadaqatul Fitr:\n" + 
                        "Discharging the obligatory charity prior to Eid-ul-Fitr prayer guarantees that impoverished brothers and sisters share in the festive joy.\n" + 
                        "\n" + 
                        "Fraternal Bonds and Good Tidings:\n" + 
                        "Exchanging greetings, visiting loved ones, and seeking reconciliation are central to the spirit of Eid.\n" + 
                        "\n" + 
                        "Wholesome Joy within Modesty:\n" + 
                        "Rejoicing is encouraged within the bounds of Islamic decency, shunning wasteful extravagance and improper entertainment.\n" + 
                        "\n" + 
                        "Forgiveness and Compassion:\n" + 
                        "Eid is a prime occasion to overlook past grievances, extend forgiveness, and foster enduring societal harmony.",
                "",
                ""
        ));

        return list;
    }
}
