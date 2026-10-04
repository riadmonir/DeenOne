package com.devflux.deenone.core.quran;

import com.devflux.deenone.data.local.entity.QuranAyahEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Universal Bismillah Helper for Holy Quran in DeenOne.
 * Provides canonical Arabic text, dual-language transliterations/pronunciations,
 * dual-language translations, and exact 4 Word-by-Word entries.
 * 
 * Rules:
 * - Surah 1 (Al-Fatihah): Ayah 1 is canonically Bismillah in the Quran text.
 * - Surah 9 (At-Tawbah): No Bismillah header.
 * - Surahs 2 to 114 (except 9): Displays dedicated Bismillah card at top (Position 0).
 */
public final class QuranBismillahHelper {

    public static final String BISMILLAH_ARABIC = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";
    public static final String BISMILLAH_PRONUNCIATION_BN = "বিসমিল্লাহির রাহমানির রাহিম";
    public static final String BISMILLAH_PRONUNCIATION_EN = "Bismillaahir-Rahmaanir-Raheem";
    public static final String BISMILLAH_TRANSLATION_BN = "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।";
    public static final String BISMILLAH_TRANSLATION_EN = "In the name of Allah, the Entirely Merciful, the Especially Merciful.";

    private QuranBismillahHelper() {}

    /**
     * Determines whether a separate Bismillah header card should be displayed before Ayah 1.
     * True for all Surahs from 2 to 114, except Surah 9 (At-Tawbah).
     */
    public static boolean shouldShowBismillahHeader(int surahNumber) {
        return surahNumber > 1 && surahNumber != 9;
    }

    /**
     * Creates a standardized QuranAyahEntity representing Bismillah for the given Surah (ayahNumber = 0).
     */
    public static QuranAyahEntity createBismillahEntity(int surahNumber) {
        return new QuranAyahEntity(
                surahNumber,
                0, // Special index for Bismillah Header Card
                BISMILLAH_ARABIC,
                BISMILLAH_TRANSLATION_BN,
                BISMILLAH_TRANSLATION_EN,
                BISMILLAH_PRONUNCIATION_BN,
                "", // Audio URL resolved via CdnAudioHelper if needed
                1,
                1
        );
    }

    /**
     * Returns the 4 canonical Word-by-Word items for Bismillah.
     */
    public static List<QuranWordItem> getBismillahWords(int surahNumber) {
        List<QuranWordItem> words = new ArrayList<>(4);
        words.add(new QuranWordItem(surahNumber, 0, 1, "بِسْمِ", "নামে", "In the name"));
        words.add(new QuranWordItem(surahNumber, 0, 2, "ٱللَّهِ", "আল্লাহর", "of Allah"));
        words.add(new QuranWordItem(surahNumber, 0, 3, "ٱلرَّحْمَٰنِ", "পরম করুণাময়", "the Entirely Merciful"));
        words.add(new QuranWordItem(surahNumber, 0, 4, "ٱلرَّحِيمِ", "অতি দয়ালু", "the Especially Merciful"));
        return words;
    }
}
