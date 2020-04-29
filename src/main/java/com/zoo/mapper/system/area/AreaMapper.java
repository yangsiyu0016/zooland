package com.zoo.mapper.system.area;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.area.Area;

@Component
public interface AreaMapper {
	List<Area> getTreeData();
	List<Area> getDataByParentId(@Param("parentId")String parentId);
	public Area getAreaById(@Param("id")String id);
	/**
	 * 插入区域数据
	 * @param parentId 父级id
	 * @param name 名称
	 * @param id 本级id
	 * @return
	 */
	int addArea(@Param("area") Area area);
	
	
	/**
	 * 修改区域数据功能
	 * @param area
	 */
	void updateArea(@Param("area") Area area);
}
