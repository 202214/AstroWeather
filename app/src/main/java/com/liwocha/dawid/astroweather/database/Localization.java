package com.liwocha.dawid.astroweather.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.liwocha.dawid.astroweather.yahoo.LocalizationInfo;

@Entity
public class Localization {

    @PrimaryKey
    private int woeid;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "choosen")
    private boolean choosen;

    public Localization(LocalizationInfo localizationInfo) {
        this.woeid = localizationInfo.getWoeid();
        this.city = localizationInfo.getCity();
        this.country = localizationInfo.getCountryCode();
        this.longitude = localizationInfo.getLongitude();
        this.latitude = localizationInfo.getLatitude();
    }

    public Localization() {

    }

    public LocalizationInfo toLocalizationInfo() {
        return new LocalizationInfo(city, country, woeid, longitude, latitude);
    }

    public int getWoeid() {
        return woeid;
    }

    public void setWoeid(int woeid) {
        this.woeid = woeid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean getChoosen() {
        return choosen;
    }

    public void setChoosen(boolean choosen) {
        this.choosen = choosen;
    }

}
