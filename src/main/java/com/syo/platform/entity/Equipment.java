package com.syo.platform.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="t_monitor_equipment")
public class Equipment {

	@Id
	@GeneratedValue(generator="assignedGen")
	@GenericGenerator(name="assignedGen", strategy="assigned")
	@Column(length=50)
	private String id;
	
	@Column(length=50)
	private String equipmentName; //设备名
	
	@Column(length=20)
	private String equipmentNo; //设备编号
	
	@Column(length=20)
	private String vendor; //品牌
	
	@Column(length=20)
	private String model; //型号
	
	@Column(length=20)
	private String deptName; //所属部门名
	
	@Column(length=10)
	private String type; //设备类型
	
	@Column(length=20)
	private String ipAddress;
	
	@ManyToOne
	@JoinColumn(name="lib")
	private Library lib;
	
	@Column(length=20)
	private String area; //所在区域 如 机房、读者区、办公室。。。
	
	@Column(length=100)
	private String position; //位置描述
	
	@Column(length=20)
	private String positionType;//位置类型
	
	private double positionX;
	
	private double positionY;
	
	private int floor; //楼层
	
	@ManyToOne
	@JoinColumn(name="area_shape")
	private PlanShape areaShape;
	
	@ManyToOne
	@JoinColumn(name="lib_plan")
	private LibraryPlan libraryPlan;
	
	private double planWidth;
	
	private double planHeight;
	
	private double planThickness;
	
	private double planX;
	
	private double planY;
	
	private double planZ;
	
	@ManyToOne
	@JoinColumn(name="cabinet")
	private Cabinet cabinet;
	
	private int startU; //开始U位置
	
	private int endU; //结束U位置
	
	@Column(length=200)
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="createTime")
	private Date createTime = new Date();
	
	private boolean isObserver;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="observer")
	private Equipment myObserver;
	
	@OneToMany(mappedBy="myObserver")
	private List<Equipment> followers;
	
	@Column(length=10)
	private String status="在用";
	
	@Column(length=10)
	private String errorStatus="正常";//只供智能馆设备标识用
	
	@Transient
	private String style;
	
	@Transient
	private boolean hasProblem;
	
	@Column(length=50)
	private String followerErrorId;
	
	@Column(length=50)
	private String ownerAccount;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public boolean isObserver() {
		return isObserver;
	}

	public void setIsObserver(boolean isObserver) {
		this.isObserver = isObserver;
	}

	public Equipment getMyObserver() {
		return myObserver;
	}

	public void setMyObserver(Equipment myObserver) {
		this.myObserver = myObserver;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getPositionX() {
		return positionX;
	}

	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getStartU() {
		return startU;
	}

	public void setStartU(int startU) {
		this.startU = startU;
	}

	public int getEndU() {
		return endU;
	}

	public void setEndU(int endU) {
		this.endU = endU;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Library getLib() {
		return lib;
	}

	public void setLib(Library lib) {
		this.lib = lib;
	}

	public Cabinet getCabinet() {
		return cabinet;
	}

	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public PlanShape getAreaShape() {
		return areaShape;
	}

	public void setAreaShape(PlanShape areaShape) {
		this.areaShape = areaShape;
	}

	public LibraryPlan getLibraryPlan() {
		return libraryPlan;
	}

	public void setLibraryPlan(LibraryPlan libraryPlan) {
		this.libraryPlan = libraryPlan;
	}

	public double getPlanWidth() {
		return planWidth;
	}

	public void setPlanWidth(double planWidth) {
		this.planWidth = planWidth;
	}

	public double getPlanHeight() {
		return planHeight;
	}

	public void setPlanHeight(double planHeight) {
		this.planHeight = planHeight;
	}

	public double getPlanThickness() {
		return planThickness;
	}

	public void setPlanThickness(double planThickness) {
		this.planThickness = planThickness;
	}

	public double getPlanX() {
		return planX;
	}

	public void setPlanX(double planX) {
		this.planX = planX;
	}

	public double getPlanY() {
		return planY;
	}

	public void setPlanY(double planY) {
		this.planY = planY;
	}

	public double getPlanZ() {
		return planZ;
	}

	public void setPlanZ(double planZ) {
		this.planZ = planZ;
	}

	public boolean isHasProblem() {
		return hasProblem;
	}

	public void setHasProblem(boolean hasProblem) {
		this.hasProblem = hasProblem;
	}

	public List<Equipment> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Equipment> followers) {
		this.followers = followers;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public String getFollowerErrorId() {
		return followerErrorId;
	}

	public void setFollowerErrorId(String followerErrorId) {
		this.followerErrorId = followerErrorId;
	}

	public String getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}
	
	
	
}
