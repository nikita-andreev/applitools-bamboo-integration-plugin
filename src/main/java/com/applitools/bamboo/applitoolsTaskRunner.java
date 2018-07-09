package com.applitools.bamboo;

import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;

public class applitoolsTaskRunner implements TaskType{
    private final ProcessService processService;

    public applitoolsTaskRunner(final ProcessService processService)
    {
        this.processService = processService;
    }

    @Override
    public TaskResult execute(final TaskContext taskContext) throws TaskException
    {
        final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext).failed(); //Initially set to Failed.


        return TaskResultBuilder.newBuilder(taskContext).success().build();
    }
}
