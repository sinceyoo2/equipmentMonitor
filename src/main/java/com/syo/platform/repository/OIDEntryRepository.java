package com.syo.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syo.platform.entity.OIDEntry;

public interface OIDEntryRepository extends JpaRepository<OIDEntry, String> {

	public List<OIDEntry> findByEquipmentTypeAndVendorAndModelsContainingOrderBySort(String equipmentType, String vendor, String model);
}
