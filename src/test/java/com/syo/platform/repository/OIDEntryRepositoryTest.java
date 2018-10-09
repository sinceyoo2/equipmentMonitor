package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.entity.OIDEntry;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OIDEntryRepositoryTest {
	
	@Autowired
	private OIDEntryRepository oidEntryRepository;

	@Test
	public void test() {
		String[][] str = {
				{"1.3.6.1.4.1.2011.5.25.31.1.1.10.1.7","风扇","风扇的运行状态,1 表示正常,2 表示异常"},
				{"1.3.6.1.4.1.2011.6.3.5.1.1.2","内存总量","内存总量，单位是字节。包括每块板上空闲的内存量和已占用的内存量"},
				{"1.3.6.1.4.1.2011.6.3.5.1.1.3","空闲内存","指示设备上空闲内存的总量，单位是字节"},
				{"1.3.6.1.4.1.2011.6.3.4.1.2","CPU5秒内使用率","表示一块单板或者一个实体在5秒钟内的CPU的平均使用率"},
				{"1.3.6.1.4.1.2011.6.3.4.1.3","CPU1分钟内使用率","表示一块单板或者一个实体在1分钟内的CPU的平均使用率"},
				{"1.3.6.1.4.1.2011.6.3.4.1.4","CPU5分钟内使用率","表示一块单板或者一个实体在5分钟内的CPU的平均使用率"},
				{"1.3.6.1.4.1.2011.6.139.18.1.2.1.34","入流量","入流量（字节）"},
				{"1.3.6.1.4.1.2011.6.139.18.1.2.1.37","出流量","出流量（字节）"}
		};
		String vendor = "华为";
		String models = "S12708,S5700-52C-SI-AC,CE5850-48T4S2Q-EI,S5700-28C-PWR-SI,AC6605";
		for(int i=0; i<str.length; i++) {
			OIDEntry entry = new OIDEntry();
			entry.setDescription(str[i][2]);
			entry.setEnable(true);
			entry.setEquipmentType("交换机");
			entry.setModels(models);
			entry.setName(str[i][1]);
			entry.setOid(str[i][0]);
			entry.setSort(i);
			entry.setVendor(vendor);
			
//			oidEntryRepository.save(entry);
			
		}
	}
	
	@Test
	public void testFind() {
//		List<OIDEntry> entries = oidEntryRepository.findByEquipmentTypeAndModelsContainingOrderBySort("交换机", "S5700-28C-PWR-SI");
//		for(OIDEntry entry :entries) {
//			System.out.println(entry.getName());
//		}
	}

}
