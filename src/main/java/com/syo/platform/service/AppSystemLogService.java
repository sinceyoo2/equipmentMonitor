package com.syo.platform.service;

import java.util.Date;

import org.springframework.data.domain.Page;

import com.syo.platform.entity.AppSystemLog;

public interface AppSystemLogService {

	public void saveLog(AppSystemLog appSystemLog);
	
	public void saveLog(String logJson);
	
	public AppSystemLog findLog(String id);
	
	public Page<AppSystemLog> findLogs(int pageNo, int pageSize, Date start, Date end, String type, String system, String module, String vague);
	
}
