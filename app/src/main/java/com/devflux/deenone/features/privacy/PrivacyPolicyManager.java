package com.devflux.deenone.features.privacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrivacyPolicyManager {

    private static final String PREF_NAME = "deenone_privacy_policy_cache";
    private static final String KEY_CACHE_JSON = "cached_policy_json";
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public interface PrivacySyncCallback {
        void onDataLoaded(PrivacyPolicyModel model);
    }

    public static PrivacyPolicyModel getCachedPrivacyPolicy(Context context) {
        if (context == null) return getDefaultPrivacyPolicy();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String rawJson = prefs.getString(KEY_CACHE_JSON, null);
        if (rawJson != null && !rawJson.trim().isEmpty()) {
            try {
                JSONObject json = new JSONObject(rawJson);
                PrivacyPolicyModel model = PrivacyPolicyModel.fromJson(json);
                if (model != null && model.contentBn != null && !model.contentBn.isEmpty()) {
                    return model;
                }
            } catch (Exception ignored) {}
        }
        return getDefaultPrivacyPolicy();
    }

    public static void saveToCache(Context context, PrivacyPolicyModel model) {
        if (context == null || model == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_CACHE_JSON, model.toJson().toString()).apply();
    }

    public static void syncRemoteData(Context context, PrivacySyncCallback callback) {
        if (context == null) return;
        final Context appCtx = context.getApplicationContext();

        EXECUTOR.execute(() -> {
            try {
                String baseUrl = BackendConfigManager.getPhpApiBaseUrl(appCtx);
                if (!baseUrl.endsWith("/")) baseUrl += "/";
                String apiUrl = baseUrl + "privacy_policy.php?lang=all";

                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("Accept", "application/json");

                int code = conn.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    if (json.optBoolean("success", false)) {
                        PrivacyPolicyModel model = PrivacyPolicyModel.fromJson(json);
                        if (model != null) {
                            saveToCache(appCtx, model);
                            if (callback != null) {
                                MAIN_HANDLER.post(() -> callback.onDataLoaded(model));
                            }
                        }
                    }
                }
                conn.disconnect();
            } catch (Exception ignored) {
                // Offline or server unreachable: local cached data is preserved
            }
        });
    }

    public static PrivacyPolicyModel getDefaultPrivacyPolicy() {
        PrivacyPolicyModel model = new PrivacyPolicyModel();
        model.titleBn = "প্রাইভেসি পলিসি";
        model.titleEn = "PRIVACY POLICY";
        model.updatedAtBn = "সর্বশেষ হালনাগাদ: ২০২৬";
        model.updatedAtEn = "Last update: September 2026";
        model.contactEmail = "privacy@deenone.top";
        model.organization = "DeenOne Technologies & Foundation";
        model.privacyUrl = "https://deenone.top/privacy";
        model.dataDeletionInfo = "ব্যবহারকারী অ্যাপের সেটিংস থেকে অথবা privacy@deenone.top এ ইমেইল পাঠিয়ে যেকোনো সময় তাদের অ্যাকাউন্ট ও ক্লাউডে সংরক্ষিত সমস্ত তথ্য স্থায়ীভাবে মুছে ফেলার আবেদন করতে পারেন।";

        model.contentBn = "<p>দ্বীনওয়ান (DeenOne) ব্যবহারকারীদের ব্যক্তিগত তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা বজায় রাখতে প্রতিশ্রুতিবদ্ধ। আমরা একটি বিশুদ্ধ, বিজ্ঞাপনমুক্ত ও খাঁটি ইসলামিক জীবনধারা অ্যাপ্লিকেশন হিসেবে ব্যবহারকারীর তথ্যের নিরাপত্তা ও সুরক্ষায় সর্বোচ্চ মানদণ্ড অনুসরণ করি।</p>"
                + "<h3>১. আমরা কী কী তথ্য সংগ্রহ করি</h3>"
                + "<p>সঠিক নামাজের সময়সূচি, কিবলা দিক এবং বিশুদ্ধ ইসলামিক লাইফস্টাইল ট্র্যাকিং নিশ্চিত করতে আমরা শুধুমাত্র প্রয়োজনীয় তথ্য সংগ্রহ করি:</p>"
                + "<ul>"
                + "<li><strong>প্রোফাইল তথ্য:</strong> নাম, ইমেইল বা ফোন নম্বর (অ্যাকাউন্ট ব্যাকআপ ও মাল্টি-ডিভাইস ডেটা সিঙ্কের জন্য)।</li>"
                + "<li><strong>লোকেশন ডাটা:</strong> শুধুমাত্র সালাতের ওয়াক্ত ও কিবলা কম্পাসের দিক নির্ধারণে ডিভাইসে স্থানীয়ভাবে ব্যবহৃত হয়। এটি আমাদের সার্ভারে স্থায়ীভাবে জমা বা তৃতীয় পক্ষের সাথে ট্র্যাক করা হয় না।</li>"
                + "<li><strong>ইবাদত ও সালাত ট্র্যাকিং:</strong> দৈনিক সালাত, কুরআন তিলাওয়াত ও তাসবীহ জিকিরের পরিসংখ্যান (যা সম্পূর্ণ ব্যক্তিগত ও সুরক্ষিত)।</li>"
                + "<li><strong>রক্তদান নেটওয়ার্ক:</strong> স্বেচ্ছায় নিবন্ধিত রক্তদাতাদের রক্তের গ্রুপ, জেলা ও যোগাযোগের নম্বর শুধুমাত্র জরুরি রক্তপ্রয়োজনে সহমর্মী ভাইদের সুবিধার্থে প্রদর্শিত হয়।</li>"
                + "</ul>"
                + "<h3>২. ফোরগ্রাউন্ড ও মিডিয়া সার্ভিস</h3>"
                + "<p>পবিত্র কুরআন অডিও তিলাওয়াত, স্লিপ মোড টাইমার ও আজান রিমাইন্ডার ব্যাকগ্রাউন্ডে নিরবচ্ছিন্নভাবে পরিচালনার সুবিধার্থে স্ট্যান্ডার্ড অ্যান্ড্রয়েড মিডিয়া সার্ভিস ব্যবহৃত হয়।</p>"
                + "<h3>৩. তথ্যের নিরাপত্তা ও এনক্রিপশন</h3>"
                + "<p>আপনার সমস্ত নেটওয়ার্ক ডেটা আধুনিক SSL/TLS ২৫৬-বিট এনক্রিপশনের মাধ্যমে সুরক্ষিত থাকে। বাণিজ্যিক বিজ্ঞাপনদাতার কাছে তথ্য বিক্রি বা বিনিময় সম্পূর্ণভাবে নিষিদ্ধ।</p>"
                + "<h3>৪. অ্যাকাউন্ট ও ডেটা স্থায়ীভাবে মুছে ফেলা (Data Deletion)</h3>"
                + "<p>ব্যবহারকারী যেকোনো সময় অ্যাপের প্রোফাইল সেটিংস থেকে অথবা privacy@deenone.top এ যোগাযোগ করে তার সংরক্ষিত সমস্ত ডেটা স্থায়ীভাবে মুছে ফেলতে পারেন।</p>";

        model.contentEn = "<p>DeenOne is committed to ensuring the maximum security, confidentiality, and integrity of users' personal information. As an authentic, distraction-free Islamic lifestyle application, we adhere to the highest privacy and ethical data protection standards.</p>"
                + "<h3>1. Information We Collect</h3>"
                + "<p>To deliver an authentic Islamic lifestyle experience and accurate prayer timings, DeenOne collects minimal necessary information:</p>"
                + "<ul>"
                + "<li><strong>Account & Profile:</strong> Name, phone number or email for account authentication, streak progress, and cloud backup across devices.</li>"
                + "<li><strong>Location Data:</strong> Accessed on-device strictly to calculate accurate prayer times, sunrise/sunset, and Qibla compass direction. Location coordinates are never sold or permanently tracked on our servers.</li>"
                + "<li><strong>Worship & Habits Activity:</strong> Daily Salah logs, Quran reading progress, and Tasbih counts stored with end-to-end security.</li>"
                + "<li><strong>Blood Donor Registry:</strong> Blood group, district, and contact numbers shared voluntarily for emergency blood requests.</li>"
                + "</ul>"
                + "<h3>2. Foreground & Media Services</h3>"
                + "<p>Android Foreground Media Services are utilized strictly to ensure uninterrupted Quran audio playback, sleep mode timer scheduling, and precise Adhan reminders.</p>"
                + "<h3>3. Data Security & Encryption</h3>"
                + "<p>All sensitive transmissions are secured using modern SSL/TLS 256-bit encryption. We never sell, rent, or trade user personal information to commercial advertisers.</p>"
                + "<h3>4. Data Retention & Permanent Deletion</h3>"
                + "<p>You have full ownership of your data. You may request permanent deletion of your account and all associated data at any time directly through app settings or by contacting privacy@deenone.top.</p>";

        return model;
    }
}
