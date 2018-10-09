package com.syo.platform.service;

import java.util.List;
import java.util.Map;

import com.syo.platform.entity.Library;

public interface LibraryService {

	public List<Library> findLibrary(String libType, String vague);
	
	public Library findLibraryByLibNo(String libNo);
	
	public Map<String, Object> findTypeAmount();
	
}
