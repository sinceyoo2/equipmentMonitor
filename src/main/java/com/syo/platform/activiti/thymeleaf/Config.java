package com.syo.platform.activiti.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Bean
    public CustomDialect testDialect(){
        return new CustomDialect();
    }
	
}
