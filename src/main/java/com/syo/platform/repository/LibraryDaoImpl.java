package com.syo.platform.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LibraryDaoImpl implements LibraryDao {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> findTypeAmount() {
		return jdbcTemplate.queryForList("select t.lib_type type, count(1) amount from t_monitor_library t group by t.lib_type");
	}

}
