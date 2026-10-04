package com.devflux.deenone.core.jummah;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JummahModeManager {

  private static final String PREF_JUMMAH = "deanone_jummah_mode_prefs";
  private static final String KEY_SALAWAT_COUNT = "key_salawat_count_";
  private static final String KEY_SUNNAH_CHECK_PREFIX = "key_sunnah_";
  private static final String KEY_KAHF_READ = "key_kahf_read_";

  public static class JummahSunnahItem {
    public final String id;
    public final String titleBn;
    public final String titleEn;
    public final String arabicHadith;
    public final String meaningBn;
    public final String meaningEn;
    public final String referenceBn;
    public final String referenceEn;

    public JummahSunnahItem(String id, String titleBn, String titleEn, String arabicHadith,
                            String meaningBn, String meaningEn, String referenceBn, String referenceEn) {
      this.id = id;
      this.titleBn = titleBn;
      this.titleEn = titleEn;
      this.arabicHadith = arabicHadith;
      this.meaningBn = meaningBn;
      this.meaningEn = meaningEn;
      this.referenceBn = referenceBn;
      this.referenceEn = referenceEn;
    }

    public String getTitle(boolean isBn) {
      return isBn ? titleBn : titleEn;
    }

    public String getMeaning(boolean isBn) {
      return isBn ? meaningBn : meaningEn;
    }

    public String getReference(boolean isBn) {
      return isBn ? referenceBn : referenceEn;
    }
  }

  private static volatile JummahModeManager instance;

  public static JummahModeManager getInstance() {
    if (instance == null) {
      synchronized (JummahModeManager.class) {
        if (instance == null) {
          instance = new JummahModeManager();
        }
      }
    }
    return instance;
  }

  public enum FridayPhase {
    MORNING_KAHF(
        "সকালের আমল ও সূরা কাহাফ",
        "Morning Devotion & Surah Al-Kahf",
        "সকালের সময় সূরা আল-কাহাফ তিলাওয়াত করুন এবং জুমার প্রস্তুতির জন্য প্রস্তুত হোন।",
        "Recite Surah Al-Kahf in the morning and prepare for the sacred Jummah prayer."
    ),
    PRE_JUMMAH_PREP(
        "জুমার প্রস্তুতি (গোসল ও পরিচ্ছন্নতা)",
        "Pre-Jummah Preparation (Ghusl & Cleanliness)",
        "জুমার গোসল, মেসওয়াক, আতর ও উত্তম পোশাক পরিধান করে মসজিদের উদ্দেশ্যে রওনা দিন।",
        "Perform Ghusl, use miswak, apply perfume, wear clean clothes, and head to the mosque."
    ),
    JUMMAH_PRAYER(
        "জুমার নামাজ ও খুতবা",
        "Jummah Prayer & Khutbah",
        "মসজিদে আগে পৌঁছান, তাহিয়্যাতুল মসজিদ আদায় করুন এবং সম্পূর্ণ নীরব থেকে খুতবা শুনুন।",
        "Arrive early at the mosque, pray Tahiyyatul Masjid, and listen attentively in silence to the Khutbah."
    ),
    SAATUL_IJABAH(
        "সা'আতুল ইজাবাহ (দোয়া কবুলের বিশেষ সময়)",
        "Sa'atul Ijabah (Hour of Acceptance)",
        "আসরের শেষ প্রহরে দোয়া কবুলের বিশেষ মুহূর্ত। অধিক পরিমাণে দোয়া ও ইস্তেগফার করুন।",
        "The special hour of accepted prayers in the late afternoon after Asr. Make abundant supplications."
    ),
    NIGHT_SALAWAT(
        "সান্ধ্যকালীন আমল ও দরূদ",
        "Evening Devotion & Salawat",
        "রাসূলুল্লাহ (ﷺ)-এর ওপর অধিক দরূদ পাঠ করুন এবং সাপ্তাহিক আমলের মুহাসাবাহ করুন।",
        "Send abundant blessings upon the Prophet (ﷺ) and reflect upon your weekly deeds."
    );

    public final String titleBn;
    public final String titleEn;
    public final String descBn;
    public final String descEn;

    FridayPhase(String titleBn, String titleEn, String descBn, String descEn) {
      this.titleBn = titleBn;
      this.titleEn = titleEn;
      this.descBn = descBn;
      this.descEn = descEn;
    }

    public String getTitle(boolean isBn) {
      return isBn ? titleBn : titleEn;
    }

    public String getDesc(boolean isBn) {
      return isBn ? descBn : descEn;
    }
  }

  public FridayPhase getCurrentFridayPhase() {
    Calendar cal = Calendar.getInstance();
    int hour = cal.get(Calendar.HOUR_OF_DAY);

    if (hour >= 5 && hour < 10) {
      return FridayPhase.MORNING_KAHF;
    } else if (hour >= 10 && hour < 12) {
      return FridayPhase.PRE_JUMMAH_PREP;
    } else if (hour >= 12 && hour < 15) {
      return FridayPhase.JUMMAH_PRAYER;
    } else if (hour >= 15 && hour < 18) {
      return FridayPhase.SAATUL_IJABAH;
    } else {
      return FridayPhase.NIGHT_SALAWAT;
    }
  }

  /**
   * Checks if Friday Jummah mode is active (Calendar.FRIDAY or Thursday Maghrib to Friday Maghrib)
   */
  public boolean isFridayToday() {
    Calendar cal = Calendar.getInstance();
    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
  }

  public static String getTodayFridayKey() {
    return new SimpleDateFormat("yyyy_MM_dd", Locale.US).format(new Date());
  }

  public int getSalawatCount(Context context) {
    if (context == null) return 0;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    return prefs.getInt(KEY_SALAWAT_COUNT + getTodayFridayKey(), 0);
  }

  public void incrementSalawatCount(Context context) {
    if (context == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    String key = KEY_SALAWAT_COUNT + getTodayFridayKey();
    int current = prefs.getInt(key, 0);
    prefs.edit().putInt(key, current + 1).apply();
  }

  public void resetSalawatCount(Context context) {
    if (context == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    prefs.edit().putInt(KEY_SALAWAT_COUNT + getTodayFridayKey(), 0).apply();
  }

  public boolean isSunnahCompleted(Context context, String sunnahId) {
    if (context == null) return false;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    return prefs.getBoolean(KEY_SUNNAH_CHECK_PREFIX + sunnahId + "_" + getTodayFridayKey(), false);
  }

  public void setSunnahCompleted(Context context, String sunnahId, boolean completed) {
    if (context == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    prefs.edit().putBoolean(KEY_SUNNAH_CHECK_PREFIX + sunnahId + "_" + getTodayFridayKey(), completed).apply();
  }

  public boolean isKahfRead(Context context) {
    if (context == null) return false;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    return prefs.getBoolean(KEY_KAHF_READ + getTodayFridayKey(), false);
  }

  public void setKahfRead(Context context, boolean read) {
    if (context == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    prefs.edit().putBoolean(KEY_KAHF_READ + getTodayFridayKey(), read).apply();
  }

  /**
   * 8 Verified Authentic Friday Sunnahs with Pure Dual-Language support
   */
  public List<JummahSunnahItem> getFridaySunnahs() {
    List<JummahSunnahItem> list = new ArrayList<>();

    list.add(new JummahSunnahItem(
        "ghusl",
        "১. জুমার উদ্দেশ্যে গোসল করা",
        "1. Taking a Bath (Ghusl) for Jummah",
        "غُسْلُ يَوْمِ الْجُمُعَةِ وَاجِبٌ عَلَى كُلِّ مُحْتَلِمٍ",
        "\"জুমার দিন গোসল করা প্রতিটি প্রাপ্তবয়স্ক ব্যক্তির ওপর ওয়াজিব/গুরুত্বপূর্ণ সুন্নত।\"",
        "\"Taking a bath on Friday is essential for every adult.\"",
        "— সহীহ বুখারী: ৮৭৭, সহীহ মুসলিম: ৮৪৬",
        "— Sahih al-Bukhari: 877, Sahih Muslim: 846"
    ));

    list.add(new JummahSunnahItem(
        "miswak",
        "২. মেসওয়াক করা ও পরিষ্কার-পরিচ্ছন্নতা",
        "2. Cleaning Teeth with Miswak & Hygiene",
        "وَأَنْ يَسْتَنَّ، وَأَنْ يَمَسَّ طِيبًا إِنْ وَجَدَ",
        "\"...এবং সে যেন মেসওয়াক করে এবং সুগন্ধি থাকলে তা ব্যবহার করে।\"",
        "\"...and he should brush his teeth with Miswak and use perfume if available.\"",
        "— সহীহ বুখারী: ৮৮০, সহীহ মুসলিম: ৮৪৬",
        "— Sahih al-Bukhari: 880, Sahih Muslim: 846"
    ));

    list.add(new JummahSunnahItem(
        "clean_dress",
        "৩. উত্তম ও পরিচ্ছন্ন পোশাক পরিধান করা",
        "3. Wearing Best and Clean Clothes",
        "مَا عَلَى أَحَدِكُمْ إِنْ وَجَدَ أَنْ يَتَّخِذَ ثَوْبَيْنِ لِيَوْمِ الْجُمُعَةِ سِوَى ثَوْبَيْ مِهْنَتِهِ",
        "\"তোমাদের কারো পক্ষে সম্ভব হলে সে যেন কর্মক্ষেত্রের পোশাক ছাড়া জুমার দিনের জন্য দুটি পরিচ্ছন্ন পোশাক নির্দিষ্ট করে নেয়।\"",
        "\"If anyone can afford it, he should reserve two clean garments for Friday besides his work clothes.\"",
        "— সুনানে আবু দাউদ: ১০৭৮, সুনানে ইবনে মাজাহ: ১০৯৫ (সহীহ)",
        "— Sunan Abi Dawud: 1078, Sunan Ibn Majah: 1095 (Sahih)"
    ));

    list.add(new JummahSunnahItem(
        "attar",
        "৪. সুগন্ধি বা আতর ব্যবহার করা",
        "4. Applying Perfume or Attar",
        "مَنِ اغْتَسَلَ يَوْمَ الْجُمُعَةِ ... وَمَسَّ مِنْ طِيبِ بَيْتِهِ",
        "\"যে ব্যক্তি জুমার দিন গোসল করল এবং ঘরে সুগন্ধি থাকলে তা ব্যবহার করল...\"",
        "\"Whoever takes a bath on Friday and touches perfume from his household...\"",
        "— সহীহ বুখারী: ৮৮৩",
        "— Sahih al-Bukhari: 883"
    ));

    list.add(new JummahSunnahItem(
        "early_mosque",
        "৫. আগে আগে মসজিদে যাওয়া",
        "5. Going to the Mosque Early",
        "مَنِ اغْتَسَلَ يَوْمَ الْجُمُعَةِ غُسْلَ الْجَنَابَةِ ثُمَّ رَاحَ فَكَأَنَّمَا قَرَّبَ بَدَنَةً",
        "\"যে ব্যক্তি জুমার প্রথম প্রহরে মসজিদে যায়, সে যেন একটি উট কুরবানী করল; দ্বিতীয় প্রহরে গেলে যেন গাভী কুরবানী করল...\"",
        "\"Whoever goes early to the mosque on Friday in the first hour is like one who sacrificed a camel...\"",
        "— সহীহ বুখারী: ৮৮১, সহীহ মুসলিম: ৮৫০",
        "— Sahih al-Bukhari: 881, Sahih Muslim: 850"
    ));

    list.add(new JummahSunnahItem(
        "walk_mosque",
        "৬. সম্ভব হলে পায়ে হেঁটে মসজিদে যাওয়া",
        "6. Walking to the Mosque on Foot",
        "مَنْ غَسَّلَ يَوْمَ الْجُمُعَةِ وَاغْتَسَلَ ، ثُمَّ بَكَّرَ وَابْتَكَرَ ، وَمَشَى وَلَمْ يَرْكَبْ",
        "\"যে ব্যক্তি জুমার দিন গোসল করাল ও নিজে গোসল করল, ভোরে বের হলো, পায়ে হেঁটে গেল এবং কোনো বাহনে চড়ল না... তার প্রতিটি কদমে এক বছরের নফল নামাজ ও রোজার সওয়াব লেখা হয়।\"",
        "\"Whoever takes a bath on Friday, goes early on foot without riding, for every step he takes, he gets the reward of one year of fasting and prayer.\"",
        "— জামে আত-তিরমিযী: ৪৯৬, সুনানে আবু দাউদ: ৩৪৫ (সহীহ)",
        "— Jami` at-Tirmidhi: 496, Sunan Abi Dawud: 345 (Sahih)"
    ));

    list.add(new JummahSunnahItem(
        "salawat_kahf",
        "৭. সূরা কাহাফ তিলাওয়াত ও অধিক দরূদ পাঠ",
        "7. Reciting Surah Al-Kahf & Abundant Salawat",
        "أَكْثِرُوا عَلَيَّ مِنَ الصَّلاَةِ فِي كُلِّ يَوْمِ جُمُعَةٍ",
        "\"তোমরা জুমার দিন আমার ওপর অধিক পরিমাণে দরূদ পাঠ করো।\"",
        "\"Increase your supplications and blessings upon me on every Friday.\"",
        "— সুনানে আবু দাউদ: ১০৪৭, সুনানে বায়হাকী: ৫৯৯৪ (সহীহ)",
        "— Sunan Abi Dawud: 1047, Sunan al-Bayhaqi: 5994 (Sahih)"
    ));

    list.add(new JummahSunnahItem(
        "khutbah_silence",
        "৮. মনোযোগ সহকারে খুতবা শোনা ও নীরব থাকা",
        "8. Listening Attentively to the Khutbah in Silence",
        "إِذَا قُلْتَ لِصَاحِبِكَ يَوْمَ الْجُمُعَةِ : أَنْصِتْ . وَالإِمَامُ يَخْطُبُ ، فَقَدْ لَغَوْتَ",
        "\"জুমার দিন খতিব যখন খুতবা দেন, তখন যদি তুমি তোমার সঙ্গীকে বলো 'চুপ থাকো', তবে তুমিও একটি অনর্থক কাজ করলে।\"",
        "\"If you say to your companion 'Listen quietly' while the Imam is delivering the Khutbah on Friday, you have committed an idle deed.\"",
        "— সহীহ বুখারী: ৯৩৪, সহীহ মুসলিম: ৮৫১",
        "— Sahih al-Bukhari: 934, Sahih Muslim: 851"
    ));

    return list;
  }
}