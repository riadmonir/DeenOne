package com.devflux.deenone.features.ramadan.data;

import android.content.Context;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.features.ramadan.model.RozaHealthAdviceItem;
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
 * Repository for Roza Health Advice (রোজায় স্বাস্থ্য পরামর্শ).
 * Provides instant in-memory cache for 60 FPS, with background async REST API sync.
 */
public class RozaHealthAdviceRepository {

    private static final List<RozaHealthAdviceItem> cachedList = Collections.synchronizedList(new ArrayList<>());
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface DataCallback {
        void onDataLoaded(List<RozaHealthAdviceItem> items);
    }

    /**
     * Get all 10 health advice topics instantly from memory/cache, with background remote sync.
     */
    public static List<RozaHealthAdviceItem> getTopics(Context context, DataCallback callback) {
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
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_roza_health_advice.php");
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
                        List<RozaHealthAdviceItem> remoteItems = new ArrayList<>();

                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int id = obj.has("id") ? obj.get("id").getAsInt() : 0;
                            String slug = obj.has("slug") ? obj.get("slug").getAsString() : "";
                            String headerTitleBn = obj.has("header_title_bn") ? obj.get("header_title_bn").getAsString() :
                                    (obj.has("title_bn") ? obj.get("title_bn").getAsString() : "");
                            String headerTitleEn = obj.has("header_title_en") ? obj.get("header_title_en").getAsString() :
                                    (obj.has("title_en") ? obj.get("title_en").getAsString() : "");
                            String cardTitleBn = obj.has("card_title_bn") ? obj.get("card_title_bn").getAsString() : headerTitleBn;
                            String cardTitleEn = obj.has("card_title_en") ? obj.get("card_title_en").getAsString() : headerTitleEn;
                            String catBn = obj.has("category_bn") ? obj.get("category_bn").getAsString() : "স্বাস্থ্য পরামর্শ";
                            String catEn = obj.has("category_en") ? obj.get("category_en").getAsString() : "Health Advice";
                            String previewBn = obj.has("preview_bn") ? obj.get("preview_bn").getAsString() : null;
                            String previewEn = obj.has("preview_en") ? obj.get("preview_en").getAsString() : null;
                            String detailsBn = obj.has("details_bn") ? obj.get("details_bn").getAsString() : "";
                            String detailsEn = obj.has("details_en") ? obj.get("details_en").getAsString() : "";
                            String refBn = obj.has("reference_bn") ? obj.get("reference_bn").getAsString() : "";
                            String refEn = obj.has("reference_en") ? obj.get("reference_en").getAsString() : "";
                            int order = obj.has("display_order") ? obj.get("display_order").getAsInt() : 1;

                            remoteItems.add(new RozaHealthAdviceItem(
                                    id, slug, headerTitleBn, headerTitleEn, cardTitleBn, cardTitleEn,
                                    catBn, catEn, previewBn, previewEn, detailsBn, detailsEn, refBn, refEn, order
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
            // Graceful fallback to cached local seed
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    public static List<RozaHealthAdviceItem> getDefaultTopics() {
        List<RozaHealthAdviceItem> list = new ArrayList<>();

        // 1. সেহরিতে পুষ্টিকর ও ভারসাম্যপূর্ণ খাবার খাওয়া (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                1,
                "sehri_balanced_diet",
                "সেহরিতে পুষ্টিকর ও ভারসাম্যপূর্ণ খাবার খাওয়া",
                "Eating Wholesome and Balanced Meals at Sehri",
                "পুষ্টিকর ও ভারসাম্যপূর্ণ খাবার খাওয়া",
                "Wholesome and Balanced Meals",
                "সেহরি ও পুষ্টি",
                "Sehri and Nutrition",
                "সেহরি এমনভাবে করুন যেন এটি আপনাকে সারাদিন শক্তি জোগায় উচ্চ প্রোটিনসমৃদ্ধ খাবার যেমন ডিম, দই, চিড়া, ওটস ইত্যাদি খান। লো গ্লাইসেমিক ইনডেক্স (GI) সম্পন্ন খাবার যেমন বাদাম, শস্যজাতীয় খাবার ও ডাল খান, যা ধীরে ধীরে শক্তি সরবরাহ করে। বেশি লবণযুক্ত ও চর্বিযুক্ত খাবার পরিহার করুন, কারণ এটি পিপাসা বাড়াতে পারে।",
                "Take your Sehri in such a manner that it provides sustained energy throughout the day. Consume high-protein foods such as eggs, yogurt, flattened rice, and oats. Eat low glycemic index (GI) foods like nuts, whole grains, and lentils which release energy gradually. Avoid excessively salty and fatty foods, as they increase thirst.",
                "সেহরি এমনভাবে করুন যেন এটি আপনাকে সারাদিন শক্তি জোগায়।\n\n" +
                        "উচ্চ প্রোটিনসমৃদ্ধ খাবার যেমন ডিম, দই, চিড়া, ওটস ইত্যাদি খান।\n" +
                        "লো গ্লাইসেমিক ইনডেক্স (GI) সম্পন্ন খাবার যেমন বাদাম, শস্যজাতীয় খাবার ও ডাল খান, যা ধীরে ধীরে শক্তি সরবরাহ করে।\n" +
                        "বেশি লবণযুক্ত ও চর্বিযুক্ত খাবার পরিহার করুন, কারণ এটি পিপাসা বাড়াতে পারে।",
                "Take your Sehri in such a manner that it provides sustained energy throughout the day.\n\n" +
                        "Consume high-protein foods such as eggs, yogurt, flattened rice, and oats.\n" +
                        "Eat low glycemic index (GI) foods like nuts, whole grains, and lentils which release energy gradually.\n" +
                        "Avoid excessively salty and fatty foods, as they increase thirst.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                1
        ));

        // 2. পর্যাপ্ত পানি পান করা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                2,
                "adequate_water_hydration",
                "পর্যাপ্ত পানি পান করা",
                "Drinking Adequate Water",
                "পর্যাপ্ত পানি পান করা (হাইড্রেশন বজায় রাখা)",
                "Drinking Adequate Water (Maintaining Hydration)",
                "হাইড্রেশন ও পানি",
                "Hydration and Water",
                "ইফতার থেকে সেহরির মধ্যে অন্তত ৮-১০ গ্লাস পানি পান করুন। ইফতারের পর একবারে বেশি পানি না খেয়ে ধীরে ধীরে খান। কফি, চা এবং কার্বোনেটেড ড্রিংকস কম পান করুন, কারণ এগুলো ডিহাইড্রেশন সৃষ্টি করতে পারে। শরবত বা ডাবের পানি খেতে পারেন, যা শরীরের পানিশূন্যতা পূরণে সাহায্য করে।",
                "Drink at least 8-10 glasses of water between Iftar and Sehri. Avoid drinking too much water at once after Iftar; drink slowly and gradually. Reduce intake of coffee, tea, and carbonated beverages, as these can cause dehydration. You may consume sherbet or green coconut water, which helps replenish bodily hydration.",
                "ইফতার থেকে সেহরির মধ্যে অন্তত ৮-১০ গ্লাস পানি পান করুন।\n" +
                        "ইফতারের পর একবারে বেশি পানি না খেয়ে ধীরে ধীরে খান।\n" +
                        "কফি, চা এবং কার্বোনেটেড ড্রিংকস কম পান করুন, কারণ এগুলো ডিহাইড্রেশন সৃষ্টি করতে পারে।\n" +
                        "শরবত বা ডাবের পানি খেতে পারেন, যা শরীরের পানিশূন্যতা পূরণে সাহায্য করে।",
                "Drink at least 8-10 glasses of water between Iftar and Sehri.\n" +
                        "Avoid drinking too much water at once after Iftar; drink slowly and gradually.\n" +
                        "Reduce intake of coffee, tea, and carbonated beverages, as these can cause dehydration.\n" +
                        "You may consume sherbet or green coconut water, which helps replenish bodily hydration.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                2
        ));

