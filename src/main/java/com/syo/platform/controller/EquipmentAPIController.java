package com.syo.platform.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syo.platform.entity.Equipment;
import com.syo.platform.service.EquipmentService;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentAPIController {

	@Autowired
	private EquipmentService equipmentService;
	
	@RequestMapping("/{ip}")
	public Equipment findByIp(@PathVariable("ip")String ip) {
		return equipmentService.findObserverEquipmentByIP(ip);
	}
	
	@RequestMapping("/error/{equipmentId}")
	public String handleFollowerError(@PathVariable("equipmentId")String equipmentId, Date errorTime) {
		equipmentService.handleError(equipmentId, errorTime);
		return "SUCCESS";
	}
	
	@RequestMapping("/recovery/{equipmentId}")
	public String recoveryFollowError(@PathVariable("equipmentId")String equipmentId, Date recoveryTime) {
		equipmentService.recoveryError(equipmentId, recoveryTime);
		return "SUCCESS";
	}
	
}
