package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanQadaTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanQadaPageDialog.StepItem> items = SalahAzanQadaPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan for Qada Prayers in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("কাযা নামাযের জন্য আযান", items.get(0).title);
        assertTrue("Card 1 should contain unscheduled adhan ruling", items.get(0).fullContent.contains("অসময়েও আযান-ইকামত দিয়ে নামায পড়া কর্তব্য"));
        assertTrue("Card 1 should contain travel Fajr missed hadith & Muslim 681", items.get(0).fullContent.contains("[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]"));
        assertTrue("Card 1 should contain Bilal adhan details", items.get(0).fullContent.contains("বিলাল (রাঃ) আযান দেন"));
        assertTrue("Card 1 should contain Khandaq four prayers hadith & Musnad Ahmad", items.get(0).fullContent.contains("[আহমাদ, মুসনাদ প্রমুখ, ইর: ১/২৫৭]"));
        assertTrue("Card 1 should contain four prayers sequence", items.get(0).fullContent.contains("যোহ্র, আসর, মাগরিব ও এশার নামায আদায় করেছিলেন"));

        // Card 2: Ruling
        assertEquals("অসময়ে আযান ও ইকামতের বিধান", items.get(1).title);
        assertTrue("Card 2 should contain duty of adhan", items.get(1).fullContent.contains("অসময়েও আযান-ইকামত"));

        // Card 3: Fajr travel hadith
        assertEquals("সফরে ফজরের নামায কাযা ও বিলালের (রাঃ) আযান", items.get(2).title);
        assertTrue("Card 3 should contain travel Fajr details", items.get(2).fullContent.contains("সূর্য ওঠার পর তেজ হয়ে এলে"));

        // Card 4: Khandaq battle hadith
        assertEquals("খন্দকের যুদ্ধে চার ওয়াক্ত নামায কাযা ও আযান", items.get(3).title);
        assertTrue("Card 4 should contain Khandaq details", items.get(3).fullContent.contains("খন্দকের যুদ্ধের সময়"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanQadaPageDialog.StepItem> items = SalahAzanQadaPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan for Qada Prayers in English", 4, items.size());

        assertEquals("Adhan for Missed (Qada) Prayers", items.get(0).title);
        assertTrue("Card 1 should contain unscheduled time in English", items.get(0).fullContent.contains("unscheduled time"));
        assertTrue("Card 1 should contain Khandaq in English", items.get(0).fullContent.contains("Battle of the Trench"));
    }
}
