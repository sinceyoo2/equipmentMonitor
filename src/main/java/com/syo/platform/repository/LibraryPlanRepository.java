package com.syo.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syo.platform.entity.LibraryPlan;

public interface LibraryPlanRepository extends JpaRepository<LibraryPlan, String> {

	public LibraryPlan findByPlanNo(String planNo);
	
}
