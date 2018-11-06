package com.github.robertnetz.dogstatsd.sanitization;

import java.util.Optional;

public class SkipOnColon implements NameSanitizer {
    @Override
    public Optional<String> sanitize(String metricName) {
        if (metricName.contains(":")) {
            return Optional.empty();
        } else {
            return Optional.of(metricName);
        }
    }
}