        // 3. ইফতার স্বাস্থ্যকর ও পরিমিত পরিমাণে করা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                3,
                "healthy_moderate_iftar",
                "ইফতার স্বাস্থ্যকর ও পরিমিত পরিমাণে করা",
                "Healthy and Moderate Iftar",
                "ইফতার স্বাস্থ্যকর ও পরিমিত পরিমাণে করা",
                "Healthy and Moderate Iftar",
                "ইফতার ও পুষ্টি",
                "Iftar and Nutrition",
                "ইফতার খেজুর ও পানি দিয়ে শুরু করুন, যা দ্রুত শক্তি দেয়। অতিরিক্ত ভাজাপোড়া খাবার এড়িয়ে চলুন, কারণ এটি হজমে সমস্যা তৈরি করতে পারে। প্রোটিন ও ফাইবারসমৃদ্ধ খাবার খান যেমন – মাছ, মুরগি, সবজি, ফলমূল ও বাদাম। প্রচুর শর্করা জাতীয় খাবার (সাদা ভাত, মিষ্টি) না খেয়ে জটিল শর্করা (লাল চালের ভাত, আটার রুটি) খান।",
                "Begin your Iftar with dates and water, which provide instant energy. Avoid excessive fried foods, as they can cause digestive issues. Consume protein and fiber-rich foods such as fish, chicken, vegetables, fruits, and nuts. Instead of excessive simple carbohydrates (white rice, sweets), eat complex carbohydrates (brown rice, whole wheat flatbread).",
                "ইফতার খেজুর ও পানি দিয়ে শুরু করুন, যা দ্রুত শক্তি দেয়।\n" +
                        "অতিরিক্ত ভাজাপোড়া খাবার এড়িয়ে চলুন, কারণ এটি হজমে সমস্যা তৈরি করতে পারে।\n" +
                        "প্রোটিন ও ফাইবারসমৃদ্ধ খাবার খান যেমন – মাছ, মুরগি, সবজি, ফলমূল ও বাদাম।\n" +
                        "প্রচুর শর্করা জাতীয় খাবার (সাদা ভাত, মিষ্টি) না খেয়ে জটিল শর্করা (লাল চালের ভাত, আটার রুটি) খান।",
                "Begin your Iftar with dates and water, which provide instant energy.\n" +
                        "Avoid excessive fried foods, as they can cause digestive issues.\n" +
                        "Consume protein and fiber-rich foods such as fish, chicken, vegetables, fruits, and nuts.\n" +
                        "Instead of excessive simple carbohydrates (white rice, sweets), eat complex carbohydrates (brown rice, whole wheat flatbread).",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                3
        ));

        // 4. সুগার ও প্রসেসড ফুড কমিয়ে রাখা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                4,
                "reduce_sugar_processed_food",
                "সুগার ও প্রসেসড ফুড কমিয়ে রাখা",
                "Reducing Sugar and Processed Foods",
                "সুগার ও প্রসেসড ফুড কমিয়ে রাখা",
                "Reducing Sugar and Processed Foods",
                "খাদ্যাভ্যাস ও সতর্কতা",
                "Dietary Habits and Precautions",
                "অতিরিক্ত মিষ্টিজাতীয় খাবার (সিরা ভেজানো মিষ্টি, কোল্ড ড্রিংকস) কম খান। প্রাকৃতিক চিনি যেমন: ফলমূল ও মধু থেকে চিনি গ্রহণ করুন। সফট ড্রিংকস ও ফাস্ট ফুড থেকে বিরত থাকুন, কারণ এগুলো গ্যাসের সমস্যা ও ওজন বাড়াতে পারে।",
                "Reduce consumption of excessively sweet foods (syrup-soaked sweets, cold drinks). Obtain natural sugars from sources like fruits and honey. Avoid soft drinks and fast food, as these can cause gas problems and weight gain.",
                "অতিরিক্ত মিষ্টিজাতীয় খাবার (সিরা ভেজানো মিষ্টি, কোল্ড ড্রিংকস) কম খান।\n" +
                        "প্রাকৃতিক চিনি যেমন: ফলমূল ও মধু থেকে চিনি গ্রহণ করুন।\n" +
                        "সফট ড্রিংকস ও ফাস্ট ফুড থেকে বিরত থাকুন, কারণ এগুলো গ্যাসের সমস্যা ও ওজন বাড়াতে পারে।",
                "Reduce consumption of excessively sweet foods (syrup-soaked sweets, cold drinks).\n" +
                        "Obtain natural sugars from sources like fruits and honey.\n" +
                        "Avoid soft drinks and fast food, as these can cause gas problems and weight gain.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                4
        ));

        // 5. হালকা ব্যায়াম ও শরীরচর্চা করা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                5,
                "light_exercise_fitness",
                "হালকা ব্যায়াম ও শরীরচর্চা করা",
                "Light Exercise and Physical Activity",
                "হালকা ব্যায়াম ও শরীরচর্চা করা",
                "Light Exercise and Physical Activity",
                "শারীরচর্চা ও ফিটনেস",
                "Exercise and Fitness",
                "রমজানে একদম অলস হয়ে যাওয়া উচিত নয়। ইফতারের ১-২ ঘণ্টা পর হালকা হাঁটা বা ব্যায়াম করুন। রোজার সময় ভারী ব্যায়াম বা জিম না করে হালকা স্ট্রেচিং বা ইয়োগা করুন। তারাবিহ নামাজের সময় দাঁড়িয়ে থাকাও একটি ভালো শারীরিক ব্যায়াম।",
                "One should not become completely inactive during Ramadan. Take a light walk or do gentle exercises 1-2 hours after Iftar. Instead of heavy workouts or strenuous gym sessions while fasting, do light stretching or yoga. Standing during Taraweeh prayer is also a beneficial physical exercise.",
                "রমজানে একদম অলস হয়ে যাওয়া উচিত নয়।\n" +
                        "ইফতারের ১-২ ঘণ্টা পর হালকা হাঁটা বা ব্যায়াম করুন।\n" +
                        "রোজার সময় ভারী ব্যায়াম বা জিম না করে হালকা স্ট্রেচিং বা ইয়োগা করুন।\n" +
                        "তারাবিহ নামাজের সময় দাঁড়িয়ে থাকাও একটি ভালো শারীরিক ব্যায়াম।",
                "One should not become completely inactive during Ramadan.\n" +
                        "Take a light walk or do gentle exercises 1-2 hours after Iftar.\n" +
                        "Instead of heavy workouts or strenuous gym sessions while fasting, do light stretching or yoga.\n" +
                        "Standing during Taraweeh prayer is also a beneficial physical exercise.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                5
        ));

        // 6. হজমের সমস্যা এড়ানোর জন্য খাবারে ভারসাম্য রাখা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                6,
                "prevent_digestive_issues",
                "হজমের সমস্যা এড়ানোর জন্য খাবারে ভারসাম্য রাখা",
                "Balancing Diet to Prevent Digestive Issues",
                "হজমের সমস্যা এড়ানোর জন্য খাবারে ভারসাম্য রাখা",
                "Balancing Diet to Prevent Digestive Issues",
                "হজম ও পরিপাকতন্ত্র",
                "Digestion and Gut Health",
                "দ্রুত বেশি খাবার না খেয়ে ধীরে ধীরে খান। বেশি তেল-মসলাযুক্ত খাবার খেলে অ্যাসিডিটি ও গ্যাসের সমস্যা হতে পারে। বেশি চর্বিযুক্ত ও প্রক্রিয়াজাত খাবার খেলে কোষ্ঠকাঠিন্য হতে পারে, তাই ফাইবারসমৃদ্ধ খাবার খান।",
                "Avoid eating quickly or in excess; eat slowly and mindfully. Consuming overly oily and spicy foods can cause acidity and gastric issues. High-fat and heavily processed foods can lead to constipation, so consume fiber-rich meals.",
                "দ্রুত বেশি খাবার না খেয়ে ধীরে ধীরে খান।\n" +
                        "বেশি তেল-মসলাযুক্ত খাবার খেলে অ্যাসিডিটি ও গ্যাসের সমস্যা হতে পারে।\n" +
                        "বেশি চর্বিযুক্ত ও প্রক্রিয়াজাত খাবার খেলে কোষ্ঠকাঠিন্য হতে পারে, তাই ফাইবারসমৃদ্ধ খাবার খান।",
                "Avoid eating quickly or in excess; eat slowly and mindfully.\n" +
                        "Consuming overly oily and spicy foods can cause acidity and gastric issues.\n" +
                        "High-fat and heavily processed foods can lead to constipation, so consume fiber-rich meals.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                6
        ));

        // 7. রমজানে ওজন নিয়ন্ত্রণ করা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                7,
                "ramadan_weight_control",
                "রমজানে ওজন নিয়ন্ত্রণ করা",
                "Weight Management in Ramadan",
                "রমজানে ওজন নিয়ন্ত্রণ করা",
                "Weight Management in Ramadan",
                "ওজন নিয়ন্ত্রণ ও স্বাস্থ্য",
                "Weight Control and Health",
                "রমজান ওজন কমানোর একটি সুযোগ, তাই অস্বাস্থ্যকর খাদ্যাভ্যাস এড়িয়ে চলুন। কম ক্যালোরিযুক্ত এবং পুষ্টিকর খাবার খান। অতিরিক্ত চিনি ও চর্বিযুক্ত খাবার খেলে ওজন বেড়ে যেতে পারে।",
                "Ramadan is an opportunity to shed excess weight, so avoid unhealthy eating habits. Consume low-calorie, nutrient-dense meals. Eating excessive sugar and fat-laden foods can result in weight gain.",
                "রমজান ওজন কমানোর একটি সুযোগ, তাই অস্বাস্থ্যকর খাদ্যাভ্যাস এড়িয়ে চলুন।\n" +
                        "কম ক্যালোরিযুক্ত এবং পুষ্টিকর খাবার খান।\n" +
                        "অতিরিক্ত চিনি ও চর্বিযুক্ত খাবার খেলে ওজন বেড়ে যেতে পারে।",
                "Ramadan is an opportunity to shed excess weight, so avoid unhealthy eating habits.\n" +
                        "Consume low-calorie, nutrient-dense meals.\n" +
                        "Eating excessive sugar and fat-laden foods can result in weight gain.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                7
        ));

        // 8. ঘুমের রুটিন ঠিক রাখা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                8,
                "proper_sleep_routine",
                "ঘুমের রুটিন ঠিক রাখা",
                "Maintaining Proper Sleep Routine",
                "ঘুমের রুটিন ঠিক রাখা",
                "Maintaining Proper Sleep Routine",
                "বিশ্রাম ও ঘুম",
                "Rest and Sleep",
                "সেহরির জন্য ভোররাতে উঠতে হয়, তাই ঘুমের রুটিন ঠিক রাখা জরুরি। দিনে ৬-৮ ঘণ্টা ঘুম নিশ্চিত করুন, যেন শরীর ক্লান্ত না হয়। রাতে খুব দেরি করে জেগে থাকা বা ফজরের পর বেশি ঘুমানো পরিহার করুন।",
                "One must wake up early for Sehri, making it essential to maintain a proper sleep routine. Ensure 6-8 hours of sleep each day so that the body does not get exhausted. Avoid staying up late into the night or oversleeping after Fajr.",
                "সেহরির জন্য ভোররাতে উঠতে হয়, তাই ঘুমের রুটিন ঠিক রাখা জরুরি।\n" +
                        "দিনে ৬-৮ ঘণ্টা ঘুম নিশ্চিত করুন, যেন শরীর ক্লান্ত না হয়।\n" +
                        "রাতে খুব দেরি করে জেগে থাকা বা ফজরের পর বেশি ঘুমানো পরিহার করুন।",
                "One must wake up early for Sehri, making it essential to maintain a proper sleep routine.\n" +
                        "Ensure 6-8 hours of sleep each day so that the body does not get exhausted.\n" +
                        "Avoid staying up late into the night or oversleeping after Fajr.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                8
        ));

        // 9. মানসিক ও আত্মিক প্রশান্তি বজায় রাখা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                9,
                "mental_spiritual_peace",
                "মানসিক ও আত্মিক প্রশান্তি বজায় রাখা",
                "Maintaining Mental and Spiritual Tranquility",
                "মানসিক ও আত্মিক প্রশান্তি বজায় রাখা",
                "Maintaining Mental and Spiritual Tranquility",
                "আত্মিক প্রশান্তি",
                "Spiritual Peace",
                "রমজানে শুধু শারীরিক স্বাস্থ্য নয়, মানসিক স্বাস্থ্যও ভালো রাখা জরুরি। অতিরিক্ত স্ট্রেস থেকে দূরে থাকার চেষ্টা করুন এবং বেশি বেশি কুরআন তিলাওয়াত ও জিকির করুন। ধৈর্য ধরে দিন অতিবাহিত করুন, কারণ রোজা সবর ও আত্মনিয়ন্ত্রণের শিক্ষা দেয়।",
                "During Ramadan, along with physical health, maintaining mental well-being is vital. Strive to stay free from excessive stress, and engage in frequent Quran recitation and dhikr. Spend your day with patience, as fasting cultivates forbearance and self-discipline.",
                "রমজানে শুধু শারীরিক স্বাস্থ্য নয়, মানসিক স্বাস্থ্যও ভালো রাখা জরুরি।\n" +
                        "অতিরিক্ত স্ট্রেস থেকে দূরে থাকার চেষ্টা করুন এবং বেশি বেশি কুরআন তিলাওয়াত ও জিকির করুন।\n" +
                        "ধৈর্য ধরে দিন অতিবাহিত করুন, কারণ রোজা সবর ও আত্মনিয়ন্ত্রণের শিক্ষা দেয়।",
                "During Ramadan, along with physical health, maintaining mental well-being is vital.\n" +
                        "Strive to stay free from excessive stress, and engage in frequent Quran recitation and dhikr.\n" +
                        "Spend your day with patience, as fasting cultivates forbearance and self-discipline.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                9
        ));

        // 10. চিকিৎসা ও স্বাস্থ্য সমস্যায় বিশেষ সতর্কতা অবলম্বন করা (100% Verbatim from User Prompt & Screenshot)
        list.add(new RozaHealthAdviceItem(
                10,
                "medical_precautions_health",
                "চিকিৎসা ও স্বাস্থ্য সমস্যায় বিশেষ সতর্কতা অবলম্বন করা",
                "Special Precautions for Medical and Health Conditions",
                "চিকিৎসা ও স্বাস্থ্য সমস্যায় বিশেষ সতর্কতা অবলম্বন করা",
                "Special Precautions for Medical and Health Conditions",
                "চিকিৎসা ও সতর্কতা",
                "Medical Guidance",
                "ডায়াবেটিস, উচ্চ রক্তচাপ, গ্যাস্ট্রিক বা অন্য কোনো দীর্ঘমেয়াদী রোগ থাকলে চিকিৎসকের পরামর্শ নিয়ে রোজা রাখুন। যারা নিয়মিত ওষুধ সেবন করেন, তারা রমজানের সময় ওষুধের সঠিক রুটিন তৈরি করুন। অতিরিক্ত দুর্বলতা, মাথা ঘোরা বা অন্য কোনো অসুবিধা হলে রোজা ভেঙে ফেলা জায়েজ, কারণ ইসলাম স্বাস্থ্যকে অগ্রাধিকার দেয়।",
                "If you have diabetes, high blood pressure, gastritis, or any other chronic condition, observe fasting in consultation with a physician. Those who take regular medication should establish a proper medicine routine for the duration of Ramadan. If excessive weakness, dizziness, or any severe discomfort occurs, breaking the fast is permissible, as Islam prioritizes health.",
                "ডায়াবেটিস, উচ্চ রক্তচাপ, গ্যাস্ট্রিক বা অন্য কোনো দীর্ঘমেয়াদী রোগ থাকলে চিকিৎসকের পরামর্শ নিয়ে রোজা রাখুন।\n" +
                        "যারা নিয়মিত ওষুধ সেবন করেন, তারা রমজানের সময় ওষুধের সঠিক রুটিন তৈরি করুন।\n" +
                        "অতিরিক্ত দুর্বলতা, মাথা ঘোরা বা অন্য কোনো অসুবিধা হলে রোজা ভেঙে ফেলা জায়েজ, কারণ ইসলাম স্বাস্থ্যকে অগ্রাধিকার দেয়।",
                "If you have diabetes, high blood pressure, gastritis, or any other chronic condition, observe fasting in consultation with a physician.\n" +
                        "Those who take regular medication should establish a proper medicine routine for the duration of Ramadan.\n" +
                        "If excessive weakness, dizziness, or any severe discomfort occurs, breaking the fast is permissible, as Islam prioritizes health.",
                "স্বাস্থ্য ও পুষ্টি বিজ্ঞান বিষয়ক স্বাস্থ্যবিধি",
                "Health and Nutrition Scientific Guidelines",
                10
        ));

        return list;
    }
}
