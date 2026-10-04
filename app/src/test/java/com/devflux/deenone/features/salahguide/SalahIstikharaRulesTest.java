package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahIstikharaRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahIstikharaRulesPageDialog.StepItem> items = SalahIstikharaRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 8 items for Istikhara prayer in Bengali", 8, items.size());

        // Card 1: Significance
        assertEquals("ইস্তেখারার সালাতের গুরুত্ব ও তাৎপর্য", items.get(0).title);
        assertTrue("Card 1 should contain definition", items.get(0).fullContent.contains("ইস্তেখারা শব্দের অর্থ হল আল্লাহর কাছে ভালোমন্দের জ্ঞান প্রার্থনা করা"));

        // Card 2: Niyyah
        assertEquals("১. নিয়ত (ইচ্ছা)", items.get(1).title);
        assertTrue("Card 2 should contain niyyah text", items.get(1).fullContent.contains("আমি ইস্তেখারার দুই রাকাত সালাত আদায় করছি আল্লাহর উদ্দেশ্যে"));

        // Card 3: Takbir
        assertEquals("২. প্রথম তাকবির (আল্লাহু আকবার বলা)", items.get(2).title);
        assertTrue("Card 3 should contain takbir", items.get(2).fullContent.contains("اللَّهُ أَكْبَرُ"));

        // Card 4: Qiyam
        assertEquals("৩. কিয়াম (দাঁড়ানো)", items.get(3).title);
        assertTrue("Card 4 should mention Surah Fatihah", items.get(3).fullContent.contains("সূরা ফাতিহা"));

        // Card 5: Ruku
        assertEquals("৪. রুকু (ঝুঁকে থাকা)", items.get(4).title);
        assertTrue("Card 5 should contain ruku tasbeeh", items.get(4).fullContent.contains("سُبْحَانَ رَبِّيَ الْعَظِيمِ"));

        // Card 6: Sujood
        assertEquals("৫. সিজদা", items.get(5).title);
        assertTrue("Card 6 should contain sujood tasbeeh", items.get(5).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 7: Two Rak'ahs
        assertEquals("৬. দুই রাকাত নামাজ আদায়", items.get(6).title);
        assertTrue("Card 7 should mention dua after 2 rakats", items.get(6).fullContent.contains("দুই রাকাত নামাজ আদায়ের পরে ইস্তেখারার দু'আ পড়ুন"));

        // Card 8: Dua of Istikhara
        assertEquals("৭. ইস্তেখারার দু'আ", items.get(7).title);
        assertTrue("Card 8 should contain full Arabic Dua", items.get(7).fullContent.contains("اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ"));
        assertTrue("Card 8 should contain pronunciation", items.get(7).fullContent.contains("আল্লাহুম্মা ইন্নি আস্তাখিরুকা"));
        assertTrue("Card 8 should contain translation", items.get(7).fullContent.contains("হে আল্লাহ! আমি আপনার জ্ঞান দ্বারা আপনার কাছে ভালোর জন্য প্রার্থনা করছি"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahIstikharaRulesPageDialog.StepItem> items = SalahIstikharaRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 8 items for Istikhara prayer in English", 8, items.size());

        // Card 1: Significance
        assertEquals("Significance of Salat al-Istikhara", items.get(0).title);
        assertTrue("Card 1 should contain English intro", items.get(0).fullContent.contains("The word 'Istikhara' means seeking the knowledge of good and bad"));

        // Card 2: Niyyah
        assertEquals("1. Niyyah (Intention)", items.get(1).title);
        assertTrue("Card 2 should contain Niyyah example", items.get(1).fullContent.contains("I am performing two Rak'ahs of Salat al-Istikhara"));

        // Card 3: Takbir
        assertEquals("2. First Takbir (Saying Allahu Akbar)", items.get(2).title);
        assertTrue("Card 3 should contain Takbir", items.get(2).fullContent.contains("Allahu Akbar"));

        // Card 4: Qiyam
        assertEquals("3. Qiyam (Standing)", items.get(3).title);
        assertTrue("Card 4 should contain Qiyam instructions", items.get(3).fullContent.contains("Surah Al-Fatihah"));

        // Card 5: Ruku
        assertEquals("4. Ruku (Bowing)", items.get(4).title);
        assertTrue("Card 5 should contain Ruku tasbih", items.get(4).fullContent.contains("Subhana Rabbiyal Azeem"));

        // Card 6: Sujood
        assertEquals("5. Sujood (Prostration)", items.get(5).title);
        assertTrue("Card 6 should contain Sujood tasbih", items.get(5).fullContent.contains("Subhana Rabbiyal"));
        assertTrue("Card 6 should contain Arabic Sujood tasbih", items.get(5).fullContent.contains("سُبْحَانَ رَبِّيَ الْأَعْلَى"));

        // Card 7: Two Rak'ahs
        assertEquals("6. Performing Two Rak'ahs", items.get(6).title);
        assertTrue("Card 7 should mention Dua after 2 rakats", items.get(6).fullContent.contains("recite the Dua of Istikhara"));

        // Card 8: Dua of Istikhara
        assertEquals("7. Dua of Istikhara", items.get(7).title);
        assertTrue("Card 8 should contain Arabic Dua", items.get(7).fullContent.contains("اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ"));
        assertTrue("Card 8 should contain English translation", items.get(7).fullContent.contains("O Allah! I seek Your guidance through Your knowledge"));
    }
}
