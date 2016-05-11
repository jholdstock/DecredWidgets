package com.jamieholdstock.dcrwidgets.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStamp {
    private String stamp;

    public TimeStamp() {
        SimpleDateFormat df = new SimpleDateFormat("EEE HH:mm");
        Calendar now = Calendar.getInstance();
        stamp = "@" + df.format(now.getTime());
    }

    @Override
    public String toString() {
        return stamp;
    }

}
