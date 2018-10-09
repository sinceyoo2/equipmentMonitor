package com.syo.platform.sysLog.ascept;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.syo.platform.entity.Functional;
import com.syo.platform.entity.SysLog;
import com.syo.platform.entity.User;
import com.syo.platform.repository.FunctionalRepository;
import com.syo.platform.service.SysLogService;


@Aspect
@Component
public class SysLogAscept {
	
	@Autowired
	private FunctionalRepository functionalRepository;
	
	@Autowired
	private SysLogService sysLogService;
	
	ObjectMapper mapper = new ObjectMapper();

	@Pointcut("execution(public * com.syo.platform.web.*.*(..))")
	public void advice() {}
	
	@Around("advice()")
	public Object interceptor(ProceedingJoinPoint pjp) {
		Object rest = null;
		
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		HttpServletResponse response = requestAttributes.getResponse();
		
		Class controllerClass = pjp.getTarget().getClass();
		MethodSignature ms = (MethodSignature) pjp.getSignature();
	    Method method = ms.getMethod();
	    
	    Object[] args = pjp.getArgs();
	    
		
	    Date logTime = new Date();
		String fromIP = request.getRemoteAddr();
		String logType = "操作";
		String requestURL = request.getRequestURI();
		String reponseType = response.getContentType();
		String codeTips = controllerClass.getName()+"."+method.getName()+"()";
		
		String funcLogName = "-";
		String funcDesc = "-";
		String funcURL = "-";		
		
		Functional func = functionalRepository.findOne(codeTips);
		if(func!=null) {
			funcLogName = func.getLogName();
			funcDesc = func.getDescription();
			funcURL = func.getUrl();
		}

		String logName = "【用户操作】"+funcLogName;
		String logDetail = logName+"|"+funcDesc;
		
		List<Object> params = new ArrayList<Object>();
		for(int i=0; i<args.length; i++) {
			Object arg = args[i];
			if(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse || arg instanceof HttpSession || 
					arg instanceof Model || arg instanceof MultipartFile) {
				params.add("-");
			} else {
				params.add(arg);
			}
		}
		String requestParam = null;
		try {
			requestParam = mapper.writeValueAsString(params);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		try {
			rest = pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			logType = "异常";
			logName = "【操作异常】"+funcLogName+"|"+e.getMessage();
			logDetail = logName+"|"+funcDesc+"|"+e.getStackTrace();
		}
		
		SysLog log = new SysLog();
		log.setCodeTips(codeTips);
		log.setFromIP(fromIP);
		log.setFunctional(func);
		log.setId(UUID.randomUUID().toString());
		log.setLogDetail(logDetail);
		log.setLogTime(logTime);
		log.setLogName(logName);
		log.setLogType(logType);
		log.setReponseType(reponseType);
		log.setRequestMapping(funcURL);
		log.setRequestParam(requestParam);
		log.setRequestURL(requestURL);
		log.setUser(user);
		log.setSessionId(request.getSession().getId());
		
		sysLogService.saveLog(log);
//		System.out.println(log);
		
		return rest;
	}
	
}
