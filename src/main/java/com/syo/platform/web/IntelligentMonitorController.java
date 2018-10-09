package com.syo.platform.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.syo.platform.service.EquipmentService;

@Controller
@RequestMapping("/intelligent")
public class IntelligentMonitorController {
	
	@Autowired
	private EquipmentService equipmentService;

	@RequestMapping({"","/"})
	public String intelligentMonitor(String vague, Model model) {
//		model.addAttribute("configs", middlewareMonitorService.findConfig(vague));
//		model.addAttribute("vague", vague);
		model.addAttribute("equipments", equipmentService.findObserverEquipment());
		return "intelligentMonitor";
	}
	
}
