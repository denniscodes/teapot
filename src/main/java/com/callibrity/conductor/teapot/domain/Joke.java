package com.callibrity.conductor.teapot.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class Joke {
    private final String setup;
    private final String punchline;
}
