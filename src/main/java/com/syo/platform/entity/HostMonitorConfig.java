package com.syo.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_monitor_hostconfig")
public class HostMonitorConfig {

	@Id
	@GeneratedValue(generator="uuidGen")
	@GenericGenerator(name="uuidGen", strategy="uuid")
	@Column(length=50)
	private String id;
	
	@Column(length=150)
	private String hostName;//主机名称
	
	@Column(length=20)
	private String ipAddress;//ip地址
	
	private int cacheCount = 60;//缓存数量 默认60个
	
//	@Column(name="is_saveHistory", columnDefinition="tinyint default 0")
	@Column(name="is_saveHistory")
	private boolean saveHistory;//是否入库历史数据
	
//	@Column(name="is_notice", columnDefinition="tinyint default 0")
	@Column(name="is_notice")
	private boolean notice;//是否通知
	
	private int cpuCheckFrequency=1;//CPU检查频率 单位：秒
	
	private int memoryCheckFrequency=1;//内存检查频率 单位：秒
	
	private int networkCheckFrequency=1;//网络检查频率 单位：秒
	
	private int diskCheckFrequency=3600;//磁盘检查频率 单位：秒
	
	private int cpuThreshold=80;//CPU预警阀值 单位：%
	
	private int memoryThreshold=80;//内存预警阀值  单位：%
	
	private int networkThreshold=512;//网络预警阀值  单位：k/s
	
	private int diskThreshold=90;//磁盘预警阀值  单位：%
	
	private int cpuContinued=30;//CPU连续超过阀值多少次才预警
	
	private int memoryContinued=60;//内存连续超过阀值多少次才预警
	
	private int networkContinued=300;//网络连续超过阀值多少次才预警
	
	private int diskContinued=1;//磁盘连续超过阀值多少次才预警
	
	@Column(length=1000)
	private String smsReceivers;//接收短信手机号码，多个用,隔开
	
	@Column(length=1000)
	private String emailReceivers;//接收通知邮箱地址，多个用,隔开
	
	@Column(length=200)
	private String remark;//配置备注
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;//创建时间
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTime;//最后更新时间
	
	@Column(length=10)
	private String lastResult;
	
	@ManyToOne
	@JoinColumn(name="bind_equipment")
	private Equipment bindEquipment;//绑定的设备

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getCacheCount() {
		return cacheCount;
	}

	public void setCacheCount(int cacheCount) {
		this.cacheCount = cacheCount;
	}

	public boolean isSaveHistory() {
		return saveHistory;
	}

	public void setSaveHistory(boolean saveHistory) {
		this.saveHistory = saveHistory;
	}

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public int getCpuCheckFrequency() {
		return cpuCheckFrequency;
	}

	public void setCpuCheckFrequency(int cpuCheckFrequency) {
		this.cpuCheckFrequency = cpuCheckFrequency;
	}

	public int getMemoryCheckFrequency() {
		return memoryCheckFrequency;
	}

	public void setMemoryCheckFrequency(int memoryCheckFrequency) {
		this.memoryCheckFrequency = memoryCheckFrequency;
	}

	public int getNetworkCheckFrequency() {
		return networkCheckFrequency;
	}

	public void setNetworkCheckFrequency(int networkCheckFrequency) {
		this.networkCheckFrequency = networkCheckFrequency;
	}

	public int getDiskCheckFrequency() {
		return diskCheckFrequency;
	}

	public void setDiskCheckFrequency(int diskCheckFrequency) {
		this.diskCheckFrequency = diskCheckFrequency;
	}

	public int getCpuThreshold() {
		return cpuThreshold;
	}

	public void setCpuThreshold(int cpuThreshold) {
		this.cpuThreshold = cpuThreshold;
	}

	public int getMemoryThreshold() {
		return memoryThreshold;
	}

	public void setMemoryThreshold(int memoryThreshold) {
		this.memoryThreshold = memoryThreshold;
	}

	public int getNetworkThreshold() {
		return networkThreshold;
	}

	public void setNetworkThreshold(int networkThreshold) {
		this.networkThreshold = networkThreshold;
	}

	public int getDiskThreshold() {
		return diskThreshold;
	}

	public void setDiskThreshold(int diskThreshold) {
		this.diskThreshold = diskThreshold;
	}

	public int getCpuContinued() {
		return cpuContinued;
	}

	public void setCpuContinued(int cpuContinued) {
		this.cpuContinued = cpuContinued;
	}

	public int getMemoryContinued() {
		return memoryContinued;
	}

	public void setMemoryContinued(int memoryContinued) {
		this.memoryContinued = memoryContinued;
	}

	public int getNetworkContinued() {
		return networkContinued;
	}

	public void setNetworkContinued(int networkContinued) {
		this.networkContinued = networkContinued;
	}

	public int getDiskContinued() {
		return diskContinued;
	}

	public void setDiskContinued(int diskContinued) {
		this.diskContinued = diskContinued;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public String getSimpleSmsInfo() {
		if(this.smsReceivers==null || this.smsReceivers.trim().equals("")) {
			return "0个短信接收人";
		}
		String[] smss = this.smsReceivers.split(",");
		return smss.length+"个短信接收人";
	}
	
	public String getSimpleEmailInfo() {
		if(this.emailReceivers==null || this.emailReceivers.trim().equals("")) {
			return "0个邮件接收人";
		}
		String[] emails = this.emailReceivers.split(",");
		return emails.length+"个邮件接收人";
	}
	
	public String getSaveHistoryInfo() {
		return this.isSaveHistory()?"是":"否";
	}

	public String getLastResult() {
		return lastResult;
	}

	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}

	public Equipment getBindEquipment() {
		return bindEquipment;
	}

	public void setBindEquipment(Equipment bindEquipment) {
		this.bindEquipment = bindEquipment;
	}
	
}
