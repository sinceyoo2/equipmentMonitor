package com.syo.platform.taskScheduler;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.support.CronSequenceGenerator;

public class CronUtil {

	public static String getCronExpression(Object cronConfig) {
		String cron = null;
		try {
			Class<?> clazz = cronConfig.getClass();
			Field[] fields = clazz.getDeclaredFields();
			String taskType = null;
			String taskHours = null;
			String taskDays = null;
			String taskWeeks = null;
			for(Field field :fields) {
				field.setAccessible(true);
				CronField cronField = field.getAnnotation(CronField.class);
				if(cronField!=null) {
					switch(cronField.value()) {
						case TASKTYPE: taskType = field.get(cronConfig)+""; break;
						case HOUR: taskHours = field.get(cronConfig)+""; break;
						case DAY: taskDays = field.get(cronConfig)+""; break;
						case WEEK: taskWeeks = field.get(cronConfig)+""; break;
					}
				}
			}
			if(taskHours==null || taskHours.trim().equals("") || taskHours.trim().equals("null")) {
				taskHours = "0";
			}
			if(taskDays==null || taskDays.trim().equals("") || taskDays.trim().equals("null")) {
				taskDays = "0";
			}
			if(taskWeeks==null || taskWeeks.trim().equals("") || taskWeeks.trim().equals("null")) {
				taskWeeks = "1";
			}
			
			String[] crons = new String[6];
			
			if(taskType.equals("每秒")) {
				crons[0] = "*";
				crons[1] = "*";
				crons[2] = "*";
				crons[3] = "*";
				crons[4] = "*";
				crons[5] = "?";
				cron = StringUtils.join(crons, " ");
			} else if(taskType.equals("每分")) {
				crons[0] = "0";
				crons[1] = "*";
				crons[2] = "*";
				crons[3] = "*";
				crons[4] = "*";
				crons[5] = "?";
				cron = StringUtils.join(crons, " ");
			} else if(taskType.equals("每半小时")) {
				crons[0] = "0";
				crons[1] = "0/30";
				crons[2] = "*";
				crons[3] = "*";
				crons[4] = "*";
				crons[5] = "?";
				cron = StringUtils.join(crons, " ");
			} else if(taskType.equals("每小时")) {
				crons[0] = "0";
				crons[1] = "0";
				crons[2] = "*";
				crons[3] = "*";
				crons[4] = "*";
				crons[5] = "?";
				cron = StringUtils.join(crons, " ");
			} else if(taskType.equals("每天")) {
				crons[0] = "0";
				crons[1] = "0";
				crons[2] = taskHours;
				crons[3] = "*";
				crons[4] = "*";
				crons[5] = "?";
				cron = StringUtils.join(crons, " ");
			} else if(taskType.equals("每周")) {
				crons[0] = "0";
				crons[1] = "0";
				crons[2] = taskHours;
				crons[3] = "*";
				crons[4] = "*";
				crons[5] = taskWeeks;
				cron = StringUtils.join(crons, " ");
			} else if(taskType.equals("每月")) {
				crons[0] = "0";
				crons[1] = "0";
				crons[2] = taskHours;
				crons[3] = taskDays;
				crons[4] = "*";
				crons[5] = "?";
				cron = StringUtils.join(crons, " ");
			}
			
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return cron;
	}
	
	public static Date getNextTriggerTime(Date lastTime, String cron) {
		if(lastTime==null) {
			lastTime = new Date();
		}
		System.out.println(cron);
		CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);  
		Date nextTriggerTime = cronSequenceGenerator.next(lastTime);
		return nextTriggerTime;
	}
	
}
