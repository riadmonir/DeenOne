package com.devflux.deenone.features.ramadan.data;

import android.content.Context;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.ramadan.model.RozaTaraweehItem;
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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Production-ready 3-tier repository for Roza Taraweeh (রোজা:- তারাবীহ).
 * 1. Instant local/in-memory retrieval (0ms, 60 FPS, lag-free).
 * 2. Background sync from PHP REST API (get_roza_taraweeh.php) via BackendConfigManager.
 * 3. Pre-seeded with 100% verbatim topics matching the screenshot.
 */
public final class RozaTaraweehRepository {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final List<RozaTaraweehItem> cachedList = new CopyOnWriteArrayList<>();

    private RozaTaraweehRepository() {}

    public interface DataCallback {
        void onDataLoaded(List<RozaTaraweehItem> items);
    }

    /**
     * Get all 9 Taraweeh topics instantly from memory/cache, with background remote sync.
     */
    public static List<RozaTaraweehItem> getTopics(Context context, DataCallback callback) {
        if (cachedList.isEmpty()) {
            cachedList.addAll(getDefaultTopics());
        }

        // Asynchronous background sync with PHP REST API
        if (context != null && NetworkConnectivityHelper.isOnline(context)) {
            final Context appContext = context.getApplicationContext();
            executor.execute(() -> fetchRemoteTopics(appContext, callback));
        }

        return new ArrayList<>(cachedList);
    }

    private static void fetchRemoteTopics(Context context, DataCallback callback) {
        HttpURLConnection conn = null;
        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_taraweeh.php");
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
                        List<RozaTaraweehItem> remoteItems = new ArrayList<>();

                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int id = obj.has("id") ? obj.get("id").getAsInt() : 0;
                            String slug = obj.has("slug") ? obj.get("slug").getAsString() : "";
                            String titleBn = obj.has("title_bn") ? obj.get("title_bn").getAsString() : "";
                            String titleEn = obj.has("title_en") ? obj.get("title_en").getAsString() : "";
                            String catBn = obj.has("category_bn") ? obj.get("category_bn").getAsString() : "তারাবীহ";
                            String catEn = obj.has("category_en") ? obj.get("category_en").getAsString() : "Taraweeh";
                            String arabic = obj.has("arabic_text") ? obj.get("arabic_text").getAsString() : "";
                            String transBn = obj.has("transliteration_bn") ? obj.get("transliteration_bn").getAsString() : "";
                            String transEn = obj.has("transliteration_en") ? obj.get("transliteration_en").getAsString() : "";
                            String trBn = obj.has("translation_bn") ? obj.get("translation_bn").getAsString() : "";
                            String trEn = obj.has("translation_en") ? obj.get("translation_en").getAsString() : "";
                            String detBn = obj.has("details_bn") ? obj.get("details_bn").getAsString() : "";
                            String detEn = obj.has("details_en") ? obj.get("details_en").getAsString() : "";
                            String prevBn = obj.has("preview_bn") ? obj.get("preview_bn").getAsString() : null;
                            String prevEn = obj.has("preview_en") ? obj.get("preview_en").getAsString() : null;
                            String refBn = obj.has("reference_bn") ? obj.get("reference_bn").getAsString() : "";
                            String refEn = obj.has("reference_en") ? obj.get("reference_en").getAsString() : "";

                            remoteItems.add(new RozaTaraweehItem(
                                    id, slug, titleBn, titleEn, catBn, catEn,
                                    arabic, transBn, transEn, trBn, trEn,
                                    prevBn, prevEn,
                                    detBn, detEn, refBn, refEn
                            ));
                        }

