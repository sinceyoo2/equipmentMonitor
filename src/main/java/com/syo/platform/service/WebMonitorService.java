package com.syo.platform.service;

import java.util.List;

import com.syo.platform.entity.WebMonitorConfig;

public interface WebMonitorService {

	public void saveConfig(WebMonitorConfig config);
	
	public List<WebMonitorConfig> findConfig(String vague);
	
	public WebMonitorConfig findConfigById(String id);
	
	public void deleteConfig(String id);
	
	public boolean startMonitor(String configId);
	
	public boolean stopMonitor(String configId);
	
	public void switchMonitor(String configId);
	
	boolean monitor(String configId);
	
	public void saveBind(String configId, String equipmentId);

}
