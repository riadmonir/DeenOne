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
 * Repository for যাতে রোযা নষ্ট ও বাতিল হয় (Things That Invalidate the Fast).
 * Pre-seeded with all 11 cards verbatim matching the user prompt.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaInvalidatorsRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaInvalidatorsRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_invalidators.php");
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

        // Card 1: স্ত্রী-সঙ্গম
        list.add(new HajjHistoryCardItem(
                1,
                "স্ত্রী-সঙ্গম",
                "Sexual Intercourse",
                "যে সব কারণে রোযা নষ্ট হয় তা দুই শ্রেণীর; প্রথম শ্রেণীর কারণ রোযা নষ্ট করে এবং তাতে কাযা ওয়াজেব হয়। আর দ্বিতীয় শ্রেণীর কারণ রোযা নষ্ট করে এবং কাযার সাথে কাফ্ফারাও ওয়াজেব করে...",
                "The causes that invalidate fasting are of two types: those requiring Qadha only, and those requiring both Qadha and major Kaffarah (expiation)...",
                "যে সব কারণে রোযা নষ্ট হয় তা দুই শ্রেণীর; প্রথম শ্রেণীর কারণ রোযা নষ্ট করে এবং তাতে কাযা ওয়াজেব হয়। আর দ্বিতীয় শ্রেণীর কারণ রোযা নষ্ট করে এবং কাযার সাথে কাফ্ফারাও ওয়াজেব করে।<br><br>"
                        + "যে কারণে রোযা নষ্ট হয় এবং কাযার সাথে কাফ্ফারাও ওয়াজেব হয়; তা হলঃ-<br><br>"
                        + "<b>১। স্ত্রী-সঙ্গমঃ</b><br><br>"
                        + "সঙ্গম বলতে স্ত্রী-যোনীতে স্বামীর লিঙ্গাগ্র প্রবেশ হলেই রোযা নষ্ট হয়ে যায়; তাতে বীর্যপাত হোক, আর নাই হোক। তদনুরূপ অবৈধভাবে পায়খানা-দ্বারে লিঙ্গাগ্র প্রবেশ করালেও রোযা বাতিল গণ্য হয়।<br><br>"
                        + "জ্ঞাতব্য যে, স্ত্রীর পায়খানাদ্বারে সঙ্গম করা মহাপাপ এবং এক প্রকার কুফরী।<br><br>"
                        + "বলা বাহুল্য রোযা অবস্থায় যখনই রোযাদার স্ত্রী-মিলন করবে, তখনই তার রোযা নষ্ট হয়ে যাবে। সুতরাং এ মিলন যদি রমাযানের দিনে সংঘটিত হয় এবং রোযা রোযাদারের জন্য ফরয হয়, (অর্থাৎ রোযা কাযা করা তার জন্য বৈধ না হয়) তাহলে ঐ মিলনের ফলে যথাক্রমে ৫টি জিনিস সংঘটিত হবেঃ-<br><br>"
                        + "(ক) কাবীরা গোনাহ; আর তার ফলে তাকে তওবা করতে হবে।<br>"
                        + "(খ) তার রোযা বাতিল হয়ে যাবে।<br>"
                        + "(গ) তাকে ঐ দিনের অবশিষ্ট অংশ পানাহার ইত্যাদি থেকে বিরত থাকতে হবে।<br>"
                        + "(ঘ) ঐ দিনের রোযা (রমাযান পর) কাযা করতে হবে।<br>"
                        + "(ঙ) বৃহৎ কাফ্ফারা আদায় করতে হবে। আর তা হল, একটি ক্রীতদাসকে দাসত্ব থেকে মুক্তি দিতে হবে। তাতে সক্ষম না হলে, লাগাতার (একটানা) দুই মাস রোযা রাখতে হবে। আর তাতে সক্ষম না হলে, ৬০ জন মিসকীনকে খাদ্যদান করতে হবে।<br><br>"
                        + "এ ব্যাপারে মূল ভিত্তি হল, মহান আল্লাহর এই বাণী,<br><br>"
                        + "<font color='#00695C'><b>(أُحِلَّ لَكُمْ لَيْلَةَ الصِّيَامِ الرَّفَثُ إِلَى نِسَائِكُمْ ...) الآية</b></font><br><br>"
                        + "অর্থাৎ, রোযার রাতে তোমাদের জন্য স্ত্রী-সম্ভোগ হালাল করা হয়েছে। (কুরআনুল কারীম ২/১৮৭)<br><br>"
                        + "আর আবূ হুরাইরা কর্তৃক বর্ণিত, তিনি বলেন, একদা আমরা নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর কাছে বসে ছিলাম। এমন সময় তাঁর নিকট এক ব্যক্তি উপস্থিত হয়ে বলল, ‘হে আল্লাহর রসূল! আমি ধ্বংসগ্রস্ত হয়ে পড়েছি।’ তিনি বললেন, ‘‘কোন জিনিস তোমাকে ধ্বংসগ্রস্ত করে ফেলল?’’ লোকটি বলল, ‘আমি রোযা অবস্থায় আমার স্ত্রীর সাথে সঙ্গম করে ফেলেছি।’ এ কথা শুনে আল্লাহর রসূল (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) তাকে বললেন, ‘‘তুমি কি একটি ক্রীতদাস মুক্ত করতে পারবে?’’ লোকটি বলল, ‘জী না।’ তিনি বললেন, ‘‘তাহলে কি তুমি একটানা দুই মাস রোযা রাখতে পারবে?’’ সে বলল, ‘জী না।’ তিনি বললেন, ‘‘তাহলে কি তুমি ৬০ জন মিসকীনকে খাদ্যদান করতে পারবে?’’ লোকটি বলল, ‘জী না।’ ---[1]<br><br>"
                        + "যে মহিলার উপর রোযা ফরয, সেই মহিলা সম্মত হয়ে রমাযানের দিনে স্বামী-সঙ্গম করলে তারও উপর কাফ্ফারা ওয়াজেব। অবশ্য তার ইচ্ছা না থাকা সত্ত্বেও স্বামী যদি তার সাথে জোরপূর্বক সহবাস করতে চায়, তাহলে তার জন্য যথাসাধ্য তা প্রতিহত করা জরুরী। রুখতে না পারলে তার উপর কাফ্ফারা ওয়াজেব নয়।<br><br>"
                        + "এই জন্যই যে মহিলা জানে যে, তার স্বামীর কামশক্তি বেশী; সে তার কাছে প্রেম-হৃদয়ে কাছাকাছি হলে নিজের যৌন-পিপাসা দমন রাখতে পারে না, সেই মহিলার জন্য উচিৎ, রমাযানের দিনে তার কাছ থেকে দূরে থাকা এবং প্রসাধন ও সাজ-সজ্জা না করা। তদনুরূপ স্বামীর জন্যও উচিৎ, পদস্খলনের জায়গা থেকে দূরে থাকা এবং রোযা থাকা অবস্থায় স্ত্রীর কাছ না ঘেঁষা; যদি আশঙ্কা হয় যে, উগ্র যৌন-কামনায় সে তার মনকে কাবু রাখতে পারবে না। কারণ, এ কথা বিদিত যে, প্রত্যেক নিষিদ্ধ জিনিসই ঈপ্সিত।[2]<br><br>"
                        + "পক্ষান্তরে যদি রমাযানের রোযা কাযা রাখতে গিয়ে স্ত্রী-সঙ্গম করে ফেলে, তাহলে তার ফলে কাফ্ফারা নেই। আর তার জন্য ঐ দিনের বাকী অংশ পানাহার ইত্যাদি থেকে বিরত থাকাও জরুরী নয়। অবশ্য তার গোনাহ হবে। কারণ, সে ইচ্ছাকৃত একটি ওয়াজেব রোযা নষ্ট করে তাই।[3]<br><br>"
                        + "মুসাফির যদি সফরে থাকা অবস্থায় রোযা রেখে স্ত্রী-সহবাস করে ফেলে, তাহলে তার জন্য কেবল কাযা ওয়াজেব, কাফ্ফারা ওয়াজেব নয়। যেমন, ঐ দিনের বাকী অংশ পানাহার ইত্যাদি থেকে বিরত থাকাও তার জন্য জরুরী নয়। কেননা, সে মুসাফির। আর মুসাফিরের জন্য সফরে রোযা ভাঙ্গা (এবং পরে কাযা করা) বৈধ।<br><br>"
                        + "অনুরূপভাবে এমন রোগী, যার রোগের জন্য রোযা ভাঙ্গা বৈধ ছিল; কিন্তু কষ্ট করে সে রোযা রেখেছিল। সে যদি তার স্ত্রীর সাথে সঙ্গম করে, যে সেই দিনেই মাসিক থেকে পবিত্রা হয়েছে, তাহলে তারও গোনাহ হবে না; অবশ্য কাযা ওয়াজেব।[4]<br><br>"
                        + "যে ব্যক্তি যে বৈধ ওযরের ফলে রোযা বন্ধ রেখেছিল, দিনের মধ্যে তার সেই ওযর দূর হয়ে যাওয়ার পর যদি স্ত্রী-সহবাস করে, তাহলে তার জন্য কাফ্ফারা ওয়াজেব নয়। যেমন, কোন মুসাফির যদি দিন থাকতে রোযা না রেখে ঘরে ফিরে দেখে যে, তার স্ত্রী সেই দিনেই (ফজরের পর) মাসিক থেকে পবিত্রা হয়েছে, তাহলে সঠিক মতে তাদের জন্য সঙ্গম বৈধ। এতে স্বামী-স্ত্রীর কোন প্রকার পাপ হবে না। যেহেতু ঐ দিন শরীয়তের অনুমতিক্রমে তাদের জন্য মান্য নয় এবং ঐ দিনে রোযা না রাখাও তাদের পক্ষে অনুমোদিত।[5]<br><br>"
                        + "যদি কোন ব্যক্তি সুস্থ অবস্থায় রোযা রেখে স্ত্রী-সহবাস করার পর দিন থাকতেই এমন অসুস্থ হয়ে পড়ে, যাতে তার জন্য রোযা ভাঙ্গা বৈধ, তাহলেও তার জন্য কাফ্ফারা ওয়াজেব; যদিও তার জন্য দিনের শেষভাগে (অসুস্থ হওয়ার পর) রোযা ভাঙ্গা বৈধ। কারণ, সহবাসের সময় সে তাতে অনুমতিপ্রাপ্ত ছিল না।<br><br>"
                        + "তদনুরূপ যে ব্যক্তি দিনের প্রথমাংশে সহবাস করার পর সফর করে তাহলে তার জন্যও কাফ্ফারা ওয়াজেব; যদিও সফর করার পরে ঐ দিনেই তার জন্য রোযা ভাঙ্গা বৈধ। কেননা, রোযা ভাঙ্গা বৈধ হওয়ার পূর্বেই সে (রমাযান) মাসের মর্যাদা ক্ষুন্ন করেছে।[6]<br><br>"
                        + "যদি কোন ব্যক্তি (কাফ্ফারা থেকে রেহাই পাওয়ার বাহানায়) প্রথমে কিছু খেয়ে অথবা পান করে তারপর স্ত্রী-সঙ্গম করে, তাহলে তার পাপ অধিক। যেহেতু সে রমাযানের মর্যাদাকে পানাহার ও সঙ্গমের মাধ্যমে ডবল করে নষ্ট করেছে। বৃহৎ কাফ্ফারা তার হক্কে অধিক কার্যকর। আর তার ঐ বাহানা ও ছলনা নিজের ঘাড়ে বোঝা স্বরূপ। তার জন্য খাঁটি তওবা ওয়াজেব।[7]<br><br>"
                        + "জ্ঞাতব্য যে, রমাযান মাসে দিনে রোযা অবস্থায় সঙ্গম ছাড়া অন্য কোন কারণে সেই ব্যক্তির জন্য কাফ্ফারা ওয়াজেব হয় না, যার জন্য রোযা রাখা ফরয। বলা বাহুল্য, নফল রোযা রেখে, কসমের কাফ্ফারার রোযা রেখে, কোন অসুবিধার ফলে ইহরাম অবস্থায় কোন নিষিদ্ধ কাজ করে ফেললে তার জরিমানার রোযা রেখে, তামাত্তু হজ্জ করতে গিয়ে কুরবানী দিতে না পেরে তার বিনিময়ে রোযা রেখে অথবা নযরের রোযা রেখে স্ত্রী-সহবাস করে ফেললে কাফ্ফারা ওয়াজেব নয়। যেমন সঙ্গম না করে (স্ত্রী-যোনীর বাইরে) বীর্যপাত করে ফেললেও কাফ্ফারা ওয়াজেব নয়।[8] অবশ্য কাযা তো ওয়াজেবই।<br><br>"
                        + "জ্ঞাতব্য যে, ব্যভিচার করে ফেললেও সহবাসের মতই কাফ্ফারা ওয়াজেব।[9] তাছাড়া ব্যভিচারের সাজা ও তওবা তো আছেই。<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (বুখারী ১৯৩৭, মুসলিম ১১১১নং)<br>"
                        + "[2] (দ্রঃ আশ্শারহুল মুমতে’ ৬/৪১৫, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৭০নং, ফাইযুর রাহীমির রাহমান, ফী আহকামি অমাওয়াইযি রামাযান ৬১পৃঃ)<br>"
                        + "[3] (আশ্শারহুল মুমতে’ ৬/৪১৩, আহকামুন মিনাস সিয়াম, ক্যাসেট, ইবনে উষাইমীন)<br>"
                        + "[4] (আশ্শারহুল মুমতে’ ৬/৪১৩)<br>"
                        + "[5] (আশ্শারহুল মুমতে’ ৬/৪২১)<br>"
                        + "[6] (আশ্শারহুল মুমতে’ ৬/৪২২)<br>"
                        + "[7] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৪৭নং)<br>"
                        + "[8] (আশ্শারহুল মুমতে’ ৬/৪২২-৪২৩)<br>"
                        + "[9] (আল-ফাওয়াইদুল জালিয়্যাহ, ইবনে বায ১১৯পৃঃ)</font>",
                "Sexual intercourse during the daytime in Ramadan when fasting is obligatory invalidates the fast, constitutes a major sin requiring repentance, entails refraining from food/drink for the rest of the day, requires making up (Qadha) the day, and mandates the major expiation (Kaffarah): freeing a slave, or fasting two consecutive months, or feeding sixty poor individuals.<br><br>"
                        + "<b>References:</b> Sahih Bukhari 1937, Sahih Muslim 1111, Ash-Sharh al-Mumti' 6/413-423."
        ));

        // Card 2: বীর্যপাত
        list.add(new HajjHistoryCardItem(
                2,
                "বীর্যপাত",
                "Ejaculation with Sexual Desire",
                "রোযা নষ্টকারী কর্মাবলীর মধ্যে জাগ্রত অবস্থায় সকাম যৌন-স্বাদ অনুভূতির সাথে বীর্যপাত অন্যতম; চাহে সে বীর্যপাত (নিজ অথবা স্ত্রীর) হস্তমৈথুন দ্বারা হোক অথবা কোলাকুলি দ্বারা...",
                "Deliberate ejaculation with sexual desire while awake (via masturbation, embracing, kissing, etc.) invalidates the fast...",
                "রোযা নষ্টকারী কর্মাবলীর মধ্যে জাগ্রত অবস্থায় সকাম যৌন-স্বাদ অনুভূতির সাথে বীর্যপাত অন্যতম; চাহে সে বীর্যপাত (নিজ অথবা স্ত্রীর) হস্তমৈথুন দ্বারা হোক অথবা কোলাকুলি দ্বারা, নচেৎ চুম্বন অথবা প্রচাপন দ্বারা। কারণ, উক্ত প্রকার সকল কর্মই হল এক এক শ্রেণীর যৌনাচার। অথচ মহান আল্লাহ (হাদীসে কুদসীতে) বলেন, ‘‘সে আমার (সন্তুষ্টি লাভের) আশায় নিজের প্রয়োজনীয় পানাহার ও যৌনাচার পরিহার করে।’’[1] আর যে ব্যক্তি যে কোন প্রকারে নিজ যৌন অনুভূতিকে উত্তেজিত করে তৃপ্তির সাথে বীর্যপাত করে, সে আসলেই নিজের যৌন-কামনা চরিতার্থ করে থাকে এবং তার রোযাতে সেই কর্ম বর্জন করে না, যা আল্লাহর উক্ত বাণীতে পানাহারের অনুরূপ।[2]<br><br>"
                        + "পরন্তু রোযাদারের জেনে রাখা উচিৎ যে, হস্ত অথবা অন্য কিছু দ্বারা বীর্যপাত ঘটানো যেমন রোযার মাসে হারাম, তেমনি অন্য মাসেও। কিন্তু রমাযানে তা অধিকরূপে হারাম। যেহেতু এ মাসের রয়েছে পৃথক মর্যাদা এবং তাতে হয়েছে রোযা ফরয।[3]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (বুখারী ১৮৯৪, মুসলিম ১১৫১নং)<br>"
                        + "[2] (আশ্শারহুল মুমতে’ ৬/৩৮৭)<br>"
                        + "[3] (ফাইযুর রাহীমির রাহমান, ফী আহকামি অমাওয়াইযি রামাযান ৬১পৃঃ)</font>",
                "Deliberate ejaculation accompanied by sexual pleasure while awake—whether through masturbation, fondling, kissing, or physical contact—invalidates the fast and necessitates Qadha without Kaffarah.<br><br>"
                        + "<b>References:</b> Sahih Bukhari 1894, Sahih Muslim 1151, Ash-Sharh al-Mumti' 6/387."
        ));

        // Card 3: পানাহার
        list.add(new HajjHistoryCardItem(
                3,
                "পানাহার",
                "Eating and Drinking",
                "পানাহার বলতে পেটের মধ্যে যে কোন প্রকারে কোন খাদ্য অথবা পানীয় পৌঁছানোকে বুঝানো হয়েছে; চাহে তা মুখ দিয়ে হোক অথবা নাক দিয়ে...",
                "Eating and drinking entails introducing any food, substance, or drink into the stomach through the mouth or nose...",
                "পানাহার বলতে পেটের মধ্যে যে কোন প্রকারে কোন খাদ্য অথবা পানীয় পৌঁছানোকে বুঝানো হয়েছে; চাহে তা মুখ দিয়ে হোক অথবা নাক দিয়ে, পানাহারের বস্ত্ত যেমনই হোক; উপকারী বা উপাদেয় হোক অথবা অপকারী বা অনুপাদেয়, হালাল হোক অথবা হারাম, অল্প হোক অথবা বেশী।<br><br>"
                        + "বলা বাহুল্য, (বিড়ি, সিগারেট, গাঁজা প্রভৃতির) ধূমপান রোযা নষ্ট করে দেয়; যদিও তা অপকারী, অনুপাদেয় ও হারাম পানীয়।<br><br>"
                        + "প্লাস্টিক বা কোন ধাতুর মালা গিলে ফেললে রোযা নষ্ট হয়ে যাবে; যদিও তা পেটে গেলে দেহের কোন উপকার সাধন হবে না। তদনরূপ যদি কেউ কোন অপবিত্র বা হারাম বস্ত্ত ভক্ষণ করে, তাহলে তারও রোযা নষ্ট হয়ে যাবে।[1]<br><br>"
                        + "পানাহারে রোযা নষ্ট হওয়ার মূল ভিত্তি হল মহান আল্লাহর এই বাণী,<br><br>"
                        + "<font color='#00695C'><b>(وَكُلُوْا وَاشْرَبُوْا حَتَّى يَتَبَيَّنَ لَكُمُ الْخَيْطُ الأَبْيَضُ مِنَ الْخَيْطِ الأَسْوَدِ مِنَ الْفَجْرِ)</b></font><br><br>"
                        + "অর্থাৎ, আর তোমরা পানাহার কর, যতক্ষণ পর্যন্ত না (রাতের) কালো অন্ধকার থেকে ফজরের সাদা রেখা তোমাদের নিকট স্পষ্ট হয়েছে। (কুরআনুল কারীম ২/১৮৭)<br><br>"
                        + "সুতরাং ফজর উদয় হওয়ার আগে পর্যন্ত মহান আল্লাহ রোযাদারের জন্য পানাহার বৈধ করেছেন। অতঃপর তিনি রাত পর্যন্ত রোযা রাখার আদেশ দিয়েছেন। আর তা হল রোযা নষ্টকারী জিনিস থেকে বিরত থাকার নাম। তিনি বলেন,<br><br>"
                        + "<font color='#00695C'><b>(ثُمَّ أَتِمُّوا الصِّيَامَ إِلَى اللَّইْلِ)</b></font><br><br>"
                        + "অর্থাৎ, অতঃপর তোমরা রাত পর্যন্ত রোযা পূর্ণ কর। (কুরআনুল কারীম ২/১৮৭) অর্থাৎ, মাগরেব পর্যন্ত।<br><br>"
                        + "আর মহান আল্লাহ (হাদীসে কুদসীতে) বলেন, ‘‘সে আমার (সন্তুষ্টি লাভের) আশায় নিজের প্রয়োজনীয় পানাহার ও যৌনাচার পরিহার করে।’’[2]<br><br>"
                        + "সুতরাং যে স্বেচ্ছায় পানাহার করবে তার রোযা নষ্ট হয়ে যাবে। সে গোনাহগার হবে, বিধায় তার জন্য তওবা ওয়াজেব এবং কাযাও। অবশ্য এর জন্য কোন কাফ্ফারা নেই। তবে ইচ্ছা করে কেউ রোযা নষ্ট করার পর তা কাযা করলেও তা কবুল হবে কি না -তা নিয়ে মতভেদ আছে。<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (দ্রঃ আশ্শারহুল মুমতে’ ৬/৩৭৯, সামানিয়া ওয়া আরবাঊন সুআলান ফিস্-সিয়াম ১৪পৃঃ)<br>"
                        + "[2] (বুখারী ১৮৯৪, মুসলিম ১১৫১নং)</font>",
                "Intentionally introducing any edible, drinkable, beneficial, or harmful substance (including smoking cigarettes, hookah, or swallowing non-food items) into the stomach invalidates the fast, requiring sincere repentance and Qadha.<br><br>"
                        + "<b>References:</b> Quran 2:187, Sahih Bukhari 1894, Sahih Muslim 1151."
        ));

        // Card 4: যা এক অর্থে পানাহার
        list.add(new HajjHistoryCardItem(
                4,
                "যা এক অর্থে পানাহার",
                "What Counts as Nourishment (Eating & Drinking)",
                "স্বাভাবিক পানাহারের পথ ছাড়া অন্য ভাবে পানাহারের কাজ নিলে তাতেও রোযা নষ্ট হয়ে যাবে। যেমন খাবারের কাজ দেয় এমন (স্যালাইন ইঞ্জেকশন) নিলে রোযা হবে না...",
                "Administering nutritional substances that substitute for food and drink, such as nourishing saline injections or blood transfusions, invalidates the fast...",
                "স্বাভাবিক পানাহারের পথ ছাড়া অন্য ভাবে পানাহারের কাজ নিলে তাতেও রোযা নষ্ট হয়ে যাবে। যেমন খাবারের কাজ দেয় এমন (স্যালাইন ইঞ্জেকশন) নিলে রোযা হবে না। কেননা, তাতে পানাহারের অর্থ বিদ্যমান। আর শরীয়তের নির্দেশ-বাণীতে যে ব্যাপক অর্থ পাওয়া যায়, যে কোন অবস্থায় সেই অর্থ পাওয়া গেলে সেই নির্দেশ ঐ অবস্থার উপর আরোপ করা হবে।[1]<br><br>"
                        + "তদনুরূপ রোযা অবস্থায় দেহের রক্ত পরিবর্তন করলেও রোযা নষ্ট হয়ে যাবে। কারণ, তাতে দেহের মধ্যে নতুন ও নির্মল রক্ত প্রদান করা হয়। আর রক্তের সাথে অন্য কোন বস্ত্ত বা ঔষধ থাকলে তো তা রোযা নষ্ট হওয়ার অন্য একটি কারণ।[2]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (তাযকীরু ইবাদির রাহমান, ফীমা অরাদা বিসিয়ামি শাহরি রামাযান ৪৪পৃঃ)<br>"
                        + "[2] (ইবনে বায, ফাতাওয়া মুহিম্মাহ, তাতাআল্লাকু বিস্সিয়াম ৩৮পৃঃ)</font>",
                "Intravenous injections that provide nourishment or replace food and water (such as glucose or nutritional salines) invalidate the fast, as do full blood transfusions.<br><br>"
                        + "<b>References:</b> Tadhkeeru 'Ibad ar-Rahman p. 44, Fatawa Ibn Baz p. 38."
        ));

        // Card 5: ইচ্ছাকৃত বমি করা
        list.add(new HajjHistoryCardItem(
                5,
                "ইচ্ছাকৃত বমি করা",
                "Deliberate Vomiting",
                "ইচ্ছাকৃত বমি করলে, অর্থাৎ পেটে থেকে খাওয়া খাদ্য (বমন ও উদ্গিরণ করে) বের করে দিলে... রোযা নষ্ট হয়ে যাবে। পক্ষান্তরে অনিচ্ছাকৃত বমি হলে রোযা নষ্ট হয় না...",
                "Deliberately inducing vomiting breaks the fast and incurs Qadha, whereas involuntary, overpowering vomiting does not break the fast...",
                "ইচ্ছাকৃত বমি করলে, অর্থাৎ পেটে থেকে খাওয়া খাদ্য (বমন ও উদ্গিরণ করে) বের করে দিলে, মুখে আঙ্গুল ভরে, পেট নিংড়ে, কোন বিকট দুর্গন্ধ জাতীয় কিছুর ঘ্রাণ নাকে নিয়ে, অথবা অরুচিকর ঘৃণ্য কিছু দেখে উল্টি করলে রোযা নষ্ট হয়ে যাবে। সে ক্ষেত্রে ঐ রোযার কাযা জরুরী।[1] পক্ষান্তরে সামলাতে না পেরে অনিচ্ছাকৃতভাবে বমি হয়ে গেলে রোযা নষ্ট হয় না। যেহেতু মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘রোযা অবস্থায় যে ব্যক্তি বমনকে দমন করতে সক্ষম হয় না, তার জন্য কাযা নেই। পক্ষান্তরে যে ইচ্ছাকৃতভাবে বমি করে, সে যেন ঐ রোযা কাযা করে।’’[2]<br><br>"
                        + "ঢেকুর তুলতে গিয়ে যদি রোযাদারের গলাতে কিছু খাবার উঠে আসে অথবা খাবারের স্বাদ গলাতে অনুভব করে এবং তারপরেই ঢোক গিলে নেয়, তাহলে তাতে রোযার কোন ক্ষতি হয় না। কারণ, তা আসলে মুখ পর্যন্ত বের হয়ে আসে না। বরং গলা পর্যন্ত এসেই পুনরায় তা পেটে নেমে যায় এবং রোযাদার কেবল নিজ গলাতে তার স্বাদ অনুভব করে থাকে।[3]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (দ্রঃ আশ্শারহুল মুমতে’ ৬/৩৮৫, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৫৩নং)<br>"
                        + "[2] (আহমাদ, মুসনাদ ২/৪৯৮, আবূ দাঊদ ২৩৮০, তিরমিযী ৭১৬, ইবনে মাজাহ ১৬৭৬, দারেমী, সুনান ১৬৮০, ইবনে খুযাইমাহ, সহীহ ১৯৬০, ইবনে হিববান, সহীহ মাওয়ারিদ ৯০৭নং, হাকেম, মুস্তাদ্রাক ১/৪২৭, দারাকুত্বনী, সুনান, বাইহাকী ৪/২১৯ প্রমুখ, ইরওয়াউল গালীল, আলবানী ৯৩০, সহীহুল জামেইস সাগীর, আলবানী ৬২৪৩নং)<br>"
                        + "[3] (আশ্শারহুল মুমতে’ ৬/৪৩১)</font>",
                "Deliberately inducing vomiting by any means nullifies the fast and necessitates Qadha. If vomiting occurs involuntarily without one's control, the fast remains sound and valid.<br><br>"
                        + "<b>References:</b> Sunan Abu Dawud 2380, Jami' at-Tirmidhi 716, Ash-Sharh al-Mumti' 6/385."
        ));

        // Card 6: মহিলার মাসিক অথবা নিফাস শুরু হওয়া
        list.add(new HajjHistoryCardItem(
                6,
                "মহিলার মাসিক অথবা নিফাস শুরু হওয়া",
                "Onset of Menstruation or Postnatal Bleeding",
                "মহিলার মাসিক অথবা নিফাসের খুন বের হতে শুরু হলে তার রোযা নষ্ট হয়ে যায়। যদি সূর্য অস্ত যাওয়ার সামান্য ক্ষণ পূর্বে খুন দেখা দেয়, তাহলে তার ঐ দিনের রোযা বাতিল...",
                "If menstrual or post-natal bleeding commences even moments before sunset, the fast is invalidated and must be made up later...",
                "মহিলার মাসিক অথবা নিফাসের খুন বের হতে শুরু হলে তার রোযা নষ্ট হয়ে যায়। যদি সূর্য অস্ত যাওয়ার সামান্য ক্ষণ পূর্বে খুন দেখা দেয়, তাহলে তার ঐ দিনের রোযা বাতিল এবং সূর্য অস্ত যাওয়ার সামান্য ক্ষণ পরে দেখা দিলে তার ঐ দিনের রোযা শুদ্ধ।[1]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (সামানিয়া ওয়া আরবাঊন সুআলান ফিস্-সিয়াম ১৫পৃঃ)</font>",
                "The discharge of menstrual or postnatal blood invalidates the fast immediately, even if it happens moments before sunset, requiring Qadha after Ramadan.<br><br>"
                        + "<b>References:</b> Thamaniya wa Arba'oona Su'alan fis-Siyam p. 15."
        ));

        // Card 7: দূষিত রক্ত বের করা
        list.add(new HajjHistoryCardItem(
                7,
                "দূষিত রক্ত বের করা",
                "Extracting Impure Blood (Cupping/Hijama)",
                "দেহ থেকে দূষিত রক্ত বের করলে রোযা নষ্ট হবে কি না - সে নিয়ে মতভেদ রয়েছে এবং সঠিক মত এই যে, তাতে রোযা নষ্ট হবে না...",
                "Scholars differ on whether extracting bad blood (cupping) breaks the fast; the preferred opinion is that it does not, though precaution is advised...",
                "দেহ থেকে দূষিত রক্ত বের করলে রোযা নষ্ট হবে কি না - সে নিয়ে মতভেদের কথা পূর্বে আলোচিত হয়েছে এবং সঠিক মত এই যে, তাতে রোযা নষ্ট হবে না। অবশ্য পূর্বসতর্কতামূলক আমল এই যে, রোযাদার রোযা অবস্থায় দিনের বেলায় ঐ কাজ করবে না।[1]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (সাবঊনা মাসআলাহ ফিস্-সিয়াম ৫৬নং)</font>",
                "Extracting blood through cupping (Hijama) is a subject of scholarly difference; the soundest view is that it does not invalidate the fast, but avoiding it during daytime is better as a precaution.<br><br>"
                        + "<b>References:</b> Sab'oona Mas'alah fis-Siyam 56."
        ));

        // Card 8: নিয়ত বাতিল করা
        list.add(new HajjHistoryCardItem(
                8,
                "নিয়ত বাতিল করা",
                "Invalidating or Revoking Intention",
                "নিয়ত প্রত্যেক ইবাদত তথা রোযার অন্যতম রুকন। আর সারা দিন সে নিয়ত নিরবচ্ছিন্নভাবে মনে জাগ্রত রাখতে হবে...",
                "Intention is an essential pillar of fasting. Making a firm resolve to cancel or break the fast terminates the fast even without consuming food...",
                "নিয়ত প্রত্যেক ইবাদত তথা রোযার অন্যতম রুকন। আর সারা দিন সে নিয়ত নিরবচ্ছিন্নভাবে মনে জাগ্রত রাখতে হবে; যাতে রোযাদার রোযা না রাখার বা রোযা বাতিল করার কোন প্রকার দৃঢ় সংকল্প না করে বসে। বলা বাহুল্য, রোযা না রাখার নিয়ত করলে এবং তার নিয়ত বাতিল করে দিলে সারাদিন পানাহার আদি না করে উপবাস করলেও রোযা বাতিল গণ্য হবে।[1]<br><br>"
                        + "পক্ষান্তরে যে ব্যক্তি কিছু খাওয়া অথবা পান করার প্রাথমিক ইচ্ছা পোষণ করার পর ধৈর্য ধরে পানাহার করার ঐ ইচ্ছা বাতিল করে পানাহার করে না, সে ব্যক্তির কেবল রোযা ভাঙ্গার ইচ্ছা পোষণ করার ফলে রোযা নষ্ট হবে না; যতক্ষণ না সে সত্য সত্যই পানাহার করে নেবে। আর এর উদাহরণ সেই ব্যক্তির মত, যে নামাযে কথা বলার ইচ্ছা পোষণ করার পর কথা না বলে অথবা নামায পড়তে পড়তে হাওয়া ছাড়ার ইচ্ছা করার পর তা সামলে নিতে পারে। এমন ব্যক্তির যেমন নামায ও ওযূ বাতিল নয়, ঠিক তেমনি ঐ রোযাদারের রোযা।[2]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (দ্রঃ ফিকহুস সুন্নাহ ১/৪১২, আশ্শারহুল মুমতে’ ৬/৩৭৬, সাওমু রামাযান ২৪পৃঃ, ফাসিঃ জিরাইসী ৮পৃঃ)<br>"
                        + "[2] (ইবনে উষাইমীন, ক্যাসেট, আহকামুন মিনাস সিয়াম)</font>",
                "Forming a definite, decisive intention to break the fast nullifies the fast, even if one does not actually eat or drink. However, mere fleeting thoughts that are resisted do not invalidate it.<br><br>"
                        + "<b>References:</b> Fiqh as-Sunnah 1/412, Ash-Sharh al-Mumti' 6/376."
        ));

        // Card 9: মুরতাদ্দ্ হওয়া
        list.add(new HajjHistoryCardItem(
                9,
                "মুরতাদ্দ্ হওয়া",
                "Apostasy (Leaving Islam)",
                "কোন সন্দেহ, কথা বা কাজের ফলে যদি কোন রোযাদার মুরতাদ্দ্ (কাফের) হয়ে যায়, তাহলে সকলের মতে তার রোযা বাতিল হয়ে যাবে...",
                "Committing apostasy through disbelief, mocking sacred religion, or denying tenets of faith completely invalidates the fast by consensus...",
                "কোন সন্দেহ, কথা বা কাজের ফলে যদি কোন রোযাদার মুরতাদ্দ্ (কাফের) হয়ে যায় (নাঊযু বিল্লাহি মিন যালিক), তাহলে সকলের মতে তার রোযা বাতিল হয়ে যাবে। অতঃপর সে যদি তওবা করে পুনরায় মুসলিম হয়, তাহলে ঐ রোযা তাকে কাযা করতে হবে; যদিও সে ঐ দিনে রোযা নষ্টকারী কোন জিনিস ব্যবহার না করে। যেহেতু খোদ মুরতাদ্দ্ হওয়াটাই একটি রোযা নষ্টকারী কর্ম। তাতে সে কুফরী কোন বিশ্বাসের ফলে মুরতাদ্দ্ হোক অথবা কুফরী কোন সন্দেহ করার ফলে, আল্লাহ ও তদীয় রসূল কিংবা দ্বীনের কোন অংশ নিয়ে ব্যঙ্গ-বিদ্রূপ করে কুফরী কথা বলে মুরতাদ্দ্ হোক অথবা তা না করে যে কোন প্রকার কুফরী মন্তব্য করে। আর যদিও তার ও তার বাপের নামখানি মুসলিমের তবুও তার সকল ইবাদত প্রত্যাখ্যাত।[1]<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (দ্রঃ আহকামুস সাওমি অল-ই’তিকাফ, আবূ সারী মঃ আব্দুল হাদী ৮৩পৃঃ)</font>",
                "Apostatizing from Islam through word, act, or conviction invalidates the fast unanimously. Returning to Islam requires making up that day.<br><br>"
                        + "<b>References:</b> Ahkamus Sawmi wal-I'tikaf p. 83."
        ));

        // Card 10: বেহুশ হওয়া
        list.add(new HajjHistoryCardItem(
                10,
                "বেহুশ হওয়া",
                "Unconsciousness Throughout the Day",
                "রোযাদার যদি ফজর থেকে নিয়ে মাগরেব পর্যন্ত বেহুশ থাকে, তাহলে তার রোযা শুদ্ধ হবে না এবং তাকে ঐ দিনের রোযা কাযা রাখতে হবে...",
                "If a fasting person remains unconscious continuously from dawn until sunset, the fast is invalid and must be made up...",
                "রোযাদার যদি ফজর থেকে নিয়ে মাগরেব পর্যন্ত বেহুশ থাকে, তাহলে তার রোযা শুদ্ধ হবে না এবং তাকে ঐ দিনের রোযা কাযা রাখতে হবে। আর এ কথা পূর্বে আলোচিত হয়েছে।",
                "If a person remains unconscious or in a coma continuously from true dawn until sunset without regaining awareness for any part of the daytime, their fast is invalid and requires Qadha."
        ));

        // Card 11: রোযা নষ্ট হওয়ার শর্তাবলী
        list.add(new HajjHistoryCardItem(
                11,
                "রোযা নষ্ট হওয়ার শর্তাবলী",
                "Conditions for Invalidation of Fasting",
                "উপর্যুক্ত রোযা নষ্টকারী (মাসিক ও নিফাসের খুন ব্যতীত) সকল জিনিস কেবল তখনই রোযা নষ্ট করবে, যখন তার সাথে ৩টি শর্ত অবশ্যই পাওয়া যাবে...",
                "The nullifiers of fasting (except menstruation/postnatal bleeding) only invalidate the fast if three conditions are satisfied: knowledge, memory, and free will...",
                "উপর্যুক্ত রোযা নষ্টকারী (মাসিক ও নিফাসের খুন ব্যতীত) সকল জিনিস কেবল তখনই রোযা নষ্ট করবে, যখন তার সাথে ৩টি শর্ত অবশ্যই পাওয়া যাবে। আর সে শর্ত ৩টি নিম্নরূপঃ-<br><br>"
                        + "১। রোযাদার জানবে যে, এই জিনিস এই সময়ে ব্যবহার করলে তার রোযা নষ্ট হয়ে যাবে। অর্থাৎ, তা ব্যবহার করার সময় তার এ কথা অজানা থাকলে চলবে না যে, এই জিনিস রোযা নষ্ট করে অথবা এখন রোযার সময়।<br>"
                        + "২। তা যেন মনে স্মরণ রাখার সাথে ব্যবহার করে; ভুলে গিয়ে নয়।<br>"
                        + "৩। তা যেন নিজস্ব ইচ্ছা ও এখতিয়ারে ব্যবহার করে; অপরের তরফ থেকে বাধ্য হয়ে নয়।<br><br>"
                        + "কেননা, মহান আল্লাহ বলেন,<br><br>"
                        + "<font color='#00695C'><b>(وَلَيْسَ عَلَيْكُمْ جُنَاحٌ فِيْمَا أَخْطَأْتُمْ بِهِ، وَلَكِنْ مَّا تَعَمَّدَتْ قُلُوْبُكُمْ)</b></font><br><br>"
                        + "অর্থাৎ, কোন ব্যাপারে তোমরা ভুল করলে তোমাদের কোন অপরাধ নেই; কিন্তু ইচ্ছাকৃত করলে অপরাধ আছে। (কুরআনুল কারীম ৩৩/৫)<br><br>"
                        + "তিনি আরো বলেন,<br><br>"
                        + "<font color='#00695C'><b>(رَبَّنَا لاَ تُؤَاخِذْنَا إِنْ نَّسِيْنَا أَوْ أَخْطَأْنَا)</b></font><br><br>"
                        + "অর্থাৎ, হে আমাদের প্রতিপালক! আমরা যদি ভুল ও ত্রুটি করে ফেলি, তাহলে তুমি আমাদেরকে অপরাধী করো না। (কুরআনুল কারীম ২/২৮৬)<br><br>"
                        + "তিনি অন্যত্র বলেন,<br><br>"
                        + "<font color='#00695C'><b>(إِلاَّ مَنْ أُكْرِهَ وَقَلْبُهُ مُطْمَئِنٌّ بِالإيْمَانِ)</b></font><br><br>"
                        + "অর্থাৎ, (কেউ ঈমান আনার পর আল্লাহকে অস্বীকার করলে এবং কুফরীর জন্য হৃদয় মুক্ত রাখলে তার উপর আল্লাহর ক্রোধ পতিত হবে। আর তার জন্য আছে মহাশাস্তি। তবে তার জন্য নয়, যাকে (কুফরী করতে) বাধ্য করা হয়; কিন্তু তার চিত্ত ঈমানে অটল থাকে। (কুরআনুল কারীম ১৬/১০৬)<br><br>"
                        + "মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘অবশ্যই আল্লাহ আমার জন্য আমার উম্মতের ভুল-ত্রুটি এবং বাধ্য হয়ে কৃত পাপকে মার্জনা করে দিয়েছেন।’’[1]<br><br>"
                        + "<b>আর এই ভিত্তিতে একাধিক মাসায়েল প্রমাণিত হয়ঃ</b><br><br>"
                        + "• যদি কোন (নও-মুসলিম) স্বামী-স্ত্রী রোযা রেখে সঙ্গম করলে রোযা নষ্ট হয় -এ কথা না জেনে সঙ্গম করে ফেলে, অথবা ফজর উদয় হয়ে যাওয়ার সময় না জানতে পেরে (সময় বাকী আছে মনে করে) ভুল করে সঙ্গম করে ফেলে, তাহলে তাদের উপর কাযা-কাফ্ফারা কিছুই ওয়াজেব নয়।<br><br>"
                        + "• স্বামী যদি মিলনের জন্য স্ত্রীকে জোর করে এবং স্ত্রী বাধা দেওয়ার চেষ্টা করা সত্ত্বেও বাধা দিতে পারঙ্গম না হয়ে সঙ্গম হয়েই যায়, তাহলে স্ত্রীর রোযা শুদ্ধ। কারণ, ঐ মিলনে তার ইচ্ছা ছিল না।<br><br>"
                        + "• রোযা রেখে ঘুমিয়ে থাকা অবস্থায় স্বপ্নে কেউ সঙ্গম করলে এবং তার ফলে সত্যসত্যই (স্বপ্নদোষ ও) বীর্যপাত হয়ে গেলে তার রোযা নষ্ট হবে না। কেননা, ঘুমন্ত ব্যক্তির স্বপ্নে কোন ইচ্ছা থাকে না। বরং তার উপর থেকে ফিরিশ্তার নেকী-বদী লেখার কলমও তুলে নেওয়া হয়।<br><br>"
                        + "• রোযাদার ভুলে কিছু খেয়ে অথবা পান করে নিলে রোযা নষ্ট হবে না। কারণ, রোযার কথা সে ভুলে গিয়েছিল। আর মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে রোযাদার ভুলে গিয়ে পানাহার করে ফেলে, সে যেন তার রোযা পূর্ণ করে নেয়। এ পানাহার তাকে আল্লাহই করিয়েছেন।’’[2] অন্য এক বর্ণনায় আছে, ‘‘যে ব্যক্তি রমাযান মাসে ভুলবশতঃ রোযা নষ্টকারী কোন কাজ করে ফেলে, তার উপর কাযা ও কাফ্ফারা কিছুই নেই।’’[3]<br><br>"
                        + "• যদি কেউ এই মনে করে খায় অথবা পান করে যে, সূর্য ডুবে গেছে অথবা এখনো ফজর উদয় হয় নি, তাহলে তার রোযা নষ্ট হবে না। যেহেতু সে না জেনে খেয়েছে।<br><br>"
                        + "• যদি ওযূ বা গোসল করতে গিয়ে অথবা সাঁতার কাটতে গিয়ে পেটে পানি চলে যায়, কিংবা নলে পানি, পেট্রোল অথবা অন্য কোন তরল পদার্থ মুখে করে টানতে গিয়ে গলার নিচে নেমে যায়, তাহলে তাতেও রোযা নষ্ট হবে না। কারণ, এ সবে রোযাদারের ইচ্ছা থাকে না।<br><br>"
                        + "• ঘুমিয়ে থাকা অবস্থায় যদি কারো মুখে খাবার (যেমন পান ইত্যাদি) থেকে যায়, অতঃপর সেই অবস্থায় ফজর হয়ে যায়, তাহলে জাগার সাথে সাথে তা উগলে ফেলে দিয়ে কুল্লি করে নিলে তার রোযা হয়ে যাবে। কেননা, সে ঘুমিয়ে ছিল এবং ফজর হওয়ার পর সে ইচ্ছাকৃত সে খাবার গিলে খায়নি।<br><br>"
                        + "• কেউ জ্ঞানশূন্য হয়ে গেলে তার মুখে পানি দিয়ে যদি তার জ্ঞান ফিরে যায়, তাহলে তার রোযা শুদ্ধ। কারণ, ঐ পানি সে নিজের ইচ্ছায় খায় নি।[4]<br><br>"
                        + "এখানে একটি সতর্কতার বিষয় এই যে, যদি কেউ কোন রোযাদারকে না জেনে বা ভুলে খেতে অথবা পান করতে প্রত্যক্ষ করে, তাহলে তাকে রোযার কথা স্মরণ করিয়ে দেওয়া জরুরী। যেহেতু মহান আল্লাহর ব্যাপক নির্দেশ হল,<br><br>"
                        + "<font color='#00695C'><b>(وَتَعَاوَنُوْا عَلَى الْبِرِّ وَالتَّقْوَى)</b></font><br><br>"
                        + "অর্থাৎ, তোমরা সৎকার্য ও আল্লাহভীতির ব্যাপারে একে অপরকে সাহায্য কর। (কুরআনুল কারীম ৫/২)<br><br>"
                        + "আর মহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর ব্যাপক নির্দেশ হল, ‘‘আমি ভুলে গেলে তোমরা আমাকে স্মরণ করিয়ে দিও।’’[5]<br><br>"
                        + "তাছাড়া আসলে এটি একটি আপত্তিকর কর্ম। অতএব তা প্রতিহত করা ওয়াজেব।[6]<br><br>"
                        + "বমি সামলাতে না পারলে রোযার কোন ক্ষতি হয় না। কারণ, তা রোযাদারের এখতিয়ারের বাইরে। আর তার দলীল হল উল্লেখিত স্পষ্ট হাদীস। এ ক্ষেত্রে মুখ ভর্তি হওয়া বা না হওয়ার কোন শর্ত কার্যকর নয়。<br><br>"
                        + "<font color='#757575'><b>তথ্যসূত্র ও রেফারেন্স:</b><br>"
                        + "[1] (আহমাদ, মুসনাদ, ইবনে মাজাহ, ত্বাবারানী, মু’জাম, হাকেম, মুস্তাদ্রাক, সহীহুল জামেইস সাগীর, আলবানী ১৭৩১নং)<br>"
                        + "[2] (বুখারী ১৯৩৩, মুসলিম ১১৫৫, আবূ দাঊদ ২৩৯৮, তিরমিযী, দারেমী, ইবনে মাজাহ ১৬৭৩, দারাকুত্বনী, সুনান, বাইহাকী ৪/২২৯, আহমাদ, মুসনাদ ২/৩৯৫, ৪২৫, ৪৯১, ৫১৩)<br>"
                        + "[3] (ইবনে হিববান, সহীহ মাওয়ারিদ ৯০৬নং, হাকেম, মুস্তাদ্রাক ১/৪৩০, ইরওয়াউল গালীল, আলবানী ৪/৮৭)<br>"
                        + "[4] (আশ্শারহুল মুমতে’ ৬/৪০১)<br>"
                        + "[5] (বুখারী ৪০১, মুসলিম ৫৭২, আবূ দাঊদ, নাসাঈ, ইবনে মাজাহ)<br>"
                        + "[6] (ইবনে উষাইমীন ফাসিঃ মুসনিদ ৪৬পৃঃ, সাবঊনা মাসআলাহ ফিস্-সিয়াম ৪৪নং)</font>",
                "Invalidators of the fast only take effect when three conditions are met simultaneously: knowledge (not ignorance), memory (not forgetfulness), and free will (not coercion). Eating or drinking forgetfully does not break the fast.<br><br>"
                        + "<b>References:</b> Quran 33:5, 2:286, 16:106, Sahih Bukhari 1933, Sahih Muslim 1155."
        ));

        return list;
    }
}
