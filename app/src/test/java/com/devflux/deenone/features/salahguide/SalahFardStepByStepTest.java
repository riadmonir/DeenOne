package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahFardStepByStepTest {

    @Test
    public void testBengaliFardSteps() {
        List<SalahFardStepByStepPageDialog.StepItem> items = SalahFardStepByStepPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals("Must contain 14 Fard Salah step cards", 14, items.size());

        // 1. নিয়ত
        assertTrue("Card 1 must contain Niyyah details", items.get(0).fullContent.contains("নিয়ত (নিয়্যত করা):"));
        assertTrue("Card 1 must contain Fajr example", items.get(0).fullContent.contains("উদাহরণ (ফজরের জন্য):"));

        // 2. তাকবীরে তাহরিমা
        assertTrue("Card 2 must contain Allahu Akbar", items.get(1).fullContent.contains("আল্লাহু আকবার"));
        assertTrue("Card 2 must contain Bukhari reference", items.get(1).fullContent.contains("[বুখারি: ৭৩৮]"));

        // 3. সানা দোয়া
        assertTrue("Card 3 must contain Thana", items.get(2).fullContent.contains("সুবহানাকাল্লাহুম্মা ওয়া বিহামদিকা"));
        assertTrue("Card 3 must contain Tirmidhi reference", items.get(2).fullContent.contains("[তিরমিজি: ২৪৩]"));

        // 4. আউজু ও বিসমিল্লাহ
        assertTrue("Card 4 must contain Ta'awwudh", items.get(3).fullContent.contains("আউজুবিল্লাহি মিনাশ শাইতানির রাজিম"));
        assertTrue("Card 4 must contain Basmalah", items.get(3).fullContent.contains("বিসমিল্লাহির রাহমানির রাহিম"));

        // 5. সূরা ফাতিহা
        assertTrue("Card 5 must contain Surah Fatihah Fard", items.get(4).fullContent.contains("সূরা ফাতিহা পড়া প্রত্যেক রাকাআতে ফরজ"));
        assertTrue("Card 5 must contain Bukhari reference", items.get(4).fullContent.contains("[সহিহ বুখারি: ৭৫৬]"));

        // 6. কোনো সূরা বা আয়াত পাঠ
        assertTrue("Card 6 must contain additional surah in first 2 rakahs", items.get(5).fullContent.contains("প্রথম দুই রাকাআতে সূরা ফাতিহার পর একটি সূরা"));
        assertTrue("Card 6 must contain Muslim reference", items.get(5).fullContent.contains("[সহিহ মুসলিম: ৩৯৪]"));

        // 7. রুকুতে যাওয়া
        assertTrue("Card 7 must contain Subhana Rabbiyal Azeem", items.get(6).fullContent.contains("সুবহানা রব্বিয়াল আজিম"));
        assertTrue("Card 7 must contain Tirmidhi reference", items.get(6).fullContent.contains("[তিরমিজি: ২৬২]"));

        // 8. রুকু থেকে উঠা
        assertTrue("Card 8 must contain Sami Allahu & Rabbana lakal hamd", items.get(7).fullContent.contains("রাব্বানা লাকাল হামদ"));
        assertTrue("Card 8 must contain Bukhari reference", items.get(7).fullContent.contains("[বুখারি: ৭৯৪]"));

        // 9. সিজদাহ
        assertTrue("Card 9 must contain Subhana Rabbiyal A'la", items.get(8).fullContent.contains("সুবহানা রাব্বিয়াল আ'লা"));
        assertTrue("Card 9 must contain Tirmidhi reference", items.get(8).fullContent.contains("[তিরমিজি: ২৬২]"));

        // 10. দুই সিজদার মাঝে বসা
        assertTrue("Card 10 must contain Rabbighfirli", items.get(9).fullContent.contains("রব্বিগ্ফিরলি, রব্বিগ্ফিরলি"));
        assertTrue("Card 10 must contain Abu Dawud reference", items.get(9).fullContent.contains("[আবু দাউদ: ৮৫০]"));

        // 11. দ্বিতীয় সিজদাহ
        assertTrue("Card 11 must contain second Sujud", items.get(10).title.contains("দ্বিতীয় সিজদাহ"));

        // 12. তাশাহহুদ
        assertTrue("Card 12 must contain Tashahhud", items.get(11).fullContent.contains("আত্তাহিয়্যাতু লিল্লাহি"));
        assertTrue("Card 12 must contain Muslim reference", items.get(11).fullContent.contains("[সহিহ মুসলিম: ৪০৩]"));

        // 13. দুরুদ শরীফ
        assertTrue("Card 13 must contain Durood", items.get(12).fullContent.contains("আল্লাহুম্মা সাল্লি আলা মুহাম্মাদ"));
        assertTrue("Card 13 must contain Bukhari reference", items.get(12).fullContent.contains("[বুখারি: ৩৩৭০]"));

        // 14. সালাম ফিরানো
        assertTrue("Card 14 must contain Salam", items.get(13).fullContent.contains("আসসালামু আলাইকুম ওয়া রহমাতুল্লাহ"));
        assertTrue("Card 14 must contain Tirmidhi reference", items.get(13).fullContent.contains("[তিরমিজি: ২৯৫]"));

        for (SalahFardStepByStepPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishFardSteps() {
        List<SalahFardStepByStepPageDialog.StepItem> items = SalahFardStepByStepPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals("Must contain 14 Fard Salah step cards in English", 14, items.size());

        for (SalahFardStepByStepPageDialog.StepItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
