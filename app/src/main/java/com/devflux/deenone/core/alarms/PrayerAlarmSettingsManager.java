package com.devflux.deenone.core.alarms;

import android.content.Context;
import android.content.SharedPreferences;

public class PrayerAlarmSettingsManager {

    private static final String PREF_NAME = "deanone_prayer_alarm_prefs";

    public static final String ALERT_MODE_FULL_AZAN = "full_azan";
    public static final String ALERT_MODE_SHORT_BEEP = "short_beep";
    public static final String ALERT_MODE_VIBRATION_ONLY = "vibration_only";
    public static final String ALERT_MODE_SILENT = "silent";

    // Standard Canonical Sound Keys
    public static final String SOUND_SILENT = "silent";
    public static final String SOUND_AZAN_DEFAULT = "azan_default";
    public static final String SOUND_AZAN_COMMON = "azan_common";
    public static final String SOUND_AZAN_STANDARD = "azan_standard";
    public static final String SOUND_BEEP = "beep";
    public static final String SOUND_RING = "ring";
    public static final String SOUND_NOTIFICATION = "notification";

    public static boolean getDefaultEnabledForWaqt(String waqtKey) {
        if (waqtKey == null) return false;
        return switch (waqtKey.toLowerCase().trim()) {
            case "asr", "maghrib", "isha", "jummah" -> true;
            default -> false; // fajr, dhuhr, tahajjud, ishraq, chasht, duha, awwabin, sunrise -> false
        };
    }

    public static class PrayerAlarmConfig {
        public String prayerKey; // "fajr", "dhuhr", "asr", "maghrib", "isha", "tahajjud", "ishraq", "chasht", "awwabin", "sunrise"
        public boolean isEnabled; // Master switch for this waqt
        public boolean isNotificationEnabled; // Notification on status bar
        public boolean isAzanEnabled; // Full Adhan playback
        public boolean isAlarmEnabled; // Intense wake-up ringing
        public boolean isVibrationEnabled; // Vibration
        public boolean isSoundEnabled; // Any sound enabled
        public int prePrayerReminderMinutes; // 0 (exact time), 5, 10, 15, 20, 30 (or negative for after)
        public String alertMode; // "full_azan", "short_beep", "vibration_only", "silent"
        public String selectedAzanTone; // Legacy tone key
        public String soundType; // "silent", "azan_default", "azan_common", "azan_standard", "beep", "ring", "notification"

        public PrayerAlarmConfig(String prayerKey) {
            this.prayerKey = prayerKey;
            this.isEnabled = getDefaultEnabledForWaqt(prayerKey);
            this.isNotificationEnabled = this.isEnabled;
            this.isAzanEnabled = this.isEnabled;
            this.isAlarmEnabled = false;
            this.isVibrationEnabled = true;
            this.isSoundEnabled = true;
            this.prePrayerReminderMinutes = 0; // Default exact time (0 min)
            this.alertMode = ALERT_MODE_FULL_AZAN;
            this.selectedAzanTone = "fajr".equalsIgnoreCase(prayerKey) ? "fajr" : "makkah";
            this.soundType = SOUND_AZAN_DEFAULT;
        }
    }

    public static PrayerAlarmConfig getConfig(Context context, String prayerKey) {
        if (context == null) return new PrayerAlarmConfig(prayerKey);
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = prayerKey != null ? prayerKey.toLowerCase().trim() : "fajr";

        PrayerAlarmConfig config = new PrayerAlarmConfig(key);
        boolean defaultEnabled = getDefaultEnabledForWaqt(key);
        config.isEnabled = prefs.getBoolean("enabled_" + key, defaultEnabled);
        config.isNotificationEnabled = prefs.getBoolean("notif_" + key, config.isEnabled);
        config.isAzanEnabled = prefs.getBoolean("azan_" + key, config.isEnabled);
        config.isAlarmEnabled = prefs.getBoolean("alarm_" + key, false);
        config.isVibrationEnabled = prefs.getBoolean("vib_" + key, true);
        config.isSoundEnabled = prefs.getBoolean("snd_" + key, true);
        config.prePrayerReminderMinutes = prefs.getInt("pre_" + key, 0);
        config.alertMode = prefs.getString("mode_" + key, ALERT_MODE_FULL_AZAN);
        config.selectedAzanTone = prefs.getString("tone_" + key, "fajr".equalsIgnoreCase(key) ? "fajr" : "makkah");
        config.soundType = prefs.getString("sound_type_" + key, SOUND_AZAN_DEFAULT);

        return config;
    }

