package com.syo.platform.service;

import org.springframework.data.domain.Page;

import com.syo.platform.entity.User;

public interface UserService {

	public User findUserByAccount(String account);
	
	public void saveUser(User user);
	
	public void deleteUser(String account);
	
	public void deleteUserComplete(String account);
	
	public void saveUserStatus(String account, String status);
	
	public Page<User> findUser(int pageNo, int pageSize, User condition);
	
	public String updatePassword(String account, String oldPassword, String newPassword);
	
	public void resetPasssword(String account);
	
}
