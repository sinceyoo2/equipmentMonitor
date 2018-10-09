package com.syo.platform.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.dto.WorkSheetDto;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.User;
import com.syo.platform.entity.WorkSheet;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.service.impl.WorkSheetServiceImpl;

@Controller
@RequestMapping("/worksheet")
public class WorkSheetController {
	
	@Autowired
	private WorkSheetServiceImpl workSheetService;
	
	@Autowired
	private ErrorMessageService errorMessageService;
	
	@RequestMapping("/{pageSize}/{pageNo}")
	public String findWorkSheet(WorkSheetDto condition, @PathVariable("pageNo")int pageNo, @PathVariable("pageSize")int pageSize, Model model) {
		model.addAttribute("condition", condition);
		model.addAttribute("workSheets", workSheetService.findWorkSheets(pageNo, pageSize, condition));
		return "worksheet";
	}
	
	@RequestMapping("/{pageNo}")
	public String findWorkSheet(WorkSheetDto condition, @PathVariable("pageNo")int pageNo, Model model) {
		return findWorkSheet(condition, pageNo, 15, model);
	}
	
	@RequestMapping({"","/","/worksheets"})
	public String findWorkSheet(WorkSheetDto condition, Model model) {
		return findWorkSheet(condition, 1, 15, model);
	}

	@RequestMapping("/new_item/{errorMessageId}")
	public String findWorkSheet(@PathVariable("errorMessageId")String errorMessageId, Model model, HttpSession session) {
		ErrorMessage errorMessage = errorMessageService.findErrorMessageById(errorMessageId);
		WorkSheet workSheet = new WorkSheet();
		workSheet.setErrorMessage(errorMessage);
		workSheet.setTitle(errorMessage.getCause());
		workSheet.setSituation(errorMessage.getDescription());
		
		User loginUser = (User) session.getAttribute("loginUser");
		workSheet.setDeptName(loginUser.getDeptName());
		workSheet.setContacts(loginUser.getName());
		workSheet.setContactNumber(loginUser.getTelephone());
		
		
		model.addAttribute("workSheet", workSheet);
		model.addAttribute("taskId", null);
		Map<String, Map<String, Boolean>> defaultProperties = new HashMap<>();
		defaultProperties.put("writable", new HashMap<>());
		defaultProperties.get("writable").put("deptName", true);
		defaultProperties.get("writable").put("title", true);
		defaultProperties.get("writable").put("contacts", true);
		defaultProperties.get("writable").put("contactNumber", true);
		defaultProperties.get("writable").put("situation", true);
		defaultProperties.get("writable").put("remarks", true);
		model.addAttribute("formProperties", defaultProperties);
		return "worksheet_detail";
	}
	
	@RequestMapping("/process_item/{taskId}")
	public String processForm(@PathVariable("taskId")String taskId, Model model) {
		model.addAttribute("workSheet", workSheetService.findFormEntityByTaskId(taskId));
		model.addAttribute("taskId", taskId);
		model.addAttribute("taskKey",workSheetService.findTaskById(taskId).getTaskDefinitionKey());
		model.addAttribute("formProperties", workSheetService.findFormPropertyByTaskId(taskId));
		return "worksheet_detail";
	}
	
	@RequestMapping("/item/{id}")
	@ResponseBody
	public WorkSheet findWorkSheet(@PathVariable("id")String id) {
		WorkSheet workSheet = workSheetService.findWorkSheetById(id);
		return workSheet;
	}
	
	@PostMapping("/new")
	@ResponseBody
	public String saveNewWorkSheet(WorkSheet workSheet, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		String account = loginUser.getAccount();
		workSheetService.saveWorkSheet(workSheet, account);
//		return "SUCCESS";
		return "操作成功";
	}

	@PostMapping("/complete/{taskId}")
	@ResponseBody
	public String complete(@PathVariable("taskId")String taskId, WorkSheet workSheet, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		String account = loginUser.getAccount();
		workSheetService.completeTaskByUser(taskId, account, workSheet, workSheet.getId());
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/running")
	public String findRunningTask(Model model, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		String account = loginUser.getAccount();
		//model.addAttribute("tasks", workSheetService.findTaskByAccount(account));
		model.addAttribute("workSheets", workSheetService.findWorkSheetsInTask(account));	
		return "worksheet_runningtask";
	}
	
	@RequestMapping("/history")
	public String findHistory(Model model, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		String account = loginUser.getAccount();
		model.addAttribute("workSheets", workSheetService.findWorkSheetInHistoryTask(account));	
		return "worksheet_historytask";
	}
	
	@RequestMapping("/history_step/{workSheetId}")
	@ResponseBody
	public List<HistoricTaskInstance> findHistoryTask(@PathVariable("workSheetId")String workSheetId) {
		return workSheetService.findHistoryTaskByBusinessKey(workSheetId);
	}
}
