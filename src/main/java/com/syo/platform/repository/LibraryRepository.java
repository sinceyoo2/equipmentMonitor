package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.syo.platform.entity.Library;

public interface LibraryRepository extends JpaRepository<Library, String>, JpaSpecificationExecutor<Library> {

	public List<Library> findByHasEngineRoom(boolean hasEngineRoom);
	
}
