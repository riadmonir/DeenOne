package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahTawbahRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahTawbahRulesPageDialog.StepItem> items = SalahTawbahRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Tawbah prayer in Bengali", 4, items.size());

        // Card 1: Definition & Abu Bakr RA Hadith
        assertEquals("সালাতুত তাওবাহ-এর পরিচিতি ও ফজিলত", items.get(0).title);
        assertTrue("Card 1 should contain definition", items.get(0).fullContent.contains("অনুতপ্ত হয়ে ক্ষমা প্রার্থনার জন্য বিশেষভাবে যে নফল সালাত আদায় করা হয়"));
        assertTrue("Card 1 should contain Abu Bakr RA narration", items.get(0).fullContent.contains("আবুবকর (রাঃ) হতে বর্ণিত তিনি বলেন"));
        assertTrue("Card 1 should contain references", items.get(0).fullContent.contains("মিশকাত হা/১৩২৪ ‘ঐচ্ছিক ছালাত’ অনুচ্ছেদ-৩৯"));
        assertTrue("Card 1 should contain Ale Imran reference", items.get(0).fullContent.contains("আলে ইমরান ৩/১৩৫"));

        // Card 2: Tabarani Hadith
        assertEquals("সালাতের রাকাত ও পদ্ধতি সম্পর্কিত হাদিস", items.get(1).title);
        assertTrue("Card 2 should contain Tabarani Hadith", items.get(1).fullContent.contains("ত্বাবারাণী কাবীরে ‘হাসান’ সনদে আবুদ্দারদা (রাঃ) হতে ‘মরফূ’ সূত্রে বর্ণিত হয়েছে"));
        assertTrue("Card 2 should contain rakats and method", items.get(1).fullContent.contains("দুই বা চার রাক‘আত ফরয কিংবা নফল"));
        assertTrue("Card 2 should contain references", items.get(1).fullContent.contains("ত্বাবারাণী কাবীর, আহমাদ হা/২৭৫৮৬"));

        // Card 3: Astaghfirullahal Azeem Dua
        assertEquals("তওবার বিশেষ দো‘আ", items.get(2).title);
        assertTrue("Card 3 should contain Arabic dua", items.get(2).fullContent.contains("أَسْتَغْفِرُ اللَّهَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ"));
        assertTrue("Card 3 should contain pronunciation", items.get(2).fullContent.contains("আস্তাগফিরুল্লা-হাল্লাযী লা ইলা-হা ইল্লা হুওয়াল"));
        assertTrue("Card 3 should contain translation", items.get(2).fullContent.contains("আমি ক্ষমা প্রার্থনা করছি সেই আল্লাহর নিকটে"));
        assertTrue("Card 3 should contain Tirmidhi reference", items.get(2).fullContent.contains("তিরমিযী, আবুদাঊদ, মিশকাত হা/২৩৫৩"));

        // Card 4: Sayyidul Istighfar
        assertEquals("সাইয়েদুল ইস্তেগফার", items.get(3).title);
        assertTrue("Card 4 should contain Arabic dua", items.get(3).fullContent.contains("اَللَّهُمَّ أَنْتَ رَبِّىْ لآ إِلهَ إلاَّ أَنْتَ"));
        assertTrue("Card 4 should contain pronunciation", items.get(3).fullContent.contains("আল্লা-হুম্মা আনতা রব্বী"));
        assertTrue("Card 4 should contain translation", items.get(3).fullContent.contains("হে আল্লাহ! তুমি আমার পালনকর্তা।"));
        assertTrue("Card 4 should contain confession", items.get(3).fullContent.contains("আমি আমার গোনাহের স্বীকৃতি দিচ্ছি"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahTawbahRulesPageDialog.StepItem> items = SalahTawbahRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Tawbah prayer in English", 4, items.size());

        // Card 1: Definition
        assertEquals("Definition and Significance of Salatut Tawbah", items.get(0).title);
        assertTrue("Card 1 should contain English definition", items.get(0).fullContent.contains("voluntary prayer performed specifically with remorse"));
        assertTrue("Card 1 should contain Abu Bakr RA", items.get(0).fullContent.contains("Abu Bakr (RA) narrated"));

        // Card 2: Rak'ahs
        assertEquals("Rak'ahs and Method of Prayer", items.get(1).title);
        assertTrue("Card 2 should contain Abu Darda RA", items.get(1).fullContent.contains("Abu Darda (RA)"));

        // Card 3: Dua
        assertEquals("Special Dua for Repentance", items.get(2).title);
        assertTrue("Card 3 should contain Arabic dua", items.get(2).fullContent.contains("أَسْتَغْفِرُ اللَّهَ"));
        assertTrue("Card 3 should contain English translation", items.get(2).fullContent.contains("I seek the forgiveness of Allah"));

        // Card 4: Sayyidul Istighfar
        assertEquals("Sayyidul Istighfar (The Master Supplication)", items.get(3).title);
        assertTrue("Card 4 should contain Arabic dua", items.get(3).fullContent.contains("اَللَّهُمَّ أَنْتَ"));
        assertTrue("Card 4 should contain English translation", items.get(3).fullContent.contains("You are my Lord"));
    }
}
