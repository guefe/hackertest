package com.hotelbeds.supplierintegrations.hackertest.detector.impl;

import com.hotelbeds.supplierintegrations.hackertest.detector.HackerDetector;
import com.hotelbeds.supplierintegrations.hackertest.lineparser.LogLineParser;
import com.hotelbeds.supplierintegrations.hackertest.model.AttemptsPerIP;
import com.hotelbeds.supplierintegrations.hackertest.model.LogLine;
import com.hotelbeds.supplierintegrations.hackertest.model.LogLineAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MyHackerDetectorImpl implements HackerDetector {

    private LogLineParser logLineParser;
    private Map<String, AttemptsPerIP> ongoingIpChecks = new HashMap<>();

    public MyHackerDetectorImpl(@Autowired LogLineParser parser) {
        this.logLineParser = parser;
    }

    /**
     * Analyses a log line in String form, deciding whether the IP is suspicious or not.
     * Internally, a Map<String, AttemptsPerIP> is used for tracking the activity of every registered IP.
     * After each invocation, the map is purged of registers older than current line timestamp minus 5min.
     *
     * @param line - String representation of a log line to be parsed and analysed
     * @return String representing IP with suspicious activity or null if no suspicious activity has been observed
     */
    @Override
    public String parseLine(String line) {
        log.info("Analysing new log line: {}", line);
        LogLine newLoginAttempt = logLineParser.parseLoginAttempt(line);
        String suspiciousIp = null;

        if (newLoginAttempt.getLogLineAction().equals(LogLineAction.SIGNIN_FAILURE)) {
            if (ongoingIpChecks.containsKey(newLoginAttempt.getIp())) {
                suspiciousIp = processFailedAttemptForExistingIp(newLoginAttempt);
            } else {
                processFailedAttemptForNewIp(newLoginAttempt);
            }
        }

        purgeOldIpChecks(newLoginAttempt.getTimestamp());

        return suspiciousIp;
    }

    private String processFailedAttemptForExistingIp(LogLine newLoginAttempt) {
        AttemptsPerIP attemptsPerIP = ongoingIpChecks.get(newLoginAttempt.getIp());
        LogLine firstLoginAttempt = attemptsPerIP.getAttempts().get(0);
        if (attemptsPerIP.getAttempts().size() == 4
                && ChronoUnit.MINUTES.between(firstLoginAttempt.getTimestamp(), newLoginAttempt.getTimestamp()) < 5) {
            log.info("Detected suspicious activity from IP {}. Reason: Number of failed login attempts exceeded",
                    attemptsPerIP.getIp());

            return attemptsPerIP.getIp();
        } else {
            attemptsPerIP.getAttempts().add(newLoginAttempt);
            ongoingIpChecks.put(attemptsPerIP.getIp(), attemptsPerIP);
            return null;
        }
    }

    private void processFailedAttemptForNewIp(LogLine logLine) {
        if (logLine.getLogLineAction().equals(LogLineAction.SIGNIN_FAILURE)) {
            AttemptsPerIP attemptsPerIP = new AttemptsPerIP(logLine.getIp());
            attemptsPerIP.getAttempts().add(logLine);
            ongoingIpChecks.put(attemptsPerIP.getIp(), attemptsPerIP);
        }
    }

    private void purgeOldIpChecks(LocalDateTime timestamp) {
        ongoingIpChecks.forEach((ip, attempts) -> {
            log.debug("Checks for {} before purging: {}", ip, attempts.getAttempts());
            List<LogLine> purged = attempts.getAttempts().stream()
                    .filter(attempt -> ChronoUnit.MINUTES.between(attempt.getTimestamp(), timestamp) <= 5)
                    .collect(Collectors.toList());
            attempts.getAttempts().clear();
            attempts.getAttempts().addAll(purged);
            log.debug("Checks for {} after purging: {}", ip, attempts.getAttempts());
        });
        ongoingIpChecks.values().removeIf(elem -> elem.getAttempts().isEmpty());
    }




/*    private boolean lessThan5MinutesDifference(LocalDateTime from, LocalDateTime to){
        if (ChronoUnit.MINUTES.between(from, to) > 5 ){
            return true;
        } else if (ChronoUnit.MINUTES.between(from, to) == 5 ){
            if (ChronoUnit.SECONDS.between(from, to) > 5 )
        }
    }*/
}
