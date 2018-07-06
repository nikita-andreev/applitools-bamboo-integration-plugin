package com.applitools.bamboo;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class applitoolsTaskConfigurator extends AbstractTaskConfigurator {
    private static final String APPLITOOLS_API_KEY = "APPLITOOLS_API_KEY";
    private static final String SAUCE_USERNAME = "SAUCE_USERNAME";
    private static final String SAUCE_ACCESS_KEY = "SAUCE_ACCESS_KEY";
    private static final String COMMAND = "command";
    private static final String COMMAND_PARAMS = "params";

    private static final String APPLITOOLS_API_KEY_ERROR_KEY = "applitools.api.key.error";
    private static final String SAUCE_USERNAME_ERROR_KEY = "SAUCE_USERNAME";
    private static final String SAUCE_ACCESS_KEY_ERROR_KEY = "SAUCE_ACCESS_KEY";
    private static final String COMMAND_ERROR_KEY = "command";


    public applitoolsTaskConfigurator()
    {
        super();
    }

    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition)
    {
      Map<String, String> result = super.generateTaskConfigMap(params, previousTaskDefinition);
      String applitoolsApiKey, sauceUserName, sauceAccessKey, commandToExecute, commandParams;

      applitoolsApiKey = params.getString(APPLITOOLS_API_KEY);
      sauceUserName = params.getString(SAUCE_USERNAME);
      sauceAccessKey = params.getString(SAUCE_ACCESS_KEY);
      commandToExecute = params.getString(COMMAND);
      commandParams = params.getString(COMMAND_PARAMS);
      return result;
    }

    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection)
    {
       String sauceUserName, sauceAccessKey;

       super.validate(params, errorCollection);

       errorIfEmpty(APPLITOOLS_API_KEY, APPLITOOLS_API_KEY_ERROR_KEY, params.getString(APPLITOOLS_API_KEY), errorCollection);
       errorIfEmpty(COMMAND, COMMAND_ERROR_KEY, params.getString(COMMAND), errorCollection);

       sauceUserName = params.getString(SAUCE_USERNAME);
       sauceAccessKey = params.getString(SAUCE_ACCESS_KEY);

       if (StringUtils.isNoneBlank(sauceUserName) | StringUtils.isNoneBlank(sauceAccessKey))
        {
            errorIfEmpty(SAUCE_USERNAME, SAUCE_ACCESS_KEY_ERROR_KEY, sauceUserName, errorCollection);
            errorIfEmpty(SAUCE_ACCESS_KEY, SAUCE_ACCESS_KEY_ERROR_KEY,sauceAccessKey, errorCollection);
        }
    }


    private void errorIfEmpty(String key, String errorKey, String value, ErrorCollection errorCollection)
    {
        if (StringUtils.isEmpty(value))
        {
            errorCollection.addError(key, "error!");
        }
    }
}
