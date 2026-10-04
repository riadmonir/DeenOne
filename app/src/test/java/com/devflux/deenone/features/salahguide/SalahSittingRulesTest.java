package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahSittingRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahSittingRulesPageDialog.StepItem> items = SalahSittingRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 7 items for Sitting prayer in Bengali", 7, items.size());

        // Card 1: Hadith
        assertEquals("হাদিসের আলোকে", items.get(0).title);
        assertTrue("Card 1 should contain hadith explanation", items.get(0).fullContent.contains("উক্ত হাদিস অনুযায়ী, অসুস্থ ব্যক্তির বসে সালাত আদায় করা সুন্নত।"));

        // Card 2: Niyyah
        assertEquals("১. নিয়ত (ইচ্ছা)", items.get(1).title);
        assertTrue("Card 2 should contain niyyah text", items.get(1).fullContent.contains("সালাত শুরু করার আগে নিয়ত করতে হবে।"));

        // Card 3: Takbir
        assertEquals("২. তাকবির (আল্লাহু আকবার)", items.get(2).title);
        assertTrue("Card 3 should contain takbir text", items.get(2).fullContent.contains("বসা অবস্থায় তাকবির দিয়ে সালাত শুরু করতে হবে।"));

        // Card 4: Qiyam
        assertEquals("৩. কিয়াম", items.get(3).title);
        assertTrue("Card 4 should contain qiyam text", items.get(3).fullContent.contains("যদি সম্ভব হয়, বসে সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে।"));

        // Card 5: Ruku
        assertEquals("৪. রুকু", items.get(4).title);
        assertTrue("Card 5 should contain ruku text", items.get(4).fullContent.contains("বসা অবস্থায় সামনের দিকে একটু ঝুঁকে রুকু করতে হবে।"));

        // Card 6: Sujood
        assertEquals("৫. সিজদা", items.get(5).title);
        assertTrue("Card 6 should contain sujood text", items.get(5).fullContent.contains("বসা অবস্থায় মাটিতে সিজদা করতে হবে।"));

        // Card 7: Lying in bed
        assertEquals("বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি", items.get(6).title);
        assertTrue("Card 7 should contain lying in bed hadith rule", items.get(6).fullContent.contains("উক্ত হাদিস অনুযায়ী, যদি বসে সালাত আদায় করা সম্ভব না হয়, তবে শুয়ে ইশারায় সালাত আদায় করতে হবে।"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahSittingRulesPageDialog.StepItem> items = SalahSittingRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 7 items for Sitting prayer in English", 7, items.size());

        // Card 1: Hadith
        assertEquals("According to the Hadith", items.get(0).title);
        assertTrue("Card 1 should contain English hadith explanation", items.get(0).fullContent.contains("it is Sunnah for a sick person to perform prayer while sitting"));

        // Card 2: Niyyah
        assertEquals("1. Niyyah (Intention)", items.get(1).title);
        assertTrue("Card 2 should contain English niyyah text", items.get(1).fullContent.contains("Intention must be made before starting the prayer"));

        // Card 3: Takbir
        assertEquals("2. Takbir (Allahu Akbar)", items.get(2).title);
        assertTrue("Card 3 should contain English takbir text", items.get(2).fullContent.contains("initiated with Takbir while in a sitting posture"));

        // Card 4: Qiyam
        assertEquals("3. Qiyam", items.get(3).title);
        assertTrue("Card 4 should contain English qiyam text", items.get(3).fullContent.contains("recite Surah Al-Fatihah and another Surah while sitting"));

        // Card 5: Ruku
        assertEquals("4. Ruku (Bowing)", items.get(4).title);
        assertTrue("Card 5 should contain English ruku text", items.get(4).fullContent.contains("Perform Ruku by leaning slightly forward"));

        // Card 6: Sujood
        assertEquals("5. Sujood (Prostration)", items.get(5).title);
        assertTrue("Card 6 should contain English sujood text", items.get(5).fullContent.contains("Perform Sujood directly on the ground"));

        // Card 7: Lying in bed
        assertEquals("Method of Prayer while Lying in Bed", items.get(6).title);
        assertTrue("Card 7 should contain English lying in bed rule", items.get(6).fullContent.contains("pray lying down using gestures"));
    }
}
