package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.syo.platform.entity.Functional;
import com.syo.platform.web.ArticleController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FunctionalRepositoryTest {
	
	@Autowired
	private FunctionalRepository functionalRepository;

	@Test
	public void test() throws URISyntaxException, ClassNotFoundException {
		//fail("Not yet implemented");
//		String packageName = "com.syo.platform.web";
//		
//		Class clazz = ArticleController.class;
//		ClassLoader loader = clazz.getClassLoader();
//		// 1. 通过classloader载入包路径，得到url
//		URL url = loader.getResource("com/syo/platform/web");
//		URI uri = url.toURI();
//		// 2. 通过File获得uri下的所有文件
//		File file = new File(uri);
//		File[] files = file.listFiles();
//		for (File f : files) {
//			String fName = f.getName();
//			if (!fName.endsWith(".class")) {
//				continue;
//			}
//			
//			if(fName.startsWith("IndexController")) {
//				Functional func = createFunctional("com.syo.platform.web.IndexController.index()", "/", "模块", null);
//				func.setLogName("访问首页");
//				func.setName("首页");
//				func.setDescription("系统控制台首页");
//				
////				functionalRepository.save(func);
//				continue;
//			}
//			fName = fName.substring(0, fName.length() - 6);
//			String perfix = "com.syo.platform.web.";
//			String allName = perfix + fName;
//			// 3. 通过反射加载类
//			clazz = Class.forName(allName);
//			System.out.println(clazz);
//			RequestMapping rmAnnotion = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
//			System.out.println(rmAnnotion);
//			Functional func = createFunctional(clazz.getName(), rmAnnotion.value()[0], "模块", null);
//			
////			functionalRepository.save(func);
//			
//			Method[] methods = clazz.getDeclaredMethods();
//			for(Method method : methods) {
//				System.out.println(method.getName()+"()");
//				RequestMapping mrmAnnotion = method.getAnnotation(RequestMapping.class);
//				String[] values = null;
//				if(mrmAnnotion!=null) {
//					values = mrmAnnotion.value();
//				} else {
//					PostMapping mpmAnnotion = method.getAnnotation(PostMapping.class);
//					values = mpmAnnotion.value();
//				}
//				String valueStr = StringUtils.join(values, ",");
//				if(values.length>1) {
//					valueStr = "["+valueStr+"]";
//				}
//				
//				Functional mfunc = createFunctional(func.getCodeTips()+"."+method.getName()+"()", func.getUrl()+valueStr, "功能", func);
////				functionalRepository.save(mfunc);
//				
//			}
//			
//		}

	}
	
	private Functional createFunctional(String codeTips, String url, String type, Functional parent) {
		Functional functional = new Functional();
		functional.setCodeTips(codeTips);
		functional.setDescription("未描述");
		functional.setLogName("未描述操作");
		functional.setName("未描述功能");
		functional.setParent(parent);
		functional.setSort(0);
		functional.setStatus("可用");
		functional.setType(type);
		functional.setUrl(url);
		functional.setLogEnable(true);
		return functional;
	}

}
