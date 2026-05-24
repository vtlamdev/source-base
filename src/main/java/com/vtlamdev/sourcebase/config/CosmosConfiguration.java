package com.vtlamdev.sourcebase.config;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCosmosRepositories(basePackages = "com.vtlamdev.sourcebase.dao.cosmos")
public class CosmosConfiguration extends AbstractCosmosConfiguration {

    @Bean
    public CosmosClientBuilder getCosmosClientBuilder() {
        DefaultAzureCredential credential = new DefaultAzureCredentialBuilder().build();
        return new CosmosClientBuilder().endpoint("https://cosmos-db-nosql-mujthbklhheym.documents.azure.com:443/").credential(credential);
    }

    @Override
    protected String getDatabaseName() {
        return "cosmicworks";
    }
}
