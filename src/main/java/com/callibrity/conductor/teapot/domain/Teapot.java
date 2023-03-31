package com.callibrity.conductor.teapot.domain;

import lombok.extern.slf4j.Slf4j;

import static com.callibrity.conductor.teapot.domain.TeapotState.OFF;
import static java.lang.Thread.sleep;

@Slf4j
public class Teapot {
    public static int TEAPOT_MAX_CAPACITY = 8;
    private TeapotState state;

    private int boilSeconds;
    private int fillSeconds;
    private int pourSeconds;
    private int level = 0;

    public Teapot(int secondsToBoil, int secondToFill, int secondsToPour) {

        this.boilSeconds = secondsToBoil;
        this.fillSeconds = secondToFill;
        this.pourSeconds = secondsToPour;
        state = OFF;
    }

    public TeapotState getState() {return state;}
    public int getAvailableCups() {
        return level;
    }

    public boolean turnOn() {
        if (state.equals(OFF)) setState(TeapotState.EMPTY);
        level = 0;
        log.debug("Teapot is on, empty.");
        return state.equals(TeapotState.EMPTY);
    }

    public boolean turnOff() {
        state = OFF;
        log.debug("Teapot is off.");
        return state.equals(OFF);
    }
    public boolean heatWater() {
        if (state.equals(TeapotState.FULL))
            startHeaterTimer();
        log.debug("Teapot is heating.");
        return state.equals(TeapotState.HEATING);
    }

    public boolean fillPot() {
        if (state.equals(TeapotState.EMPTY)) {
            startFillerTimer();
            log.debug("Teapot is filling.");
        } else {
            log.warn("Expected empty pot, was: {}.", state.name());
            return false;
        }
        return state.equals(TeapotState.FILLING);
    }

    public boolean pourCup() {
        boolean success = false;
        if (state.equals(TeapotState.READY) && level > 0) {
            setState(TeapotState.BREWING);
            try {sleep(pourSeconds*1000);}
            catch (InterruptedException ex) {
                log.warn("Brewing interrupted.");
                return false;
            }
            decrementCups();
            setState(TeapotState.READY);
            success = true;
        }
        if (level < 1) setState(TeapotState.EMPTY);
        log.debug("Cup brewed. {} cups remaining.", level);
        return success;
    }

    /**
     * Helper method for testing,
     * @param state desired state
     * @param currentCups desired cups available
     */
    void configure(TeapotState state, int currentCups) {
        synchronized (this) {
            this.state = state;
            this.level = currentCups;
        }
    }

    /**
     * Thread safe removal of one cup.
     */
    private void decrementCups() {
        synchronized (this) {
            --level;
        }
    }

    /**
     * Thread-safe update of teapot state.
     * @param state
     */
    private void setState(TeapotState state) {
        synchronized (this) {
            this.state = state;
        }
    }
    private void startHeaterTimer() {
        setState(TeapotState.HEATING);
        var thread = (new Thread(() -> {
            try {
                sleep(boilSeconds*1000);
                log.debug("Water boiled.");
                setState(TeapotState.READY);
            }
            catch (InterruptedException e) {
                log.error("Teapot did not heat.");
            }
        }, "heater"));
        thread.start();
        log.debug("Heater started.");
    }
    private void startFillerTimer() {
        setState(TeapotState.FILLING);
        var thread = (new Thread(() -> {
            try {
                sleep(fillSeconds*1000);
                log.debug("teapot is full.");
                setLevel(TEAPOT_MAX_CAPACITY);
                setState(TeapotState.FULL);
            }
            catch (InterruptedException e) {
                log.error("Teapot did not fill.");
            }
        }, "filler"));
        thread.start();
        log.debug("Heater started.");
    }

    /**
     * Thread safe update of teapot capacity.
     * @param teapotCapacity
     */
    private void setLevel(int teapotCapacity) {
        synchronized (this) {
            level = teapotCapacity;
        }
    }
}
