package com.zoo.service.system.area;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.system.area.AreaMapper;
import com.zoo.model.system.area.Area;
@Service
@Transactional
public class AreaService {
	@Autowired
	AreaMapper areaMapper;
	public List<Area> getTreeData(){
		return areaMapper.getTreeData();
	}
	public List<Area> getDataByParentId(String parentId){
		return areaMapper.getDataByParentId(parentId);
	}
	/**
	 * 插入数据并返回插入数据
	 * @param area
	 */
	public Area addArea(Area area) {
		// TODO Auto-generated method stub
		//生成uuid
		String uuidStr = UUID.randomUUID().toString();
		area.setId(uuidStr);
		area.setLeaf(true);
		if(area.getParentId()==null) {
			area.setParentId("0");
			
		} else {
			this.updateLeaf(area.getParentId(), false);
		}
		
		
		areaMapper.addArea(area);
		return area;
	}
	
	/**
	 * 修改区域数据功能
	 * @param area
	 */
	public void updateArea(Area area) {
		// TODO Auto-generated method stub
		System.out.println(area);
		areaMapper.updateArea(area);
	}
	public void updateLeaf(String id,Boolean leaf) {
		areaMapper.updateLeaf(id, leaf);
	}
}
