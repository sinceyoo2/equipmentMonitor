package com.syo.platform.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ErrorReportDao {

	public List<Map<String, Object>> findErrorAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorAmountAndDuration(Date start, Date end);
	
	public List<Map<String, Object>> findErrorCauseAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorTypeAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorDeptAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorDeptDealAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorLevelAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorActiveAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorTroubleshooterAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorTroubleshooterDealAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorDeclareTypeAmount(Date start, Date end);
	
	public List<Map<String, Object>> findErrorDeclareTypeDealAmount(Date start, Date end);
}
