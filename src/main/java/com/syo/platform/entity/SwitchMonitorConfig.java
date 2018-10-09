package com.syo.platform.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.syo.platform.taskScheduler.CronField;
import com.syo.platform.taskScheduler.CronFieldType;

@Entity
@Table(name="t_monitor_switchconfig")
public class SwitchMonitorConfig {

	@Id
	@GeneratedValue(generator="uuidGen")
	@GenericGenerator(name="uuidGen", strategy="uuid")
	@Column(length=50)
	private String id;
	
	@Column(length=150)
	private String name;//名称
	
	@Column(length=20)
	private String ipAddress;//ip地址
	
	private Integer port;//端口
	
	@Column(length=20)
	private String vendor;
	
	@Column(length=20)
	private String model;
	
	@Column(length=20)
	private String community;
	
	@Column(length=20)
	private String cron;//执行时间表达式
	
	@Column(length=20)
	@CronField(CronFieldType.TASKTYPE)
	private String taskType;
	
	@Column(length=500)
	@CronField(CronFieldType.HOUR)
	private String taskHours;
	
	@Column(length=200)
	@CronField(CronFieldType.WEEK)
	private String taskWeeks;
	
	@Column(length=200)
	@CronField(CronFieldType.DAY)
	private String taskDays;
	
	@Column(length=1000)
	private String emailReceivers;
	
	@Column(length=1000)
	private String smsReceivers;
	
	@Column(length=200)
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_time")
	private Date lastTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="next_time")
	private Date nextTime;
	
	@Column(length=10)
	private String status = "未运行";
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;
	
	@Column(length=10)
	private String lastResult;
	
	@ManyToOne
	@JoinColumn(name="bind_equipment")
	private Equipment bindEquipment;//绑定的设备
	
	@ManyToMany
	@JoinTable(name="t_monitor_switch_oid", joinColumns=@JoinColumn(name="switch_config_id"), inverseJoinColumns=@JoinColumn(name="oid_entry_id"))
	private List<OIDEntry> oidEntries = new ArrayList<>();
	
	private int cpuThreshold=80;//CPU预警阀值 单位：%
	
	private int memoryThreshold=80;//内存预警阀值  单位：%
	
	private int networkThreshold=512;//网络预警阀值  单位：k/s

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskHours() {
		return taskHours;
	}

	public void setTaskHours(String taskHours) {
		this.taskHours = taskHours;
	}

	public String getTaskWeeks() {
		return taskWeeks;
	}

	public void setTaskWeeks(String taskWeeks) {
		this.taskWeeks = taskWeeks;
	}

	public String getTaskDays() {
		return taskDays;
	}

	public void setTaskDays(String taskDays) {
		this.taskDays = taskDays;
	}

	public String getEmailReceivers() {
		return emailReceivers;
	}

	public void setEmailReceivers(String emailReceivers) {
		this.emailReceivers = emailReceivers;
	}

	public String getSmsReceivers() {
		return smsReceivers;
	}

	public void setSmsReceivers(String smsReceivers) {
		this.smsReceivers = smsReceivers;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public Date getNextTime() {
		return nextTime;
	}

	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getLastResult() {
		return lastResult;
	}

	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}

	public Equipment getBindEquipment() {
		return bindEquipment;
	}

	public void setBindEquipment(Equipment bindEquipment) {
		this.bindEquipment = bindEquipment;
	}

	public List<OIDEntry> getOidEntries() {
		return oidEntries;
	}

	public void setOidEntries(List<OIDEntry> oidEntries) {
		this.oidEntries = oidEntries;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public int getCpuThreshold() {
		return cpuThreshold;
	}

	public void setCpuThreshold(int cpuThreshold) {
		this.cpuThreshold = cpuThreshold;
	}

	public int getMemoryThreshold() {
		return memoryThreshold;
	}

	public void setMemoryThreshold(int memoryThreshold) {
		this.memoryThreshold = memoryThreshold;
	}

	public int getNetworkThreshold() {
		return networkThreshold;
	}

	public void setNetworkThreshold(int networkThreshold) {
		this.networkThreshold = networkThreshold;
	}
	
	
	
}
