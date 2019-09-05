package com.hotelbeds.supplierintegrations.hackertest.detector.impl;

import com.hotelbeds.supplierintegrations.hackertest.model.LogLineParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MyHackerDetectorImplTest {

    private MyHackerDetectorImpl hackerDetector;
    private String logLine = "80.238.9.179,1336129471,SIGNIN_FAILURE,Will.Smith";
    private LogLineParser parser = new LogLineParser();

    @Before
    public void prepare() {
        hackerDetector = new MyHackerDetectorImpl(parser);
    }

    @Test
    public void should_return_null_first_invocation() {
        assertNull(hackerDetector.parseLine(logLine));
    }

    @Test
    public void should_return_ip_after_five_invocations() {
        assertNull(hackerDetector.parseLine(logLine));
        assertNull(hackerDetector.parseLine(logLine));
        assertNull(hackerDetector.parseLine(logLine));
        assertNull(hackerDetector.parseLine(logLine));
        assertEquals("80.238.9.179", hackerDetector.parseLine(logLine));
    }

    @Test
    public void should_return_ip_fifth_invocation_less_than_5min_from_2nd_call() {
        String firstLogLine = "80.238.9.179,1236129471,SIGNIN_FAILURE,Ages.Ago";
        String secondLogLine = "80.238.9.179,1336136851,SIGNIN_FAILURE,Will.Smith";
        String logLineMoreThan5min = "80.238.9.179,1336136951,SIGNIN_FAILURE,Will.Smith";
        assertNull(hackerDetector.parseLine(firstLogLine));
        assertNull(hackerDetector.parseLine(secondLogLine));
        assertNull(hackerDetector.parseLine(secondLogLine));
        assertNull(hackerDetector.parseLine(secondLogLine));
        assertNull(hackerDetector.parseLine(secondLogLine));
        assertEquals("80.238.9.179", hackerDetector.parseLine(logLineMoreThan5min));
    }

    @Test
    public void should_return_null_fifth_invocation_later_than_5min() {
        String logLineMoreThan5min = "80.238.9.179,1336137031,SIGNIN_FAILURE,Will.Smith";
        assertNull(hackerDetector.parseLine(logLine));
        assertNull(hackerDetector.parseLine(logLine));
        assertNull(hackerDetector.parseLine(logLine));
        assertNull(hackerDetector.parseLine(logLine));
        assertNull(hackerDetector.parseLine(logLineMoreThan5min));
    }


}