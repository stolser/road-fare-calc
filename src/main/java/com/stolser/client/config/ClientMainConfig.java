package com.stolser.client.config;

import com.stolser.entity.Car;
import com.stolser.entity.User;
import org.springframework.context.annotation.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = {"com.stolser.client"})
@Import(value = {CarConfig.class})
@ImportResource(value = {"classpath:/config/clientMainConfig.xml"})
public class ClientMainConfig {

    @Bean(value = "clientThreadPool")
    public ExecutorService newThreadPool() {
        return Executors.newFixedThreadPool(10);
    }

}
