package com.devflux.deenone.core.prayer;

import com.devflux.deenone.core.constants.AppConstants;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.utils.PrayerCalculator;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class RealtimePrayerScheduleAndLocationTest {

    @Test
    public void testDefaultLocationConstants() {
        Assert.assertEquals("ঢাকা", AppConstants.DEFAULT_LOCATION_NAME);
        Assert.assertEquals(23.8103, AppConstants.DEFAULT_LATITUDE, 0.0001);
        Assert.assertEquals(90.4125, AppConstants.DEFAULT_LONGITUDE, 0.0001);
        Assert.assertEquals(6.0, AppConstants.DEFAULT_TIMEZONE, 0.0001);
    }

    @Test
    public void testRealtimePrayerCalculationForUserLocation() {
        Calendar cal = Calendar.getInstance();
        // Test for Dhaka coordinates
        PrayerCalculator.PrayerTimesResult resDhaka = PrayerCalculator.calculateForLocation(
                23.8103, 90.4125, 6.0, cal
        );

        Assert.assertNotNull(resDhaka);
        Assert.assertNotNull(resDhaka.fajrStr);
        Assert.assertNotNull(resDhaka.zohrStr);
        Assert.assertNotNull(resDhaka.asrStr);
        Assert.assertNotNull(resDhaka.maghribStr);
        Assert.assertNotNull(resDhaka.ishaStr);
        Assert.assertTrue(resDhaka.fajrMillis < resDhaka.sunriseMillis);
        Assert.assertTrue(resDhaka.sunriseMillis < resDhaka.zohrMillis);
        Assert.assertTrue(resDhaka.zohrMillis < resDhaka.asrMillis);
        Assert.assertTrue(resDhaka.asrMillis < resDhaka.maghribMillis);
        Assert.assertTrue(resDhaka.maghribMillis < resDhaka.ishaMillis);
    }

    @Test
    public void testDynamicLocationUpdateAndEntityGeneration() {
        LocationProvider.Coordinates customGps = new LocationProvider.Coordinates(
                "📍 গুলশান, ঢাকা", 23.7925, 90.4078, 6.0
        );

        Calendar cal = Calendar.getInstance();
        PrayerCalculator.PrayerTimesResult pt = PrayerCalculator.calculateForLocation(
                customGps.latitude, customGps.longitude, customGps.timezone, cal
        );

        PrayerScheduleEntity entity = new PrayerScheduleEntity(
                "2026-08-22",
                customGps.locationName.replace("📍", "").trim(),
                pt.fajrStr,
                pt.sunriseStr,
                pt.sunriseStr,
                pt.zohrStr,
                pt.asrStr,
                pt.maghribStr,
                pt.ishaStr,
                pt.sehriStr,
                pt.iftarStr
        );

        Assert.assertEquals("গুলশান", entity.getLocation());
        Assert.assertNotNull(entity.getFajr());
        Assert.assertNotNull(entity.getZohr());
        Assert.assertNotNull(entity.getAsr());
        Assert.assertNotNull(entity.getMaghrib());
        Assert.assertNotNull(entity.getIsha());
    }
}
