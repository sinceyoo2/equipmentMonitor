package com.syo.platform.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.entity.Cabinet;
import com.syo.platform.entity.Equipment;
import com.syo.platform.service.EquipmentService;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;
	
	@RequestMapping("/collect/{pageSize}/{pageNo}")
	public String equipment(@PathVariable("pageNo")int pageNo, @PathVariable("pageSize")int pageSize, Date start, Date end, String type, String vague, Model model) {
		model.addAttribute("equipments", equipmentService.findEquipment(pageNo, pageSize, start, end, type, vague));
		model.addAttribute("start",start);
		model.addAttribute("end",end);
		model.addAttribute("type",type);
		model.addAttribute("vague",vague);
		model.addAttribute("libs", equipmentService.findEngineRoomLibs());
		return "equipment";
	}
	
	@RequestMapping("/collect/{pageNo}")
	public String equipment(@PathVariable("pageNo")int pageNo, Date start, Date end, String type, String vague, Model model) {
		return equipment(pageNo, 15, start, end, type, vague, model);
	}
	
	@RequestMapping("/collect")
	public String equipment(Date start, Date end, String type, String vague, Model model) {
		return equipment(1, 15, start, end, type, vague, model);
	}
	
	@RequestMapping("/engineRoom")
	public String engineRoom(@RequestParam(required=false,defaultValue="0001")String libNo, Model model) {
		model.addAttribute("libs", equipmentService.findEngineRoomLibs());
		model.addAttribute("Arow",equipmentService.findCabinet(libNo, "A排"));
		model.addAttribute("Brow",equipmentService.findCabinet(libNo, "B排"));
		model.addAttribute("currLib", libNo);
		return "engine_room";
	}
	
	@PostMapping("/equipment_save")
	@ResponseBody
	public String saveEquipment(Equipment equipment) {
		if(equipment.getMyObserver()!=null && StringUtils.isBlank(equipment.getMyObserver().getId())) {
			equipment.setMyObserver(null);
		}
		equipmentService.saveEquipment(equipment);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@PostMapping("/server_position_save")
	@ResponseBody
	public String saveServerPosition(String serverId, String libNo, String cabinetId, int startU, int endU) {
		equipmentService.saveServerPosition(serverId, libNo, cabinetId, startU, endU);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/item/{id}")
	@ResponseBody
	public Equipment findEquipment(@PathVariable("id")String id) {
		return equipmentService.findEquipmentById(id);
	}
	
	@RequestMapping("/cabinet_server/{id}")
	@ResponseBody
	public Cabinet findCabinet(@PathVariable("id")String id) {
		Cabinet cabinet = equipmentService.findEquipmentById(id).getCabinet();
		if(cabinet!=null)
			cabinet.getLib();
		return cabinet;
	}
	
	@RequestMapping("/equipment_del/{id}")
	@ResponseBody
	public String delementEquipment(@PathVariable("id")String id) {
		equipmentService.deleteEquipment(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@PostMapping("/cabinet_row_lib")
	@ResponseBody
	public List<Cabinet> findCabinetByLibNoAndRowname(String libNo, String rowName) {
		return equipmentService.findCabinet(libNo, rowName);
	}
	
	
	@RequestMapping("/server_cabinet/{cabinetId}")
	@ResponseBody
	public List<Equipment> findServer(@PathVariable("cabinetId")String cabinetId) {
		return equipmentService.findEquipmentByCabinetId(cabinetId);
	}
	
	@RequestMapping("/bindable")
	@ResponseBody
	public List<Equipment> findBindableEquipment(String vague) {
		return equipmentService.findBindableEquipment(vague);
	}
	
	@RequestMapping("/bindable_cabinet")
	@ResponseBody
	public List<Equipment> findBindableEquipmentInCabinet(String vague) {
		return equipmentService.findBindableEquipmentInCabinet(vague);
	}
}
