package com.devflux.deenone.features.quran;

import com.devflux.deenone.core.quran.QuranCdnAudioHelper;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QuranMediaNotificationAndPlayerComprehensiveTest {

    @Test
    public void testAyahAudioCdnUrlGeneration() {
        QuranCdnAudioHelper.Reciter reciter = QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
        String urlFatiha1 = QuranCdnAudioHelper.getAyahAudioUrl(reciter, 1, 1);
        Assert.assertEquals("https://everyayah.com/data/Alafasy_128kbps/001001.mp3", urlFatiha1);

        String urlBaqara3 = QuranCdnAudioHelper.getAyahAudioUrl(reciter, 2, 3);
        Assert.assertEquals("https://everyayah.com/data/Alafasy_128kbps/002003.mp3", urlBaqara3);
    }

    @Test
    public void testFallbackCdnUrlGeneration() {
        QuranCdnAudioHelper.Reciter reciter = QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
        String fallback = QuranCdnAudioHelper.getAyahFallbackUrl(reciter, 1, 4);
        Assert.assertEquals("https://verses.quran.com/Alafasy/mp3/001004.mp3", fallback);
    }

    @Test
    public void testNotificationTitleFormatting() {
        String surahNameEn = "Al-Baqara";
        int ayahNumber = 3;
        String formattedTitle = surahNameEn + " — Ayah " + ayahNumber;
        Assert.assertEquals("Al-Baqara — Ayah 3", formattedTitle);
    }

    @Test
    public void testSurahDataSeedingIntegrity() {
        List<QuranSurahEntity> surahs = QuranSurahDataSeeder.get114Surahs();
        Assert.assertNotNull(surahs);
        Assert.assertEquals(114, surahs.size());

        QuranSurahEntity fatiha = surahs.get(0);
        Assert.assertEquals(1, fatiha.getNumber());
        Assert.assertEquals(7, fatiha.getNumberOfAyahs());
        Assert.assertEquals("আল-ফাতিহা", fatiha.getNameBengali());
    }

    @Test
    public void testEssentialAyahsIntegrity() {
        java.util.List<com.devflux.deenone.data.local.entity.QuranAyahEntity> ayahs = QuranSurahDataSeeder.getEssentialAyahs();
        Assert.assertNotNull(ayahs);
        Assert.assertTrue(ayahs.size() >= 7); // At least Surah Fatiha 7 ayahs seeded

        // Verify Fatiha ayahs are present in order
        for (int i = 0; i < 7; i++) {
            com.devflux.deenone.data.local.entity.QuranAyahEntity a = ayahs.get(i);
            Assert.assertEquals(1, a.getSurahNumber());
            Assert.assertEquals(i + 1, a.getAyahNumber());
            Assert.assertNotNull(a.getTextArabic());
            Assert.assertNotNull(a.getTranslationBengali());
        }
    }

    @Test
    public void testAyah1BismillahPrefixSanitization() {
        // Surah 1 Ayah 1: Bismillah is the verse itself
        String fatiha1 = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";
        String cleanFatiha1 = com.devflux.deenone.data.repository.QuranRepository.sanitizeArabicVerse(1, 1, fatiha1);
        Assert.assertEquals(fatiha1, cleanFatiha1);

        // Surah 2 Ayah 1: Tanzil prepends Bismillah to Alif-Lam-Meem
        String baqarahRawTanzil = "\uFEFFبِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ الٓمٓ";
        String cleanBaqarah1 = com.devflux.deenone.data.repository.QuranRepository.sanitizeArabicVerse(2, 1, baqarahRawTanzil);
        Assert.assertEquals("الٓمٓ", cleanBaqarah1);

        // Surah 114 Ayah 1: Tanzil prepends Bismillah to Qul Audhu bi-Rabbin-Naas
        String nasRawTanzil = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ";
        String cleanNas1 = com.devflux.deenone.data.repository.QuranRepository.sanitizeArabicVerse(114, 1, nasRawTanzil);
        Assert.assertEquals("قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ", cleanNas1);
    }

    @Test
    public void testTransliterationMuqattaatLetters() {
        // Alif-Lam-Meem transliteration in Bengali and English
        String bnPronunciation = com.devflux.deenone.utils.QuranTransliterationUtil.getPronunciation("الٓمٓ", "", true);
        Assert.assertEquals("আলিফ-লাম-মীম", bnPronunciation);

        String enPronunciation = com.devflux.deenone.utils.QuranTransliterationUtil.getPronunciation("الٓمٓ", "", false);
        Assert.assertEquals("Alif-Laaam-Meeem", enPronunciation);

        // Ya-Sin
        String yasinBn = com.devflux.deenone.utils.QuranTransliterationUtil.getPronunciation("يسٓ", "", true);
        Assert.assertEquals("ইয়া-সীন", yasinBn);
    }

    @Test
    public void testQuranBismillahHelper() {
        // Surah 1 & 9 do not show separate Bismillah header card
        Assert.assertFalse(com.devflux.deenone.core.quran.QuranBismillahHelper.shouldShowBismillahHeader(1));
        Assert.assertFalse(com.devflux.deenone.core.quran.QuranBismillahHelper.shouldShowBismillahHeader(9));

        // Surah 2 and all other Surahs show Bismillah header
        Assert.assertTrue(com.devflux.deenone.core.quran.QuranBismillahHelper.shouldShowBismillahHeader(2));
        Assert.assertTrue(com.devflux.deenone.core.quran.QuranBismillahHelper.shouldShowBismillahHeader(114));

        // Bismillah Entity
        com.devflux.deenone.data.local.entity.QuranAyahEntity bismillahEntity =
                com.devflux.deenone.core.quran.QuranBismillahHelper.createBismillahEntity(2);
        Assert.assertEquals(2, bismillahEntity.getSurahNumber());
        Assert.assertEquals(0, bismillahEntity.getAyahNumber());
        Assert.assertEquals(com.devflux.deenone.core.quran.QuranBismillahHelper.BISMILLAH_ARABIC, bismillahEntity.getTextArabic());
        Assert.assertEquals(com.devflux.deenone.core.quran.QuranBismillahHelper.BISMILLAH_TRANSLATION_BN, bismillahEntity.getTranslationBengali());

        // Bismillah 4 Word-by-Word items
        List<com.devflux.deenone.core.quran.QuranWordItem> bismillahWords =
                com.devflux.deenone.core.quran.QuranBismillahHelper.getBismillahWords(2);
        Assert.assertEquals(4, bismillahWords.size());
        Assert.assertEquals("بِسْمِ", bismillahWords.get(0).getTextArabic());
        Assert.assertEquals("নামে", bismillahWords.get(0).getMeaning(true));
        Assert.assertEquals("ٱللَّهِ", bismillahWords.get(1).getTextArabic());
        Assert.assertEquals("আল্লাহর", bismillahWords.get(1).getMeaning(true));
        Assert.assertEquals("ٱلرَّحْمَٰنِ", bismillahWords.get(2).getTextArabic());
        Assert.assertEquals("পরম করুণাময়", bismillahWords.get(2).getMeaning(true));
        Assert.assertEquals("ٱلرَّحِيمِ", bismillahWords.get(3).getTextArabic());
        Assert.assertEquals("অতি দয়ালু", bismillahWords.get(3).getMeaning(true));
    }

    @Test
    public void testQuranApiDataNormalizerStrictMapping() {
        // Prepare out-of-order, duplicate, and raw payloads
        java.util.List<com.devflux.deenone.data.repository.QuranApiDataNormalizer.RawAyahPayload> raw = new java.util.ArrayList<>();
        
        // Ayah 2
        raw.add(new com.devflux.deenone.data.repository.QuranApiDataNormalizer.RawAyahPayload(
                2, 2, "ذَٰلِكَ ٱلْكِتَٰبُ لَا رَيْبَ ۛ فِيهِ", "এই সেই কিতাব যাতে কোন সন্দেহ নেই",
                "This is the Book about which there is no doubt", "যালিকা কিতাবু...", "", 1, 1
        ));

        // Ayah 1 (with prepended Tanzil Bismillah)
        raw.add(new com.devflux.deenone.data.repository.QuranApiDataNormalizer.RawAyahPayload(
                2, 1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ الم", "আলিফ লাম মীম",
                "Alif Lam Meem", "আলিফ লাম মীম", "", 1, 1
        ));

        // Duplicate Ayah 1 (should be discarded by duplicate protection)
        raw.add(new com.devflux.deenone.data.repository.QuranApiDataNormalizer.RawAyahPayload(
                2, 1, "الم DUPLICATE", "আলিফ লাম মীম", "Alif Lam Meem", "", "", 1, 1
        ));

        // Ayah 3
        raw.add(new com.devflux.deenone.data.repository.QuranApiDataNormalizer.RawAyahPayload(
                2, 3, "ٱلَّذِينَ يُؤْمِنُونَ بِٱلْغَيْبِ", "যারা অদৃশ্যের উপর ঈমান আনে",
                "Who believe in the unseen", "আল্লাযীনা ইউ'মিনুনা...", "", 1, 1
        ));

        java.util.List<com.devflux.deenone.data.local.entity.QuranAyahEntity> validated =
                com.devflux.deenone.data.repository.QuranApiDataNormalizer.normalizeAndValidate(2, raw);

        Assert.assertEquals(3, validated.size());

        // Validate Ayah 1
        com.devflux.deenone.data.local.entity.QuranAyahEntity ayah1 = validated.get(0);
        Assert.assertEquals(1, ayah1.getAyahNumber());
        Assert.assertEquals("الم", ayah1.getTextArabic());
        Assert.assertEquals("আলিফ লাম মীম", ayah1.getTranslationBengali());

        // Validate Ayah 2
        com.devflux.deenone.data.local.entity.QuranAyahEntity ayah2 = validated.get(1);
        Assert.assertEquals(2, ayah2.getAyahNumber());
        Assert.assertEquals("ذَٰلِكَ ٱلْكِتَٰبُ لَا رَيْبَ ۛ فِيهِ", ayah2.getTextArabic());
        Assert.assertEquals("এই সেই কিতাব যাতে কোন সন্দেহ নেই", ayah2.getTranslationBengali());

        // Validate Ayah 3
        com.devflux.deenone.data.local.entity.QuranAyahEntity ayah3 = validated.get(2);
        Assert.assertEquals(3, ayah3.getAyahNumber());
        Assert.assertEquals("ٱلَّذِينَ يُؤْمِنُونَ بِٱلْغَيْبِ", ayah3.getTextArabic());
        Assert.assertEquals("যারা অদৃশ্যের উপর ঈমান আনে", ayah3.getTranslationBengali());
    }
}
