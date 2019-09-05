package com.hotelbeds.supplierintegrations.hackertest.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AttemptsPerIP {
    private String ip;
    private List<LogLine> attempts = new ArrayList<>();
}
