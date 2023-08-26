package com.banquito.core.branches.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mongo") 
@Data
public class MongoValues {
    
    private String host;
    private String database;
    private String user;
    private String password;
    private int port;
}
