package com.syo.platform.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syo.platform.entity.AppSystemLog;
import com.syo.platform.service.AppSystemLogService;
import com.syo.platform.service.impl.AppSystemLogServiceImpl.AppSystemLogException;

@RestController
@RequestMapping("/api/app_log")
public class AppSystemLogAPIController {

	@Autowired
	private AppSystemLogService appSystemLogService;
	
//	@PostMapping("/save")
//	public APIResult save(@RequestBody String logJson) {
//		System.out.println(logJson);
////		appSystemLogService.saveLog(logJson);
//		return new APIResult("0", "成功");
//	}
	
//	@PostMapping("/save")
//	public APIResult save(HttpServletRequest req) {
//		String logJson = req.getParameterMap().keySet().toArray()[0].toString();
//		appSystemLogService.saveLog(logJson);
//		return new APIResult("0", "成功");
//	}
	
//	@PostMapping("/save")
//	public APIResult save(@RequestBody String logJson) {
//		appSystemLogService.saveLog(logJson);
//		return new APIResult("0", "成功");
//	}
	
	@PostMapping("/save")
	public APIResult save(@RequestBody AppSystemLog appSystemLog) {
		appSystemLogService.saveLog(appSystemLog);
		return new APIResult("0", "成功");
	}
	
	
	@ExceptionHandler
	public APIResult exceptionHandler(Exception ex) {
		ex.printStackTrace();
		if(ex instanceof AppSystemLogException) {
			AppSystemLogException appex = (AppSystemLogException) ex;
			return new APIResult(appex.getCode(), appex.getMessage());
		}
		
		return new APIResult("500", "未知内容错误");
	}

}
