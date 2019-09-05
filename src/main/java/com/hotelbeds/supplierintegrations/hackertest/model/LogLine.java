package com.hotelbeds.supplierintegrations.hackertest.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Java representation of a log line
 */
@Data
public class LogLine {
    private final String ip;
    private final LocalDateTime timestamp;
    private final LogLineAction logLineAction;
    private final String username;
}
