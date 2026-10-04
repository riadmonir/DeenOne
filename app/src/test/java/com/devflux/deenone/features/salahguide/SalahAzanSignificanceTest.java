package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanSignificanceTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanSignificancePageDialog.StepItem> items = SalahAzanSignificancePageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan Significance in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("আযান ও তার মাহাত্ম", items.get(0).title);
        assertTrue("Card 1 should contain Fard al-Kifayah & Bukhari 628", items.get(0).fullContent.contains("আযান ফরয এবং তা দেওয়া হল ফ র্যে কিফায়াহ্"));
        assertTrue("Card 1 should contain Bukhari 628", items.get(0).fullContent.contains("[বুখারী ৬২৮নং, মুসলিম, নাসাঈ, সুনান, দারেমী, সুনান]"));
        assertTrue("Card 1 should contain Jihad & Bukhari 610", items.get(0).fullContent.contains("[বুখারী ৬১০ নং, মুসলিম, সহীহ]"));
        assertTrue("Card 1 should contain travel fatwa", items.get(0).fullContent.contains("[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫৫]"));
        assertTrue("Card 1 should contain Quran 41/33", items.get(0).fullContent.contains("[কুরআন মাজীদ ৪১/৩৩]"));
        assertTrue("Card 1 should contain lottery hadith & Bukhari 615", items.get(0).fullContent.contains("[বুখারী ৬১৫, মুসলিম, সহীহ ৪৩৭নং]"));
        assertTrue("Card 1 should contain forgiveness to the reach of voice & Targhib 228", items.get(0).fullContent.contains("[আহ্মদ, নাসাঈ, সহীহ তারগীব ২২৮নং]"));
        assertTrue("Card 1 should contain long neck on Judgment Day & Muslim 387", items.get(0).fullContent.contains("[মুসলিম, সহীহ৩৮৭নং]"));
        assertTrue("Card 1 should contain 12 years adhan & Targhib 240", items.get(0).fullContent.contains("[ইবনে মাজাহ্, দারাকুত্বনী,হাকেম, সহীহ তারগীব ২৪০নং]"));
        assertTrue("Card 1 should contain witness of all creation & Bukhari 609", items.get(0).fullContent.contains("[বুখারী ৬০৯ নং]"));

        // Card 2: Rulings & Symbol
        assertEquals("আযানের বিধান ও ইসলামের নিদর্শন", items.get(1).title);
        assertTrue("Card 2 should contain rulings", items.get(1).fullContent.contains("আযান ফরয এবং তা দেওয়া হল ফ র্যে কিফায়াহ্"));

        // Card 3: Virtues
        assertEquals("মুয়াযযিন ও আযান দেওয়ার মহা ফজিলত", items.get(2).title);
        assertTrue("Card 3 should contain Quran 41/33", items.get(2).fullContent.contains("[কুরআন মাজীদ ৪১/৩৩]"));

        // Card 4: Judgment Day
        assertEquals("কিয়ামতের ময়দানে মুয়াযযিনের মর্যাদা ও জান্নাতের সুসংবাদ", items.get(3).title);
        assertTrue("Card 4 should contain 12 years reward", items.get(3).fullContent.contains("বারো বৎসর আযান"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanSignificancePageDialog.StepItem> items = SalahAzanSignificancePageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan Significance in English", 4, items.size());

        assertEquals("Significance & Virtues of Adhan", items.get(0).title);
        assertTrue("Card 1 should contain Fard al-Kifayah in English", items.get(0).fullContent.contains("Fard al-Kifayah"));
        assertTrue("Card 1 should contain Quran 41:33 in English", items.get(0).fullContent.contains("[Al-Quran 41:33]"));
    }
}
