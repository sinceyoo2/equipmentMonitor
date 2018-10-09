package com.syo.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.syo.platform.entity.WorkSheet;


public interface WorkSheetRepository extends JpaRepository<WorkSheet, String>, JpaSpecificationExecutor<WorkSheet> {

}
