package com.hotelbeds.supplierintegrations.hackertest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Java representation of a log line
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogLine {
    private String ip;
    private LocalDateTime timestamp;
    private LogLineAction logLineAction;
    private String username;
}
