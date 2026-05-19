package com.vtlamdev.sourcebase.controller;

import com.vtlamdev.sourcebase.queue.settings.QueueProperties;
import com.vtlamdev.sourcebase.security.SourceBaseSecurityProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    private final SourceBaseSecurityProperties securityProperties;
    private final QueueProperties queueProperties;

    public SystemController(SourceBaseSecurityProperties securityProperties, QueueProperties queueProperties) {
        this.securityProperties = securityProperties;
        this.queueProperties = queueProperties;
    }

    @GetMapping("/base-info")
    public Map<String, Object> getBaseInfo() {
        return Map.of(
                "application", "source-base",
                "singleModule", true,
                "securityPermitAll", securityProperties.isPermitAll(),
                "queueEnabled", queueProperties.isEnabled(),
                "defaultTopic", queueProperties.getDefaultTopic()
        );
    }

}
