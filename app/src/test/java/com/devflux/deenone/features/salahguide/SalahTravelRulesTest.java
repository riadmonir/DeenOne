package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahTravelRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahTravelRulesPageDialog.StepItem> items = SalahTravelRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 7 items for Travel prayer in Bengali", 7, items.size());

        // Card 1: Quran
        assertEquals("কুরআনের আলোকে", items.get(0).title);
        assertTrue("Card 1 should contain Arabic verse", items.get(0).fullContent.contains("وَإِذَا ضَرَبْتُمْ فِي الْأَرْضِ فَلَيْسَ عَلَيْكُمْ جُنَاحٌ"));
        assertTrue("Card 1 should contain pronunciation", items.get(0).fullContent.contains("ওয়া ইযা দারাবতুম্ ফিল্ আর্দি"));
        assertTrue("Card 1 should contain translation", items.get(0).fullContent.contains("আর যখন তোমরা যাত্রা করবে, তখন তোমাদের জন্য সালাত সংক্ষেপ করা কোন অপরাধ নয়"));
        assertTrue("Card 1 should contain reference", items.get(0).fullContent.contains("সূরা আন-নিসা - ৪:১০১"));

        // Card 2: Hadith
        assertEquals("হাদিসের আলোকে", items.get(1).title);
        assertTrue("Card 2 should contain Arabic Hadith", items.get(1).fullContent.contains("كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ يَقْصُرُ فِي السَّفَرِ"));
        assertTrue("Card 2 should contain pronunciation", items.get(1).fullContent.contains("কানা আন্ নাবিয়্যু সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম"));
        assertTrue("Card 2 should contain translation", items.get(1).fullContent.contains("নবী সাল্লাল্লাহু 'আলাইহি ওয়া সাল্লাম যাত্রার সময় সালাত সংক্ষেপ করতেন এবং দুই সালাত একত্রে আদায় করতেন।"));
        assertTrue("Card 2 should contain reference", items.get(1).fullContent.contains("সহীহ বুখারি- ১০৯০"));

        // Card 3: Summary Stages
        assertEquals("ভ্রমণ অবস্থায় সালাতের ধাপসমূহ", items.get(2).title);
        assertTrue("Card 3 should contain niyyah summary", items.get(2).fullContent.contains("নিয়ত (ইচ্ছা): নামাজ সংক্ষেপ করার নিয়ত করতে হবে।"));
        assertTrue("Card 3 should contain 4 rakats summary", items.get(2).fullContent.contains("চার রাকাত ফরয নামাজকে দুই রাকাত পড়া: যোহর, আসর এবং এশার চার রাকাত ফরয নামাজকে দুই রাকাত করে আদায় করতে হবে।"));
        assertTrue("Card 3 should contain maghrib & fajr summary", items.get(2).fullContent.contains("মাগরিব ও ফজরের সালাত আগের মতই পড়া: মাগরিব তিন রাকাত এবং ফজর দুই রাকাত আগের মতই আদায় করতে হবে।"));
        assertTrue("Card 3 should contain combining prayers summary", items.get(2).fullContent.contains("দুই সালাত একত্রে আদায় করা: যোহর-আসর এবং মাগরিব-এশা একত্রে আদায় করা যাবে।"));

        // Card 4: Niyyah
        assertEquals("১. নিয়ত (ইচ্ছা)", items.get(3).title);
        assertTrue("Card 4 should contain niyyah text", items.get(3).fullContent.contains("নামাজ সংক্ষেপ করার নিয়ত করতে হবে।"));

        // Card 5: 4 Rak'ahs to 2 Rak'ahs
        assertEquals("২. চার রাকাত ফরয নামাজকে দুই রাকাত পড়া", items.get(4).title);
        assertTrue("Card 5 should contain 4 rakats text", items.get(4).fullContent.contains("যোহর, আসর এবং এশার চার রাকাত ফরয নামাজকে দুই রাকাত করে আদায় করতে হবে।"));

        // Card 6: Maghrib & Fajr
        assertEquals("৩. মাগরিব ও ফজরের সালাত আগের মতই পড়া", items.get(5).title);
        assertTrue("Card 6 should contain maghrib & fajr text", items.get(5).fullContent.contains("মাগরিব তিন রাকাত এবং ফজর দুই রাকাত আগের মতই আদায় করতে হবে।"));

        // Card 7: Combining two prayers
        assertEquals("৪. দুই সালাত একত্রে আদায় করা", items.get(6).title);
        assertTrue("Card 7 should contain combining prayers text", items.get(6).fullContent.contains("যোহর-আসর এবং মাগরিব-এশা একত্রে আদায় করা যাবে।"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahTravelRulesPageDialog.StepItem> items = SalahTravelRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 7 items for Travel prayer in English", 7, items.size());

        // Card 1: Quran
        assertEquals("According to the Quran", items.get(0).title);
        assertTrue("Card 1 should contain Arabic verse", items.get(0).fullContent.contains("وَإِذَا ضَرَبْتُمْ فِي الْأَرْضِ"));
        assertTrue("Card 1 should contain English translation", items.get(0).fullContent.contains("there is no blame upon you for shortening the prayer"));
        assertTrue("Card 1 should contain reference", items.get(0).fullContent.contains("Surah An-Nisa, 4:101"));

        // Card 2: Hadith
        assertEquals("According to the Hadith", items.get(1).title);
        assertTrue("Card 2 should contain Arabic Hadith", items.get(1).fullContent.contains("كَانَ النَّبِيُّ"));
        assertTrue("Card 2 should contain English translation", items.get(1).fullContent.contains("used to shorten the prayers during travel"));
        assertTrue("Card 2 should contain reference", items.get(1).fullContent.contains("Sahih al-Bukhari, 1090"));

        // Card 3: Summary Stages
        assertEquals("Stages of Prayer while Traveling", items.get(2).title);
        assertTrue("Card 3 should contain English summary", items.get(2).fullContent.contains("Shortening 4-Rak'ah Fard Prayers to 2 Rak'ahs"));

        // Card 4: Niyyah
        assertEquals("1. Niyyah (Intention)", items.get(3).title);
        assertTrue("Card 4 should contain English niyyah", items.get(3).fullContent.contains("Intention must be made to shorten the prayer"));

        // Card 5: 4 Rak'ahs to 2 Rak'ahs
        assertEquals("2. Shortening 4-Rak'ah Fard Prayers", items.get(4).title);
        assertTrue("Card 5 should contain English 4 rakats text", items.get(4).fullContent.contains("performed as 2 Rak'ahs"));

        // Card 6: Maghrib & Fajr
        assertEquals("3. Praying Maghrib and Fajr as Usual", items.get(5).title);
        assertTrue("Card 6 should contain English maghrib & fajr text", items.get(5).fullContent.contains("Maghrib is performed as 3 Rak'ahs and Fajr as 2 Rak'ahs"));

        // Card 7: Combining two prayers
        assertEquals("4. Combining Two Prayers", items.get(6).title);
        assertTrue("Card 7 should contain English combining text", items.get(6).fullContent.contains("Dhuhr with Asr, and Maghrib with Isha can be combined"));
    }
}
