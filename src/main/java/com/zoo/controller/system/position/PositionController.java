package com.zoo.controller.system.position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.position.Position;
import com.zoo.service.system.position.PositionService;

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
	@PostMapping("add")
	public boolean add(Position position){
		positionService.addPosition(position);
		return true;	
	}
}
