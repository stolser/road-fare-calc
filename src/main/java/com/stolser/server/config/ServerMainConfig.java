package com.stolser.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(value = {"/config/serverMainConfig.xml"})
public class ServerMainConfig {

}
