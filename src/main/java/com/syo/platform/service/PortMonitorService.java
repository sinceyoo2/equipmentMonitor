package com.syo.platform.service;

import java.util.List;

import com.syo.platform.entity.PortMonitorConfig;

public interface PortMonitorService {

	public void saveConfig(PortMonitorConfig config);
	
	public List<PortMonitorConfig> findConfig(String vague);
	
	public PortMonitorConfig findConfigById(String id);
	
	public void deleteConfig(String id);
	
	public boolean startMonitor(String configId);
	
	public boolean stopMonitor(String configId);
	
	public void switchMonitor(String configId);
	
	boolean monitor(String configId);
	
	public void saveBind(String configId, String equipmentId);

}
