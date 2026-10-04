package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahNaflTimesTest {

    @Test
    public void testBengaliNaflCards() {
        List<SalahNaflTimesPageDialog.NaflItem> items = SalahNaflTimesPageDialog.getNaflItems(true);
        assertNotNull(items);
        assertEquals("Must contain 4 nafl cards", 4, items.size());

        // Card 1: নফল নামাযের প্রকারভেদ
        assertEquals("Card 1 title must match", "নফল নামাযের প্রকারভেদ", items.get(0).title);
        assertTrue("Card 1 preview must match screenshot", items.get(0).previewSubtitle.contains("ফরয নামায"));
        assertTrue("Card 1 full content must contain verbatim text", items.get(0).fullContent.contains("সুন্নাতে মুআক্কাদাহ ও গায়র মুআক্কাদাহ"));

        // Card 2: নফল নামায এর বিবরণ
        assertEquals("Card 2 title must match", "নফল নামায এর বিবরণ", items.get(1).title);
        assertTrue("Card 2 preview must match screenshot", items.get(1).previewSubtitle.contains("যে নামায"));
        assertTrue("Card 2 full content must contain Bukhari 6502", items.get(1).fullContent.contains("বুখারী ৬৫০২নং"));
        assertTrue("Card 2 full content must contain Abu Dawud reference", items.get(1).fullContent.contains("আবূদাঊদ, সুনান ৭৭০"));
        assertTrue("Card 2 full content must contain explanation", items.get(1).fullContent.contains("আল্লাহ বান্দার কান, চোখ, হাত ও পা হওয়ার অর্থ হল"));

        // Card 3: নফল নামায ঘরে পড়া ভাল
        assertEquals("Card 3 title must match", "নফল নামায ঘরে পড়া ভাল", items.get(2).title);
        assertTrue("Card 3 preview must match screenshot", items.get(2).previewSubtitle.contains("ফরয নামায বিধিবদ্ধ হয়েছে দ্বীনের প্রচার"));
        assertTrue("Card 3 full content must contain Fayd al-Qadir", items.get(2).fullContent.contains("ফাইযুল ক্বাদীর ৪/২২০"));
        assertTrue("Card 3 full content must contain Bukhari 432 / Muslim 777", items.get(2).fullContent.contains("বুখারী ৪৩২, মুসলিম, সহীহ ৭৭৭"));
        assertTrue("Card 3 full content must contain Muslim 778", items.get(2).fullContent.contains("মুসলিম, সহীহ ৭৭৮নং"));
        assertTrue("Card 3 full content must contain Madinah reference", items.get(2).fullContent.contains("মদ্বীনাবাসীর জন্যও মসজিদে নববীতে"));

        // Card 4: নফল নামাযে লম্বা কিয়াম করা উত্তম
        assertEquals("Card 4 title must match", "নফল নামাযে লম্বা কিয়াম করা উত্তম", items.get(3).title);
        assertTrue("Card 4 preview must match screenshot", items.get(3).previewSubtitle.contains("নফল নামায সাধারণত: একার নামায"));
        assertTrue("Card 4 full content must contain Abu Dawud 1449", items.get(3).fullContent.contains("আবূদাঊদ, সুনান ১৪৪৯"));
        assertTrue("Card 4 full content must contain swollen feet Tahajjud Hadith", items.get(3).fullContent.contains("তাতে তাঁর পা ফুলে যেত"));
        assertTrue("Card 4 full content must contain grateful slave Hadith", items.get(3).fullContent.contains("আমি কি আল্লাহর কৃতজ্ঞ বান্দা হ্ব না?"));
        assertTrue("Card 4 full content must contain Mishkat 1220", items.get(3).fullContent.contains("মিশকাত ১২২০"));

        for (SalahNaflTimesPageDialog.NaflItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishNaflCards() {
        List<SalahNaflTimesPageDialog.NaflItem> items = SalahNaflTimesPageDialog.getNaflItems(false);
        assertNotNull(items);
        assertEquals("Must contain 4 nafl cards in English", 4, items.size());

        for (SalahNaflTimesPageDialog.NaflItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
