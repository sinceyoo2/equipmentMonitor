package com.syo.platform.dto;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syo.platform.converter.JsonDateDeserializer;
import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.ErrorMessage;


public class ErrorAPIDTO {

	private String id;
	
	private String errorNo; //故障编号
	
	private String msgSource; //故障信息来源：监控，申报，读者
	
	private String troubleshooter;//上报者，可能用户，也可能是程序

	private String declareType;//申报方式
	
	private String department;//申报单位
	
	private String errorType;//警告、错误
	
	private int errorLevel;
	
	@JsonDeserialize(using=JsonDateDeserializer.class)
	private Date errorTime;//故障发生时间

	private String status = "发现故障";//发现故障，处理中，	已恢复

	private String description;	//详细描述
	
	private String targetName;//故障主体。目标设备名称

	private String targetType;//故障主体类型
	
	private String cause;//故障原因
	
	private String summary;//简要描述
	
	private String emergency = "一般";//紧急程度
	
	private String equipmentId;
	
	public ErrorMessage createErrorMessage() {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setId(this.id);
		errorMessage.setErrorNo(this.errorNo);
		errorMessage.setTroubleshooter(troubleshooter);
		errorMessage.setDeclareType(this.declareType);
		errorMessage.setDepartment(this.department);
		
		if(!"警告".equals(this.errorType) && !"错误".equals(this.errorType)) {
			errorMessage.setErrorType("警告");
		} else {
			errorMessage.setErrorType(this.errorType);
		}
		
		if(this.errorLevel>0 && this.errorLevel<=3) {
			errorMessage.setErrorLevel(this.errorLevel);
		} else {
			errorMessage.setErrorLevel(1);
		}
		
		if(StringUtils.isNotBlank(equipmentId)) {
			Equipment equipment = new Equipment();
			equipment.setId(equipmentId);
			errorMessage.setEquipment(equipment);
		}
		
		errorMessage.setErrorTime(this.errorTime);
		
		errorMessage.setStatus(this.status);
		errorMessage.setDescription(this.description);
		errorMessage.setTargetName(this.targetName);
		errorMessage.setTargetType(this.targetType);
		errorMessage.setCause(this.cause);
		errorMessage.setSummary(this.summary);
		
		if(!"一般".equals(this.emergency) && !"特急".equals(this.emergency) && !"加急".equals(this.emergency)) {
			errorMessage.setEmergency("一般");
		} else {
			errorMessage.setEmergency(this.emergency);
		}
		
		return errorMessage;
		
	}

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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	
	
}
