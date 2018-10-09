package com.syo.platform.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.entity.LibraryPlan;
import com.syo.platform.entity.PlanShape;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanShapeRepositoryTest {
	
	@Autowired
	private PlanShapeRepository planShapeRepository;

	@Test
	public void test() {
//		fail("Not yet implemented");
		
		LibraryPlan plan = new LibraryPlan();
		plan.setId("d64937a6-b9b3-4c0f-bf53-8f5b2e8b8ae6");
		
		PlanShape shape = new PlanShape();
		
		shape.setBevelEnabled(false);
		shape.setBevelSize(0);
		shape.setColor(0xf4f4f4);
		shape.setLibraryPlan(plan);
		shape.setName("地板");
		shape.setOpacity(0);
		shape.setShapePaths("[[-215,190,0],[330,190,0],[330,10,0],[190,-190,0],[55,-190,0],[55,-60,0],[120,-60,0],[160,-30,0],[160,0,0],[120,40,0],[-30,40,0],[-30,-10,0],[-30,-10,0],[25,-60,0],[55,-60,0],[55,-135,0],[5,-135,0],[5,-190,0],[-300,-190,0],[-300,-150,0],[-280,-150,0],[-280,-60,0],[-230,-60,0],[-230,-30,0],[-235,-30,0],[-235,10,0],[-215,10,0],[-215,70,0],[-235,70,0],[-235,110,0],[-215,110,0]]");
		shape.setShapeType("floor");
		shape.setShowText(false);
		shape.setText(null);
		shape.setThickness(2);
		shape.setTransparent(false);
		shape.setSort(0);
		
//		planShapeRepository.save(shape);
	}
	
	
	@Test
	public void test2() {
		int startX = 170;
		int startY = -87;
		int width = 2;
		int height = 15;
		int count = 11;
		int startSort = 318;
		
		LibraryPlan plan = new LibraryPlan();
		plan.setId("0cfb8d48-4c88-4d05-be56-b659ac3ac7c0");
		
		for(int i=0; i<count; i++) {
			int x = startX+width*i;
//			if(i>6) {
//				x += 5;
//			}
			List<Integer> p1 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<Integer> p2 = Arrays.asList(new Integer[] {x+width, startY, 0});
			List<Integer> p3 = Arrays.asList(new Integer[] {x+width, startY-height, 0});
			List<Integer> p4 = Arrays.asList(new Integer[] {x, startY-height, 0});
			List<Integer> p5 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<List<Integer>> ps = new ArrayList<>();
			ps.add(p1);ps.add(p2);ps.add(p3);ps.add(p4);ps.add(p5);
//			System.out.println(ps);
			
			PlanShape shape = new PlanShape();
			shape.setBevelEnabled(true);
			shape.setBevelSize(0);
			shape.setColor(i%2==0?15132390:11522281);
			shape.setId(UUID.randomUUID().toString());
			shape.setLibraryPlan(plan);
			shape.setName("item"+(startSort+i));
			shape.setOpacity(0.65);
			shape.setOverdraw(0.5);
			shape.setShapePaths(ps.toString());
			shape.setShapeType("area");
			shape.setShowText(false);
			shape.setSort(startSort+i);
			shape.setText("F区");
			shape.setTextColor(10066329);
			shape.setTextFamily("assets/js/three.js/fonts/Microsoft YaHei_Regular.json");
			shape.setTextOverdraw(0.5);
			shape.setTextSize(10);
			shape.setTextThickness(0);
			shape.setThickness(15);
			shape.setTransparent(true);
			
//			planShapeRepository.save(shape);	
		}
		
	}
	
	@Test
	public void test3() {
		int startX = 143;
		int startY = -62;
		int width = 20;
		int height = 2;
		int count = 16;
		int startSort = 289;
		
		LibraryPlan plan = new LibraryPlan();
		plan.setId("0cfb8d48-4c88-4d05-be56-b659ac3ac7c0");
		
		for(int i=0; i<count; i++) {
			int y = startY-height*i;
//			if(i>9) {
//				x += 5;
//			}
			List<Integer> p1 = Arrays.asList(new Integer[] {startX, y, 0}); 
			List<Integer> p2 = Arrays.asList(new Integer[] {startX+width, y, 0});
			List<Integer> p3 = Arrays.asList(new Integer[] {startX+width, y-height, 0});
			List<Integer> p4 = Arrays.asList(new Integer[] {startX, y-height, 0});
			List<Integer> p5 = Arrays.asList(new Integer[] {startX, y, 0}); 
			List<List<Integer>> ps = new ArrayList<>();
			ps.add(p1);ps.add(p2);ps.add(p3);ps.add(p4);ps.add(p5);
//			System.out.println(ps);
			
			PlanShape shape = new PlanShape();
			shape.setBevelEnabled(true);
			shape.setBevelSize(0);
			shape.setColor(i%2==0?15132390:11522281);
			shape.setId(UUID.randomUUID().toString());
			shape.setLibraryPlan(plan);
			shape.setName("item"+(startSort+i));
			shape.setOpacity(0.65);
			shape.setOverdraw(0.5);
			shape.setShapePaths(ps.toString());
			shape.setShapeType("area");
			shape.setShowText(false);
			shape.setSort(startSort+i);
			shape.setText("D区");
			shape.setTextColor(10066329);
			shape.setTextFamily("assets/js/three.js/fonts/Microsoft YaHei_Regular.json");
			shape.setTextOverdraw(0.5);
			shape.setTextSize(10);
			shape.setTextThickness(0);
			shape.setThickness(15);
			shape.setTransparent(true);
			
//			planShapeRepository.save(shape);	
		}
		
	}
	
	@Test
	public void test4() {
		int startX = -160;
		int startY = -10;
		int width = 3;
		int height = 40;
		int space = 4;
		int count = 18;
		int startSort = 100;
		
//		int startX = -160;
//		int startY = -60;
//		int width = 3;
//		int height = 35;
//		int space = 4;
//		int count = 18;
//		int startSort = 118;
		
		LibraryPlan plan = new LibraryPlan();
		plan.setId("b559c7af-4397-4769-82fe-958044936ad7");
		
		for(int i=0; i<count; i++) {
			int x = startX-width*i-space*i;
			
			if(i==0 || i==6 || i==7 || i==13) {
				height = 35;
			} else {
				height = 40;
			}

			List<Integer> p1 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<Integer> p2 = Arrays.asList(new Integer[] {x-width, startY, 0});
			List<Integer> p3 = Arrays.asList(new Integer[] {x-width, startY-height, 0});
			List<Integer> p4 = Arrays.asList(new Integer[] {x, startY-height, 0});
			List<Integer> p5 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<List<Integer>> ps = new ArrayList<>();
			ps.add(p1);ps.add(p2);ps.add(p3);ps.add(p4);ps.add(p5);
//			System.out.println(ps);
			
			PlanShape shape = new PlanShape();
			shape.setBevelEnabled(true);
			shape.setBevelSize(0);
			shape.setColor(i%2==0?15132390:11522281);
			shape.setId(UUID.randomUUID().toString());
			shape.setLibraryPlan(plan);
			shape.setName("书架-西-A-"+i);
//			shape.setName("书架-西-B-"+i);
			shape.setOpacity(0.65);
			shape.setOverdraw(0.5);
			shape.setShapePaths(ps.toString());
			shape.setShapeType("area");
			shape.setShowText(false);
			shape.setSort(startSort+i);
			shape.setText("书架");
			shape.setTextColor(10066329);
			shape.setTextFamily("assets/js/three.js/fonts/Microsoft YaHei_Regular.json");
			shape.setTextOverdraw(0.5);
			shape.setTextSize(10);
			shape.setTextThickness(0);
			shape.setThickness(15);
			shape.setTransparent(true);
			
//			planShapeRepository.save(shape);	
		}
		
	}
	
	@Test
	public void test5() {
//		int startX = 160;
//		int startY = -10;
//		int width = 3;
//		int height = 40;
//		int space = 4;
//		int count = 18;
//		int startSort = 136;
		
		int startX = 160;
		int startY = -60;
		int width = 3;
		int height = 35;
		int space = 4;
		int count = 18;
		int startSort = 154;
		
		LibraryPlan plan = new LibraryPlan();
		plan.setId("b559c7af-4397-4769-82fe-958044936ad7");
		
		for(int i=0; i<count; i++) {
			int x = startX+width*i+space*i;
			
//			if(i==0 || i==6 || i==7 || i==13) {
//				height = 35;
//			} else if(i==3) {
//				height = 50;
//			} else {
//				height = 40;
//			}
			if(i==3) {
				continue;
			}

			List<Integer> p1 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<Integer> p2 = Arrays.asList(new Integer[] {x+width, startY, 0});
			List<Integer> p3 = Arrays.asList(new Integer[] {x+width, startY-height, 0});
			List<Integer> p4 = Arrays.asList(new Integer[] {x, startY-height, 0});
			List<Integer> p5 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<List<Integer>> ps = new ArrayList<>();
			ps.add(p1);ps.add(p2);ps.add(p3);ps.add(p4);ps.add(p5);
//			System.out.println(ps);
			
			PlanShape shape = new PlanShape();
			shape.setBevelEnabled(true);
			shape.setBevelSize(0);
			shape.setColor(i%2==0?15132390:11522281);
			shape.setId(UUID.randomUUID().toString());
			shape.setLibraryPlan(plan);
//			shape.setName("书架-东-A-"+i);
			shape.setName("书架-东-B-"+i);
			shape.setOpacity(0.65);
			shape.setOverdraw(0.5);
			shape.setShapePaths(ps.toString());
			shape.setShapeType("area");
			shape.setShowText(false);
			shape.setSort(startSort+i);
			shape.setText("书架");
			shape.setTextColor(10066329);
			shape.setTextFamily("assets/js/three.js/fonts/Microsoft YaHei_Regular.json");
			shape.setTextOverdraw(0.5);
			shape.setTextSize(10);
			shape.setTextThickness(0);
			shape.setThickness(15);
			shape.setTransparent(true);
			
//			planShapeRepository.save(shape);	
		}
		
	}
	
	@Test
	public void test6() {
//		int startX = -157;
//		int startY = -10;
//		int width = 3;
//		int height = 25;
//		int space = 4;
//		int count = 14;
//		int startSort = 100;
		
		int startX = 157;
		int startY = -10;
		int width = 3;
		int height = 25;
		int space = 4;
		int count = 14;
		int startSort = 125;
		

		
		LibraryPlan plan = new LibraryPlan();
		plan.setId("82c03b3a-bf83-4e69-b89b-4a1d1e830fc3");
		
		for(int i=0; i<count; i++) {
//			int x = startX-width*i-space*i;
			int x = startX+width*i+space*i;

//			List<Integer> p1 = Arrays.asList(new Integer[] {x, startY, 0}); 
//			List<Integer> p2 = Arrays.asList(new Integer[] {x-width, startY, 0});
//			List<Integer> p3 = Arrays.asList(new Integer[] {x-width, startY-height, 0});
//			List<Integer> p4 = Arrays.asList(new Integer[] {x, startY-height, 0});
//			List<Integer> p5 = Arrays.asList(new Integer[] {x, startY, 0}); 
			
			List<Integer> p1 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<Integer> p2 = Arrays.asList(new Integer[] {x+width, startY, 0});
			List<Integer> p3 = Arrays.asList(new Integer[] {x+width, startY-height, 0});
			List<Integer> p4 = Arrays.asList(new Integer[] {x, startY-height, 0});
			List<Integer> p5 = Arrays.asList(new Integer[] {x, startY, 0}); 
			List<List<Integer>> ps = new ArrayList<>();
			ps.add(p1);ps.add(p2);ps.add(p3);ps.add(p4);ps.add(p5);
			
			PlanShape shape = new PlanShape();
			shape.setBevelEnabled(true);
			shape.setBevelSize(0);
			shape.setColor(i%2==0?15132390:11522281);
			shape.setId(UUID.randomUUID().toString());
			shape.setLibraryPlan(plan);
//			shape.setName("书架-西-A-"+i);
			shape.setName("书架-东-A-"+i);
			shape.setOpacity(0.65);
			shape.setOverdraw(0.5);
			shape.setShapePaths(ps.toString());
			shape.setShapeType("area");
			shape.setShowText(false);
			shape.setSort(startSort+i);
			shape.setText("书架");
			shape.setTextColor(10066329);
			shape.setTextFamily("assets/js/three.js/fonts/Microsoft YaHei_Regular.json");
			shape.setTextOverdraw(0.5);
			shape.setTextSize(10);
			shape.setTextThickness(0);
			shape.setThickness(15);
			shape.setTransparent(true);
			
//			planShapeRepository.save(shape);	
		}
		
	}
	
	@Test
	public void test7() {
//		int startX = -283;
//		int startY = -100;
//		int width = 20;
//		int height = 3;
//		int space = 4;
//		int count = 5;
//		int startSort = 114;
		
//		int startX = -142;
//		int startY = -93;
//		int width = 20;
//		int height = 3;
//		int space = 4;
//		int count = 6;
//		int startSort = 119;
		
//		int startX = 263;
//		int startY = -100;
//		int width = 20;
//		int height = 3;
//		int space = 4;
//		int count = 5;
//		int startSort = 139;
		
		int startX = 122;
		int startY = -93;
		int width = 20;
		int height = 3;
		int space = 4;
		int count = 6;
		int startSort = 144;
		
		LibraryPlan plan = new LibraryPlan();
		plan.setId("82c03b3a-bf83-4e69-b89b-4a1d1e830fc3");
		
		for(int i=0; i<count; i++) {
			//int x = startX-width*i-space*i;
			int y = startY - height*i - space*i;

			List<Integer> p1 = Arrays.asList(new Integer[] {startX, y, 0}); 
			List<Integer> p2 = Arrays.asList(new Integer[] {startX+width, y, 0});
			List<Integer> p3 = Arrays.asList(new Integer[] {startX+width, y-height, 0});
			List<Integer> p4 = Arrays.asList(new Integer[] {startX, y-height, 0});
			List<Integer> p5 = Arrays.asList(new Integer[] {startX, y, 0}); 
			List<List<Integer>> ps = new ArrayList<>();
			ps.add(p1);ps.add(p2);ps.add(p3);ps.add(p4);ps.add(p5);
			
			PlanShape shape = new PlanShape();
			shape.setBevelEnabled(true);
			shape.setBevelSize(0);
			shape.setColor(i%2==0?15132390:11522281);
			shape.setId(UUID.randomUUID().toString());
			shape.setLibraryPlan(plan);
//			shape.setName("书架-西-B-"+i);
//			shape.setName("书架-西-C-"+i);
//			shape.setName("书架-东-B-"+i);
			shape.setName("书架-东-C-"+i);
			shape.setOpacity(0.65);
			shape.setOverdraw(0.5);
			shape.setShapePaths(ps.toString());
			shape.setShapeType("area");
			shape.setShowText(false);
			shape.setSort(startSort+i);
			shape.setText("书架");
			shape.setTextColor(10066329);
			shape.setTextFamily("assets/js/three.js/fonts/Microsoft YaHei_Regular.json");
			shape.setTextOverdraw(0.5);
			shape.setTextSize(10);
			shape.setTextThickness(0);
			shape.setThickness(15);
			shape.setTransparent(true);
			
//			planShapeRepository.save(shape);	
		}
		
	}
	
	
	
	@Test
	public void testFind() {
		List<PlanShape> list = planShapeRepository.findByPlanId("d64937a6-b9b3-4c0f-bf53-8f5b2e8b8ae6");
		System.out.println(list);
	}
	
	private static List<List<Double>> drawCricle(int r, double angle, int centerX, int centerY) {
		List<List<Double>> rest = new ArrayList<>();
		double currentAngle = 0;
		while(currentAngle<2*Math.PI) {
			List<Double> list = new ArrayList<>();
			list.add(Math.round(Math.cos(currentAngle) * r * 100)/100d + centerX);
			list.add(Math.round(Math.sin(currentAngle) * r * 100)/100d + centerY);
			list.add(0d);
			rest.add(list);
			currentAngle += angle;
		}
		System.out.println(rest.size());
		return rest;
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
//		System.out.println(0xcfddff);
//		System.out.println(0xf8ea81);
//		System.out.println(0x81b8e5);
//		System.out.println(0xffd1ad);
//		System.out.println(0xffe9f2);
//		System.out.println(0xfbf396);
//		System.out.println(0xd3ba7f);
//		System.out.println(0xe6e6e6);
//		System.out.println(0xafd0e9);
//		System.out.println(0xf7ffe4);
		System.out.println(0xfff799);
		
		System.out.println(drawCricle(15, Math.PI/6, 75, -20));
	}

}
