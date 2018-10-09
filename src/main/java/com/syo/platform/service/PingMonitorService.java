package com.syo.platform.service;

import java.util.List;

import com.syo.platform.entity.PingMonitorConfig;

public interface PingMonitorService {

	public void saveConfig(PingMonitorConfig config);
	
	public List<PingMonitorConfig> findConfig(String vague);
	
	public PingMonitorConfig findConfigById(String id);
	
	public void deleteConfig(String id);
	
	public boolean startMonitor(String configId);
	
	public boolean stopMonitor(String configId);
	
	public void switchMonitor(String configId);
	
	boolean monitor(String configId);
	
	public void saveBind(String configId, String equipmentId);

	public List<PingMonitorConfig> findSegmentConfig(String fromIp, String toIp);
	
	public String saveConigs(List<PingMonitorConfig> configs);
}
