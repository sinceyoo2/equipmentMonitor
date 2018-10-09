package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.PingMonitorConfig;
import com.syo.platform.entity.WebMonitorConfig;

public interface PingMonitorConfigRepository extends JpaRepository<PingMonitorConfig, String> {
	
	@Query("from PingMonitorConfig c where c.name like %:vague% or c.remarks like %:vague%")
	public List<PingMonitorConfig> findByVague(@Param("vague")String vague);
	
	@Query("from PingMonitorConfig c where c.status='监控中'")
	public List<PingMonitorConfig> findRunning();

	@Query("select count(id) from PingMonitorConfig c where c.lastResult='故障' and c.bindEquipment.id=:equipmentId")
	public int findErrorCountByBindEquipment(@Param("equipmentId")String equipmentId);
	
	public PingMonitorConfig findByIpAddress(String ipAddress);
	
}
