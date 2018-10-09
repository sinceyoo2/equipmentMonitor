package com.syo.platform.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syo.platform.entity.AppSystemLog;
import com.syo.platform.repository.AppSystemLogRepository;
import com.syo.platform.repository.jpautils.Criteria;
import com.syo.platform.repository.jpautils.Restrictions;
import com.syo.platform.service.AppSystemLogService;

@Service
public class AppSystemLogServiceImpl implements AppSystemLogService {

	@Autowired
	private AppSystemLogRepository appSystemLogRepository;
	
	private List<String> operateTypes = Arrays.asList(new String[]{"登录","查询","新增","修改","删除"});

	@Override
	public void saveLog(AppSystemLog appSystemLog) {
		if(appSystemLog.getInsid()!=null) {
			throw new AppSystemLogException("500", "不允许更新日志");
		}
		if(StringUtils.isBlank(appSystemLog.getBusiSystemName()) 
				|| StringUtils.isBlank(appSystemLog.getOperateModule())  
				|| StringUtils.isBlank(appSystemLog.getUserId())  
				|| StringUtils.isBlank(appSystemLog.getUserName()) 
				|| StringUtils.isBlank(appSystemLog.getOperateType())) {
			throw new AppSystemLogException("500", "日志信息不完整");
		}
		appSystemLogRepository.save(appSystemLog);
	}

	@Override
	public void saveLog(String logJson) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			AppSystemLog log = mapper.readValue(logJson, AppSystemLog.class);
			saveLog(log);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof AppSystemLogException) {
				throw (AppSystemLogException)e;
			}
			throw new AppSystemLogException("500", "参数解析错误");
		}	
	}

	@Override
	public AppSystemLog findLog(String id) {
		return appSystemLogRepository.findOne(id);
	}

	@Override
	public Page<AppSystemLog> findLogs(int pageNo, int pageSize, Date start, Date end, String type, String system, String module, String vague) {
		Criteria<AppSystemLog> c = new Criteria<>();
		c.add(Restrictions.gte("operateTime", start, true));
		c.add(Restrictions.lt("operateTime", end, true));
		if(StringUtils.isNotBlank(type)) {
			c.add(Restrictions.eq("operateType", type, true));
		}
		if(StringUtils.isNotBlank(system)) {
			c.add(Restrictions.like("busiSystemName", system, true));
		}
		if(StringUtils.isNotBlank(module)) {
			c.add(Restrictions.like("operateModule", module, true));
		}
		if(StringUtils.isNotBlank(vague)) {
			c.add(Restrictions.or(Restrictions.like("userName", vague, true), Restrictions.like("userId", vague, true), Restrictions.like("operateIP", vague, true), Restrictions.like("deptName", vague, true)));
		}
		return appSystemLogRepository.findAll(c, new PageRequest(pageNo-1, pageSize, Direction.DESC, "operateTime"));
	}
	
	public class AppSystemLogException extends RuntimeException {
		
		private String code;
		
		private String message;

		public AppSystemLogException(String code, String message) {
			super();
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
}
