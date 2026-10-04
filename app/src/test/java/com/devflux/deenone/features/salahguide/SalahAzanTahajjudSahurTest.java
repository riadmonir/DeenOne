package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanTahajjudSahurTest {

    @Test
    public void testBengaliStepItems() {
        List<SalahAzanTahajjudSahurPageDialog.StepItem> items = SalahAzanTahajjudSahurPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals(4, items.size());

        SalahAzanTahajjudSahurPageDialog.StepItem item1 = items.get(0);
        assertEquals("তাহাজ্জুদ ও সেহ্রী বা সাহারীর আযান", item1.title);
        assertTrue(item1.fullContent.contains("মহানবী (ﷺ) বলেন, বিলাল রাতে (ফজরের পূর্বে) আযান দেয়।"));
        assertTrue(item1.fullContent.contains("[বুখারী, মুসলিম, মিশকাত ৬৮০নং]"));
        assertTrue(item1.fullContent.contains("সেহ্রীর আযান মহানবী (ﷺ) এর যুগে প্রচলিত ছিল"));
        assertTrue(item1.fullContent.contains("মক্কা-মদ্বীনা সহ্ সঊদী আরবের প্রায় সকল স্থানে"));
        assertTrue(item1.fullContent.contains("আযানের পরিবর্তে শোনা যায় কুরআন ও গজল পাঠ"));
        assertTrue(item1.fullContent.contains("সুন্নতের জায়গা দখল করেছে মনগড়া বিদআত"));
        assertTrue(item1.fullContent.contains("পৃথক পৃথক উভয় সময়ের জন্য নির্দিষ্ট দু’জন মুআযযিন"));
        assertTrue(item1.fullContent.contains("الصَّلاَةُ خَيْرٌ مِّنَ النَّوْم"));
        assertTrue(item1.fullContent.contains("শব্দ থাকবে না"));

        SalahAzanTahajjudSahurPageDialog.StepItem item2 = items.get(1);
        assertEquals("তাহাজ্জুদ ও সেহ্রীর আযানের হাদীস", item2.title);
        assertTrue(item2.fullContent.contains("ইবনে উম্মে মাকতূম (ফজরের) আযান না দেওয়া পর্যন্ত"));

        SalahAzanTahajjudSahurPageDialog.StepItem item3 = items.get(2);
        assertEquals("সুন্নাত প্রমাণ ও প্রচলিত বিদআত বর্জন", item3.title);
        assertTrue(item3.fullContent.contains("সুন্নতের জায়গা দখল করেছে মনগড়া বিদআত"));

        SalahAzanTahajjudSahurPageDialog.StepItem item4 = items.get(3);
        assertEquals("গোলমাল নিরসন ও সেহ্রীর আযানের বৈশিষ্ট্য", item4.title);
        assertTrue(item4.fullContent.contains("الصَّلاَةُ خَيْرٌ مِّنَ النَّوْم"));
    }

    @Test
    public void testEnglishStepItems() {
        List<SalahAzanTahajjudSahurPageDialog.StepItem> items = SalahAzanTahajjudSahurPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals(4, items.size());

        SalahAzanTahajjudSahurPageDialog.StepItem item1 = items.get(0);
        assertEquals("Adhan for Tahajjud and Sahur", item1.title);
        assertTrue(item1.fullContent.contains("Bilal calls the Adhan during the night"));
        assertTrue(item1.fullContent.contains("[Sahih Bukhari, Sahih Muslim, Mishkat 680]"));
        assertTrue(item1.fullContent.contains("Makkah and Madinah"));
        assertTrue(item1.fullContent.contains("الصَّلاَةُ خَيْرٌ مِّنَ النَّوْم"));
    }
}
