package com.syo.platform.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.LibraryPlan;
import com.syo.platform.entity.PlanShape;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.LibraryPlanRepository;
import com.syo.platform.repository.PlanShapeRepository;
import com.syo.platform.service.LibraryPlanService;

@Service
public class LibraryPlanServiceImpl implements LibraryPlanService {
	
	@Autowired
	private LibraryPlanRepository libraryPlanRepository;
	
	@Autowired
	private PlanShapeRepository planShapeRepository;
	
	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Autowired
	private ErrorMessageRepository errorMessageRepository;
	
	@Override
	public LibraryPlan findLibraryPlan(String id) {
		return libraryPlanRepository.findOne(id);
	}
	
	@Override
	public LibraryPlan findLibraryPlanByPlanNo(String planNo) {
		// TODO Auto-generated method stub
		return libraryPlanRepository.findByPlanNo(planNo);
	}

	@Override
	public Map findLibraryPlanTree() {
		List<LibraryPlan> plans = libraryPlanRepository.findAll(new Sort(Direction.ASC,"sort"));
		
		Map<String, Map<String,Object>> rest = new LinkedHashMap<String, Map<String,Object>>();

		for(LibraryPlan plan : plans) {
			if(rest.get(plan.getLib().getLibNo())==null) {
				Map<String, Object> libMap = new HashMap<String, Object>();
				libMap.put("text", plan.getLib().getLibName());
				libMap.put("type", "folder");
				Map<String, Object> additionalParameters = new HashMap<String, Object>();
				additionalParameters.put("libNo", plan.getLib().getLibNo());
				additionalParameters.put("children", new LinkedHashMap<String, Object>());
				libMap.put("additionalParameters", additionalParameters);
				rest.put(plan.getLib().getLibNo(), libMap);
			}
			Map<String, Object> libMap = rest.get(plan.getLib().getLibNo());
			Map<String, Object> additionalParameters = (Map<String, Object>) libMap.get("additionalParameters");
			Map<String, Object> children = (Map<String, Object>) additionalParameters.get("children");

			Map<String,Object> childMap = new HashMap<String, Object>();
//			childMap.put("text", "<a href='plan/map/"+plan.getId()+"'>"+plan.getName()+"</a>");
			childMap.put("text", "<a href='javascript:planClick(\""+plan.getId()+"\")'>"+plan.getName()+"</a>");
			childMap.put("type", "item");
			Map<String, Object> childAdditionalParameters = new HashMap<String, Object>();
			childAdditionalParameters.put("planNo", plan.getPlanNo());
			childMap.put("additionalParameters", childAdditionalParameters);
			children.put(plan.getPlanNo(), childMap);
			
		}
		
		return rest;
	}

	@Override
	public List<PlanShape> findPlanShapeByPlanId(String planId) {
		// TODO Auto-generated method stub
		return planShapeRepository.findByPlanId(planId);
	}

	@Override
	public List<Equipment> findEquipmentByPlanId(String planId) {
		List<Equipment> equipments = equipmentRepository.findByPlanId(planId);
		
		List<ErrorMessage> errs = errorMessageRepository.findRunningByPlan(planId);
		if(errs!=null && errs.size()>0) {
			Map<String, Boolean> isErrMapping = new HashMap<>();
			for(ErrorMessage err : errs) {
				isErrMapping.put(err.getEquipment().getId(), true);
			}
			for(Equipment equipment : equipments) {
				equipment.setHasProblem(isErrMapping.get(equipment.getId())==null?false:true);
			}
		}
		
		return equipments;
	}

	

}