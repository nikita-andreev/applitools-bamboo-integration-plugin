package it.com.applitools.bamboo;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.applitools.bamboo.api.ApplitoolsPluginComponent;
import com.atlassian.sal.api.ApplicationProperties;

import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class ApplitoolsComponentWiredTest
{
    private final ApplicationProperties applicationProperties;
    private final ApplitoolsPluginComponent applitoolsPluginComponent;

    public ApplitoolsComponentWiredTest(ApplicationProperties applicationProperties, ApplitoolsPluginComponent applitoolsPluginComponent)
    {
        this.applicationProperties = applicationProperties;
        this.applitoolsPluginComponent = applitoolsPluginComponent;
    }

    @Test
    public void testMyName()
    {
        assertEquals("names do not match!", "ApplitoolsComponent:" + applicationProperties.getDisplayName(), applitoolsPluginComponent.getName());
    }
}