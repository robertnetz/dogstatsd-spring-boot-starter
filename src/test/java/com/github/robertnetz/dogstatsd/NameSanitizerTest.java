package com.github.robertnetz.dogstatsd;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.fail;

public class NameSanitizerTest {

    @Test
    public void nameWithoutColonPassedThroughWhenSkipOnColon() {
        final String metricName = "long.nested.name";

        NameSanitizer strategy = new NameSanitizer.SkipOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long.nested.name", sanitized.get());
    }

    @Test
    public void nameWithoutColonPassedThroughWhenRaiseOnColon() {
        final String metricName = "long.nested.name";

        NameSanitizer strategy = new NameSanitizer.RaiseOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long.nested.name", sanitized.get());
    }

    @Test
    public void nameWithoutColonPassedThroughWhenEscapeOnColon() {
        final String metricName = "long.nested.name";

        NameSanitizer strategy = new NameSanitizer.EscapeOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long.nested.name", sanitized.get());
    }

    @Test
    public void nameWithColonIsEscaped() {
        final String metricName = "long:nested:name";

        NameSanitizer strategy = new NameSanitizer.EscapeOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertTrue(sanitized.isPresent());
        Assert.assertEquals("long-nested-name", sanitized.get());
    }

    @Test
    public void nameWithColonReturnsEmptyName() {
        final String metricName = "long:nested:name";

        NameSanitizer strategy = new NameSanitizer.SkipOnColon();

        Optional<String> sanitized = strategy.sanitize(metricName);

        Assert.assertFalse(sanitized.isPresent());
    }

    @Test
    public void nameWithColonRaises() {
        final String metricName = "long:nested:name";

        NameSanitizer strategy = new NameSanitizer.RaiseOnColon();

        try {
            strategy.sanitize(metricName);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("long:nested:name"));
        }
    }
}
