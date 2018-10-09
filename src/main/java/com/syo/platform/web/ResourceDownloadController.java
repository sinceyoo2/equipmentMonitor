package com.syo.platform.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResourceDownloadController {
	
	@RequestMapping("/download")
	public ResponseEntity<byte[]> export(HttpServletRequest req) throws IOException {  
    	
        HttpHeaders headers = new HttpHeaders();    
        String filePath = req.getAttribute("filePath").toString();
        String fileName = req.getAttribute("fileName").toString();
        File file = new File(filePath);
        
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);    
//        headers.setContentDispositionFormData("attachment", fileName);    
        headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"),"iso-8859-1"));//解决中文乱码问题
       
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),    
                                              headers, HttpStatus.CREATED);    
//		return null;
    }

}
