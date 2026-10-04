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
 * Repository for রোযাদারের জন্য যা করা অপছন্দনীয় (Disliked Acts While Fasting).
 * Pre-seeded with Card 1 verbatim matching user prompt and screenshot.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaDislikedActsRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaDislikedActsRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_disliked_acts.php");
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
            // Fallback
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: রোযাদারের জন্য যা করা অপছন্দনীয়
        list.add(new HajjHistoryCardItem(
                1,
                "রোযাদারের জন্য যা করা অপছন্দনীয়",
                "Disliked Acts While Fasting",
                "উলামাগণ কিছু এমন বৈধ কর্ম করাকে রোযাদারের জন্য অপছন্দনীয় মনে করেন, যা করার ফলে তার রোযা নষ্ট হওয়ার আশঙ্কা থাকে...",
                "Scholars regard certain permissible actions as disliked (Makruh) for a fasting person because they carry the risk of invalidating the fast or reducing its spiritual reward...",
                "উলামাগণ কিছু এমন বৈধ কর্ম করাকে রোযাদারের জন্য অপছন্দনীয় মনে করেন, যা করার ফলে তার রোযা নষ্ট হওয়ার আশঙ্কা থাকে। আর তা নিম্নরূপঃ-\n\n" + 
                        "মুখে থুথু জমা করে গিলে নেওয়া।\nগয়ের বা শ্লেমা গিলা।\nচুইংগাম জাতীয় কিছু চিবানো।\nদাঁতের ফাঁকে লেগে থাকা খাবার পরিষ্কার না করা।\nঅপ্রয়োজনে খাবার চেখে দেখা। কারণ, তা গলার নিচে নেমে যাওয়ার আশঙ্কা আছে।\nএমন জিনিস নাকে নিয়ে ঘ্রাণ নেওয়া (শোঁকা); যা রোযাদারের নিঃশ্বাসের সাথে গলার ভিতরে যেতে পারে।\nস্ত্রীর সাথে এমন আচরণ করা, যা রোযাদারের যৌনক্ষুধা জাগ্রত করে। যেমন চুম্বন, কোলাকুলি, গলাগলি প্রভৃতি।\nএমন কিছু করা, যাতে তার শরীর দুর্বল হয়ে যাবে এবং রোযা চালিয়ে যেতে কষ্ট হবে। যেমন দূষিত রক্ত বহিষ্করণ ও অধিক রক্তদান।\nকুল্লি করা ও নাকে পানি নেওয়াতে অতিরঞ্জন করা।[1]\nমাজন বা টুথ্ পেষ্ট্ দিয়ে দাঁত মাজা।\nএ ছাড়া এমন কিছু কর্ম রয়েছে, যা করলে রোযাদারের রোযা অসম্পূর্ণ থেকে যায় এবং তার সওয়াবও কম হয়ে যায়। যেমন মিথ্যা কথা বলা, মিথ্যা সাক্ষ্য দেওয়া, গীবত করা, চুগলী করা, অনুরূপ প্রত্যেক সেই কথা বলা, যা শরীয়তঃ বলা নিষিদ্ধ। কারণ, প্রিয় নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি রোযা রেখে মিথ্যা কথা ও তার উপর আমল ত্যাগ করতে পারল না, সে ব্যক্তির পানাহার ত্যাগ করার মাঝে আল্লাহর কোন প্রয়োজন নেই।’’[2]\n\n" + 
                        "[1] (দ্রঃ আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ১৬২-১৬৩পৃঃ, তাযকীরু ইবাদির রাহমান, ফীমা অরাদা বিসিয়ামি শাহরি রামাযান ১৬-১৭পৃঃ)\n\n" + 
                        "[2] (বুখারী ৬০৫৭, ইবনে মাজাহ ১৬৮৯, আহমাদ, মুসনাদ ২/৪৫২, ৫০৫)",
                "Scholars consider certain actions disliked (Makruh) for a fasting person due to the risk of invalidating the fast or significantly diminishing its reward:\n\n" + 
                        "1. Deliberately accumulating saliva in the mouth and swallowing it.\n2. Swallowing phlegm or mucus.\n3. Chewing gum or similar non-dissolving substances.\n4. Failing to clean and remove food particles trapped between teeth.\n5. Tasting food unnecessarily, due to the risk of it slipping down the throat.\n6. Sniffing or inhaling powdery scents or volatile aromas that can enter the throat with breathing.\n7. Romantic physical intimacy with one's spouse that provokes uncontrollable lust (such as passionate kissing or embracing).\n8. Excessive blood donation or cupping (Hijama) that severely debilitates the body and hinders fasting.\n9. Exaggerated rinsing of the mouth or deep sniffing of water into the nose during ablution.[1]\n10. Using flavored toothpaste or toothpowder during fasting hours.\n\n" + 
                        "Furthermore, spiritual sins render the fast deficient and strip away its divine reward: lying, bearing false witness, backbiting (Gheebah), slander (Nameemah), and any prohibited speech. The Prophet (peace and blessings be upon him) said: 'Whoever does not give up false speech and acting upon it, Allah has no need of his leaving food and drink.'[2]\n\n" + 
                        "References:\n[1] (Ahkamus Sawmi wal-I'tikaf, pp. 162-163; Tazkeeru Ibadir Rahman, pp. 16-17)\n[2] (Sahih Bukhari 6057, Sunan Ibn Majah 1689, Musnad Ahmad 2/452)"
        ));

        return list;
    }
}
