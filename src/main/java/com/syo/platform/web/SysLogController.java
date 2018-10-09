package com.syo.platform.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.dto.SysLogQueryDTO;
import com.syo.platform.entity.SysLog;
import com.syo.platform.service.SysLogService;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {
	
	@Autowired
	private SysLogService sysLogService;

	@RequestMapping("/{pageSize}/{pageNo}")
	public String findSysLog(SysLogQueryDTO condition, @PathVariable("pageNo")int pageNo, @PathVariable("pageSize")int pageSize, Model model) {
		model.addAttribute("condition", condition);
		Page<SysLog> logs = sysLogService.findSysLog(pageNo, pageSize, condition);
		model.addAttribute("logs", logs);
		return "sysLog";
	}
	
	@RequestMapping("/{pageNo}")
	public String findSysLog(SysLogQueryDTO condition, @PathVariable("pageNo")int pageNo, Model model) {
		return findSysLog(condition, pageNo, 15, model);
	}
	
	@RequestMapping({"","/","/logs"})
	public String findSysLog(SysLogQueryDTO condition, Model model) {
		return findSysLog(condition, 1, 15, model);
	}
	
	@RequestMapping("/item/{id}")
	@ResponseBody
	public SysLog findLogById(@PathVariable("id")String id) {
		SysLog log = sysLogService.findSysLogById(id);
//		log.getUser();
		return log;
	}
	
}
