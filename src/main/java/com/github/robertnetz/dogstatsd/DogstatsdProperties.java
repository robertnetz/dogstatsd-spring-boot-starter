package com.github.robertnetz.dogstatsd;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Objects;

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
     * How are colons in metric names handled?
     * Supported options are "raise", "escape", "skip"
     */
    private String nameSanitizer = "raise";

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

    public String getNameSanitizer() {
        return nameSanitizer;
    }

    public void setNameSanitizer(String nameSanitizer) {
        this.nameSanitizer = nameSanitizer;
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
        if (!(o instanceof DogstatsdProperties)) return false;
        DogstatsdProperties that = (DogstatsdProperties) o;
        return isEnabled() == that.isEnabled() &&
                getPort() == that.getPort() &&
                isIncludeActuatorMetrics() == that.isIncludeActuatorMetrics() &&
                Objects.equals(getPrefix(), that.getPrefix()) &&
                Objects.equals(getHost(), that.getHost()) &&
                Arrays.equals(getTags(), that.getTags()) &&
                Objects.equals(getNameSanitizer(), that.getNameSanitizer());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(isEnabled(), getPrefix(), getHost(), getPort(), getNameSanitizer(), isIncludeActuatorMetrics());
        result = 31 * result + Arrays.hashCode(getTags());
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DogstatsdProperties{");
        sb.append("enabled=").append(enabled);
        sb.append(", prefix='").append(prefix).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append(", port=").append(port);
        sb.append(", tags=").append(Arrays.toString(tags));
        sb.append(", nameSanitizer='").append(nameSanitizer).append('\'');
        sb.append(", includeActuatorMetrics=").append(includeActuatorMetrics);
        sb.append('}');
        return sb.toString();
    }
}
