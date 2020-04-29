package com.zoo.service.erp.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.erp.product.SpecGroupMapper;
import com.zoo.model.erp.product.SpecGroup;

@Service
@Transactional
public class SpecGroupService {
	@Autowired
	SpecGroupMapper specGroupMapper;
	public List<SpecGroup> getGroupByTypeId(String typeId){
		return specGroupMapper.getGroupByTypeId(typeId);
	}
	public int addGroup(SpecGroup group) {
		group.setId(UUID.randomUUID().toString());
		return specGroupMapper.addGroup(group);
	}
	public int updateGroup(SpecGroup group) {
		return specGroupMapper.updateGroup(group);
		
	}
}
