package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.entity.Library;
import com.syo.platform.entity.LibraryPlan;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryPlanRepositoryTest {
	
	@Autowired
	private LibraryPlanRepository libraryPlanRepository;
	
	@Autowired
	private LibraryRepository libraryRepository;

	@Test
	public void test() {
		//fail("Not yet implemented");
		String[] zms = {"祖庙分馆-2f","祖庙分馆-3f"};
		String[] zms_code = {"2F","3F"};
		String[] zgs = {"负一楼","一楼","一楼夹层","二楼","二楼夹层","三楼","四楼","五楼","六楼","七楼"};
		String[] zgs_code = {"1B","1F","1J","2F","2J","3F","4F","5F","6F","7F"};
		
		Library lib1 = libraryRepository.findOne("0001");
		
		Library lib2 = libraryRepository.findOne("0003");
		
		for(int i=0; i<zms.length; i++) {
			LibraryPlan plan = new LibraryPlan();
			plan.setId(UUID.randomUUID().toString());
			plan.setLib(lib1);
			plan.setName(zms[i]);
			plan.setSort(i);
			plan.setPlanNo(lib1.getLibNo()+"_"+zms_code[i]);
			plan.setFloorNo(zms_code[i]);
			
//			libraryPlanRepository.save(plan);
		}
		
		for(int i=0; i<zgs.length; i++) {			
			LibraryPlan plan = new LibraryPlan();
			plan.setId(UUID.randomUUID().toString());
			plan.setLib(lib2);
			plan.setName(zgs[i]);
			plan.setSort(i);
			plan.setPlanNo(lib2.getLibNo()+"_"+zgs_code[i]);
			plan.setFloorNo(zgs_code[i]);
			
//			libraryPlanRepository.save(plan);
		}
//		LibraryPlan 
	}

}
