package com.devflux.deenone.core.calculations;

public class QiblaCalculator {

    // Kaaba Coordinates (Makkah, Saudi Arabia)
    public static final double KAABA_LATITUDE = 21.422487;
    public static final double KAABA_LONGITUDE = 39.826206;

    /**
     * Calculates the forward azimuth bearing in degrees from user's coordinates towards Kaaba.
     * Uses Great-Circle spherical trigonometry.
     *
     * @param userLat User's latitude in degrees
     * @param userLng User's longitude in degrees
     * @return Bearing in degrees (0° - 360° clockwise from True North)
     */
    public static float calculateQiblaBearing(double userLat, double userLng) {
        double phi1 = Math.toRadians(userLat);
        double phi2 = Math.toRadians(KAABA_LATITUDE);
        double deltaLambda = Math.toRadians(KAABA_LONGITUDE - userLng);

        double y = Math.sin(deltaLambda) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltaLambda);

        double qiblaAngle = Math.toDegrees(Math.atan2(y, x));
        return (float) ((qiblaAngle + 360.0) % 360.0);
    }

    /**
     * Calculates the distance in kilometers from user's location to Kaaba.
     */
    public static double calculateDistanceToKaabaKm(double userLat, double userLng) {
        final double EARTH_RADIUS_KM = 6371.0;
        double dLat = Math.toRadians(KAABA_LATITUDE - userLat);
        double dLng = Math.toRadians(KAABA_LONGITUDE - userLng);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(KAABA_LATITUDE)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
