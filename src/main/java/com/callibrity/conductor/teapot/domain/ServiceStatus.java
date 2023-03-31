package com.callibrity.conductor.teapot.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceStatus {
    private LocalDateTime asOf = LocalDateTime.now();
    private String state;
    private int cupCapacity;
    private int cupsRemaining;
}
