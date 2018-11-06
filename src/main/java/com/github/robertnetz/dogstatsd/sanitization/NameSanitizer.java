package com.github.robertnetz.dogstatsd.sanitization;

import java.util.Optional;

public interface NameSanitizer {
    Optional<String> sanitize(String metricName);
}
