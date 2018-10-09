package com.syo.platform.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LibraryDao {
	
	public List<Map<String, Object>> findTypeAmount();
}
