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

public class TodayFragment extends Fragment{

    public TextView temperatureValue;
    public TextView descriptionValue;
    public TextView chillValue;
    public TextView speedValue;
    public TextView directionValue;
    public TextView pressureValue;
    public TextView humidityValue;
    public TextView visibilityValue;
    public ImageView weatherImage;
    private WeatherInfo weatherInfo;
    private Bitmap bitmap;
    public String imageURL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today,container,false);
        temperatureValue = (TextView) view.findViewById(R.id.temperatureValue);
        descriptionValue = (TextView) view.findViewById(R.id.descriptionValue);
        chillValue = (TextView) view.findViewById(R.id.chillValue);
        speedValue = (TextView) view.findViewById(R.id.speedValue);
        directionValue = (TextView) view.findViewById(R.id.directionValue);
        pressureValue = (TextView) view.findViewById(R.id.pressureValue);
        humidityValue = (TextView) view.findViewById(R.id.humidityValue);
        visibilityValue = (TextView) view.findViewById(R.id.visibilityValue);
        weatherImage = (ImageView) view.findViewById(R.id.weatherImage);
        return view;
    }


    public boolean isLoading() {
        if (weatherImage == null) {
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                temperatureValue.setText(Integer.toString(weatherInfo.getCondition().getTemperature()) + " \u00b0C");
                temperatureValue.setText(Integer.toString(weatherInfo.getCondition().getTemperature()) + " \u00b0C");
                descriptionValue.setText(weatherInfo.getCondition().getDesc());
                chillValue.setText(Integer.toString(weatherInfo.getWind().getChill()) + " \u00b0C");
                speedValue.setText(Double.toString(weatherInfo.getWind().getSpeed()) + " kph");
                directionValue.setText(Integer.toString(weatherInfo.getWind().getDirection()) + " \u00b0");
                pressureValue.setText(Integer.toString(weatherInfo.getAtmosphere().getPressure()) + " hpa");
                humidityValue.setText(Integer.toString(weatherInfo.getAtmosphere().getHumidity()) + " %");
                visibilityValue.setText(Double.toString(weatherInfo.getAtmosphere().getVisibility()) + " km");
            }
        });
        if (Connection.isOnline(getActivity())) {
            imageURL = "http://l.yimg.com/a/i/us/we/52/" + Integer.toString(weatherInfo.getCondition().getCode()) + ".gif";
            loadImageFromURL();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weatherImage.setImageBitmap(bitmap);
                }
            });
        }
    }

    public void loadImageFromURL(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL myFileUrl = new URL(imageURL);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
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