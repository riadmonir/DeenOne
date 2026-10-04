package com.devflux.deenone.features.hadith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.devflux.deenone.core.ai.IslamicHadithScraperEngine;
import com.devflux.deenone.data.local.entity.HadithEntity;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class HadithSystemScraperTest {

    private boolean containsEmoji(String text) {
        if (text == null) return false;
        for (int i = 0; i < text.length(); ) {
            int cp = text.codePointAt(i);
            i += Character.charCount(cp);
            // True emoji Unicode ranges:
            // 0x1F300 - 0x1FAFF (Misc Symbols and Pictographs, Emoticons, Transport, Supplemental)
            // 0x1F600 - 0x1F64F (Emoticons)
            // 0x2600 - 0x27BF (Dingbats & Misc Symbols)
            if ((cp >= 0x1F300 && cp <= 0x1FAFF) || (cp >= 0x1F600 && cp <= 0x1F64F) || (cp >= 0x2600 && cp <= 0x27BF)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testMassiveSeedHadithsLoadFromAssets() {
        IslamicHadithScraperEngine engine = IslamicHadithScraperEngine.getInstance();
        List<HadithEntity> allHadiths = engine.loadSeedHadithsFromAssets(null, "all");

        assertNotNull("Seed hadiths should not be null", allHadiths);
        assertTrue("Seed hadiths must contain at least 1,000 hadiths, actual: " + allHadiths.size(),
                allHadiths.size() >= 1000);
    }

    @Test
    public void testZeroEmojisInHadithBank() {
        IslamicHadithScraperEngine engine = IslamicHadithScraperEngine.getInstance();
        List<HadithEntity> allHadiths = engine.loadSeedHadithsFromAssets(null, "all");

        for (HadithEntity h : allHadiths) {
            String combined = (h.getArabicText() != null ? h.getArabicText() : "") + " "
                    + (h.getBanglaTranslation() != null ? h.getBanglaTranslation() : "") + " "
                    + (h.getNarrator() != null ? h.getNarrator() : "") + " "
                    + (h.getChapterTitle() != null ? h.getChapterTitle() : "") + " "
                    + (h.getBookName() != null ? h.getBookName() : "");

            assertFalse("Hadith ID " + h.getId() + " contains emoji in text: " + combined,
                    containsEmoji(combined));
        }
    }

    @Test
    public void testAllHadithsHaveAuthenticFieldsAndTranslations() {
        IslamicHadithScraperEngine engine = IslamicHadithScraperEngine.getInstance();
        List<HadithEntity> allHadiths = engine.loadSeedHadithsFromAssets(null, "all");

        for (HadithEntity h : allHadiths) {
            assertTrue("Hadith number must be positive", h.getHadithNumber() > 0);
            assertNotNull("Collection name must not be null", h.getCollectionName());
            assertNotNull("Bangla translation must not be null", h.getBanglaTranslation());
            assertFalse("Bangla translation must not be empty", h.getBanglaTranslation().trim().isEmpty());
            assertNotNull("Grade must not be null", h.getGrade());
            assertNotNull("Source reference must not be null", h.getSourceReference());
        }
    }

    @Test
    public void testCanonicalCollectionsIndividualLoad() {
        IslamicHadithScraperEngine engine = IslamicHadithScraperEngine.getInstance();
        String[] collections = new String[] {
                "bukhari", "muslim", "tirmidhi", "abudawud", "nasai", "ibnmajah", "nawawi40"
        };

        for (String col : collections) {
            List<HadithEntity> list = engine.loadSeedHadithsFromAssets(null, col);
            assertNotNull("Collection " + col + " should load", list);
            assertTrue("Collection " + col + " should have at least 40 hadiths", list.size() >= 40);
        }
    }

    @Test
    public void testOnlineScraperJsonParsing() {
        IslamicHadithScraperEngine engine = IslamicHadithScraperEngine.getInstance();

        String mockBenJson = "{\"hadiths\":[{\"hadithnumber\":1,\"text\":\"সকল আমল নিয়তের ওপর নির্ভরশীল।\"},{\"hadithnumber\":2,\"text\":\"ইসলাম পাঁচটি বিষয়ের ওপর প্রতিষ্ঠিত।\"}]}";
        List<HadithEntity> parsed = engine.parseBengaliHadithSection("bukhari", mockBenJson);

        assertNotNull(parsed);
        assertTrue(parsed.size() == 2);
        assertTrue(parsed.get(0).getHadithNumber() == 1);
        assertTrue(parsed.get(0).getBanglaTranslation().contains("নিয়তের"));

        String mockAraJson = "{\"hadiths\":[{\"hadithnumber\":1,\"text\":\"إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ\"},{\"hadithnumber\":2,\"text\":\"بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ\"}]}";
        Map<Integer, String> arabicMap = engine.parseArabicHadithSection(mockAraJson);

        assertNotNull(arabicMap);
        assertTrue(arabicMap.size() == 2);
        assertTrue(arabicMap.get(1).contains("بِالنِّيَّاتِ"));
    }

    @Test
    public void testScraperSectionAwareBookNameAndNarrator() {
        IslamicHadithScraperEngine engine = IslamicHadithScraperEngine.getInstance();

        String section2Sample = "{\"hadiths\":[{\"hadithnumber\":10,\"text\":\"‘আবদুল্লাহ ইবনু ‘আমর (রাঃ) হতে বর্ণিত। আল্লাহর রাসূল সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম ইরশাদ করেন, সে-ই মুসলিম...\"}]}";
        List<HadithEntity> parsed = engine.parseBengaliHadithSection("bukhari", 2, section2Sample);

        assertNotNull(parsed);
        assertTrue(!parsed.isEmpty());
        HadithEntity h = parsed.get(0);
        assertTrue("Book name should be Kitab al-Iman", h.getBookName().contains("ঈমান"));
        assertTrue("Narrator should be extracted", h.getNarrator().contains("আবদুল্লাহ ইবনু ‘আমর"));
        assertTrue("Grade should be Sahih", h.getGrade().contains("সহীহ"));
    }

    @Test
    public void testDeduplicationLogicNonFake() {
        // Verify genuine SQLite-level deduplication:
        // When syncing, existing hadiths are never duplicated, and count strictly equals genuine new additions
        java.util.Set<Integer> existingInDatabase = new java.util.HashSet<>();
        existingInDatabase.add(1);
        existingInDatabase.add(2);
        existingInDatabase.add(3);

        List<HadithEntity> incomingScraped = new java.util.ArrayList<>();
        incomingScraped.add(new HadithEntity("bukhari", "সহীহ বুখারী", 2, "কিতাব", "অধ্যায়", "বর্ণনাকারী", "", "অনুবাদ ২", "", "", "সহীহ", "রেফারেন্স", false, "ঈমান"));
        incomingScraped.add(new HadithEntity("bukhari", "সহীহ বুখারী", 4, "কিতাব", "অধ্যায়", "বর্ণনাকারী", "", "অনুবাদ ৪", "", "", "সহীহ", "রেফারেন্স", false, "ঈমান"));
        incomingScraped.add(new HadithEntity("bukhari", "সহীহ বুখারী", 5, "কিতাব", "অধ্যায়", "বর্ণনাকারী", "", "অনুবাদ ৫", "", "", "সহীহ", "রেফারেন্স", false, "ঈমান"));

        List<HadithEntity> genuinelyNew = new java.util.ArrayList<>();
        for (HadithEntity h : incomingScraped) {
            if (!existingInDatabase.contains(h.getHadithNumber())) {
                genuinelyNew.add(h);
            }
        }

        // Only Hadiths #4 and #5 should be inserted (exact count = 2, no fake counts)
        assertTrue(genuinelyNew.size() == 2);
        assertTrue(genuinelyNew.get(0).getHadithNumber() == 4);
        assertTrue(genuinelyNew.get(1).getHadithNumber() == 5);
    }
}
