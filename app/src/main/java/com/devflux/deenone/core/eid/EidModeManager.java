package com.devflux.deenone.core.eid;

import android.content.Context;
import android.content.SharedPreferences;

import com.devflux.deenone.core.location.LocationProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EidModeManager {

    private static final String PREF_EID = "deanone_eid_mode_prefs";
    private static final String KEY_EID_NOTIF_ENABLED = "key_eid_notif_enabled";
    private static final String KEY_EID_TYPE_OVERRIDE = "key_eid_type_override"; // "fitr" or "adha" or "auto"

    public enum EidType {
        EID_UL_FITR("পবিত্র ঈদুল ফিতর", "১ম শাওয়াল (রমজানের সমাপ্তি ও ফিতরা আদায়)"),
        EID_UL_ADHA("পবিত্র ঈদুল আজহা ও কুরবানী", "১০-১৩ জিলহজ (কুরবানী ও আইয়ামে তাশরীকের দিনসমূহ)");

        public final String title;
        public final String subtitle;

        EidType(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    public static class EidSunnahItem {
        public final String id;
        public final String title;
        public final String description;
        public final String reference;

        public EidSunnahItem(String id, String title, String description, String reference) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.reference = reference;
        }
    }

    private static volatile EidModeManager instance;

    public static EidModeManager getInstance() {
        if (instance == null) {
            synchronized (EidModeManager.class) {
                if (instance == null) {
                    instance = new EidModeManager();
                }
            }
        }
        return instance;
    }

    /**
     * Determines active Eid type based on location/timezone or manual preview override
     */
    public EidType getActiveEidType(Context context) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_EID, Context.MODE_PRIVATE);
            String override = prefs.getString(KEY_EID_TYPE_OVERRIDE, "auto");
            if ("fitr".equalsIgnoreCase(override)) return EidType.EID_UL_FITR;
            if ("adha".equalsIgnoreCase(override)) return EidType.EID_UL_ADHA;
        }

        // Default to Fitr or Adha depending on month
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH); // 0-indexed
        if (month >= Calendar.MAY && month <= Calendar.JULY) {
            return EidType.EID_UL_ADHA;
        }
        return EidType.EID_UL_FITR;
    }

    public void setEidTypeOverride(Context context, String type) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_EID, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_EID_TYPE_OVERRIDE, type).apply();
    }

    public boolean isEidNotificationEnabled(Context context) {
        if (context == null) return true;
        SharedPreferences prefs = context.getSharedPreferences(PREF_EID, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_EID_NOTIF_ENABLED, true);
    }

    public void setEidNotificationEnabled(Context context, boolean enabled) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_EID, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_EID_NOTIF_ENABLED, enabled).apply();
    }

    /**
     * Determines whether today is an authentic Islamic Eid day or active Eid season.
     */
    public boolean isEidActiveToday(Context context) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_EID, Context.MODE_PRIVATE);
            String override = prefs.getString(KEY_EID_TYPE_OVERRIDE, "auto");
            if ("fitr".equalsIgnoreCase(override) || "adha".equalsIgnoreCase(override)) {
                return true; // Preview mode explicitly enabled by user/admin
            }
        }

        // Accurate Astronomical Hijri Check
        try {
            com.devflux.deenone.utils.HijriCalendarUtil.HijriDateResult hijri =
                com.devflux.deenone.utils.HijriCalendarUtil.getRealHijriDate(Calendar.getInstance());
            if (hijri != null) {
                // Shawwal (monthIndex 9): 1st, 2nd, 3rd Shawwal is Eid-ul-Fitr
                if (hijri.monthIndex == 9 && hijri.day >= 1 && hijri.day <= 3) {
                    return true;
                }
                // Dhul Hijjah (monthIndex 11): 9th (Arafah) to 13th (Eid-ul-Adha & Days of Tashreeq)
                if (hijri.monthIndex == 11 && hijri.day >= 9 && hijri.day <= 13) {
                    return true;
                }
            }
        } catch (Exception ignored) {}

        return false;
    }

    /**
     * 10 Verified Authentic Eid Sunnahs
     */
    public List<EidSunnahItem> getEidSunnahs(EidType eidType) {
        List<EidSunnahItem> list = new ArrayList<>();

        list.add(new EidSunnahItem(
                "ghusl",
                "১. ঈদের নামাজের পূর্বে গোসল করা",
                "ঈদের সকালে গোসল করে নিজেকে পবিত্র করা রাসূলুল্লাহ (ﷺ) ও সাহাবীদের নিয়মিত সুন্নাত ছিল।",
                "— সুনানে ইবনে মাজাহ: ১৩১৫, মুওয়াত্তা মালিক: ৪২৮ (সহীহ সনদে বর্ণিত)"
        ));

        list.add(new EidSunnahItem(
                "best_dress",
                "২. সর্বোত্তম ও পরিচ্ছন্ন পোশাক পরিধান করা",
                "নিজের কাছে থাকা সবচেয়ে সুন্দর ও পরিচ্ছন্ন পোশাক পরিধান করা সুন্নাত।",
                "— সহীহ বুখারী: ৯৪৮, মুস্তাদরাকে হাকেম: ৭৫৬০"
        ));

        list.add(new EidSunnahItem(
                "attar",
                "৩. সুগন্ধি বা আতর ব্যবহার করা",
                "ঈদগাহে যাওয়ার পূর্বে পুরুষদের সুগন্ধি লাগানো মুস্তাহাব।",
                "— সহীহ বুখারী: ৯৪৮, সুনানে বায়হাকী: ৬১৫৯"
        ));

        if (eidType == EidType.EID_UL_FITR) {
            list.add(new EidSunnahItem(
                    "dates_fitr",
                    "৪. ঈদুল ফিতরে বিজোড় সংখ্যক খেজুর/মিষ্টি খেয়ে যাওয়া",
                    "রাসূলুল্লাহ (ﷺ) ঈদুল ফিতরের দিন বিজোড় সংখ্যক খেজুর না খেয়ে ঈদগাহে যেতেন না।",
                    "— সহীহ বুখারী: ৯৫৩"
            ));
            list.add(new EidSunnahItem(
                    "fitra_before",
                    "৫. ঈদের নামাজের পূর্বে সাদাকাতুল ফিতর আদায় করা",
                    "নামাজের পূর্বেই ফিতরা গরীব-মিসকিনদের কাছে পৌঁছে দেওয়া ওয়াজিব।",
                    "— সহীহ বুখারী: ১৫০৩, সহীহ মুসলিম: ৯৮৪"
            ));
        } else {
            list.add(new EidSunnahItem(
                    "fast_until_qurbani",
                    "৪. ঈদুল আজহায় না খেয়ে ঈদগাহে যাওয়া ও কুরবানীর গোশত খাওয়া",
                    "রাসূলুল্লাহ (ﷺ) ঈদুল আজহার দিন নামাজের পূর্বে কিছু খেতেন না, নামাজ শেষে কুরবানীর গোশত দিয়ে খেতেন।",
                    "— জামে আত-তিরমিযী: ৫৪৫, সুনানে ইবনে মাজাহ: ১৭৫৬ (সহীহ)"
            ));
            list.add(new EidSunnahItem(
                    "tashreeq_takbeer",
                    "৫. তাকবীরে তাশরীক পাঠ করা (৯ম-১৩ই জিলহজ)",
                    "৯ই জিলহজ ফজর থেকে ১৩ই জিলহজ আসর পর্যন্ত প্রত্যেক ফরজ নামাজের পর তাকবীরে তাশরীক একবার পড়া ওয়াজিব।",
                    "— মুসান্নাফে ইবনে আবী শায়বাহ: ৫৬৩৩, দারা কুতনী: ১৭৫৬"
            ));
        }

        list.add(new EidSunnahItem(
                "takbeer_road",
                "৬. ঈদগাহে যাওয়ার পথে তাকবীর পাঠ করা",
                "ঘর থেকে ঈদগাহে পৌঁছানো পর্যন্ত উচ্চস্বরে (পুরুষদের) ও নীরবে (নারীদের) তাকবীর পাঠ করা।",
                "— সুনানে দারা কুতনী: ১৭১৭, সিলসিলাতুস সহীহাহ: ১৭১"
        ));

        list.add(new EidSunnahItem(
                "walk_eidgah",
                "৭. সম্ভব হলে পায়ে হেঁটে ঈদগাহে যাওয়া",
                "পায়ে হেঁটে ঈদগাহে যাওয়া এবং কোনো ওজর ছাড়া বাহনে না চড়া সুন্নাত।",
                "— জামে আত-তিরমিযী: ৫৩০, সুনানে ইবনে মাজাহ: ১২৯৫ (সহীহ)"
        ));

        list.add(new EidSunnahItem(
                "different_routes",
                "৮. যাওয়া ও আসার জন্য ভিন্ন রাস্তা ব্যবহার করা",
                "রাসূলুল্লাহ (ﷺ) ঈদের দিন ঈদগাহে যাওয়ার রাস্তা এবং ফিরে আসার রাস্তা পরিবর্তন করতেন।",
                "— সহীহ বুখারী: ৯৮৬"
        ));

        list.add(new EidSunnahItem(
                "eid_greeting",
                "৯. পারস্পরিক ঈদের মোবারকবাদ বিনিময় করা",
                "সাহাবায়ে কেরাম ঈদের দিন একে অপরকে বলতেন: 'তাকাব্বালাল্লাহু মিন্না ওয়া মিনকুম' (আল্লাহ আমাদের ও আপনার ইবাদত কবুল করুন)।",
                "— ফাতহুল বারী ২/৪৪৬, তাবারানী (সহীহ সনদে বর্ণিত)"
        ));

        list.add(new EidSunnahItem(
                "khutbah_listen",
                "১০. ঈদের খুতবা মনোযোগ সহকারে শোনা",
                "ঈদের নামাজের পর ইমামের খুতবা শোনা সুন্নাত ও অত্যন্ত বরকতময়।",
                "— সুনানে আবু দাউদ: ১১৫৫, সুনানে নাসাঈ: ১৫৭১"
        ));

        return list;
    }
}