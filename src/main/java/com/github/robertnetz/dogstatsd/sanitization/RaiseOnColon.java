package com.github.robertnetz.dogstatsd.sanitization;

import java.util.Optional;

public class RaiseOnColon implements NameSanitizer {
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
