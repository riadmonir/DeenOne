package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanLeavingMosqueTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanLeavingMosquePageDialog.StepItem> items = SalahAzanLeavingMosquePageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Leaving Mosque After Adhan in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("আযানের পর মসজিদ থেকে বের হওয়া", items.get(0).title);
        assertTrue("Card 1 should contain invalidity text", items.get(0).fullContent.contains("আযান হয়ে গেলে বিনা ওজরে নামায না পড়ে মসজিদ থেকে বের হয়ে যাওয়া বৈধ নয়"));
        assertTrue("Card 1 should contain Ahmad hadith & Mishkat 1074", items.get(0).fullContent.contains("[আহমাদ, মুসনাদ, মিশকাত ১০৭৪ নং]"));
        assertTrue("Card 1 should contain Abu Hurairah statement", items.get(0).fullContent.contains("আবুল কাসেম (ﷺ) এর নাফরমানী করল"));
        assertTrue("Card 1 should contain Muslim Abu Dawud 536 references", items.get(0).fullContent.contains("[মুসলিম, আবূদাঊদ, সুনান ৫৩৬ নং, তিরমিযী, সুনান, ইবনে মাজাহ্, সুনান, দারেমী, সুনান, বায়হাকী]"));
        assertTrue("Card 1 should contain Munafiq warning & Targhib 157", items.get(0).fullContent.contains("সে ব্যক্তি মুনাফিক"));
        assertTrue("Card 1 should contain Ibn Majah reference", items.get(0).fullContent.contains("[ইবনে মাজাহ্, সুনান, সহিহ তারগিব ১৫৭নং]"));

        // Card 2: Ahmad hadith
        assertEquals("আযানের পর বিনা ওজরে বের হওয়ার নিষেধাজ্ঞা", items.get(1).title);
        assertTrue("Card 2 should contain Ahmad reference", items.get(1).fullContent.contains("মিশকাত ১০৭৪ নং"));

        // Card 3: Abu Hurairah
        assertEquals("আবূ হুরাইরা (রাঃ)-এর সতর্কবার্তা ও আবুল কাসেমের (ﷺ) নাফরমানী", items.get(2).title);
        assertTrue("Card 3 should contain Abul Qasim", items.get(2).fullContent.contains("আবুল কাসেম"));

        // Card 4: Munafiq warning
        assertEquals("বিনা প্রয়োজনে বের হওয়া ও মুনাফেকীর হুশিয়ারী", items.get(3).title);
        assertTrue("Card 4 should contain Munafiq", items.get(3).fullContent.contains("মুনাফিক"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanLeavingMosquePageDialog.StepItem> items = SalahAzanLeavingMosquePageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Leaving Mosque After Adhan in English", 4, items.size());

        assertEquals("Leaving the Mosque After Adhan", items.get(0).title);
        assertTrue("Card 1 should contain Abul Qasim in English", items.get(0).fullContent.contains("Abul Qasim"));
        assertTrue("Card 1 should contain hypocrite in English", items.get(0).fullContent.contains("hypocrite"));
    }
}
