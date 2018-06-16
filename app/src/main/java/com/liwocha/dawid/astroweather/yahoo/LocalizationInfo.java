package com.liwocha.dawid.astroweather.yahoo;

public class LocalizationInfo {

    private String city;

    private String country;

    private int woeid;

    private double longitude;

    private double latitude;

    public LocalizationInfo(String city, String country, int woeid, double longitude, double latitude) {
        this.city = city;
        this.country = country;
        this.woeid = woeid;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public String getCountryCode() {
        return country;
    }

    public int getWoeid() {
        return woeid;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
