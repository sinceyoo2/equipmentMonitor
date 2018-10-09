package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.entity.Cabinet;
import com.syo.platform.entity.Library;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CabinetRepositoryTest {
	
	@Autowired
	CabinetRepository cabinetRepository;

	@Test
	public void test() {
		Library lib = new Library();
//		lib.setLibNo("0001");
		lib.setLibNo("0003");
		Cabinet c = new Cabinet();
		c.setId(UUID.randomUUID().toString());
		c.setLib(lib);
		c.setMaxU(40);
		c.setName("列头机柜AP1");
		c.setCabinetNo("A0");
		c.setHeader(true);
		c.setRowName("A排");
		c.setPositionIndex(0);
//		cabinetRepository.save(c);
	}
	
	@Test
	public void test2() {
		Library lib = new Library();
//		lib.setLibNo("0001");
		lib.setLibNo("0003");
		Cabinet c = new Cabinet();
		c.setId(UUID.randomUUID().toString());
		c.setLib(lib);
		c.setMaxU(40);
		c.setName("列头机柜AP2");
		c.setCabinetNo("B0");
		c.setHeader(true);
		c.setRowName("B排");
		c.setPositionIndex(0);
//		cabinetRepository.save(c);
	}
	
	@Test
	public void test3() {
		Library lib = new Library();
//		lib.setLibNo("0001");
		lib.setLibNo("0003");
		
		for(int i=1; i<=14; i++) {
			Cabinet c = new Cabinet();
			c.setId(UUID.randomUUID().toString());
			c.setLib(lib);
			c.setMaxU(40);
			c.setName("机柜A"+i);
			c.setCabinetNo("A"+i);
			c.setHeader(false);
			c.setRowName("A排");
			c.setPositionIndex(i);
//			cabinetRepository.save(c);
			
			c = new Cabinet();
			c.setId(UUID.randomUUID().toString());
			c.setLib(lib);
			c.setMaxU(40);
			c.setName("机柜B"+i);
			c.setCabinetNo("B"+i);
			c.setHeader(false);
			c.setRowName("B排");
			c.setPositionIndex(i);
//			cabinetRepository.save(c);
		}
		
	}

}
