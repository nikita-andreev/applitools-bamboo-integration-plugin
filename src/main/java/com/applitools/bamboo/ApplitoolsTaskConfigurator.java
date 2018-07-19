package com.applitools.bamboo;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ApplitoolsTaskConfigurator extends AbstractTaskConfigurator {
    public static final String APPLITOOLS_API_KEY = "APPLITOOLS_API_KEY";
    public static final String COMMAND = "command";
    public static final String COMMAND_PARAMS = "params";
    public static final String ENVIRONMENT_VARIABLES = "envvars";

    public static final String APPLITOOLS_API_KEY_ERROR_KEY = "applitools.api.key.error";
    public static final String COMMAND_ERROR_KEY = "command";


    public ApplitoolsTaskConfigurator() {
        super();
    }

    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition) {
        Map<String, String> result = super.generateTaskConfigMap(params, previousTaskDefinition);
        String applitoolsApiKey, commandToExecute, commandParams, rawEnvironmentVariables;

        applitoolsApiKey = params.getString(APPLITOOLS_API_KEY);
        commandToExecute = params.getString(COMMAND);
        commandParams = params.getString(COMMAND_PARAMS);
        rawEnvironmentVariables = params.getString(ENVIRONMENT_VARIABLES);

        result.put(APPLITOOLS_API_KEY, applitoolsApiKey);
        result.put(COMMAND, commandToExecute);

        if (StringUtils.isNoneBlank(rawEnvironmentVariables)) {
            result.put(ENVIRONMENT_VARIABLES, rawEnvironmentVariables);
        }

        if (null != commandParams && StringUtils.isNoneBlank(commandParams)) {
            result.put(COMMAND_PARAMS, commandParams);
        }

        return result;
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);

        context.put(APPLITOOLS_API_KEY, taskDefinition.getConfiguration().get(APPLITOOLS_API_KEY));
        context.put(COMMAND, taskDefinition.getConfiguration().get(COMMAND));
        context.put(COMMAND_PARAMS, taskDefinition.getConfiguration().get(COMMAND_PARAMS));
        context.put(ENVIRONMENT_VARIABLES, taskDefinition.getConfiguration().get(ENVIRONMENT_VARIABLES));
    }

    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
        String sauceUserName, sauceAccessKey;

        super.validate(params, errorCollection);

        EnvVarsParser envVars = new EnvVarsParser(params.getString(ENVIRONMENT_VARIABLES));

        if (!envVars.isValid()) {
            errorCollection.addError(ENVIRONMENT_VARIABLES, "Parse error!");
        }

        errorIfEmpty(APPLITOOLS_API_KEY, APPLITOOLS_API_KEY_ERROR_KEY, params.getString(APPLITOOLS_API_KEY), errorCollection);
        errorIfEmpty(COMMAND, COMMAND_ERROR_KEY, params.getString(COMMAND), errorCollection);
    }


    private void errorIfEmpty(String key, String errorKey, String value, ErrorCollection errorCollection) {
        if (StringUtils.isEmpty(value)) {
            errorCollection.addError(key, "error!");
        }
    }
}
