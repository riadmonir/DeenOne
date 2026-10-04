package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanHistoryTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanHistoryPageDialog.StepItem> items = SalahAzanHistoryPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan History in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("আজানের প্রারম্ভিক ইতিহাস", items.get(0).title);
        assertTrue("Card 1 should contain Makkah stay & Fath al-Bari reference", items.get(0).fullContent.contains("মক্কায় অবস্থানকালে মহানবী (ﷺ) তথা মুসলিমগণ বিনা আযানে নামায পড়েছেন।"));
        assertTrue("Card 1 should contain Fath al-Bari", items.get(0).fullContent.contains("[ফাতহুল বারী, ইবনে হাজার ২/৭৮]"));
        assertTrue("Card 1 should contain consultation & Umar RA proposal", items.get(0).fullContent.contains("নাসারাদের ঘন্টার মত আমরাও ঘন্টা ব্যবহার করব"));
        assertTrue("Card 1 should contain Bukhari 604", items.get(0).fullContent.contains("[বুখারী ৬০৪, মুসলিম, সহীহ]"));
        assertTrue("Card 1 should contain Flag proposal & Abu Dawud 498", items.get(0).fullContent.contains("[আবূদাঊদ, সুনান ৪৯৮নং]"));
        assertTrue("Card 1 should contain Abdullah ibn Zayd dream", items.get(0).fullContent.contains("আব্দুল্লাহ বিন যায়দ (রাঃ) স্বপ্নে দেখলেন"));
        assertTrue("Card 1 should contain Prophet instruction to teach Bilal", items.get(0).fullContent.contains("বিলালের আওয়াজ তোমার চেয়ে উচ্চ"));
        assertTrue("Card 1 should contain Arabic hadith text", items.get(0).fullContent.contains("وَالَّذِي بَعَثَكَ بِالْحَقِّ"));
        assertTrue("Card 1 should contain meaning and Abu Dawud 495", items.get(0).fullContent.contains("[আবুদাঊদ হা/৪৯৫; মিশকাত হা/৬৫০]"));
        assertTrue("Card 1 should contain 11 companions report", items.get(0).fullContent.contains("১১ জন সাহাবী একই আযানের স্বপ্ন দেখেন"));
        assertTrue("Card 1 should contain 20 days prior note", items.get(0).fullContent.contains("২০ দিন পূর্বে উক্ত স্বপ্ন দেখেছিলেন"));

        // Card 2: Context
        assertEquals("আযান ফরজ হওয়ার প্রেক্ষাপট ও সাহাবীগণের পরামর্শ", items.get(1).title);
        assertTrue("Card 2 should contain consultation", items.get(1).fullContent.contains("হিজরী ১ম (মতান্তরে ২য়) সনে আযান ফরয হয়"));

        // Card 3: Abdullah ibn Zayd dream
        assertEquals("আব্দুল্লাহ বিন যায়দ (রাঃ)-এর স্বপ্ন ও রাসুলুল্লাহ (ﷺ)-এর স্বীকৃতি", items.get(2).title);
        assertTrue("Card 3 should contain dream details", items.get(2).fullContent.contains("ইনশাআল্লাহ! এটি সত্য স্বপ্ন"));

        // Card 4: Umar RA & Other companions
        assertEquals("হযরত ওমর (রাঃ) ও অন্যান্য সাহাবীগণের স্বপ্ন", items.get(3).title);
        assertTrue("Card 4 should contain Umar RA response", items.get(3).fullContent.contains("وَالَّذِي بَعَثَكَ بِالْحَقِّ"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanHistoryPageDialog.StepItem> items = SalahAzanHistoryPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Azan History in English", 4, items.size());

        assertEquals("Initial History of Adhan", items.get(0).title);
        assertTrue("Card 1 should contain Makkah stay in English", items.get(0).fullContent.contains("During their time in Makkah"));
        assertTrue("Card 1 should contain Arabic hadith", items.get(0).fullContent.contains("وَالَّذِي بَعَثَكَ بِالْحَقِّ"));
    }
}
