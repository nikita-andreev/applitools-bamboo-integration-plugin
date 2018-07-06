package ut.com.applitools.bamboo;

import org.junit.Test;
import com.applitools.bamboo.api.ApplitoolsPluginComponent;
import com.applitools.bamboo.impl.ApplitoolsPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class ApplitoolsComponentUnitTest
{
    @Test
    public void testMyName()
    {
        ApplitoolsPluginComponent component = new ApplitoolsPluginComponentImpl(null);
        assertEquals("names do not match!", "ApplitoolsComponent",component.getName());
    }
}