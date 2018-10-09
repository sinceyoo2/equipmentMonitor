package com.syo.platform.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.syo.platform.activiti.service.impl.GenericProcessService;
import com.syo.platform.dto.WorkSheetDto;
import com.syo.platform.entity.User;
import com.syo.platform.entity.WorkSheet;
import com.syo.platform.repository.UserRepository;
import com.syo.platform.repository.WorkSheetRepository;
import com.syo.platform.repository.jpautils.Criteria;
import com.syo.platform.repository.jpautils.Restrictions;
import com.syo.platform.service.WorkSheetService;


@Service("workSheetService")
public class WorkSheetServiceImpl extends GenericProcessService implements WorkSheetService {
	
	@Autowired
	private WorkSheetRepository workSheetRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveFormEntity(Object formEntity) {
		workSheetRepository.save((WorkSheet) formEntity);
	}

	@Override
	public Object findFormEntity(Object id) {
		return workSheetRepository.findOne((String) id);
	}

	@Override
	public List<String> findTaskExecutor(DelegateExecution execution, String executorTag) {
		WorkSheet workSheet = workSheetRepository.findOne(execution.getProcessBusinessKey());
		if("acceptor".equals(executorTag)) {
			if(workSheet.getAcceptor()!=null) {
				return Arrays.asList(workSheet.getAcceptor().getAccount());
			}
			return Arrays.asList("admin");
		} else if("executor".equals(executorTag)) {
			if(workSheet.getExecutor()!=null) {
				return Arrays.asList(workSheet.getExecutor().getAccount());
			}
			return Arrays.asList("user4","user5");
		}
		return null;
	}

	@Override
	public void saveWorkSheet(WorkSheet workSheet, String account) {
		User creator = userRepository.findOne(account);
		workSheet.setCreator(creator);
		workSheet.setCreateTime(new Date());
		WorkSheet newSheet = workSheetRepository.save(workSheet);
		startProcess("workSheetProcess", newSheet.getId());
	}

	@Override
	public WorkSheet findWorkSheetById(String id) {
		return workSheetRepository.findOne(id);
	}

	@Override
	public Page<WorkSheet> findWorkSheets(int pageNo, int pageSize, WorkSheetDto dto) {
		Criteria<WorkSheet> c = new Criteria<WorkSheet>();
		c.add(Restrictions.gt("createTime", dto.getStart(), true));
		c.add(Restrictions.lte("createTime", dto.getEnd(), true));
		c.add(Restrictions.eq("status", dto.getStatus(), true));
		c.add(Restrictions.eq("hasFiled", dto.getHasFiled(), true));
		if(StringUtils.isNotBlank(dto.getVague())) {
			c.add(Restrictions.or(Restrictions.like("title", dto.getVague(), true), Restrictions.like("situation", dto.getVague(), true)));
		}
		return workSheetRepository.findAll(c, new PageRequest(pageNo-1, pageSize, Direction.DESC, "createTime"));
	}

	@Override
	public List<WorkSheet> findWorkSheetsInTask(String account) {
		List<Task> tasks = findTaskByAccountAndProcessKey(account, "workSheetProcess");
		List<WorkSheet> list = new ArrayList<>();
		for(Task task : tasks) {
			WorkSheet workSheet = (WorkSheet) findFormEntityByTaskId(task.getId());
			workSheet.setTask(task);
			list.add(workSheet);
		}
		return list;
	}

	@Override
	public void file(DelegateExecution execution) {
		WorkSheet workSheet = workSheetRepository.findOne(execution.getProcessBusinessKey());
		workSheet.setHasFiled(true);
		workSheetRepository.save(workSheet);
	}

	@Override
	public List<WorkSheet> findWorkSheetInHistoryTask(String account) {
		Criteria<WorkSheet> c = new Criteria<WorkSheet>();
		c.add(Restrictions.or(Restrictions.eq("creator.account", account, true), Restrictions.eq("acceptor.account", account, true), Restrictions.eq("executor.account", account, true)));
		return workSheetRepository.findAll(c, new Sort(Direction.DESC, "createTime"));
	}

	@Override
	public List<HistoricTaskInstance> findHistoryTaskByBusinessKey(String businessKey) {
		WorkSheet workSheet = workSheetRepository.findOne(businessKey);

		HistoricTaskInstanceEntity first = new HistoricTaskInstanceEntity();
		first.setAssignee(workSheet.getCreator().getAccount());
		first.setName("提交工单");
		first.setStartTime(workSheet.getCreateTime());
		first.setEndTime(workSheet.getCreateTime());
		List<HistoricTaskInstance> list = new ArrayList<>();
		list.add(first);
		list.addAll(super.findHistoryTaskByBusinessKey(businessKey));
		if(workSheet.isHasFiled()) {
			HistoricTaskInstanceEntity last = new HistoricTaskInstanceEntity();
			last.setAssignee("-");
			last.setName("工单归档");
			last.setStartTime(workSheet.getRepaireConfirmTime());
			last.setEndTime(workSheet.getRepaireConfirmTime());
			list.add(last);
		}

		return list;
	}
	
	

}
