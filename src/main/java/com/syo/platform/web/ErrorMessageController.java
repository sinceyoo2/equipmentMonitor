package com.syo.platform.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.dto.ErrorQueryDTO;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.service.ErrorMessageService;

@Controller
@RequestMapping("/errMsg")
public class ErrorMessageController {
	
	@Autowired
	private ErrorMessageService errorMessageService;

	@RequestMapping({"{pageSize}/{pageNo}","/{pageSize}/{pageNo}","/list/{pageSize}/{pageNo}"})
	public String findErrors(ErrorQueryDTO condition, @PathVariable(name="pageNo")int pageNo,  @PathVariable(name="pageSize")int pageSize, Model model) {
		if(condition==null) {
			condition = new ErrorQueryDTO();
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.DATE, 1);
			Date end = c.getTime();
			c.add(Calendar.DATE, -1);
			c.set(Calendar.DATE, 1);
			Date start = c.getTime();
			condition.setStart(start);
			condition.setEnd(end);
		}
		model.addAttribute("errorPage", errorMessageService.findErros(pageNo, pageSize, condition));
		model.addAttribute("condition", condition);
		return "error_query";
	}
	
	@RequestMapping({"{pageNo}","/{pageNo}","/list/{pageNo}"})
	public String findErrors(ErrorQueryDTO condition, @PathVariable(name="pageNo")int pageNo, Model model) {
		return findErrors(condition, pageNo, 15, model);
	}
	
	@RequestMapping({"","/","/list"})
	public String findErrors(ErrorQueryDTO condition, Model model) {
		return findErrors(condition, 1, 15, model);
	}
	
	@RequestMapping("/warning")
	@ResponseBody
	public Page<ErrorMessage> findWarning(@RequestParam(defaultValue="1", required=false)int pageNo, @RequestParam(defaultValue="15", required=false)int pageSize) {
		return errorMessageService.findWarningErros(pageNo, pageSize);
	}
	
	@RequestMapping("/item/{id}")
	@ResponseBody
	public ErrorMessage findErrorById(@PathVariable("id")String id) {
		return errorMessageService.findErrorMessageById(id);
	}
	
}
