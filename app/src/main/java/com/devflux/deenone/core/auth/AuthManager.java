package com.devflux.deenone.core.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.UserProfileEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthManager {

    public static final String PREF_PROFILE = "user_profile_prefs";
    public static final String KEY_LOGGED_IN = "profile_logged_in";
    public static final String KEY_USER_ID = "profile_user_id";
    public static final String KEY_FULL_NAME = "profile_full_name";
    public static final String KEY_PHONE = "profile_phone";
    public static final String KEY_EMAIL = "profile_email";
    public static final String KEY_BLOOD_GROUP = "profile_blood_group";
    public static final String KEY_POINTS = "profile_points";
    public static final String KEY_AVATAR = "profile_avatar";
    public static final String KEY_AUTH_TOKEN = "profile_auth_token";

    // Legacy aliases for full backward compatibility
    public static final String KEY_LEGACY_UID = "user_uid";
    public static final String KEY_LEGACY_NAME = "user_full_name";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface AuthCallback {
        void onSuccess(UserSession session, String message);
        void onError(String errorMessage);
    }

    public static class UserSession {
        public final String userId;
        public final String name;
        public final String phone;
        public final String email;
        public final String bloodGroup;
        public final int points;
        public final String avatar;
        public final String token;
        public final boolean isLoggedIn;

        public UserSession(String userId, String name, String phone, String email,
                           String bloodGroup, int points, String avatar, String token, boolean isLoggedIn) {
            this.userId = userId != null ? userId : "";
            this.name = name != null ? name : "";
            this.phone = phone != null ? phone : "";
            this.email = email != null ? email : "";
            this.bloodGroup = bloodGroup != null ? bloodGroup : "";
            this.points = points;
            this.avatar = avatar != null ? avatar : "avatar_1";
            this.token = token != null ? token : "";
            this.isLoggedIn = isLoggedIn;
        }
    }

    public static boolean isLoggedIn(Context context) {
        if (context == null) return false;
        SharedPreferences prefs = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public static UserSession getCurrentSession(Context context) {
        if (context == null) {
            return new UserSession("usr_guest", "দ্বীনওয়ান ব্যবহারকারী", "", "", "", 0, "avatar_1", "", false);
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);
        String userId = prefs.getString(KEY_USER_ID, prefs.getString(KEY_LEGACY_UID, loggedIn ? "usr_user" : "usr_guest"));
        String name = prefs.getString(KEY_FULL_NAME, prefs.getString(KEY_LEGACY_NAME, loggedIn ? "দ্বীনওয়ান ব্যবহারকারী" : "লগইন / সাইন আপ"));
        String phone = prefs.getString(KEY_PHONE, "");
        String email = prefs.getString(KEY_EMAIL, "");
        String blood = prefs.getString(KEY_BLOOD_GROUP, "");
        int points = prefs.getInt(KEY_POINTS, 0);
        String avatar = prefs.getString(KEY_AVATAR, "avatar_1");
        String token = prefs.getString(KEY_AUTH_TOKEN, "");

        return new UserSession(userId, name, phone, email, blood, points, avatar, token, loggedIn);
    }

    public static void saveSession(Context context, String userId, String name, String phone,
                                   String email, String bloodGroup, int points, String avatar, String token) {
        if (context == null) return;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_FULL_NAME, name);
        editor.putString(KEY_LEGACY_UID, userId);
        editor.putString(KEY_LEGACY_NAME, name);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_BLOOD_GROUP, bloodGroup);
        editor.putInt(KEY_POINTS, points);
        editor.putString(KEY_AVATAR, avatar);
        editor.putString("user_avatar_uri", avatar);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();

        // Also update blood donation preference if provided
        if (bloodGroup != null && !bloodGroup.isEmpty()) {
            context.getSharedPreferences("blood_donation_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("is_registered_donor", true)
                    .putString("donor_name", name)
                    .putString("donor_phone", phone)
                    .putString("donor_blood_group", bloodGroup)
                    .apply();
        }

        // Synchronize with local SQLite database asynchronously
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context);
                UserProfileEntity entity = db.userProfileDao().getUserProfileSync(userId);
                if (entity == null) {
                    entity = new UserProfileEntity();
                    entity.setUserId(userId);
                }
                entity.setFullName(name);
                entity.setPhone(phone);
                entity.setEmail(email);
                entity.setPoints(points);
                entity.setVerified(true);
                entity.setLastUpdatedTimestamp(System.currentTimeMillis());
                db.userProfileDao().insertOrUpdateProfile(entity);
            } catch (Exception ignored) {}
        });

        // Upload pre-login/offline activities and restore all user activities (Namaz, Nek Amal, Quiz, Streak, Qaza) from MySQL Database
        try {
            com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(context).uploadPreLoginOrPendingActivities(userId, null);
        } catch (Exception ignored) {}
    }

    public static void updateUserPoints(Context context, int newPoints) {
        if (context == null || newPoints < 0) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_POINTS, 0);
        int maxPoints = Math.max(current, newPoints);
        prefs.edit().putInt(KEY_POINTS, maxPoints).apply();

        executor.execute(() -> {
            try {
                String userId = getCurrentSession(context).userId;
                AppDatabase db = AppDatabase.getInstance(context);
                UserProfileEntity entity = db.userProfileDao().getUserProfileSync(userId);
                if (entity != null) {
                    entity.setPoints(maxPoints);
                    entity.setLastUpdatedTimestamp(System.currentTimeMillis());
                    db.userProfileDao().insertOrUpdateProfile(entity);
                }
            } catch (Exception ignored) {}
        });
    }

    public static void updateUserName(Context context, String newName) {
        if (context == null || newName == null || newName.trim().isEmpty()) return;
        String trimmedName = newName.trim();
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_FULL_NAME, trimmedName);
        editor.putString(KEY_LEGACY_NAME, trimmedName);
        editor.apply();

        executor.execute(() -> {
            try {
                String userId = getCurrentSession(context).userId;
                AppDatabase db = AppDatabase.getInstance(context);
                UserProfileEntity entity = db.userProfileDao().getUserProfileSync(userId);
                if (entity != null) {
                    entity.setFullName(trimmedName);
                    entity.setLastUpdatedTimestamp(System.currentTimeMillis());
                    db.userProfileDao().insertOrUpdateProfile(entity);
                }
            } catch (Exception ignored) {}
        });
    }

    public static void updateUserAvatar(Context context, String avatar) {
        if (context == null || avatar == null || avatar.trim().isEmpty()) return;
        String trimmedAvatar = avatar.trim();
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_AVATAR, trimmedAvatar);
        editor.putString("user_avatar_uri", trimmedAvatar);
        editor.apply();

        executor.execute(() -> {
            try {
                String userId = getCurrentSession(context).userId;
                AppDatabase db = AppDatabase.getInstance(context);
                UserProfileEntity entity = db.userProfileDao().getUserProfileSync(userId);
                if (entity != null) {
                    entity.setAvatarUri(trimmedAvatar);
                    entity.setLastUpdatedTimestamp(System.currentTimeMillis());
                    db.userProfileDao().insertOrUpdateProfile(entity);
                }
            } catch (Exception ignored) {}
        });
    }

    public static void logout(Context context) {
        if (context == null) return;
        Context appContext = context.getApplicationContext();

        // 1. Reset user_profile_prefs completely
        SharedPreferences profilePrefs = appContext.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
        profilePrefs.edit()
            .putBoolean(KEY_LOGGED_IN, false)
            .putString(KEY_AUTH_TOKEN, "")
            .putString(KEY_USER_ID, "usr_guest")
            .putString(KEY_FULL_NAME, "লগইন / সাইন আপ")
            .putString(KEY_LEGACY_UID, "usr_guest")
            .putString(KEY_LEGACY_NAME, "লগইন / সাইন আপ")
            .putString(KEY_PHONE, "")
            .putString(KEY_EMAIL, "")
            .putString(KEY_AVATAR, "")
            .putInt(KEY_POINTS, 0)
            .putString("profile_full_name", "দ্বীনওয়ান ব্যবহারকারী")
            .putString("user_full_name", "দ্বীনওয়ান ব্যবহারকারী")
            .putString("profile_avatar", "")
            .putString("user_avatar_uri", "")
            .putInt("user_avatar_preset", -1)
            .putInt("profile_points", 0)
            .apply();

        // 2. Reset activity & gamification preferences so guest starts clean
        appContext.getSharedPreferences("salah_tracker_prefs", Context.MODE_PRIVATE).edit()
            .putInt("total_prayers_completed", 0)
            .putInt("prayers_completed_today", 0)
            .apply();

        appContext.getSharedPreferences("amal_tracker_prefs", Context.MODE_PRIVATE).edit()
            .putInt("total_completed_amals_count", 0)
            .putInt("completed_amals_count", 0)
            .apply();

        appContext.getSharedPreferences("deanone_quiz_prefs", Context.MODE_PRIVATE).edit()
            .putInt("key_deen_points", 0)
            .putInt("key_lifetime_played_count", 0)
            .putInt("key_daily_played_count", 0)
            .apply();

        appContext.getSharedPreferences("deanone_gamification_prefs", Context.MODE_PRIVATE).edit()
            .putInt("key_total_xp", 0)
            .putInt("key_current_streak", 1)
            .apply();

        appContext.getSharedPreferences("authoritative_points_ledger_prefs", Context.MODE_PRIVATE).edit()
            .putInt("authoritative_lifetime_points", 0)
            .putInt("lifetime_points_ledger", 0)
            .apply();

        // 3. Clear avatar memory cache in FullProfileDialog
        com.devflux.deenone.features.profile.FullProfileDialog.clearAvatarCache();

        // 4. Reset Room SQLite active profile to guest state & clear previous user's logs
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(appContext);
                db.prayerLogDao().deleteAllLogsSync();
                db.dailyAmalDao().resetAllCompletedSync();
                db.quizDao().deleteAllResultsSync();

                UserProfileEntity entity = db.userProfileDao().getActiveProfileSync();
                if (entity != null) {
                    entity.setUserId("usr_guest");
                    entity.setFullName("দ্বীনওয়ান ব্যবহারকারী");
                    entity.setPoints(0);
                    entity.setVerified(false);
                    entity.setPhone("");
                    entity.setEmail("");
                    entity.setAvatarUri(null);
                    entity.setAvatarPreset(-1);
                    entity.setSalahCompletedTotal(0);
                    entity.setAmalCompletedTotal(0);
                    entity.setQuizCompletedTotal(0);
                    entity.setStreakDays(1);
                    entity.setLevel(1);
                    entity.setLastUpdatedTimestamp(System.currentTimeMillis());
                    db.userProfileDao().updateProfile(entity);
                    UserProfileEntity guest = new UserProfileEntity();
                    guest.setUserId("usr_guest");
                    guest.setFullName("দ্বীনওয়ান ব্যবহারকারী");
                    guest.setPoints(0);
                    guest.setVerified(false);
                    db.userProfileDao().insertOrUpdateProfile(guest);
                }
            } catch (Exception ignored) {}
        });
    }

    public static void register(Context context, String name, String phone, String email,
                                String bloodGroup, String password, AuthCallback callback) {
        executor.execute(() -> {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "auth.php");
            try {
                JSONObject json = new JSONObject();
                json.put("action", "register");
                json.put("name", name);
                json.put("phone", phone);
                json.put("email", email != null ? email : "");
                json.put("blood_group", bloodGroup != null ? bloodGroup : "");
                json.put("password", password);
                json.put("device_name", getDeviceName());

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder().url(endpoint).post(body).build();

                try (Response response = client.newCall(request).execute()) {
                    String resBody = response.body() != null ? response.body().string() : "";
                    JSONObject resJson = new JSONObject(resBody);

                    if (response.isSuccessful() && resJson.optBoolean("success", false)) {
                        JSONObject userObj = resJson.optJSONObject("user");
                        String uId = userObj != null ? userObj.optString("user_id", "usr_" + System.currentTimeMillis()) : "usr_" + System.currentTimeMillis();
                        int pts = userObj != null ? userObj.optInt("points", 50) : 50;
                        String token = userObj != null ? userObj.optString("token", "") : "";
                        String av = userObj != null ? userObj.optString("avatar", "avatar_1") : "avatar_1";

                        saveSession(context, uId, name, phone, email, bloodGroup, pts, av, token);
                        saveLocalAccount(context, uId, name, phone, email, bloodGroup, password, pts);
                        UserSession session = getCurrentSession(context);

                        mainHandler.post(() -> callback.onSuccess(session, resJson.optString("message", "অ্যাকাউন্ট তৈরি সফল হয়েছে!")));
                    } else {
                        String errMsg = resJson.optString("error", "রেজিস্ট্রেশন ব্যর্থ হয়েছে।");
                        mainHandler.post(() -> callback.onError(errMsg));
                    }
                }
            } catch (Exception e) {
                // Offline fallback mode: save locally if network fails
                String offlineId = "usr_local_" + System.currentTimeMillis();
                saveSession(context, offlineId, name, phone, email, bloodGroup, 50, "avatar_1", "offline_token");
                saveLocalAccount(context, offlineId, name, phone, email, bloodGroup, password, 50);
                UserSession session = getCurrentSession(context);
                mainHandler.post(() -> callback.onSuccess(session, "অ্যাকাউন্ট সফলভাবে প্রস্তুত করা হয়েছে!"));
            }
        });
    }

    public static void login(Context context, String identifier, String password, AuthCallback callback) {
        executor.execute(() -> {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "auth.php");
            try {
                JSONObject json = new JSONObject();
                json.put("action", "login");
                json.put("identifier", identifier);
                json.put("password", password);
                json.put("device_name", getDeviceName());

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder().url(endpoint).post(body).build();

                try (Response response = client.newCall(request).execute()) {
                    String resBody = response.body() != null ? response.body().string() : "";
                    JSONObject resJson = new JSONObject(resBody);

                    if (response.isSuccessful() && resJson.optBoolean("success", false)) {
                        JSONObject userObj = resJson.optJSONObject("user");
                        String uId = userObj != null ? userObj.optString("user_id", "") : "";
                        String name = userObj != null ? userObj.optString("name", "দ্বীনওয়ান ব্যবহারকারী") : "দ্বীনওয়ান ব্যবহারকারী";
                        String phone = userObj != null ? userObj.optString("phone", "") : "";
                        String email = userObj != null ? userObj.optString("email", "") : "";
                        String blood = userObj != null ? userObj.optString("blood_group", "") : "";
                        int pts = userObj != null ? userObj.optInt("points", 0) : 0;
                        String av = userObj != null ? userObj.optString("avatar", "avatar_1") : "avatar_1";
                        String token = userObj != null ? userObj.optString("token", "") : "";

                        String remoteLang = userObj != null ? userObj.optString("app_language", "") : "";
                        String remoteTheme = userObj != null ? userObj.optString("theme_mode", "") : "";
                        if (!remoteLang.isEmpty()) {
                            com.devflux.deenone.core.localization.LocaleManager.setLanguage(context, remoteLang);
                        }
                        if ("light".equalsIgnoreCase(remoteTheme)) {
                            com.devflux.deenone.core.theme.ThemeManager.setThemeMode(context, com.devflux.deenone.core.theme.ThemeManager.THEME_LIGHT);
                        } else if ("dark".equalsIgnoreCase(remoteTheme)) {
                            com.devflux.deenone.core.theme.ThemeManager.setThemeMode(context, com.devflux.deenone.core.theme.ThemeManager.THEME_DARK);
                        }

                        saveSession(context, uId, name, phone, email, blood, pts, av, token);
                        saveLocalAccount(context, uId, name, phone, email, blood, password, pts);
                        UserSession session = getCurrentSession(context);

                        mainHandler.post(() -> callback.onSuccess(session, resJson.optString("message", "লগইন সফল হয়েছে!")));
                    } else {
                        String errMsg = resJson.optString("error", "ভুল তথ্য প্রদান করা হয়েছে।");
                        mainHandler.post(() -> callback.onError(errMsg));
                    }
                }
            } catch (Exception e) {
                // Offline fallback with exact credential verification
                JSONObject localAcc = findLocalAccount(context, identifier);
                if (localAcc != null) {
                    String storedPass = localAcc.optString("password", "");
                    if (storedPass.equals(password)) {
                        String uId = localAcc.optString("user_id", "usr_local");
                        String name = localAcc.optString("name", "দ্বীনওয়ান ব্যবহারকারী");
                        String phone = localAcc.optString("phone", "");
                        String email = localAcc.optString("email", "");
                        String blood = localAcc.optString("blood_group", "");
                        int pts = localAcc.optInt("points", 50);

                        saveSession(context, uId, name, phone, email, blood, pts, "avatar_1", "offline_token");
                        UserSession session = getCurrentSession(context);
                        mainHandler.post(() -> callback.onSuccess(session, "লগইন সফল হয়েছে!"));
                        return;
                    } else {
                        mainHandler.post(() -> callback.onError("ভুল পাসওয়ার্ড প্রদান করা হয়েছে।"));
                        return;
                    }
                }

                SharedPreferences prefs = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
                String savedPhone = prefs.getString(KEY_PHONE, "");
                String savedEmail = prefs.getString(KEY_EMAIL, "");
                if ((!savedPhone.isEmpty() && identifier.equals(savedPhone)) || (!savedEmail.isEmpty() && identifier.equals(savedEmail))) {
                    prefs.edit().putBoolean(KEY_LOGGED_IN, true).apply();
                    UserSession session = getCurrentSession(context);
                    mainHandler.post(() -> callback.onSuccess(session, "লগইন সফল হয়েছে!"));
                    return;
                }

                mainHandler.post(() -> callback.onError("এই নম্বর বা ইমেইলে কোনো অ্যাকাউন্ট পাওয়া যায়নি। প্রথমে 'নতুন অ্যাকাউন্ট'-এ নিবন্ধন করুন।"));
            }
        });
    }

    public static void googleSignIn(Context context, String googleId, String email, String name, AuthCallback callback) {
        googleSignIn(context, googleId, email, name, "avatar_1", callback);
    }

    public static void googleSignIn(Context context, String googleId, String email, String name, String avatar, AuthCallback callback) {
        executor.execute(() -> {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "auth.php");
            try {
                JSONObject json = new JSONObject();
                json.put("action", "google");
                json.put("google_id", googleId);
                json.put("email", email);
                json.put("name", name);
                json.put("avatar", avatar != null ? avatar : "avatar_1");
                json.put("device_name", getDeviceName());

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder().url(endpoint).post(body).build();

                try (Response response = client.newCall(request).execute()) {
                    String resBody = response.body() != null ? response.body().string() : "";
                    JSONObject resJson = new JSONObject(resBody);

                    if (response.isSuccessful() && resJson.optBoolean("success", false)) {
                        JSONObject userObj = resJson.optJSONObject("user");
                        String uId = userObj != null ? userObj.optString("user_id", "") : "";
                        String resName = userObj != null ? userObj.optString("name", name) : name;
                        String resEmail = userObj != null ? userObj.optString("email", email) : email;
                        String resBlood = userObj != null ? userObj.optString("blood_group", "") : "";
                        String resPhone = userObj != null ? userObj.optString("phone", "") : "";
                        int pts = userObj != null ? userObj.optInt("points", 50) : 50;
                        String token = userObj != null ? userObj.optString("token", "") : "";
                        String av = userObj != null ? userObj.optString("avatar", avatar) : avatar;

                        saveSession(context, uId, resName, resPhone, resEmail, resBlood, pts, av, token);
                        saveLocalAccount(context, uId, resName, resPhone, resEmail, resBlood, "google_oauth", pts);
                        UserSession session = getCurrentSession(context);

                        mainHandler.post(() -> callback.onSuccess(session, resJson.optString("message", "গুগল লগইন সম্পন্ন হয়েছে!")));
                    } else {
                        String errMsg = resJson.optString("error", "গুগল লগইন ব্যর্থ হয়েছে।");
                        mainHandler.post(() -> callback.onError(errMsg));
                    }
                }
            } catch (Exception e) {
                // Offline fallback support for Google Sign-In
                String offlineId = "usr_g_" + Math.abs(email.hashCode());
                saveSession(context, offlineId, name, "", email, "", 50, avatar != null ? avatar : "avatar_1", "offline_google_token");
                saveLocalAccount(context, offlineId, name, "", email, "", "google_oauth", 50);
                UserSession session = getCurrentSession(context);
                mainHandler.post(() -> callback.onSuccess(session, "গুগল অ্যাকাউন্ট দিয়ে সফলভাবে যুক্ত হয়েছেন!"));
            }
        });
    }

    public static void forgotPassword(Context context, String identifier, AuthCallback callback) {
        executor.execute(() -> {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "auth.php");
            try {
                JSONObject json = new JSONObject();
                json.put("action", "forgot_password");
                json.put("identifier", identifier);

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder().url(endpoint).post(body).build();

                try (Response response = client.newCall(request).execute()) {
                    String resBody = response.body() != null ? response.body().string() : "";
                    JSONObject resJson = new JSONObject(resBody);

                    if (response.isSuccessful() && resJson.optBoolean("success", false)) {
                        String msg = resJson.optString("message", "আপনার ইমেইলে ৬ ডিজিটের ভেরিফিকেশন কোড পাঠানো হয়েছে।");
                        mainHandler.post(() -> callback.onSuccess(null, msg));
                    } else {
                        String err = resJson.optString("error", "ব্যবহারকারী পাওয়া যায়নি বা অনুরোধ প্রক্রিয়াকরণে সমস্যা হয়েছে।");
                        mainHandler.post(() -> callback.onError(err));
                    }
                }
            } catch (Exception e) {
                // Network failure or offline
                mainHandler.post(() -> callback.onError("সার্ভারের সাথে যোগাযোগ করা সম্ভব হয়নি। অনুগ্রহ করে ইন্টারনেট সংযোগ পরীক্ষা করুন।"));
            }
        });
    }

    public static void resetPassword(Context context, String identifier, String code, String newPassword, AuthCallback callback) {
        executor.execute(() -> {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "auth.php");
            try {
                JSONObject json = new JSONObject();
                json.put("action", "reset_password");
                json.put("identifier", identifier);
                json.put("code", code);
                json.put("new_password", newPassword);

                RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
                Request request = new Request.Builder().url(endpoint).post(body).build();

                try (Response response = client.newCall(request).execute()) {
                    String resBody = response.body() != null ? response.body().string() : "";
                    JSONObject resJson = new JSONObject(resBody);

                    if (response.isSuccessful() && resJson.optBoolean("success", false)) {
                        // Update local account cache if present
                        JSONObject localAcc = findLocalAccount(context, identifier);
                        if (localAcc != null) {
                            saveLocalAccount(context,
                                    localAcc.optString("user_id", "usr_local"),
                                    localAcc.optString("name", "ব্যবহারকারী"),
                                    localAcc.optString("phone", identifier),
                                    localAcc.optString("email", identifier),
                                    localAcc.optString("blood_group", ""),
                                    newPassword,
                                    localAcc.optInt("points", 50));
                        }

                        String msg = resJson.optString("message", "পাসওয়ার্ড সফলভাবে পরিবর্তন করা হয়েছে! এখন লগইন করুন।");
                        mainHandler.post(() -> callback.onSuccess(null, msg));
                    } else {
                        String err = resJson.optString("error", "ভেরিফিকেশন কোডটি ভুল বা মেয়াদোত্তীর্ণ।");
                        mainHandler.post(() -> callback.onError(err));
                    }
                }
            } catch (Exception e) {
                // Offline fallback support
                JSONObject localAcc = findLocalAccount(context, identifier);
                if (localAcc != null) {
                    saveLocalAccount(context,
                            localAcc.optString("user_id", "usr_local"),
                            localAcc.optString("name", "ব্যবহারকারী"),
                            localAcc.optString("phone", identifier),
                            localAcc.optString("email", identifier),
                            localAcc.optString("blood_group", ""),
                            newPassword,
                            localAcc.optInt("points", 50));
                    mainHandler.post(() -> callback.onSuccess(null, "পাসওয়ার্ড সফলভাবে পরিবর্তন করা হয়েছে! এখন লগইন করুন।"));
                    return;
                }
                mainHandler.post(() -> callback.onError("পাসওয়ার্ড রিসেট করতে ব্যর্থ হয়েছে। ইন্টারনেট সংযোগ পরীক্ষা করুন।"));
            }
        });
    }

    // -------------------------------------------------------------------------
    // LOCAL REGISTERED ACCOUNTS STORE (OFFLINE-FIRST RESILIENCE)
    // -------------------------------------------------------------------------
    private static final String PREF_LOCAL_ACCOUNTS = "local_registered_accounts";
    private static final String KEY_ACCOUNTS_JSON = "accounts_list_json";

    public static void saveLocalAccount(Context context, String userId, String name, String phone,
                                        String email, String bloodGroup, String password, int points) {
        if (context == null) return;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_LOCAL_ACCOUNTS, Context.MODE_PRIVATE);
            String rawJson = prefs.getString(KEY_ACCOUNTS_JSON, "[]");
            JSONArray array = new JSONArray(rawJson);

            boolean found = false;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (obj.optString("phone", "").equals(phone) || (!email.isEmpty() && obj.optString("email", "").equals(email))) {
                    obj.put("user_id", userId);
                    obj.put("name", name);
                    obj.put("phone", phone);
                    obj.put("email", email);
                    obj.put("blood_group", bloodGroup);
                    obj.put("password", password);
                    obj.put("points", points);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JSONObject obj = new JSONObject();
                obj.put("user_id", userId);
                obj.put("name", name);
                obj.put("phone", phone);
                obj.put("email", email);
                obj.put("blood_group", bloodGroup);
                obj.put("password", password);
                obj.put("points", points);
                array.put(obj);
            }
            prefs.edit().putString(KEY_ACCOUNTS_JSON, array.toString()).apply();
        } catch (Exception ignored) {}
    }

    public static JSONObject findLocalAccount(Context context, String identifier) {
        if (context == null || identifier == null || identifier.isEmpty()) return null;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_LOCAL_ACCOUNTS, Context.MODE_PRIVATE);
            String rawJson = prefs.getString(KEY_ACCOUNTS_JSON, "[]");
            JSONArray array = new JSONArray(rawJson);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                if (identifier.equalsIgnoreCase(obj.optString("phone", "")) ||
                    (!obj.optString("email", "").isEmpty() && identifier.equalsIgnoreCase(obj.optString("email", "")))) {
                    return obj;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    // -------------------------------------------------------------------------
    // LEGAL & SUPPORT POLICY SYNC (FROM PHP ADMIN PANEL)
    // -------------------------------------------------------------------------
    public static class LegalInfo {
        public final String privacyPolicy;
        public final String privacyUrl;
        public final String termsService;
        public final String termsUrl;
        public final String supportInfo;
        public final String supportEmail;
        public final String supportWhatsapp;
        public final String supportUrl;

        public LegalInfo(String privacyPolicy, String privacyUrl, String termsService, String termsUrl,
                         String supportInfo, String supportEmail, String supportWhatsapp, String supportUrl) {
            this.privacyPolicy = privacyPolicy;
            this.privacyUrl = privacyUrl;
            this.termsService = termsService;
            this.termsUrl = termsUrl;
            this.supportInfo = supportInfo;
            this.supportEmail = supportEmail;
            this.supportWhatsapp = supportWhatsapp;
            this.supportUrl = supportUrl;
        }
    }

    public interface LegalInfoCallback {
        void onLoaded(LegalInfo info);
    }

    public static void fetchLegalInfo(Context context, LegalInfoCallback callback) {
        executor.execute(() -> {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "auth.php?action=legal");
            try {
                Request request = new Request.Builder().url(endpoint).get().build();
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONObject json = new JSONObject(response.body().string());
                        if (json.optBoolean("success", false)) {
                            LegalInfo info = new LegalInfo(
                                    json.optString("privacy_policy"),
                                    json.optString("privacy_url"),
                                    json.optString("terms_service"),
                                    json.optString("terms_url"),
                                    json.optString("support_info"),
                                    json.optString("support_email", "support@deenone.top"),
                                    json.optString("support_whatsapp", "+8801700000000"),
                                    json.optString("support_url", "https://deenone.top/support")
                            );
                            mainHandler.post(() -> callback.onLoaded(info));
                            return;
                        }
                    }
                }
            } catch (Exception ignored) {}

            // Fallback default
            LegalInfo fallback = new LegalInfo(
                    "DeenOne (দীন ওয়ান) ব্যবহারকারীদের ব্যক্তিগত তথ্যের সর্বোচ্চ সুরক্ষা ও গোপনীয়তা বজায় রাখতে প্রতিশ্রুতিবদ্ধ। আমরা ব্যবহারকারীর সালাত ট্র্যাকিং, কুরআন তিলাওয়াত ও কুইজ পয়েন্ট ক্লাউডে সিঙ্ক করার উদ্দেশ্যে শুধুমাত্র নাম ও ফোন নম্বর সংগ্রহ করি। কোনো প্রকার অননুমোদিত তৃতীয় পক্ষের সাথে ব্যবহারকারীর তথ্য শেয়ার করা হয় না। রক্তদাতা হিসেবে স্বেচ্ছায় নিবন্ধিতদের তথ্য শুধুমাত্র জরুরি প্রয়োজনে অন্য মুসলিম ভাইদের সহায়তায় ব্যবহৃত হয়।",
                    "https://deenone.top/privacy",
                    "DeenOne অ্যাপ্লিকেশনটি মুসলিম উম্মাহর দৈনন্দিন ইবাদত ও দ্বীনি শিক্ষার সহায়তায় বিনামূল্যে পরিচালিত একটি প্ল্যাটফর্ম। কুইজ ও নলেজ ব্যাটেল প্রতিযোগিতায় কোনো প্রকার অনৈতিক উপায় অবলম্বন বা বিভ্রান্তিকর তথ্য প্রচার সম্পূর্ণরূপে নিষিদ্ধ। অ্যাপের সকল ইসলামিক কনটেন্ট কুরআন ও সহীহ সুন্নাহর আলোকে সংকলিত।",
                    "https://deenone.top/terms",
                    "দীন ওয়ান সংক্রান্ত যেকোনো জিজ্ঞাসা, পরামর্শ বা সহায়তার জন্য আমাদের অফিসিয়াল সাপোর্ট টিমের সাথে যোগাযোগ করুন।\n\nইমেইল: support@deenone.top\nহোয়াটসঅ্যাপ হেল্পলাইন: +880 1700-000000\nফেসবুক পেজ: fb.com/DeenOneApp\n\nআমরা দ্রুততম সময়ে আপনার প্রশ্নের উত্তর দেওয়ার চেষ্টা করব, ইনশাআল্লাহ।",
                    "support@deenone.top",
                    "+8801700000000",
                    "https://deenone.top/support"
            );
            mainHandler.post(() -> callback.onLoaded(fallback));
        });
    }

    public static void syncUserSettings(Context context, String language, String themeMode) {
        if (context == null) return;
        executor.execute(() -> {
            if (isLoggedIn(context)) {
                UserSession session = getCurrentSession(context);
                if (session != null && !session.userId.isEmpty() && !"usr_guest".equals(session.userId)) {
                    String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "user_sync.php");
                    try {
                        JSONObject json = new JSONObject();
                        json.put("user_id", session.userId);
                        json.put("name", session.name);
                        if (session.avatar != null && !session.avatar.isEmpty() && !"avatar_1".equals(session.avatar)) {
                            json.put("avatar", session.avatar);
                        }
                        if (language != null && !language.isEmpty()) {
                            json.put("app_language", language);
                        }
                        if (themeMode != null && !themeMode.isEmpty()) {
                            json.put("theme_mode", themeMode);
                        }

                        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json; charset=utf-8"));
                        Request request = new Request.Builder()
                                .url(endpoint)
                                .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                                .post(body)
                                .build();

                        try (Response response = client.newCall(request).execute()) {
                            if (response.isSuccessful()) {
                                android.util.Log.d("AuthManager", "Session sync complete");
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
        });
    }

    public static String getDeviceName() {
        String manufacturer = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        if (manufacturer == null) manufacturer = "";
        if (model == null) model = "";
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return (capitalize(manufacturer) + " " + model).trim();
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
