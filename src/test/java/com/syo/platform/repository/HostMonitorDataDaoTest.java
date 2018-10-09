package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HostMonitorDataDaoTest {
	
	@Autowired
	HostMonitorDataDao dao;

	@Test
	public void testFindHisData() throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//		System.out.println(dao.findHisData(sdf.parse("2018/04/18 00:00"), sdf.parse("2018/06/20 00:00"), "CPUInfo", "192.168.1.103").size());
	}
	
	@Test
	public void testFindErrData() throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//		System.out.println(dao.findErrData(sdf.parse("2018/04/18 00:00"), sdf.parse("2018/06/20 00:00"), null, "192.168.1.103").size());
	}

}
