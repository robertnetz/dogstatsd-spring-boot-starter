package com.github.robertnetz.dogstatsd;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;


public interface NameSanitizer {

    Optional<String> sanitize(String metricName);

    @Configuration
    @ConditionalOnProperty(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX, name = "nameSanitizer", havingValue = "escape")
    class EscapeOnColon implements NameSanitizer {

        @Override
        public Optional<String> sanitize(String metricName) {
            String sanitized = metricName.replace(":", "-");
            return Optional.of(sanitized);
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX, name = "nameSanitizer", havingValue = "raise", matchIfMissing = true)
    class RaiseOnColon implements NameSanitizer {

        @Override
        public Optional<String> sanitize(String metricName) {
            if (metricName.contains(":")) {
                throw new IllegalArgumentException(
                        String.format("colon is not allowed in metric names: %s", metricName)
                );
            }
            return Optional.of(metricName);
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX, name = "nameSanitizer", havingValue = "skip")
    class SkipOnColon implements NameSanitizer {

        @Override
        public Optional<String> sanitize(String metricName) {
            if (metricName.contains(":")) {
                return Optional.empty();
            } else {
                return Optional.of(metricName);
            }
        }
    }
}
