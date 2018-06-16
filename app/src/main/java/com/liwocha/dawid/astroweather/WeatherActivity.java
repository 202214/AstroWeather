package com.liwocha.dawid.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liwocha.dawid.astroweather.Connection.Connection;
import com.liwocha.dawid.astroweather.database.AppDatabase;
import com.liwocha.dawid.astroweather.database.Localization;
import com.liwocha.dawid.astroweather.yahoo.WeatherFile;
import com.liwocha.dawid.astroweather.yahoo.WeatherInfo;
import com.liwocha.dawid.astroweather.yahoo.YahooWeather;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private SharedPreferences preferences;
    private TextView localizationValue;
    private TextView refreshDateValue;
    private AppDatabase database;
    private Localization localization;
    private boolean destroy;
    private WeatherInfo weatherInfo;
    private TodayFragment todayFragment;
    private ForecastFragment forecastFragment;
    private JSONObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        viewPager = (ViewPager) findViewById(R.id.container);
        if (savedInstanceState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            todayFragment = (TodayFragment) fragments.get(0);
            forecastFragment = (ForecastFragment) fragments.get(1);
        }
        else {
            todayFragment = new TodayFragment();
            forecastFragment = new ForecastFragment();
        }
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(todayFragment, "TODAY");
        adapter.addFragment(forecastFragment, "FORECAST");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        localizationValue = (TextView) findViewById(R.id.localizationValue);
        refreshDateValue = (TextView) findViewById(R.id.refreshDateValue);
        database = AppDatabase.getAppDatabase(this);
    }

    private void loadSettings() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        localization = database.localizationDAO().findByChoosen();
        if (localization == null) {
            return;
        }
        localizationValue.setText(localization.getCity()+", "+localization.getCountry());
        calculateData();
    }

    public void onSettingsButtonClick(View view) {
        if (!Connection.isOnline(this)) {
            Toast.makeText(this, "No internet connection!\nYou can't change settings!.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Intent settingsActivity = new Intent(WeatherActivity.this, SettingsActivity.class);
            startActivity(settingsActivity);
        }
    }

    public void onRefreshButtonClick(View view) {
        if (Connection.isOnline(this)) {
            calculateData();
        }
        else {
            Toast.makeText(this, "No internet connection!\nYou can't refresh data.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        destroy = false;
        loadSettings();
    }

    @Override
    public void onPause() {
        destroy = true;
        super.onPause();
    }

    private void calculateData() {
        if (Connection.isOnline(this)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        response = YahooWeather.getWeatherInfo(localization.toLocalizationInfo());
                        weatherInfo = YahooWeather.buildWeatherInfoFromJSON(localization.toLocalizationInfo(), response);
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
            try {
                WeatherFile.writeToFile(response.toString(), this);
            } catch (Exception e) {
                return;
            }
        }
        else {
            try {
                response = new JSONObject(WeatherFile.readFromFile(this));
                weatherInfo = YahooWeather.buildWeatherInfoFromJSON(localization.toLocalizationInfo(), response);
                Toast.makeText(this, "No internet connection!\nLoading file with last loaded data...",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "No internet connection!\nNo file with last loaded data!.",
                        Toast.LENGTH_LONG).show();
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        refreshDateValue.setText(format.format(weatherInfo.getCondition().getDate()));
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(todayFragment.isLoading());
                    todayFragment.setWeatherInfo(weatherInfo);
                    todayFragment.updateData();
                    while(forecastFragment.isLoading());
                    forecastFragment.setWeatherInfo(weatherInfo);
                    forecastFragment.updateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
    }

}