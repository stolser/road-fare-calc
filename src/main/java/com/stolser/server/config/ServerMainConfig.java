package com.stolser.server.config;

import org.springframework.context.annotation.*;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableSpringConfigured
@ImportResource(value = {"classpath:/config/serverMainConfig.xml"})
public class ServerMainConfig {

    @Bean(value = "serverThreadPool")
    public ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(10);
    }

}
