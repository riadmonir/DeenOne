package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahLyingBedStepsTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahLyingBedStepsPageDialog.StepItem> items = SalahLyingBedStepsPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 3 items for Lying in Bed Steps in Bengali", 3, items.size());

        // Card 1: Verbatim Full Text
        assertEquals("বিছানায় শুয়ে সালাত আদায়ের পদ্ধতি", items.get(0).title);
        assertTrue("Card 1 should contain first step text", items.get(0).fullContent.contains("প্রথমত: সালাতের জন্য নিয়ত করবেন।"));
        assertTrue("Card 1 should contain qiblah alignment", items.get(0).fullContent.contains("ক্বিবলার দিকে পা দিয়ে চিত হয়ে শুয়ে যেতে।"));
        assertTrue("Card 1 should contain side lying instructions", items.get(0).fullContent.contains("উত্তর-দক্ষিণ হয়ে মাথা উত্তর দিকে রেখে ডান কাতে শুয়ে"));
        assertTrue("Card 1 should contain second step text", items.get(0).fullContent.contains("দ্বিতীয়ত: মাথার নিচে বালিশ দিয়ে মাথা যতটুকু সম্ভব উঁচু করে নিতে হবে"));
        assertTrue("Card 1 should contain ruku sujud gesture rule", items.get(0).fullContent.contains("রুকুর ইশারার চেয়ে সিজদার ইশারায় একটু বেশি ঝুঁকতে হবে।"));

        // Card 2: Step 1
        assertEquals("১ম ধাপ: নিয়ত ও ক্বিবলামুখী শোয়ার নিয়ম", items.get(1).title);
        assertTrue("Card 2 should contain step 1 details", items.get(1).fullContent.contains("ক্বিবলার দিকে মুখ ফিরিয়ে সালাতের কিয়াম অংশের কাজ শুরু করতে হবে।"));

        // Card 3: Step 2
        assertEquals("২য় ধাপ: মাথা উঁচু করা ও ইশারায় রুকু-সিজদা", items.get(2).title);
        assertTrue("Card 3 should contain step 2 details", items.get(2).fullContent.contains("মাথার নিচে বালিশ দিয়ে মাথা যতটুকু সম্ভব উঁচু করে নিতে হবে"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahLyingBedStepsPageDialog.StepItem> items = SalahLyingBedStepsPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 3 items for Lying in Bed Steps in English", 3, items.size());

        // Card 1: Full English Text
        assertEquals("Method of Prayer while Lying in Bed", items.get(0).title);
        assertTrue("Card 1 should contain English intention", items.get(0).fullContent.contains("Form the intention (Niyyah) for Salah."));
        assertTrue("Card 1 should contain English pillow text", items.get(0).fullContent.contains("Place a pillow under the head to elevate it"));

        // Card 2: Step 1 English
        assertEquals("Step 1: Intention & Qiblah Alignment", items.get(1).title);
        assertTrue("Card 2 should contain English step 1", items.get(1).fullContent.contains("face towards the Qiblah to begin the Qiyam portion"));

        // Card 3: Step 2 English
        assertEquals("Step 2: Head Elevation, Ruku & Sujud", items.get(2).title);
        assertTrue("Card 3 should contain English step 2", items.get(2).fullContent.contains("bend the head slightly more for Sujud than for Ruku"));
    }
}
