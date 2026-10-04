package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahTaraweehRulesTest {

    @Test
    public void testBengaliTaraweehRulesCountAndContent() {
        List<SalahTaraweehRulesPageDialog.StepItem> items = SalahTaraweehRulesPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals("Must contain 14 Taraweeh cards", 14, items.size());

        // Card 1: তারাবীর নামাজের গুরুত্ব ও ফজিলত
        assertEquals("Card 1 title must match", "তারাবীর নামাজের গুরুত্ব ও ফজিলত", items.get(0).title);
        assertTrue("Card 1 full content must contain Surah Baqarah 183", items.get(0).fullContent.contains("সূরা আল-বাকারা, ২:১৮৩"));
        assertTrue("Card 1 full content must contain Bukhari 2014 & Muslim 760", items.get(0).fullContent.contains("সহীহ বুখারি, ২০১৪; সহীহ মুসলিম, ৭৬০"));
        assertTrue("Card 1 full content must contain Arabic hadith", items.get(0).fullContent.contains("مَنْ قَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ"));

        // Card 2: ১. নিয়ত (ইচ্ছা)
        assertEquals("Card 2 title must match", "১. নিয়ত (ইচ্ছা)", items.get(1).title);
        assertTrue("Card 2 full content must contain Taraweeh intention", items.get(1).fullContent.contains("আমি তারাবীর নামাজ পড়ছি আল্লাহর উদ্দেশ্যে"));

        // Card 3: ২. তাকবির (আল্লাহু আকবার বলা)
        assertEquals("Card 3 title must match", "২. তাকবির (আল্লাহু আকবার বলা)", items.get(2).title);
        assertTrue("Card 3 full content must contain Takbir", items.get(2).fullContent.contains("اللَّهُ أَكْبَرُ"));

        // Card 4: ৩. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা
        assertEquals("Card 4 title must match", "৩. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা", items.get(3).title);
        assertTrue("Card 4 full content must contain Surah Fatihah verses", items.get(3).fullContent.contains("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ") && items.get(3).fullContent.contains("صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ"));

        // Card 5: ৪. সূরা পড়া
        assertEquals("Card 5 title must match", "৪. সূরা পড়া", items.get(4).title);
        assertTrue("Card 5 full content must contain Surah Ikhlas", items.get(4).fullContent.contains("قُلْ هُوَ اللَّهُ أَحَدٌ") && items.get(4).fullContent.contains("وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ"));

        // Card 6: ৫. রুকু (ঝুঁকে থাকা)
        assertEquals("Card 6 title must match", "৫. রুকু (ঝুঁকে থাকা)", items.get(5).title);
        assertTrue("Card 6 full content must contain Arabic ruku tasbih", items.get(5).fullContent.contains("سُبْحَانَ رَبِّيَ الْعَظِيمِ"));

        // Card 7: ৬. কিয়াম (দাঁড়ানো)
        assertEquals("Card 7 title must match", "৬. কিয়াম (দাঁড়ানো)", items.get(6).title);
        assertTrue("Card 7 full content must contain Sami Allahu liman hamidah", items.get(6).fullContent.contains("سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ") && items.get(6).fullContent.contains("رَبَّنَا لَكَ الْحَمْدُ"));

        // Card 8: ৭. সিজদা (প্রথম সেজদা)
        assertEquals("Card 8 title must match", "৭. সিজদা (প্রথম সেজদা)", items.get(7).title);
        assertTrue("Card 8 full content must contain Arabic sujud tasbih", items.get(7).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 9: ৮. জলসা (দুই সেজদার মধ্যে বসা)
        assertEquals("Card 9 title must match", "৮. জলসা (দুই সেজদার মধ্যে বসা)", items.get(8).title);
        assertTrue("Card 9 full content must contain sitting", items.get(8).fullContent.contains("সেজদা থেকে উঠে বসেন"));

        // Card 10: ৯. সিজদা (দ্বিতীয় সেজদা)
        assertEquals("Card 10 title must match", "৯. সিজদা (দ্বিতীয় সেজদা)", items.get(9).title);
        assertTrue("Card 10 full content must contain second sujud", items.get(9).fullContent.contains("আবার সেজদা করুন"));

        // Card 11: ১০. দ্বিতীয় রাকাত
        assertEquals("Card 11 title must match", "১০. দ্বিতীয় রাকাত", items.get(10).title);
        assertTrue("Card 11 full content must contain second rakah instruction", items.get(10).fullContent.contains("প্রথম রাকাতের মতই সম্পন্ন করুন"));

        // Card 12: ১১. তাশাহহুদ (আত্তাহিয়্যাতু)
        assertEquals("Card 12 title must match", "১১. তাশাহহুদ (আত্তাহিয়্যাতু)", items.get(11).title);
        assertTrue("Card 12 full content must contain Arabic Tashahhud", items.get(11).fullContent.contains("التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ"));

        // Card 13: ১২. দরুদ শরীফ
        assertEquals("Card 13 title must match", "১২. দরুদ শরীফ", items.get(12).title);
        assertTrue("Card 13 full content must contain Arabic Durood", items.get(12).fullContent.contains("اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ"));

        // Card 14: ১৩. সালাম (নামাজ সমাপ্ত করা)
        assertEquals("Card 14 title must match", "১৩. সালাম (নামাজ সমাপ্ত করা)", items.get(13).title);
        assertTrue("Card 14 full content must contain Arabic Salam", items.get(13).fullContent.contains("السلام عليكم ورحمة الله"));

        for (SalahTaraweehRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishTaraweehRulesCountAndContent() {
        List<SalahTaraweehRulesPageDialog.StepItem> items = SalahTaraweehRulesPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals("Must contain 14 Taraweeh cards", 14, items.size());

        for (SalahTaraweehRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
