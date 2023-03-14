package com.callibrity.conductor.teapot.rest;

import com.callibrity.conductor.teapot.domain.TeapotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TeapotController {
   @Autowired private final TeapotService teapotService;

    public TeapotController(TeapotService teapotService) {
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
}
