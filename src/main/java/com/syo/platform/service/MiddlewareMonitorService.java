package com.syo.platform.service;

import java.util.List;

import com.syo.platform.entity.MiddlewareMonitorConfig;

public interface MiddlewareMonitorService {

	public void saveConfig(MiddlewareMonitorConfig config);
	
	public List<MiddlewareMonitorConfig> findConfig(String vague);
	
	public MiddlewareMonitorConfig findConfigById(String id);
	
	public void deleteConfig(String id);
	
	public boolean startMonitor(String configId);
	
	public boolean stopMonitor(String configId);
	
	public void switchMonitor(String configId);
	
	boolean monitor(String configId);
	
	public void saveBind(String configId, String equipmentId);

}
