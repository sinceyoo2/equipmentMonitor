package com.syo.platform.entity;

import java.util.Date;

public class HostErrorMessage {

	private String id;
	
	private String configId;
	
	private String msgSource;
	
	private String troubleshooter;
	
	private Date time;
	
	private long timestamp;
	
	private String msgType;
	
	private String infoType;
	
	private int errorLevel;
	
	private String hostName;
	
	private String ipAddress;
	
	private Integer port;
	
	private String errorName;
	
	private String smsReceivers;
	
	private String emailReceivers;
	
	private String shortMsg;
	
	private String msg;
	
	private String batchId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsgSource() {
		return msgSource;
	}

	public void setMsgSource(String msgSource) {
		this.msgSource = msgSource;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
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

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public String getSmsReceivers() {
		return smsReceivers;
	}

	public void setSmsReceivers(String smsReceivers) {
		this.smsReceivers = smsReceivers;
	}

	public String getEmailReceivers() {
		return emailReceivers;
	}

	public void setEmailReceivers(String emailReceivers) {
		this.emailReceivers = emailReceivers;
	}

	public String getShortMsg() {
		return shortMsg;
	}

	public void setShortMsg(String shortMsg) {
		this.shortMsg = shortMsg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTroubleshooter() {
		return troubleshooter;
	}

	public void setTroubleshooter(String troubleshooter) {
		this.troubleshooter = troubleshooter;
	}

	public int getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	@Override
	public String toString() {
		return "HostErrorMessage [id=" + id + ", configId=" + configId + ", msgSource=" + msgSource
				+ ", troubleshooter=" + troubleshooter + ", time=" + time + ", timestamp=" + timestamp + ", msgType="
				+ msgType + ", infoType=" + infoType + ", errorLevel=" + errorLevel + ", hostName=" + hostName
				+ ", ipAddress=" + ipAddress + ", port=" + port + ", errorName=" + errorName + ", smsReceivers="
				+ smsReceivers + ", emailReceivers=" + emailReceivers + ", shortMsg=" + shortMsg + ", msg=" + msg
				+ ", batchId=" + batchId + "]";
	}
	
}
