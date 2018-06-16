package com.liwocha.dawid.astroweather.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocalizationDAO {

    @Query("SELECT * FROM Localization")
    public List<Localization> findAll();

    @Query("SELECT * FROM Localization WHERE woeid = :woeid")
    public Localization findByWOEID(int woeid);

    @Query("SELECT * FROM Localization WHERE choosen = 1")
    public Localization findByChoosen();

    @Query("UPDATE Localization SET choosen = 0")
    public void resetChoosen();

    @Query("UPDATE Localization SET choosen = 1 WHERE woeid = :woeid")
    public void setChoosen(int woeid);

    @Insert
    public void addLocalization(Localization localization);

    @Query("DELETE FROM Localization WHERE woeid = :woeid")
    public void deleteByWOEID(int woeid);

}
