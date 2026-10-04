package com.devflux.deenone.features.ramadan.data;

import android.content.Context;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.ramadan.model.RozaHadithItem;
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
 * 3-Tier production-ready repository for Roza Hadiths (রোজার হাদিস).
 * 1. Instant in-memory / local retrieval (0ms, 60 FPS, lag-free).
 * 2. Background sync from PHP REST API (get_roza_hadiths.php) via BackendConfigManager.
 * 3. Offline fallback pre-seeded with 100% verbatim hadiths from the screenshot.
 */
public final class RozaHadithRepository {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final List<RozaHadithItem> cachedList = new CopyOnWriteArrayList<>();

    private RozaHadithRepository() {}

    public interface DataCallback {
        void onDataLoaded(List<RozaHadithItem> items);
    }

    /**
     * Get hadiths list with instant return and background remote sync.
     */
    public static List<RozaHadithItem> getHadiths(Context context, DataCallback callback) {
        if (cachedList.isEmpty()) {
            cachedList.addAll(getDefaultHadiths(context));
        }

        // Refresh favorite statuses on cached items
        for (RozaHadithItem item : cachedList) {
            item.setFavorite(RozaHadithFavoritesManager.isFavorite(context, item.getHadithNumber()));
        }

        // Background sync with PHP REST API
        if (context != null && NetworkConnectivityHelper.isOnline(context)) {
            final Context appContext = context.getApplicationContext();
            executor.execute(() -> fetchRemoteHadiths(appContext, callback));
        }

        return new ArrayList<>(cachedList);
    }

