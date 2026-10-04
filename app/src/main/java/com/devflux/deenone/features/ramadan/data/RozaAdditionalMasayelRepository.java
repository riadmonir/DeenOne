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
 * Repository for রোযা সংক্রান্ত আরো কিছু মাসায়েল (Additional Masayel of Fasting).
 * Pre-seeded with 4 cards verbatim matching user prompt and screenshot.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaAdditionalMasayelRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaAdditionalMasayelRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_additional_masayel.php");
            URL url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                if (jsonResponse.has("success") && jsonResponse.get("success").getAsBoolean()) {
                    JsonArray data = jsonResponse.getAsJsonArray("data");
                    List<HajjHistoryCardItem> remoteList = new ArrayList<>();
                    for (JsonElement elem : data) {
                        JsonObject obj = elem.getAsJsonObject();
                        int cardNum = obj.has("card_number") ? obj.get("card_number").getAsInt() : 1;
                        String titleBn = obj.has("title_bn") ? obj.get("title_bn").getAsString() : "";
                        String titleEn = obj.has("title_en") ? obj.get("title_en").getAsString() : "";
                        String previewBn = obj.has("preview_bn") ? obj.get("preview_bn").getAsString() : "";
                        String previewEn = obj.has("preview_en") ? obj.get("preview_en").getAsString() : "";
                        String fullBn = obj.has("full_content_bn") ? obj.get("full_content_bn").getAsString() : "";
                        String fullEn = obj.has("full_content_en") ? obj.get("full_content_en").getAsString() : "";

                        remoteList.add(new HajjHistoryCardItem(cardNum, titleBn, titleEn, previewBn, previewEn, fullBn, fullEn));
                    }

                    if (!remoteList.isEmpty()) {
                        cachedList.clear();
                        cachedList.addAll(remoteList);
                        if (callback != null) {
                            mainHandler.post(() -> callback.onDataLoaded(new ArrayList<>(cachedList)));
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // Keep existing cache resiliently
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();
        list.add(new HajjHistoryCardItem(1, "ঈদের চাঁদ", "The Crescent Moon of Eid", "এ কথা পূর্বেই আলোচিত হয়েছে যে, মেঘ বা অন্য কোন কারণে চাঁদ যথাসময়ে না দেখা গেলে মাসের তারীখ ৩০ পূর্ণ করে নিতে হবে। অবশ্য ঈদের চাঁদ প্রমাণ করার জন্য ২ জন মুসলিমের সাক্ষ্য প্রয়োজন...", "As discussed earlier, if the moon is not visible due to clouds or other reasons, the month must complete 30 days. However, to establish the sighting of the Eid crescent, testimony of two trustworthy Muslims is required...", "এ কথা পূর্বেই আলোচিত হয়েছে যে, মেঘ বা অন্য কোন কারণে চাঁদ যথাসময়ে না দেখা গেলে মাসের তারীখ ৩০ পূর্ণ করে নিতে হবে। অবশ্য ঈদের চাঁদ প্রমাণ করার জন্য ২ জন মুসলিমের সাক্ষ্য প্রয়োজন। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তোমরা চাঁদ দেখে রোযা রাখ, চাঁদ দেখে রোযা ছাড়। যদি চাঁদ না দেখা যায়, তাহলে মাস ৩০ পূর্ণ করে নাও। কিন্তু যদি দুই জন মুসলিম সাক্ষ্য দেয়, তাহলে তোমরা রোযা রাখ ও রোযা ছাড়।’’[1]\n\nপক্ষান্তরে রোযার মাসের শুরু হওয়ার কথা প্রমাণ করার জন্য এ কথা প্রমাণিত যে, আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) একজন লোকের সাক্ষি নিয়ে রোযা রেখেছেন।[2]\n\n[1] (আহমাদ, মুসনাদ ৪/৩২১, নাসাঈ, দারাকুত্বনী, সুনান, সহীহ নাসাঈ, আলবানী ১৯৯৭, ইরওয়াউল গালীল, আলবানী ৯০৯নং)\n\n[2] (আবূ দাঊদ ২৩৪২, দাঃ, দারাকুত্বনী, সুনান, বাইহাকী ৪/২১২, ইরওয়াউল গালীল, আলবানী ৯০৮নং)", "It has already been discussed that if the moon is not sighted in due time owing to clouds or other factors, the count of the month must be completed to thirty days. However, to establish the sighting of the Eid crescent moon, the testimony of two trustworthy Muslim witnesses is required. As the Prophet (peace and blessings of Allah be upon him) said: \"Fast when you see it (the crescent), and break your fast when you see it. But if the moon is obscured from you, complete thirty days. However, if two Muslim witnesses give testimony, then fast and break your fast.\" [1]\n\nOn the other hand, regarding the commencement of the month of Ramadan, it is authentically established that the Messenger of Allah (peace and blessings of Allah be upon him) accepted the testimony of a single individual to begin fasting. [2]\n\n[1] (Ahmad, Musnad 4/321; An-Nasa'i, Ad-Daraqutni, Sunan, Sahih An-Nasa'i, Al-Albani 1997; Irwa al-Ghalil, Al-Albani no. 909)\n\n[2] (Abu Dawud 2342; Ad-Daraqutni, Sunan; Al-Bayhaqi 4/212; Irwa al-Ghalil, Al-Albani no. 908)"));
        list.add(new HajjHistoryCardItem(2, "কেউ একা চাঁদ দেখলে", "Sighting the Crescent Moon Alone", "ঈদের চাঁদ কেউ একা দেখলে সে কিন্তু একা একা ঈদ করতে পারে না। বরং চাঁদ দেখা সত্ত্বেও তার জন্য রোযা রাখা ওয়াজেব...", "If someone sights the Eid crescent alone, he cannot celebrate Eid by himself. Rather, it remains obligatory for him to fast...", "ঈদের চাঁদ কেউ একা দেখলে সে কিন্তু একা একা ঈদ করতে পারে না। বরং চাঁদ দেখা সত্ত্বেও তার জন্য রোযা রাখা ওয়াজেব। কেননা, শওয়ালের চাঁদ দুই জন মুসলিম দেখার সাক্ষ্য না দেওয়া পর্যন্ত শরীয়তের দৃষ্টিতে প্রমাণ হয় না। তা ছাড়া মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘ঈদ সেদিন, যেদিন লোকেরা ঈদ করে। কুরবানী সেদিন, যেদিন লোকেরা কুরবানী করে।’’[1] যেহেতু শরীয়তে জামাআতের বড় মর্যাদা আছে।\n\nমতান্তরে যে ব্যক্তি একা চাঁদ দেখবে সে পরের দিন রোযা রাখবে না। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘তোমরা চাঁদ দেখে রোযা রাখ, চাঁদ দেখে রোযা ছাড়।’’ তবে প্রকাশ্যে নয়, বরং গোপনে ইফতার করবে সে। যাতে সে জামাআত-বিরোধী না হয়ে যায়। অথবা তাকে কেউ অসঙ্গত অপবাদ না দিয়ে বসে। আর আল্লাহই অধিক জানেন।[2]\n\n[1] (সহীহ তিরমিযী, আলবানী ৬৪৩, ইরওয়াউল গালীল, আলবানী ৯০৫নং)\n\n[2] (আশ্শারহুল মুমতে’ ৬/৩২৯, আসইলাতুন অআজবিবাতুন ফী স্বালাতিল ঈদাঈন ২৫পৃঃ, আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ৪২পৃঃ)", "If an individual sights the Eid crescent alone, he cannot celebrate Eid alone. Rather, despite sighting the moon, it remains obligatory for him to keep fasting. This is because according to the Islamic Shariah, the Shawwal moon is not established until two Muslims testify to having sighted it. Moreover, the Prophet (peace and blessings of Allah be upon him) said: \"Eid is the day when people celebrate Eid, and Adha (sacrifice) is the day when people sacrifice.\" [1] For the Shariah attaches immense importance and honor to unity with the community (Jama'ah).\n\nAccording to an alternate scholarly opinion, the person who sighted the moon alone does not fast the following day, in accordance with the Prophet's (peace and blessings of Allah be upon him) saying: \"Fast when you see it, and break your fast when you see it.\" However, he must break his fast privately and not openly, so as not to oppose the community or bring unjustified accusations upon himself. And Allah knows best. [2]\n\n[1] (Sahih At-Tirmidhi, Al-Albani 643; Irwa al-Ghalil, Al-Albani no. 905)\n\n[2] (Ash-Sharh al-Mumti' 6/329; As'ilah wa Ajwibah fi Salat al-Eidayn p. 25; Ahkam as-Sawm wal-I'tikaf, Abu Sari M. Abdul Hadi p. 42)"));
        list.add(new HajjHistoryCardItem(3, "রোযা ২৮টি হলে", "When Only 28 Fasts Have Been Completed", "২৮ দিন রোযা রাখার পর শওয়ালের চাঁদ শরয়ী সাক্ষ্য দ্বারা প্রমাণিত হলে জানতে হবে যে, রমাযান মাসের প্রথম দিন অবশ্যই ছুটে গেছে...", "If the Shawwal crescent is proven by valid Shariah testimony after fasting only 28 days, it is known that the first day of Ramadan was missed...", "২৮ দিন রোযা রাখার পর শওয়ালের চাঁদ শরয়ী সাক্ষ্য দ্বারা প্রমাণিত হলে জানতে হবে যে, রমাযান মাসের প্রথম দিন অবশ্যই ছুটে গেছে। সুতরাং সে ক্ষেত্রে ঐ দিন ঈদের পরে কাযা করতে হবে। কারণ, চান্দ্র মাস ২৮ দিনের হতেই পারে না। হয় ৩০ দিনে মাস হবে, নচেৎ ২৯ দিনে।[1]\n\n[1] (ফাসিঃ মুসনিদ ১৫পৃঃ)", "If the Shawwal crescent is established by valid Shariah testimony after fasting only 28 days, one must know that the first day of Ramadan was certainly missed. Therefore, in that case, that day must be made up (Qadha) after Eid. This is because a lunar Islamic month can never consist of 28 days; it is either 30 days or 29 days. [1]\n\n[1] (Fasi, Musnid p. 15)"));
        list.add(new HajjHistoryCardItem(4, "রোযা ৩১টি কখন হয়?", "When Does Fasting Reach 31 Days?", "পূর্ব দিককার (প্রাচ্যের) দেশগুলিতে চাঁদ ১ অথবা ২ দিন পরে দেখা দেয়। এখন ২৯শে রমাযান চাঁদ দেখার পর অথবা ৩০শে রমাযান ঐ দিককার কোন দেশে সফর করলে...", "In eastern countries, the moon appears 1 or 2 days later. If someone travels to an eastern country after 29th or 30th Ramadan...", "পূর্ব দিককার (প্রাচ্যের) দেশগুলিতে চাঁদ ১ অথবা ২ দিন পরে দেখা দেয়। এখন ২৯শে রমাযান চাঁদ দেখার পর অথবা ৩০শে রমাযান ঐ দিককার কোন দেশে সফর করলে সেখানে গিয়ে দেখবে তার পরের দিনও রোযা। সে ক্ষেত্রে তাকে ঐ দেশের মুসলিমদের সাথে রোযা রাখতে হবে। অতঃপর তারা ঈদ করলে তাদের সাথে সেও ঈদ করবে; যদিও তার রোযা ৩১টি হয়ে যায়। কারণ, মহান আল্লাহ বলেন,\n\n(فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ)\n\nঅর্থাৎ, অতএব তোমাদের মধ্যে যে কেউ এ মাস পাবে সে যেন এ মাসে রোযা রাখে। (কুরআনুল কারীম ২/১৮৫)\n\nআর মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘রোযা সেদিন, যেদিন লোকেরা রোযা রাখে। ঈদ সেদিন, যেদিন লোকেরা ঈদ করে।’’[1]\n\nকিন্তু যদি কেউ পূর্ব থেকে পশ্চিম দিকে ২৮শে রমাযান সফর করে, অতঃপর তার পর দিনই সেখানে ঈদ হয়, তাহলে সেও রোযা ভেঙ্গে লোকদের সাথে ঈদ করবে। অবশ্য তার পরে সে একটি রোযা কাযা রাখবে। কারণ, মাস ২৯ দিনের কম হয় না। পক্ষান্তরে যদি ২৯শে রমাযান সফর করে তার পরের দিন ঈদ হয়, তাহলে তাদের সাথে ঈদ করার পর তাকে আর কোন রোযা কাযা করতে হবে না। কারণ, তার ২৯টি রোযা হয়ে গেছে এবং মাস ২৯ দিনেও হয়।[2]\n\nপরন্তু যদি কেউ ঈদের দিনে ঈদ করে প্রাচ্যের দেশে সফর করে এবং সেখানে গিয়ে দেখে সেখানকার লোকেদের রোযা চলছে, তাহলে সে ক্ষেত্রে তাকে পানাহার বন্ধ করতে হবে না এবং রোযা কাযা করতেও হবে না। কেননা, সে শরয়ী নিয়ম মতে রোযা ভেঙ্গেছে। অতএব এ দিন তার জন্য পানাহার বৈধ হওয়ার দিন।[3]\n\n[1] (তিরমিযী, ইরওয়াউল গালীল, আলবানী ৯০৫, সিলসিলাহ সহীহাহ, আলবানী ২২৪নং)\n\n[2] (ইবনে বায, ফাসিঃ মুসনিদ ১৬পৃঃ)\n\n[3] (আআসাঈঃ ২৮পৃঃ)", "In eastern countries, the moon may be sighted 1 or 2 days later. If someone travels to an eastern country after sighting the crescent on the 29th of Ramadan or on the 30th of Ramadan, he may find that the following day is still a day of fasting there. In that scenario, he must fast along with the Muslims of that country. Then, when they celebrate Eid, he celebrates Eid with them, even if his fasts total 31 days. This is because Almighty Allah states:\n\n(فَمَنْ شَهِدَ مِنْكُمُ الشَّهْرَ فَلْيَصُمْهُ)\n\n\"So whoever of you sights the month, let him fast it.\" (Holy Quran 2:185)\n\nAnd the Prophet (peace and blessings of Allah be upon him) said: \"Fasting is the day when people fast, and Eid is the day when people celebrate Eid.\" [1]\n\nHowever, if someone travels from east to west on the 28th of Ramadan, and the next day is Eid there, he breaks his fast and celebrates Eid with the local people. Afterwards, he must make up (Qadha) one fast, because a month cannot be less than 29 days. On the other hand, if he travels on the 29th of Ramadan and the following day is Eid there, after celebrating Eid with them he does not need to make up any fast, because he has completed 29 fasts and a lunar month can be 29 days. [2]\n\nFurthermore, if someone celebrates Eid on Eid day and then travels to an eastern country where people are still fasting, he is not required to abstain from food and drink, nor does he have to make up any fast. This is because he broke his fast according to valid Shariah rules; therefore, that day is lawful for him to eat and drink. [3]\n\n[1] (Tirmidhi; Irwa al-Ghalil, Al-Albani 905; Silsilah Sahihah, Al-Albani no. 224)\n\n[2] (Ibn Baz, Fasi, Musnid p. 16)\n\n[3] (A'asai, p. 28)"));
        return list;
    }
}
