package com.devflux.deenone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.core.content.DailyAyahHadithManager;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WidgetDataHelper {

    public static final String PREFS_TASBIH = "deanone_widget_tasbih_prefs";
    public static final String KEY_TASBIH_COUNT = "widget_tasbih_count";
    public static final String KEY_TASBIH_INDEX = "widget_tasbih_index";

    public static final String[] TASBIH_ITEMS = {
        "সুবহানাল্লাহ (SubhanAllah)",
        "আলহামদুলিল্লাহ (Alhamdulillah)",
        "আল্লাহু আকবার (Allahu Akbar)",
        "লা ইলাহা ইল্লাল্লাহ (La ilaha illallah)",
        "আস্তাগফিরুল্লাহ (Astaghfirullah)",
        "সুবহানাল্লাহি ওয়া বিহামদিহী"
    };

    public static class WidgetPrayerData {
        public String locationName;
        public String hijriDateBengali;
        public String hijriDateShort;
        public String hijriDay;
        public String hijriMonth;
        public String gregorianDateBengali;
        public String gregorianDayOfWeek;
        public String gregorianDateShort;
        public String updatedTimeStr;

        public String currentWaqtName;
        public String currentWaqtKey;
        public String currentWaqtRange;
        public String nextWaqtName;
        public String nextWaqtTime;
        public String nextWaqtCountdown;
        public String currentWaqtCountdown;
        public String currentWaqtEndTimeStr;

        public String fajrTimeStr;
        public String sunriseTimeStr;
        public String dhuhrTimeStr;
        public String asrTimeStr;
        public String maghribTimeStr;
        public String ishaTimeStr;

        public String fajrRangeStr;
        public String dhuhrRangeStr;
        public String asrRangeStr;
        public String maghribRangeStr;
        public String ishaRangeStr;

        public String sehriTimeStr;
        public String iftarTimeStr;
        public String sunsetTimeStr;

        public String sehriCountdownStr;
        public String sehriStartTimeStr;
        public String iftarCountdownStr;
        public String iftarEndTimeStr;

        public String qiblaBearingDeg;
        public String islamicEventBadge;

        public String dailyAyahArabic;
        public String dailyAyahBengali;
        public String dailyAyahReference;

        public String dailyHadithBengali;
        public String dailyHadithReference;

        public int tasbihCount;
        public String tasbihZikrName;
    }

    public static WidgetPrayerData getWidgetData(Context context) {
        if (context == null) return new WidgetPrayerData();

        LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
        Calendar nowCal = Calendar.getInstance();
        PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocationWithContext(
            context, coords.latitude, coords.longitude, coords.timezone, nowCal
        );

        WidgetPrayerData data = new WidgetPrayerData();
        data.locationName = coords.locationName != null && !coords.locationName.isEmpty() ? coords.locationName : "ঢাকা";

        // Hijri date with user offset
        HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getAdjustedHijriDate(context, nowCal);
        data.hijriDay = BengaliNumberUtil.toBengali(hijri.day);
        data.hijriMonth = hijri.monthNameBengali != null ? hijri.monthNameBengali : "রমাদান";
        data.hijriDateBengali = hijri.getFormattedBengali();
        data.hijriDateShort = data.hijriDay + " " + data.hijriMonth + ", " + getDayOfWeekBengali(nowCal);

        // Gregorian date
        SimpleDateFormat sdfDay = new SimpleDateFormat("d MMMM", new Locale("bn", "BD"));
        SimpleDateFormat sdfFull = new SimpleDateFormat("d MMMM, yyyy", new Locale("bn", "BD"));
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        data.gregorianDateShort = BengaliNumberUtil.toBengali(sdfDay.format(nowCal.getTime()));
        String gregFull = BengaliNumberUtil.toBengali(sdfFull.format(nowCal.getTime()));
        if (com.devflux.deenone.core.calendar.CalendarSettingsManager.isBengaliCalendarEnabled(context)) {
            com.devflux.deenone.utils.BengaliCalendarUtil.BengaliDateResult bDate =
                    com.devflux.deenone.utils.BengaliCalendarUtil.getBengaliDate(nowCal);
            gregFull = gregFull + " • " + bDate.getShortFormatted();
        }
        data.gregorianDateBengali = gregFull;
        data.gregorianDayOfWeek = getDayOfWeekBengali(nowCal);
        data.updatedTimeStr = BengaliNumberUtil.toBengali(sdfTime.format(nowCal.getTime()));

        // Prayer Times formatted strings
        data.fajrTimeStr = res.fajrStr != null ? res.fajrStr : "০৪:৩০ AM";
        data.sunriseTimeStr = res.sunriseStr != null ? res.sunriseStr : "০৫:৫০ AM";
        data.dhuhrTimeStr = res.zohrStr != null ? res.zohrStr : "১২:০৫ PM";
        data.asrTimeStr = res.asrStr != null ? res.asrStr : "০৪:৩০ PM";
        data.sunsetTimeStr = res.sunsetStr != null ? res.sunsetStr : "০৬:১৫ PM";
        data.maghribTimeStr = res.maghribStr != null ? res.maghribStr : "০৬:১৫ PM";
        data.ishaTimeStr = res.ishaStr != null ? res.ishaStr : "০৭:৩০ PM";

        data.fajrRangeStr = data.fajrTimeStr + " - " + data.sunriseTimeStr;
        data.dhuhrRangeStr = data.dhuhrTimeStr + " - " + data.asrTimeStr;
        data.asrRangeStr = data.asrTimeStr + " - " + data.maghribTimeStr;
        data.maghribRangeStr = data.maghribTimeStr + " - " + data.ishaTimeStr;
        data.ishaRangeStr = data.ishaTimeStr + " - " + data.fajrTimeStr;

        data.sehriTimeStr = res.sehriStr != null ? res.sehriStr : data.fajrTimeStr;
        data.iftarTimeStr = res.iftarStr != null ? res.iftarStr : data.maghribTimeStr;

        // Determine current waqt & countdowns
        long now = nowCal.getTimeInMillis();
        long currentWaqtEnd = res.sunriseMillis;
        long nextWaqtTimeMillis = res.zohrMillis;

        if (now < res.fajrMillis) {
            data.currentWaqtName = "তাহাজ্জুদ";
            data.currentWaqtKey = "tahajjud";
            data.currentWaqtRange = res.lastThirdOfNightStr + " - " + res.fajrStr;
            data.nextWaqtName = "ফজর";
            data.nextWaqtTime = res.fajrStr;
            currentWaqtEnd = res.fajrMillis;
            nextWaqtTimeMillis = res.fajrMillis;
            data.currentWaqtEndTimeStr = res.fajrStr;
        } else if (now < res.sunriseMillis) {
            data.currentWaqtName = "ফজর";
            data.currentWaqtKey = "fajr";
            data.currentWaqtRange = data.fajrRangeStr;
            data.nextWaqtName = "সূর্যোদয়";
            data.nextWaqtTime = res.sunriseStr;
            currentWaqtEnd = res.sunriseMillis;
            nextWaqtTimeMillis = res.sunriseMillis;
            data.currentWaqtEndTimeStr = res.sunriseStr;
        } else if (now < res.zohrMillis) {
            data.currentWaqtName = "ইশরাক / চাশত";
            data.currentWaqtKey = "chasht";
            data.currentWaqtRange = res.sunriseStr + " - " + res.zohrStr;
            data.nextWaqtName = "জোহর";
            data.nextWaqtTime = res.zohrStr;
            currentWaqtEnd = res.zohrMillis;
            nextWaqtTimeMillis = res.zohrMillis;
            data.currentWaqtEndTimeStr = res.zohrStr;
        } else if (now < res.asrMillis) {
            data.currentWaqtName = "জোহর";
            data.currentWaqtKey = "dhuhr";
            data.currentWaqtRange = data.dhuhrRangeStr;
            data.nextWaqtName = "আসর";
            data.nextWaqtTime = res.asrStr;
            currentWaqtEnd = res.asrMillis;
            nextWaqtTimeMillis = res.asrMillis;
            data.currentWaqtEndTimeStr = res.asrStr;
        } else if (now < res.maghribMillis) {
            data.currentWaqtName = "আসর";
            data.currentWaqtKey = "asr";
            data.currentWaqtRange = data.asrRangeStr;
            data.nextWaqtName = "মাগরিব";
            data.nextWaqtTime = res.maghribStr;
            currentWaqtEnd = res.maghribMillis;
            nextWaqtTimeMillis = res.maghribMillis;
            data.currentWaqtEndTimeStr = res.maghribStr;
        } else if (now < res.ishaMillis) {
            data.currentWaqtName = "মাগরিব";
            data.currentWaqtKey = "maghrib";
            data.currentWaqtRange = data.maghribRangeStr;
            data.nextWaqtName = "এশা";
            data.nextWaqtTime = res.ishaStr;
            currentWaqtEnd = res.ishaMillis;
            nextWaqtTimeMillis = res.ishaMillis;
            data.currentWaqtEndTimeStr = res.ishaStr;
        } else {
            data.currentWaqtName = "এশা";
            data.currentWaqtKey = "isha";
            data.currentWaqtRange = data.ishaRangeStr;
            data.nextWaqtName = "ফজর";
            data.nextWaqtTime = res.fajrStr;
            currentWaqtEnd = res.fajrMillis + (24 * 3600 * 1000);
            nextWaqtTimeMillis = res.fajrMillis + (24 * 3600 * 1000);
            data.currentWaqtEndTimeStr = res.fajrStr;
        }

        // Format countdowns
        long currentWaqtDiffSec = Math.max(0, (currentWaqtEnd - now) / 1000);
        data.currentWaqtCountdown = formatDurationHMS(currentWaqtDiffSec);

        long nextWaqtDiffSec = Math.max(0, (nextWaqtTimeMillis - now) / 1000);
        long nHours = nextWaqtDiffSec / 3600;
        long nMins = (nextWaqtDiffSec % 3600) / 60;
        if (nHours > 0) {
            data.nextWaqtCountdown = BengaliNumberUtil.toBengali(nHours) + " ঘণ্টা " + BengaliNumberUtil.toBengali(nMins) + " মিনিট";
        } else {
            data.nextWaqtCountdown = BengaliNumberUtil.toBengali(nMins) + " মিনিট";
        }

        // Fasting / Saom countdowns
        long iftarTarget = res.iftarMillis;
        if (now > res.iftarMillis) {
            iftarTarget += (24 * 3600 * 1000);
        }
        long iftarDiffSec = Math.max(0, (iftarTarget - now) / 1000);
        data.iftarCountdownStr = formatDurationHMS(iftarDiffSec);
        data.iftarEndTimeStr = data.iftarTimeStr;

        long sehriTarget = res.sehriMillis;
        if (now > res.sehriMillis) {
            sehriTarget += (24 * 3600 * 1000);
        }
        long sehriDiffSec = Math.max(0, (sehriTarget - now) / 1000);
        data.sehriCountdownStr = formatDurationHMS(sehriDiffSec);
        data.sehriStartTimeStr = data.sehriTimeStr;

        // Qibla bearing calculation
        double kaabaLat = Math.toRadians(21.4225);
        double kaabaLng = Math.toRadians(39.8262);
        double userLat = Math.toRadians(coords.latitude);
        double userLng = Math.toRadians(coords.longitude);
        double dLng = kaabaLng - userLng;
        double y = Math.sin(dLng);
        double x = Math.cos(userLat) * Math.tan(kaabaLat) - Math.sin(userLat) * Math.cos(dLng);
        double qiblaDegrees = (Math.toDegrees(Math.atan2(y, x)) + 360.0) % 360.0;
        data.qiblaBearingDeg = BengaliNumberUtil.toBengali((int) Math.round(qiblaDegrees)) + "°";

        // Islamic event badge
        int dayOfWeek = nowCal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.FRIDAY) {
            data.islamicEventBadge = "জুমু'আ মোবারক";
        } else if (hijri.monthIndex == 8) { // Ramadan
            data.islamicEventBadge = "রমাদানুল মোবারক";
        } else {
            data.islamicEventBadge = "দৈনিক ওয়াক্ত";
        }

        // Daily Ayah & Hadith
        try {
            DailyAyahHadithManager.DailyDisplayContent content =
                DailyAyahHadithManager.getInstance().getOrRefreshDailyContent(context);
            if (content != null && content.ayah != null) {
                data.dailyAyahArabic = content.ayah.arabic != null ? content.ayah.arabic : "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";
                data.dailyAyahBengali = content.ayah.bengali != null ? content.ayah.bengali : "আল্লাহর নামে শুরু করছি যিনি পরম করুণাময় ও অসীম দয়ালু।";
                data.dailyAyahReference = (content.ayah.surahName != null ? content.ayah.surahName : "সূরা আল-ফাতিহা") + " (" + BengaliNumberUtil.toBengali(content.ayah.ayahNumber) + ")";
            } else {
                data.dailyAyahArabic = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَوْقُوتًا";
                data.dailyAyahBengali = "নিশ্চয়ই নির্দিষ্ট সময়ে নামাজ কায়েম করা মুমিনদের উপর ফরজ।";
                data.dailyAyahReference = "সূরা আন-নিসা (১০৩)";
            }

            if (content != null && content.hadith != null) {
                data.dailyHadithBengali = content.hadith.bengali != null ? content.hadith.bengali : "যে ব্যক্তি নিয়মিত পাঁচ ওয়াক্ত নামাজ আদায় করে, তা তার জন্য কিয়ামতের দিন নূর ও পরিত্রাণের কারণ হবে।";
                data.dailyHadithReference = content.hadith.reference != null ? content.hadith.reference : "সহীহ মুসলিম";
            } else {
                data.dailyHadithBengali = "তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সে, যে কুরআন শিক্ষা করে এবং অন্যকে শিক্ষা দেয়।";
                data.dailyHadithReference = "সহীহ বুখারী: ৫০২৭";
            }
        } catch (Exception e) {
            data.dailyAyahArabic = "إِنَّ مَعَ الْعُسْرِ يُسْرًا";
            data.dailyAyahBengali = "নিশ্চয়ই কষ্টের সাথে স্বস্তি রয়েছে।";
            data.dailyAyahReference = "সূরা আল-ইনশিরাহ (৬)";
            data.dailyHadithBengali = "পরিষ্কার-পরিচ্ছন্নতা ঈমানের অঙ্গ।";
            data.dailyHadithReference = "সহীহ মুসলিম";
        }

        // Tasbih state
        SharedPreferences tasbihPrefs = context.getSharedPreferences(PREFS_TASBIH, Context.MODE_PRIVATE);
        data.tasbihCount = tasbihPrefs.getInt(KEY_TASBIH_COUNT, 0);
        int zikrIdx = tasbihPrefs.getInt(KEY_TASBIH_INDEX, 0);
        if (zikrIdx < 0 || zikrIdx >= TASBIH_ITEMS.length) zikrIdx = 0;
        data.tasbihZikrName = TASBIH_ITEMS[zikrIdx];

        return data;
    }

    public static String formatDurationHMS(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        String h = hours < 10 ? "0" + hours : String.valueOf(hours);
        String m = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        String s = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
        return BengaliNumberUtil.toBengali(h + ":" + m + ":" + s);
    }

    public static String getDayOfWeekBengali(Calendar cal) {
        int day = cal.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SATURDAY: return "শনিবার";
            case Calendar.SUNDAY: return "রবিবার";
            case Calendar.MONDAY: return "সোমবার";
            case Calendar.TUESDAY: return "মঙ্গলবার";
            case Calendar.WEDNESDAY: return "বুধবার";
            case Calendar.THURSDAY: return "বৃহস্পতিবার";
            case Calendar.FRIDAY: return "শুক্রবার";
            default: return "আজ";
        }
    }

    public static PendingIntent getOpenAppPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static PendingIntent getRefreshPendingIntent(Context context) {
        Intent intent = new Intent(context, WidgetActionReceiver.class);
        intent.setAction(WidgetActionReceiver.ACTION_WIDGET_REFRESH);
        return PendingIntent.getBroadcast(
            context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static PendingIntent getTasbihCountPendingIntent(Context context) {
        Intent intent = new Intent(context, WidgetActionReceiver.class);
        intent.setAction(WidgetActionReceiver.ACTION_TASBIH_COUNT);
        return PendingIntent.getBroadcast(
            context, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static PendingIntent getTasbihResetPendingIntent(Context context) {
        Intent intent = new Intent(context, WidgetActionReceiver.class);
        intent.setAction(WidgetActionReceiver.ACTION_TASBIH_RESET);
        return PendingIntent.getBroadcast(
            context, 103, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static PendingIntent getNextAyahHadithPendingIntent(Context context) {
        Intent intent = new Intent(context, WidgetActionReceiver.class);
        intent.setAction(WidgetActionReceiver.ACTION_NEXT_AYAH_HADITH);
        return PendingIntent.getBroadcast(
            context, 104, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public static void updateAllWidgets(Context context) {
        if (context == null) return;
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            Class<?>[] providers = new Class<?>[] {
                DeenOnePrayerAppWidgetProvider.class,
                WidgetDaySummaryTransparentProvider.class,
                WidgetSalatCountdownScheduleProvider.class,
                WidgetSalatCountdownCompactProvider.class,
                WidgetSaomTimerProvider.class,
                WidgetMuslimsDayCompactProvider.class,
                WidgetPrayerTimesListProvider.class,
                WidgetPrayerTimesListTransparentProvider.class,
                WidgetCompactPrayerBarProvider.class,
                WidgetSingleWaqtFocusProvider.class,
                WidgetDailyAyahProvider.class,
                WidgetDailyHadithProvider.class,
                WidgetDigitalTasbihProvider.class,
                WidgetIslamicCalendarProvider.class,
                WidgetQiblaSnapshotProvider.class
            };

            for (Class<?> providerClass : providers) {
                try {
                    ComponentName cn = new ComponentName(context, providerClass);
                    int[] ids = appWidgetManager.getAppWidgetIds(cn);
                    if (ids != null && ids.length > 0) {
                        Intent updateIntent = new Intent(context, providerClass);
                        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                        context.sendBroadcast(updateIntent);
                    }
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
    }
}
