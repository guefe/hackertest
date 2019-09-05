package com.hotelbeds.supplierintegrations.hackertest.lineparser;

import com.hotelbeds.supplierintegrations.hackertest.model.LogLine;
import com.hotelbeds.supplierintegrations.hackertest.model.LogLineAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@Component
public class LogLineParser {

    /**
     * Parse a line with format "ip,date,action,username"
     *
     * @param line to be parsed
     * @return {@link LogLine}
     * @throws IllegalArgumentException - either if line is empty,
     *   or if it does not have exactly 4 comma-delimited elements, or if any element does not parse correctly.
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
        Long secondsDate = Long.parseLong(chunks[1]);
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(secondsDate), TimeZone.getDefault().toZoneId());

        LogLine parsed = new LogLine(chunks[0], dateTime, LogLineAction.valueOf(chunks[2]), chunks[3]);

        log.debug("Log line parsed: {}", parsed);
        return parsed;
    }


    private void validateLineFormat(String[] chunks) {
        if (chunks.length != 4) {
            throw new IllegalArgumentException();
        }
    }
}
