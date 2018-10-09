package com.syo.platform.activiti.service.impl;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import com.syo.platform.activiti.exception.FormFieldDissatisfyException;
import com.syo.platform.activiti.service.ProcessService;
import com.syo.platform.entity.User;
import com.syo.platform.repository.UserRepository;

public abstract class GenericProcessService implements ProcessService {
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public final void startProcess(String processKey, String businessKey) {
		runtimeService.startProcessInstanceByKey(processKey, businessKey);
	}

	@Override
	public final List<Task> findTaskByAccount(String account) {
		return taskService.createTaskQuery().taskCandidateOrAssigned(account).orderByTaskCreateTime().desc().list();
	}
	
	@Override
	public List<Task> findTaskByAccountAndProcessKey(String account, String processKey) {
		return taskService.createTaskQuery().taskCandidateOrAssigned(account).processDefinitionKey(processKey).orderByTaskCreateTime().desc().list();
	}
	
	@Override
	public final void completeTaskByUser(String taskId, String account, Object formEntity, Object formEntityId) {
		User user = userRepository.findOne(account);
		Object persistedEntity = findFormEntity(formEntityId);
		
		taskService.claim(taskId, account);
		Map<String, Object> var = new HashMap<>();
		List<FormProperty> list = formService.getTaskFormData(taskId).getFormProperties();
		if(list!=null && list.size()>0){  
            for(FormProperty formProperty:list){  
            	if(!formProperty.isWritable()) {
            		continue;
            	}
            	String fieldName = formProperty.getName();
            	if(formEntity instanceof Map) {
    				Map entityMap = (Map) formEntity;
    				
    				var.put(fieldName, entityMap.get(fieldName));
    				((Map)persistedEntity).put(fieldName, entityMap.get(fieldName));
    				
    				if(formProperty.isRequired() && ((Map)persistedEntity).get(fieldName)==null) {
    					throw new FormFieldDissatisfyException(fieldName, "不能为空");
    				}
    			} else {
    				try {
						Field field = formEntity.getClass().getDeclaredField(fieldName);
						field.setAccessible(true);
						
						if(formProperty.getType().getName().equals("date")) {
							if(field.get(formEntity)==null) {
								field.set(persistedEntity, new Date());
							} else {
								field.set(persistedEntity, field.get(formEntity));
							}
							var.put(fieldName, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(field.get(persistedEntity)));
						} else {
							if(field.getType().getName().equals(User.class.getName())) {
								if(field.get(formEntity)==null) {
									field.set(persistedEntity, user);
								} else {
									field.set(persistedEntity, field.get(formEntity));
								}
								var.put(fieldName, ((User)field.get(persistedEntity)).getAccount());
							} else {
								field.set(persistedEntity, field.get(formEntity));
								var.put(fieldName, field.get(persistedEntity));
							}
						}
						if(formProperty.isRequired() && field.get(persistedEntity)==null) {
	    					throw new FormFieldDissatisfyException(fieldName, "不能为空");
	    				}
					} catch (Exception e) {
						if(e instanceof FormFieldDissatisfyException) {
							throw (FormFieldDissatisfyException)e;
						}
						e.printStackTrace();
					}
    			}
             
            }  
        }
		taskService.complete(taskId, var);
		
		saveFormEntity(persistedEntity);
	}

	@Override
	public final void completeTaskByUser(String taskId, String account, Object formEntity, String timeField,
			String... auditFields) {
		taskService.claim(taskId, account);
		
		Map<String, Object> var = new HashMap<>();
		if(auditFields!=null && auditFields.length>0) {			
			if(formEntity instanceof Map) {
				Map entityMap = (Map) formEntity;
				for(String auditField : auditFields) {
					var.put(auditField, entityMap.get(auditField));
				}
			} else {
				for(String auditField : auditFields) {
					try {
						Field field = formEntity.getClass().getDeclaredField(auditField);
						field.setAccessible(true);
						var.put(auditField, field.get(formEntity));
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(timeField!=null) {
			if(formEntity instanceof Map) {
				Map entityMap = (Map) formEntity;
				entityMap.put(timeField, new Date());
			} else {
				try {
					Field field = formEntity.getClass().getField(timeField);
					field.setAccessible(true);
					field.set(formEntity, new Date());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
			}
		}
		taskService.complete(taskId, var);
		
		saveFormEntity(formEntity);
		
	}

	@Override
	public void changeStatus(DelegateExecution execution, String statusField, String status) {
		Object formEntity = findFormEntity(execution.getProcessBusinessKey());
		if(formEntity instanceof Map) {
			Map entityMap = (Map) formEntity;
			entityMap.put(statusField, status);
		} else {
			try {
				Field field = formEntity.getClass().getDeclaredField(statusField);
				field.setAccessible(true);
				field.set(formEntity, status);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		saveFormEntity(formEntity);
	}

	@Override
	public Object findFormEntityByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		Object businessKey = processInstance.getBusinessKey();
		return findFormEntity(businessKey);
	}

	@Override
	public Map<String, Map<String, Boolean>> findFormPropertyByTaskId(String taskId) {
		Map<String, Map<String, Boolean>> result = new HashMap<>();
		result.put("readable", new HashMap<>());
		result.put("writable", new HashMap<>());
		List<FormProperty> list = formService.getTaskFormData(taskId).getFormProperties();
		for(FormProperty formProperty:list){
			if(formProperty.isReadable()) {
				result.get("readable").put(formProperty.getId(), true);
			} else {
				result.get("readable").put(formProperty.getId(), false);
			}
			
			if(formProperty.isWritable()) {
				result.get("writable").put(formProperty.getId(), true);
			} else {
				result.get("writable").put(formProperty.getId(), false);
			}
		}
		return result;
	}

	@Override
	public Task findTaskById(String taskId) {
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@Override
	public List<HistoricTaskInstance> findHistoryTaskByAccount(String account) {
		return historyService.createHistoricTaskInstanceQuery().taskAssignee(account).orderByTaskCreateTime().desc().list();
	}

	@Override
	public List<HistoricTaskInstance> findHistoryTaskByAccountAndProcessKey(String account, String processKey) {
		return historyService.createHistoricTaskInstanceQuery().taskAssignee(account).processDefinitionKey(processKey).orderByTaskCreateTime().desc().list();
	}

	@Override
	public List<HistoricTaskInstance> findHistoryTaskByBusinessKey(String businessKey) {
		return historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).orderByTaskCreateTime().asc().list();
	}

	@Override
	public Object findFormEntityByHistoryTaskId(String historyTaskId) {
		HistoricTaskInstance historyTask = historyService.createHistoricTaskInstanceQuery().taskId(historyTaskId).singleResult();
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historyTask.getProcessInstanceId()).singleResult();
		Object businessKey = processInstance.getBusinessKey();
		return findFormEntity(businessKey);
	}

	
	

}
