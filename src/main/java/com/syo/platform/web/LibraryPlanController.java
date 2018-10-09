package com.syo.platform.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.LibraryPlan;
import com.syo.platform.service.EquipmentService;
import com.syo.platform.service.LibraryPlanService;

@Controller
@RequestMapping("/plan")
public class LibraryPlanController {

	@Autowired
	private LibraryPlanService libraryPlanService;
	
	@Autowired
	private EquipmentService equipmentService;
	
	@RequestMapping("/map")
	public String planMap(Model model) {
		return planMap("d64937a6-b9b3-4c0f-bf53-8f5b2e8b8ae6", model);
	}
	
	@RequestMapping("/map/{id}")
	public String planMap(@PathVariable("id")String id, Model model) {
		model.addAttribute("plan", libraryPlanService.findLibraryPlan(id));
		model.addAttribute("plans", libraryPlanService.findLibraryPlanTree());
		
		model.addAttribute("planShapes", libraryPlanService.findPlanShapeByPlanId(id));
		model.addAttribute("equipments", libraryPlanService.findEquipmentByPlanId(id));
		
		return "plan_map";
	}
	
	@RequestMapping("/position/{equipmentId}")
	public String planEdit(@PathVariable("equipmentId")String equipmentId, @RequestParam(name="plan", required=false)String planId, Model model) {
		
		model.addAttribute("plans", libraryPlanService.findLibraryPlanTree());
		Equipment equipment = equipmentService.findEquipmentById(equipmentId);
		model.addAttribute("equipment", equipment);
		
		LibraryPlan plan = null;
		if(planId!=null) {
			plan = libraryPlanService.findLibraryPlan(planId);
		} else {
			if(equipment.getLibraryPlan() != null) {
				plan = equipment.getLibraryPlan();
			} else {
				plan = libraryPlanService.findLibraryPlanByPlanNo("0001_1F");				
			}
		}
		model.addAttribute("plan", plan);
		model.addAttribute("planShapes", libraryPlanService.findPlanShapeByPlanId(plan.getId()));
		model.addAttribute("equipments", libraryPlanService.findEquipmentByPlanId(plan.getId()));
		
		return "plan_edit";
	}
	
	@PostMapping("/position_save")
	@ResponseBody
	public String savePlanPosition(String equipmentId, String libNo, String planId, double x, double y, double width, double height) {
		equipmentService.saveEquipmentPosition(equipmentId, libNo, planId, x, y, width, height);
//		return "SUCCESS";
		return "操作成功";
	}
	
}
