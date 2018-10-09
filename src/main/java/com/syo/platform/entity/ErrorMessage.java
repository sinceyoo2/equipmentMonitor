package com.syo.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_monitor_errormsg")
public class ErrorMessage {

	@Id
	@GeneratedValue(generator="assignedGen")
	@GenericGenerator(name="assignedGen", strategy="assigned")
	@Column(length=50)
	private String id;
	
	@Column(length=20)
	private String errorNo;
	
	@Column(length=20)
	private String msgSource; //故障信息来源：监控，申报，读者
	
	@Column(length=20)
	private String troubleshooter;//上报者，可能用户，也可能是程序
	
	@Column(length=20)
	private String declareType;//申报方式
	
	@Column(length=20)
	private String department;//申报单位
	
	@Column(length=10)
	private String errorType;//警告、错误
	
	private int errorLevel;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="error_time")
	private Date errorTime;//故障发生时间
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="recovery_time")
	private Date recoveryTime;//故障恢复时间
	
	private Long duration = Long.MIN_VALUE;//持续时间 秒
	
	@Column(length=5)
	private String status;
	
	@Column(length=500)
	private String description;
	
	@Column(length=50)
	private String targetName;//目标设备名称
	
	@Column(length=20)
	private String targetType;
	
	@Column(length=20)
	private String cause;
	
	@Column(length=50)
	private String configId;
	
	@Column(length=100)
	private String summary;
	
	@Column(length=50)
	private String emergency = "一般";
	
	@ManyToOne
	@JoinColumn(name="equipment")
	private Equipment equipment;
	
	@Column(length=20)
	private String errorIp;
	
	private boolean hashWorkSheet;
	
	@Column(length=20)
	private String workSheetStatus;
	
	@JsonIgnore
	@OneToOne(mappedBy = "errorMessage")
	private WorkSheet workSheet;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(String errorNo) {
		this.errorNo = errorNo;
	}

	public String getMsgSource() {
		return msgSource;
	}

	public void setMsgSource(String msgSource) {
		this.msgSource = msgSource;
	}

	public String getTroubleshooter() {
		return troubleshooter;
	}

	public void setTroubleshooter(String troubleshooter) {
		this.troubleshooter = troubleshooter;
	}

	public String getDeclareType() {
		return declareType;
	}

	public void setDeclareType(String declareType) {
		this.declareType = declareType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public int getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	public Date getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(Date errorTime) {
		this.errorTime = errorTime;
	}

	public Date getRecoveryTime() {
		return recoveryTime;
	}

	public void setRecoveryTime(Date recoveryTime) {
		this.recoveryTime = recoveryTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	@Override
	public String toString() {
		return "ErrorMessage [id=" + id + ", errorNo=" + errorNo + ", msgSource=" + msgSource + ", troubleshooter="
				+ troubleshooter + ", declareType=" + declareType + ", department=" + department + ", erroType="
				+ errorType + ", errorLevel=" + errorLevel + ", errorTime=" + errorTime + ", recoveryTime="
				+ recoveryTime + ", duration=" + duration + ", status=" + status + ", description=" + description
				+ ", targetName=" + targetName + ", targetType=" + targetType + ", cause=" + cause + ", configId="
				+ configId + ", summary=" + summary + "]";
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public WorkSheet getWorkSheet() {
		return workSheet;
	}

	public void setWorkSheet(WorkSheet workSheet) {
		this.workSheet = workSheet;
	}

	public String getWorkSheetStatus() {
		return workSheetStatus;
	}

	public void setWorkSheetStatus(String workSheetStatus) {
		this.workSheetStatus = workSheetStatus;
	}

	public boolean isHashWorkSheet() {
		return hashWorkSheet;
	}

	public void setHashWorkSheet(boolean hashWorkSheet) {
		this.hashWorkSheet = hashWorkSheet;
	}

	public String getErrorIp() {
		return errorIp;
	}

	public void setErrorIp(String errorIp) {
		this.errorIp = errorIp;
	}
	
}
