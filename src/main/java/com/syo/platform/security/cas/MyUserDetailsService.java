package com.syo.platform.security.cas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syo.platform.entity.User;
import com.syo.platform.service.UserService;

public class MyUserDetailsService implements AuthenticationUserDetailsService {

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		if(((CasAssertionAuthenticationToken)token).getAssertion().getPrincipal()!=null) {
			AttributePrincipal principal = ((CasAssertionAuthenticationToken)token).getAssertion().getPrincipal();
			if(principal.getAttributes()!=null && principal.getAttributes().get("user_id")!=null) {
				String account = principal.getAttributes().get("login_name").toString();				
				User user = userService.findUserByAccount(account);
				
				if(user==null) {
					ObjectMapper mapper = new ObjectMapper();
					try {

						Map<String, Object> principalMap = new HashMap<>();
						String[] validKey = {"login_name","unit_name","user_type","post_id","user_id","post_name","user_name","unit_id"};
						for(String key : validKey) {
							principalMap.put(key, principal.getAttributes().get(key));
						}
						String json = mapper.writeValueAsString(principalMap);
						user = mapper.readValue(json, User.class);
						
						user.setAccount(user.getCasLoginName());
						user.setName(user.getCasUserName());
						user.setDeptName(user.getCasUnitName());
						user.setRoleName("ROLE_USER");
						user.setCreateTime(new Date());
						user.setWorkNo(user.getCasLoginName());
						user.setStatus("启用");
						userService.saveUser(user);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
				//权限
//				List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
//				auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//				user.setAuthorities(auths);
				user.setRoleName("ROLE_ADMIN");
				
				return user;
			}
			throw new UsernameNotFoundException("用户不存在");
		}
		
		throw new UsernameNotFoundException("用户不存在");
	}
	

	/*@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		if(((CasAssertionAuthenticationToken)token).getAssertion().getPrincipal()!=null) {
			AttributePrincipal principal = ((CasAssertionAuthenticationToken)token).getAssertion().getPrincipal();
			if(principal.getAttributes()!=null && principal.getAttributes().get("user_id")!=null) {
				String account = principal.getAttributes().get("login_name").toString();
				
//				try {
//					System.setOut(new java.io.PrintStream(new java.io.BufferedOutputStream(
//							new java.io.FileOutputStream("c:/print.txt")),true));
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				}
//				System.out.println(principal.getAttributes());
//				System.out.println("account:"+account);
				
				User user = userService.findUserByAccount(account);
//				System.out.println("user:"+user);
				if(user==null) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String json = mapper.writeValueAsString(principal.getAttributes());
//						System.out.println("json:"+json);
						user = mapper.readValue(json, User.class);
//						System.out.println("user2"+user);
						user.setAccount(user.getCasLoginName());
						user.setName(user.getCasUserName());
						user.setDeptName(user.getCasUnitName());
						user.setRoleName("ROLE_USER");
						user.setCreateTime(new Date());
						user.setWorkNo(user.getCasLoginName());
						user.setStatus("启用");
						userService.saveUser(user);
						System.out.println("入库完成");
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
				//权限
//				List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
//				auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//				user.setAuthorities(auths);
				try {
				user.setRoleName("ROLE_ADMIN");
				}catch(Exception e) {}
				
				return user;
			}
			throw new UsernameNotFoundException("用户不存在");
		}
		
		throw new UsernameNotFoundException("用户不存在");
	}*/

}
