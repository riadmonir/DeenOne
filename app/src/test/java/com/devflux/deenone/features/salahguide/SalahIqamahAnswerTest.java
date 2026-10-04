package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahIqamahAnswerTest {

    @Test
    public void testBengaliStepItems() {
        List<SalahIqamahAnswerPageDialog.StepItem> items = SalahIqamahAnswerPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals(5, items.size());

        SalahIqamahAnswerPageDialog.StepItem item1 = items.get(0);
        assertEquals("ইকামতের জওয়াব", item1.title);
        assertTrue(item1.fullContent.contains("ইকামতকে দ্বিতীয় আযান বলা হয়"));
        assertTrue(item1.fullContent.contains("ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৪৯"));
        assertTrue(item1.fullContent.contains("লাহাউলা অলা ক্বুওয়াতা ইল্লা বিল্লাহ্"));
        assertTrue(item1.fullContent.contains("দরুদ ও অসীলার দুআ"));
        assertTrue(item1.fullContent.contains("[মুসলিম, মিশকাত ৬৫৭নং]"));
        assertTrue(item1.fullContent.contains("ক্বাদ ক্বামাতিস স্বলাহ্ এর জওয়াবে ‘ক্বাদ ক্বামাতিস স্বলাহ্’ই বলতে হবে"));
        assertTrue(item1.fullContent.contains("আক্বামাহুল্লাহু অআদামাহা"));
        assertTrue(item1.fullContent.contains("যয়ীফ হাদীসকে ভিত্তি করে শরীয়তের কোন আমল ও ইবাদত বৈধ নয়"));
        assertTrue(item1.fullContent.contains("[মিশকাত, আলবানীর টীকা ১/১২১]"));
        assertTrue(item1.fullContent.contains("ইকামতের জবাব দেওয়া সুন্নত নয়"));

        SalahIqamahAnswerPageDialog.StepItem item2 = items.get(1);
        assertEquals("ইকামত দ্বিতীয় আযান ও জওয়াবের বিধান", item2.title);
        assertTrue(item2.fullContent.contains("ইকামতও এক প্রকার আযান"));

        SalahIqamahAnswerPageDialog.StepItem item3 = items.get(2);
        assertEquals("ইকামতের শব্দের জওয়াব ও দরুদ-অসীলাহ্", item3.title);
        assertTrue(item3.fullContent.contains("মুআযযিনের জওয়াব (তার মতই) বলতে"));

        SalahIqamahAnswerPageDialog.StepItem item4 = items.get(3);
        assertEquals("‘ক্বাদ ক্বামাতিস স্বলাহ্’-এর জওয়াব ও যয়ীফ হাদীস বর্জন", item4.title);
        assertTrue(item4.fullContent.contains("আক্বামাহুল্লাহু অআদামাহা’ বলার হাদীস শুদ্ধ নয়"));

        SalahIqamahAnswerPageDialog.StepItem item5 = items.get(4);
        assertEquals("ইকামতের জওয়াব সংক্রান্ত ভিন্ন মত", item5.title);
        assertTrue(item5.fullContent.contains("ইকামতের জবাব দেওয়া সুন্নত নয়"));
    }

    @Test
    public void testEnglishStepItems() {
        List<SalahIqamahAnswerPageDialog.StepItem> items = SalahIqamahAnswerPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals(5, items.size());

        SalahIqamahAnswerPageDialog.StepItem item1 = items.get(0);
        assertEquals("Responding to Iqamah", item1.title);
        assertTrue(item1.fullContent.contains("Iqamah is called the second Adhan"));
        assertTrue(item1.fullContent.contains("[Sahih Muslim, Mishkat 657]"));
        assertTrue(item1.fullContent.contains("Aqamahallahu wa adamaha"));
        assertTrue(item1.fullContent.contains("Albani"));
    }
}
