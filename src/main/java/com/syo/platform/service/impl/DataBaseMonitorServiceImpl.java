package com.syo.platform.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.syo.platform.activemq.MonitorMessageSender;
import com.syo.platform.entity.DataBaseMonitorConfig;
import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.MiddlewareMonitorConfig;
import com.syo.platform.repository.DataBaseMonitorConfigRepository;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.service.DataBaseMonitorService;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.taskScheduler.CronUtil;

@Service
public class DataBaseMonitorServiceImpl implements DataBaseMonitorService {

	@Autowired
	private DataBaseMonitorConfigRepository dataBaseMonitorConfigRepository;
	
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
	
	@Value("${dbMonitor.driver.oracle}")
	private String oracleDriver;
	
	@Value("${dbMonitor.driver.mysql}")
	private String mysqlDriver;
	
	@Value("${dbMonitor.driver.sql_server}")
	private String sqlServerDriver;
	
	@Value("${dbMonitor.url.oracle}")
	private String oracleURL;
	
	@Value("${dbMonitor.url.mysql}")
	private String mysqlURL;
	
	@Value("${dbMonitor.url.sql_server}")
	private String sqlServerURL;
	
	@PostConstruct
	public void init() {
		//恢复正在执行的任务
		List<DataBaseMonitorConfig> runnings = dataBaseMonitorConfigRepository.findRunning();
		for(DataBaseMonitorConfig config : runnings) {
			startMonitor(config.getId());
		}
		List<ErrorMessage> runningErrs = errorMessageRepository.findRunning("数据库");
		for(ErrorMessage err : runningErrs) {
			errors.put(err.getConfigId(), err);
		}
		System.out.println("数据库监控就绪");
	}
	
	@PreDestroy
	public void destroy() {
		for(Iterator<Entry<String, ScheduledFuture>> i=futures.entrySet().iterator(); i.hasNext();) {
			Entry<String, ScheduledFuture> entry = i.next();
			entry.getValue().cancel(true);
			i.remove();
		}
		System.out.println("数据库监控停止完成");
	}
	
	@Override
	public void saveConfig(DataBaseMonitorConfig config) {
		if(config.getId()==null || config.getId().trim().equals("")) {
			config.setLastUpdateTime(new Date());
			config.setCreateTime(config.getLastUpdateTime());
			initDriverAndURL(config);
			config.setCron(CronUtil.getCronExpression(config));
			dataBaseMonitorConfigRepository.save(config);
		} else {
			DataBaseMonitorConfig currentConfig = dataBaseMonitorConfigRepository.findOne(config.getId());
			if(currentConfig!=null) {
				currentConfig.setName(config.getName());
//				currentConfig.setCron(config.getCron());
				currentConfig.setDbDriver(config.getDbDriver());
				currentConfig.setDbName(config.getDbName());
				currentConfig.setDbPassword(config.getDbPassword());
				currentConfig.setDbPort(config.getDbPort());
				currentConfig.setDbUser(config.getDbUser());
				currentConfig.setDbVendor(config.getDbVendor());
				currentConfig.setSmsReceivers(config.getSmsReceivers());
				currentConfig.setEmailReceivers(config.getEmailReceivers());
				currentConfig.setErrorContinued(config.getErrorContinued());
				currentConfig.setIpAddress(config.getIpAddress());
				currentConfig.setLastUpdateTime(new Date());
				currentConfig.setRemarks(config.getRemarks());
				currentConfig.setTaskDays(config.getTaskDays());
				currentConfig.setTaskHours(config.getTaskHours());
				currentConfig.setTaskWeeks(config.getTaskWeeks());
				currentConfig.setTaskType(config.getTaskType());
				initDriverAndURL(currentConfig);
				currentConfig.setCron(CronUtil.getCronExpression(currentConfig));
				dataBaseMonitorConfigRepository.save(currentConfig);
			}
		}
		//dataBaseMonitorConfigRepository.save(config);
	}
	
