package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahHajatRulesTest {

    @Test
    public void testBengaliItemsCompleteness() {
        List<SalahHajatRulesPageDialog.StepItem> items = SalahHajatRulesPageDialog.getStepItems(true);
        assertNotNull("Bengali items list should not be null", items);
        assertEquals("Should have exactly 4 items for Hajat prayer in Bengali", 4, items.size());

        // Card 1: Definition
        assertEquals("সালাতুল হাজতের পরিচিতি ও তাৎপর্য", items.get(0).title);
        assertTrue("Card 1 should contain definition", items.get(0).fullContent.contains("বিশেষ কোন বৈধ চাহিদা পূরণের জন্য আল্লাহর উদ্দেশ্যে যে দু' রাক'আত নফল সালাত আদায় করা হয়"));
        assertTrue("Card 1 should contain Ibn Majah reference", items.get(0).fullContent.contains("ইবনু মাজাহ হা/১৩৮৫, ছালাত অধ্যায়-২ অনুচ্ছেদ-১৮৯"));
        assertTrue("Card 1 should contain Baqarah reference", items.get(0).fullContent.contains("বাক্বারাহ ২/১৫৩"));

        // Card 2: Dua & Hadith
        assertEquals("সালাতুল হাজতে পঠিত সারগর্ভ দো'আ", items.get(1).title);
        assertTrue("Card 2 should contain Arabic dua", items.get(1).fullContent.contains("رَبَّنَاۤ اٰتِنَا فِی الدُّنۡیَا حَسَنَۃً"));
        assertTrue("Card 2 should contain pronunciation", items.get(1).fullContent.contains("রব্বানা আ-তিনা ফিদ্দুন্ইয়া হাসানাতাঁও"));
        assertTrue("Card 2 should contain translation", items.get(1).fullContent.contains("হে আমাদের পালনকর্তা! আপনি আমাদেরকে দুনিয়াতে মঙ্গল দিন"));
        assertTrue("Card 2 should contain Anas RA narration", items.get(1).fullContent.contains("হযরত আনাস (রাঃ) বলেন, রাসূলুল্লাহ (সাঃ) অধিকাংশ সময় এ দো'আটিই পড়তেন।"));
        assertTrue("Card 2 should contain Bukhari reference", items.get(1).fullContent.contains("বুখারী হা/৪৫২২, ৬৩৮৯"));

        // Card 3: Sujud Dua & Hudhayfah RA Hadith
        assertEquals("সিজদায় দো'আ পাঠ ও সংকটে সালাতের বিধান", items.get(2).title);
        assertTrue("Card 3 should contain sujud rule", items.get(2).fullContent.contains("দো'আটি সিজদায় পড়লে দো'আর পূর্বে 'আল্লা-হুম্মা' শব্দটি যুক্ত করতে হবে।"));
        assertTrue("Card 3 should contain reason", items.get(2).fullContent.contains("কেননা রুকু-সিজদায় কুরআনী দো'আ পড়া চলে না।"));
        assertTrue("Card 3 should contain Hudhayfah RA narration", items.get(2).fullContent.contains("রাসূলুল্লাহ (সাঃ) যখন কোন সংকটে পড়তেন, তখন সালাতে রত হতেন"));
        assertTrue("Card 3 should contain Abu Dawood reference", items.get(2).fullContent.contains("আবুদাঊদ হা/১৩১৯ ‘সালাত’ অধ্যায়-২, অনুচ্ছেদ-৩১২"));

        // Card 4: Sarah AS incident
        assertEquals("হযরত সারা (আঃ)-এর সালাতের মাধ্যমে সাহায্য প্রাপ্তির ঘটনা", items.get(3).title);
        assertTrue("Card 4 should contain Sarah AS incident", items.get(3).fullContent.contains("উক্ত বিষয়ে হযরত ইবরাহীম (আঃ)-এর স্ত্রী সারা’র ঘটনা স্মরণ করা যেতে পারে।"));
        assertTrue("Card 4 should contain prayer of Sarah AS", items.get(3).fullContent.contains("হে আল্লাহ! এই কাফেরকে তুমি আমার উপর বিজয়ী করোনা"));
        assertTrue("Card 4 should contain release and Hajar companion", items.get(3).fullContent.contains("বিবি সারা-কে সসম্মানে মুক্তি দেয়"));
        assertTrue("Card 4 should contain Bukhari reference", items.get(3).fullContent.contains("বুখারী হা/২২১৭ ‘ক্রয়-বিক্রয়’ অধ্যায়-৩৪, অনুচ্ছেদ-১০০"));
    }

    @Test
    public void testEnglishItemsCompleteness() {
        List<SalahHajatRulesPageDialog.StepItem> items = SalahHajatRulesPageDialog.getStepItems(false);
        assertNotNull("English items list should not be null", items);
        assertEquals("Should have exactly 4 items for Hajat prayer in English", 4, items.size());

        // Card 1: Definition
        assertEquals("Definition and Significance of Salat al-Hajat", items.get(0).title);
        assertTrue("Card 1 should contain English definition", items.get(0).fullContent.contains("two-Rak'ah voluntary prayer performed sincerely"));
        assertTrue("Card 1 should contain Ibn Majah", items.get(0).fullContent.contains("Sunan Ibn Majah, Hadith 1385"));

        // Card 2: Dua
        assertEquals("Comprehensive Dua in Salat al-Hajat", items.get(1).title);
        assertTrue("Card 2 should contain Arabic dua", items.get(1).fullContent.contains("رَبَّنَاۤ اٰتِنَا"));
        assertTrue("Card 2 should contain English translation", items.get(1).fullContent.contains("Our Lord! Grant us good in this world"));

        // Card 3: Sujud rule
        assertEquals("Reciting Dua in Sujud and Salah during Crisis", items.get(2).title);
        assertTrue("Card 3 should contain Sujud rule", items.get(2).fullContent.contains("word 'Allahumma' should be prefixed"));
        assertTrue("Card 3 should contain Abu Dawud reference", items.get(2).fullContent.contains("Sunan Abi Dawud, Hadith 1319"));

        // Card 4: Sarah AS incident
        assertEquals("Historical Event of Sarah (AS) Seeking Help through Salah", items.get(3).title);
        assertTrue("Card 4 should contain Sarah AS incident", items.get(3).fullContent.contains("historical event of Sarah (AS)"));
        assertTrue("Card 4 should contain Bukhari reference", items.get(3).fullContent.contains("Sahih al-Bukhari, Hadith 2217"));
    }
}
