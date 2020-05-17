package com.zoo.controller.system.position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.position.Position;
import com.zoo.service.system.position.PositionService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/position")
public class PositionController {
	@Autowired
	PositionService positionService;
	
	@GetMapping("page")
	public Map<String,Object> queryPositionByPage(
			@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Position> positions = positionService.getPositionByPage(page, size);
		Long count = positionService.getCount();
		map.put("positions",positions);
		map.put("count",count);
		return map;
	}
	@GetMapping("all")
	public List<Position> getAll(){
		List<Position> positions = positionService.getAllPositons();
		return positions;
	}
	@GetMapping("getResource")
	public List<String> getResource(String positionId){
		return positionService.getResource(positionId);
	}
	@PostMapping("add")
	public RespBean add(@RequestBody
			 Position position){
		try {
			positionService.addPosition(position);
			return new RespBean("200","保存成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
			
	}
	@PutMapping("update")
	public RespBean update(@RequestBody Position position){
		try {
			positionService.updatePosition(position);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
			
	}
	@PutMapping("/updateResource")
	public RespBean updateResource(String positionId,String[] menuIds) {
		try {
			positionService.updateResource(positionId, menuIds);
			return new RespBean("success","操作成功");
		} catch (Exception e) {
			return new RespBean("error",e.getMessage());
		}
	}
}
