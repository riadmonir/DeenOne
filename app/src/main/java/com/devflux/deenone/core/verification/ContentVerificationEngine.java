package com.devflux.deenone.core.verification;

import android.util.Log;

public class ContentVerificationEngine {

    private static final String TAG = "ContentVerificationEngine";

    public static class ValidationResult {
        public final boolean isValid;
        public final String rejectionReason;
        public final VerificationStatus status;

        public ValidationResult(boolean isValid, String rejectionReason, VerificationStatus status) {
            this.isValid = isValid;
            this.rejectionReason = rejectionReason;
            this.status = status;
        }

        public static ValidationResult valid(VerificationStatus status) {
            return new ValidationResult(true, null, status);
        }

        public static ValidationResult invalid(String reason) {
            return new ValidationResult(false, reason, VerificationStatus.REJECTED);
        }
    }

    /**
     * Validates Quran Ayah before UI rendering or DB caching
     */
    public static ValidationResult validateQuranAyah(int surahNumber, int ayahNumber, String textArabic, String translation) {
        if (surahNumber < 1 || surahNumber > 114) {
            return ValidationResult.invalid("অবৈধ সূরা নম্বর: " + surahNumber);
        }
        if (ayahNumber < 1 || ayahNumber > 286) {
            return ValidationResult.invalid("অবৈধ আয়াত নম্বর: " + ayahNumber);
        }
        if (textArabic == null || textArabic.trim().isEmpty()) {
            return ValidationResult.invalid("আরবি টেক্সট খালি বা অনুপস্থিত");
        }
        if (translation == null || translation.trim().isEmpty()) {
            return ValidationResult.invalid("অনুবাদ খালি বা অনুপস্থিত");
        }

        // Basic Arabic Unicode script validation
        boolean containsArabic = false;
        for (char c : textArabic.toCharArray()) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
            if (block == Character.UnicodeBlock.ARABIC ||
                block == Character.UnicodeBlock.ARABIC_SUPPLEMENT ||
                block == Character.UnicodeBlock.ARABIC_EXTENDED_A ||
                block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_A ||
                block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_B) {
                containsArabic = true;
                break;
            }
        }

        if (!containsArabic) {
            return ValidationResult.invalid("আরবি হরফ অনুপস্থিত বা বিকৃত টেক্সট");
        }

        return ValidationResult.valid(VerificationStatus.MUTAWATIR);
    }

    /**
     * Validates Hadith text and canonical collection reference
     */
    public static ValidationResult validateHadith(String collection, String hadithRef, String matn) {
        if (collection == null || collection.trim().isEmpty()) {
            return ValidationResult.invalid("হাদিস গ্রন্থ অনুপস্থিত");
        }
        if (hadithRef == null || hadithRef.trim().isEmpty()) {
            return ValidationResult.invalid("হাদিস নম্বর বা রেফারেন্স অনুপস্থিত");
        }
        if (matn == null || matn.trim().length() < 10) {
            return ValidationResult.invalid("হাদিসের মূল পাঠ্য অত্যন্ত সংক্ষিপ্ত বা অসম্পূর্ণ");
        }

        // Check against recognized canonical collections
        String cLower = collection.toLowerCase();
        boolean isCanonical = cLower.contains("bukhari") || cLower.contains("বুখারী") ||
                              cLower.contains("muslim") || cLower.contains("মুসলিম") ||
                              cLower.contains("abu dawud") || cLower.contains("দাউদ") ||
                              cLower.contains("tirmidhi") || cLower.contains("তিরমিযী") ||
                              cLower.contains("nasa'i") || cLower.contains("নাসায়ী") ||
                              cLower.contains("ibn majah") || cLower.contains("মাজাহ") ||
                              cLower.contains("muwatta") || cLower.contains("মুয়াত্তা") ||
                              cLower.contains("ahmad") || cLower.contains("আহমাদ");

        if (!isCanonical) {
            Log.w(TAG, "Non-canonical collection under review: " + collection);
            return ValidationResult.valid(VerificationStatus.UNDER_REVIEW);
        }

        return ValidationResult.valid(VerificationStatus.AUTHENTIC_SAHIH);
    }

    /**
     * Validates calculated Prayer timings
     */
    public static ValidationResult validatePrayerTimings(String fajr, String sunrise, String dhuhr, String asr, String maghrib, String isha) {
        if (fajr == null || sunrise == null || dhuhr == null || asr == null || maghrib == null || isha == null) {
            return ValidationResult.invalid("নামাজের সময়সূচীর ওয়াক্ত অনুপস্থিত");
        }
        if (fajr.isEmpty() || dhuhr.isEmpty() || asr.isEmpty() || maghrib.isEmpty() || isha.isEmpty()) {
            return ValidationResult.invalid("নামাজের সময়সূচীর ওয়াক্ত খালি");
        }
        return ValidationResult.valid(VerificationStatus.VERIFIED_ASTRONOMICAL);
    }

    /**
     * Validates calculated Hijri date
     */
    public static ValidationResult validateHijriDate(int day, int monthIndex, int year) {
        if (day < 1 || day > 30) {
            return ValidationResult.invalid("অবৈধ হিজরী তারিখ (দিন): " + day);
        }
        if (monthIndex < 0 || monthIndex > 11) {
            return ValidationResult.invalid("অবৈধ হিজরী মাস: " + monthIndex);
        }
        if (year < 1400 || year > 1600) {
            return ValidationResult.invalid("অসম্ভাব্য হিজরী সাল: " + year);
        }
        return ValidationResult.valid(VerificationStatus.VERIFIED_ASTRONOMICAL);
    }
}
