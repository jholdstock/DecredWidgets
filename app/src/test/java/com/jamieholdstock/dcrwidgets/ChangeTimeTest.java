package com.jamieholdstock.dcrwidgets;

import com.jamieholdstock.dcrwidgets.intenthandlers.ChangeTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class ChangeTimeTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0d, "0m 0s" },
                { 0.0d, "0m 0s" },
                { 5.5d, "0m 5s" },
                { 59.5d, "0m 59s" },
                { 71.9d, "1m 11s" },
                { 659.5d, "10m 59s" },
                { 660.9d, "11m 0s" },
                { 3900d, "1h 5m" },
                { 3959d, "1h 5m" },
                { 46800, "13h 0m" },
                { 47976, "13h 19m" },
        });
    }

    private double input;
    private String expected;

    public ChangeTimeTest(double input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        String actual = new ChangeTime(input).format();
        Assert.assertEquals(expected, actual);
    }
}
