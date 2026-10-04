package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahLyingBedRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahLyingBedRulesPageDialog.StepItem> items = SalahLyingBedRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 3 items for Lying in Bed prayer in Bengali", 3, items.size());

        // Card 1: Verbatim Main Text
        assertEquals("শুয়ে সালাত আদায়ের মূল বিধান ও হাদিস", items.get(0).title);
        assertTrue("Card 1 should contain verbatim intro", items.get(0).fullContent.contains("ইসলামে শুয়ে সারাত আদায়ের প্রসঙ্গ জায়েজ করা হয়েছে।"));
        assertTrue("Card 1 should contain intentional rule", items.get(0).fullContent.contains("ইচ্ছাকৃত ভাবে শুয়ে সালাত পড়লে তার হক্ব আদায় হবে না।"));
        assertTrue("Card 1 should contain unable rule", items.get(0).fullContent.contains("মুমিন ব্যক্তি অপারগ হলে আল্লাহর সুন্দর বিধান মতে সে শুয়েও সালাত পড়তে পারে।"));
        assertTrue("Card 1 should contain Arabic Hadith", items.get(0).fullContent.contains("صَلِّ قَائِمًا، فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَى جَنْبٍ"));
        assertTrue("Card 1 should contain translation", items.get(0).fullContent.contains("তুমি দাঁড়িয়ে সালাত আদায় করবে, তাতে সক্ষম না হলে বসে"));
        assertTrue("Card 1 should contain references", items.get(0).fullContent.contains("বুখারী হা/১১১৭; মিশকাত হা/১২৪৮"));

        // Card 2: Conditions
        assertEquals("শুয়ে সালাতের অপরিহার্য শর্তাবলী", items.get(1).title);
        assertTrue("Card 2 should contain condition points", items.get(1).fullContent.contains("ইচ্ছাকৃত ভাবে শুয়ে সালাত পড়লে তার হক্ব আদায় হবে না"));

        // Card 3: Hadith guidance
        assertEquals("রাসূলুল্লাহ (সাঃ)-এর হাদিসের নির্দেশনা", items.get(2).title);
        assertTrue("Card 3 should contain Arabic Hadith", items.get(2).fullContent.contains("صَلِّ قَائِمًا"));
        assertTrue("Card 3 should contain Bukhari reference", items.get(2).fullContent.contains("বুখারী হা/১১১৭"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahLyingBedRulesPageDialog.StepItem> items = SalahLyingBedRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 3 items for Lying in Bed prayer in English", 3, items.size());

        // Card 1: Main Text
        assertEquals("Rules and Hadith of Praying while Lying Down", items.get(0).title);
        assertTrue("Card 1 should contain English intro", items.get(0).fullContent.contains("In Islam, praying while lying down is permissible"));
        assertTrue("Card 1 should contain Arabic Hadith", items.get(0).fullContent.contains("صَلِّ قَائِمًا"));
        assertTrue("Card 1 should contain Bukhari reference", items.get(0).fullContent.contains("Sahih al-Bukhari, Hadith 1117"));

        // Card 2: Conditions
        assertEquals("Essential Conditions for Praying while Lying Down", items.get(1).title);
        assertTrue("Card 2 should contain English conditions", items.get(1).fullContent.contains("Not Praying Lying Down Intentionally"));

        // Card 3: Hadith guidance
        assertEquals("Guidance from the Prophetic Hadith", items.get(2).title);
        assertTrue("Card 3 should contain English translation", items.get(2).fullContent.contains("Pray standing; if you are unable, then sitting"));
    }
}
