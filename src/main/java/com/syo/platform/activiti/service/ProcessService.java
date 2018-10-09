package com.syo.platform.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

public interface ProcessService {
	
	/**
	 * 启动流程
	 */
	public void startProcess(String processKey, String businessKey);
	
	/**
	 * 查找代办
	 * @param account
	 * @return
	 */
	public List<Task> findTaskByAccount(String account);
	
	/**
	 * 
	 * @param account
	 * @param processKey
	 * @return
	 */
	public List<Task> findTaskByAccountAndProcessKey(String account, String processKey);
	
	/**
	 * 
	 * @param taskId
	 * @param account
	 * @param formEntity
	 */
	public void completeTaskByUser(String taskId, String account, Object formEntity, Object formEntityId);
		
	/**
	 * 
	 * @param taskId
	 * @param account
	 * @param formEntity
	 * @param timeField
	 * @param auditFields
	 */
	public void completeTaskByUser(String taskId, String account, Object formEntity, String timeField, String... auditFields);
	
	/**
	 * 保存表单对象
	 * @param formEntity
	 */
	public void saveFormEntity(Object formEntity);
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Object findFormEntity(Object id);
	
	/**
	 * 
	 * @param execution
	 * @param executorTag
	 * @return
	 */
	public List<String> findTaskExecutor(DelegateExecution execution, String executorTag);
	
	/**
	 * 
	 * @param execution
	 * @param status
	 */
	public void changeStatus(DelegateExecution execution, String statusField, String status);
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public Object findFormEntityByTaskId(String taskId);
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public Map<String, Map<String, Boolean>> findFormPropertyByTaskId(String taskId);
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	public Task findTaskById(String taskId);
	
	/**
	 * 
	 * @param account
	 * @return
	 */
	public List<HistoricTaskInstance> findHistoryTaskByAccount(String account);
	
	/**
	 * 
	 * @param account
	 * @param processKey
	 * @return
	 */
	public List<HistoricTaskInstance> findHistoryTaskByAccountAndProcessKey(String account, String processKey);
	
	/**
	 * 
	 * @param processKey
	 * @return
	 */
	public List<HistoricTaskInstance> findHistoryTaskByBusinessKey(String businessKey);
	
	/**
	 * 
	 * @param historyTaskId
	 * @return
	 */
	public Object findFormEntityByHistoryTaskId(String historyTaskId);
	
	
	

}
