package com.liwocha.dawid.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liwocha.dawid.astroweather.Connection.Connection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AstroActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private TextView timeValue;
    private TextView latitudeValue;
    private TextView longitudeValue;
    private double longitude;
    private double latitude;
    private Thread timeThread;
    private Thread preferencesThread;
    private SharedPreferences preferences;
    private long lastMilis;
    private boolean destroy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
            viewPager = (ViewPager) findViewById(R.id.container);
            setupViewPager(viewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
        timeValue = (TextView) findViewById(R.id.timeValue);
        latitudeValue = (TextView) findViewById(R.id.latitudeValue);
        longitudeValue = (TextView) findViewById(R.id.longitudeValue);
        if (savedInstanceState != null) {
            lastMilis = savedInstanceState.getLong("lastMillis");
        }
    }

    private void loadSettings() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        longitude = Double.parseDouble(preferences.getString("longitude", "0.0"));
        latitude = Double.parseDouble(preferences.getString("latitude", "0.0"));
        longitudeValue.setText(Double.toString(longitude));
        latitudeValue.setText(Double.toString(latitude));
    }

    private void runTimeThread() {
        timeThread = new Thread(new Runnable(){
            @Override
            public void run()
            {
                DateFormat format = new SimpleDateFormat("hh:mm:ss");
                Date date = Calendar.getInstance().getTime();
                updateTimeValue(format.format(date));
                if (lastMilis == 0) {
                    lastMilis = System.currentTimeMillis();
                }
                while(!destroy) {
                    if (System.currentTimeMillis() - lastMilis >= 1000) {
                        lastMilis = System.currentTimeMillis();
                        date = Calendar.getInstance().getTime();
                        updateTimeValue(format.format(date));
                    }
                }
            }
        });
        timeThread.start();
    }

    private void updateTimeValue(final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeValue.setText(value);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new SunFragment(), "SUN");
        adapter.addFragment(new MoonFragment(), "MOON");
        viewPager.setAdapter(adapter);
    }

    public void onSettingsButtonClick(View view) {
        if (!Connection.isOnline(this)) {
            Toast.makeText(this, "No internet connection!\nYou can't change settings!.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Intent settingsActivity = new Intent(AstroActivity.this, SettingsActivity.class);
            startActivity(settingsActivity);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("lastMillis", lastMilis);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        destroy = false;
        loadSettings();
        runTimeThread();
    }

    @Override
    public void onPause() {
        destroy = true;
        super.onPause();
    }

}