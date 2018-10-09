package com.syo.platform.service.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.syo.platform.entity.PingMonitorConfig;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.PingMonitorConfigRepository;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.service.PingMonitorService;
import com.syo.platform.taskScheduler.CronUtil;

@Service
public class PingMonitorServiceImpl implements PingMonitorService {
	
	@Autowired
	private PingMonitorConfigRepository pingMonitorConfigRepository;
	
	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Autowired
	private ErrorMessageRepository errorMessageRepository;
	
	@Autowired
	private ErrorMessageService errorMessageService;
	
	@Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	
	@Autowired
	private MonitorMessageSender messageSender;
	
	private Map<String, ScheduledFuture> futures = new HashMap<String, ScheduledFuture>();
	
	private Map<String, ErrorMessage> errors = new HashMap<String, ErrorMessage>();
	
	
	@PostConstruct
	public void init() {
		//恢复正在执行的任务
		List<PingMonitorConfig> runnings = pingMonitorConfigRepository.findRunning();
		for(PingMonitorConfig config : runnings) {
			startMonitor(config.getId());
		}
		List<ErrorMessage> runningErrs = errorMessageRepository.findRunning("ping监控");
		for(ErrorMessage err : runningErrs) {
			errors.put(err.getConfigId(), err);
		}
		System.out.println("ping监控就绪");
	}
	
	@PreDestroy
	public void destroy() {
		for(Iterator<Entry<String, ScheduledFuture>> i=futures.entrySet().iterator(); i.hasNext();) {
			Entry<String, ScheduledFuture> entry = i.next();
			entry.getValue().cancel(true);
			i.remove();
		}
		System.out.println("ping监控停止完成");
	}

	@Override
	public void saveConfig(PingMonitorConfig config) {
		if(config.getId()==null || config.getId().trim().equals("")) {
			config.setLastUpdateTime(new Date());
			config.setCreateTime(config.getLastUpdateTime());
			config.setCron(CronUtil.getCronExpression(config));
			pingMonitorConfigRepository.save(config);
		} else {
			PingMonitorConfig currentConfig = pingMonitorConfigRepository.findOne(config.getId());
			if(currentConfig!=null) {
				currentConfig.setName(config.getName());
				currentConfig.setSmsReceivers(config.getSmsReceivers());
				currentConfig.setEmailReceivers(config.getEmailReceivers());
				currentConfig.setErrorContinued(config.getErrorContinued());
				currentConfig.setLastUpdateTime(new Date());
				currentConfig.setRemarks(config.getRemarks());
				currentConfig.setTaskDays(config.getTaskDays());
				currentConfig.setTaskHours(config.getTaskHours());
				currentConfig.setTaskWeeks(config.getTaskWeeks());
				currentConfig.setTaskType(config.getTaskType());				

				currentConfig.setIpAddress(config.getIpAddress());
				currentConfig.setCron(CronUtil.getCronExpression(currentConfig));
				pingMonitorConfigRepository.save(currentConfig);
			}
		}
	}

	@Override
	public List<PingMonitorConfig> findConfig(String vague) {
		if(vague==null || vague.trim().equals("")) {
			return pingMonitorConfigRepository.findAll();
		}
		return pingMonitorConfigRepository.findByVague(vague);
	}

	@Override
	public PingMonitorConfig findConfigById(String id) {
		return pingMonitorConfigRepository.findOne(id);
	}

	@Override
	public void deleteConfig(String id) {
		pingMonitorConfigRepository.delete(id);
	}

	@Override
	public boolean startMonitor(String configId) {
		
		PingMonitorConfig config = pingMonitorConfigRepository.findOne(configId);
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
		pingMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public boolean stopMonitor(String configId) {
		
		PingMonitorConfig config = pingMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			return false;
		}
		
		ScheduledFuture future = futures.get(configId);
		if(future!=null) {
			future.cancel(true);
			futures.remove(configId);
		}
		
		config.setStatus("未运行");
		pingMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public void switchMonitor(String configId) {
		PingMonitorConfig config = pingMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			startMonitor(configId);
		} else if(config.getStatus().equals("监控中")) {
			stopMonitor(configId);
		}
	}
	
