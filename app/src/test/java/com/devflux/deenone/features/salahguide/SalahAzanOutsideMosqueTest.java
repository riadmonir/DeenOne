package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanOutsideMosqueTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanOutsideMosquePageDialog.StepItem> items = SalahAzanOutsideMosquePageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan Outside Mosque in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("মসজিদ ছাড়া অন্য স্থানে আযান", items.get(0).title);
        assertTrue("Card 1 should contain fear, hostility & wilderness", items.get(0).fullContent.contains("ভয়, শত্রুতা প্রভৃতির কারণে মসজিদে যেতে বাধা থাকলে"));
        assertTrue("Card 1 should contain travel hadith & Bukhari Mishkat 682", items.get(0).fullContent.contains("[বুখারী, মিশকাত ৬৮২নং]"));
        assertTrue("Card 1 should contain Muslim 681", items.get(0).fullContent.contains("[মুসলিম, সহীহ ৬৮১নং, প্রমুখ]"));
        assertTrue("Card 1 should contain shepherd hadith & Targhib 239", items.get(0).fullContent.contains("[আবূদাঊদ, সুনান, নাসাঈ, সুনান, সহিহ তারগিব ২৩৯ নং]"));
        assertTrue("Card 1 should contain angel rows hadith & Targhib 241", items.get(0).fullContent.contains("[আব্দুর রাযযাক, মুসান্নাফ, সহিহ তারগিব ২৪১নং]"));
        assertTrue("Card 1 should contain Abdullah ibn Abdur Rahman & Mishkat 656", items.get(0).fullContent.contains("[বুখারী প্রমুখ, মিশকাত ৬৫৬নং]"));

        // Card 2: Travel rulings
        assertEquals("সফর ও নির্জন প্রান্তরে আযানের বিধান ও আমল", items.get(1).title);
        assertTrue("Card 2 should contain travel hadith", items.get(1).fullContent.contains("যখন সফরে থাকবে"));

        // Card 3: Shepherd hadith
        assertEquals("পর্বত চূড়ায় রাখালের আযান ও আল্লাহর বিস্ময়", items.get(2).title);
        assertTrue("Card 3 should contain shepherd hadith", items.get(2).fullContent.contains("পর্বত চূড়ায় সেই ছাগলের রাখালকে"));

        // Card 4: Angels rows
        assertEquals("মরুপ্রান্তরে অগণিত ফিরিশতার জামাত ও উচ্চশব্দে আযান", items.get(3).title);
        assertTrue("Card 4 should contain angels hadith", items.get(3).fullContent.contains("যাদের দুই প্রান্ত নজরে আসে না"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanOutsideMosquePageDialog.StepItem> items = SalahAzanOutsideMosquePageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan Outside Mosque in English", 4, items.size());

        assertEquals("Adhan in Places Other Than Mosques", items.get(0).title);
        assertTrue("Card 1 should contain wilderness in English", items.get(0).fullContent.contains("open wilderness"));
        assertTrue("Card 1 should contain shepherd in English", items.get(0).fullContent.contains("shepherd"));
    }
}
