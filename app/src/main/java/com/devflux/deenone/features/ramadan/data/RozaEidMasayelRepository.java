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
 * 3-Tier Production Repository for: ঈদ সম্পর্কিত মাসআলা-মাসায়েল (Fiqh Rulings of Eid).
 * Pre-seeded with all 10 verbatim cards matching the user screenshot and prompt:
 * 1. যাদের উপর জুমার নামায ফরয, তাদের উপর ঈদের নামায ওয়াজিব কি?
 * 2. মহিলাদের উপর ঈদের নামায ওয়াজিব কি ?
 * 3. মুসাফির তথা যে ৪৮ মাইল বা ৭৮ কি. মি. দূরত্বে যাওয়া
 * 4. ঈদের নামাযে তায়াম্মুম
 * 5. ঈদের নামাযে আযান-ইকামত
 * 6. নিয়ত মুখে উচ্চারণ করা
 * 7. ঈদের নামাযে সাহু সিজদা
 * 8. ঈদের নামাযের পর তাকবীরে তাশরীক
 * 9. ঈদের নামায ছুটে গেলে
 * 10. ঈদ ও জুমা একই দিনে হলে
 * 
 * 0ms instant memory cache with background async REST API sync (Rule 11).
 */
public final class RozaEidMasayelRepository {

