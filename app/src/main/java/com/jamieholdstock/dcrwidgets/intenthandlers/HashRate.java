package com.jamieholdstock.dcrwidgets.intenthandlers;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class HashRate {

    private static final NavigableMap<Double, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000d, "k");
        suffixes.put(1_000_000d, "M");
        suffixes.put(1_000_000_000d, "G");
        suffixes.put(1_000_000_000_000d, "T");
        suffixes.put(1_000_000_000_000_000d, "P");
        suffixes.put(1_000_000_000_000_000_000d, "E");
        suffixes.put(1_000_000_000_000_000_000_000d, "Z");
        suffixes.put(1_000_000_000_000_000_000_000_000d, "Y");
    }

    private double rate;

    public HashRate(double rate) {
        this.rate = rate;
    }

    public String format() {
        if (rate < 1000) {
            int iRate = (int) rate;
            return Integer.toString(iRate);
        }

        Map.Entry<Double, String> e = suffixes.floorEntry(rate);
        Double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = (rate / (divideBy / 10d)) / 10d;

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(truncated) + " " + suffix;
    }
}
