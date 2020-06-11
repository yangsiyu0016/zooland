package com.zoo.service.system.parameter;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.system.parameter.SystemParameterDirectoryMapper;
import com.zoo.model.system.parameter.SystemParameterDirectory;

@Service
@Transactional
public class SystemParameterDirectoryService {
	@Autowired
	SystemParameterDirectoryMapper directoryMapper;
	
	public List<SystemParameterDirectory> getTreeData(){
		String companyId = LoginInterceptor.getLoginUser().getCompanyId();
		
		return directoryMapper.getTreeData(companyId);
	}
	public List<SystemParameterDirectory> getDirectoryList(){
		String companyId = LoginInterceptor.getLoginUser().getCompanyId();
		
		return directoryMapper.getDirectoryList(companyId);
	}
	public void addDirectory(SystemParameterDirectory directory) {
		directory.setId(UUID.randomUUID().toString());
		directory.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		directoryMapper.addDirectory(directory);
	}
}
