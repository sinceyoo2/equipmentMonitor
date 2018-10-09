package com.syo.platform.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.service.DataBaseMonitorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataBaseMonitorServiceImplTest {
	
	@Autowired
	private DataBaseMonitorService dataBaseMonitorService;

	@Test
	public void testStartMonitor() throws InterruptedException {
//		dataBaseMonitorService.startMonitor("402881e962f0d4c20162f0d837fd0000");
//		Thread.sleep(300000);
	}

}
