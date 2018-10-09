package com.syo.platform.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syo.platform.dto.ErrorQueryDTO;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.jpautils.CriteriaFactory;
import com.syo.platform.service.ErrorMessageService;

@Service
public class ErrorMessageServiceImpl implements ErrorMessageService {

	@Autowired
	private ErrorMessageRepository errorMessageRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${kyle.api.create-flow}")
	private String createFlowApi;
	
	
	@Override
	public Page<ErrorMessage> findErros(int pageNo, int pageSize, ErrorQueryDTO condition) {
		return errorMessageRepository.findAll(CriteriaFactory.createCriteria(condition), new PageRequest(pageNo-1, pageSize, Direction.DESC, "errorTime"));
	}


	@Override
	public Page<ErrorMessage> findWarningErros(int pageNo, int pageSize) {
		ErrorQueryDTO condition = new ErrorQueryDTO();
		condition.setStatus("发现故障");
		return errorMessageRepository.findAll(CriteriaFactory.createCriteria(condition), new PageRequest(pageNo-1, pageSize, Direction.DESC, "errorTime"));
	}


	@Override
	public void saveErrorMessage(ErrorMessage errorMessage) {
		errorMessageRepository.save(errorMessage);
	}


	@Override
	public ErrorMessage findErrorMessageById(String id) {
		return errorMessageRepository.findOne(id);
	}


	@Override
	public void saveAndStartFlow(ErrorMessage errorMessage) {
		
		if(StringUtils.isNotBlank(errorMessage.getId()) && errorMessageRepository.findOne(errorMessage.getId())==null) {
			if(errorMessageRepository.findOne(errorMessage.getId())==null) {
				errorMessageRepository.save(errorMessage);
			}
			errorMessage = errorMessageRepository.findOne(errorMessage.getId());
		}
		
		
		if(StringUtils.isNotBlank(errorMessage.getId()) && errorMessage.getEquipment()!=null && StringUtils.isNotBlank(errorMessage.getEquipment().getOwnerAccount())) {
		
			Map<String, String> data = new HashMap<>();
			data.put("GZID", errorMessage.getId());
			data.put("GZBT", errorMessage.getCause());
			data.put("GZMS", errorMessage.getDescription());
			if(errorMessage.getEquipment()!=null) {
				data.put("SBID", errorMessage.getEquipment().getId());
			}
			String dataJson=null;
			try {
				dataJson = new ObjectMapper().writeValueAsString(data);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			System.out.println(dataJson);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	
			MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
			map.add("flowid", "600101");
			map.add("nodeid", "600101003");
			map.add("data", dataJson);
			map.add("userid", "admin");
			map.add("temp", new Date().getTime()+"");
	
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
	
//			ResponseEntity<String> response = restTemplate.postForEntity( "http://192.168.1.106/wf/createflow", request , String.class );
			ResponseEntity<String> response = restTemplate.postForEntity( createFlowApi, request , String.class );
			System.out.println("-------------------------------------------------------------------------");
			System.out.println(response);
			System.out.println("-------------------------------------------------------------------------");
			
			errorMessage.setHashWorkSheet(true);
			errorMessage.setWorkSheetStatus("处理中");
			errorMessageRepository.save(errorMessage);
		
		}
	}


	@Override
	public void saveFlow(String errorMessageId) {
		ErrorMessage errorMessage = errorMessageRepository.findOne(errorMessageId);
		if(errorMessage!=null) {
			saveAndStartFlow(errorMessage);
		}
	}

}
