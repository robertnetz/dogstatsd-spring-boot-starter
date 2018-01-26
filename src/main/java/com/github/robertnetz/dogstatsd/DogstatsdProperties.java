package com.github.robertnetz.dogstatsd;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration of our Dogstatsd Writer.
 */
@ConfigurationProperties(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX)
public class DogstatsdProperties {

    /**
     * indicator to export metrics to the datadog-agent
     */
    private boolean enabled = true;

    /**
     * a prefix that is preceded to the metric name
     */
    private String prefix;

    /**
     * the hostname of the datadog-agent.
     */
    private String host = "localhost";

    /**
     * the port of the datadog-agent
     */
    private int port = 8125;

    /**
     * global tags to add to the service's metrics.
     */
    private String[] tags;

    /**
     * indicator to export spring actuators' SystemPublicMetrics to the datadog-agent.
     */
    private boolean includeActuatorMetrics = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public boolean isIncludeActuatorMetrics() {
        return includeActuatorMetrics;
    }

    public void setIncludeActuatorMetrics(boolean includeActuatorMetrics) {
        this.includeActuatorMetrics = includeActuatorMetrics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogstatsdProperties that = (DogstatsdProperties) o;
        return enabled == that.enabled &&
                port == that.port &&
                includeActuatorMetrics == that.includeActuatorMetrics &&
                Objects.equal(prefix, that.prefix) &&
                Objects.equal(host, that.host) &&
                Objects.equal(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(enabled, prefix, host, port, tags, includeActuatorMetrics);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("enabled", enabled)
                .add("prefix", prefix)
                .add("host", host)
                .add("port", port)
                .add("tags", tags)
                .add("includeActuatorMetrics", includeActuatorMetrics)
                .toString();
    }
}
