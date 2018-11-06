package com.github.robertnetz.dogstatsd.sanitization;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class EscapeOnColonTest {

    @Test
    public void nameWithoutColonPassedThrough() {
        final String metricName = "long.nested.name";

        NameSanitizer strategy = new EscapeOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long.nested.name", sanitized.get());
    }

    @Test
    public void nameWithColonIsEscaped() {
        final String metricName = "long:nested:name";

        NameSanitizer strategy = new EscapeOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long-nested-name", sanitized.get());
    }

}
