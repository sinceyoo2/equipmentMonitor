package com.syo.platform.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ErrorReportService {

	/**
	 * 故障总量统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findTotalReport(Date start, Date end);
	
	
	/**
	 * 故障总量及历时统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findDurationReport(Date start, Date end);
	
	
	/**
	 * 故障原因统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findCauseReport(Date start, Date end);
	
	/**
	 * 故障专业类型统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findTypeReport(Date start, Date end);
	
	
	/**
	 * 故障单位统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findDeptReport(Date start, Date end);
	
	/**
	 * 故障级别统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findLevelReport(Date start, Date end);
	
	/**
	 * 故障主动发现统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findActiveReport(Date start, Date end);
	
	/**
	 * 故障申告者统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findTroubleshooterReport(Date start, Date end);
	
	/**
	 * 故障申告方式统计
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> findDeclareTypeReport(Date start, Date end);
	
	
}
