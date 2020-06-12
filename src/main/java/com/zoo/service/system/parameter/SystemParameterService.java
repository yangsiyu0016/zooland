package com.zoo.service.system.parameter;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.system.parameter.SystemParameterMapper;
import com.zoo.model.system.parameter.SystemParameter;

@Service
@Transactional
public class SystemParameterService {
	@Autowired
	SystemParameterMapper parameterMapper;
	
	public List<SystemParameter> getParameterList(){
		String companyId = LoginInterceptor.getLoginUser().getCompanyId();
		return parameterMapper.getParameterList(companyId);
	}
	
	public void addParameter(SystemParameter parameter) {
		parameter.setId(UUID.randomUUID().toString());
		parameter.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		parameterMapper.addParameter(parameter);
	}

	public void update(SystemParameter parameter) {
		// TODO Auto-generated method stub
		parameterMapper.update(parameter);
	}

	public void delete(String id) {
		// TODO Auto-generated method stub
		parameterMapper.delete(id);
		
	}
}
