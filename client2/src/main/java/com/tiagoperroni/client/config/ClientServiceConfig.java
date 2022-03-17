package com.tiagoperroni.client.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "client")
@Data
public class ClientServiceConfig {

    private String msg;
    private String buildVersion;
    private String properties;
    private String repo;
    private Map<String, String> mailDetails;
  
    
}
