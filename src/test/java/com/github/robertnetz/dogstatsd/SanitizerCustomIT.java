package com.github.robertnetz.dogstatsd;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("sanitizerCustom")
@SpringBootTest(classes = {AbstractIntegrationTest.Config.class, SanitizerCustomIT.CustomSanitizer.class})
public class SanitizerCustomIT extends AbstractIntegrationTest {

    @Autowired
    private NameSanitizer sanitizer;

    @Test
    public void testCustomSanitizerWasSet() {
        Assert.isInstanceOf(CustomSanitizer.class, sanitizer);
        assertEquals(Optional.of("bar"), sanitizer.sanitize("foo"));
    }

    @Configuration
    @ConditionalOnProperty(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX, name = "nameSanitizer", havingValue = "custom")
    static class CustomSanitizer implements NameSanitizer {

        @Override
        public Optional<String> sanitize(String metricName) {
            String sanitized = metricName.replace("foo", "bar");
            return Optional.of(sanitized);
        }
    }
}
