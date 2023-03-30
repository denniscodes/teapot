package com.callibrity.conductor.teapot.workers;

import com.callibrity.conductor.teapot.domain.TeapotJokeService;
import com.callibrity.conductor.teapot.domain.TeapotService;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Component;

@Component
public class TeapotWorker implements Worker {
    public final TeapotJokeService teapotJokeService;
    public TeapotWorker(TeapotJokeService teapotJokeService) {this.teapotJokeService = teapotJokeService;}
    @Override
    public String getTaskDefName() {
        return "teapot_worker";
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);
        result.addOutputData("jokeResponse", teapotJokeService.getRandomJoke());
        return result;
    }

}
