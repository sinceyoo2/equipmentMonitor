package com.syo.platform.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.syo.platform.activemq.MonitorMessageSender;
import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.WebMonitorConfig;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.WebMonitorConfigRepository;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.service.WebMonitorService;
import com.syo.platform.taskScheduler.CronUtil;

@Service
public class WebMonitorServiceImpl implements WebMonitorService {
	
	@Autowired
	private WebMonitorConfigRepository webMonitorConfigRepository;
	
	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Autowired
	private ErrorMessageRepository errorMessageRepository;
	
	@Autowired
	private ErrorMessageService errorMessageService;
	
	@Autowired
	private MonitorMessageSender messageSender;
	
	@Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	private Map<String, ScheduledFuture> futures = new HashMap<String, ScheduledFuture>();
	
	private Map<String, ErrorMessage> errors = new HashMap<String, ErrorMessage>();
	
	@PostConstruct
	public void init() {
		//恢复正在执行的任务
		List<WebMonitorConfig> runnings = webMonitorConfigRepository.findRunning();
		for(WebMonitorConfig config : runnings) {
			startMonitor(config.getId());
		}
		List<ErrorMessage> runningErrs = errorMessageRepository.findRunning("web服务");
		for(ErrorMessage err : runningErrs) {
			errors.put(err.getConfigId(), err);
		}
		System.out.println("web服务监控就绪");
	}
	
	@PreDestroy
	public void destroy() {
		for(Iterator<Entry<String, ScheduledFuture>> i=futures.entrySet().iterator(); i.hasNext();) {
			Entry<String, ScheduledFuture> entry = i.next();
			entry.getValue().cancel(true);
			i.remove();
		}
		System.out.println("web服务监控停止完成");
	}

	@Override
	public void saveConfig(WebMonitorConfig config) {
		if(config.getId()==null || config.getId().trim().equals("")) {
			config.setLastUpdateTime(new Date());
			config.setCreateTime(config.getLastUpdateTime());
			if(StringUtils.isBlank(config.getWebURL())) {
				initURL(config);
			}
			config.setCron(CronUtil.getCronExpression(config));
			webMonitorConfigRepository.save(config);
		} else {
			WebMonitorConfig currentConfig = webMonitorConfigRepository.findOne(config.getId());
			if(currentConfig!=null) {
				currentConfig.setName(config.getName());
				currentConfig.setWebServerVendor(config.getWebServerVendor());
				currentConfig.setServiceName(config.getServiceName());
				currentConfig.setSmsReceivers(config.getSmsReceivers());
				currentConfig.setEmailReceivers(config.getEmailReceivers());
				currentConfig.setErrorContinued(config.getErrorContinued());
				currentConfig.setLastUpdateTime(new Date());
				currentConfig.setRemarks(config.getRemarks());
				currentConfig.setTaskDays(config.getTaskDays());
				currentConfig.setTaskHours(config.getTaskHours());
				currentConfig.setTaskWeeks(config.getTaskWeeks());
				currentConfig.setTaskType(config.getTaskType());				
				if(!StringUtils.isBlank(config.getWebURL())) {
					currentConfig.setWebURL(config.getWebURL());
				}
				if((!StringUtils.isBlank(config.getIpAddress()) && !config.getIpAddress().equals(currentConfig.getIpAddress())) || 
						(config.getPort()!=null && config.getPort().equals(currentConfig.getPort()))) {
					if(StringUtils.isBlank(currentConfig.getWebURL()) || currentConfig.getWebURL().equals(config.getWebURL())) {
						initURL(currentConfig);
					}
				}
				
				currentConfig.setIpAddress(config.getIpAddress());
				currentConfig.setPort(config.getPort());
				currentConfig.setCron(CronUtil.getCronExpression(currentConfig));
				webMonitorConfigRepository.save(currentConfig);
			}
		}
	}

	private void initURL(WebMonitorConfig config) {
		config.setWebURL("http://"+config.getIpAddress()+":"+config.getPort());
	}

	@Override
	public List<WebMonitorConfig> findConfig(String vague) {
		if(vague==null || vague.trim().equals("")) {
			return webMonitorConfigRepository.findAll();
		}
		return webMonitorConfigRepository.findByVague(vague);
	}

	@Override
	public WebMonitorConfig findConfigById(String id) {
		return webMonitorConfigRepository.findOne(id);
	}

	@Override
	public void deleteConfig(String id) {
		webMonitorConfigRepository.delete(id);
	}

	@Override
	public boolean startMonitor(String configId) {
		
		WebMonitorConfig config = webMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("监控中")) {
			return false;
		}
		if(StringUtils.isBlank(config.getCron())) {
			return false;
		}
		
