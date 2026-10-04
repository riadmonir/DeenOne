package com.devflux.deenone.core.jamaat;

import android.content.Context;
import android.content.SharedPreferences;

public class JamaatPrayerManager {

    private static final String PREF_JAMAAT = "deanone_jamaat_prefs";
    private static final String KEY_SELECTED_MOSQUE = "key_selected_mosque";

    public static class MosqueJamaatSchedule {
        public final String mosqueName;
        public final String location;
        public final String fajrJamaat;
        public final String dhuhrJamaat;
        public final String asrJamaat;
        public final String maghribJamaat;
        public final String ishaJamaat;
        public final String jummahJamaat;
        public final boolean isVerified;

        public MosqueJamaatSchedule(String mosqueName, String location,
                                    String fajrJamaat, String dhuhrJamaat, String asrJamaat,
                                    String maghribJamaat, String ishaJamaat, String jummahJamaat,
                                    boolean isVerified) {
            this.mosqueName = mosqueName;
            this.location = location;
            this.fajrJamaat = fajrJamaat;
            this.dhuhrJamaat = dhuhrJamaat;
            this.asrJamaat = asrJamaat;
            this.maghribJamaat = maghribJamaat;
            this.ishaJamaat = ishaJamaat;
            this.jummahJamaat = jummahJamaat;
            this.isVerified = isVerified;
        }
    }

    private static volatile JamaatPrayerManager instance;

    public static JamaatPrayerManager getInstance() {
        if (instance == null) {
            synchronized (JamaatPrayerManager.class) {
                if (instance == null) {
                    instance = new JamaatPrayerManager();
                }
            }
        }
        return instance;
    }

    public MosqueJamaatSchedule getActiveMosqueSchedule(Context context) {
        if (context == null) {
            return new MosqueJamaatSchedule("বায়তুল মুকাররম জাতীয় মসজিদ", "ঢাকা",
                    "০৫:১৫ AM", "০১:৩০ PM", "০৪:৪৫ PM", "সূর্যাস্তের ৫ মিনিট পর", "০৮:১৫ PM", "০১:৩০ PM", true);
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_JAMAAT, Context.MODE_PRIVATE);
        String mosque = prefs.getString(KEY_SELECTED_MOSQUE, "baitul_mukarram");

        if ("baitul_mukarram".equals(mosque)) {
            return new MosqueJamaatSchedule("বায়তুল মুকাররম জাতীয় মসজিদ", "পল্টন, ঢাকা",
                    "০৫:১৫ AM", "০১:৩০ PM", "০৪:৪৫ PM", "মাগরিবের আযানের ৫ মিনিট পর", "০৮:১৫ PM", "০১:৩০ PM", true);
        } else if ("kakrail".equals(mosque)) {
            return new MosqueJamaatSchedule("কাকরাইল জামে মসজিদ", "রমনা, ঢাকা",
                    "০৫:০০ AM", "০১:১৫ PM", "০৪:৩০ PM", "আযানের ৩ মিনিট পর", "০৮:০০ PM", "০১:১৫ PM", true);
        } else {
            // Unconfigured / custom mosque without reliable data
            return new MosqueJamaatSchedule("নিকটস্থ মসজিদ (অনির্ধারিত)", "লোকাল এলাকা",
                    null, null, null, null, null, null, false);
        }
    }

    public void selectMosque(Context context, String mosqueKey) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_JAMAAT, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_SELECTED_MOSQUE, mosqueKey).apply();
    }
}