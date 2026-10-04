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
 * Repository for রমাযানের কিছু বিদআত (Innovations in Ramadan).
 * Pre-seeded with complete verbatim text matching user prompt and screenshot.
 * Instant 0ms memory cache with asynchronous background REST API sync (Rule 11).
 */
public final class RozaBidahRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaBidahRepository() {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_bidah.php");
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
            // Resilient cache
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();
        list.add(new HajjHistoryCardItem(1, "রমাযানের কিছু বিদআতের নমুনা", "Innovations and Unfounded Practices in Ramadan", "কোন কোন অঞ্চলে বা সমাজে রমাযান মাসে এক এক প্রকার বিদআত প্রচলিত হয়ে পড়েছে। সে সকল বিদআত থেকে সাবধান করার জন্য এখানে কিছু বিদআত উল্লেখ করা সঙ্গত বলে মনে করছি...", "In certain regions and communities, various innovations (Bid'ah) have become widespread during Ramadan. To caution believers and preserve the purity of the Sunnah, prominent examples are presented here...", "কোন কোন অঞ্চলে বা সমাজে রমাযান মাসে এক এক প্রকার বিদআত প্রচলিত হয়ে পড়েছে। সে সকল বিদআত থেকে সাবধান করার জন্য এখানে কিছু বিদআত উল্লেখ করা সঙ্গত বলে মনে করছি।\n\n\n\n১। রোযার নিয়ত মুখে উচ্চারণ করা।\n\n\n\n২। ‘‘নাওয়াইতু আন আসূমা গাদাম মিন শাহরি রামাযান’’ বলে বাঁধা নিয়ত বলা।\n\n\n\n৩। রমাযানের রাত্রে কুরআন পড়ার জন্য ভাড়াটিয়া কারী ভাড়া করা।[1]\n\n\n\n৪। মাইকে এক রাত্রে কুরআন খতম (শবীনা পাঠ) করা।\n\n\n\n৫। মীলাদ বা মওলূদ পাঠ করা এবং তার শেষে নবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম)-এর শানে দরূদ পাঠ করার জন্য উঠে দাঁড়িয়ে মনগড়া দরূদ পড়া। সেই সাথে মুনাজাতে আমলের সওয়াব আম্বিয়া ও আওলিয়া বা কোন আত্মীয়র রূহের জন্য বখশে দেওয়া।[2]\n\n\n\n৬। পূর্বসতর্কতামূলক কর্ম ভেবে ফজর হওয়ার ৫/১০ মিনিট আগে খাওয়া বন্ধ করা এবং সূর্য অস্ত যাওয়ার ৩/৫ মিনিট পরে ইফতার করা।[3]\n\n\n\n৭। সেহরী ও ইফতারের সময় জানানোর উদ্দেশ্যে তোপ দাগা।[4]\n\n\n\n৮। সেহরী খেতে জাগানোর উদ্দেশ্যে আযানের পরিবর্তে কুরআন ও গজল পাঠ করা।\n\n\n\n৯। মসজিদের মিনারে সেহরী ও ইফতারের জন্য নির্দিষ্ট লাইট ব্যবহার করা। যেমন, সেহরীর সময় শেষ হলে লাল বাতি এবং ইফতারীর সময় শুরু হলে সবুজ বাতি জ্বালিয়ে দেওয়া।[5]\n\n\n\n১০। সেহরী না খেয়ে অধিক সওয়াবের আশা করা।[6]\n\n\n\n১১। কুরআন খতম হওয়ার পর বাকী রাতে তারাবীহ না পড়া।[7]\n\n\n\n১২। প্রথমে পানি না খেয়ে আদা ও লবণ দিয়ে ইফতারী করাকে ভালো মনে করা।\n\n\n\n১৩। ইফতারের আগে হাত তুলে জামাআতী মুনাজাত করা।\n\n\n\n১৪। ইফতারের সময় ‘‘আল্লাহুম্মা ইন্নী আসআলুকা বিরাহমাতিকাল্লাতী অসিআত কুল্লা শাইইন আন তাগফিরা লী’’ বলে দুআ করা।[8]\n\n\n\n১৫। ইফতারের সময় ‘‘আল্লাহুম্মা লাকা সুমতু, অবিকা আ-মানতু, অআলাইকা তাওয়াক্কালতু, অআলা রিযক্বিকা আফতারতু, বিরাহমাতিকা ইয়া আরহামুর রা-হিমীন’’ বলে দুআ করা।\n\n\n\n১৬। বিশেষ করে রজব, শাবান ও রমাযানে মৃতদের কল্যাণের উদ্দেশ্যে দান-খয়রাত করা।[9]\n\n\n\n১৭। সারা বছর নামায না পড়ে এবং তার সংকল্প না নিয়ে কেবল রমাযান মাসে রোযা রেখে (ফরয, সুন্নত ও নফল) নামায পড়া ও তসবীহ আওড়ানো।[10]\n\n\n\n১৮। শবেকদরের ১০০ বা ১০০০ রাকআত নামায পড়া।\n\n\n\n১৯। শবেকদরে বিশেষ করে ‘সালাতুত তাসবীহ’ নামায পড়া।\n\n\n\n২০। কেবল ২৭শের রাতকে শবেকদর মনে করা এবং কেবল সেই রাত জাগরণ করা ও বাকী রাত না জাগা।\n\n\n\n২১। বিশেষ করে শবেকদরের রাতে উমরাহ করা।[11]\n\n\n\n২২। বিশেষ করে ২৭শের রাত্রি জাগরণ করে জামাআতী যিক্র করা, নানা রকমের পানাহার সামগ্রী তৈরী বা ক্রয় করে পান-ভোজন করা, মিষ্টি বিতরণ করা ও ওয়ায-মাহফিল করা।[12]\n\n\n\n২৩। নির্দিষ্ট কোন রাতে একাকী বা জামাআতী নির্দিষ্ট যিক্র পড়া।[13]\n\n\n\n২৪। সাতাশের রাত্রে লোকেদের মিষ্টি কিনতে ভিঁড় করা, (তা খাওয়া ও দান করা)।[14]\n\n\n\n২৫। ঈদের রাত্রি জাগরণ করে ইবাদত করা। পূর্বে উল্লেখিত হয়েছে যে, এ ব্যাপারে বর্ণিত হাদীসটি জাল।[15]\n\n\n\n২৬। রমাযানের শেষ জুমআহ (বিদায়ী জুমআহ) বিশেষ উদ্দীপনার সাথে পালন করা।\n\n\n\n২৭। মা-বাপের নামে বিশেষ ভোজ-অনুষ্ঠান করা।[16]\n\n\n\n২৮। শাবানের ১৫ তারীখের রাতে নামায ও দিনে রোযা রাখা।[17] বলা বাহুল্য এ ব্যাপারে কোন সহীহ হাদীস নেই।\n\n\n\nসবশেষে এ কথা সকল মুসলিমের জেনে রাখা উচিত যে, নিশ্চয় উত্তম বাণী আল্লাহর গ্রন্থ এবং উত্তম পথ-নির্দেশ মুহাম্মাদ (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) -এর পথ-নির্দেশ। সব চেয়ে মন্দ কর্ম দ্বীনের অভিনব রচিত কর্মসমূহ। এবং প্রত্যেক নব কর্মই বিদআত, আর প্রত্যেক বিদআত্ই ভ্রষ্টতা।’’ ‘‘এবং প্রত্যেক ভ্রষ্টতার স্থান দোযখে।’’[18]\n\n\n\nমহানবী (সাল্লাল্লাহু আলাইহে ওয়াসাল্লাম) বলেন, ‘‘যে ব্যক্তি কোন এমন কাজ করে যার উপর আমাদের কোন নির্দেশ নেই তা প্রত্যাখ্যাত।’’[19] তিনি আরো বলেন, ‘‘যে ব্যক্তি আমাদের এ (দ্বীন) বিষয়ে কিছু এমন কর্ম উদ্ভাবন করবে যা ওর পর্যায়ভুক্ত নয়, তা প্রত্যাখ্যাত।’’[20]\n\n\n\n[1] (মু’জামুল বিদা’ ২৬৮পৃঃ, দুরুসু রামাযান অকাফাত লিস্-সায়েমীন ৪০পৃঃ)\n\n\n\n[2] (আহকামুল জানায়েয, আলবানী ২৬০-২৬১পৃঃ)\n\n\n\n[3] (ফাতহুল বারী ৪/১৯৯, তামামুল মিন্নাহ, আল্লামা আলবানী ৪১৫পৃঃ, মু’জামুল বিদা’ ২৬৮, ৩৬১পৃঃ)\n\n\n\n[4] (মু’জামুল বিদা’ ২৬৮পৃঃ)\n\n\n\n[5] (ফাতহুল বারী ৪/১৯৯, মু’জামুল বিদা’ ৩৬১পৃঃ)\n\n\n\n[6] (মু’জামুল বিদা’ ৩৬১পৃঃ)\n\n\n\n[7] (ঐ ২৬৮পৃঃ)\n\n\n\n[8] (এ ব্যাপারে আসারটি যয়ীফ। দ্রঃ যয়ীফ ইবনে মাজাহ, আলবানী ৩৮৭, ইরওয়াউল গালীল, আলবানী ৯২১নং, আর ইফতারীর বিবরণে আলোচিত হয়েছে যে, ‘‘যাহাবায যামাউ---’’ ছাড়া ইফতারীর জন্য অন্য কোন দুআ বিশুদ্ধভাবে প্রমাণিত নয়।)\n\n\n\n[9] (আহকামুল জানায়েয, আলবানী ২৫৭নং বিদআত, মু’জামুল বিদা’ ২৬৯পৃঃ)\n\n\n\n[10] (মু’জামুল বিদা’ ২৭০পৃঃ)\n\n\n\n[11] (আশ্শারহুল মুমতে’ ৬/৪৯৬, ৪৯৭)\n\n\n\n[12] (মাজাল্লাতুদ দা’ওয়া, ইবনে বায ১৬৭৪/১৪ রমাযান ১৪১৯হিঃ)\n\n\n\n[13] (ঐ)\n\n\n\n[14] (মু’জামুল বিদা’ ২৬৯পৃঃ)\n\n\n\n[15] (দ্রঃ সিলসিলাহ যায়ীফাহ, আলবানী ৫২০, ৫২১, ৫২২নং, মু’জামুল বিদা’ ৩৩২পৃঃ, দুরুসু রামাযান অকাফাত লিস্-সায়েমীন ১০০পৃঃ)\n\n\n\n[16] (ফাসিঃ মুসনিদ ১০৪পৃঃ, তাযকীরু ইবাদির রাহমান, ফীমা অরাদা বিসিয়ামি শাহরি রামাযান ৫০পৃঃ)\n\n\n\n[17] (মু’জামুল বিদা’ ৩৬২পৃঃ)\n\n\n\n[18] (মুসলিম, নাসাঈ)\n\n\n\n[19] (মুসলিম)\n\n\n\n[20] (বুখারী, মুসলিম)", "In various regions and societies, certain innovations (Bid'ah) have become widespread during the month of Ramadan. To warn against these innovations and preserve authentic practice, it is appropriate to mention some of them here:\n\n1. Verbally pronouncing the intention for fasting.\n\n2. Reciting invented verbal formulas such as \"Nawaytu an asuma ghadan min shahri Ramadan\".\n\n3. Hiring commercial reciters to recite the Quran during Ramadan nights. [1]\n\n4. Reciting the entire Quran in a single night over loudspeakers (Shabinah).\n\n5. Reciting Mawlid/Milad ceremonies and standing up at the end to recite invented poetic salutations upon the Prophet, along with dedicating the reward of deeds to prophets, saints, or deceased relatives in supplication. [2]\n\n6. Stopping eating 5 to 10 minutes before true dawn out of unwarranted caution (Imsak), and delaying Iftar 3 to 5 minutes after the sun sets. [3]\n\n7. Firing cannons to announce the arrival of Sehri and Iftar times. [4]\n\n8. Reciting Quran or singing poetic ghazals over microphones instead of calling the authentic Adhan to wake people for Sehri.\n\n9. Using colored signal lights on mosque minarets to indicate Sehri and Iftar (e.g., turning on red light when Sehri ends and green light when Iftar begins). [5]\n\n10. Deliberately skipping Sehri in hopes of attaining greater reward. [6]\n\n11. Abandoning Taraweeh prayers for the remainder of Ramadan once the recitation of the Quran is completed. [7]\n\n12. Believing it is preferable to break fast with ginger and salt rather than drinking water first.\n\n13. Raising hands to conduct collective congregational supplication before Iftar.\n\n14. Reciting the supplication: \"Allahumma inni as'aluka bi-rahmatika al-lati wasi'at kulla shay'in an taghfira li\" at Iftar. [8]\n\n15. Reciting the invented composite dua at Iftar: \"Allahumma laka sumtu, wa bika amantu, wa 'alayka tawakkaltu, wa 'ala rizqika aftartu, bi-rahmatika ya Arhamar-Rahimin\".\n\n16. Specifying the months of Rajab, Sha'ban, and Ramadan for charitable donations intended specifically for the deceased. [9]\n\n17. Neglecting prayers throughout the entire year and praying (obligatory, Sunnah, and voluntary) or reciting Tasbih only during Ramadan while fasting, without making a permanent commitment to regular prayer. [10]\n\n18. Performing 100 or 1,000 rak'ahs of prayer on Laylatul Qadr.\n\n19. Performing Salat at-Tasbih specifically on the night of Laylatul Qadr.\n\n20. Confining the search for Laylatul Qadr exclusively to the 27th night, staying awake only on that night while neglecting all other odd nights.\n\n21. Performing Umrah specifically and exclusively on the 27th night of Ramadan. [11]\n\n22. Gathering specifically on the 27th night for collective loud Dhikr, preparing or purchasing lavish feasts, distributing sweets, and hosting cultural gatherings. [12]\n\n23. Reciting specific invented Dhikr formulas individually or collectively on designated nights. [13]\n\n24. Crowding sweet shops to purchase sweets specifically on the 27th night for eating and distribution. [14]\n\n25. Staying awake all night in worship on the night preceding Eid based on fabricated narrations. [15]\n\n26. Celebrating the last Friday of Ramadan (Jumatul Wida) with invented religious significance.\n\n27. Hosting special feast banquets dedicated in the name of parents. [16]\n\n28. Observing night vigil prayers specifically on the 15th night of Sha'ban and fasting on its day; needless to say, there is no sound hadith establishing this practice. [17]\n\nFinally, every Muslim should understand that the finest speech is the Book of Allah, and the best guidance is the guidance of Muhammad (peace and blessings of Allah be upon him). The worst of matters are newly invented religious affairs; every newly invented matter is an innovation (Bid'ah), every innovation is misguidance, and every misguidance leads to the Fire. [18]\n\nThe Prophet (peace and blessings of Allah be upon him) said: \"Whoever performs an act that is not in accordance with our matter (our religion), it is rejected.\" [19] He also said: \"Whoever introduces into this affair of ours that which is not part of it, it is rejected.\" [20]\n\n[1] (Mu'jam al-Bida' p. 268; Durus Ramadan Waqafat lis-Sa'imin p. 40)\n[2] (Ahkam al-Jana'iz, Al-Albani pp. 260-261)\n[3] (Fath al-Bari 4/199; Tamam al-Minnah, Al-Albani p. 415; Mu'jam al-Bida' pp. 268, 361)\n[4] (Mu'jam al-Bida' p. 268)\n[5] (Fath al-Bari 4/199; Mu'jam al-Bida' p. 361)\n[6] (Mu'jam al-Bida' p. 361)\n[7] (Ibid p. 268)\n[8] (The narration regarding this is weak. See Da'eef Ibn Majah 387; Irwa al-Ghalil 921; and nothing is authentically established for Iftar except 'Dhahabadh-dhama'u...')\n[9] (Ahkam al-Jana'iz, Al-Albani Bid'ah no. 257; Mu'jam al-Bida' p. 269)\n[10] (Mu'jam al-Bida' p. 270)\n[11] (Ash-Sharh al-Mumti' 6/496, 497)\n[12] (Majallat ad-Da'wah, Ibn Baz 1674/14 Ramadan 1419 AH)\n[13] (Ibid)\n[14] (Mu'jam al-Bida' p. 269)\n[15] (Silsilah Da'ifah, Al-Albani nos. 520, 521, 522; Mu'jam al-Bida' p. 332; Durus Ramadan p. 100)\n[16] (Fasi, Musnid p. 104; Tadhkiru 'Ibadir-Rahman p. 50)\n[17] (Mu'jam al-Bida' p. 362)\n[18] (Sahih Muslim, An-Nasa'i)\n[19] (Sahih Muslim)\n[20] (Sahih Bukhari, Sahih Muslim)"));
        return list;
    }
}
