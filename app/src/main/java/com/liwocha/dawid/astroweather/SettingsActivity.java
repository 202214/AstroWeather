package com.liwocha.dawid.astroweather;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import com.liwocha.dawid.astroweather.Connection.Connection;
import com.liwocha.dawid.astroweather.database.AppDatabase;
import com.liwocha.dawid.astroweather.database.Localization;
import com.liwocha.dawid.astroweather.yahoo.LocalizationInfo;
import com.liwocha.dawid.astroweather.yahoo.WeatherInfo;
import com.liwocha.dawid.astroweather.yahoo.YahooWeather;


import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.EditTextPreference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    private String[] entries;
    private AppDatabase database;
    private ListPreference chooseLocalization;
    private ListPreference deleteLocalization;
    private EditTextPreference longitude;
    private EditTextPreference latitude;
    private Localization localization;
    private List<CharSequence> localizationsEntries;
    private List<Integer> localizationsValues;
    private List<Localization> localizations;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.preferences,
                false);
        chooseLocalization = (ListPreference) findPreference("choose_localization");
        deleteLocalization = (ListPreference) findPreference("delete_localization");
        longitude = (EditTextPreference) findPreference("longitude");
        latitude = (EditTextPreference) findPreference("latitude");
        database = AppDatabase.getAppDatabase(this);
        localization = database.localizationDAO().findByChoosen();
        localizations = database.localizationDAO().findAll();
        localizationsEntries = new ArrayList<>();
        localizationsValues = new ArrayList<>();
        for (int i=0; i<localizations.size(); i++) {
            localizationsEntries.add(localizations.get(i).getCity()+", "+localizations.get(i).getCountry());
            localizationsValues.add(localizations.get(i).getWoeid());
        }
        chooseLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
        chooseLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
        deleteLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
        deleteLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
        initSummary(getPreferenceScreen());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        localizations = database.localizationDAO().findAll();
        localizationsEntries = new ArrayList<>();
        localizationsValues = new ArrayList<>();
        for (int i=0; i<localizations.size(); i++) {
            localizationsEntries.add(localizations.get(i).getCity()+", "+localizations.get(i).getCountry());
            localizationsValues.add(localizations.get(i).getWoeid());
        }
        chooseLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
        chooseLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
        deleteLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
        deleteLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (!Connection.isOnline(this)) {
            Toast.makeText(this, "No internet connection!\nYou can't change settings!.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (editTextPref.getKey().equals("add_localization")) {
                String loc = editTextPref.getText();
                loc = loc.replaceAll(" ", "%20");
                entries = loc.split(",");
                if (entries.length == 2) {
                    System.out.println(entries[0]);
                    System.out.println(entries[1]);
                    addLocalization();
                }
            }
        }
        else if (p instanceof ListPreference){
            ListPreference listPref = (ListPreference) p;
            if (listPref.getKey().equals("choose_localization")) {
                for (int i = 0; i< localizationsEntries.size(); i++) {
                    if (localizationsEntries.get(i) == listPref.getValue()) {
                        index = i;
                        chooseLocalization();
                        break;
                    }
                }
                p.setSummary(localization.getCity() + ", " + localization.getCountry());
                longitude.setText(Double.toString(localization.getLongitude()));
                longitude.setSummary(Double.toString(localization.getLongitude()));
                latitude.setText(Double.toString(localization.getLatitude()));
                latitude.setSummary(Double.toString(localization.getLatitude()));
            }
            else if (listPref.getKey().equals("delete_localization")) {
                for (int i = 0; i< localizationsEntries.size(); i++) {
                    if (localizationsEntries.get(i) == listPref.getValue()) {
                        index = i;
                        deleteLocalization();
                        break;
                    }
                }
                p.setSummary(" ");
            }
            else if (listPref.getKey().equals("refresh_interval")) {
                p.setSummary(listPref.getValue());
            }
        }
    }

    private void chooseLocalization() {
        if (!Connection.isOnline(this)) {
            Toast.makeText(this, "No internet connection!\nYou can't change settings!.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                localization = database.localizationDAO().findByWOEID(localizationsValues.get(index));
                database.localizationDAO().resetChoosen();
                database.localizationDAO().setChoosen(localization.getWoeid());
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            // nothing to do
        }
    }

    private void deleteLocalization() {
        if (!Connection.isOnline(this)) {
            Toast.makeText(this, "No internet connection!\nYou can't change settings!.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                localization = database.localizationDAO().findByWOEID(localizationsValues.get(index));
                if (localization.getChoosen() == false) {
                    database.localizationDAO().deleteByWOEID(localizationsValues.get(index));
                    localizations = database.localizationDAO().findAll();
                    localizationsEntries = new ArrayList<>();
                    localizationsValues = new ArrayList<>();
                    for (int i=0; i<localizations.size(); i++) {
                        localizationsEntries.add(localizations.get(i).getCity()+", "+localizations.get(i).getCountry());
                        localizationsValues.add(localizations.get(i).getWoeid());
                    }
                    chooseLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
                    chooseLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
                    deleteLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
                    deleteLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
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

    private void addLocalization() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalizationInfo localizationInfo = YahooWeather.getLocalizationInfo(entries[0], entries[1]);
                    Localization localization = new Localization(localizationInfo);
                    localization.setChoosen(true);
                    database.localizationDAO().addLocalization(localization);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                localizations = database.localizationDAO().findAll();
                localizationsEntries = new ArrayList<>();
                localizationsValues = new ArrayList<>();
                for (int i=0; i<localizations.size(); i++) {
                    localizationsEntries.add(localizations.get(i).getCity()+", "+localizations.get(i).getCountry());
                    localizationsValues.add(localizations.get(i).getWoeid());
                }
                chooseLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
                chooseLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
                deleteLocalization.setEntries(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
                deleteLocalization.setEntryValues(localizationsEntries.toArray(new CharSequence[localizationsEntries.size()]));
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
