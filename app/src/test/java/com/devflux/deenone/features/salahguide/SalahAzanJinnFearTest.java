package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanJinnFearTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanJinnFearPageDialog.StepItem> items = SalahAzanJinnFearPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Jinn Fear Adhan in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("জিন-ভূতের ভয়ে আযান", items.get(0).title);
        assertTrue("Card 1 should contain jinn frighten text", items.get(0).fullContent.contains("শয়তান জিন মানুষকে ভয় দেখায়"));
        assertTrue("Card 1 should contain Suhail narration", items.get(0).fullContent.contains("সুহাইল বলেন, একদা আমার আব্বা আমাকে বনী হারেসায় পাঠান"));
        assertTrue("Card 1 should contain orchard incident", items.get(0).fullContent.contains("এক বাগান হতে কে যেন নাম ধরে"));
        assertTrue("Card 1 should contain father advice", items.get(0).fullContent.contains("যখন (এই ধরনের) কোন শব্দ শুনবে, তখন নামাযের মত আযান দিও"));
        assertTrue("Card 1 should contain Abu Hurairah hadith", items.get(0).fullContent.contains("আবূ হুরাইরা (রাঃ) কে আল্লাহর রসূল (ﷺ) হতে হাদীস বর্ণনা করতে শুনেছি"));
        assertTrue("Card 1 should contain Muslim 389", items.get(0).fullContent.contains("[মুসলিম, সহীহ ৩৮৯নং]"));

        // Card 2: Jinn fleeing
        assertEquals("ভয় পেয়ে আযান দিলে জিন-শয়তান পলায়ন", items.get(1).title);
        assertTrue("Card 2 should contain fleeing text", items.get(1).fullContent.contains("পালিয়ে যায়"));

        // Card 3: Suhail father
        assertEquals("সুহাইলের পিতার উপদেশ ও বনী হারেসার ঘটনা", items.get(2).title);
        assertTrue("Card 3 should contain Banu Harithah", items.get(2).fullContent.contains("বনী হারেসায়"));

        // Card 4: Satan fleeing hadith
        assertEquals("আযান ধ্বনিতে শয়তানের পলায়ন সংক্রান্ত হাদিস", items.get(3).title);
        assertTrue("Card 4 should contain Muslim reference", items.get(3).fullContent.contains("[মুসলিম, সহীহ ৩৮৯নং]"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanJinnFearPageDialog.StepItem> items = SalahAzanJinnFearPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Jinn Fear Adhan in English", 4, items.size());

        assertEquals("Adhan When Frightened by Spirits or Jinn", items.get(0).title);
        assertTrue("Card 1 should contain Suhail in English", items.get(0).fullContent.contains("Suhail"));
        assertTrue("Card 1 should contain Banu Harithah in English", items.get(0).fullContent.contains("Banu Harithah"));
    }
}
