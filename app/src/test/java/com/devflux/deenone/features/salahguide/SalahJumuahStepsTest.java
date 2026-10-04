package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahJumuahStepsTest {

    @Test
    public void testBengaliJumuahSteps() {
        List<SalahJumuahStepsPageDialog.StepItem> items = SalahJumuahStepsPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals("Must contain 13 Jum'ah step cards", 13, items.size());

        // 1. নিয়ত
        assertTrue("Card 1 must contain Niyyah details", items.get(0).fullContent.contains("আমি দুই রাকাত জুম'আর ফরজ নামাজ পড়ছি আল্লাহর উদ্দেশ্যে।"));

        // 2. তাকবির
        assertTrue("Card 2 must contain Allahu Akbar in Arabic", items.get(1).fullContent.contains("اللَّهُ أَكْبَرُ"));

        // 3. কিয়াম
        assertTrue("Card 3 must contain Qiyam", items.get(2).fullContent.contains("সূরা ফাতিহা এবং কুরআনের অন্য একটি সূরা পড়তে হয়"));

        // 4. রুকু
        assertTrue("Card 4 must contain Subhana Rabbiyal Azeem Arabic", items.get(3).fullContent.contains("سُبْحَانَ رَبِّيَ الْعَظِيمِ"));

        // 5. কিয়াম (রুকু থেকে উঠা)
        assertTrue("Card 5 must contain Sami Allahu in Arabic", items.get(4).fullContent.contains("سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ"));
        assertTrue("Card 5 must contain Rabbana lakal hamd Arabic", items.get(4).fullContent.contains("رَبَّنَا لَكَ الْحَمْدُ"));

        // 6. সিজদা (প্রথম সেজদা)
        assertTrue("Card 6 must contain Subhana Rabbiyal A'la in Arabic", items.get(5).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // 7. জলসা
        assertTrue("Card 7 must contain Jalsah", items.get(6).fullContent.contains("জলসা (দুই সেজদার মধ্যে বসা):"));

        // 8. দ্বিতীয় সিজদা
        assertTrue("Card 8 must contain second Sujud", items.get(7).fullContent.contains("সিজদা (দ্বিতীয় সেজদা):"));

        // 9. দ্বিতীয় রাকাত
        assertTrue("Card 9 must contain second Rak'ah", items.get(8).fullContent.contains("দ্বিতীয় রাকাত:"));

        // 10. তাশাহহুদ
        assertTrue("Card 10 must contain Tashahhud in Arabic", items.get(9).fullContent.contains("التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ"));
        assertTrue("Card 10 must contain pronunciation", items.get(9).fullContent.contains("আত্তাহিয়্যাতু লিল্লাহি ওয়াস-সালাওয়াতু"));

        // 11. দরুদ শরীফ
        assertTrue("Card 11 must contain Durood in Arabic", items.get(10).fullContent.contains("اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ وَعَلَىٰ آلِ مُحَمَّدٍ"));
        assertTrue("Card 11 must contain pronunciation", items.get(10).fullContent.contains("আল্লাহুম্মা সাল্লি আ’লা মুহাম্মাদিন"));

        // 12. দু'আ
        assertTrue("Card 12 must contain Dua in Arabic", items.get(11).fullContent.contains("اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي"));
        assertTrue("Card 12 must contain pronunciation", items.get(11).fullContent.contains("আল্লাহুম্মা ইন্নি যালামতু নাফসি"));

        // 13. সালাম
        assertTrue("Card 13 must contain Salam in Arabic", items.get(12).fullContent.contains("السلام عليكم ورحمة الله"));

        for (SalahJumuahStepsPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishJumuahSteps() {
        List<SalahJumuahStepsPageDialog.StepItem> items = SalahJumuahStepsPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals("Must contain 13 Jum'ah step cards in English", 13, items.size());

        for (SalahJumuahStepsPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
