package com.applitools.bamboo;

import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import javax.inject.Inject;

@Scanned
public class ApplitoolsLinkCondition {
    public static final String MODULE_KEY="com.applitools.bamboo.bamboo-applitools-plugin:ApplitoolsTestsTask";
    @ComponentImport
    private final PlanManager planManager;

    @Inject
    public ApplitoolsLinkCondition(PlanManager planManager) {
          this.planManager = planManager;
    }

    protected Plan getPlan(String key) {
        PlanKey planKey = PlanKeys.getPlanKey(key);
        return getPlanManager().getPlanByKey(planKey);
    }

    public PlanManager getPlanManager() {
        return planManager;
    }
}
