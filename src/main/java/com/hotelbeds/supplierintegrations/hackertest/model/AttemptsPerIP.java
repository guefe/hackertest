package com.hotelbeds.supplierintegrations.hackertest.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AttemptsPerIP {
    private final String ip;
    private final List<LogLine> attempts = new ArrayList<>();
}
