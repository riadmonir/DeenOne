package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahJumuahRulesTest {

    @Test
    public void testBengaliJumuahRules() {
        List<SalahJumuahRulesPageDialog.JumuahRuleItem> items = SalahJumuahRulesPageDialog.getJumuahItems(true);
        assertNotNull(items);
        assertEquals("Must contain 4 Jumuah rule cards", 4, items.size());

        // Card 1: হুকুম ও বিধান
        assertEquals("Card 1 title must match", "জুম'আর সালাতের হুকুম ও বিধান", items.get(0).title);
        assertTrue("Card 1 must contain Fard Ayn", items.get(0).fullContent.contains("ফরযে আয়েন"));
        assertTrue("Card 1 must contain Caliph Umar RA Bahrain decree", items.get(0).fullContent.contains("খলীফা ওমর (রাঃ) বলেন,‘তোমরা যেখানেই থাক, জুম'আ আদায় কর'"));
        assertTrue("Card 1 must contain Surah Taghabun reference", items.get(0).fullContent.contains("সূরা তাগাবুন - ৬৪/১৬"));

        // Card 2: প্রস্তুতি ও আদায় পদ্ধতি
        assertEquals("Card 2 title must match", "জুম'আর সালাতের প্রস্তুতি ও আদায়ের পদ্ধতি", items.get(1).title);
        assertTrue("Card 2 must contain Ghusl & early arrival", items.get(1).fullContent.contains("জুম'আর দিন সুন্দরভাবে গোসল করে সাধ্যমত উত্তম পোষাক ও সুগন্ধি লাগিয়ে আগেভাগে মসজিদে যেতে হবে"));
        assertTrue("Card 2 must contain Tahiyyatul Masjid", items.get(1).fullContent.contains("তাহিইয়াতুল মাসজিদ"));
        assertTrue("Card 2 must contain Surah Jumuah or A'la", items.get(1).fullContent.contains("সূরায়ে ‘জুম'আ’ অথবা সূরায়ে ‘আ'লা'"));
        assertTrue("Card 2 must contain Abu Dawud reference", items.get(1).fullContent.contains("আবুদাঊদ - ৮১৮, ৮২০, ৮৫৯"));

        // Card 3: রাকাত না পেলে
        assertEquals("Card 3 title must match", "জুম'আর রাক'আত না পেলে কিভাবে সালাত পড়েবেন/পদ্ধতি", items.get(2).title);
        assertTrue("Card 3 must contain 1 rak'ah catch rule", items.get(2).fullContent.contains("জুম'আর সালাত ইমামের সাথে এক রাক'আত পেলে বাকী আরেক রাক'আত যোগ করে পূরা পড়ে নিলে হয়ে যাবে"));
        assertTrue("Card 3 must contain Bayhaqi reference", items.get(2).fullContent.contains("বায়হাক্বী ৩/২০৪"));
        assertTrue("Card 3 must contain Fiqh us-Sunnah reference", items.get(2).fullContent.contains("ফিক্বহুস সুন্নাহ ১/২৩৫"));

        // Card 4: সুন্নাত সালাত
        assertEquals("Card 4 title must match", "জুম'আর সালাতের পরে সুন্নাত সালাতের পদ্ধতি", items.get(3).title);
        assertTrue("Card 4 must contain 4 rak'ahs in mosque", items.get(3).fullContent.contains("চার রাক'আত:"));
        assertTrue("Card 4 must contain Arabic Hadith", items.get(3).fullContent.contains("مَنْ كَانَ مِنْكُمْ مُصَلِّيًا بَعْدَ الْجُمُعَةِ فَلْيُصَلِّ أَرْبَعًا"));
        assertTrue("Card 4 must contain 2 rak'ahs at home", items.get(3).fullContent.contains("বাড়িত গিয়ে দু'রাকা'আত সালাত আদায় করা যায়"));
        assertTrue("Card 4 must contain Tirmidhi reference", items.get(3).fullContent.contains("তিরমিযী হা/৫২২-২৩ ‘জুম'আ অধ্যায়-৪, অনুচ্ছেদ-২৪"));
        assertTrue("Card 4 must contain Mir'at reference", items.get(3).fullContent.contains("মিরআত ৪/২৫৭-৫৮"));

        for (SalahJumuahRulesPageDialog.JumuahRuleItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }

    @Test
    public void testEnglishJumuahRules() {
        List<SalahJumuahRulesPageDialog.JumuahRuleItem> items = SalahJumuahRulesPageDialog.getJumuahItems(false);
        assertNotNull(items);
        assertEquals("Must contain 4 Jumuah rule cards in English", 4, items.size());

        for (SalahJumuahRulesPageDialog.JumuahRuleItem item : items) {
            assertNotNull(item.title);
            assertFalse(item.title.trim().isEmpty());
            assertNotNull(item.previewSubtitle);
            assertFalse(item.previewSubtitle.trim().isEmpty());
            assertNotNull(item.fullContent);
            assertFalse(item.fullContent.trim().isEmpty());
        }
    }
}
