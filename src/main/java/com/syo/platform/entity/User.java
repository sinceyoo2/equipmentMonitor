package com.syo.platform.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="t_monitor_user")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue(generator="assignedGen")
	@GenericGenerator(name="assignedGen", strategy="assigned")
	@Column(length=50)
	private String account;
	
	@Column(length=50)
	private String password;
	
	@Column(length=50)
	private String name;
	
	@Column(length=20)
	private String telephone;
	
	@Column(length=20)
	private String identity;
	
	@Column(length=20)
	private String workNo;
	
	@Column(length=50)
	private String email;
	
	@Column(length=200)
	private String remarks;
	
	@Column(length=50)
	private String deptName;
	
	@Column(length=100)
	private String roleName;
	
	@Column(length=5)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@JsonProperty("user_id")
	private int casUserId;//用户ID
	
	@Column(length=128)
	@JsonProperty("login_name")
	private String casLoginName;//用户登录名
	
	@Column(length=128)
	@JsonProperty("user_name")
	private String casUserName;//用户姓名
	
	@JsonProperty("user_type")
	private int casUserType;//用户类型
	
	@JsonProperty("post_id")
	private int casPostId;//岗位ID
	
	@Column(length=128)
	@JsonProperty("post_name")
	private String casPostName;//岗位名称
	
	@JsonProperty("unit_id")
	private int casUnitId;//组织ID
	
	@Column(length=128)
	@JsonProperty("unit_name")
	private String casUnitName;//组织名称

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		auths.add(new SimpleGrantedAuthority(this.getRoleName()));
		return auths;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getAccount();
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public int getCasUserId() {
		return casUserId;
	}

	public void setCasUserId(int casUserId) {
		this.casUserId = casUserId;
	}

	public String getCasLoginName() {
		return casLoginName;
	}

	public void setCasLoginName(String casLoginName) {
		this.casLoginName = casLoginName;
	}

	public String getCasUserName() {
		return casUserName;
	}

	public void setCasUserName(String casUserName) {
		this.casUserName = casUserName;
	}

	public int getCasUserType() {
		return casUserType;
	}

	public void setCasUserType(int casUserType) {
		this.casUserType = casUserType;
	}

	public int getCasPostId() {
		return casPostId;
	}

	public void setCasPostId(int casPostId) {
		this.casPostId = casPostId;
	}

	public String getCasPostName() {
		return casPostName;
	}

	public void setCasPostName(String casPostName) {
		this.casPostName = casPostName;
	}

	public int getCasUnitId() {
		return casUnitId;
	}

	public void setCasUnitId(int casUnitId) {
		this.casUnitId = casUnitId;
	}

	public String getCasUnitName() {
		return casUnitName;
	}

	public void setCasUnitName(String casUnitName) {
		this.casUnitName = casUnitName;
	}
	
	
	
}