	private void initDriverAndURL(DataBaseMonitorConfig config) {
		if(config.getDbVendor().equals("MySQL")) {
			config.setDbDriver(mysqlDriver);
			config.setDbURL(mysqlURL.replace("<ip>", config.getIpAddress()).replace("<port>", config.getDbPort()+"").replace("<dbname>", config.getDbName()));
		} else if(config.getDbVendor().equals("SQL Server")) {
			config.setDbDriver(sqlServerDriver);
			config.setDbURL(sqlServerURL.replace("<ip>", config.getIpAddress()).replace("<port>", config.getDbPort()+"").replace("<dbname>", config.getDbName()));
		} else if(config.getDbVendor().equals("Oracle")) {
			config.setDbDriver(oracleDriver);
			config.setDbURL(oracleURL.replace("<ip>", config.getIpAddress()).replace("<port>", config.getDbPort()+"").replace("<dbname>", config.getDbName()));
		}
	}

	@Override
	public List<DataBaseMonitorConfig> findConfig(String vague) {
		if(vague==null || vague.trim().equals("")) {
			return dataBaseMonitorConfigRepository.findAll();
		}
		return dataBaseMonitorConfigRepository.findByVague(vague);
	}

	@Override
	public DataBaseMonitorConfig findConfigById(String id) {
		return dataBaseMonitorConfigRepository.findOne(id);
	}


	@Override
	public void deleteConfig(String id) {
		dataBaseMonitorConfigRepository.delete(id);
	}
	
	@Override
	public String testDB(DataBaseMonitorConfig config) {
		initDriverAndURL(config);
		Connection conn = null;
		String result = null;
		try {
			Class.forName(config.getDbDriver());
			conn = DriverManager.getConnection(config.getDbURL(), config.getDbUser(), config.getDbPassword());
			result = "连接成功";
		} catch (Exception e) {
			result = "连接失败\n"+e.getMessage();		
			if(e instanceof SQLException) {
				System.out.println(((SQLException)e).getErrorCode()+"----------"+((SQLException)e).getSQLState());
			}
//			if("Oracle".equals(config.getDbVendor())) {
//				int separatorIndex = config.getDbURL().lastIndexOf(":");
//				String sidUrl = config.getDbURL().substring(0, separatorIndex)+"/"+config.getDbURL().substring(separatorIndex+1);
//				try {
//					conn = DriverManager.getConnection(sidUrl, config.getDbUser(), config.getDbPassword());
//					result = "连接成功";
//				} catch (SQLException e1) {
//					result = "连接失败\n"+e1.getMessage();			
//				}
//			}
		} finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	
	@Override
	public boolean startMonitor(String configId) {
		
		DataBaseMonitorConfig config = dataBaseMonitorConfigRepository.findOne(configId);
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
		
		//String cron = CronUtil.getCronExpression(config);
		future = threadPoolTaskScheduler.schedule(new Monitor(config.getId()), new CronTrigger(config.getCron()));
		futures.put(configId, future);
//		System.out.println(cron);
		
		config.setStatus("监控中");
		dataBaseMonitorConfigRepository.save(config);
		
		return true;
		
	}
	
	@Override
	public boolean stopMonitor(String configId) {
		
		DataBaseMonitorConfig config = dataBaseMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			return false;
		}
		
		ScheduledFuture future = futures.get(configId);
		if(future!=null) {
			future.cancel(true);
			futures.remove(configId);
		}
		
		config.setStatus("未运行");
		dataBaseMonitorConfigRepository.save(config);

		return true;
		
	}
	
