package com.syo.platform.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.syo.platform.poi.ExcelData;
import com.syo.platform.poi.ExportExcelUtils;
import com.syo.platform.service.ErrorReportService;

@Controller
@RequestMapping("/errReport")
public class ErrorReportController {
	
	@Autowired
	private ErrorReportService errorReportService;
	
	@RequestMapping("/report_export/{type}")
	public void exportReport(@PathVariable("type")String type, Date start, Date end, HttpServletResponse resp) throws Exception {
		if(start==null || end==null) {
			return;
		}
		List<Map<String, Object>> datas = null;
		String name = null;
		String[] titles = null;
		String[] fields = null;
		if("total".equals(type)) {
			datas = errorReportService.findTotalReport(start, end);
			name = "故障总量统计";
			titles = new String[]{"设备/系统名称","故障总量"};
			fields = new String[]{"target_name","amount"};
		} else if("duration".equals(type)) {
			datas = errorReportService.findDurationReport(start, end);
			name = "故障总量及历时统计";
			titles = new String[]{"设备/系统名称","故障总量","故障总时间"};
			fields = new String[]{"target_name","amount", "duration"};
		} else if("cause".equals(type)) {
			datas = errorReportService.findCauseReport(start, end);
			name = "故障原因分类统计";
			titles = new String[]{"故障原因","总数"};
			fields = new String[]{"cause","amount"};
		} else if("type".equals(type)) {
			datas = errorReportService.findTypeReport(start, end);
			name = "故障专业类型统计";
			titles = new String[]{"告警类型","总数"};
			fields = new String[]{"error_type","amount"};
		} else if("dept".equals(type)) {
			datas = errorReportService.findDeptReport(start, end);
			name = "故障申告单位统计";
			titles = new String[]{"申告单位","总数","已处理数量"};
			fields = new String[]{"department","amount","dual_amount"};
		} else if("level".equals(type)) {
			datas = errorReportService.findLevelReport(start, end);
			name = "故障级别统计";
			titles = new String[]{"故障级别","总数"};
			fields = new String[]{"error_level","amount"};
		} else if("active".equals(type)) {
			datas = errorReportService.findActiveReport(start, end);
			name = "故障主动发现率统计";
			titles = new String[]{"故障主体","故障总量","主动发现数量","主动发现率"};
			fields = new String[]{"target_name","amount","active_amount","percentage"};
		} else if("troubleshooter".equals(type)) {
			datas = errorReportService.findTroubleshooterReport(start, end);
			name = "故障申告者统计";
			titles = new String[]{"申告人","数量","已处理数量"};
			fields = new String[]{"troubleshooter","amount","dual_amount"};
		} else if("declare".equals(type)) {
			datas = errorReportService.findDeclareTypeReport(start, end);
			name = "故障申告方式统计";
			titles = new String[]{"申告方式","数量","已处理数量"};
			fields = new String[]{"declare_type","amount","dual_amount"};
		}
		ExcelData<Map<String,Object>> excelData = new ExcelData<Map<String,Object>>(titles, fields, datas, name);
		ExportExcelUtils.exportExcel(resp, name+".xls", excelData);
	}
	
	@RequestMapping("/report/{type}")
	public String report(@PathVariable("type")String type, Date start, Date end, Model model) {
		if(start==null || end==null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.DATE, 1);
			end = c.getTime();
			c.add(Calendar.DATE, -1);
			c.set(Calendar.DATE, 1);
			start = c.getTime();
		}
		
		if("total".equals(type)) {
			model.addAttribute("report", errorReportService.findTotalReport(start, end));
			model.addAttribute("reportName","故障总量统计");
		} else if("duration".equals(type)) {
			model.addAttribute("report", errorReportService.findDurationReport(start, end));
			model.addAttribute("reportName","故障总量及历时统计");
		} else if("cause".equals(type)) {
			model.addAttribute("report", errorReportService.findCauseReport(start, end));
			model.addAttribute("reportName","故障原因分类统计");
		} else if("type".equals(type)) {
			model.addAttribute("report", errorReportService.findTypeReport(start, end));
			model.addAttribute("reportName","故障专业类型统计");
		} else if("dept".equals(type)) {
			model.addAttribute("report", errorReportService.findDeptReport(start, end));
			model.addAttribute("reportName","故障申告单位统计");
		} else if("level".equals(type)) {
			model.addAttribute("report", errorReportService.findLevelReport(start, end));
			model.addAttribute("reportName","故障级别统计");
		} else if("active".equals(type)) {
			model.addAttribute("report", errorReportService.findActiveReport(start, end));
			model.addAttribute("reportName","故障主动发现率统计");
		} else if("troubleshooter".equals(type)) {
			model.addAttribute("report", errorReportService.findTroubleshooterReport(start, end));
			model.addAttribute("reportName","故障申告者统计");
		} else if("declare".equals(type)) {
			model.addAttribute("report", errorReportService.findDeclareTypeReport(start, end));
			model.addAttribute("reportName","故障申告方式统计");
		}      
		
		model.addAttribute("type", type);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		
		return "errreport";
	}

//	@RequestMapping("/total")
//	public String totalReport(Date start, Date end, Model model) {
//		if(start==null || end==null) {
//			Calendar c = Calendar.getInstance();
//			c.set(Calendar.HOUR_OF_DAY, 0);
//			c.set(Calendar.MINUTE, 0);
//			c.set(Calendar.SECOND, 0);
//			c.set(Calendar.MILLISECOND, 0);
//			c.add(Calendar.DATE, 1);
//			end = c.getTime();
//			c.add(Calendar.DATE, -1);
//			c.set(Calendar.DATE, 1);
//			start = c.getTime();
//		}
//		
//		model.addAttribute("report", errorReportService.findTotalReport(start, end));
//		model.addAttribute("start", start);
//		model.addAttribute("end", end);
//		return "errreport_total";
//	}
//	
//	@RequestMapping("/duration")
//	public String durationReport(Date start, Date end, Model model) {
//		if(start==null || end==null) {
//			Calendar c = Calendar.getInstance();
//			c.set(Calendar.HOUR_OF_DAY, 0);
//			c.set(Calendar.MINUTE, 0);
//			c.set(Calendar.SECOND, 0);
//			c.set(Calendar.MILLISECOND, 0);
//			c.add(Calendar.DATE, 1);
//			end = c.getTime();
//			c.add(Calendar.DATE, -1);
//			c.set(Calendar.DATE, 1);
//			start = c.getTime();
//		}
//		
//		model.addAttribute("report", errorReportService.findDurationReport(start, end));
//		model.addAttribute("start", start);
//		model.addAttribute("end", end);
//		return "errreport_duration";
//	}
//	
//	@RequestMapping("/cause")
//	public String causeReport(Date start, Date end, Model model) {
//		if(start==null || end==null) {
//			Calendar c = Calendar.getInstance();
//			c.set(Calendar.HOUR_OF_DAY, 0);
//			c.set(Calendar.MINUTE, 0);
//			c.set(Calendar.SECOND, 0);
//			c.set(Calendar.MILLISECOND, 0);
//			c.add(Calendar.DATE, 1);
//			end = c.getTime();
//			c.add(Calendar.DATE, -1);
//			c.set(Calendar.DATE, 1);
//			start = c.getTime();
//		}
//		
//		model.addAttribute("report", errorReportService.findCauseReport(start, end));
//		model.addAttribute("start", start);
//		model.addAttribute("end", end);
//		return "errreport_cause";
//	}
	
}
