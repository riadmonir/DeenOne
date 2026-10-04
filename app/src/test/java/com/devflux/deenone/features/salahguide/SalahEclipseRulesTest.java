package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahEclipseRulesTest {

    @Test
    public void testBengaliEclipseRulesCountAndContent() {
        List<SalahEclipseRulesPageDialog.StepItem> items = SalahEclipseRulesPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals("Must contain 18 eclipse prayer cards", 18, items.size());

        // Card 1: গুরুত্ব ও ফজিলত
        assertEquals("Card 1 title must match", "চন্দ্র ও সূর্য গ্রহণের সালাতের গুরুত্ব ও ফজিলত", items.get(0).title);
        assertTrue("Card 1 must contain Surah Fussilat 37", items.get(0).fullContent.contains("সূরা ফুসসিলাত, ৪১:৩৭"));
        assertTrue("Card 1 must contain Bukhari 1044 / Muslim 901", items.get(0).fullContent.contains("সহীহ বুখারি, ১০৪৪; সহীহ মুসলিম, ৯০১"));
        assertTrue("Card 1 must contain Arabic Hadith", items.get(0).fullContent.contains("إِنَّ الشَّمْسَ وَالْقَمَرَ آيَتَانِ مِنْ آيَاتِ اللَّهِ"));

        // Card 2: ১. নিয়ত (ইচ্ছা)
        assertEquals("Card 2 title must match", "১. নিয়ত (ইচ্ছা)", items.get(1).title);
        assertTrue("Card 2 must contain intention text", items.get(1).fullContent.contains("সালাতুল কুসুফ/খুসুফ"));

        // Card 3: ২. তাকবির (আল্লাহু আকবার বলা)
        assertEquals("Card 3 title must match", "২. তাকবির (আল্লাহু আকবার বলা)", items.get(2).title);
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

        // Card 8: ৭. দ্বিতীয় কিয়াম (দাঁড়ানো)
        assertEquals("Card 8 title must match", "৭. দ্বিতীয় কিয়াম (দাঁড়ানো)", items.get(7).title);
        assertTrue("Card 8 must contain second recitation instruction", items.get(7).fullContent.contains("পুনরায় সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়া"));

        // Card 9: ৮. দ্বিতীয় রুকু (ঝুঁকে থাকা)
        assertEquals("Card 9 title must match", "৮. দ্বিতীয় রুকু (ঝুঁকে থাকা)", items.get(8).title);
        assertTrue("Card 9 must contain second ruku tasbih", items.get(8).fullContent.contains("سُبْحَانَ رَبِّيَ الْعَظِيمِ"));

        // Card 10: ৯. দ্বিতীয় কিয়াম (দাঁড়ানো)
        assertEquals("Card 10 title must match", "৯. দ্বিতীয় কিয়াম (দাঁড়ানো)", items.get(9).title);
        assertTrue("Card 10 must contain Sami Allahu liman hamidah", items.get(9).fullContent.contains("سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ"));

        // Card 11: ১০. সিজদা (প্রথম সেজদা)
        assertEquals("Card 11 title must match", "১০. সিজদা (প্রথম সেজদা)", items.get(10).title);
        assertTrue("Card 11 must contain Sujud tasbih", items.get(10).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 12: ১১. জলসা (দুই সেজদার মধ্যে বসা)
        assertEquals("Card 12 title must match", "১১. জলসা (দুই সেজদার মধ্যে বসা)", items.get(11).title);
        assertTrue("Card 12 must contain sitting between sujud", items.get(11).fullContent.contains("সেজদা থেকে উঠে বসেন"));

        // Card 13: ১২. সিজদা (দ্বিতীয় সেজদা)
        assertEquals("Card 13 title must match", "১২. সিজদা (দ্বিতীয় সেজদা)", items.get(12).title);
        assertTrue("Card 13 must contain second sujud tasbih", items.get(12).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 14: ১৩. দ্বিতীয় রাকাত
        assertEquals("Card 14 title must match", "১৩. দ্বিতীয় রাকাত", items.get(13).title);
        assertTrue("Card 14 must contain second rakah instruction", items.get(13).fullContent.contains("প্রথম রাকাতের মতো একই প্রক্রিয়া অনুসরণ করুন"));

        // Card 15: ১৪. তাশাহহুদ (আত্তাহিয়্যাতু)
        assertEquals("Card 15 title must match", "১৪. তাশাহহুদ (আত্তাহিয়্যাতু)", items.get(14).title);
        assertTrue("Card 15 must contain Arabic Tashahhud", items.get(14).fullContent.contains("التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ"));

        // Card 16: ১৫. দরুদ শরীফ
        assertEquals("Card 16 title must match", "১৫. দরুদ শরীফ", items.get(15).title);
        assertTrue("Card 16 must contain Arabic Durood", items.get(15).fullContent.contains("اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ"));

        // Card 17: ১৬. দু'আ
        assertEquals("Card 17 title must match", "১৬. দু'আ", items.get(16).title);
        assertTrue("Card 17 must contain Arabic Dua", items.get(16).fullContent.contains("اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي"));

        // Card 18: ১৭. সালাম (নামাজ সমাপ্ত করা)
        assertEquals("Card 18 title must match", "১৭. সালাম (নামাজ সমাপ্ত করা)", items.get(17).title);
        assertTrue("Card 18 must contain Arabic Salam", items.get(17).fullContent.contains("السلام عليكم ورحمة الله"));

        for (SalahEclipseRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishEclipseRulesCountAndContent() {
        List<SalahEclipseRulesPageDialog.StepItem> items = SalahEclipseRulesPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals("Must contain 18 eclipse prayer cards", 18, items.size());

        for (SalahEclipseRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
