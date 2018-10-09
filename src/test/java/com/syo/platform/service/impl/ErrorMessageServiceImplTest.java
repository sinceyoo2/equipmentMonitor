package com.syo.platform.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.syo.platform.service.ErrorMessageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorMessageServiceImplTest {
	
	@Autowired
	ErrorMessageService errorMessageService;

	@Test
	public void testSaveAndStartFlow() {
//		errorMessageService.saveAndStartFlow(errorMessageService.findErrorMessageById("03dc3bbc-e74c-4a21-83b5-6fe8ceab95cd"));
	}

}
