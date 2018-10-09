package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.ErrorMessage;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, String>, JpaSpecificationExecutor<ErrorMessage> {

	@Query("from ErrorMessage e where e.targetType=:targetType and e.status='发现故障' and e.msgSource='监控'")
	public List<ErrorMessage> findRunning(@Param("targetType")String targetType);
	
	@Query("from ErrorMessage e where e.configId=:configId and e.status='发现故障' and e.msgSource='监控'")
	public List<ErrorMessage> findRunningByConfigId(@Param("configId")String configId);
	
	@Query("from ErrorMessage e where e.status='发现故障' and e.equipment.id in (select id from Equipment eq where eq.libraryPlan.id=:planId)")
	public List<ErrorMessage> findRunningByPlan(@Param("planId")String planId); 
	
}
