package com.syo.platform.service.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
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
import com.syo.platform.entity.PortMonitorConfig;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.PortMonitorConfigRepository;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.service.PortMonitorService;
import com.syo.platform.taskScheduler.CronUtil;

@Service
public class PortMonitorServiceImpl implements PortMonitorService {
	
	@Autowired
	private PortMonitorConfigRepository portMonitorConfigRepository;
	
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
		List<PortMonitorConfig> runnings = portMonitorConfigRepository.findRunning();
		for(PortMonitorConfig config : runnings) {
			startMonitor(config.getId());
		}
		List<ErrorMessage> runningErrs = errorMessageRepository.findRunning("端口服务");
		for(ErrorMessage err : runningErrs) {
			errors.put(err.getConfigId(), err);
		}
		System.out.println("端口服务监控就绪");
	}
	
	@PreDestroy
	public void destroy() {
		for(Iterator<Entry<String, ScheduledFuture>> i=futures.entrySet().iterator(); i.hasNext();) {
			Entry<String, ScheduledFuture> entry = i.next();
			entry.getValue().cancel(true);
			i.remove();
		}
		System.out.println("端口服务监控停止完成");
	}

	@Override
	public void saveConfig(PortMonitorConfig config) {
		if(config.getId()==null || config.getId().trim().equals("")) {
			config.setLastUpdateTime(new Date());
			config.setCreateTime(config.getLastUpdateTime());
			config.setCron(CronUtil.getCronExpression(config));
			portMonitorConfigRepository.save(config);
		} else {
			PortMonitorConfig currentConfig = portMonitorConfigRepository.findOne(config.getId());
			if(currentConfig!=null) {
				currentConfig.setName(config.getName());
				currentConfig.setServiceName(config.getServiceName());
				currentConfig.setSmsReceivers(config.getSmsReceivers());
				currentConfig.setEmailReceivers(config.getEmailReceivers());
				currentConfig.setErrorContinued(config.getErrorContinued());
				currentConfig.setIpAddress(config.getIpAddress());
				currentConfig.setPort(config.getPort());
				currentConfig.setRemarks(config.getRemarks());
				currentConfig.setLastUpdateTime(new Date());
				currentConfig.setTaskDays(config.getTaskDays());
				currentConfig.setTaskHours(config.getTaskHours());
				currentConfig.setTaskWeeks(config.getTaskWeeks());
				currentConfig.setTaskType(config.getTaskType());
				currentConfig.setCron(CronUtil.getCronExpression(currentConfig));
				portMonitorConfigRepository.save(currentConfig);
			}
		}
	}


	@Override
	public List<PortMonitorConfig> findConfig(String vague) {
		if(vague==null || vague.trim().equals("")) {
			return portMonitorConfigRepository.findAll();
		}
		return portMonitorConfigRepository.findByVague(vague);
	}

	@Override
	public PortMonitorConfig findConfigById(String id) {
		return portMonitorConfigRepository.findOne(id);
	}

	@Override
	public void deleteConfig(String id) {
		portMonitorConfigRepository.delete(id);
	}

	@Override
	public boolean startMonitor(String configId) {
		
		PortMonitorConfig config = portMonitorConfigRepository.findOne(configId);
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
		portMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public boolean  stopMonitor(String configId) {
		
		PortMonitorConfig config = portMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			return false;
		}
		
		ScheduledFuture future = futures.get(configId);
		if(future!=null) {
			future.cancel(true);
			futures.remove(configId);
		}
		
		config.setStatus("未运行");
		portMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public void switchMonitor(String configId) {
		PortMonitorConfig config = portMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			startMonitor(configId);
		} else if(config.getStatus().equals("监控中")) {
			stopMonitor(configId);
		}
	}
	
	public boolean monitor(String configId) {
		Date checkTime = new Date();
		PortMonitorConfig config = portMonitorConfigRepository.findOne(configId);
		
		String cause = null;
		String summary = null;
		String description = null;
		boolean success = false;

		int counts = 0;
		while (counts < config.getErrorContinued()) {
			Socket server = null;
	        try {
	            server = new Socket();
	            InetSocketAddress address = new InetSocketAddress(config.getIpAddress(), config.getPort());
	            server.connect(address, 5000);
	            success = true;
				break;
	        } catch (Exception e) {
	            //System.out.println("telnet失败");
	            cause = "连接端口服务失败 ";
				summary = config.getIpAddress()+":"+config.getPort()+" 端口服务出现异常。异常原因："+cause;
				description = summary+"|"+e.getMessage();
				counts++; 
	        } finally{
	            if(server!=null)
	                try {
	                    server.close();
	                } catch (IOException e) {
	                }
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
				error.setErrorNo("port_m001");//
				error.setErrorTime(checkTime);
				error.setErrorType("警告");
				error.setMsgSource("监控");
				error.setStatus("发现故障");
				error.setTargetName(config.getName());
				error.setTargetType("端口服务");
				error.setTroubleshooter("端口服务监控任务");
				error.setErrorIp(config.getIpAddress());
				
				error.setEquipment(config.getBindEquipment());
				
//				errorMessageRepository.save(error);
				errorMessageService.saveAndStartFlow(error);
				errors.put(config.getId(), error);
			}
			
			config.setLastResult("故障");
			
			String message = "【端口服务故障】"+summary + "【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
			if(StringUtils.isNotBlank(config.getSmsReceivers())) {
				messageSender.sendSms(message, config.getSmsReceivers());
			}
			if(StringUtils.isNotBlank(config.getEmailReceivers())) {
				messageSender.sendMail("端口服务故障", message, config.getEmailReceivers());
			}
		} else {
			if(errors.get(config.getId())!=null) {
				ErrorMessage error = errors.get(config.getId());
				error.setRecoveryTime(checkTime);
				error.setDuration((error.getRecoveryTime().getTime()-error.getErrorTime().getTime())/1000);
				error.setStatus("已恢复");
				
				errorMessageRepository.save(error);
				errors.remove(config.getId());
				
				String message = "【端口服务故障恢复】"+summary + ". 故障已恢复。【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
				if(StringUtils.isNotBlank(config.getSmsReceivers())) {
					messageSender.sendSms(message, config.getSmsReceivers());
				}
				if(StringUtils.isNotBlank(config.getEmailReceivers())) {
					messageSender.sendMail("端口服务故障恢复", message, config.getEmailReceivers());
				}
			}
			config.setLastResult("正常");
		}
		portMonitorConfigRepository.save(config);	
		
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
		PortMonitorConfig config = portMonitorConfigRepository.findOne(configId);
		config.setBindEquipment(equipment);
		portMonitorConfigRepository.save(config);
	}

}
