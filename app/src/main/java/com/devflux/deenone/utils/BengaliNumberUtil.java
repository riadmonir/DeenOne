package com.devflux.deenone.utils;

public class BengaliNumberUtil {

    private static final char[] BENGALI_DIGITS = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};
    private static final char[] ARABIC_DIGITS = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};

    public static String toArabicDigits(int number) {
        String numStr = String.valueOf(number);
        StringBuilder sb = new StringBuilder();
        for (char c : numStr.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(ARABIC_DIGITS[c - '0']);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toBengali(int number) {
        return toBengali(String.valueOf(number));
    }

    public static String toBengali(long number) {
        return toBengali(String.valueOf(number));
    }

    public static String toBengali(double number) {
        return toBengali(String.valueOf(number));
    }

    public static String toBengali(String input) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(BENGALI_DIGITS[c - '0']);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toEnglish(String input) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= '০' && c <= '৯') {
                sb.append((char) ('0' + (c - '০')));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getBengaliDayOfWeek(int calendarDayOfWeek) {
        switch (calendarDayOfWeek) {
            case java.util.Calendar.SUNDAY: return "রবিবার";
            case java.util.Calendar.MONDAY: return "সোমবার";
            case java.util.Calendar.TUESDAY: return "মঙ্গলবার";
            case java.util.Calendar.WEDNESDAY: return "বুধবার";
            case java.util.Calendar.THURSDAY: return "বৃহস্পতিবার";
            case java.util.Calendar.FRIDAY: return "শুক্রবার";
            case java.util.Calendar.SATURDAY: return "শনিবার";
            default: return "";
        }
    }

    public static String getBengaliMonthName(int calendarMonth) {
        String[] months = {
                "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
                "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
        };
        if (calendarMonth >= 0 && calendarMonth < months.length) {
            return months[calendarMonth];
        }
        return "";
    }

    public static String getBengaliRelativeTime(long timestamp) {
        long diff = System.currentTimeMillis() - timestamp;
        if (diff < 60 * 1000L) {
            return "এইমাত্র";
        }
        long minutes = diff / (60 * 1000L);
        if (minutes < 60) {
            return toBengali(minutes) + " মিনিট আগে";
        }
        long hours = diff / (3600 * 1000L);
        if (hours < 24) {
            return toBengali(hours) + " ঘণ্টা আগে";
        }
        long days = diff / (24 * 3600 * 1000L);
        if (days == 1) {
            return "গতকাল";
        }
        return toBengali(days) + " দিন আগে";
    }
}
