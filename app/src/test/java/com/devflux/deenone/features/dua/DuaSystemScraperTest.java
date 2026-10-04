package com.devflux.deenone.features.dua;

import com.devflux.deenone.core.ai.IslamicDuaScraperEngine;
import com.devflux.deenone.data.local.entity.DuaEntity;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class DuaSystemScraperTest {

    private static boolean hasEmoji(String text) {
        if (text == null) return false;
        for (int i = 0; i < text.length(); ) {
            int cp = text.codePointAt(i);
            // Check true graphical emoji ranges (Emoticons, Pictographs, Symbols, Transport/Map)
            if ((cp >= 0x1F300 && cp <= 0x1FAFF) || (cp >= 0x1F600 && cp <= 0x1F64F) || (cp >= 0x1F900 && cp <= 0x1F9FF)) {
                return true;
            }
            i += Character.charCount(cp);
        }
        return false;
    }

    @Test
    public void testAllTwelveSeedFilesExistAndContainSubstantialDuas() {
        IslamicDuaScraperEngine engine = IslamicDuaScraperEngine.getInstance();
        List<DuaEntity> allDuas = engine.loadSeedDuasFromAssets(null, "all");

        Assert.assertNotNull("Seed Duas must not be null", allDuas);
        Assert.assertTrue("Seed Duas must contain over 150 Duas, found: " + allDuas.size(),
                allDuas.size() >= 150);

        // Verify categories
        Set<String> categories = new HashSet<>();
        for (DuaEntity d : allDuas) {
            categories.add(d.getCategory());
        }

        Assert.assertTrue("Must contain Rabbana category", categories.contains("Rabbana"));
        Assert.assertTrue("Must contain Daily Life category", categories.contains("Daily Life"));
        Assert.assertTrue("Must contain Salah category", categories.contains("Salah-related Duas"));
        Assert.assertTrue("Must contain Protection category", categories.contains("Protection"));
        Assert.assertTrue("Must contain Forgiveness category", categories.contains("Forgiveness"));
        Assert.assertTrue("Must contain Anxiety category", categories.contains("Anxiety/Worry"));
        Assert.assertTrue("Must contain Parents category", categories.contains("Parents"));
        Assert.assertTrue("Must contain Rizq category", categories.contains("Rizq"));
        Assert.assertTrue("Must contain Health category", categories.contains("Health"));
        Assert.assertTrue("Must contain Travel category", categories.contains("Travel"));
        Assert.assertTrue("Must contain Ramadan category", categories.contains("Ramadan"));
        Assert.assertTrue("Must contain Hajj category", categories.contains("Hajj"));
    }

    @Test
    public void testZeroEmojisInAllSeedDuas() {
        IslamicDuaScraperEngine engine = IslamicDuaScraperEngine.getInstance();
        List<DuaEntity> allDuas = engine.loadSeedDuasFromAssets(null, "all");

        for (DuaEntity d : allDuas) {
            Assert.assertFalse("Title should not have emoji: " + d.getTitle(), hasEmoji(d.getTitle()));
            Assert.assertFalse("Meaning should not have emoji: " + d.getBengaliMeaning(), hasEmoji(d.getBengaliMeaning()));
            if (d.getWhenToRead() != null) {
                Assert.assertFalse("WhenToRead should not have emoji: " + d.getWhenToRead(), hasEmoji(d.getWhenToRead()));
            }
            if (d.getWhyToRead() != null) {
                Assert.assertFalse("WhyToRead should not have emoji: " + d.getWhyToRead(), hasEmoji(d.getWhyToRead()));
            }
        }
    }

    @Test
    public void testAuthenticReferencesPresentInAllDuas() {
        IslamicDuaScraperEngine engine = IslamicDuaScraperEngine.getInstance();
        List<DuaEntity> allDuas = engine.loadSeedDuasFromAssets(null, "all");

        for (DuaEntity d : allDuas) {
            Assert.assertNotNull("Reference cannot be null: " + d.getTitle(), d.getReference());
            Assert.assertFalse("Reference cannot be empty: " + d.getTitle(), d.getReference().trim().isEmpty());

            boolean hasCitation = d.getReference().contains("সূরা") ||
                    d.getReference().contains("বুখারী") ||
                    d.getReference().contains("মুসলিম") ||
                    d.getReference().contains("তিরমিযী") ||
                    d.getReference().contains("আবু দাউদ") ||
                    d.getReference().contains("নাসায়ী") ||
                    d.getReference().contains("ইবনে মাজাহ") ||
                    d.getReference().contains("আহমাদ") ||
                    d.getReference().contains("হাকিম") ||
                    d.getReference().contains("মালিক") ||
                    d.getReference().contains("হিব্বান") ||
                    d.getReference().contains("তাবারানী") ||
                    d.getReference().contains("জামি") ||
                    d.getReference().contains("আদাবুল মুফরাদ") ||
                    d.getReference().contains("তারগীব") ||
                    d.getReference().contains("লাইলাহ") ||
                    d.getReference().contains("দারা কুতনী");

            Assert.assertTrue("Dua '" + d.getTitle() + "' must have authentic Quran or Hadith citation: " + d.getReference(), hasCitation);
        }
    }

    @Test
    public void testOnlineScraperSectionParsing() {
        IslamicDuaScraperEngine engine = IslamicDuaScraperEngine.getInstance();
        IslamicDuaScraperEngine.SupplicationSource source = IslamicDuaScraperEngine.ONLINE_SOURCES[0]; // Bukhari 80

        String sampleBengaliJson = "{\n" +
                "  \"hadiths\": [\n" +
                "    {\n" +
                "      \"hadithnumber\": 6312,\n" +
                "      \"text\": \"হুযাইফাহ (রাঃ) হতে বর্ণিত। নবী সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম যখন ঘুমানোর ইচ্ছা করতেন তখন বলতেন, আল্লাহুম্মা বিসমিকা আমূতু ওয়া আহ্ইয়া।\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        List<DuaEntity> parsed = engine.parseBengaliSupplicationSection(source, sampleBengaliJson);
        Assert.assertEquals(1, parsed.size());
        DuaEntity d = parsed.get(0);

        Assert.assertTrue(d.getTitle().contains("ঘুম") || d.getTitle().contains("দোয়া"));
        Assert.assertTrue(d.getReference().contains("সহীহ বুখারী"));
        Assert.assertEquals("Before Sleeping", d.getCategory());

        String sampleArabicJson = "{\n" +
                "  \"hadiths\": [\n" +
                "    {\n" +
                "      \"hadithnumber\": 6312,\n" +
                "      \"text\": \"بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Map<Integer, String> araMap = engine.parseArabicSectionMap(sampleArabicJson);
        Assert.assertEquals(1, araMap.size());
        Assert.assertEquals("بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا", araMap.get(6312));
    }

    @Test
    public void testDeduplicationDetection() {
        Set<String> existingTitles = new HashSet<>();
        existingTitles.add("ঘুম থেকে জাগ্রত হওয়ার দোয়া");
        existingTitles.add("ঘুমানোর পূর্বে পাঠের দোয়া");

        Set<String> existingArabic = new HashSet<>();
        existingArabic.add("بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا");

        // Dua 1: Duplicate title
        DuaEntity d1 = new DuaEntity();
        d1.setTitle("ঘুম থেকে জাগ্রত হওয়ার দোয়া");
        d1.setArabic("الْحَمْدُ لِلَّهِ");
        boolean isDuplicate1 = existingTitles.contains(d1.getTitle()) || existingArabic.contains(d1.getArabic());
        Assert.assertTrue("d1 must be duplicate by title", isDuplicate1);

        // Dua 2: Duplicate Arabic
        DuaEntity d2 = new DuaEntity();
        d2.setTitle("অন্য শিরোনাম");
        d2.setArabic("بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا");
        boolean isDuplicate2 = existingTitles.contains(d2.getTitle()) || existingArabic.contains(d2.getArabic());
        Assert.assertTrue("d2 must be duplicate by Arabic", isDuplicate2);

        // Dua 3: Unique
        DuaEntity d3 = new DuaEntity();
        d3.setTitle("নতুন প্রামাণ্য দোয়া");
        d3.setArabic("سُبْحَانَ اللَّهِ الْعَظِيمِ");
        boolean isDuplicate3 = existingTitles.contains(d3.getTitle()) || existingArabic.contains(d3.getArabic());
        Assert.assertFalse("d3 must be unique", isDuplicate3);
    }
}
