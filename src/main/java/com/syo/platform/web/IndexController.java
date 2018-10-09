package com.syo.platform.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping({"","/","/index"})
	public String index() {
		return "index";
	}
	
	@RequestMapping("/map")
	public String map() {
		return "map_data";
	}
	
	@RequestMapping("/cabinet")
	public String cabinet() {
		return "cabinet";
	}
	
	@RequestMapping("/fpage/{pageName}")
	public String framePage(@PathVariable("pageName")String pageName) {
		return "pageName";
	}
	
}
