package com.devflux.deenone.features.about;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AboutUsManager {

    private static final String PREF_NAME = "deanone_about_us_cache";
    private static final String KEY_ABOUT_JSON = "cached_about_json";
    private static final String KEY_TEAM_JSON = "cached_team_json";

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final OkHttpClient httpClient = new OkHttpClient();

    public interface OnAboutDataLoadedListener {
        void onLoaded(AboutUsModel.AboutInfo aboutInfo, List<AboutUsModel.TeamMember> teamMembers);
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static AboutUsModel.AboutInfo getCachedAboutInfo(Context context) {
        String json = getPrefs(context).getString(KEY_ABOUT_JSON, null);
        if (json != null && !json.trim().isEmpty()) {
            try {
                JSONObject obj = new JSONObject(json);
                AboutUsModel.AboutInfo info = new AboutUsModel.AboutInfo();
                info.appName = obj.optString("app_name", "DeenOne");
                info.taglineBn = obj.optString("tagline_bn", info.taglineBn);
                info.taglineEn = obj.optString("tagline_en", info.taglineEn);
                info.appVersion = obj.optString("app_version", info.appVersion);
                info.versionBadgeBn = obj.optString("version_badge_bn", info.versionBadgeBn);
                info.versionBadgeEn = obj.optString("version_badge_en", info.versionBadgeEn);
                info.missionTitleBn = obj.optString("mission_title_bn", info.missionTitleBn);
                info.missionTitleEn = obj.optString("mission_title_en", info.missionTitleEn);
                info.missionDescBn = obj.optString("mission_desc_bn", info.missionDescBn);
                info.missionDescEn = obj.optString("mission_desc_en", info.missionDescEn);
                info.quoteTextBn = obj.optString("quote_text_bn", info.quoteTextBn);
                info.quoteTextEn = obj.optString("quote_text_en", info.quoteTextEn);
                info.copyrightBn = obj.optString("copyright_bn", info.copyrightBn);
                info.copyrightEn = obj.optString("copyright_en", info.copyrightEn);

                JSONArray pillsArr = obj.optJSONArray("pills");
                if (pillsArr != null && pillsArr.length() > 0) {
                    info.pills.clear();
                    for (int i = 0; i < pillsArr.length(); i++) {
                        info.pills.add(pillsArr.optString(i));
                    }
                }
                return info;
            } catch (Exception ignored) {}
        }
        return new AboutUsModel.AboutInfo();
    }

    public static List<AboutUsModel.TeamMember> getCachedTeamMembers(Context context) {
        String json = getPrefs(context).getString(KEY_TEAM_JSON, null);
        List<AboutUsModel.TeamMember> list = new ArrayList<>();
        if (json != null && !json.trim().isEmpty()) {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    AboutUsModel.TeamMember m = new AboutUsModel.TeamMember();
                    m.id = obj.optInt("id", 1);
                    m.nameBn = obj.optString("name_bn", "রিয়াদ মনির");
                    m.nameEn = obj.optString("name_en", "Riad Monir");
                    m.roleBn = obj.optString("role_bn", "প্রতিষ্ঠাতা ও প্রধান ডেভেলপার");
                    m.roleEn = obj.optString("role_en", "Founder & Lead Developer");
                    m.avatarUrl = obj.optString("avatar_url", "");
                    m.githubUrl = obj.optString("github_url", "https://github.com/riadmonir");
                    m.linkedinUrl = obj.optString("linkedin_url", "https://linkedin.com/in/riadmonir");
                    m.websiteUrl = obj.optString("website_url", "https://deenone.top");
                    m.email = obj.optString("email", "support@deenone.top");
                    m.displayOrder = obj.optInt("display_order", 1);
                    m.isActive = obj.optInt("is_active", 1) == 1;
                    if (m.isActive) {
                        list.add(m);
                    }
                }
            } catch (Exception ignored) {}
        }

        if (list.isEmpty()) {
            list.add(new AboutUsModel.TeamMember());
        }
        return list;
    }

    public static void syncRemoteData(Context context, OnAboutDataLoadedListener listener) {
        if (context == null) return;
        Context appContext = context.getApplicationContext();

        String endpoint = BackendConfigManager.getPhpApiEndpoint(appContext, "about_us.php");

        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(appContext))
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Fallback to cache
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) return;
                try {
                    String bodyStr = response.body().string();
                    JSONObject root = new JSONObject(bodyStr);
                    if (root.optBoolean("success", false)) {
                        JSONObject aboutObj = root.optJSONObject("about");
                        JSONArray teamArr = root.optJSONArray("team");

                        SharedPreferences.Editor editor = getPrefs(appContext).edit();
                        if (aboutObj != null) {
                            editor.putString(KEY_ABOUT_JSON, aboutObj.toString());
                        }
                        if (teamArr != null) {
                            editor.putString(KEY_TEAM_JSON, teamArr.toString());
                        }
                        editor.apply();

                        if (listener != null) {
                            AboutUsModel.AboutInfo updatedInfo = getCachedAboutInfo(appContext);
                            List<AboutUsModel.TeamMember> updatedTeam = getCachedTeamMembers(appContext);
                            mainHandler.post(() -> listener.onLoaded(updatedInfo, updatedTeam));
                        }
                    }
                } catch (Exception ignored) {}
            }
        });
    }
}
