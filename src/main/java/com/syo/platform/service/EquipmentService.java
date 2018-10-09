package com.syo.platform.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.syo.platform.entity.Cabinet;
import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.Library;

public interface EquipmentService {

	public List<Library> findEngineRoomLibs();
	
	public List<Cabinet> findCabinet(String libNo, String rowName);
	
	public Page<Equipment> findEquipment(int pageNo, int pageSize, String type, String vague);
	
	public Page<Equipment> findEquipment(int pageNo, int pageSize, Date start, Date end, String type, String vague);
	
	public void saveEquipment(Equipment equipment);
	
	public void deleteEquipment(String id);
	
	public Equipment findEquipmentById(String id);
	
	public void saveServerPosition(String serverId, String libNo, String cabinetId, int startU, int endU);
	
	public void saveEquipmentPosition(String equipmentId, String libNo, String planId, double x, double y, double width, double height);
	
	public List<Equipment> findEquipmentByCabinetId(String cabinetId);
	
	public List<Equipment> findBindableEquipment(String vague);
	
	public List<Equipment> findBindableEquipmentInCabinet(String vague);
	
	public List<Equipment> findObserverEquipment();
	
	public Equipment findObserverEquipmentByIP(String ip);
	
	public void handleError(String id, Date errorTime);
	
	public void recoveryError(String id, Date recoveryTime);
}
