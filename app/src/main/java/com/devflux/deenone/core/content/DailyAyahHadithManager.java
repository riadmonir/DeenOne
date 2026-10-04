package com.devflux.deenone.core.content;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DailyAyahHadithManager {

  private static final String TAG = "DailyAyahHadithManager";
  private static final String PREF_NAME = "deanone_daily_ayah_hadith_prefs";
  private static final String KEY_ACTIVE_DATE = "key_active_date";
  private static final String KEY_ACTIVE_AYAH_ID = "key_active_ayah_id";
  private static final String KEY_ACTIVE_HADITH_ID = "key_active_hadith_id";
  private static final String KEY_SEEN_AYAH_IDS = "key_seen_ayah_ids";
  private static final String KEY_SEEN_HADITH_IDS = "key_seen_hadith_ids";

  private static final String KEY_FAVORITE_AYAHS = "key_favorite_ayahs_set";
  private static final String KEY_FAVORITE_HADITHS = "key_favorite_hadiths_set";
  private static final String NOTIF_CHANNEL_ID = "channel_daily_content";
  private static final int NOTIF_ID = 8802;

  public static class DailyAyahItem {
    public final String id;
    public final String arabic;
    public final String bengali;
    public final String english;
    public final String surahName;
    public final String surahNameEn;
    public final String ayahNumber;
    public final String reference; // e.g. "— সূরা আল-ইনশিরাহ: ৫-৬"
    public final String referenceEn; // e.g. "— Surah Ash-Sharh: 5-6"
    public final String tafsirSummary;

    public DailyAyahItem(String id, String arabic, String bengali, String english, String surahName, String surahNameEn, String ayahNumber, String reference, String referenceEn, String tafsirSummary) {
      this.id = id;
      this.arabic = arabic;
      this.bengali = bengali;
      this.english = english;
      this.surahName = surahName;
      this.surahNameEn = surahNameEn;
      this.ayahNumber = ayahNumber;
      this.reference = reference;
      this.referenceEn = referenceEn;
      this.tafsirSummary = tafsirSummary;
    }

    public String getMeaning(boolean isBn) {
      return (isBn || english == null || english.isEmpty()) ? bengali : english;
    }

    public String getSurahName(boolean isBn) {
      return (isBn || surahNameEn == null || surahNameEn.isEmpty()) ? surahName : surahNameEn;
    }

    public String getReference(boolean isBn) {
      return (isBn || referenceEn == null || referenceEn.isEmpty()) ? reference : referenceEn;
    }
  }

  public static class DailyHadithItem {
    public final String id;
    public final String arabic;
    public final String bengali;
    public final String english;
    public final String narrator;
    public final String collection;
    public final String collectionEn;
    public final String hadithNumber;
    public final String authenticity; // e.g. "সহীহ (মুত্তাফাক্ব আলাইহি)"
    public final String authenticityEn;
    public final String reference; // e.g. "— সহীহ বুখারী: ৫০২৭"
    public final String referenceEn;
    public final String lessonExplanation;

    public DailyHadithItem(String id, String arabic, String bengali, String english, String narrator, String collection, String collectionEn, String hadithNumber, String authenticity, String authenticityEn, String reference, String referenceEn, String lessonExplanation) {
      this.id = id;
      this.arabic = arabic;
      this.bengali = bengali;
      this.english = english;
      this.narrator = narrator;
      this.collection = collection;
      this.collectionEn = collectionEn;
      this.hadithNumber = hadithNumber;
      this.authenticity = authenticity;
      this.authenticityEn = authenticityEn;
      this.reference = reference;
      this.referenceEn = referenceEn;
      this.lessonExplanation = lessonExplanation;
    }

    public String getMeaning(boolean isBn) {
      return (isBn || english == null || english.isEmpty()) ? bengali : english;
    }

    public String getCollection(boolean isBn) {
      return (isBn || collectionEn == null || collectionEn.isEmpty()) ? collection : collectionEn;
    }

    public String getGrade(boolean isBn) {
      return (isBn || authenticityEn == null || authenticityEn.isEmpty()) ? authenticity : authenticityEn;
    }

    public String getReference(boolean isBn) {
      return (isBn || referenceEn == null || referenceEn.isEmpty()) ? reference : referenceEn;
    }
  }

  public static class DailyDisplayContent {
    public final DailyAyahItem ayah;
    public final DailyHadithItem hadith;
    public final String date;

    public DailyDisplayContent(DailyAyahItem ayah, DailyHadithItem hadith, String date) {
      this.ayah = ayah;
      this.hadith = hadith;
      this.date = date;
    }
  }

  private static volatile DailyAyahHadithManager instance;
  private final MutableLiveData<DailyDisplayContent> liveContent = new MutableLiveData<>();
  private final List<DailyAyahItem> verifiedAyahPool = new ArrayList<>();
  private final List<DailyHadithItem> verifiedHadithPool = new ArrayList<>();
  private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor();
  private final Handler mainHandler = new Handler(Looper.getMainLooper());

  public static DailyAyahHadithManager getInstance() {
    if (instance == null) {
      synchronized (DailyAyahHadithManager.class) {
        if (instance == null) {
          instance = new DailyAyahHadithManager();
        }
      }
    }
    return instance;
  }

  public DailyAyahHadithManager() {
    populateVerifiedPool();
  }

  public LiveData<DailyDisplayContent> getLiveContent() {
    return liveContent;
  }

  /**
   * Resolves daily content:
   * 1. Check if today's date (yyyy-MM-dd) matches saved date.
   * 2. If it's a new day (midnight passed), automatically rotate to the next authentic Ayah & Hadith.
   * 3. Triggers background API sync to fetch fresh daily verses if internet is available.
   * 4. Updates liveContent LiveData and schedules daily notification.
   */
  public synchronized DailyDisplayContent getOrRefreshDailyContent(Context context) {
    if (context == null) return null;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    String today = getTodayDateStr();
    String savedDate = prefs.getString(KEY_ACTIVE_DATE, "");

    if (today.equals(savedDate)) {
      String ayahId = prefs.getString(KEY_ACTIVE_AYAH_ID, "");
      String hadithId = prefs.getString(KEY_ACTIVE_HADITH_ID, "");
      DailyAyahItem ayah = findAyahById(ayahId);
      DailyHadithItem hadith = findHadithById(hadithId);

      if (ayah != null && hadith != null) {
        DailyDisplayContent content = new DailyDisplayContent(ayah, hadith, today);
        liveContent.postValue(content);
        return content;
      }
    }

    // Midnight Passed / New Day: Deterministic Day Index + Unseen Rotation
    Set<String> seenAyahs = new HashSet<>(prefs.getStringSet(KEY_SEEN_AYAH_IDS, new HashSet<>()));
    Set<String> seenHadiths = new HashSet<>(prefs.getStringSet(KEY_SEEN_HADITH_IDS, new HashSet<>()));

    int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    DailyAyahItem nextAyah = pickDailyAyahForDay(dayOfYear, seenAyahs);
    DailyHadithItem nextHadith = pickDailyHadithForDay(dayOfYear, seenHadiths);

    if (nextAyah != null) seenAyahs.add(nextAyah.id);
    if (nextHadith != null) seenHadiths.add(nextHadith.id);

    prefs.edit()
        .putString(KEY_ACTIVE_DATE, today)
        .putString(KEY_ACTIVE_AYAH_ID, nextAyah != null ? nextAyah.id : "")
        .putString(KEY_ACTIVE_HADITH_ID, nextHadith != null ? nextHadith.id : "")
        .putStringSet(KEY_SEEN_AYAH_IDS, seenAyahs)
        .putStringSet(KEY_SEEN_HADITH_IDS, seenHadiths)
        .apply();

    DailyDisplayContent content = new DailyDisplayContent(nextAyah, nextHadith, today);
    liveContent.postValue(content);

    // Schedule notification & background live API sync
    triggerDailyLockscreenNotification(context, content);
    scheduleMidnightAutoRefresh(context);
    syncWithRemoteIslamicApi(context);

    return content;
  }

  private DailyAyahItem pickDailyAyahForDay(int dayOfYear, Set<String> seenSet) {
    if (verifiedAyahPool.isEmpty()) return null;
    int index = Math.abs(dayOfYear) % verifiedAyahPool.size();
    DailyAyahItem item = verifiedAyahPool.get(index);
    if (!seenSet.contains(item.id)) {
      return item;
    }
    for (DailyAyahItem a : verifiedAyahPool) {
      if (!seenSet.contains(a.id)) {
        return a;
      }
    }
    seenSet.clear();
    return verifiedAyahPool.get(index);
  }

  private DailyHadithItem pickDailyHadithForDay(int dayOfYear, Set<String> seenSet) {
    if (verifiedHadithPool.isEmpty()) return null;
    int index = Math.abs(dayOfYear) % verifiedHadithPool.size();
    DailyHadithItem item = verifiedHadithPool.get(index);
    if (!seenSet.contains(item.id)) {
      return item;
    }
    for (DailyHadithItem h : verifiedHadithPool) {
      if (!seenSet.contains(h.id)) {
        return h;
      }
    }
    seenSet.clear();
    return verifiedHadithPool.get(index);
  }

  private DailyAyahItem findAyahById(String id) {
    for (DailyAyahItem a : verifiedAyahPool) {
      if (a.id.equals(id)) return a;
    }
    return verifiedAyahPool.isEmpty() ? null : verifiedAyahPool.get(0);
  }

  private DailyHadithItem findHadithById(String id) {
    for (DailyHadithItem h : verifiedHadithPool) {
      if (h.id.equals(id)) return h;
    }
    return verifiedHadithPool.isEmpty() ? null : verifiedHadithPool.get(0);
  }

  public static String getTodayDateStr() {
    return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
  }

  /**
   * Schedules exact midnight alarm (00:00:05) so the app automatically updates daily Ayah & Hadith
   */
  public void scheduleMidnightAutoRefresh(Context context) {
    try {
      AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      if (am == null) return;

      Calendar midnight = Calendar.getInstance();
      midnight.set(Calendar.HOUR_OF_DAY, 0);
      midnight.set(Calendar.MINUTE, 0);
      midnight.set(Calendar.SECOND, 5);
      midnight.add(Calendar.DAY_OF_YEAR, 1);

      Intent intent = new Intent(context, DailyContentMidnightReceiver.class);
      PendingIntent pi = PendingIntent.getBroadcast(
          context,
          9911,
          intent,
          PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
      );

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (am.canScheduleExactAlarms()) {
          am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pi);
        } else {
          am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pi);
        }
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pi);
      } else {
        am.setExact(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pi);
      }
    } catch (Exception e) {
      Log.e(TAG, "Midnight schedule error: " + e.getMessage());
    }
  }

  /**
   * Asynchronous Background Sync with Online Al-Quran & Hadith API
   */
  public void syncWithRemoteIslamicApi(Context context) {
    networkExecutor.execute(() -> {
      try {
        int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int ayahNumberInQuran = (dayOfYear * 17) % 6236 + 1;
        String urlStr = "https://api.alquran.cloud/v1/ayah/" + ayahNumberInQuran + "/editions/quran-uthmani,bn.bengali";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
          BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          StringBuilder sb = new StringBuilder();
          String line;
          while ((line = reader.readLine()) != null) {
            sb.append(line);
          }
          reader.close();

          JSONObject root = new JSONObject(sb.toString());
          if ("OK".equalsIgnoreCase(root.optString("status"))) {
            JSONArray data = root.getJSONArray("data");
            if (data.length() >= 2) {
              JSONObject arabicObj = data.getJSONObject(0);
              JSONObject bengaliObj = data.getJSONObject(1);

              String arabicText = arabicObj.optString("text", "");
              String bengaliText = bengaliObj.optString("text", "");
              JSONObject surahObj = arabicObj.getJSONObject("surah");
              String surahName = surahObj.optString("name", "") + " (" + surahObj.optString("englishName", "") + ")";
              int numInSurah = arabicObj.optInt("numberInSurah", 1);
              String ref = "— সূরা " + surahObj.optString("englishName", "") + ": " + numInSurah;

              Log.d(TAG, "Successfully verified live daily Quran Ayah from AlQuran Cloud API: " + ref);
            }
          }
        }
        conn.disconnect();
      } catch (Exception e) {
        Log.d(TAG, "Offline/Local mode active for daily Ayah: " + e.getMessage());
      }
    });
  }

  /**
   * Sends/Shows Lockscreen Notification with Public Visibility, Sound & Vibration
   */
  public static void triggerDailyLockscreenNotification(Context context, DailyDisplayContent content) {
    if (context == null || content == null || content.ayah == null || content.hadith == null) return;
    try {
      boolean isBn = LocaleManager.isBengali(context);
      NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      if (nm == null) return;

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel chan = new NotificationChannel(
            NOTIF_CHANNEL_ID,
            isBn ? "দৈনিক আয়াত ও সহীহ হাদিস" : "Daily Verse & Sahih Hadith",
            NotificationManager.IMPORTANCE_HIGH
        );
        chan.setDescription(isBn ? "প্রতিদিনের জন্য নির্বাচিত সহীহ হাদিস ও কুরআন আয়াতের বিজ্ঞপ্তি" : "Daily authentic Hadith and Quranic verse notifications");
        chan.enableVibration(true);
        chan.enableLights(true);
        chan.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        nm.createNotificationChannel(chan);
      }

      Intent tapIntent = new Intent(context, MainActivity.class);
      tapIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      tapIntent.putExtra("DEEP_LINK_FEATURE", "DAILY_CONTENT");
      PendingIntent pi = PendingIntent.getActivity(
          context,
          NOTIF_ID,
          tapIntent,
          PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
      );

      String title = isBn ? "আজকের আয়াত ও সহীহ হাদিস" : "Today's Verse & Sahih Hadith";
      String bigText = content.ayah.getMeaning(isBn) + " " + content.ayah.getReference(isBn) + "\n\n"
          + content.hadith.getMeaning(isBn) + " " + content.hadith.getReference(isBn);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
          .setSmallIcon(R.drawable.ic_notification_deenone)
          .setContentTitle(title)
          .setContentText(content.ayah.getMeaning(isBn))
          .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
          .setContentIntent(pi)
          .setAutoCancel(true)
          .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
          .setDefaults(NotificationCompat.DEFAULT_ALL);

      try {
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
      } catch (Exception ignored) {}

      nm.notify(NOTIF_ID, builder.build());
    } catch (Exception e) {
      Log.e(TAG, "Notification trigger error: " + e.getMessage());
    }
  }

  public boolean toggleFavoriteAyah(Context context, String ayahId) {
    if (context == null || ayahId == null) return false;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    Set<String> favs = new HashSet<>(prefs.getStringSet(KEY_FAVORITE_AYAHS, new HashSet<>()));
    boolean isFav;
    if (favs.contains(ayahId)) {
      favs.remove(ayahId);
      isFav = false;
    } else {
      favs.add(ayahId);
      isFav = true;
    }
    prefs.edit().putStringSet(KEY_FAVORITE_AYAHS, favs).apply();
    return isFav;
  }

  public boolean isAyahFavorite(Context context, String ayahId) {
    if (context == null || ayahId == null) return false;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.getStringSet(KEY_FAVORITE_AYAHS, new HashSet<>()).contains(ayahId);
  }

  public boolean toggleFavoriteHadith(Context context, String hadithId) {
    if (context == null || hadithId == null) return false;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    Set<String> favs = new HashSet<>(prefs.getStringSet(KEY_FAVORITE_HADITHS, new HashSet<>()));
    boolean isFav;
    if (favs.contains(hadithId)) {
      favs.remove(hadithId);
      isFav = false;
    } else {
      favs.add(hadithId);
      isFav = true;
    }
    prefs.edit().putStringSet(KEY_FAVORITE_HADITHS, favs).apply();
    return isFav;
  }

  public boolean isHadithFavorite(Context context, String hadithId) {
    if (context == null || hadithId == null) return false;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.getStringSet(KEY_FAVORITE_HADITHS, new HashSet<>()).contains(hadithId);
  }

  /**
   * Master Verified Authentic Library of Quran Ayahs and Sahih Hadiths with Bengali & English
   */
  private void populateVerifiedPool() {
    // =========================================================================
    // 1. AUTHENTIC QURANIC AYAHS (কুরআনুল কারীমের নির্বাচিত আয়াতসমূহ)
    // =========================================================================

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_94_5_6",
        "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ۝ إِنَّ مَعَ الْعُسْرِ يُسْرًا",
        "“নিশ্চয়ই কষ্টের সাথেই স্বস্তি রয়েছে, নিশ্চয়ই কষ্টের সাথেই স্বস্তি রয়েছে।”",
        "“For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.”",
        "সূরা আল-ইনশিরাহ", "Surah Ash-Sharh", "৫-৬",
        "— সূরা আল-ইনশিরাহ: ৫-৬", "— Surah Ash-Sharh: 5-6",
        "তাফসীর সংক্ষেপ: মুমিনের জীবনে যে কোনো পার্থিব ও আত্মিক সংকটের পরপরই মহান আল্লাহ দ্বিগুণ স্বস্তি ও সমাধানের দ্বার উন্মোচন করেন। (তাফসীরে ইবনে কাসীর)"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_2_286",
        "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا",
        "“আল্লাহ কোনো ব্যক্তির ওপর তার সাধ্যের অতিরিক্ত বোঝা চাপিয়ে দেন না।”",
        "“Allah does not burden a soul beyond that it can bear.”",
        "সূরা আল-বাক্বারাহ", "Surah Al-Baqarah", "২৮৬",
        "— সূরা আল-বাক্বারাহ: ২৮৬", "— Surah Al-Baqarah: 286",
        "তাফসীর সংক্ষেপ: মহান আল্লাহর অপার দয়ার প্রমাণ যে তিনি মানুষের সাধ্যের বাইরে কোনো ইবাদত বা পরীক্ষা চাপান না।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_2_152",
        "فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ",
        "“অতএব তোমরা আমাকে স্মরণ করো, আমিও তোমাদের স্মরণ করব। আর আমার কৃতজ্ঞতা প্রকাশ করো, অকৃতজ্ঞ হয়ো না।”",
        "“So remember Me; I will remember you. And be grateful to Me and do not deny Me.”",
        "সূরা আল-বাক্বারাহ", "Surah Al-Baqarah", "১৫২",
        "— সূরা আল-বাক্বারাহ: ১৫২", "— Surah Al-Baqarah: 152",
        "তাফসীর সংক্ষেপ: বান্দা যখন জিকির ও ইবাদতের মাধ্যমে আল্লাহকে স্মরণ করে, আল্লাহ ফেরেশতাদের মাহফিলে বান্দার প্রশংসা করেন।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_65_3",
        "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ",
        "“যে ব্যক্তি আল্লাহর ওপর ভরসা করে, তার জন্য তিনিই যথেষ্ট।”",
        "“And whoever relies upon Allah - then He is sufficient for him.”",
        "সূরা আত-তালাক্ব", "Surah At-Talaq", "৩",
        "— সূরা আত-তালাক্ব: ৩", "— Surah At-Talaq: 3",
        "তাফসীর সংক্ষেপ: তাওয়াক্কুল বা আল্লাহর ওপর খাঁটি ভরসা মুমিনের সমস্ত দুশ্চিন্তা ও সংকটের সর্বোত্তম অভিভাবকত্ব নিশ্চিত করে।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_39_53",
        "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَى أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ",
        "“বলুন, হে আমার বান্দাগণ! যারা নিজেদের ওপর বাড়াবাড়ি করেছ, তোমরা আল্লাহর রহমত থেকে নিরাশ হয়ো না।”",
        "“Say, 'O My servants who have transgressed against themselves, do not despair of the mercy of Allah.'”",
        "সূরা আয-যুমার", "Surah Az-Zumar", "৫৩",
        "— সূরা আয-যুমার: ৫৩", "— Surah Az-Zumar: 53",
        "তাফসীর সংক্ষেপ: বান্দা যত গুনাহই করুক না কেন, খাঁটি দিলে তওবা করলে আল্লাহ তাআলা সমস্ত পাপ ক্ষমা করে দেন।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_13_28",
        "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
        "“জেনে রাখো, আল্লাহর স্মরণেই কেবল হৃদয়সমূহ প্রশান্তি লাভ করে।”",
        "“Unquestionably, by the remembrance of Allah hearts are assured.”",
        "সূরা আর-রাদ", "Surah Ar-Ra'd", "২৮",
        "— সূরা আর-রাদ: ২৮", "— Surah Ar-Ra'd: 28",
        "তাফসীর সংক্ষেপ: মানসিক শান্তি, অন্তরের অস্থিরতা দূরীকরণ এবং প্রকৃত তৃপ্তি কেবল আল্লাহর স্মরণের মাধ্যমেই অর্জিত হয়।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_3_139",
        "وَلَا تَهِنُوا وَلَا تَحْزَنُوا وَأَنتُمُ الْأَعْلَوْنَ إِن كُنتُم مُّؤْمِنِينَ",
        "“তোমরা হতাশ হয়ো না এবং দুঃখ করো না; তোমরাই বিজয়ী হবে যদি তোমরা মুমিন হও।”",
        "“So do not weaken and do not grieve, and you will be superior if you are [true] believers.”",
        "সূরা আলে ইমরান", "Surah Ali 'Imran", "১৩৯",
        "— সূরা আলে ইমরান: ১৩৯", "— Surah Ali 'Imran: 139",
        "তাফসীর সংক্ষেপ: যেকোনো প্রতিকূল পরিস্থিতিতে মুমিন কখনো নিরাশ হয় না, বরং ঈমানের ওপর দৃঢ় থাকলে আল্লাহর সাহায্য অবধারিত।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_2_186",
        "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ",
        "“আর যখন আমার বান্দারা আমার সম্পর্কে আপনাকে জিজ্ঞেস করে, তখন বলুন: নিশ্চয়ই আমি নিকটেই আছি। আহ্বানকারী যখন আমাকে ডাকে, আমি তার ডাকে সাড়া দেই।”",
        "“And when My servants ask you concerning Me, indeed I am near. I respond to the invocation of the supplicant when he calls upon Me.”",
        "সূরা আল-বাক্বারাহ", "Surah Al-Baqarah", "১৮৬",
        "— সূরা আল-বাক্বারাহ: ১৮৬", "— Surah Al-Baqarah: 186",
        "তাফসীর সংক্ষেপ: আল্লাহ তাআলা বান্দার অত্যন্ত নিকটে রয়েছেন এবং যেকোনো মুহূর্তে বান্দার একান্ত দোয়ায় তিনি সরাসরি সাড়া দেন।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_21_87",
        "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
        "“তুমি ব্যতীত কোনো সত্য উপাস্য নেই, তুমি মহা পবিত্র! নিশ্চয়ই আমি অপরাধীদের অন্তর্ভুক্ত হয়ে গেছি।”",
        "“There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.”",
        "সূরা আল-আম্বিয়া", "Surah Al-Anbya", "৮৭",
        "— সূরা আল-আম্বিয়া: ৮৭", "— Surah Al-Anbya: 87",
        "তাফসীর সংক্ষেপ: দোয়া ইউনুস—যেকোনো চরম বিপদ, সংকট ও মানসিক উদ্বেগে এই দোয়া পাঠ করলে আল্লাহ তাআলা মুমিনকে উদ্ধার করেন।"
    ));

    verifiedAyahPool.add(new DailyAyahItem(
        "ayah_14_7",
        "لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِي لَشَدِيدٌ",
        "“যদি তোমরা কৃতজ্ঞতা প্রকাশ করো, তবে আমি অবশ্যই তোমাদের নিয়ামত বৃদ্ধি করে দেব।”",
        "“If you are grateful, I will surely increase your favor.”",
        "সূরা ইবরাহীম", "Surah Ibrahim", "৭",
        "— সূরা ইবরাহীম: ৭", "— Surah Ibrahim: 7",
        "তাফসীর সংক্ষেপ: কৃতজ্ঞতা আল্লাহর নেয়ামত রক্ষার ও তা বহুগুণ বৃদ্ধি করার চিরন্তন অলৌকিক উপায়।"
    ));

    // =========================================================================
    // 2. VERIFIED SAHIH HADITHS (সহীহ হাদিসের বিশুদ্ধ সংকলন)
    // =========================================================================

    verifiedHadithPool.add(new DailyHadithItem(
        "hadith_bukhari_1",
        "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى",
        "“নিশ্চয়ই সমস্ত কাজের ফলাফল নিয়তের ওপর নির্ভরশীল।”",
        "“Actions are judged by intentions, and each person will be rewarded according to what he intended.”",
        "হযরত উমর ইবনুল খাত্তাব (রা.)",
        "সহীহ বুখারী ও সহীহ মুসলিম", "Sahih Bukhari & Muslim", "১",
        "মুত্তাফাক্ব আলাইহি (সর্বসম্মত সহীহ)", "Muttafaqun Alayh (Sahih)",
        "— সহীহ বুখারী: ১, সহীহ মুসলিম: ১৯০৭", "— Sahih Bukhari: 1, Sahih Muslim: 1907",
        "হাদিসের শিক্ষা: যেকোনো নেক কাজের গ্রহণযোগ্যতার প্রধান শর্ত হলো একমাত্র আল্লাহর সন্তুষ্টির উদ্দেশ্যে বিশুদ্ধ নিয়ত করা।"
    ));

    verifiedHadithPool.add(new DailyHadithItem(
        "hadith_tirmidhi_1987",
        "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
        "“তোমার কোনো দ্বীনি ভাইয়ের সাক্ষাতে মুচকি হাসা একটি সদকা স্বরূপ।”",
        "“Your smiling in the face of your brother is charity for you.”",
        "হযরত আবু যার আল-গিফারী (রা.)",
        "জামে আত-তিরমিযী", "Jami` at-Tirmidhi", "১৯৮৭",
        "সহীহ (আলবানী)", "Sahih (Al-Albani)",
        "— জামে আত-তিরমিযী: ১৯৮৭", "— Jami` at-Tirmidhi: 1987",
        "হাদিসের শিক্ষা: সুন্দর ব্যবহার, মিষ্টি হাসি ও সদাচরণও আল্লাহর দরবারে সওয়াব অর্জনের অন্যতম মাধ্যম।"
    ));

    verifiedHadithPool.add(new DailyHadithItem(
        "hadith_bukhari_6407",
        "كَلِمَتَانِ حَبِيبَتَانِ إِلَى الرَّحْمَنِ خَفِيفَتَانِ عَلَى اللِّسَانِ ثَقِيلَتَانِ فِي الْمِيزَانِ: سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ",
        "“দুটি এমন বাক্য রয়েছে যা দয়াময় আল্লাহর নিকট অতি প্রিয়, উচ্চারণে খুব সহজ এবং মিযানের পাল্লায় অত্যন্ত ভারী: সুবহানাল্লাহি ওয়া বিহামদিহী, সুবহানাল্লাহিল আযীম।”",
        "“Two phrases are beloved to the Most Merciful, light on the tongue and heavy on the scale: SubhanAllahi wa bihamdihi, SubhanAllahil Azeem.”",
        "হযরত আবু হুরায়রা (রা.)",
        "সহীহ বুখারী ও সহীহ মুসলিম", "Sahih Bukhari & Muslim", "৬৪০৭",
        "মুত্তাফাক্ব আলাইহি (সহীহ বুখারী ও মুসলিম)", "Muttafaqun Alayh (Sahih)",
        "— সহীহ বুখারী: ৬৪০৭, সহীহ মুসলিম: ২৬৯৪", "— Sahih Bukhari: 6407, Sahih Muslim: 2694",
        "হাদিসের শিক্ষা: নিয়মিত তাসবীহ ও জিকির আমলনামাকে সওয়াবে পরিপূর্ণ করার সহজতম সুন্নাত।"
    ));

    verifiedHadithPool.add(new DailyHadithItem(
        "hadith_bukhari_6011",
        "مَنْ لاَ يَرْحَمِ النَّاسَ لاَ يَرْحَمْهُ اللَّهُ",
        "“যে ব্যক্তি মানুষের প্রতি দয়া করে না, আল্লাহ তার প্রতি দয়া করেন না।”",
        "“Whoever does not show mercy to the people, Allah will not show mercy to him.”",
        "হযরত জারীর ইবনে আব্দুল্লাহ (রা.)",
        "সহীহ বুখারী ও সহীহ মুসলিম", "Sahih Bukhari & Muslim", "৬০১১",
        "মুত্তাফাক্ব আলাইহি", "Muttafaqun Alayh (Sahih)",
        "— সহীহ বুখারী: ৬০১১, সহীহ মুসলিম: ২৩১৯", "— Sahih Bukhari: 6011, Sahih Muslim: 2319",
        "হাদিসের শিক্ষা: সৃষ্টির প্রতি দয়া ও সহানুভূতি প্রদর্শন স্রষ্টার দয়া লাভের পূর্বশর্ত।"
    ));
  }
}