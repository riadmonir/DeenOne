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
 * Repository for সিয়ামের তাৎপর্য ও ফযীলত (Significance & Virtues of Fasting).
 * Pre-seeded with all 5 cards verbatim matching user prompt and screenshot.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaVirtuesOfSawmRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaVirtuesOfSawmRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_virtues_of_sawm.php");
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
            // Fallback gracefully to pre-seeded cache
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();
        // Card 1: সিয়াম শব্দের তাৎপর্য
        list.add(new HajjHistoryCardItem(
                1,
                "সিয়াম শব্দের তাৎপর্য",
                "Significance and Linguistic Meaning of Fasting",
                "অভিধানে صيام (সিয়াম)-এর সাধারণ অর্থ হল, বিরত থাকা। আর এ জন্যই কথা বলা থেকে যে বিরত থাকে -অর্থাৎ চুপ ও নিস্তব্ধ থাকে তাকে صائم (সায়েম) বলা হয়। মহান আল্লাহ মার...",
                "In the Arabic language, Sawm (Fasting) means refraining. In Islamic Shariah, it signifies worshipping Allah by abstaining from food, drink, and intimacy from true dawn until sunset...",
                "অভিধানে صيام (সিয়াম)-এর সাধারণ অর্থ হল, বিরত থাকা। আর এ জন্যই কথা বলা থেকে যে বিরত থাকে -অর্থাৎ চুপ ও নিস্তব্ধ থাকে তাকে صائم (সায়েম) বলা হয়। মহান আল্লাহ মারয়্যাম (আঃ)-এর ইতিহাস উল্লেখ করে বলেন,\n\n" + 
                        "(فَإِمَّا تَرَيِنَّ مِنَ الْبَشَرِ أَحَداً فَقُولِي إِنِّي نَذَرْتُ لِلرَّحْمَنِ صَوْماً فَلَنْ أُكَلِّمَ الْيَوْمَ إِنْسِيّاً)\n\n" + 
                        "অর্থাৎ, (সন্তান ভূমিষ্ঠ করার পর) যদি তুমি কাউকে (কোন প্রশ্ন বা কৈফিয়ত করতে) দেখ, তবে তুমি বল, ‘আমি দয়াময় (আল্লাহর) জন্য (কথা বলা থেকে) বিরত থাকার নযর মেনেছি। সুতরাং আজ আমি কিছুতেই কোন মানুষের সাথে কথাই বলব না।’ (কুরআনুল কারীম ১৯/২৬)\n\n" + 
                        "বলা বাহুল্য, এখানে ‘সওম’-এর অর্থ হল কথা বলা থেকে বিরত থাকা।\n\n" + 
                        "শরীয়তের পরিভাষায় ‘সওম’ বা ‘সিয়াম’-এর অর্থ হল, ফজর উদয় থেকে সূর্যাস্ত পর্যন্ত পানাহার, স্ত্রী-সঙ্গম ইত্যাদি যাবতীয় রোযা নষ্টকারী কর্ম হতে বিরত থাকার মাধ্যমে আল্লাহর ইবাদত করা।[1]\n\n" + 
                        "অবশ্য এই সংজ্ঞায় অসারতা ও অশ্লীলতা থেকে বিরত থাকাও শামিল রয়েছে। কারণ, প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘কেবল পানাহার থেকে বিরত থাকার নামই সিয়াম নয়; বরং অসারতা ও অশ্লীলতা থেকে বিরত থাকার নামই হল (আসল) সিয়াম। সুতরাং যদি তোমাকে কেউ গালাগালি করে অথবা তোমার প্রতি মূর্খতা প্রদর্শন করে, তাহলে তুমি (তার প্রতিশোধ না নিয়ে) তাকে বল যে, ‘আমি রোযা রেখেছি, আমি রোযা রেখেছি।’’[2]\n\n" + 
                        "‘রোযা’ আভিধানিক অর্থে সিয়ামের সমার্থবোধক না হলেও পারিভাষিক অর্থে ফারসী, উর্দু, হিন্দী ও বাংলা ভাষায় সিয়ামের জায়গায় ‘রোযা’ শব্দটি ব্যবহার হয় বলেই সাধারণ জনসাধারণের বুঝার সুবিধার্থে আমিও এই পুস্তিকায় রোযা শব্দই প্রয়োগ করেছি। আর এ ব্যবহারে শরীয়তগত কোন ক্ষতি নেই।\n\n" + 
                        "[1] (আশ্শারহুল মুমতে’ ৬/৩১০, তাযঃ ৯পৃঃ)\n\n" + 
                        "[2] (হাকেম, মুস্তাদ্রাক, বাইহাকী, ইবনে খুযাইমাহ, সহীহ, ইবনে হিববান, সহীহ, সহীহুল জামেইস সাগীর, আলবানী ৫৩৭৬নং)",
                "In the Arabic language, 'Sawm' (fasting) fundamentally denotes refraining and abstaining. For this reason, one who abstains from speaking—remaining silent and quiet—is called 'Sa\\'im'. Almighty Allah recounts the history of Maryam (peace be upon her):\n\n" + 
                        "'So if you see from mankind anyone, say: Indeed, I have vowed to the Most Merciful a fast, so I will not speak today to any human being.' (Quran 19:26)\n\n" + 
                        "Here, 'Sawm' specifically means abstaining from speech.\n\n" + 
                        "In the terminology of Islamic Shariah, 'Sawm' or 'Siyam' means worshipping Allah by abstaining from food, drink, sexual intimacy, and all invalidators of fasting from the break of dawn (Fajr) until sunset (Maghrib).[1]\n\n" + 
                        "This definition also inherently encompasses abstaining from idle talk, falsehood, and vulgarity. The beloved Prophet (peace and blessings be upon him) said: 'Fasting does not merely consist of abstaining from food and drink; rather, true fasting is refraining from vanity and obscenity. So if someone insults you or behaves foolishly toward you, say to him: Indeed, I am fasting, I am fasting.'[2]\n\n" + 
                        "Although the colloquial term 'Roza' is not linguistically identical in origin to 'Siyam', it is widely understood and applied in Persian, Urdu, Hindi, and Bengali as a synonym for fasting, and using it for clarity entails no legal hindrance under Shariah.\n\n" + 
                        "[1] (Ash-Sharhul Mumti' 6/310)\n[2] (Al-Hakim, Al-Mustadrak, Al-Bayhaqi, Ibn Khuzaymah, Sahih Ibn Hibban, Sahihul Jami' 5376)"
        ));
        // Card 2: পূর্ববর্তী ধর্মমতে রোযা
        list.add(new HajjHistoryCardItem(
                2,
                "পূর্ববর্তী ধর্মমতে রোযা",
                "Fasting in Previous Religions and Nations",
                "মানুষের জন্য রমাযানের রোযাই প্রথম রোযা নয়। কারণ, রোযা হল এমন ইবাদত, যা আল্লাহ তাআলা মানুষ সৃষ্টির পর থেকেই নিজের বান্দার জন্য ফরয করেছেন। তিনি কুরআন মাজীদে ব...",
                "Ramadan was not the first fast ordained for mankind; Allah ordained fasting for His servants throughout history, as stated in Surah Al-Baqarah 2:183...",
                "মানুষের জন্য রমাযানের রোযাই প্রথম রোযা নয়। কারণ, রোযা হল এমন ইবাদত, যা আল্লাহ তাআলা মানুষ সৃষ্টির পর থেকেই নিজের বান্দার জন্য ফরয করেছেন। তিনি কুরআন মাজীদে বলেন,\n\n" + 
                        "(يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِنْ قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ)\n\n" + 
                        "অর্থাৎ, হে ঈমানদারগণ! তোমাদের উপর রোযা ফরয করা হল, যেমন তোমাদের পূর্ববর্তী উম্মতের উপর ফরয করা হয়েছিল। যাতে তোমরা পরহেযগার হতে পার। (কুরআনুল কারীম ২/১৮৩)\n\n" + 
                        "এ থেকে বুঝা যায় যে, পূর্ববর্তী সকল উম্মতের জন্য রোযা ফরয ছিল। অবশ্য ইসলাম, খ্রিষ্টান ও ইয়াহুদী ধর্মের পূর্বের ধর্মাবলম্বী মানুষরা কিভাবে কোন্ পদ্ধতিতে রোযা পালন করত, তা নির্দিষ্টরূপে জানা যায় না। তবে ইয়াহুদ ও খ্রিষ্টানরা নির্দিষ্ট সময়ে নির্দিষ্ট জিনিস থেকে বিরত থেকে রোযা পালন করত। তাওরাত ও ইঞ্জীলের বর্তমান সংস্কারগুলো থেকেও জানা যায় যে, আল্লাহ তাআলা রোযাকে তাঁর পূর্ববর্তী বান্দাদের উপর ফরয করেছিলেন।\n\n" + 
                        "বুখারী-মুসলিমের একটি হাদীসে পাওয়া যায় যে, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) যখন মক্কা থেকে হিজরত করে মদ্বীনায় এলেন, তখন দেখলেন, ইয়াহুদীরা আশূরার দিনে রোযা পালন করছে। তিনি তাদেরকে জিজ্ঞাসা করলেন, ‘‘এটা কি এমন দিন যে, তোমরা এ দিনে রোযা রাখছ?’’ ইয়াহুদীরা বলল, ‘এ এক মহান দিন। এ দিনে আল্লাহ মূসা ও তাঁর কওমকে পরিত্রাণ দিয়েছিলেন এবং ফিরআঊন ও তার কওমকে পানিতে ডুবিয়ে ধ্বংস করেছিলেন। তাই মূসা এরই কৃতজ্ঞতা জ্ঞাপনের উদ্দেশ্যে এই দিনে রোযা পালন করেছিলেন। আর সেই জন্যই আমরাও এ দিনে রোযা রেখে থাকি।’\n\n" + 
                        "এ কথা শুনে মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বললেন, ‘‘মূসার স্মৃতি পালন করার ব্যাপারে তোমাদের চাইতে আমরা অধিক হকদার।’’ সুতরাং তিনি ঐ দিনে রোযা রাখলেন এবং সকলকে রোযা রাখতে আদেশ দিলেন।[1]\n\n" + 
                        "অবশ্য আহলে কিতাব (ইয়াহুদী-খ্রিষ্টান) ও মুসলিমদের রোযার মাঝে একটি পার্থক্য এই যে, আহলে কিতাবরা সেহরী ([2]) খায় না। কিন্তু মুসলিমরা খায়।[3]\n\n" + 
                        "তদনুরূপ আহলে কিতাবরা ইফতার করতে দেরী করে। পক্ষান্তরে মুসলিমরা সূর্য ডোবামাত্র তড়িঘড়ি ইফতার করে থাকে।[4]\n\n" + 
                        "ইনসাইক্লোপেডিয়া বৃটানিকাতে বলা হয়েছে যে, জল, বায়ু, জাতি, ধর্ম ও পারিপার্শ্বিকতা ভেদে রোজার নিয়ম-পদ্ধতি বিভিন্ন হইলেও এমন কোন ধর্মের নাম উল্লেখ করা কঠিন যাহার ধর্মীয় বিধানে রোযার আবশ্যকতা স্বীকার করা হয় নাই।[5]\n\n" + 
                        "যাই হোক না কেন, এই খবরের মাঝে মুসলিমদের জন্য রয়েছে সান্ত্বনা ও আশ্বাস। কারণ, শুধু উম্মতে মুহাম্মাদীর উপরেই নয়; বরং পূর্ববর্তী সকল উম্মতের উপরেই রোযা ফরয ছিল। আর সে মনে করেই মুসলিমদের উপরেও রোযার ভার অনেক হাল্কা হয়ে যায়। কারণ, মুসলিম যখন জানতে পারে যে, এই রোযা রাখার পথ হল পূর্ববর্তী আম্বিয়া ও তাঁদের অনুগামী নেক লোকদের, তখন সে এই পথ অবলম্বন করে আনন্দবোধ করে এবং রোযার কোন কষ্টকেই সে নিজের জন্য ভারী মনে করে না।[6]\n\n" + 
                        "[1] (বুখারী ২০০৪, মুসলিম ১১৩০নং)\n\n" + 
                        "[2] সেহরী কথাটি সাহারীর অপভ্রংশ। এটি شاذ في القياس وفصيح في الاستعمال । সেহরী শব্দটি উর্দু অভিধানে পাওয়া যায়, কিন্তু সাহারী শব্দটি কোন অভিধানে পাওয়া যায় না।\n\n" + 
                        "[3] (মুসলিম ১০৯৬নং)\n\n" + 
                        "[4] (আবূ দাঊদ, হাকেম, মুস্তাদ্রাক, ইবনে হিববান, সহীহ, সহীহুল জামেইস সাগীর, আলবানী ৭৬৮৯নং)\n\n" + 
                        "[5] (বাংলা মিশকাত, এমদাদিয়া লাইব্রেরী ছাপা ৪/২৬৪ দ্রঃ)\n\n" + 
                        "[6] (দুরুসু রামাযান অকাফাত লিস্-সায়েমীন ৫পৃঃ)",
                "Fasting in Ramadan was not the first fast ordained for mankind; fasting is a profound act of worship that Almighty Allah has mandated for His servants throughout generations. Allah states in the Holy Quran:\n\n" + 
                        "'O you who believe! Fasting is prescribed for you as it was prescribed for those before you, that you may attain Taqwa (righteousness and God-consciousness).' (Quran 2:183)\n\n" + 
                        "This establishes that fasting was obligatory upon all preceding nations. While the exact forms and regulations of earlier eras are not fully known, Jews and Christians observed fasts by abstaining from specific foods during designated periods. Preserved versions of the Torah and Gospel also affirm that Allah obligated fasting upon earlier communities.\n\n" + 
                        "In Sahih al-Bukhari and Sahih Muslim, it is recorded that when the Prophet (peace and blessings be upon him) migrated from Makkah to Madinah, he found the Jews observing the fast of the Day of Ashura. He asked them: 'What is this day on which you are fasting?' They replied: 'This is a monumental day on which Allah saved Musa (Moses) and his people, and drowned Pharaoh and his army. Musa fasted on this day in thanksgiving, and therefore we fast as well.'\n\n" + 
                        "Upon hearing this, the Prophet (peace and blessings be upon him) said: 'We have more right and closer connection to Musa than you.' Consequently, he fasted on that day and instructed everyone to fast.[1]\n\n" + 
                        "A key distinction between the fasting of the People of the Book and Muslims is that they do not partake in Sehri, whereas Muslims do.[3]\n\n" + 
                        "Furthermore, the People of the Book delay breaking their fast, whereas Muslims hasten to break the fast promptly upon sunset.[4]\n\n" + 
                        "The Encyclopaedia Britannica notes that while methods and practices of fasting have varied across climates, nations, and cultures, it is difficult to find any religion whose sacred laws did not acknowledge the vital necessity of fasting.[5]\n\n" + 
                        "This brings immense comfort to believers: knowing that fasting was walked by the noble Prophets and righteous predecessors makes its observance an honor, joy, and light upon the soul.[6]\n\n" + 
                        "[1] (Sahih Bukhari 2004, Sahih Muslim 1130)\n[2] Sehri is the popular linguistic form of Sahari.\n[3] (Sahih Muslim 1096)\n[4] (Sunan Abi Dawud, Al-Hakim, Sahihul Jami' 7689)\n[5] (Bengali Mishkat, Emdadia Library 4/264)\n[6] (Duroos Ramadan Waqafat Lis-Sa'imeen, p. 5)"
        ));
        // Card 3: ইসলামী শরীয়তে রোযা ও তার পর্যায়ক্রম
        list.add(new HajjHistoryCardItem(
                3,
                "ইসলামী শরীয়তে রোযা ও তার পর্যায়ক্রম",
                "Phases and Progression of Fasting in Islamic Shariah",
                "মহাবিজ্ঞান ও হিকমতময় মহান আল্লাহর একটি হিকমত ও অনুগ্রহ এই যে, তিনি বান্দার উপর যে আদেশ-নিষেধ আরোপ করেছেন তার মধ্যে বহু বিষয়কেই পর্যায় অনুক্রমে ধীরে ধীরে ফরয ...",
                "Through divine wisdom and mercy, Allah phased in the obligation of fasting gradually across four stages before establishing the complete Ramadan fast...",
                "মহাবিজ্ঞান ও হিকমতময় মহান আল্লাহর একটি হিকমত ও অনুগ্রহ এই যে, তিনি বান্দার উপর যে আদেশ-নিষেধ আরোপ করেছেন তার মধ্যে বহু বিষয়কেই পর্যায় অনুক্রমে ধীরে ধীরে ফরয অথবা হারাম করেছেন। অনুরূপ তাঁর এক ফরয হল সিয়াম বা রোযা। যা তিনি উম্মতে মুহাম্মাদীর উপর পর্যায়ক্রমে কিছু কিছু করে ফরয করেছেন। যেমনঃ-\n\n" + 
                        "প্রথম পর্যায়ঃ-\n\n" + 
                        "প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) প্রত্যেক মাসে ৩টি করে রোযা পালন করতেন। আর এ দেখে সাহাবাগণও (রাঃ) তাঁর অনুসরণে ঐ রোযা রাখতেন। যাতে করে রোযার অভ্যাস তাদের জন্য সহজ হয়ে ওঠে।\n\n" + 
                        "দ্বিতীয় পর্যায়ঃ-\n\n" + 
                        "কুরাইশদল জাহেলী যুগে আশূরার রোযা রাখত।[1] অতঃপর তিনি মক্কা থেকে হিজরত করে মদ্বীনায় এলে মূসা u-এর অনুকরণে তাঁর স্মৃতি পালন করে আশূরার দিনে খুব গুরুত্বের সাথে রোযা রাখলেন এবং সাহাবাদেরকেও এ রোযা রাখতে আদেশ করলেন। তখন এ রোযা রাখা ফরয ছিল।\n\n" + 
                        "তৃতীয় পর্যায়ঃ-\n\n" + 
                        "অতঃপর রোযার বিধান নিয়ে কুরআন কারীমের উপর্যুক্ত আয়াত অবতীর্ণ হল। কিন্তু শুরুতে তখনও রোযা পূর্ণ আকারে ফরয ছিল না। যার ইচ্ছা সে রোযা রাখত এবং যার ইচ্ছা সে না রেখে মিসকীনকে খাদ্য দান করত। তবে রোযা রাখাটাই আল্লাহর নিকট অধিক পছন্দনীয় ছিল। এ ব্যাপারে মহান আল্লাহর নির্দেশ ছিলঃ-\n\n" + 
                        "(وَعَلَى الَّذِينَ يُطِيقُونَهُ فِدْيَةٌ طَعَامُ مِسْكِينٍ فَمَنْ تَطَوَّعَ خَيْراً فَهُوَ خَيْرٌ لَهُ وَأَنْ تَصُومُوا خَيْرٌ لَكُمْ إِنْ كُنْتُمْ تَعْلَمُونَ)\n\n" + 
                        "অর্থাৎ, যারা রোযা রাখার সামর্থ্য থাকা সত্ত্বেও রোযা রাখতে চায় না, তারা এর পরিবর্তে একজন মিসকীনকে খাদ্য দান করবে। যে ব্যক্তি খুশীর সাথে সৎকর্ম করে, তা তার জন্য কল্যাণকর হয়। আর যদি তোমরা রোযা রাখ, তাহলে তা তোমাদের জন্য বিশেষ কল্যাণপ্রসূ; যদি তোমরা উপলব্ধি করতে পার। (কুরআনুল কারীম ২/১৮৪)\n\n" + 
                        "চতুর্থ পর্যায়ঃ-\n\n" + 
                        "অতঃপর সন ২ হিজরীর শা’বান মাসের ২য় তারীখ সোমবারে ([2]) প্রত্যেক সামর্থ্যবান ভারপ্রাপ্ত মুসলিমের পক্ষে পূর্ণ রমাযান ([3]) মাসের রোযা ফরয করা হল। এ ব্যাপারে মহান আল্লাহ বলেন,\n\n" + 
                        "(شَهْرُ رَمَضَانَ الَّذِي أُنْزِلَ فِيهِ الْقُرْآنُ هُدىً لِلنَّاسِ وَبَيِّنَاتٍ مِنَ الْهُدَى وَالْفُرْقَانِ فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ)\n\n" + 
                        "অর্থাৎ, রমাযান মাস; যে মাসে মানুষের দিশারী এবং সৎপথের স্পষ্ট নিদর্শন ও সত্যাসত্যের পার্থক্যকারীরূপে কুরআন অবতীর্ণ হয়েছে। অতএব তোমাদের মধ্যে যে কেউ এ মাস পাবে সে যেন এ মাসে রোযা রাখে। (কুরআনুল কারীম ২/১৮৫)\n\n" + 
                        "সুতরাং সামর্থ্যবান ভারপ্রাপ্ত (জ্ঞানসম্পন্ন সাবালক) গৃহবাসীর জন্য মিসকীনকে খাদ্যদানের বিধান রহিত হয়ে গেল এবং বৃদ্ধ ও চিররোগীর জন্য তা বহাল রাখা হল। অনুরূপ (কিছু উলামার মতে) এ বিধান গর্ভবতী ও দুগ্ধদাত্রী মহিলার জন্যও বহাল করা হল; যারা গর্ভকালে বা দুগ্ধদান কালে রোযা রাখলে তার সন্তানের বিশেষ ক্ষতি হবে বলে আশঙ্কা করে।\n\n" + 
                        "মুসলিমদের প্রতি আল্লাহর বিশেষ রহমত এই ছিল যে, রোযার বহু কষ্টভার তিনি লাঘব করে দিয়েছেন। যেমন; শুরুর দিকে এ রোযা ফরয ছিল এশার নামায বা রাত্রে ঘুমিয়ে যাওয়ার পর থেকে পর দিন সূর্যাস্ত পর্যন্ত। অর্থাৎ, রাত্রে একবার ঘুমিয়ে পড়লে পানাহার ও স্ত্রী-সহবাস হারাম হয়ে যেত। এতে মুসলিমরা বড় কষ্টবোধ করতে লাগলেন। সময় লম্বা থাকার কারণে তাঁরা বড় দুর্বল হয়ে পড়তেন। অতঃপর মহান আল্লাহর তরফ থেকে সে ভার হাল্কা করা হল। পরিশেষে ফজর উদয়কাল থেকে শুরু করে সূর্যাস্ত কাল পর্যন্ত হল রোযা রাখার সময়।\n\n" + 
                        "আনসার গোত্রের সিরমাহ নামক এক ব্যক্তি রোযা রাখা অবস্থায় সন্ধ্যা পর্যন্ত কাজ করতেন। একদিন তিনি বাড়ি ফিরে এসে এশার নামায পড়ে ঘুমিয়ে পড়লেন এবং কোন প্রকার পানাহার না করেই তাঁর ফজর হয়ে গেল। সুতরাং এ অবস্থাতেই পরদিন রোযা রাখলেন। আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাঁকে সেই কঠিন দুর্বল ও ক্লিষ্ট অবস্থায় দেখে জিজ্ঞাসা করলেন, ‘‘কি ব্যাপার! আমি তোমাকে বড় দুর্বল দেখছি যে?’’ তিনি বললেন, ‘হে আল্লাহর রসূল! আমি গতকাল কাজ করার পর যখন এলাম তখন এলাম। তারপর (পরিশ্রান্ত হয়ে) শুয়ে পড়তেই ঘুমিয়ে গেলাম। তারপর ফজর হয়ে গেলে আবার রোযা রেখে নিলাম।’[4]\n\n" + 
                        "উমার (রাঃ) এক রাত্রে ঘুমিয়ে যাওয়ার পর উঠে স্ত্রী-মিলন করে ফেললেন। তিনি মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর নিকট এসে ঘটনা উল্লেখ করলেন। অতঃপর এই সবের পরিপ্রেক্ষীতে আল্লাহর বিধান অবতীর্ণ হল,\n\n" + 
                        "(أُحِلَّ لَكُمْ لَيْلَةَ الصِّيَامِ الرَّفَثُ إِلَى نِسَائِكُمْ هُنَّ لِبَاسٌ لَكُمْ وَأَنْتُمْ لِبَاسٌ لَهُنَّ عَلِمَ اللهُ أَنَّكُمْ كُنْتُمْ تَخْتَانُونَ أَنْفُسَكُمْ فَتَابَ عَلَيْكُمْ وَعَفَا عَنْكُمْ فَالْآنَ بَاشِرُوهُنَّ وَابْتَغُوا مَا كَتَبَ اللهُ لَكُمْ وَكُلُوا وَاشْرَبُوا حَتَّى يَتَبَيَّنَ لَكُمُ الْخَيْطُ الْأَبْيَضُ مِنَ الْخَيْطِ الْأَسْوَدِ مِنَ الْفَجْرِ ثُمَّ أَتِمُّوا الصِّيَامَ إِلَى اللَّيْلِ )\n\n" + 
                        "অর্থাৎ, রোযার রাতে তোমাদের জন্য স্ত্রী-সম্ভোগ হালাল করা হয়েছে। তারা তোমাদের পোষাক এবং তোমরা তাদের পোষাক। আল্লাহ জানতেন যে, তোমরা আত্ম-প্রতারণা করছ। তাই তো তিনি তোমাদের প্রতি সদয় হয়েছেন এবং তোমাদের অপরাধ ক্ষমা করে দিয়েছেন। অতএব এখন তোমরা তোমাদের স্ত্রীদের সঙ্গে সহবাস করতে পার এবং আল্লাহ তোমাদের জন্য যা (সন্তান, শবেকদর, সকল বৈধ বস্ত্ত বা আল্লাহর তরফ থেকে কোন কিছুর ব্যাপারে অব্যাহতি) লিখে রেখেছেন তা কামনা কর। আর তোমরা পানাহার কর; যতক্ষণ রাত্রির কালো রেখা হতে ফজরের সাদা রেখা স্পষ্টরূপে তোমাদের নিকট প্রকাশ না পায়। অতঃপর রাত পর্যন্ত রোযা পূর্ণ কর।(কুরআনুল কারীম ২/১৮৭) [5]\n\n" + 
                        "[1] (বুখারী ১৮৯৩, মুসলিম ১১২৫)\n[2] ফিকহুস সুন্নাহ ১/৩৮৩ দ্রঃ\n[3] রমাযান কথাটি আরবী রামাযান-এর অপভ্রংশ ও বাংলায় চলিত রূপ।\n[4] (বুখারী ১৯১৫, সহীহ আবূ দাঊদ ২০২৯নং)\n[5] (সহীহ আবূ দাঊদ ২০২৮নং, তাফসীর ইবনে কাসীর ১/২৯১, আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ২৩-২৫পৃঃ দ্রঃ)",
                "From the infinite wisdom and mercy of Almighty Allah, many commandments and prohibitions were revealed gradually to ease implementation upon His servants. Among these was the obligation of fasting, phased in across distinct stages:\n\n" + 
                        "First Stage:\nThe Prophet (peace and blessings be upon him) regularly observed three days of fasting every month, and the noble Companions followed his practice to cultivate the habit of fasting.\n\n" + 
                        "Second Stage:\nThe Quraysh fasted on Ashura during Jahiliyyah.[1] When the Prophet (peace be upon him) migrated to Madinah, he observed Ashura in honor of Musa (peace be upon him) and commanded the Muslims to fast, making it an obligatory fast at that time.\n\n" + 
                        "Third Stage:\nWhen the verse of Surah Al-Baqarah was revealed, fasting was not yet an absolute individual obligation for all capable persons; rather, a person could choose either to fast or to feed a poor person instead, though fasting was declared far superior:\n\n" + 
                        "'And upon those who are able [to fast, but only with hardship] - a ransom [as substitute] of feeding a poor person. And whoever does good spontaneously, it is better for him. But to fast is best for you, if you only knew.' (Quran 2:184)\n\n" + 
                        "Fourth Stage:\nOn Monday, the 2nd of Sha'ban in the 2nd year of Hijrah, Allah obligated the entire month of Ramadan upon every sane, adult, capable Muslim:\n\n" + 
                        "'The month of Ramadan [is that] in which was revealed the Quran, a guidance for the people and clear proofs of guidance and criterion. So whoever sights [the new moon of] the month, let him fast it.' (Quran 2:185)\n\n" + 
                        "Thus the dispensation of paying a ransom (Fidyah) was abrogated for healthy residents, while remaining valid for the elderly and chronically ill, as well as pregnant and nursing mothers fearing harm for their children.\n\n" + 
                        "Moreover, Allah alleviated hardships: initially, if a person slept after Isha, eating, drinking, and marital intimacy became forbidden until the next sunset. When companions like Sirmah (RA) grew severely exhausted, and Umar (RA) faced distress, Allah revealed divine ease:\n\n" + 
                        "'It has been made lawful for you the night preceding fasting to go to your wives [for sexual relations]. They are clothing for you and you are clothing for them... And eat and drink until the white thread of dawn becomes distinct to you from the black thread [of night]. Then complete the fast until the sunset.' (Quran 2:187)[5]\n\n" + 
                        "[1] (Sahih Bukhari 1893, Sahih Muslim 1125)\n[2] (Fiqhus Sunnah 1/383)\n[3] Ramadan linguistic note.\n[4] (Sahih Bukhari 1915, Sahih Abi Dawud 2029)\n[5] (Sahih Abi Dawud 2028, Tafsir Ibn Kathir 1/291)"
        ));
        // Card 4: সাধারণভাবে রোযার ফযীলত
        list.add(new HajjHistoryCardItem(
                4,
                "সাধারণভাবে রোযার ফযীলত",
                "General Virtues of Fasting in Prophetic Traditions",
                "সাধারণভাবে রোযার ফযীলত বর্ণনা করে বহু হাদীস বর্ণিত হয়েছে। নিম্নে কতিপয় সহীহ হাদীস প্রণিধানযোগ্যঃ- ১। আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল...",
                "Numerous authentic Hadiths elucidate the immense virtues of fasting: a divine personal reward from Allah, an impenetrable shield against Hellfire, and the gate of Ar-Rayyan...",
                "সাধারণভাবে রোযার ফযীলত বর্ণনা করে বহু হাদীস বর্ণিত হয়েছে। নিম্নে কতিপয় সহীহ হাদীস প্রণিধানযোগ্যঃ-\n\n" + 
                        "১। আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘আল্লাহ আয্যা অজাল্ল বলেন, ‘আদম সন্তানের প্রত্যেক আমল তার নিজের জন্য; তবে রোযা নয়, যেহেতু তা আমারই জন্য এবং আমি নিজেই তার প্রতিদান দেব।’ রোযা ঢাল স্বরূপ। সুতরাং তোমাদের কারো রোযার দিন হলে সে যেন অশ্লীল না বকে ও ঝগড়া-হৈচৈ না করে; পরন্তু যদি তাকে কেউ গালাগালি করে অথবা তার সাথে লড়তে চায়, তবে সে যেন বলে, ‘আমি রোযা রেখেছি, আমার রোযা আছে।’ সেই সত্তার শপথ যাঁর হাতে মুহাম্মদের প্রাণ আছে! নিশ্চয়ই রোযাদারের মুখের দুর্গন্ধ আল্লাহর নিকট কস্ত্তরীর সুবাস অপেক্ষা অধিকতর সুগন্ধময়। রোযাদারের জন্য রয়েছে দু’টি খুশী, যা সে লাভ করে; যখন সে ইফতার করে তখন ইফতারী নিয়ে খুশী হয়। আর যখন সে তার প্রতিপালকের সাথে সাক্ষাৎ করবে তখন তার রোযা নিয়ে খুশী হবে।[1]\n\n" + 
                        "২। হুযাইফা (রাঃ) বলেন, আমি নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-কে বলতে শুনেছি যে, ‘‘মানুষের পরিবার, ধন-সম্পদ ও প্রতিবেশীর ব্যাপারে ঘটিত বিভিন্ন ফিতনা ও গোনাহর কাফ্ফারা হল নামায, রোযা ও সদকাহ।’’[2]\n\n" + 
                        "৩। সাহ্ল বিন সা’দ (রাঃ) হতে বর্ণিত, নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, জান্নাতের এক প্রবেশদ্বার রয়েছে, যার নাম ‘রাইয়ান।’ কিয়ামতের দিন ঐ দ্বার দিয়ে রোযাদারগণ প্রবেশ করবে। তারা ছাড়া তাদের সাথে আর কেউই ঐ দ্বার দিয়ে প্রবেশ করবে না। বলা হবে, ‘কোথায় রোযাদারগণ?’ সুতরাং তারা ঐ দরজা দিয়ে (জান্নাতে) প্রবেশ করবে। অতঃপর যখন তাদের সর্বশেষ ব্যক্তি প্রবেশ করবে, তখন সে দ্বার রুদ্ধ করা হবে। ফলে সে দ্বার দিয়ে আর কেউই প্রবেশ করতে পারবে না।’’[3]\n\n" + 
                        "৪। আবু হুরাইরা (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘--- আর যে ব্যক্তি রোযা রাখায় অভ্যাসী হবে, তাকে (কিয়ামতের দিন) ‘রাইয়ান’ দুয়ার হতে (জান্নাতের দিকে) আহবান করা হবে। ---’’[4]\n\n" + 
                        "৫। আবু সাঈদ (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘যে বান্দা আল্লাহর রাস্তায় একদিন মাত্র রোযা রাখবে সেই বান্দাকে আল্লাহ ঐ রোযার বিনিময়ে জাহান্নাম থেকে ৭০ বছরের পথ পরিমাণ দূরত্বে রাখবেন।’’[5]\n\n" + 
                        "৬। আম্র বিন আবাসাহ (রাঃ) কর্তৃক বর্ণিত, তিনি বলেন, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, ‘‘যে ব্যক্তি আল্লাহর রাস্তায় একদিন মাত্র রোযা রাখবে সেই ব্যক্তি থেকে জাহান্নাম ১০০ বছরের পথ পরিমাণ দূরে সরে যাবে।’’[6]\n\n" + 
                        "৭। আবু উমামাহ (রাঃ) হতে বর্ণিত, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি আল্লাহর পথে একটি রোযা রাখবে, আল্লাহ সেই ব্যক্তি ও দোযখের মাঝে একটি এমন প্রতিরক্ষার খাদ তৈরী করে দেবেন; যা আকাশ ও পৃথিবীর মধ্যবর্তী জায়গা সমপরিমাণ চওড়া।’’[7]\n\n" + 
                        "৮। উসমান বিন আবূল আস কর্তৃক বর্ণিত, মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘রোযা হল দোযখ থেকে বাঁচার জন্য ঢালস্বরূপ; যেমন যুদ্ধের সময় নিজেকে রক্ষা করার জন্য তোমাদের ঢাল হয়ে থাকে।’’[8]\n\n" + 
                        "৯। আবূ হুরাইরা (রাঃ) কর্তৃক বর্ণিত, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘রোযা হল জাহান্নাম থেকে রক্ষার জন্য ঢাল ও দুর্ভেদ্য দুর্গস্বরূপ।’’[9]\n\n" + 
                        "১০। আব্দুল্লাহ বিন আম্র (রাঃ) কর্তৃক বর্ণিত, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘কিয়ামতের দিন রোযা এবং কুরআন বান্দার জন্য সুপারিশ করবে। রোযা বলবে, ‘হে আমার প্রতিপালক! আমি ওকে পানাহার ও যৌনকর্ম থেকে বিরত রেখেছিলাম। সুতরাং ওর ব্যাপারে আমার সুপারিশ গ্রহণ কর।’ আর কুরআন বলবে, ‘আমি ওকে রাত্রে নিদ্রা থেকে বিরত রেখেছিলাম। সুতরাং ওর ব্যাপারে আমার সুপারিশ গ্রহণ কর।’ নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘অতএব ওদের উভয়ের সুপারিশ গৃহীত হবে।’’[10]\n\n" + 
                        "১১। আবু উমামাহ (রাঃ) হতে বর্ণিত, তিনি বলেন, আমি বললাম, ‘হে আল্লাহর রসূল! আমাকে এমন কোন আমলের আজ্ঞা করুন; যদদ্বারা আল্লাহ আমাকে লাভবান করবেন।’ (অন্য এক বর্ণনায় আছে, ‘যার মাধ্যমে আমি জান্নাত যেতে পারব।’) তিনি বললেন, তুমি রোযা রাখ, কারণ এর সমতুল কিছু নেই।’ পুনরায় আমি বললাম, হে আল্লাহর রসূল! আমাকে কোন আমলের আদেশ করুন।’ তিনিও পুনঃ ঐ কথাই বললেন, ‘‘তুমি রোযা রাখ, কারণ এর সমতুল কিছু নেই।’[11]\n\n" + 
                        "১২। হুযাইফা (রাঃ) হতে বর্ণিত, তিনি বলেন, নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) আমার বুকে হেলান দিয়ে ছিলেন। সেই সময় তিনি বললেন, ‘‘লা ইলাহা ইল্লাল্লাহ’ বলার পর যে ব্যক্তির জীবনের পরিসমাপ্তি ঘটবে সে জান্নাতে প্রবেশ করবে। আল্লাহর সন্তুষ্টিলাভের উদ্দেশ্যে একদিন রোযা রাখার পর যে ব্যক্তির জীবনের পরিসমাপ্তি ঘটবে সে জান্নাতে প্রবেশ করবে। আর আল্লাহর সন্তুষ্টিলাভের আশায় কিছু সাদকাহ করার পর যে ব্যক্তির জীবনের পরিসমাপ্তি ঘটবে সেও জান্নাত প্রবেশ করবে।’’[12]\n\n" + 
                        "[1] (বুখারী ১৯০৪, মুসলিম ১১৫১ নং)\n\n" + 
                        "[2] (বুখারী ১৮৯৫, মুসলিম ১৪৪নং)\n\n" + 
                        "[3] (বুখারী১৮৯৬ নং, মুসলিম ১১৫২ নং, নাসাঈ, তিরমিযী)\n\n" + 
                        "[4] (বুখারী ১৮৯৭, মুসলিম ১০২৭নং)\n\n" + 
                        "[5] (বুখারী ২৮৪০ নং, মুসলিম ১১৫৩ নং , তিরমিযী, নাসাঈ)\n\n" + 
                        "[6] (নাসাঈ, সহীহুল জামেইস সাগীর, আলবানী ৬৩৩০নং উকবাহ হতে,ত্বাবারানী কাবীর ও আওসাত্ব, সহীহ তারগীবঃ৯৭৫ নং)\n\n" + 
                        "[7] (তিরমিযী, সহীহুল জামেইস সাগীর, আলবানী ৬৩৩৩নং)\n\n" + 
                        "[8] (আহমাদ, মুসনাদ, নাসাঈ, ইবনে মাজাহ, সহীহুল জামেইস সাগীর, আলবানী ৩৮৭৯নং)\n\n" + 
                        "[9] (আহমাদ, মুসনাদ, বাইহাকী শুআবুল ঈমান, সহীহুল জামেইস সাগীর, আলবানী ৩৮৮০নং)\n\n" + 
                        "[10] (আহমাদ, মুসনাদ, ত্বাবারানী, মু’জাম কাবীর, হাকেম, মুস্তাদ্রাক, ইবনে আবিদ্দুনয়্যার ‘কিতাবুল জু’, সহীহ তারগীব, আলবানী ৯৬৯ নং)\n\n" + 
                        "[11] (নাসাঈ, ইবনে খুযাইমাহ, সহীহ, হাকেম, মুস্তাদ্রাক, সহীহ তারগীব, আলবানী ৯৭৩ নং)\n\n" + 
                        "[12] (আহমাদ, মুসনাদ, সহীহ তারগীব, আলবানী ৯৭২ নং)",
                "Numerous authentic traditions record the sublime virtues and immense rewards of fasting:\n\n" + 
                        "1. Abu Hurairah (may Allah be pleased with him) reported that the Messenger of Allah (peace be upon him) said: 'Allah, the Exalted and Majestic, said: Every deed of the son of Adam is for himself, except for fasting; for it is for Me, and I Myself will give reward for it.' Fasting is a shield; on the day of fasting, one should neither utter obscenities nor raise voice in dispute. If someone abuses or fights him, let him say: 'I am fasting, I am fasting.' By Him in Whose Hand is the soul of Muhammad, the breath of a fasting person is sweeter to Allah than the fragrance of musk. The fasting person experiences two joys: joy when breaking fast, and joy when meeting his Lord.[1]\n\n" + 
                        "2. Hudhayfah (may Allah be pleased with him) reported that the Prophet (peace be upon him) said: 'The trials of a man regarding his family, wealth, and neighbor are expiated by prayer, fasting, and charity.'[2]\n\n" + 
                        "3. Sahl bin Sa'd (may Allah be pleased with him) reported that the Prophet (peace be upon him) said: 'In Paradise there is a gate called Ar-Rayyan, through which only those who fast will enter on the Day of Resurrection, and none will enter through it besides them... Once they enter, it will be closed and none else will enter.'[3]\n\n" + 
                        "4. Abu Hurairah (may Allah be pleased with him) reported that the Messenger of Allah (peace be upon him) said: 'Whoever was habitual in fasting will be called from the gate of Ar-Rayyan.'[4]\n\n" + 
                        "5. Abu Sa'eed (may Allah be pleased with him) reported that the Messenger of Allah (peace be upon him) said: 'Whoever fasts a day in the cause of Allah, Allah will put between him and the Fire a trench seventy years of journey in distance.'[5]\n\n" + 
                        "6. Amr bin Abasah (may Allah be pleased with him) reported that the Messenger of Allah (peace be upon him) said: 'Whoever fasts a single day in the way of Allah, Hellfire will move away from him a distance of 100 years.'[6]\n\n" + 
                        "7. Abu Umamah (may Allah be pleased with him) reported that the Prophet (peace be upon him) said: 'Whoever fasts a day in the path of Allah, Allah creates between him and the Fire a protective trench as vast as the distance between heaven and earth.'[7]\n\n" + 
                        "8. Uthman bin Abul Aas reported that the Prophet (peace be upon him) said: 'Fasting is a shield from the Fire, just as the shield of one of you protects in battle.'[8]\n\n" + 
                        "9. Abu Hurairah (may Allah be pleased with him) reported: 'Fasting is a shield and an impenetrable fortress against the Fire.'[9]\n\n" + 
                        "10. Abdullah bin Amr (may Allah be pleased with him) reported that the Prophet (peace be upon him) said: 'Fasting and the Quran will intercede for the servant on the Day of Resurrection... And their intercession will be accepted.'[10]\n\n" + 
                        "11. Abu Umamah (may Allah be pleased with him) said: 'O Messenger of Allah, command me to an action by which Allah will benefit me.' He replied: 'Take to fasting, for there is nothing equal to it.'[11]\n\n" + 
                        "12. Hudhayfah (may Allah be pleased with him) reported that the Prophet (peace be upon him) said: 'Whoever concludes his life with La ilaha illallah enters Paradise; whoever concludes his life with a day of fasting for Allah's pleasure enters Paradise; and whoever concludes his life giving charity for Allah's sake enters Paradise.'[12]\n\n" + 
                        "References: [1] Bukhari 1904, Muslim 1151; [2] Bukhari 1895, Muslim 144; [3] Bukhari 1896, Muslim 1152; [4] Bukhari 1897; [5] Bukhari 2840, Muslim 1153; [6] Sunan an-Nasa'i, Sahihul Jami' 6330; [7] Sunan at-Tirmidhi 6333; [8] Musnad Ahmad, Nasa'i 3879; [9] Musnad Ahmad 3880; [10] Musnad Ahmad, Hakim 969; [11] Sunan an-Nasa'i 973; [12] Musnad Ahmad 972."
        ));
        // Card 5: রোযার উদ্দেশ্য, লক্ষ্য, উপকারিতা ও যৌক্তিকতা
        list.add(new HajjHistoryCardItem(
                5,
                "রোযার উদ্দেশ্য, লক্ষ্য, উপকারিতা ও যৌক্তিকতা",
                "Objectives, Spiritual Goals, Benefits and Divine Wisdom of Fasting",
                "মহান আল্লাহর ৯৯ এর অধিক সুন্দর নামাবলীর অন্যতম নাম হল ‘আল-হাকীম।’ ‘আল-হাকীম’ অর্থ হিকমত-ওয়ালা, বিজ্ঞানময়, প্রজ্ঞাময়। আর হিকমত ও প্রজ্ঞা হল সর্বকর্ম যথাযোগ্যভ...",
                "Among Allah's 99 sublime names is Al-Hakeem (The All-Wise). Fasting encompasses boundless wisdom, spiritual purification, social unity, and medical vitality...",
                "মহান আল্লাহর ৯৯ এর অধিক সুন্দর নামাবলীর অন্যতম নাম হল ‘আল-হাকীম।’ ‘আল-হাকীম’ অর্থ হিকমত-ওয়ালা, বিজ্ঞানময়, প্রজ্ঞাময়। আর হিকমত ও প্রজ্ঞা হল সর্বকর্ম যথাযোগ্যভাবে নৈপুণ্যের সাথে সম্পাদন করা। মহান আল্লাহর এই নামের দাবী এই যে, তিনি যা কিছু সৃষ্টি করেছেন অথবা মানুষের জন্য বিধিবদ্ধ করেছেন তার প্রত্যেকটার পশ্চাতে আছে পরিপূর্ণ যুক্তি ও হিকমত; তা কেউ বুঝতে সক্ষম হোক অথবা অক্ষম।\n\n" + 
                        "যে রোযা আল্লাহ তাআলা বান্দার উপর ফরয ও বিধিবদ্ধ করেছেন তার মাঝে রয়েছে অভাবনীয় যৌক্তিকতা ও অচিন্তনীয় উপকারিতা। যেমনঃ-\n\n" + 
                        "১। রোযা হল এক এমন ইবাদত, যার মাধ্যমে বান্দা প্রভুর নৈকট্যলাভ করতে সক্ষম হয়। এতে সে প্রকৃতিগতভাবে যে জিনিস ভালোবাসে তা বর্জন করে; বর্জন করে সকল প্রকার পানাহার ও যৌনক্রিয়া। আর এর মাধ্যমে সে নিজ প্রতিপালকের সন্তুষ্টি কামনা করে। আশা করে পরকালের সাফল্য ও বেহেশতলাভ। এতে এই কথাই স্পষ্ট হয় যে, সে নিজের প্রিয় বস্ত্তর উপর প্রভুর প্রিয় বস্ত্তকে প্রাধান্য দেয় এবং ইহকালের জীবনের উপর পরকালের জীবনকেই শ্রেষ্ঠত্ব দেয়।\n\n" + 
                        "২। রোযাদার যথানিয়মে রোযা পালন করলে রোযা তাকে মুত্তাকী ও পরহেযগার বানাতে সহায়ক হয়। তার জীবন পথে তাকওয়া ও পরহেযগারীর আলো বিচ্ছুরিত হয়। মহান আল্লাহ বলেন, ‘‘হে ঈমানদারগণ! তোমাদের উপর রোযা ফরয করা হল, যেমন তোমাদের পূর্ববর্তী উম্মতের উপর ফরয করা হয়েছিল। যাতে তোমরা পরহেযগার হতে পার।’’ (কুরআনুল কারীম ২/১৮৩)\n\n" + 
                        "সুতরাং রোযাদার রোযা রেখে তার জীবনের প্রত্যেক চিন্তা, কথা ও কর্মে ‘তাকওয়া’ আনবে -এটাই বাঞ্ছিত। আর ‘তাকওয়া’ হল সেই আল্লাহ-ভীতির নাম, যার মাধ্যমে বান্দা তাঁর সকল আদেশ যথাসাধ্য পালন করে চলবে এবং যাবতীয় নিষিদ্ধ কর্ম থেকে সুদূরে থাকবে। বলা বাহুল্য, এটাই হল রোযার মহান উদ্দেশ্য ও প্রধান লক্ষ্য। পানাহার ও যৌনক্রিয়া নিষিদ্ধকরণের মাধ্যমে মানুষকে বৃথা কষ্ট দেওয়া রোযার উদ্দেশ্য নয়। প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি রোযা রেখে মিথ্যা কথা ও তার উপর আমল ত্যাগ করতে পারল না, সে ব্যক্তির পানাহার ত্যাগ করার মাঝে আল্লাহর কোন প্রয়োজন নেই।’’[1]\n\n" + 
                        "৩। রোযা আত্মাকে তরবিয়ত দান করে, চরিত্রকে সভ্য ও আদর্শভিত্তিক করে গড়ে তোলে এবং রোযাদারের আচরণে উৎকৃষ্টতার স্থায়িত্ব আনয়ন করে। মুসলিমের সবভাব-প্রকৃতিতে রোযা গভীরভাবে প্রভাব বিস্তার করে। রোযার সংশোধনী বার্তা তার হৃদয়-মনে তাসীর রেখে যায়। রোযাদারের অন্তরে এমন জাগরণ সৃষ্টি করে এবং তার মনের দুয়ারে এমন অতন্দ্র প্রহরী খাড়া করে দেয় যে, সে নিজের আত্মাকে নিয়ন্ত্রণে রাখতে সক্ষম হয় এবং এই প্রহরীর চোখে ফাঁকি দিয়ে কোনও নৈতিকতা-বিরোধী কর্ম করতে ইচ্ছা ও চেষ্টাও করতে পারে না।\n\n" + 
                        "এটা কি করে হতে পারে যে, রোযাদার তার প্রতিপালকের নিকট সত্যবাদিতার পরিচয় দেবে, অথচ মানুষের সঙ্গে মিথ্যা বলবে? নিজের রোযায় আন্তরিকতা রাখবে, অথচ নিজ সমাজের সঙ্গে ধোকাবাজী ও কপটতা প্রদর্শন করবে? ইখলাস ও আন্তরিকতা একটি সামগ্রিক বস্ত্ত; যা ভাগাভাগি হয় না। যার সর্বোচ্চ পর্যায় ও সারাংশ হল সৃষ্টিকর্তা অন্তর্যামী আল্লাহর সাথে আন্তরিকতা ও বিশুদ্ধচিত্ততা। সুতরাং যে ব্যক্তির আল্লাহর সাথে আন্তরিকতা থাকবে, সে ব্যক্তির ক্ষেত্রে অসম্ভব যে, সে মানুষকে ধোকা দেবে, আমানতে খেয়ানত করবে, অপরকে ঠকিয়ে খাবে, চুরি করবে, যুলম করবে অথবা অপরকে কষ্ট দেবে। পক্ষান্তরে যদি কারো চক্রান্তে পড়ে বা ভুলক্রমে এ ধরনের কোন পাপ করেই বসে, তাহলে সাথে সাথে সে সুপথে ফিরে আসে, আল্লাহর নিকট তওবা করে, অনুতপ্ত হয়, লজ্জিত হয় সীমাহীন।\n\n" + 
                        "সুতরাং রোযা হল একটি সুদৃঢ় ভিত্তির উপর সুচরিত্র গঠনের উপকরণ এবং তা সমৃদ্ধকরণের জন্য আভ্যন্তরীণ এক মৌলিক উপাদান। আর বিদিত যে, বাহ্যিক সৌন্দর্যের বাহার কোন মূল্য রাখে না; যদি না অভ্যন্তর সুদৃঢ় ও মজবুত হয়। তাই রোযাদারের জীবনে তার আখলাক-চরিত্র স্থায়িত্ব, স্থিতিশীলতা, বর্ধনশীলতা ও শ্রীবৃদ্ধিশীলতার গুণাবলী গ্রহণ করে থাকে। কারণ, তার সকল আচরণ ভিতর ও বাইরে থেকে নিয়ন্ত্রিত ও সুরক্ষিত হয়ে যায়।[2]\n\n" + 
                        "৪। রোযা রোযাদারের আচার-ব্যবহারকে সুন্দর করার কাজে বড় সক্রিয় ভূমিকা পালন করে থাকে। পূর্ণ একটি মাস ধরে তাকে পাপ থেকে দূরে রাখে, নিষিদ্ধ ও হারাম বস্ত্ত থেকে নিরাপদে রাখে। বরং রোযা তাকে এক মহান ইবাদতে মশ্গুল রাখে, হীনতা ও নীচতা হতে রক্ষা করে, প্রত্যেক নোংরামীর বিরুদ্ধে সংগ্রাম করে। সুতরাং সে না চুগলী করে, না গীবত। না মিথ্যা বলে, না অশ্লীল। না ফিতনা সৃষ্টি করে, না ফাসাদ। না অসার বকে, না ফালতু। কোন প্রকারের পাপাচরণ তার দ্বারা সংঘটিত হয় না। ফলে প্রকৃত রোযাদার রোযার পরেও একটি নিষ্পাপ ও পবিত্র মানুষের মত যাবতীয় সচ্চরিত্রতার অলঙ্কারে ভূষিত হয়ে সুখময় জীবন-যাপন করতে পারে।[3]\n\n" + 
                        "৫। রোযা মন ও প্রবৃত্তিকে দমন ও নিয়ন্ত্রণ করার অনুশীলন দেয়। জিতেন্দ্রিয় ও সংযমী হতে উদ্বুদ্ধ করে। ফলে রোযাদার তার মন ও প্রবৃত্তিকে সেই কাজে ব্যবহার করতে পারে; যাতে ইহ-পারলৌকিক সকল প্রকার মঙ্গল ও কল্যাণ নিহিত আছে। আর এমন আচরণ ও কর্ম থেকে তাকে দূরে রাখে; যাতে সে একটি ইন্দ্রিয়সেবী ও পাশবিক গুণসম্পন্ন মানুষ বলে পরিচিত হতে পারে; যেখানে সে কামনা-বাসনা ও লালসার প্রবণতা থেকে তাকে রুখতে সক্ষম হয় না।\n\n" + 
                        "সুতরাং রোযা সেই মন্দপ্রবণ আত্মার বিরুদ্ধে লড়ায়ে বিজয়ী হতে মুসলিমকে সার্বিক সহযোগিতা করে, যে আত্মা সর্বদা হারাম কাজে লিপ্ত হতে চায়, অবৈধভাবে কাম-লালসা চরিতার্থ করতে চায়। রোযা রোযাদারের ইচ্ছাশক্তিকে সর্বপ্রকার পাপ ও কুপ্রবৃত্তির স্পর্শ থেকে দূরে থাকার ‘ট্রেনিং’ দেয়। রোযার মাঝে রয়েছে আত্মসংযম এবং কুপ্রবৃত্তির দমন।\n\n" + 
                        "আধুনিক যুগের মানুষ অধিকাংশে নিজ কামনা-বাসনার কাছে বড় দুর্বল, কুপ্রবৃত্তি ও মন্দ-প্রবণ খেয়ালখুশীর বশীভূত। আর মনকে সবল ও সুদৃঢ় করতে রোযা ছাড়া আর অন্য কোন উপায়-উপকরণ নেই। কারণ, রোযাদার অত্যন্ত ক্ষুৎ-পিপাসায় কাতর থাকা সত্ত্বেও পানাহার বর্জন করে থাকে। আর নিঃসন্দেহে এ কাজে আত্মবিশ্বাস ও আত্মনিয়ন্ত্রণ-ক্ষমতা সৃষ্টি হয় এবং সর্বকাজে মনোবল প্রবল ও সুদৃঢ় হয়।\n\n" + 
                        "৬। রোযা রোযাদারকে কুঅভ্যাসের দাসত্ব থেকে মুক্তিদান করে। এমন বহু মানুষ আছে, যারা এমন বহু নোংরা অভ্যাসে অভ্যাসী হয়ে পড়ে এবং তার ফাঁদ থেকে বের হওয়ার কোন পথ খুঁজে পায় না। কিন্তু রোযা এলে তাদেরকে দেখা যায় যে, তারা তাদের সে সমস্ত কুঅভ্যাসকে পরিপূর্ণরূপে বর্জন করে ফেলেছে।\n\n" + 
                        "বলা বাহুল্য, এটাই হয় তাদের জন্য সুবর্ণ-সুযোগ; যার মাঝে তাদের সেই সকল মন্দ অভ্যাসের পঞ্জা থেকে নিজেদেরকে সহজ উপায়ে স্বাধীন করে নিতে পারে, যে সকল অভ্যাস তাদের মানসিক দুশ্চিন্তা ও ব্যাধির একমাত্র কারণ।[4]\n\n" + 
                        "অতএব সেই সকল রোযাদারগণ যারা ধুমপানে অভ্যাসী; যাদের অবৈধ বিড়ি-সিগারেট বিনা ৩০ মিনিটও অতিবাহত হয় না, অথবা তা পান না পর্যন্ত পায়খানাও হয় না, যাদের দৈনিক ১ প্যাকেট সিগারেট পানে তাদের ৫০ বছর জীবনে প্রায় ১ লাখ ৮২ হাজার ৫০০ টাকা এবং ১৫২০৮ ঘ¦টা ২০ মিনিট সময় অপচয় হয়, তাদের উচিৎ, রোযার এই পবিত্র অবসরে এই শ্রেণীর ‘বিষপান’ চিরদিনের জন্য পরিত্যাগ করা। কারণ, এ ‘সুখটান’ এমন ‘অগ্নিবাণ’ যে, তা মানুষের সুসবাস্থ্য, দেহ, অর্থ, দ্বীন, দুনিয়া ও আখেরাতের জন্য বড় ক্ষতিকর। যে মানুষ ১২/১৩ ঘ¦টা আল্লাহর ওয়াস্তে তা বর্জন করে থাকতে পারে, সে মানুষ আল্লাহরই ভয়ে বাকী সময় পান না করলেও থাকতে পারবে। আর যে ব্যক্তি আল্লাহর ওয়াস্তে কোন জিনিস বর্জন করবে, সে ব্যক্তি আল্লাহর ইচ্ছায় তার চাইতে উত্তম জিনিস অর্জন করবে। এটাই হল আল্লাহর রীতি। পরন্তু এ কোনক্রমেই উচিৎ নয় যে, রোযাদার সারাদিন হালাল জিনিস না খেয়ে রোযা রেখে পরিশেষে হারাম জিনিস দিয়ে রোযা খুলবে![5]\n\n" + 
                        "৭। রোযার মাঝে রয়েছে আল্লাহর প্রতি সুদৃঢ় ঈমান রাখার সবিশেষ প্রশিক্ষণ। কারণ, রোযা হল গুপ্ত ইবাদত। যেহেতু মানুষ এ ইবাদতে মুনাফেকী রাখতে পারে না। ইচ্ছা করলে সে গোপনে খেতে বা পান করতে পারে, অথবা উপবাস থেকেও নিয়ত ভেঙ্গে ফেলতে পারে। সুতরাং নিছক আল্লাহর প্রতি সুদৃঢ় ঈমান ও সত্য ভয় না থাকলে প্রকৃতরূপে রোযা রাখা যায় না।\n\n" + 
                        "বলা বাহুল্য, রোযা হল এমন একটি আন্তরিক ইবাদত, যা বান্দা ও প্রভুর মাঝে একান্ত গুপ্ত। অতএব গোপনে পানাহার করার সামর্থ্য ও সুযোগ থাকা সত্ত্বেও তা না করা এই কথাই প্রমাণ করে যে, সে বান্দা নিঃসন্দেহে এই বলে অটল বিশ্বাস রাখে যে, মহান আল্লাহ তার গোপন সব কিছুই দেখেন ও জানেন। আর এখান থেকেই রোযাদারের মনে ইবাদতে সততা ও আমানতদারী সৃষ্টি হয়। তাইতো আল্লাহ তাআলা রোযাকে পৃথক বৈশিষ্ট্য দান করেছেন; বান্দার প্রত্যেক আমলের সওয়াবকে ১০ গুণ থেকে ৭০০ গুণ; বরং আরো অনেক অনেক গুণ বর্ধিত করে থাকেন। কিন্তু রোযা নয়। রোযাকে তিনি নিজের জন্য খাস করে নিয়েছেন। আর তার সওয়াবের পরিমাণ যে কত, তা তিনি ছাড়া অন্য কেউ জানে না।\n\n" + 
                        "মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘আল্লাহ তাআলা বলেন, আদম সন্তানের প্রত্যেক আমল তার নিজের জন্য; তাতে তার সওয়াব ১০ থেকে ৭০০ গুণ বাড়িয়ে দেওয়া হয়। কিন্তু রোযা নয়। রোযা হল আমার জন্য। আর আমি নিজে তার প্রতিদান দেব।’’[6]\n\n" + 
                        "৮। রোযা রোযাদারের মনে পরকালের প্রতি আগ্রহ ও উৎকণ্ঠা বৃদ্ধি করে। কারণ, সে আল্লাহর নিকট আখেরাতে যে সওয়াব ও প্রতিদান আছে তা পাওয়ার আশায় আগ্রহান্বিত হয়ে পার্থিব কিছু সুখ-উপভোগ থেকে বিরত থাকে। সে যে নিক্তিতে লাভ-নোকসান ওজন করে থাকে তা হল পারলৌকিক। রোযার দিনে পানাহার ও যৌনসুখ শুধু এই আশায় পরিহার করে যে, এতে সে আল্লাহর সন্তুষ্টি পাওয়ার সাথে সাথে কিয়ামতের দিন উত্তম প্রতিদান পাবে। সুতরাং এইভাবে রোযা রোযাদারের মনে পরকালের প্রতি ঈমান বদ্ধমূল করে, পরলোকের সাথে অন্তরকে জুড়ে রাখে এবং ক্ষণস্থায়ী এই ধরাধামের পার্থিব ভোগ-বিলাসে বিতৃষ্ণা সৃষ্টি করে; যে ভোগ-বিলাস অনেক সময় মানুষকে আখেরাতের কথা বিস্মৃত করে এই ধারণা দেয় যে, সে পৃথিবীতে অমর ও চিরকাল থাকবে।[7]\n\n" + 
                        "৯। রোযা পরিপূর্ণরূপে আল্লাহর নিকট আত্মসমর্পণ এবং তাঁর পূর্ণ দাসত্ব করার কথা শিক্ষা দেয়। রোযা মুসলিমকে প্রকৃত দাসত্বের অনুশীলন দেয়। তাই তো সে রাতের বেলায় খায়, পান করে। কারণ, তার প্রভু যে বলেছেন,\n\n" + 
                        "(وَكُلُوا وَاشْرَبُوا حَتَّى يَتَبَيَّنَ لَكُمُ الْخَيْطُ الْأَبْيَضُ مِنَ الْخَيْطِ الْأَسْوَدِ مِنَ الْفَجْرِ)\n\n" + 
                        "অর্থাৎ, আর তোমরা পানাহার কর, যতক্ষণ পর্যন্ত না (রাতের) কালো অন্ধকার থেকে ফজরের সাদা রেখা তোমাদের নিকট স্পষ্ট হয়েছে। (কুরআনুল কারীম ২/১৮৭)\n\n" + 
                        "বলা বাহুল্য, এ জন্যই ইফতার ও সেহরীর সময় খাওয়া হল সুন্নত ও মুস্তাহাব এবং না খেয়ে একটানা পরপর কয়েকদিন রোযা রাখা মকরূহ। অতএব রোযা রাখার জন্য সেহরী খাওয়া এবং রোযার শেষে ইফতারী খাওয়া হল এক প্রকার আল্লাহর ইবাদত ও তাঁর নির্দেশের আনুগত্য।\n\n" + 
                        "তদনুরূপ ফজর উদয় হলে মুসলিম পানাহার সহ সেই সকল বস্ত্ত ও বিষয় থেকে দূরে থাকে, যাতে রোযা নষ্ট করে ফেলে। আর এর মাঝেও সে একমাত্র আল্লাহরই দাত্ব ও আনুগত্য করে। কারণ, তিনি বলেন,\n\n" + 
                        "(ثُمَّ أَتِمُّوا الصِّيَامَ إِلَى اللَّيْلِ)\n\n" + 
                        "অর্থাৎ, অতঃপর তোমরা রাত পর্যন্ত রোযা পূর্ণ কর। (কুরআনুল কারীম ২/১৮৭)\n\n" + 
                        "সুতরাং এইভাবে মুসলিম মহান আল্লাহর পূর্ণ দাসত্ব ও আনুগত্যের উপর দীর্ঘ প্রশিক্ষণ লাভ করে থাকে।[8]\n\n" + 
                        "১০। রোযা মুসলিমের জন্য আল্লাহর এক প্রকার রহমত, করুণা ও অনুগ্রহ। মহান আল্লাহ মুসলিম জাতির প্রতি অনুগ্রহ ও করুণা প্রদর্শন করেই রোযা ফরয করেছেন। কারণ, এরই মাধ্যমে তিনি মুসলিমের পাপরাশি মার্জনা করে থাকেন, তার মর্যাদা উন্নীত করে থাকেন এবং বহুগুণ হারে তার সওয়াব বৃদ্ধি করে থাকেন।[9]\n\n" + 
                        "১১। রোযা হল গোনাহের কাফ্ফারা। কারণ, নেকীর কাজ গোনাহর কাজের গোনাহ নাশ করে দেয়। আর রোযা হল বড় নেকীর কাজ। মহান আল্লাহ বলেন,\n\n" + 
                        "(إِنَّ الْحَسَنَاتِ يُذْهِبْنَ السَّيِّئَاتِ)\n\n" + 
                        "অর্থাৎ, নিশ্চয় পুণ্যরাশি (সওয়াবের কাজ) পাপরাশিকে দূরীভূত করে। (কুরআনুল কারীম ১১/১১৪)\n\n" + 
                        "আর প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘মানুষের পরিবার, ধন-সম্পদ ও প্রতিবেশী সংক্রান্ত পাপরাশিকে নামায, রোযা এবং সদকাহ মোচন করে দেয়।’’[10] অর্থাৎ, মুসলিম যে গোনাহ তার পরিবারকে অন্যায়ভাবে উচ্চবাচ্য করে, কষ্ট দিয়ে অথবা কোন বিষয়ে তাদের প্রতি ত্রুটি ও অবহেলা প্রদর্শন করে, অথবা প্রতিবেশীকে কোন কথায় বা কাজে কোন প্রকার কষ্ট দিয়ে, অথবা আর্থিক কোন প্রকার ত্রুটি ঘটিয়ে অথবা অনুরূপ অন্যান্য সাগীরা (ছোট) গোনাহ করে থাকে, সে সবকে তার নামায, রোযা এবং দান-খয়রাত মোচন করে দেয়।\n\n" + 
                        "পরন্তু প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি ঈমান ও সওয়াবের আশা রেখে রমাযানের রোযা রাখে, তার পূর্বেকার সকল গোনাহ মাফ হয়ে যায়।’’[11]\n\n" + 
                        "আবু হুরাইরা (রাঃ) হতে বর্ণিত, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘কাবীরাহ গোনাহ না করলে পাঁচ ওয়াক্ত নামায, এক জুমআহ থেকে অপর জুমআহ এবং এক রমাযান থেকে অন্য রমাযান -এর মধ্যবর্তীকালে সংঘটিত পাপসমূহের কাফফারা (প্রায়শ্চিত্ত)।’’ [12]\n\n" + 
                        "তদনুরূপ রোযা হল কসম ভাঙ্গার কাফ্ফারা (জরিমানা)। (কুরআনুল কারীম ৫/৮৯) যিহারের কাফ্ফারা। (কুরআনুল কারীম ৫৮/৪) কোন মুসলিমকে বা চুক্তিবদ্ধ কোন যিম্মীকে ভুলবশতঃ হত্যা করে ফেলার কাফ্ফারা। (কুরআনুল কারীম ৪/৯২) ইহরামে নিষিদ্ধ কর্ম করে ফেলার কাফ্ফারা। (কুরআনুল কারীম ২/১৯৬, ৫/৫) তামাত্তু’ হজ্জের কুরবানী দিতে না পারলে তার কাফ্ফারা। (কুরআনুল কারীম ২/১৯৬) ইত্যাদি।\n\n" + 
                        "১২। রোযা রোযাদারের মনে ধৈর্য ও সহনশীলতা সৃষ্টি করে। কষ্টে ধৈর্য ধারণ ও সহনশীলতা অবলম্বন করতে অভ্যাসী বানায়। রোযা তাকে তার প্রিয় বস্ত্ত ব্যবহার বর্জন করতে ধৈর্যের শিক্ষা দেয়। যেমন শিক্ষা দেয় কাম-দমন ও মনের যথেচ্ছাচার দমন করার; যা নিশ্চয় সহজ কাজ নয়।\n\n" + 
                        "বলা বাহুল্য, রোযা পালনে রয়েছে ৩ প্রকার ধৈর্য। মহান আল্লাহর আনুগত্যে ধৈর্য, তাঁর হারামকৃত বস্ত্ত পরিহার করার উপর ধৈর্য এবং তাঁর নির্ধারিত তকদীরের বালা-মসীবতের উপর ধৈর্য। এই ৩ প্রকার ধৈর্য যে বান্দার মাঝে একত্রিত হবে, সেই হবে ইহকালে পরম সুখী এবং পরকালে আল্লাহর ইচ্ছায় সে জান্নাতে প্রবেশ করবে। মহান আল্লাহ বলেন,\n\n" + 
                        "(إِنَّمَا يُوَفَّى الصَّابِرُونَ أَجْرَهُمْ بِغَيْرِ حِسَابٍ)\n\n" + 
                        "অর্থাৎ, ধৈর্যশীলদেরকে তো অপরিমিত পুরস্কার ও সওয়াব দান করা হবে। (কুরআনুল কারীম ৩৯/১০)\n\n" + 
                        "এতে কোন সন্দেহ নেই যে, ক্ষুৎ-পিপাসা ও যৌনক্ষুধায় ধৈর্যধারণ করাই হল ধৈর্যের শেষ পর্যায়। সুতরাং যে ব্যক্তি এই শ্রেণীর ধৈর্য ধারণ করতে পারঙ্গম হবে, সে ব্যক্তির জন্য অন্য শ্রেণীর ধৈর্য ধারণ করা সহজ হয়ে যাবে। আর যে ব্যক্তি আল্লাহর সন্তুষ্টি লাভের উদ্দেশ্যে ধৈর্য ধারণ করবে, সে ব্যক্তি লাভ করবে শুভপরিণাম।\n\n" + 
                        "মহান আল্লাহ বলেন,\n\n" + 
                        "(وَالَّذِينَ صَبَرُوا ابْتِغَاءَ وَجْهِ رَبِّهِمْ وَأَقَامُوا الصَّلاةَ وَأَنْفَقُوا مِمَّا رَزَقْنَاهُمْ سِرّاً وَعَلانِيَةً وَيَدْرَأُونَ بِالْحَسَنَةِ السَّيِّئَةَ أُولَئِكَ لَهُمْ عُقْبَى الدَّارِ- جَنَّاتُ عَدْنٍ يَدْخُلُونَهَا)\n\n" + 
                        "‘‘যারা তাদের প্রতিপালকের সন্তুষ্টিলাভের জন্য ধৈর্যকষ্ট বরণ করে, যথাযথভাবে নামায পড়ে, আমি যে রুযী তাদেরকে দান করেছি তা হতে গোপনে ও প্রকাশ্যে দান করে এবং যারা ভালো দ্বারা মন্দকে দূর করে- তাদেরই জন্য রয়েছে শুভ পরিণাম; (আদ্ন) স্থায়ী বেহেশত, ওতে ওরা প্রবেশ করবে---।’’ (কুরআনুল কারীম ১৩/২২-২৩)\n\n" + 
                        "১৩। রোযা হল ঢালস্বরূপ; দোযখ থেকে রক্ষার ঢালস্বরূপ।[13] একটি মাত্র রোযা জাহান্নামকে রোযাদার থেকে ৭০ বছরের পথ দূরে সরিয়ে দেয়।[14] সুতরাং যে ব্যক্তি পূর্ণ রমাযান মাসের রোযা রাখে এবং প্রত্যেক মাসে ৩টি রোযা অথবা আরো অন্যান্য নফল রোযা রাখে, সে ব্যক্তি থেকে দোযখ কত বছরের পথ দূরে সরে যায় তা অনুমেয়।\n\n" + 
                        "১৪। রোযা হল চরিত্রহীনতা ও ব্যভিচার ইত্যাদি অশ্লীলতা থেকে ঢালস্বরূপ। রোযা রোযাদারকে অবৈধ যৌনাচার থেকে হিফাযতে রাখে, যেমন ঢাল মুজাহিদ (যোদ্ধা)কে শত্রুপক্ষের তীর ও তরবারির আঘাত থেকে রক্ষা করে থাকে।\n\n" + 
                        "আব্দুল্লাহ বিন মাসঊদ (রাঃ) কর্তৃক বর্ণিত, প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘হে যুবকদল! তোমাদের মধ্যে যে ব্যক্তি (বিবাহের অর্থাৎ স্ত্রীর ভরণপোষণ ও রতিক্রিয়ার) সামর্থ্য রাখে সে যেন বিবাহ করে। কারণ, বিবাহ চক্ষুকে দস্ত্তরমত সংযত করে এবং লজ্জাস্থান হিফাযত করে। আর যে ব্যক্তি ঐ সামর্থ্য রাখে না, সে যেন রোযা রাখে। কারণ, তা যৌনক্ষুধা উপশমকারী।’’[15]\n\n" + 
                        "বলা বাহুল্য, যে যুবক বিবাহের খরচাদি বহন করতে সক্ষম নয়, সে যুবককে মহানবী এই নির্দেশ দিলেন যে, সে যেন তার কামক্ষুধা ও যৌন-উত্তেজনা প্রশমিত করতে রোযার সাহায্য নেয়। কারণ, রোযা উক্ত ক্ষুধা ও উত্তেজনা দমন ও নিবারণ করে। আর অনেকের অভিজ্ঞতা দ্বারা উক্ত নববী চিকিৎসা প্রমাণিত ও পরীক্ষিত যে, কামপীড়িত যুবকের জন্য যে কোনও সেব্য ঔষধ অপেক্ষা রোযাই হল উত্তম ও অব্যর্থ ঔষধ।\n\n" + 
                        "১৫। রোযা হল বেহেশ্তেগামী পথ। আবূ উমামাহ (রাঃ) জান্নাতে প্রবেশ করাবে এমন আমল প্রসঙ্গে যখন আল্লাহর নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর নিকট নির্দেশ চাইলেন, তখন তিনি তাঁকে বললেন, ‘‘তুমি রোযা রাখ। কারণ, তার মত অন্য কোন আমল নেই।’’[16] তাছাড়া মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) রোযাদারকে বেহেশ্তে ‘রাইয়ান’ নামক এক বিশেষ দরজা দিয়ে প্রবেশ করার সুসংবাদ দিয়েছেন।[17] আর ‘রাইয়ান’ (তৃষ্ণাহীন) দ্বার আমল অনুযায়ী রোযাদারের জন্য বড় উপযুক্ত। কারণ, রোযা রাখার ফলে দুনিয়াতে সে পিপাসায় কাতর হয়। তাই তারই বিনিময়ে পরকালে ‘‘সেই দ্বারে যে প্রবেশ করবে সে (বেহেশ্তী পানীয়) পান করবে। আর যে ব্যক্তি একবার তা পান করবে, সে ব্যক্তি আর কোন কালেও পিপাসিত হবে না।’’[18]\n\n" + 
                        "১৬। রোযা পালনের মাধ্যমে রোযাদার তার মহান প্রভুর সন্তুষ্টি লাভ করে থাকে। যার জন্য তার উপবাস-জনিত মুখের দুর্গন্ধও আল্লাহর নিকট কস্ত্তরী অপেক্ষাও অধিক সুগন্ধময় হয়![19] অথচ খালি পেটে থাকা অবস্থায় মুখ থেকে বের হওয়া ঐ দুর্গন্ধ কোন মানুষ পছন্দ করে না; বরং ঘৃণাই করে থাকে। কিন্তু তা মহান স্রষ্টার নিকটে অতি পছন্দনীয়। কারণ, এ গন্ধ তাঁরই আনুগত্য ও সন্তুষ্টির পথে নির্গত হয়ে থাকে।\n\n" + 
                        "১৭। রোযাদার ব্যক্তির দুআ রোযা রাখা অবস্থায় কবুল হয়ে থাকে। প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তিন প্রকার দুআ আল্লাহর নিকট কবুল হয়ে থাকে; রোযাদারের দুআ, অত্যাচারিতের দুআ এবং মুসাফিরের দুআ।’’[20]\n\n" + 
                        "১৮। রোযা কিয়ামতের ভীষণ বিচার দিনে রোযাদারের জন্য আল্লাহর দরবারে সুপারিশ করবে; বলবে, ‘হে আমার প্রভু! আমি ওকে দিনের বেলায় পানাহার ও যৌনক্রিয়া থেকে বিরত রেখেছিলাম। সুতরাং ওর ব্যাপারে তুমি আমার সুপারিশ গ্রহণ করে নাও।’ অতঃপর মহান প্রভু তার সে সুপারিশ গ্রহণ করে নেবেন।[21]\n\n" + 
                        "১৯। রোযা রোযাদারের জন্য ইহ-পরকালের খুশী ও সুখের হেতু। যেমন মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘রোযাদারের জন্য রয়েছে ২টি খুশী; প্রথম খুশী হল ইফতার করার সময় এবং দ্বিতীয় খুশী হল প্রতিপালকের সাথে সাক্ষাতের সময়।’’[22]\n\n" + 
                        "রোযাদারের ইফতার করার সময় যে খুশী, তা হল সেই সুখ ও তৃপ্তির একটি নমুনামাত্র; যা মুমিন ব্যক্তি নিজ প্রভুর আনুগত্য ও তাকওয়ার মাধ্যমে অর্জন করে থাকে। আর প্রকৃতপ্রস্তাবে এটাই হল আসল সুখ। এই সুখ ও তৃপ্তি দুইভাবে অনুভূত হয়ে থাকেঃ-\n\n" + 
                        "(ক) আল্লাহ তাআলা ঐ ইফতারের সময় রোযাদারের জন্য পানাহার বৈধ করে দিয়েছেন। আর নিঃসন্দেহে মানুষের প্রকৃতি এই যে, (বিশেষ করে খিদে থাকা অবস্থায়) খাবার দেখলে মন আনন্দে নেচে ওঠে। আর এ জন্যই তা বর্জন করা হল আল্লাহর ইবাদত।\n\n" + 
                        "(খ) সে মুহূর্তে রোযাদার তার একটি রোযা সম্পন্ন করে থাকে। সুতরাং আল্লাহর তওফীক অনুযায়ী সে সেদিনকার রোযা ও ইবাদত যে পালন ও পূর্ণ করতে পারল, তারই খুশী তার মনকে আন্দোলিত করে তোলে।[23]\n\n" + 
                        "পক্ষান্তরে সবচেয়ে বড় খুশী যা, তা রয়েছে পরকালে; যখন তাঁর সাথে সাক্ষাৎ হবে যাঁর জন্য রোযাদার রোযা রেখে থাকে।\n\n" + 
                        "২০। রোযা হল পরহেযগার ও নেক লোকদের ট্রেনিং-ময়দান; যার মাঝে আল্লাহর দেওয়া পৃথিবীর খেলাফতের দায়িত্ব পালন করার উপর নিজেদের কর্তব্যের বিভিন্ন ট্রেনিং নিয়ে থাকে। বলা বাহুল্য, রোযা দেহ-মনের জন্য একটি বড় রহমত। রোযার মাঝেই হৃদয় ও সকল চিন্তা-ভাবনা আল্লাহ তাআলার সাথে যুক্ত থাকে। সকল মনোবল তাঁর ভালোবাসা, আনুগত্য ও তাঁর পথে জিহাদের কাজে বর্ধিত ও সংবদ্ধ হয়ে থাকে। যার পশ্চাতে উদ্দেশ্য থাকে এই যে, আল্লাহর বাণীই সমুন্নত হোক এবং কাফেরদের বাণী হোক অবনত; সে কাফের যেমনই হোক, তার যে নাম বা উপাধি হোক অথবা যে প্রতীকই হোক।[24]\n\n" + 
                        "২১। রোযা হল কচি-কাঁচা শিশুর মনের মাটিতে ‘আমানতদারী’র বীজ রোপণ করার এক বাস্তবভিত্তিক ইতিবাচক ও কার্যকর প্রক্রিয়া। শিশু-কিশোরকে রোযা রাখতে অভ্যাসী করার সময় যখন তাকে পানাহার করতে নিষেধ করা হয় এবং খাবার ও পানি হাতের কাছে থাকা সত্ত্বেও সে শুধু এই বিশ্বাসে তা খেতে পারে না যে, এ নিষেধ হল আল্লাহর এবং তিনি তাকে দেখছেন। অথচ এ ব্যাপারে কেবল তার মন ও বিবেক ছাড়া অন্য কেউ পর্যবেক্ষক নেই। সুতরাং কাঁচা মনে আমানতদারী বদ্ধমূল করতে এই অনুভূতি অপেক্ষা অধিক প্রতিক্রিয়াশীল আর অন্য কি হতে পারে?\n\n" + 
                        "যার ফলে শৈশব থেকেই শিশু আমানতদারীর মত এক নৈতিকতাপূর্ণ কর্মে অভ্যাসী হয়ে গড়ে ওঠে এবং বয়ঃপ্রাপ্ত হওয়ার পরেও তার যথার্থ হিফাযত করতে ও তার মনে তা আজীবন বহাল রাখতে কোন প্রকার কষ্টবোধ করে না।[25]\n\n" + 
                        "২২। রোযা মানুষের হৃদয়কে নরম করে, আল্লাহ-প্রেমী করে এবং সর্বদা তাঁর যিক্র ও শুক্র করতে অভ্যাসী করে।\n\n" + 
                        "২৩। রোযা মানুষের মাঝে শয়তানের প্রবেশ ও প্রবাহ-পথ রুদ্ধ করে। এর ফলে তার দেহ-মনে শয়তানের আধিপত্য কমে যায়। পক্ষান্তরে যখনই মানুষ নিজ প্রবৃত্তির লাগাম ছেড়ে দেয়, তখনই শয়তান তা লুফে নিয়ে তাকে যেদিকে ইচ্ছা সেদিকে পরিচালিত করতে থাকে।[26]\n\n" + 
                        "২৪। রোযা হল আল্লাহর দেওয়া নেয়ামতের শুক্রিয়া আদায় করার অন্যতম মাধ্যম। কারণ, রোযা হল পানাহার ও যৌনমিলন থেকে বিরত থাকার নাম। আর মানুষের উপর আল্লাহর যে সকল বড় বড় নেয়ামত রয়েছে তার মধ্যে পানাহার ও যৌনমিলন হল অন্যতম। সুতরাং মানুষ এ নেয়ামতের কদর তখনই বুঝবে, যখন সে এ নেয়ামত থেকে নির্দিষ্টকাল পর্যন্ত বঞ্চিত থাকবে। কারণ, হারিয়ে না গেলে কোন নেয়ামতের কদর বুঝা যায় না। আর যখনই উক্ত নেয়ামতের কদর সে বুঝবে, তখনই তার অবশিষ্ট অধিকার আদায়ের জন্য শুক্রিয়া জ্ঞাপন করবে। পক্ষান্তরে নেয়ামতের শুক্র আদায় করা ফরয; শরীয়তে এবং বিবেক মতেও। রোযার আয়াতে মহান আল্লাহ ঐ কথার প্রতি ইঙ্গিত করেই বলেন,\n\n" + 
                        "(ولعلَّكُمْ تَشْكُرُوْن)\n\n" + 
                        "অর্থাৎ, যাতে তোমরা শুক্র আদায় কর। (কুরআনুল কারীম ২/১৮৫)\n\n" + 
                        "রোযাদার যখন ক্ষুধার জ্বালা অনুভব করে, তখন সে সেই গরীব-নিঃসবদের কষ্টের কথাও উপলব্ধি করে; যারা ক্ষুধার সময় পেটে এক মুঠো অন্নও যোগাড় করতে সমর্থ নয়। এর ফলে ঐ উপলব্ধি তাকে তাদের জন্য দান-খয়রাত করে সহানুভূতি প্রকাশ করতে উদ্বুদ্ধ করে। কারণ, নিজের দেখা বিষয় শোনা বিষয়ের মত নয়। নিজের দেখা ও পরীক্ষা করা বিষয়ে অভিজ্ঞতালব্ধ প্রতীতি জন্মে অধিক। যেমন একজন ঘোড়সওয়ার লোক পথ চলার কষ্ট ততক্ষণ অনুভব করতে সক্ষম নয়; যতক্ষণ না সে নিজে পায়ে হেঁটে পথ চলে দেখেছে।[27]\n\n" + 
                        "২৫। রোযার মাধ্যমে রোযাদার ক্ষুধা-জনিত দুর্বলতার ফলে সে আল্লাহর কতটা মুখাপেক্ষী তা আন্দাজ করতে পারে। আর যে ব্যক্তি নিজের মাঝে নিজের দুর্বলতা চিনতে পারে, সে ব্যক্তির মিথ্যা অহংকার দূরীভূত হয়ে যায়। পরন্তু আল্লাহ সেই ব্যক্তির প্রতি রহম করেন, যে নিজের কদর নিজে জেনেছে।[28]\n\n" + 
                        "২৬। রোযাতে রোযাদার ফিরিশ্তামন্ডলীর অনুরূপ কর্মে শামিল হতে পারে; যে ফিরিশ্তামন্ডলী আল্লাহর কোন প্রকার অবাধ্যাচরণ করেন না। তাঁরা তাই করেন, যা করতে তাঁদের প্রতিপালক তাঁদেরকে আদেশ করেন। দিবারাত্র তাসবীহ পাঠ করতে থাকেন এবং কোন প্রকার ক্লান্তিবোধ করেন না। যাঁরা খান না এবং পানও করেন না।[29]\n\n" + 
                        "২৭। রোযা রোযাদারের ঈমান বৃদ্ধি করে। এই রোযাতে মানুষ অধিকাধিক নামায পড়ে, কুরআন তেলাঅত ও যিক্র করে, দান-খয়রাত করে, দুআ ও ইস্তিগফার তথা তওবা করে, ওয়ায-নসীহত শোনে। রোযা তাকে মন্দ কাজ করতে বাধা দেয়। বলা বাহুল্য, এ সবে পাপ বন্ধ থাকে এবং ঈমান বর্ধিত হয়।[30]\n\n" + 
                        "২৮। রোযার মাসে রোযাদারের দ্বীনী জ্ঞান বর্ধিত হয়ে থাকে। কারণ, রমাযান হল ইবাদতের মাস, আল্লাহর আয়াত নিয়ে ভাবনা-চিন্তা ও গবেষণা করার মাস। কুরআন মাজীদ তেলাঅত করা ও শোনার মাস।[31]\n\n" + 
                        "২৯। এ মাসে দ্বীনের আহবায়কদের জন্য রয়েছে সুবর্ণ সুযোগ। এ মাসে অধিকাংশ মুসলিম জনসাধারণ মসজিদের দিকে ধাবিত হয়। এদের মধ্যে কেউ বা তার জীবনে প্রথমবার প্রবেশ করে, কেউ বা অনেক দিন হল মসজিদ ত্যাগ করেছিল। এ সময় তাদের হৃদয় এক প্রকার দুর্লভ নম্রতা ও তরঙ্গায়িত ভক্তিতে গদ্গদ্ করে।\n\n" + 
                        "সুতরাং এ সুযোগের সদ্ব্যবহার করে মন-গলানো উপদেশমালা এবং উপযুক্ত ওয়ায ও দর্স প্রয়োগ করে তাদের ঈমান বাড়াতে সাহায্য করা উচিৎ। আর এ কাজে অবশ্যই সৎ ও আল্লাহভীতির কাজে সহায়তা হয়ে থাকে।[32]\n\n" + 
                        "পরন্তু রমাযানের রোযা বছরান্তে একবার ফরযরূপে এসে থাকে। যা হজ্জের মত জীবনে একবার নয়। যাতে প্রত্যেক বছর ঈমানী দর্সের পুনরাবৃত্তি হয় এবং রোপিত ঈমানী বৃক্ষ সহসায় বেড়ে ওঠে।\n\n" + 
                        "৩০। রোযার মাধ্যমে মুসলিমদের সামাজিক উপকারিতা সাধন হয়ে থাকে। রোযা হল মুসলিম জাতির ঐক্যের নিদর্শন, সারা উম্মাহর মাঝে সংহতির প্রতীক, গরীব-ধনীর মাঝে সাম্য ও সম্প্রীতির চিহÁ। এতে আম-খাস, আতরাফ-আশরাফ, আমীর-ফকীরের ভেদাভেদ চূর্ণ হয়ে যায়। সকলের মনে একটাই বোধ জাগে যে, মুসলিম জাতি হল এক জাতি। সকলে একই সময়ে পানাহার করে, একই সময়ে রোযা রাখে। একই জামাআতে মসজিদে তারাবীহর নামায পড়ে। যেন সকলের হৃদয় এক, মাস এক, কর্মও এক, যাদেরকে তাদের নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেছেন, তারা হল একটি দেহের মত।\n\n" + 
                        "৩১। রোযার উপবাস সবাস্থ্যের জন্য বড় উপকারী। রোযাতে তুলনামূলকভাবে কম খাওয়া হয় এবং নির্দিষ্ট সময় ধরে পাকস্থলীকে বিরতি দেওয়া হয়। এর ফলে শরীরের মেদ, ক্লেদ ও আর্দ্রতা ইত্যাদি দূরীভূত হয়ে যায়।[33] আর একথা বিদিত ও স্বীকৃত যে, শরীরের মধ্যে পেট হল রোগের বাসা এবং ব্যবস্থাপিত ও নিয়ন্ত্রিত খাদ্য আহার করা হল শ্রেষ্ঠ চিকিৎসা। বলা বাহুল্য, এ কথা বহু চিকিৎসকই স্বীকার করেছেন যে, রোযাতে রয়েছে বহু দীর্ঘস্থায়ী ব্যাধি থেকে নিরাপত্তা, বিশেষ করে যক্ষ্মা, ক্যানসার ইত্যাদি।\n\n" + 
                        "রোযায় রয়েছে শরীরের ওজন বৃদ্ধি, হ্যাপাটাইটিস, জন্ডিস, প্লীহা, যকৃৎ, বদহজম, প্রভৃতি রোগের চিকিৎসা।\n\n" + 
                        "রোযা ফরয হয়েছে সুস্থ মানুষের উপর। যাতে আক্রমণের পূর্বেই ঐ সকল বা আরো অজানা বহু রোগের হাত থেকে বাঁচার উপায় পাওয়া যায়। তাছাড়া বহু গবেষণা এ কথা প্রমাণ করেছে যে, পানাহার থেকে বিরত থাকা একটি প্রকৃতিগত ব্যাপার; যা মহান সৃষ্টিকর্তা নিয়মিতভাবে নির্দিষ্ট সময় ব্যাপী জীবজগতের জন্য অনিবার্য করেছেন। আর তা শুধু এই জন্য যে, যাতে করে প্রাণীজগৎ ধ্বংসের হাত থেকে রক্ষা পায়, বাঁচার জন্য শক্তি পায় এবং নিজ নিজ বংশবিস্তারে যথানিয়মে সক্রিয় থাকতে পারে।\n\n" + 
                        "জীবজন্তু ও কীটপতঙ্গের উপবাস করার কথা অনেকের অজানা নয়। কোন কোন জন্তু লম্বা সময় ধরে প্রায় কয়েক মাস যাবৎ উপবাস করে। কোন কোন জন্তু কয়েক দিন ধরে উপবাস করে। বরং উদ্ভিদজগৎও উপবাস পালন করে থাকে। যার ফলে নতুন, সুন্দর ও লকলকে পাতা বের হয়ে আসে এবং শান্ত শীতে নিদ্রার পর ফুল-ফলে সুশোভিত হয়ে শক্তিশালী ও সজীবরূপে শুরু হয় বৃক্ষ-তরুলতার আনন্দময় বসন্তকাল।\n\n" + 
                        "ডাঃ সলোমন মানব-দেহকে ইঞ্জিনের সাথে তুলনা করে বলেন, ‘ইঞ্জিন রক্ষাকল্পে মধ্যে মধ্যে ডকে নিয়া চুল্লি হইতে ছাই ও অঙ্গার সম্পূর্ণরূপে নিষ্কাশিত করা যেমনটা আবশ্যক - উপবাস দ্বারা মধ্যে মধ্যে পাকস্থলী হইতে অজীর্ণ খাদ্যটি নিষ্কাশিত করাও তেমনটা দরকার।’[34]\n\n" + 
                        "৩২। রোযা মানুষের চিন্তাশক্তির প্রখরতা বৃদ্ধি করে। কারণ, পেট খালি থাকলে চিন্তা-গবেষণা নির্মল হয় এবং মন-মগজের কর্ম সুন্দর হয়।\n\n" + 
                        "পক্ষান্তরে যারা ধারণা করে যে, রোযা মানুষের খরচ বাড়ায় এবং অতিরিক্ত অর্থ ব্যয় হয় এই রোযার মাসে, তাদের ধারণা সঠিক নয়। কেননা, খাবারের নানান ভ্যারাইটিজ তৈরী করা এবং প্রয়োজনের তুলনায় বেশী খাবার প্রস্ত্তত করা, রমাযানের জন্য বিশেষ বিশেষ ধরন ও বরনের খাদ্যপণ্যের বিপণন ঘটানোতে ইসলামের অনুমোদন নেই। বরং তা হল অপচয়। আর অপচয় ইসলামে নিষিদ্ধ; রমাযানে এবং অন্য মাসেও।\n\n" + 
                        "বলাই বাহুল্য যে, রোযাতে রয়েছে মঙ্গলই মঙ্গল। ব্যক্তি ও সমাজের জন্য দুনিয়া ও আখেরাতে সেই মঙ্গল অনস্বীকার্য। আর মহান আল্লাহর এই বাণীর মধ্যে সেই মঙ্গলের প্রতিই ইঙ্গিত করা হয়েছে। তিনি বলেন,\n\n" + 
                        "(وَأَنْ تَصُوْمُوْا خَيْرٌ لَّكُمْ إِنْ كُنْتُمْ تَعْلَمُوْنَ)\n\n" + 
                        "অর্থাৎ, তোমাদের রোযা রাখাটাই তোমাদের জন্য কল্যাণকর; যদি তোমরা উপলব্ধি কর। (কুরআনুল কারীম ২/১৮৪)",
                "Among Allah's sublime names is 'Al-Hakeem' (The All-Wise). In the fasting that Allah ordained, there exists boundless wisdom and profound benefits:\n\n" + 
                        "1. Attaining Divine Proximity: The believer renounces innate desires of food, drink, and intimacy solely for the pleasure of their Creator.\n2. Nurturing Taqwa (God-consciousness): As stated in Quran 2:183, fasting builds self-restraint and mindfulness of Allah in every thought, word, and deed.\n3. Spiritual Purification & Moral Integrity: Instills honesty and inner vigilance; one who is sincere with Allah cannot deceive humanity.\n4. Elevating Character and Decorum: A month of training against slander, backbiting, falsehood, obscenity, and hostility.\n5. Mastery over Desires: Strengthens willpower and breaks the enslavement to base urges.\n6. Freedom from Harmful Addictions: Provides an ideal golden opportunity to break noxious habits such as smoking.\n7. Pure Unadulterated Faith: Fasting is a secret worship known only to the servant and Allah, eliminating ostentation (Riya).\n8. Awakening Longing for the Hereafter: Shifts the believer's scale of gain and loss toward eternal rewards.\n9. Cultivating Absolute Submission and Servitude: Eating at Sehri and breaking fast at sunset strictly in obedience to Allah's command.\n10. Manifestation of Divine Mercy and Compassion: An avenue for expiating sins and multiplying rewards.\n11. Expiation of Transgressions: Sins concerning family, wealth, and neighbors are wiped away through prayer, fasting, and charity.\n12. Training in Three Levels of Patience: Patience in obeying Allah, patience in abstaining from what He forbade, and patience with divine decree (Quran 39:10).\n13. Armor Against Hellfire: Shields the soul from eternal punishment.\n14. Safeguard of Chastity: Cools lustful urges for unmarried youth who cannot yet marry.\n15. Path Leading to Paradise: Entrance through the specialized Ar-Rayyan gate.\n16. Gaining Supreme Pleasure of Allah: The breath of the faster is more pleasing to Allah than musk.\n17. Guaranteed Acceptance of Supplications: The prayer of the fasting person is never rejected.\n18. Powerful Intercession on the Day of Judgment: Fasting will plead before Allah on behalf of the servant.\n19. Two Unmatched Joys: Joy at the moment of Iftar and supreme bliss upon meeting the Lord.\n20. Training Ground for Righteous Believers: Fosters unity, moral courage, and dedication to uplifting Allah's word.\n21. Planting Seeds of Integrity in Youth: Teaches children trustworthiness when abstaining while unobserved.\n22. Softening Hearts: Inspires remembrance and gratitude to Allah.\n23. Constricting Satanic Pathways: Weakens the whispers and influence of the devil.\n24. Awakening Gratitude for Divine Bounties: Feeling the pangs of hunger connects the heart with the plight of the destitute.\n25. Realizing Human Frailty: Dispels arrogance and self-sufficiency.\n26. Resemblance to Noble Angels: Emulating beings who neither eat nor drink, perpetually glorifying Allah.\n27. Increase in Sincere Faith: Fosters Quranic recitation, charity, repentance, and listening to counsel.\n28. Growth in Sacred Knowledge: Contemplation of the Quran and Islamic jurisprudence.\n29. Golden Opportunity for Islamic Callers: Welcoming hearts returning to mosques with uplifting reminders.\n30. Societal Solidarity & Equality: Eliminates class distinctions as all fast, break fast, and pray together as one body.\n31. Medical and Physical Health: Purifies the digestive system, reduces metabolic burden, and shields against obesity, indigestion, and chronic ailments.\n32. Mental Clarity and Focus: A light stomach sharpens intellect and reflection."
        ));
        return list;
    }
}
