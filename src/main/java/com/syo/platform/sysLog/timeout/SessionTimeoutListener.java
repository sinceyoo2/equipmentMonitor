package com.syo.platform.sysLog.timeout;

import java.util.Date;
import java.util.UUID;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.syo.platform.entity.SysLog;
import com.syo.platform.entity.User;
import com.syo.platform.service.SysLogService;

@WebListener
public class SessionTimeoutListener implements HttpSessionListener {
	
	@Autowired
	private SysLogService sysLogService;

	@Override
	public void sessionCreated(HttpSessionEvent event) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		if(event.getSession().getAttribute("loginUser")!=null) {
			User loginUser = (User) event.getSession().getAttribute("loginUser");
			
			SysLog log = new SysLog();
			log.setCodeTips("com.syo.platform.sysLog.login.LogoutSuccessHandler.onLogoutSuccess()");
			log.setFromIP("-");
			log.setFunctional(null);
			log.setId(UUID.randomUUID().toString());
			log.setLogDetail("用户因超时退出");
			log.setLogTime(new Date());
			log.setLogName("用户超时退出");
			log.setLogType("登出");
			log.setReponseType("-");
			log.setRequestMapping("-");
			log.setRequestParam("-");
			log.setRequestURL("-");
			log.setUser(loginUser);
			log.setSessionId(event.getSession().getId());
			
//			System.out.println(log);
			sysLogService.saveLog(log);
		}
//		System.out.println("11111111111111111111111111111111111111111111111111111111111");

	}

}
