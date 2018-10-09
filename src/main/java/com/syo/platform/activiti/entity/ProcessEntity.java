package com.syo.platform.activiti.entity;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

public interface ProcessEntity {

	public String getProcessName();
	
	public Task getTask();
	
	public HistoricTaskInstance getHistoryTask();
	
}
