package com.callibrity.conductor.teapot.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class TeapotServiceTests {
    @Test void newServiceShouldBeOff() {
        TeapotService service = new TeapotService(new Teapot(1, 1, 1));
        var status = service.getStatus();
        assertThat(status.getAsOf(), notNullValue());
        assertThat(status.getState(), equalTo(TeapotState.OFF.toString()));
    }

    @Test void brewOneCupShouldReturnACup() {
        TeapotService service = new TeapotService(new Teapot(0, 0, 0));
        service.setupTeapot(TeapotState.READY, 2);
        var result = service.brewTea(1, null);
        assertThat(result.getCupsMade(), equalTo(1));
    }
}
