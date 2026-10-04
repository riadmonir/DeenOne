package com.devflux.deenone.core.prayer;

import com.devflux.deenone.utils.PrayerCalculator;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class SalahTimeAdjustmentAndMazhabTest {

    @Test
    public void testPrayerOffsetsAppliedAccurately() {
        Calendar cal = Calendar.getInstance();
        double lat = 28.3835; // Tabuk, Saudi Arabia
        double lng = 36.5662;
        double tz = 3.0;

        PrayerSettingsManager.CalculationMethod method = PrayerSettingsManager.CalculationMethod.UMM_AL_QURA;
        PrayerSettingsManager.JuristicMethod juristic = PrayerSettingsManager.JuristicMethod.HANAFI;

        // Base times with 0 offsets
        PrayerCalculator.PrayerTimesResult base = PrayerCalculator.calculateForLocationWithSettings(
                lat, lng, tz, cal, method, juristic,
                0, 0, 0, 0, 0, 0
        );

        // Adjusted times: Fajr +5m, Sunrise -3m, Dhuhr +10m, Asr -5m, Maghrib +2m, Isha -15m
        int fajrOffset = 5;
        int sunriseOffset = -3;
        int dhuhrOffset = 10;
        int asrOffset = -5;
        int maghribOffset = 2;
        int ishaOffset = -15;

        PrayerCalculator.PrayerTimesResult adjusted = PrayerCalculator.calculateForLocationWithSettings(
                lat, lng, tz, cal, method, juristic,
                fajrOffset, sunriseOffset, dhuhrOffset, asrOffset, maghribOffset, ishaOffset
        );

        Assert.assertEquals(base.fajrMillis + (5 * 60 * 1000), adjusted.fajrMillis);
        Assert.assertEquals(base.sunriseMillis + (-3 * 60 * 1000), adjusted.sunriseMillis);
        Assert.assertEquals(base.zohrMillis + (10 * 60 * 1000), adjusted.zohrMillis);
        Assert.assertEquals(base.asrMillis + (-5 * 60 * 1000), adjusted.asrMillis);
        Assert.assertEquals(base.maghribMillis + (2 * 60 * 1000), adjusted.maghribMillis);
        Assert.assertEquals(base.ishaMillis + (-15 * 60 * 1000), adjusted.ishaMillis);
    }

    @Test
    public void testMazhabShadowSystemDifference() {
        Calendar cal = Calendar.getInstance();
        double lat = 23.8103; // Dhaka
        double lng = 90.4125;
        double tz = 6.0;

        PrayerSettingsManager.CalculationMethod method = PrayerSettingsManager.CalculationMethod.KARACHI;

        // 1x Shadow (Shafi, Maliki, Hanbali)
        PrayerCalculator.PrayerTimesResult shafiRes = PrayerCalculator.calculateForLocationWithSettings(
                lat, lng, tz, cal, method, PrayerSettingsManager.JuristicMethod.SHAFI,
                0, 0, 0, 0, 0, 0
        );

        // 2x Shadow (Hanafi)
        PrayerCalculator.PrayerTimesResult hanafiRes = PrayerCalculator.calculateForLocationWithSettings(
                lat, lng, tz, cal, method, PrayerSettingsManager.JuristicMethod.HANAFI,
                0, 0, 0, 0, 0, 0
        );

        // Hanafi Asr time must be strictly later than Shafi Asr time
        Assert.assertTrue("Hanafi Asr should start later than Shafi Asr", hanafiRes.asrMillis > shafiRes.asrMillis);
        // All other prayers should remain identical
        Assert.assertEquals(shafiRes.fajrMillis, hanafiRes.fajrMillis);
        Assert.assertEquals(shafiRes.sunriseMillis, hanafiRes.sunriseMillis);
        Assert.assertEquals(shafiRes.zohrMillis, hanafiRes.zohrMillis);
        Assert.assertEquals(shafiRes.maghribMillis, hanafiRes.maghribMillis);
        Assert.assertEquals(shafiRes.ishaMillis, hanafiRes.ishaMillis);
    }

    @Test
    public void testTabukSaudiArabiaCalculation() {
        Calendar cal = Calendar.getInstance();
        double tabukLat = 28.3835;
        double tabukLng = 36.5662;
        double tabukTz = 3.0;

        PrayerCalculator.PrayerTimesResult pt = PrayerCalculator.calculateForLocationWithSettings(
                tabukLat, tabukLng, tabukTz, cal,
                PrayerSettingsManager.CalculationMethod.UMM_AL_QURA,
                PrayerSettingsManager.JuristicMethod.SHAFI,
                0, 0, 0, 0, 0, 0
        );

        Assert.assertNotNull(pt);
        Assert.assertTrue(pt.fajrMillis < pt.sunriseMillis);
        Assert.assertTrue(pt.sunriseMillis < pt.zohrMillis);
        Assert.assertTrue(pt.zohrMillis < pt.asrMillis);
        Assert.assertTrue(pt.asrMillis < pt.maghribMillis);
        Assert.assertTrue(pt.maghribMillis < pt.ishaMillis);
    }
}
