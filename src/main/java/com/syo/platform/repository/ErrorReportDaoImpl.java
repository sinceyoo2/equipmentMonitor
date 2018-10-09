package com.syo.platform.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ErrorReportDaoImpl implements ErrorReportDao {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> findErrorAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.target_name, count(1) amount from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.target_name order by count(1) desc", start, end);
	}

	@Override
	public List<Map<String, Object>> findErrorAmountAndDuration(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.target_name, count(1) amount, sum(t.duration) duration from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.target_name order by count(1) desc", start, end);
	}
	
	@Override
	public List<Map<String, Object>> findErrorCauseAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.cause, count(1) amount from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.cause order by count(1) desc", start, end);
	}
	
	@Override
	public List<Map<String, Object>> findErrorTypeAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.error_type, count(1) amount from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.error_type order by count(1) desc", start, end);
	}
	
	@Override
	public List<Map<String, Object>> findErrorDeptAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.department, count(1) amount from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.department order by count(1) desc", start, end);
	}

	@Override
	public List<Map<String, Object>> findErrorDeptDealAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.department, count(1) amount from t_monitor_errormsg t where t.status='已恢复' and t.error_time>=? and t.error_time<? group by t.department order by count(1) desc", start, end);
	}

	@Override
	public List<Map<String, Object>> findErrorLevelAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.error_level, count(1) amount from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.error_level order by count(1) desc", start, end);
	}

	@Override
	public List<Map<String, Object>> findErrorActiveAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.target_name, count(1) amount from t_monitor_errormsg t where t.declare_type='监控发现' and t.error_time>=? and t.error_time<? group by t.target_name order by count(1) desc", start, end);
	}

	@Override
	public List<Map<String, Object>> findErrorTroubleshooterAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.troubleshooter, count(1) amount from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.troubleshooter order by count(1) desc", start, end);
	}

	@Override
	public List<Map<String, Object>> findErrorTroubleshooterDealAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.troubleshooter, count(1) amount from t_monitor_errormsg t where t.status='已恢复' and t.error_time>=? and t.error_time<? group by t.troubleshooter order by count(1) desc", start, end);
	}

	@Override
	public List<Map<String, Object>> findErrorDeclareTypeAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.declare_type, count(1) amount from t_monitor_errormsg t where t.error_time>=? and t.error_time<? group by t.declare_type order by count(1) desc", start, end);
	}
	
	@Override
	public List<Map<String, Object>> findErrorDeclareTypeDealAmount(Date start, Date end) {
		return jdbcTemplate.queryForList("select t.declare_type, count(1) amount from t_monitor_errormsg t where t.status='已恢复' and t.error_time>=? and t.error_time<? group by t.declare_type order by count(1) desc", start, end);
	}

}
