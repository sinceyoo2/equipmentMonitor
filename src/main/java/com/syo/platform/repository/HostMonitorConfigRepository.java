package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.HostMonitorConfig;

public interface HostMonitorConfigRepository extends JpaRepository<HostMonitorConfig, String> {

	@Query("from HostMonitorConfig c where c.hostName like %:vague% or c.ipAddress like %:vague% or c.remark like %:vague%")
	public List<HostMonitorConfig> findByVague(@Param("vague")String vague);
	
	public HostMonitorConfig findByIpAddress(String ipAddress);
	
	@Query("select count(id) from HostMonitorConfig c where c.lastResult='故障' and c.bindEquipment.id=:equipmentId")
	public int findErrorCountByBindEquipment(@Param("equipmentId")String equipmentId);
	
}
