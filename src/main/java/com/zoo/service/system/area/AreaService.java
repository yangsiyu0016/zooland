package com.zoo.service.system.area;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.system.area.AreaMapper;
import com.zoo.model.system.area.Area;
import com.zoo.vo.RespBean;
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
		areaMapper.addArea(area);
		return area;
	}
	
	
	
}
