package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.entity.Library;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryRepositoryTest {
	
	@Autowired
	private LibraryRepository libraryRepository;

	@Test
	public void test() throws IOException {
		//fail("Not yet implemented");
//		InputStreamReader isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("libs.json"));
//		BufferedReader br = new BufferedReader(isr);
//		String line = br.readLine();
//		StringBuffer json = new StringBuffer();
//		while(line!=null) {
////			System.out.println(line);
//			if(!line.trim().equals("")) {
//				json.append(line);
//			}
//			line = br.readLine();
//		}
//		br.close();
//		isr.close();
//		
////		System.out.println(json);
//		List<Map<String, Object>> libs = new ObjectMapper().readValue(json.toString(), List.class);
////		System.out.println(libs.size());
//		int count = 1;
//		DecimalFormat df = new DecimalFormat("0000");
//		for(Map<String, Object> lib :libs) {
//			Library library = new Library();
//			library.setLibNo(df.format(count));
//			library.setLibName(lib.get("title").toString());
//			library.setWebsite("http://www.fslib.com.cn/");
//			
//			String[] contents = lib.get("content").toString().split("<br/>");
//			String libCode = contents[1].substring(contents[1].indexOf("：")+1, contents[1].indexOf("&nbsp;"));
//			library.setLibCode(libCode);
//			String hotline = contents[2].substring(contents[2].indexOf("：")+1, contents[2].indexOf("&nbsp;"));
//			library.setHotline(hotline);
//			String address = contents[3].substring(contents[3].indexOf("：")+1, contents[3].indexOf("&nbsp;"));
//			library.setAddress(address);
//			String businessHour = contents[4].substring(contents[4].indexOf("：")+1, contents[4].indexOf("&nbsp;"));
//			library.setBusinessHour(businessHour);
//			String trafficTips = contents[5].substring(contents[5].indexOf("：")+1);
//			library.setTrafficTips(trafficTips);
//			
//			if(lib.get("point").toString().indexOf("|")>0) {
//				String[] point = lib.get("point").toString().split("\\|");
//				library.setLongitude(Double.parseDouble(point[0]));
//				library.setLatitude(Double.parseDouble(point[1]));
//			}
//
////			libraryRepository.save(library);
//			count++;
////			System.out.println(library);
////			System.out.println("------------------------------------------------------");
//		}
		
	}

}
