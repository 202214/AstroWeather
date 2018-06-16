package com.liwocha.dawid.astroweather.yahoo;

public class Forecast {

    private String date;

    private String day;

    private int lowestTemperature;

    private int highestTemperature;

    private String desc;

    private int conditionCode;

    public Forecast(String date, String day, int lowestTemperature, int highestTemperature, String desc, int conditionCode) {
        this.date = date;
        this.day = day;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.desc = desc;
        this.conditionCode = conditionCode;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public int getLowestTemperature() {
        return lowestTemperature;
    }

    public int getHighestTemperature() {
        return highestTemperature;
    }

    public String getDesc() {
        return desc;
    }

    public int getConditionCode() {
        return conditionCode;
    }

}
