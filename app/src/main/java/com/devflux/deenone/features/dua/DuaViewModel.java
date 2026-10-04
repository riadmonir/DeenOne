package com.devflux.deenone.features.dua;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.devflux.deenone.data.local.dao.DuaLogDao;
import com.devflux.deenone.data.local.entity.DuaEntity;
import com.devflux.deenone.data.local.entity.DuaLogEntity;
import com.devflux.deenone.data.repository.DuaRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DuaViewModel extends AndroidViewModel {

    private static final String PREF_NAME = "dua_tracker_prefs";
    private static final String KEY_DAILY_GOAL = "pref_dua_daily_goal";

    private final DuaRepository repository;
    private final SharedPreferences prefs;
    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>("all");
    private final MutableLiveData<String> selectedLanguage = new MutableLiveData<>("bn");
    private final MutableLiveData<Integer> dailyGoalLiveData = new MutableLiveData<>(7);

    private final MutableLiveData<Boolean> isSyncing = new MutableLiveData<>(false);
    private final MutableLiveData<String> syncStatusMessage = new MutableLiveData<>("সকল প্রামাণ্য দোয়া সংরক্ষিত ও আপ-টু-ডেট আছে");

    private final LiveData<List<DuaEntity>> duasLiveData;
    private final LiveData<Integer> todayDuaCountLiveData;

    public DuaViewModel(@NonNull Application application) {
        super(application);
        this.repository = new DuaRepository(application);
        this.prefs = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        int savedGoal = prefs.getInt(KEY_DAILY_GOAL, 7);
        this.dailyGoalLiveData.setValue(savedGoal);

        this.duasLiveData = Transformations.switchMap(selectedCategory, cat -> {
            if ("all".equalsIgnoreCase(cat) || "সকল দোয়া".equalsIgnoreCase(cat)) {
                return repository.getAllDuas();
            } else if ("Favorites".equalsIgnoreCase(cat) || "প্রিয় দোয়া".equalsIgnoreCase(cat)) {
                return repository.getFavoriteDuas();
            } else {
                return repository.getDuasByCategory(cat);
            }
        });

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        this.todayDuaCountLiveData = repository.getTodayDuaCount(todayDate);
    }

    public LiveData<List<DuaEntity>> getDuas() {
        return duasLiveData;
    }

    public LiveData<String> getSelectedLanguage() {
        return selectedLanguage;
    }

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }

    public LiveData<Integer> getTodayDuaCount() {
        return todayDuaCountLiveData;
    }

    public LiveData<Integer> getDailyGoal() {
        return dailyGoalLiveData;
    }

    public void setDailyGoal(int goal) {
        prefs.edit().putInt(KEY_DAILY_GOAL, goal).apply();
        dailyGoalLiveData.setValue(goal);
    }

    public void setCategory(String category) {
        selectedCategory.setValue(category);
    }

    public void setLanguage(String langCode) {
        selectedLanguage.setValue(langCode);
    }

    public LiveData<List<DuaEntity>> searchDuas(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getDuas();
        }
        return repository.searchDuas(query.trim());
    }

    public void toggleFavorite(DuaEntity dua) {
        if (dua != null) {
            repository.toggleFavorite(dua.getId(), !dua.isFavorite());
        }
    }

    public void markDuaAsRead(DuaEntity dua) {
        if (dua != null) {
            repository.logDuaRead(dua.getId(), dua.getTitle(), dua.getCategory());
        }
    }

    public LiveData<DuaLogDao.DuaCountStat> getMostReadDua() {
        return repository.getMostReadDua();
    }

    public LiveData<List<DuaLogDao.DuaCountStat>> getTopReadDuas() {
        return repository.getTopReadDuas();
    }

    public LiveData<List<DuaLogDao.CategoryUsageStat>> getCategoryUsage() {
        return repository.getCategoryUsage();
    }

    public LiveData<List<DuaLogEntity>> getRecentHistory(int limit) {
        return repository.getRecentHistory(limit);
    }

    public LiveData<Integer> getTotalDuaCount() {
        return repository.getTotalDuaCount();
    }

    public LiveData<Boolean> getIsSyncing() {
        return isSyncing;
    }

    public LiveData<String> getSyncStatusMessage() {
        return syncStatusMessage;
    }

    public LiveData<Integer> getTotalDuaCountLive() {
        return repository.getDuaCountLive();
    }

    public void ensureSeedDuasLoaded() {
        repository.ensureSeedDuasLoaded(null);
    }

    public void triggerOnlineSync() {
        if (Boolean.TRUE.equals(isSyncing.getValue())) return;
        isSyncing.setValue(true);
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(getApplication());
        syncStatusMessage.setValue(isBn ? "অনলাইন হাদিস গ্রন্থ থেকে প্রামাণ্য দোয়া স্ক্র্যাপিং হচ্ছে..." : "Scraping authentic Duas from online Hadith sources...");

        repository.syncNextDuasFromOnline(new DuaRepository.DuaSyncCallback() {
            @Override
            public void onSuccess(int newlyAddedCount, int totalCount, String sourceBook) {
                isSyncing.setValue(false);
                boolean isBnLocale = com.devflux.deenone.core.localization.LocaleManager.isBengali(getApplication());
                if (newlyAddedCount > 0) {
                    syncStatusMessage.setValue(isBnLocale ? (sourceBook + " থেকে " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(newlyAddedCount) + "টি নতুন দোয়া যুক্ত হয়েছে, সর্বমোট: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(totalCount) + "টি") : (newlyAddedCount + " new Duas added from " + sourceBook + ", Total: " + totalCount));
                } else {
                    syncStatusMessage.setValue(isBnLocale ? (sourceBook + " এর দোয়াগুলো ইতোমধ্যে সংরক্ষিত আছে, সর্বমোট: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(totalCount) + "টি") : ("Duas from " + sourceBook + " are already up-to-date, Total: " + totalCount));
                }
            }

            @Override
            public void onError(String errorMessage) {
                isSyncing.setValue(false);
                syncStatusMessage.setValue(errorMessage);
            }
        });
    }
}
