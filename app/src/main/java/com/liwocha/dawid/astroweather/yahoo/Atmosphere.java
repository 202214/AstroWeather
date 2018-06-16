package com.liwocha.dawid.astroweather.yahoo;

public class Atmosphere {

    private int humidity;

    private int pressure;

    private double visibility;

    public Atmosphere(int humidity, int pressure, double visibility) {
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public double getVisibility() {
        return visibility;
    }

}
