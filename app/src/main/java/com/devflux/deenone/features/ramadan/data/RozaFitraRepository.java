package com.devflux.deenone.features.ramadan.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.hajj.model.HajjHistoryCardItem;
import com.devflux.deenone.features.ramadan.model.RozaFitraCommodity;
import com.devflux.deenone.features.ramadan.model.RozaFitraTopicItem;
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
 * Production-ready Repository for section: ফিতরা (Sadaqatul Fitr).
 * Provides:
 * 1. Commodity standards (Wheat, Dates, Raisins, Barley, Cheese) with authentic weights & rates.
 * 2. 10 authentic topic items with 100% verbatim Islamic jurisprudence text and citations.
 * 3. 0ms instant local cache with asynchronous PHP REST API synchronization (Rule 11).
 */
public final class RozaFitraRepository {

    private static final List<RozaFitraCommodity> cachedCommodities = Collections.synchronizedList(new ArrayList<>());
    private static final List<RozaFitraTopicItem> cachedTopics = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedCommodities.addAll(getDefaultCommodities());
        cachedTopics.addAll(getDefaultTopics());
    }

    private RozaFitraRepository() {
        // Utility class
    }

    public interface TopicDataCallback {
        void onDataLoaded(List<RozaFitraTopicItem> topics);
    }

    public static List<RozaFitraCommodity> getCommodities() {
        return new ArrayList<>(cachedCommodities);
    }

    public static RozaFitraCommodity getCommodityById(String id) {
        synchronized (cachedCommodities) {
            for (RozaFitraCommodity c : cachedCommodities) {
                if (c.getId().equalsIgnoreCase(id)) return c;
            }
        }
        return cachedCommodities.isEmpty() ? null : cachedCommodities.get(0);
    }

    public static List<RozaFitraTopicItem> getTopics(Context context, TopicDataCallback callback) {
        if (context != null && NetworkConnectivityHelper.isOnline(context)) {
            final Context appContext = context.getApplicationContext();
            executor.execute(() -> fetchRemoteData(appContext, callback));
        }
        return new ArrayList<>(cachedTopics);
    }

    public static List<RozaFitraTopicItem> getDefaultTopics() {
        List<RozaFitraTopicItem> list = new ArrayList<>();

        // 1. ফিতরা কি?
        list.add(new RozaFitraTopicItem(
                1,
                "fitra_what",
                "ফিতরা কি?",
                "What is Fitra?",
                "ফিতরা কি?",
                "What is Fitra?",
                "ফিতরা, যাকে সাদাকাতুল ফিতর (সাদাকা অর্থ দান, ফিতর অর্থ রোজা ভাঙা বা ঈদুল ফিতর) বলা হয়, এটি রমজান মাসের শেষের দিকে ঈদের আগে মুসলিমদের জন্য আদায় করা একটি বিশেষ দান। এটি প্রত্যেক সামর্থ্যবান মুসলিমের উপর ওয়াজিব (আবশ্যকীয়) ইবাদত। ফিতরা ঈদুল ফিতরের আনন্দের মুহূর্তে গরিব ও অসহায় মানুষদের সহায়তা করার একটি গুরুত্বপূর্ণ মাধ্যম।",
                "Fitra, known as Sadaqatul Fitr (Sadaqah meaning charity, Fitr meaning breaking the fast or Eid al-Fitr), is a special charity given by Muslims before Eid at the end of Ramadan. It is an obligatory (Wajib) worship upon every capable Muslim, serving as a vital medium to assist the poor and helpless during Eid al-Fitr.",
                "ফিতরা, যাকে সাদাকাতুল ফিতর (সাদাকা অর্থ দান, ফিতর অর্থ রোজা ভাঙা বা ঈদুল ফিতর) বলা হয়, এটি রমজান মাসের শেষের দিকে ঈদের আগে মুসলিমদের জন্য আদায় করা একটি বিশেষ দান। এটি প্রত্যেক সামর্থ্যবান মুসলিমের উপর ওয়াজিব (আবশ্যকীয়) ইবাদত। ফিতরা ঈদুল ফিতরের আনন্দের মুহূর্তে গরিব ও অসহায় মানুষদের সহায়তা করার একটি গুরুত্বপূর্ণ মাধ্যম।\n\n"
                        + "ফিতরার সংজ্ঞা ও উদ্দেশ্য:\n\n"
                        + "ফিতরা হল ঈদুল ফিতরের দিন দরিদ্রদের মুখে হাসি ফোটানোর জন্য নির্ধারিত একটি আর্থিক সাহায্য। এর দুটি প্রধান উদ্দেশ্য রয়েছে:\n\n"
                        + "১. আত্মশুদ্ধি ও রোজা ত্রুটিমুক্ত করা:\n\n"
                        + "রোজা রাখার সময় মনের অজান্তে যেসব ভুলত্রুটি, অনর্থক কথাবার্তা বা ছোটখাটো পাপ হয়ে থাকে, ফিতরা আদায়ের মাধ্যমে তা থেকে ক্ষমা পাওয়া যায় এবং রোজা আল্লাহর কাছে গ্রহণযোগ্যতা পায়। রাসুলুল্লাহ ﷺ বলেছেন:\n\n"
                        + "‘শাবান ও রমজানের রোজা আসমান ও জমিনের মাঝখানে ঝুলন্ত থাকে, যা কেবল সাদাকাতুল ফিতর আদায় করার মাধ্যমেই আল্লাহর কাছে পৌঁছায়।’\n\n"
                        + "[কানজুল উম্মাল: ২৪১৩০, জামি আস-সগীর: ৪১৯১]\n\n"
                        + "২. গরিবদের আনন্দে শামিল করা:\n\n"
                        + "ঈদের দিন যাতে গরিব-অসহায় মানুষ না খেয়ে না থাকে এবং তারাও ঈদের আনন্দ সবার সঙ্গে ভাগ করে নিতে পারে, সেজন্য ফিতরা আদায়ের নির্দেশ দেওয়া হয়েছে।\n\n"
                        + "কুরআন ও হাদিসের আলোকে ফিতরার গুরুত্ব:\n\n"
                        + "কুরআনে সরাসরি ফিতরা শব্দের উল্লেখ না থাকলেও, দান-সদকার গুরুত্ব সম্পর্কে বহু আয়াত রয়েছে। আল্লাহ তাআলা বলেন:\n\n"
                        + "قَدْ أَفْلَحَ مَن تَزَكَّىٰ وَذَكَرَ ٱسْمَ رَبِّهِۦ فَصَلَّىٰ\n\n"
                        + "অর্থ:\n\n"
                        + "নিশ্চয় সে সফল হয়েছে, যে আত্মশুদ্ধি অর্জন করেছে এবং তার প্রতিপালকের নাম স্মরণ করে সালাত আদায় করেছে।\n\n"
                        + "[সূরা আল-আ'লা: ১৪-১৫]\n\n"
                        + "মুফাসসিরদের মতে, এখানে \"تَزَكَّىٰ\" দ্বারা সাদাকাতুল ফিতর আদায় করা এবং এরপর ঈদের নামাজ পড়ার কথা বলা হয়েছে।\n\n"
                        + "হাদিসে ফিতরার গুরুত্ব:\n\n"
                        + "হাদিসে ফিতরাকে অত্যন্ত গুরুত্ব দেওয়া হয়েছে এবং এটি আদায় করা ফরজ/ওয়াজিব বলে ঘোষণা করা হয়েছে। ইবনে আব্বাস রা. থেকে বর্ণিত:\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ.\n\n"
                        + "অর্থ:\n\n"
                        + "রাসুলুল্লাহ ﷺ সাদাকাতুল ফিতর নির্ধারণ করেছেন রোজাদারকে অনর্থক ও অশ্লীল কথা বা কাজের ত্রুটি থেকে পবিত্র করার জন্য এবং দরিদ্রদের খাদ্যের ব্যবস্থা করার জন্য।\n\n"
                        + "[সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭]\n\n"
                        + "ফিতরা কাদের উপর আবশ্যক (ওয়াজিব):\n\n"
                        + "ফিতরা প্রত্যেক সামর্থ্যবান মুসলিমের উপর আবশ্যক, যার কাছে ঈদের দিন ও রাতের প্রয়োজনীয় খরচের অতিরিক্ত অর্থ বা খাদ্যসামগ্রী থাকবে।\n\n"
                        + "পুরুষ, নারী, ছোট, বড়, ধনী, এমনকি নবজাতকের পক্ষ থেকেও ফিতরা আদায় করতে হয়।\n\n"
                        + "পরিবারের প্রধান তার নিজের এবং তার অধীনস্থদের (স্ত্রী, সন্তান) পক্ষ থেকে ফিতরা আদায় করবেন।",
                "Fitra, known as Sadaqatul Fitr (Sadaqah meaning charity, Fitr meaning breaking the fast or Eid al-Fitr), is a special charity given by Muslims before Eid at the end of Ramadan. It is an obligatory (Wajib) worship upon every capable Muslim, serving as a vital medium to assist the poor and helpless during Eid al-Fitr.\n\n"
                        + "Definition and Purpose of Fitra:\n\n"
                        + "Fitra is a prescribed financial support aimed at bringing smiles to the faces of the poor on Eid al-Fitr. It has two main objectives:\n\n"
                        + "1. Self-Purification and Fast Rectification:\n\n"
                        + "During fasting, inadvertent mistakes, idle chatter, or minor sins may occur. Through the payment of Fitra, forgiveness is attained and fasting achieves acceptance before Allah. The Messenger of Allah ﷺ said:\n\n"
                        + "'The fasts of Sha'ban and Ramadan remain suspended between the heavens and the earth, and they are elevated to Allah only through the payment of Sadaqatul Fitr.'\n\n"
                        + "[Kanz al-Ummal: 24130, Jami' al-Saghir: 4191]\n\n"
                        + "2. Including the Poor in Eid Joy:\n\n"
                        + "Fitra is commanded so that the impoverished and helpless do not go hungry on Eid day and can share the joyous celebration with everyone.\n\n"
                        + "Significance of Fitra in Light of the Quran and Hadith:\n\n"
                        + "Although the word 'Fitra' is not directly mentioned in the Quran, numerous verses emphasize the virtues of charity. Allah Almighty says:\n\n"
                        + "قَدْ أَفْلَحَ مَن تَزَكَّىٰ وَذَكَرَ ٱسْمَ رَبِّهِۦ فَصَلَّىٰ\n\n"
                        + "Meaning:\n\n"
                        + "'He has certainly succeeded who purifies himself, and mentions the name of his Lord and prays.'\n\n"
                        + "[Surah Al-A'la: 14-15]\n\n"
                        + "According to exegetes, 'تَزَكَّىٰ' here denotes paying Sadaqatul Fitr and subsequently proceeding to the Eid prayer.\n\n"
                        + "Significance of Fitra in Hadith:\n\n"
                        + "The Hadith places supreme importance on Fitra, establishing it as an obligatory duty. Ibn Abbas (RA) narrated:\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ.\n\n"
                        + "Meaning:\n\n"
                        + "'The Messenger of Allah ﷺ ordained Zakat al-Fitr as a purification for the fasting person from idle talk and obscenity, and as food for the needy.'\n\n"
                        + "[Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827]\n\n"
                        + "Upon Whom is Fitra Obligatory (Wajib):\n\n"
                        + "Fitra is obligatory upon every capable Muslim who possesses wealth or food beyond essential expenses for the day and night of Eid.\n\n"
                        + "It must be discharged for men, women, young, old, wealthy, and even newborn infants.\n\n"
                        + "The head of the family fulfills it on his own behalf and on behalf of his dependents (wife, children).",
                "সূরা আল-আ'লা: ১৪-১৫; সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭",
                "Surah Al-A'la: 14-15; Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827"
        ));

        // 2. ইসলামে ফিতরার বিধান
        list.add(new RozaFitraTopicItem(
                2,
                "fitra_ruling",
                "ইসলামে ফিতরার বিধান",
                "Ruling of Fitra in Islam",
                "ফিতরার বিধান",
                "Ruling of Fitra",
                "ইসলামে সাদাকাতুল ফিতর (ফিতরা) একটি গুরুত্বপূর্ণ ইবাদত এবং আর্থিক দান, যা রমজানের শেষে ঈদুল ফিতরের আগে সামর্থ্যবান মুসলিমদের জন্য আদায় করা ওয়াজিব (আবশ্যকীয়)। এটি ব্যক্তিগতভাবে আল্লাহর নির্দেশ মানা এবং সমাজের দরিদ্রদের সহায়তার জন্য একটি বিশেষ বিধান।",
                "In Islam, Sadaqatul Fitr (Fitra) is an essential act of worship and financial charity that is obligatory (Wajib) upon capable Muslims before Eid al-Fitr at the end of Ramadan. It is an individual obligation obeying Allah's command and supporting the destitute of society.",
                "ইসলামে সাদাকাতুল ফিতর (ফিতরা) একটি গুরুত্বপূর্ণ ইবাদত এবং আর্থিক দান, যা রমজানের শেষে ঈদুল ফিতরের আগে সামর্থ্যবান মুসলিমদের জন্য আদায় করা ওয়াজিব (আবশ্যকীয়)। এটি ব্যক্তিগতভাবে আল্লাহর নির্দেশ মানা এবং সমাজের দরিদ্রদের সহায়তার জন্য একটি বিশেষ বিধান।\n\n"
                        + "ফিতরার বিধান (হুকুম):\n\n"
                        + "ফিতরা আদায় করা ওয়াজিব। এটি প্রত্যেক সামর্থ্যবান মুসলিমের জন্য আবশ্যক, যিনি নিজে ও তার পরিবারকে ঈদের দিন মৌলিক প্রয়োজনীয়তা মেটানোর পর ফিতরার সম্পদ রাখেন। এটি কুরআন ও হাদিসের মাধ্যমে প্রমাণিত।\n\n"
                        + "কুরআনের আলোকে ফিতরার বিধান:\n\n"
                        + "যদিও ফিতরার কথা সরাসরি কুরআনে উল্লেখ নেই, তবে কুরআনে দান-সদকা এবং আত্মশুদ্ধির গুরুত্ব বারবার এসেছে। ফিতরা এই দানের একটি প্রকার। আল্লাহ বলেন,\n\n"
                        + "قَدْ أَفْلَحَ مَن تَزَكَّىٰ وَذَكَرَ ٱسْمَ رَبِّهِۦ فَصَلَّىٰ\n\n"
                        + "অর্থ:\n\n"
                        + "সফল হয়েছে সেই ব্যক্তি, যে পবিত্রতা অর্জন করেছে এবং তার রবের নাম স্মরণ করে সালাত আদায় করেছে।\n\n"
                        + "[সূরা আল-আ'লা: ১৪-১৫]\n\n"
                        + "ইবনে আব্বাস রা. এর ব্যাখ্যা অনুযায়ী, \"تَزَكَّىٰ\" শব্দটি ফিতরা আদায় করার প্রতি ইঙ্গিত করে।\n\n"
                        + "হাদিসের আলোকে ফিতরার বিধান:\n\n"
                        + "রাসুলুল্লাহ ﷺ ফিতরার বিধানকে সুস্পষ্টভাবে উল্লেখ করেছেন এবং এর গুরুত্ব তুলে ধরেছেন।\n\n"
                        + "হাদিসে এসেছে,\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ.\n\n"
                        + "অর্থ:\n\n"
                        + "রাসুলুল্লাহ ﷺ ফিতরা প্রদানকে ফরজ করেছেন, যা রোজাদারের জন্য অপ্রয়োজনীয় কথা ও কাজ থেকে পবিত্রতা এবং দরিদ্রদের খাদ্যের সংস্থান হিসেবে কাজ করে।\n\n"
                        + "[সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭]\n\n"
                        + "এ হাদিস থেকে বোঝা যায়, ফিতরা রোজার পবিত্রতা রক্ষা করে এবং সমাজের অভাবীদের সাহায্য করে। হাদিসে রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "صاعًا من تمر أو صاعًا من شعير على العبد والحر والذكر والأنثى والصغير والكبير من المسلمين.\n\n"
                        + "অর্থ:\n\n"
                        + "রাসুলুল্লাহ ﷺ বলেছেন, খেজুর বা যবের এক সা’ পরিমাণ ফিতরা আদায় করতে হবে, তা স্বাধীন বা দাস, পুরুষ বা নারী, ছোট বা বড় প্রত্যেক মুসলিমের জন্য।\n\n"
                        + "[সহিহ বুখারি: ১৫০৩, সহিহ মুসলিম: ৯৮৪]\n\n"
                        + "ফিতরা যারা আদায় করবে:\n\n"
                        + "প্রত্যেক মুসলিমের জন্য:\n\n"
                        + "যিনি নিজের মৌলিক প্রয়োজন মেটানোর পর ফিতরার পরিমাণ পরিমাণ সম্পদ রাখেন।\n\n"
                        + "সামর্থ্য অনুযায়ী:\n\n"
                        + "পুরুষ, নারী, শিশু এমনকি নবজাতকের জন্যও ফিতরা প্রদান করতে হবে।\n\n"
                        + "গৃহকর্তা দায়িত্বশীল:\n\n"
                        + "পরিবারের গৃহকর্তা তার পরিবারের সব সদস্যের পক্ষ থেকে ফিতরা আদায় করবেন।\n\n"
                        + "ফিতরার নির্ধারিত পরিমাণ:\n\n"
                        + "ফিতরার পরিমাণ রাসুলুল্লাহ ﷺ এর সময় নির্ধারিত ছিল খাদ্যশস্যের ভিত্তিতে।\n\n"
                        + "১ সা’ পরিমাণ খাদ্য:\n\n"
                        + "রাসুলুল্লাহ ﷺ খেজুর, যব, কিশমিশ বা গমের এক সা’ পরিমাণ ফিতরা নির্ধারণ করেছেন।\n\n"
                        + "১ সা’ = প্রায় ৩ কেজি:\n\n"
                        + "বর্তমানে এটি খাদ্যদ্রব্যের মূল্যের ভিত্তিতে হিসাব করা হয়। বিভিন্ন দেশে ইসলামিক স্কলাররা প্রতিটি পণ্যের জন্য ফিতরার মান নির্ধারণ করেন।\n\n"
                        + "ফিতরা আদায়ের সময়:\n\n"
                        + "সর্বোত্তম সময়:\n\n"
                        + "ঈদের নামাজে যাওয়ার আগে ফিতরা প্রদান করা।\n\n"
                        + "আগ্রিম প্রদান:\n\n"
                        + "রমজানের শেষের দিনগুলোতে বা আগেই ফিতরা প্রদান করা যেতে পারে।\n\n"
                        + "বিলম্ব না করা:\n\n"
                        + "ঈদের নামাজের পরে ফিতরা প্রদান করা হলে এটি সাধারণ সদকা হিসেবে গণ্য হবে, তবে ফিতরার মূল উদ্দেশ্য পূরণ হবে না। রাসুলুল্লাহ ﷺ বলেন,\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ.\n\n"
                        + "অর্থ:\n\n"
                        + "ঈদের দিনে দরিদ্রদের ভিক্ষার প্রয়োজন থেকে মুক্ত রাখো।\n\n"
                        + "[সহিহ ইবনে খুজাইমা: ২৪১৭]\n\n"
                        + "ফিতরার অর্থ কোথায় ব্যয় করা উচিত:\n\n"
                        + "ফিতরার অর্থ নিম্নলিখিত শ্রেণীর মানুষের মধ্যে বিতরণ করা যায়:\n\n"
                        + "দরিদ্র (ফকির)।\n"
                        + "অভাবগ্রস্ত (মিসকিন)।\n"
                        + "এতিম।\n"
                        + "ঋণগ্রস্ত ব্যক্তি।\n"
                        + "যারা নিজেদের মৌলিক চাহিদা পূরণে অক্ষম।\n\n"
                        + "উল্লেখযোগ্য বিষয়:\n\n"
                        + "ফিতরা দানের জন্য আত্মীয়-স্বজন, প্রতিবেশী এবং নিকটবর্তী দরিদ্রদের অগ্রাধিকার দেওয়া উচিত।",
                "In Islam, Sadaqatul Fitr (Fitra) is an essential act of worship and financial charity that is obligatory (Wajib) upon capable Muslims before Eid al-Fitr at the end of Ramadan. It is an individual obligation obeying Allah's command and supporting the destitute of society.\n\n"
                        + "Ruling (Hukm) of Fitra:\n\n"
                        + "Paying Fitra is Wajib. It is required of every capable Muslim who possesses wealth equal to or exceeding the Fitra amount after meeting basic necessities for themselves and their family on Eid day. This is established by Quran and Sunnah.\n\n"
                        + "Ruling of Fitra in Light of the Quran:\n\n"
                        + "Although Fitra is not mentioned by name directly in the Quran, charity and spiritual purification are repeatedly emphasized. Fitra is a form of this charity. Allah says:\n\n"
                        + "قَدْ أَفْلَحَ مَن تَزَكَّىٰ وَذَكَرَ ٱسْمَ رَبِّهِۦ فَصَلَّىٰ\n\n"
                        + "Meaning:\n\n"
                        + "He has certainly succeeded who purifies himself, and mentions the name of his Lord and prays.\n\n"
                        + "[Surah Al-A'la: 14-15]\n\n"
                        + "According to the explanation of Ibn Abbas (RA), the word \"تَزَكَّىٰ\" refers to paying Sadaqatul Fitr.\n\n"
                        + "Ruling of Fitra in Light of Hadith:\n\n"
                        + "The Messenger of Allah ﷺ explicitly clarified the ruling of Fitra and underscored its significance.\n\n"
                        + "In Hadith it is reported:\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ.\n\n"
                        + "Meaning:\n\n"
                        + "The Messenger of Allah ﷺ ordained Zakat al-Fitr as a purification for the fasting person from idle talk and obscenity, and as food for the needy.\n\n"
                        + "[Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827]\n\n"
                        + "This demonstrates that Fitra safeguards the sanctity of fasting and assists the destitute of society. The Prophet ﷺ also stated:\n\n"
                        + "صاعًا من تمر أو صاعًا من شعير على العبد والحر والذكر والأنثى والصغير والكبير من المسلمين.\n\n"
                        + "Meaning:\n\n"
                        + "One Sa' of dates or one Sa' of barley must be paid as Fitra for every Muslim, whether slave or free, male or female, young or old.\n\n"
                        + "[Sahih Bukhari: 1503, Sahih Muslim: 984]\n\n"
                        + "Who Must Pay Fitra:\n\n"
                        + "• Every Muslim: Anyone who possesses wealth beyond basic living necessities on Eid.\n"
                        + "• According to Capability: Must be given for males, females, children, and even newborn infants.\n"
                        + "• Head of Household: The head of the household pays on behalf of all dependents.\n\n"
                        + "Prescribed Amount of Fitra:\n\n"
                        + "During the time of the Prophet ﷺ, the amount was fixed based on staple food commodities:\n"
                        + "• 1 Sa' of Food: Dates, barley, raisins, or wheat.\n"
                        + "• 1 Sa' = Approximately 3 kg: Today this is calculated based on commodity market rates. Islamic scholars in each country establish the official standards.\n\n"
                        + "Time of Paying Fitra:\n\n"
                        + "• Best Time: Before proceeding to the Eid prayer.\n"
                        + "• Early Payment: Permissible in the final days of Ramadan or earlier.\n"
                        + "• Avoid Delay: If given after the Eid prayer, it counts only as ordinary voluntary charity (Sadaqah) and the primary purpose of Fitra is unfulfilled. The Prophet ﷺ said:\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ.\n\n"
                        + "Meaning:\n\n"
                        + "Enrich them so they do not have to beg on this day.\n\n"
                        + "[Sahih Ibn Khuzaymah: 2417]\n\n"
                        + "Where Fitra Funds Should Be Spent:\n\n"
                        + "Fitra may be distributed to the following eligible recipients:\n"
                        + "• Destitute (Faqir)\n"
                        + "• Poor/Needy (Miskin)\n"
                        + "• Orphans\n"
                        + "• Debt-ridden individuals\n"
                        + "• Those unable to meet basic life necessities\n\n"
                        + "Notable Note:\n\n"
                        + "Priority should be given to needy relatives, neighbors, and nearby poor individuals.",
                "সূরা আল-আ'লা: ১৪-১৫; সহিহ বুখারি: ১৫০৩, সহিহ মুসলিম: ৯৮৪, সহিহ আবু দাউদ: ১৬০৯",
                "Surah Al-A'la: 14-15; Sahih Bukhari: 1503, Sahih Muslim: 984, Sunan Abi Dawud: 1609"
        ));

        // 3. ফিতরার হিসাব
        list.add(new RozaFitraTopicItem(
                3,
                "fitra_calculation",
                "ফিতরার হিসাব",
                "Calculation of Fitra",
                "ফিতরার হিসাব",
                "Calculation of Fitra",
                "ফিতরার হিসাব নির্ধারণ রাসুলুল্লাহ ﷺ এর সময় খাদ্যশস্যের ভিত্তিতে করা হতো। বর্তমানে এটি স্থান, সময় এবং খাদ্যপণ্যের মূল্যের ভিত্তিতে নির্ধারিত হয়। এখানে ফিতরার হিসাবের পদ্ধতি এবং বর্তমান প্রেক্ষাপটে এর মূল্য নির্ধারণের পদ্ধতি ব্যাখ্যা করা হলো...",
                "The calculation of Fitra during the time of the Prophet ﷺ was determined on the basis of staple food commodities. Currently, it is determined based on region, time, and commodity prices...",
                "ফিতরার হিসাব নির্ধারণ রাসুলুল্লাহ ﷺ এর সময় খাদ্যশস্যের ভিত্তিতে করা হতো। বর্তমানে এটি স্থান, সময় এবং খাদ্যপণ্যের মূল্যের ভিত্তিতে নির্ধারিত হয়। এখানে ফিতরার হিসাবের পদ্ধতি এবং বর্তমান প্রেক্ষাপটে এর মূল্য নির্ধারণের পদ্ধতি ব্যাখ্যা করা হলো,\n\n"
                        + "ফিতরার পরিমাণ: রাসুলুল্লাহ ﷺ এর নির্দেশনা:\n\n"
                        + "হাদিসে বর্ণিত হয়েছে যে ফিতরা আদায়ের পরিমাণ ১ সা' খাদ্যশস্য। রাসুলুল্লাহ ﷺ বলেন,\n\n"
                        + "صاعًا من تمر أو صاعًا من شعير.\n\n"
                        + "অর্থ:\n\n"
                        + "এক সা' পরিমাণ খেজুর অথবা এক সা' পরিমাণ যব (ফিতরা হিসেবে দিতে হবে)।\n\n"
                        + "[সহিহ বুখারি: ১৫০৩, সহিহ মুসলিম: ৯৮৪]\n\n"
                        + "১ সা’ হলো রাসুলুল্লাহ ﷺ এর সময়ের একটি ওজন পরিমাপ পদ্ধতি, যা আনুমানিক ৩ কেজি বা ২.৭৫ কেজির কাছাকাছি হয়।\n\n"
                        + "ফিতরা আদায়:\n\n"
                        + "ফিতরা মূলত খাদ্যশস্যের দ্বারা আদায় করতে বলা হয়েছে, তবে বর্তমানে অনেক জায়গায় ফিতরা নগদ অর্থে প্রদান করা হয়। ফিতরা দেওয়ার জন্য প্রধান খাদ্যশস্যের তালিকা,\n\n"
                        + "খেজুর\n"
                        + "যব\n"
                        + "গম\n"
                        + "কিশমিশ\n"
                        + "পনির বা শুকনো দুধ\n"
                        + "অথবা স্থানীয়ভাবে প্রচলিত প্রধান খাদ্য যেমন চাল বা আটা\n\n"
                        + "খাদ্যশস্যের মাধ্যমে ফিতরা:\n\n"
                        + "রাসুলুল্লাহ ﷺ এর সময় সরাসরি খাদ্যশস্য প্রদান করা হতো। এখনো যেকোনো খাদ্যশস্য (যেমন গম, চাল, যব) দিয়ে ফিতরা প্রদান করা যায়।\n\n"
                        + "উদাহরণ:\n\n"
                        + "যদি গম দিয়ে ফিতরা দিতে হয় এবং ১ সা’ গমের পরিমাণ ২.৭৫ কেজি হয়, তবে ২.৭৫ কেজি গমের বর্তমান বাজারমূল্য দেখে ফিতরার হিসাব করা হবে।\n\n"
                        + "নগদ অর্থের মাধ্যমে ফিতরা:\n\n"
                        + "আজকাল অনেক স্কলার ও ইসলামিক সংস্থা স্থানীয় খাদ্যশস্যের গড়মূল্যের উপর ভিত্তি করে ফিতরার নগদ পরিমাণ নির্ধারণ করেন।\n\n"
                        + "উদাহরণ:\n\n"
                        + "যদি চাল দিয়ে ফিতরা আদায় করা হয় এবং প্রতি কেজি চালের মূল্য ৫০ টাকা হয়, তাহলে ফিতরার পরিমাণ = ২.৭৫ কেজি × ৫০ টাকা = ১৩৭.৫০ টাকা। এটি ফিতরার ন্যূনতম পরিমাণ।\n\n"
                        + "খাদ্যের ধরন অনুযায়ী ফিতরার মান (ভিন্নতা):\n\n"
                        + "ফিতরার পরিমাণ বিভিন্ন খাদ্যপণ্যের ভিত্তিতে ভিন্ন হতে পারে। যেমন,\n\n"
                        + "গম (আটা): ২.৭৫ কেজি গমের বর্তমান বাজারমূল্য।\n"
                        + "চাল: ২.৭৫ কেজি চালের বর্তমান বাজারমূল্য।\n"
                        + "যব: ২.৭৫ কেজি যবের বর্তমান বাজারমূল্য।\n"
                        + "খেজুর বা কিশমিশ: উন্নত মানের খেজুর বা কিশমিশের মূল্য অনুযায়ী হিসাব।\n\n"
                        + "ফিতরা আদায়ের সময়:\n\n"
                        + "সর্বোত্তম সময়: ঈদের নামাজে যাওয়ার আগে।\n\n"
                        + "আগ্রিম প্রদান: রমজানের শেষ দিনগুলোতে প্রদান করা যায়।\n\n"
                        + "বিলম্ব করলে: ঈদের নামাজের পর আদায় করলে এটি সাধারণ সদকা হিসেবে গণ্য হবে।\n\n"
                        + "ফিতরা নিম্নলিখিত ব্যক্তিদের দেওয়া যায়:\n\n"
                        + "দরিদ্র (ফকির)।\n"
                        + "অভাবগ্রস্ত (মিসকিন)।\n"
                        + "এতিম।\n"
                        + "ঋণগ্রস্ত ব্যক্তি।\n"
                        + "যারা নিজের মৌলিক চাহিদা পূরণে অক্ষম।\n\n"
                        + "গুরুত্বপূর্ণ:\n\n"
                        + "ফিতরা নিজের আত্মীয়স্বজন, প্রতিবেশী বা এলাকাবাসীকে দেওয়া উত্তম। যাদের ঈদের দিন খাবারের চাহিদা মেটানোর সামর্থ্য নেই, তাদের অগ্রাধিকার দিতে হবে।\n\n"
                        + "ফিতরা আদায়ের ইসলামী শিক্ষা ও তাৎপর্য:\n\n"
                        + "রোজার পরিপূর্ণতা: রমজানের রোজার মধ্যে যে ছোটখাটো ভুল-ত্রুটি হয়, ফিতরা তা পূরণ করে।\n\n"
                        + "দরিদ্রদের সহায়তা: এটি দরিদ্রদের ঈদের আনন্দে অংশগ্রহণ নিশ্চিত করে।\n\n"
                        + "সমাজে ভ্রাতৃত্ব ও সংহতি: ফিতরা প্রদানের মাধ্যমে ধনী-গরিবের মাঝে সম্প্রীতি বৃদ্ধি পায়।\n\n"
                        + "আল্লাহর আদেশ পালনের মাধ্যম: এটি কুরআন ও হাদিসে বর্ণিত গুরুত্বপূর্ণ ইবাদত, যা আখিরাতে মুক্তির পাথেয়।",
                "The calculation of Fitra during the time of the Prophet ﷺ was determined on the basis of staple food commodities. Currently, it is determined based on region, time, and commodity prices. Here the method of calculating Fitra and pricing standards in the contemporary context is explained,\n\n"
                        + "Prescribed Measure of Fitra: Instruction of the Prophet ﷺ:\n\n"
                        + "In Hadith it is narrated that the measure of paying Fitra is 1 Sa' of staple food. The Messenger of Allah ﷺ said:\n\n"
                        + "صاعًا من تمر أو صاعًا من شعير.\n\n"
                        + "Meaning:\n\n"
                        + "One Sa' of dates or one Sa' of barley (must be given as Fitra).\n\n"
                        + "[Sahih Bukhari: 1503, Sahih Muslim: 984]\n\n"
                        + "1 Sa' is a volumetric weight measurement method from the era of the Prophet ﷺ, which approximately equals 3 kg or close to 2.75 kg.\n\n"
                        + "Discharging Fitra:\n\n"
                        + "Fitra was originally prescribed to be discharged in staple food grains, but today it is frequently given in cash equivalent in many places. Primary staple food grains for giving Fitra include:\n\n"
                        + "• Dates\n"
                        + "• Barley\n"
                        + "• Wheat\n"
                        + "• Raisins\n"
                        + "• Cheese or Dried Milk\n"
                        + "• Or locally prevalent staple food such as rice or flour\n\n"
                        + "Fitra Through Food Grains:\n\n"
                        + "Direct food grains were distributed during the time of the Prophet ﷺ. Even now, Fitra can be given through any staple grain (e.g. wheat, rice, barley).\n\n"
                        + "Example:\n\n"
                        + "If Fitra is given using wheat and 1 Sa' of wheat is 2.75 kg, the calculation will be based on the prevailing market price of 2.75 kg wheat.\n\n"
                        + "Fitra Through Cash Money:\n\n"
                        + "Nowadays many scholars and Islamic institutions calculate the monetary value of Fitra based on average local staple food commodity rates.\n\n"
                        + "Example:\n\n"
                        + "If rice is used and each kg of rice costs 50 BDT, then Fitra = 2.75 kg × 50 BDT = 137.50 BDT. This is the minimum rate.\n\n"
                        + "Fitra Standards Based on Food Commodity:\n\n"
                        + "Fitra values vary depending on the chosen food commodity. For example:\n\n"
                        + "• Wheat (Flour): Current market price of 2.75 kg wheat.\n"
                        + "• Rice: Current market price of 2.75 kg rice.\n"
                        + "• Barley: Current market price of 2.75 kg barley.\n"
                        + "• Dates or Raisins: Calculated according to high quality dates or raisins.\n\n"
                        + "Time of Discharging Fitra:\n\n"
                        + "• Best Time: Before proceeding to the Eid prayer.\n\n"
                        + "• Early Payment: Permissible in the concluding days of Ramadan.\n\n"
                        + "• If Delayed: If discharged after the Eid prayer, it counts as voluntary charity.\n\n"
                        + "Fitra May Be Given to the Following:\n\n"
                        + "1. Destitute (Faqir)\n"
                        + "2. Poor/Needy (Miskin)\n"
                        + "3. Orphans\n"
                        + "4. Debt-ridden individuals\n"
                        + "5. Those incapable of fulfilling basic necessities\n\n"
                        + "Important:\n\n"
                        + "It is best to give Fitra to needy relatives, neighbors, or local community members who lack the means to eat on Eid day.\n\n"
                        + "Islamic Lessons and Significance of Fitra:\n\n"
                        + "• Completion of Fasting: Compensates for minor inadvertent mistakes during Ramadan.\n\n"
                        + "• Aiding the Needy: Ensures poor individuals share in Eid joy.\n\n"
                        + "• Fraternity and Solidarity: Deepens harmony between wealthy and less fortunate.\n\n"
                        + "• Obeying Divine Command: A vital worship prescribed in Quran and Sunnah.",
                "সহিহ বুখারি: ১৫০৩, সহিহ মুসলিম: ৯৮৪",
                "Sahih Bukhari: 1503, Sahih Muslim: 984"
        ));

        // 4. ফিতরার সামাজিক গুরুত্ব
        list.add(new RozaFitraTopicItem(
                4,
                "fitra_social_importance",
                "ফিতরার সামাজিক গুরুত্ব",
                "Social Importance of Fitra",
                "ফিতরার সামাজিক গুরুত্ব",
                "Social Importance of Fitra",
                "ইসলামে সাদাকাতুল ফিতর (ফিতরা) শুধুমাত্র একটি ইবাদত নয়, বরং এটি সমাজে দরিদ্রদের সাহায্য এবং ভ্রাতৃত্ব প্রতিষ্ঠার একটি গুরুত্বপূর্ণ মাধ্যম। ফিতরার মাধ্যমে ঈদের দিন ধনী ও গরিব একসঙ্গে আনন্দ করতে পারে। এটি ব্যক্তিগত আত্মশুদ্ধি এবং সামাজিক সমতার সুন্দর উদাহরণ। নিচে ফিতরার সামাজিক গুরুত্ব বিস্তারিতভাবে আলোচনা করা হলো...",
                "In Islam, Sadaqatul Fitr (Fitra) is not merely an act of worship, but also a vital medium for aiding the poor and establishing fraternity in society. Through Fitra, the rich and the poor can rejoice together on Eid day. It is a beautiful example of individual self-purification and social equity...",
                "ইসলামে সাদাকাতুল ফিতর (ফিতরা) শুধুমাত্র একটি ইবাদত নয়, বরং এটি সমাজে দরিদ্রদের সাহায্য এবং ভ্রাতৃত্ব প্রতিষ্ঠার একটি গুরুত্বপূর্ণ মাধ্যম। ফিতরার মাধ্যমে ঈদের দিন ধনী ও গরিব একসঙ্গে আনন্দ করতে পারে। এটি ব্যক্তিগত আত্মশুদ্ধি এবং সামাজিক সমতার সুন্দর উদাহরণ। নিচে ফিতরার সামাজিক গুরুত্ব বিস্তারিতভাবে আলোচনা করা হলো,\n\n"
                        + "দরিদ্রদের জন্য খাদ্যের সংস্থান:\n\n"
                        + "ফিতরার অন্যতম প্রধান উদ্দেশ্য হলো দরিদ্র ও অসহায় মানুষদের ঈদের দিনে খাবারের সংস্থান নিশ্চিত করা। রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ\n\n"
                        + "অর্থ:\n\n"
                        + "ঈদের দিনে তাদের (দরিদ্রদের) ভিক্ষার প্রয়োজন থেকে মুক্ত রাখো।\n\n"
                        + "[সহিহ ইবনে খুজাইমা: ২৪১৭]\n\n"
                        + "ফিতরা নিশ্চিত করে যে দরিদ্ররা ঈদের দিনে ভিক্ষা না করে সম্মানিতভাবে তাদের খাদ্যের প্রয়োজন পূরণ করতে পারে। এটি সমাজের দরিদ্র শ্রেণীর জন্য আশীর্বাদস্বরূপ।\n\n"
                        + "ঈদের আনন্দ সবার জন্য নিশ্চিত করা:\n\n"
                        + "ফিতরা ধনী-গরিব নির্বিশেষে ঈদের আনন্দ ভাগাভাগি করার সুযোগ করে দেয়। ফিতরা দরিদ্রদের নতুন কাপড়, খাবার এবং ঈদের অন্যান্য প্রয়োজন মেটাতে সাহায্য করে। এটি ঈদের দিনে ধনী ও গরিবের মধ্যে বিভেদ কমিয়ে দেয় এবং সমাজে একটি সামগ্রিক আনন্দময় পরিবেশ তৈরি করে।\n\n"
                        + "সমাজে ভ্রাতৃত্ব ও সংহতি বৃদ্ধি:\n\n"
                        + "ফিতরা ধনী ও গরিবের মধ্যে দূরত্ব দূর করে এবং ভ্রাতৃত্বের বন্ধন দৃঢ় করে। ধনীরা যখন দরিদ্রদের ফিতরা প্রদান করেন, তখন এটি তাদের মধ্যে সহানুভূতি ও মানবিকতা সৃষ্টি করে। গরিবরা ঈদের দিন নিজেদেরকে সমাজের অংশ মনে করে এবং এক ধরনের সামাজিক নিরাপত্তা অনুভব করে।\n\n"
                        + "দারিদ্র্য বিমোচনে ভূমিকা:\n\n"
                        + "ফিতরা সমাজের অর্থনৈতিক ভারসাম্য রক্ষা করে। ধনীরা তাদের সম্পদ থেকে দরিদ্রদের অংশ প্রদান করে, যা তাদের দৈনন্দিন জীবনে সাহায্য করে। ফিতরা একটি অর্থনৈতিক পুনঃবণ্টনের ব্যবস্থা, যা ধনী-গরিবের মধ্যকার অর্থনৈতিক ব্যবধান হ্রাস করে।\n\n"
                        + "ভিক্ষাবৃত্তি কমানো:\n\n"
                        + "ফিতরার মাধ্যমে দরিদ্ররা ঈদের সময় তাদের মৌলিক চাহিদাগুলো পূরণ করতে পারে। এর ফলে ঈদের দিনে দরিদ্রদের ভিক্ষার প্রয়োজন পড়ে না। এটি ভিক্ষাবৃত্তি কমাতে এবং সমাজে সম্মানজনক জীবনযাপন নিশ্চিত করতে সহায়ক।\n\n"
                        + "রমজানের শিক্ষার বাস্তবায়ন:\n\n"
                        + "রমজান আমাদের ত্যাগ, সংযম ও দানশীলতার শিক্ষা দেয়। ফিতরা হলো এই শিক্ষার বাস্তবায়নের একটি মাধ্যম। এটি ধনীদের মধ্যে ত্যাগের শিক্ষা এবং দরিদ্রদের জন্য সহানুভূতির চর্চা করে। ফিতরা দানের মাধ্যমে রমজানের সংযম ও কৃপার শিক্ষা বাস্তব জীবনে প্রয়োগ করা হয়।\n\n"
                        + "সামাজিক ন্যায়বিচার প্রতিষ্ঠা:\n\n"
                        + "ফিতরা সমাজের প্রতিটি মানুষের মৌলিক অধিকার নিশ্চিত করে। এটি দরিদ্রদের তাদের অধিকার আদায়ের সুযোগ দেয়। ধনীদের থেকে সম্পদের একটি অংশ দরিদ্রদের কাছে পৌঁছানো ন্যায়বিচারের প্রতীক।\n\n"
                        + "মানবিক মূল্যবোধ বৃদ্ধি:\n\n"
                        + "ফিতরা আমাদের মধ্যে মানবিক মূল্যবোধের বিকাশ ঘটায়। ধনীরা যখন দরিদ্রদের সহায়তা করে, তখন তাদের মধ্যে কৃতজ্ঞতা এবং তাওহীদের প্রতি আনুগত্য বাড়ে। গরিবরা এই দান গ্রহণ করে সম্মানের সঙ্গে ঈদ পালন করতে পারে।\n\n"
                        + "দানশীলতার চর্চা এবং আল্লাহর সন্তুষ্টি লাভ:\n\n"
                        + "ফিতরা দানশীলতার মাধ্যমে আল্লাহর প্রতি কৃতজ্ঞতা প্রকাশের একটি মাধ্যম। এটি ধনী ব্যক্তিদের শিখায় যে তাদের সম্পদে গরিবদেরও অধিকার রয়েছে। ফিতরা আদায় করে আল্লাহর আদেশ পালন করা হয়, যা আখিরাতে সফলতার কারণ।\n\n"
                        + "সমাজে পারস্পরিক সহযোগিতা বৃদ্ধি:\n\n"
                        + "ফিতরা ধনী ও গরিবের মধ্যে পারস্পরিক সহযোগিতার সম্পর্ক তৈরি করে। ধনীরা ফিতরা প্রদানের মাধ্যমে গরিবদের পাশে দাঁড়ায়। গরিবরা ধনীদের থেকে সাহায্য পেয়ে তাদের প্রতি কৃতজ্ঞ থাকে।",
                "In Islam, Sadaqatul Fitr (Fitra) is not merely an act of worship, but also a vital medium for aiding the poor and establishing fraternity in society. Through Fitra, the rich and the poor can rejoice together on Eid day. It is a beautiful example of individual self-purification and social equity. Below, the social significance of Fitra is discussed in detail,\n\n"
                        + "Provision of Food for the Poor:\n\n"
                        + "One of the primary objectives of Fitra is to ensure the provision of food for poor and helpless individuals on Eid day. The Messenger of Allah ﷺ said:\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ\n\n"
                        + "Meaning:\n\n"
                        + "Enrich them on this day so they do not have to beg.\n\n"
                        + "[Sahih Ibn Khuzaymah: 2417]\n\n"
                        + "Fitra ensures that the poor can satisfy their food needs with dignity on Eid without having to beg. It is a profound blessing for the destitute stratum of society.\n\n"
                        + "Ensuring Eid Joy for Everyone:\n\n"
                        + "Fitra provides the opportunity to share Eid happiness regardless of rich or poor. Fitra helps the poor acquire new clothing, wholesome food, and meet other Eid necessities. It diminishes disparities between the wealthy and the impoverished on Eid, fostering an all-embracing festive atmosphere across society.\n\n"
                        + "Strengthening Fraternity and Solidarity in Society:\n\n"
                        + "Fitra bridges the gap between the affluent and the underprivileged, reinforcing bonds of brotherhood. When the wealthy disburse Fitra to the poor, it engenders mutual empathy and humaneness. On Eid day, the poor feel recognized as integral members of the community and experience genuine social security.\n\n"
                        + "Role in Poverty Alleviation:\n\n"
                        + "Fitra preserves social economic equilibrium. The wealthy allocate a portion of their resources to the poor, aiding them in daily living. Fitra serves as a mechanism of economic redistribution, reducing the financial divide between rich and poor.\n\n"
                        + "Curtailing Begging:\n\n"
                        + "Through Fitra, the poor can fulfill their fundamental needs during Eid festivities. Consequently, there is no need for destitute people to beg on Eid day. This helps suppress begging and guarantees a dignified life within society.\n\n"
                        + "Realization of Ramadan's Teachings:\n\n"
                        + "Ramadan inculcates lessons of sacrifice, self-restraint, and benevolence. Fitra is a vital channel for actualizing these teachings. It imparts lessons of sacrifice among the affluent and cultivates active empathy for the poor. Through Fitra charity, the restraint and mercy of Ramadan are tangibly applied to practical life.\n\n"
                        + "Establishing Social Justice:\n\n"
                        + "Fitra guarantees the fundamental rights of every member in society. It grants the poor the opportunity to obtain their rightful share. Channeling a fraction of wealth from the affluent to the poor is the emblem of social justice.\n\n"
                        + "Fostering Human Values:\n\n"
                        + "Fitra stimulates the flourishing of noble human values within us. When the affluent assist the needy, it deepens gratitude and devotion to Tawheed. In turn, receiving this charity enables the poor to celebrate Eid with dignity.\n\n"
                        + "Cultivating Generosity and Attaining Allah's Pleasure:\n\n"
                        + "Fitra is a vehicle for expressing thankfulness to Allah through charitable generosity. It educates wealthy individuals that the poor hold a rightful claim upon their wealth. Discharging Fitra fulfills Allah's command, which is the key to salvation in the Hereafter.\n\n"
                        + "Fostering Mutual Cooperation in Society:\n\n"
                        + "Fitra establishes a relationship of reciprocal cooperation between the affluent and the underprivileged. The rich stand beside the needy through Fitra contributions, while the poor receive support and harbor sincere appreciation for the wealthy.",
                "সহিহ ইবনে খুজাইমা: ২৪১৭",
                "Sahih Ibn Khuzaymah: 2417"
        ));

        // 5. ফিতরা প্রদানের নিয়ম ও পদ্ধতি
        list.add(new RozaFitraTopicItem(
                5,
                "fitra_rules_methods",
                "ফিতরা প্রদানের নিয়ম ও পদ্ধতি",
                "Rules and Methods of Giving Fitra",
                "ফিতরা প্রদানের নিয়ম ও পদ্ধতি",
                "Rules and Methods of Giving Fitra",
                "সাদাকাতুল ফিতর (ফিতরা) ইসলামের একটি গুরুত্বপূর্ণ আর্থিক ইবাদত, যা প্রত্যেক সামর্থ্যবান মুসলিমের জন্য ঈদুল ফিতরের আগে বা ঈদের দিন আদায় করা ওয়াজিব। এর মাধ্যমে রমজানের রোজার ত্রুটি পূরণ করা হয় এবং দরিদ্রদের সাহায্য করা হয়। ফিতরা প্রদা...",
                "Sadaqatul Fitr (Fitra) is an important financial act of worship in Islam, which is obligatory (Wajib) upon every capable Muslim to discharge before Eid al-Fitr or on Eid day...",
                "সাদাকাতুল ফিতর (ফিতরা) ইসলামের একটি গুরুত্বপূর্ণ আর্থিক ইবাদত, যা প্রত্যেক সামর্থ্যবান মুসলিমের জন্য ঈদুল ফিতরের আগে বা ঈদের দিন আদায় করা ওয়াজিব। এর মাধ্যমে রমজানের রোজার ত্রুটি পূরণ করা হয় এবং দরিদ্রদের সাহায্য করা হয়। ফিতরা প্রদানের নিয়ম ও পদ্ধতি সম্পর্কে নিচে বিস্তারিত আলোচনা করা হলো।\n\n"
                        + "ফিতরা প্রদানের সময়:\n\n"
                        + "ফিতরা প্রদানের জন্য সময়ের নির্দিষ্ট বিধান রয়েছে। এটি রমজানের শেষ দিন এবং ঈদের দিনের মধ্যে আদায় করতে হয়। ফিতরা প্রদানের সর্বোত্তম সময় ঈদের নামাজের আগে। রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "من أداها قبل الصلاة فهي زكاة مقبولة، ومن أداها بعد الصلاة فهي صدقة من الصدقات\n\n"
                        + "অর্থ:\n\n"
                        + "যে ব্যক্তি ঈদের নামাজের আগে ফিতরা প্রদান করবে, তা হবে গ্রহণযোগ্য জাকাত। আর যদি নামাজের পরে প্রদান করে, তবে তা সাধারণ সদকা হিসেবে গণ্য হবে।\n\n"
                        + "[সহিহ আবু দাউদ: ১৬০৯]\n\n"
                        + "আগে প্রদান করা:\n\n"
                        + "রমজানের শেষের দিনগুলোতেও ফিতরা প্রদান করা বৈধ। বিশেষত, যদি ফিতরা প্রাপকদের কাছে সময়মতো পৌঁছানোর জন্য এটি আগে প্রদান করতে হয়।\n\n"
                        + "বিলম্ব না করা:\n\n"
                        + "ঈদের নামাজের পরে ফিতরা দিলে এটি ফিতরা হিসেবে গণ্য হবে না, বরং সাধারণ সদকা হিসেবে বিবেচিত হবে।\n\n"
                        + "ফিতরা আদায়ের পরিমাণ:\n\n"
                        + "ফিতরার নির্দিষ্ট পরিমাণ রাসুলুল্লাহ ﷺ নির্ধারণ করেছেন, যা খাদ্যশস্যের মাধ্যমে আদায় করা হয়।\n\n"
                        + "১ সা’ খাদ্য, ফিতরার পরিমাণ হলো ১ সা' খাদ্যশস্য। ১ সা’ = প্রায় ২.৭৫ থেকে ৩ কেজি।\n\n"
                        + "খাদ্যশস্য হতে পারে:\n\n"
                        + "গম বা আটা,\n"
                        + "চাল,\n"
                        + "যব,\n"
                        + "খেজুর,\n"
                        + "কিশমিশ।\n\n"
                        + "বর্তমান বাজারমূল্যের ভিত্তিতে নগদ ফিতরা:\n\n"
                        + "যদি খাদ্যশস্য প্রদান সম্ভব না হয়, তবে খাদ্যশস্যের মূল্যের সমপরিমাণ অর্থ প্রদান করা যায়। গম, চাল বা অন্য খাদ্যশস্যের বাজারদর অনুযায়ী স্থানীয় স্কলার বা ইসলামিক সংস্থা প্রতি বছরের জন্য ফিতরার মান নির্ধারণ করে। উদাহরণস্বরূপ, যদি চালের বর্তমান মূল্য প্রতি কেজি ৫০ টাকা হয়, তবে ২.৭৫ কেজি চালের ফিতরা = ১৩৭.৫০ টাকা। গরিবদের জন্য বেশি উপকার হয় এমন খাদ্য বা অর্থ প্রদান করাই উত্তম।\n\n"
                        + "যারা ফিতরা প্রদান করবে:\n\n"
                        + "সামর্থ্যবান মুসলিমদের ওপর: যিনি ঈদের দিন নিজের ও পরিবারের মৌলিক চাহিদা পূরণের পর অতিরিক্ত কিছু সম্পদ রাখেন। এটি ধনী-গরিব নির্বিশেষে প্রত্যেক সামর্থ্যবান ব্যক্তির জন্য আবশ্যক।\n\n"
                        + "পরিবারের দায়িত্ব: একজন ব্যক্তি তার পরিবারের সদস্যদের পক্ষ থেকে ফিতরা প্রদান করবে। শিশু, নারী, এমনকি নবজাতকের পক্ষ থেকেও ফিতরা প্রদান করতে হবে।",
                "Sadaqatul Fitr (Fitra) is an important financial act of worship in Islam, which is obligatory (Wajib) upon every capable Muslim to discharge before Eid al-Fitr or on Eid day. Through it, inadvertent shortcomings of Ramadan's fasting are remedied and the poor are supported. The rules and methods of giving Fitra are discussed in detail below.\n\n"
                        + "Time of Giving Fitra:\n\n"
                        + "There are specific temporal rulings for giving Fitra. It must be discharged between the concluding days of Ramadan and the day of Eid. The optimal time for paying Fitra is before the Eid prayer. The Messenger of Allah ﷺ said:\n\n"
                        + "من أداها قبل الصلاة فهي زكاة مقبولة، ومن أداها بعد الصلاة فهي صدقة من الصدقات\n\n"
                        + "Meaning:\n\n"
                        + "Whoever pays it before the Eid prayer, it is an accepted Zakat, and whoever pays it after the prayer, it is merely one of the charities.\n\n"
                        + "[Sahih Abi Dawud: 1609]\n\n"
                        + "Paying in Advance:\n\n"
                        + "It is also permissible to give Fitra during the concluding days of Ramadan. In particular, if it needs to be delivered early so that it reaches the deserving recipients in due time.\n\n"
                        + "Not Delaying:\n\n"
                        + "If Fitra is given after the Eid prayer, it will not count as Fitra, but will rather be treated as ordinary voluntary charity.\n\n"
                        + "Amount of Fitra to be Paid:\n\n"
                        + "The Prophet ﷺ prescribed a specific quantity of Fitra, which is discharged through staple food grains.\n\n"
                        + "1 Sa' of Food: The prescribed quantity of Fitra is 1 Sa' of food grains. 1 Sa' = approximately 2.75 to 3 kilograms.\n\n"
                        + "Food grains can be:\n\n"
                        + "• Wheat or flour,\n"
                        + "• Rice,\n"
                        + "• Barley,\n"
                        + "• Dates,\n"
                        + "• Raisins.\n\n"
                        + "Cash Fitra Based on Current Market Value:\n\n"
                        + "If giving staple food grains is not feasible, the equivalent monetary value of the food grain can be paid. Based on current market rates of wheat, rice, or other staples, local Islamic scholars or organizations determine annual Fitra rates each year. For instance, if the current price of rice is 50 BDT per kg, then Fitra for 2.75 kg rice = 137.50 BDT. It is best to give whichever staple food or monetary amount brings greater benefit to the poor.\n\n"
                        + "Who Must Pay Fitra:\n\n"
                        + "Upon Capable Muslims: Anyone who possesses surplus wealth beyond fundamental needs for themselves and their dependents on Eid day. This is obligatory upon every capable person regardless of rich or poor.\n\n"
                        + "Family Responsibility: A person must pay Fitra on behalf of the members of their household. It must also be paid on behalf of children, women, and even newborn babies.",
                "সহিহ আবু দাউদ: ১৬০৯",
                "Sahih Abi Dawud: 1609"
        ));

        // 6. ফিতরা সম্পর্কিত মাসআলা-মাসায়েল
        list.add(new RozaFitraTopicItem(
                6,
                "fitra_masayel",
                "ফিতরা সম্পর্কিত মাসআলা-মাসায়েল",
                "Masayel Regarding Fitra",
                "টাকা দ্বারা ফিৎরা আদায়ের রীতি ইসলামের সোনালী যুগে ছিল না। রাসূলুল্লাহ (ছাঃ) ও ছাহাবায়ে কেরাম টাকা দ্বারা ফিৎরা আদায় করেছেন মর্মে কোন প্রমাণ পাওয়া যায় না...",
                "The practice of paying Fitra in cash did not exist in the golden era of Islam. There is no evidence that the Messenger of Allah ﷺ and his companions paid Fitra in cash...",
                "ফিতরা সম্পর্কিত জরুরি ফিকহী মাসআলাসমূহ:\n\n"
                        + "১. নিসাবের শর্ত:\n\n"
                        + "ফিতরা ওয়াজিব হওয়ার জন্য সম্পদের উপর এক বছর অতিবাহিত হওয়া আবশ্যক নয়। ঈদের দিন যার কাছে নিজের ও পরিবারের মৌলিক প্রয়োজনের অতিরিক্ত খাদ্য বা সম্পদ থাকে, তার উপরই ফিতরা প্রযোজ্য।\n\n"
                        + "২. গর্ভস্থ অনাগত সন্তানের ফিতরা:\n\n"
                        + "মায়ের গর্ভে থাকা সন্তানের পক্ষ থেকে ফিতরা দেওয়া ওয়াজিব নয়। তবে কোনো ব্যক্তি যদি মুস্তাহাব আমল হিসেবে বা সাওয়াবের নিয়তে দান করে, তবে তা উত্তম (হযরত উসমান রা. গর্ভস্থ সন্তানের পক্ষ থেকেও ফিতরা দিতেন)। কিন্তু ঈদের সুবহে সাদিকের পূর্বে কোনো সন্তান জন্ম নিলে তার পক্ষ থেকে ফিতরা ওয়াজিব হবে।\n\n"
                        + "৩. স্ত্রীর ফিতরা কে দেবে:\n\n"
                        + "হানাফী মাযহাব মতে, স্বামী তার স্ত্রীর ভরণপোষণের দায়িত্বশীল হলেও স্ত্রীর নিজস্ব সম্পদ থাকলে তিনি নিজ ফিতরা আদায় করতে পারেন; তবে স্বামী যদি স্ত্রীর সম্মতিক্রমে তার পক্ষ থেকে আদায় করে দেন, তবে তা অনায়াসে আদায় হয়ে যাবে।\n\n"
                        + "৪. একাধিক ফিতরা এক মিসকীনকে দেওয়া:\n\n"
                        + "পরিবারের একাধিক সদস্যের ফিতরা একত্রিত করে একজন মাত্র মিসকীনকে দেওয়া জায়েয, আবার একজন ব্যক্তির ফিতরা বণ্টন করে একাধিক মিসকীনের মাঝে প্রদান করাও সম্পূর্ণরূপে বৈধ।",
                "Key Fiqh Masayel of Sadaqatul Fitr:\n\n"
                        + "1. Wealth Condition:\n\n"
                        + "Holding wealth for a complete lunar year is not required. Possession of essentials beyond one day and night on Eid establishes the obligation.\n\n"
                        + "2. Unborn Fetus:\n\n"
                        + "Paying for an unborn fetus in the womb is not obligatory, though considered Mustahabb (recommended) if voluntarily given, as practiced by Uthman (RA). However, if an infant is born before dawn on Eid, Fitr is strictly due.\n\n"
                        + "3. Spouse's Payment:\n\n"
                        + "A husband may pay on behalf of his wife with mutual agreement, which fully satisfies the obligation.\n\n"
                        + "4. Pooling and Portioning:\n\n"
                        + "Multiple Fitra allocations may be consolidated and granted to a single poor family, or one allocation distributed among several destitute individuals.",
                "আল-হিদায়া ১/১০৫; মুসান্নাফে ইবনে আবি শাইবাহ ৩/১৭৪; ফাতাওয়া তাতারখানিয়া ২/২০৭",
                "Al-Hidayah 1/105; Musannaf Ibn Abi Shaybah 3/174; Fatawa Tatarkhaniya 2/207"
        ));

        // 7. ফিতরার ইতিহাস ও প্রাসঙ্গিকতা
        list.add(new RozaFitraTopicItem(
                7,
                "fitra_history",
                "ফিতরার ইতিহাস ও প্রাসঙ্গিকতা",
                "History and Relevance of Fitra",
                "ফিতরার ইতিহাস ও প্রাসঙ্গিকতা",
                "History and Relevance of Fitra",
                "সাদাকাতুল ফিতর (ফিতরা) ইসলামে একটি গুরুত্বপূর্ণ আর্থিক ইবাদত, যা ঈদুল ফিতরের আগে প্রত্যেক সামর্থ্যবান মুসলিমের জন্য আদায় করা ওয়াজিব (আবশ্যকীয়)। এটি রাসুলুল্লাহ ﷺ কর্তৃক প্রতিষ্ঠিত একটি বিধান, যার মূল উদ্দেশ্য দরিদ্রদের সহায়তা করা এবং ঈদের আনন্দ সবার মাঝে ছড়িয়ে দেওয়া। ফিতরার ইতিহাস ও প্রাসঙ্গিকতা সমাজের অর্থনৈতিক ভারসাম্য এবং ভ্রাতৃত্বের নিদর্শন।",
                "Sadaqatul Fitr (Fitra) is an essential financial act of worship in Islam, which is obligatory (Wajib) upon every capable Muslim to discharge before Eid al-Fitr. It is an ordinance established by the Messenger of Allah ﷺ, the primary purpose of which is to assist the poor and spread the festive joy of Eid among everyone. The history and relevance of Fitra stand as a testimony to social economic balance and fraternity.",
                "সাদাকাতুল ফিতর (ফিতরা) ইসলামে একটি গুরুত্বপূর্ণ আর্থিক ইবাদত, যা ঈদুল ফিতরের আগে প্রত্যেক সামর্থ্যবান মুসলিমের জন্য আদায় করা ওয়াজিব (আবশ্যকীয়)। এটি রাসুলুল্লাহ ﷺ কর্তৃক প্রতিষ্ঠিত একটি বিধান, যার মূল উদ্দেশ্য দরিদ্রদের সহায়তা করা এবং ঈদের আনন্দ সবার মাঝে ছড়িয়ে দেওয়া। ফিতরার ইতিহাস ও প্রাসঙ্গিকতা সমাজের অর্থনৈতিক ভারসাম্য এবং ভ্রাতৃত্বের নিদর্শন।\n\n"
                        + "ফিতরার প্রবর্তন:\n\n"
                        + "সাদাকাতুল ফিতর (ফিতরা) ইসলামের সূচনায়, রাসুলুল্লাহ ﷺ এর মাধ্যমে প্রবর্তিত হয়। এটি রমজান মাসে রোজার পরিশুদ্ধি এবং দরিদ্রদের সহায়তার জন্য প্রবর্তিত হয়েছিল। রাসুলুল্লাহ ﷺ এর সময় ফিতরা খাদ্যশস্যের মাধ্যমে আদায় করা হতো। যেমন: খেজুর, যব, গম ইত্যাদি।\n\n"
                        + "রাসুলুল্লাহ ﷺ এর আদেশ:\n\n"
                        + "রাসুলুল্লাহ ﷺ ফিতরাকে ফরজ করেছেন এবং এর মূল উদ্দেশ্য দুটি বলে উল্লেখ করেছেন। রোজার মধ্যে যেসব ভুল-ত্রুটি হয়েছে, তা শোধরানো। দরিদ্রদের খাদ্য সহায়তা প্রদান করা, যেন তারা ঈদের দিন আনন্দ করতে পারে। হাদিসে এসেছে,\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ\n\n"
                        + "অর্থ:\n\n"
                        + "রাসুলুল্লাহ ﷺ ফিতরা প্রদানকে ফরজ করেছেন, যা রোজাদারের জন্য অপ্রয়োজনীয় কথা ও কাজ থেকে পবিত্রতা এবং দরিদ্রদের খাদ্যের সংস্থান হিসেবে কাজ করে।\n\n"
                        + "[সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭]\n\n"
                        + "আত্মশুদ্ধি ও রোজার পূর্ণতা:\n\n"
                        + "রমজানের রোজা আল্লাহর নৈকট্য অর্জনের জন্য রাখা হয়। তবে রোজা রাখার সময় ভুলত্রুটি হওয়া স্বাভাবিক। ফিতরা আদায় রোজার ত্রুটি-বিচ্যুতি পূরণ করার একটি মাধ্যম। এটি ব্যক্তির আত্মশুদ্ধি ঘটায় এবং আল্লাহর সন্তুষ্টি অর্জনের পথে সহায়ক হয়।\n\n"
                        + "দরিদ্রদের সাহায্য ও সমর্থন:\n\n"
                        + "ফিতরার অন্যতম লক্ষ্য হলো দরিদ্রদের সহায়তা করা। এটি ঈদের দিনে গরিবদের জন্য খাদ্য ও মৌলিক চাহিদা পূরণের সুযোগ তৈরি করে। রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ\n\n"
                        + "অর্থ:\n\n"
                        + "ঈদের দিনে তাদের (দরিদ্রদের) ভিক্ষার প্রয়োজন থেকে মুক্ত রাখো।\n\n"
                        + "[সহিহ ইবনে খুজাইমা: ২৪১৭]",
                "Sadaqatul Fitr (Fitra) is an essential financial act of worship in Islam, which is obligatory (Wajib) upon every capable Muslim to discharge before Eid al-Fitr. It is an ordinance established by the Messenger of Allah ﷺ, the primary purpose of which is to assist the poor and spread the festive joy of Eid among everyone. The history and relevance of Fitra stand as a testimony to social economic balance and fraternity.\n\n"
                        + "Institution of Fitra:\n\n"
                        + "Sadaqatul Fitr (Fitra) was instituted at the dawn of Islam through the Messenger of Allah ﷺ. It was ordained for the purification of the fast during the month of Ramadan and for supporting the destitute. In the era of the Messenger of Allah ﷺ, Fitra was discharged through staple food grains such as dates, barley, wheat, etc.\n\n"
                        + "Command of the Messenger of Allah ﷺ:\n\n"
                        + "The Messenger of Allah ﷺ made Fitra obligatory and specified two principal objectives for it: remedying shortcomings that occurred during fasting, and providing food assistance to the poor so they can rejoice on the day of Eid. In Hadith it is reported:\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ\n\n"
                        + "Meaning:\n\n"
                        + "The Messenger of Allah ﷺ ordained Zakat al-Fitr as a purification for the fasting person from idle talk and obscenity, and as food for the needy.\n\n"
                        + "[Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827]\n\n"
                        + "Self-Purification and Perfection of the Fast:\n\n"
                        + "The fasting of Ramadan is observed to attain closeness to Allah. However, unintentional mistakes during fasting are natural. Discharging Fitra serves as a means of remedying the defects of fasting. It cleanses the individual's soul and aids in attaining Allah's divine pleasure.\n\n"
                        + "Aid and Support for the Poor:\n\n"
                        + "One of the foremost objectives of Fitra is supporting the poor. It creates an opportunity for the impoverished to fulfill their food and basic needs on the day of Eid. The Messenger of Allah ﷺ said:\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ\n\n"
                        + "Meaning:\n\n"
                        + "Enrich them (the poor) on this day so they do not have to beg.\n\n"
                        + "[Sahih Ibn Khuzaymah: 2417]",
                "সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭; সহিহ ইবনে খুজাইমা: ২৪১৭",
                "Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827; Sahih Ibn Khuzaymah: 2417"
        ));

        // 8. ফিতরা প্রদান ও ঈদ উদযাপন
        list.add(new RozaFitraTopicItem(
                8,
                "fitra_eid_celebration",
                "ফিতরা প্রদান ও ঈদ উদযাপন",
                "Paying Fitra and Celebrating Eid",
                "ফিতরা প্রদান ও ঈদ উদযাপন",
                "Paying Fitra and Celebrating Eid",
                "সাদাকাতুল ফিতর (ফিতরা) এবং ঈদুল ফিতর ইসলামের দুটি গুরুত্বপূর্ণ দিক, যা মুসলিম উম্মাহর মাঝে ভ্রাতৃত্ব, মানবতা, এবং সুখ-শান্তি প্রতিষ্ঠায় ভূমিকা রাখে। ফিতরা প্রদানের মাধ্যমে সমাজের দরিদ্র ও বঞ্চিত মানুষদের ঈদের আনন্দে সামিল করা হয়, যা ঈদ উদযাপনকে সর্বজনীন ও পূর্ণাঙ্গ করে তোলে। এখানে ফিতরা প্রদান এবং ঈদ উদযাপনের মধ্যে সংযোগ এবং এর তাৎপর্য বিস্তারিতভাবে আলোচনা করা হলো।",
                "Sadaqatul Fitr (Fitra) and Eid al-Fitr are two vital aspects of Islam that contribute to establishing brotherhood, humanity, peace, and happiness within the Muslim Ummah. Through Fitra, impoverished and marginalized members of society are included in the joy of Eid, making the celebration universal and complete. Here the connection between paying Fitra and celebrating Eid and its significance is discussed in detail.",
                "সাদাকাতুল ফিতর (ফিতরা) এবং ঈদুল ফিতর ইসলামের দুটি গুরুত্বপূর্ণ দিক, যা মুসলিম উম্মাহর মাঝে ভ্রাতৃত্ব, মানবতা, এবং সুখ-শান্তি প্রতিষ্ঠায় ভূমিকা রাখে। ফিতরা প্রদানের মাধ্যমে সমাজের দরিদ্র ও বঞ্চিত মানুষদের ঈদের আনন্দে সামিল করা হয়, যা ঈদ উদযাপনকে সর্বজনীন ও পূর্ণাঙ্গ করে তোলে। এখানে ফিতরা প্রদান এবং ঈদ উদযাপনের মধ্যে সংযোগ এবং এর তাৎপর্য বিস্তারিতভাবে আলোচনা করা হলো।\n\n"
                        + "ফিতরা প্রদানের মূল উদ্দেশ্য:\n\n"
                        + "রোজার পবিত্রতা অর্জন: রমজানের রোজার মধ্যে ভুলত্রুটি মাফ এবং আত্মশুদ্ধি অর্জন করা। রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ\n\n"
                        + "অর্থ:\n\n"
                        + "রাসুলুল্লাহ ﷺ ফিতরা প্রদানকে ফরজ করেছেন, যা রোজাদারের জন্য অপ্রয়োজনীয় কথা ও কাজ থেকে পবিত্রতা এবং দরিদ্রদের খাদ্যের সংস্থান হিসেবে কাজ করে।\n\n"
                        + "[সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭]\n\n"
                        + "দরিদ্রদের ঈদে আনন্দ উপভোগের সুযোগ দেওয়া:\n\n"
                        + "ফিতরার মাধ্যমে দরিদ্র ও বঞ্চিত মানুষরা ঈদের দিনে খাবার এবং মৌলিক প্রয়োজন পূরণ করতে পারে। রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ.\n\n"
                        + "অর্থ:\n\n"
                        + "ঈদের দিনে তাদের (দরিদ্রদের) ভিক্ষার প্রয়োজন থেকে মুক্ত রাখো।\n\n"
                        + "[সহিহ ইবনে খুজাইমা: ২৪১৭]\n\n"
                        + "ঈদের আনন্দ সর্বজনীন করা:\n\n"
                        + "ফিতরা প্রদানের মাধ্যমে ধনী-গরিব সবাই একসঙ্গে ঈদ উদযাপনের সুযোগ পায়। এটি মুসলিম উম্মাহর ঐক্যের প্রতীক।\n\n"
                        + "ফিতরা প্রদানের নিয়ম ও পদ্ধতি:\n\n"
                        + "ফিতরা ঈদের নামাজে যাওয়ার আগে প্রদান করতে হবে। রমজানের শেষ দিনগুলোতে আগেও প্রদান করা যায়, যাতে ফিতরার হকদাররা ঈদের জন্য প্রস্তুতি নিতে পারে।\n\n"
                        + "ঈদের শুরু ফিতরা দিয়ে:\n\n"
                        + "ঈদুল ফিতর মুসলিমদের জন্য আনন্দ ও ইবাদতের দিন। এর শুরু হয় ফিতরা প্রদান এবং তাকবির দিয়ে। ফিতরা প্রদান আমাদেরকে শেখায় যে ঈদের আনন্দ শুধু ধনীদের জন্য নয়, বরং দরিদ্রদের সঙ্গেও ভাগাভাগি করা।\n\n"
                        + "দরিদ্রদের মুখে হাসি ফোটানো:\n\n"
                        + "ফিতরা প্রদানের মাধ্যমে দরিদ্রদের প্রয়োজনীয় চাহিদা মেটানো হয়, যেন তারা ঈদের দিন ভিক্ষা না করে, বরং আনন্দ উপভোগ করতে পারে।\n\n"
                        + "ফিতরা ও ঈদের মধ্যে সম্পর্ক: ফিতরা ও ঈদ একে অপরের সঙ্গে গভীরভাবে সংযুক্ত।\n\n"
                        + "ফিতরা ঈদের পূর্বশর্ত: ফিতরা প্রদান ঈদের নামাজের আগেই সম্পন্ন করতে বলা হয়েছে, যা ঈদের দিনের শুভ সূচনা।\n\n"
                        + "ঈদ উদযাপনের সর্বজনীনতা: ফিতরা প্রদান নিশ্চিত করে যে দরিদ্ররাও ঈদের আনন্দে অংশগ্রহণ করতে পারে।\n\n"
                        + "ঈদের শিক্ষা: ঈদ আমাদের শেখায় আত্মশুদ্ধি, ভ্রাতৃত্ববোধ এবং মানবতার চর্চা। ফিতরা এ শিক্ষার একটি বাস্তব রূপ।",
                "Sadaqatul Fitr (Fitra) and Eid al-Fitr are two vital aspects of Islam that contribute to establishing brotherhood, humanity, peace, and happiness within the Muslim Ummah. Through Fitra, impoverished and marginalized members of society are included in the joy of Eid, making the celebration universal and complete. Here the connection between paying Fitra and celebrating Eid and its significance is discussed in detail.\n\n"
                        + "Primary Objectives of Paying Fitra:\n\n"
                        + "Attaining Purity of Fasting: Forgiveness of unintentional shortcomings during Ramadan fasting and achieving self-purification. The Messenger of Allah ﷺ said:\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ\n\n"
                        + "Meaning:\n\n"
                        + "The Messenger of Allah ﷺ ordained Zakat al-Fitr as a purification for the fasting person from idle talk and obscenity, and as food for the needy.\n\n"
                        + "[Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827]\n\n"
                        + "Affording the Poor the Opportunity to Enjoy Eid Joy:\n\n"
                        + "Through Fitra, underprivileged and marginalized individuals can obtain food and meet fundamental necessities on Eid day. The Messenger of Allah ﷺ said:\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ.\n\n"
                        + "Meaning:\n\n"
                        + "Enrich them (the poor) on this day so they do not have to beg.\n\n"
                        + "[Sahih Ibn Khuzaymah: 2417]\n\n"
                        + "Universalizing the Joy of Eid:\n\n"
                        + "Through the disbursement of Fitra, the rich and the poor alike receive the opportunity to celebrate Eid together. It serves as an emblem of unity for the Muslim Ummah.\n\n"
                        + "Rules and Timing of Disbursing Fitra:\n\n"
                        + "Fitra must be paid prior to proceeding to the Eid prayer. It is also permissible to disburse it during the concluding days of Ramadan, enabling rightful recipients to prepare adequately for Eid.\n\n"
                        + "Commencement of Eid with Fitra:\n\n"
                        + "Eid al-Fitr is a day of spiritual joy and worship for Muslims. It commences with the disbursement of Fitra and the chanting of Takbeer. Paying Fitra teaches us that the joy of Eid is not solely reserved for the affluent, but is meant to be shared wholeheartedly with the destitute.\n\n"
                        + "Bringing Smiles to the Faces of the Poor:\n\n"
                        + "Through Fitra, the essential needs of the impoverished are fulfilled so that they do not have to beg on Eid day, but rather rejoice in festivities with dignity.\n\n"
                        + "Relationship Between Fitra and Eid: Fitra and Eid are profoundly interconnected.\n\n"
                        + "Fitra as a Prerequisite of Eid: Paying Fitra before the Eid prayer is instructed as an auspicious inception of Eid day.\n\n"
                        + "Universality of Eid Celebration: Discharging Fitra guarantees that the impoverished participate fully in festive joy.\n\n"
                        + "Teachings of Eid: Eid teaches us self-purification, fraternal solidarity, and active humanity. Fitra is the tangible manifestation of these profound lessons.",
                "সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭; সহিহ ইবনে খুজাইমা: ২৪১৭",
                "Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827; Sahih Ibn Khuzaymah: 2417"
        ));

        // 9. ফিতরা নিয়ে ইসলামিক দৃষ্টিকোণ
        list.add(new RozaFitraTopicItem(
                9,
                "fitra_islamic_perspective",
                "ফিতরা নিয়ে ইসলামিক দৃষ্টিকোণ",
                "Islamic Perspective on Fitra",
                "ফিতরা নিয়ে ইসলামিক দৃষ্টিকোণ",
                "Islamic Perspective on Fitra",
                "সাদাকাতুল ফিতর (ফিতরা) ইসলামের একটি গুরুত্বপূর্ণ আর্থিক ইবাদত। এটি ঈদুল ফিতরের আগে আদায় করা একটি দান, যা প্রত্যেক সামর্থ্যবান মুসলিমের জন্য ওয়াজিব। ফিতরা ইসলামের সামাজিক ও অর্থনৈতিক দিকগুলোর একটি বাস্তব উদাহরণ, যা দরিদ্রদের সহায়তা, রমজানের ইবাদতের পূর্ণতা, এবং ঈদের আনন্দ সর্বজনীন করার একটি ব্যবস্থা। ইসলামিক দৃষ্টিকোণ থেকে ফিতরার উদ্দেশ্য, গুরুত্ব এবং বিধান অত্যন্ত স্পষ্টভাবে বর্ণিত হয়েছে।",
                "Sadaqatul Fitr (Fitra) is an essential financial act of worship in Islam. It is a charitable offering discharged before Eid al-Fitr, which is obligatory (Wajib) upon every capable Muslim. Fitra represents a tangible embodiment of Islamic social and economic tenets, serving as a mechanism to support the poor, complete Ramadan worship, and make festive joy universal. From an Islamic perspective, the objective, significance, and rulings of Fitra are articulated with utmost clarity.",
                "সাদাকাতুল ফিতর (ফিতরা) ইসলামের একটি গুরুত্বপূর্ণ আর্থিক ইবাদত। এটি ঈদুল ফিতরের আগে আদায় করা একটি দান, যা প্রত্যেক সামর্থ্যবান মুসলিমের জন্য ওয়াজিব। ফিতরা ইসলামের সামাজিক ও অর্থনৈতিক দিকগুলোর একটি বাস্তব উদাহরণ, যা দরিদ্রদের সহায়তা, রমজানের ইবাদতের পূর্ণতা, এবং ঈদের আনন্দ সর্বজনীন করার একটি ব্যবস্থা। ইসলামিক দৃষ্টিকোণ থেকে ফিতরার উদ্দেশ্য, গুরুত্ব এবং বিধান অত্যন্ত স্পষ্টভাবে বর্ণিত হয়েছে।\n\n"
                        + "ফিতরার মূল অর্থ ও লক্ষ্য:\n\n"
                        + "ফিতরা শব্দটি এসেছে আরবি \"فطر\" (ফিতর) শব্দ থেকে, যার অর্থ \"ভাঙা\" বা \"রোজা ভাঙা।\" এটি মূলত রমজানের রোজার পর ঈদুল ফিতরের মাধ্যমে আনন্দ উদযাপন এবং সমাজের দরিদ্রদের পাশে দাঁড়ানোর একটি মাধ্যম।\n\n"
                        + "লক্ষ্য:\n\n"
                        + "আত্মশুদ্ধি: রোজার মধ্যকার ছোটখাটো ভুল-ত্রুটির পবিত্রতা অর্জন।\n\n"
                        + "সামাজিক ন্যায়বিচার: দরিদ্রদের মৌলিক প্রয়োজন মেটানো এবং তাদের ঈদের আনন্দে শরিক করা।\n\n"
                        + "ধনী-গরিবের সংযোগ: সমাজের ধনী ও গরিবদের মাঝে ভ্রাতৃত্ব ও ঐক্য প্রতিষ্ঠা।\n\n"
                        + "ফিতরা নিয়ে কুরআনের নির্দেশনা:\n\n"
                        + "ফিতরার বিষয়ে কুরআনে সরাসরি কোনো আয়াত নেই, তবে দান-সদকা এবং সমাজের দরিদ্রদের প্রতি দায়িত্ব সম্পর্কে আল্লাহ তাআলা বলেছেন,\n\n"
                        + "وَفِي أَمْوَالِهِمْ حَقٌّ لِلسَّائِلِ وَالْمَحْرُومِ\n\n"
                        + "অর্থ:\n\n"
                        + "তাদের সম্পদে রয়েছে প্রার্থনাকারী এবং বঞ্চিতদের অধিকার।\n\n"
                        + "[সূরা আদ-ধারিয়াত: ১৯]\n\n"
                        + "এই আয়াত থেকে বোঝা যায়, ধনীদের সম্পদে দরিদ্রদের অধিকার রয়েছে। ফিতরা সেই অধিকার পূরণের একটি অংশ।\n\n"
                        + "আত্মশুদ্ধি:\n\n"
                        + "قَدْ أَفْلَحَ مَن تَزَكَّىٰ وَذَكَرَ ٱسْمَ رَبِّهِۦ فَصَلَّىٰ\n\n"
                        + "সফল হয়েছে সেই ব্যক্তি, যে পবিত্রতা অর্জন করেছে এবং তার রবের নাম স্মরণ করে সালাত আদায় করেছে।\n\n"
                        + "[সূরা আল-আ'লা: ১৪-১৫]\n\n"
                        + "অনেক তাফসিরে উল্লেখ করা হয়েছে যে, এখানে \"تَزَكَّىٰ\" শব্দটি ফিতরা প্রদানকেও বোঝায়, কারণ এটি আত্মার পবিত্রতার একটি মাধ্যম।\n\n"
                        + "ফিতরা নিয়ে রাসুলুল্লাহ ﷺ এর হাদিস:\n\n"
                        + "রাসুলুল্লাহ ﷺ ফিতরাকে রোজার পবিত্রতা রক্ষা এবং দরিদ্রদের সহায়তার জন্য ফরজ করেছেন। রাসুলুল্লাহ ﷺ বলেন,\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ.\n\n"
                        + "অর্থ:\n\n"
                        + "রাসুলুল্লাহ ﷺ ফিতরা প্রদানকে ফরজ করেছেন, যা রোজাদারের জন্য অপ্রয়োজনীয় কথা ও কাজ থেকে পবিত্রতা এবং দরিদ্রদের খাদ্যের সংস্থান হিসেবে কাজ করে।\n\n"
                        + "[সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭]\n\n"
                        + "এ হাদিসে রোজার পবিত্রতা রক্ষা এবং দরিদ্রদের খাদ্য সহায়তার জন্য ফিতরার গুরুত্ব স্পষ্ট করা হয়েছে।",
                "Sadaqatul Fitr (Fitra) is an essential financial act of worship in Islam. It is a charitable offering discharged before Eid al-Fitr, which is obligatory (Wajib) upon every capable Muslim. Fitra represents a tangible embodiment of Islamic social and economic tenets, serving as a mechanism to support the poor, complete Ramadan worship, and make festive joy universal. From an Islamic perspective, the objective, significance, and rulings of Fitra are articulated with utmost clarity.\n\n"
                        + "Core Meaning and Objectives of Fitra:\n\n"
                        + "The word Fitra derives from the Arabic term \"فطر\" (Fitr), signifying \"breaking\" or \"breaking the fast.\" It fundamentally denotes celebrating joy through Eid al-Fitr following the fasting of Ramadan and standing beside the impoverished of society.\n\n"
                        + "Objectives:\n\n"
                        + "Self-Purification: Attaining purification from inadvertent minor shortcomings during fasting.\n\n"
                        + "Social Justice: Meeting fundamental needs of the underprivileged and including them in the joy of Eid.\n\n"
                        + "Bridging the Rich and the Poor: Establishing fraternity and unity between the affluent and marginalized sections of society.\n\n"
                        + "Quranic Guidance Concerning Fitra:\n\n"
                        + "While there is no verse in the Quran explicitly naming Fitra, regarding charity and responsibility towards the poor of society, Allah the Almighty states:\n\n"
                        + "وَفِي أَمْوَالِهِمْ حَقٌّ لِلسَّائِلِ وَالْمَحْرُومِ\n\n"
                        + "Meaning:\n\n"
                        + "And in their wealth there was a rightful share for the beggar and the deprived.\n\n"
                        + "[Surah Adh-Dhariyat: 19]\n\n"
                        + "From this verse, it is understood that the poor hold a rightful claim upon the wealth of the rich. Fitra is a part of fulfilling that divine entitlement.\n\n"
                        + "Self-Purification:\n\n"
                        + "قَدْ أَفْلَحَ مَن تَزَكَّىٰ وَذَكَرَ ٱسْمَ رَبِّهِۦ فَصَلَّىٰ\n\n"
                        + "He has certainly succeeded who purifies himself, and mentions the name of his Lord and prays.\n\n"
                        + "[Surah Al-A'la: 14-15]\n\n"
                        + "Many classical exegeses explain that the word \"تَزَكَّىٰ\" here also refers to disbursing Fitra, as it serves as a powerful medium for the purification of the soul.\n\n"
                        + "Hadith of the Messenger of Allah ﷺ Concerning Fitra:\n\n"
                        + "The Messenger of Allah ﷺ ordained Fitra to safeguard the sanctity of fasting and support the destitute. The Messenger of Allah ﷺ said:\n\n"
                        + "فَرَضَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ زَكَاةَ الْفِطْرِ طُهْرَةً لِلصَّائِمِ مِنَ اللَّغْوِ وَالرَّفَثِ، وَطُعْمَةً لِلْمَسَاكِينِ.\n\n"
                        + "Meaning:\n\n"
                        + "The Messenger of Allah ﷺ ordained Zakat al-Fitr as a purification for the fasting person from idle talk and obscenity, and as food for the needy.\n\n"
                        + "[Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827]\n\n"
                        + "In this Hadith, the vital importance of Fitra for safeguarding the sanctity of fasting and providing food sustenance for the impoverished is made abundantly clear.",
                "সূরা আদ-ধারিয়াত: ১৯; সূরা আল-আ'লা: ১৪-১৫; সহিহ আবু দাউদ: ১৬০৯, সহিহ ইবনে মাজাহ: ১৮২৭",
                "Surah Adh-Dhariyat: 19; Surah Al-A'la: 14-15; Sahih Abi Dawud: 1609, Sunan Ibn Majah: 1827"
        ));
        // 10. ফিতরা বিতরণে সতর্কতা
        list.add(new RozaFitraTopicItem(
                10,
                "fitra_cautions",
                "ফিতরা বিতরণে সতর্কতা",
                "Precautions in Distributing Fitra",
                "ফিতরা বিতরণে সতর্কতা",
                "Precautions in Distributing Fitra",
                "ফিতরা বিতরণ একটি গুরুত্বপূর্ণ আর্থিক ইবাদত। এটি সঠিকভাবে বিতরণ করা যেমন আল্লাহর সন্তুষ্টি অর্জনের মাধ্যম, তেমনি যদি যথাযথ নিয়ম বা সতর্কতা না নেওয়া হয়, তাহলে এর উদ্দেশ্য অপূর্ণ থেকে যেতে পারে। ইসলামে ফিতরা বিতরণের ক্ষেত্রে বিশেষ সতর্কতা অবলম্বনের নির্দেশ দেওয়া হয়েছে, যাতে এটি যথাযথভাবে প্রাপ্য ব্যক্তিদের কাছে পৌঁছায় এবং সমাজে এর ইতিবাচক প্রভাব নিশ্চিত হয়। নিচে ফিতরা বিতরণের সতর্কতা এবং সংশ্লিষ্ট করণীয় বিষয়গুলো আলোচনা করা হলো।",
                "Distributing Fitra is a vital financial act of worship. Just as its correct disbursement serves as a means of attaining Allah's pleasure, neglecting prescribed rules or precautions may leave its purpose unfulfilled. Islam ordains special care in distributing Fitra so that it reaches entitled recipients and ensures a positive societal impact. Below, precautions and essential guidelines for distributing Fitra are discussed.",
                "ফিতরা বিতরণ একটি গুরুত্বপূর্ণ আর্থিক ইবাদত। এটি সঠিকভাবে বিতরণ করা যেমন আল্লাহর সন্তুষ্টি অর্জনের মাধ্যম, তেমনি যদি যথাযথ নিয়ম বা সতর্কতা না নেওয়া হয়, তাহলে এর উদ্দেশ্য অপূর্ণ থেকে যেতে পারে। ইসলামে ফিতরা বিতরণের ক্ষেত্রে বিশেষ সতর্কতা অবলম্বনের নির্দেশ দেওয়া হয়েছে, যাতে এটি যথাযথভাবে প্রাপ্য ব্যক্তিদের কাছে পৌঁছায় এবং সমাজে এর ইতিবাচক প্রভাব নিশ্চিত হয়। নিচে ফিতরা বিতরণের সতর্কতা এবং সংশ্লিষ্ট করণীয় বিষয়গুলো আলোচনা করা হলো।\n\n"
                        + "সময়মতো প্রদান করা:\n\n"
                        + "ফিতরা ঈদের নামাজের আগে প্রদান করা আবশ্যক। রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "من أداها قبل الصلاة فهي زكاة مقبولة، ومن أداها بعد الصلاة فهي صدقة من الصدقات.\n\n"
                        + "অর্থ:\n\n"
                        + "যে ব্যক্তি ঈদের নামাজের আগে ফিতরা প্রদান করে, তা গ্রহণযোগ্য জাকাত হিসেবে গণ্য হবে। আর যদি নামাজের পরে প্রদান করে, তবে তা সাধারণ সদকা হিসেবে গণ্য হবে।\n\n"
                        + "[সহিহ আবু দাউদ: ১৬০৯]\n\n"
                        + "সতর্কতা:\n\n"
                        + "ফিতরা ঈদের আগেই দরিদ্রদের হাতে পৌঁছে দিতে হবে। ঈদের নামাজের পরে ফিতরা দিলে এটি মূল ফিতরা আদায়ের উদ্দেশ্য পূরণ করবে না।\n\n"
                        + "প্রাপ্য ব্যক্তিদের সঠিকভাবে নির্ধারণ করা:\n\n"
                        + "ফিতরা শুধুমাত্র সেই ব্যক্তিদের দেওয়া যাবে, যারা জাকাত পাওয়ার যোগ্য।\n\n"
                        + "প্রাপ্যদের তালিকা:\n\nদরিদ্র (ফকির)।\nঅভাবগ্রস্ত (মিসকিন)।\nঋণগ্রস্ত ব্যক্তি।\nভ্রমণে সমস্যাগ্রস্ত মুসাফির।\nএতিম বা বিধবা।\n\n"
                        + "যাদের ফিতরা দেওয়া যাবে না:\n\nধনী ব্যক্তি (যিনি নিসাব পরিমাণ সম্পদ রাখেন)।\nনিজের পিতা-মাতা, সন্তান, বা যাদের ভরণ-পোষণের দায়িত্ব আপনার ওপর বর্তায়।\nমসজিদ, মাদ্রাসা বা ইসলামিক প্রতিষ্ঠানে সরাসরি ফিতরা দেওয়া।\n\n"
                        + "সতর্কতা:\n\n"
                        + "ফিতরা এমন ব্যক্তিকে দিতে হবে, যিনি প্রকৃতপক্ষে দরিদ্র এবং মৌলিক চাহিদা পূরণে অক্ষম। আত্মীয়স্বজনের মধ্যে যারা প্রাপ্য, তাদের অগ্রাধিকার দেওয়া উত্তম।\n\n"
                        + "ফিতরার প্রকৃত উদ্দেশ্য নিশ্চিত করা:\n\n"
                        + "ফিতরার মূল উদ্দেশ্য হলো দরিদ্রদের সহায়তা করা এবং তাদের ঈদের দিনে ভিক্ষাবৃত্তি থেকে মুক্ত রাখা। রাসুলুল্লাহ ﷺ বলেছেন,\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ\n\n"
                        + "অর্থ:\n\n"
                        + "ঈদের দিনে তাদের ভিক্ষার প্রয়োজন থেকে মুক্ত রাখো।\n\n"
                        + "[সহিহ ইবনে খুজাইমা: ২৪১৭]",
                "Distributing Fitra is a vital financial act of worship. Just as its correct disbursement serves as a means of attaining Allah's pleasure, neglecting prescribed rules or precautions may leave its purpose unfulfilled. Islam ordains special care in distributing Fitra so that it reaches entitled recipients and ensures a positive societal impact. Below, precautions and essential guidelines for distributing Fitra are discussed.\n\n"
                        + "Disbursing in a Timely Manner:\n\n"
                        + "Fitra must be paid prior to the Eid prayer. The Messenger of Allah ﷺ said:\n\n"
                        + "من أداها قبل الصلاة فهي زكاة مقبولة، ومن أداها بعد الصلاة فهي صدقة من الصدقات.\n\n"
                        + "Meaning:\n\n"
                        + "Whoever pays it before the prayer, it is an accepted Zakat, and whoever pays it after the prayer, it is merely an ordinary charity from charities.\n\n"
                        + "[Sahih Abi Dawud: 1609]\n\n"
                        + "Precaution:\n\n"
                        + "Fitra must physically reach the poor before Eid. Disbursing it after the Eid prayer will not fulfill the foundational purpose of discharging Fitra.\n\n"
                        + "Accurately Identifying Entitled Recipients:\n\n"
                        + "Fitra may only be given to those who are eligible to receive Zakat.\n\n"
                        + "List of Eligible Recipients:\n\nPoor (Faqir).\nDestitute / Needy (Miskeen).\nIndebted individuals.\nStranded travelers facing distress.\nOrphans or widows.\n\n"
                        + "Those to Whom Fitra Cannot Be Given:\n\nWealthy individuals (who possess the Nisab threshold of wealth).\nOne's own parents, children, or dependents whose maintenance is your responsibility.\nDirectly to mosques, madrasas, or Islamic institutions without individual entitlement.\n\n"
                        + "Precaution:\n\n"
                        + "Fitra must be handed to someone who is genuinely impoverished and unable to fulfill basic life necessities. Giving priority to eligible relatives who qualify is highly meritorious.\n\n"
                        + "Ensuring the True Purpose of Fitra:\n\n"
                        + "The primary goal of Fitra is to support the poor and keep them free from begging on Eid day. The Messenger of Allah ﷺ said:\n\n"
                        + "أَغْنُوهُمْ عَنِ الطَّوَافِ فِي هَذَا الْيَوْمِ\n\n"
                        + "Meaning:\n\n"
                        + "Enrich them so that they do not have to beg on this day.\n\n"
                        + "[Sahih Ibn Khuzaymah: 2417]",
                "সহিহ আবু দাউদ: ১৬০৯; সহিহ ইবনে খুজাইমা: ২৪১৭",
                "Sahih Abi Dawud: 1609; Sahih Ibn Khuzaymah: 2417"
        ));

        return list;
    }

    public static List<RozaFitraCommodity> getDefaultCommodities() {
        List<RozaFitraCommodity> list = new ArrayList<>();
        list.add(new RozaFitraCommodity("wheat", "গম / আটা", "Wheat / Flour", 1.65,
                "অর্ধ সা' = জনপ্রতি ১.৬৫ কেজি গম / আটা",
                "Half Sa' = 1.65 kg Wheat/Flour per person", 60.0));
        list.add(new RozaFitraCommodity("dates", "খেজুর", "Dates", 3.3,
                "১ সা' = জনপ্রতি ৩.৩ কেজি খেজুর",
                "1 Sa' = 3.3 kg Dates per person", 600.0));
        list.add(new RozaFitraCommodity("raisins", "কিশমিশ", "Raisins", 3.3,
                "১ সা' = জনপ্রতি ৩.৩ কেজি কিশমিশ",
                "1 Sa' = 3.3 kg Raisins per person", 500.0));
        list.add(new RozaFitraCommodity("barley", "যব", "Barley", 3.3,
                "১ সা' = জনপ্রতি ৩.৩ কেজি যব",
                "1 Sa' = 3.3 kg Barley per person", 150.0));
        list.add(new RozaFitraCommodity("cheese", "পনির", "Cheese", 3.3,
                "১ সা' = জনপ্রতি ৩.৩ কেজি পনির",
                "1 Sa' = 3.3 kg Cheese per person", 800.0));
        return list;
    }

    private static void fetchRemoteData(Context context, TopicDataCallback callback) {
        HttpURLConnection conn = null;
        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_fitra.php");
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
                    if (root.has("success") && root.get("success").getAsBoolean()) {
                        // Remote commodities sync
                        if (root.has("commodities")) {
                            JsonArray commArr = root.getAsJsonArray("commodities");
                            for (JsonElement el : commArr) {
                                JsonObject obj = el.getAsJsonObject();
                                String cid = obj.get("id").getAsString();
                                double price = obj.get("price_per_kg").getAsDouble();
                                RozaFitraCommodity item = getCommodityById(cid);
                                if (item != null) {
                                    item.setPricePerKg(price);
                                }
                            }
                        }

                        // Remote topics sync
                        if (root.has("topics")) {
                            JsonArray topArr = root.getAsJsonArray("topics");
                            List<RozaFitraTopicItem> remoteTopics = new ArrayList<>();
                            for (JsonElement el : topArr) {
                                JsonObject obj = el.getAsJsonObject();
                                int id = obj.get("id").getAsInt();
                                String slug = obj.has("slug") ? obj.get("slug").getAsString() : "topic_" + id;
                                String titleBn = obj.get("title_bn").getAsString();
                                String titleEn = obj.get("title_en").getAsString();
                                String cardTitleBn = obj.has("card_title_bn") && !obj.get("card_title_bn").isJsonNull() ? obj.get("card_title_bn").getAsString() : titleBn;
                                String cardTitleEn = obj.has("card_title_en") && !obj.get("card_title_en").isJsonNull() ? obj.get("card_title_en").getAsString() : titleEn;
                                String previewBn = obj.has("preview_bn") && !obj.get("preview_bn").isJsonNull() ? obj.get("preview_bn").getAsString() : "";
                                String previewEn = obj.has("preview_en") && !obj.get("preview_en").isJsonNull() ? obj.get("preview_en").getAsString() : "";
                                String detailsBn = obj.get("details_bn").getAsString();
                                String detailsEn = obj.get("details_en").getAsString();
                                String refBn = obj.has("reference_bn") && !obj.get("reference_bn").isJsonNull() ? obj.get("reference_bn").getAsString() : "";
                                String refEn = obj.has("reference_en") && !obj.get("reference_en").isJsonNull() ? obj.get("reference_en").getAsString() : "";

                                remoteTopics.add(new RozaFitraTopicItem(id, slug, titleBn, titleEn,
                                        cardTitleBn, cardTitleEn,
                                        previewBn, previewEn, detailsBn, detailsEn, refBn, refEn));
                            }
                            if (!remoteTopics.isEmpty()) {
                                cachedTopics.clear();
                                cachedTopics.addAll(remoteTopics);
                                if (callback != null) {
                                    mainHandler.post(() -> callback.onDataLoaded(new ArrayList<>(cachedTopics)));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Keep resilient 0ms offline cache
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // Keep backwards compatibility for any legacy callers of getAllCards
    public static List<HajjHistoryCardItem> getAllCards(Context context, Object ignored) {
        List<HajjHistoryCardItem> list = new ArrayList<>();
        for (RozaFitraTopicItem it : cachedTopics) {
            list.add(new HajjHistoryCardItem(
                    it.getId(),
                    it.getTitleBn(),
                    it.getTitleEn(),
                    it.getPreviewBn(),
                    it.getPreviewEn(),
                    it.getDetailsBn(),
                    it.getDetailsEn()
            ));
        }
        return list;
    }
}
