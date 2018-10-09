package com.syo.platform.service;

import java.util.List;

import com.syo.platform.entity.DataBaseMonitorConfig;

public interface DataBaseMonitorService {
	
	public void saveConfig(DataBaseMonitorConfig config);
	
	public List<DataBaseMonitorConfig> findConfig(String vague);
	
	public DataBaseMonitorConfig findConfigById(String id);
	
	public void deleteConfig(String id);
	
	public boolean startMonitor(String configId);
	
	public boolean stopMonitor(String configId);
	
	
	public void switchMonitor(String configId);
	
	boolean monitor(String configId);
	
	public String testDB(DataBaseMonitorConfig config);

	public void saveBind(String configId, String equipmentId);
	
}
