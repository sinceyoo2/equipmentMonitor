package com.syo.platform.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syo.platform.entity.Equipment;
import com.syo.platform.entity.HostMonitorConfig;
import com.syo.platform.repository.EquipmentRepository;
import com.syo.platform.repository.HostMonitorConfigRepository;
import com.syo.platform.service.HostMonitorConfigService;

@Service
public class HostMonitorConfigServiceImpl implements HostMonitorConfigService {

	@Autowired
	private HostMonitorConfigRepository hostMonitorConfigRepository;
	
	@Autowired
	private EquipmentRepository equipmentRepository;
	
	@Override
	public void saveConfig(HostMonitorConfig config) {
		if(config.getId()==null || config.getId().trim().equals("")) {
			config.setLastUpdateTime(new Date());
			config.setCreateTime(config.getLastUpdateTime());
			hostMonitorConfigRepository.save(config);
		} else {
			HostMonitorConfig currentConfig = hostMonitorConfigRepository.findOne(config.getId());
			if(currentConfig!=null) {
				currentConfig.setHostName(config.getHostName());
				currentConfig.setIpAddress(config.getIpAddress());
				currentConfig.setRemark(config.getRemark());
				currentConfig.setCpuCheckFrequency(config.getCpuCheckFrequency());
				currentConfig.setMemoryCheckFrequency(config.getMemoryCheckFrequency());
				currentConfig.setNetworkCheckFrequency(config.getNetworkCheckFrequency());
				currentConfig.setDiskCheckFrequency(config.getDiskCheckFrequency());
				currentConfig.setCpuThreshold(config.getCpuThreshold());
				currentConfig.setMemoryThreshold(config.getMemoryThreshold());
				currentConfig.setNetworkThreshold(config.getNetworkThreshold());
				currentConfig.setDiskThreshold(config.getDiskThreshold());
				currentConfig.setCpuContinued(config.getCpuContinued());
				currentConfig.setMemoryContinued(config.getMemoryContinued());
				currentConfig.setNetworkContinued(config.getNetworkContinued());
				currentConfig.setDiskContinued(config.getDiskContinued());
				currentConfig.setSaveHistory(config.isSaveHistory());
				currentConfig.setNotice(config.isNotice());
				currentConfig.setSmsReceivers(config.getSmsReceivers());
				currentConfig.setEmailReceivers(config.getEmailReceivers());
				currentConfig.setLastUpdateTime(new Date());
				
				hostMonitorConfigRepository.save(currentConfig);
			}
		}
	}

	@Override
	public List<HostMonitorConfig> findConfig(String vague) {
		if(vague==null || vague.trim().equals("")) {
			return hostMonitorConfigRepository.findAll();
		}
		return hostMonitorConfigRepository.findByVague(vague);
	}

	@Override
	public HostMonitorConfig findConfigById(String id) {
		return hostMonitorConfigRepository.findOne(id);
	}

	@Override
	public void deleteConfig(String id) {
		hostMonitorConfigRepository.delete(id);
	}

	@Override
	public HostMonitorConfig findConfigByIp(String ip) {
		return hostMonitorConfigRepository.findByIpAddress(ip);
	}

	@Override
	public void saveBind(String configId, String equipmentId) {
		Equipment equipment = equipmentRepository.findOne(equipmentId);
		HostMonitorConfig config = hostMonitorConfigRepository.findOne(configId);
		config.setBindEquipment(equipment);
		hostMonitorConfigRepository.save(config);
	}

}
