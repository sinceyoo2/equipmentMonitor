package com.syo.platform.activemq;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Queue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MonitorMessageSender {

	@Resource(name="monitor_sms_queue")
	private Queue smsQueue;
	
	@Resource(name="monitor_mail_queue")
	private Queue mailQueue;
	
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	ObjectMapper mapper = new ObjectMapper();
	
//	public void sendSms(String sms, String... phones) {
//		if(phones.length<=0) {
//			return;
//		}
//		String targets = StringUtils.join(phones, ",");
//		sendSms(sms, targets);
//	}
	
	public void sendSms(String sms, String targetStr) {
		if(StringUtils.isBlank(sms)) {
			return;
		}
		if(targetStr.length()<=0) {
			return;
		}
		Map<String, String> map = new HashMap<>();
		map.put("targets", targetStr);
		map.put("content", sms);
		
		try {
			jmsMessagingTemplate.convertAndSend(smsQueue, mapper.writeValueAsString(map));
		} catch (MessagingException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
//	public void sendMail(String title, String mail, String...addresses) {
//		if(addresses.length<=0) {
//			return;
//		}
//		String targets = StringUtils.join(addresses, ",");
//		sendMail(title, mail, targets);
//	}
	
	public void sendMail(String title, String mail, String targetStr) {
		if(StringUtils.isBlank(title)) {
			return;
		}
		
		if(StringUtils.isBlank(mail)) {
			return;
		}
		if(targetStr.length()<=0) {
			return;
		}
		Map<String, String> map = new HashMap<>();
		map.put("title", title);
		map.put("targets", targetStr);
		map.put("content", mail);
		
		try {
			jmsMessagingTemplate.convertAndSend(mailQueue, mapper.writeValueAsString(map));
		} catch (MessagingException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
