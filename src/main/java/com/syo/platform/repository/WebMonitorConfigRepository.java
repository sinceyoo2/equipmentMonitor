package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.WebMonitorConfig;

public interface WebMonitorConfigRepository extends JpaRepository<WebMonitorConfig, String> {
	
	@Query("from WebMonitorConfig c where c.name like %:vague% or c.webServerVendor like %:vague% or c.remarks like %:vague% or c.serviceName like %:vague%")
	public List<WebMonitorConfig> findByVague(@Param("vague")String vague);
	
	@Query("from WebMonitorConfig c where c.status='监控中'")
	public List<WebMonitorConfig> findRunning();

	@Query("select count(id) from WebMonitorConfig c where c.lastResult='故障' and c.bindEquipment.id=:equipmentId")
	public int findErrorCountByBindEquipment(@Param("equipmentId")String equipmentId);
	
}
