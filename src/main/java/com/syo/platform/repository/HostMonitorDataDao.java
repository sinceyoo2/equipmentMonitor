package com.syo.platform.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class HostMonitorDataDao {

	@Resource
	MongoTemplate hisMongoTemplate;
	
	@Resource
	MongoTemplate errMongoTemplate;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	
	public List<Map> findHisData(Date start, Date end, String infoType, String ip) {
		
		List<Map> result = new ArrayList<Map>();
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(start);
		c1.set(Calendar.MILLISECOND, 0);
		int year1 = c1.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(end);
		c2.set(Calendar.MILLISECOND, 0);
		int year2 = c2.get(Calendar.YEAR);
		int month2 = c2.get(Calendar.MONTH);
		
		if(year1==year2 && month1==month2) {
			String collName = infoType+"x"+ip.replaceAll("\\.", "_")+"x"+sdf.format(start);
			Criteria criteria = new Criteria();
			criteria.and("timestamp").gte(c1.getTimeInMillis()).lt(c2.getTimeInMillis());
			Query query = new Query(criteria);
			query.with(new Sort(new Order(Direction.ASC,"timestamp"))); 
			result = hisMongoTemplate.find(query, Map.class, collName);
		} else {
			while(c2.getTimeInMillis() > c1.getTimeInMillis()) {
				String collName = infoType+"x"+ip.replaceAll("\\.", "_")+"x"+sdf.format(c1.getTime());
				
				long startstamp = c1.getTimeInMillis();
				
				c1.add(Calendar.MONTH, 1);
				c1.set(Calendar.DATE, 1);
				c1.set(Calendar.HOUR_OF_DAY, 0);
				c1.set(Calendar.MINUTE, 0);
				c1.set(Calendar.SECOND, 0);
				
				long endstamp = c1.getTimeInMillis();
				if(endstamp > c2.getTimeInMillis()) {
					endstamp = c2.getTimeInMillis();
				}
				
//				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				System.out.println(sdf2.format(new Date(startstamp))+"——>"+sdf2.format(new Date(endstamp)));
								
				Criteria criteria = new Criteria();
				criteria.and("timestamp").gte(startstamp).lt(endstamp); 
				Query query = new Query(criteria);
				query.with(new Sort(new Order(Direction.ASC,"timestamp"))); 
				result.addAll(hisMongoTemplate.find(query, Map.class, collName));
			}
		}
		

		return result;
	}
	
	public List<Map> findErrData(Date start, Date end, String infoType, String ip) {
		
		List<Map> result = new ArrayList<Map>();
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(start);
		c1.set(Calendar.MILLISECOND, 0);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(end);
		c2.set(Calendar.MILLISECOND, 0);
		
		String collName = "error_message";
		Criteria criteria = new Criteria();
		criteria.and("timestamp").gte(c1.getTimeInMillis()).lt(c2.getTimeInMillis());
		if(infoType!=null && !infoType.trim().equals("")) {
			criteria.and("infoType").is(infoType);
		}
		if(ip!=null && !ip.trim().equals("")) {
			criteria.and("ipAddress").is(ip);
		}
		Query query = new Query(criteria);
		query.with(new Sort(new Order(Direction.ASC,"timestamp")));  
		result = errMongoTemplate.find(query, Map.class, collName);
		

		return result;
	}
	
	
	
}