    public static void saveConfig(Context context, PrayerAlarmConfig config) {
        if (context == null || config == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String key = config.prayerKey != null ? config.prayerKey.toLowerCase().trim() : "fajr";

        prefs.edit()
                .putBoolean("enabled_" + key, config.isEnabled)
                .putBoolean("notif_" + key, config.isNotificationEnabled)
                .putBoolean("azan_" + key, config.isAzanEnabled)
                .putBoolean("alarm_" + key, config.isAlarmEnabled)
                .putBoolean("vib_" + key, config.isVibrationEnabled)
                .putBoolean("snd_" + key, config.isSoundEnabled)
                .putInt("pre_" + key, config.prePrayerReminderMinutes)
                .putString("mode_" + key, config.alertMode)
                .putString("tone_" + key, config.selectedAzanTone)
                .putString("sound_type_" + key, config.soundType)
                .apply();
    }

    public static String getAzanAudioUrl(Context context, String prayerKey, String soundType) {
        if (context == null) return "";
        String pkg = context.getPackageName();
        String pKey = prayerKey != null ? prayerKey.toLowerCase().trim() : "fajr";
        String sType = soundType != null ? soundType.toLowerCase().trim() : SOUND_AZAN_DEFAULT;

        if (SOUND_SILENT.equals(sType)) {
            return "";
        }

        if (SOUND_AZAN_DEFAULT.equals(sType) || "default".equals(sType) || "azan".equals(sType) || "fajr".equals(sType) || "makkah".equals(sType)) {
            if ("fajr".equals(pKey)) {
                return "android.resource://" + pkg + "/raw/read_azan_fajr";
            } else {
                return "android.resource://" + pkg + "/raw/read_azan_default";
            }
        }

        return switch (sType) {
            case "azan_fajr" -> "android.resource://" + pkg + "/raw/read_azan_fajr";
            case "azan_default" -> "android.resource://" + pkg + "/raw/read_azan_default";
            case SOUND_AZAN_COMMON -> "android.resource://" + pkg + "/raw/read_azan_common";
            case SOUND_AZAN_STANDARD, "read_azan" -> "android.resource://" + pkg + "/raw/read_azan";
            case SOUND_BEEP, "short_beep" -> "android.resource://" + pkg + "/raw/beep";
            case SOUND_RING, "ring_clock_alarm", "alarm" -> "android.resource://" + pkg + "/raw/ring_clock_alarm";
            case SOUND_NOTIFICATION, "alarm_notification" -> "android.resource://" + pkg + "/raw/alarm_notification";
            default -> "fajr".equals(pKey)
                    ? "android.resource://" + pkg + "/raw/read_azan_fajr"
                    : "android.resource://" + pkg + "/raw/read_azan_default";
        };
    }

    public static String getAzanAudioUrl(Context context, String tone) {
        return getAzanAudioUrl(context, "dhuhr", tone);
    }

    public static String getSoundDisplayName(String soundType, String prayerKey) {
        if (soundType == null) return "Azan";
        String pKey = prayerKey != null ? prayerKey.toLowerCase().trim() : "fajr";
        switch (soundType.toLowerCase().trim()) {
            case SOUND_SILENT:
                return "Silent";
            case SOUND_AZAN_DEFAULT:
            case "default":
            case "azan":
                return "fajr".equals(pKey) ? "Azan (Fajr Special)" : "Azan (Default)";
            case SOUND_AZAN_COMMON:
                return "Azan (Common)";
            case SOUND_AZAN_STANDARD:
                return "Azan (Standard)";
            case SOUND_BEEP:
                return "Beep";
            case SOUND_RING:
                return "Ring";
            case SOUND_NOTIFICATION:
                return "Notification";
            default:
                return "Azan";
        }
    }

    public static String getWaqtSummaryText(Context context, String waqtKey) {
        if (context == null) return "রিমাইন্ডার বন্ধ";
        PrayerAlarmConfig config = getConfig(context, waqtKey);
        if (!config.isEnabled) {
            return "রিমাইন্ডার বন্ধ";
        }

        String timeStr = config.prePrayerReminderMinutes == 0 ? "ওয়াক্তের সময়" : (config.prePrayerReminderMinutes + " মিনিট পূর্বে");
        String soundName = getSoundDisplayName(config.soundType, waqtKey);

        return timeStr + " • " + soundName;
    }

    public static String getWaqtDisplayName(String waqtKey) {
        if (waqtKey == null) return "সালাত";
        return switch (waqtKey.toLowerCase().trim()) {
            case "fajr" -> "ফজর";
            case "dhuhr" -> "যোহর";
            case "jummah" -> "জুম্মা";
            case "asr" -> "আসর";
            case "maghrib" -> "মাগরিব";
            case "isha" -> "এশা";
            case "tahajjud" -> "তাহাজ্জুদ";
            case "ishraq" -> "ইশরাক";
            case "chasht", "duha" -> "চাশত";
            case "awwabin" -> "আওয়াবিন";
            case "sunrise" -> "সূর্যোদয়";
            default -> "সালাত";
        };
    }

    public static String getWaqtEnglishName(String waqtKey) {
        if (waqtKey == null) return "Fajr";
        return switch (waqtKey.toLowerCase().trim()) {
            case "fajr" -> "Fajr";
            case "dhuhr" -> "Dhuhr";
            case "jummah" -> "Jummah";
            case "asr" -> "Asr";
            case "maghrib" -> "Maghrib";
            case "isha" -> "Isha";
            case "tahajjud" -> "Tahajjud";
            case "ishraq" -> "Ishraq";
            case "chasht", "duha" -> "Chasht";
            case "awwabin" -> "Awwabin";
            case "sunrise" -> "Sunrise";
            default -> "Prayer";
        };
    }

    public static boolean isAnyWaqtEnabled(Context context) {
        if (context == null) return false;
        String[] waqts = {"fajr", "dhuhr", "jummah", "asr", "maghrib", "isha", "tahajjud", "ishraq", "chasht", "awwabin", "sunrise"};
        for (String w : waqts) {
            if (getConfig(context, w).isEnabled) return true;
        }
        return false;
    }

    public static void setAllWaqtEnabled(Context context, boolean enabled) {
        if (context == null) return;
        String[] waqts = {"fajr", "dhuhr", "jummah", "asr", "maghrib", "isha", "tahajjud", "ishraq", "chasht", "awwabin", "sunrise"};
        if (!enabled) {
            for (String w : waqts) {
                PrayerAlarmConfig config = getConfig(context, w);
                config.isEnabled = false;
                config.isNotificationEnabled = false;
                config.isAzanEnabled = false;
                saveConfig(context, config);
            }
        } else {
            // As per user requirement: only enable active default prayers (Asr, Maghrib, Isha, Jummah).
            // Do NOT forcefully turn on Fajr, Dhuhr, or Nafl prayers.
            for (String w : waqts) {
                PrayerAlarmConfig config = getConfig(context, w);
                boolean shouldEnable = getDefaultEnabledForWaqt(w);
                config.isEnabled = shouldEnable;
                if (shouldEnable) {
                    config.isAzanEnabled = true;
                    config.isSoundEnabled = true;
                    config.isVibrationEnabled = true;
                    config.isNotificationEnabled = true;
                }
                saveConfig(context, config);
            }
        }
    }
}
