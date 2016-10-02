package com.stolser.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ImportResource(value = {"/config/serverMainConfig.xml"})
public class ServerMainConfig {

    @Bean(value = "threadPool")
    public ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(10);
    }

}
