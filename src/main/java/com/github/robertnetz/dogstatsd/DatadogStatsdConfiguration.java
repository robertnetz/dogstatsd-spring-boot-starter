package com.github.robertnetz.dogstatsd;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.ExportMetricReader;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.endpoint.SystemPublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.reader.MetricReader;
import org.springframework.boot.actuate.metrics.reader.MetricRegistryMetricReader;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Spring Context Configuration for this Starter Project.
 */
@Configuration
@ConditionalOnProperty(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(DogstatsdProperties.class)
public class DatadogStatsdConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatadogStatsdConfiguration.class);
    private static final String SYSTEM_PREFIX = "actuator.";
    static final String CONFIG_PREFIX = "datadog";

    private final MetricRegistry metricRegistry;
    private final String prefix;
    private final String host;
    private final int port;
    private final String[] tags;
    private final boolean enableActuatorMetrics;

    final NameSanitizer nameSanitizer;

    /**
     * @param config         the configuration to use
     * @param nameSanitizer  the nameSanitizer to use
     * @param metricRegistry the dropwizards' metric registry
     */
    DatadogStatsdConfiguration(final DogstatsdProperties config, final NameSanitizer nameSanitizer, final MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;

        this.host = config.getHost();
        this.port = config.getPort();

        this.prefix = config.getPrefix();
        this.tags = config.getTags();
        this.nameSanitizer = nameSanitizer;

        this.enableActuatorMetrics = config.isIncludeActuatorMetrics();

        LOGGER.info("Exporting Metrics to statsd at '{}:{}' using prefix '{}' and tags '{}' nameSanitizer:{}",
                host, port, prefix, tags, nameSanitizer);
    }

    @Bean
    @ExportMetricReader
    MetricReader metricReader() {
        if (enableActuatorMetrics) {
            addActuatorMetrics(metricRegistry);
        }

        return new MetricRegistryMetricReader(metricRegistry);
    }

    @Bean
    @ExportMetricWriter
    MetricWriter metricWriter() {
        return new DatadogStatsdMetricWriter(prefix, host, port, nameSanitizer, tags);
    }

    /**
     * This method adds all {@link SystemPublicMetrics} to our MetricRegistry.
     *
     * @param metricRegistry the metricRegistry to add the system metrics to
     */
    void addActuatorMetrics(final MetricRegistry metricRegistry) {

        final SystemPublicMetrics metrics = new SystemPublicMetrics();
        final List<String> doubles = new LinkedList<>();
        doubles.add("systemload.average");

        final Predicate<Metric> exceptionFilter = metric -> doubles.contains(metric.getName());

        metrics.metrics().stream()
                .filter(exceptionFilter.negate())
                .forEach(e -> {
                    final String name = SYSTEM_PREFIX + e.getName();
                    metricRegistry.register(name, (Gauge<Long>) () -> (Long) e.getValue().longValue());
                    LOGGER.debug("registering {} (long) to metricRegistry", name);
                });

        metrics.metrics().stream()
                .filter(exceptionFilter)
                .forEach(e -> {
                    final String name = SYSTEM_PREFIX + e.getName();
                    metricRegistry.register(name, (Gauge<Double>) () -> (Double) e.getValue().doubleValue());
                    LOGGER.debug("registering {} (double) to metricRegistry", name);
                });
    }
}
