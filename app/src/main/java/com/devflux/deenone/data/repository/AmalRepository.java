package com.devflux.deenone.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.core.amal.DailyAmalManager;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.core.quiz.QuizManager;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.AmalRecordDao;
import com.devflux.deenone.data.local.dao.DailyAmalDao;
import com.devflux.deenone.data.local.dao.PrayerLogDao;
import com.devflux.deenone.data.local.dao.QuizDao;
import com.devflux.deenone.data.local.entity.AmalRecordEntity;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.domain.repository.IAmalRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AmalRepository implements IAmalRepository {

    private final Context context;
    private final AmalRecordDao amalRecordDao;
    private final DailyAmalDao dailyAmalDao;
    private final PrayerLogDao prayerLogDao;
    private final QuizDao quizDao;

    public AmalRepository(Context context) {
        this.context = context.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        this.amalRecordDao = db.amalRecordDao();
        this.dailyAmalDao = db.dailyAmalDao();
        this.prayerLogDao = db.prayerLogDao();
        this.quizDao = db.quizDao();
        DailyAmalManager.ensureTodayAmals(this.context);
        recalculateAndSyncTodayRecord();
    }

    public LiveData<List<DailyAmalEntity>> getTodayAmals() {
        String today = DailyAmalManager.getTodayDateString();
        DailyAmalManager.ensureTodayAmals(context);
        return dailyAmalDao.getAmalsByDate(today);
    }

    public LiveData<Integer> getTodayCompletedCount() {
        String today = DailyAmalManager.getTodayDateString();
        return dailyAmalDao.getCompletedCountByDate(today);
    }

    public LiveData<Integer> getTodayTotalCount() {
        String today = DailyAmalManager.getTodayDateString();
        return dailyAmalDao.getTotalCountByDate(today);
    }

    public LiveData<Integer> getEarnedPointsToday() {
        String today = DailyAmalManager.getTodayDateString();
        return dailyAmalDao.getEarnedPointsByDate(today);
    }

    public void completeAmal(DailyAmalEntity amal) {
        if (amal == null || amal.isCompleted()) return;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            amal.setCompleted(true);
            dailyAmalDao.setCompletionStatus(amal.getId(), true);
            com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.recordAmalCompletion(
                    context, "user_main", amal.getAmalCode(), amal.getPoints());
            recalculateAndSyncTodayRecord();
            try {
                android.content.SharedPreferences prefs = context.getSharedPreferences("amal_tracker_prefs", Context.MODE_PRIVATE);
                int totalAmals = prefs.getInt("total_completed_amals_count", 0) + 1;
                prefs.edit().putInt("total_completed_amals_count", totalAmals).apply();
            } catch (Exception ignored) {}
            try {
                com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(context)
                        .syncAmalItem(amal.getDateString(), String.valueOf(amal.getId()), amal.getTitle(), amal.getPoints(), true);
            } catch (Exception ignored) {}
        });
    }

    public void toggleAmalCompletion(DailyAmalEntity amal) {
        if (amal == null || amal.isCompleted()) return; // Lock: cannot uncomplete
        completeAmal(amal);
    }

    @Override
    public LiveData<AmalRecordEntity> getLatestRecord() {
        return amalRecordDao.getLatestRecord();
    }

    @Override
    public void updateRecord(AmalRecordEntity record) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            amalRecordDao.insertOrUpdate(record);
        });
    }

    public final void recalculateAndSyncTodayRecord() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String today = sdf.format(new Date());

            int prayedToday = prayerLogDao.getPrayedCountForDateSync(today);
            int amalsCompleted = dailyAmalDao.getCompletedCountByDateSync(today);
            int quizPlayed = quizDao.getDailyQuizzesPlayedCountSync(today);
            if (quizPlayed == 0) {
                quizPlayed = QuizManager.getInstance().getDailyQuizzesPlayedCount(context);
            }

            SharedPreferences salahPrefs = context.getSharedPreferences("salah_tracker_prefs", Context.MODE_PRIVATE);
            salahPrefs.edit().putInt("prayers_completed_today", prayedToday).apply();

            SharedPreferences amalPrefs = context.getSharedPreferences("amal_tracker_prefs", Context.MODE_PRIVATE);
            amalPrefs.edit().putInt("completed_amals_count", amalsCompleted).apply();

            int streakDays = GamificationManager.getStreakDays(context);

            int dailyAmalPoints = dailyAmalDao.getEarnedPointsByDateSync(today);
            int dailyPrayerPoints = prayerLogDao.getEarnedPointsForDateSync(today);
            int dailyQuizPoints = quizDao.getEarnedPointsForDateSync(today);
            int ledgerDailyPoints = com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.getTodayPoints(context);
            int gamificationDailyPoints = GamificationManager.getTodayXP(context);

            int dailyPoints = Math.max(dailyAmalPoints + dailyPrayerPoints + dailyQuizPoints, Math.max(ledgerDailyPoints, gamificationDailyPoints));

            int totalPossibleItems = 11; // 5 prayers + 5 amals + 1 daily quiz
            int completedItems = prayedToday + amalsCompleted + (quizPlayed > 0 ? 1 : 0);
            double percentage = Math.min(100.0, ((double) completedItems / totalPossibleItems) * 100.0);

            AmalRecordEntity realRecord = new AmalRecordEntity(
                    today,
                    streakDays,
                    amalsCompleted,
                    totalPossibleItems,
                    prayedToday,
                    dailyPoints,
                    percentage
            );
            amalRecordDao.insertOrUpdate(realRecord);
        });
    }
}
