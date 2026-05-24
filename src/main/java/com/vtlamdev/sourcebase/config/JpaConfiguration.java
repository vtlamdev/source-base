package com.vtlamdev.sourcebase.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.vtlamdev.sourcebase.dao.repository")
@EntityScan("com.vtlamdev.sourcebase.dao.model")
public class JpaConfiguration {
}