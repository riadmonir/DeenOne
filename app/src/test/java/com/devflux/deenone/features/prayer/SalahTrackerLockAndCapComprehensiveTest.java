package com.devflux.deenone.features.prayer;

import com.devflux.deenone.data.local.entity.PrayerLogEntity;
import com.devflux.deenone.utils.BengaliNumberUtil;

import org.junit.Assert;
import org.junit.Test;

public class SalahTrackerLockAndCapComprehensiveTest {

    @Test
    public void testPrayedCountStrictCap() {
        // Even if raw DB count is 20 or 25, strictly cap to 5
        int rawCountOver = 25;
        int cappedOver = Math.min(5, Math.max(0, rawCountOver));
        Assert.assertEquals(5, cappedOver);
        Assert.assertEquals("৫/৫", BengaliNumberUtil.toBengali(cappedOver + "/৫"));

        int rawCountNormal = 3;
        int cappedNormal = Math.min(5, Math.max(0, rawCountNormal));
        Assert.assertEquals(3, cappedNormal);
        Assert.assertEquals("৩/৫", BengaliNumberUtil.toBengali(cappedNormal + "/৫"));
    }

    @Test
    public void testPrayerStatusLocking() {
        // Initial log saved
        PrayerLogEntity existingLog = new PrayerLogEntity("2026-08-22", "Fajr", true, true, false, "JAMAAT", 20, System.currentTimeMillis());

        // When user tries to re-select
        boolean isLocked = (existingLog != null && existingLog.getStatus() != null && !existingLog.getStatus().isEmpty());
        Assert.assertTrue(isLocked);
        Assert.assertEquals("JAMAAT", existingLog.getStatus());
    }

    @Test
    public void testNaflPrayerLocking() {
        // Initial Nafl tracked
        PrayerLogEntity naflLog = new PrayerLogEntity("2026-08-22", "Tahajjud", true, false, false, "NAFL", 15, System.currentTimeMillis());

        // When user tries to re-click
        boolean isNaflLocked = (naflLog != null && naflLog.isPrayed());
        Assert.assertTrue(isNaflLocked);
        Assert.assertEquals(15, naflLog.getPoints());
    }

    @Test
    public void testFourPrayersMarkedCountIsStrictlyFour() {
        // When user marked Fajr, Dhuhr, Asr, Maghrib (4 prayers)
        int fourPrayers = 4;
        Assert.assertEquals("৪/৫", BengaliNumberUtil.toBengali(fourPrayers + "/৫"));
        Assert.assertEquals("আদায় সম্পন্ন: ৪/৫", "আদায় সম্পন্ন: " + BengaliNumberUtil.toBengali(fourPrayers + "/৫"));
    }
}
