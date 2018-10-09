package com.syo.platform.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.syo.platform.dto.ErrorQueryDTO;
import com.syo.platform.entity.ErrorMessage;

public interface ErrorMessageService {

	public Page<ErrorMessage> findErros(int pageNo, int pageSize, ErrorQueryDTO condition);
	
	public Page<ErrorMessage> findWarningErros(int pageNo, int pageSize);
	
	public void saveErrorMessage(ErrorMessage errorMessage);
	
	public ErrorMessage findErrorMessageById(String id);
	
	public void saveAndStartFlow(ErrorMessage errorMessage);
	
	public void saveFlow(String errorMessageId);
	
	
}
