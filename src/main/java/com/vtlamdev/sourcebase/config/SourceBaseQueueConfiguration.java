package com.vtlamdev.sourcebase.config;

import com.vtlamdev.sourcebase.queue.settings.QueueProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QueueProperties.class)
public class SourceBaseQueueConfiguration {
}
