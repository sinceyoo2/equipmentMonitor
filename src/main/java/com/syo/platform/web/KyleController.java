package com.syo.platform.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kyle")
public class KyleController {

	@RequestMapping("/page")
	public String page(String id, Model model) {
		model.addAttribute("id", id);
		return "kyle_page";
	}
	
}
