package com.syo.platform.repository.jpautils;

import com.syo.platform.dto.ErrorQueryDTO;
import com.syo.platform.entity.ErrorMessage;
import com.syo.platform.entity.Library;

public class CriteriaFactory {

	public static Criteria<ErrorMessage> createCriteria(ErrorQueryDTO condition) {
		Criteria<ErrorMessage> c = new Criteria<ErrorMessage>();  
		c.add(Restrictions.like("troubleshooter", condition.getTroubleshooter(), true));  
		c.add(Restrictions.like("msgSource", condition.getMsgSource(), true));
		c.add(Restrictions.eq("declareType", condition.getDeclareType(), false)); 
		c.add(Restrictions.like("summary", condition.getSummary(), true));
		if(condition.getErrorLevel()>0) {
			c.add(Restrictions.eq("errorLevel", condition.getErrorLevel(), false));
		}
        c.add(Restrictions.eq("status", condition.getStatus(), false));  
        
        if(condition.getStart()!=null && condition.getEnd()!=null) {
	        c.add(Restrictions.gte("errorTime", condition.getStart(), false));
	        c.add(Restrictions.lt("errorTime", condition.getEnd(), false));
        }
        
        return c;

	}
	
}
