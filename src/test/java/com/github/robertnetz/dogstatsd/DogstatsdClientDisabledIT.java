package com.github.robertnetz.dogstatsd;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ActiveProfiles("disabled")
public class DogstatsdClientDisabledIT extends AbstractIntegrationTest {

    @Autowired
    private Environment env;

    @Autowired(required = false)
    private MetricWriter metricWriter;

    @Test
    public void testApplicationStartup() throws InterruptedException, IOException, ExecutionException {
        final Boolean enabled = env.getProperty("datadog.enabled", Boolean.class);
        Assert.assertEquals(false, enabled);
        Assert.assertEquals(null, metricWriter);
    }

    @Test(expected = TimeoutException.class)
    public void testNoDataIsSentToDatadog() throws InterruptedException, IOException, ExecutionException, TimeoutException {

        startUdpServer(1).get(10, TimeUnit.SECONDS);
    }
}
