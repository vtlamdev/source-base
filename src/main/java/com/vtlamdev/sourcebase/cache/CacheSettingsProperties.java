package com.vtlamdev.sourcebase.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "spring.cache.redis")
public class CacheSettingsProperties {

    private Duration timeToLive = Duration.ofMinutes(10);

    public Duration getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

}
