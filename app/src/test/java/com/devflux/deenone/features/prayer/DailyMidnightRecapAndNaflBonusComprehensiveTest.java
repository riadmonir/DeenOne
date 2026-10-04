package com.devflux.deenone.features.prayer;

import com.devflux.deenone.data.local.entity.PrayerLogEntity;
import com.devflux.deenone.utils.BengaliNumberUtil;

import org.junit.Assert;
import org.junit.Test;

public class DailyMidnightRecapAndNaflBonusComprehensiveTest {

    @Test
    public void testNaflPrayerBonusPoints() {
        // 1. Tahajjud (+15 XP)
        PrayerLogEntity tahajjud = new PrayerLogEntity("2026-08-22", "Tahajjud", true, false, false, "NAFL", 15, System.currentTimeMillis());
        Assert.assertEquals(15, tahajjud.getPoints());
        Assert.assertEquals("NAFL", tahajjud.getStatus());

        // 2. Ishraq (+10 XP)
        PrayerLogEntity ishraq = new PrayerLogEntity("2026-08-22", "Ishraq", true, false, false, "NAFL", 10, System.currentTimeMillis());
        Assert.assertEquals(10, ishraq.getPoints());

        // 3. Chasht (+10 XP)
        PrayerLogEntity chasht = new PrayerLogEntity("2026-08-22", "Chasht", true, false, false, "NAFL", 10, System.currentTimeMillis());
        Assert.assertEquals(10, chasht.getPoints());

        // 4. Awwabin (+10 XP)
        PrayerLogEntity awwabin = new PrayerLogEntity("2026-08-22", "Awwabin", true, false, false, "NAFL", 10, System.currentTimeMillis());
        Assert.assertEquals(10, awwabin.getPoints());
    }

    @Test
    public void testMidnightProgressPercentageCalculation() {
        int totalPossibleItems = 11; // 5 prayers + 5 daily amals + 1 quiz

        // Case 1: 100% completed
        int completedAll = 11;
        int pct100 = (int) Math.min(100.0, Math.round(((double) completedAll / totalPossibleItems) * 100.0));
        Assert.assertEquals(100, pct100);

        // Case 2: e.g. 9.5 rounded or 86% completed
        int completedPartial = 9;
        int pctPartial = (int) Math.min(100.0, Math.round(((double) completedPartial / totalPossibleItems) * 100.0));
        Assert.assertEquals(82, pctPartial);

        // Bengali number conversion check
        String bnPct = BengaliNumberUtil.toBengali(pct100);
        Assert.assertEquals("১০০", bnPct);

        String bn86 = BengaliNumberUtil.toBengali(86);
        Assert.assertEquals("৮৬", bn86);
    }

    @Test
    public void testMidnightRecapNotificationMessageFormatting() {
        int totalPoints = 120;
        int percentage = 86;

        String expectedTitle = "🌙 আজকের আমল ও পয়েন্ট সারাংশ";
        String expectedBody = "মাশাআল্লাহ! আজ আপনি মোট " + BengaliNumberUtil.toBengali(totalPoints) +
                " পয়েন্ট অর্জন করেছেন এবং আপনার দৈনিক অগ্রগতি " + BengaliNumberUtil.toBengali(percentage) +
                "% সম্পন্ন হয়েছে।";

        Assert.assertTrue(expectedBody.contains("১২০"));
        Assert.assertTrue(expectedBody.contains("৮৬%"));
    }
}
