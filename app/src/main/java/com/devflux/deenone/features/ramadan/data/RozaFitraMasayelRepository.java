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
 * Repository for: ফিতরা সম্পর্কিত মাসআলা-মাসায়েল (Fiqh Rulings & Masayel of Sadaqatul Fitr).
 * Contains the 7 verbatim cards shown in the user screenshot:
 * 1. টাকা দ্বারা ফিতরা আদায় করা যাবে কি?
 * 2. মুসাফির ব্যক্তি কোন জায়গার হিসাবে ফিতরা দিবে?
 * 3. চাউল দ্বারা সাদকায়ে ফিতর
 * 4. সদকায়ে ফিতর কখন আদায় করবেন?
 * 5. সাদকায়ে ফিতর কার পক্ষ থেকে দেওয়া ওয়াজিব?
 * 6. প্রবাসী মুকীম ব্যক্তি কোন হিসাবে ফিতরা দিবে?
 * 7. সম্পূর্ণ ফিতরা একজনকে কী দেওয়া যাবে?
 */
public final class RozaFitraMasayelRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaFitraMasayelRepository() {}

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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_fitra_masayel.php");
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
                            String detailsBn = obj.has("details_bn") ? obj.get("details_bn").getAsString() : (obj.has("full_content_bn") ? obj.get("full_content_bn").getAsString() : "");
                            String detailsEn = obj.has("details_en") ? obj.get("details_en").getAsString() : (obj.has("full_content_en") ? obj.get("full_content_en").getAsString() : "");

                            HajjHistoryCardItem item = new HajjHistoryCardItem(
                                    id, titleBn, titleEn, previewBn, previewEn, detailsBn, detailsEn
                            );
                            item.setExpanded(false);
                            remoteItems.add(item);
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
            // Keep instant cached cards on offline/network failure
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // 1. টাকা দ্বারা ফিতরা আদায় করা যাবে কি?
        list.add(new HajjHistoryCardItem(
                1,
                "টাকা দ্বারা ফিতরা আদায় করা যাবে কি?",
                "Can Fitra be Paid in Cash?",
                "টাকা দ্বারা ফিৎরা আদায়ের রীতি ইসলামের সোনালী যুগে ছিল না। রাসূলুল্লাহ (ছাঃ) ও ছাহাবায়ে কেরাম টাকা দ্বারা ফিৎরা আদায় করেছেন মর্মে কোন প্রমাণ পাওয়া যায় না। রাসূলুল্লাহ (ছাঃ)-এর যুগে স্বর্ণ ও রৌপ্য মুদ্রা বাজারে চালু থাকা সত্ত্বেও তিনি খাদ্য বস্ত্ত ...",
                "The practice of paying Fitra in cash did not exist in the golden era of Islam. There is no evidence that the Messenger of Allah ﷺ and his companions paid Fitra in cash...",
                "টাকা দ্বারা ফিৎরা আদায়ের রীতি ইসলামের সোনালী যুগে ছিল না। রাসূলুল্লাহ (ছাঃ) ও ছাহাবায়ে কেরাম টাকা দ্বারা ফিৎরা আদায় করেছেন মর্মে কোন প্রমাণ পাওয়া যায় না। রাসূলুল্লাহ (ছাঃ)-এর যুগে স্বর্ণ ও রৌপ্য মুদ্রা বাজারে চালু থাকা সত্ত্বেও তিনি খাদ্য বস্ত্ত দ্বারা ফিৎরা আদায় করেছেন, আদায় করতে বলেছেন এবং বিভিন্ন শস্যের কথা হাদীছে উল্লেখ রয়েছে। আবু সাঈদ খুদরী (রাঃ) বলেন, ‘আমরা এক ছা‘ ত্বা‘আম বা খাদ্য, অথবা এক ছা যব, অথবা এক ছা খেজুর, অথবা এক ছা পনির, অথবা এক ছা কিশমিশ থেকে যাকাতুল ফিৎর বের করতাম।\n\n"
                        + "[বুখারী হা/১৫০৬; মুসলিম হা/৯৮৫; মিশকাত হা/১৮১৬]\n\n"
                        + "ইবনু ওমর (রাঃ) হতে বর্ণিত, তিনি বলেন, রাসূলুল্লাহ (ছাঃ) যাকাতুল ফিৎর হিসাবে মুসলমানদের ছোট-বড়, পুরুষ-নারী এবং স্বাধীন-দাস প্রত্যেকের উপর এক ছা‘ খেজুর অথবা এক ছা‘ যব ফরয করেছেন এবং তিনি ছালাতের উদ্দেশ্যে বের হওয়ার পূর্বেই তা আদায়ের নির্দেশ দিয়েছেন।\n\n"
                        + "[বুখারী হা/১৫০৩, ‘যাকাত’ অধ্যায়, ‘ছাদাকাতুল ফিৎর’ অনুচ্ছেদ; মুসলিম হা/৩৮৪; মিশকাত হা/১৮১৫]\n\n"
                        + "অতএব খাদ্যশস্য দ্বারা ‘যাকাতুল ফিৎর’ আদায় করাই ইসলামী শরী‘আতের বিধান। টাকা-পয়সা দ্বারা ফিৎরা প্রদান করা তার পরিপন্থী। ছায়েম নিজে যা খান, তা থেকেই ফিৎরা দানের মধ্যে অধিক মহববত নিহিত থাকে। যে ব্যক্তি ২০ টাকা কেজি দরের চাউল খান সে উক্ত মানের চাউল এক ছা‘ ফিৎরা দিবেন। আর যে ব্যক্তি ৫০ টাকা কেজি দরের চাউল খান সে উক্ত মানের চাউল এক ছা‘ ফিৎরা দিবেন। উল্লেখ্য যে, বর্তমানে টাকা-পয়সার দ্বারা ফিৎরা আদায়ের ফলে একজন রিক্সা চালক যে ২০ টাকা কেজি দরের চাউল খায়, আর একজন দেশের মন্ত্রী যে ৭০-১০০ টাকা কেজি দরের চাউল খান, উভয়ের যাকাতুল ফিৎরের মান সমান হয়ে যায়। অর্থাৎ সরকার কর্তৃক নির্ধারিত টাকা দ্বারা রাজা প্রজা সকলেই ফিৎরা আদায় করে থাকে। যা ইসলাম ও মানুষের বিবেক বিরোধী।",
                "The practice of paying Fitra in cash did not exist in the golden era of Islam. There is no evidence that the Messenger of Allah ﷺ and his companions paid Fitra in cash money. Despite gold and silver currency being in circulation in the markets during the era of the Prophet ﷺ, he paid Fitra in food items, instructed others to do so, and diverse grains are specified in the Hadiths. Abu Sa'id al-Khudri (RA) narrated: 'We used to give as Zakat al-Fitr one Sa' of food, or one Sa' of barley, or one Sa' of dates, or one Sa' of cheese, or one Sa' of raisins.'\n\n"
                        + "[Bukhari: 1506; Muslim: 985; Mishkat: 1816]\n\n"
                        + "Ibn Umar (RA) narrated: The Messenger of Allah ﷺ prescribed Zakat al-Fitr upon Muslims—whether young or old, male or female, free or slave—as one Sa' of dates or one Sa' of barley, and commanded that it be discharged before people set out for the prayer.\n\n"
                        + "[Bukhari: 1503; Muslim: 384; Mishkat: 1815]\n\n"
                        + "Therefore, paying 'Zakat al-Fitr' in staple food commodities is the established ordinance of Islamic Shariah. Paying Fitra in cash is contrary to this practice. Sincere devotion lies in giving Fitra from the quality of grain one personally consumes. If a person consumes rice worth 20 BDT per kg, they should pay one Sa' of that grade, while one consuming rice worth 50 BDT should pay one Sa' of that grade. By fixing a uniform flat cash rate, both an ordinary laborer and a wealthy minister end up discharging the exact same monetary sum, despite vastly disparate consumption levels."
        ));

        // 2. মুসাফির ব্যক্তি কোন জায়গার হিসাবে ফিতরা দিবে?
        list.add(new HajjHistoryCardItem(
                2,
                "মুসাফির ব্যক্তি কোন জায়গার হিসাবে ফিতরা দিবে?",
                "Which Location's Rate Should a Traveler Use for Fitra?",
                "মুসাফির ব্যক্তি যদি মালিকে নেসাব হয় তাহলে ঈদের দিন সে যেখানে থাকবে সেখানের মূল্য হিসাবে সাদাকাতুল ফিতর আদায় করবে । [আদ্দুরুল মুনতাকা ১/২২৬]",
                "If a traveler is the owner of Nisab, on Eid day he shall pay Sadaqatul Fitr according to the rate of where he is located... [Ad-Durrul Muntaqa 1/226]",
                "মুসাফির ব্যক্তি যদি মালিকে নেসাব হয় তাহলে ঈদের দিন সে যেখানে থাকবে সেখানের মূল্য হিসাবে সাদাকাতুল ফিতর আদায় করবে ।\n\n"
                        + "[আদ্দুরুল মুনতাকা ১/২২৬]",
                "If a traveler (musafir) is the owner of Nisab, then on Eid day, he will discharge Sadaqatul Fitr according to the market value of the place where he is physically residing on that day.\n\n"
                        + "[Ad-Durrul Muntaqa 1/226]"
        ));

        // 3. চাউল দ্বারা সাদকায়ে ফিতর
        list.add(new HajjHistoryCardItem(
                3,
                "চাউল দ্বারা সাদকায়ে ফিতর",
                "Sadaqatul Fitr Through Rice",
                "যদি কেউ কৃষিজাত ফসল যেমন ধান-চাউল ইত্যাদি দ্বারা সাদাকাতুল ফিতর আদায় করতে চায় তাহলে এতে ওজন গ্রহনযোগ্য নয়; বরং এক্ষেত্রে মূল্য বিবেচ্য হবে। অর্থাৎ এক ছা’ খেজুর বা আধা ছা’ গমের বাজার দর যা আসে সে মূল্যের ধান-চাউল আদায় করতে হ...",
                "If anyone desires to pay Sadaqatul Fitr through agricultural produce such as paddy or rice, the mere direct weight is not considered; rather, the market value is taken into consideration...",
                "যদি কেউ কৃষিজাত ফসল যেমন ধান-চাউল ইত্যাদি দ্বারা সাদাকাতুল ফিতর আদায় করতে চায় তাহলে এতে ওজন গ্রহনযোগ্য নয়; বরং এক্ষেত্রে মূল্য বিবেচ্য হবে। অর্থাৎ এক ছা’ খেজুর বা আধা ছা’ গমের বাজার দর যা আসে সে মূল্যের ধান-চাউল আদায় করতে হবে।\n\n"
                        + "[দুররে মুখতার ৩/৩১৯,ইমদাদুল ফাতাওয়া ২/১০৮]",
                "If someone desires to discharge Sadaqatul Fitr through agricultural produce such as paddy or rice, the mere direct weight is not considered in classical Hanafi fiqh; rather, the monetary value is what is taken into consideration. That is, one must pay paddy or rice equivalent to the market value of one Sa' of dates or half a Sa' of wheat.\n\n"
                        + "[Durrul Mukhtar 3/319, Imdadul Fatawa 2/108]"
        ));

        // 4. সদকায়ে ফিতর কখন আদায় করবেন?
        list.add(new HajjHistoryCardItem(
                4,
                "সদকায়ে ফিতর কখন আদায় করবেন?",
                "When Should Sadaqatul Fitr be Paid?",
                "উত্তম হল ঈদের নামাযের আগে সাদাকাতুল ফিতর আদায় করা । এসময় যদি আদায় না করা হয়,তবে পরে যখন ইচ্ছা আদায় করতে পারবে। পরে যখনই তা আদায় করা হবে আদায় বলে গণ্য হবে,কাযা বলা যাবেনা । [বাদায়ে উস সানায়ে ২/৫৪৬]",
                "It is optimal to pay Sadaqatul Fitr before the Eid prayer. If not discharged by that time, it can be paid later whenever desired... [Bada'i al-Sana'i 2/546]",
                "উত্তম হল ঈদের নামাযের আগে সাদাকাতুল ফিতর আদায় করা । এসময় যদি আদায় না করা হয়,তবে পরে যখন ইচ্ছা আদায় করতে পারবে। পরে যখনই তা আদায় করা হবে আদায় বলে গণ্য হবে,কাযা বলা যাবেনা ।\n\n"
                        + "[বাদায়ে উস সানায়ে ২/৫৪৬]",
                "It is best and most virtuous to discharge Sadaqatul Fitr before the Eid prayer. If it is not paid at this time, it can be paid subsequently whenever desired. Whenever it is discharged afterwards, it will be considered fulfilled (Ada') rather than missed (Qada').\n\n"
                        + "[Bada'i al-Sana'i 2/546]"
        ));

        // 5. সাদকায়ে ফিতর কার পক্ষ থেকে দেওয়া ওয়াজিব?
        list.add(new HajjHistoryCardItem(
                5,
                "সাদকায়ে ফিতর কার পক্ষ থেকে দেওয়া ওয়াজিব?",
                "On Whose Behalf is Sadaqatul Fitr Obligatory?",
                "নেসাব পরিমাণ মালের মালিক যিনি,সদকায়ে ফিতর আদায় করা তার পক্ষ থেকে ওয়াজিব। না-বালিগ সন্তান নিজে মালিকে নিছাব না হলে তার পক্ষ থেকে সাদকায়ে ফিতর আদায় করা পিতার উপর ওয়াজিব। আর সে যদি মালিকে নিছাব হয়,তাহলে তার মাল থে...",
                "For the person owning Nisab amount of wealth, paying Sadaqatul Fitr on his own behalf is obligatory. If an immature child is not an owner of Nisab, the father is obligated to pay on their behalf...",
                "নেসাব পরিমাণ মালের মালিক যিনি,সদকায়ে ফিতর আদায় করা তার পক্ষ থেকে ওয়াজিব। না-বালিগ সন্তান নিজে মালিকে নিছাব না হলে তার পক্ষ থেকে সাদকায়ে ফিতর আদায় করা পিতার উপর ওয়াজিব। আর সে যদি মালিকে নিছাব হয়,তাহলে তার মাল থেকে সাদকায়ে ফিতর আদায় করতে হবে।\n\n"
                        + "[শরহে বেকায়া ১খন্ড সাদকায়ে ফিতর অধ্যায়]",
                "Paying Sadaqatul Fitr is obligatory upon the owner of Nisab-amount wealth on his own behalf. If an immature (minor) child is not himself an owner of Nisab, paying Sadaqatul Fitr on his behalf is obligatory upon the father. But if the minor child possesses Nisab-amount wealth, it shall be discharged from the child's own assets.\n\n"
                        + "[Sharh al-Wiqayah, Vol. 1, Chapter on Sadaqatul Fitr]"
        ));

        // 6. প্রবাসী মুকীম ব্যক্তি কোন হিসাবে ফিতরা দিবে?
        list.add(new HajjHistoryCardItem(
                6,
                "প্রবাসী মুকীম ব্যক্তি কোন হিসাবে ফিতরা দিবে?",
                "Which Standard Should an Expatriate Resident Use for Fitra?",
                "বিদেশে অবস্থানরত বাংলাদেশ প্রবাসী কোন ব্যক্তি যদি বাংলাদেশে সাদাকাতুল ফিতর আদায় করতে চান তাহলে যে দেশে অবস্থান করছেন সেখানকার মূল্য হিসাবে তিনি ফিতর আদায় করবেন। এটাই অতি বিশুদ্ধমত । তবে হ্যা বাংলাদেশের মূল্য ধরে দিলেও দেয়া যা...",
                "If an expatriate Bangladeshi residing abroad wishes to pay Sadaqatul Fitr in Bangladesh, he should pay according to the value of the country of his residence. This is the most authentic view...",
                "বিদেশে অবস্থানরত বাংলাদেশ প্রবাসী কোন ব্যক্তি যদি বাংলাদেশে সাদাকাতুল ফিতর আদায় করতে চান তাহলে যে দেশে অবস্থান করছেন সেখানকার মূল্য হিসাবে তিনি ফিতর আদায় করবেন। এটাই অতি বিশুদ্ধমত । তবে হ্যা বাংলাদেশের মূল্য ধরে দিলেও দেয়া যাবে তা বৈধ আছে। প্রবাসীদের জন্য উত্তম হলো যে দেশের মূল্য হিসাব করা হলে সাদাকা গ্রহণকারী গরীব মিসকিন প্রমূখদের উপকার বেশি হবে সেখানকার মূল্য বিবেচনা করা।\n\n"
                        + "বর্তমানে লন্ডন আমেরিকা সৌদি আরব ইত্যাদি উন্নত দেশেগুলোতে অবস্থানরত প্রবাসীগণ স্বীয় বাংলাদেশে ফিতরা আদায় করতে চাইলে আপনি যে দেশে আছেন সেখানকার মূল্য ধরে টাকা পাঠিয়ে দিবেন।এতে পৌনে দুই সের আটা বা গম ইত্যাদির মূল্য বেশি পরিমাণে আসবে।যা সাদাকা গ্রহণকারীদের জন্য উপকারী।অনুরূপ নিয়মে দেশে অবস্থানরত না-বালিগ সন্তানের পক্ষ থেকেও তাদের ফিতরা প্রবাসী পিতাকে আদায় করতে হবে।\n\n"
                        + "[আদ্দুরুল মুনতাকা ১/২২৬, বাদায়েউসসানায়ে২/৫৪৭,ফাতাওয়ারাহিমিয়া৭/১৯৪,১৯৫,দারুল উলুম ৬/৩০৬]",
                "If an expatriate Bangladeshi residing abroad wishes to pay Sadaqatul Fitr in Bangladesh, he should calculate and disburse Fitra according to the monetary value of the country where he is residing. This is the most authentic opinion. However, paying according to the rate in Bangladesh is also valid and permissible. For expatriates, it is best to consider the rate of the country where calculating the value results in greater financial benefit for the poor and destitute recipients.\n\n"
                        + "Currently, expatriates residing in developed countries like the UK, USA, Saudi Arabia, etc., wishing to disburse Fitra in Bangladesh should send money based on the value in their country of residence. This yields a higher monetary value for the prescribed portion of flour/wheat, which is significantly more beneficial for recipients. Similarly, the expatriate father must pay Fitra for his immature children residing in the homeland.\n\n"
                        + "[Ad-Durrul Muntaqa 1/226, Bada'i al-Sana'i 2/547, Fatawa Rahimiya 7/194-195, Darul Uloom 6/306]"
        ));

        // 7. সম্পূর্ণ ফিতরা একজনকে কী দেওয়া যাবে?
        list.add(new HajjHistoryCardItem(
                7,
                "সম্পূর্ণ ফিতরা একজনকে কী দেওয়া যাবে?",
                "Can the Entire Fitra be Given to a Single Person?",
                "একজনের ফিতরা একজন ফকীরকে দেওয়া উত্তম। একজনের ফিতরা কয়েকজন ফকীরকে বন্টন করে দেওয়া কমপক্ষে মাকরূহে তানযীহী। তবে কয়েকজনের ফিতরা একজনকে দেওয়া জায়েজ আছে কোন অসুবিধা নেই । [ফাতাওয়া শামী ৩/৯২১,মাসাইলে রোযা ১৯০ পৃ: দারুল উলুম ৬/৩২৫]",
                "It is best to give one person's Fitra to a single poor person. Distributing one person's Fitra among multiple poor people is at least Makruh Tanzihi. However, giving the Fitra of several people to one person is permissible...",
                "একজনের ফিতরা একজন ফকীরকে দেওয়া উত্তম। একজনের ফিতরা কয়েকজন ফকীরকে বন্টন করে দেওয়া কমপক্ষে মাকরূহে তানযীহী। তবে কয়েকজনের ফিতরা একজনকে দেওয়া জায়েজ আছে কোন অসুবিধা নেই ।\n\n"
                        + "[ফাতাওয়া শামী ৩/৯২১,মাসাইলে রোযা ১৯০ পৃ: দারুল উলুম ৬/৩২৫]",
                "It is best to give one person's Fitra to a single poor person (faqir). Dividing one individual's Fitra among several poor persons is at least Makruh Tanzihi. However, giving the Fitra of several individuals combined to a single poor person is completely permissible without any objection.\n\n"
                        + "[Fatawa Shami 3/921, Masayele Roza p. 190, Darul Uloom 6/325]"
        ));

        return list;
    }
}
