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

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Heavily inspired by Spring Boot's {@link org.springframework.boot.actuate.metrics.statsd.StatsdMetricWriter}.
 */
public class DataDogStatsDMetricWriter implements MetricWriter, Closeable, StatsDClientErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataDogStatsDMetricWriter.class);

    private final StatsDClient client;
    private final String[] tags;

    /**
     * @param prefix all metrics will be prefixed with this string
     * @param host   the dogstatsd host
     * @param port   the dogstatsd port
     * @param tags   all metrics will be tagged with all of the tags
     */
    DataDogStatsDMetricWriter(final String prefix, final String host, final int port, final String... tags) {
        this.client = new NonBlockingStatsDClient(prefix, host, port, tags, this);
        this.tags = tags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final Delta<?> delta) {
        final String name = delta.getName();
        checkArgument(!name.contains(":"), "colon is not allowed in metric names.");

        this.client.count(name, delta.getValue().longValue(), tags);
        LOGGER.trace("sent delta count: {}={}, tags='{}'", name, delta.getValue(), tags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(final Metric<?> metric) {
        final String name = metric.getName();
        checkArgument(!name.contains(":"), "colon is not allowed in metric names.");

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
        LOGGER.trace("reset requested for '{}'", name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.client.stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final Exception e) {
        LOGGER.warn("Failed to write metric. Exception: {}, message: {}", e.getClass(), e.getMessage());
    }
}
