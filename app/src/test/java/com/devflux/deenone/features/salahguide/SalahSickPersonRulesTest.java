package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahSickPersonRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahSickPersonRulesPageDialog.StepItem> items = SalahSickPersonRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 6 items for Sick Person prayer in Bengali", 6, items.size());

        // Card 1: Quran
        assertEquals("কুরআনের আলোকে", items.get(0).title);
        assertTrue("Card 1 should contain Arabic verse", items.get(0).fullContent.contains("فَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ"));
        assertTrue("Card 1 should contain pronunciation", items.get(0).fullContent.contains("ফাত্তাকুল্লাহা মা-স্তাতাতুম্"));
        assertTrue("Card 1 should contain translation", items.get(0).fullContent.contains("তোমরা আল্লাহকে যতটা সম্ভব ভয় করো।"));
        assertTrue("Card 1 should contain reference", items.get(0).fullContent.contains("সূরা আত-তাগাবুন- ৬৪:১৬"));

        // Card 2: Hadith
        assertEquals("হাদিসের আলোকে", items.get(1).title);
        assertTrue("Card 2 should contain Arabic Hadith", items.get(1).fullContent.contains("صَلِّ قَائِمًا فَإِنْ لَمْ تَسْتَطِعْ فَقَاعِدًا، فَإِنْ لَمْ تَسْتَطِعْ فَعَلَىٰ جَنْبٍ"));
        assertTrue("Card 2 should contain pronunciation", items.get(1).fullContent.contains("সাল্লি ক্বায়িমান"));
        assertTrue("Card 2 should contain translation", items.get(1).fullContent.contains("তুমি দাঁড়িয়ে সালাত আদায় কর, যদি সক্ষম না হও তবে বসে, আর যদি সক্ষম না হও তবে শুয়ে।"));
        assertTrue("Card 2 should contain reference", items.get(1).fullContent.contains("সহীহ বুখারি- ১১১৭"));

        // Card 3: Summary Stages
        assertEquals("অসুস্থ ব্যক্তির সালাতের ধাপসমূহ", items.get(2).title);
        assertTrue("Card 3 should contain standing", items.get(2).fullContent.contains("দাঁড়িয়ে: যদি সম্ভব হয়, দাঁড়িয়ে সালাত আদায় করুন।"));
        assertTrue("Card 3 should contain sitting", items.get(2).fullContent.contains("বসে: যদি দাঁড়াতে সক্ষম না হন, তবে বসে সালাত আদায় করুন।"));
        assertTrue("Card 3 should contain lying", items.get(2).fullContent.contains("শুয়ে: যদি বসতেও সক্ষম না হন, তবে শুয়ে সালাত আদায় করুন।"));

        // Card 4: Standing
        assertEquals("১. দাঁড়িয়ে সালাত আদায়", items.get(3).title);
        assertTrue("Card 4 should contain standing instruction", items.get(3).fullContent.contains("যদি সম্ভব হয়, দাঁড়িয়ে সালাত আদায় করুন।"));

        // Card 5: Sitting
        assertEquals("২. বসে সালাত আদায়", items.get(4).title);
        assertTrue("Card 5 should contain sitting instruction", items.get(4).fullContent.contains("যদি দাঁড়াতে সক্ষম না হন, তবে বসে সালাত আদায় করুন।"));

        // Card 6: Lying
        assertEquals("৩. শুয়ে সালাত আদায়", items.get(5).title);
        assertTrue("Card 6 should contain lying instruction", items.get(5).fullContent.contains("যদি বসতেও সক্ষম না হন, তবে শুয়ে সালাত আদায় করুন।"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahSickPersonRulesPageDialog.StepItem> items = SalahSickPersonRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 6 items for Sick Person prayer in English", 6, items.size());

        // Card 1: Quran
        assertEquals("According to the Quran", items.get(0).title);
        assertTrue("Card 1 should contain Arabic verse", items.get(0).fullContent.contains("فَاتَّقُوا اللَّهَ مَا اسْتَطَعْتُمْ"));
        assertTrue("Card 1 should contain English translation", items.get(0).fullContent.contains("So fear Allah as much as you are able"));
        assertTrue("Card 1 should contain reference", items.get(0).fullContent.contains("Surah At-Taghabun, 64:16"));

        // Card 2: Hadith
        assertEquals("According to the Hadith", items.get(1).title);
        assertTrue("Card 2 should contain Arabic Hadith", items.get(1).fullContent.contains("صَلِّ قَائِمًا"));
        assertTrue("Card 2 should contain English translation", items.get(1).fullContent.contains("Pray standing; if you are unable, then sitting"));
        assertTrue("Card 2 should contain reference", items.get(1).fullContent.contains("Sahih al-Bukhari, 1117"));

        // Card 3: Summary Stages
        assertEquals("Stages of Prayer for the Sick Person", items.get(2).title);
        assertTrue("Card 3 should contain standing", items.get(2).fullContent.contains("Standing: If possible, perform the prayer standing."));
        assertTrue("Card 3 should contain sitting", items.get(2).fullContent.contains("Sitting: If unable to stand, perform the prayer sitting."));
        assertTrue("Card 3 should contain lying", items.get(2).fullContent.contains("Lying Down: If unable to sit, perform the prayer lying down."));

        // Card 4: Standing
        assertEquals("1. Praying while Standing", items.get(3).title);
        assertTrue("Card 4 should contain standing instruction", items.get(3).fullContent.contains("If possible, perform the prayer standing."));

        // Card 5: Sitting
        assertEquals("2. Praying while Sitting", items.get(4).title);
        assertTrue("Card 5 should contain sitting instruction", items.get(4).fullContent.contains("If unable to stand, perform the prayer sitting."));

        // Card 6: Lying
        assertEquals("3. Praying while Lying Down", items.get(5).title);
        assertTrue("Card 6 should contain lying instruction", items.get(5).fullContent.contains("If unable to sit, perform the prayer lying down."));
    }
}