    private static void fetchRemoteHadiths(Context context, DataCallback callback) {
        HttpURLConnection conn = null;
        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_hadiths.php");
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
                    if (root.has("success") && root.get("success").getAsBoolean() && root.has("hadiths")) {
                        JsonArray arr = root.getAsJsonArray("hadiths");
                        List<RozaHadithItem> remoteItems = new ArrayList<>();

                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int id = obj.has("id") ? obj.get("id").getAsInt() : 0;
                            int hadithNo = obj.has("hadith_number") ? obj.get("hadith_number").getAsInt() : 0;
                            String noBn = obj.has("hadith_number_bn") ? obj.get("hadith_number_bn").getAsString() : String.valueOf(hadithNo);
                            String noEn = obj.has("hadith_number_en") ? obj.get("hadith_number_en").getAsString() : String.valueOf(hadithNo);
                            String arabic = obj.has("arabic_text") ? obj.get("arabic_text").getAsString() : "";
                            String bangla = obj.has("bangla_text") ? obj.get("bangla_text").getAsString() : "";
                            String english = obj.has("english_text") ? obj.get("english_text").getAsString() : "";
                            boolean isFav = RozaHadithFavoritesManager.isFavorite(context, hadithNo);

                            remoteItems.add(new RozaHadithItem(
                                    id, hadithNo, noBn, noEn, arabic, bangla, english, isFav
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
            // Silently fall back to cached / pre-seeded items
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    /**
     * Pre-seeded default Sahih Bukhari Hadiths on Roza (Fasting).
     * 100% Verbatim matching the screenshot line-by-line.
     */
    public static List<RozaHadithItem> getDefaultHadiths(Context context) {
        List<RozaHadithItem> list = new ArrayList<>();

        // 1. হাদিস- ১৮৯১ (Screenshot Card 1)
        list.add(new RozaHadithItem(
                1,
                1891,
                "১৮৯১",
                "1891",
                "حَدَّثَنَا قُتَيْبَةُ بْنُ سَعِيدٍ حَدَّثَنَا إِسْمَاعِيلُ بْنُ جَعْفَرٍ عَنْ أَبِي سُهَيْلٍ عَنْ أَبِيهِ عَنْ طَلْحَةَ بْنِ عُبَيْدِ اللَّهِ أَنَّ أَعْرَابِيًّا جَاءَ إِلَى رَسُولِ اللَّهِ صلى الله عليه وسلم ثَائِرَ الرَّأْسِ فَقَالَ يَا رَسُولَ اللَّهِ أَخْبِرْنِي مَاذَا فَرَضَ اللَّهُ عَلَيَّ مِنَ الصَّلَاةِ فَقَالَ الصَّلَوَاتِ الْخَمْسَ إِلَّا أَنْ تَطَّوَّعَ شَيْئًا فَقَالَ أَخْبِرْنِي مَا فَرَضَ اللَّهُ عَلَيَّ مِنَ الصِّيَامِ فَقَالَ شَهْرَ رَمَضَانَ إِلَّا أَنْ تَطَّوَّعَ شَيْئًا فَقَالَ أَخْبِرْنِي بِمَا فَرَضَ اللَّهُ عَلَيَّ مِنَ الزَّكَاةِ فَقَالَ فَأَخْبَرَهُ رَسُولُ اللَّهِ صلى الله عليه وسلم شَرَائِعَ الإِسْلامِ قَالَ وَالَّذِي أَكْرَمَكَ لَا أَتَطَوَّعُ شَيْئًا وَلَا أَنْقُصُ مِمَّا فَرَضَ اللَّهُ عَلَيَّ شَيْئًا فَقَالَ رَسُولُ اللَّهِ صلى الله عليه وسلم أَفْلَحَ إِنْ صَدَقَ أَوْ دَخَلَ الْجَنَّةَ إِنْ صَدَقَ",
                "তালহা ইবনু 'উবায়দুল্লাহ (রাঃ) বর্ণিত এলোমেলো চুলসহ একজন গ্রাম্য আরব রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম)-এর নিকট এলেন। অতঃপর বললেন, হে আল্লাহর রসূল! আমাকে বলুন, আল্লাহ্ তা'আলা আমার উপর কত সালাত ফরজ করেছেন? তিনি বললেনঃ পাঁচ (ওয়াক্ত) সালাত; তবে তুমি যদি কিছু নফল আদায় কর তা স্বতন্ত্র কথা। এরপর তিনি বললেন, বলুন, আল্লাহ্ আমার উপর কত সিয়াম আদায় ফরজ করেছেন? আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বললেনঃ রমযান মাসের সওম; তবে তুমি যদি কিছু নফল সিয়াম আদায় কর তা হল স্বতন্ত্র কথা। এরপর তিনি বললেন, বলুন, আল্লাহ্ আমার উপর কী পরিমাণ যাকাত ফরজ করেছেন? রাবী বলেন, আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) তাঁকে ইসলামের বিধান জানিয়ে দিলেন। এরপর তিনি বললেন, ঐ সত্তার কসম, যিনি আপনাকে সত্য দিয়ে সম্মানিত করেছেন, আল্লাহ্ আমার উপর যা ফরজ করেছেন, আমি এর মাঝে কিছু বাড়াব না এবং কমাবও না। আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বললেনঃ সে সত্য বলে থাকলে সফলতা লাভ করল কিংবা বলেছেন, সে সত্য বলে থাকলে জান্নাত লাভ করল।",
                "Narrated Talhah bin 'Ubaidullah (RA): A bedouin with unkempt hair came to Allah's Messenger (peace be upon him) and said, \"O Allah's Messenger! Inform me, what has Allah made obligatory for me regarding prayers?\" He said, \"The five prayers, unless you do additional voluntary prayers.\" Then he said, \"Inform me, what has Allah made obligatory for me regarding fasting?\" Allah's Messenger (peace be upon him) said, \"The month of Ramadan, unless you do additional voluntary fasting.\" Then he said, \"Inform me, what has Allah made obligatory for me regarding Zakat?\" The narrator said, Allah's Messenger (peace be upon him) informed him of the laws of Islam. The bedouin said, \"By Him Who has honored you with truth, I will not do anything extra nor will I decrease anything from what Allah has made obligatory upon me.\" Allah's Messenger (peace be upon him) said, \"He will succeed if he is truthful,\" or said, \"He will enter Paradise if he is truthful.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1891)
        ));

        // 2. হাদিস- ১৮৯২ (Screenshot Card 2)
        list.add(new RozaHadithItem(
                2,
                1892,
                "১৮৯২",
                "1892",
                "حَدَّثَنَا مُسَدَّدٌ حَدَّثَنَا إِسْمَاعِيلُ عَنْ أَيُّوبَ عَنْ نَافِعٍ عَنْ ابْنِ عُمَرَ قَالَ صَامَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ عَاشُورَاءَ وَأَمَرَ بِصِيَامِهِ فَلَمَّا فُرِضَ رَمَضَانُ تُرِكَ وَكَانَ عَبْدُ اللَّهِ لَا يَصُومُهُ إِلَّا أَنْ يُوَافِقَ صَوْمَهُ",
                "ইবনু 'উমর (রাঃ) বর্ণিত তিনি বলেন, নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) 'আশুরা'র দিন সিয়াম পালন করেছেন এবং এ সিয়ামের জন্য আদেশ করেছেন। পরে যখন রমযানের সিয়াম ফরজ হল তখন তা ছেড়ে দেওয়া হয়। 'আবদুল্লাহ (রহঃ) এ সিয়াম পালন করতেন না, তবে মাসের যে দিনগুলোতে সাধারণত সিয়াম পালন করতেন তার সাথে মিল হলে করতেন।",
                "Narrated Ibn 'Umar (RA): The Prophet (peace be upon him) observed the fast on the day of 'Ashura and ordered Muslims to fast on that day. When the fasting during the month of Ramadan was made obligatory, the fast of 'Ashura was abandoned. 'Abdullah would not fast on that day unless it coincided with his routine fasting.",
                RozaHadithFavoritesManager.isFavorite(context, 1892)
        ));

        // 3. হাদিস- ১৮৯৪ (Screenshot Card 3)
        list.add(new RozaHadithItem(
                3,
                1894,
                "১৮৯৪",
                "1894",
                "حَدَّثَنَا عَبْدُ اللَّهِ بْنُ مَسْلَمَةَ عَنْ مَالِكٍ عَنْ أَبِي الزِّنَادِ عَنْ الْأَعْرَجِ عَنْ أَبِي هُرَيْرَةَ أَنَّ رَسُولَ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ قَالَ الصِّيَامُ جُنَّةٌ فَلَا يَرْفُثْ وَلَا يَجْهَلْ وَإِنْ امْرُؤٌ قَاتَلَهُ أَوْ شَاتَمَهُ فَلْيَقُلْ إِنِّي صَائِمٌ مَرَّتَيْنِ وَالَّذِي نَفْسِي بِيَدِهِ لَخُلُوفُ فَمِ الصَّائِمِ أَطْيَبُ عِنْدَ اللَّهِ تَعَالَى مِنْ رِيحِ الْمِسْكِ يَتْرُكُ طَعَامَهُ وَشَرَابَهُ وَشَهْوَتَهُ مِنْ أَجْلِي الصِّيَامُ لِي وَأَنَا أَجْزِي بِهِ وَالْحَسَنَةُ بِعَشْرِ أَمْثَالِهَا",
                "আবু হুরায়রা (রাঃ) বর্ণিত আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেনঃ সিয়াম ঢাল স্বরূপ। সুতরাং অশ্লীলতা করবে না এবং মূর্খের মত কাজ করবে না। যদি কেউ তার সাথে ঝগড়া করতে চায়, তাকে গালি দেয়, তবে সে যেন দুই বার বলে, আমি সওম পালন করছি। ঐ সত্তার শপথ, যার হাতে আমার প্রাণ, অবশ্যই সওম পালনকারীর মুখের গন্ধ আল্লাহ্র নিকট মিসকের সুগন্ধির চাইতেও উৎকৃষ্ট, সে আমার জন্য আহার, পান ও কামাচার পরিত্যাগ করে। সিয়াম আমারই জন্য। তাই এর পুরস্কার আমি নিজেই দান করব। আর প্রত্যেক নেক কাজের বিনিময় দশ গুণ।",
                "Narrated Abu Huraira (RA): Allah's Messenger (peace be upon him) said, \"Fasting is a shield. So the person observing fasting should avoid obscene language and should not behave foolishly. If somebody fights with him or abuses him, he should say twice, 'I am fasting.' By Him in Whose Hands my soul is, the smell of the mouth of a fasting person is better to Allah than the scent of musk. Allah says: 'He leaves his food, drink and desires for My sake. The fast is for Me and I will reward for it, and every good deed is rewarded ten times over.'\"",
                RozaHadithFavoritesManager.isFavorite(context, 1894)
        ));

        // 4. হাদিস- ১৮৯৫ (Screenshot Card 4)
        list.add(new RozaHadithItem(
                4,
                1895,
                "১৮৯৫",
                "1895",
                "حَدَّثَنَا عَلِيُّ بْنُ عَبْدِ اللَّهِ حَدَّثَنَا سُفْيَانُ حَدَّثَنَا جَامِعٌ عَنْ أَبِي وَائِلٍ عَنْ حُذَيْفَةَ قَالَ قَالَ عُمَرُ مَنْ يَحْفَظُ حَدِيثًا عَنْ النَّبِيِّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ فِي الْفِتْنَةِ قَالَ حُذَيْفَةُ أَنَا سَمِعْتُهُ يَقُولُ فِتْنَةُ الرَّجُلِ فِي أَهْلِهِ وَمَالِهِ وَجَارِهِ تُكَفِّرُهَا الصَّلَاةُ وَالصِّيَامُ وَالصَّدَقَةُ قَالَ لَيْسَ أَسْأَلُ عَنْ ذِهِ إِنَّمَا أَسْأَلُ عَنْ الَّتِي تَمُوجُ كَمَا يَمُوجُ الْبَحْرُ قَالَ وَإِنَّ دُونَ ذَلِكَ بَابًا مُغْلَقًا قَالَ فَيُفْتَحُ أَوْ يُكْسَرُ قَالَ يُكْسَرُ قَالَ ذَاكَ أَجْدَرُ أَنْ لَا يُغْلَقَ إِلَى يَوْمِ الْقِيَامَةِ فَقُلْنَا لِمَسْرُوقٍ سَلْهُ أَكَانَ عُمَرُ يَعْلَمُ مَنْ الْبَابُ فَسَأَلَهُ فَقَالَ نَعَمْ كَمَا يَعْلَمُ أَنَّ دُونَ غَدٍ اللَّيْلَةَ",
                "হুযাইফাহ (রাঃ) বর্ণিত তিনি বলেন, একদা 'উমার (রাঃ) বললেন, ফিতনা সম্পর্কিত নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম)-এর হাদীসটি কার মুখস্ত আছে? হুযাইফাহ (রাঃ) বললেন, আমি নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) কে বলতে শুনেছি যে, পরিবার, ধন-সম্পদ এবং প্রতিবেশীর মানুষের জন্য ফিতনা। সালাত, সিয়াম এবং সাদকা এর কাফফারা হয়ে যায়। 'উমার (রাঃ) বললেন, এ ফিতনা সম্পর্কে আমি প্রশ্ন করছি না, আমি তো প্রশ্ন করেছি ঐ ফিতনা সম্পর্কে, যা সমুদ্রের ঢেউয়ের ন্যায় আন্দোলিত হতে থাকবে। হুযাইফাহ (রাঃ) বললেন এ ফিতনার সামনে বন্ধ দরজা আছে। 'উমার (রাঃ) বললেন, এ দরজা কি খুলে যাবে, না ভেঙ্গে যাবে? হুযাইফাহ (রাঃ) বললেন, ভেঙ্গে যাবে। 'উমার (রাঃ) বললেন, তাহলে তো তা ক্বিয়ামত পর্যন্ত বন্ধ হবে না। আমরা মাসরূক (রহঃ)-কে বললাম, হুযাইফাহ (রাঃ)-কে জিজ্ঞেস করুন, 'উমার (রাঃ) কি জানতেন, কে সেই দরজা? তিনি বললেন, হ্যাঁ, তিনি এরূপ জানতেন যেরূপ কালকের দিনের পূর্বে আজকের রাত।",
                "Narrated Hudhaifa (RA): 'Umar asked the people, \"Who among you remembers the statement of the Prophet (peace be upon him) regarding the affliction?\" Hudhaifa replied, \"I heard him saying, 'The affliction of a person concerning his family, his wealth, and his neighbor is expiated by prayer, fasting, and charity.'\" 'Umar said, \"I am not asking about this, but about that affliction which will surge like the waves of the sea.\" Hudhaifa said, \"There is a closed door between you and that affliction.\" 'Umar asked, \"Will it be opened or broken?\" Hudhaifa replied, \"It will be broken.\" 'Umar said, \"Then it will never be closed till the Day of Resurrection.\" We said to Masruq, \"Ask Hudhaifa whether 'Umar knew who that door was.\" So he asked him and Hudhaifa replied, \"Yes, he knew it as well as one knows that night precedes the dawn.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1895)
        ));

        // 5. হাদিস- ১৮৯৬ (Screenshot 1, Card 1)
        list.add(new RozaHadithItem(
                5,
                1896,
                "১৮৯৬",
                "1896",
                "حَدَّثَنَا خَالِدُ بْنُ مَخْلَدٍ حَدَّثَنَا سُلَيْمَانُ بْنُ بِلَالٍ قَالَ حَدَّثَنِي أَبُو حَازِمٍ عَنْ سَهْلٍ عَنْ النَّبِيِّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ قَالَ إِنَّ فِي الْجَنَّةِ بَابًا يُقَالُ لَهُ الرَّيَّانُ يَدْخُلُ مِنْهُ الصَّائِمُونَ يَوْمَ الْقِيَامَةِ لَا يَدْخُلُ مِنْهُ أَحَدٌ غَيْرُهُمْ يُقَالُ أَيْنَ الصَّائِمُونَ فَيَقُومُونَ لَا يَدْخُلُ مِنْهُ أَحَدٌ غَيْرُهُمْ فَإِذَا دَخَلُوا أُغْلِقَ فَلَمْ يَدْخُلْ مِنْهُ أَحَدٌ",
                "সাহল (রাঃ) বর্ণিত নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেনঃ জান্নাতের রাইয়ান নামক একটি দরজা আছে। এ দরজা দিয়ে কিয়ামতের দিন সওম পালনকারীরাই প্রবেশ করবে। তাদের ব্যতীত আর কেউ এ দরজা দিয়ে প্রবেশ করতে পারবে না। ঘোষণা দেয়া হবে, সওম পালনকারীরা কোথায়? তখন তারা দাঁড়াবে। তারা ব্যতীত আর কেউ এ দরজা দিয়ে প্রবেশ করবে না। তাদের প্রবেশের পরই দরজা বন্ধ করে দেয়া হবে। যাতে করে এ দরোজা দিয়ে আর কেউ প্রবেশ না করে।",
                "Narrated Sahl (RA): The Prophet (peace be upon him) said, \"There is a gate in Paradise called Ar-Rayyan, and those who observe fasts will enter through it on the Day of Resurrection and none except them will enter through it. It will be said, 'Where are those who used to observe fasts?' They will get up, and none except them will enter through it. After their entry the gate will be closed and nobody will enter through it.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1896)
        ));

        // 6. হাদিস- ১৮৯৭ (Screenshot 1, Card 2)
        list.add(new RozaHadithItem(
                6,
                1897,
                "১৮৯৭",
                "1897",
                "حَدَّثَنَا إِبْرَاهِيمُ بْنُ الْمُنْذِرِ قَالَ حَدَّثَنِي مَعْنٌ قَالَ حَدَّثَنِي مَالِكٌ عَنْ ابْنِ شِهَابٍ عَنْ حُمَيْدِ بْنِ عَبْدِ الرَّحْمَنِ عَنْ أَبِي هُرَيْرَةَ أَنَّ رَسُولَ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ قَالَ مَنْ أَنْفَقَ زَوْجَيْنِ فِي سَبِيلِ اللَّهِ نُودِيَ مِنْ أَبْوَابِ الْجَنَّةِ يَا عَبْدَ اللَّهِ هَذَا خَيْرٌ فَمَنْ كَانَ مِنْ أَهْلِ الصَّلَاةِ دُعِيَ مِنْ بَابِ الصَّلَاةِ وَمَنْ كَانَ مِنْ أَهْلِ الْجِهَادِ دُعِيَ مِنْ بَابِ الْجِهَادِ وَمَنْ كَانَ مِنْ أَهْلِ الصِّيَامِ دُعِيَ مِنْ بَابِ الرَّيَّانِ وَمَنْ كَانَ مِنْ أَهْلِ الصَّدَقَةِ دُعِيَ مِنْ بَابِ الصَّدَقَةِ فَقَالَ أَبُو بَكْرٍ بِأَبِي أَنْتَ وَأُمِّي يَا رَسُولَ اللَّهِ مَا عَلَى مَنْ دُعِيَ مِنْ تِلْكَ الْأَبْوَابِ مِنْ ضَرُورَةٍ فَهَلْ يُدْعَى أَحَدٌ مِنْ تِلْكَ الْأَبْوَابِ كُلِّهَا قَالَ نَعَمْ وَأَرْجُو أَنْ تَكُونَ مِنْهُمْ",
                "আবু হুরায়রা (রাঃ) বর্ণিত আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেনঃ যে কেউ আল্লাহ্র পথে জোড়া জোড়া ব্যয় করবে তাকে জান্নাতের দরজাসমূহ হতে ডাক দিয়ে বলা হবে, হে আল্লাহ্র বান্দা! এটাই উত্তম। অতএব যে সালাত আদায়কারী, তাকে সালাতের দরজা হতে ডাকা হবে। যে মুজাহিদ, তাকে জিহাদের দরজা হতে ডাকা হবে। যে সিয়াম পালনকারী, তাকে রাইয়ান দরজা হতে ডাকা হবে। যে সদাকাহ্ দানকারী, তাকে সদাকাহ্র দরজা হতে ডাকা হবে। এরপর আবু বকর (রাঃ) বললেন, হে আল্লাহ্র রসূল! আপনার জন্য আমার পিতা-মাতা কুরবান হোক, সকল দরজা হতে কাউকে ডাকার কোন প্রয়োজন নেই, তবে কি কাউকে সব দরজা হতে ডাকা হবে? আল্লাহ্র রসূল (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) বললেনঃ হ্যাঁ। আমি আশা করি তুমি তাদের মধ্যে হবে।",
                "Narrated Abu Huraira (RA): Allah's Messenger (peace be upon him) said, \"Whoever gives two kinds of things in charity for Allah's cause will be called from the gates of Paradise: 'O Allah's slave! This is good.' So whoever was among the people of prayer will be called from the gate of prayer; whoever was among the people of Jihad will be called from the gate of Jihad; whoever was among the people of fasting will be called from the gate of Ar-Rayyan; and whoever was among the people of charity will be called from the gate of charity.\" Abu Bakr said, \"May my father and mother be sacrificed for you, O Allah's Messenger! There is no necessity for anyone to be called from all these gates, but will anyone be called from all of them?\" He said, \"Yes, and I hope that you will be one of them.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1897)
        ));

        // 7. হাদিস- ১৮৯৮ (Screenshot 1, Card 3)
        list.add(new RozaHadithItem(
                7,
                1898,
                "১৮৯৮",
                "1898",
                "حَدَّثَنَا قُتَيْبَةُ حَدَّثَنَا إِسْمَاعِيلُ بْنُ جَعْفَرٍ عَنْ أَبِي سُهَيْلٍ عَنْ أَبِيهِ عَنْ أَبِي هُرَيْرَةَ أَنَّ رَسُولَ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ قَالَ إِذَا جَاءَ رَمَضَانُ فُتِّحَتْ أَبْوَابُ الْجَنَّةِ",
                "আবু হুরায়রা (রাঃ) বর্ণিত আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেনঃ যখন রমযান আসে তখন জান্নাতের দরজাসমূহ উন্মুক্ত করে দেয়া হয়।",
                "Narrated Abu Huraira (RA): Allah's Messenger (peace be upon him) said, \"When Ramadan comes, the gates of Paradise are opened.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1898)
        ));

        // 8. হাদিস- ১৮৯৯ (Screenshot 1, Card 4)
        list.add(new RozaHadithItem(
                8,
                1899,
                "১৮৯৯",
                "1899",
                "حَدَّثَنِي يَحْيَى بْنُ بُكَيْرٍ قَالَ حَدَّثَنِي اللَّيْثُ عَنْ عُقَيْلٍ عَنْ ابْنِ شِهَابٍ قَالَ أَخْبَرَنِي ابْنُ أَبِي أَنَسٍ مَوْلَى التَّيْمِيِّينَ أَنَّ أَبَاهُ حَدَّثَهُ أَنَّهُ سَمِعَ أَبَا هُرَيْرَةَ يَقُولُ قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا دَخَلَ شَهْرُ رَمَضَانَ فُتِّحَتْ أَبْوَابُ السَّمَاءِ وَغُلِّقَتْ أَبْوَابُ جَهَنَّمَ وَسُلْسِلَتْ الشَّيَاطِينُ",
                "আবু হুরায়রা (রাঃ) বর্ণিত তিনি বলছেন, আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেনঃ রমযান আসলে আসমানের দরজাসমূহ খুলে দেয়া হয় এবং জাহান্নামের দরজাসমূহ বন্ধ করে দেয়া হয় এবং শয়তানগুলোকে শিকলবন্দী করে দেয়া হয়।",
                "Narrated Abu Huraira (RA): He said, Allah's Messenger (peace be upon him) said, \"When the month of Ramadan enters, the gates of heaven are opened, the gates of Hell are closed, and the devils are chained.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1899)
        ));

        // 9. হাদিস- ১৯০০ (Screenshot 1, Card 5)
        list.add(new RozaHadithItem(
                9,
                1900,
                "১৯০০",
                "1900",
                "حَدَّثَنَا يَحْيَى بْنُ بُكَيْرٍ قَالَ حَدَّثَنِي اللَّيْثُ عَنْ عُقَيْلٍ عَنْ ابْنِ شِهَابٍ قَالَ أَخْبَرَنِي سَالِمُ بْنُ عَبْدِ اللَّهِ أَنَّ ابْنَ عُمَرَ قَالَ سَمِعْتُ رَسُولَ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَقُولُ إِذَا رَأَيْتُمُوهُ فَصُومُوا وَإِذَا رَأَيْتُمُوهُ فَأَفْطِرُوا فَإِنْ غُمَّ عَلَيْكُمْ فَاقْدُرُوا لَهُ وَقَالَ غَيْرُهُ عَنْ اللَّيْثِ حَدَّثَنِي عُقَيْلٌ وَيُونُسُ لِهِلَالِ رَمَضَانَ",
                "ইবনু 'উমর (রাঃ) বর্ণিত তিনি বলেন, আমি আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম)-কে বলতে শুনেছি, যখন তোমরা তা (চাঁদ) দেখবে তখন সওম রাখবে, আবার যখন তা দেখবে তখন ইফতার করবে। আর যদি আকাশ মেঘলা থাকে তবে সময় হিসাব করে (ত্রিশ দিন) পূর্ণ করবে। ইয়াহ্ইয়া ইবনু বুকায়র (রহঃ) ব্যতীত অন্যান্য রাওয়ী হতে 'উকায়ল এবং ইউনুস (রহঃ) সূত্রে বর্ণনা করেন, নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) কথাটি বলেছেন রমযানের চাঁদ সম্পর্কে।",
                "Narrated Ibn 'Umar (RA): He said, I heard Allah's Messenger (peace be upon him) saying, \"When you see it (the crescent), then fast, and when you see it, then break your fast. And if it is overcast for you, then calculate for it (thirty days).\" Others besides Yahya bin Bukayr narrated from Al-Layth from 'Uqayl and Yunus regarding the crescent of Ramadan.",
                RozaHadithFavoritesManager.isFavorite(context, 1900)
        ));

        // 10. হাদিস- ১৯০১ (Screenshot 2, Card 1)
        list.add(new RozaHadithItem(
                10,
                1901,
                "১৯০১",
                "1901",
                "حَدَّثَنَا مُسْلِمُ بْنُ إِبْرَاهِيمَ حَدَّثَنَا هِشَامٌ حَدَّثَنَا يَحْيَى عَنْ أَبِي سَلَمَةَ عَنْ أَبِي هُرَيْرَةَ عَنْ النَّبِيِّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ قَالَ مَنْ قَامَ لَيْلَةَ الْقَدْرِ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ وَمَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
                "আবু হুরায়রা (রাঃ) বর্ণিত নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেনঃ যে ব্যক্তি লাইলাতুল ক্বদরে ঈমানের সাথে সাওয়াবের আশায় রাত জেগে ইবাদাত করে, তার পিছনের সমস্ত গুনাহ ক্ষমা করা হবে। আর যে ব্যক্তি ঈমানসহ সওয়াবের আশায় রমাযানে সিয়াম পালন করবে, তারও অতীতের সমস্ত গোনাহ মাফ করা হবে।",
                "Narrated Abu Huraira (RA): The Prophet (peace be upon him) said, \"Whoever stands in prayer during Laylatul Qadr with faith and seeking reward, his past sins will be forgiven. And whoever fasts Ramadan with faith and seeking reward, his past sins will be forgiven.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1901)
        ));

        // 11. হাদিস- ১৯০২ (Screenshot 2, Card 2)
        list.add(new RozaHadithItem(
                11,
                1902,
                "১৯০২",
                "1902",
                "حَدَّثَنَا مُوسَى بْنُ إِسْمَاعِيلَ حَدَّثَنَا إِبْرَاهِيمُ بْنُ سَعْدٍ أَخْبَرَنَا ابْنُ شِهَابٍ عَنْ عُبَيْدِ اللَّهِ بْنِ عَبْدِ اللَّهِ بْنِ عُتْبَةَ أَنَّ ابْنَ عَبَّاسٍ قَالَ كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ أَجْوَدَ النَّاسِ بِالْخَيْرِ وَكَانَ أَجْوَدُ مَا يَكُونُ فِي رَمَضَانَ حِينَ يَلْقَاهُ جِبْرِيلُ وَكَانَ جِبْرِيلُ عَلَيْهِ السَّلَام يَلْقَاهُ كُلَّ لَيْلَةٍ فِي رَمَضَانَ حَتَّى يَنْسَلِخَ يَعْرِضُ عَلَيْهِ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ الْقُرْآنَ فَإِذَا لَقِيَهُ جِبْرِيلُ عَلَيْهِ السَّلَام كَانَ أَجْوَدَ بِالْخَيْرِ مِنْ الرِّيحِ الْمُرْسَلَةِ",
                "ইবনু আব্বাস (রাঃ) বর্ণিত তিনি বলেন, নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) ধন-সম্পদ ব্যয় করার ব্যাপারে সকলের চেয়ে দানশীল ছিলেন। রমাযানে জিবরাঈল (আঃ) যখন তাঁর সাথে সাক্ষাৎ করতেন, তখন তিনি আরো অধিক দান করতেন। রমাযান শেষ না হওয়া পর্যন্ত প্রতি রাতেই জিবরাঈল (আঃ) তাঁর সাথে একবার সাক্ষাৎ করতেন। আর নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) তাঁকে কুরআন শোনাতেন। জিবরাঈল যখন তাঁর সঙ্গে সাক্ষাৎ করতেন তখন তিনি রহমতসহ প্রেরিত বায়ুর চেয়ে অধিক ধন-সম্পদ দান করতেন।",
                "Narrated Ibn Abbas (RA): He said, The Prophet (peace be upon him) was the most generous of people in charity, and he was at his most generous in Ramadan when Gabriel met him. Gabriel (peace be upon him) used to meet him every night in Ramadan until it came to an end, and the Prophet (peace be upon him) recited the Quran to him. When Gabriel met him, he was more generous with charity than the sent wind.",
                RozaHadithFavoritesManager.isFavorite(context, 1902)
        ));

        // 12. হাদিস- ১৯০৩ (Screenshot 2, Card 3)
        list.add(new RozaHadithItem(
                12,
                1903,
                "১৯০৩",
                "1903",
                "حَدَّثَنَا آدَمُ بْنُ أَبِي إِيَاسٍ حَدَّثَنَا ابْنُ أَبِي ذِئْبٍ حَدَّثَنَا سَعِيدٌ الْمَقْبُرِيُّ عَنْ أَبِيهِ عَنْ أَبِي هُرَيْرَةَ قَالَ قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ مَنْ لَمْ يَدَعْ قَوْلَ الزُّورِ وَالْعَمَلَ بِهِ فَلَيْسَ لِلَّهِ حَاجَةٌ فِي أَنْ يَدَعَ طَعَامَهُ وَشَرَابَهُ",
                "আবু হুরায়রা (রাঃ) বর্ণিত তিনি বলেন, নবী (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) বলেছেনঃ যে ব্যক্তি মিথ্যা বলা ও সে অনুযায়ী আমল বর্জন করেনি, তাঁর এ পানাহার পরিত্যাগ করায় আল্লাহ্র কোন প্রয়োজন নেই।",
                "Narrated Abu Huraira (RA): He said, The Prophet (peace be upon him) said, \"Whoever does not abandon false speech and acting upon it, Allah has no need of his giving up his food and drink.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1903)
        ));

        // 13. হাদিস- ১৯০৪ (Screenshot 2, Card 4)
        list.add(new RozaHadithItem(
                13,
                1904,
                "১৯০৪",
                "1904",
                "حَدَّثَنَا إِبْرَاهِيمُ بْنُ مُوسَى أَخْبَرَنَا هِشَامُ بْنُ يُوسُفَ عَنْ ابْنِ جُرَيْجٍ قَالَ أَخْبَرَنِي عَطَاءٌ عَنْ أَبِي صَالِحٍ الزَّيَّاتِ أَنَّهُ سَمِعَ أَبَا هُرَيْرَةَ يَقُولُ قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ قَالَ اللَّهُ كُلُّ عَمَلِ ابْنِ آدَمَ لَهُ إِلَّا الصِّيَامَ فَإِنَّهُ لِي وَأَنَا أَجْزِي بِهِ وَالصِّيَامُ جُنَّةٌ وَإِذَا كَانَ يَوْمُ صَوْمِ أَحَدِكُمْ فَلَا يَرْفُثْ وَلَا يَصْخَبْ فَإِنْ سَابَّهُ أَحَدٌ أَوْ قَاتَلَهُ فَلْيَقُلْ إِنِّي امْرُؤٌ صَائِمٌ وَالَّذِي نَفْسُ مُحَمَّدٍ بِيَدِهِ لَخُلُوفُ فَمِ الصَّائِمِ أَطْيَبُ عِنْدَ اللَّهِ مِنْ رِيحِ الْمِسْكِ لِلصَّائِمِ فَرْحَتَانِ يَفْرَحُهُمَا إِذَا أَفْطَرَ فَرِحَ وَإِذَا لَقِيَ رَبَّهُ فَرِحَ بِصَوْمِهِ",
                "আবু হুরায়রা (রাঃ) বর্ণিত তিনি বলেন, আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেনঃ আল্লাহ্ তা'আলা বলেছেন, সওম ব্যতীত আদম সন্তানের প্রতিটি কাজই তাঁর নিজের জন্য, কিন্তু সিয়াম আমার জন্য। তাই আমি এর প্রতিদান দেব। সিয়াম ঢাল স্বরূপ। তোমাদের কেউ যেন সিয়াম পালনের দিন অশ্লীলতায় লিপ্ত না হয় এবং ঝগড়া-বিবাদ না করে। যদি কেউ তাঁকে গালি দেয় অথবা তাঁর সঙ্গে ঝগড়া করে, তাহলে সে যেন বলে, আমি একজন সায়িম। যার কবজায় মুহাম্মাদের প্রাণ, তাঁর শপথ! অবশ্যই সায়িমের মুখের গন্ধ আল্লাহ্র নিকট মিসকের গন্ধের চাইতেও সুগন্ধি। সায়িমের জন্য রয়েছে দু'টি খুশী যা তাঁকে খুশী করে। যখন সে ইফতার করে, সে খুশী হয় এবং যখন সে তাঁর রবের সাথে সাক্ষাৎ করবে, তখন সওমের বিনিময়ে আনন্দিত হবে।",
                "Narrated Abu Huraira (RA): He said, Allah's Messenger (peace be upon him) said: Allah said, \"Every deed of the son of Adam is for himself, except fasting; for it is for Me, and I will reward for it. Fasting is a shield. When any of you is fasting, he should not use obscene language or raise his voice in dispute. If someone abuses him or fights with him, he should say, 'I am a fasting person.' By Him in Whose Hand is the life of Muhammad, the breath of a fasting person is sweeter to Allah than the scent of musk. The fasting person has two moments of joy: when he breaks his fast he is joyful, and when he meets his Lord he is joyful because of his fast.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1904)
        ));

        // 14. হাদিস- ১৯০৫ (Screenshot 2, Card 5 & Screenshot 3, Card 1)
        list.add(new RozaHadithItem(
                14,
                1905,
                "১৯০৫",
                "1905",
                "حَدَّثَنَا عَبْدَانُ عَنْ أَبِي حَمْزَةَ عَنْ الْأَعْمَشِ عَنْ إِبْرَاهِيمَ عَنْ عَلْقَمَةَ قَالَ بَيْنَا أَنَا أَمْشِي مَعَ عَبْدِ اللَّهِ فَقَالَ كُنَّا مَعَ النَّبِيِّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ فَقَالَ مَنْ اسْتَطَاعَ الْبَاءَةَ فَلْيَتَزَوَّجْ فَإِنَّهُ أَغَضُّ لِلْبَصَرِ وَأَحْصَنُ لِلْفَرْجِ وَمَنْ لَمْ يَسْتَطِعْ فَعَلَيْهِ بِالصَّوْمِ فَإِنَّهُ لَهُ وِجَاءٌ",
                "'আলক্বামাহ (রহঃ) বর্ণিত তিনি বলেন, আমি 'আবদুল্লাহ (রাঃ)-এর সঙ্গে চলতে ছিলাম, তখন তিনি বললেন, আমরা আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম)-এর সাথে ছিলাম, তিনি বললেনঃ যে ব্যক্তির সামর্থ্য আছে, সে যেন বিয়ে করে নেয়। কেননা বিয়ে চোখকে অবনত রাখে এবং লজ্জাস্থানকে সংবৃত করে। আর যার সামর্থ্য নেই, সে যেন সওম পালন করে। সওম তার প্রবৃত্তিকে দমন করে। আবু 'আবদুল্লাহ (রহঃ) বলেন, ------------ শব্দের অর্থ বিবাহ।",
                "Narrated 'Alqamah (RA): He said, While I was walking with 'Abdullah (RA), he said: We were with the Prophet (peace be upon him), and he said, \"Whoever among you has the means should get married, for it lowers the gaze and guards modesty; and whoever is unable, let him observe fasting, for it is a restraint for him.\" Abu 'Abdullah (RA) said, the word ... means marriage.",
                RozaHadithFavoritesManager.isFavorite(context, 1905)
        ));

        // 15. হাদিস- ১৯০৬ (Screenshot 3, Card 2)
        list.add(new RozaHadithItem(
                15,
                1906,
                "১৯০৬",
                "1906",
                "حَدَّثَنَا عَبْدُ اللَّهِ بْنُ مَسْلَمَةَ حَدَّثَنَا مَالِكٌ عَنْ نَافِعٍ عَنْ عَبْدِ اللَّهِ بْنِ عُمَرَ أَنَّ رَسُولَ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ ذَكَرَ رَمَضَانَ فَقَالَ لَا تَصُومُوا حَتَّى تَرَوُا الْهِلَالَ وَلَا تُفْطِرُوا حَتَّى تَرَوْهُ فَإِنْ غُمَّ عَلَيْكُمْ فَاقْدُرُوا لَهُ",
                "'আবদুল্লাহ ইবনু 'উমর (রাঃ) বর্ণিত আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) রমযানের কথা আলোচনা করে বললেনঃ চাঁদ না দেখে তোমরা সওম পালন করবে না এবং চাঁদ না দেখে ইফতার করবে না। যদি মেঘাচ্ছন্ন থাকে তাহলে তার সময় (ত্রিশ দিন) পরিমাণ পূর্ণ করবে।",
                "Narrated 'Abdullah bin 'Umar (RA): Allah's Messenger (peace be upon him) mentioned Ramadan and said, \"Do not fast until you see the crescent, and do not break your fast until you see it; and if it is overcast, then calculate its duration (to thirty days).\"",
                RozaHadithFavoritesManager.isFavorite(context, 1906)
        ));

        // 16. হাদিস- ১৯০৭ (Screenshot 3, Card 3)
        list.add(new RozaHadithItem(
                16,
                1907,
                "১৯০৭",
                "1907",
                "حَدَّثَنَا عَبْدُ اللَّهِ بْنُ مَسْلَمَةَ حَدَّثَنَا مَالِكٌ عَنْ عَبْدِ اللَّهِ بْنِ دِينَارٍ عَنْ عَبْدِ اللَّهِ بْنِ عُمَرَ أَنَّ رَسُولَ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ قَالَ الشَّهْرُ تِسْعٌ وَعِشْرُونَ لَيْلَةً فَلَا تَصُومُوا حَتَّى تَرَوْهُ فَإِنْ غُمَّ عَلَيْكُمْ فَأَكْمِلُوا الْعِدَّةَ ثَلَاثِينَ",
                "'আবদুল্লাহ ইবনু 'উমর (রাঃ) বর্ণিত আল্লাহ্র রসূল (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেনঃ মাস উনত্রিশ রাত বিশিষ্টও হয়। তাই তোমরা চাঁদ না দেখে সওম শুরু করবে না। যদি আকাশ মেঘাচ্ছন্ন থাকে তাহলে তোমরা ত্রিশ দিন পূর্ণ করবে।",
                "Narrated 'Abdullah bin 'Umar (RA): Allah's Messenger (peace be upon him) said, \"The month can be twenty-nine nights, so do not fast until you see it (the crescent). If it is overcast, then complete the number thirty.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1907)
        ));

        // 17. হাদিস- ১৯০৮ (Screenshot 3, Card 4)
        list.add(new RozaHadithItem(
                17,
                1908,
                "১৯০৮",
                "1908",
                "حَدَّثَنَا أَبُو الْوَلِيدِ حَدَّثَنَا شُعْبَةُ عَنْ جَبَلَةَ بْنِ سُحَيْمٍ قَالَ سَمِعْتُ ابْنَ عُمَرَ يَقُولُ قَالَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ الشَّهْرُ هَكَذَا وَهَكَذَا وَخَنَسَ الْإِبْهَامَ فِي الثَّالِثَةِ",
                "ইবনু 'উমর (রাঃ) বর্ণিত তিনি বলেন, নবী (সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম) (দুইহাতের অঙ্গুলি তুলে ইঙ্গিত করে) বলেনঃ মাস এর এর দিনে হয় এবং তৃতীয় বার বৃদ্ধাঙ্গুলিটি বন্ধ করে নিলেন।",
                "Narrated Ibn 'Umar (RA): He said, The Prophet (peace be upon him) said (gesturing with the fingers of both hands), \"The month is such-and-such days,\" and bent down his thumb on the third gesture.",
                RozaHadithFavoritesManager.isFavorite(context, 1908)
        ));

        // 18. হাদিস- ১৯০৯ (Screenshot 3, Card 5)
        list.add(new RozaHadithItem(
                18,
                1909,
                "১৯০৯",
                "1909",
                "حَدَّثَنَا آدَمُ حَدَّثَنَا شُعْبَةُ حَدَّثَنَا مُحَمَّدُ بْنُ زِيَادٍ قَالَ سَمِعْتُ أَبَا هُرَيْرَةَ يَقُولُ قَالَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ أَوْ قَالَ قَالَ أَبُو الْقَاسِمِ صُومُوا لِرُؤْيَتِهِ وَأَفْطِرُوا لِرُؤْيَتِهِ فَإِنْ غُبِّيَ عَلَيْكُمْ فَأَكْمِلُوا عِدَّةَ شَعْبَانَ ثَلَاثِينَ",
                "আবু হুরায়রা (রাঃ) বর্ণিত তিনি বলেন, নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) অথবা বলেন, আবুল কাসিম (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) বলেছেনঃ তোমরা চাঁদ দেখে সিয়াম আরম্ভ করবে এবং চাঁদ দেখে ইফতার করবে। আকাশ যদি মেঘে ঢাকা থাকে তাহলে শা'বানের গণনা ত্রিশ দিন পুরা করবে।",
                "Narrated Abu Huraira (RA): He said, The Prophet (peace be upon him) or he said, Abul Qasim (peace be upon him) said, \"Fast upon seeing it (the crescent), and break your fast upon seeing it. If it is obscured from you, then complete the count of Sha'ban as thirty days.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1909)
        ));

        // 19. হাদিস- ১৯১০ (Screenshot 3, Card 6)
        list.add(new RozaHadithItem(
                19,
                1910,
                "১৯১০",
                "1910",
                "حَدَّثَنَا أَبُو عَاصِمٍ عَنْ ابْنِ جُرَيْجٍ عَنْ يَحْيَى بْنِ عَبْدِ اللَّهِ بْنِ صَيْفِيٍّ عَنْ عِكْرِمَةَ بْنِ عَبْدِ الرَّحْمَنِ عَنْ أُمِّ سَلَمَةَ أَنَّ النَّبِيَّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ آلَى مِنْ نِسَائِهِ شَهْرًا فَلَمَّا مَضَى تِسْعَةٌ وَعِشْرُونَ يَوْمًا غَدَا أَوْ رَاحَ فَقِيلَ لَهُ إِنَّكَ حَلَفْتَ أَنْ لَا تَدْخُلَ شَهْرًا فَقَالَ إِنَّ الشَّهْرَ يَكُونُ تِسْعَةً وَعِشْرِينَ يَوْمًا",
                "উম্মু সালামাহ (রাঃ) বর্ণিত তিনি বলেন, নবী (সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম) এক মাসের জন্য তাঁর স্ত্রীদের সাথে ঈলা করলেন। উনত্রিশ দিন অতিবাহিত হওয়ার পর সকালে বা সন্ধ্যায় তিনি তাঁদের নিকট গমন করলেন। তাঁকে জিজ্ঞেস করা হল, আপনি তো এক মাস পর্যন্ত না আসার শপথ করেছিলেন? তিনি বললেন, মাস উনত্রিশ দিনেও হয়ে থাকে।",
                "Narrated Umm Salamah (RA): She said, The Prophet (peace be upon him) took an oath of abstention (Ila) from his wives for a month. When twenty-nine days had elapsed, he visited them in the morning or evening. It was said to him, \"Did you not swear not to enter for a month?\" He said, \"Indeed, the month can be twenty-nine days.\"",
                RozaHadithFavoritesManager.isFavorite(context, 1910)
        ));

        return list;
    }
}
