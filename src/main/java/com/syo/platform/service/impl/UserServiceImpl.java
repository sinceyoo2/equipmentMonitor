package com.syo.platform.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import com.syo.platform.entity.User;
import com.syo.platform.repository.UserRepository;
import com.syo.platform.repository.jpautils.Criteria;
import com.syo.platform.repository.jpautils.Restrictions;
import com.syo.platform.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${security.salt}")
	private String salt;

	@Override
	public User findUserByAccount(String account) {
		return userRepository.findOne(account);
	}

	@Override
	public void saveUser(User user) {
		User currentUser = userRepository.findOne(user.getAccount());
		if(currentUser!=null) {
			user.setCreateTime(currentUser.getCreateTime());
			user.setStatus(currentUser.getStatus());
			user.setPassword(currentUser.getPassword());
			user.setCasLoginName(currentUser.getCasLoginName());
			user.setCasPostId(currentUser.getCasPostId());
			user.setCasPostName(currentUser.getCasPostName());
			user.setCasUnitId(currentUser.getCasUnitId());
			user.setCasUnitName(currentUser.getCasUnitName());
			user.setCasUserId(currentUser.getCasUserId());
			user.setCasUserName(currentUser.getCasUserName());
			user.setCasUserType(currentUser.getCasUserType());
		} else {
			user.setCreateTime(new Date());
			user.setStatus("启用");
//			user.setPassword("123456");
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			user.setPassword(encoder.encodePassword(encoder.encodePassword("123456", null), salt));
		}
		userRepository.save(user);
	}

	@Override
	public void deleteUser(String account) {
		User currentUser = userRepository.findOne(account);
		currentUser.setStatus("停用");
		userRepository.save(currentUser);
	}

	@Override
	public void deleteUserComplete(String account) {
		userRepository.delete(account);
	}

	@Override
	public Page<User> findUser(int pageNo, int pageSize, User condition) {
		Criteria<User> c = new Criteria<>();
		c.add(Restrictions.like("account", condition.getAccount(), true));
		c.add(Restrictions.like("name", condition.getName(), true));
		c.add(Restrictions.like("deptName", condition.getDeptName(), true));
		c.add(Restrictions.like("workNo", condition.getWorkNo(), true));
		c.add(Restrictions.like("identity", condition.getIdentity(), true));
		c.add(Restrictions.eq("status", condition.getStatus(), true));
		return userRepository.findAll(c, new PageRequest(pageNo-1, pageSize, Direction.ASC, "createTime"));
	}

	@Override
	public void saveUserStatus(String account, String status) {
		User user = userRepository.findOne(account);
		user.setStatus(status);
		userRepository.save(user);
	}

	@Override
	public String updatePassword(String account, String oldPassword, String newPassword) {
		User user = userRepository.findOne(account);
		if(user==null) {
			return "不存在的用户";
		}
		
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		
		if(!user.getPassword().equals(encoder.encodePassword(oldPassword, salt))) {
			return "修改失败,旧密码不正确";
		}
		
		user.setPassword(encoder.encodePassword(newPassword, salt));
		
//		user.setPassword(newPassword);
		userRepository.save(user);
		return "密码修改成功";
	}

	@Override
	public void resetPasssword(String account) {
		User user = userRepository.findOne(account);
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		user.setPassword(encoder.encodePassword(encoder.encodePassword("123456", null), salt));
	}

}
