package com.liwocha.dawid.astroweather;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import com.liwocha.dawid.astroweather.Connection.Connection;

import com.liwocha.dawid.astroweather.yahoo.WeatherInfo;



import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends Fragment {

    public List<ImageView> weatherImages = new ArrayList<>();
    public List<TextView> dayDateValues = new ArrayList<>();
    public List<TextView> lowestTemperatureValues = new ArrayList<>();
    public List<TextView> highestTemperatureValues = new ArrayList<>();
    public List<TextView> descriptionValues = new ArrayList<>();
    private WeatherInfo weatherInfo;
    public String imageURL;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private int i;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast,container,false);
        weatherImages.add((ImageView) view.findViewById(R.id.weatherImage1));
        weatherImages.add((ImageView) view.findViewById(R.id.weatherImage2));
        weatherImages.add((ImageView) view.findViewById(R.id.weatherImage3));
        weatherImages.add((ImageView) view.findViewById(R.id.weatherImage4));
        weatherImages.add((ImageView) view.findViewById(R.id.weatherImage5));
        weatherImages.add((ImageView) view.findViewById(R.id.weatherImage6));
        weatherImages.add((ImageView) view.findViewById(R.id.weatherImage7));
        dayDateValues.add((TextView) view.findViewById(R.id.dayDateValue1));
        dayDateValues.add((TextView) view.findViewById(R.id.dayDateValue2));
        dayDateValues.add((TextView) view.findViewById(R.id.dayDateValue3));
        dayDateValues.add((TextView) view.findViewById(R.id.dayDateValue4));
        dayDateValues.add((TextView) view.findViewById(R.id.dayDateValue5));
        dayDateValues.add((TextView) view.findViewById(R.id.dayDateValue6));
        dayDateValues.add((TextView) view.findViewById(R.id.dayDateValue7));
        lowestTemperatureValues.add((TextView) view.findViewById(R.id.lowestTemperatureValue1));
        lowestTemperatureValues.add((TextView) view.findViewById(R.id.lowestTemperatureValue2));
        lowestTemperatureValues.add((TextView) view.findViewById(R.id.lowestTemperatureValue3));
        lowestTemperatureValues.add((TextView) view.findViewById(R.id.lowestTemperatureValue4));
        lowestTemperatureValues.add((TextView) view.findViewById(R.id.lowestTemperatureValue5));
        lowestTemperatureValues.add((TextView) view.findViewById(R.id.lowestTemperatureValue6));
        lowestTemperatureValues.add((TextView) view.findViewById(R.id.lowestTemperatureValue7));
        highestTemperatureValues.add((TextView) view.findViewById(R.id.highestTemperatureValue1));
        highestTemperatureValues.add((TextView) view.findViewById(R.id.highestTemperatureValue2));
        highestTemperatureValues.add((TextView) view.findViewById(R.id.highestTemperatureValue3));
        highestTemperatureValues.add((TextView) view.findViewById(R.id.highestTemperatureValue4));
        highestTemperatureValues.add((TextView) view.findViewById(R.id.highestTemperatureValue5));
        highestTemperatureValues.add((TextView) view.findViewById(R.id.highestTemperatureValue6));
        highestTemperatureValues.add((TextView) view.findViewById(R.id.highestTemperatureValue7));
        descriptionValues.add((TextView) view.findViewById(R.id.descriptionValue1));
        descriptionValues.add((TextView) view.findViewById(R.id.descriptionValue2));
        descriptionValues.add((TextView) view.findViewById(R.id.descriptionValue3));
        descriptionValues.add((TextView) view.findViewById(R.id.descriptionValue4));
        descriptionValues.add((TextView) view.findViewById(R.id.descriptionValue5));
        descriptionValues.add((TextView) view.findViewById(R.id.descriptionValue6));
        descriptionValues.add((TextView) view.findViewById(R.id.descriptionValue7));
        return view;
    }

    public boolean isLoading() {
        if (descriptionValues.size() == 0 || descriptionValues == null) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public void updateData() {
        if (Connection.isOnline(getActivity())) {
            loadImagesFromURL();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (i = 0; i < dayDateValues.size(); i++) {
                    dayDateValues.get(i).setText(weatherInfo.getForecast().get(i).getDay() + ", " + weatherInfo.getForecast().get(i).getDate());
                    lowestTemperatureValues.get(i).setText(Integer.toString(weatherInfo.getForecast().get(i).getLowestTemperature()) + " \u00b0C");
                    highestTemperatureValues.get(i).setText(Integer.toString(weatherInfo.getForecast().get(i).getHighestTemperature()) + " \u00b0C");
                    descriptionValues.get(i).setText(weatherInfo.getForecast().get(i).getDesc());
                }
            }
        });
        if (Connection.isOnline(getActivity())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (i=0; i<weatherImages.size(); i++) {
                        weatherImages.get(i).setImageBitmap(bitmaps.get(i));
                    }
                }
            });
        }
    }

    public void loadImagesFromURL(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i=0; i<weatherImages.size(); i++) {
                        imageURL = "http://l.yimg.com/a/i/us/we/52/" + Integer.toString(weatherInfo.getForecast().get(i).getConditionCode()) + ".gif";
                        URL myFileUrl = new URL(imageURL);
                        HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmaps.add(BitmapFactory.decodeStream(is));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            // nothing to do
        }
    }

}