    private static final List<HajjHistoryCardItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        cachedList.addAll(getDefaultCards());
    }

    private RozaEidMasayelRepository() {}

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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_eid_masayel.php");
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

    private static List<HajjHistoryCardItem> getDefaultCards() {
        List<HajjHistoryCardItem> list = new ArrayList<>();

        // Card 1: যাদের উপর জুমার নামায ফরয, তাদের উপর ঈদের নামায ওয়াজিব কি?
        list.add(new HajjHistoryCardItem(
                1,
                "যাদের উপর জুমার নামায ফরয, তাদের উপর ঈদের নামায ওয়াজিব কি?",
                "Is Eid Prayer Obligatory on Those Obligated to Pray Jumu'ah?",
                "প্রাপ্ত বয়স্ক, সুস্থ মস্তিষ্কসম্পন্ন, যেসকল মুসলিম পুরুষ, জামাতে উপস্থিত হয়ে ঈদের নামায আদায়ের সক্ষমতা রাখে তাদেরকে ঈদের নামায পড়তে হবে। [আলমুহীতুল বুরহানী ২/৪৭৬; বাদায়েউস সানায়ে ১/৬১৭; শরহুল মুনইয়া, পৃ. ৫৬৫]",
                "Adult, sane Muslim men capable of attending the congregation are obligated to observe Eid prayer... [Al-Muheet al-Burhani 2/476; Bada'i' as-Sana'i' 1/617; Sharh al-Munyah, p. 565]",
                "প্রাপ্ত বয়স্ক, সুস্থ মস্তিষ্কসম্পন্ন, যেসকল মুসলিম পুরুষ, জামাতে উপস্থিত হয়ে ঈদের নামায আদায়ের সক্ষমতা রাখে তাদেরকে ঈদের নামায পড়তে হবে।\n\n[আলমুহীতুল বুরহানী ২/৪৭৬; বাদায়েউস সানায়ে ১/৬১৭; শরহুল মুনইয়া, পৃ. ৫৬৫]",
                "Every adult, sane Muslim male who possesses the physical ability to attend the congregation is religiously obligated to perform the Eid prayer.\n\n[Al-Muheet al-Burhani 2/476; Bada'i' as-Sana'i' 1/617; Sharh al-Munyah, p. 565]"
        ));

        // Card 2: মহিলাদের উপর ঈদের নামায ওয়াজিব কি ?
        list.add(new HajjHistoryCardItem(
                2,
                "মহিলাদের উপর ঈদের নামায ওয়াজিব কি ?",
                "Is Eid Prayer Obligatory upon Women?",
                "মহিলাদের উপর ঈদের নামায ওয়াজিব নয়। অনুরূপ এমন অসুস্থ পুরুষ, যে ঈদগাহে উপস্থিত হয়ে ঈদের নামায আদায়ের সক্ষমতা রাখে না, তার উপরও ঈদের নামায ওয়াজিব নয়...",
                "Eid prayer is not obligatory upon women, nor is it obligatory upon an ailing man incapable of attending the Eidgah...",
                "মহিলাদের উপর ঈদের নামায ওয়াজিব নয়। অনুরূপ এমন অসুস্থ পুরুষ, যে ঈদগাহে উপস্থিত হয়ে ঈদের নামায আদায়ের সক্ষমতা রাখে না, তার উপরও ঈদের নামায ওয়াজিব নয়।\n\n[কিতাবুল আছল ১/৩২৩; মাবসূত, সারাখসী ২/৪০; আলমুহীতুল বুরহানী ২/৪৮৫; বাদায়েউস সানায়ে ১/৬১৭]",
                "Eid prayer is not obligatory upon women. Similarly, it is not obligatory upon an ill man who lacks the physical ability to attend the Eidgah and perform the prayer.\n\n[Kitab al-Asl 1/323; Al-Mabsut by As-Sarakhsi 2/40; Al-Muheet al-Burhani 2/485; Bada'i' as-Sana'i' 1/617]"
        ));

        // Card 3: মুসাফির তথা যে ৪৮ মাইল বা ৭৮ কি. মি. দূরত্বে যাওয়া
        list.add(new HajjHistoryCardItem(
                3,
                "মুসাফির তথা যে ৪৮ মাইল বা ৭৮ কি. মি. দূরত্বে যাওয়া",
                "The Traveler Journeying 48 Miles or 78 Kilometers",
                "মুসাফির তথা যে ৪৮ মাইল বা ৭৮ কি. মি. দূরত্বে যাওয়ার উদ্দেশ্যে নিজ এলাকা ত্যাগ করেছে- এমন ব্যক্তির উপর ঈদের নামায ওয়াজিব নয়...",
                "A traveler who has departed his locality with the intention of traveling 48 miles (78 km) is not obligated to pray Eid...",
                "মুসাফির তথা যে ৪৮ মাইল বা ৭৮ কি. মি. দূরত্বে যাওয়ার উদ্দেশ্যে নিজ এলাকা ত্যাগ করেছে- এমন ব্যক্তির উপর ঈদের নামায ওয়াজিব নয়। তবে সে যদি ঈদের নামায পড়ে তাহলে তা সহীহ হবে এবং এর সওয়াবও পাবে।\n\n[আততাজরীদ, কুদুরী ২/৯৮১; বাদায়েউস সানায়ে ১/৬১৭; আযযাখীরাতুল বুরহানিয়া ২/৩৯৬]",
                "A traveler (Musafir) who has left his home locality with the intent of traveling a distance of 48 miles (approx. 78 km) or more is not obligated to perform the Eid prayer. However, if he attends and performs the Eid prayer, it is valid and fully rewarded.\n\n[At-Tajreed by Al-Quduri 2/981; Bada'i' as-Sana'i' 1/617; Adh-Dhakheerah al-Burhaniyyah 2/396]"
        ));

        // Card 4: ঈদের নামাযে তায়াম্মুম
        list.add(new HajjHistoryCardItem(
                4,
                "ঈদের নামাযে তায়াম্মুম",
                "Tayammum for Eid Prayer",
                "নামাযে শরীক হওয়ার আগ মুহূর্তে কারো ওযু না থাকলে এবং ওযু করতে গেলে জামাত ছুটে যাওয়ার আশঙ্কা হলে তায়াম্মুম করে ঈদের নামায আদায় করা যাবে...",
                "If one lacks Wudu right before Eid prayer and fears missing the congregation by making Wudu, Tayammum is permissible...",
                "নামাযে শরীক হওয়ার আগ মুহূর্তে কারো ওযু না থাকলে এবং ওযু করতে গেলে জামাত ছুটে যাওয়ার আশঙ্কা হলে তায়াম্মুম করে ঈদের নামায আদায় করা যাবে। হযরত আব্দুর রহমান ইবনুল কাসিম রাহ. থেকে বর্ণিত, তিনি বলেন -\n\n"
                        + "يَتَيَمّمُ وَيُصَلِّي إِذَا خَافَ.\n\n"
                        + "অর্থ:\n\n"
                        + "(ঈদের নামায) ছুটে যাওয়ার আশঙ্কা হলে তায়াম্মুম করে নামায পড়ে নেবে।\n\n"
                        + "[মুসান্নাফে ইবনে আবি শাইবা, বর্ণনা ৫৮৬৯]\n\n"
                        + "হযরত ইবরাহীম নাখায়ী রাহ. বলেন-\n\n"
                        + "يَتَيَمّمُ لِلْعِيدَيْنِ وَالْجِنَازَةِ.\n\n"
                        + "অর্থ:\n\n"
                        + "ঈদ ও জানাযার ক্ষেত্রে (ছুটে যাওয়ার আশঙ্কায়) তায়াম্মুম করা যাবে।\n\n"
                        + "[মুসান্নাফে ইবনে আবি শাইবা, বর্ণনা ৫৮৬৮) -কিতাবুল আছল ১/৩২০; আলমুহীতুল বুরহানী ২/৫০২]\n\n"
                        + "তবে জুমা ও ওয়াক্তিয়া নামাযে জামাত ছুটে যাওয়ার আশঙ্কায় তায়াম্মুমের উক্ত বিধান প্রযোজ্য নয়।",
                "If someone lacks ablution (Wudu) right before joining the prayer and genuinely fears missing the entire Eid congregation if he goes to perform Wudu, he is permitted to perform Tayammum and join the Eid prayer.\n\n"
                        + "Abdur Rahman ibn al-Qasim (may Allah have mercy on him) stated:\n\n"
                        + "'He performs Tayammum and prays if he fears missing it.' (Musannaf Ibn Abi Shaybah #5869)\n\n"
                        + "Ibrahim an-Nakha'i (may Allah have mercy on him) said:\n\n"
                        + "'Tayammum is permissible for the two Eids and Janazah.' (Musannaf Ibn Abi Shaybah #5868; Kitab al-Asl 1/320; Al-Muheet al-Burhani 2/502)\n\n"
                        + "However, this concession of Tayammum due to fear of missing the congregation does NOT apply to Jumu'ah or the regular five daily prayers."
        ));

        // Card 5: ঈদের নামাযে আযান-ইকামত
        list.add(new HajjHistoryCardItem(
                5,
                "ঈদের নামাযে আযান-ইকামত",
                "Adhan and Iqamah in Eid Prayer",
                "ঈদের নামাযে আযান-ইকামতের বিধান নেই। হযরত জাবের রা. থেকে বর্ণিত, তিনি বলেন- صَلّيْتُ مَعَ رَسُولِ اللهِ صَلّى اللهُ عَلَيْهِ وَسَلّمَ الْعِيدَيْنِ...",
                "There is no Adhan or Iqamah for Eid prayer. Jabir (ra) narrated: I prayed with the Messenger of Allah both Eids without Adhan or Iqamah...",
                "ঈদের নামাযে আযান-ইকামতের বিধান নেই। হযরত জাবের রা. থেকে বর্ণিত, তিনি বলেন-\n\n"
                        + "صَلّيْتُ مَعَ رَسُولِ اللهِ صَلّى اللهُ عَلَيْهِ وَسَلّمَ الْعِيدَيْنِ، غَيْرَ مَرّةٍ وَلَا مَرّتَيْنِ، بِغَيْرِ أَذَانٍ وَلَا إِقَامَةٍ.\n\n"
                        + "অর্থ:\n\n"
                        + "আমি রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লামের সাথে একাধিকবার ঈদের নামায পড়েছি এবং আযান-ইকামত ছাড়া পড়েছি।\n\n"
                        + "[সহীহ মুসলিম, হাদীস ৮৭৮]\n\n"
                        + "তবে কেউ অজ্ঞতাবশত ইকামত দিয়ে দিলে এর কারণে নামায মাকরূহ হবে না।\n\n"
                        + "[কিতাবুল আছল ১/৩১৯; আলহাবীল কুদসী ১/২৪২; শরহুল মুনইয়া, পৃ. ৫৬৭]",
                "There is no Adhan or Iqamah ordained for the Eid prayer.\n\n"
                        + "Jabir (may Allah be pleased with him) reported:\n\n"
                        + "''I prayed the two Eids with the Messenger of Allah (peace and blessings be upon him) more than once or twice, without an Adhan or an Iqamah.'' (Sahih Muslim #878)\n\n"
                        + "However, if someone out of ignorance calls the Iqamah, the prayer does not become Makrooh.\n\n"
                        + "[Kitab al-Asl 1/319; Al-Hawi al-Qudsi 1/242; Sharh al-Munyah, p. 567]"
        ));

        // Card 6: নিয়ত মুখে উচ্চারণ করা
        list.add(new HajjHistoryCardItem(
                6,
                "নিয়ত মুখে উচ্চারণ করা",
                "Verbal Utterance of Intention (Niyyah)",
                "ঈদ বা যে কোনো নামায, রোযা বা অন্যান্য আমলের ক্ষেত্রে অন্তরের সংকল্পই নিয়ত হিসাবে যথেষ্ট। মুখে উচ্চারণ করে বলা জরুরি নয়...",
                "For Eid or any prayer, fasting, or deed, the firm resolve of the heart constitutes sufficient Niyyah...",
                "ঈদ বা যে কোনো নামায, রোযা বা অন্যান্য আমলের ক্ষেত্রে অন্তরের সংকল্পই নিয়ত হিসাবে যথেষ্ট। মুখে উচ্চারণ করে বলা জরুরি নয়। তবে অন্তরের নিয়তের সাথে মুখে উচ্চারণ করা নিষেধও নয়। কেউ ইচ্ছার দৃঢ়তার জন্য মুখেও উচ্চারণ করে নিলে তা দূষণীয় হবে না।\n\n"
                        + "[উমদাতুল কারী ১/৩৩; শরহুল মুনইয়া, পৃ. ২৫৪; আদ্দুররুল মুখতার ১/৪১৫]",
                "For Eid prayer, as well as for all prayers, fasts, and acts of devotion, the sincere intention formed within the heart is entirely sufficient. Uttering the intention verbally is neither mandatory nor a prerequisite. However, articulating it verbally alongside the heart's intention is not forbidden; if done to reinforce one's focus, it is not blameworthy.\n\n"
                        + "['Umdat al-Qari 1/33; Sharh al-Munyah, p. 254; Radd al-Muhtar 1/415]"
        ));

        // Card 7: ঈদের নামাযে সাহু সিজদা
        list.add(new HajjHistoryCardItem(
                7,
                "ঈদের নামাযে সাহু সিজদা",
                "Sahu Sajdah in Eid Prayer",
                "অন্যান্য নামাযের ন্যায় ঈদের নামাযেও জামাত ছোট হলে এবং বিশৃঙ্খলার আশঙ্কা না থাকলে ওয়াজিব ছুটে গেলে সিজদায়ে সাহু দিতে হয়...",
                "As in other prayers, if the congregation is small without risk of confusion, missed Wajib warrants Sahu Sajdah...",
                "অন্যান্য নামাযের ন্যায় ঈদের নামাযেও জামাত ছোট হলে এবং বিশৃঙ্খলার আশঙ্কা না থাকলে ওয়াজিব ছুটে গেলে সিজদায়ে সাহু দিতে হয়। তবে যেহেতু ঈদের জামাতে সাধারণত অনেক বড় জমায়েত হয়ে থাকে, অনেক মানুষ সিজদায়ে সাহুর নিয়ম-কানুন সম্পর্কে জ্ঞাত থাকে না, তাই সিজদায়ে সাহু আদায় করতে গেলে অনেক সময় বিশৃঙ্খলা দেখা দিতে পারে। এজন্য কোনো কোনো ফকীহের মতে ঈদ, জুমা বা এরকম বড় কোনো জামাতের ক্ষেত্রে ইমাম সাহেব সিজদায়ে সাহু করতে গেলে যদি মুসল্লীদের মাঝে ভুল বোঝাবুঝির আশঙ্কা হয় তাহলে সেক্ষেত্রে সিজদায়ে সাহু মাফ হয়ে যাবে। ইমাম স্বাভাবিক নিয়মে নামায শেষ করবে।\n\n"
                        + "[কিতাবুল আছল ১/৩২৪ ; আলমুহীতুল বুরহানী ২/৫০১]",
                "Just like in other prayers, if an obligatory element (Wajib) is inadvertently omitted during Eid prayer and the congregation is small with no danger of confusion, Sajdah as-Sahu should be observed. However, because Eid gatherings are typically massive and many worshippers are unfamiliar with the rules of Sahu Sajdah, executing it can lead to severe confusion.\n\n"
                        + "Therefore, according to leading jurists, in massive congregations like Eid or Jumu'ah, if the Imam fears that performing Sajdah as-Sahu will cause widespread misunderstanding among the congregation, Sajdah as-Sahu is waived and omitted. The Imam completes the prayer normally.\n\n"
                        + "[Kitab al-Asl 1/324; Al-Muheet al-Burhani 2/501]"
        ));

        // Card 8: ঈদের নামাযের পর তাকবীরে তাশরীক
        list.add(new HajjHistoryCardItem(
                8,
                "ঈদের নামাযের পর তাকবীরে তাশরীক",
                "Takbeer at-Tashreeq After Eid Prayer",
                "ঈদুল আযহার নামাযের পর তাকবীরে তাশরীক বলা যেতে পারে। তবে ফরয নামাযের মত ঈদের নামাযের পর তাকবীর বলা ওয়াজিব বা আবশ্যকীয় নয়।",
                "Takbeer at-Tashreeq may be uttered following Eid al-Adha prayer, though unlike obligatory prayers, it is not Wajib...",
                "ঈদুল আযহার নামাযের পর তাকবীরে তাশরীক বলা যেতে পারে। তবে ফরয নামাযের মত ঈদের নামাযের পর তাকবীর বলা ওয়াজিব বা আবশ্যকীয় নয়।\n\n"
                        + "[আলবাহরুর রায়েক ২/১৬৫; ফাতাওয়া হিন্দিয়া ১/১৫২; রদ্দুল মুহতার ২/১৮০]",
                "Takbeer at-Tashreeq may be proclaimed after the prayer of Eid al-Adha. However, unlike the five daily obligatory prayers, reciting it after the Eid prayer is not Wajib (strictly required), but permissible and recommended.\n\n"
                        + "[Al-Bahr ar-Ra'iq 2/165; Fatawa Hindiyyah 1/152; Radd al-Muhtar 2/180]"
        ));

        // Card 9: ঈদের নামায ছুটে গেলে
        list.add(new HajjHistoryCardItem(
                9,
                "ঈদের নামায ছুটে গেলে",
                "What to Do if Eid Prayer is Missed",
                "ঈদের নামাযে কাযার বিধান নেই। তাই কারো ঈদের নামায ছুটে গেলে সে আশপাশের অন্য কোনো ঈদের জামাতে শরীক হওয়ার চেষ্টা করবে...",
                "There is no Qadha ordained for Eid prayer. If someone misses the congregation, he should endeavor to join another nearby Eid prayer...",
                "ঈদের নামাযে কাযার বিধান নেই। তাই কারো ঈদের নামায ছুটে গেলে সে আশপাশের অন্য কোনো ঈদের জামাতে শরীক হওয়ার চেষ্টা করবে। এমনটি সম্ভব না হলে তওবা-ইস্তেগফার করবে।\n\n"
                        + "[শরহু মুখতাসারিত তাহাবী ২/১৬১; আলমুহীতুল বুরহানী ২/৪৯৮; আলহাবীল কুদসী ১/২৪৪]",
                "According to the Hanafi school, there is no individual makeup (Qadha) for missed Eid prayer. Therefore, if someone misses his local Eid congregation, he should make every effort to catch another Eid congregation in a neighboring locality. If that is not possible, he should repent and seek forgiveness from Allah.\n\n"
                        + "[Sharh Mukhtasar at-Tahawi 2/161; Al-Muheet al-Burhani 2/498; Al-Hawi al-Qudsi 1/244]"
        ));

        // Card 10: ঈদ ও জুমা একই দিনে হলে
        list.add(new HajjHistoryCardItem(
                10,
                "ঈদ ও জুমা একই দিনে হলে",
                "When Eid and Jumu'ah Fall on the Same Day",
                "জুমার দিন ঈদ হলে ঈদ ও জুমা উভয়টিই পড়তে হবে। ঈদের নামায পড়লে জুমা পড়তে হবে না- এ ধারণা সম্পূর্ণ ভুল...",
                "If Eid falls on a Friday, both Eid and Jumu'ah prayers must be observed according to the majority of classical jurists...",
                "জুমার দিন ঈদ হলে ঈদ ও জুমা উভয়টিই পড়তে হবে। ঈদের নামায পড়লে জুমা পড়তে হবে না- এ ধারণা সম্পূর্ণ ভুল। কেননা ঈদের নামায ও জুমার নামায দুটি পৃথক পৃথক আমল। অন্যদিকে ঈদের নামায ওয়াজিব আর জুমার নামায ফরয। সুতরাং একটি আদায় করে আরেকটি বাদ দেয়ার কোনো সুযোগ নেই। এর স্বপক্ষে হাদীসের অনেক সুস্পষ্ট প্রমাণও রয়েছে। জুমার দিন ঈদ হলে রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম উভয় নামাযই পড়তেন। সহীহ হাদীস দ্বারা এটিই প্রমাণিত। এক্ষেত্রে রাসূলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম থেকে জুমার নামায না পড়ার কোনো প্রমাণ নেই।\n\n"
                        + "[সহীহ বুখারী, হাদীস ৫৫৭২; সহীহ মুসলিম, হাদীস ৮৭৮; মুসনাদুশ শাফেয়ী, হাদীস ৫০০; শরহু মুশকিলিল আছার ৩/১৮৭; আততামহীদ ১৪/২৭৪]",
                "If Eid occurs on a Friday, both the Eid prayer and the Jumu'ah prayer must be performed. The notion that performing Eid prayer exempts one from Jumu'ah is incorrect under mainstream jurisprudence. This is because Eid prayer and Jumu'ah prayer are two distinct acts of worship: Eid prayer is Wajib while Jumu'ah prayer is Fard 'Ayn. Therefore, performing one does not waive the obligation of the other.\n\n"
                        + "Clear prophetic evidence confirms this practice: whenever Eid fell on a Friday, the Messenger of Allah (peace and blessings be upon him) performed both prayers. There is no authentic report indicating that the Prophet omitted Jumu'ah on Eid day.\n\n"
                        + "[Sahih Bukhari #5572; Sahih Muslim #878; Musnad ash-Shafi'i #500; Sharh Mushkil al-Athar 3/187; At-Tamheed 14/274]"
        ));

        return list;
    }
}
