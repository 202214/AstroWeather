package com.liwocha.dawid.astroweather.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Localization.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LocalizationDAO localizationDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "localization-database2")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void initialize(AppDatabase database) {
        Localization localization = new Localization();
        localization.setWoeid(505120);
        localization.setCity("Lodz");
        localization.setCountry("Poland");
        localization.setLongitude(19.46801);
        localization.setLatitude(51.761742);
        localization.setChoosen(true);
        try {
            database.localizationDAO().addLocalization(localization);
        } catch (Exception e) {
        }
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
