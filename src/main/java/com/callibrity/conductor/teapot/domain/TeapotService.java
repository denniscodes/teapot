package com.callibrity.conductor.teapot.domain;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class TeapotService {
    private final List<Joke> jokes;
    private final Random random = new Random();

    public TeapotService() {
        jokes = Arrays.asList(new Joke("What starts with T, ends with T, but only has T in it?", "Teapot"),
                new Joke("Why does a teapot whistle when it’s boiling?", "Because it’s telighted!"),
                new Joke("What do you call a teapot of boiling water on top of Mount Everest?", "A HIGH-POT-IN-USE"),
                new Joke("What language do teapots speak?", "Teabrew"));
    }

    public Joke getRandomJoke() {
        return jokes.get(random.nextInt(jokes.size()));
    }

    public Joke getSpecificJoke(int index) {
        if (index >= 0 && index < jokes.size()) return jokes.get(index);
        else {
            throw new IllegalArgumentException(String.format("Index must be between 0 and %d.", jokes.size()-1));
        }
    }
}
