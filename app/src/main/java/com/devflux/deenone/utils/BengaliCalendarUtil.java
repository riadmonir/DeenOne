package com.devflux.deenone.utils;

import android.content.Context;

import com.devflux.deenone.core.calendar.BengaliCalendarOnlineSyncEngine;
import com.devflux.deenone.core.calendar.CalendarSettingsManager;

import java.util.Calendar;

/**
 * High-precision utility for calculating the Bengali Calendar (বঙ্গাব্দ) date, month, era, and season.
 * Supports both:
 * 1. Bangla Academy (Bangladesh Revised Standard)
 * 2. Indian Drik Siddhanta / Panjika (West Bengal, India)
 */
public class BengaliCalendarUtil {

    public enum CalculationMethod {
        BANGLA_ACADEMY("Bangla Academy", "বাংলা একাডেমি", "Bangladesh", "বাংলাদেশ"),
        INDIAN_DRIK_SIDDHANTA("Indian Drik Siddhanta", "ভারতীয় দৃক সিদ্ধান্ত", "West Bengal, India", "পশ্চিমবঙ্গ, ভারত");

        private final String displayName;
        private final String displayNameBn;
        private final String region;
        private final String regionBn;

        CalculationMethod(String displayName, String displayNameBn, String region, String regionBn) {
            this.displayName = displayName;
            this.displayNameBn = displayNameBn;
            this.region = region;
            this.regionBn = regionBn;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDisplayName(boolean isBn) {
            return isBn ? displayNameBn : displayName;
        }

        public String getRegion() {
            return region;
        }

        public String getRegion(boolean isBn) {
            return isBn ? regionBn : region;
        }
    }

    public static final String[] BENGALI_MONTHS = {
            "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ",
            "ভাদ্র", "আশ্বিন", "কার্তিক", "অগ্রহায়ণ",
            "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র"
    };

    public static final String[] BENGALI_SEASONS = {
            "গ্রীষ্মকাল", "বর্ষাকাল", "শরৎকাল",
            "হেমন্তকাল", "শীতকাল", "বসন্তকাল"
    };

    public static class BengaliDateResult {
        public int day;
        public int monthIndex; // 0-based: 0 = Boishakh, ..., 11 = Choitro
        public String monthName;
        public String seasonName;
        public int year;
        public CalculationMethod method;

        public String getFormattedDate() {
            return BengaliNumberUtil.toBengali(day) + " " + monthName + " " + BengaliNumberUtil.toBengali(year) + " বঙ্গাব্দ";
        }

        public String getFormattedDateWithSeason() {
            return BengaliNumberUtil.toBengali(day) + " " + monthName + " " + BengaliNumberUtil.toBengali(year) + " বঙ্গাব্দ (" + seasonName + ")";
        }

        public String getShortFormatted() {
            return BengaliNumberUtil.toBengali(day) + " " + monthName + " " + BengaliNumberUtil.toBengali(year);
        }
    }

    public static BengaliDateResult getBengaliDate(Context context, Calendar calendar) {
        CalculationMethod method = CalculationMethod.BANGLA_ACADEMY;
        if (context != null) {
            method = CalendarSettingsManager.getBengaliCalculationMethod(context);
            return BengaliCalendarOnlineSyncEngine.getAuthenticBengaliDate(context, calendar, method);
        }
        return getBengaliDate(calendar, method);
    }

    public static BengaliDateResult getBengaliDate(Calendar calendar) {
        return getBengaliDate(calendar, CalculationMethod.BANGLA_ACADEMY);
    }

