package com.applitools.bamboo;

import com.atlassian.bamboo.build.Job;
import com.atlassian.bamboo.chains.DefaultChain;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.plugin.PluginParseException;

import java.util.List;
import java.util.Map;


public class ShowLinkConditionChainResults extends ApplitoolsLinkCondition implements com.atlassian.plugin.web.Condition {

    public ShowLinkConditionChainResults(PlanManager planManager) {
        super(planManager);
    }

    @Override
    public void init(Map<String, String> map) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> map) {
        Boolean result = false;
        Plan plan = getPlan(map.get("planKey").toString());

        List<Job> allJobs = ((DefaultChain)plan).getAllJobs();
        for (Job job : allJobs) {
            if (result) {
                break;
            }
            List<TaskDefinition> jobTasks = job.getTaskDefinitions();
            for (TaskDefinition task : jobTasks) {
                if (MODULE_KEY == task.getPluginKey() && task.isEnabled()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
