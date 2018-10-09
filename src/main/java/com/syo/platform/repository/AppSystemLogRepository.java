package com.syo.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.syo.platform.entity.AppSystemLog;

public interface AppSystemLogRepository extends JpaRepository<AppSystemLog, String>, JpaSpecificationExecutor<AppSystemLog> {

}
