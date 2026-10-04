package com.devflux.deenone.features.salahguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class SalahIqamahPrayerIntervalTest {

    @Test
    public void testBengaliStepItems() {
        List<SalahIqamahPrayerIntervalPageDialog.StepItem> items = SalahIqamahPrayerIntervalPageDialog.getStepItems(true);
        assertNotNull(items);
        assertEquals(5, items.size());

        SalahIqamahPrayerIntervalPageDialog.StepItem item1 = items.get(0);
        assertEquals("ইকামত ও নামায শুরু করার মাঝে ব্যবধান", item1.title);
        assertTrue(item1.fullContent.contains("নামাযের ইকামত হয়ে গেলে তোমরা আমাকে না দেখা পর্যন্ত (নামাযের জন্য) দাঁড়াও না।"));
        assertTrue(item1.fullContent.contains("[বুখারী ৬৩৭নং]"));
        assertTrue(item1.fullContent.contains("তোমাদের মাঝে যেন ধীরতা ও শান্তভাব থাকে"));
        assertTrue(item1.fullContent.contains("রাজাধিরাজের দরবারে কোন প্রকারের হৈ-হুল্লোড় ও তাড়াহুড়ো চলে না"));
        assertTrue(item1.fullContent.contains("হুমাইদ বলেন, আমি সাবেত আল-বুনানীকে ইকামতের পর কথাবার্তা বলার বৈধতার ব্যাপারে প্রশ্ন করলে"));
        assertTrue(item1.fullContent.contains("[বুখারী ৬৪৩নং]"));
        assertTrue(item1.fullContent.contains("তোমরা স্বস্থানে দন্ডায়মান থাক"));
        assertTrue(item1.fullContent.contains("নাপাকীর গোসল করেননি"));
        assertTrue(item1.fullContent.contains("প্রয়োজনে ইকামত ও নামাযের মাঝে বেশ কিছু সময় বিরতি হলে কোন ক্ষতি হয় না"));
        assertTrue(item1.fullContent.contains("[ফাতাওয়া ইসলামিয়্যাহ্, সঊদী উলামা-কমিটি ১/২৫১]"));
        assertTrue(item1.fullContent.contains("ইকামত শুরু হলে এবং ইমাম উপস্থিত থাকলে প্রত্যেকে নিজের সুবিধামত উঠে নামাযের জন্য দন্ডায়মান হবে"));
        assertTrue(item1.fullContent.contains("[আলমুমতে শারহে ফিক্হ, ইবনে উষাইমীন ৩/১০]"));

        SalahIqamahPrayerIntervalPageDialog.StepItem item2 = items.get(1);
        assertEquals("ইমামকে না দেখে না দাঁড়ানো ও ধীরস্থিরতা বজায় রাখা", item2.title);
        assertTrue(item2.fullContent.contains("নামাযের ইকামত হয়ে গেলে তোমরা আমাকে না দেখা পর্যন্ত"));

        SalahIqamahPrayerIntervalPageDialog.StepItem item3 = items.get(2);
        assertEquals("ইকামতের পর কথাবার্তা ও রাসুলুল্লাহর (ﷺ) গোসলের ঘটনা", item3.title);
        assertTrue(item3.fullContent.contains("এক ব্যক্তি নবী (ﷺ) কে নামাযে প্রবেশ করতে আটকে রেখেছিল"));

        SalahIqamahPrayerIntervalPageDialog.StepItem item4 = items.get(3);
        assertEquals("ইকামতের পর বিরতি ও জরুরী কথা বলার বিধান", item4.title);
        assertTrue(item4.fullContent.contains("ইকামত হওয়ার পর কোন জরুরী কথা, নামায ও কাতার বিষয়ক কথা বলা বৈধ"));

        SalahIqamahPrayerIntervalPageDialog.StepItem item5 = items.get(4);
        assertEquals("ইকামতের সময় নামাযের জন্য দাঁড়ানোর সঠিক মুহূর্ত", item5.title);
        assertTrue(item5.fullContent.contains("ইকামতের শুরুতে, মাঝে বা শেষে, যে কোন সময়ে দাঁড়ালেই চলবে"));
    }

    @Test
    public void testEnglishStepItems() {
        List<SalahIqamahPrayerIntervalPageDialog.StepItem> items = SalahIqamahPrayerIntervalPageDialog.getStepItems(false);
        assertNotNull(items);
        assertEquals(5, items.size());

        SalahIqamahPrayerIntervalPageDialog.StepItem item1 = items.get(0);
        assertEquals("Interval Between Iqamah and Starting Prayer", item1.title);
        assertTrue(item1.fullContent.contains("When the Iqamah for prayer is called, do not stand"));
        assertTrue(item1.fullContent.contains("[Sahih Bukhari 637]"));
        assertTrue(item1.fullContent.contains("[Sahih Bukhari 643]"));
        assertTrue(item1.fullContent.contains("Ghusl"));
        assertTrue(item1.fullContent.contains("[Fatawa Islamiyyah, Saudi Scholars Committee 1/251]"));
        assertTrue(item1.fullContent.contains("[Al-Mumti', Sharh al-Fiqh, Ibn Uthaymeen 3/10]"));
    }
}
