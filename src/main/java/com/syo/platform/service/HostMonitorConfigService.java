package com.syo.platform.service;

import java.util.List;

import com.syo.platform.entity.HostMonitorConfig;

public interface HostMonitorConfigService {

	public void saveConfig(HostMonitorConfig config);
	
	public List<HostMonitorConfig> findConfig(String vague);
	
	public HostMonitorConfig findConfigById(String id);
	
	public HostMonitorConfig findConfigByIp(String ip);
	
	public void deleteConfig(String id);
	
	public void saveBind(String configId, String equipmentId);
	
}
