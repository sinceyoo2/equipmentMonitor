package com.syo.platform.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.service.LibraryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryServiceImplTest {

	@Autowired
	private LibraryService libraryService;
	
	@Test
	public void testFindLibrary() {
		//fail("Not yet implemented");
//		System.out.println(libraryService.findLibrary("", ""));
//		System.out.println(libraryService.findLibrary("master", ""));
//		System.out.println(libraryService.findLibrary("intelligent", "F"));
	}

}
