package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahGeneralStepsTest {

    @Test
    public void testBengaliGeneralStepsCountAndContent() {
        List<SalahGeneralStepsPageDialog.StepItem> items = SalahGeneralStepsPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals("Must contain 14 steps", 14, items.size());

        // Card 1: ১. নিয়ত (ইচ্ছা)
        assertEquals("Card 1 title must match", "১. নিয়ত (ইচ্ছা)", items.get(0).title);
        assertTrue("Card 1 content must contain Fajr intention", items.get(0).fullContent.contains("আমি ফজরের দুই রাকাত ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে"));

        // Card 2: ২. তাকবির (আল্লাহু আকবার বলা)
        assertEquals("Card 2 title must match", "২. তাকবির (আল্লাহু আকবার বলা)", items.get(1).title);
        assertTrue("Card 2 content must contain Allahu Akbar and earlobe raising", items.get(1).fullContent.contains("اللَّهُ أَكْبَرُ") && items.get(1).fullContent.contains("কানের লতি"));

        // Card 3: ৩. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা
        assertEquals("Card 3 title must match", "৩. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা", items.get(2).title);
        assertTrue("Card 3 content must contain Fatihah verses", items.get(2).fullContent.contains("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ") && items.get(2).fullContent.contains("صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ"));

        // Card 4: ৪. সূরা পড়া
        assertEquals("Card 4 title must match", "৪. সূরা পড়া", items.get(3).title);
        assertTrue("Card 4 content must contain Surah Ikhlas verses", items.get(3).fullContent.contains("قُلْ هُوَ اللَّهُ أَحَدٌ") && items.get(3).fullContent.contains("وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ"));

        // Card 5: ৫. রুকু (ঝুঁকে থাকা)
        assertEquals("Card 5 title must match", "৫. রুকু (ঝুঁকে থাকা)", items.get(4).title);
        assertTrue("Card 5 content must contain Subhana Rabbiyal Azeem", items.get(4).fullContent.contains("سُبْحَانَ رَبِّيَ الْعَظِيمِ"));

        // Card 6: ৬. কিয়াম (দাঁড়ানো)
        assertEquals("Card 6 title must match", "৬. কিয়াম (দাঁড়ানো)", items.get(5).title);
        assertTrue("Card 6 content must contain Sami Allahu liman hamidah", items.get(5).fullContent.contains("سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ") && items.get(5).fullContent.contains("رَبَّنَا لَكَ الْحَمْدُ"));

        // Card 7: ৭. সিজদা (প্রথম সেজদা)
        assertEquals("Card 7 title must match", "৭. সিজদা (প্রথম সেজদা)", items.get(6).title);
        assertTrue("Card 7 content must contain Subhana Rabbiyal A'la", items.get(6).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 8: ৮. জলসা (দুই সেজদার মধ্যে বসা)
        assertEquals("Card 8 title must match", "৮. জলসা (দুই সেজদার মধ্যে বসা)", items.get(7).title);
        assertTrue("Card 8 content must contain sitting instruction", items.get(7).fullContent.contains("সেজদা থেকে উঠে বসেন"));

        // Card 9: ৯. সিজদা (দ্বিতীয় সেজদা)
        assertEquals("Card 9 title must match", "৯. সিজদা (দ্বিতীয় সেজদা)", items.get(8).title);
        assertTrue("Card 9 content must contain second sujud instruction", items.get(8).fullContent.contains("আবার সেজদা করুন"));

        // Card 10: ১০. দ্বিতীয় রাকাত
        assertEquals("Card 10 title must match", "১০. দ্বিতীয় রাকাত", items.get(9).title);
        assertTrue("Card 10 content must contain second rakah instruction", items.get(9).fullContent.contains("প্রথম রাকাতের মতই সম্পন্ন করুন"));

        // Card 11: ১১. তাশাহহুদ (আত্তাহিয়্যাতু)
        assertEquals("Card 11 title must match", "১১. তাশাহহুদ (আত্তাহিয়্যাতু)", items.get(10).title);
        assertTrue("Card 11 content must contain Tashahhud", items.get(10).fullContent.contains("التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ"));

        // Card 12: ১২. দরুদ শরীফ
        assertEquals("Card 12 title must match", "১২. দরুদ শরীফ", items.get(11).title);
        assertTrue("Card 12 content must contain Durood Ibrahim", items.get(11).fullContent.contains("اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ"));

        // Card 13: ১৩. দু'আ
        assertEquals("Card 13 title must match", "১৩. দু'আ", items.get(12).title);
        assertTrue("Card 13 content must contain Dua Masura", items.get(12).fullContent.contains("اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي"));

        // Card 14: ১৪. সালাম (নামাজ সমাপ্ত করা)
        assertEquals("Card 14 title must match", "১৪. সালাম (নামাজ সমাপ্ত করা)", items.get(13).title);
        assertTrue("Card 14 content must contain Salam", items.get(13).fullContent.contains("السلام عليكم ورحمة الله"));

        for (SalahGeneralStepsPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishGeneralStepsCountAndContent() {
        List<SalahGeneralStepsPageDialog.StepItem> items = SalahGeneralStepsPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals("Must contain 14 steps", 14, items.size());

        for (SalahGeneralStepsPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
