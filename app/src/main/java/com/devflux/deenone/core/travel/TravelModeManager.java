package com.devflux.deenone.core.travel;

import android.content.Context;
import android.content.SharedPreferences;

import com.devflux.deenone.core.calculations.QiblaCalculator;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.utils.PrayerCalculator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TravelModeManager {

    private static final String PREF_TRAVEL = "deanone_travel_mode_prefs";
    private static final String KEY_ORIGIN_NAME = "key_origin_name";
    private static final String KEY_ORIGIN_LAT = "key_origin_lat";
    private static final String KEY_ORIGIN_LNG = "key_origin_lng";
    private static final String KEY_ORIGIN_AUTO_GPS = "key_origin_auto_gps";
    private static final String KEY_DEST_NAME = "key_dest_name";
    private static final String KEY_DEST_LAT = "key_dest_lat";
    private static final String KEY_DEST_LNG = "key_dest_lng";
    private static final String KEY_DEST_COUNTRY = "key_dest_country";
    private static final String KEY_STAY_DAYS = "key_stay_days";
    private static final String KEY_TRANSPORT_MODE = "key_transport_mode";
    private static final String KEY_MADHHAB = "key_safar_madhhab";

    // Standard Shariah Safar Distance Threshold (approx 48 Shar'i miles / 77.25 km to 88 km)
    public static final double SAFAR_DISTANCE_KM_THRESHOLD = 77.25;

    public enum TransportMode {
        FLIGHT("উড়োজাহাজ", 700.0, "ic_flight"),
        TRAIN("রেল / ট্রেন", 80.0, "ic_directions_transit"),
        CAR_BUS("বাস ও কার", 60.0, "ic_directions_car"),
        WALKING("পায়ে হেঁটে", 5.0, "ic_directions_walk");

        public final String titleBn;
        public final double avgSpeedKmh;
        public final String iconName;

        TransportMode(String titleBn, double avgSpeedKmh, String iconName) {
            this.titleBn = titleBn;
            this.avgSpeedKmh = avgSpeedKmh;
            this.iconName = iconName;
        }

        public String getTitle(boolean isBn) {
            if (isBn) return titleBn;
            switch (this) {
                case FLIGHT: return "Flight";
                case TRAIN: return "Train";
                case CAR_BUS: return "Bus / Car";
                case WALKING: return "Walking";
                default: return name();
            }
        }
    }

    public enum TravelStatus {
        MUQEEM("মুক্বীম - পূর্ণ সালাত", false, "আপনার ভ্রমণ দূরত্ব সফরের শরয়ী সীমা (৭৭.২৫ কিমি)-এর কম অথবা গন্তব্যে ১৫ দিন বা তদূর্ধ্ব অবস্থানের নিয়ত রয়েছে। সুতরাং ৪ রাকাতের ফরজ নামাজ পূর্ণ পড়তে হবে।"),
        MUSAFIR("শরয়ী মুসাফির - কসর ওয়াজিব", true, "আপনার ভ্রমণ দূরত্ব সফরের শরয়ী সীমা (৭৭.২৫ কিমি বা তদূর্ধ্ব) অতিক্রম করেছে এবং অবস্থান ১৫ দিনের কম। ৪ রাকাতবিশিষ্ট ফরজ নামাজে (যোহর, আসর, ইশা) ২ রাকাত কসর আদায় করবেন।");

        public final String title;
        public final boolean isQasrApplicable;
        public final String description;

        TravelStatus(String title, boolean isQasrApplicable, String description) {
            this.title = title;
            this.isQasrApplicable = isQasrApplicable;
            this.description = description;
        }

        public String getTitle(boolean isBn) {
            if (this == MUSAFIR) {
                return isBn ? "মুসাফির (কসর প্রযোজ্য)" : "Musafir (Qasr Applicable)";
            } else {
                return isBn ? "মুক্বীম (পূর্ণ সালাত)" : "Muqeem (Full Prayer)";
            }
        }

        public String getDescription(boolean isBn) {
            if (this == MUSAFIR) {
                return isBn ? "আপনার ভ্রমণ দূরত্ব সফরের শরয়ী সীমা (৭৭.২৫ কিমি বা তদূর্ধ্ব) অতিক্রম করেছে এবং অবস্থান ১৫ দিনের কম। ৪ রাকাতবিশিষ্ট ফরজ নামাজে (যোহর, আসর, ইশা) ২ রাকাত কসর আদায় করবেন।"
                            : "Your travel distance exceeds the Shar'i Safar threshold (77.25 km or more) and stay is under 15 days. Shorten 4-rak'ah Fard prayers (Dhuhr, Asr, Isha) to 2 rak'ahs.";
            } else {
                return isBn ? "আপনার ভ্রমণ দূরত্ব সফরের শরয়ী সীমা (৭৭.২৫ কিমি)-এর কম অথবা গন্তব্যে ১৫ দিন বা তদূর্ধ্ব অবস্থানের নিয়ত রয়েছে। সুতরাং ৪ রাকাতের ফরজ নামাজ পূর্ণ পড়তে হবে।"
                            : "Your travel distance is below the Shar'i Safar threshold (77.25 km) or your intended stay at the destination is 15 days or more. Therefore, complete all 4-rak'ah Fard prayers in full.";
            }
        }
    }

    public static class MadhhabRuleItem {
        public final String madhhabName;
        public final String qasrRule;
        public final String jamaRule;
        public final String stayLimit;
        public final String references;

        public MadhhabRuleItem(String madhhabName, String qasrRule, String jamaRule, String stayLimit, String references) {
            this.madhhabName = madhhabName;
            this.qasrRule = qasrRule;
            this.jamaRule = jamaRule;
            this.stayLimit = stayLimit;
            this.references = references;
        }
    }

    public static class TravelerDuaItem {
        public final String id;
        public final String title;
        public final String arabic;
        public final String pronunciation;
        public final String meaning;
        public final String reference;

        public TravelerDuaItem(String id, String title, String arabic, String pronunciation, String meaning, String reference) {
            this.id = id;
            this.title = title;
            this.arabic = arabic;
            this.pronunciation = pronunciation;
            this.meaning = meaning;
            this.reference = reference;
        }
    }

    public static class TravelInfoResult {
        public final String originName;
        public final double originLat;
        public final double originLng;
        public final String destinationName;
        public final double destLat;
        public final double destLng;
        public final double distanceKm;
        public final double distanceSharaiMiles;
        public final TravelStatus status;
        public final int stayDays;
        public final TransportMode transportMode;
        public final String estimatedDurationFormatted;
        public final float destQiblaBearing;
        public final double destDistanceToMakkahKm;
        public final PrayerCalculator.PrayerTimesResult destPrayerTimes;

        public TravelInfoResult(String originName, double originLat, double originLng,
                                String destinationName, double destLat, double destLng,
                                double distanceKm, double distanceSharaiMiles,
                                TravelStatus status, int stayDays, TransportMode transportMode,
                                String estimatedDurationFormatted, float destQiblaBearing,
                                double destDistanceToMakkahKm,
                                PrayerCalculator.PrayerTimesResult destPrayerTimes) {
            this.originName = originName;
            this.originLat = originLat;
            this.originLng = originLng;
            this.destinationName = destinationName;
            this.destLat = destLat;
            this.destLng = destLng;
            this.distanceKm = distanceKm;
            this.distanceSharaiMiles = distanceSharaiMiles;
            this.status = status;
            this.stayDays = stayDays;
            this.transportMode = transportMode;
            this.estimatedDurationFormatted = estimatedDurationFormatted;
            this.destQiblaBearing = destQiblaBearing;
            this.destDistanceToMakkahKm = destDistanceToMakkahKm;
            this.destPrayerTimes = destPrayerTimes;
        }
    }

    private static volatile TravelModeManager instance;

    public static TravelModeManager getInstance() {
        if (instance == null) {
            synchronized (TravelModeManager.class) {
                if (instance == null) {
                    instance = new TravelModeManager();
                }
            }
        }
        return instance;
    }

    /**
     * Calculates great-circle distance between two coordinate pairs using Haversine formula
     */
    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public TravelInfoResult evaluateTravelStatus(Context context, LocationProvider.Coordinates currentGpsCoords) {
        if (context == null) {
            return new TravelInfoResult("ঢাকা", 23.8103, 90.4125, "চট্টগ্রাম", 22.3569, 91.7832, 245.5, 152.5,
                    TravelStatus.MUSAFIR, 3, TransportMode.CAR_BUS, "৪ ঘণ্টা ৫ মিনিট", 267.5f, 5100, null);
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_TRAVEL, Context.MODE_PRIVATE);
        boolean isOriginAutoGps = prefs.getBoolean(KEY_ORIGIN_AUTO_GPS, true);

        double originLat;
        double originLng;
        String originName;

        if (isOriginAutoGps && currentGpsCoords != null && currentGpsCoords.latitude != 0.0) {
            originLat = currentGpsCoords.latitude;
            originLng = currentGpsCoords.longitude;
            originName = currentGpsCoords.locationName != null ? currentGpsCoords.locationName.replace("", "").trim() : "বর্তমান অবস্থান (GPS)";
        } else {
            originLat = prefs.getFloat(KEY_ORIGIN_LAT, 23.8103f);
            originLng = prefs.getFloat(KEY_ORIGIN_LNG, 90.4125f);
            originName = prefs.getString(KEY_ORIGIN_NAME, "ঢাকা");
        }

        double destLat = prefs.getFloat(KEY_DEST_LAT, 22.3569f);
        double destLng = prefs.getFloat(KEY_DEST_LNG, 91.7832f);
        String destName = prefs.getString(KEY_DEST_NAME, "চট্টগ্রাম");
        int stayDays = prefs.getInt(KEY_STAY_DAYS, 3);
        String modeStr = prefs.getString(KEY_TRANSPORT_MODE, TransportMode.CAR_BUS.name());
        TransportMode transportMode;
        try {
            transportMode = TransportMode.valueOf(modeStr);
        } catch (Exception e) {
            transportMode = TransportMode.CAR_BUS;
        }

        double distance = calculateDistanceKm(originLat, originLng, destLat, destLng);
        double sharaiMiles = distance * 0.621371;

        boolean isMusafir = distance >= SAFAR_DISTANCE_KM_THRESHOLD && stayDays < 15;
        TravelStatus status = isMusafir ? TravelStatus.MUSAFIR : TravelStatus.MUQEEM;

        double hours = distance / transportMode.avgSpeedKmh;
        int totalMins = (int) Math.round(hours * 60.0);
        int h = totalMins / 60;
        int m = totalMins % 60;
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        String durationFormatted;
        if (isBn) {
            if (h > 0 && m > 0) {
                durationFormatted = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(h) + " ঘণ্টা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(m) + " মিনিট";
            } else if (h > 0) {
                durationFormatted = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(h) + " ঘণ্টা";
            } else {
                durationFormatted = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(Math.max(1, m)) + " মিনিট";
            }
        } else {
            if (h > 0 && m > 0) {
                durationFormatted = h + " hr " + m + " min";
            } else if (h > 0) {
                durationFormatted = h + " hr";
            } else {
                durationFormatted = Math.max(1, m) + " min";
            }
        }

        float destQibla = QiblaCalculator.calculateQiblaBearing(destLat, destLng);
        double distToMakkah = calculateDistanceKm(destLat, destLng, 21.4225, 39.8262);

        Calendar cal = Calendar.getInstance();
        double destTzOffset = 6.0;
        if (destLng < 45.0 && destLng > 30.0) destTzOffset = 3.0;
        else if (destLng < -30.0 && destLng > -90.0) destTzOffset = -5.0;
        else if (destLng < 20.0 && destLng > -10.0) destTzOffset = 0.0;

        PrayerCalculator.PrayerTimesResult destPrayers =
                PrayerCalculator.calculateForLocation(destLat, destLng, destTzOffset, cal);

        return new TravelInfoResult(originName, originLat, originLng,
                destName, destLat, destLng,
                Math.round(distance * 10.0) / 10.0,
                Math.round(sharaiMiles * 10.0) / 10.0,
                status, stayDays, transportMode,
                durationFormatted, destQibla,
                Math.round(distToMakkah), destPrayers);
    }

    public void setOriginLocation(Context context, String originName, double lat, double lng, boolean isAutoGps) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_TRAVEL, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_ORIGIN_NAME, originName)
                .putFloat(KEY_ORIGIN_LAT, (float) lat)
                .putFloat(KEY_ORIGIN_LNG, (float) lng)
                .putBoolean(KEY_ORIGIN_AUTO_GPS, isAutoGps)
                .apply();
    }

    public void setTravelDestination(Context context, String destName, double lat, double lng, String country, int stayDays) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_TRAVEL, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_DEST_NAME, destName)
                .putFloat(KEY_DEST_LAT, (float) lat)
                .putFloat(KEY_DEST_LNG, (float) lng)
                .putString(KEY_DEST_COUNTRY, country != null ? country : "")
                .putInt(KEY_STAY_DAYS, stayDays)
                .apply();
    }

    public void swapOriginAndDestination(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_TRAVEL, Context.MODE_PRIVATE);
        String oName = prefs.getString(KEY_ORIGIN_NAME, "ঢাকা");
        float oLat = prefs.getFloat(KEY_ORIGIN_LAT, 23.8103f);
        float oLng = prefs.getFloat(KEY_ORIGIN_LNG, 90.4125f);

        String dName = prefs.getString(KEY_DEST_NAME, "চট্টগ্রাম");
        float dLat = prefs.getFloat(KEY_DEST_LAT, 22.3569f);
        float dLng = prefs.getFloat(KEY_DEST_LNG, 91.7832f);

        prefs.edit()
                .putString(KEY_ORIGIN_NAME, dName)
                .putFloat(KEY_ORIGIN_LAT, dLat)
                .putFloat(KEY_ORIGIN_LNG, dLng)
                .putBoolean(KEY_ORIGIN_AUTO_GPS, false)
                .putString(KEY_DEST_NAME, oName)
                .putFloat(KEY_DEST_LAT, oLat)
                .putFloat(KEY_DEST_LNG, oLng)
                .apply();
    }

    public void setTransportMode(Context context, TransportMode mode) {
        if (context == null || mode == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_TRAVEL, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_TRANSPORT_MODE, mode.name()).apply();
    }

    public void setStayDays(Context context, int stayDays) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_TRAVEL, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_STAY_DAYS, stayDays).apply();
    }

    public List<MadhhabRuleItem> getMadhhabRules() {
        return getMadhhabRules(true);
    }

    public List<MadhhabRuleItem> getMadhhabRules(boolean isBn) {
        List<MadhhabRuleItem> list = new ArrayList<>();

        if (isBn) {
            list.add(new MadhhabRuleItem(
                    "হানাফী মাযহাব",
                    "• কসর করা ওয়াজিব (আবশ্যক)।\n• ৪ রাকাতবিশিষ্ট ফরজ নামাজ (যোহর, আসর, ইশা) ২ রাকাত পড়তে হবে।\n• ফজর (২ রাকাত), মাগরিব (৩ রাকাত) এবং বিতর (৩ রাকাত) সম্পূর্ণ পড়তে হবে।\n• ভ্রমণের গতিশীল অবস্থায় সুন্নত নামাজ ছেড়ে দেওয়ার অবকাশ আছে, তবে গন্তব্যে অবস্থানকালে সুন্নত পড়া উত্তম।",
                    "• হজের আরাফাত ও মুযদালিফা ব্যতীত অন্য সাধারণ সফরে হাক্বীক্বী বা প্রকৃত জমা (এক ওয়াক্তে দুই নামাজ আদায়) জায়েজ নয়।\n• তবে 'জমায়ে সূরী' (যোহরের শেষ সময়ে যোহর এবং আসরের প্রথম সময়ে আসর আদায়) করা মুস্তাহাব।",
                    "• গন্তব্যে পৌঁছার পর যদি এক স্থানে একাধারে ১৫ দিন বা তার বেশি অবস্থানের নিয়ত থাকে, তবে সেখানে পৌঁছামাত্র মুক্বীম হয়ে যাবে এবং পূর্ণ নামাজ পড়তে হবে। ১৫ দিনের কম থাকলে মুসাফির থাকবে।",
                    "সহীহ বুখারী: ১০৮৯, ১০৯০; আল-হিদায়া ১/৮০; ফাতাওয়া হিন্দিয়া ১/১৩৯"
            ));

            list.add(new MadhhabRuleItem(
                    "শাফেয়ী মাযহাব",
                    "• কসর করা রুখসত বা জায়েজ (অনুমোদিত সুযোগ)। তবে কসর করা পূর্ণ পড়ার চেয়ে উত্তম।\n• ৪ রাকাতের ফরজ নামাজ সংক্ষিপ্ত করে ২ রাকাত আদায় করা যায়।",
                    "• সফরকালে জমা বা একত্রীকরণ অনুমোদিত (জমায়ে তাক্বদীম - অগ্রিম জমা অথবা জমায়ে তা'খীর - বিলম্বিত জমা)। যোহরের সাথে আসর এবং মাগরিবের সাথে ইশা একত্রে পড়া যায়।",
                    "• গন্তব্যে পৌঁছার দিন ও রওয়ানা হওয়ার দিন বাদে পূর্ণ ৪ দিন বা তার বেশি অবস্থানের নিয়ত করলে মুক্বীম গণ্য হবে। ৪ দিনের কম হলে মুসাফির থাকবে।",
                    "সহীহ মুসলিম: ৬৮৭, ৭০৫; আল-মাজমু' শরহুল মুহাযযাব ৪/৩৫৩"
            ));

            list.add(new MadhhabRuleItem(
                    "মালেকী মাযহাব",
                    "• কসর করা সুন্নাতে মুয়াক্কাদাহ। মুসাফিরের জন্য কসর করা অত্যন্ত তাগিদপূর্ণ সুন্নত।\n• ৪ রাকাতের ফরজ নামাজ ২ রাকাতে কসর করবে।",
                    "• ভ্রমণের গতিশীল অবস্থায় প্রয়োজনে যোহর-আসর এবং মাগরিব-ইশা জমা করা জায়েজ।",
                    "• গন্তব্যে পূর্ণ ৪ দিন বা ২০ ওয়াক্ত নামাজ পড়ার নিয়ত থাকলে সেখানে পৌঁছামাত্র মুক্বীম হয়ে যাবে।",
                    "মুওয়াত্তা মালিক: ৩২৮; বিদায়াতুল মুজতাহিদ ১/১৬১"
            ));

            list.add(new MadhhabRuleItem(
                    "হাম্বলী মাযহাব",
                    "• কসর করা মুস্তাহাব ও উত্তম। ৪ রাকাত নামাজ ২ রাকাত পড়া সুন্নত।",
                    "• সফরে প্রয়োজনে জমায়ে তাক্বদীম ও জমায়ে তা'খীর উভয়টিই করা সম্পূর্ণ বৈধ।",
                    "• গন্তব্যে ৪ দিনের বেশি (বা ২১ ওয়াক্তের বেশি) অবস্থানের নিয়ত করলে পৌঁছামাত্র মুক্বীম গণ্য হবে।",
                    "সহীহ বুখারী: ১১০১; আল-মুগনী ২/১৯৬"
            ));
        } else {
            list.add(new MadhhabRuleItem(
                    "Hanafi Madhhab",
                    "• Qasr is Wajib (obligatory).\n• 4-rak'ah Fard prayers (Dhuhr, Asr, Isha) must be shortened to 2 rak'ahs.\n• Fajr (2 rak'ahs), Maghrib (3 rak'ahs), and Witr (3 rak'ahs) remain full.\n• Sunnah prayers may be omitted while in transit, but recommended during stay.",
                    "• Combining prayers (Jam') in one time slot is not permitted in general travel except during Hajj at Arafah & Muzdalifah.\n• Jam' Suri (praying first prayer at end of its time and second at beginning of its time) is Mustahabb.",
                    "• Intending to stay 15 days or more at the destination makes one Muqeem upon arrival. Under 15 days, one remains Musafir.",
                    "Sahih Bukhari: 1089, 1090; Al-Hidayah 1/80; Fatawa Hindiyyah 1/139"
            ));

            list.add(new MadhhabRuleItem(
                    "Shafi'i Madhhab",
                    "• Qasr is a permitted dispensation (Rukhsah). Shortening is preferred over full.\n• 4-rak'ah Fard prayers are shortened to 2 rak'ahs.",
                    "• Combining prayers is permissible (Jam' Taqdeem - advance, or Jam' Ta'kheer - delayed). Dhuhr with Asr, and Maghrib with Isha.",
                    "• Intending to stay 4 complete days or more (excluding arrival and departure days) makes one Muqeem. Under 4 days, one remains Musafir.",
                    "Sahih Muslim: 687, 705; Al-Majmu' 4/353"
            ));

            list.add(new MadhhabRuleItem(
                    "Maliki Madhhab",
                    "• Qasr is Sunnah Mu'akkadah (strongly emphasized Sunnah).\n• 4-rak'ah Fard prayers are shortened to 2 rak'ahs.",
                    "• Combining prayers (Dhuhr-Asr, Maghrib-Isha) is permissible while actively on the move.",
                    "• Intending to stay 4 full days (or 20 prayer times) makes one Muqeem upon arrival.",
                    "Muwatta Malik: 328; Bidayat al-Mujtahid 1/161"
            ));

            list.add(new MadhhabRuleItem(
                    "Hanbali Madhhab",
                    "• Qasr is Mustahabb and preferred. Shortening 4 rak'ahs to 2 is Sunnah.",
                    "• Both Jam' Taqdeem and Jam' Ta'kheer are permissible when needed during travel.",
                    "• Intending to stay more than 4 days or more than 20 prayers makes one Muqeem upon arrival.",
                    "Sahih Bukhari: 1101; Al-Mughni 2/196"
            ));
        }

        return list;
    }

    public List<TravelerDuaItem> getTravelerDuas() {
        return getTravelerDuas(true);
    }

    public List<TravelerDuaItem> getTravelerDuas(boolean isBn) {
        List<TravelerDuaItem> list = new ArrayList<>();

        if (isBn) {
            list.add(new TravelerDuaItem(
                    "dua_vehicle",
                    "১. যানবাহনে আরোহণের দোয়া",
                    "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ",
                    "সুবহা-নাল্লাযী সাখখারা লানা- হা-যা- ওয়া মা- কুন্না- লাহূ মুক্বরিনী-ন, ওয়া ইন্না- ইলা- রব্বিনা- লামুনক্বালিবূ-ন।",
                    "\"পবিত্র ও মহান সেই সত্তা, যিনি একে আমাদের বশীভূত করে দিয়েছেন, অথচ আমরা একে নিয়ন্ত্রণে আনতে সক্ষম ছিলাম না। আর নিশ্চয়ই আমরা আমাদের প্রতিপালকের নিকট প্রত্যাবর্তনকারী।\"",
                    "— সূরা আয-যুখরুফ: ১৩-১৪, সহীহ মুসলিম: ১৩৪২"
            ));

            list.add(new TravelerDuaItem(
                    "dua_journey_start",
                    "২. সফর শুরুর দোয়া ও আল্লাহর সাহায্য প্রার্থনা",
                    "اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى ، وَمِنَ الْعَمَلِ مَا تَرْضَى ، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا وَاطْوِ عَنَّا بُعْدَهُ ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ ، وَالْخَلِيفَةُ فِي الأَهْلِ",
                    "আল্লাহুম্মা ইন্না নাসআলুকা ফী সাফারিনা হা-যাল বিররা ওয়াত তাক্বওয়া, ওয়া মিনাল আমালি মা তারদা। আল্লাহুম্মা হাওয়িন আলাইনা সাফারানা হা-যা ওয়াতবি আন্না বু'দাহ। আল্লাহুম্মা আনতাস সাহিবু ফিস সাফারি ওয়াল খালিফাতু ফিল আহলি।",
                    "\"হে আল্লাহ! আমরা আমাদের এই সফরে আপনার কাছে পুণ্য, তাকওয়া এবং এমন আমল প্রার্থনা করছি যা আপনি পছন্দ করেন। হে আল্লাহ! আমাদের জন্য এই সফরকে সহজ করে দিন এবং এর দূরত্বকে সংকুচিত করে দিন। হে আল্লাহ! আপনিই সফরে আমাদের একমাত্র সঙ্গী এবং পরিবার-পরিজনের অভিভাবক ও সংরক্ষক।\"",
                    "— সহীহ মুসলিম: ১৩৪২, সুনানে আবু দাউদ: ২৫৯৮"
            ));

            list.add(new TravelerDuaItem(
                    "dua_high_low",
                    "৩. পাহাড়ে/উঁচু স্থানে আরোহণ ও নিচে নামার সময় তাসবিহ",
                    "اللهُ أَكْبَرُ (উঁচু স্থানে উঠার সময়) • سُبْحَانَ اللَّهِ (নিচে নামার সময়)",
                    "আল্লাহু আকবার (আরোহণের সময়) • সুবহানাল্লাহ (নিচে নামার সময়)",
                    "\"হযরত জাবির (রা.) বলেন: আমরা যখন কোনো উঁচু জায়গায় বা পাহাড়ে উঠতাম তখন 'আল্লাহু আকবার' বলতাম, আর যখন নিচে নামতাম তখন 'সুবহানাল্লাহ' বলতাম।\"",
                    "— সহীহ বুখারী: ২৯৯৩, ২৯৯৪"
            ));

            list.add(new TravelerDuaItem(
                    "dua_entering_town",
                    "৪. নতুন শহর বা গন্তব্যে প্রবেশের দোয়া",
                    "اللَّهُمَّ رَبَّ السَّمَاوَاتِ السَّبْعِ وَمَا أَظْلَلْنَ ، وَرَبَّ الأَرَضِينَ السَّبْعِ وَمَا أَقْلَلْنَ ، أَسْأَلُكَ خَيْرَ هَذِهِ الْقَرْيَةِ وَخَيْرَ أَهْلِهَا ، وَأَعُوذُ بِكَ مِنْ شَرِّهَا وَشَرِّ أَهْلِهَا",
                    "আল্লাহুম্মা রব্বাস সামাওয়াতিস সাব'ই ওয়া মা আজলালনা, ওয়া রব্বাল আরাদীনাস সাব'ই ওয়া মা আক্বলালনা... আসআলুকা খইরা হাযিহিল ক্বারইয়াতি ওয়া খইরা আহলিহা, ওয়া আউজু বিকা মিন শাররিহা ওয়া শাররি আহলিহা।",
                    "\"হে আল্লাহ! সাত আসমান ও যা কিছু তা ছায়া দিয়ে রেখেছে তার প্রতিপালক! সাত জমিন ও যা কিছু তা ধারণ করেছে তার রব! আমি আপনার নিকট এই জনপদের কল্যাণ এবং এর অধিবাসীদের কল্যাণ প্রার্থনা করছি, আর এই জনপদের অনিষ্ট এবং এর অধিবাসীদের অনিষ্ট থেকে আপনার আশ্রয় চাচ্ছি।\"",
                    "— মুস্তাদরাকে হাকেম: ১৯৮৩, হিসনুল মুসলিম: ১৯৭"
            ));

            list.add(new TravelerDuaItem(
                    "dua_stopping_place",
                    "৫. সফরে কোনো স্থানে যাত্রাবিরতি বা হোটেলে অবস্থানের দোয়া",
                    "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
                    "আ'ঊযু বিকালিমা-তিল্লা-হিত তা-ম্মা-তি মিন শাররি মা খালাক্ব।",
                    "\"আমি আল্লাহর পরিপূর্ণ কালিমাসমূহের ওসিলায় তাঁর সৃষ্ট সকল সৃষ্টির অনিষ্ট হতে আশ্রয় প্রার্থনা করছি। (যে ব্যক্তি এই দোয়া পড়বে, সেই স্থান ত্যাগ করা পর্যন্ত কোনো কিছুই তার ক্ষতি করতে পারবে না।)\"",
                    "— সহীহ মুসলিম: ২৭০৮"
            ));

            list.add(new TravelerDuaItem(
                    "dua_returning",
                    "৬. সফর থেকে নিরাপদে প্রত্যাবর্তনের দোয়া",
                    "آيِبُونَ تَائِبُونَ عَابِدُونَ لِرَبِّنَا حَامِدُونَ",
                    "আইবূনা তা-ইবূনা আবিদূনা লিরাব্বিনা হা-মিদূন।",
                    "\"আমরা আমাদের প্রতিপালকের নিকট প্রত্যাবর্তনকারী, তওবাকারী, ইবাদতকারী এবং তাঁরই প্রশংসাকারী।\"",
                    "— সহীহ বুখারী: ১৭৯৭, সহীহ মুসলিম: ১৩৪২"
            ));

            list.add(new TravelerDuaItem(
                    "dua_farewell",
                    "৭. মুসাফিরকে বিদায় দেওয়ার ও বিদায় নেওয়ার দোয়া",
                    "أَسْتَوْدِعُ اللَّهَ دِينَكَ ، وَأَمَانَتَكَ ، وَخَوَاتِيمَ عَمَلِكَ",
                    "আস্তাওদি'উল্লাহা দীনাকা, ওয়া আমানাতাকা, ওয়া খাওয়াতিমা আমালিক।",
                    "\"আমি আপনার দ্বীন, আপনার আমানত ও আপনার সর্বশেষ কৃতকর্মের হেফাজতের দায়িত্ব আল্লাহর নিকট সমর্পণ করছি।\"",
                    "— জামে তিরমিযী: ৩৪৪২, সুনানে আবু দাউদ: ২৬০০"
            ));
        } else {
            list.add(new TravelerDuaItem(
                    "dua_vehicle",
                    "1. Supplication when boarding a vehicle / transportation",
                    "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ",
                    "Subhanal-ladhee sakhkhara lanaa haadhaa wa maa kunnaa lahoo muqrineen, wa innaa ilaa rabbinaa lamunqaliboon.",
                    "\"Glory to Him who has subjected this to us, and we could never have it by our efforts. And verily, unto our Lord we indeed are returning.\"",
                    "— Surah Az-Zukhruf: 13-14, Sahih Muslim: 1342"
            ));

            list.add(new TravelerDuaItem(
                    "dua_journey_start",
                    "2. Supplication when commencing journey & seeking Allah's guidance",
                    "اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى ، وَمِنَ الْعَمَلِ مَا تَرْضَى ، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا وَاطْوِ عَنَّا بُعْدَهُ ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ ، وَالْخَلِيفَةُ فِي الأَهْلِ",
                    "Allahumma inna nas'aluka fee safarina hadha al-birra wat-taqwa, wa minal-'amali ma tardha. Allahumma hawwin 'alayna safarana hadha watwi 'anna bu'dah. Allahumma antas-sahibu fis-safar, wal-khalifatu fil-ahl.",
                    "\"O Allah, we ask You on this journey for righteousness and piety, and for deeds that are pleasing to You. O Allah, make this journey easy for us and shorten its distance. O Allah, You are our companion on the journey and the protector of our family.\"",
                    "— Sahih Muslim: 1342, Sunan Abi Dawud: 2598"
            ));

            list.add(new TravelerDuaItem(
                    "dua_high_low",
                    "3. Glorification when ascending high places and descending",
                    "اللهُ أَكْبَرُ (Ascending) • سُبْحَانَ اللَّهِ (Descending)",
                    "Allahu Akbar (When ascending) • Subhanallah (When descending)",
                    "\"Jabir (RA) said: Whenever we went up an elevation or hill we would say 'Allahu Akbar', and whenever we went down a valley we would say 'Subhanallah'.\"",
                    "— Sahih al-Bukhari: 2993, 2994"
            ));

            list.add(new TravelerDuaItem(
                    "dua_entering_town",
                    "4. Supplication upon entering a town or destination",
                    "اللَّهُمَّ رَبَّ السَّمَاوَاتِ السَّبْعِ وَمَا أَظْلَلْنَ ، وَرَبَّ الأَرَضِينَ السَّبْعِ وَمَا أَقْلَلْنَ ، أَسْأَلُكَ خَيْرَ هَذِهِ الْقَرْيَةِ وَخَيْرَ أَهْلِهَا ، وَأَعُوذُ بِكَ مِنْ شَرِّهَا وَشَرِّ أَهْلِهَا",
                    "Allahumma Rabbas-samawatis-sab'i wa ma azlalna, wa Rabbal-ardeenas-sab'i wa ma aqlalna... as'aluka khayra hadhihil-qaryati wa khayra ahliha, wa a'oodhu bika min sharriha wa sharri ahliha.",
                    "\"O Allah, Lord of the seven heavens and all they overshadow, Lord of the seven earths and all they uphold... I ask You for the good of this town and the good of its people, and seek refuge in You from its evil and the evil of its people.\"",
                    "— Mustadrak al-Hakim: 1983, Hisn al-Muslim: 197"
            ));

            list.add(new TravelerDuaItem(
                    "dua_stopping_place",
                    "5. Supplication when stopping at a place or lodging",
                    "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
                    "A'oodhu bi kalimaatillaahit-taammaati min sharri maa khalaq.",
                    "\"I seek refuge in the perfect words of Allah from the evil of what He has created. (Whoever recites this, nothing will harm him until he departs from that place.)\"",
                    "— Sahih Muslim: 2708"
            ));

            list.add(new TravelerDuaItem(
                    "dua_returning",
                    "6. Supplication when returning from journey",
                    "آيِبُونَ تَائِبُونَ عَابِدُونَ لِرَبِّنَا حَامِدُونَ",
                    "Ayiboona, ta'iboona, 'abidoona, li-Rabbina hamidoon.",
                    "\"We return repentant, worshipping, and praising our Lord.\"",
                    "— Sahih al-Bukhari: 1797, Sahih Muslim: 1342"
            ));

            list.add(new TravelerDuaItem(
                    "dua_farewell",
                    "7. Supplication when bidding farewell to a traveler",
                    "أَسْتَوْدِعُ اللَّهَ دِينَكَ ، وَأَمَانَتَكَ ، وَخَوَاتِيمَ عَمَلِكَ",
                    "Astawdi'ullaha deenaka, wa amanataka, wa khawaateema 'amalika.",
                    "\"I entrust to Allah your religion, your trust, and the last of your deeds.\"",
                    "— Jami' at-Tirmidhi: 3442, Sunan Abi Dawud: 2600"
            ));
        }

        return list;
    }
}
