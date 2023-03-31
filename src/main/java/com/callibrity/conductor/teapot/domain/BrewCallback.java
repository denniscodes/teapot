package com.callibrity.conductor.teapot.domain;

import com.callibrity.conductor.teapot.domain.BrewResult;

public interface BrewCallback {
    void callback(BrewResult result);
}
