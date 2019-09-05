package com.hotelbeds.supplierintegrations.hackertest.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
public class LogLineParser {

    /**
     * Parse a line with format "ip,timestamp,ACTION,username"
     *
     * @param line to be parsed
     * @return {@link LogLine}
     * @throws IllegalArgumentException - either if line is empty,
     *                                  or if it does not have exactly 4 comma-delimited elements, or if any element does not parse correctly.
     */
    public LogLine parseLoginAttempt(String line) {
        log.debug("Parsing line: {}", line);
        if (StringUtils.isEmpty(line)) {
            throw new IllegalArgumentException();
        }

        String[] chunks = line.split(",");

        validateLineFormat(chunks);

        return doParseLine(chunks);
    }


    private LogLine doParseLine(String[] chunks) {
        LogLine parsed = new LogLine();

        parsed.setIp(chunks[0]);
        Long secondsDate = Long.parseLong(chunks[1]);
        LocalDateTime timestamp =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(secondsDate), TimeZone.getDefault().toZoneId());
        parsed.setTimestamp(timestamp);
        parsed.setLogLineAction(LogLineAction.valueOf(chunks[2]));
        parsed.setUsername(chunks[3]);

        log.debug("Log line parsed: {}", parsed);
        return parsed;
    }


    private void validateLineFormat(String[] chunks) {
        if (chunks.length != 4) {
            throw new IllegalArgumentException();
        }
    }
}
