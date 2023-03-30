package com.callibrity.conductor.teapot.rest;

import com.callibrity.conductor.teapot.domain.BrewCallback;
import com.callibrity.conductor.teapot.domain.BrewResult;
import com.callibrity.conductor.teapot.domain.TeapotJokeService;
import com.callibrity.conductor.teapot.domain.TeapotService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;


@Controller @Slf4j
public class TeapotController {
   @Autowired private final TeapotJokeService teapotService;

    public TeapotController(TeapotJokeService teapotService) {
        this.teapotService = teapotService;
    }

    @GetMapping
    public ResponseEntity<JokeResponse> getRoot() {
        return new ResponseEntity<>(new JokeResponse("I am a teapot."), HttpStatus.I_AM_A_TEAPOT);
    }

    @GetMapping("/joke")
    public ResponseEntity<JokeResponse> getRandomJoke() {
        return new ResponseEntity<>(new JokeResponse(teapotService.getRandomJoke()), HttpStatus.OK);
    }
    @GetMapping("/joke/{index}")
    public ResponseEntity<JokeResponse> getSpecificJoke(@PathVariable Integer index) {
        try {
            return new ResponseEntity<>(new JokeResponse(teapotService.getSpecificJoke(index)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new JokeResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tea")
    public ResponseEntity<TeapotResponse> brewTea(TeapotRequest teapotRequest) {
        BrewCallback notifyWhenDone = (BrewResult brewResult) -> {
            try (CloseableHttpClient instance = HttpClients.createDefault();
                CloseableHttpResponse response = instance.execute(new HttpGet(teapotRequest.getUri())))
            {
                log.info("Client notified with status: {}", brewResult.getStatus());

            } catch (IOException ex) {
                log.warn("IOException when notifying client.");
            }
        };
        var brewTeaResult = teapotService.brewTea(teapotRequest.getCupsRequested(),
                StringUtils.isNotBlank(teapotRequest.getUri()) ? notifyWhenDone : null);
        var teapotResponse = new TeapotResponse();
        teapotResponse.setMessage(brewTeaResult.getStatus());
        return new ResponseEntity<>(teapotResponse, HttpStatus.OK);
    }
}
