package com.syo.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_monitor_functional")
public class Functional {

	@Id
	@GeneratedValue(generator="assignedGen")
	@GenericGenerator(name="assignedGen", strategy="assigned")
	@Column(length=100)
	private String codeTips;
	
	@Column(length=300)
	private String url;
	
	@Column(length=150)
	private String name;
	
	@Column(length=150)
	private String logName;
	
	@Column(length=100)
	private String description;
	
	private int sort;
	
	@Column(length=5)
	private String type;
	
	@Column(length=5)
	private String status;
	
	@ManyToOne
	@JoinColumn(name="parent")
	private Functional parent;
	
	private boolean logEnable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Functional getParent() {
		return parent;
	}

	public void setParent(Functional parent) {
		this.parent = parent;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getCodeTips() {
		return codeTips;
	}

	public void setCodeTips(String codeTips) {
		this.codeTips = codeTips;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isLogEnable() {
		return logEnable;
	}

	public void setLogEnable(boolean logEnable) {
		this.logEnable = logEnable;
	}
	
	
	
}
