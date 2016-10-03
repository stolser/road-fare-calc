package com.stolser.client.config;

import org.springframework.context.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = {"com.stolser.client"})
@Import(value = {CarConfig.class})
@ImportResource(value = {"/config/clientMainConfig.xml"})
public class ClientMainConfig {

    @Bean(value = "clientThreadPool")
    public ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(10);
    }
}
