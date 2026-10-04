package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahIstisqaRulesTest {

    @Test
    public void testBengaliIstisqaRulesCountAndContent() {
        List<SalahIstisqaRulesPageDialog.StepItem> items = SalahIstisqaRulesPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals("Must contain 16 cards for Istisqa prayer", 16, items.size());

        // Card 1: গুরুত্ব ও তাৎপর্য
        assertEquals("Card 1 title must match", "সালাতুল ইস্তিসকার গুরুত্ব ও তাৎপর্য", items.get(0).title);
        assertTrue("Card 1 must contain Surah Ash-Shura 28", items.get(0).fullContent.contains("সূরা আশ-শুরা, ৪২:২৮"));
        assertTrue("Card 1 must contain Bukhari 1020", items.get(0).fullContent.contains("সহীহ বুখারি, ১০২০"));
        assertTrue("Card 1 must contain Arabic Hadith", items.get(0).fullContent.contains("خَرَجَ النَّبِيُّ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ إِلَى المُصَلَّى فَاسْتَسْقَى"));

        // Card 2: ১. নিয়ত (ইচ্ছা)
        assertEquals("Card 2 title must match", "১. নিয়ত (ইচ্ছা)", items.get(1).title);
        assertTrue("Card 2 must contain intention text", items.get(1).fullContent.contains("সালাতুল ইস্তিসকা"));

        // Card 3: ২. প্রথম তাকবির (আল্লাহু আকবার বলা)
        assertEquals("Card 3 title must match", "২. প্রথম তাকবির (আল্লাহু আকবার বলা)", items.get(2).title);
        assertTrue("Card 3 must contain Takbir", items.get(2).fullContent.contains("اللَّهُ أَكْبَرُ"));

        // Card 4: ৩. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা
        assertEquals("Card 4 title must match", "৩. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা", items.get(3).title);
        assertTrue("Card 4 must contain Surah Fatihah", items.get(3).fullContent.contains("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ") && items.get(3).fullContent.contains("সুরা দীর্ঘ করা সুন্নত"));

        // Card 5: ৪. সূরা পড়া
        assertEquals("Card 5 title must match", "৪. সূরা পড়া", items.get(4).title);
        assertTrue("Card 5 must contain Surah Ikhlas", items.get(4).fullContent.contains("قُلْ هُوَ اللَّهُ أَحَدٌ"));

        // Card 6: ৫. রুকু (ঝুঁকে থাকা)
        assertEquals("Card 6 title must match", "৫. রুকু (ঝুঁকে থাকা)", items.get(5).title);
        assertTrue("Card 6 must contain Ruku tasbih", items.get(5).fullContent.contains("سُبْحَانَ رَبِّيَ الْعَظِيمِ"));

        // Card 7: ৬. কিয়াম (দাঁড়ানো)
        assertEquals("Card 7 title must match", "৬. কিয়াম (দাঁড়ানো)", items.get(6).title);
        assertTrue("Card 7 must contain Sami Allahu liman hamidah", items.get(6).fullContent.contains("سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ"));

        // Card 8: ৭. সিজদা (প্রথম সেজদা)
        assertEquals("Card 8 title must match", "৭. সিজদা (প্রথম সেজদা)", items.get(7).title);
        assertTrue("Card 8 must contain Sujud tasbih", items.get(7).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 9: ৮. জলসা (দুই সেজদার মধ্যে বসা)
        assertEquals("Card 9 title must match", "৮. জলসা (দুই সেজদার মধ্যে বসা)", items.get(8).title);
        assertTrue("Card 9 must contain sitting instruction", items.get(8).fullContent.contains("সেজদা থেকে উঠে বসেন"));

        // Card 10: ৯. সিজদা (দ্বিতীয় সেজদা)
        assertEquals("Card 10 title must match", "৯. সিজদা (দ্বিতীয় সেজদা)", items.get(9).title);
        assertTrue("Card 10 must contain second sujud tasbih", items.get(9).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 11: ১০. দ্বিতীয় রাকাত
        assertEquals("Card 11 title must match", "১০. দ্বিতীয় রাকাত", items.get(10).title);
        assertTrue("Card 11 must contain second rakah instruction", items.get(10).fullContent.contains("প্রথম রাকাতের মতই সম্পন্ন করুন"));

        // Card 12: ১১. তাশাহহুদ (আত্তাহিয়্যাতু)
        assertEquals("Card 12 title must match", "১১. তাশাহহুদ (আত্তাহিয়্যাতু)", items.get(11).title);
        assertTrue("Card 12 must contain Tashahhud", items.get(11).fullContent.contains("التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ"));

        // Card 13: ১২. দরুদ শরীফ
        assertEquals("Card 13 title must match", "১২. দরুদ শরীফ", items.get(12).title);
        assertTrue("Card 13 must contain Durood", items.get(12).fullContent.contains("اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ"));

        // Card 14: ১৩. দু'আ
        assertEquals("Card 14 title must match", "১৩. দু'আ", items.get(13).title);
        assertTrue("Card 14 must contain Dua", items.get(13).fullContent.contains("اللَّهُمَّ") && items.get(13).fullContent.contains("الرَّحِيمُ") && items.get(13).fullContent.contains("আল্লাহুম্মা ইন্নি যালামতু"));

        // Card 15: ১৪. সালাম (নামাজ সমাপ্ত করা)
        assertEquals("Card 15 title must match", "১৪. সালাম (নামাজ সমাপ্ত করা)", items.get(14).title);
        assertTrue("Card 15 must contain Salam", items.get(14).fullContent.contains("السلام عليكم ورحمة الله") && items.get(14).fullContent.contains("আসসালামু আলাইকুম ওয়া রহমাতুল্লাহ"));

        // Card 16: সালাতুল ইস্তিসকার ফজিলত
        assertEquals("Card 16 title must match", "সালাতুল ইস্তিসকার ফজিলত", items.get(15).title);
        assertTrue("Card 16 must contain virtures text", items.get(15).fullContent.contains("বৃষ্টি প্রার্থনার জন্য নামাজ আদায় করেছেন"));

        for (SalahIstisqaRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishIstisqaRulesCountAndContent() {
        List<SalahIstisqaRulesPageDialog.StepItem> items = SalahIstisqaRulesPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals("Must contain 16 cards for Istisqa prayer", 16, items.size());

        for (SalahIstisqaRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