		ScheduledFuture future = futures.get(configId);
		if(future!=null) {
			future.cancel(true);
			futures.remove(configId);
		}
		
		future = threadPoolTaskScheduler.schedule(new Monitor(config.getId()), new CronTrigger(config.getCron()));
		futures.put(configId, future);
		
		config.setStatus("监控中");
		webMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public boolean stopMonitor(String configId) {
		
		WebMonitorConfig config = webMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			return false;
		}
		
		ScheduledFuture future = futures.get(configId);
		if(future!=null) {
			future.cancel(true);
			futures.remove(configId);
		}
		
		config.setStatus("未运行");
		webMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public void switchMonitor(String configId) {
		WebMonitorConfig config = webMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			startMonitor(configId);
		} else if(config.getStatus().equals("监控中")) {
			stopMonitor(configId);
		}
	}
	
	public boolean monitor(String configId) {
		Date checkTime = new Date();
		WebMonitorConfig config = webMonitorConfigRepository.findOne(configId);
		
		URL url = null;
		HttpURLConnection con = null;
		int state = -1;
		
		String cause = null;
		String summary = null;
		String description = null;
		boolean success = false;
		
		int counts = 0;
		while (counts < config.getErrorContinued()) {
			try {
				url = new URL(config.getWebURL());
				con = (HttpURLConnection) url.openConnection();
				state = con.getResponseCode();
				System.out.println(counts +"= "+state);
				if (state == 200) {
					System.out.println("URL可用！");
				}
				success = true;
				break;
			}catch (Exception e) {
				counts++; 
				//System.out.println("URL不可用，连接第 "+counts+" 次");
				cause = "URL不可用,无法访问此网站";
				summary = config.getWebURL()+" web服务出现异常。异常原因："+cause;
				description = summary+"|"+e.getMessage();
				continue;
			}
		}
		
		config.setLastTime(checkTime);
		config.setNextTime(CronUtil.getNextTriggerTime(checkTime, config.getCron()));
		if(!success) {
			if(errors.get(config.getId())==null) {
				ErrorMessage error = new ErrorMessage();
				error.setId(UUID.randomUUID().toString());
				error.setCause(cause);//
				error.setSummary(summary);
				error.setConfigId(config.getId());
				error.setDeclareType("监控发现");
				error.setDepartment("监控平台");
				error.setDescription(description);//
				error.setErrorLevel(2);
				error.setErrorNo("web_m001");//
				error.setErrorTime(checkTime);
				error.setErrorType("警告");
				error.setMsgSource("监控");
				error.setStatus("发现故障");
				error.setTargetName(config.getName());
				error.setTargetType("web服务");
				error.setTroubleshooter("web服务监控任务");
				error.setErrorIp(config.getIpAddress());
				
				error.setEquipment(config.getBindEquipment());
				
//				errorMessageRepository.save(error);
				errorMessageService.saveAndStartFlow(error);
				errors.put(config.getId(), error);
			}
			
			config.setLastResult("故障");
			
			String message = "【web服务故障】"+summary + "【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
			if(StringUtils.isNotBlank(config.getSmsReceivers())) {
				messageSender.sendSms(message, config.getSmsReceivers());
			}
			if(StringUtils.isNotBlank(config.getEmailReceivers())) {
				messageSender.sendMail("web服务故障", message, config.getEmailReceivers());
			}
		} else {
			if(errors.get(config.getId())!=null) {
				ErrorMessage error = errors.get(config.getId());
				error.setRecoveryTime(checkTime);
				error.setDuration((error.getRecoveryTime().getTime()-error.getErrorTime().getTime())/1000);
				error.setStatus("已恢复");
				
				errorMessageRepository.save(error);
				errors.remove(config.getId());
				
				String message = "【web服务故障恢复】"+summary + ". 故障已恢复。【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
				if(StringUtils.isNotBlank(config.getSmsReceivers())) {
					messageSender.sendSms(message, config.getSmsReceivers());
				}
				if(StringUtils.isNotBlank(config.getEmailReceivers())) {
					messageSender.sendMail("web服务故障恢复", message, config.getEmailReceivers());
				}
			}
			config.setLastResult("正常");
		}
		webMonitorConfigRepository.save(config);	
		
		return success;
	}
	
	public class Monitor implements Runnable {
		
		private String configId;
		
		public Monitor(String configId) {
			super();
			this.configId = configId;
		}

		public void run() {
			monitor(configId);
		}
		
	}

	@Override
	public void saveBind(String configId, String equipmentId) {
		Equipment equipment = equipmentRepository.findOne(equipmentId);
		WebMonitorConfig config = webMonitorConfigRepository.findOne(configId);
		config.setBindEquipment(equipment);
		webMonitorConfigRepository.save(config);
	}

}