    /**
     * Converts a Gregorian Calendar instance into a BengaliDateResult using the specified method.
     */
    public static BengaliDateResult getBengaliDate(Calendar calendar, CalculationMethod method) {
        if (calendar == null) calendar = Calendar.getInstance();
        if (method == null) method = CalculationMethod.BANGLA_ACADEMY;

        int gYear = calendar.get(Calendar.YEAR);
        int gMonth = calendar.get(Calendar.MONTH); // 0-based: 0 = Jan, ..., 11 = Dec
        int gDay = calendar.get(Calendar.DAY_OF_MONTH);

        boolean isLeapYear = isGregorianLeapYear(gYear);

        int bDay;
        int bMonthIndex;
        int bYear;

        if (method == CalculationMethod.INDIAN_DRIK_SIDDHANTA) {
            // --- Indian Drik Siddhanta (West Bengal, India) ---
            // Pohela Boishakh is on April 15.
            if (gMonth < 3 || (gMonth == 3 && gDay < 15)) {
                bYear = gYear - 594;
            } else {
                bYear = gYear - 593;
            }

            switch (gMonth) {
                case Calendar.JANUARY: // Jan
                    if (gDay < 15) {
                        bMonthIndex = 8; // পৌষ (Poush)
                        bDay = gDay + 16;
                    } else {
                        bMonthIndex = 9; // মাঘ (Magh)
                        bDay = gDay - 14;
                    }
                    break;

                case Calendar.FEBRUARY: // Feb
                    if (gDay < 14) {
                        bMonthIndex = 9; // মাঘ (Magh)
                        bDay = gDay + 17;
                    } else {
                        bMonthIndex = 10; // ফাল্গুন (Falgun)
                        bDay = gDay - 13;
                    }
                    break;

                case Calendar.MARCH: // Mar
                    int falgunDays = isLeapYear ? 30 : 29;
                    if (gDay < 15) {
                        bMonthIndex = 10; // ফাল্গুন (Falgun)
                        bDay = (falgunDays == 30) ? (gDay + 16) : (gDay + 15);
                    } else {
                        bMonthIndex = 11; // চৈত্র (Choitro)
                        bDay = gDay - 14;
                    }
                    break;

                case Calendar.APRIL: // Apr
                    if (gDay < 15) {
                        bMonthIndex = 11; // চৈত্র (Choitro)
                        bDay = gDay + 17;
                    } else {
                        bMonthIndex = 0; // বৈশাখ (Boishakh)
                        bDay = gDay - 14;
                    }
                    break;

                case Calendar.MAY: // May
                    if (gDay < 16) {
                        bMonthIndex = 0; // বৈশাখ (Boishakh)
                        bDay = gDay + 16;
                    } else {
                        bMonthIndex = 1; // জ্যৈষ্ঠ (Joistho)
                        bDay = gDay - 15;
                    }
                    break;

                case Calendar.JUNE: // Jun
                    if (gDay < 16) {
                        bMonthIndex = 1; // জ্যৈষ্ঠ (Joistho)
                        bDay = gDay + 16;
                    } else {
                        bMonthIndex = 2; // আষাঢ় (Asharh)
                        bDay = gDay - 15;
                    }
                    break;

                case Calendar.JULY: // Jul
                    if (gDay < 17) {
                        bMonthIndex = 2; // আষাঢ় (Asharh)
                        bDay = gDay + 15;
                    } else {
                        bMonthIndex = 3; // শ্রাবণ (Shrabon)
                        bDay = gDay - 16;
                    }
                    break;

                case Calendar.AUGUST: // Aug
                    if (gDay < 17) {
                        bMonthIndex = 3; // শ্রাবণ (Shrabon)
                        bDay = gDay + 15;
                    } else {
                        bMonthIndex = 4; // ভাদ্র (Bhadro)
                        bDay = gDay - 16;
                    }
                    break;

                case Calendar.SEPTEMBER: // Sep
                    if (gDay < 17) {
                        bMonthIndex = 4; // ভাদ্র (Bhadro)
                        bDay = gDay + 15;
                    } else {
                        bMonthIndex = 5; // আশ্বিন (Ashwin)
                        bDay = gDay - 16;
                    }
                    break;

                case Calendar.OCTOBER: // Oct
                    if (gDay < 18) {
                        bMonthIndex = 5; // আশ্বিন (Ashwin)
                        bDay = gDay + 14;
                    } else {
                        bMonthIndex = 6; // কার্তিক (Kartik)
                        bDay = gDay - 17;
                    }
                    break;

                case Calendar.NOVEMBER: // Nov
                    if (gDay < 17) {
                        bMonthIndex = 6; // কার্তিক (Kartik)
                        bDay = gDay + 14;
                    } else {
                        bMonthIndex = 7; // অগ্রহায়ণ (Agrahayan)
                        bDay = gDay - 16;
                    }
                    break;

                case Calendar.DECEMBER: // Dec
                    if (gDay < 16) {
                        bMonthIndex = 7; // অগ্রহায়ণ (Agrahayan)
                        bDay = gDay + 14;
                    } else {
                        bMonthIndex = 8; // পৌষ (Poush)
                        bDay = gDay - 15;
                    }
                    break;

                default:
                    bMonthIndex = 0;
                    bDay = 1;
                    break;
            }
        } else {
            // --- Bangla Academy (Bangladesh Revised Standard) ---
            // Pohela Boishakh is on April 14.
            if (gMonth < 3 || (gMonth == 3 && gDay < 14)) {
                bYear = gYear - 594;
            } else {
                bYear = gYear - 593;
            }

            switch (gMonth) {
                case Calendar.JANUARY: // Jan
                    if (gDay < 15) {
                        bMonthIndex = 8; // পৌষ (Poush)
                        bDay = gDay + 16;
                    } else {
                        bMonthIndex = 9; // মাঘ (Magh)
                        bDay = gDay - 14;
                    }
                    break;

                case Calendar.FEBRUARY: // Feb
                    if (gDay < 14) {
                        bMonthIndex = 9; // মাঘ (Magh)
                        bDay = gDay + 17;
                    } else {
                        bMonthIndex = 10; // ফাল্গুন (Falgun)
                        bDay = gDay - 13;
                    }
                    break;

                case Calendar.MARCH: // Mar
                    int falgunDays = isLeapYear ? 30 : 29;
                    if (gDay < 15) {
                        bMonthIndex = 10; // ফাল্গুন (Falgun)
                        bDay = (falgunDays == 30) ? (gDay + 16) : (gDay + 15);
                    } else {
                        bMonthIndex = 11; // চৈত্র (Choitro)
                        bDay = gDay - 14;
                    }
                    break;

                case Calendar.APRIL: // Apr
                    if (gDay < 14) {
                        bMonthIndex = 11; // চৈত্র (Choitro)
                        bDay = gDay + 17;
                    } else {
                        bMonthIndex = 0; // বৈশাখ (Boishakh)
                        bDay = gDay - 13;
                    }
                    break;

                case Calendar.MAY: // May
                    if (gDay < 15) {
                        bMonthIndex = 0; // বৈশাখ (Boishakh)
                        bDay = gDay + 17;
                    } else {
                        bMonthIndex = 1; // জ্যৈষ্ঠ (Jaishtha)
                        bDay = gDay - 14;
                    }
                    break;

                case Calendar.JUNE: // Jun
                    if (gDay < 15) {
                        bMonthIndex = 1; // জ্যৈষ্ঠ (Jaishtha)
                        bDay = gDay + 17;
                    } else {
                        bMonthIndex = 2; // আষাঢ় (Asharh)
                        bDay = gDay - 14;
                    }
                    break;

                case Calendar.JULY: // Jul
                    if (gDay < 16) {
                        bMonthIndex = 2; // আষাঢ় (Asharh)
                        bDay = gDay + 16;
                    } else {
                        bMonthIndex = 3; // শ্রাবণ (Shrabon)
                        bDay = gDay - 15;
                    }
                    break;

                case Calendar.AUGUST: // Aug
                    if (gDay < 16) {
                        bMonthIndex = 3; // শ্রাবণ (Shrabon)
                        bDay = gDay + 16;
                    } else {
                        bMonthIndex = 4; // ভাদ্র (Bhadro)
                        bDay = gDay - 15;
                    }
                    break;

                case Calendar.SEPTEMBER: // Sep
                    if (gDay < 16) {
                        bMonthIndex = 4; // ভাদ্র (Bhadro)
                        bDay = gDay + 16;
                    } else {
                        bMonthIndex = 5; // আশ্বিন (Ashwin)
                        bDay = gDay - 15;
                    }
                    break;

                case Calendar.OCTOBER: // Oct
                    if (gDay < 17) {
                        bMonthIndex = 5; // আশ্বিন (Ashwin)
                        bDay = gDay + 15;
                    } else {
                        bMonthIndex = 6; // কার্তিক (Kartik)
                        bDay = gDay - 16;
                    }
                    break;

                case Calendar.NOVEMBER: // Nov
                    if (gDay < 16) {
                        bMonthIndex = 6; // কার্তিক (Kartik)
                        bDay = gDay + 15;
                    } else {
                        bMonthIndex = 7; // অগ্রহায়ণ (Agrahayan)
                        bDay = gDay - 15;
                    }
                    break;

                case Calendar.DECEMBER: // Dec
                    if (gDay < 16) {
                        bMonthIndex = 7; // অগ্রহায়ণ (Agrahayan)
                        bDay = gDay + 15;
                    } else {
                        bMonthIndex = 8; // পৌষ (Poush)
                        bDay = gDay - 15;
                    }
                    break;

                default:
                    bMonthIndex = 0;
                    bDay = 1;
                    break;
            }
        }

        BengaliDateResult result = new BengaliDateResult();
        result.day = Math.max(1, bDay);
        result.monthIndex = Math.max(0, Math.min(11, bMonthIndex));
        result.monthName = BENGALI_MONTHS[result.monthIndex];
        result.seasonName = BENGALI_SEASONS[result.monthIndex / 2];
        result.year = bYear;
        result.method = method;

        return result;
    }

    private static boolean isGregorianLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}
