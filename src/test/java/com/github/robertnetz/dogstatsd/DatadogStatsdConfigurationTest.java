package com.github.robertnetz.dogstatsd;

import com.codahale.metrics.MetricRegistry;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DatadogStatsdConfigurationTest {

    @Test
    public void testActuatorSystemMetricsDisabled() {
        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.isIncludeActuatorMetrics()).thenReturn(false);

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = spy(new DatadogStatsdConfiguration(config, new NameSanitizer.RaiseOnColon(), metricRegistry));

        d.metricReader();

        verify(metricRegistry, never()).register(anyString(), anyObject());
    }

    @Test
    public void testActuatorSystemMetricsAdded() {

        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.isIncludeActuatorMetrics()).thenReturn(true);

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = spy(new DatadogStatsdConfiguration(config, new NameSanitizer.RaiseOnColon(), metricRegistry));

        d.metricReader();

        verify(metricRegistry).register(eq("actuator.mem"), anyObject());
        verify(metricRegistry).register(eq("actuator.mem.free"), anyObject());
        verify(metricRegistry).register(eq("actuator.processors"), anyObject());
        verify(metricRegistry).register(eq("actuator.instance.uptime"), anyObject());
        verify(metricRegistry).register(eq("actuator.uptime"), anyObject());

        verify(metricRegistry).register(eq("actuator.heap"), anyObject());
        verify(metricRegistry).register(eq("actuator.heap.committed"), anyObject());
        verify(metricRegistry).register(eq("actuator.heap.init"), anyObject());
        verify(metricRegistry).register(eq("actuator.heap.used"), anyObject());

        verify(metricRegistry).register(eq("actuator.nonheap"), anyObject());
        verify(metricRegistry).register(eq("actuator.nonheap.committed"), anyObject());
        verify(metricRegistry).register(eq("actuator.nonheap.init"), anyObject());
        verify(metricRegistry).register(eq("actuator.nonheap.used"), anyObject());

        verify(metricRegistry).register(eq("actuator.threads"), anyObject());
        verify(metricRegistry).register(eq("actuator.threads.peak"), anyObject());
        verify(metricRegistry).register(eq("actuator.threads.daemon"), anyObject());
        verify(metricRegistry).register(eq("actuator.threads.totalStarted"), anyObject());

        verify(metricRegistry).register(eq("actuator.classes"), anyObject());
        verify(metricRegistry).register(eq("actuator.classes.loaded"), anyObject());
        verify(metricRegistry).register(eq("actuator.classes.unloaded"), anyObject());

        verify(metricRegistry).register(eq("actuator.gc.ps_scavenge.count"), anyObject());
        verify(metricRegistry).register(eq("actuator.gc.ps_scavenge.time"), anyObject());
        verify(metricRegistry).register(eq("actuator.gc.ps_marksweep.count"), anyObject());
        verify(metricRegistry).register(eq("actuator.gc.ps_marksweep.time"), anyObject());

        verify(metricRegistry).register(eq("actuator.systemload.average"), anyObject());

    }
}
