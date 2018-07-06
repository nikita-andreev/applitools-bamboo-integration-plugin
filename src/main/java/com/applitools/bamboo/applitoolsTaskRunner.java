package com.applitools.bamboo;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;

public class applitoolsTaskRunner implements TaskType{
    @Override
    public TaskResult execute(final TaskContext taskContext) throws TaskException
    {
        return TaskResultBuilder.newBuilder(taskContext).success().build();
    }
}
