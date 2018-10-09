package com.syo.platform.dto;

import java.util.Date;

public class ErrorQueryDTO {

	private String troubleshooter = "";
	
	private String msgSource = ""; //故障信息来源：监控，申报，读者 //申告单号
	
	private String declareType = "";//申报方式//申告来源
	
	private String summary = "";//申告情况
	
	private int errorLevel;//紧急情况
	
	private String status = "";//申告处理
	
	private Date start;
	
	private Date end;

	public String getTroubleshooter() {
		return troubleshooter;
	}

	public void setTroubleshooter(String troubleshooter) {
		this.troubleshooter = troubleshooter;
	}

	public String getMsgSource() {
		return msgSource;
	}

	public void setMsgSource(String msgSource) {
		this.msgSource = msgSource;
	}

	public String getDeclareType() {
		return declareType;
	}

	public void setDeclareType(String declareType) {
		this.declareType = declareType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
