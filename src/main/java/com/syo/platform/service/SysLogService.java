package com.syo.platform.service;

import org.springframework.data.domain.Page;

import com.syo.platform.dto.SysLogQueryDTO;
import com.syo.platform.entity.SysLog;

public interface SysLogService {

	public Page<SysLog> findSysLog(int pageNo, int pageSize, SysLogQueryDTO condition);
	
	public SysLog findSysLogById(String id);
	
	public void saveLog(SysLog log);
	
}
