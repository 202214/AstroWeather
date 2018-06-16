package com.liwocha.dawid.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liwocha.dawid.astroweather.Connection.Connection;
import com.liwocha.dawid.astroweather.database.AppDatabase;
import com.liwocha.dawid.astroweather.database.Localization;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private Localization localization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = AppDatabase.getAppDatabase(this);
    }

    public void onAstroActivityButtonClick(View view) {
        localization = database.localizationDAO().findByChoosen();
        if (localization == null && !Connection.isOnline(this)) {
            Toast.makeText(this, "No internet connection!\nFirst time you need internet connection!.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        database.initialize(database);
        Intent Intent = new Intent(this, AstroActivity.class);
        startActivity(Intent);
    }

    public void onWeatherActivityButtonClick(View view) {
        localization = database.localizationDAO().findByChoosen();
        if (localization == null && !Connection.isOnline(this)) {
            Toast.makeText(this, "No internet connection!\nFirst time you need internet connection!.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        database.initialize(database);
        Intent Intent = new Intent(this, WeatherActivity.class);
        startActivity(Intent);
    }

    public void onExitButtonClick(View view) {
        finish();
        System.exit(0);
    }

    public void onAboutButtonClick(View view) {
        // NOT IMPLEMENTED YET
    }

}