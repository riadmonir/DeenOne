package com.devflux.deenone.data.repository;

import android.util.Log;

import com.devflux.deenone.core.quran.QuranBismillahHelper;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Robust, production-grade Quran API Data Normalizer for DeenOne.
 * 
 * Solves:
 * 1. Array-index mismatch: Merges API streams using explicit (SurahNumber, AyahNumber) keys.
 * 2. Bismillah offset problem: Sanitizes any prepended Bismillah in Ayah 1 for Surahs 2..114.
 * 3. Missing Ayah protection: Validates continuous sequence without shift errors.
 * 4. Duplicate Ayah protection: Ensures strictly unique (SurahNumber, AyahNumber) keys.
 * 5. Universal support across all 114 Surahs.
 */
public final class QuranApiDataNormalizer {

    private static final String TAG = "QuranApiNormalizer";

    public static class RawAyahPayload {
        public final int surahNumber;
        public final int ayahNumber;
        public final String textArabic;
        public final String translationBengali;
        public final String translationEnglish;
        public final String transliterationBengali;
        public final String audioUrl;
        public final int juzNumber;
        public final int rukuNumber;

        public RawAyahPayload(int surahNumber, int ayahNumber, String textArabic,
                                String translationBengali, String translationEnglish,
                                String transliterationBengali, String audioUrl,
                                int juzNumber, int rukuNumber) {
            this.surahNumber = surahNumber;
            this.ayahNumber = ayahNumber;
            this.textArabic = textArabic;
            this.translationBengali = translationBengali;
            this.translationEnglish = translationEnglish;
            this.transliterationBengali = transliterationBengali;
            this.audioUrl = audioUrl;
            this.juzNumber = juzNumber;
            this.rukuNumber = rukuNumber;
        }
    }

    private QuranApiDataNormalizer() {}

    private static void logWarning(String message) {
        try {
            Log.w(TAG, message);
        } catch (Throwable t) {
            // Running in JVM unit test environment
        }
    }

    /**
     * Normalizes and validates a list of raw ayah payloads for a specific Surah.
     * 
     * @param surahNumber Surah number (1 to 114)
     * @param rawList List of raw ayah payloads from API responses
     * @return 100% normalized, validated, and sequentially ordered list of QuranAyahEntity
     */
    public static List<QuranAyahEntity> normalizeAndValidate(int surahNumber, List<RawAyahPayload> rawList) {
        if (rawList == null || rawList.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. Key-based deduplication and mapping by ayahNumber
        Map<Integer, RawAyahPayload> ayahMap = new HashMap<>();
        Set<Integer> duplicateCheck = new HashSet<>();

        for (RawAyahPayload item : rawList) {
            if (item == null) continue;
            if (item.surahNumber != surahNumber) {
                logWarning("Mismatched surah number in payload: expected " + surahNumber + ", found " + item.surahNumber);
                continue;
            }
            if (item.ayahNumber <= 0) {
                logWarning("Invalid ayah number in payload: " + item.ayahNumber);
                continue;
            }

            if (duplicateCheck.contains(item.ayahNumber)) {
                logWarning("Duplicate ayah detected and ignored for Surah " + surahNumber + ", Ayah " + item.ayahNumber);
                continue;
            }

            duplicateCheck.add(item.ayahNumber);
            ayahMap.put(item.ayahNumber, item);
        }

        // 2. Sort available ayah numbers sequentially
        List<Integer> sortedAyahNumbers = new ArrayList<>(ayahMap.keySet());
        Collections.sort(sortedAyahNumbers);

        List<QuranAyahEntity> validatedEntities = new ArrayList<>(sortedAyahNumbers.size());

        // 3. Validate continuity and sanitize Arabic text
        int expectedAyah = 1;
        for (int ayahNum : sortedAyahNumbers) {
            if (ayahNum != expectedAyah) {
                logWarning("Missing ayah detected: expected " + expectedAyah + ", found " + ayahNum + " for Surah " + surahNumber);
            }
            expectedAyah = ayahNum + 1;

            RawAyahPayload payload = ayahMap.get(ayahNum);
            if (payload == null) continue;

            // Sanitize prepended Bismillah from Ayah 1 (for Surahs 2 to 114)
            String cleanArabic = QuranRepository.sanitizeArabicVerse(surahNumber, ayahNum, payload.textArabic);

            QuranAyahEntity entity = new QuranAyahEntity(
                    surahNumber,
                    ayahNum,
                    cleanArabic != null ? cleanArabic.trim() : "",
                    payload.translationBengali != null ? payload.translationBengali.trim() : "",
                    payload.translationEnglish != null ? payload.translationEnglish.trim() : "",
                    payload.transliterationBengali != null ? payload.transliterationBengali.trim() : "",
                    payload.audioUrl != null ? payload.audioUrl.trim() : "",
                    payload.juzNumber > 0 ? payload.juzNumber : 1,
                    payload.rukuNumber > 0 ? payload.rukuNumber : 1
            );

            validatedEntities.add(entity);
        }

        return validatedEntities;
    }

    /**
     * Merges independent API streams (Arabic map, Bengali translation map, English translation map)
     * using strict (SurahNumber, AyahNumber) key validation.
     */
    public static List<QuranAyahEntity> mergeStreams(
            int surahNumber,
            Map<Integer, String> arabicMap,
            Map<Integer, String> bnTranslationMap,
            Map<Integer, String> enTranslationMap,
            Map<Integer, String> transliterationMap,
            Map<Integer, String> audioMap,
            int juzNumber,
            int totalAyahs
    ) {
        List<RawAyahPayload> payloads = new ArrayList<>();
        int count = totalAyahs > 0 ? totalAyahs : (arabicMap != null ? arabicMap.size() : 0);

        for (int ayah = 1; ayah <= count; ayah++) {
            String ar = arabicMap != null ? arabicMap.get(ayah) : "";
            String bn = bnTranslationMap != null ? bnTranslationMap.get(ayah) : "";
            String en = enTranslationMap != null ? enTranslationMap.get(ayah) : "";
            String trans = transliterationMap != null ? transliterationMap.get(ayah) : "";
            String audio = audioMap != null ? audioMap.get(ayah) : "";

            if (ar == null && bn == null && en == null) {
                continue;
            }

            payloads.add(new RawAyahPayload(
                    surahNumber,
                    ayah,
                    ar != null ? ar : "",
                    bn != null ? bn : "",
                    en != null ? en : "",
                    trans != null ? trans : "",
                    audio != null ? audio : "",
                    juzNumber,
                    1
            ));
        }

        return normalizeAndValidate(surahNumber, payloads);
    }
}
