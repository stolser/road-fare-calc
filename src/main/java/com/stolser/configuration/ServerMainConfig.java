package com.stolser.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@Import(value = {CarConfig.class})
@ImportResource(value = {"/config/serverMainConfig.xml"})
public class ServerMainConfig {

}
