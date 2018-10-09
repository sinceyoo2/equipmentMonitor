package com.syo.platform.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

@Configuration
@EnableJms
public class ActiveMQConfig {

	@Bean(name="monitor_sms_queue")
	public Queue smsQueue() {
		return new ActiveMQQueue("monitor.sms");
	}
	
	@Bean(name="monitor_mail_queue")
	public Queue mailQueue() {
		return new ActiveMQQueue("monitor.mail");
	}
	
}
