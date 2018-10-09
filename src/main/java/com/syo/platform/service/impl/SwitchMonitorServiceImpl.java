package com.syo.platform.service.impl;

import java.io.IOException;
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
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.syo.platform.activemq.MonitorMessageSender;
import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.OIDEntry;
import com.syo.platform.entity.SwitchMonitorConfig;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.ErrorMessageRepository;
import com.syo.platform.repository.OIDEntryRepository;
import com.syo.platform.repository.SwitchMonitorConfigRepository;
import com.syo.platform.repository.jpautils.UpdateUtil;
import com.syo.platform.service.ErrorMessageService;
import com.syo.platform.service.SwitchMonitorService;
import com.syo.platform.taskScheduler.CronUtil;

@Service
public class SwitchMonitorServiceImpl implements SwitchMonitorService {
	
	@Autowired
	private SwitchMonitorConfigRepository switchMonitorConfigRepository;
	
	@Autowired
	private OIDEntryRepository oidEntryRepository;
	
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
		List<SwitchMonitorConfig> runnings = switchMonitorConfigRepository.findRunning();
		for(SwitchMonitorConfig config : runnings) {
			startMonitor(config.getId());
		}
		List<ErrorMessage> runningErrs = errorMessageRepository.findRunning("交换机");
		for(ErrorMessage err : runningErrs) {
			errors.put(err.getConfigId(), err);
		}
		System.out.println("交换机监控就绪");
	}
	
	@PreDestroy
	public void destroy() {
		for(Iterator<Entry<String, ScheduledFuture>> i=futures.entrySet().iterator(); i.hasNext();) {
			Entry<String, ScheduledFuture> entry = i.next();
			entry.getValue().cancel(true);
			i.remove();
		}
		System.out.println("交换机监控停止完成");
	}

	@Override
	public void saveConfig(SwitchMonitorConfig config) {
		List<OIDEntry> oidEntries = oidEntryRepository.findByEquipmentTypeAndVendorAndModelsContainingOrderBySort("交换机", config.getVendor(), config.getModel());
		if(config.getId()==null || config.getId().trim().equals("")) {
			config.setLastUpdateTime(new Date());
			config.setCreateTime(config.getLastUpdateTime());
			config.setCron(CronUtil.getCronExpression(config));
			config.setOidEntries(oidEntries);
			switchMonitorConfigRepository.save(config);
		} else {
			try {
				config.setCron(CronUtil.getCronExpression(config));
				config.setOidEntries(oidEntries);
				new UpdateUtil<SwitchMonitorConfig>().update(config, switchMonitorConfigRepository, "createTime");
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public List<SwitchMonitorConfig> findConfig(String vague) {
		if(vague==null || vague.trim().equals("")) {
			return switchMonitorConfigRepository.findAll();
		}
		return switchMonitorConfigRepository.findByVague(vague);
	}

	@Override
	public SwitchMonitorConfig findConfigById(String id) {
		return switchMonitorConfigRepository.findOne(id);
	}

	@Override
	public void deleteConfig(String id) {
		switchMonitorConfigRepository.delete(id);
	}

	@Override
	public boolean startMonitor(String configId) {
		
		SwitchMonitorConfig config = switchMonitorConfigRepository.findOne(configId);
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
		switchMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public boolean stopMonitor(String configId) {
		
		SwitchMonitorConfig config = switchMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			return false;
		}
		
		ScheduledFuture future = futures.get(configId);
		if(future!=null) {
			future.cancel(true);
			futures.remove(configId);
		}
		
		config.setStatus("未运行");
		switchMonitorConfigRepository.save(config);
		
		return true;
	}

	@Override
	public void switchMonitor(String configId) {
		SwitchMonitorConfig config = switchMonitorConfigRepository.findOne(configId);
		if(config.getStatus().equals("未运行")) {
			startMonitor(configId);
		} else if(config.getStatus().equals("监控中")) {
			stopMonitor(configId);
		}
	}

	@Override
	public boolean monitor(String configId) {
		Date checkTime = new Date();
		
		String cause = null;
		String summary = null;
		String description = null;
		boolean success = true;
		
		SwitchMonitorConfig config = switchMonitorConfigRepository.findOne(configId);
		
		//设定CommunityTarget   
        CommunityTarget communityTarget = new CommunityTarget();
        //定义本机的地址   
        Address localAdd = GenericAddress.parse("udp:"+config.getIpAddress()+"/"+config.getPort());
        //设定本地主机的地址
        communityTarget.setAddress(localAdd);
        //设置snmp共同体   
        communityTarget.setCommunity(new OctetString(config.getCommunity())); 
        //设置超时重试次数   
        communityTarget.setRetries(2); 
        //设置超时的时间  
        communityTarget.setTimeout(5*60);
        //设置使用的snmp版本 
        communityTarget.setVersion(SnmpConstants.version2c);  
        
        TransportMapping transport = null;
        
        try {
        	 //设定采取的协议  
			transport = new DefaultUdpTransportMapping();//设定传输协议为UDP
			//调用TransportMapping中的listen()方法，启动监听进程，接收消息，由于该监听进程是守护进程，最后应调用close()方法来释放该进程
            transport.listen();  
            //创建SNMP对象，用于发送请求PDU
            Snmp protocol = new Snmp(transport);  
            //创建请求pdu,获取mib   
            PDU request = new PDU(); 
			
			List<OIDEntry> oidEntries = config.getOidEntries();
			Map<String, OIDEntry> entryMap = new HashMap<>();
	        for(OIDEntry entry : oidEntries) {
	        	entryMap.put(entry.getOid(), entry);
	        	//调用的add方法绑定要查询的OID
	            request.add(new VariableBinding(new OID(entry.getOid())));
	        }
	        
	        //调用setType()方法来确定该pdu的类型
            request.setType(PDU.GETNEXT);
            //调用 send(PDU pdu,Target target)发送pdu，返回一个ResponseEvent对象
            ResponseEvent responseEvent = protocol.send(request, communityTarget);  
            //通过ResponseEvent对象来获得SNMP请求的应答pdu，方法：public PDU getResponse()
            PDU response=responseEvent.getResponse();
            
            //输出   
            if(response != null){  
            	for(int i=0; i<response.size(); i++) {
            		VariableBinding vb = response.get(i);
            		OIDEntry entry = entryMap.get(vb.getOid());
            		if(entry.getEntryKey()==null) {
            			continue;
            		}
            		
            		if("风扇".equals(entry.getEntryKey())) {
            			if(vb.getVariable().toString().equals("2")) {
            				success = false;
            				cause = entry.getErrorCause();
            				summary = config.getName()+" 交换机监控出现异常。异常原因："+cause;
            				description = summary;
            				break;
            			}
            		}
            		
            		if("cpu".equals(entry.getEntryKey())) {
            			if(Integer.parseInt(vb.getVariable().toString())>=config.getCpuThreshold()) {
            				success = false;
            				cause = entry.getErrorCause();
            				summary = config.getName()+" 交换机监控出现异常。异常原因："+cause;
            				description = summary;
            				break;
            			}
            		}

            	}
            } else {
            	throw new Exception("没有获取到交换机监控的相关信息");
            }
			
		} catch (Exception e) {
			success = false;
			cause = "获取监控信息失败";
			summary = config.getName()+" 交换机监控出现异常。异常原因："+cause;
			description = summary+"|"+e.getMessage();			
		} finally {
			if(transport!=null) {
				try {
					transport.close();
				} catch (IOException e) {
					e.printStackTrace();
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
				error.setErrorNo("switch_m001");//
				error.setErrorTime(checkTime);
				error.setErrorType("警告");
				error.setMsgSource("监控");
				error.setStatus("发现故障");
				error.setTargetName(config.getName());
				error.setTargetType("交换机");
				error.setTroubleshooter("交换机监控任务");
				error.setErrorIp(config.getIpAddress());
				
				error.setEquipment(config.getBindEquipment());
				
//				errorMessageRepository.save(error);
				errorMessageService.saveAndStartFlow(error);
				errors.put(config.getId(), error);
			}
			
			config.setLastResult("故障");
			
			String message = "【交换机故障】"+summary + "【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
			if(StringUtils.isNotBlank(config.getSmsReceivers())) {
				messageSender.sendSms(message, config.getSmsReceivers());
			}
			if(StringUtils.isNotBlank(config.getEmailReceivers())) {
				messageSender.sendMail("交换机故障", message, config.getEmailReceivers());
			}
		} else {
			if(errors.get(config.getId())!=null) {
				ErrorMessage error = errors.get(config.getId());
				error.setRecoveryTime(checkTime);
				error.setDuration((error.getRecoveryTime().getTime()-error.getErrorTime().getTime())/1000);
				error.setStatus("已恢复");
				
				errorMessageRepository.save(error);
				errors.remove(config.getId());
				
				String message = "【交换机故障恢复】"+summary + ". 故障已恢复。【检查时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(checkTime)+"】";
				if(StringUtils.isNotBlank(config.getSmsReceivers())) {
					messageSender.sendSms(message, config.getSmsReceivers());
				}
				if(StringUtils.isNotBlank(config.getEmailReceivers())) {
					messageSender.sendMail("交换机故障恢复", message, config.getEmailReceivers());
				}
			}
			config.setLastResult("正常");
		}
		switchMonitorConfigRepository.save(config);	
		
		return success;
	}

	@Override
	public void saveBind(String configId, String equipmentId) {
		Equipment equipment = equipmentRepository.findOne(equipmentId);
		SwitchMonitorConfig config = switchMonitorConfigRepository.findOne(configId);
		config.setBindEquipment(equipment);
		switchMonitorConfigRepository.save(config);
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

}
