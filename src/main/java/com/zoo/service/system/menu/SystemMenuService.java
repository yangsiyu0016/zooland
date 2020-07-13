package com.zoo.service.system.menu;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.system.menu.SystemMenuMapper;
import com.zoo.model.system.menu.SystemMenu;
import com.zoo.model.system.user.UserInfo;


@Service
@Transactional
public class SystemMenuService {
	@Autowired
	SystemMenuMapper systemMenuMapper;
	public List<SystemMenu> menuTree(){
		return systemMenuMapper.getMenuTreeData();
	}
	public List<SystemMenu> getCompanyMenuData(){
		UserInfo user = LoginInterceptor.getLoginUser();
		return systemMenuMapper.getCompanyMenuData(user.getCompanyId());
	}
	public SystemMenu getMenuById(String id) {
		return systemMenuMapper.getMenuById(id);
	}
	public int addMenu(SystemMenu menu) {
		try {
			menu.setId(UUID.randomUUID().toString());
			//if(StringUtils.isBlank(menu.getParentId())) {
				//menu.setParentId("d4582b80-7831-47cf-844b-3a4f754807af");
			//}
			return systemMenuMapper.addMenu(menu);
		} catch (Exception e) {
			throw new ZooException(ExceptionEnum.INVALID_PARAM);
		}	
	}
	public int updateMenu(SystemMenu menu) {
		return systemMenuMapper.updateMenu(menu);
	}
	public void deleteMenuById(String id) {
		SystemMenu menu = systemMenuMapper.getMenuById(id);
		long count = systemMenuMapper.getCountByParentId(menu.getId());
		if(count>0) {
			throw new ZooException(ExceptionEnum.DELETE_MENU_HAS_CHIILDREN);
		}else {
			systemMenuMapper.deleteCompanyTypeMenuByMenuId(id);
			systemMenuMapper.deletePositionMenuByMenuId(id);
			systemMenuMapper.deleteMenuById(id);
		}
		
	}
}
