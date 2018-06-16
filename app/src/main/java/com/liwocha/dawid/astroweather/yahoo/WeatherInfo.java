package com.liwocha.dawid.astroweather.yahoo;

import java.util.List;

public class WeatherInfo {

    private LocalizationInfo localizationInfo;

    private Condition condition;

    private List<Forecast> forecast;

    private Wind wind;

    private Atmosphere atmosphere;

    int timeToLive;

    public WeatherInfo(LocalizationInfo localizationInfo, Condition condition, List<Forecast> forecast, Wind wind, Atmosphere atmosphere, int timeToLive) {
        this.localizationInfo = localizationInfo;
        this.condition = condition;
        this.forecast = forecast;
        this.wind = wind;
        this.atmosphere = atmosphere;
        this.timeToLive = timeToLive;
    }

    public LocalizationInfo getLocalizationInfo() {
        return localizationInfo;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    public Wind getWind() {
        return wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Condition getCondition() {
        return condition;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

}
