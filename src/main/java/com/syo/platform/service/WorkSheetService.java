package com.syo.platform.service;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.data.domain.Page;

import com.syo.platform.dto.WorkSheetDto;
import com.syo.platform.entity.WorkSheet;

public interface WorkSheetService {

	public void saveWorkSheet(WorkSheet workSheet, String account);
	
	public WorkSheet findWorkSheetById(String id);
	
	public Page<WorkSheet> findWorkSheets(int pageNo, int pageSize, WorkSheetDto dto);
	
	public List<WorkSheet> findWorkSheetsInTask(String account);
	
	public void file(DelegateExecution execution);
	
	public List<WorkSheet> findWorkSheetInHistoryTask(String account);
	
}
