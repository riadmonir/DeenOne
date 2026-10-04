package com.devflux.deenone.ui.dashboard;

import android.app.Application;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.AmalRecordEntity;
import com.devflux.deenone.data.local.entity.DailyContentEntity;
import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.data.repository.AmalRepository;
import com.devflux.deenone.data.repository.ContentRepository;
import com.devflux.deenone.data.repository.PrayerRepository;
import com.devflux.deenone.ui.dashboard.model.FeatureItem;
import com.devflux.deenone.ui.dashboard.model.PrayerWaqtItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final PrayerRepository prayerRepository;
    private final ContentRepository contentRepository;
    private final AmalRepository amalRepository;

    private final MutableLiveData<List<FeatureItem>> featureListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PrayerWaqtItem>> prayerWaqtListLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> hijriDateLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> gregorianDateLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> currentWaqtNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> currentWaqtRangeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> currentWaqtRemainingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentWaqtProgressLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> nextPrayerNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> nextPrayerTimeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> nextPrayerCountdownLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> iftarCountdownLiveData = new MutableLiveData<>();

    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;

    // Real coordinates for Madinah Munawwarah (Default) or GPS location
    private double currentLatitude = 24.4672;
    private double currentLongitude = 39.6111;
    private double currentTimezone = 3.0;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        prayerRepository = new PrayerRepository(application);
        contentRepository = new ContentRepository(application);
        amalRepository = new AmalRepository(application);

        initFeatures();
        startRealTimeEngine();
    }

    public LiveData<PrayerScheduleEntity> getPrayerSchedule() {
        return prayerRepository.getLatestSchedule();
    }

    public LiveData<DailyContentEntity> getDailyContent() {
        return contentRepository.getLatestContent();
    }

    public LiveData<AmalRecordEntity> getAmalRecord() {
        return amalRepository.getLatestRecord();
    }

    public LiveData<List<FeatureItem>> getFeatureList() {
        return featureListLiveData;
    }

    public LiveData<List<PrayerWaqtItem>> getPrayerWaqtList() {
        return prayerWaqtListLiveData;
    }

    public LiveData<String> getHijriDate() {
        return hijriDateLiveData;
    }

    public LiveData<String> getGregorianDate() {
        return gregorianDateLiveData;
    }

    public LiveData<String> getCurrentWaqtName() {
        return currentWaqtNameLiveData;
    }

    public LiveData<String> getCurrentWaqtRange() {
        return currentWaqtRangeLiveData;
    }

    public LiveData<String> getCurrentWaqtRemaining() {
        return currentWaqtRemainingLiveData;
    }

    public LiveData<Integer> getCurrentWaqtProgress() {
        return currentWaqtProgressLiveData;
    }

    public LiveData<String> getNextPrayerName() {
        return nextPrayerNameLiveData;
    }

    public LiveData<String> getNextPrayerTime() {
        return nextPrayerTimeLiveData;
    }

    public LiveData<String> getNextPrayerCountdown() {
        return nextPrayerCountdownLiveData;
    }

    public LiveData<String> getIftarCountdown() {
        return iftarCountdownLiveData;
    }

    public void setLocation(double lat, double lng, double timezone) {
        this.currentLatitude = lat;
        this.currentLongitude = lng;
        this.currentTimezone = timezone;
        updateRealTimeCalculations();
    }

    public void toggleDuaReadStatus(DailyContentEntity entity) {
        if (entity != null) {
            contentRepository.setDuaRead(entity.getId(), !entity.isDuaRead());
        }
    }

    private void initFeatures() {
        List<FeatureItem> list = new ArrayList<>();
        list.add(new FeatureItem(1, "তাসবিহ কাউন্টার", R.drawable.ic_feat_tasbih, Color.parseColor("#0F3A30")));
        list.add(new FeatureItem(2, "দৈনিক আজকার", R.drawable.ic_feat_azkar, Color.parseColor("#381F0A")));
        list.add(new FeatureItem(3, "কিবলা কম্পাস", R.drawable.ic_feat_qibla, Color.parseColor("#152D32")));
        list.add(new FeatureItem(4, "মুসলিম বিবাহ", R.drawable.ic_feat_marriage, Color.parseColor("#0E3B27")));
        list.add(new FeatureItem(5, "উত্তরাধিকার বণ্টন", R.drawable.ic_feat_faraid, Color.parseColor("#122E2B")));
        list.add(new FeatureItem(6, "রক্তদান সেকশন", R.drawable.ic_feat_blood, Color.parseColor("#3C1215")));
        list.add(new FeatureItem(7, "ইসলামিক অডিও হাব", R.drawable.ic_feat_audio, Color.parseColor("#0F3A2C")));
        list.add(new FeatureItem(8, "ইসলামিক বই", R.drawable.ic_feat_book, Color.parseColor("#103833")));
        list.add(new FeatureItem(9, "হাদিস শরিফ", R.drawable.ic_feat_hadith, Color.parseColor("#102838")));
        list.add(new FeatureItem(10, "দোয়া ভাণ্ডার", R.drawable.ic_feat_dua, Color.parseColor("#381028")));
        list.add(new FeatureItem(11, "আমল ট্র্যাকার", R.drawable.ic_feat_amal, Color.parseColor("#102738")));
        list.add(new FeatureItem(12, "কুইজ হাব", R.drawable.ic_feat_quiz, Color.parseColor("#291238")));
        list.add(new FeatureItem(13, "মসজিদ সন্ধান", R.drawable.ic_feat_mosque, Color.parseColor("#103338")));
        list.add(new FeatureItem(14, "সফর মোড", R.drawable.ic_feat_safar, Color.parseColor("#102B38")));
        list.add(new FeatureItem(15, "জুম্মা মোড", R.drawable.ic_feat_jumma, Color.parseColor("#0E3827")));
        list.add(new FeatureItem(16, "ঈদ মোড", R.drawable.ic_feat_eid, Color.parseColor("#38320E")));
        list.add(new FeatureItem(17, "জানাযা গাইড", R.drawable.ic_feat_janaza, Color.parseColor("#103538")));
        list.add(new FeatureItem(18, "রোজা ও রমজান", R.drawable.ic_feat_ramadan, Color.parseColor("#38230E")));
        list.add(new FeatureItem(19, "খতম প্ল্যানার", R.drawable.ic_feat_khatam, Color.parseColor("#0E3827")));
        list.add(new FeatureItem(20, "আজকের আয়াত", R.drawable.ic_feat_ayah, Color.parseColor("#103834")));
        list.add(new FeatureItem(21, "হজ ও উমরাহ", R.drawable.ic_feat_hajj, Color.parseColor("#382B10")));
        list.add(new FeatureItem(22, "যাকাত ক্যালকুলেটর", R.drawable.ic_feat_zakat, Color.parseColor("#281238")));
        list.add(new FeatureItem(23, "আল্লাহর ৯৯ নাম", R.drawable.ic_feat_allah_names, Color.parseColor("#383110")));
        list.add(new FeatureItem(24, "ইসলামিক ক্যালেন্ডার", R.drawable.ic_feat_calendar, Color.parseColor("#102B38")));

        featureListLiveData.setValue(list);
    }

    private void startRealTimeEngine() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                updateRealTimeCalculations();
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(timerRunnable);
    }

    private void updateRealTimeCalculations() {
        Calendar cal = Calendar.getInstance();

        // 1. Real Hijri & Gregorian Dates
        HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getAdjustedHijriDate(getApplication(), cal);
        hijriDateLiveData.setValue(hijri.getFormattedBengali());
        gregorianDateLiveData.setValue(HijriCalendarUtil.getRealGregorianDateBengali(cal));

        // 2. Real Astronomical Prayer Calculation
        PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocation(
                currentLatitude, currentLongitude, currentTimezone, cal
        );

        // Waqt Name & Range
        currentWaqtNameLiveData.setValue(res.currentWaqtName);
        currentWaqtRangeLiveData.setValue(res.chashtRangeStr);

        // Waqt Remaining Text in Bengali Numerals
        long wMin = res.currentWaqtRemainingSeconds / 60;
        long wSec = res.currentWaqtRemainingSeconds % 60;
        currentWaqtRemainingLiveData.setValue("ওয়াক্ত শেষ হতে বাকি: " + BengaliNumberUtil.toBengali(wMin) + " মিনিট " + BengaliNumberUtil.toBengali(wSec) + " সেকেন্ড");
        currentWaqtProgressLiveData.setValue(res.currentWaqtProgressPercent);

        // Next Prayer & Live Countdown
        nextPrayerNameLiveData.setValue(res.nextPrayerName);
        nextPrayerTimeLiveData.setValue(res.nextPrayerTimeStr);

        long zHours = res.nextPrayerRemainingSeconds / 3600;
        long zMinutes = (res.nextPrayerRemainingSeconds % 3600) / 60;
        long zSecs = res.nextPrayerRemainingSeconds % 60;
        nextPrayerCountdownLiveData.setValue(String.format("%02d:%02d:%02d", zHours, zMinutes, zSecs));

        // Iftar Live Countdown
        long iHours = res.iftarRemainingSeconds / 3600;
        long iMinutes = (res.iftarRemainingSeconds % 3600) / 60;
        long iSecs = res.iftarRemainingSeconds % 60;
        iftarCountdownLiveData.setValue(String.format("%02d : %02d : %02d", iHours, iMinutes, iSecs));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }
}
