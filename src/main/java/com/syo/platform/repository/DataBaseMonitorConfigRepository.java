package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.DataBaseMonitorConfig;

public interface DataBaseMonitorConfigRepository extends JpaRepository<DataBaseMonitorConfig, String> {
	
	@Query("from DataBaseMonitorConfig c where c.name like %:vague% or c.dbVendor like %:vague% or c.remarks like %:vague%")
	public List<DataBaseMonitorConfig> findByVague(@Param("vague")String vague);
	
	@Query("from DataBaseMonitorConfig c where c.status='监控中'")
	public List<DataBaseMonitorConfig> findRunning();
	
	@Query("select count(id) from DataBaseMonitorConfig c where c.lastResult='故障' and c.bindEquipment.id=:equipmentId")
	public int findErrorCountByBindEquipment(@Param("equipmentId")String equipmentId);
}
