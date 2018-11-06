package com.github.robertnetz.dogstatsd.sanitization;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class RaiseOnColonTest {

    @Test
    public void nameWithoutColonPassedThrough() {
        final String metricName = "long.nested.name";

        NameSanitizer strategy = new RaiseOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long.nested.name", sanitized.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nameWithColonRaises() {
        final String metricName = "long:nested:name";

        NameSanitizer strategy = new RaiseOnColon();

        strategy.sanitize(metricName);
    }

}
