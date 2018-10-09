package com.syo.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_monitor_library")
public class Library {

	@Id
	@GeneratedValue(generator="assignedGen")
	@GenericGenerator(name="assignedGen", strategy="assigned")
	@Column(length=50)
	private String libNo;
	
	@Column(length=50)
	private String libCode;
	
	@Column(length=50)
	private String libName;
	
	@Column(length=10)
	private String libType;
	
	@Column(length=150)
	private String address;
	
	@Column(length=40)
	private String hotline;
	
	@Column(length=50)
	private String website;
	
	@Column(length=200)
	private String businessHour;
	
	@Column(length=300)
	private String trafficTips;
	
	@Column(length=500)
	private String profiles;
	
	private double longitude;//经度
	
	private double latitude;//纬度
	
	private boolean isIntelligentLibrary; //是否智能图书馆
	
	private boolean isMaster;
	
	private boolean hasEngineRoom;
	
	private int certificateCount=-1;//办证数
	
	private int circulateCount=-1;//流通量
	
	private int visitorCount=-1;//人流量
	
	private int collectCount=-1;//馆藏量
	
	private String selfBARStatus;//自助借还机状态
	
	private String diskRecorderStatus;//硬盘录像机状态
	
	private String entranceGuardStatus;//门禁状态
	
	private String touchScreenStatus;//触屏一体机状态
	
	private String temperatureSensorStatus;//温度感应器状态
	
	private String sensorStatus;//传感器状态 

	public String getLibCode() {
		return libCode;
	}

	public void setLibCode(String libCode) {
		this.libCode = libCode;
	}

	public String getLibName() {
		return libName;
	}

	public void setLibName(String libName) {
		this.libName = libName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBusinessHour() {
		return businessHour;
	}

	public void setBusinessHour(String businessHour) {
		this.businessHour = businessHour;
	}

	public String getTrafficTips() {
		return trafficTips;
	}

	public void setTrafficTips(String trafficTips) {
		this.trafficTips = trafficTips;
	}

	public String getProfiles() {
		return profiles;
	}

	public void setProfiles(String profiles) {
		this.profiles = profiles;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLibNo() {
		return libNo;
	}

	public void setLibNo(String libNo) {
		this.libNo = libNo;
	}

	public boolean isIntelligentLibrary() {
		return isIntelligentLibrary;
	}

	public void setIntelligentLibrary(boolean isIntelligentLibrary) {
		this.isIntelligentLibrary = isIntelligentLibrary;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public boolean isHasEngineRoom() {
		return hasEngineRoom;
	}

	public void setHasEngineRoom(boolean hasEngineRoom) {
		this.hasEngineRoom = hasEngineRoom;
	}
	
	

	public int getCertificateCount() {
		return certificateCount;
	}

	public void setCertificateCount(int certificateCount) {
		this.certificateCount = certificateCount;
	}

	public int getCirculateCount() {
		return circulateCount;
	}

	public void setCirculateCount(int circulateCount) {
		this.circulateCount = circulateCount;
	}

	public int getVisitorCount() {
		return visitorCount;
	}

	public void setVisitorCount(int visitorCount) {
		this.visitorCount = visitorCount;
	}

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	public String getSelfBARStatus() {
		return selfBARStatus;
	}

	public void setSelfBARStatus(String selfBARStatus) {
		this.selfBARStatus = selfBARStatus;
	}

	public String getDiskRecorderStatus() {
		return diskRecorderStatus;
	}

	public void setDiskRecorderStatus(String diskRecorderStatus) {
		this.diskRecorderStatus = diskRecorderStatus;
	}

	public String getEntranceGuardStatus() {
		return entranceGuardStatus;
	}

	public void setEntranceGuardStatus(String entranceGuardStatus) {
		this.entranceGuardStatus = entranceGuardStatus;
	}

	public String getTouchScreenStatus() {
		return touchScreenStatus;
	}

	public void setTouchScreenStatus(String touchScreenStatus) {
		this.touchScreenStatus = touchScreenStatus;
	}

	public String getTemperatureSensorStatus() {
		return temperatureSensorStatus;
	}

	public void setTemperatureSensorStatus(String temperatureSensorStatus) {
		this.temperatureSensorStatus = temperatureSensorStatus;
	}

	public String getSensorStatus() {
		return sensorStatus;
	}

	public void setSensorStatus(String sensorStatus) {
		this.sensorStatus = sensorStatus;
	}

	@Override
	public String toString() {
		return "Library [libNo=" + libNo + ", libCode=" + libCode + ", libName=" + libName + ", address=" + address
				+ ", hotline=" + hotline + ", website=" + website + ", businessHour=" + businessHour + ", trafficTips="
				+ trafficTips + ", profiles=" + profiles + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", isIntelligentLibrary=" + isIntelligentLibrary + ", isMaster=" + isMaster + ", hasEngineRoom="
				+ hasEngineRoom + "]";
	}

	public String getLibType() {
		return libType;
	}

	public void setLibType(String libType) {
		this.libType = libType;
	}
	
}
