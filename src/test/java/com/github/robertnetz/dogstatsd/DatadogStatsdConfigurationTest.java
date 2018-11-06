package com.github.robertnetz.dogstatsd;

import com.codahale.metrics.MetricRegistry;
import com.github.robertnetz.dogstatsd.sanitization.EscapeOnColon;
import com.github.robertnetz.dogstatsd.sanitization.RaiseOnColon;
import com.github.robertnetz.dogstatsd.sanitization.SkipOnColon;
import org.junit.Test;
import org.springframework.util.Assert;

import static org.mockito.Mockito.*;

public class DatadogStatsdConfigurationTest {

    @Test
    public void testActuatorSystemMetricsDisabled() {
        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.isIncludeActuatorMetrics()).thenReturn(false);

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = spy(new DatadogStatsdConfiguration(config, metricRegistry));

        d.metricReader();

        verify(metricRegistry, never()).register(anyString(), anyObject());
    }

    @Test
    public void testActuatorSystemMetricsAdded() {

        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.isIncludeActuatorMetrics()).thenReturn(true);

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = spy(new DatadogStatsdConfiguration(config, metricRegistry));

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

    @Test
    public void testDefaultNameSanitizer() {
        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.getNameSanitizer()).thenReturn(null);

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = new DatadogStatsdConfiguration(config, metricRegistry);

        Assert.isInstanceOf(RaiseOnColon.class, d.nameSanitizer, "Expecting default strategy");
    }

    @Test
    public void testUsingRaiseNameSanitizer() {
        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.getNameSanitizer()).thenReturn("raise");

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = new DatadogStatsdConfiguration(config, metricRegistry);

        Assert.isInstanceOf(RaiseOnColon.class, d.nameSanitizer, "Expecting raise strategy");
    }

    @Test
    public void testUsingSkipNameSanitizer() {
        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.getNameSanitizer()).thenReturn("skip");

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = new DatadogStatsdConfiguration(config, metricRegistry);

        Assert.isInstanceOf(SkipOnColon.class, d.nameSanitizer, "Expecting skip strategy");
    }

    @Test
    public void testUsingEscapeNameSanitizer() {
        final DogstatsdProperties config = mock(DogstatsdProperties.class);
        when(config.getNameSanitizer()).thenReturn("escape");

        final MetricRegistry metricRegistry = mock(MetricRegistry.class);
        final DatadogStatsdConfiguration d = new DatadogStatsdConfiguration(config, metricRegistry);

        Assert.isInstanceOf(EscapeOnColon.class, d.nameSanitizer, "Expecting escape strategy");
    }

}
