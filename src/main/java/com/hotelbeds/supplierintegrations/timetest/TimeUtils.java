package com.hotelbeds.supplierintegrations.timetest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Slf4j
public class TimeUtils {


    public static long minutesBetween(String time1, String time2) {
        if (StringUtils.isEmpty(time1) || StringUtils.isEmpty(time2)) {
            throw new IllegalArgumentException("Arguments time1 and time2 must not be empty");
        }
        log.debug("Calculating minutes between {} and {}", time1, time2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        ZonedDateTime dateTime1 = ZonedDateTime.from(formatter.parse(time1));
        ZonedDateTime dateTime2 = ZonedDateTime.from(formatter.parse(time2));
        return ChronoUnit.MINUTES.between(dateTime1, dateTime2);
    }
}
