package com.jamieholdstock.dcrwidgets.intenthandlers;

public class ChangeTime {

    private double seconds;

    public ChangeTime(double seconds) {
        this.seconds = seconds;
    }

    public String format() {
        double hours = Math.floor(seconds / (60 * 60));

        double divisor_for_minutes = seconds % (60 * 60);
        double minutes = Math.floor(divisor_for_minutes / 60);

        seconds = Math.floor(seconds % 60);

        String sHours = Integer.toString((int) hours);
        String sMinutes = Integer.toString((int) minutes);
        String sSeconds = Integer.toString((int) seconds);

        if (hours < 1) {
            return sMinutes + "m " + sSeconds + "s";
        } else {
            return sHours + "h " + sMinutes + "m";
        }
    }
}
