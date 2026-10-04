package com.devflux.deenone.features.mosque.model;

public class MosqueItem {
    private final int id;
    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;
    private double distanceMeters;
    private String distanceFormatted;
    private String walkingTimeFormatted;
    private String drivingTimeFormatted;
    private final String status;
    private final double rating;
    private final boolean hasWuduArea;
    private final boolean hasWomenArea;
    private final boolean hasAc;

    public MosqueItem(int id, String name, String address, double latitude, double longitude,
                      String status, double rating, boolean hasWuduArea, boolean hasWomenArea, boolean hasAc) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.rating = rating;
        this.hasWuduArea = hasWuduArea;
        this.hasWomenArea = hasWomenArea;
        this.hasAc = hasAc;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }
    public String getDistanceFormatted() { return distanceFormatted; }
    public void setDistanceFormatted(String distanceFormatted) { this.distanceFormatted = distanceFormatted; }
    public String getWalkingTimeFormatted() { return walkingTimeFormatted; }
    public void setWalkingTimeFormatted(String walkingTimeFormatted) { this.walkingTimeFormatted = walkingTimeFormatted; }
    public String getDrivingTimeFormatted() { return drivingTimeFormatted; }
    public void setDrivingTimeFormatted(String drivingTimeFormatted) { this.drivingTimeFormatted = drivingTimeFormatted; }
    public String getStatus() { return status; }
    public double getRating() { return rating; }
    public boolean isHasWuduArea() { return hasWuduArea; }
    public boolean isHasWomenArea() { return hasWomenArea; }
    public boolean isHasAc() { return hasAc; }
}
