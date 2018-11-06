package com.github.robertnetz.dogstatsd.sanitization;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class RaiseOnColon implements NameSanitizer {
    @Override
    public Optional<String> sanitize(String metricName) {
        checkArgument(
                !metricName.contains(":"),
                "colon is not allowed in metric names.",
                metricName
        );
        return Optional.of(metricName);
    }
}
