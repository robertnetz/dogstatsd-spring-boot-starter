package com.github.robertnetz.dogstatsd;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.Lists;
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

import java.util.List;
import java.util.function.Predicate;

/**
 * Spring Context Configuration for this Starter Project.
 */
@Configuration
@ConditionalOnProperty(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(DogstatsdProperties.class)
class DatadogStatsdConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatadogStatsdConfiguration.class);
    private static final String SYSTEM_PREFIX = "actuator.";
    public static final String CONFIG_PREFIX = "datadog";

    private final MetricRegistry metricRegistry;
    private final String prefix;
    private final String host;
    private final int port;
    private final String[] tags;

    /**
     * @param config         the configuration to use
     * @param metricRegistry the dropwizards' metric registry
     */
    DatadogStatsdConfiguration(final DogstatsdProperties config, final MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;

        this.host = config.getHost();
        this.port = config.getPort();

        this.prefix = config.getPrefix();
        this.tags = config.getTags();

        LOGGER.info("Exporting Metrics to statsd at '{}:{}' using prefix '{}' and tags '{}'", host, port, prefix, tags);
    }

    @Bean
    @ExportMetricReader
    MetricReader metricReader() {
        addActuatorSystemMetrics(metricRegistry);
        return new MetricRegistryMetricReader(metricRegistry);
    }

    @Bean
    @ExportMetricWriter
    MetricWriter metricWriter() {
        return new DataDogStatsDMetricWriter(prefix, host, port, tags);
    }

    /**
     * This method adds all {@link SystemPublicMetrics} to our MetricRegistry.
     *
     * @param metricRegistry the metricRegistry to add the system metrics to
     */
    private static void addActuatorSystemMetrics(final MetricRegistry metricRegistry) {

        final SystemPublicMetrics metrics = new SystemPublicMetrics();
        final List<String> doubles = Lists.newArrayList("systemload.average");
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
