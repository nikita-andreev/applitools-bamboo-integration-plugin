package com.applitools.bamboo;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.utils.process.ExternalProcess;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

@Scanned
public class ApplitoolsTaskRunner implements TaskType {
    private static final String BATCH_ID = "APPLITOOLS_BATCH_ID";
    @ComponentImport
    private final ProcessService processService;

    @ComponentImport
    private final CapabilityContext capabilityContext;

    @Inject
    public ApplitoolsTaskRunner(@NotNull final ProcessService processService, @NotNull final CapabilityContext capabilityContext) {
        this.processService = processService;
        this.capabilityContext = capabilityContext;
    }

    @Override
    public TaskResult execute(final TaskContext taskContext) throws TaskException {
        final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext).success();
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        String executable, execFromCaps, toRun;

        ExternalProcessBuilder processBuilder = new ExternalProcessBuilder();
        ConfigurationMap configMap = taskContext.getConfigurationMap();

        String batchId = PlanUidUtils.getBatchId(taskContext.getBuildContext().getTypedPlanKey().getKey(), taskContext.getBuildContext().getBuildNumber());

        executable = configMap.get(ApplitoolsTaskConfigurator.COMMAND);
        execFromCaps = capabilityContext.getCapabilityValue(ApplitoolsTaskConfigurator.CAPABILITY_KEY_PREFIX + executable);
        toRun = execFromCaps + " " + configMap.get(ApplitoolsTaskConfigurator.COMMAND_PARAMS);

        processBuilder.commandFromString(toRun)
                .workingDirectory(taskContext.getWorkingDirectory())
                .env(ApplitoolsTaskConfigurator.APPLITOOLS_API_KEY, configMap.get(ApplitoolsTaskConfigurator.APPLITOOLS_API_KEY))
                .env(BATCH_ID, batchId);

        EnvVarsParser envVarsParser = new EnvVarsParser(configMap.get(ApplitoolsTaskConfigurator.ENVIRONMENT_VARIABLES));

        if (envVarsParser.isValid()) {
            processBuilder.env(envVarsParser.asMap());
        }

        ExternalProcess process = processService.createExternalProcess(
                taskContext,
                processBuilder
        );

        buildLogger.addBuildLogEntry("APPLITOOLS_BATCH_ID:");
        buildLogger.addBuildLogEntry(batchId);
        process.execute();

        return builder.checkReturnCode(process, 0).build();
    }

}
