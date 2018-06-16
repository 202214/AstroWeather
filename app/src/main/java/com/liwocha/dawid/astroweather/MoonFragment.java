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
import com.liwocha.dawid.astroweather.astro.MoonAstroCalculator;

public class MoonFragment extends Fragment {

    private AstroCalculator astroCalculator;
    private double longitude;
    private double latitude;
    private int refreshInterval;
    private TextView riseValue;
    private TextView setValue;
    private TextView newValue;
    private TextView fullValue;
    private TextView phaseValue;
    private TextView dayValue;
    private Thread preferencesThread;
    private Thread refreshThread;
    private SharedPreferences preferences;
    private long lastMillis;
    private boolean destroy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon,container,false);
        setRetainInstance(true);
        riseValue = (TextView) view.findViewById(R.id.riseValue);
        setValue = (TextView) view.findViewById(R.id.setValue);
        newValue = (TextView) view.findViewById(R.id.newValue);
        fullValue = (TextView) view.findViewById(R.id.fullValue);
        phaseValue = (TextView) view.findViewById(R.id.phaseValue);
        dayValue = (TextView) view.findViewById(R.id.dayValue);
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
        System.out.println(refreshInterval);
    }

    private void calculateData() {
        MoonAstroCalculator moonAstroCalculator = new MoonAstroCalculator(latitude, longitude);
        riseValue.setText(moonAstroCalculator.getMoonRise());
        setValue.setText(moonAstroCalculator.getMoonSet());
        newValue.setText(moonAstroCalculator.getNextNewMoon());
        fullValue.setText(moonAstroCalculator.getNextFullMoon());
        phaseValue.setText(moonAstroCalculator.getMoonPhase());
        dayValue.setText(moonAstroCalculator.getSynodDay());
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