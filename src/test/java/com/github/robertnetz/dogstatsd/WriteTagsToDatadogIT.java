package com.github.robertnetz.dogstatsd;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ActiveProfiles("withTags")
public class WriteTagsToDatadogIT extends AbstractIntegrationTest {

    // TODO feels like a bug
    private static final String[] tags = new String[]{"baz", "bam", "bar", "foo", "baz", "bam", "bar", "foo"};
    private static final int NUMBER_OF_SAMPLES = 30;

    @Autowired(required = false)
    private MetricWriter metricWriter;

    @Test
    public void testMetricWriterPresent() {
        Assert.assertNotNull(metricWriter);
        Assert.assertEquals(DatadogStatsdMetricWriter.class, metricWriter.getClass());
    }

    @Test
    public void testTagsAreSentToDatadog() throws InterruptedException, IOException, ExecutionException {

        final List<String> result = startUdpServer(NUMBER_OF_SAMPLES).get();
        final String tagsString = Arrays.stream(tags).collect(Collectors.joining(","));
        long count = result.stream().filter(e -> e.endsWith("#" + tagsString)).count();

        Assert.assertEquals(NUMBER_OF_SAMPLES, count);
    }
}
