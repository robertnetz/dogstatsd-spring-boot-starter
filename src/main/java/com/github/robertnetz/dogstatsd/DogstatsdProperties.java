package com.github.robertnetz.dogstatsd;

import com.google.common.base.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration of our Dogstatsd Writer.
 */
@ConfigurationProperties(prefix = DatadogStatsdConfiguration.CONFIG_PREFIX)
public class DogstatsdProperties {

    /**
     * indicator to export metrics to statsd
     */
    private boolean enabled = false;

    /**
     * a prefix that is preceded to the metric name
     */
    private String prefix;

    /**
     * the hostname of the datadog statsd agent
     */
    private String host = "localhost";

    /**
     * the port of the datadog statsd agent
     */
    private int port = 8125;

    /**
     * global tags to add to the service's metrics."
     */
    private String[] tags;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogstatsdProperties that = (DogstatsdProperties) o;
        return isEnabled() == that.isEnabled() &&
                getPort() == that.getPort() &&
                Objects.equal(getPrefix(), that.getPrefix()) &&
                Objects.equal(getHost(), that.getHost()) &&
                Objects.equal(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isEnabled(), getPrefix(), getHost(), getPort(), getTags());
    }
}
