package com.applitools.bamboo;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.TaskRequirementSupport;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.Requirement;
import com.atlassian.bamboo.v2.build.agent.capability.RequirementImpl;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.xwork2.TextProvider;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Scanned
public class ApplitoolsTaskConfigurator extends AbstractTaskConfigurator implements TaskRequirementSupport {
    public static final String APPLITOOLS_API_KEY = "APPLITOOLS_API_KEY";
    public static final String COMMAND = "command";
    public static final String COMMAND_PARAMS = "params";
    public static final String ENVIRONMENT_VARIABLES = "envvars";
    public static final String SELECTED_EXECUTABLE = "selectedExecutable";

    public static final String CAPABILITY_KEY_PREFIX = "system.builder.command.";

    public static final String APPLITOOLS_API_KEY_ERROR_KEY = "applitools.api.key.error";
    public static final String COMMAND_ERROR_KEY = "command";
    public static final String SELECTED_EXECUTABLE_ERROR_KEY = "selected.executable.error";
    public static final String ENV_VARS_PARSE_ERROR_KEY = "envvars.parse.error";
    private static final String UI_CONFIG_BEAN_VAR_NAME = "uiConfigBean";

    @ComponentImport
    private UIConfigSupport uiConfigBean;

    @ComponentImport
    private CapabilityContext capabilityContext;

    @ComponentImport
    private TextProvider textProvider;

    @Inject
    public ApplitoolsTaskConfigurator(@NotNull UIConfigSupport uiConfigSupport, @NotNull final CapabilityContext capabilityContext, @NotNull TextProvider textProvider) {
        super();
        this.uiConfigBean = uiConfigSupport;
        this.capabilityContext = capabilityContext;
        this.textProvider = textProvider;
    }

    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition) {
        Map<String, String> result = super.generateTaskConfigMap(params, previousTaskDefinition);
        String applitoolsApiKey, commandToExecute, commandParams, rawEnvironmentVariables, selectedExecutable;

        applitoolsApiKey = params.getString(APPLITOOLS_API_KEY);
        commandToExecute = params.getString(COMMAND);
        commandParams = params.getString(COMMAND_PARAMS);
        rawEnvironmentVariables = params.getString(ENVIRONMENT_VARIABLES);
        selectedExecutable = params.getString(SELECTED_EXECUTABLE);

        result.put(APPLITOOLS_API_KEY, applitoolsApiKey);
        result.put(COMMAND, commandToExecute);
        result.put(SELECTED_EXECUTABLE, selectedExecutable);

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
        context.put(UI_CONFIG_BEAN_VAR_NAME, uiConfigBean);
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);
        context.put(UI_CONFIG_BEAN_VAR_NAME, uiConfigBean);
    }

    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
        String selectedExecutable = params.getString(SELECTED_EXECUTABLE);

        super.validate(params, errorCollection);

        errorIfEmpty(SELECTED_EXECUTABLE, SELECTED_EXECUTABLE_ERROR_KEY, selectedExecutable, errorCollection);

        EnvVarsParser envVars = new EnvVarsParser(params.getString(ENVIRONMENT_VARIABLES));
        if (!envVars.isValid()) {
            errorCollection.addError(ENVIRONMENT_VARIABLES, textProvider.getText(ENV_VARS_PARSE_ERROR_KEY));
        }

        errorIfEmpty(APPLITOOLS_API_KEY, APPLITOOLS_API_KEY_ERROR_KEY, params.getString(APPLITOOLS_API_KEY), errorCollection);

    }


    private void errorIfEmpty(String key, String errorKey, String value, ErrorCollection errorCollection) {
        if (StringUtils.isEmpty(value)) {
            errorCollection.addError(key, textProvider.getText(errorKey));
        }
    }

    @NotNull
    @Override
    public Set<Requirement> calculateRequirements(@NotNull TaskDefinition taskDefinition) {
      Set<Requirement> result = new HashSet<Requirement>();
      Requirement commandRequirement = new RequirementImpl(
              CAPABILITY_KEY_PREFIX + taskDefinition.getConfiguration().get(SELECTED_EXECUTABLE),
              true, ".*"
      );
      result.add(commandRequirement);
      return result;
    }
}
