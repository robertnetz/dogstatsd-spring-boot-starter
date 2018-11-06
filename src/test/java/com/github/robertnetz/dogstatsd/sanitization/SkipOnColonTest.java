package com.github.robertnetz.dogstatsd.sanitization;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class SkipOnColonTest {

    @Test
    public void nameWithoutColonPassedThrough() {
        final String metricName = "long.nested.name";

        NameSanitizer strategy = new SkipOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long.nested.name", sanitized.get());
    }

    @Test
    public void nameWithColonReturnsEmptyName() {
        final String metricName = "long:nested:name";

        NameSanitizer strategy = new SkipOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertFalse(sanitized.isPresent());
    }
}
