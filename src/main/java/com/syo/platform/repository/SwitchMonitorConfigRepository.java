package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.SwitchMonitorConfig;

public interface SwitchMonitorConfigRepository extends JpaRepository<SwitchMonitorConfig, String> {

	@Query("from SwitchMonitorConfig c where c.name like %:vague% or c.vendor like %:vague% or c.model like %:vague% or c.remarks like %:vague%")
	public List<SwitchMonitorConfig> findByVague(@Param("vague")String vague);
	
	@Query("from SwitchMonitorConfig c where c.status='监控中'")
	public List<SwitchMonitorConfig> findRunning();
	
}
