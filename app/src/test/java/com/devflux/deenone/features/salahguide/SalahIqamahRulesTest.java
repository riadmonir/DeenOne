package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahIqamahRulesTest {

    @Test
    public void testBengaliStepItems() {
        List<SalahIqamahRulesPageDialog.StepItem> items = SalahIqamahRulesPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals(6, items.size());

        SalahIqamahRulesPageDialog.StepItem item1 = items.get(0);
        assertEquals("ইকামত", item1.title);
        assertTrue(item1.fullContent.contains("যেমন দুই মুআযযিনের আযান দুই রকম ছিল"));
        assertTrue(item1.fullContent.contains("বিলাল (রাঃ) কে আযান ডবল ডবল শব্দে"));
        assertTrue(item1.fullContent.contains("[বুখারী, মুসলিম, সহীহ প্রমুখ, মিশকাত ৬৪১ নং]"));
        assertTrue(item1.fullContent.contains("আলমুমতে, শারহে ফিক্হ, ইবনে উষাইমীন ২/৫৯"));
        assertTrue(item1.fullContent.contains("اَللهُ أَكْبَر اَللهُ أَكْبَر، أَشْهَدُ أَنْ لاَّ إِلهَ إِلاَّ الله"));
        assertTrue(item1.fullContent.contains("ক্বাদ ক্বামাতিস স্বলাহ্ (অর্থাৎ নামায প্রতিষ্ঠা বা শুরু হল) ২ বার"));
        assertTrue(item1.fullContent.contains("আবূ মাহ্যূরার মত তারজী’ আযান"));
        assertTrue(item1.fullContent.contains("ইমাম ইবনে তাইমিয়্যাহ্ (রহঃ) বলেন"));
        assertTrue(item1.fullContent.contains("মাজমূউ ফাতাওয়া ২২/৩৩৫"));
        assertTrue(item1.fullContent.contains("ভুলে ইকামত না দিয়ে (একাকী অথবা জামাআতী) নামায পড়ে ফেললে"));
        assertTrue(item1.fullContent.contains("তুহ্ফাতুল ইখওয়ান, ইবনে বায ৭৮পৃ:"));

        SalahIqamahRulesPageDialog.StepItem item2 = items.get(1);
        assertEquals("বিলাল (রাঃ)-এর ইকামত ও বাক্য সংখ্যা", item2.title);
        assertTrue(item2.fullContent.contains("ইকামত হবে ৯টি বাক্যে"));

        SalahIqamahRulesPageDialog.StepItem item3 = items.get(2);
        assertEquals("প্রসিদ্ধ ইকামত ও বাক্য বিন্যাস", item3.title);
        assertTrue(item3.fullContent.contains("আব্দুল্লাহ বিন যায়দকে স্বপ্নে শিখানো হয়েছিল"));

        SalahIqamahRulesPageDialog.StepItem item4 = items.get(3);
        assertEquals("আবূ মাহ্যূরাহ (রাঃ)-এর ১৭ বাক্যের ইকামত", item4.title);
        assertTrue(item4.fullContent.contains("ইকামতের ১৭টি বাক্য"));

        SalahIqamahRulesPageDialog.StepItem item5 = items.get(4);
        assertEquals("ইমাম ইবনে তাইমিয়্যাহ্ (রহঃ)-এর ফাতওয়া ও আমলযোগ্যতা", item5.title);
        assertTrue(item5.fullContent.contains("উভয় প্রকারই আযান ও ইকামত আমলযোগ্য"));

        SalahIqamahRulesPageDialog.StepItem item6 = items.get(5);
        assertEquals("ভুলে ইকামত না দিয়ে নামায পড়ার বিধান ও সহু সিজদা", item6.title);
        assertTrue(item6.fullContent.contains("ঐ ভুলের জন্য সহু সিজদা বিধেয় নয়"));
    }

    @Test
    public void testEnglishStepItems() {
        List<SalahIqamahRulesPageDialog.StepItem> items = SalahIqamahRulesPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals(6, items.size());

        SalahIqamahRulesPageDialog.StepItem item1 = items.get(0);
        assertEquals("Rules of Iqamah", item1.title);
        assertTrue(item1.fullContent.contains("Just as the Adhan of the two Mu'adhins differed"));
        assertTrue(item1.fullContent.contains("[Sahih Bukhari, Sahih Muslim, Mishkat 641]"));
        assertTrue(item1.fullContent.contains("Abdullah ibn Zayd"));
        assertTrue(item1.fullContent.contains("Imam Ibn Taymiyyah"));
        assertTrue(item1.fullContent.contains("Sajdah Sahw is not required"));
    }
}
