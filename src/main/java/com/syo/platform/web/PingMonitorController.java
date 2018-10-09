package com.syo.platform.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.entity.PingMonitorConfig;
import com.syo.platform.service.PingMonitorService;

@Controller
@RequestMapping("/pingMonitor")
public class PingMonitorController {
	
	@Autowired
	private PingMonitorService pingMonitorService;

	@RequestMapping({"","/"})
	public String dbmonitor(String vague, Model model) {
		model.addAttribute("configs", pingMonitorService.findConfig(vague));
		model.addAttribute("vague", vague);
		
		return "pingMonitor";
	}
	
	@PostMapping("/config_save")
	@ResponseBody
	public String saveHostConfig(PingMonitorConfig config) {
		pingMonitorService.saveConfig(config);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/config/{id}")
	@ResponseBody
	public PingMonitorConfig findConfigById(@PathVariable("id")String id) {
		return pingMonitorService.findConfigById(id);
	}
	
	@RequestMapping("/config_del/{id}")
	@ResponseBody
	public String del(@PathVariable("id")String id) {
		pingMonitorService.deleteConfig(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/start/{id}")
	@ResponseBody
	public String startMonitor(@PathVariable("id")String id) {
		pingMonitorService.startMonitor(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/stop/{id}")
	@ResponseBody
	public String stopMonitor(@PathVariable("id")String id) {
		pingMonitorService.stopMonitor(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/start_batch/{ids}")
	@ResponseBody
	public String startMonitorBatch(@PathVariable("ids")String ids) {
		String[] idArr = ids.split(",");
		int successCount = 0;
		for(String id : idArr) {
			boolean success = pingMonitorService.startMonitor(id);
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
			boolean success = pingMonitorService.stopMonitor(id);
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
		pingMonitorService.switchMonitor(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/doMonitor/{id}")
	@ResponseBody
	public boolean doMonitor(@PathVariable("id")String id) {
		return pingMonitorService.monitor(id);
	}
	
	@RequestMapping("/setBind/{configId}/{equipmentId}")
	@ResponseBody
	public String setBind(@PathVariable("configId")String configId, @PathVariable("equipmentId")String equipmentId) {
		pingMonitorService.saveBind(configId, equipmentId);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/segment")
	@ResponseBody
	public List<PingMonitorConfig> findConfigBySegment(String from, String to) {
		return pingMonitorService.findSegmentConfig(from, to);
	}
	
	@PostMapping("/segment_save")
	@ResponseBody
	public String saveSegment(@RequestBody List<PingMonitorConfig> configs) {
		return pingMonitorService.saveConigs(configs);
	}
}
