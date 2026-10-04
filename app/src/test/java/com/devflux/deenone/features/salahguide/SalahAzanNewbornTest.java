package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanNewbornTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanNewbornPageDialog.StepItem> items = SalahAzanNewbornPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Newborn Adhan in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("সন্তান ভূমিষ্ঠ হলে আযান", items.get(0).title);
        assertTrue("Card 1 should contain Abu Rafi hadith", items.get(0).fullContent.contains("আবূ রাফে (রাঃ) বলেন, আমি আল্লাহর রসূল (ﷺ) কে দেখেছি"));
        assertTrue("Card 1 should contain Hasan bin Ali", items.get(0).fullContent.contains("ফাতেমা (রাঃ) হাসান বিন আলীকে প্রসব করলে"));
        assertTrue("Card 1 should contain references", items.get(0).fullContent.contains("[আবূদাঊদ, সুনান ৫১০৫, তিরমিযী, সুনান ১৫৬৬, মিশকাত ৪১৫৭ নং]"));
        assertTrue("Card 1 should contain boys and girls sunnah", items.get(0).fullContent.contains("ছেলে-মেয়ে সকলের কানে ঐ সময় নামাযের জন্য আযান দেওয়ার মতই আযান দেওয়া সুন্নত"));
        assertTrue("Card 1 should contain fabricated hadith clarification", items.get(0).fullContent.contains("ডান কানে আযান এবং বাম কানে ইকামত দিলে ‘উম্মুস সিবয়্যান (ভূত,পেত) বা এক প্রকার রোগ কোন ক্ষতি করতে না পারারহাদীসটি জাল"));
        assertTrue("Card 1 should contain Albani references", items.get(0).fullContent.contains("[সিলসিলাহ যায়ীফাহ, আলবানী ৩২১নং, জামে ৫৮৮১, ইরওয়াউল গালীল, আলবানী ১১৭৪নং]"));

        // Card 2: Abu Rafi
        assertEquals("হাসান (রাঃ)-এর কানে রাসুলুল্লাহর (ﷺ) আযান", items.get(1).title);
        assertTrue("Card 2 should contain Hasan", items.get(1).fullContent.contains("হাসান বিন আলীকে"));

        // Card 3: Ruling
        assertEquals("নবজাতকের কানে আযানের হুকুম ও তাহক্বীক্ব", items.get(2).title);
        assertTrue("Card 3 should contain Sunnah ruling", items.get(2).fullContent.contains("ছেলে-মেয়ে সকলের কানে"));

        // Card 4: Fabricated hadith
        assertEquals("ডান কানে আযান ও বাম কানে ইকামত সংক্রান্ত জাল হাদিস", items.get(3).title);
        assertTrue("Card 4 should contain fabricated notice", items.get(3).fullContent.contains("হাদীসটি জাল"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanNewbornPageDialog.StepItem> items = SalahAzanNewbornPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Newborn Adhan in English", 4, items.size());

        assertEquals("Adhan Upon the Birth of a Child", items.get(0).title);
        assertTrue("Card 1 should contain Abu Rafi in English", items.get(0).fullContent.contains("Abu Rafi"));
        assertTrue("Card 1 should contain fabricated in English", items.get(0).fullContent.contains("fabricated"));
    }
}
