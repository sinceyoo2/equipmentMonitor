package com.syo.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_monitor_oidentry")
public class OIDEntry {
	
	@Id
	@GeneratedValue(generator="uuidGen")
	@GenericGenerator(name="uuidGen", strategy="uuid")
	@Column(length=50)
	private String id;
	
	@Column(length=100)
	private String oid;
	
	@Column(length=50)
	private String name;
	
	@Column(length=250)
	private String description;

	@Column(length=50)
	private String equipmentType;
	
	@Column(length=20)
	private String vendor;
	
	@Column(length=2000)
	private String models;
	
	private int sort;
	
	private boolean enable;
	
	@Column(length=20)
	private String entryKey;
	
	@Column(length=100)
	private String errorCause;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

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

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModels() {
		return models;
	}

	public void setModels(String models) {
		this.models = models;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getEntryKey() {
		return entryKey;
	}

	public void setEntryKey(String entryKey) {
		this.entryKey = entryKey;
	}

	public String getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
	
	
}
