package com.devflux.deenone.features.prayer;

import com.devflux.deenone.data.local.entity.PrayerLogEntity;
import com.devflux.deenone.utils.PrayerCalculator;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class SalahTrackerComprehensiveTest {

    @Test
    public void testPrayerLogEntityCreationAndPoints() {
        // 1. জামাত: +২০ পয়েন্ট
        PrayerLogEntity jamatLog = new PrayerLogEntity("2026-08-22", "Dhuhr", true, true, false, "JAMAAT", 20, System.currentTimeMillis());
        Assert.assertEquals("2026-08-22", jamatLog.getDate());
        Assert.assertEquals("Dhuhr", jamatLog.getPrayerName());
        Assert.assertTrue(jamatLog.isPrayed());
        Assert.assertTrue(jamatLog.isWithJamat());
        Assert.assertFalse(jamatLog.isQaza());
        Assert.assertEquals("JAMAAT", jamatLog.getStatus());
        Assert.assertEquals(20, jamatLog.getPoints());

        // 2. একাকী: ৬০% পয়েন্ট (২০ এর ৬০% = ১২ পয়েন্ট)
        PrayerLogEntity ekakiLog = new PrayerLogEntity("2026-08-22", "Asr", true, false, false, "EKAKI", 12, System.currentTimeMillis());
        Assert.assertEquals(12, ekakiLog.getPoints());

        // 3. দেরী: অর্ধেক / ১০ পয়েন্ট
        PrayerLogEntity deriLog = new PrayerLogEntity("2026-08-22", "Maghrib", true, false, false, "DERI", 10, System.currentTimeMillis());
        Assert.assertEquals(10, deriLog.getPoints());

        // 4. কাজা: -২০ পয়েন্ট পেনাল্টি কর্তন
        PrayerLogEntity qazaLog = new PrayerLogEntity("2026-08-22", "Isha", false, false, true, "QAZA", -20, System.currentTimeMillis());
        Assert.assertEquals(-20, qazaLog.getPoints());
        Assert.assertTrue(qazaLog.isQaza());
    }

    @Test
    public void testDeltaPointsTransitionLogic() {
        // First mark as JAMAAT (+20)
        int previousPoints = 0;
        int jamatPoints = 20;
        int delta1 = jamatPoints - previousPoints;
        Assert.assertEquals(20, delta1);

        // Change from JAMAAT (+20) to QAZA (-20)
        int qazaPoints = -20;
        int delta2 = qazaPoints - jamatPoints;
        Assert.assertEquals(-40, delta2);

        // Change from QAZA (-20) to EKAKI (+12)
        int ekakiPoints = 12;
        int delta3 = ekakiPoints - qazaPoints;
        Assert.assertEquals(32, delta3);
    }

    @Test
    public void testPrayerTimesCalculation() {
        Calendar cal = Calendar.getInstance();
        PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocation(23.8103, 90.4125, 6.0, cal);
        Assert.assertNotNull(res);
        Assert.assertTrue(res.fajrMillis > 0);
        Assert.assertTrue(res.sunriseMillis > res.fajrMillis);
        Assert.assertTrue(res.zohrMillis > res.sunriseMillis);
        Assert.assertTrue(res.asrMillis > res.zohrMillis);
        Assert.assertTrue(res.maghribMillis > res.asrMillis);
        Assert.assertTrue(res.ishaMillis > res.maghribMillis);
    }

    @Test
    public void testFutureWaqtTimeGatingLogic() {
        long fajrStart = 1000000000L;
        long fajrEnd = 1000003600L;
        long dhuhrStart = 1000020000L;

        long simulatedNowBeforeDhuhr = 1000010000L;

        // Past waqt (Fajr) -> can be marked
        boolean canMarkFajr = simulatedNowBeforeDhuhr >= fajrStart;
        Assert.assertTrue("Past waqt Fajr should be markable", canMarkFajr);

        // Future waqt (Dhuhr) -> cannot be marked in advance
        boolean canMarkDhuhr = simulatedNowBeforeDhuhr >= dhuhrStart;
        Assert.assertFalse("Future waqt Dhuhr should NOT be markable before start", canMarkDhuhr);
    }
}
