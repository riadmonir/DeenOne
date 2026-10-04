package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahAzanDuaTest {

    @Test
    public void testBengaliStepItems() {
        List<SalahAzanDuaPageDialog.StepItem> items = SalahAzanDuaPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals(4, items.size());

        SalahAzanDuaPageDialog.StepItem item1 = items.get(0);
        assertEquals("আযান ও ইকামতের মাঝে দুআ", item1.title);
        assertTrue(item1.fullContent.contains("আযান হওয়ার পর এবং ইকামত হওয়ার পূর্বের সময়ে দুআ কবুল হয়ে থাকে"));
        assertTrue(item1.fullContent.contains("আযান ও ইকামতের মাঝে দুআ রদ্দ্ করা হয় না"));
        assertTrue(item1.fullContent.contains("[আহমাদ, মুসনাদ, আবূদাঊদ, সুনান ৫২১নং, তিরমিযী, সুনান]"));
        assertTrue(item1.fullContent.contains("সুতরাং তোমরা এ সময়ে দুআ কর"));
        assertTrue(item1.fullContent.contains("[জামে ৩৪০৫ নং]"));
        assertTrue(item1.fullContent.contains("দু’টি সময়ে দুআ (প্রার্থনা)কারীর দুআ রদ্দ্ হয় না"));
        assertTrue(item1.fullContent.contains("[হাকেম, মুস্তাদরাক, মালেক, মুঅত্তা, সহিহ তারগিব ২৬০ নং]"));

        SalahAzanDuaPageDialog.StepItem item2 = items.get(1);
        assertEquals("আযান ও ইকামতের মাঝে দুআ কবুল হওয়ার হাদীস", item2.title);
        assertTrue(item2.fullContent.contains("আযান ও ইকামতের মাঝে দুআ রদ্দ্ করা হয় না"));

        SalahAzanDuaPageDialog.StepItem item3 = items.get(2);
        assertEquals("এ সময়ে দুআ করার বিশেষ নির্দেশ", item3.title);
        assertTrue(item3.fullContent.contains("সুতরাং তোমরা এ সময়ে দুআ কর"));

        SalahAzanDuaPageDialog.StepItem item4 = items.get(3);
        assertEquals("দুআ রদ্দ্ না হওয়ার দুটি বিশেষ মুহূর্ত", item4.title);
        assertTrue(item4.fullContent.contains("যখন নামাযের ইকামত হয় এবং জিহাদের কাতারে"));
    }

    @Test
    public void testEnglishStepItems() {
        List<SalahAzanDuaPageDialog.StepItem> items = SalahAzanDuaPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals(4, items.size());

        SalahAzanDuaPageDialog.StepItem item1 = items.get(0);
        assertEquals("Dua Between Adhan and Iqamah", item1.title);
        assertTrue(item1.fullContent.contains("Supplication made between the Adhan and the Iqamah is not rejected"));
        assertTrue(item1.fullContent.contains("[Musnad Ahmad, Sunan Abi Dawud 521, Jami at-Tirmidhi]"));
        assertTrue(item1.fullContent.contains("So supplicate during this time"));
        assertTrue(item1.fullContent.contains("[Al-Jami' 3405]"));
        assertTrue(item1.fullContent.contains("battlefield row of Jihad"));
        assertTrue(item1.fullContent.contains("[Mustadrak al-Hakim, Muwatta Malik, Sahih at-Targhib 260]"));
    }
}
