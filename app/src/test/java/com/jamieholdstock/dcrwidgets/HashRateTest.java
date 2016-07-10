package com.jamieholdstock.dcrwidgets;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class HashRateTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0d, "0" },
                { 0.0d, "0" },
                { 999.01d, "999" },
                { 999d, "999" },
                { 1000d, "1.00 k" },
                { 9994d, "9.99 k" },
                { 10999d, "11.00 k" },
                { 110989d, "110.99 k" },
                { 91110999d, "91.11 M" },
                { 691110999d, "691.11 M" },
                { 1691110999d, "1.69 G" },
                { 5451691110999d, "5.45 T" },
                { 125451691110999d, "125.45 T" },
                { 1231235451691110999d, "1.23 E" },
        });
    }

    private double input;
    private String expected;

    public HashRateTest(double input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        String actual = new HashRate(input).format();
        Assert.assertEquals(expected, actual);
    }
}
