package com.callibrity.conductor.teapot.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data public class BrewResult {
    public static final String BREW_COMPLETE = "COMPLETE";
    public static final String BREW_IN_PROGRESS = "IN_PROGRESS";
    public static final String ORDER_TOO_LARGE = "ORDER_TOO_LARGE";

    private LocalDateTime asOf = LocalDateTime.now();
    private String status;
    private int cupsMade = 0;

    public BrewResult() {}
    public BrewResult(String status, int cupsMade) {this.status = status; this.cupsMade = cupsMade;}
}
