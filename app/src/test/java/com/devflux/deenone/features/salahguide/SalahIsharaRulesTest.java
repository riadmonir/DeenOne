package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahIsharaRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahIsharaRulesPageDialog.StepItem> items = SalahIsharaRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 6 items for Gesture prayer in Bengali", 6, items.size());

        // Card 1: Rule
        assertEquals("ইশারায় সালাত আদায়ের বিধান", items.get(0).title);
        assertTrue("Card 1 should contain rule", items.get(0).fullContent.contains("যদি কেউ শুয়ে সালাত আদায় করতে না পারে তবে ইশারায় সালাত আদায় করতে হবে।"));

        // Card 2: Stages summary
        assertEquals("ইশারায় সালাতের ধাপসমূহ", items.get(1).title);
        assertTrue("Card 2 should contain stages summary", items.get(1).fullContent.contains("নিয়ত (ইচ্ছা): সালাত শুরু করার আগে নিয়ত করতে হবে।"));
        assertTrue("Card 2 should contain takbir", items.get(1).fullContent.contains("তাকবির (আল্লাহু আকবার): ইশারায় তাকবির দিয়ে সালাত শুরু করতে হবে।"));
        assertTrue("Card 2 should contain qiyam", items.get(1).fullContent.contains("কিয়াম: ইশারায় সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে।"));
        assertTrue("Card 2 should contain ruku and sujood", items.get(1).fullContent.contains("রুকু ও সিজদা: ইশারায় রুকু এবং সিজদা করতে হবে।"));

        // Card 3: Niyyah
        assertEquals("১. নিয়ত (ইচ্ছা)", items.get(2).title);
        assertTrue("Card 3 should contain niyyah text", items.get(2).fullContent.contains("সালাত শুরু করার আগে নিয়ত করতে হবে।"));

        // Card 4: Takbir
        assertEquals("২. তাকবির (আল্লাহু আকবার)", items.get(3).title);
        assertTrue("Card 4 should contain takbir text", items.get(3).fullContent.contains("ইশারায় তাকবির দিয়ে সালাত শুরু করতে হবে।"));

        // Card 5: Qiyam
        assertEquals("৩. কিয়াম", items.get(4).title);
        assertTrue("Card 5 should contain qiyam text", items.get(4).fullContent.contains("ইশারায় সূরা ফাতিহা এবং অন্য একটি সূরা পড়তে হবে।"));

        // Card 6: Ruku and Sujood
        assertEquals("৪. রুকু ও সিজদা", items.get(5).title);
        assertTrue("Card 6 should contain ruku & sujood text", items.get(5).fullContent.contains("রুকুর সময় কিছুটা কম ঝুঁকতে হবে এবং সিজদার সময় একটু বেশি ঝুঁকতে হবে।"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahIsharaRulesPageDialog.StepItem> items = SalahIsharaRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 6 items for Gesture prayer in English", 6, items.size());

        // Card 1: Rule
        assertEquals("Rule of Gesture Prayer", items.get(0).title);
        assertTrue("Card 1 should contain English rule", items.get(0).fullContent.contains("If someone cannot perform prayer while lying down"));

        // Card 2: Stages summary
        assertEquals("Stages of Gesture Prayer", items.get(1).title);
        assertTrue("Card 2 should contain English summary", items.get(1).fullContent.contains("Niyyah (Intention): Intention must be made before starting the prayer"));

        // Card 3: Niyyah
        assertEquals("1. Niyyah (Intention)", items.get(2).title);
        assertTrue("Card 3 should contain English niyyah text", items.get(2).fullContent.contains("Intention must be made before starting the prayer"));

        // Card 4: Takbir
        assertEquals("2. Takbir (Allahu Akbar)", items.get(3).title);
        assertTrue("Card 4 should contain English takbir text", items.get(3).fullContent.contains("The prayer must be initiated with Takbir using gestures"));

        // Card 5: Qiyam
        assertEquals("3. Qiyam", items.get(4).title);
        assertTrue("Card 5 should contain English qiyam text", items.get(4).fullContent.contains("Recite Surah Al-Fatihah and another Surah with gestures"));

        // Card 6: Ruku and Sujood
        assertEquals("4. Ruku and Sujood", items.get(5).title);
        assertTrue("Card 6 should contain English ruku & sujood text", items.get(5).fullContent.contains("Bow slightly less for Ruku and bow slightly more for Sujood"));
    }
}
