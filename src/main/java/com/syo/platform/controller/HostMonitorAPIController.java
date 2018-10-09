package com.syo.platform.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.HostErrorMessage;
import com.syo.platform.entity.HostMonitorConfig;
import com.syo.platform.service.HostMonitorConfigService;
import com.syo.platform.service.HostMonitorService;

@RestController
@RequestMapping("/api/hostMonitor")
public class HostMonitorAPIController {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HostMonitorConfigService hostMonitorConfigService;
	
	@Autowired
	private HostMonitorService hostMonitorService;
	
	@RequestMapping("/hostConfig")
	public HostMonitorConfig findConfig(String ip) {
		System.out.println(ip);
		return hostMonitorConfigService.findConfigByIp(ip);
	}
	
	@PostMapping("/handleError")
	public String handleError(@RequestBody HostErrorMessage hostError) {
		hostMonitorService.handleError(hostError);
		return "SUCCESS";
	}
	
	@RequestMapping("/runningError")
	public List<ErrorMessage> findRunningError(String configId) {
		return hostMonitorService.findRunningByConfigId(configId);
	}
	
	@RequestMapping("/monitor")
	@ResponseBody
	public Map<String, Object> monitor(String ip) {
		return restTemplate.getForObject("http://"+ip+":6666/monitorInfos", Map.class);
	}
}
