package com.liwocha.dawid.astroweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.liwocha.dawid.astroweather.astro.SunAstroCalculator;

public class SunFragment extends Fragment {

    private AstroCalculator astroCalculator;
    private double longitude;
    private double latitude;
    private int refreshInterval;
    private TextView riseTimeValue;
    private TextView riseAzimuthValue;
    private TextView setTimeValue;
    private TextView setAzimuthValue;
    private TextView twilightEveningValue;
    private TextView twilightMorningValue;
    private Thread preferencesThread;
    private Thread refreshThread;
    private SharedPreferences preferences;
    private long lastMillis;
    private boolean destroy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sun,container,false);
        setRetainInstance(true);
        riseTimeValue = (TextView) view.findViewById(R.id.riseTimeValue);
        riseAzimuthValue = (TextView) view.findViewById(R.id.riseAzimuthValue);
        setTimeValue = (TextView) view.findViewById(R.id.setTimeValue);
        setAzimuthValue = (TextView) view.findViewById(R.id.setAzimuthValue);
        twilightEveningValue = (TextView) view.findViewById(R.id.twilightEveningValue);
        twilightMorningValue = (TextView) view.findViewById(R.id.twilightMorningValue);
        if (savedInstanceState != null) {
            lastMillis = savedInstanceState.getLong("lastMillis");
        }
        return view;
    }

    private void loadSettings() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        longitude = Double.parseDouble(preferences.getString("longitude", "0.0"));
        latitude = Double.parseDouble(preferences.getString("latitude", "0.0"));
        refreshInterval = Integer.parseInt(preferences.getString("refresh_interval", "15"));
    }

    private void calculateData() {
        SunAstroCalculator sunAstroCalculator = new SunAstroCalculator(latitude, longitude);
        riseTimeValue.setText(sunAstroCalculator.getSunRiseTime());
        riseAzimuthValue.setText(sunAstroCalculator.getSunRiseAzimuth());
        setTimeValue.setText(sunAstroCalculator.getSunSetTime());
        setAzimuthValue.setText(sunAstroCalculator.getSunSetAzimuth());
        twilightEveningValue.setText(sunAstroCalculator.getSunTwilightEvening());
        twilightMorningValue.setText(sunAstroCalculator.getSunTwilightMorning());
    }

    private void runRefreshThread() {
        refreshThread = new Thread(new Runnable(){
            @Override
            public void run()
            {
                if (lastMillis == 0) {
                    lastMillis = System.currentTimeMillis();
                }
                while(!destroy) {
                    if (System.currentTimeMillis() - lastMillis >= refreshInterval * 1000) {
                        runCalculateData();
                    }
                }
            }
        });
        refreshThread.start();
    }

    private void runCalculateData() {
        lastMillis = System.currentTimeMillis();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                calculateData();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("lastMillis", lastMillis);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        destroy = false;
        loadSettings();
        calculateData();
        runRefreshThread();
    }

    @Override
    public void onPause() {
        destroy = true;
        super.onPause();
    }

}