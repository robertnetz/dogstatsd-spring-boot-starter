package com.github.robertnetz.dogstatsd;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@ActiveProfiles("withPrefix")
public class WriteWithPrefixToDatadogIT extends AbstractIntegrationTest {

    private static final String prefix = "service1";
    private static final int NUMBER_OF_SAMPLES = 10;

    @Test
    public void testTagsAreSentToDatadog() throws InterruptedException, IOException, ExecutionException {

        final List<String> result = startUdpServer(NUMBER_OF_SAMPLES).get();
        long count = result.stream().filter(e -> e.startsWith(prefix + ".actuator")).count();

        Assert.assertEquals(NUMBER_OF_SAMPLES, count);
    }
}
