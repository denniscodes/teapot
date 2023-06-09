package com.callibrity.teapot.rest;

import com.callibrity.teapot.domain.Joke;
import lombok.Data;

@Data
public class JokeResponse {
    public JokeResponse(Joke joke) {
        this.joke = joke;
    }

    public JokeResponse(String message) {
        this.message = message;
    }
    private Joke joke;
    private String message;
}
