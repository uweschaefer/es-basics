package com.mercateo.edu.infra;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EventsourcingInfraConfiguration.class)
public class EventsourcingInfraConfiguration {
	// configuration to let spring know, where to scan for @Components

}
