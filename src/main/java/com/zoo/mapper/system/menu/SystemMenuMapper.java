package com.zoo.mapper.system.menu;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zoo.model.system.menu.SystemMenu;

public interface SystemMenuMapper {
	List<SystemMenu> getMenuTreeData();
	List<SystemMenu> getCompanyMenuData(@Param("companyId") String companyId);
	SystemMenu getMenuById(@Param("id")String id);
	int addMenu(@Param("menu")SystemMenu menu);
	int updateMenu(@Param("menu") SystemMenu menu);
	int deleteMenuById(@Param("id") String id);
	Long getCountByParentId(@Param("parentId") String parentId);
	
}
