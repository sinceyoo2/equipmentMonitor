package com.syo.platform.service;

import java.util.List;
import java.util.Map;

import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.LibraryPlan;
import com.syo.platform.entity.PlanShape;

public interface LibraryPlanService {
	
	public LibraryPlan findLibraryPlan(String id);
	
	public LibraryPlan findLibraryPlanByPlanNo(String planNo);

	public Map findLibraryPlanTree();
	
	public List<PlanShape> findPlanShapeByPlanId(String planId);
	
	public List<Equipment> findEquipmentByPlanId(String planId);
	
}
