package com.github.robertnetz.dogstatsd.sanitization;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.fail;

public class RaiseOnColonTest {

    @Test
    public void nameWithoutColonPassedThrough() {
        final String metricName = "long.nested.name";

        NameSanitizer strategy = new RaiseOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long.nested.name", sanitized.get());
    }

    @Test
    public void nameWithColonRaises() {
        final String metricName = "long:nested:name";

        NameSanitizer strategy = new RaiseOnColon();

        try {
            strategy.sanitize(metricName);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("long:nested:name"));
        }
    }

}
