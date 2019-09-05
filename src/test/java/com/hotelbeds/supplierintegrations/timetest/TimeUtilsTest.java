package com.hotelbeds.supplierintegrations.timetest;

import org.junit.Test;

import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;

public class TimeUtilsTest {

    @Test
    public void should_return_5_same_timezone() {
        String time1 = "Thu, 21 Dec 2000 17:01:07 +0100";
        String time2 = "Thu, 21 Dec 2000 16:06:08 +0000";
        assertEquals(5, TimeUtils.minutesBetween(time1, time2));
    }

    @Test
    public void should_return_65_different_timezone() {
        String time1 = "Thu, 21 Dec 2000 17:01:07 +0100";
        String time2 = "Thu, 21 Dec 2000 17:06:08 +0000";
        assertEquals(65, TimeUtils.minutesBetween(time1, time2));

    }

    @Test(expected = DateTimeParseException.class)
    public void wrong_format_exception() {
        String time1 = "21 Dec 2000 16:01:07 +0000";
        String time2 = "Thu, 21 Dec 2000 16:01:07 +0000";
        TimeUtils.minutesBetween(time1, time2);

    }

    @Test(expected = IllegalArgumentException.class)
    public void empty_argument_exception() {
        String time1 = "";
        String time2 = "Thu, 21 Dec 2000 16:01:07 +0000";
        TimeUtils.minutesBetween(time1, time2);
    }

}