	public boolean monitor(String configId) {
		Date checkTime = new Date();
		PingMonitorConfig config = pingMonitorConfigRepository.findOne(configId);
		
		URL url = null;
		HttpURLConnection con = null;
		int state = -1;
		
		String cause = null;
		String summary = null;
		String description = null;
		boolean success = false;
		
		int counts = 0;
		while (counts < config.getErrorContinued()) {
			counts++; 
			int  timeOut =  3000 ;  //超时应该在3钞以上        
			try {
				success = InetAddress.getByName(config.getIpAddress()).isReachable(timeOut);// 当返回值是true时，说明host是可用的，false则不可。
			} catch (Exception e) {
				cause = "设备失去联系";
				summary = config.getName()+"("+config.getIpAddress()+") 出现异常。异常原因："+cause;
				description = summary+"|"+e.getMessage();
				success = false;
			}     
			
		}
		if(!success && cause==null) {
			cause = "设备失去联系";
			summary = config.getName()+"("+config.getIpAddress()+") 出现异常。异常原因："+cause;
			description = summary;
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
				error.setErrorNo("ping_m001");//
				error.setErrorTime(checkTime);
				error.setErrorType("警告");
				error.setMsgSource("监控");
				error.setStatus("发现故障");
				error.setTargetName(config.getName());
				error.setTargetType("ping监控");
				error.setTroubleshooter("ping监控任务");
				error.setErrorIp(config.getIpAddress());
				
				error.setEquipment(config.getBindEquipment());
				
//				errorMessageRepository.save(error);
				errorMessageService.saveAndStartFlow(error);
				errors.put(config.getId(), error);
			}
			
			config.setLastResult("故障");
			
			String message = "【ping监控故障】"+summary + "【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
			
			if(StringUtils.isNotBlank(config.getSmsReceivers())) {
				messageSender.sendSms(message, config.getSmsReceivers());
			}
			if(StringUtils.isNotBlank(config.getEmailReceivers())) {
				messageSender.sendMail("ping监控故障", message, config.getEmailReceivers());
			}
			
		} else {
			if(errors.get(config.getId())!=null) {
				ErrorMessage error = errors.get(config.getId());
				error.setRecoveryTime(checkTime);
				error.setDuration((error.getRecoveryTime().getTime()-error.getErrorTime().getTime())/1000);
				error.setStatus("已恢复");
				
				errorMessageRepository.save(error);
				errors.remove(config.getId());
				
				String message = "【ping监控故障恢复】"+summary + ". 故障已恢复。【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
				
				if(StringUtils.isNotBlank(config.getSmsReceivers())) {
					messageSender.sendSms(message, config.getSmsReceivers());
				}
				if(StringUtils.isNotBlank(config.getEmailReceivers())) {
					messageSender.sendMail("ping监控故障恢复", message, config.getEmailReceivers());
				}
			}
			config.setLastResult("正常");
		}
		pingMonitorConfigRepository.save(config);	
		
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
		PingMonitorConfig config = pingMonitorConfigRepository.findOne(configId);
		config.setBindEquipment(equipment);
		pingMonitorConfigRepository.save(config);
	}

	@Override
	public List<PingMonitorConfig> findSegmentConfig(String fromIp, String toIp) {
		String fromSegment = fromIp.substring(0, fromIp.lastIndexOf("."));
		String toSegment = toIp.substring(0, toIp.lastIndexOf("."));
		if(!fromSegment.equals(toSegment)) {
			return null;
		}
		String fromNumStr = fromIp.substring(fromIp.lastIndexOf(".")+1, fromIp.length());
		String toNumStr = toIp.substring(toIp.lastIndexOf(".")+1, toIp.length());
		int fromNum = Integer.parseInt(fromNumStr);
		int toNum = Integer.parseInt(toNumStr);
		if(fromNum>toNum) {
			return null;
		}
		List<PingMonitorConfig> result = new ArrayList<>();
		for(int i=fromNum; i<=toNum; i++) {
			String ip = fromSegment + "." + i;
			
			if(pingMonitorConfigRepository.findByIpAddress(ip)!=null) {
				continue;
			}

			try {
				if(!InetAddress.getByName(ip).isReachable(500)) {
					continue;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			PingMonitorConfig config = new PingMonitorConfig();
			config.setIpAddress(ip);
			config.setName("ping监控:"+ip);
			config.setTaskType("每半小时");
			result.add(config);
		}
		
		return result;
	}

	@Override
	public String saveConigs(List<PingMonitorConfig> configs) {
		int success = 0;
		for(PingMonitorConfig config : configs) {
			if(pingMonitorConfigRepository.findByIpAddress(config.getIpAddress())==null) {
				pingMonitorConfigRepository.save(config);
				success++;
			}
		}
		return "一共 "+configs.size()+" 个配置,成功添加 "+success+" 个";
	}
	
	

}
