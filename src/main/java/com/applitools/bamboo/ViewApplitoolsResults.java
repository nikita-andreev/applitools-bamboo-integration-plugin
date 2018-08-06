package com.applitools.bamboo;

import com.atlassian.bamboo.build.ChainResultsAction;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.task.TaskDefinition;

import java.util.List;

public class ViewApplitoolsResults extends ChainResultsAction {
    public static final String APPLITOOLS_SERVER = "https://eyes.applitools.com/app/batchesnoauth/";
    public static final String QUERY_STRING_BEFORE_BATCH_ID = "?startInfoBatchId=";
    public static final String QUERY_STRING_AFTER_BATCH_ID = "&hideBatchList=true&intercom=false";
    public static final String MODULE_KEY = "com.applitools.bamboo.bamboo-applitools-plugin:ApplitoolsTestsTask";
    public String batchId, iframeUrl;

    public String execute() throws Exception {

        this.batchId = PlanUidUtils.getBatchId(getBuildKey(), getBuildNumber());
        this.iframeUrl = APPLITOOLS_SERVER + QUERY_STRING_BEFORE_BATCH_ID + batchId + QUERY_STRING_AFTER_BATCH_ID;
        if (hasApplitoolsTask()) {
            return "success";
        } else {
            return "notask";
        }

    }

    private boolean hasApplitoolsTask() {
        Boolean result = false;
        if(null != getChainResult()) {
            List<ImmutableJob> allJobs = (List<ImmutableJob>) getChainResult().getImmutablePlan().getAllJobs();
            for (ImmutableJob job : allJobs) {
                if (result) {
                    break;
                }
                List<TaskDefinition> jobTasks = job.getTaskDefinitions();
                result = detectApplitoolsTask(jobTasks);
            }
        } else {
            List<TaskDefinition> jobTasks =  getResultsSummary().getImmutablePlan().getBuildDefinition().getTaskDefinitions();
            result = detectApplitoolsTask(jobTasks);
        }

        return result;
    }

    private boolean detectApplitoolsTask(List<TaskDefinition> jobTasks) {
        boolean result = false;
        for (TaskDefinition task : jobTasks) {
            if (MODULE_KEY == task.getPluginKey() && task.isEnabled()) {
                result = true;
                break;
            }
        }
        return result;
    }
}
