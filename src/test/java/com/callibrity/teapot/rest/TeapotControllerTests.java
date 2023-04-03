package com.callibrity.teapot.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class TeapotControllerTests {
    @Autowired private MockMvc mockMvc;

    //public TeapotControllerTests(MockMvc mockMvc) {this.mockMvc = mockMvc;}

    @Test
    public void defaultShouldReturnTeapotCode() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isIAmATeapot());
    }

    @Test
    public void jokesAreReturned() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/joke").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.joke").exists());
    }
}
