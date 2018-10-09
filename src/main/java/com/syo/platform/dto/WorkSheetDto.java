package com.syo.platform.dto;

import java.util.Date;

public class WorkSheetDto {
	
	private String vague;
	
	private String status;
	
	private Date start;
	
	private Date end;

	private Boolean hasFiled;

	public String getVague() {
		return vague;
	}

	public void setVague(String vague) {
		this.vague = vague;
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

	public Boolean getHasFiled() {
		return hasFiled;
	}

	public void setHasFiled(Boolean hasFiled) {
		this.hasFiled = hasFiled;
	}
	
	

}
