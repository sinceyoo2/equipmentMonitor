package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.MiddlewareMonitorConfig;

public interface MiddlewareMonitorConfigRepository extends JpaRepository<MiddlewareMonitorConfig, String> {

	@Query("from MiddlewareMonitorConfig c where c.name like %:vague% or c.serviceName like %:vague% or c.vendor like %:vague% or c.remarks like %:vague%")
	public List<MiddlewareMonitorConfig> findByVague(@Param("vague")String vague);
	
	@Query("from MiddlewareMonitorConfig c where c.status='监控中'")
	public List<MiddlewareMonitorConfig> findRunning();
	
	@Query("select count(id) from MiddlewareMonitorConfig c where c.lastResult='故障' and c.bindEquipment.id=:equipmentId")
	public int findErrorCountByBindEquipment(@Param("equipmentId")String equipmentId);
	
}
