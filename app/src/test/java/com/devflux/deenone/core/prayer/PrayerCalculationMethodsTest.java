package com.devflux.deenone.core.prayer;

import com.devflux.deenone.utils.PrayerCalculator;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class PrayerCalculationMethodsTest {

    @Test
    public void testAllCalculationMethodsPresence() {
        PrayerSettingsManager.CalculationMethod[] methods = PrayerSettingsManager.CalculationMethod.values();
        // 19 standardized calculation methods (+ Automatic recommendation = 20 choices in UI)
        Assert.assertEquals(19, methods.length);

        for (PrayerSettingsManager.CalculationMethod method : methods) {
            Assert.assertNotNull("Display name must not be null for " + method.name(), method.displayName);
            Assert.assertNotNull("Bengali name must not be null for " + method.name(), method.displayNameBn);
            Assert.assertTrue("Fajr angle must be positive for " + method.name(), method.fajrAngle > 0);
        }
    }

    @Test
    public void testCalculationsAcrossAllMethods() {
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.AUGUST, 31, 12, 0, 0);

        // Test coordinates (Makkah)
        double latMakkah = 21.4225;
        double lngMakkah = 39.8262;
        double tzMakkah = 3.0;

        for (PrayerSettingsManager.CalculationMethod method : PrayerSettingsManager.CalculationMethod.values()) {
            PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocation(
                    latMakkah, lngMakkah, tzMakkah, cal,
                    PrayerSettingsManager.JuristicMethod.SHAFI,
                    method
            );

            Assert.assertNotNull("Result must not be null for " + method.name(), res);
            Assert.assertNotNull("Fajr must not be null for " + method.name(), res.fajrStr);
            Assert.assertNotNull("Sunrise must not be null for " + method.name(), res.sunriseStr);
            Assert.assertNotNull("Zohr must not be null for " + method.name(), res.zohrStr);
            Assert.assertNotNull("Asr must not be null for " + method.name(), res.asrStr);
            Assert.assertNotNull("Maghrib must not be null for " + method.name(), res.maghribStr);
            Assert.assertNotNull("Isha must not be null for " + method.name(), res.ishaStr);

            Assert.assertTrue("Fajr must be before sunrise for " + method.name(), res.fajrMillis < res.sunriseMillis);
            Assert.assertTrue("Sunrise must be before zohr for " + method.name(), res.sunriseMillis < res.zohrMillis);
            Assert.assertTrue("Zohr must be before asr for " + method.name(), res.zohrMillis < res.asrMillis);
            Assert.assertTrue("Asr must be before maghrib for " + method.name(), res.asrMillis < res.maghribMillis);
            Assert.assertTrue("Maghrib must be before isha for " + method.name(), res.maghribMillis < res.ishaMillis);
        }
    }

    @Test
    public void testHighLatitudePolarCalculations() {
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.JUNE, 21, 12, 0, 0); // Summer solstice in high-lat (Oslo / St. Petersburg)

        double latHigh = 59.9139;
        double lngHigh = 10.7522;
        double tzHigh = 2.0;

        PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocation(
                latHigh, lngHigh, tzHigh, cal,
                PrayerSettingsManager.JuristicMethod.SHAFI,
                PrayerSettingsManager.CalculationMethod.MWL_HIGH_LATITUDE
        );

        Assert.assertNotNull(res);
        Assert.assertNotNull(res.fajrStr);
        Assert.assertNotNull(res.ishaStr);
        Assert.assertTrue(res.fajrMillis < res.sunriseMillis);
        Assert.assertTrue(res.maghribMillis < res.ishaMillis);
    }
}
