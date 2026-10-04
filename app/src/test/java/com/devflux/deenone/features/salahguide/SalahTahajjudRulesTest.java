package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahTahajjudRulesTest {

    @Test
    public void testBengaliTahajjudRulesCountAndContent() {
        List<SalahTahajjudRulesPageDialog.StepItem> items = SalahTahajjudRulesPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals("Must contain 16 Tahajjud cards", 16, items.size());

        // Card 1: তাহাজ্জুত নামাজের গুরুত্ব ও ফজিলত
        assertEquals("Card 1 title must match", "তাহাজ্জুত নামাজের গুরুত্ব ও ফজিলত", items.get(0).title);
        assertTrue("Card 1 full content must contain Surah Isra 79", items.get(0).fullContent.contains("সূরা আল-ইসরাআ, ১৭:৭৯"));
        assertTrue("Card 1 full content must contain Tirmidhi 3549", items.get(0).fullContent.contains("তিরমিজি, ৩৫৪৯"));
        assertTrue("Card 1 full content must contain Arabic hadith", items.get(0).fullContent.contains("عَلَيْكُمْ بِقِيَامِ اللَّيْلِ فَإِنَّهُ دَأْبُ الصَّالِحِينَ قَبْلَكُمْ"));

        // Card 2: ১. নিয়ত (ইচ্ছা)
        assertEquals("Card 2 title must match", "১. নিয়ত (ইচ্ছা)", items.get(1).title);
        assertTrue("Card 2 full content must contain waking up and intention", items.get(1).fullContent.contains("রাতে ঘুম থেকে জেগে উঠা এবং নামাজ পড়ার নিয়ত করা"));

        // Card 3: ২. ওযু করা
        assertEquals("Card 3 title must match", "২. ওযু করা", items.get(2).title);
        assertTrue("Card 3 full content must contain wudu", items.get(2).fullContent.contains("পবিত্রতার জন্য ওযু করা সুন্নত"));

        // Card 4: ৩. তাকবির (আল্লাহু আকবার বলা)
        assertEquals("Card 4 title must match", "৩. তাকবির (আল্লাহু আকবার বলা)", items.get(3).title);
        assertTrue("Card 4 full content must contain Takbir", items.get(3).fullContent.contains("اللَّهُ أَكْبَرُ"));

        // Card 5: ৪. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা
        assertEquals("Card 5 title must match", "৪. কিয়াম (দাঁড়ানো) ও সুরা ফাতিহা", items.get(4).title);
        assertTrue("Card 5 full content must contain Surah Fatihah verses", items.get(4).fullContent.contains("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ") && items.get(4).fullContent.contains("صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ"));

        // Card 6: ৫. সূরা পড়া
        assertEquals("Card 6 title must match", "৫. সূরা পড়া", items.get(5).title);
        assertTrue("Card 6 full content must contain Surah Ikhlas", items.get(5).fullContent.contains("قُلْ هُوَ اللَّهُ أَحَدٌ") && items.get(5).fullContent.contains("وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ"));

        // Card 7: ৬. রুকু (ঝুঁকে থাকা)
        assertEquals("Card 7 title must match", "৬. রুকু (ঝুঁকে থাকা)", items.get(6).title);
        assertTrue("Card 7 full content must contain Arabic ruku tasbih", items.get(6).fullContent.contains("سُبْحَانَ رَبِّيَ الْعَظِيمِ"));

        // Card 8: ৭. কিয়াম (দাঁড়ানো)
        assertEquals("Card 8 title must match", "৭. কিয়াম (দাঁড়ানো)", items.get(7).title);
        assertTrue("Card 8 full content must contain Sami Allahu liman hamidah", items.get(7).fullContent.contains("سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ") && items.get(7).fullContent.contains("رَبَّنَا لَكَ الْحَمْدُ"));

        // Card 9: ৮. সিজদা (প্রথম সেজদা)
        assertEquals("Card 9 title must match", "৮. সিজদা (প্রথম সেজদা)", items.get(8).title);
        assertTrue("Card 9 full content must contain Arabic sujud tasbih", items.get(8).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 10: ৯. জলসা (দুই সেজদার মধ্যে বসা)
        assertEquals("Card 10 title must match", "৯. জলসা (দুই সেজদার মধ্যে বসা)", items.get(9).title);
        assertTrue("Card 10 full content must contain sitting", items.get(9).fullContent.contains("সেজদা থেকে উঠে বসেন"));

        // Card 11: ১০. সিজদা (দ্বিতীয় সেজদা)
        assertEquals("Card 11 title must match", "১০. সিজদা (দ্বিতীয় সেজদা)", items.get(10).title);
        assertTrue("Card 11 full content must contain second sujud", items.get(10).fullContent.contains("আবার সেজদা করুন"));

        // Card 12: ১১. দ্বিতীয় রাকাত
        assertEquals("Card 12 title must match", "১১. দ্বিতীয় রাকাত", items.get(11).title);
        assertTrue("Card 12 full content must contain second rakah instruction", items.get(11).fullContent.contains("প্রথম রাকাতের মতই সম্পন্ন করুন"));

        // Card 13: ১২. তাশাহহুদ (আত্তাহিয়্যাতু)
        assertEquals("Card 13 title must match", "১২. তাশাহহুদ (আত্তাহিয়্যাতু)", items.get(12).title);
        assertTrue("Card 13 full content must contain Arabic Tashahhud", items.get(12).fullContent.contains("التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ"));

        // Card 14: ১৩. দরুদ শরীফ
        assertEquals("Card 14 title must match", "১৩. দরুদ শরীফ", items.get(13).title);
        assertTrue("Card 14 full content must contain Arabic Durood", items.get(13).fullContent.contains("اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ"));

        // Card 15: ১৪. দু'আ
        assertEquals("Card 15 title must match", "১৪. দু'আ", items.get(14).title);
        assertTrue("Card 15 full content must contain Arabic Dua", items.get(14).fullContent.contains("اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي"));

        // Card 16: ১৫. সালাম (নামাজ সমাপ্ত করা)
        assertEquals("Card 16 title must match", "১৫. সালাম (নামাজ সমাপ্ত করা)", items.get(15).title);
        assertTrue("Card 16 full content must contain Arabic Salam", items.get(15).fullContent.contains("السلام عليكم ورحمة الله"));

        for (SalahTahajjudRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishTahajjudRulesCountAndContent() {
        List<SalahTahajjudRulesPageDialog.StepItem> items = SalahTahajjudRulesPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals("Must contain 16 Tahajjud cards", 16, items.size());

        for (SalahTahajjudRulesPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
