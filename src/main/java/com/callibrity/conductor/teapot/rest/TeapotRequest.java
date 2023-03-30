package com.callibrity.conductor.teapot.rest;

import lombok.Data;

@Data
public class TeapotRequest {
    private int cupsRequested;
    private String uri;
}
