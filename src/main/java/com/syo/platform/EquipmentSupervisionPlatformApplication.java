package com.syo.platform;

import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ServletComponentScan
public class EquipmentSupervisionPlatformApplication {
	
	@Bean
	public RestTemplate restTemplate() {
//		return new RestTemplate();
		return new RestTemplate(clientHttpRequestFactory());
	}
	
	private ClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory();
		factory.setReadTimeout(2000);
		factory.setConnectTimeout(2000);
		return factory;
	}
	
//	@Bean
//    @ConfigurationProperties(prefix = "custom_rest.connection")
//    public HttpComponentsClientHttpRequestFactory customHttpRequestFactory() {
//        return new HttpComponentsClientHttpRequestFactory();
//    }
//
//    @Bean
//    public RestTemplate customRestTemplate(){
//        return new RestTemplate(customHttpRequestFactory());
//    }

	public static void main(String[] args) {
		SpringApplication.run(EquipmentSupervisionPlatformApplication.class, args);
	}
}
