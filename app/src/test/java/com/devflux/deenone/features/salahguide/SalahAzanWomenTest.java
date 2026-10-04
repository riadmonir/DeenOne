package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanWomenTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahAzanWomenPageDialog.StepItem> items = SalahAzanWomenPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Women Adhan & Iqamah in Bengali", 4, items.size());

        // Card 1: Verbatim Comprehensive
        assertEquals("খাস মহিলা মহলে মহিলাদের আযান ও ইকামত", items.get(0).title);
        assertTrue("Card 1 should contain Aisha hadith status", items.get(0).fullContent.contains("হযরত আয়েশা (রাঃ) এর আযান ও ইকামত দেওয়ার ব্যাপারে বর্ণিত হাদীস সহীহ নয়"));
        assertTrue("Card 1 should contain Bayhaqi and Thawban", items.get(0).fullContent.contains("আম্র বিন আবী সালামাহ্ বলেন, আমি সওবানকে জিজ্ঞাসা করলাম"));
        assertTrue("Card 1 should contain Makhul statement", items.get(0).fullContent.contains("মকহুল বলেছেন, যদি মহিলারা আযান-ইকামত দেয় তবে তা আফযল"));
        assertTrue("Card 1 should contain Urwah from Aisha", items.get(0).fullContent.contains("আমরা বিনা ইকামতেই নামায পড়তাম"));
        assertTrue("Card 1 should contain Imam Bayhaqi reconciliation", items.get(0).fullContent.contains("ইমাম বাইহাকী বলেন, প্রথমোক্ত আসারের সাথে"));
        assertTrue("Card 1 should contain Allamah Albani and Nawab Siddiq Hasan Khan", items.get(0).fullContent.contains("আল্লামা আলবানী বলেন, এ ব্যাপারে সঠিক অভিমত হল নবাব সিদ্দীক হাসান খানের"));
        assertTrue("Card 1 should contain women counterparts of men", items.get(0).fullContent.contains("মহিলারা পুরুষদের সহোদরা"));
        assertTrue("Card 1 should contain references", items.get(0).fullContent.contains("[আর-রওযাতুন নাদিয়্যাহ্ ১/৭৯, সিলসিলাহ যায়ীফাহ, আলবানী ২/২৭১]"));

        // Card 2: Aisha & Bayhaqi
        assertEquals("আয়েশা (রাঃ)-এর আসার ও বাইহাকীর বর্ণনা", items.get(1).title);
        assertTrue("Card 2 should contain Makhul", items.get(1).fullContent.contains("মকহুল"));

        // Card 3: Bayhaqi reconciliation
        assertEquals("ইমাম বাইহাকীর পর্যালোচনা ও সমন্বয়", items.get(2).title);
        assertTrue("Card 3 should contain Bayhaqi words", items.get(2).fullContent.contains("উভয় প্রকারের আমল"));

        // Card 4: Albani research
        assertEquals("আল্লামা আলবানী ও নবাব সিদ্দীক হাসান খানের তাহক্বীক্ব", items.get(3).title);
        assertTrue("Card 4 should contain Nawab Siddiq Hasan Khan", items.get(3).fullContent.contains("নবাব সিদ্দীক হাসান খানের"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahAzanWomenPageDialog.StepItem> items = SalahAzanWomenPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Women Adhan in English", 4, items.size());

        assertEquals("Adhan & Iqamah for Women in Exclusive Gatherings", items.get(0).title);
        assertTrue("Card 1 should contain Makhul in English", items.get(0).fullContent.contains("Makhul"));
        assertTrue("Card 1 should contain counterparts in English", items.get(0).fullContent.contains("counterparts of men"));
    }
}
