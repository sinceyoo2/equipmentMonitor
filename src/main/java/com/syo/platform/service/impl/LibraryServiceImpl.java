package com.syo.platform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.syo.platform.entity.Library;
import com.syo.platform.repository.LibraryDao;
import com.syo.platform.repository.LibraryRepository;
import com.syo.platform.repository.jpautils.Criteria;
import com.syo.platform.repository.jpautils.Restrictions;
import com.syo.platform.service.LibraryService;

@Service
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	private LibraryRepository libraryRepository;
	
	@Autowired
	private LibraryDao libraryDao;
	
//	@Override
//	public List<Library> findLibrary(String libType, String vague) {
//		Criteria<Library> c = new Criteria<Library>();
//		if(vague!=null && !vague.trim().equals("")) {
//			c.add(Restrictions.or(
//					Restrictions.like("libCode", vague, true), 
//					Restrictions.like("libName", vague, true), 
//					Restrictions.like("address", vague, true)
//					));
//		}
//		
////		if(libType==null || libType.trim().equals("")) {
////			return libraryRepository.findAll(new Sort(Direction.ASC, "libNo"));
////		}
//		
//		
//		if(libType.equals("master")) {
//			c.add(Restrictions.eq("isMaster", true, false));  
////			return libraryRepository.findAll(c, new Sort(Direction.ASC, "libNo"));
//		}
//		
//		if(libType.equals("member")) {
//			c.add(Restrictions.eq("isMaster", false, false));
////			return libraryRepository.findAll(c, new Sort(Direction.ASC, "libNo"));
//		}
//		
//		if(libType.equals("intelligent")) {
//			c.add(Restrictions.eq("isIntelligentLibrary", true, false));
////			return libraryRepository.findAll(c, new Sort(Direction.ASC, "libNo"));
//		}
//
//		return libraryRepository.findAll(c, new Sort(Direction.ASC, "libNo"));
//	}
	
	@Override
	public List<Library> findLibrary(String libType, String vague) {
		Criteria<Library> c = new Criteria<Library>();
		if(vague!=null && !vague.trim().equals("")) {
			c.add(Restrictions.or(
					Restrictions.like("libCode", vague, true), 
					Restrictions.like("libName", vague, true), 
					Restrictions.like("address", vague, true)
					));
		}
		
		if(libType.equals("master")) {
			c.add(Restrictions.eq("libType", "主馆", false));  
		}
		
		if(libType.equals("member")) {
			c.add(Restrictions.eq("libType", "成员馆", false));  
		}
		
		if(libType.equals("intelligent")) {
			c.add(Restrictions.eq("libType", "智能馆", false));  
		}

		return libraryRepository.findAll(c, new Sort(Direction.ASC, "libNo"));
	}

	@Override
	public Library findLibraryByLibNo(String libNo) {
		return libraryRepository.findOne(libNo);
	}

	@Override
	public Map<String, Object> findTypeAmount() {
		List<Map<String, Object>> list = libraryDao.findTypeAmount();
		Map<String, Object> result = new HashMap<>();
		for(Map<String, Object> item : list) {
			result.put(item.get("type").toString(), item.get("amount"));
		}
		return result;
	}

}
