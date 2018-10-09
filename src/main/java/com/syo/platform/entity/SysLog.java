package com.syo.platform.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_monitor_syslog")
public class SysLog {

	@Id
	@GeneratedValue(generator="assignedGen")
	@GenericGenerator(name="assignedGen", strategy="assigned")
	@Column(length=50)
	private String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date logTime;
	
	@ManyToOne
	@JoinColumn(name="user_account")
	private User user;
	
	@Column(length=50)
	private String fromIP;
	
	@Column(length=5)
	private String logType;
	
	@Column(length=1000)
	private String requestURL;
	
	@Column(length=300)
	private String requestMapping;
	
	@Column(length=100)
	private String reponseType;
	
	@Column(length=150)
	private String logName;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String requestParam;
	
	@Column(length=150)
	private String codeTips;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private String logDetail;
	
	@ManyToOne
	@JoinColumn(name="functional")
	private Functional functional;
	
	@Column(length=60)
	private String sessionId;

	@Override
	public String toString() {
		return "SysLog [id=" + id + ", logTime=" + logTime + ", user=" + user.getAccount() + ", fromIP=" + fromIP + ", logType="
				+ logType + ", requestURL=" + requestURL + ", requestMapping=" + requestMapping + ", reponseType="
				+ reponseType + ", logTitle=" + logName + ", requestParam=" + requestParam + ", codeTips=" + codeTips
				+ ", logDetail=" + logDetail + ", functional=" + (functional==null?"null":functional.getUrl()) + ", sessionId=" + sessionId + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFromIP() {
		return fromIP;
	}

	public void setFromIP(String fromIP) {
		this.fromIP = fromIP;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(String requestMapping) {
		this.requestMapping = requestMapping;
	}

	public String getReponseType() {
		return reponseType;
	}

	public void setReponseType(String reponseType) {
		this.reponseType = reponseType;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getCodeTips() {
		return codeTips;
	}

	public void setCodeTips(String codeTips) {
		this.codeTips = codeTips;
	}

	public String getLogDetail() {
		return logDetail;
	}

	public void setLogDetail(String logDetail) {
		this.logDetail = logDetail;
	}

	public Functional getFunctional() {
		return functional;
	}

	public void setFunctional(Functional functional) {
		this.functional = functional;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
	
}
