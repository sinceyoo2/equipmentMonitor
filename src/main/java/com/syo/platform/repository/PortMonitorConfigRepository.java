package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.PortMonitorConfig;

public interface PortMonitorConfigRepository extends JpaRepository<PortMonitorConfig, String> {

	@Query("from PortMonitorConfig c where c.name like %:vague% or c.serviceName like %:vague% or c.remarks like %:vague%")
	public List<PortMonitorConfig> findByVague(@Param("vague")String vague);
	
	@Query("from PortMonitorConfig c where c.status='监控中'")
	public List<PortMonitorConfig> findRunning();
	
	@Query("select count(id) from PortMonitorConfig c where c.lastResult='故障' and c.bindEquipment.id=:equipmentId")
	public int findErrorCountByBindEquipment(@Param("equipmentId")String equipmentId);
	
}
