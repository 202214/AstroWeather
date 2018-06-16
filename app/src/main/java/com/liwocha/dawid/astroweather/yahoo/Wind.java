package com.liwocha.dawid.astroweather.yahoo;

public class Wind {

    private int chill;

    private int direction;

    private double speed;

    public Wind(int chill, int direction, double speed) {
        this.chill = chill;
        this.direction = direction;
        this.speed = speed;
    }

    public int getChill() {
        return chill;
    }

    public int getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

}
