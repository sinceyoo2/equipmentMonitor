package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.PlanShape;

public interface PlanShapeRepository extends JpaRepository<PlanShape, String> {

	@Query("from PlanShape p where p.libraryPlan.id=:planId order by p.sort asc")
	public List<PlanShape> findByPlanId(@Param("planId")String planId);
	
}
