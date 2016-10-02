package com.stolser.client.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = {"com.stolser.client"})
@Import(value = {CarConfig.class})
@ImportResource(value = {"/config/clientMainConfig.xml"})
public class ClientMainConfig {

}