	@Override
	public void switchMonitor(String configId) {
		DataBaseMonitorConfig config = dataBaseMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			startMonitor(configId);
		} else if(config.getStatus().equals("监控中")) {
			stopMonitor(configId);
		}
	}
	
	public boolean monitor(String configId) {
		Date checkTime = new Date();
		DataBaseMonitorConfig config = dataBaseMonitorConfigRepository.findOne(configId);
		
		String cause = null;
		String summary = null;
		String description = null;
		boolean success = false;
		
		for(int i=0; i<config.getErrorContinued(); i++) {
			Connection conn = null;
			try {
				Class.forName(config.getDbDriver());
				conn = DriverManager.getConnection(config.getDbURL(), config.getDbUser(), config.getDbPassword());
				success = true;
			} catch (ClassNotFoundException e) {
				cause = "监控配置错误,数据库驱动名不正确";
				summary = config.getIpAddress()+":"+config.getDbPort()+"/"+config.getDbName()+"数据库出现异常。异常原因："+cause;
				description = summary+"|"+e.getMessage();
				
			} catch (SQLException e) {
				cause = "无法获取数据库连接";
				summary = config.getIpAddress()+":"+config.getDbPort()+"/"+config.getDbName()+"数据库出现异常。异常原因："+cause;
				description = summary+"|"+e.getMessage();
				
//				if("Oracle".equals(config.getDbVendor())) {
//					int separatorIndex = config.getDbURL().lastIndexOf(":");
//					String sidUrl = config.getDbURL().substring(0, separatorIndex)+"/"+config.getDbURL().substring(separatorIndex+1);
//					try {
//						conn = DriverManager.getConnection(sidUrl, config.getDbUser(), config.getDbPassword());
//						success = true;
//					} catch (SQLException e1) {
//						cause = "无法获取数据库连接";
//						summary = config.getIpAddress()+":"+config.getDbPort()+"/"+config.getDbName()+"数据库出现异常。异常原因："+cause;
//						description = summary+"|"+e.getMessage();
//					}
//				}
			} catch (Exception e) {
				cause = "无法获取数据库连接";
				summary = config.getIpAddress()+":"+config.getDbPort()+"/"+config.getDbName()+"数据库出现异常。异常原因："+cause;
				description = summary+"|"+e.getMessage();
			} finally {
				if(conn!=null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			if(success) {
				break;
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
				error.setErrorNo("db_m001");//
				error.setErrorTime(checkTime);
				error.setErrorType("警告");
				error.setMsgSource("监控");
				error.setStatus("发现故障");
				error.setTargetName(config.getName());
				error.setTargetType("数据库");
				error.setTroubleshooter("数据库监控任务");
				error.setErrorIp(config.getIpAddress());
				
				error.setEquipment(config.getBindEquipment());
				
//				errorMessageRepository.save(error);
				errorMessageService.saveAndStartFlow(error);
				errors.put(config.getId(), error);
			}
			
			config.setLastResult("故障");
			
			String message = "【数据库故障】"+summary + "【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
//			System.out.println(message);
			
			if(StringUtils.isNotBlank(config.getSmsReceivers())) {
				messageSender.sendSms(message, config.getSmsReceivers());
			}
			if(StringUtils.isNotBlank(config.getEmailReceivers())) {
				messageSender.sendMail("数据库故障", message, config.getEmailReceivers());
			}
		} else {
			if(errors.get(config.getId())!=null) {
				ErrorMessage error = errors.get(config.getId());
				error.setRecoveryTime(checkTime);
				error.setDuration((error.getRecoveryTime().getTime()-error.getErrorTime().getTime())/1000);
				error.setStatus("已恢复");
				
				errorMessageRepository.save(error);
				errors.remove(config.getId());
				
				String message = "【数据库故障恢复】"+summary + ". 故障已恢复。【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
//				System.out.println(message);
				
				if(StringUtils.isNotBlank(config.getSmsReceivers())) {
					messageSender.sendSms(message, config.getSmsReceivers());
				}
				if(StringUtils.isNotBlank(config.getEmailReceivers())) {
					messageSender.sendMail("数据库故障恢复", message, config.getEmailReceivers());
				}
			}
			config.setLastResult("正常");
		}
		dataBaseMonitorConfigRepository.save(config);	
		
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
		DataBaseMonitorConfig config = dataBaseMonitorConfigRepository.findOne(configId);
		config.setBindEquipment(equipment);
		dataBaseMonitorConfigRepository.save(config);
	}


}
