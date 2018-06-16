package com.liwocha.dawid.astroweather.yahoo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class YahooWeather {

    public static LocalizationInfo getLocalizationInfo(String city, String countryCode) throws Exception {
        String path = "https://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20geo.places(1)%20where%20text%3D%22("+city+"%2C%20"+countryCode+
                ")%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            JSONObject response = YahooRequest.execute(path)
                    .getJSONObject("query")
                    .getJSONObject("results")
                    .getJSONObject("place");
        int woeid = response.getInt("woeid");
        double longitude = response.getJSONObject("centroid").getDouble("longitude");
        double latitude = response.getJSONObject("centroid").getDouble("latitude");
        String cityName = response.getString("name");
        String countryName = response.getJSONObject("country").getString("content");
        return new LocalizationInfo(cityName, countryName, woeid, longitude, latitude);
    }



    public static JSONObject getWeatherInfo(LocalizationInfo localizationInfo) throws Exception {
        int woeid = localizationInfo.getWoeid();
        String path = "https://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20weather.forecast%20where%20woeid%3D"+woeid+"%20and%20u%3D%22c%22&format=" +
                "json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        JSONObject response = YahooRequest.execute(path)
                .getJSONObject("query")
                .getJSONObject("results")
                .getJSONObject("channel");
        return response;

    }

    public static WeatherInfo buildWeatherInfoFromJSON(LocalizationInfo localizationInfo, JSONObject response) throws Exception {
        int timeToLive = response.getInt("ttl");
        int chill = response.getJSONObject("wind").getInt("chill");
        int direction = response.getJSONObject("wind").getInt("direction");
        double speed = response.getJSONObject("wind").getDouble("speed");
        Wind wind = new Wind(chill, direction, speed);
        int humidity = response.getJSONObject("atmosphere").getInt("humidity");
        int pressure = (int) (response.getJSONObject("atmosphere").getDouble("pressure") * 0.0295301);
        double visibility = response.getJSONObject("atmosphere").getDouble("visibility") / 100;
        Atmosphere atmosphere = new Atmosphere(humidity, pressure, visibility);
        response = response.getJSONObject("item");
        int temperature = response.getJSONObject("condition").getInt("temp");
        String description = response.getJSONObject("condition").getString("text");
        int code = response.getJSONObject("condition").getInt("code");
        Date date = new Date();
        Condition condition = new Condition(temperature, description, code, date);
        JSONArray forecastResponse = response.getJSONArray("forecast");
        List<Forecast> forecast = new ArrayList<Forecast>();
        for (int i=0; i<forecastResponse.length(); i++) {
            String dateString = forecastResponse.getJSONObject(i).getString("date");
            String day = forecastResponse.getJSONObject(i).getString("day");
            int lowestTemperature = forecastResponse.getJSONObject(i).getInt("low");
            int highestTemperature = forecastResponse.getJSONObject(i).getInt("high");
            String desc = forecastResponse.getJSONObject(i).getString("text");
            int conditionCode = forecastResponse.getJSONObject(i).getInt("code");
            forecast.add(new Forecast(dateString, day, lowestTemperature, highestTemperature, desc, conditionCode));
        }
        return new WeatherInfo(localizationInfo, condition, forecast, wind, atmosphere, timeToLive);
    }

}
