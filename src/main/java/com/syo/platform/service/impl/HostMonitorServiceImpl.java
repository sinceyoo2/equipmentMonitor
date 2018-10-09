package com.syo.platform.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syo.platform.activemq.MonitorMessageSender;
import com.syo.platform.entity.DataBaseMonitorConfig;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.HostErrorMessage;
import com.syo.platform.entity.HostMonitorConfig;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.HostMonitorConfigRepository;
import com.syo.platform.repository.HostMonitorDataDao;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.service.HostMonitorService;
import com.syo.platform.sms.SMSUtil;

@Service
public class HostMonitorServiceImpl implements HostMonitorService {
	
	@Autowired
	HostMonitorDataDao dao;
	
	@Autowired
	private HostMonitorConfigRepository hostMonitorConfigRepository;
	
	@Autowired
	private ErrorMessageRepository errorMessageRepository;	
	
	@Autowired
	private ErrorMessageService errorMessageService;
	
	@Autowired
	private MonitorMessageSender messageSender;

	@Override
	public void handleError(HostErrorMessage hostError) {
		HostMonitorConfig config = hostMonitorConfigRepository.findOne(hostError.getConfigId());
		if(hostError.getMsgType().equals("error")) {						
			ErrorMessage error = new ErrorMessage();
			error.setId(hostError.getBatchId());	
			error.setCause(hostError.getShortMsg());//
			error.setSummary(config.getHostName()+"("+config.getIpAddress()+")"+"|"+hostError.getShortMsg());
			error.setConfigId(config.getId());
			error.setDeclareType("监控发现");
			error.setDepartment("监控平台");
			error.setDescription(error.getSummary()+"|"+hostError.getMsg());//
			error.setErrorLevel(2);
			error.setErrorNo("host_m001");//
			error.setErrorTime(hostError.getTime());
			error.setErrorType("警告");
			error.setMsgSource("监控");
			error.setStatus("发现故障");
			error.setTargetName(config.getHostName());
			error.setTargetType(hostError.getInfoType());
			error.setTroubleshooter(hostError.getInfoType()+"监控任务");
			error.setErrorIp(config.getIpAddress());
			
			error.setEquipment(config.getBindEquipment());

//			errorMessageRepository.save(error);
			errorMessageService.saveAndStartFlow(error);
			
			config.setLastResult("故障");
			hostMonitorConfigRepository.save(config);
			
			
		} else if(hostError.getMsgType().equals("recovery")) {
			ErrorMessage error = errorMessageRepository.findOne(hostError.getBatchId());
			if(error!=null && error.getId()!=null) {
				error.setRecoveryTime(hostError.getTime());
				error.setDuration((error.getRecoveryTime().getTime()-error.getErrorTime().getTime())/1000);
				error.setStatus("已恢复");
				errorMessageRepository.save(error);
			}
			config.setLastResult("正常");
			hostMonitorConfigRepository.save(config);
		}
		
//		SMSUtil.sendSMS(hostError.getMsg(), config.getSmsReceivers());
		if(StringUtils.isNotBlank(config.getSmsReceivers())) {
			messageSender.sendSms(hostError.getMsg(), config.getSmsReceivers());
		}
		if(StringUtils.isNotBlank(config.getEmailReceivers())) {
			messageSender.sendMail("服务器监控", hostError.getMsg(), config.getEmailReceivers());
		}
		
	}
	
	@Override
	public List<ErrorMessage> findRunningByConfigId(String configId) {
		return errorMessageRepository.findRunningByConfigId(configId);
	}

	@Override
	public List<Map> findHisData(Date start, Date end, String infoType, String ip) {
		return dao.findHisData(start, end, infoType, ip);
	}

	@Override
	public List<Map> findErrData(Date start, Date end, String infoType, String ip) {
		return dao.findErrData(start, end, infoType, ip);
	}

	@Override
	public Map<String, Object> findHisChart(Date start, Date end, String infoType, String ip) {
		List<Map> datas = dao.findHisData(start, end, infoType, ip);
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(infoType.equals("CPUInfo")) {
			result.put("chartName", "cpu");
			
			Object[] times = new Object[datas.size()];
			Object[] combineds = new Object[datas.size()];
			Object[] idles = new Object[datas.size()];
			
			result.put("combineds", combineds);
			result.put("idles", idles);
			result.put("times", times);
			for(int i=0; i<datas.size(); i++) {
				Map data = datas.get(i);
				combineds[i] = data.get("combined");
				idles[i] = data.get("idle");
				
				times[i] = "";
			}
		} else if(infoType.equals("MemoryInfo")) {
			result.put("chartName", "memory");
			
			Object[] times = new Object[datas.size()];
			Object[] usedPercents = new Object[datas.size()];

			result.put("usedPercents", usedPercents);
			result.put("times", times);
			for(int i=0; i<datas.size(); i++) {
				Map data = datas.get(i);
				usedPercents[i] = data.get("usedPercent");
				
				times[i] = "";
			}
		} else if(infoType.equals("NetworkInfo")) {
			result.put("chartName", "network");
			
			Object[] times = new Object[datas.size()];
			Object[] rxBytes = new Object[datas.size()];
			Object[] txBytes = new Object[datas.size()];
			
			result.put("rxBytes", rxBytes);
			result.put("txBytes", txBytes);
			result.put("times", times);
			for(int i=0; i<datas.size(); i++) {
				Map data = datas.get(i);
				rxBytes[i] = data.get("rxBytes");
				txBytes[i] = data.get("txBytes");
				
				times[i] = "";
			}
		} else {
			if(datas.size()<=0) {
				start = new Date(start.getTime()-3600000);
				end = new Date(end.getTime()+3600000);
				datas =  dao.findHisData(start, end, infoType, ip);
			}
			result.put("chartName", "disk");
			Map diskInfo = datas.get(datas.size()-1);
			List<Map> items = (List<Map>) diskInfo.get("items");
			
			Object[] parts = new Object[items.size()];
			Object[] totals = new Object[items.size()];
			Object[] useds = new Object[items.size()];
			
			result.put("parts", parts);
			result.put("totals", totals);
			result.put("useds", useds);
			
			for(int i=0; i<items.size(); i++) {
				parts[i] = items.get(i).get("devName");
				totals[i] = items.get(i).get("total");
				useds[i] = items.get(i).get("used");
			}
		}
		
		
		return result;
	}

}
