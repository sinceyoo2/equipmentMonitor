package com.syo.platform.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.entity.Library;
import com.syo.platform.service.LibraryService;

@Controller
@RequestMapping("/lib_map")
public class LibraryMapController {
	
	@Autowired
	private LibraryService libraryService;

	@RequestMapping({"","/"})
	public String map(Model model) {
		model.addAttribute("typeAmount", libraryService.findTypeAmount());
		return "lib_map";
	}
	
	
	@RequestMapping("/libs")
	@ResponseBody
	public List<Library> findLibs(String libType, String vague) {
		return libraryService.findLibrary(libType, vague);
	}
}
