package com.github.robertnetz.dogstatsd;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@ActiveProfiles("actuatorDisabled")
public class ActuatorDisabledIT extends AbstractIntegrationTest {

    @Autowired
    private Environment env;

    @Autowired(required = false)
    private MetricWriter metricWriter;

    @Autowired
    private MetricRegistry metricRegistry;

    @Before
    public void registerCustomMetric() {
        metricRegistry.register("foo", (Gauge<Double>) Math::random);
    }

    @After
    public void removeCustomMetric() {
        metricRegistry.remove("foo");
    }

    @Test
    public void testNoDataIsSentToDatadog() throws InterruptedException, IOException, ExecutionException, TimeoutException {

        final int samples = 2;
        final List<String> datapoints = startUdpServer(samples).get();
        long foo = datapoints.stream().filter(s -> s.startsWith("foo")).count();

        Assert.assertEquals(samples, foo);
    }
}
