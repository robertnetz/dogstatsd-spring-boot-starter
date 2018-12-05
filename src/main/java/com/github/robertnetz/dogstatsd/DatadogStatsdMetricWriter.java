package com.github.robertnetz.dogstatsd;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.StatsDClientErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import java.io.Closeable;
import java.util.Optional;

/**
 * Heavily inspired by Spring Boot's {@link org.springframework.boot.actuate.metrics.statsd.StatsdMetricWriter}.
 * This class adds tags to metrics.
 */
public class DatadogStatsdMetricWriter implements MetricWriter, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatadogStatsdMetricWriter.class);

    private final StatsDClient client;
    private final NameSanitizer nameSanitizer;
    private final String[] tags;

    /**
     * @param prefix all metrics will be prefixed with this string
     * @param host   the dogstatsd host
     * @param port   the dogstatsd port
     * @param tags   all metrics will be tagged with all of the tags
     */
    DatadogStatsdMetricWriter(final String prefix, final String host, final int port, final NameSanitizer nameSanitizer,
                              final String... tags) {
        this(new NonBlockingStatsDClient(prefix, host, port, tags, new ErrorHandler()), nameSanitizer, tags);
    }

    /**
     * @param client the statsdclient to use
     * @param tags all metrics will be tagged with all of the tags
     */
    DatadogStatsdMetricWriter(final StatsDClient client, final NameSanitizer nameSanitizer, final String[] tags) {
        this.client = client;
        this.nameSanitizer = nameSanitizer;
        this.tags = tags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final Delta<?> delta) {
        final Optional<String> name = nameSanitizer.sanitize(delta.getName());
        if (!name.isPresent()) {
            return;
        }

        this.client.count(name.get(), delta.getValue().longValue(), tags);
        LOGGER.trace("sent delta count: {}={}, tags='{}'", name, delta.getValue(), tags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final Metric<?> metric) {
        final Optional<String> sanitizedName = nameSanitizer.sanitize(metric.getName());
        if (!sanitizedName.isPresent()) {
            return;
        }

        final String name = sanitizedName.get();

        if (name.contains("timer.") && !name.contains("gauge.") && !name.contains("counter.")) {
            this.client.recordExecutionTime(name, metric.getValue().longValue(), tags);
            LOGGER.trace("sent set execution time: {}={}, tags='{}'", name, metric.getValue(), tags);
        } else {
            if (name.contains("counter.")) {
                this.client.count(name, metric.getValue().longValue(), tags);
                LOGGER.trace("sent set count: {}={}, tags='{}'", name, metric.getValue(), tags);
            } else {
                this.client.gauge(name, metric.getValue().doubleValue(), tags);
                LOGGER.trace("sent set gauge: {}={}, tags='{}'", name, metric.getValue(), tags);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(final String name) {
        LOGGER.trace("reset requested for '{}' ignored", name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.client.stop();
    }

    /**
     * Our {@link StatsDClientErrorHandler}.
     */
    static class ErrorHandler implements StatsDClientErrorHandler {

        /**
         * {@inheritDoc}
         */
        @Override
        public void handle(final Exception e) {
            LOGGER.warn("Failed to write metric. Exception: {}, message: {}", e.getClass(), e.getMessage());
        }
    }
}
