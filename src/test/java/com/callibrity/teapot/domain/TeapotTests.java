package com.callibrity.teapot.domain;

import com.callibrity.teapot.domain.Teapot;
import com.callibrity.teapot.domain.TeapotState;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TeapotTests {
    @Test void teapotShouldTakeTimeToFill() throws InterruptedException {
        var teapot = new Teapot(2, 2, 1);
        assertThat(teapot.getState(),equalTo(TeapotState.OFF));
        assertThat(teapot.turnOn(), equalTo(true));
        assertThat(teapot.fillPot(), equalTo(true));
        assertThat(teapot.getState(), equalTo(TeapotState.FILLING));
        sleep(2000+500);
        assertThat(teapot.getState(), equalTo(TeapotState.FULL));
    }

    @Test void teapotShouldEventuallyBoil() throws InterruptedException {
        var teapot = new Teapot(2, 1, 1);
        assertThat(teapot.getState(), equalTo(TeapotState.OFF));
        teapot.turnOn();
        teapot.fillPot();
        sleep(1000+500);
        assertThat(teapot.getState(), equalTo(TeapotState.FULL));
        assertThat(teapot.heatWater(), equalTo(true));
        assertThat(teapot.getState(), equalTo(TeapotState.HEATING));
        sleep(2000+500);
        assertThat(teapot.getState(), equalTo(TeapotState.READY));
    }

    @Test void teapotShouldPourCups() throws InterruptedException {
        var teapot = new Teapot(1, 1, 1);
        teapot.turnOn();
        teapot.fillPot();
        sleep(1000+ 500);
        teapot.heatWater();
        sleep(1000+500);
        assertThat(teapot.getState(), equalTo(TeapotState.READY));
        int cups = 0;
        while(teapot.pourCup()) {
            ++cups;
        }
        assertThat(cups, equalTo(8));
        assertThat(teapot.getState(), equalTo(TeapotState.EMPTY));
    }
}
