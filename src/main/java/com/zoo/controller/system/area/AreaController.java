package com.zoo.controller.system.area;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.area.Area;
import com.zoo.service.system.area.AreaService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/area")
public class AreaController {
	@Autowired
	AreaService areaService;
	@GetMapping("getTreeData")
	public List<Area> getTreeData(){
		return areaService.getTreeData();
	}
	@GetMapping("getDataByParentId")
	public List<Area> getDataByParentId(@RequestParam("parentId")String parentId){
		return areaService.getDataByParentId(parentId);
	}
	
	/**
	 * 添加区域数据
	 */
	@PostMapping("addArea")
	public Map<String, Object> addArea(@RequestBody Area area) {
		HashMap<String,Object> map = new HashMap<String, Object>();
		try {
			Area respArea = areaService.addArea(area);
			
			map.put("respArea", respArea);
			map.put("respBean", new RespBean("200", "保存成功"));
			return map;
		} catch (Exception e) {
			// TODO: handle exception
			map.put("respArea", null);
			map.put("respBean", new RespBean("500","保存失败"));
			return map;
		}
	}
	
}
