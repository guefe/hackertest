package com.hotelbeds.supplierintegrations.hackertest.model;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;


public class LogLineParserTest {

    private LogLineParser testee = new LogLineParser();
    private String lineToParse = "80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith";
    private String wrongFormatLine = "80.238.9.179,133612947,SIGNIN_SUCCESS";


    @Test
    public void should_parse_line_correctly() {
        LogLine expected =
                new LogLine(
                        "80.238.9.179",
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(133612947), TimeZone.getDefault().toZoneId()),
                        LogLineAction.SIGNIN_SUCCESS,
                        "Will.Smith");

        assertEquals(expected, testee.parseLoginAttempt(lineToParse));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_empty_line() {
        testee.parseLoginAttempt("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_null_line() {
        testee.parseLoginAttempt(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_incorrect_format_line() {
        testee.parseLoginAttempt(wrongFormatLine);
    }
}