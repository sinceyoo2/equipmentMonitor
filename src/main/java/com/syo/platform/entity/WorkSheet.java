package com.syo.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.syo.platform.activiti.entity.ProcessEntity;

@Entity
@Table(name="t_monitor_worksheet")
public class WorkSheet implements ProcessEntity {

	@Id
	@GeneratedValue(generator="uuidGen")
	@GenericGenerator(name="uuidGen", strategy="uuid")
	@Column(length=50)
	private String id;
	
	@OneToOne
    @JoinColumn(name = "error_message")
    public ErrorMessage errorMessage;
	
	@Column(length=50)
	private String deptName;
	
	@Column(length=150)
	private String title;
	
	@Column(length=500)
	private String situation;//情况描述
	
	@Column(length=500)
	private String remarks;
	
	@Column(length=50)
	private String contacts; //联系人
	
	@Column(length=50)
	private String contactNumber; //联系电话
	
	@ManyToOne
	@JoinColumn(name="creator")
	private User creator;//申请人
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@ManyToOne
	@JoinColumn(name="acceptor")
	private User acceptor;
	
	@Column(length=5)
	private String acceptAudit;
	
	@Column(length=500)
	private String acceptRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date acceptTime;
	
	@ManyToOne
	@JoinColumn(name="executor")
	private User executor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date exeStartTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date exeEndTime;
	
	@Column(length=500)
	private String exeRemarks;
	
	@Column(length=5)
	private String repaireConfirm;
	
	@Column(length=500)
	private String repaireRemarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date repaireConfirmTime;
	
	@Column(length=10)
	private String status;
	
	private boolean hasFiled;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getAcceptAudit() {
		return acceptAudit;
	}

	public void setAcceptAudit(String acceptAudit) {
		this.acceptAudit = acceptAudit;
	}

	public String getAcceptRemarks() {
		return acceptRemarks;
	}

	public void setAcceptRemarks(String acceptRemarks) {
		this.acceptRemarks = acceptRemarks;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}


	public Date getExeStartTime() {
		return exeStartTime;
	}

	public void setExeStartTime(Date exeStartTime) {
		this.exeStartTime = exeStartTime;
	}

	public Date getExeEndTime() {
		return exeEndTime;
	}

	public void setExeEndTime(Date exeEndTime) {
		this.exeEndTime = exeEndTime;
	}

	public String getExeRemarks() {
		return exeRemarks;
	}

	public void setExeRemarks(String exeRemarks) {
		this.exeRemarks = exeRemarks;
	}

	public String getRepaireConfirm() {
		return repaireConfirm;
	}

	public void setRepaireConfirm(String repaireConfirm) {
		this.repaireConfirm = repaireConfirm;
	}

	public String getRepaireRemarks() {
		return repaireRemarks;
	}

	public void setRepaireRemarks(String repaireRemarks) {
		this.repaireRemarks = repaireRemarks;
	}

	public Date getRepaireConfirmTime() {
		return repaireConfirmTime;
	}

	public void setRepaireConfirmTime(Date repaireConfirmTime) {
		this.repaireConfirmTime = repaireConfirmTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isHasFiled() {
		return hasFiled;
	}

	public void setHasFiled(boolean hasFiled) {
		this.hasFiled = hasFiled;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(User acceptor) {
		this.acceptor = acceptor;
	}

	public User getExecutor() {
		return executor;
	}

	public void setExecutor(User executor) {
		this.executor = executor;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}



	@Transient
	@JsonIgnore
	private Task task;
	
	@Transient
	@JsonIgnore
	private HistoricTaskInstance historicTaskInstance;

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public String getProcessName() {
		return "工单管理流程";
	}

	@Override
	public Task getTask() {
		return this.task;
	}

	@Override
	public HistoricTaskInstance getHistoryTask() {
		return this.historicTaskInstance;
	}

	public HistoricTaskInstance getHistoricTaskInstance() {
		return historicTaskInstance;
	}

	public void setHistoricTaskInstance(HistoricTaskInstance historicTaskInstance) {
		this.historicTaskInstance = historicTaskInstance;
	}
}
