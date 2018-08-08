package com.applitools.bamboo;

import com.atlassian.bamboo.build.Job;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.task.TaskDefinition;

import java.util.List;
import java.util.Map;


public class ShowLinkConditionJobResults extends ApplitoolsLinkCondition implements com.atlassian.plugin.web.Condition {

    public ShowLinkConditionJobResults(PlanManager planManager) {
        super(planManager);
    }

    @Override
    public void init(Map<String, String> value) {

    }

    @Override
    public boolean shouldDisplay(Map<String, Object> map) {
        Boolean result = false;
        Plan plan = getPlan(map.get("planKey").toString());
        List<TaskDefinition> taskDefinitions = ((Job)plan).getTaskDefinitions();
        for (TaskDefinition task : taskDefinitions) {
            if (MODULE_KEY == task.getPluginKey() && task.isEnabled()) {
                result = true;
                break;
            }
        }
        return result;
    }
}
