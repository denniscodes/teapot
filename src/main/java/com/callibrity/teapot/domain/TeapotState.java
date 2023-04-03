package com.callibrity.teapot.domain;

public enum TeapotState {
    OFF("Off"), EMPTY("Empty"), FILLING("Filling"), FULL("Full"), HEATING("Heating"), READY("Ready"), BREWING("Brewing");

    private String name;
    private TeapotState(String name) {
        this.name = name;
    }

}