                        if (!remoteItems.isEmpty()) {
                            cachedList.clear();
                            cachedList.addAll(remoteItems);
                            if (callback != null) {
                                callback.onDataLoaded(new ArrayList<>(cachedList));
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Keep instant offline list intact
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Pre-seeded list containing all 9 items verbatim matching the screenshot.
     */
    public static List<RozaTaraweehItem> getDefaultTopics() {
        List<RozaTaraweehItem> list = new ArrayList<>();

        // 1. তারাবীহ কি (100% Verbatim matching user prompt & screenshot)
        String whatIsDetailsBn = "তারাবীহ নামাজ রমজান মাসের একটি বিশেষ ইবাদত, যা এশার নামাজের পরে আদায় করা হয়। এটি সুন্নাতে মুআক্কাদা (যে সুন্নত অনুসরণ করা অত্যন্ত গুরুত্বপূর্ণ) এবং রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) নিজে এটি আদায় করেছেন। এর পক্ষে কুরআন এবং হাদিস থেকে প্রমাণ রয়েছে।\n\n" +
                "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ\n\n" +
                "অর্থ:\n\n" +
                "হে ঈমানদারগণ! তোমাদের উপর রোজা ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তী লোকদের উপর, যাতে তোমরা তাকওয়া অর্জন করতে পারো।\n\n" +
                "[সুরা আল-বাকারাহ: ১৮৩]\n\n" +
                "হজরত আবু হুরাইরা (রা.) বর্ণিত হাদিস, রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) বলেছেন,\n\n" +
                "যে ব্যক্তি ঈমান সহকারে এবং সওয়াবের প্রত্যাশায় রমজান মাসে তারাবীহ নামাজ আদায় করে, তার পূর্বের সমস্ত গুনাহ মাফ করে দেওয়া হয়।\n\n" +
                "[বুখারি: ২০০৯, মুসলিম: ৭৫৯]\n\n" +
                "তারাবীহ নামাজের রাকাত:\n\n" +
                "তারাবীহ নামাজ সাধারণত ২০ রাকাত সুন্নাহ হিসেবে পরিচিত। তবে ৮ রাকাত নামাজও আদায় করা যায়। হজরত উমর (রা.)-এর খিলাফতের সময়ে ২০ রাকাত তারাবীহ নামাজ আদায় করার ব্যবস্থা করা হয়েছিল।\n\n" +
                "হজরত আয়েশা (রা.) বর্ণিত:\n\n" +
                "নবী (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) রমজানে এবং অন্যান্য সময় ১১ রাকাতের বেশি নামাজ পড়েননি।\n\n" +
                "[বুখারি: ২০১৩, মুসলিম: ৭৩৭]\n\n" +
                "এই হাদিসে নবীজির তারাবীহ নামাজের দৈর্ঘ্যের দিকে ইঙ্গিত করা হয়েছে। তবে পরে সাহাবিদের মাঝে ২০ রাকাত তারাবীহ নামাজের প্রচলন হয়।";

        String whatIsDetailsEn = "Taraweeh prayer is a special act of worship in the month of Ramadan, performed after the Isha prayer. It is Sunnah Mu'akkadah (a highly emphasized Sunnah) and the Messenger of Allah (peace be upon him) observed it himself. Evidence for it exists from the Quran and Hadith.\n\n" +
                "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ\n\n" +
                "Meaning:\n\n" +
                "O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.\n\n" +
                "[Surah Al-Baqarah: 183]\n\n" +
                "Hadith narrated by Hazrat Abu Hurairah (RA), the Messenger of Allah (peace be upon him) said:\n\n" +
                "Whoever prays during the nights of Ramadan out of sincere faith and seeking reward, all his past sins will be forgiven.\n\n" +
                "[Bukhari: 2009, Muslim: 759]\n\n" +
                "Rak'ahs of Taraweeh Prayer:\n\n" +
                "Taraweeh prayer is commonly known as 20 rak'ahs Sunnah. However, 8 rak'ahs can also be prayed. During the caliphate of Hazrat Umar (RA), arrangements were established for 20 rak'ahs of Taraweeh prayer.\n\n" +
                "Narrated by Hazrat Aisha (RA):\n\n" +
                "The Prophet (peace be upon him) did not pray more than eleven rak'ahs in Ramadan or in any other month.\n\n" +
                "[Bukhari: 2013, Muslim: 737]\n\n" +
                "This hadith indicates the length and nature of the Prophet's night prayer. Later, among the Companions, 20 rak'ahs became widely practiced.";

        String whatIsPreviewBn = "তারাবীহ নামাজ রমজান মাসের একটি বিশেষ ইবাদত, যা এশার নামাজের পরে আদায় করা হয়। এটি সুন্নাতে মুআক্কাদা (যে সুন্নত অনুসরণ করা অত্যন্ত গুরুত্বপূর্ণ) এবং রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) নিজে এটি আদায় করেছেন। এর পক্ষে কুরআন এবং হাদিস থেকে প্রমাণ রয়েছে (يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ...";

        String whatIsPreviewEn = "Taraweeh prayer is a special act of worship in the month of Ramadan, performed after the Isha prayer. It is Sunnah Mu'akkadah (a highly emphasized Sunnah) and the Messenger of Allah (peace be upon him) observed it himself. Evidence for it exists from the Quran and Hadith (O you who have believed, decreed upon you is fasting as it was decreed upon those before you...";

        list.add(new RozaTaraweehItem(
                1,
                "taraweeh_what_is",
                "তারাবীহ কি",
                "What is Taraweeh",
                "তারাবীহ পরিচয়",
                "Introduction to Taraweeh",
                "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
                "ইয়া আইয়্যুহাল্লাযীনা আমানূ কুতিবা আলাইকুমুস সিয়ামু কামা কুতিবা আলাল্লাযীনা মিন ক্বাবলিকুম লা'আল্লাকুম তাত্তাক্বূন।",
                "Ya ayyuhal-ladheena amanoo kutiba 'alaykumus-siyaamu kama kutiba 'alal-ladheena min qablikum la'allakum tattaqoon.",
                "হে ঈমানদারগণ! তোমাদের উপর রোজা ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তী লোকদের উপর, যাতে তোমরা তাকওয়া অর্জন করতে পারো।",
                "O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.",
                whatIsPreviewBn,
                whatIsPreviewEn,
                whatIsDetailsBn,
                whatIsDetailsEn,
                "[সুরা আল-বাকারাহ: ১৮৩, বুখারি: ২০০৯, মুসলিম: ৭৫৯, বুখারি: ২০১৩, মুসলিম: ৭৩৭]",
                "[Surah Al-Baqarah: 183, Bukhari: 2009, Muslim: 759, Bukhari: 2013, Muslim: 737]"
        ));

        // 2. তারাবীহর গুরুত্ব ও ফজিলত (100% Verbatim matching user prompt & screenshot)
        String virtuesDetailsBn = "তারাবীহ নামাজ রমজান মাসের একটি বিশেষ ইবাদত। এটি এশার নামাজের পরে আদায় করা হয় এবং মূলত কুরআন তিলাওয়াত ও রমজানের ফজিলতপূর্ণ সময়গুলোতে ইবাদতের জন্য নির্ধারিত। এর গুরুত্ব ও ফজিলত কুরআন ও হাদিসে সুস্পষ্টভাবে উল্লেখ রয়েছে।\n\n" +
                "কুরআন থেকে তারাবীহর গুরুত্ব, তাকওয়া অর্জনের নির্দেশ:\n\n" +
                "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ\n\n" +
                "অর্থ:\n\n" +
                "হে ঈমানদারগণ! তোমাদের উপর রোজা ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তী লোকদের উপর, যাতে তোমরা তাকওয়া অর্জন করতে পারো।\n\n" +
                "[সুরা আল-বাকারাহ: ১৮৩]\n\n" +
                "এই আয়াতে আল্লাহ রমজানের রোজা ফরজ করার মাধ্যমে তাকওয়া অর্জনের গুরুত্ব আরোপ করেছেন। তারাবীহ নামাজ এই তাকওয়া অর্জনের একটি মাধ্যম, যা রাতে আল্লাহর ইবাদতের জন্য নির্ধারিত।\n\n" +
                "রমজানের রাতের ইবাদত:\n\n" +
                "إِنَّا أَنْزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ\n\n" +
                "অর্থ:\n\n" +
                "নিশ্চয়ই আমি এ কুরআনকে কদরের রাতে অবতীর্ণ করেছি।\n\n" +
                "[সুরা আল-কদর: ১]\n\n" +
                "রমজানের রাতগুলোতে ইবাদতের বিশেষ গুরুত্ব রয়েছে, যার মধ্যে তারাবীহ নামাজ অন্যতম।\n\n" +
                "গুনাহ মাফ পাওয়া:\n\n" +
                "রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) বলেছেন:\n\n" +
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n" +
                "অর্থ:\n\n" +
                "যে ব্যক্তি ঈমান ও সওয়াবের আশায় রমজানের রাতে নামাজ (তারাবীহ) আদায় করবে, তার পূর্বের সমস্ত গুনাহ মাফ করে দেওয়া হয়।\n\n" +
                "[সহিহ বুখারি: ২০০৯, সহিহ মুসলিম: ৭৫৯]\n\n" +
                "সাহাবাদের তারাবীহ আদায়:\n\n" +
                "হজরত উমর ইবনে খাত্তাব (রা.)-এর খিলাফতের সময়ে তারাবীহ নামাজ জামাতে ২০ রাকাত হিসেবে আদায় করা হতো।\n\n" +
                "وَأَمَرَ عُمَرُ بْنُ الْخَطَّابِ أُبَيَّ بْنَ كَعْبٍ وَتَمِيمًا الدَّارِيَّ أَنْ يُصَلِّيَا بِالنَّاسِ إِحْدَى وَعِشْرِينَ رَكْعَةً\n\n" +
                "অর্থ:\n\n" +
                "উমর ইবনে খাত্তাব (রা.) উবাই ইবনে কাব ও তামিম আদ-দারি (রা.)-কে নির্দেশ দিয়েছিলেন, তারা যেন লোকদের নিয়ে ২১ রাকাত (তারাবীহ ও বিতর) নামাজ আদায় করেন।\n\n" +
                "[মুওয়াত্তা মালিক: ১/১১৫]\n\n" +
                "হজরত আয়েশা (রা.) বর্ণিত একটি হাদিসে বলা হয়েছে,\n\n" +
                "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يُصَلِّي فِي اللَّيْلِ إِحْدَى عَشْرَةَ رَكْعَةً\n\n" +
                "অর্থ:\n\n" +
                "নবী (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) রাতে ১১ রাকাত নামাজ পড়তেন।\n\n" +
                "[সহিহ বুখারি: ২০১৩, সহিহ মুসলিম: ৭৩৭]\n\n" +
                "এটি তারাবীহ নামাজের দীর্ঘ সময় ধরে পড়ার ফজিলতের প্রতি ইঙ্গিত করে।\n\n" +
                "তারাবীহর ফজিলত, জান্নাত লাভের সুসংবাদ:\n\n" +
                "রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) বলেন:\n\n" +
                "إِنَّ فِي الْجَنَّةِ غُرَفًا يُرَى ظَاهِرُهَا مِنْ بَاطِنِهَا، وَبَاطِنُهَا مِنْ ظَاهِرِهَا\n\n" +
                "অর্থ:\n\n" +
                "জান্নাতে এমন ঘর রয়েছে, যার ভেতর থেকে বাইরেটা এবং বাইরের থেকে ভেতরটা দেখা যাবে।\n\n" +
                "[তিরমিজি: ২৫২৫]\n\n" +
                "তারাবীহ নামাজের মাধ্যমে এই বিশেষ মর্যাদা লাভ করা সম্ভব।\n\n" +
                "আত্মার পরিশুদ্ধি:\n\n" +
                "রমজানের রাতের ইবাদত আত্মাকে পরিশুদ্ধ করে এবং আল্লাহর নৈকট্য লাভের সুযোগ তৈরি করে।\n\n" +
                "তাকওয়া ও সওয়াবের দ্বিগুণতা:\n\n" +
                "তারাবীহ নামাজ আল্লাহর সন্তুষ্টি অর্জন এবং দুনিয়া ও আখিরাতে বরকত লাভের মাধ্যম। তারাবীহ নামাজ রমজান মাসের বিশেষ সুন্নত এবং আল্লাহর কাছে আত্মসমর্পণের একটি পদ্ধতি। এটি ইমান দৃঢ় করা, গুনাহ মাফ পাওয়া, এবং জান্নাতের মর্যাদা লাভের মাধ্যম। প্রত্যেক মুসলিমের উচিত, নিয়মিত তারাবীহ নামাজ আদায় করা এবং এর ফজিলত অর্জনের চেষ্টা করা।";

        String virtuesDetailsEn = "Taraweeh prayer is a special act of worship during the month of Ramadan. It is performed after the Isha prayer and is primarily designated for Quran recitation and devotion during the virtuous times of Ramadan. Its importance and virtues are clearly stated in the Quran and Hadith.\n\n" +
                "Importance of Taraweeh from the Quran, Command to Attain Taqwa:\n\n" +
                "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ\n\n" +
                "Meaning:\n\n" +
                "O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.\n\n" +
                "[Surah Al-Baqarah: 183]\n\n" +
                "In this verse, Allah emphasizes attaining Taqwa through the obligation of fasting in Ramadan. Taraweeh prayer is a prime means of attaining this Taqwa, designated for worshipping Allah during the night.\n\n" +
                "Worship during Ramadan Nights:\n\n" +
                "إِنَّا أَنْزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ\n\n" +
                "Meaning:\n\n" +
                "Indeed, We sent the Quran down during the Night of Decree.\n\n" +
                "[Surah Al-Qadr: 1]\n\n" +
                "Special significance is attached to worship during the nights of Ramadan, among which Taraweeh prayer holds a foremost place.\n\n" +
                "Forgiveness of Sins:\n\n" +
                "The Messenger of Allah (peace be upon him) said:\n\n" +
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n" +
                "Meaning:\n\n" +
                "Whoever prays during the nights of Ramadan out of sincere faith and seeking reward, all his past sins will be forgiven.\n\n" +
                "[Sahih al-Bukhari: 2009, Sahih Muslim: 759]\n\n" +
                "Taraweeh Observed by the Companions:\n\n" +
                "During the caliphate of Hazrat Umar ibn al-Khattab (RA), Taraweeh prayer was performed in congregation as 20 rak'ahs.\n\n" +
                "وَأَمَرَ عُمَرُ بْنُ الْخَطَّابِ أُبَيَّ بْنَ كَعْبٍ وَتَمِيمًا الدَّارِيَّ أَنْ يُصَلِّيَا بِالنَّاسِ إِحْدَى وَعِشْرِينَ رَكْعَةً\n\n" +
                "Meaning:\n\n" +
                "Umar ibn al-Khattab (RA) instructed Ubayy ibn Ka'b and Tamim ad-Dari (RA) to lead the people in twenty-one rak'ahs (Taraweeh and Witr).\n\n" +
                "[Muwatta Malik: 1/115]\n\n" +
                "In a narration by Hazrat Aisha (RA), it is reported:\n\n" +
                "كَانَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يُصَلِّي فِي اللَّيْلِ إِحْدَى عَشْرَةَ رَكْعَةً\n\n" +
                "Meaning:\n\n" +
                "The Prophet (peace be upon him) used to pray eleven rak'ahs at night.\n\n" +
                "[Sahih al-Bukhari: 2013, Sahih Muslim: 737]\n\n" +
                "This indicates the profound virtue of praying long and devoted rak'ahs during Taraweeh.\n\n" +
                "Virtue of Taraweeh, Glad Tidings of Paradise:\n\n" +
                "The Messenger of Allah (peace be upon him) said:\n\n" +
                "إِنَّ فِي الْجَنَّةِ غُرَفًا يُرَى ظَاهِرُهَا مِنْ بَاطِنِهَا، وَبَاطِنُهَا مِنْ ظَاهِرِهَا\n\n" +
                "Meaning:\n\n" +
                "Indeed, in Paradise there are chambers whose outside can be seen from within, and whose inside can be seen from without.\n\n" +
                "[Jami' at-Tirmidhi: 2525]\n\n" +
                "This exalted status can be attained through Taraweeh prayer.\n\n" +
                "Purification of the Soul:\n\n" +
                "Worship during the nights of Ramadan purifies the soul and creates an invaluable opportunity to draw closer to Allah.\n\n" +
                "Taqwa and Multiplication of Rewards:\n\n" +
                "Taraweeh prayer is a means of attaining the pleasure of Allah and abundant blessings in this world and the Hereafter. Taraweeh prayer is a distinguished Sunnah of the blessed month of Ramadan and an avenue of complete surrender to Allah. It strengthens faith, cleanses sins, and elevates spiritual ranks in Paradise. Every Muslim should strive to perform Taraweeh regularly and attain its immense virtues.";

        String virtuesPreviewBn = "তারাবীহ নামাজ রমজান মাসের একটি বিশেষ ইবাদত। এটি এশার নামাজের পরে আদায় করা হয় এবং মূলত কুরআন তিলাওয়াত ও রমজানের ফজিলতপূর্ণ সময়গুলোতে ইবাদতের জন্য নির্ধারিত। এর গুরুত্ব ও ফজিলত কুরআন ও হাদিসে সুস্পষ্টভাবে উল্লেখ রয়েছে। কুরআন থেকে তারাবীহর গুরুত্ব, তাকওয়া অর্জনের নির্দেশ: كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ...";

        String virtuesPreviewEn = "Taraweeh prayer is a special act of worship in Ramadan, offered after Isha prayer and primarily designated for Quran recitation and devotion during the blessed times of Ramadan. Its importance and virtues are clearly mentioned in the Quran and Hadith. The importance of Taraweeh from the Quran, command to attain Taqwa: decreed upon you is fasting as it was decreed upon those before you...";

        list.add(new RozaTaraweehItem(
                2,
                "taraweeh_importance_virtues",
                "তারাবীহর গুরুত্ব ও ফজিলত",
                "Importance and Virtues of Taraweeh",
                "ফজিলত ও মর্যাদা",
                "Virtues & Status",
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
                "মান ক্বামা রামাধানা ঈমানান ওয়াহতিসাবান গুফিরা লাহু মা তাক্বাদ্দামা মিন যামবিহ।",
                "Man qama Ramadana imanan wahtisaban ghufira lahu ma taqaddama min dhanbih.",
                "যে ব্যক্তি ঈমান ও সওয়াবের আশায় রমজানের রাতে নামাজ (তারাবীহ) আদায় করবে, তার পূর্বের সমস্ত গুনাহ মাফ করে দেওয়া হয়।",
                "Whoever prays during the nights of Ramadan out of sincere faith and seeking reward, all his past sins will be forgiven.",
                virtuesPreviewBn,
                virtuesPreviewEn,
                virtuesDetailsBn,
                virtuesDetailsEn,
                "[সুরা আল-বাকারাহ: ১৮৩, সুরা আল-কদর: ১, সহিহ বুখারি: ২০০৯, সহিহ মুসলিম: ৭৫৯, মুওয়াত্তা মালিক: ১/১১৫, সহিহ বুখারি: ২০১৩, সহিহ মুসলিম: ৭৩৭, তিরমিজি: ২৫২৫]",
                "[Surah Al-Baqarah: 183, Surah Al-Qadr: 1, Sahih al-Bukhari: 2009, Sahih Muslim: 759, Muwatta Malik: 1/115, Sahih al-Bukhari: 2013, Sahih Muslim: 737, Jami' at-Tirmidhi: 2525]"
        ));

        // 3. তারাবীহ নামাজের বিধান (100% Verbatim matching user prompt & screenshot)
        String rulingsDetailsBn = "তারাবীহ নামাজ হলো রমজান মাসে এশার নামাজের পর আদায় করা একটি বিশেষ ইবাদত। এটি সুন্নাতে মুআক্কাদা (মজবুত সুন্নত) হিসেবে গণ্য হয়। মুসলিম উম্মাহর মধ্যে এটি অত্যন্ত গুরুত্বপূর্ণ ইবাদত হিসেবে বিবেচিত হয় এবং এটি রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) এবং সাহাবিদের দ্বারা প্রতিষ্ঠিত।\n\n" +
                "বাধ্যতামূলক নয়, তবে সুন্নাতে মুআক্কাদা:\n\n" +
                "তারাবীহ নামাজ ফরজ বা ওয়াজিব নয়। এটি রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) দ্বারা প্রতিষ্ঠিত একটি গুরুত্বপূর্ণ সুন্নত।\n\n" +
                "জামাতে পড়া উত্তম:\n\n" +
                "মসজিদে জামাতে তারাবীহ নামাজ আদায় করা সুন্নাত। তবে কেউ যদি একা বাড়িতে পড়ে, তাহলেও তা সহীহ হবে।\n\n" +
                "নারীদের জন্য অনুমতি:\n\n" +
                "নারীরা মসজিদে গিয়ে তারাবীহ নামাজ জামাতে আদায় করতে পারেন। তবে ঘরে পড়া তাদের জন্য অধিক উত্তম।\n\n" +
                "রাকাত সংখ্যা:\n\n" +
                "তারাবীহ নামাজের রাকাত সংখ্যার বিষয়ে ভিন্নমত রয়েছে। অধিকাংশ মুসলিম বিশ্বে এটি ২০ রাকাত আদায় করা হয়। কিছু স্থানে এটি ৮ রাকাত পড়া হয়। রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) রাতের নামাজে সাধারণত ৮ রাকাত পড়তেন এবং বিতরসহ ১১ রাকাত সম্পন্ন করতেন।\n\n" +
                "বিতরের সঙ্গে সম্পর্ক:\n\n" +
                "তারাবীহ নামাজ এশার নামাজের পর পড়া হয় এবং এর পরে বিতর নামাজ আদায় করা হয়। বিতর নামাজের মাধ্যমে রাতের ইবাদত সমাপ্ত হয়।\n\n" +
                "পড়ার সময়:\n\n" +
                "তারাবীহ নামাজ এশার নামাজের পরে শুরু হয়ে তাহাজ্জুদের সময়ের আগ পর্যন্ত পড়া যায়। তবে সাধারণত এশার নামাজের পরে জামাতে পড়া হয়।\n\n" +
                "কুরআন ও হাদিস থেকে তারাবীহ নামাজের বিধান:\n\n" +
                "আল্লাহ তাআলা বলেছেন,\n\n" +
                "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَكَ عَسَىٰ أَنْ يَبْعَثَكَ رَبُّكَ مَقَامًا مَحْمُودًا\n\n" +
                "অর্থ:\n\n" +
                "রাতের কিছু অংশে কুরআন দ্বারা তাহাজ্জুদ পড়ো। এটি তোমার জন্য একটি নফল ইবাদত। আশা করা যায় তোমার প্রভু তোমাকে প্রশংসিত একটি স্থানে উত্তীর্ণ করবেন।\n\n" +
                "[সুরা আল-ইসরা: ৭৯]\n\n" +
                "রমজানের তারাবীহ নামাজ এই রাতের বিশেষ ইবাদতের একটি অংশ। রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) বলেছেন,\n\n" +
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n" +
                "অর্থ:\n\n" +
                "যে ব্যক্তি ঈমান ও সওয়াবের আশায় রমজানের রাতের নামাজ (তারাবীহ) আদায় করবে, তার পূর্বের সমস্ত গুনাহ মাফ করে দেওয়া হবে।\n\n" +
                "[সহিহ বুখারি: ২০০৯, সহিহ মুসলিম: ৭৫৯]\n\n" +
                "সাহাবিদের সময়ে তারাবীহ:\n\n" +
                "হজরত উমর (রা.) তারাবীহ নামাজের জন্য জামাতের ব্যবস্থা করেন।\n\n" +
                "نِعْمَتِ الْبِدْعَةُ هَذِهِ\n\n" +
                "অর্থ:\n\n" +
                "এটি একটি উত্তম প্রচলন।\n\n" +
                "[সহিহ বুখারি: ২০১০]\n\n" +
                "তারাবীহ নামাজের উদ্দেশ্য\n\n" +
                "• তাকওয়া অর্জন করা\n" +
                "• গুনাহ মাফ পাওয়া\n" +
                "• আল্লাহর নৈকট্য লাভ করা\n" +
                "• কুরআনের সঙ্গে সম্পর্ক স্থাপন করা";

        String rulingsDetailsEn = "Taraweeh prayer is a special act of worship performed after the Isha prayer in the month of Ramadan. It is regarded as Sunnah Mu'akkadah (an emphasized Sunnah). It is considered a deeply significant worship among the Muslim Ummah and was established by the Messenger of Allah (peace be upon him) and his Companions.\n\n" +
                "Not obligatory, but Sunnah Mu'akkadah:\n\n" +
                "Taraweeh prayer is neither Fard (obligatory) nor Wajib. It is an emphasized Sunnah established by the Messenger of Allah (peace be upon him).\n\n" +
                "Preferable to pray in congregation:\n\n" +
                "Offering Taraweeh prayer in congregation at the mosque is Sunnah. However, if someone prays alone at home, it is also valid.\n\n" +
                "Permission for women:\n\n" +
                "Women may go to the mosque to offer Taraweeh prayer in congregation. However, praying at home is more excellent for them.\n\n" +
                "Number of Rak'ahs:\n\n" +
                "There are different perspectives regarding the number of rak'ahs in Taraweeh prayer. In most parts of the Muslim world, 20 rak'ahs are performed. In some places, 8 rak'ahs are prayed. The Messenger of Allah (peace be upon him) typically prayed 8 rak'ahs during the night prayer and concluded with 11 rak'ahs including Witr.\n\n" +
                "Relation with Witr:\n\n" +
                "Taraweeh prayer is performed after the Isha prayer, followed by the Witr prayer. Night worship is concluded through the Witr prayer.\n\n" +
                "Time of performance:\n\n" +
                "Taraweeh prayer begins after the Isha prayer and can be prayed until the time of Tahajjud. However, it is generally prayed in congregation immediately after the Isha prayer.\n\n" +
                "Rulings of Taraweeh Prayer from the Quran and Hadith:\n\n" +
                "Allah the Almighty has said:\n\n" +
                "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَكَ عَسَىٰ أَنْ يَبْعَثَكَ رَبُّكَ مَقَامًا مَحْمُودًا\n\n" +
                "Meaning:\n\n" +
                "And from part of the night, pray with it as additional worship for you; it is expected that your Lord will raise you to a praised station.\n\n" +
                "[Surah Al-Isra: 79]\n\n" +
                "Ramadan's Taraweeh prayer is part of this special night worship. The Messenger of Allah (peace be upon him) said:\n\n" +
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n" +
                "Meaning:\n\n" +
                "Whoever prays during the nights of Ramadan out of sincere faith and seeking reward, all his past sins will be forgiven.\n\n" +
                "[Sahih al-Bukhari: 2009, Sahih Muslim: 759]\n\n" +
                "Taraweeh in the era of the Companions:\n\n" +
                "Hazrat Umar (RA) arranged the congregation for Taraweeh prayer.\n\n" +
                "نِعْمَتِ الْبِدْعَةُ هَذِهِ\n\n" +
                "Meaning:\n\n" +
                "What an excellent innovation (practice) this is.\n\n" +
                "[Sahih al-Bukhari: 2010]\n\n" +
                "Objectives of Taraweeh Prayer\n\n" +
                "• Attaining Taqwa (God-consciousness)\n" +
                "• Receiving forgiveness of sins\n" +
                "• Gaining proximity to Allah\n" +
                "• Establishing a connection with the Quran";

        String rulingsPreviewBn = "তারাবীহ নামাজ হলো রমজান মাসে এশার নামাজের পর আদায় করা একটি বিশেষ ইবাদত। এটি সুন্নাতে মুআক্কাদা (মজবুত সুন্নত) হিসেবে গণ্য হয়। মুসলিম উম্মাহর মধ্যে এটি অত্যন্ত গুরুত্বপূর্ণ ইবাদত হিসেবে বিবেচিত হয় এবং এটি রাসুলুল্লাহ (সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম) এবং সাহাবিদের দ্বারা প্রতিষ্ঠিত। বাধ্যতামূলক নয়, তবে সুন্নাতে মুআক্কাদা: তারাবীহ না...";

        String rulingsPreviewEn = "Taraweeh prayer is a special act of worship performed after the Isha prayer in the month of Ramadan. It is regarded as Sunnah Mu'akkadah (an emphasized Sunnah). It is considered a deeply significant worship among the Muslim Ummah and was established by the Messenger of Allah (peace be upon him) and his Companions. Not obligatory, but Sunnah Mu'akkadah: Taraweeh...";

        list.add(new RozaTaraweehItem(
                3,
                "taraweeh_rulings",
                "তারাবীহ নামাজের বিধান",
                "Rules of Taraweeh Prayer",
                "শারয়ী বিধান ও মাসআলা",
                "Shar'i Rulings",
                "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَكَ عَسَىٰ أَنْ يَبْعَثَكَ رَبُّكَ مَقَامًا مَحْمُودًا",
                "ওয়া মিনাল লাইলি ফাতাহাজ্জাদ বিহী নাফিলাতাল লাকা 'আসা আঁই ইয়াব'আছাকা রাব্বুকা মাক্বামাম মাহমূদা।",
                "Wa minal-layli fatahajjad bihee naafilatan laka 'asaa ay-yab'athaka rabbuka maqaamam-mahmoodaa.",
                "রাতের কিছু অংশে কুরআন দ্বারা তাহাজ্জুদ পড়ো। এটি তোমার জন্য একটি নফল ইবাদত। আশা করা যায় তোমার প্রভু তোমাকে প্রশংসিত একটি স্থানে উত্তীর্ণ করবেন।",
                "And from part of the night, pray with it as additional worship for you; it is expected that your Lord will raise you to a praised station.",
                rulingsPreviewBn,
                rulingsPreviewEn,
                rulingsDetailsBn,
                rulingsDetailsEn,
                "[সুরা আল-ইসরা: ৭৯, সহিহ বুখারি: ২০০৯, সহিহ মুসলিম: ৭৫৯, সহিহ বুখারি: ২০১০]",
                "[Surah Al-Isra: 79, Sahih al-Bukhari: 2009, Sahih Muslim: 759, Sahih al-Bukhari: 2010]"
        ));

        // 4. তারাবীহর আয়োজন (100% Verbatim matching user prompt & screenshot)
        String prepDetailsBn = "তারাবীহ নামাজের আয়োজন রমজান মাসে একটি গুরুত্বপূর্ণ ধর্মীয় ইবাদত। এটির সঠিকভাবে আয়োজনের জন্য কিছু বিষয় অনুসরণ করা হয়। তারাবীহ নামাজের আয়োজন ব্যক্তি বা জামাত উভয়ভাবেই করা যেতে পারে। নিচে তারাবীহ নামাজের আয়োজন সম্পর্কিত গুরুত্বপূর্ণ দিকগুলো আলোচনা করা হলো,\n\n" +
                "তারাবীহ নামাজের আয়োজনের ধাপ:\n\n" +
                "নামাজের সময়:\n\n" +
                "তারাবীহ নামাজ এশার নামাজের পর থেকে তাহাজ্জুদের সময় পর্যন্ত আদায় করা যায়। সাধারণত জামাতের আয়োজন এশার নামাজের পর মসজিদে করা হয়।\n\n" +
                "স্থান নির্বাচন:\n\n" +
                "মসজিদ: তারাবীহ নামাজের জন্য জামাত আয়োজনের উত্তম স্থান।\n\n" +
                "বাড়ি: যারা মসজিদে যেতে পারেন না, তারা বাড়িতে একা বা পরিবারের সদস্যদের সঙ্গে তারাবীহ পড়তে পারেন।\n\n" +
                "রাকাত সংখ্যা:\n\n" +
                "অধিকাংশ মুসলিম দেশে তারাবীহ ২০ রাকাত পড়া হয়। কিছু স্থানে বা ব্যক্তিগতভাবে ৮ রাকাত নামাজ পড়া হয়। রাকাত সংখ্যা যাই হোক, এর পরে ৩ রাকাত বিতর নামাজ পড়া হয়।\n\n" +
                "ইমাম নির্বাচন (যদি জামাত হয়):\n\n" +
                "তারাবীহ নামাজের জন্য একজন যোগ্য ইমাম নির্বাচন করা হয়, যিনি কুরআন তিলাওয়াত সঠিকভাবে করতে পারেন। ইমামের কুরআনের হাফেজ হওয়া উত্তম, যাতে পুরো রমজানে কুরআনের খতম করা যায়।\n\n" +
                "কুরআন খতম:\n\n" +
                "তারাবীহ নামাজে কুরআন খতম করার প্রথা অনেক স্থানে প্রচলিত। ইমাম প্রতিদিন নির্দিষ্ট পরিমাণ কুরআন তিলাওয়াত করেন, যা ২৭ বা ২৯ রমজানে সম্পন্ন হয়।\n\n" +
                "জামাতের আয়োজন:\n\n" +
                "সারিবদ্ধভাবে দাঁড়ানো: জামাতে মুসল্লিরা সারিবদ্ধভাবে দাঁড়িয়ে নামাজ আদায় করেন।\n\n" +
                "দুই রাকাত করে নামাজ: সাধারণত তারাবীহ ১০ বার দুই রাকাত করে পড়া হয় (যদি ২০ রাকাত হয়)।\n\n" +
                "তারাবীহর দোয়া:\n\n" +
                "তারাবীহ নামাজের শেষ রাকাতের পরে একটি বিশেষ দোয়া পড়া হয়।\n\n" +
                "سُبْحَانَ ذِي الْمُلْكِ وَالْمَلَكُوتِ، سُبْحَانَ ذِي الْعِزَّةِ وَالْعَظَمَةِ وَالْهَيْبَةِ وَالْقُدْرَةِ وَالْكِبْرِيَاءِ وَالْجَبَرُوتِ\n\n" +
                "অর্থ:\n\n" +
                "মহিমাময় রাজত্ব ও শাসনের অধিকারী মহান সত্তা পবিত্র, মহা গৌরবময় এবং শক্তির অধিকারী।\n\n" +
                "বিতর নামাজ:\n\n" +
                "তারাবীহ নামাজ শেষ হলে জামাতে বা একা ৩ রাকাত বিতর নামাজ পড়া হয়। এটি রাতের ইবাদতের সমাপ্তি নির্দেশ করে।\n\n" +
                "তারাবীহ নামাজে শান্তি ও স্থিরতা বজায় রাখা:\n\n" +
                "• তারাবীহ নামাজ ধীরস্থিরভাবে পড়া উচিত।\n\n" +
                "• কুরআন তিলাওয়াত বুঝে এবং মনোযোগ দিয়ে শোনা উচিত।\n\n" +
                "• দীর্ঘ সময় ধরে দাঁড়িয়ে নামাজ পড়লে ক্লান্তি এড়াতে নিয়মিত পানি পান ও বিশ্রাম নেওয়া যেতে পারে।";

        String prepDetailsEn = "Arranging Taraweeh prayer is an important religious act of devotion in the month of Ramadan. Certain guidelines are observed for organizing it properly. Arrangements for Taraweeh prayer can be made either individually or in congregation. The vital aspects concerning the arrangement of Taraweeh prayer are discussed below:\n\n" +
                "Steps for Arranging Taraweeh Prayer:\n\n" +
                "Prayer Time:\n\n" +
                "Taraweeh prayer can be performed from after the Isha prayer until the time of Tahajjud. Typically, the congregational arrangement is made at the mosque following the Isha prayer.\n\n" +
                "Place Selection:\n\n" +
                "Mosque: The finest location for arranging congregational Taraweeh prayer.\n\n" +
                "Home: Those who cannot attend the mosque may pray Taraweeh at home individually or together with family members.\n\n" +
                "Number of Rak'ahs:\n\n" +
                "In most Muslim countries, 20 rak'ahs of Taraweeh are performed. In some places or individually, 8 rak'ahs are prayed. Whatever the number of rak'ahs, it is followed by 3 rak'ahs of Witr prayer.\n\n" +
                "Selecting the Imam (in case of congregation):\n\n" +
                "A qualified Imam who can recite the Holy Quran accurately is chosen for Taraweeh prayer. It is preferable for the Imam to be a Hafiz of the Quran so that a complete recitation of the Quran can be concluded throughout Ramadan.\n\n" +
                "Khatam of the Quran:\n\n" +
                "The tradition of completing the Quran in Taraweeh prayer is widespread in many places. The Imam recites a designated portion of the Quran daily, completing it by the 27th or 29th of Ramadan.\n\n" +
                "Congregational Arrangement:\n\n" +
                "Standing in Straight Rows: Worshippers stand in aligned rows to offer prayer in congregation.\n\n" +
                "Praying in Two Rak'ah Units: Taraweeh is generally offered in 10 sets of two rak'ahs each (if praying 20 rak'ahs).\n\n" +
                "Dua of Taraweeh:\n\n" +
                "A special supplication is recited after the final rak'ah of Taraweeh prayer:\n\n" +
                "سُبْحَانَ ذِي الْمُلْكِ وَالْمَلَكُوتِ، سُبْحَانَ ذِي الْعِزَّةِ وَالْعَظَمَةِ وَالْهَيْبَةِ وَالْقُدْرَةِ وَالْكِبْرِيَاءِ وَالْجَبَرُوتِ\n\n" +
                "Meaning:\n\n" +
                "Pure and glorified is the Master of the earthly and celestial realm; pure and glorified is the Possessor of might, majesty, awe, power, greatness, and supreme authority.\n\n" +
                "Witr Prayer:\n\n" +
                "Once the Taraweeh prayer concludes, 3 rak'ahs of Witr prayer are offered either in congregation or individually. This signifies the completion of the night worship.\n\n" +
                "Maintaining Peace and Composure in Taraweeh Prayer:\n\n" +
                "• Taraweeh prayer should be performed in a calm and measured manner.\n\n" +
                "• The Quran recitation should be listened to attentively and with comprehension.\n\n" +
                "• When standing in prayer for extended periods, regular hydration and brief rests may be taken to avoid fatigue.";

        String prepPreviewBn = "তারাবীহ নামাজের আয়োজন রমজান মাসে একটি গুরুত্বপূর্ণ ধর্মীয় ইবাদত। এটির সঠিকভাবে আয়োজনের জন্য কিছু বিষয় অনুসরণ করা হয়। তারাবীহ নামাজের আয়োজন ব্যক্তি বা জামাত উভয়ভাবেই করা যেতে পারে। নিচে তারাবীহ নামাজের আয়োজন সম্পর্কিত গুরুত্বপূর্ণ দিকগুলো আলোচনা করা হলো,তারাবীহ নামাজের আয়োজনের ধাপ:নামা...";

        String prepPreviewEn = "Arranging Taraweeh prayer is an important religious act of devotion in the month of Ramadan. Certain guidelines are observed for organizing it properly. Arrangements for Taraweeh prayer can be made either individually or in congregation. The vital aspects concerning the arrangement of Taraweeh prayer are discussed below, Steps for Arranging Taraweeh Prayer: Prayer...";

        list.add(new RozaTaraweehItem(
                4,
                "taraweeh_preparation",
                "তারাবীহর আয়োজন",
                "Arrangements for Taraweeh",
                "প্রস্তুতি ও ব্যবস্থাপনা",
                "Preparation & Arrangements",
                "سُبْحَانَ ذِي الْمُلْكِ وَالْمَلَكُوتِ، سُبْحَانَ ذِي الْعِزَّةِ وَالْعَظَمَةِ وَالْهَيْبَةِ وَالْقُدْرَةِ وَالْكِبْرِيَاءِ وَالْجَبَرُوتِ",
                "সুবহানা যিল মুলকি ওয়াল মালাকূত, সুবহানা যিল 'ইয্যাতি ওয়াল 'আযামাতি ওয়াল হাইবাতি ওয়াল ক্বুদরাতি ওয়াল কিবরিয়া-ই ওয়াল জাবারূত।",
                "Subhana dhil-mulki wal-malakoot, subhana dhil-'izzati wal-'azamati wal-haybati wal-qudrati wal-kibriya-i wal-jabaroot.",
                "মহিমাময় রাজত্ব ও শাসনের অধিকারী মহান সত্তা পবিত্র, মহা গৌরবময় এবং শক্তির অধিকারী।",
                "Pure and glorified is the Master of the earthly and celestial realm; pure and glorified is the Possessor of might, majesty, awe, power, greatness, and supreme authority.",
                prepPreviewBn,
                prepPreviewEn,
                prepDetailsBn,
                prepDetailsEn,
                "[ফতোয়ায়ে হিন্দিয়া: ১/১১৫, আল-মাবসূত: ২/১৪৫]",
                "[Fatawa al-Hindiyyah: 1/115, Al-Mabsut: 2/145]"
        ));

        // 5. তারাবীহর দোয়া ও সূরা (100% Verbatim matching user prompt & screenshot)
        String duaSurahsDetailsBn = "তারাবীহ নামাজে নির্দিষ্ট কোনো সূরা পড়া বাধ্যতামূলক নয়। তবে সাধারণত কুরআনের আয়াত ধারাবাহিকভাবে তিলাওয়াত করা হয়। পাশাপাশি তারাবীহ নামাজে দোয়া ও জিকির পড়ার কিছু সুন্নত আমল রয়েছে, যা রমজানের বিশেষ ইবাদতের অংশ।\n\n" +
                "তারাবীহ নামাজের দোয়া:\n\n" +
                "তারাবীহ নামাজে প্রতিটি চার রাকাতের পর একটি ছোট বিরতি নেওয়া হয়, যা তরাবীহা বলা হয়। এই বিরতিতে সাধারণত একটি বিশেষ দোয়া পড়া হয়।\n\n" +
                "سُبْحَانَ ذِي الْمُلْكِ وَالْمَلَكُوتِ، سُبْحَانَ ذِي الْعِزَّةِ وَالْعَظَمَةِ وَالْهَيْبَةِ وَالْقُدْرَةِ وَالْكِبْرِيَاءِ وَالْجَبَرُوتِ، سُبْحَانَ الْمَلِكِ الْحَيِّ الَّذِي لَا يَمُوتُ، سُبُّوحٌ قُدُّوسٌ رَبُّنَا وَرَبُّ الْمَلَائِكَةِ وَالرُّوحِ، اللَّهُمَّ أَجِرْنَا مِنَ النَّارِ يَا مُجِيرُ، يَا مُجِيرُ، يَا مُجِيرُ\n\n" +
                "অর্থ:\n\n" +
                "মহিমাময় রাজত্ব ও শাসনের অধিকারী মহান সত্তা পবিত্র, মহা গৌরবময়, শক্তির অধিকারী। এমন রব যিনি চিরঞ্জীব এবং কখনো মৃত্যুবরণ করবেন না। আমাদের রব, ফেরেশতাদের এবং জিবরাঈল (আ.) এরও রব। হে আল্লাহ, আমাদের জাহান্নামের আগুন থেকে রক্ষা করুন। হে রক্ষাকারী, হে রক্ষাকারী, হে রক্ষাকারী।\n\n" +
                "তারাবীহ নামাজে সূরা পড়ার নিয়ম, ধারাবাহিক কুরআন তিলাওয়াত:\n\n" +
                "ইমাম বা ব্যক্তি কুরআনের আয়াত ধারাবাহিকভাবে তারাবীহ নামাজে তিলাওয়াত করেন। জামাতে সাধারণত পুরো রমজান মাসে কুরআন খতম করার চেষ্টা করা হয়।\n\n" +
                "ছোট সূরা পড়া:\n\n" +
                "যদি কেউ কুরআনের আয়াত বা সূরা বেশি না জানেন, তবে তিনি ছোট সূরা যেমন:\n\n" +
                "সুরা ইখলাস (قُلْ هُوَ اللَّهُ أَحَدٌ)\n" +
                "সুরা ফালাক (قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ)\n" +
                "সুরা নাস (قُلْ أَعُوذُ بِرَبِّ النَّاسِ)\n" +
                "এগুলো পড়তে পারেন।\n\n" +
                "প্রতি রাকাতে আলাদা সূরা:\n\n" +
                "প্রতি রাকাতে ফাতিহা পড়ার পর আলাদা কোনো সূরা পড়া উত্তম। তবে বারবার একই সূরা পড়লেও নামাজ সহীহ হবে।\n\n" +
                "তারাবীহ নামাজের জন্য গুরুত্বপূর্ণ সূরা:\n\n" +
                "তারাবীহ নামাজ দীর্ঘ সময় ধরে পড়া হয়। যারা পুরো কুরআন পড়তে পারেন না, তারা নিচের সূরাগুলো ধারাবাহিকভাবে পড়তে পারেন:\n\n" +
                "সুরা বাকারাহ (আয়াতের অংশ)\n" +
                "সুরা আল ইমরান (আয়াতের অংশ)\n" +
                "সুরা নিসা (আয়াতের অংশ)\n" +
                "সুরা মুলক\n" +
                "সুরা ওয়াকিয়া\n" +
                "সুরা রহমান";

        String duaSurahsDetailsEn = "Reciting specific Surahs in Taraweeh prayer is not obligatory. However, verses of the Holy Quran are typically recited in sequence. Alongside this, reciting supplications and remembrances (Dhikr) in Taraweeh prayer carries established Sunnah virtues, serving as an integral part of Ramadan's special devotion.\n\n" +
                "Dua of Taraweeh Prayer:\n\n" +
                "In Taraweeh prayer, a brief pause is taken after every four rak'ahs, which is known as 'Tarweehah'. During this pause, a special supplication is traditionally recited:\n\n" +
                "سُبْحَانَ ذِي الْمُلْكِ وَالْمَلَكُوتِ، سُبْحَانَ ذِي الْعِزَّةِ وَالْعَظَمَةِ وَالْهَيْبَةِ وَالْقُدْرَةِ وَالْكِبْرِيَاءِ وَالْجَبَرُوتِ، سُبْحَانَ الْمَلِكِ الْحَيِّ الَّذِي لَا يَمُوتُ، سُبُّوحٌ قُدُّوسٌ رَبُّنَا وَرَبُّ الْمَلَائِكَةِ وَالرُّوحِ، اللَّهُمَّ أَجِرْنَا مِنَ النَّارِ يَا مُجِيرُ، يَا مُجِيرُ، يَا مُجِيرُ\n\n" +
                "Meaning:\n\n" +
                "Glory be to the Possessor of the kingdom and the sovereignty; glory be to the Possessor of might, majesty, awe, power, greatness, and supreme authority. Glory be to the Sovereign King, the Ever-Living Who never dies. All-Glorious, All-Holy, our Lord and the Lord of the angels and of the Spirit. O Allah, save us from the fire of Hell, O Protector, O Protector, O Protector.\n\n" +
                "Rules of Reciting Surahs in Taraweeh Prayer, Sequential Quran Recitation:\n\n" +
                "The Imam or individual recites verses of the Holy Quran sequentially during Taraweeh prayer. In congregational prayer, the goal is typically to complete the entire Quran during the month of Ramadan.\n\n" +
                "Reciting Short Surahs:\n\n" +
                "If someone does not know many verses or Surahs of the Quran, they may recite shorter Surahs, such as:\n\n" +
                "Surah Al-Ikhlas (قُلْ هُوَ اللَّهُ أَحَدٌ)\n" +
                "Surah Al-Falaq (قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ)\n" +
                "Surah An-Nas (قُلْ أَعُوذُ بِرَبِّ النَّاسِ)\n" +
                "These may be recited.\n\n" +
                "Distinct Surah in Each Rak'ah:\n\n" +
                "It is preferable to recite a different Surah after Surah Al-Fatihah in each rak'ah. However, repeating the same Surah multiple times also renders the prayer valid.\n\n" +
                "Significant Surahs for Taraweeh Prayer:\n\n" +
                "Taraweeh prayer is performed for an extended duration. Those unable to recite the entire Quran may recite the following Surahs sequentially:\n\n" +
                "Surah Al-Baqarah (selected verses)\n" +
                "Surah Ali 'Imran (selected verses)\n" +
                "Surah An-Nisa (selected verses)\n" +
                "Surah Al-Mulk\n" +
                "Surah Al-Waqi'ah\n" +
                "Surah Ar-Rahman";

        String duaSurahsPreviewBn = "তারাবীহ নামাজে নির্দিষ্ট কোনো সূরা পড়া বাধ্যতামূলক নয়। তবে সাধারণত কুরআনের আয়াত ধারাবাহিকভাবে তিলাওয়াত করা হয়। পাশাপাশি তারাবীহ নামাজে দোয়া ও জিকির পড়ার কিছু সুন্নত আমল রয়েছে, যা রমজানের বিশেষ ইবাদতের অংশ তারাবীহ নামাজের দোয়া:তারাবীহ নামাজে প্রতিটি চার রাকাতের পর একটি ছোট বিরতি নেওয়া হয়, যা তরাবীহা ...";

        String duaSurahsPreviewEn = "Reciting specific Surahs in Taraweeh prayer is not obligatory. However, verses of the Holy Quran are typically recited in sequence. Alongside this, reciting supplications and remembrances in Taraweeh carries established Sunnah practices as part of Ramadan's special devotion. Dua of Taraweeh Prayer: A brief pause is observed after every four rak'ahs...";

        list.add(new RozaTaraweehItem(
                5,
                "taraweeh_dua_surahs",
                "তারাবীহর দোয়া ও সূরা",
                "Dua and Surahs of Taraweeh",
                "মাসনূন তাসবীহ ও সুরা",
                "Supplications & Surahs",
                "سُبْحَانَ ذِي الْمُلْكِ وَالْمَلَكُوتِ، سُبْحَانَ ذِي الْعِزَّةِ وَالْعَظَمَةِ وَالْهَيْبَةِ وَالْقُدْرَةِ وَالْكِبْرِيَاءِ وَالْجَبَرُوتِ، سُبْحَانَ الْمَلِكِ الْحَيِّ الَّذِي لَا يَمُوتُ، سُبُّوحٌ قُدُّوسٌ رَبُّنَا وَرَبُّ الْمَلَائِكَةِ وَالرُّوحِ، اللَّهُمَّ أَجِرْنَا مِنَ النَّارِ يَا مُجِيرُ، يَا مُجِيرُ، يَا مُجِيرُ",
                "সুবহানা যিল মুলকি ওয়াল মালাকূত, সুবহানা যিল 'ইয্যাতি ওয়াল 'আযামাতি ওয়াল হাইবাতি ওয়াল ক্বুদরাতি ওয়াল কিবরিয়া-ই ওয়াল জাবারূত, সুবহানাল মালিকিল হাইয়্যিল্লাযী লা ইয়ামূত, সুব্বূহুন কুদ্দূসুন রাব্বুনা ওয়া রাব্বুল মালাইকাতি ওয়ার রূহ, আল্লাহুম্মা আজিরনা মিনান-নার ইয়া মুজীর, ইয়া মুজীর, ইয়া মুজীর।",
                "Subhana dhil-mulki wal-malakoot, subhana dhil-'izzati wal-'azamati wal-haybati wal-qudrati wal-kibriya-i wal-jabaroot, subhanal-malikil-hayyil-ladhee la yamoot, subboohun quddoosun rabbuna wa rabbul-mala'ikati war-rooh, Allahumma ajirna minan-naari ya Mujeer, ya Mujeer, ya Mujeer.",
                "মহিমাময় রাজত্ব ও শাসনের অধিকারী মহান সত্তা পবিত্র, মহা গৌরবময়, শক্তির অধিকারী। এমন রব যিনি চিরঞ্জীব এবং কখনো মৃত্যুবরণ করবেন না। আমাদের রব, ফেরেশতাদের এবং জিবরাঈল (আ.) এরও রব। হে আল্লাহ, আমাদের জাহান্নামের আগুন থেকে রক্ষা করুন। হে রক্ষাকারী, হে রক্ষাকারী, হে রক্ষাকারী।",
                "Glory be to the Possessor of the kingdom and the sovereignty; glory be to the Possessor of might, majesty, awe, power, greatness, and supreme authority. Glory be to the Sovereign King, the Ever-Living Who never dies. All-Glorious, All-Holy, our Lord and the Lord of the angels and of the Spirit. O Allah, save us from the fire of Hell, O Protector, O Protector, O Protector.",
                duaSurahsPreviewBn,
                duaSurahsPreviewEn,
                duaSurahsDetailsBn,
                duaSurahsDetailsEn,
                "[আল-মাবসূত লিস-সারাখসী: ২/১৪৫, রাদ্দুল মুহতার: ২/৪৬]",
                "[Al-Mabsut by As-Sarakhsi: 2/145, Radd al-Muhtar: 2/46]"
        ));

        // 6. তারাবীহর ফজিলত ও আধুনিক প্রাসঙ্গিকতা (100% Verbatim matching user prompt & screenshot)
        String modernDetailsBn = "তারাবীহ নামাজ ইসলামের একটি বিশেষ ইবাদত যা রমজান মাসে এশার নামাজের পরে জামাআতের সঙ্গে আদায় করা হয়। এটি সুন্নাতে মুয়াক্কাদা হিসেবে পরিচিত, অর্থাৎ অত্যন্ত গুরুত্ব সহকারে পালনীয় একটি সুন্নত। এর ফজিলত কুরআন ও হাদিস দ্বারা প্রমাণিত এবং এটি ব্যক্তি ও সমাজের জন্য গভীর তাৎপর্য বহন করে।\n\n" +
                "রমজানের রাতের ইবাদতের প্রশংসা:\n\n" +
                "আল্লাহ তাআলা রমজান মাস সম্পর্কে বলেন,\n\n" +
                "شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدًى لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ\n\n" +
                "অর্থ:\n\n" +
                "রমজান মাস, যার মধ্যে কুরআন নাযিল করা হয়েছে; এটি মানবজাতির জন্য পথনির্দেশ, সুষ্পষ্ট সত্য এবং সঠিক ও ভুলের মাঝে পার্থক্যকারী।\n\n" +
                "[সূরা আল-বাকারাহ: ১৮৫]\n\n" +
                "রমজানের প্রতিটি ইবাদতের মতো তারাবীহও আল্লাহর সান্নিধ্য অর্জনের একটি মাধ্যম। যদিও\n\n" +
                "কুরআনে সরাসরি তারাবীহ উল্লেখ নেই, তবে রাতের ইবাদত সম্পর্কে বলা হয়েছে,\n\n" +
                "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَكَ عَسَى أَنْ يَبْعَثَكَ رَبُّكَ مَقَامًا مَحْمُودًا\n\n" +
                "অর্থ:\n\n" +
                "আর রাতের কিছু অংশে তাহাজ্জুদ পড়ুন, এটি আপনার জন্য নফল। আশা করা যায়, আপনার রব আপনাকে প্রশংসিত মর্যাদায় উন্নীত করবেন।\n\n" +
                "[সূরা আল-ইসরা: ৭৯]\n\n" +
                "যদিও এটি মূলত তাহাজ্জুদের জন্য বলা হয়েছে, রমজানের রাতের তারাবীহ নামাজকেও এর অন্তর্ভুক্ত করা হয়। কারণ, রমজানে রাতের ইবাদতের বিশেষ গুরুত্ব রয়েছে।\n\n" +
                "তারাবীহর আধুনিক প্রাসঙ্গিকতা:\n\n" +
                "আধ্যাত্মিক প্রশান্তি ও মানসিক সুস্থতা:\n\n" +
                "আজকের ব্যস্ত জীবনযাত্রায় তারাবীহ নামাজ ব্যক্তির মধ্যে আত্মিক প্রশান্তি আনে। মনোযোগ সহকারে দীর্ঘ সময় ধরে আল্লাহর সামনে দাঁড়ানো আমাদের মানসিক চাপ কমাতে সাহায্য করে।\n\n" +
                "সামাজিক সংহতি:\n\n" +
                "তারাবীহ নামাজ জামাআতের সঙ্গে আদায় করলে মুসলিম সমাজে ঐক্য বৃদ্ধি পায়। মুসলিম উম্মাহর ভ্রাতৃত্ববোধ শক্তিশালী হয়।\n\n" +
                "আত্ম-উন্নয়ন ও ধৈর্যশীলতা:\n\n" +
                "তারাবীহ দীর্ঘ সময় ধরে পড়তে হয়। এটি ধৈর্য, মনোযোগ ও ইবাদতের প্রতি গভীর মনোযোগ সৃষ্টি করে।\n\n" +
                "আল-কুরআনের প্রতি সংযোগ বৃদ্ধি:\n\n" +
                "তারাবীহতে কুরআন তিলাওয়াত শোনা হয়। এ সময় মুসলিমরা কুরআনের গভীর বার্তা শোনার সুযোগ পায়, যা তাদের জীবনে আল্লাহর কালামের গুরুত্ব স্মরণ করিয়ে দেয়।";

        String modernDetailsEn = "Taraweeh prayer is a special act of Islamic worship performed in congregation after the Isha prayer in the month of Ramadan. It is recognized as Sunnah Mu'akkadah, meaning a highly emphasized Sunnah to observe. Its virtues are substantiated by the Quran and Hadith, holding profound significance for both the individual and society.\n\n" +
                "Praise of Night Worship in Ramadan:\n\n" +
                "Allah Almighty says regarding the month of Ramadan:\n\n" +
                "شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدًى لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ\n\n" +
                "Meaning:\n\n" +
                "The month of Ramadan in which was revealed the Quran, a guidance for the people and clear proofs of guidance and criterion.\n\n" +
                "[Surah Al-Baqarah: 185]\n\n" +
                "Like every act of worship in Ramadan, Taraweeh is also a means of attaining nearness to Allah. Although\n\n" +
                "Taraweeh is not explicitly mentioned by name in the Quran, concerning night worship it has been said:\n\n" +
                "وَمِنَ اللَّيْلِ فَتَهَجَّدْ بِهِ نَافِلَةً لَكَ عَسَى أَنْ يَبْعَثَكَ رَبُّكَ مَقَامًا مَحْمُودًا\n\n" +
                "Meaning:\n\n" +
                "And from part of the night, pray with it as additional worship for you; it is expected that your Lord will raise you to a praised station.\n\n" +
                "[Surah Al-Isra: 79]\n\n" +
                "Although this is primarily addressed regarding Tahajjud, Taraweeh prayer of Ramadan nights is also included within its broader scope. This is because night worship in Ramadan carries distinct importance.\n\n" +
                "Modern Relevance of Taraweeh:\n\n" +
                "Spiritual Serenity and Mental Well-being:\n\n" +
                "In today's fast-paced lifestyle, Taraweeh prayer brings inner peace to an individual. Standing attentively before Allah for an extended duration helps diminish our mental stress.\n\n" +
                "Social Cohesion:\n\n" +
                "Performing Taraweeh prayer in congregation enhances unity within Muslim society. It solidifies the bond of brotherhood among the Muslim Ummah.\n\n" +
                "Self-Development and Patience:\n\n" +
                "Taraweeh is prayed over an extended period. It fosters patience, attentiveness, and deep focus toward worship.\n\n" +
                "Strengthening Connection with the Holy Quran:\n\n" +
                "The recitation of the Quran is listened to during Taraweeh. During this time, Muslims have the opportunity to hear the profound messages of the Quran, reminding them of the significance of Allah's word in their lives.";

        String modernPreviewBn = "তারাবীহ নামাজ ইসলামের একটি বিশেষ ইবাদত যা রমজান মাসে এশার নামাজের পরে জামাআতের সঙ্গে আদায় করা হয়। এটি সুন্নাতে মুয়াক্কাদা হিসেবে পরিচিত, অর্থাৎ অত্যন্ত গুরুত্ব সহকারে পালনীয় একটি সুন্নত। এর ফজিলত কুরআন ও হাদিস দ্বারা প্রমাণিত এবং এটি ব্যক্তি ও সমাজের জন্য গভীর তাৎপর্য বহন করে রমজানের রাতের ইবাদ...";

        String modernPreviewEn = "Taraweeh prayer is a special act of Islamic worship performed in congregation after the Isha prayer in the month of Ramadan. It is recognized as Sunnah Mu'akkadah, meaning a highly emphasized Sunnah to observe. Its virtues are substantiated by the Quran and Hadith, holding profound significance for both individual and society Praise of Night Worship in Ramadan...";

        list.add(new RozaTaraweehItem(
                6,
                "taraweeh_virtues_modern",
                "তারাবীহর ফজিলত ও আধুনিক প্রাসঙ্গিকতা",
                "Virtues of Taraweeh and Modern Relevance",
                "আধ্যাত্মিক ও জাগতিক কল্যাণ",
                "Spiritual & Holistic Value",
                "شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدًى لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ",
                "শাহরু রামাধানাল্লাযী উনযিলা ফীহিল ক্বুরআনু হুদাল লিন্নাসি ওয়া বাইয়্যিনাতিম মিনাল হুদা ওয়াল ফুরক্বান।",
                "Shahru Ramadanalladhee unzila feehil-Qur'anu hudal lin-naasi wa bayyinatim minal-huda wal-furqaan.",
                "রমজান মাস, যার মধ্যে কুরআন নাযিল করা হয়েছে; এটি মানবজাতির জন্য পথনির্দেশ, সুষ্পষ্ট সত্য এবং সঠিক ও ভুলের মাঝে পার্থক্যকারী।",
                "The month of Ramadan in which was revealed the Quran, a guidance for the people and clear proofs of guidance and criterion.",
                modernPreviewBn,
                modernPreviewEn,
                modernDetailsBn,
                modernDetailsEn,
                "[সূরা আল-বাকারাহ: ১৮৫, সূরা আল-ইসরা: ৭৯]",
                "[Surah Al-Baqarah: 185, Surah Al-Isra: 79]"
        ));        // 7. তারাবীহ নিয়ে মাসআলা-মাসায়েল
        list.add(new RozaTaraweehItem(
                7,
                "taraweeh_masail",
                "তারাবীহ নিয়ে মাসআলা-মাসায়েল",
                "Masail and Rulings on Taraweeh",
                "প্রয়োজনীয় ফিকহি সমাধান",
                "Essential Fiqh Solutions",
                "فَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ",
                "ফাত্তাকুল্লাহ্ মা-সতাত্বা'তুম।",
                "Fattaqullaha mastata'tum.",
                "অতএব তোমরা যথাসাধ্য আল্লাহকে ভয় করো ও তাঁর বিধান মেনে চলো।",
                "So fear Allah as much as you are able.",
                "অসুস্থ ও রুগীর জন্য তারাবি প্রসঙ্গে\n" +
                        "তারাবির নামাজ প্রাপ্ত বয়স্ক পুরষ-মহিলা সবার ওপর সুন্নতে মোয়াক্কাদা। অসুস্থ ও রুগীর ওপর তারাবি জরুরি নয়, তবে কোনো কষ্ট না হলে তাদেরও পড়া মুস্তাহাব।\n\n" +
                        "[রদ্দুল মুহতার: ১/৭৪২]\n\n" +
                        "তারাবির নামাজ জামাতে আদায় করা মুস্তাহাব, একাকি আদায় করলেও আদায় হবে।\n\n" +
                        "[বাদায়েউস সানায়ে: ১/২৯০]\n\n" +
                        "তারাবির জামাত থেকে কিছু রাকাত ছুটে গেলে\n" +
                        "কারও যদি তারাবির জামাত থেকে কিছু রাকাত ছুটে যায় তাহলে বেতরের নামাজের পর তা আদায় করে নেবে।\n\n" +
                        "[রদ্দুল মুহতার: ২/৪৪]\n\n" +
                        "নাবালেগ হাফেজের পেছনে নামাজ প্রসঙ্গে\n" +
                        "নাবালেগ হাফেজের পেছনে বালেগ পুরুষ-মহিলা কারও জন্যই ইক্তিদা করা বৈধ নয়।\n\n" +
                        "[আল বাহরুর রায়েক: ১/৩৫৯]\n\n" +
                        "নামাজে সূরার শুরুতে বিসমিল্লাহ পড়া\n" +
                        "ফরজ, নফল বা তারাবি যে নামাজেই প্রত্যেক সূরার শুরুতে বিসমিল্লাহ নিঃসন্দেহে পড়া সুন্নত। তবে বিসমিল্লাহ নিঃশব্দে পড়া সুন্নত। তাই তারাবির নামাজেও খতমে কোরআনের সময় প্রত্যেক সূরার শুরুতে নিঃশব্দে পড়া সুন্নত। তবে যেহেতু বিসমিল্লাহির রাহমানির রাহিমও কোরআনের একটি আয়াত; তাই মুসল্লিদের খতম পূর্ণ হওয়ার জন্য যেকোনো সূরার শুরুতে বিসমিল্লাহ স্বশব্দে পড়ে নিলে সবার খতম পূর্ণ হয়ে যাবে। প্রতি সূরার শুরুতে বিসমিল্লাহ স্বশব্দে পড়লেও কোনো সমস্যা নেই। উভয়ের ওপর আমল করার অবকাশ আছে।\n\n" +
                        "[রদ্দুল মুহতার: ১/৪৯০]\n\n" +
                        "ইসলামি শরিয়তের দৃষ্টিতে নামাজের ভেতর লোকমা\n" +
                        "ইসলামি শরিয়তের দৃষ্টিতে নামাজের ভেতর লোকমা (নামাজের কেরাতে কোথাও ইমামের সন্দেহ হলে এবং সামনে অগ্রসর হতে না পারলে মুক্তাদির তাকে সহযোগিতা করা উত্তম। সহযোগিতার পদ্ধতি হলো, মুক্তাদি উচ্চস্বরে শুদ্ধভাবে পাঠ করবেন। এটাকে পরিভাষায় ‘লোকমা দেয়া’ বলে। অনেক সময় কেরাত ছাড়াও উঠা-বসার ক্ষেত্রে কোথাও ইমামের ভুল হলে তাকে সতর্ক করাকেও লোকমা দেয়া বলে। ইসলামে লোকমা দেয়া ও নেয়ার বিধান রয়েছে। যেগুলো জানা ও মেনে চলা অপরিহার্য) দেয়ার ব্যাপারে তাড়াহুড়ো না করা উচিত এবং ইমাম সাহেবের জন্য লোকমার অপেক্ষা না করে অন্য আয়াত পড়ে নামাজ শেষ করা উচিত। লোকমা দেয়ার সঠিক পদ্ধতি হলো- প্রথমে ইমাম সাহেবকে আয়াত পুনরাবৃত্তির সুযোগ দেয়া। এতদসত্ত্বেও ইমাম সাহেব শুধরে নিতে না পারলে সেক্ষেত্রে মুক্তাদি লোকমা দিলে কোনো ক্ষতি হবে না। তারাবি নামাজে খতমে কোরআনে যদি হাফেজ সাহেব লোকমার অপেক্ষা না করে, তাহলে লোকমা না দিলে কোনো অসুবিধা হবে না। তবে ভুলে যাওয়া আয়াত পরবর্তীতে সূরা ফাতেহার পর পড়ে নিতে হবে।\n\n" +
                        "[রদ্দুল মুহতার: ১/৬২৩]\n\n" +
                        "তারাবির নামাজে ভুল করলে\n" +
                        "তারাবির নামাজে দ্বিতীয় রাকাতে না বসে দাঁড়িয়ে গেলে তৃতীয় রাকাতে সিজদা করার পূর্বে স্মরণ হলে বসে তাশাহহুদ ও সেজদায়ে সাহু আদায় করলে তেলাওয়াত ও নামাজ শুদ্ধ হয়ে যাবে। যদি তৃতীয় রাকাতে সিজদা করে ফেলে, তবে চতুর্থ রাকাত মিলিয়ে নেবে। এতে শেষের দুই রাকাত তারাবির নামাজ হিসেবে ধর্তব্য হবে এবং শেষ বৈঠক না করার কারণে প্রথম দুই রাকাত তারাবি হিসেবে গণ্য না হওয়ায় তেলাওয়াতসহ পুনরায় পড়তে হবে।\n\n" +
                        "[বাদায়েউস সানায়ে: ১/২৮৯]\n\n" +
                        "যদি কোনো ব্যক্তি তারাবির নামাজ চার রাকাতের নিয়ত করে শুরু করে এবং ভুলে দুই রাকাতের পর বৈঠক না করে চার রাকাত শেষ করেই বৈঠক করে, তাহলে সে যদি নামাজ শেষে সেজদায়ে সাহু করে থাকে, তবে শুধু শেষের দুই রাকাত তারাবি হিসেবে গণ্য হবে।\n\n" +
                        "[আল বাহরুর রায়েক: ২/১১৭]\n\n" +
                        "বসে তারাবির নামাজ\n" +
                        "মাটিতে বসে রুকু-সেজদার মাধ্যমে তারাবি নামাজ বৈধ। অনুরূপ তারাবির কেরাতের সময় চেয়ারে বসে রুকু-সেজদা নামাজের নিয়মমাফিক আদায় করলে তাও বৈধ। কিন্তু বিনা ওজরে এরূপ করলে নামাজের পূর্ণ সওয়াব পাবে না, বরং অর্ধেক সওয়াব পাবে। তবে হ্যাঁ, নিয়মমাফিক রুকু সেজদায় সক্ষম ব্যক্তি চেয়ারে বসে ইশারায় রুকু সেজদার মাধ্যমে নামাজ পড়লে নামাজ শুদ্ধ হবে না।\n\n" +
                        "[ফাতাওয়ায়ে হিন্দিয়া: ১/১১৮]",
                "Regarding Taraweeh for the Sick and Ill\n" +
                        "Taraweeh prayer is Sunnah Mu'akkadah upon all adult men and women. For the sick and infirm, Taraweeh is not obligatory; however, if there is no undue hardship, it is recommended (Mustahabb) for them to perform it.\n\n" +
                        "[Radd al-Muhtar: 1/742]\n\n" +
                        "Offering Taraweeh prayer in congregation is Mustahabb; praying individually also fulfills the prayer.\n\n" +
                        "[Bada'i al-Sana'i: 1/290]\n\n" +
                        "Missing Rak'ahs from the Taraweeh Congregation\n" +
                        "If someone misses a few rak'ahs of the Taraweeh congregation, they should make them up after offering the Witr prayer.\n\n" +
                        "[Radd al-Muhtar: 2/44]\n\n" +
                        "Praying Behind an Underage (Non-Adult) Hafiz\n" +
                        "It is not permissible for any adult man or woman to follow an underage (non-adult) Hafiz in prayer.\n\n" +
                        "[Al-Bahr ar-Ra'iq: 1/359]\n\n" +
                        "Reciting Bismillah at the Beginning of a Surah in Prayer\n" +
                        "Reciting Bismillah at the beginning of each Surah in Fard, Nafl, or Taraweeh prayer is undoubtedly Sunnah. However, reciting Bismillah silently is Sunnah. Therefore, reciting silently at the start of each Surah during Khatm al-Quran in Taraweeh is Sunnah. Nonetheless, since Bismillahir Rahmanir Rahim is also a verse of the Quran, reciting it aloud at the beginning of any Surah fulfills the complete Khatm for all worshippers. Reciting Bismillah audibly at the beginning of every Surah is also permissible without issue. There is scope to act upon both views.\n\n" +
                        "[Radd al-Muhtar: 1/490]\n\n" +
                        "Giving Luqmah (Prompting) in Prayer According to Islamic Shariah\n" +
                        "In Islamic Shariah, one should not rush into giving Luqmah (prompting) during prayer (when the Imam falters or doubts his recitation and cannot proceed, it is meritorious for a follower to assist him by reciting correctly aloud; this is termed 'giving Luqmah'. Alerting the Imam when he errs in physical postures is also called Luqmah. Shariah prescribes essential guidelines for giving and receiving Luqmah), and the Imam should not wait for a Luqmah but rather transition to another verse to conclude the prayer. The correct method is first to allow the Imam opportunity to repeat the verse. If he is still unable to correct himself, the follower may prompt him without detriment. In Taraweeh Khatm al-Quran, if the Hafiz does not wait for a Luqmah, omitting it causes no harm; however, the missed verse should subsequently be recited after Surah Al-Fatihah.\n\n" +
                        "[Radd al-Muhtar: 1/623]\n\n" +
                        "Mistakes Made in Taraweeh Prayer\n" +
                        "If in Taraweeh prayer one mistakenly stands up instead of sitting in the second rak'ah, and remembers before performing the Sajdah of the third rak'ah, sitting down immediately and performing Tashahhud followed by Sajdah as-Sahw renders both the recitation and prayer valid. If one has already prostrated in the third rak'ah, one should add a fourth rak'ah. In that case, the final two rak'ahs count as Taraweeh, and because the sitting after the second rak'ah was missed, the first two rak'ahs do not count as Taraweeh and must be repeated along with the recitation.\n\n" +
                        "[Bada'i al-Sana'i: 1/289]\n\n" +
                        "If someone begins Taraweeh intending four rak'ahs and mistakenly sits only after finishing four rak'ahs without sitting after two rak'ahs, then if they perform Sajdah as-Sahw at the end, only the final two rak'ahs are credited as Taraweeh.\n\n" +
                        "[Al-Bahr ar-Ra'iq: 2/117]\n\n" +
                        "Performing Taraweeh Sitting Down\n" +
                        "Offering Taraweeh prayer sitting on the floor with full bowing and prostration is permissible. Similarly, sitting on a chair during recitation but bowing and prostrating on the floor according to the prescribed manner is also valid. However, doing so without a legitimate excuse yields only half the reward. Furthermore, a person capable of regular bowing and prostration who sits on a chair and performs Ruku and Sajdah merely through gestures will not have their prayer validated.\n\n" +
                        "[Fatawa al-Hindiyyah: 1/118]",
                "[রদ্দুল মুহতার, বাদায়েউস সানায়ে, আল বাহরুর রায়েক, ফাতাওয়ায়ে হিন্দিয়া]",
                "[Radd al-Muhtar, Bada'i al-Sana'i, Al-Bahr ar-Ra'iq, Fatawa al-Hindiyyah]"
        ));        // 8. বাচ্চাদের জন্য তারাবীহ শেখানো
        String kidsDetailsBn = "বাচ্চাদের জন্য তারাবীহ শেখানো একটি অত্যন্ত গুরুত্বপূর্ণ এবং সাওয়াবের কাজ। এটি তাদের ইসলামের প্রতি আগ্রহ জাগিয়ে তোলে এবং ছোটবেলা থেকেই আল্লাহর ইবাদতের প্রতি ভালোবাসা তৈরি করে। তবে এটি এমনভাবে করতে হবে যাতে তারা আগ্রহ হারিয়ে না ফেলে বা এই ইবাদতকে চাপ হিসেবে অনুভব না করে। নিচে বাচ্চাদের জন্য তারাবীহ শেখানোর কিছু উপায় ও প্রাসঙ্গিক দিক উল্লেখ করা হলো,\n\n" +
                "তারাবীহ নামাজের অর্থ ও গুরুত্ব ব্যাখ্যা করুন:\n\n" +
                "তাদের সহজ ভাষায় বোঝান যে তারাবীহ একটি বিশেষ নামাজ যা আমরা আল্লাহর কাছে কৃতজ্ঞতা প্রকাশ করার জন্য পড়ি এবং এতে অনেক সওয়াব রয়েছে। রাসুলুল্লাহ ﷺ বলেছেন,\n\n" +
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n" +
                "অর্থ:\n\n" +
                "যে ব্যক্তি ঈমান সহকারে ও সওয়াবের আশা নিয়ে রমজানের রাতে কিয়াম (তারাবীহ) আদায় করবে, তার অতীতের গুনাহ মাফ করে দেওয়া হবে।\n\n" +
                "[সহিহ বুখারি: ২০০৯, সহিহ মুসলিম: ৭৬০]\n\n" +
                "বাচ্চাদের তাদের বয়স অনুযায়ী এই ফজিলত সম্পর্কে জানানো তাদের উৎসাহিত করবে।\n\n" +
                "ধীরে ধীরে অভ্যাস গড়ে তোলা, ছোট সময় দিয়ে শুরু করুন:\n\n" +
                "প্রথমে ২ রাকাত বা ৪ রাকাত নামাজ পড়াতে উৎসাহিত করুন। ধীরে ধীরে রাকাত বাড়ানোর মাধ্যমে তাদের অভ্যাস গড়ে তুলুন।\n\n" +
                "তাদের ওপর চাপ না দিন:\n\n" +
                "বাচ্চারা যদি ক্লান্ত হয় বা মনোযোগ হারায়, তাদের বিশ্রাম নিতে দিন। এতে তারা তারাবীহকে ভালোভাবে গ্রহণ করবে এবং এটি কঠিন মনে হবে না।\n\n" +
                "নামাজে অংশগ্রহণকে আনন্দদায়ক করুন, পুরস্কার ব্যবস্থা চালু করুন:\n\n" +
                "যদি তারা তারাবীহতে অংশগ্রহণ করে, তাদেরকে ছোট পুরস্কার দিন (যেমন একটি গল্পের বই, প্রিয় খাবার, বা অন্য কিছু যা তারা পছন্দ করে)। এতে তাদের উৎসাহ বাড়বে।\n\n" +
                "তাদের ভূমিকা গুরুত্ব দিন:\n\n" +
                "তারাবীহতে ছোট দোয়া শিখতে দিন, কুরআনের ছোট সূরাগুলো পড়াতে দিন বা আজান দেওয়ার কাজে যুক্ত করুন। এতে তারা ইবাদতে সম্পৃক্ততা অনুভব করবে।\n\n" +
                "বাড়িতে তারাবীহ পড়ার ব্যবস্থা করুন:\n\n" +
                "যদি বাচ্চারা মসজিদে যেতে অসুবিধা বোধ করে, তবে পরিবারের সবাই মিলে বাড়িতে জামাআতের মাধ্যমে তারাবীহ পড়ার ব্যবস্থা করুন। এটি তাদের জন্য আরামদায়ক হবে এবং পরিবারের মধ্যে একটি সুন্দর পরিবেশ তৈরি করবে।\n\n" +
                "ছোট সূরা দিয়ে তারাবীহ পড়ান:\n\n" +
                "ছোট সূরা দিয়ে তারাবীহ পড়ানো হলে বাচ্চারা সহজেই মনোযোগ ধরে রাখতে পারবে। দীর্ঘ সময় ধরে দাঁড়ানো তাদের জন্য কষ্টকর হতে পারে।\n\n" +
                "কুরআন তিলাওয়াতের প্রতি আকৃষ্ট করুন:\n\n" +
                "তারাবীহতে তারা যে সূরা শুনছে তা সহজ করে তাদের শেখানোর চেষ্টা করুন। এটি তাদের কুরআনের প্রতি ভালোবাসা বাড়াবে।\n\n" +
                "শিক্ষণীয় গল্প বলুন:\n\n" +
                "তারাবীহ নামাজের পরে বা আগে নবীদের জীবন, সাহাবাদের ত্যাগ ও রমজানের ফজিলত নিয়ে গল্প বলুন।\n\n" +
                "ধৈর্য এবং নম্রতা বজায় রাখুন:\n\n" +
                "বাচ্চারা কখনও কখনও ক্লান্ত হতে পারে বা অমনোযোগী হতে পারে। এর জন্য তাদের শাসন করার পরিবর্তে ধৈর্য ধরুন এবং তাদের ভালোবাসা দিয়ে শেখানোর চেষ্টা করুন। তাদের সামনে ইবাদতের গুরুত্ব তুলে ধরুন। আপনি নিজে তারাবীহ পড়লে তারা আপনাকে অনুসরণ করতে আগ্রহী হবে।";

        String kidsDetailsEn = "Teaching children Taraweeh is an immensely vital and rewarding endeavor. It awakens their interest in Islam and cultivates love for worshipping Allah from an early age. However, this must be carried out in a way that prevents them from losing enthusiasm or feeling that this worship is a burden. Below are some practical methods and relevant aspects of teaching Taraweeh to children:\n\n" +
                "Explain the Meaning and Significance of Taraweeh Prayer:\n\n" +
                "Explain to them in simple language that Taraweeh is a special prayer performed to express gratitude to Allah, carrying immense rewards. The Messenger of Allah ﷺ said:\n\n" +
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ\n\n" +
                "Meaning:\n\n" +
                "Whoever stands in prayer during the nights of Ramadan out of faith and seeking reward, their past sins will be forgiven.\n\n" +
                "[Sahih al-Bukhari: 2009, Sahih Muslim: 760]\n\n" +
                "Informing children about this virtue in an age-appropriate manner will inspire and encourage them.\n\n" +
                "Build the Habit Gradually, Start with Short Durations:\n\n" +
                "Encourage them initially to pray 2 or 4 rak'ahs. Gradually build their habit by progressively increasing the rak'ahs over time.\n\n" +
                "Do Not Pressure Them:\n\n" +
                "If children get tired or lose concentration, allow them to rest. This ensures they receive Taraweeh positively and do not view it as difficult or exhausting.\n\n" +
                "Make Prayer Participation Enjoyable, Introduce a Reward System:\n\n" +
                "When they participate in Taraweeh, reward them with small treats (such as a storybook, a favorite snack, or something else they cherish). This boosts their enthusiasm and morale.\n\n" +
                "Value Their Role:\n\n" +
                "Teach them short supplications (duas) during Taraweeh, let them recite short surahs of the Quran, or involve them in calling the Adhan. This helps them feel personally involved in worship.\n\n" +
                "Arrange Taraweeh at Home:\n\n" +
                "If children find it difficult to go to the mosque, arrange Taraweeh in congregation at home with the whole family. This provides a comfortable environment and fosters a beautiful Islamic atmosphere within the household.\n\n" +
                "Recite Short Surahs in Taraweeh:\n\n" +
                "When Taraweeh is prayed with shorter surahs, children can maintain their focus more easily. Standing for prolonged durations can be physically challenging for them.\n\n" +
                "Attract Them to Quran Recitation:\n\n" +
                "Try to teach them the meaning and beauty of the surahs they hear during Taraweeh in an accessible way. This deepens their love for the Noble Quran.\n\n" +
                "Share Educational and Inspiring Stories:\n\n" +
                "Before or after the Taraweeh prayer, narrate inspiring stories about the lives of the Prophets, the sacrifices of the Companions, and the virtues of Ramadan.\n\n" +
                "Maintain Patience and Gentleness:\n\n" +
                "Children may at times become weary or inattentive. Instead of reprimanding them, practice patience and guide them with love and warmth. Highlight the nobility of worship before them. When you pray Taraweeh regularly yourself, they will eagerly emulate your example.";

        String kidsPreviewBn = "বাচ্চাদের জন্য তারাবীহ শেখানো একটি অত্যন্ত গুরুত্বপূর্ণ এবং সাওয়াবের কাজ। এটি তাদের ইসলামের প্রতি আগ্রহ জাগিয়ে তোলে এবং ছোটবেলা থেকেই আল্লাহর ইবাদতের প্রতি ভালোবাসা তৈরি করে। তবে এটি এমনভাবে করতে হবে যাতে তারা আগ্রহ হারিয়ে না ফেলে বা এই ইবাদতকে চাপ হিসেবে অনুভব না করে। নিচে বাচ্চাদের জন্য তারাবীহ শেখা...";

        String kidsPreviewEn = "Teaching children Taraweeh is an immensely vital and rewarding endeavor. It awakens their interest in Islam and cultivates love for worshipping Allah from an early age. However, this must be carried out in a way that prevents them from losing enthusiasm or feeling that this worship is a burden. Below are some practical methods and relevant aspects of teaching Taraweeh to children...";

        list.add(new RozaTaraweehItem(
                8,
                "taraweeh_teaching_children",
                "বাচ্চাদের জন্য তারাবীহ শেখানো",
                "Teaching Children Taraweeh",
                "শিশু প্রশিক্ষণ ও শিষ্টাচার",
                "Parenting & Child Guidance",
                "مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
                "মান ক্বা-মা রামাদ্বা-না ঈমা-নান ওয়া ইতিসা-বান গুফিরা লাহু মা- তাক্বাদ্দামা মিন যামবিহ্।",
                "Man qama Ramadana imanan wahtisaban ghufira lahu ma taqaddama min dhanbih.",
                "যে ব্যক্তি ঈমান সহকারে ও সওয়াবের আশা নিয়ে রমজানের রাতে কিয়াম (তারাবীহ) আদায় করবে, তার অতীতের গুনাহ মাফ করে দেওয়া হবে।",
                "Whoever stands in prayer during the nights of Ramadan out of faith and seeking reward, his past sins will be forgiven.",
                kidsPreviewBn,
                kidsPreviewEn,
                kidsDetailsBn,
                kidsDetailsEn,
                "[সহিহ বুখারি: ২০০৯, সহিহ মুসলিম: ৭৬০]",
                "[Sahih al-Bukhari: 2009, Sahih Muslim: 760]"
        ));        // 9. নারীদের জন্য তারাবীহ
        String womenDetailsBn = "নারীদের তারাবীর নামায প্রসঙ্গে\n\n" +
                "তারাবীর নামায সুন্নতে মুয়াক্কাদা। নারীদের জন্যে কিয়ামুল লাইল (রাতের নামায) ঘরে পড়া উত্তম। যেহেতু নবী সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম বলেন: “নারীদেরকে মসজিদে যেতে বাধা দিও না। তবে, তাদের জন্য ঘরই উত্তম।\n\n" +
                "[সহিহুল ৭৪৫৮]\n\n" +
                "নারীদের তারাবি কোথায় পড়া উত্তম\n\n" +
                "নারীর নামাযের স্থান যতবেশী নির্জনে হবে, যতবেশি ব্যক্তিগত হবে সেটাই উত্তম। যেহেতু নবী সাল্লল্লাহু আলাইহি ওয়া সাল্লাম বলেছেন, “মহিলাদের জন্য শোয়ার ঘরে নামায আদায় করা বৈঠকখানায় নামায আদায় করার চেয়ে উত্তম। তাদের জন্য গোপন প্রকোষ্ঠে নামায করা শোয়ার ঘরে নামায আদায় করার চেয়ে উত্তম।\n\n" +
                "[সহিহুল ৩৮৩৩]\n\n" +
                "আবু হুমাইদ আল-সায়েদি এর স্ত্রী উম্মে হুমাইদ থেকে বর্ণিত তিনি একবার নবী সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের কাছে এসে বললেন: ইয়া রাসূলুল্লাহ্! আমি আপনার সাথে নামায আদায় করতে পছন্দ করি। তখন তিনি বললেন: আমি জেনেছি আপনি আমার সাথে নামায পড়া পছন্দ করেন। কিন্তু, আপনি আপনার শোয়ার ঘরে নামায আদায় করা বৈঠক ঘরে নামায আদায় করার চেয়ে উত্তম। আপনি আপনার বৈঠক ঘরে নামায আদায় করা বাড়ীর উঠোনে নামায আদায় করার চেয়ে উত্তম। আপনি আপনার বাড়ীর উঠোনে নামায আদায় করা গোত্রীয় মসজিদে নামায আদায় করার চেয়ে উত্তম। আপনি আপনার গোত্রীয় মসজিদে নামায আদায় করা আমার মসজিদে নামায আদায় করার চেয়ে উত্তম। বর্ণনাকারী বলেন: ফলে তিনি তার ঘরের একেবারে ভিতরে অন্ধকার স্থানে তার জন্য নামাযের জায়গা বানানোর নির্দেশ দিলেন। তিনি মৃত্যু পর্যন্ত সে জায়গায় নামায আদায় করেছেন।\n\n" +
                "[মুসনাদে আহমাদ, হাদিসটির বর্ণনাকারীগণ নির্ভরযোগ্য]\n\n" +
                "তবে উল্লেখিত ফযিলত নারীদেরকে মসজিদে যাওয়ার অনুমতি দেয়ার ক্ষেত্রে প্রতিবন্ধক নয়। যেমনটি আব্দুল্লাহ্ বিন উমর (রাঃ) কর্তৃক হাদিসে এসেছে, তিনি বলেন: আমি রাসূলুল্লাহ্ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামকে বলতে শুনেছি তিনি বলেন: যদি নারীরা তোমাদের কাছে মসজিদে যেতে অনুমতি চায় তাহলে তোমরা তাদেরকে মসজিদে যেতে বাধা দিও না। বর্ণনাকারী বলেন, তখন বিলাল বিন আব্দুল্লাহ্ (বিন উমর) বলল: আল্লাহ্র শপথ, অবশ্যই আমরা তাদেরকে বাধা দিব। বর্ণনাকারী বলেন: তখন আব্দুল্লাহ্ তার দিকে এগিয়ে এসে তাকে তীব্র গালমন্দ করলেন; আমি তাঁর কাছ থেকে এমন কথা আর কখনও শুনিনি। এবং তিনি বললেন: আমি তোমাকে রাসূলুল্লাহ্ সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম থেকে হাদিস জানাচ্ছি। আর তুমি বল: আল্লাহ্র শপথ, অবশ্যই আমরা তাদেরকে বাধা দিব।\n\n" +
                "[সহিহ মুসলিম: ৬৬৭]\n\n" +
                "কিন্তু, কোন নারী মসজিদে যাওয়ার ক্ষেত্রে নিম্নোক্ত শর্ত রয়েছে:\n\n" +
                "পরিপূর্ণ হিজাব থাকতে হবে।\n" +
                "সুগন্ধি লাগিয়ে যাবে না।\n" +
                "স্বামীর অনুমতি লাগবে।\n\n" +
                "এবং এ বের হওয়ার ক্ষেত্রে অন্য আরেকটি হারাম যেন সংঘটিত না হয়; যেমন একাকী ড্রাইভারের সাথে বের হওয়া। যদি কোন নারী উল্লেখিত শর্তগুলোর কোনটি ভঙ্গ করে সেক্ষেত্রে নারীর স্বামী কিংবা অভিভাবক তাকে মসজিদে যেতে বাধা দিতে পারবেন; বরং বাধা দেওয়া আবশ্যক হবে।";

        String womenDetailsEn = "Rulings on Women's Taraweeh Prayer:\n\n" +
                "Taraweeh prayer is Sunnah Mu'akkadah. For women, performing Qiyam al-Layl (the night prayer) at home is more virtuous. As the Prophet ﷺ stated: 'Do not prevent women from going to mosques; however, their homes are better for them.'\n\n" +
                "[Sahihul 7458]\n\n" +
                "Where It Is Most Virtuous for Women to Pray Taraweeh:\n\n" +
                "The more secluded and private a woman's place of prayer is, the more virtuous it is. The Prophet ﷺ said: 'A woman's prayer in her inner bedroom is better than her prayer in her living room, and her prayer in her innermost chamber is better than her prayer in her bedroom.'\n\n" +
                "[Sahihul 3833]\n\n" +
                "Narrated from Umm Humaid, the wife of Abu Humaid as-Sa'idi, that she came to the Prophet ﷺ and said: 'O Messenger of Allah! I love to pray with you.' He ﷺ replied: 'I know that you love to pray with me, but your prayer in your inner room is better for you than your prayer in your living chamber; your prayer in your living chamber is better than your prayer in your household courtyard; your prayer in your household courtyard is better than your prayer in the mosque of your tribe; and your prayer in the mosque of your tribe is better than your prayer in my mosque.' The narrator said: She then ordered a place of prayer to be built for her in the deepest, darkest corner of her home, and she prayed there until her death.\n\n" +
                "[Musnad Ahmad, authentic chain of narrators]\n\n" +
                "However, the mentioned virtues do not preclude granting women permission to go to the mosque. As narrated by Abdullah ibn Umar (RA): 'I heard the Messenger of Allah ﷺ say: \"If your women ask permission to go to the mosque, do not prevent them.\"' The narrator stated that Bilal ibn Abdullah (ibn Umar) said: 'By Allah, we will certainly prevent them.' The narrator said: Abdullah turned to him and rebuked him severely in words I had never heard him speak before, saying: 'I convey to you a Hadith from the Messenger of Allah ﷺ and you say: \"By Allah, we will certainly prevent them\"!\'\n\n" +
                "[Sahih Muslim: 667]\n\n" +
                "However, for a woman going to the mosque, the following conditions must strictly be observed:\n\n" +
                "Full and proper Hijab must be maintained.\n" +
                "No perfume or fragrance may be worn.\n" +
                "Permission of the husband must be obtained.\n\n" +
                "And her going out must not involve any prohibited act, such as being in seclusion alone with a driver. If any woman breaches these conditions, her husband or guardian has the right to prevent her from going to the mosque; rather, preventing her becomes an obligation.";

        String womenPreviewBn = "তারাবীর নামায সুন্নতে মুয়াক্কাদা। নারীদের জন্যে কিয়ামুল লাইল (রাতের নামায) ঘরে পড়া উত্তম। যেহেতু নবী সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম বলেন: “নারীদেরকে মসজিদে যেতে বাধা দিও না। তবে, তাদের জন্য ঘরই উত্তম [সহিহুল ৭৪৫৮]";

        String womenPreviewEn = "Taraweeh prayer is Sunnah Mu'akkadah. For women, performing Qiyam al-Layl (the night prayer) at home is more virtuous. As the Prophet ﷺ stated: 'Do not prevent women from going to mosques; however, their homes are better for them.' [Sahihul 7458]";

        list.add(new RozaTaraweehItem(
                9,
                "taraweeh_for_women",
                "নারীদের জন্য তারাবীহ",
                "Taraweeh for Women",
                "মা-বোনদের শারয়ী গাইড",
                "Guide for Women",
                "صَلَاةُ الْمَرْأَةِ فِي بَيْتِهَا أَفْضَلُ مِنْ صَلَاتِهَا فِي حُجْرَتِهَا",
                "সালা-তুল মারআতি ফী বায়তিহা- আফদ্বালু মিন সালা-তিহা- ফী হুজরাতিহা-।",
                "Salatul-mar'ati fee baytiha afdalu min salatiha fee hujratiha.",
                "নারীদের ঘরের ভেতরের অংশে নামাজ আদায় করা বাইরের কামরায় আদায়ের চেয়েও অধিক উত্তম।",
                "A woman's prayer in the inner part of her house is more virtuous than her prayer in the outer room.",
                womenPreviewBn,
                womenPreviewEn,
                womenDetailsBn,
                womenDetailsEn,
                "[সহিহুল ৭৪৫৮, সহিহুল ৩৮৩৩, মুসনাদে আহমাদ, সহিহ মুসলিম: ৬৬৭]",
                "[Sahihul 7458, Sahihul 3833, Musnad Ahmad, Sahih Muslim: 667]"
        ));

        return list;
    }
}
