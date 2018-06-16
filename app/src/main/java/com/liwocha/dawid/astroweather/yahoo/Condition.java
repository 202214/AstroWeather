package com.liwocha.dawid.astroweather.yahoo;

import java.util.Date;

public class Condition {

    private int temperature;

    private int code;

    private String desc;

    private Date date;

    public Condition(int temperature, String desc, int code, Date date) {
        this.temperature = temperature;
        this.desc = desc;
        this.code = code;
        this.date = date;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

}
