package com.devflux.deenone.features.qibla;

import com.devflux.deenone.core.calculations.QiblaCalculator;

import org.junit.Assert;
import org.junit.Test;

public class QiblaFeatureComprehensiveTest {

    @Test
    public void testMadinahQiblaBearingCalculation() {
        // Madinah coordinates (24.4672° N, 39.6111° E)
        double madinahLat = 24.4672;
        double madinahLng = 39.6111;

        float bearing = QiblaCalculator.calculateQiblaBearing(madinahLat, madinahLng);
        // Bearing from Madinah to Makkah is approximately 176.2° (Almost due South)
        Assert.assertTrue(bearing > 150f && bearing < 185f);

        double distance = QiblaCalculator.calculateDistanceToKaabaKm(madinahLat, madinahLng);
        // Distance is ~338 - 350 km great-circle
        Assert.assertTrue(distance > 300 && distance < 450);
    }

    @Test
    public void testDhakaQiblaBearingCalculation() {
        // Dhaka coordinates (23.8103° N, 90.4125° E)
        double dhakaLat = 23.8103;
        double dhakaLng = 90.4125;

        float bearing = QiblaCalculator.calculateQiblaBearing(dhakaLat, dhakaLng);
        // Bearing from Dhaka to Makkah is approximately 277° (West-Southwest)
        Assert.assertTrue(bearing > 265f && bearing < 285f);

        double distance = QiblaCalculator.calculateDistanceToKaabaKm(dhakaLat, dhakaLng);
        // Distance is ~5,150 - 5,250 km
        Assert.assertTrue(distance > 5000 && distance < 5400);
    }

    @Test
    public void testQiblaAlignmentThresholds() {
        float qiblaBearing = 156.4f;

        // Exactly aligned (azimuth = 156.4f)
        float diff1 = (qiblaBearing - 156.4f + 360f) % 360f;
        if (diff1 > 180f) diff1 -= 360f;
        Assert.assertTrue(Math.abs(diff1) <= 3.5f);

        // Needs to turn right 15° (azimuth = 141.4f)
        float diff2 = (qiblaBearing - 141.4f + 360f) % 360f;
        if (diff2 > 180f) diff2 -= 360f;
        Assert.assertTrue(diff2 > 3.5f);
        Assert.assertEquals(15.0f, diff2, 0.5f);

        // Needs to turn left 20° (azimuth = 176.4f)
        float diff3 = (qiblaBearing - 176.4f + 360f) % 360f;
        if (diff3 > 180f) diff3 -= 360f;
        Assert.assertTrue(diff3 < -3.5f);
        Assert.assertEquals(-20.0f, diff3, 0.5f);
    }
}
