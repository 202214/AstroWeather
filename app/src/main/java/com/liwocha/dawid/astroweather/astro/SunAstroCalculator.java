package com.liwocha.dawid.astroweather.astro;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;

public class SunAstroCalculator {
    private AstroCalculator.SunInfo sunInfo;

    public SunAstroCalculator(double latitude, double longitude) {
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
        sunInfo = astroCalculator.getSunInfo();
    }

    public String getSunRiseTime() {
        AstroDateTime sunRiseTime = sunInfo.getSunrise();
        return sunRiseTime.toString().substring(11);
    }

    public String getSunRiseAzimuth() {
        double sunRiseAzimuth = sunInfo.getAzimuthRise();
        return Double.toString(sunRiseAzimuth).substring(11);
    }

    public String getSunSetTime() {
        AstroDateTime sunSetTime = sunInfo.getSunset();
        return sunSetTime.toString().substring(11);
    }

    public String getSunSetAzimuth() {
        double sunSetAzimuth = sunInfo.getAzimuthSet();
        return Double.toString(sunSetAzimuth).substring(11);
    }

    public String getSunTwilightEvening() {
        AstroDateTime sunTwilightEvening = sunInfo.getTwilightEvening();
        return sunTwilightEvening.toString().substring(11);
    }

    public String getSunTwilightMorning() {
        AstroDateTime sunTwilightMorning = sunInfo.getTwilightMorning();
        return sunTwilightMorning.toString().substring(11);
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
