package com.callibrity.teapot.rest;

import com.callibrity.teapot.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;


@Controller @Slf4j
public class TeapotController {
    private final TeapotJokeService teapotJokeService;
    private final TeapotService teapotService;

    public TeapotController(TeapotJokeService teapotJokeService, TeapotService teapotService) {
        this.teapotService = teapotService;
        this.teapotJokeService = teapotJokeService;
    }

    @GetMapping
    public ResponseEntity<JokeResponse> getRoot() {
        return new ResponseEntity<>(new JokeResponse("I am a teapot."), HttpStatus.I_AM_A_TEAPOT);
    }

    @GetMapping("/joke")
    public ResponseEntity<JokeResponse> getRandomJoke() {
        return new ResponseEntity<>(new JokeResponse(teapotJokeService.getRandomJoke()), HttpStatus.OK);
    }
    @GetMapping("/joke/{index}")
    public ResponseEntity<JokeResponse> getSpecificJoke(@PathVariable Integer index) {
        try {
            return new ResponseEntity<>(new JokeResponse(teapotJokeService.getSpecificJoke(index)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new JokeResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tea")
    public ResponseEntity<ServiceStatus> getStatus() {
        return new ResponseEntity<>(teapotService.getStatus(), HttpStatus.OK);
    }

    @PostMapping("/tea")
    public ResponseEntity<TeapotResponse> brewTea(@RequestBody TeapotRequest teapotRequest) {
        BrewCallback notifyWhenDone = (BrewResult brewResult) -> {
            try (CloseableHttpClient instance = HttpClients.createDefault();
                CloseableHttpResponse response = instance.execute(new HttpGet(teapotRequest.getUri())))
            {
                log.info("Client notified with status: {}, http status {}", brewResult.getStatus(), response.getStatusLine().getStatusCode());
            } catch (IOException ex) {
                log.warn("IOException when notifying client.");
            }
        };
        var brewTeaResult = teapotService.brewTea(teapotRequest.getCupsRequested(),
                StringUtils.hasLength(teapotRequest.getUri()) ? notifyWhenDone : null);
        var teapotResponse = new TeapotResponse();
        teapotResponse.setMessage(brewTeaResult.getStatus());
        return new ResponseEntity<>(teapotResponse, HttpStatus.OK);
    }
}
