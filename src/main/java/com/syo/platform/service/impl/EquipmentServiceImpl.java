package com.syo.platform.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.syo.platform.entity.Cabinet;
import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.Library;
import com.syo.platform.entity.LibraryPlan;
import com.syo.platform.repository.CabinetRepository;
import com.syo.platform.repository.DataBaseMonitorConfigRepository;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.HostMonitorConfigRepository;
import com.syo.platform.repository.LibraryPlanRepository;
import com.syo.platform.repository.LibraryRepository;
import com.syo.platform.repository.MiddlewareMonitorConfigRepository;
import com.syo.platform.repository.PortMonitorConfigRepository;
import com.syo.platform.repository.WebMonitorConfigRepository;
import com.syo.platform.repository.jpautils.Criteria;
import com.syo.platform.repository.jpautils.Restrictions;
import com.syo.platform.repository.jpautils.UpdateUtil;
import com.syo.platform.service.EquipmentService;
import com.syo.platform.service.ErrorMessageService;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	@Autowired
	private LibraryRepository libraryRepository;
	
	@Autowired
	private LibraryPlanRepository libraryPlanRepository;
	
	@Autowired
	private CabinetRepository cabinetRepository;
	
	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Autowired
	private DataBaseMonitorConfigRepository dataBaseMonitorConfigRepository;
	
	@Autowired
	private MiddlewareMonitorConfigRepository middlewareMonitorConfigRepository;
	
	@Autowired
	private PortMonitorConfigRepository portMonitorConfigRepository;
	
	@Autowired
	private WebMonitorConfigRepository webMonitorConfigRepository;
	
	@Autowired
	private HostMonitorConfigRepository hostMonitorConfigRepository;
	
	@Autowired
	private ErrorMessageRepository errorMessageRepository;
	
	@Autowired
	private ErrorMessageService errorMessageService;
	
	@Override
	public List<Library> findEngineRoomLibs() {
		return libraryRepository.findByHasEngineRoom(true);
	}

	@Override
	public List<Cabinet> findCabinet(String libNo, String rowName) {
		List<Cabinet> cabinets = cabinetRepository.findByLibAndRowName(libNo, rowName);
		for(Cabinet cabinet :cabinets) {
			setHasProblem(cabinet);
		}
		return cabinets;
	}
	
	private void setHasProblem(Cabinet cabinet) {
		List<Equipment> equipments = equipmentRepository.findByCabinetId(cabinet.getId());
		for(Equipment equipment : equipments) {
			if(dataBaseMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
				cabinet.setHasProblem(true);
				return;
			}
			if(middlewareMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
				cabinet.setHasProblem(true);
				return;
			}
			if(portMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
				cabinet.setHasProblem(true);
				return;
			}
			if(webMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
				cabinet.setHasProblem(true);
				return;
			}
			if(hostMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
				cabinet.setHasProblem(true);
				return;
			}
		}
	}

	@Override
	public Page<Equipment> findEquipment(int pageNo, int pageSize, String type, String vague) {
		Pageable page = new PageRequest(pageNo-1, pageSize, Direction.DESC, "createTime");
		if(type==null || type.trim().equals("")) {
			if(vague==null || vague.trim().equals("")) {
				return equipmentRepository.findAll(page);
			} else {
				return equipmentRepository.findByVague(vague, page);
			}
		} else {
			if(vague==null || vague.trim().equals("")) {
				return equipmentRepository.findByType(type, page);
			} else {
				return equipmentRepository.findByTypeAndVague(type, vague, page);
			}
		}
	}
	
	@Override
	public Page<Equipment> findEquipment(int pageNo, int pageSize, Date start, Date end, String type, String vague) {
		Pageable page = new PageRequest(pageNo-1, pageSize, Direction.DESC, "createTime");
		Criteria<Equipment> c = new Criteria<>();
		if(start!=null && end!=null) {
			c.add(Restrictions.gte("createTime", start, true));
			c.add(Restrictions.lt("createTime", end, true));
		}
		if(type!=null && !type.trim().equals("")) {
			c.add(Restrictions.eq("type", type, true));
		}
		if(vague!=null && !vague.trim().equals("")) {
			c.add(Restrictions.or(Restrictions.like("equipmentName", vague, true),Restrictions.like("equipmentNo", vague, true),Restrictions.like("deptName", vague, true)));
		}
		return equipmentRepository.findAll(c, page);
	}

	@Override
	public void saveEquipment(Equipment equipment) {
		if(equipment.getId()==null || equipment.getId().trim().equals("")) {
			equipment.setId(UUID.randomUUID().toString());
			equipmentRepository.save(equipment);
		} else {
			try {
				new UpdateUtil<Equipment>().update(equipment, equipmentRepository, "createTime", "lib", "startU", "endU", "cabinet", "floor", "positionX", "positionY", "position", "area");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteEquipment(String id) {
		equipmentRepository.delete(id);
	}

	@Override
	public Equipment findEquipmentById(String id) {
		return equipmentRepository.findOne(id);
	}

	@Override
	public void saveServerPosition(String serverId, String libNo, String cabinetId, int startU, int endU) {
		Equipment server = equipmentRepository.findOne(serverId);
		Library lib = libraryRepository.findOne(libNo);
		Cabinet cabinet = cabinetRepository.findOne(cabinetId);
		server.setLib(lib);
		server.setCabinet(cabinet);
		server.setStartU(startU);
		server.setEndU(endU);
		equipmentRepository.save(server);
	}
	
	@Override
	public void saveEquipmentPosition(String equipmentId, String libNo, String planId, double x, double y, double width,
			double height) {
		Equipment equipment = equipmentRepository.findOne(equipmentId);
		Library lib = libraryRepository.findOne(libNo);
		LibraryPlan plan = libraryPlanRepository.findOne(planId);
		
		equipment.setLib(lib);
		equipment.setLibraryPlan(plan);
		equipment.setPlanX(x);
		equipment.setPlanY(y);
		equipment.setPlanWidth(width);
		equipment.setPlanHeight(height);
		equipment.setPlanThickness(10);
		equipment.setPlanZ(5);
		equipmentRepository.save(equipment);
	}

	@Override
	public List<Equipment> findEquipmentByCabinetId(String cabinetId) {
		List<Equipment> equipments = equipmentRepository.findByCabinetId(cabinetId);
		for(Equipment equipment : equipments) {
			setStyle(equipment);
		}
		return equipments;
	}
	
	private void setStyle(Equipment equipment) {
		if(dataBaseMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
			equipment.setStyle("btn-warning");
			return;
		}
		if(middlewareMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
			equipment.setStyle("btn-warning");
			return;
		}
		if(portMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
			equipment.setStyle("btn-warning");
			return;
		}
		if(webMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
			equipment.setStyle("btn-warning");
			return;
		}
		if(hostMonitorConfigRepository.findErrorCountByBindEquipment(equipment.getId())>0) {
			equipment.setStyle("btn-warning");
			return;
		}
		equipment.setStyle("btn-info");
	}

	@Override
	public List<Equipment> findBindableEquipment(String vague) {
		Criteria<Equipment> c = new Criteria<Equipment>();
		if(vague!=null && !vague.trim().equals("")) {
			c.add(Restrictions.or(
					Restrictions.like("equipmentNo", vague, true), 
					Restrictions.like("equipmentName", vague, true), 
					Restrictions.like("deptName", vague, true)
					));
		}
		return equipmentRepository.findAll(c);
	}

	@Override
	public List<Equipment> findBindableEquipmentInCabinet(String vague) {
		Criteria<Equipment> c = new Criteria<Equipment>();
		c.add(Restrictions.eq("type", "服务器", false));
		if(vague!=null && !vague.trim().equals("")) {
			c.add(Restrictions.or(
					Restrictions.like("equipmentNo", vague, true), 
					Restrictions.like("equipmentName", vague, true), 
					Restrictions.like("deptName", vague, true)
					));
		}
		return equipmentRepository.findAll(c);
	}

	@Override
	public List<Equipment> findObserverEquipment() {
		// TODO Auto-generated method stub
		return equipmentRepository.findByIsObserver(true);
	}

	@Override
	public Equipment findObserverEquipmentByIP(String ip) {
		return equipmentRepository.findByIsObserverAndIpAddress(true, ip);
	}

	@Override
	public void handleError(String id, Date errorTime) {
		Equipment equipment = equipmentRepository.findOne(id);
		
		ErrorMessage error = new ErrorMessage();
		error.setId(UUID.randomUUID().toString());
		error.setCause("设备失去联系");//
		error.setSummary("设备失去联系");
		error.setDeclareType("监控发现");
		error.setDepartment("监控平台");
		error.setDescription("设备失去联系");//
		error.setErrorLevel(2);
		error.setErrorNo("follower_m001");//
		error.setErrorTime(errorTime);
		error.setErrorType("警告");
		error.setMsgSource("监控");
		error.setStatus("发现故障");
		error.setTargetName(equipment.getEquipmentName());
		error.setTargetType("智能馆设备监控");
		error.setTroubleshooter("智能馆设备监控任务");
		
		error.setEquipment(equipment);
		
//		errorMessageRepository.save(error);
		errorMessageService.saveAndStartFlow(error);
		
		equipment.setErrorStatus("故障");
		equipment.setFollowerErrorId(error.getId());
		equipmentRepository.save(equipment);
	}

	@Override
	public void recoveryError(String id, Date recoveryTime) {
		
		Equipment equipment = equipmentRepository.findOne(id);
		ErrorMessage error = errorMessageRepository.findOne(equipment.getFollowerErrorId());
		error.setRecoveryTime(recoveryTime);
		error.setDuration((error.getRecoveryTime().getTime()-error.getErrorTime().getTime())/1000);
		error.setStatus("已恢复");
		errorMessageRepository.save(error);
		
		equipment.setErrorStatus("正常");
		equipment.setFollowerErrorId(null);
		equipmentRepository.save(equipment);
	}

}
