package com.syo.platform.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.syo.platform.dto.ErrorAPIDTO;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.service.impl.AppSystemLogServiceImpl.AppSystemLogException;

@RestController
@RequestMapping("/api/errorMessage")
public class ErrorMsgAPIController {

	@Autowired
	private ErrorMessageService errorMessageService;
	
	@RequestMapping("/declareError")
	public APIResult declareError(@RequestBody ErrorAPIDTO apiDto) {
		apiDto.setStatus("发现故障");
		errorMessageService.saveErrorMessage(apiDto.createErrorMessage());
		return new APIResult("0", "成功");
	}
	
	@RequestMapping("/errorMessage")
	public APIResult findErrorById(@RequestBody String id) {
		return new APIResult("0", "成功", errorMessageService.findErrorMessageById(id));
	}
	
	
	@RequestMapping("/warning")
	public APIResult findWarning(@RequestBody APIPageRequest page) {
		if(page==null) {
			page = new APIPageRequest();
		}
		return new APIResult("0", "成功", errorMessageService.findWarningErros(page.getPageNo(), page.getPageSize()));
	}
	
	@ExceptionHandler
	public APIResult exceptionHandler(Exception ex) {
		ex.printStackTrace();		
		return new APIResult("500", "未知内容错误");
	}
	
	@RequestMapping("/setFlowStatus")
	public String setFlowStatus(String id, String status) {
		ErrorMessage error = errorMessageService.findErrorMessageById(id);
		error.setHashWorkSheet(true);
		error.setWorkSheetStatus(status);
		errorMessageService.saveErrorMessage(error);
		return "SUCCESS";
	}
	
//	@RequestMapping("/declareError")
//	public String declareError(ErrorAPIDTO apiDto) {
//		apiDto.setStatus("发现故障");
//		errorMessageService.saveErrorMessage(apiDto.createErrorMessage());
//		return "SUCCESS";
//	}
	
//	@RequestMapping("/recoveryError")
//	public String recoveryError(String id, String status, Date recoveryTime) {
//		if("处理中".equals(status) || "已恢复".equals(status)) {
//			ErrorMessage msg = errorMessageService.findErrorMessageById(id);
//			msg.setStatus(status);
//			
//			if("已恢复".equals(status)) {
//				msg.setRecoveryTime(recoveryTime);
//				msg.setDuration((msg.getRecoveryTime().getTime()-msg.getErrorTime().getTime())/1000);
//			}
//			
//			errorMessageService.saveErrorMessage(msg);
//			return "SUCCESS";
//		}
//		return "FAIL";
//	}
//	
//	@RequestMapping("/errorMessage")
//	public ErrorMessage findErrorById(String id) {
//		return errorMessageService.findErrorMessageById(id);
//	}
//	
	@RequestMapping("/warningErr")
	@ResponseBody
	public Page<ErrorMessage> findWarning(@RequestParam(defaultValue="1", required=false)int pageNo, @RequestParam(defaultValue="15", required=false)int pageSize) {
		return errorMessageService.findWarningErros(pageNo, pageSize);
	}
	
}
