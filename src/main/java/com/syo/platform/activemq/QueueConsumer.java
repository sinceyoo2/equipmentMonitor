package com.syo.platform.activemq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syo.platform.util.ValidStringUtil;

@Component
public class QueueConsumer {

	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${spring.mail.from-name}")
	private String mailFromName;
	
	@JmsListener(destination="monitor.sms")
	public void consumeSmsQueue(String msg) {
		try {
			Map<String, String> map = mapper.readValue(msg, Map.class);
			String targetStr = map.get("targets");
			String content = map.get("content");
			System.out.println("发送信息：");
			System.out.println("内容：");
			System.out.println(content);
			System.out.println("接收人：");
			System.out.println(targetStr);
			
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
            requestEntity.add("mobileNumber", targetStr);
            requestEntity.add("content", content);
            requestEntity.add("appId", "5");
            Map rest = restTemplate.postForObject("http://10.100.100.207:8866/sms/send", requestEntity, Map.class);
	
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@JmsListener(destination="monitor.mail")
	public void consumeMailQueue(String msg) {
		try {
			Map<String, String> map = mapper.readValue(msg, Map.class);
			String targetStr = map.get("targets");
			String title = map.get("title");
			String content = map.get("content");

			List<String> targets = new ArrayList<>();
			for(String target : targetStr.split(",")) {
				String str = target.trim();
				if(ValidStringUtil.isEmail(str)) {
					targets.add(str);
				}
			}
			if(targets.size()<=0) {
				return;
			}
			
			System.out.println("发送邮件：");
			System.out.println("内容：");
			System.out.println(content);
			System.out.println("接收人：");
			System.out.println(targetStr);
			
			String[] targetArr = new String[targets.size()];
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(mailFromName);
			message.setTo(targets.toArray(targetArr));
			message.setSubject(title);
			message.setText(content);
			 
			mailSender.send(message);
			
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
