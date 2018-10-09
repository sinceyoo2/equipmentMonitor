package com.syo.platform.security.cas;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.syo.platform.entity.SysLog;
import com.syo.platform.entity.User;
import com.syo.platform.service.SysLogService;

public class MyCasAuthenticationFilter extends CasAuthenticationFilter {
	
	@Autowired
	private SysLogService sysLogService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {
		Authentication authentication = super.attemptAuthentication(request, response);
		
		
		if(authentication!=null && authentication.getPrincipal()!=null) {
			SysLog log = new SysLog();
			log.setCodeTips("com.syo.platform.sysLog.login.LoginSuccessHandler.onAuthenticationSuccess()");
			log.setFromIP(request.getRemoteAddr());
			log.setFunctional(null);
			log.setId(UUID.randomUUID().toString());
			log.setLogDetail("用户登录成功");
			log.setLogTime(new Date());
			log.setLogName("用户登录");
			log.setLogType("登录");
			log.setReponseType(request.getContentType());
			log.setRequestMapping("-");
			log.setRequestParam("-");
			log.setRequestURL(request.getRequestURI());
			log.setUser((User) authentication.getPrincipal());
			log.setSessionId(request.getSession().getId());
			sysLogService.saveLog(log);
			
			request.getSession().setAttribute("loginUser", authentication.getPrincipal());
			
		}
		
		return authentication;
	}

	

}
