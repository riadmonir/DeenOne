package com.devflux.deenone.features.notifications;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.alarms.AlarmRescheduler;
import com.devflux.deenone.core.notifications.NotificationSettingsManager;

public class NotificationSettingsViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> prayerNotifsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> adhanAudioLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> adhanReciterLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> prePrayerMinutesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sehriIftarLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dailyAmalLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dailyDuaHadithLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dailyQuizLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> adminNotifsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> vibrationLiveData = new MutableLiveData<>();

    public NotificationSettingsViewModel(@NonNull Application application) {
        super(application);
        loadAllSettings();
    }

    public void loadAllSettings() {
        prayerNotifsLiveData.setValue(NotificationSettingsManager.isPrayerNotifsEnabled(getApplication()));
        adhanAudioLiveData.setValue(NotificationSettingsManager.isAdhanEnabled(getApplication()));
        adhanReciterLiveData.setValue(NotificationSettingsManager.getAdhanReciter(getApplication()));
        prePrayerMinutesLiveData.setValue(NotificationSettingsManager.getPrePrayerMinutes(getApplication()));
        sehriIftarLiveData.setValue(NotificationSettingsManager.isSehriIftarEnabled(getApplication()));
        dailyAmalLiveData.setValue(NotificationSettingsManager.isDailyAmalEnabled(getApplication()));
        dailyDuaHadithLiveData.setValue(NotificationSettingsManager.isDailyDuaHadithEnabled(getApplication()));
        dailyQuizLiveData.setValue(NotificationSettingsManager.isDailyQuizEnabled(getApplication()));
        adminNotifsLiveData.setValue(NotificationSettingsManager.isAdminAnnouncementsEnabled(getApplication()));
        vibrationLiveData.setValue(NotificationSettingsManager.isVibrationEnabled(getApplication()));
    }

    public LiveData<Boolean> getPrayerNotifs() { return prayerNotifsLiveData; }
    public LiveData<Boolean> getAdhanAudio() { return adhanAudioLiveData; }
    public LiveData<String> getAdhanReciter() { return adhanReciterLiveData; }
    public LiveData<Integer> getPrePrayerMinutes() { return prePrayerMinutesLiveData; }
    public LiveData<Boolean> getSehriIftar() { return sehriIftarLiveData; }
    public LiveData<Boolean> getDailyAmal() { return dailyAmalLiveData; }
    public LiveData<Boolean> getDailyDuaHadith() { return dailyDuaHadithLiveData; }
    public LiveData<Boolean> getDailyQuiz() { return dailyQuizLiveData; }
    public LiveData<Boolean> getAdminNotifs() { return adminNotifsLiveData; }
    public LiveData<Boolean> getVibration() { return vibrationLiveData; }

    public void setPrayerNotifs(boolean enabled) {
        NotificationSettingsManager.setPrayerNotifsEnabled(getApplication(), enabled);
        prayerNotifsLiveData.setValue(enabled);
        AlarmRescheduler.rescheduleAll(getApplication());
    }

    public void setAdhanAudio(boolean enabled) {
        NotificationSettingsManager.setAdhanEnabled(getApplication(), enabled);
        adhanAudioLiveData.setValue(enabled);
        AlarmRescheduler.rescheduleAll(getApplication());
    }

    public void setAdhanReciter(String reciter) {
        NotificationSettingsManager.setAdhanReciter(getApplication(), reciter);
        adhanReciterLiveData.setValue(reciter);
    }

    public void setPrePrayerMinutes(int minutes) {
        NotificationSettingsManager.setPrePrayerMinutes(getApplication(), minutes);
        prePrayerMinutesLiveData.setValue(minutes);
        AlarmRescheduler.rescheduleAll(getApplication());
    }

    public void setSehriIftar(boolean enabled) {
        NotificationSettingsManager.setSehriIftarEnabled(getApplication(), enabled);
        sehriIftarLiveData.setValue(enabled);
        AlarmRescheduler.rescheduleAll(getApplication());
    }

    public void setDailyAmal(boolean enabled) {
        NotificationSettingsManager.setDailyAmalEnabled(getApplication(), enabled);
        dailyAmalLiveData.setValue(enabled);
    }

    public void setDailyDuaHadith(boolean enabled) {
        NotificationSettingsManager.setDailyDuaHadithEnabled(getApplication(), enabled);
        dailyDuaHadithLiveData.setValue(enabled);
    }

    public void setDailyQuiz(boolean enabled) {
        NotificationSettingsManager.setDailyQuizEnabled(getApplication(), enabled);
        dailyQuizLiveData.setValue(enabled);
    }

    public void setAdminNotifs(boolean enabled) {
        NotificationSettingsManager.setAdminAnnouncementsEnabled(getApplication(), enabled);
        adminNotifsLiveData.setValue(enabled);
    }

    public void setVibration(boolean enabled) {
        NotificationSettingsManager.setVibrationEnabled(getApplication(), enabled);
        vibrationLiveData.setValue(enabled);
    }
}
