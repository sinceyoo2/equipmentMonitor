package com.syo.platform.sysLog.login;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.syo.platform.entity.SysLog;
import com.syo.platform.entity.User;
import com.syo.platform.service.SysLogService;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	private SysLogService sysLogService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth)
			throws IOException, ServletException {
		
		if(auth!=null && auth.getPrincipal()!=null) {
			SysLog log = new SysLog();
			log.setCodeTips("com.syo.platform.sysLog.login.LoginSuccessHandler.onAuthenticationSuccess()");
			log.setFromIP(req.getRemoteAddr());
			log.setFunctional(null);
			log.setId(UUID.randomUUID().toString());
			log.setLogDetail("用户登录成功");
			log.setLogTime(new Date());
			log.setLogName("用户登录");
			log.setLogType("登录");
			log.setReponseType(resp.getContentType());
			log.setRequestMapping("-");
			log.setRequestParam("-");
			log.setRequestURL(req.getRequestURI());
			log.setUser((User) auth.getPrincipal());
			log.setSessionId(req.getSession().getId());
			
//			System.out.println(log);
			sysLogService.saveLog(log);
			
			req.getSession().setAttribute("loginUser", auth.getPrincipal());
			
		}
//		System.out.println("222222222222222222222222222222222222222222222222222222222222222");
		super.onAuthenticationSuccess(req, resp, auth);
	}

}
