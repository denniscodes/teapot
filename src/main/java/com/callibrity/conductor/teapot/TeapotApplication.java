package com.callibrity.conductor.teapot;

import com.callibrity.conductor.teapot.domain.TeapotService;
import com.callibrity.conductor.teapot.workers.TeapotWorker;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication @Slf4j
public class TeapotApplication {
    private static final String CONDUCTOR_SERVER_URL = "conductor.server.url";
    private static final String CONDUCTOR_CLIENT_KEY_ID = "conductor.security.client.key-id";
    private static final String CONDUCTOR_CLIENT_SECRET = "conductor.security.client.secret";

    @Autowired private TeapotService teapotService;
    public static void main(String[] args) throws IOException {
        SpringApplication.run(TeapotApplication.class, args);
    }

    @PostConstruct
    public void startWorkers() {
        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI("http://localhost:8080/api/");        //Point this to the server API

        int threadCount = 2;            //number of threads used to execute workers.  To avoid starvation, should be same or more than number of workers

        Worker worker1 = new TeapotWorker(teapotService);

        // Create TaskRunnerConfigurer
        TaskRunnerConfigurer configurer = new TaskRunnerConfigurer.Builder(taskClient, Arrays.asList(worker1))
                .withThreadCount(threadCount)
                .build();

        // Start the polling and execution of tasks
        configurer.init();
    }
}
