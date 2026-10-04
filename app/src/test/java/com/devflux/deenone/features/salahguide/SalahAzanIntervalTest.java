package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanIntervalTest {

    @Test
    public void testBengaliStepItems() {
        List<SalahAzanIntervalPageDialog.StepItem> items = SalahAzanIntervalPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals(4, items.size());

        SalahAzanIntervalPageDialog.StepItem item1 = items.get(0);
        assertEquals("আযান ও ইকামতের মাঝে ব্যবধান", item1.title);
        assertTrue(item1.fullContent.contains("আযান ও ইকামতের মাঝে কতটা বিরতি থাকবে"));
        assertTrue(item1.fullContent.contains("সুন্নাতে রাতেবাহ্ বা মুআক্কাদাহ"));
        assertTrue(item1.fullContent.contains("প্রত্যেক আযান ও ইকামতের মাঝে নামায আছে"));
        assertTrue(item1.fullContent.contains("[বুখারী, মুসলিম, মিশকাত ৬৬২নং]"));
        assertTrue(item1.fullContent.contains("মাগরেবের আযানের পরেও সত্বর জামাআত শুরু করা উচিৎ নয়"));
        assertTrue(item1.fullContent.contains("মসজিদের খাম্বাগুলোর পশ্চাতে ২ রাকআত নামায"));
        assertTrue(item1.fullContent.contains("[মুসলিম, মিশকাত ১১৮০ নং]"));

        SalahAzanIntervalPageDialog.StepItem item2 = items.get(1);
        assertEquals("আযান ও ইকামতের বিরতির সাধারণ নীতিমালা ও হাদীস", item2.title);
        assertTrue(item2.fullContent.contains("যে চাইবে তার জন্য"));

        SalahAzanIntervalPageDialog.StepItem item3 = items.get(2);
        assertEquals("মাগরিবের আযানের পর জামাআত শুরু করার বিধান", item3.title);
        assertTrue(item3.fullContent.contains("যদিও সময় সংকীর্ণ তবুও জামাআত হওয়ার পূর্বে নামায আছে"));

        SalahAzanIntervalPageDialog.StepItem item4 = items.get(3);
        assertEquals("মাগরিবের আযানের পর সাহাবীদের দুই রাকআত নামাযের আমল", item4.title);
        assertTrue(item4.fullContent.contains("আনাস (রাঃ) বলেন, আমরা মদ্বীনায় ছিলাম"));
        assertTrue(item4.fullContent.contains("[মুসলিম, মিশকাত ১১৮০ নং]"));
    }

    @Test
    public void testEnglishStepItems() {
        List<SalahAzanIntervalPageDialog.StepItem> items = SalahAzanIntervalPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals(4, items.size());

        SalahAzanIntervalPageDialog.StepItem item1 = items.get(0);
        assertEquals("Interval Between Adhan and Iqamah", item1.title);
        assertTrue(item1.fullContent.contains("Between every two calls"));
        assertTrue(item1.fullContent.contains("[Sahih Bukhari, Sahih Muslim, Mishkat 662]"));
        assertTrue(item1.fullContent.contains("pillars of the mosque to pray two Rak'ahs"));
        assertTrue(item1.fullContent.contains("[Sahih Muslim, Mishkat 1180]"));
    }
}
