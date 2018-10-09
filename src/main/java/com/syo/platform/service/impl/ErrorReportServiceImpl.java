package com.syo.platform.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syo.platform.repository.ErrorReportDao;
import com.syo.platform.service.ErrorReportService;

@Service
public class ErrorReportServiceImpl implements ErrorReportService {

	@Autowired
	private ErrorReportDao errorReportDao;
	
	@Override
	public List<Map<String, Object>> findTotalReport(Date start, Date end) {
		if(start==null || end==null) {
			return new ArrayList<Map<String,Object>>();
		}
		return errorReportDao.findErrorAmount(start, end);
	}

	@Override
	public List<Map<String, Object>> findDurationReport(Date start, Date end) {
		if(start==null || end==null) {
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String, Object>> list = errorReportDao.findErrorAmountAndDuration(start, end);
		for(Map<String, Object> item : list) {
			BigDecimal duration = (BigDecimal) item.get("duration");
			long dvalue = duration.longValue();
			if(item.get("duration").toString().startsWith("-") || dvalue<0) {
				item.put("duration", "故障至今未恢复");
			} else {
				if(dvalue>3600) {
					String s = dvalue/3600+"时";
					long mod = dvalue%3600;
					if(mod>60) {
						s += " "+mod/60+"分";
						if(mod%60>0) {
							s+=" "+mod%60+"秒";
						}
					}
					item.put("duration", s);
				} else if(dvalue>60){
					String s = dvalue/60+"分";
					if(dvalue%60>0) {
						s+=" "+dvalue%60+"秒";
					}
					item.put("duration", s);
				} else {
					String s = dvalue+"秒";
					item.put("duration", s);
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findCauseReport(Date start, Date end) {
		if(start==null || end==null) {
			return new ArrayList<Map<String,Object>>();
		}
		return errorReportDao.findErrorCauseAmount(start, end);
	}

	@Override
	public List<Map<String, Object>> findTypeReport(Date start, Date end) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> warning = new HashMap<String, Object>();
		warning.put("error_type", "警告");
		warning.put("amount", 0);
		list.add(warning);
		Map<String, Object> error = new HashMap<String, Object>();
		error.put("error_type", "错误");
		error.put("amount", 0);
		list.add(error);
		
		if(start==null || end==null) {
			return list;
		}
		
		for(Map<String, Object> item : errorReportDao.findErrorTypeAmount(start, end)) {
			if("警告".equals(item.get("error_type"))) {
				warning.put("amount",  Integer.parseInt(warning.get("amount").toString())+Integer.parseInt(item.get("amount").toString()));
			} else if("错误".equals(item.get("error_type"))) {
				error.put("amount",  Integer.parseInt(error.get("amount").toString())+Integer.parseInt(item.get("amount").toString()));
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findDeptReport(Date start, Date end) {
		if(start==null || end==null) {
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String, Object>> list = errorReportDao.findErrorDeptAmount(start, end);
		Map<String, Map<String,Object>> temp = new HashMap<String, Map<String,Object>>();
		for(Map<String, Object> item : list) {
			item.put("dual_amount", 0);
			temp.put(item.get("department")+"", item);
		}
		List<Map<String, Object>> dualList = errorReportDao.findErrorDeptDealAmount(start, end);
		for(Map<String, Object> item : dualList) {
			Map<String, Object> listItem = temp.get(item.get("department")); 
			listItem.put("dual_amount", item.get("amount"));
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findLevelReport(Date start, Date end) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> lv1 = new HashMap<String, Object>();
		lv1.put("error_level", "轻微");
		lv1.put("amount", 0);
		list.add(lv1);
		
		Map<String, Object> lv2 = new HashMap<String, Object>();
		lv2.put("error_level", "一般");
		lv2.put("amount", 0);
		list.add(lv2);
		
		Map<String, Object> lv3 = new HashMap<String, Object>();
		lv3.put("error_level", "严重");
		lv3.put("amount", 0);
		list.add(lv3);
		
		if(start==null || end==null) {
			return list;
		}
		
		for(Map<String, Object> item : errorReportDao.findErrorLevelAmount(start, end)) {
			if(item.get("error_level").equals(1)) {
				lv1.put("amount", Integer.parseInt(lv1.get("amount").toString())+Integer.parseInt(item.get("amount").toString()));
			} else if(item.get("error_level").equals(2)) {
				lv2.put("amount", Integer.parseInt(lv2.get("amount").toString())+Integer.parseInt(item.get("amount").toString()));
			} else if(item.get("error_level").equals(3)) {
				lv3.put("amount", Integer.parseInt(lv3.get("amount").toString())+Integer.parseInt(item.get("amount").toString()));
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findActiveReport(Date start, Date end) {
		if(start==null || end==null) {
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String, Object>> list = errorReportDao.findErrorAmount(start, end);
		Map<String, Map<String,Object>> temp = new HashMap<String, Map<String,Object>>();
		for(Map<String, Object> item : list) {
			item.put("active_amount",0);
			temp.put(item.get("target_name")+"", item);
		}
		List<Map<String, Object>> activeList = errorReportDao.findErrorActiveAmount(start, end);
		DecimalFormat df = new DecimalFormat("00.00");
		for(Map<String, Object> item : activeList) {
			Map<String, Object> listItem = temp.get(item.get("target_name"));
			double total = Double.parseDouble(listItem.get("amount").toString());
			double active = Double.parseDouble(item.get("amount").toString());
			listItem.put("active_amount", item.get("amount"));
			String percent = df.format(active/total*100)+"%";
			listItem.put("percentage", percent);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findTroubleshooterReport(Date start, Date end) {
		if(start==null || end==null) {
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String, Object>> list = errorReportDao.findErrorTroubleshooterAmount(start, end);
		Map<String, Map<String,Object>> temp = new HashMap<String, Map<String,Object>>();
		for(Map<String, Object> item : list) {
			item.put("dual_amount", 0);
			temp.put(item.get("troubleshooter")+"", item);
		}
		List<Map<String, Object>> dualList = errorReportDao.findErrorTroubleshooterDealAmount(start, end);
		for(Map<String, Object> item : dualList) {
			Map<String, Object> listItem = temp.get(item.get("troubleshooter")); 
			listItem.put("dual_amount", item.get("amount"));
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> findDeclareTypeReport(Date start, Date end) {
		if(start==null || end==null) {
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String, Object>> list = errorReportDao.findErrorDeclareTypeAmount(start, end);
		Map<String, Map<String,Object>> temp = new HashMap<String, Map<String,Object>>();
		for(Map<String, Object> item : list) {
			item.put("dual_amount", 0);
			temp.put(item.get("declare_type")+"", item);
		}
		List<Map<String, Object>> dualList = errorReportDao.findErrorDeclareTypeDealAmount(start, end);
		for(Map<String, Object> item : dualList) {
			Map<String, Object> listItem = temp.get(item.get("declare_type")); 
			listItem.put("dual_amount", item.get("amount"));
		}
		return list;
	}

}
