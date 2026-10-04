package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahWitrNiyyahTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahWitrNiyyahPageDialog.StepItem> items = SalahWitrNiyyahPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 3 items for Witr Niyyah in Bengali", 3, items.size());

        // Card 1: Arabic Niyyah, Pronunciation & Meaning
        assertEquals("বিতর নামাজের নিয়ত", items.get(0).title);
        assertTrue("Card 1 should contain Arabic Niyyah", items.get(0).fullContent.contains("نويت أن أصلي ثلاث ركعات صلاة الوتر لله تعالى"));
        assertTrue("Card 1 should contain pronunciation", items.get(0).fullContent.contains("নাওয়াইতু আন উসাল্লিয়া সালাতা ল-উইতির সালাসা রাকা'আতিন লিল্লাহি তাআলা।"));
        assertTrue("Card 1 should contain meaning", items.get(0).fullContent.contains("আমি নিয়ত করলাম, তিন রাকাত বিতর ওয়াজিব নামাজ পড়ার জন্য কাবার দিকে মুখ করে, আল্লাহর জন্য।"));

        // Card 2: Summary Method
        assertEquals("বিতর নামাজ আদায়ের সংক্ষিপ্ত নিয়ম", items.get(1).title);
        assertTrue("Card 2 should contain beginning with Takbir", items.get(1).fullContent.contains("নিয়তের পর তাকবির (আল্লাহু আকবার) দিয়ে নামাজ শুরু করবেন।"));
        assertTrue("Card 2 should contain first 2 rakats rule", items.get(1).fullContent.contains("প্রথম দুই রাকাতে স্বাভাবিক ফরজ নামাজের মত কিরাআত করবেন।"));
        assertTrue("Card 2 should contain 3rd rakat qunut rule", items.get(1).fullContent.contains("তৃতীয় রাকাতে সুরা পড়ার পর তাকবির দিয়ে হাত উঠিয়ে \"কুনূতের দোয়া\" পড়বেন।"));
        assertTrue("Card 2 should contain qunut example", items.get(1).fullContent.contains("اللَّهُمَّ إِنَّا نَسْتَعِينُكَ"));
        assertTrue("Card 2 should contain conclusion with salam", items.get(1).fullContent.contains("এরপর রুকু ও সেজদা করে সালাম ফিরিয়ে নামাজ শেষ করবেন।"));

        // Card 3: Step-by-Step stages
        assertEquals("বিতর নামাজের ধারাবাহিক ধাপসমূহ", items.get(2).title);
        assertTrue("Card 3 should contain stages breakdown", items.get(2).fullContent.contains("১ম ও ২য় রাকাত"));
        assertTrue("Card 3 should contain 3rd rakat", items.get(2).fullContent.contains("৩য় রাকাত ও কুনূতের দোয়া"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahWitrNiyyahPageDialog.StepItem> items = SalahWitrNiyyahPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 3 items for Witr Niyyah in English", 3, items.size());

        // Card 1: Arabic Niyyah, Transliteration & Translation
        assertEquals("Niyyah for Witr Prayer", items.get(0).title);
        assertTrue("Card 1 should contain Arabic Niyyah", items.get(0).fullContent.contains("نويت أن أصلي"));
        assertTrue("Card 1 should contain English transliteration", items.get(0).fullContent.contains("Nawaytu an usalliya"));
        assertTrue("Card 1 should contain English translation", items.get(0).fullContent.contains("I intend to perform three Rak'ahs of the Wajib Witr prayer"));

        // Card 2: Summary Method
        assertEquals("Summary Method of Witr Prayer", items.get(1).title);
        assertTrue("Card 2 should contain English start", items.get(1).fullContent.contains("begin the prayer with Takbir"));
        assertTrue("Card 2 should contain Dua Qunut", items.get(1).fullContent.contains("Dua Qunut"));

        // Card 3: Stages
        assertEquals("Step-by-Step Stages of Witr Prayer", items.get(2).title);
        assertTrue("Card 3 should contain 1st & 2nd Rak'ahs", items.get(2).fullContent.contains("1st & 2nd Rak'ahs"));
    }
}
