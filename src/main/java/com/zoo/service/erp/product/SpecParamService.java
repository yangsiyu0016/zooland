package com.zoo.service.erp.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.product.SpecParam;

@Service
@Transactional
public class SpecParamService {
	@Autowired
	SpecParamMapper specParamMapper;
	public int addParam(SpecParam param) {
		param.setId(UUID.randomUUID().toString());
		return specParamMapper.addParam(param);
	}
	public List<SpecParam> querySpecParams(String typeId) {
		// TODO Auto-generated method stub
		return specParamMapper.getSpecParams(typeId);
	}
	public List<SpecParam> getParamsByGroupId(String groupId) {
		// TODO Auto-generated method stub
		return specParamMapper.getParamsByGroupId(groupId);
	}
	public int updateParam(SpecParam param) {
		return specParamMapper.updateParam(param);	
	}
}
