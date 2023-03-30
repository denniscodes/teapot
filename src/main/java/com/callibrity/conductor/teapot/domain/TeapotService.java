package com.callibrity.conductor.teapot.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.Thread.sleep;


@Service @Slf4j
public class TeapotJokeService {

    public final Teapot teapot;

    public TeapotService(Teapot teapot) {
        this.teapot = teapot;
    }
    public ServiceStatus getStatus() {
        ServiceStatus status = new ServiceStatus();
        status.setState(teapot.getState().toString());
        status.setCupCapacity(Teapot.TEAPOT_MAX_CAPACITY);
        status.setCupsRemaining(teapot.getAvailableCups());
        return status;
    }

    public BrewResult brewTea(int cupCount, BrewCallback optionalCallback) {
        BrewResult result = new BrewResult();
        if (cupCount > Teapot.TEAPOT_MAX_CAPACITY) {
            result.setStatus(BrewResult.ORDER_TOO_LARGE);
        } else if (cupCount == 1 && teapot.getState().equals(TeapotState.READY)) {
            teapot.pourCup();
            result.setStatus(BrewResult.BREW_COMPLETE);
            result.setCupsMade(1);
        } else {
            result.setStatus(BrewResult.BREW_IN_PROGRESS);
            startAsyncBrewProcess(cupCount,optionalCallback);
        }
        return result;
    }

    /**
     * Helper method for testing,
     * @param state desired state
     * @param currentCups desired cups available
     */
    void setupTeapot(TeapotState state, int currentCups) {
        teapot.configure(state, currentCups);
    }

    /**
     * Create a new thread to brew cups of tea.
     * @param cupCount number of cups to brew.
     * @param optionalCallback callback function for notification of completion.
     */
    private void startAsyncBrewProcess(int cupCount, BrewCallback optionalCallback) {
        var brewThread = new Thread(() -> {
            for (int cup = 0; cup< cupCount; cup++) {
                if (!teapot.getState().equals(TeapotState.READY))
                    makeTeapotReady();
                teapot.pourCup();
            }
            if (optionalCallback != null) optionalCallback.callback(new BrewResult(BrewResult.BREW_COMPLETE, cupCount));
        }, "brewThread");
        brewThread.start();
    }

    private boolean makeTeapotReady()  {
        try {
            if (teapot.getState().equals(TeapotState.OFF)) {
                teapot.turnOn();
                return makeTeapotReady();
            } else if (teapot.getState().equals(TeapotState.EMPTY)) {
                teapot.fillPot();
                while (!teapot.getState().equals(TeapotState.FULL))
                    sleep(1000);
                return makeTeapotReady();
            } else if (teapot.getState().equals(TeapotState.FULL)) {
                teapot.heatWater();
                while (!teapot.getState().equals(TeapotState.READY))
                    sleep(1000);
            } else {
                log.warn("Unexpected state: {}", teapot.getState().toString());
            }
        } catch (InterruptedException ex) {
            log.warn(ex.getMessage());
        }
        return false;
    }
}
