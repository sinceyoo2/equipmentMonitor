package com.syo.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.syo.platform.taskScheduler.CronField;
import com.syo.platform.taskScheduler.CronFieldType;

@Entity
@Table(name="t_monitor_middlewareconfig")
public class MiddlewareMonitorConfig {

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
	private String serviceName;
	
	private int errorContinued = 3;//连续多少次失败认为故障
	
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getErrorContinued() {
		return errorContinued;
	}

	public void setErrorContinued(int errorContinued) {
		this.errorContinued = errorContinued;
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

	@Override
	public String toString() {
		return "MiddlewareConfig [id=" + id + ", name=" + name + ", ipAddress=" + ipAddress + ", port=" + port
				+ ", vendor=" + vendor + ", serviceName=" + serviceName + ", errorContinued=" + errorContinued
				+ ", cron=" + cron + ", taskType=" + taskType + ", taskHours=" + taskHours + ", taskWeeks=" + taskWeeks
				+ ", taskDays=" + taskDays + ", emailReceivers=" + emailReceivers + ", smsReceivers=" + smsReceivers
				+ ", remarks=" + remarks + ", createTime=" + createTime + ", lastTime=" + lastTime + ", nextTime="
				+ nextTime + ", status=" + status + ", lastUpdateTime=" + lastUpdateTime + ", lastResult=" + lastResult
				+ "]";
	}

	public Equipment getBindEquipment() {
		return bindEquipment;
	}

	public void setBindEquipment(Equipment bindEquipment) {
		this.bindEquipment = bindEquipment;
	}
	
}
