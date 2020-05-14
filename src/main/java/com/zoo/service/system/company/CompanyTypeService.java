package com.zoo.service.system.company;


import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.system.company.CompanyTypeMapper;
import com.zoo.model.system.company.CompanyType;
import com.zoo.model.system.menu.SystemMenu;
import com.zoo.service.system.menu.SystemMenuService;



@Service
@Transactional
public class CompanyTypeService {
	@Autowired
	CompanyTypeMapper companyTypeMapper;
	@Autowired
	SystemMenuService systemMenuService;
	public List<CompanyType> getCompanyTypeByPage(Integer page,Integer size){
		int start = (page-1)*size;
		return companyTypeMapper.getCompanyTypeByPage(start, size);
	}
	public Long getCount() {
		return companyTypeMapper.getCount();
	}
	public List<CompanyType> getAllCmpanyType(){
		return companyTypeMapper.getCompanyTypeByPage(null,null);
	}
	public boolean addCompanyType(CompanyType type) {
		type.setId(UUID.randomUUID().toString());
		return companyTypeMapper.addCompanyType(type)==1;
	}
	public void updateResource(String typeId,String[] menuIds) {
		companyTypeMapper.deleteResourceByTypeId(typeId);
		for(String menuId:menuIds) {
			String id = UUID.randomUUID().toString();
			SystemMenu menu = systemMenuService.getMenuById(menuId);
			companyTypeMapper.addResource(id, typeId, menuId);
			if(!StringUtils.isBlank(menu.getParentId())) {
				addParentMenu(typeId,menu.getParentId());
			}
		}
	}
	private void addParentMenu(String typeId, String parentId) {
		long count = companyTypeMapper.getCountByCompanyIdAndMenuId(typeId, parentId);
		if(count==0) {
			companyTypeMapper.addResource(UUID.randomUUID().toString(), typeId, parentId);
			SystemMenu menu = systemMenuService.getMenuById(parentId);
			if(!StringUtils.isBlank(menu.getParentId())) {
				addParentMenu(typeId,menu.getParentId());
			}
		}
		
		
	}
	public List<String> getResourceByTypeId(String typeId){
		return companyTypeMapper.getResourceByTypeId(typeId);
	}
	public void updateCompanyType(CompanyType type) {
		companyTypeMapper.updateCompanyType(type);
	}
}
