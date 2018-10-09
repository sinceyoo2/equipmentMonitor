package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.PlanShape;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipmentRepositoryTest {

	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Autowired
	private PlanShapeRepository planShapeRepository;
	
	@Test
	public void test() {
		
		/*//成人区
		PlanShape areaShape = planShapeRepository.findOne("a29adafa-07b2-405e-83d8-5f163dea7105");
		
		//检索机
		for(int i=1; i<=6; i++) {
			Equipment e = new Equipment();
			e.setArea("公共区域");
			e.setAreaShape(areaShape);
			e.setEquipmentName("检索机_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setEquipmentNo("JSJ_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setId(UUID.randomUUID().toString());
			e.setLib(areaShape.getLibraryPlan().getLib());
			e.setLibraryPlan(areaShape.getLibraryPlan());
			e.setModel("JSJXXX");
			e.setPlanWidth(4);
			e.setPlanHeight(6);
			e.setPlanThickness(10);
			e.setPlanX(-200+i*8);
			e.setPlanY(-25);
			e.setPlanZ(5);
			e.setType("检索机");
			e.setVendor("卖商");
			
			equipmentRepository.save(e);
		}
		
		//自助借还机
		for(int i=1; i<=4; i++) {
			Equipment e = new Equipment();
			e.setArea("公共区域");
			e.setAreaShape(areaShape);
			e.setEquipmentName("自助借还机_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setEquipmentNo("ZZJHJ_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setId(UUID.randomUUID().toString());
			e.setLib(areaShape.getLibraryPlan().getLib());
			e.setLibraryPlan(areaShape.getLibraryPlan());
			e.setModel("ZZJHJXXX");
			e.setPlanWidth(7);
			e.setPlanHeight(6);
			e.setPlanThickness(10);
			e.setPlanX(-150+i*9);
			e.setPlanY(-10);
			e.setPlanZ(5);
			e.setType("自助借还机");
			e.setVendor("卖商");
			
			equipmentRepository.save(e);

		}
		
		//上网机
		for(int i=1; i<=24; i++) {
			int col = (i-1)%4;
			int row = (i-1)/4;
			
			Equipment e = new Equipment();
			e.setArea("公共区域");
			e.setAreaShape(areaShape);
			e.setEquipmentName("上网机_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setEquipmentNo("SWJ_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setId(UUID.randomUUID().toString());
			e.setLib(areaShape.getLibraryPlan().getLib());
			e.setLibraryPlan(areaShape.getLibraryPlan());
			e.setModel("SWJXXX");
			e.setPlanWidth(4);
			e.setPlanHeight(6);
			e.setPlanThickness(10);
			e.setPlanX(-142+col*4+col%2+col/2*4);
			e.setPlanY(55-row*7-row/2*3);
			e.setPlanZ(5);
			e.setType("上网机");
			e.setVendor("卖商");
			
			equipmentRepository.save(e);				
		}
		
		Equipment e1 = new Equipment();
		e1.setArea("公共区域");
		e1.setAreaShape(areaShape);
		e1.setEquipmentName("门禁_"+areaShape.getLibraryPlan().getPlanNo()+"_1");
		e1.setEquipmentNo("MJ_"+areaShape.getLibraryPlan().getPlanNo()+"_1");
		e1.setId(UUID.randomUUID().toString());
		e1.setLib(areaShape.getLibraryPlan().getLib());
		e1.setLibraryPlan(areaShape.getLibraryPlan());
		e1.setModel("MJXXX");
		e1.setPlanWidth(4);
		e1.setPlanHeight(15);
		e1.setPlanThickness(10);
		e1.setPlanX(-100);
		e1.setPlanY(-5);
		e1.setPlanZ(5);
		e1.setType("门禁");
		e1.setVendor("卖商");
		equipmentRepository.save(e1);
		
		Equipment e2 = new Equipment();
		e2.setArea("公共区域");
		e2.setAreaShape(areaShape);
		e2.setEquipmentName("门禁_"+areaShape.getLibraryPlan().getPlanNo()+"_2");
		e2.setEquipmentNo("MJ_"+areaShape.getLibraryPlan().getPlanNo()+"_2");
		e2.setId(UUID.randomUUID().toString());
		e2.setLib(areaShape.getLibraryPlan().getLib());
		e2.setLibraryPlan(areaShape.getLibraryPlan());
		e2.setModel("MJXXX");
		e2.setPlanWidth(15);
		e2.setPlanHeight(4);
		e2.setPlanThickness(10);
		e2.setPlanX(-65);
		e2.setPlanY(60);
		e2.setPlanZ(5);
		e2.setType("门禁");
		e2.setVendor("卖商");
		equipmentRepository.save(e2);
		
		//期刊区
		PlanShape areaShape1 = planShapeRepository.findOne("a34a0780-f761-4b08-bbd2-83d85ec03f77");
		
		Equipment e3 = new Equipment();
		e3.setArea("公共区域");
		e3.setAreaShape(areaShape1);
		e3.setEquipmentName("门禁_"+areaShape1.getLibraryPlan().getPlanNo()+"_3");
		e3.setEquipmentNo("MJ_"+areaShape1.getLibraryPlan().getPlanNo()+"_3");
		e3.setId(UUID.randomUUID().toString());
		e3.setLib(areaShape1.getLibraryPlan().getLib());
		e3.setLibraryPlan(areaShape1.getLibraryPlan());
		e3.setModel("MJXXX");
		e3.setPlanWidth(4);
		e3.setPlanHeight(15);
		e3.setPlanThickness(10);
		e3.setPlanX(-30);
		e3.setPlanY(-80);
		e3.setPlanZ(5);
		e3.setType("门禁");
		e3.setVendor("卖商");
		equipmentRepository.save(e3);
		
		Equipment e4 = new Equipment();
		e4.setArea("公共区域");
		e4.setAreaShape(areaShape1);
		e4.setEquipmentName("门禁_"+areaShape1.getLibraryPlan().getPlanNo()+"_4");
		e4.setEquipmentNo("MJ_"+areaShape1.getLibraryPlan().getPlanNo()+"_4");
		e4.setId(UUID.randomUUID().toString());
		e4.setLib(areaShape1.getLibraryPlan().getLib());
		e4.setLibraryPlan(areaShape1.getLibraryPlan());
		e4.setModel("MJXXX");
		e4.setPlanWidth(15);
		e4.setPlanHeight(4);
		e4.setPlanThickness(10);
		e4.setPlanX(155);
		e4.setPlanY(-70);
		e4.setPlanZ(5);
		e4.setType("门禁");
		e4.setVendor("卖商");
		equipmentRepository.save(e4);
		
		Equipment ew5 = new Equipment();
		ew5.setArea("公共区域");
		ew5.setAreaShape(areaShape1);
		ew5.setEquipmentName("自助借还机_"+areaShape1.getLibraryPlan().getPlanNo()+"_"+5);
		ew5.setEquipmentNo("ZZJHJ_"+areaShape1.getLibraryPlan().getPlanNo()+"_"+5);
		ew5.setId(UUID.randomUUID().toString());
		ew5.setLib(areaShape1.getLibraryPlan().getLib());
		ew5.setLibraryPlan(areaShape1.getLibraryPlan());
		ew5.setModel("ZZJHJXXX");
		ew5.setPlanWidth(6);
		ew5.setPlanHeight(7);
		ew5.setPlanThickness(10);
		ew5.setPlanX(-25);
		ew5.setPlanY(-105);
		ew5.setPlanZ(5);
		ew5.setType("自助借还机");
		ew5.setVendor("卖商");	
		equipmentRepository.save(ew5);
		
		Equipment ew6 = new Equipment();
		ew6.setArea("公共区域");
		ew6.setAreaShape(areaShape1);
		ew6.setEquipmentName("自助借还机_"+areaShape1.getLibraryPlan().getPlanNo()+"_"+6);
		ew6.setEquipmentNo("ZZJHJ_"+areaShape1.getLibraryPlan().getPlanNo()+"_"+6);
		ew6.setId(UUID.randomUUID().toString());
		ew6.setLib(areaShape1.getLibraryPlan().getLib());
		ew6.setLibraryPlan(areaShape1.getLibraryPlan());
		ew6.setModel("ZZJHJXXX");
		ew6.setPlanWidth(6);
		ew6.setPlanHeight(7);
		ew6.setPlanThickness(10);
		ew6.setPlanX(-25);
		ew6.setPlanY(-120);
		ew6.setPlanZ(5);
		ew6.setType("自助借还机");
		ew6.setVendor("卖商");	
		equipmentRepository.save(ew6);*/


	}
	
	@Test
	public void test2() {
		PlanShape areaShape = planShapeRepository.findOne("2efc0877-8cab-4cbd-9a5c-da54a04ba537");
		for(int i=1; i<=26; i++) {
			Equipment e = new Equipment();
			e.setArea("公共区域");
			e.setEquipmentName("智能书架控制器_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setEquipmentNo("ZNSJKZQ_"+areaShape.getLibraryPlan().getPlanNo()+"_"+i);
			e.setId(UUID.randomUUID().toString());
			e.setLib(areaShape.getLibraryPlan().getLib());
			e.setLibraryPlan(areaShape.getLibraryPlan());
			e.setModel("ZNSJKZQ");
			e.setPlanWidth(5);
			e.setPlanHeight(5);
			e.setPlanThickness(20);
			e.setPlanZ(10);
			e.setType("智能书架控制器");
			e.setVendor("卖商");	
//			equipmentRepository.save(e);
		}
	}

}
