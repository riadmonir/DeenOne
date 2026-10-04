package com.devflux.deenone.features.home;

import android.app.Application;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.R;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.AmalRecordEntity;
import com.devflux.deenone.data.local.entity.DailyContentEntity;
import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.data.repository.AmalRepository;
import com.devflux.deenone.data.repository.ContentRepository;
import com.devflux.deenone.data.repository.PrayerRepository;
import com.devflux.deenone.domain.usecase.GetDailyContentUseCase;
import com.devflux.deenone.domain.usecase.GetPrayerScheduleUseCase;
import com.devflux.deenone.domain.usecase.TrackAmalUseCase;
import com.devflux.deenone.features.home.model.FeatureItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final PrayerRepository prayerRepo;
    private final GetPrayerScheduleUseCase getPrayerScheduleUseCase;
    private final GetDailyContentUseCase getDailyContentUseCase;
    private final TrackAmalUseCase trackAmalUseCase;

    private final MutableLiveData<List<FeatureItem>> featureListLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> hijriDateLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> gregorianDateLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> currentWaqtNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> currentWaqtRangeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> currentWaqtRemainingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentWaqtProgressLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> sunriseTimeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> sunsetTimeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> nextPrayerNameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> nextPrayerTimeLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> nextPrayerCountdownLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> iftarCountdownLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> fastingCountdownTitleLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> fastingCountdownValueLiveData = new MutableLiveData<>();
    private final MutableLiveData<com.devflux.deenone.core.prayer.ForbiddenTimesCalculator.ForbiddenStatus> forbiddenStatusLiveData = new MutableLiveData<>();
    private final MutableLiveData<DynamicPrayerWidgetState> dynamicPrayerWidgetLiveData = new MutableLiveData<>();

    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;

    private LocationProvider.Coordinates currentCoordinates;

    public static class DynamicPrayerWidgetState {
        public boolean isForbidden;
        public String forbiddenTitle;
        public String forbiddenRange;
        public String forbiddenRemainingText;
        public int forbiddenProgress;

        public String waqtName;
        public String waqtRange;
        public String waqtRemainingText;
        public int waqtProgress;

        public long fajrMillis;
        public long sunriseMillis;
        public long zohrMillis;
        public long asrMillis;
        public long sunsetMillis;
        public long ishaMillis;
    }

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.prayerRepo = new PrayerRepository(application);
        ContentRepository contentRepo = new ContentRepository(application);
        AmalRepository amalRepo = new AmalRepository(application);

        this.getPrayerScheduleUseCase = new GetPrayerScheduleUseCase(prayerRepo);
        this.getDailyContentUseCase = new GetDailyContentUseCase(contentRepo);
        this.trackAmalUseCase = new TrackAmalUseCase(amalRepo);

        this.currentCoordinates = LocationProvider.getSavedOrCurrentLocation(application);

        com.devflux.deenone.core.sync.SunriseSunsetSyncManager.getInstance().getLiveSunTimes().observeForever(sunTimes -> {
            if (sunTimes != null) {
                if (sunTimes.sunrise != null) sunriseTimeLiveData.postValue(sunTimes.sunrise);
                if (sunTimes.sunset != null) sunsetTimeLiveData.postValue(sunTimes.sunset);
            }
        });
        com.devflux.deenone.core.sync.SunriseSunsetSyncManager.getInstance().syncSunTimes(
                application, currentCoordinates.latitude, currentCoordinates.longitude, currentCoordinates.timezone
        );

        initFeatures();
        startRealTimeEngine();
    }

    public LiveData<PrayerScheduleEntity> getPrayerSchedule() {
        return getPrayerScheduleUseCase.execute();
    }

    public LiveData<DailyContentEntity> getDailyContent() {
        return getDailyContentUseCase.execute();
    }

    public LiveData<AmalRecordEntity> getAmalRecord() {
        return trackAmalUseCase.execute();
    }

    public LiveData<List<FeatureItem>> getFeatureList() {
        return featureListLiveData;
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

    public LiveData<String> getSunriseTime() {
        return sunriseTimeLiveData;
    }

    public LiveData<String> getSunsetTime() {
        return sunsetTimeLiveData;
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
        return fastingCountdownValueLiveData;
    }

    public LiveData<String> getFastingCountdownTitle() {
        return fastingCountdownTitleLiveData;
    }

    public LiveData<String> getFastingCountdownValue() {
        return fastingCountdownValueLiveData;
    }

    public LiveData<com.devflux.deenone.core.prayer.ForbiddenTimesCalculator.ForbiddenStatus> getForbiddenStatus() {
        return forbiddenStatusLiveData;
    }

    public LiveData<DynamicPrayerWidgetState> getDynamicPrayerWidget() {
        return dynamicPrayerWidgetLiveData;
    }

    public LocationProvider.Coordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setLocation(LocationProvider.Coordinates coordinates) {
        if (coordinates != null) {
            this.currentCoordinates = coordinates;
            updateRealTimeCalculations();
            if (prayerRepo != null) {
                prayerRepo.refreshRealtimeSchedule();
            }
            com.devflux.deenone.core.sync.SunriseSunsetSyncManager.getInstance().syncSunTimes(
                    getApplication(), coordinates.latitude, coordinates.longitude, coordinates.timezone
            );
            com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(getApplication());
        }
    }

    public void updateLocation(String locationName, double latitude, double longitude, double timezone) {
        setLocation(new LocationProvider.Coordinates(locationName, latitude, longitude, timezone));
    }

    public void toggleDuaReadStatus(DailyContentEntity entity) {
        if (entity != null) {
            getDailyContentUseCase.setDuaRead(entity.getId(), !entity.isDuaRead());
        }
    }

    public void toggleCurrentDuaRead() {
        DailyContentEntity current = getDailyContent().getValue();
        if (current != null) {
            toggleDuaReadStatus(current);
        }
    }

    private void initFeatures() {
        Application app = getApplication();
        AppDatabase db = AppDatabase.getInstance(app);

        // 1. Initial 30 features with dedicated Islamic icons
        List<FeatureItem> defaultList = buildDefaultFeatureList(app);
        featureListLiveData.setValue(defaultList);

        // 2. Database Sync in Background
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<com.devflux.deenone.data.local.entity.FeatureWidgetEntity> entities = db.featureWidgetDao().getEnabledFeaturesSync();
            if (entities != null && !entities.isEmpty()) {
                boolean hasAllahNames = false;
                boolean hasKalima = false;
                boolean hasHalal = false;
                for (com.devflux.deenone.data.local.entity.FeatureWidgetEntity entity : entities) {
                    if ("feat_28".equals(entity.getFeatureId()) || "action_allah_names".equals(entity.getActionKey())) hasAllahNames = true;
                    if ("feat_29".equals(entity.getFeatureId()) || "action_six_kalima".equals(entity.getActionKey())) hasKalima = true;
                    if ("feat_30".equals(entity.getFeatureId()) || "action_knowledge_battle".equals(entity.getActionKey())) hasHalal = true;

                    // Migrate legacy icon strings to dedicated Islamic icons
                    if ("feat_1".equals(entity.getFeatureId()) && "ic_clock".equals(entity.getIconResName())) {
                        entity.setIconResName("ic_feat_salat");
                        db.featureWidgetDao().insertOrUpdate(entity);
                    } else if ("feat_2".equals(entity.getFeatureId()) && "ic_feat_book".equals(entity.getIconResName())) {
                        entity.setIconResName("ic_feat_namaz_shikha");
                        db.featureWidgetDao().insertOrUpdate(entity);
                    } else if ("feat_3".equals(entity.getFeatureId()) && "ic_feat_book".equals(entity.getIconResName())) {
                        entity.setIconResName("ic_feat_quran");
                        db.featureWidgetDao().insertOrUpdate(entity);
                    } else if ("feat_15".equals(entity.getFeatureId()) || "action_roza".equals(entity.getActionKey())) {
                        entity.setTitle("রোজা ও রমজান");
                        entity.setIconResName("ic_feat_ramadan");
                        entity.setActionKey("action_roza_ramadan");
                        entity.setCircleBgHex("#38230E");
                        entity.setIconTintHex("#FB923C");
                        db.featureWidgetDao().insertOrUpdate(entity);
                    } else if ("feat_19".equals(entity.getFeatureId()) || "action_ramadan".equals(entity.getActionKey())) {
                        entity.setTitle("নবীদের জীবনী");
                        entity.setIconResName("ic_feat_prophets");
                        entity.setActionKey("action_prophets");
                        entity.setCircleBgHex("#103833");
                        entity.setIconTintHex("#2DD4BF");
                        db.featureWidgetDao().insertOrUpdate(entity);
                    } else if ("feat_24".equals(entity.getFeatureId())) {
                        entity.setTitle("কিবলা কম্পাস");
                        entity.setIconResName("ic_feat_qibla");
                        entity.setActionKey("action_qibla");
                        entity.setCircleBgHex("#152D32");
                        entity.setIconTintHex("#2DD4BF");
                        db.featureWidgetDao().insertOrUpdate(entity);
                    } else if ("feat_25".equals(entity.getFeatureId()) || "action_journey".equals(entity.getActionKey()) || "দ্বীন জার্নি".equals(entity.getTitle()) || "ic_feat_journey".equals(entity.getIconResName())) {
                        entity.setTitle("মুসলিম বিবাহ");
                        entity.setIconResName("ic_feat_marriage");
                        entity.setActionKey("action_marriage");
                        entity.setCircleBgHex("#0E3B27");
                        entity.setIconTintHex("#34D399");
                        db.featureWidgetDao().insertOrUpdate(entity);
                    }
                }
                if (!hasAllahNames) {
                    db.featureWidgetDao().insertOrUpdate(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_28", "আল্লাহর ৯৯ নাম", "ic_feat_allah_names", "#0E3827", "#34D399", 28, true, "action_allah_names"));
                }
                if (!hasKalima) {
                    db.featureWidgetDao().insertOrUpdate(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_29", "৬ কালিমা", "ic_feat_kalima", "#382810", "#FBBF24", 29, true, "action_six_kalima"));
                }
                if (!hasHalal) {
                    db.featureWidgetDao().insertOrUpdate(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_30", "নলেজ ব্যাটেল", "ic_feat_battle", "#281238", "#C084FC", 30, true, "action_knowledge_battle"));
                }
                entities = db.featureWidgetDao().getEnabledFeaturesSync();
                List<FeatureItem> dbList = new ArrayList<>();
                for (com.devflux.deenone.data.local.entity.FeatureWidgetEntity entity : entities) {
                    int iconRes = resolveIconRes(app, entity.getIconResName());
                    int bgInt = android.graphics.Color.parseColor(entity.getCircleBgHex());
                    int tintInt = android.graphics.Color.parseColor(entity.getIconTintHex());
                    dbList.add(new FeatureItem(entity.getDisplayOrder(), entity.getTitle(), iconRes, bgInt, tintInt, entity.getActionKey()));
                }
                featureListLiveData.postValue(dbList);
            }
        });
    }

    private List<FeatureItem> buildDefaultFeatureList(Application app) {
        List<FeatureItem> list = new ArrayList<>();
        // Row 1
        list.add(new FeatureItem(1, "সালাত", R.drawable.ic_feat_salat, android.graphics.Color.parseColor("#38230E"), android.graphics.Color.parseColor("#F59E0B"), "action_salat"));
        list.add(new FeatureItem(2, "নামাজ শিক্ষা", R.drawable.ic_feat_namaz_shikha, android.graphics.Color.parseColor("#0E3827"), android.graphics.Color.parseColor("#34D399"), "action_namaz_shikha"));
        list.add(new FeatureItem(3, "কুরআন মাজিদ", R.drawable.ic_feat_quran, android.graphics.Color.parseColor("#0E3827"), android.graphics.Color.parseColor("#10B981"), "action_quran"));

        // Row 2
        list.add(new FeatureItem(4, "ইসলামিক অডিও হাব", R.drawable.ic_feat_audio, android.graphics.Color.parseColor("#0F3A2C"), android.graphics.Color.parseColor("#34D399"), "action_audio"));
        list.add(new FeatureItem(5, "ইসলামিক বই", R.drawable.ic_feat_book, android.graphics.Color.parseColor("#103833"), android.graphics.Color.parseColor("#2DD4BF"), "action_books"));
        list.add(new FeatureItem(6, "হাদিস শরিফ", R.drawable.ic_feat_hadith, android.graphics.Color.parseColor("#102838"), android.graphics.Color.parseColor("#38BDF8"), "action_hadith"));

        // Row 3
        list.add(new FeatureItem(7, "দোয়া ভাণ্ডার", R.drawable.ic_feat_dua, android.graphics.Color.parseColor("#381028"), android.graphics.Color.parseColor("#F472B6"), "action_dua"));
        list.add(new FeatureItem(8, "আমল ট্র্যাকার", R.drawable.ic_feat_amal, android.graphics.Color.parseColor("#102738"), android.graphics.Color.parseColor("#60A5FA"), "action_amal"));
        list.add(new FeatureItem(9, "কুইজ হাব", R.drawable.ic_feat_quiz, android.graphics.Color.parseColor("#291238"), android.graphics.Color.parseColor("#C084FC"), "action_quiz"));

        // Row 4
        list.add(new FeatureItem(10, "মসজিদ সন্ধান", R.drawable.ic_feat_mosque, android.graphics.Color.parseColor("#103338"), android.graphics.Color.parseColor("#22D3EE"), "action_mosque"));
        list.add(new FeatureItem(11, "সফর মোড", R.drawable.ic_feat_safar, android.graphics.Color.parseColor("#102B38"), android.graphics.Color.parseColor("#38BDF8"), "action_safar"));
        list.add(new FeatureItem(12, "জুম্মা মোড", R.drawable.ic_feat_jumma, android.graphics.Color.parseColor("#0E3827"), android.graphics.Color.parseColor("#34D399"), "action_jummah"));

        // Row 5
        list.add(new FeatureItem(13, "ঈদ মোড", R.drawable.ic_feat_eid, android.graphics.Color.parseColor("#38320E"), android.graphics.Color.parseColor("#FBBF24"), "action_eid"));
        list.add(new FeatureItem(14, "জানাযা গাইড", R.drawable.ic_feat_janaza, android.graphics.Color.parseColor("#103538"), android.graphics.Color.parseColor("#2DD4BF"), "action_janaza"));
        list.add(new FeatureItem(15, "রোজা ও রমজান", R.drawable.ic_feat_ramadan, android.graphics.Color.parseColor("#38230E"), android.graphics.Color.parseColor("#FB923C"), "action_roza_ramadan"));

        // Row 6
        list.add(new FeatureItem(16, "খতম প্ল্যানার", R.drawable.ic_feat_khatam, android.graphics.Color.parseColor("#0E3827"), android.graphics.Color.parseColor("#34D399"), "action_khatm"));
        list.add(new FeatureItem(17, "আজকের আয়াত", R.drawable.ic_feat_ayah, android.graphics.Color.parseColor("#103834"), android.graphics.Color.parseColor("#2DD4BF"), "action_daily_ayah"));
        list.add(new FeatureItem(18, "হজ ও উমরাহ", R.drawable.ic_feat_hajj, android.graphics.Color.parseColor("#382B10"), android.graphics.Color.parseColor("#FBBF24"), "action_hajj"));

        // Row 7
        list.add(new FeatureItem(19, "নবীদের জীবনী", R.drawable.ic_feat_prophets, android.graphics.Color.parseColor("#103833"), android.graphics.Color.parseColor("#2DD4BF"), "action_prophets"));
        list.add(new FeatureItem(20, "হিজরি ক্যালেন্ডার", R.drawable.ic_feat_calendar, android.graphics.Color.parseColor("#102B38"), android.graphics.Color.parseColor("#818CF8"), "action_calendar"));
        list.add(new FeatureItem(21, "যাকাত ক্যালকুলেটর", R.drawable.ic_feat_zakat, android.graphics.Color.parseColor("#281238"), android.graphics.Color.parseColor("#C084FC"), "action_zakat"));

        // Row 8
        list.add(new FeatureItem(22, "তাসবিহ কাউন্টার", R.drawable.ic_feat_tasbih, android.graphics.Color.parseColor("#0F3A30"), android.graphics.Color.parseColor("#34D399"), "action_tasbih"));
        list.add(new FeatureItem(23, "দৈনিক আজকার", R.drawable.ic_feat_azkar, android.graphics.Color.parseColor("#381F0A"), android.graphics.Color.parseColor("#FB923C"), "action_azkar"));
        list.add(new FeatureItem(24, "কিবলা কম্পাস", R.drawable.ic_feat_qibla, android.graphics.Color.parseColor("#152D32"), android.graphics.Color.parseColor("#2DD4BF"), "action_qibla"));

        // Row 9
        list.add(new FeatureItem(25, "মুসলিম বিবাহ", R.drawable.ic_feat_marriage, android.graphics.Color.parseColor("#0E3B27"), android.graphics.Color.parseColor("#34D399"), "action_marriage"));
        list.add(new FeatureItem(26, "উত্তরাধিকার বণ্টন", R.drawable.ic_feat_faraid, android.graphics.Color.parseColor("#122E2B"), android.graphics.Color.parseColor("#35D99B"), "action_faraid"));
        list.add(new FeatureItem(27, "রক্তদান সেকশন", R.drawable.ic_feat_blood, android.graphics.Color.parseColor("#3C1215"), android.graphics.Color.parseColor("#EF4444"), "action_blood"));

        // Row 10: Asmaul Husna (99 Names), 6 Kalimas & Knowledge Battle
        list.add(new FeatureItem(28, "আল্লাহর ৯৯ নাম", R.drawable.ic_feat_allah_names, android.graphics.Color.parseColor("#0E3827"), android.graphics.Color.parseColor("#34D399"), "action_allah_names"));
        list.add(new FeatureItem(29, "৬ কালিমা", R.drawable.ic_feat_kalima, android.graphics.Color.parseColor("#382810"), android.graphics.Color.parseColor("#FBBF24"), "action_six_kalima"));
        list.add(new FeatureItem(30, "নলেজ ব্যাটেল", R.drawable.ic_feat_battle, android.graphics.Color.parseColor("#281238"), android.graphics.Color.parseColor("#C084FC"), "action_knowledge_battle"));

        return list;
    }

    private int resolveIconRes(Application app, String iconName) {
        if (iconName == null) return R.drawable.ic_feat_quran;
        if ("ic_clock".equals(iconName)) return R.drawable.ic_feat_salat;
        if ("ic_moon".equals(iconName)) return R.drawable.ic_feat_ramadan;
        int resId = app.getResources().getIdentifier(iconName, "drawable", app.getPackageName());
        return resId != 0 ? resId : R.drawable.ic_feat_quran;
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

    private static String formatTimeCountdown(long seconds, boolean isBn) {
        if (seconds <= 0) return isBn ? "০ সেকেন্ড" : "0 sec";
        long hrs = seconds / 3600;
        long mins = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hrs > 0) {
            sb.append(isBn ? BengaliNumberUtil.toBengali(hrs) : String.valueOf(hrs))
              .append(isBn ? " ঘণ্টা " : " hr ");
        }
        if (mins > 0 || hrs > 0) {
            sb.append(isBn ? BengaliNumberUtil.toBengali(mins) : String.valueOf(mins))
              .append(isBn ? " মিনিট " : " min ");
        }
        sb.append(isBn ? BengaliNumberUtil.toBengali(secs) : String.valueOf(secs))
          .append(isBn ? " সেকেন্ড" : " sec");
        return sb.toString().trim();
    }

    public void updateRealTimeCalculations() {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(getApplication());
        Calendar cal = Calendar.getInstance();

        HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getAdjustedHijriDate(getApplication(), cal);
        hijriDateLiveData.setValue(hijri.getFormatted(isBn));

        String gregDate = isBn ? HijriCalendarUtil.getRealGregorianDateBengali(cal) : HijriCalendarUtil.getRealGregorianDateEnglish(cal);
        if (com.devflux.deenone.core.calendar.CalendarSettingsManager.isBengaliCalendarEnabled(getApplication())) {
            com.devflux.deenone.utils.BengaliCalendarUtil.BengaliDateResult bDate =
                    com.devflux.deenone.utils.BengaliCalendarUtil.getBengaliDate(getApplication(), cal);
            gregDate = gregDate + " • " + bDate.getFormattedDate();
        }
        gregorianDateLiveData.setValue(gregDate);

        PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocationWithContext(
                getApplication(), currentCoordinates.latitude, currentCoordinates.longitude, currentCoordinates.timezone, cal
        );

        if (prayerRepo != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            PrayerScheduleEntity liveSchedule = new PrayerScheduleEntity(
                    sdf.format(cal.getTime()),
                    currentCoordinates.locationName,
                    res.fajrStr,
                    res.sunriseStr,
                    res.chashtRangeStr,
                    res.zohrStr,
                    res.asrStr,
                    res.maghribStr,
                    res.ishaStr,
                    res.sehriStr,
                    res.iftarStr
            );
            prayerRepo.updateSchedule(liveSchedule);
        }

        String waqtNameLoc = PrayerCalculator.getWaqtName(res.currentWaqtName, isBn);
        currentWaqtNameLiveData.setValue(waqtNameLoc);
        currentWaqtRangeLiveData.setValue(res.chashtRangeStr);

        long wMin = res.currentWaqtRemainingSeconds / 60;
        long wSec = res.currentWaqtRemainingSeconds % 60;
        if (isBn) {
            currentWaqtRemainingLiveData.setValue("ওয়াক্ত শেষ হতে বাকি: " + BengaliNumberUtil.toBengali(wMin) + " মিনিট " + BengaliNumberUtil.toBengali(wSec) + " সেকেন্ড");
        } else {
            currentWaqtRemainingLiveData.setValue("Time remaining: " + wMin + " min " + wSec + " sec");
        }
        currentWaqtProgressLiveData.setValue(res.currentWaqtProgressPercent);

        sunriseTimeLiveData.setValue(res.sunriseStr.toLowerCase());
        sunsetTimeLiveData.setValue(res.sunsetStr.toLowerCase());

        String nextNameLoc = PrayerCalculator.getWaqtName(res.nextPrayerName, isBn);
        nextPrayerNameLiveData.setValue(nextNameLoc);
        nextPrayerTimeLiveData.setValue(res.nextPrayerTimeStr);

        long zHours = res.nextPrayerRemainingSeconds / 3600;
        long zMinutes = (res.nextPrayerRemainingSeconds % 3600) / 60;
        long zSecs = res.nextPrayerRemainingSeconds % 60;
        String rawNextCountdown = String.format(java.util.Locale.ENGLISH, "%02d:%02d:%02d", zHours, zMinutes, zSecs);
        nextPrayerCountdownLiveData.setValue(isBn ? BengaliNumberUtil.toBengali(rawNextCountdown) : rawNextCountdown);

        String fastingTitle = PrayerCalculator.getFastingCountdownTitle(res.isFastingDaytime, isBn);
        fastingCountdownTitleLiveData.setValue(fastingTitle);

        long fHours = res.fastingRemainingSeconds / 3600;
        long fMinutes = (res.fastingRemainingSeconds % 3600) / 60;
        long fSecs = res.fastingRemainingSeconds % 60;
        String rawFastingCountdown = String.format(java.util.Locale.ENGLISH, "%02d : %02d : %02d", fHours, fMinutes, fSecs);
        String fastingValueStr = isBn ? BengaliNumberUtil.toBengali(rawFastingCountdown) : rawFastingCountdown;

        fastingCountdownValueLiveData.setValue(fastingValueStr);
        iftarCountdownLiveData.setValue(fastingValueStr);

        // Forbidden & Makruh Times status
        com.devflux.deenone.core.prayer.ForbiddenTimesCalculator.ForbiddenStatus forbidden =
                com.devflux.deenone.core.prayer.ForbiddenTimesCalculator.calculateForbiddenStatus(res, System.currentTimeMillis());
        forbiddenStatusLiveData.setValue(forbidden);

        // =========================================================================
        // Dynamic Prayer Widget State (Strictly matching reference images & rules)
        // =========================================================================
        long now = System.currentTimeMillis();
        java.text.SimpleDateFormat timeFmt = new java.text.SimpleDateFormat("h:mm a", java.util.Locale.ENGLISH);

        DynamicPrayerWidgetState widgetState = new DynamicPrayerWidgetState();
        widgetState.fajrMillis = res.fajrMillis;
        widgetState.sunriseMillis = res.sunriseMillis;
        widgetState.zohrMillis = res.zohrMillis;
        widgetState.asrMillis = res.asrMillis;
        widgetState.sunsetMillis = res.sunsetMillis;
        widgetState.ishaMillis = res.ishaMillis;

        long sunriseStart = res.sunriseMillis;
        long sunriseEnd = res.sunriseMillis + (15 * 60 * 1000);

        long zawalStart = res.zohrMillis - (12 * 60 * 1000);
        long zawalEnd = res.zohrMillis;

        long sunsetStart = res.sunsetMillis - (15 * 60 * 1000);
        long sunsetEnd = res.sunsetMillis;

        if (now >= sunriseStart && now < sunriseEnd) {
            // 1. In Sunrise Forbidden Time
            widgetState.isForbidden = true;
            widgetState.forbiddenTitle = isBn ? "বর্তমান নিষিদ্ধ সময়: সূর্যোদয়" : "Current Forbidden Time: Sunrise";
            widgetState.forbiddenRange = timeFmt.format(new java.util.Date(sunriseStart)).toLowerCase() + " - " + timeFmt.format(new java.util.Date(sunriseEnd)).toLowerCase();
            long remSec = Math.max(0, (sunriseEnd - now) / 1000);
            widgetState.forbiddenRemainingText = (isBn ? "নিষিদ্ধ সময় শেষ হতে বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
            long total = sunriseEnd - sunriseStart;
            long elapsed = now - sunriseStart;
            widgetState.forbiddenProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else if (now >= zawalStart && now < zawalEnd) {
            // 2. In Zawal / Midday Forbidden Time
            widgetState.isForbidden = true;
            widgetState.forbiddenTitle = isBn ? "বর্তমান নিষিদ্ধ সময়: যাওয়াল" : "Current Forbidden Time: Zawal";
            widgetState.forbiddenRange = timeFmt.format(new java.util.Date(zawalStart)).toLowerCase() + " - " + res.zohrStr.toLowerCase();
            long remSec = Math.max(0, (zawalEnd - now) / 1000);
            widgetState.forbiddenRemainingText = (isBn ? "নিষিদ্ধ সময় শেষ হতে বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
            long total = zawalEnd - zawalStart;
            long elapsed = now - zawalStart;
            widgetState.forbiddenProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else if (now >= sunsetStart && now < sunsetEnd) {
            // 3. In Sunset Forbidden Time
            widgetState.isForbidden = true;
            widgetState.forbiddenTitle = isBn ? "বর্তমান নিষিদ্ধ সময়: সূর্যাস্ত" : "Current Forbidden Time: Sunset";
            widgetState.forbiddenRange = timeFmt.format(new java.util.Date(sunsetStart)).toLowerCase() + " - " + res.sunsetStr.toLowerCase();
            long remSec = Math.max(0, (sunsetEnd - now) / 1000);
            widgetState.forbiddenRemainingText = (isBn ? "নিষিদ্ধ সময় শেষ হতে বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
            long total = sunsetEnd - sunsetStart;
            long elapsed = now - sunsetStart;
            widgetState.forbiddenProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else {
            // Regular Prayer Waqt
            widgetState.isForbidden = false;

            if (now < res.fajrMillis) {
                widgetState.waqtName = isBn ? "তাহাজ্জুদ" : "Tahajjud";
                widgetState.waqtRange = res.lastThirdOfNightStr.toLowerCase() + " - " + res.fajrStr.toLowerCase();
                long remSec = Math.max(0, (res.fajrMillis - now) / 1000);
                widgetState.waqtRemainingText = (isBn ? "সময় বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
                long total = Math.max(1, res.fajrMillis - res.lastThirdOfNightMillis);
                long elapsed = Math.max(0, now - res.lastThirdOfNightMillis);
                widgetState.waqtProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
            } else if (now < res.sunriseMillis) {
                widgetState.waqtName = isBn ? "ফজর" : "Fajr";
                widgetState.waqtRange = res.fajrStr.toLowerCase() + " - " + res.sunriseStr.toLowerCase();
                long remSec = Math.max(0, (res.sunriseMillis - now) / 1000);
                widgetState.waqtRemainingText = (isBn ? "সময় বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
                long total = Math.max(1, res.sunriseMillis - res.fajrMillis);
                long elapsed = Math.max(0, now - res.fajrMillis);
                widgetState.waqtProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
            } else if (now < zawalStart) {
                widgetState.waqtName = isBn ? "চাশত" : "Chasht";
                widgetState.waqtRange = timeFmt.format(new java.util.Date(sunriseEnd)).toLowerCase() + " - " + timeFmt.format(new java.util.Date(zawalStart)).toLowerCase();
                long remSec = Math.max(0, (zawalStart - now) / 1000);
                widgetState.waqtRemainingText = (isBn ? "সময় বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
                long total = Math.max(1, zawalStart - sunriseEnd);
                long elapsed = Math.max(0, now - sunriseEnd);
                widgetState.waqtProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
            } else if (now < res.asrMillis) {
                widgetState.waqtName = res.isFriday ? (isBn ? "জুম্মা" : "Jummah") : (isBn ? "জোহর" : "Dhuhr");
                widgetState.waqtRange = res.zohrStr.toLowerCase() + " - " + res.asrStr.toLowerCase();
                long remSec = Math.max(0, (res.asrMillis - now) / 1000);
                widgetState.waqtRemainingText = (isBn ? "সময় বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
                long total = Math.max(1, res.asrMillis - res.zohrMillis);
                long elapsed = Math.max(0, now - res.zohrMillis);
                widgetState.waqtProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
            } else if (now < sunsetStart) {
                widgetState.waqtName = isBn ? "আসর" : "Asr";
                widgetState.waqtRange = res.asrStr.toLowerCase() + " - " + timeFmt.format(new java.util.Date(sunsetStart)).toLowerCase();
                long remSec = Math.max(0, (sunsetStart - now) / 1000);
                widgetState.waqtRemainingText = (isBn ? "সময় বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
                long total = Math.max(1, sunsetStart - res.asrMillis);
                long elapsed = Math.max(0, now - res.asrMillis);
                widgetState.waqtProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
            } else if (now < res.ishaMillis) {
                widgetState.waqtName = isBn ? "মাগরিব" : "Maghrib";
                widgetState.waqtRange = res.maghribStr.toLowerCase() + " - " + res.ishaStr.toLowerCase();
                long remSec = Math.max(0, (res.ishaMillis - now) / 1000);
                widgetState.waqtRemainingText = (isBn ? "সময় বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
                long total = Math.max(1, res.ishaMillis - res.maghribMillis);
                long elapsed = Math.max(0, now - res.maghribMillis);
                widgetState.waqtProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
            } else {
                widgetState.waqtName = isBn ? "এশা" : "Isha";
                widgetState.waqtRange = res.ishaStr.toLowerCase() + " - " + res.fajrStr.toLowerCase();
                long nextFajr = res.fajrMillis + (24 * 3600 * 1000);
                long remSec = Math.max(0, (nextFajr - now) / 1000);
                widgetState.waqtRemainingText = (isBn ? "সময় বাকি: " : "Time remaining: ") + formatTimeCountdown(remSec, isBn);
                long total = Math.max(1, nextFajr - res.ishaMillis);
                long elapsed = Math.max(0, now - res.ishaMillis);
                widgetState.waqtProgress = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
            }
        }

        dynamicPrayerWidgetLiveData.setValue(widgetState);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }
}
