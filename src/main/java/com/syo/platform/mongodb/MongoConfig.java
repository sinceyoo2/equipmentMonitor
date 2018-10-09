package com.syo.platform.mongodb;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfig {

	@Value("${spring.data.mongodb-history.uri}")
	private String HIS_URI;
	
	@Value("${spring.data.mongodb-error.uri}")
	private String ERR_URI;
	
	@Bean
	public MongoMappingContext mongoMappingContext() {
		return new MongoMappingContext();
	}
	
	@Bean
	@Primary
	public MongoDbFactory hisDbFactory() throws UnknownHostException {
		return new SimpleMongoDbFactory(new MongoClientURI(HIS_URI));
	}
	
	@Bean
	@Primary
	public MongoTemplate hisMongoTemplate() throws UnknownHostException {
		return new MongoTemplate(hisDbFactory());
	}
	
	
	@Bean
	public MongoDbFactory errDbFactory() throws UnknownHostException {
		return new SimpleMongoDbFactory(new MongoClientURI(ERR_URI));
	}
	
	@Bean
	public MongoTemplate errMongoTemplate() throws UnknownHostException {
		return new MongoTemplate(errDbFactory());
	}
}
