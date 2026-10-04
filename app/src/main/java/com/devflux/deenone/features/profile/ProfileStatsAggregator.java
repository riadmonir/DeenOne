package com.devflux.deenone.features.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.devflux.deenone.core.backend.BackendRepository;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.core.quiz.QuizManager;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.DailyAmalDao;
import com.devflux.deenone.data.local.dao.PrayerLogDao;
import com.devflux.deenone.data.local.dao.QuizDao;
import com.devflux.deenone.data.local.dao.UserProfileDao;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.features.community.data.CommunityRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Single Source of Truth Profile Statistics Aggregator.
 * Reliably computes and synchronizes Post, Comment, Like, Prayer, Amal, Quiz,
 * and Points directly from the authoritative source databases.
 */
public class ProfileStatsAggregator {

    private static final String TAG = "ProfileStatsAggregator";

    public interface OnStatsCalculatedListener {
        void onStatsCalculated(UserProfileEntity updatedProfile, CommunityRepository.SocialStats socialStats);
    }

    /**
     * Executes authoritative aggregation of all 7 statistics from source data.
     */
    public static void refreshAndSyncAllStatistics(@NonNull Context context, String userId, OnStatsCalculatedListener callback) {
        Context appContext = context.getApplicationContext();
        if (!com.devflux.deenone.core.auth.AuthManager.isLoggedIn(appContext)) {
            if (callback != null) {
                callback.onStatsCalculated(null, null);
            }
            return;
        }
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(appContext);
                UserProfileDao userProfileDao = db.userProfileDao();
                PrayerLogDao prayerLogDao = db.prayerLogDao();
                DailyAmalDao dailyAmalDao = db.dailyAmalDao();
                QuizDao quizDao = db.quizDao();

                String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());

                // 1. Authoritative Lifetime Prayer Count (Only on-time/performed 5 Fard Waqts, excluding Qaza and Nafl)
                int lifetimePrayers = prayerLogDao.getTotalPrayedCountSync();

                // 2. Authoritative Lifetime Completed Amal Count
                int lifetimeAmals = dailyAmalDao.getTotalCompletedCountSync();

                // 3. Authoritative Quiz Count (Only Correct/Won Quizzes)
                int totalQuizzes = quizDao.getTotalQuizzesPlayedCountSync();

                // 4. Authoritative Social Stats Aggregation (Single Source of Truth: CommunityRepository)
                CommunityRepository communityRepo = CommunityRepository.getInstance(appContext);
                CommunityRepository.SocialStats socialStats = communityRepo.calculateUserSocialStats();

                // 5. Authoritative Points & Gamification Calculation
                int gamificationXP = GamificationManager.getTotalXP(appContext);
                int quizPoints = QuizManager.getInstance().getDeenPoints(appContext);
                int streakDays = GamificationManager.getStreakDays(appContext);
                int ledgerPoints = com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.getLifetimePoints(appContext);
                int sessionPoints = 0;
                if (com.devflux.deenone.core.auth.AuthManager.isLoggedIn(appContext)) {
                    com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(appContext);
                    if (session != null) sessionPoints = session.points;
                }
                SharedPreferences profilePrefs = appContext.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
                int prefPoints = profilePrefs.getInt("profile_points", 0);

                int computedPoints = Math.max(sessionPoints, Math.max(prefPoints, Math.max(ledgerPoints, Math.max(gamificationXP, quizPoints))));

                // 6. Update Denormalized / Cached Room SQLite User Profile Entity
                UserProfileEntity profile = userProfileDao.getUserProfileSync(userId);
                if (profile != null) {
                    if (profile.getPoints() > computedPoints) computedPoints = profile.getPoints();
                }

                if (computedPoints > 0) {
                    com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.syncInitialPoints(appContext, computedPoints);
                    GamificationManager.setTotalXP(appContext, computedPoints);
                    profilePrefs.edit().putInt("profile_points", computedPoints).apply();
                }
                long now = System.currentTimeMillis();
                boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(appContext);

                String currentCity = com.devflux.deenone.core.location.LocationProvider.formatCityNameOnly(
                        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(appContext).locationName, isBn
                );

                if (profile == null) {
                    String name;
                    String email = "";
                    String phone = "";
                    long joinTime = now;

                    if (com.devflux.deenone.core.auth.AuthManager.isLoggedIn(appContext)) {
                        com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(appContext);
                        name = (session != null && session.name != null && !session.name.trim().isEmpty()) ? session.name : (isBn ? "দ্বীনওয়ান ব্যবহারকারী" : "DeenOne User");
                        email = (session != null && session.email != null) ? session.email : "";
                        phone = (session != null && session.phone != null) ? session.phone : "";
                    } else {
                        SharedPreferences userPrefs = appContext.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
                        name = userPrefs.getString("user_full_name", isBn ? "দ্বীনওয়ান ব্যবহারকারী" : "DeenOne User");
                        try {
                            joinTime = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0).firstInstallTime;
                        } catch (Exception ignored) {
                            joinTime = now;
                        }
                    }

                    profile = new UserProfileEntity(
                            userId, name, email, phone,
                            currentCity, joinTime, computedPoints,
                            GamificationManager.getLevel(appContext), streakDays, true,
                            null, 0, lifetimePrayers, lifetimeAmals, totalQuizzes, now
                    );
                } else {
                    if (com.devflux.deenone.core.auth.AuthManager.isLoggedIn(appContext)) {
                        com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(appContext);
                        if (session != null && session.name != null && !session.name.trim().isEmpty()) {
                            profile.setFullName(session.name);
                        }
                        if (session != null && session.email != null && !session.email.trim().isEmpty()) {
                            profile.setEmail(session.email);
                        }
                        if (session != null && session.phone != null && !session.phone.trim().isEmpty()) {
                            profile.setPhone(session.phone);
                        }
                    }
                    profile.setTimezone(currentCity);
                    profile.setSalahCompletedTotal(lifetimePrayers);
                    profile.setAmalCompletedTotal(lifetimeAmals);
                    profile.setQuizCompletedTotal(totalQuizzes);
                    profile.setPoints(computedPoints);
                    profile.setStreakDays(streakDays);
                    profile.setLevel(GamificationManager.getLevel(appContext));
                    profile.setLastUpdatedTimestamp(now);
                }

                userProfileDao.insertOrUpdateProfile(profile);

                // Notify Community LiveData
                communityRepo.recalculateAndNotifyStats();

                // Sync with remote backend repository
                BackendRepository.getInstance(appContext).submitUserPoints(userId, profile.getFullName(), computedPoints, null);

                if (callback != null) {
                    final UserProfileEntity fProfile = profile;
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        callback.onStatsCalculated(fProfile, socialStats);
                    });
                }

                Log.d(TAG, "Successfully aggregated and synced all 7 Profile statistics.");
            } catch (Exception e) {
                Log.e(TAG, "Error calculating authoritative profile statistics", e);
            }
        });
    }
}
