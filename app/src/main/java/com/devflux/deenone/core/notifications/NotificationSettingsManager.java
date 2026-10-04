package com.devflux.deenone.core.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import com.devflux.deenone.R;

public class NotificationSettingsManager {

    private static final String PREF_NAME = "deanone_notification_settings";

    public static final String KEY_PRAYER_NOTIFS = "pref_prayer_notifs";
    public static final String KEY_ADHAN_ENABLED = "pref_adhan_enabled";
    public static final String KEY_ADHAN_RECITER = "pref_adhan_reciter";
    public static final String KEY_PRE_PRAYER_MINUTES = "pref_pre_prayer_minutes";
    public static final String KEY_SEHRI_IFTAR = "pref_sehri_iftar";
    public static final String KEY_DAILY_AMAL = "pref_daily_amal";
    public static final String KEY_DAILY_DUA_HADITH = "pref_daily_dua_hadith";
    public static final String KEY_DAILY_QUIZ = "pref_daily_quiz";
    public static final String KEY_ADMIN_ANNOUNCEMENTS = "pref_admin_announcements";
    public static final String KEY_VIBRATION = "pref_vibration";
    public static final String KEY_QURAN_REMINDER = "pref_quran_reminder";
    public static final String KEY_DHIKR_REMINDER = "pref_dhikr_reminder";
    public static final String KEY_TAHAJJUD_REMINDER = "pref_tahajjud_reminder";
    public static final String KEY_SUNNAH_FASTING_REMINDER = "pref_sunnah_fasting_reminder";
    public static final String KEY_ISLAMIC_EVENTS_REMINDER = "pref_islamic_events_reminder";
    public static final String KEY_JUMUAH_REMINDER = "pref_jumuah_reminder";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isPrayerNotifsEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_PRAYER_NOTIFS, true);
    }

    public static void setPrayerNotifsEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_PRAYER_NOTIFS, enabled).apply();
    }

    public static boolean isAdhanEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ADHAN_ENABLED, true);
    }

    public static void setAdhanEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ADHAN_ENABLED, enabled).apply();
    }

    public static String getAdhanReciter(Context context) {
        return getPrefs(context).getString(KEY_ADHAN_RECITER, "makkah");
    }

    public static void setAdhanReciter(Context context, String reciter) {
        getPrefs(context).edit().putString(KEY_ADHAN_RECITER, reciter).apply();
    }

    public static int getPrePrayerMinutes(Context context) {
        return getPrefs(context).getInt(KEY_PRE_PRAYER_MINUTES, 10);
    }

    public static void setPrePrayerMinutes(Context context, int minutes) {
        getPrefs(context).edit().putInt(KEY_PRE_PRAYER_MINUTES, minutes).apply();
    }

    public static boolean isSehriIftarEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_SEHRI_IFTAR, true);
    }

    public static void setSehriIftarEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_SEHRI_IFTAR, enabled).apply();
    }

    public static boolean isDailyAmalEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_DAILY_AMAL, true);
    }

    public static void setDailyAmalEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DAILY_AMAL, enabled).apply();
    }

    public static boolean isDailyDuaHadithEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_DAILY_DUA_HADITH, true);
    }

    public static void setDailyDuaHadithEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DAILY_DUA_HADITH, enabled).apply();
    }

    public static boolean isDailyQuizEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_DAILY_QUIZ, true);
    }

    public static void setDailyQuizEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DAILY_QUIZ, enabled).apply();
    }

    public static boolean isAdminAnnouncementsEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ADMIN_ANNOUNCEMENTS, true);
    }

    public static void setAdminAnnouncementsEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ADMIN_ANNOUNCEMENTS, enabled).apply();
    }

    public static boolean isVibrationEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_VIBRATION, true);
    }

    public static void setVibrationEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_VIBRATION, enabled).apply();
    }

    public static final String KEY_NOTIFICATION_SOUND_URI = "pref_notification_sound_uri";

    public static String getCustomNotificationSoundUri(Context context) {
        return getPrefs(context).getString(KEY_NOTIFICATION_SOUND_URI, "");
    }

    public static void setCustomNotificationSoundUri(Context context, String uriStr) {
        getPrefs(context).edit().putString(KEY_NOTIFICATION_SOUND_URI, uriStr != null ? uriStr : "").apply();
    }

    public static final String KEY_VIBRATION_PATTERN = "pref_vibration_pattern";

    public static String getVibrationPattern(Context context) {
        return getPrefs(context).getString(KEY_VIBRATION_PATTERN, "STRONG_ALARM");
    }

    public static void setVibrationPattern(Context context, String pattern) {
        getPrefs(context).edit().putString(KEY_VIBRATION_PATTERN, pattern).apply();
    }

    public static boolean isQuranReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_QURAN_REMINDER, true);
    }

    public static void setQuranReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_QURAN_REMINDER, enabled).apply();
    }

    public static boolean isDhikrReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_DHIKR_REMINDER, true);
    }

    public static void setDhikrReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DHIKR_REMINDER, enabled).apply();
    }

    public static boolean isTahajjudReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_TAHAJJUD_REMINDER, true);
    }

    public static void setTahajjudReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_TAHAJJUD_REMINDER, enabled).apply();
    }

    public static boolean isSunnahFastingReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_SUNNAH_FASTING_REMINDER, true);
    }

    public static void setSunnahFastingReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_SUNNAH_FASTING_REMINDER, enabled).apply();
    }

    public static boolean isIslamicEventsReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ISLAMIC_EVENTS_REMINDER, true);
    }

    public static void setIslamicEventsReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ISLAMIC_EVENTS_REMINDER, enabled).apply();
    }

    public static boolean isJumuahReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_JUMUAH_REMINDER, true);
    }

    public static void setJumuahReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_JUMUAH_REMINDER, enabled).apply();
    }

    public static final String KEY_LOCKSCREEN_WIDGET = "pref_lockscreen_widget";

    public static boolean isLockScreenWidgetEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_LOCKSCREEN_WIDGET, true);
    }

    public static void setLockScreenWidgetEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_LOCKSCREEN_WIDGET, enabled).apply();
    }

    // ==========================================
    // Ramadan & Fasting Settings
    // ==========================================
    public static final String KEY_RAMADAN_MASTER_ENABLED = "pref_ramadan_master_enabled";
    public static final String KEY_RAMADAN_SEHRI_ENABLED = "pref_ramadan_sehri_enabled";
    public static final String KEY_RAMADAN_SEHRI_OFFSET_MINUTES = "pref_ramadan_sehri_offset_minutes";
    public static final String KEY_RAMADAN_SEHRI_SOUND_TYPE = "pref_ramadan_sehri_sound_type";
    public static final String KEY_RAMADAN_SEHRI_VIBRATION = "pref_ramadan_sehri_vibration";
    public static final String KEY_RAMADAN_IFTAR_ENABLED = "pref_ramadan_iftar_enabled";
    public static final String KEY_RAMADAN_IFTAR_OFFSET_MINUTES = "pref_ramadan_iftar_offset_minutes";
    public static final String KEY_RAMADAN_IFTAR_SOUND_TYPE = "pref_ramadan_iftar_sound_type";
    public static final String KEY_RAMADAN_IFTAR_VIBRATION = "pref_ramadan_iftar_vibration";
    public static final String KEY_RAMADAN_TARAWEEH_ENABLED = "pref_ramadan_taraweeh_enabled";
    public static final String KEY_RAMADAN_DUA_ENABLED = "pref_ramadan_dua_enabled";
    public static final String KEY_SUNNAH_MONTHU_ENABLED = "pref_sunnah_monthu_enabled";
    public static final String KEY_SUNNAH_AYYAM_BEED_ENABLED = "pref_sunnah_ayyam_beed_enabled";

    public static boolean isRamadanReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_MASTER_ENABLED, false);
    }

    public static void setRamadanReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_MASTER_ENABLED, enabled).apply();
    }

    public static boolean isRamadanSehriEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_SEHRI_ENABLED, false);
    }

    public static void setRamadanSehriEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_SEHRI_ENABLED, enabled).apply();
    }

    public static final String KEY_RAMADAN_SEHRI_ALARM_HOUR = "pref_ramadan_sehri_alarm_hour";
    public static final String KEY_RAMADAN_SEHRI_ALARM_MINUTE = "pref_ramadan_sehri_alarm_minute";
    public static final String KEY_RAMADAN_SEHRI_IS_CUSTOM_TIME = "pref_ramadan_sehri_is_custom_time";

    public static int getRamadanSehriAlarmHour(Context context) {
        return getPrefs(context).getInt(KEY_RAMADAN_SEHRI_ALARM_HOUR, 3);
    }

    public static void setRamadanSehriAlarmHour(Context context, int hour) {
        getPrefs(context).edit().putInt(KEY_RAMADAN_SEHRI_ALARM_HOUR, hour).apply();
    }

    public static int getRamadanSehriAlarmMinute(Context context) {
        return getPrefs(context).getInt(KEY_RAMADAN_SEHRI_ALARM_MINUTE, 30);
    }

    public static void setRamadanSehriAlarmMinute(Context context, int minute) {
        getPrefs(context).edit().putInt(KEY_RAMADAN_SEHRI_ALARM_MINUTE, minute).apply();
    }

    public static boolean isRamadanSehriCustomTime(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_SEHRI_IS_CUSTOM_TIME, false);
    }

    public static void setRamadanSehriCustomTime(Context context, boolean isCustom) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_SEHRI_IS_CUSTOM_TIME, isCustom).apply();
    }

    public static String getRamadanSehriSoundType(Context context) {
        return getPrefs(context).getString(KEY_RAMADAN_SEHRI_SOUND_TYPE, "allah_hu_allah_default");
    }

    public static void setRamadanSehriSoundType(Context context, String soundType) {
        getPrefs(context).edit().putString(KEY_RAMADAN_SEHRI_SOUND_TYPE, soundType).apply();
    }

    public static int getRamadanSehriSoundResId(String soundKey) {
        if (soundKey == null) return R.raw.read_allah_hu_allah_default;
        switch (soundKey) {
            case "islamic_ringtone":
                return R.raw.read_islamic_rintone;
            case "alhamdulillah_khabib":
                return R.raw.read_alhamdulillah_khabib;
            case "allah_ho_allah_beautiful":
                return R.raw.read_allah_ho_allah_beautiful;
            case "awesome_ramadan":
                return R.raw.read_awesome_ramadan;
            case "fs_ramadan":
                return R.raw.read_fs_ramadan;
            case "islamic_spiritual":
                return R.raw.read_islamic_spiritual;
            case "muhammad_saw":
                return R.raw.read_muhammad_saw;
            case "salam_e_ajizana":
                return R.raw.read_salam_e_ajizana;
            case "allah_hu_allah_default":
            default:
                return R.raw.read_allah_hu_allah_default;
        }
    }

    public static final String KEY_RAMADAN_SEHRI_REPEAT_COUNT = "pref_ramadan_sehri_repeat_count";

    public static int getRamadanSehriRepeatCount(Context context) {
        return getPrefs(context).getInt(KEY_RAMADAN_SEHRI_REPEAT_COUNT, 3);
    }

    public static void setRamadanSehriRepeatCount(Context context, int count) {
        if (count < 1) count = 1;
        if (count > 5) count = 5;
        getPrefs(context).edit().putInt(KEY_RAMADAN_SEHRI_REPEAT_COUNT, count).apply();
    }

    public static boolean isRamadanSehriVibrationEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_SEHRI_VIBRATION, true);
    }

    public static void setRamadanSehriVibrationEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_SEHRI_VIBRATION, enabled).apply();
    }

    public static boolean isSunnahMonThuEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_SUNNAH_MONTHU_ENABLED, true);
    }

    public static void setSunnahMonThuEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_SUNNAH_MONTHU_ENABLED, enabled).apply();
    }

    public static boolean isSunnahAyyamBeedEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_SUNNAH_AYYAM_BEED_ENABLED, true);
    }

    public static void setSunnahAyyamBeedEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_SUNNAH_AYYAM_BEED_ENABLED, enabled).apply();
    }

    public static int getRamadanSehriOffsetMinutes(Context context) {
        return getPrefs(context).getInt(KEY_RAMADAN_SEHRI_OFFSET_MINUTES, 30);
    }

    public static void setRamadanSehriOffsetMinutes(Context context, int minutes) {
        getPrefs(context).edit().putInt(KEY_RAMADAN_SEHRI_OFFSET_MINUTES, minutes).apply();
    }

    public static boolean isRamadanIftarEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_IFTAR_ENABLED, false);
    }

    public static void setRamadanIftarEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_IFTAR_ENABLED, enabled).apply();
    }

    public static int getRamadanIftarOffsetMinutes(Context context) {
        return getPrefs(context).getInt(KEY_RAMADAN_IFTAR_OFFSET_MINUTES, 0);
    }

    public static void setRamadanIftarOffsetMinutes(Context context, int minutes) {
        getPrefs(context).edit().putInt(KEY_RAMADAN_IFTAR_OFFSET_MINUTES, minutes).apply();
    }

    public static String getRamadanIftarSoundType(Context context) {
        return getPrefs(context).getString(KEY_RAMADAN_IFTAR_SOUND_TYPE, "azan_dua");
    }

    public static void setRamadanIftarSoundType(Context context, String soundType) {
        getPrefs(context).edit().putString(KEY_RAMADAN_IFTAR_SOUND_TYPE, soundType).apply();
    }

    public static boolean isRamadanIftarVibrationEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_IFTAR_VIBRATION, true);
    }

    public static void setRamadanIftarVibrationEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_IFTAR_VIBRATION, enabled).apply();
    }

    public static boolean isRamadanTaraweehEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_TARAWEEH_ENABLED, true);
    }

    public static void setRamadanTaraweehEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_TARAWEEH_ENABLED, enabled).apply();
    }

    public static boolean isRamadanDuaEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_RAMADAN_DUA_ENABLED, true);
    }

    public static void setRamadanDuaEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_RAMADAN_DUA_ENABLED, enabled).apply();
    }

    // ==========================================
    // Durud (Salawat) Reminder Settings
    // ==========================================
    public static final String KEY_DURUD_REMINDER_ENABLED = "pref_durud_reminder_enabled";
    public static final String KEY_DURUD_INTERVAL_MINUTES = "pref_durud_interval_minutes";
    public static final String KEY_DURUD_INTERVAL_HOURS = "pref_durud_interval_hours";
    public static final String KEY_DURUD_AUDIO_KEY = "pref_durud_audio_key";
    public static final String KEY_DURUD_SILENT_HOURS_ENABLED = "pref_durud_silent_hours_enabled";
    public static final String KEY_DURUD_SILENT_START_HOUR = "pref_durud_silent_start_hour";
    public static final String KEY_DURUD_SILENT_START_MINUTE = "pref_durud_silent_start_minute";
    public static final String KEY_DURUD_SILENT_END_HOUR = "pref_durud_silent_end_hour";
    public static final String KEY_DURUD_SILENT_END_MINUTE = "pref_durud_silent_end_minute";
    public static final String KEY_DURUD_JUMUAH_SPECIAL = "pref_durud_jumuah_special";
    public static final String KEY_DURUD_TYPE = "pref_durud_type";
    public static final String KEY_DURUD_DAILY_TARGET = "pref_durud_daily_target";

    public static boolean isDurudReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_DURUD_REMINDER_ENABLED, false);
    }

    public static void setDurudReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DURUD_REMINDER_ENABLED, enabled).apply();
    }

    public static int getDurudIntervalMinutes(Context context) {
        return getPrefs(context).getInt(KEY_DURUD_INTERVAL_MINUTES, 15);
    }

    public static void setDurudIntervalMinutes(Context context, int minutes) {
        getPrefs(context).edit().putInt(KEY_DURUD_INTERVAL_MINUTES, minutes).apply();
    }

    public static int getDurudIntervalHours(Context context) {
        return getPrefs(context).getInt(KEY_DURUD_INTERVAL_HOURS, 1);
    }

    public static void setDurudIntervalHours(Context context, int hours) {
        getPrefs(context).edit().putInt(KEY_DURUD_INTERVAL_HOURS, hours).apply();
    }

    public static String getDurudAudioKey(Context context) {
        return getPrefs(context).getString(KEY_DURUD_AUDIO_KEY, "ar1");
    }

    public static void setDurudAudioKey(Context context, String key) {
        getPrefs(context).edit().putString(KEY_DURUD_AUDIO_KEY, key).apply();
    }

    public static boolean isDurudSilentHoursEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_DURUD_SILENT_HOURS_ENABLED, false);
    }

    public static void setDurudSilentHoursEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DURUD_SILENT_HOURS_ENABLED, enabled).apply();
    }

    public static int getDurudSilentStartHour(Context context) {
        return getPrefs(context).getInt(KEY_DURUD_SILENT_START_HOUR, 22);
    }

    public static void setDurudSilentStartHour(Context context, int hour) {
        getPrefs(context).edit().putInt(KEY_DURUD_SILENT_START_HOUR, hour).apply();
    }

    public static int getDurudSilentStartMinute(Context context) {
        return getPrefs(context).getInt(KEY_DURUD_SILENT_START_MINUTE, 0);
    }

    public static void setDurudSilentStartMinute(Context context, int minute) {
        getPrefs(context).edit().putInt(KEY_DURUD_SILENT_START_MINUTE, minute).apply();
    }

    public static int getDurudSilentEndHour(Context context) {
        return getPrefs(context).getInt(KEY_DURUD_SILENT_END_HOUR, 8);
    }

    public static void setDurudSilentEndHour(Context context, int hour) {
        getPrefs(context).edit().putInt(KEY_DURUD_SILENT_END_HOUR, hour).apply();
    }

    public static int getDurudSilentEndMinute(Context context) {
        return getPrefs(context).getInt(KEY_DURUD_SILENT_END_MINUTE, 0);
    }

    public static void setDurudSilentEndMinute(Context context, int minute) {
        getPrefs(context).edit().putInt(KEY_DURUD_SILENT_END_MINUTE, minute).apply();
    }

    public static boolean isWithinDurudSilentHours(Context context) {
        if (!isDurudSilentHoursEnabled(context)) return false;
        java.util.Calendar now = java.util.Calendar.getInstance();
        int currentMinutes = now.get(java.util.Calendar.HOUR_OF_DAY) * 60 + now.get(java.util.Calendar.MINUTE);
        int startMinutes = getDurudSilentStartHour(context) * 60 + getDurudSilentStartMinute(context);
        int endMinutes = getDurudSilentEndHour(context) * 60 + getDurudSilentEndMinute(context);

        if (startMinutes == endMinutes) return false;

        if (startMinutes < endMinutes) {
            return currentMinutes >= startMinutes && currentMinutes < endMinutes;
        } else {
            // Spans midnight (e.g. 22:00 to 08:00)
            return currentMinutes >= startMinutes || currentMinutes < endMinutes;
        }
    }

    public static boolean isDurudJumuahSpecialEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_DURUD_JUMUAH_SPECIAL, true);
    }

    public static void setDurudJumuahSpecialEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_DURUD_JUMUAH_SPECIAL, enabled).apply();
    }

    public static String getDurudType(Context context) {
        return getPrefs(context).getString(KEY_DURUD_TYPE, "all");
    }

    public static void setDurudType(Context context, String type) {
        getPrefs(context).edit().putString(KEY_DURUD_TYPE, type).apply();
    }

    public static int getDurudDailyTarget(Context context) {
        return getPrefs(context).getInt(KEY_DURUD_DAILY_TARGET, 100);
    }

    public static void setDurudDailyTarget(Context context, int target) {
        getPrefs(context).edit().putInt(KEY_DURUD_DAILY_TARGET, target).apply();
    }

    // ==========================================
    // Istigfar (Tawbah) Reminder Settings
    // ==========================================
    public static final String KEY_ISTIGFAR_REMINDER_ENABLED = "pref_istigfar_reminder_enabled";
    public static final String KEY_ISTIGFAR_INTERVAL_MINUTES = "pref_istigfar_interval_minutes";
    public static final String KEY_ISTIGFAR_INTERVAL_HOURS = "pref_istigfar_interval_hours";
    public static final String KEY_ISTIGFAR_AUDIO_LANGUAGE = "pref_istigfar_audio_language";
    public static final String KEY_ISTIGFAR_SILENT_HOURS_ENABLED = "pref_istigfar_silent_hours_enabled";
    public static final String KEY_ISTIGFAR_SILENT_START_HOUR = "pref_istigfar_silent_start_hour";
    public static final String KEY_ISTIGFAR_SILENT_START_MINUTE = "pref_istigfar_silent_start_minute";
    public static final String KEY_ISTIGFAR_SILENT_END_HOUR = "pref_istigfar_silent_end_hour";
    public static final String KEY_ISTIGFAR_SILENT_END_MINUTE = "pref_istigfar_silent_end_minute";
    public static final String KEY_ISTIGFAR_SAYYIDUL_ENABLED = "pref_istigfar_sayyidul_enabled";
    public static final String KEY_ISTIGFAR_BEDTIME_ENABLED = "pref_istigfar_bedtime_enabled";
    public static final String KEY_ISTIGFAR_DAILY_TARGET = "pref_istigfar_daily_target";

    public static boolean isIstigfarReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ISTIGFAR_REMINDER_ENABLED, false);
    }

    public static void setIstigfarReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ISTIGFAR_REMINDER_ENABLED, enabled).apply();
    }

    public static int getIstigfarIntervalMinutes(Context context) {
        return getPrefs(context).getInt(KEY_ISTIGFAR_INTERVAL_MINUTES, 15);
    }

    public static void setIstigfarIntervalMinutes(Context context, int minutes) {
        getPrefs(context).edit().putInt(KEY_ISTIGFAR_INTERVAL_MINUTES, minutes).apply();
    }

    public static int getIstigfarIntervalHours(Context context) {
        return getPrefs(context).getInt(KEY_ISTIGFAR_INTERVAL_HOURS, 1);
    }

    public static void setIstigfarIntervalHours(Context context, int hours) {
        getPrefs(context).edit().putInt(KEY_ISTIGFAR_INTERVAL_HOURS, hours).apply();
    }

    public static String getIstigfarAudioLanguage(Context context) {
        return getPrefs(context).getString(KEY_ISTIGFAR_AUDIO_LANGUAGE, "ar");
    }

    public static void setIstigfarAudioLanguage(Context context, String lang) {
        getPrefs(context).edit().putString(KEY_ISTIGFAR_AUDIO_LANGUAGE, lang).apply();
    }

    public static boolean isIstigfarSilentHoursEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ISTIGFAR_SILENT_HOURS_ENABLED, false);
    }

    public static void setIstigfarSilentHoursEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ISTIGFAR_SILENT_HOURS_ENABLED, enabled).apply();
    }

    public static int getIstigfarSilentStartHour(Context context) {
        return getPrefs(context).getInt(KEY_ISTIGFAR_SILENT_START_HOUR, 22);
    }

    public static void setIstigfarSilentStartHour(Context context, int hour) {
        getPrefs(context).edit().putInt(KEY_ISTIGFAR_SILENT_START_HOUR, hour).apply();
    }

    public static int getIstigfarSilentStartMinute(Context context) {
        return getPrefs(context).getInt(KEY_ISTIGFAR_SILENT_START_MINUTE, 0);
    }

    public static void setIstigfarSilentStartMinute(Context context, int minute) {
        getPrefs(context).edit().putInt(KEY_ISTIGFAR_SILENT_START_MINUTE, minute).apply();
    }

    public static int getIstigfarSilentEndHour(Context context) {
        return getPrefs(context).getInt(KEY_ISTIGFAR_SILENT_END_HOUR, 8);
    }

    public static void setIstigfarSilentEndHour(Context context, int hour) {
        getPrefs(context).edit().putInt(KEY_ISTIGFAR_SILENT_END_HOUR, hour).apply();
    }

    public static int getIstigfarSilentEndMinute(Context context) {
        return getPrefs(context).getInt(KEY_ISTIGFAR_SILENT_END_MINUTE, 0);
    }

    public static void setIstigfarSilentEndMinute(Context context, int minute) {
        getPrefs(context).edit().putInt(KEY_ISTIGFAR_SILENT_END_MINUTE, minute).apply();
    }

    public static boolean isWithinIstigfarSilentHours(Context context) {
        if (!isIstigfarSilentHoursEnabled(context)) return false;
        java.util.Calendar now = java.util.Calendar.getInstance();
        int currentMinutes = now.get(java.util.Calendar.HOUR_OF_DAY) * 60 + now.get(java.util.Calendar.MINUTE);
        int startMinutes = getIstigfarSilentStartHour(context) * 60 + getIstigfarSilentStartMinute(context);
        int endMinutes = getIstigfarSilentEndHour(context) * 60 + getIstigfarSilentEndMinute(context);

        if (startMinutes == endMinutes) return false;

        if (startMinutes < endMinutes) {
            return currentMinutes >= startMinutes && currentMinutes < endMinutes;
        } else {
            // Spans midnight (e.g. 22:00 to 08:00)
            return currentMinutes >= startMinutes || currentMinutes < endMinutes;
        }
    }

    public static boolean isIstigfarSayyidulEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ISTIGFAR_SAYYIDUL_ENABLED, true);
    }

    public static void setIstigfarSayyidulEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ISTIGFAR_SAYYIDUL_ENABLED, enabled).apply();
    }

    public static boolean isIstigfarBedtimeEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_ISTIGFAR_BEDTIME_ENABLED, true);
    }

    public static void setIstigfarBedtimeEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_ISTIGFAR_BEDTIME_ENABLED, enabled).apply();
    }

    public static int getIstigfarDailyTarget(Context context) {
        return getPrefs(context).getInt(KEY_ISTIGFAR_DAILY_TARGET, 100);
    }

    public static void setIstigfarDailyTarget(Context context, int target) {
        getPrefs(context).edit().putInt(KEY_ISTIGFAR_DAILY_TARGET, target).apply();
    }

    public static final String KEY_CUSTOM_NOTIF_SOUND_NAME = "pref_custom_notif_sound_name";
    public static final String KEY_NOTIFICATION_TONE = "pref_notification_tone";

    public static final String TONE_SYSTEM_DEFAULT = "default";
    public static final String TONE_SUBHANALLAH = "subhanallah";
    public static final String TONE_BISMILLAH = "bismillah";
    public static final String TONE_ALLAHU_AKBAR = "allahu_akbar";
    public static final String TONE_CHIME_DROP = "chime_drop";

    public static String getCustomNotificationSoundName(Context context) {
        return getPrefs(context).getString(KEY_CUSTOM_NOTIF_SOUND_NAME, "DEFAULT");
    }

    public static void setCustomNotificationSoundName(Context context, String name) {
        getPrefs(context).edit().putString(KEY_CUSTOM_NOTIF_SOUND_NAME, name).apply();
    }

    public static String getNotificationTone(Context context) {
        return getPrefs(context).getString(KEY_NOTIFICATION_TONE, TONE_SYSTEM_DEFAULT);
    }

    public static void setNotificationTone(Context context, String tone) {
        getPrefs(context).edit().putString(KEY_NOTIFICATION_TONE, tone).apply();
    }

    public static String getNotificationToneDisplayName(Context context, String tone) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        if (TONE_SUBHANALLAH.equals(tone)) {
            return isBn ? "সুবহানাল্লাহ সুর" : "Subhanallah Tone";
        } else if (TONE_BISMILLAH.equals(tone)) {
            return isBn ? "বিসমিল্লাহ সুর" : "Bismillah Tone";
        } else if (TONE_ALLAHU_AKBAR.equals(tone)) {
            return isBn ? "আল্লাহু আকবার সুর" : "Allahu Akbar Tone";
        } else if (TONE_CHIME_DROP.equals(tone)) {
            return isBn ? "মৃদু ড্রপ চাইম" : "Gentle Drop Chime";
        }
        return isBn ? "ডিফল্ট সিস্টেম টিউন" : "Default System Tone";
    }

    public static android.net.Uri getNotificationToneUri(Context context, String tone) {
        if (context == null) return null;
        int soundResId = 0;
        if (TONE_SUBHANALLAH.equals(tone)) {
            soundResId = R.raw.read_islamic_rintone;
        } else if (TONE_BISMILLAH.equals(tone)) {
            soundResId = R.raw.read_allah_ho_allah_beautiful;
        } else if (TONE_ALLAHU_AKBAR.equals(tone)) {
            soundResId = R.raw.read_allah_hu_allah_default;
        } else if (TONE_CHIME_DROP.equals(tone)) {
            soundResId = R.raw.universfield_new_notification_022_370046;
        } else {
            soundResId = R.raw.universfield_new_notification;
        }
        return android.net.Uri.parse(android.content.ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + soundResId);
    }
}

