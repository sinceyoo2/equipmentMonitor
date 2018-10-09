package com.syo.platform.service;

import java.util.List;

import com.syo.platform.entity.SwitchMonitorConfig;


public interface SwitchMonitorService {

	public void saveConfig(SwitchMonitorConfig config);
	
	public List<SwitchMonitorConfig> findConfig(String vague);
	
	public SwitchMonitorConfig findConfigById(String id);
	
	public void deleteConfig(String id);
	
	public boolean startMonitor(String configId);
	
	public boolean stopMonitor(String configId);
	
	public void switchMonitor(String configId);
	
	boolean monitor(String configId);
	
	public void saveBind(String configId, String equipmentId);

}
