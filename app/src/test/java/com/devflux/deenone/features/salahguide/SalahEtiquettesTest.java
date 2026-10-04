package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahEtiquettesTest {

    @Test
    public void testBengaliEtiquettesCountAndContent() {
        List<SalahEtiquettesPageDialog.EtiquetteItem> items = SalahEtiquettesPageDialog.getEtiquetteItems(true);
        assertNotNull(items);
        assertEquals("Must contain 5 etiquette cards", 5, items.size());

        // Card 1: ওয়াক্ত হলে নামায আদায় করা
        assertEquals("Card 1 title must match", "ওয়াক্ত হলে নামায আদায় করা", items.get(0).title);
        assertTrue("Card 1 preview must contain prefix", items.get(0).previewSubtitle.contains("প্রত্যেক সালাত নির্ধারিত ওয়াক্তের মধ্যে"));
        assertTrue("Card 1 full content must contain Surah Ma'un verse", items.get(0).fullContent.contains("সূরা মাউন"));
        assertTrue("Card 1 full content must contain Ghafeleen definition", items.get(0).fullContent.contains("গাফেল হলো ঐসব লোক"));

        // Card 2: কিবলামুখী হওয়া
        assertEquals("Card 2 title must match", "কিবলামুখী হওয়া", items.get(1).title);
        assertTrue("Card 2 preview must match", items.get(1).previewSubtitle.contains("পূর্ণ দেহসহ কিবলার দিকে মুখ করে দাঁড়ানো"));
        assertTrue("Card 2 full content must contain Surah Baqarah and Muslim reference", items.get(1).fullContent.contains("সূরা বাকারা: ১৪৪") && items.get(1).fullContent.contains("মুসলিম ৩৯৭"));

        // Card 3: পাশের মুসল্লির পায়ের সাথে পা মিলিয়ে দাঁড়ানো
        assertEquals("Card 3 title must match", "পাশের মুসল্লির পায়ের সাথে পা মিলিয়ে দাঁড়ানো", items.get(2).title);
        assertTrue("Card 3 preview must contain shoulder to shoulder", items.get(2).previewSubtitle.contains("পায়ের সাথে পা ও কাধের সাথে কাঁধ মিলিয়ে"));
        assertTrue("Card 3 full content must contain Bukhari and Abu Dawud references", items.get(2).fullContent.contains("বুখারী: ৭২৫, ইফা ৬৮৯") && items.get(2).fullContent.contains("আবু দাউদ- ৬৬৬"));

        // Card 4: সামনের কাতার আগে পূর্ণ করা
        assertEquals("Card 4 title must match", "সামনের কাতার আগে পূর্ণ করা", items.get(3).title);
        assertTrue("Card 4 preview must match", items.get(3).previewSubtitle.contains("সামনের কাতারে এগিয়ে বসবেন"));
        assertTrue("Card 4 full content must contain neck stepping restriction", items.get(3).fullContent.contains("ঘাড় ডিঙিয়ে"));

        // Card 5: সালাতের আহকাম-আরকান
        assertEquals("Card 5 title must match", "সালাতের আহকাম-আরকান", items.get(4).title);
        assertTrue("Card 5 preview must match", items.get(4).previewSubtitle.contains("আহকাম শব্দটি বহুবচন"));
        assertTrue("Card 5 full content must contain 7 ahkam and 3 conditions", items.get(4).fullContent.contains("সালাতের আহকাম সাতটি") && items.get(4).fullContent.contains("প্রাপ্তবয়স্ক হওয়া"));

        for (SalahEtiquettesPageDialog.EtiquetteItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishEtiquettesCountAndContent() {
        List<SalahEtiquettesPageDialog.EtiquetteItem> items = SalahEtiquettesPageDialog.getEtiquetteItems(false);
        assertNotNull(items);
        assertEquals("Must contain 5 etiquette cards", 5, items.size());

        for (SalahEtiquettesPageDialog.EtiquetteItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
