package com.syo.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.syo.platform.converter.JsonDateDeserializer;

@Entity
@Table(name="T_APP_SYSTEM_LOG")
public class AppSystemLog {
	
	@Id
	@GeneratedValue(generator="assignedGen")
	@GenericGenerator(name="assignedGen", strategy="uuid")
	@Column(name="INSID", length=40)
	@JsonProperty("INSID")
	private String insid;
	
	@Column(name="BUSI_SYSTEM_NAME", length=200)
	@JsonProperty("BUSI_SYSTEM_NAME")
	private String busiSystemName;
	
	@Column(name="DEPTNAME", length=100)
	@JsonProperty("DEPTNAME")
	private String deptName;
	
	@Column(name="USERNAME", length=50)
	@JsonProperty("USERNAME")
	private String userName;
	
	@Column(name="USERID", length=50)
	@JsonProperty("USERID")
	private String userId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OPERATE_TIME")
	@JsonProperty("OPERATE_TIME")
	private Date operateTime;
	
	@Column(name="OPERATE_IP", length=50)
	@JsonProperty("OPERATE_IP")
	private String operateIP;
	
	@Column(name="OPERATE_MODULE", length=100)
	@JsonProperty("OPERATE_MODULE")
	private String operateModule;
	
	@Column(name="OPERATE_TYPE", length=100)
	@JsonProperty("OPERATE_TYPE")
	private String operateType;
	
	@Column(name="OPERATE_DESC", length=200)
	@JsonProperty("OPERATE_DESC")
	private String operateDesc;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INSERT_TIME")
	@JsonProperty("INSERT_TIME")
	private Date insertTime = new Date();

	public String getInsid() {
		return insid;
	}

	public void setInsid(String insid) {
		this.insid = insid;
	}

	public String getBusiSystemName() {
		return busiSystemName;
	}

	public void setBusiSystemName(String busiSystemName) {
		this.busiSystemName = busiSystemName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateIP() {
		return operateIP;
	}

	public void setOperateIP(String operateIP) {
		this.operateIP = operateIP;
	}

	public String getOperateModule() {
		return operateModule;
	}

	public void setOperateModule(String operateModule) {
		this.operateModule = operateModule;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getOperateDesc() {
		return operateDesc;
	}

	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
		
}


