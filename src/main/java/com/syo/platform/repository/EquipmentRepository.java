package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, String>, JpaSpecificationExecutor<Equipment> {

	public Page<Equipment> findByType(String type, Pageable page);

	@Query("from Equipment e where e.equipmentName like %:vague% or e.equipmentNo like %:vague% or e.deptName like %:vague%")
	public Page<Equipment> findByVague(@Param("vague")String vague, Pageable page);
	
	@Query("from Equipment e where e.type=:type and (e.equipmentName like %:vague% or e.equipmentNo like %:vague% or e.deptName like %:vague%)")
	public Page<Equipment> findByTypeAndVague(@Param("type")String type, @Param("vague")String vague, Pageable page);
	
	@Query("from Equipment e where e.cabinet.id=:cabinetId")
	public List<Equipment> findByCabinetId(@Param("cabinetId")String cabinetId);
	
	@Query("from Equipment e where e.libraryPlan.id=:planId")
	public List<Equipment> findByPlanId(@Param("planId")String planId);
	
	public List<Equipment> findByIsObserver(boolean isObserver);
	
	public Equipment findByIsObserverAndIpAddress(boolean isObserver, String ipAddress);

}
