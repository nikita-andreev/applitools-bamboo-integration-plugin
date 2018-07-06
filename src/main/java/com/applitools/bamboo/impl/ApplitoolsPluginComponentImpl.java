package com.applitools.bamboo.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import com.applitools.bamboo.api.ApplitoolsPluginComponent;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService ({ApplitoolsPluginComponent.class})
@Named ("ApplitoolsPluginComponent")
public class ApplitoolsPluginComponentImpl implements ApplitoolsPluginComponent
{
    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public ApplitoolsPluginComponentImpl(final ApplicationProperties applicationProperties)
    {
        this.applicationProperties = applicationProperties;
    }

    public String getName()
    {
        if(null != applicationProperties)
        {
            return "ApplitoolsComponent:" + applicationProperties.getDisplayName();
        }

        return "ApplitoolsComponent";
    }
}