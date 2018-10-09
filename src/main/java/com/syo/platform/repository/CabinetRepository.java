package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syo.platform.entity.Cabinet;

public interface CabinetRepository extends JpaRepository<Cabinet, String> {

	@Query("from Cabinet c where c.lib.libNo=:libNo and c.rowName=:rowName order by c.positionIndex")
	public List<Cabinet> findByLibAndRowName(@Param("libNo")String libNo, @Param("rowName")String rowName);
	
}
