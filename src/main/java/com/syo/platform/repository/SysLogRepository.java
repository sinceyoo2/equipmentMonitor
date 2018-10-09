package com.syo.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.syo.platform.entity.SysLog;

public interface SysLogRepository extends JpaRepository<SysLog, String>, JpaSpecificationExecutor<SysLog> {

}
