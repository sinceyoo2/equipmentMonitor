package com.syo.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.syo.platform.dto.SysLogQueryDTO;
import com.syo.platform.entity.SysLog;
import com.syo.platform.repository.SysLogRepository;
import com.syo.platform.repository.jpautils.Criteria;
import com.syo.platform.repository.jpautils.Restrictions;
import com.syo.platform.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService {

	
	@Autowired
	private SysLogRepository sysLogRepository;
	
	@Value("${sysLog.enable}")
	private boolean logEnable;
	
	@Override
	public Page<SysLog> findSysLog(int pageNo, int pageSize, SysLogQueryDTO condition) {
		Criteria<SysLog> c = new Criteria<SysLog>();
		c.add(Restrictions.gte("logTime", condition.getStart(), true));
		c.add(Restrictions.lt("logTime", condition.getEnd(), true));
		c.add(Restrictions.like("user.account", condition.getAccount(), true));
		c.add(Restrictions.like("fromIP", condition.getFromIP(), true));
		c.add(Restrictions.eq("logType", condition.getLogType(), true));
		return sysLogRepository.findAll(c, new PageRequest(pageNo-1, pageSize, Direction.DESC, "logTime"));
	}

	@Override
	public SysLog findSysLogById(String id) {
		return sysLogRepository.findOne(id);
	}

	@Override
	public void saveLog(SysLog log) {
		if(logEnable)
			sysLogRepository.save(log);
		else 
			System.out.println(log);
	}

}
