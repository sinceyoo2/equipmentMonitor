package com.syo.platform.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.entity.DataBaseMonitorConfig;
import com.syo.platform.entity.HostMonitorConfig;
import com.syo.platform.service.DataBaseMonitorService;

@Controller
@RequestMapping("/dbMonitor")
public class DataBaseMonitorController {
	
	@Autowired
	private DataBaseMonitorService dataBaseMonitorConfigService;

	@RequestMapping({"","/"})
	public String dbmonitor(String vague, Model model) {
		model.addAttribute("configs", dataBaseMonitorConfigService.findConfig(vague));
		model.addAttribute("vague", vague);
		
		return "dbMonitor";
	}
	
	@PostMapping("/config_save")
	@ResponseBody
	public String saveHostConfig(DataBaseMonitorConfig config) {
		dataBaseMonitorConfigService.saveConfig(config);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/config/{id}")
	@ResponseBody
	public DataBaseMonitorConfig findConfigById(@PathVariable("id")String id) {
		return dataBaseMonitorConfigService.findConfigById(id);
	}
	
	@RequestMapping("/config_del/{id}")
	@ResponseBody
	public String del(@PathVariable("id")String id) {
		dataBaseMonitorConfigService.deleteConfig(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/start/{id}")
	@ResponseBody
	public String startMonitor(@PathVariable("id")String id) {
		dataBaseMonitorConfigService.startMonitor(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/stop/{id}")
	@ResponseBody
	public String stopMonitor(@PathVariable("id")String id) {
		dataBaseMonitorConfigService.stopMonitor(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/start_batch/{ids}")
	@ResponseBody
	public String startMonitorBatch(@PathVariable("ids")String ids) {
		String[] idArr = ids.split(",");
		int successCount = 0;
		for(String id : idArr) {
			boolean success = dataBaseMonitorConfigService.startMonitor(id);
			if(success) {
				successCount ++;
			}
		}
		String result = "供选择配置 "+idArr.length+" 个，成功启用 "+successCount+" 个";

		return result;
	}
	
	@RequestMapping("/stop_batch/{ids}")
	@ResponseBody
	public String stopMonitorBatch(@PathVariable("ids")String ids) {
		String[] idArr = ids.split(",");
		int successCount = 0;
		for(String id : idArr) {
			boolean success = dataBaseMonitorConfigService.stopMonitor(id);
			if(success) {
				successCount ++;
			}
		}
		String result = "供选择配置 "+idArr.length+" 个，成功停用 "+successCount+" 个";

		return result;
	}
	
	@RequestMapping("/switch/{id}")
	@ResponseBody
	public String switchMonitor(@PathVariable("id")String id) {
		dataBaseMonitorConfigService.switchMonitor(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/config/testDB")
	@ResponseBody
	public String testDB(DataBaseMonitorConfig config) {
		return dataBaseMonitorConfigService.testDB(config);
	}
	
	@RequestMapping("/doMonitor/{id}")
	@ResponseBody
	public boolean doMonitor(@PathVariable("id")String id) {
		return dataBaseMonitorConfigService.monitor(id);
	}
	
	@RequestMapping("/setBind/{configId}/{equipmentId}")
	@ResponseBody
	public String setBind(@PathVariable("configId")String configId, @PathVariable("equipmentId")String equipmentId) {
		dataBaseMonitorConfigService.saveBind(configId, equipmentId);
//		return "SUCCESS";
		return "操作成功";
	}
}
