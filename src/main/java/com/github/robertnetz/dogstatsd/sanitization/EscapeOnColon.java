package com.github.robertnetz.dogstatsd.sanitization;

import java.util.Optional;

public class EscapeOnColon implements NameSanitizer {
    @Override
    public Optional<String> sanitize(String metricName) {
        String sanitized = metricName.replace(":", "-");
        return Optional.of(sanitized);
    }
}
