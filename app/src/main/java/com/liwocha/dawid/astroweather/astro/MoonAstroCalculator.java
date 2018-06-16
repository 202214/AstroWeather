package com.liwocha.dawid.astroweather.astro;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MoonAstroCalculator {
    private AstroCalculator.MoonInfo moonInfo;

    public MoonAstroCalculator(double latitude, double longitude) {
        AstroCalculator.Location location = new AstroCalculator.Location(latitude, longitude);
        AstroDateTime astroDateTime = new AstroDateTime(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH)+1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND),
                Calendar.getInstance().get(Calendar.ZONE_OFFSET)/3600000,
                true);
        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);
        moonInfo = astroCalculator.getMoonInfo();
    }

    public String getMoonRise() {
        AstroDateTime moonRise = moonInfo.getMoonrise();
        return moonRise.toString().substring(11);
    }

    public String getMoonSet() {
        AstroDateTime moonSet = moonInfo.getMoonset();
        return moonSet.toString().substring(11);
    }

    public String getNextFullMoon() {
        AstroDateTime nextFullMoon = moonInfo.getNextFullMoon();
        return nextFullMoon.toString();
    }

    public String getNextNewMoon() {
        AstroDateTime nextNewMoon = moonInfo.getNextNewMoon();
        return nextNewMoon.toString();
    }

    public String getMoonPhase() {
        return String.format("%.2f", moonInfo.getIllumination())+"%";
    }

    public String getSynodDay() {
        return String.format("%.2f", moonInfo.getAge());
    }

    /*private String getFormattedTime(AstroDateTime astroDateTime) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss z");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(astroDateTime.getYear(), astroDateTime.getMonth(), astroDateTime.getDay(), astroDateTime.getHour(), astroDateTime.getMinute(), astroDateTime.getSecond());
        return format.format(date);
    }

    private String getFormattedDate(AstroDateTime astroDateTime) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date(astroDateTime.getYear(), astroDateTime.getMonth(), astroDateTime.getDay(), astroDateTime.getHour(), astroDateTime.getMinute(), astroDateTime.getSecond());
        return format.format(date);
    }*/
}
