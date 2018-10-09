package com.syo.platform.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syo.platform.entity.User;
import com.syo.platform.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/users/{pageSize}/{pageNo}")
	public String userList(User condition, @PathVariable("pageNo")int pageNo, @PathVariable("pageSize")int pageSize, Model model) {
		model.addAttribute("condition", condition);
		model.addAttribute("users", userService.findUser(pageNo, pageSize, condition));
		return "user";
	}
	
	
	@RequestMapping("/users/{pageNo}")
	public String userList(User condition, @PathVariable("pageNo")int pageNo, Model model) {
		return userList(condition, pageNo, 15, model);
	}
	
	@RequestMapping("/users")
	public String userList(User condition, Model model) {
		return userList(condition, 1, 15, model);
	}
	
	@PostMapping("/user_save")
	@ResponseBody
	public String saveUser(User user) {
		userService.saveUser(user);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/user_del/{account}")
	@ResponseBody
	public String deleteUser(@PathVariable("account")String account) {
		userService.deleteUser(account);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/user_enable/{account}")
	@ResponseBody
	public String enableUser(@PathVariable("account")String account) {
		userService.saveUserStatus(account, "启用");
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/account/{account}")
	@ResponseBody
	public User findByAccount(@PathVariable("account")String account) {
		return userService.findUserByAccount(account);
	}
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public String updatePassword(String newPassword, String oldPassword, HttpSession session) {
		if(session.getAttribute("loginUser")==null) {
			return "修改失败，当前用户不存在，请重新登录";
		}
		User loginUser = (User) session.getAttribute("loginUser");
		return userService.updatePassword(loginUser.getAccount(), oldPassword, newPassword);
	}
	
	@RequestMapping("/resetPassword")
	@ResponseBody
	public String resetPassword(String account) {
		userService.resetPasssword(account);
//		return "SUCCESS";
		return "操作成功";
	}
	
}
