package com.zoo.controller.erp.product;

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

import com.zoo.exception.ZooException;
import com.zoo.model.erp.product.Unit;
import com.zoo.service.erp.product.UnitService;
import com.zoo.vo.RespBean;


@RestController
@RequestMapping("/product/unit")
public class UnitController {
	@Autowired
	UnitService unitService;
	@GetMapping("list")
	public List<Unit> list(){
		return unitService.getUnitList();
	}
	
	@PostMapping("addUnit")
	public RespBean addUnit(@RequestBody Unit unit) {
		try {
			unitService.addUnit(unit);
			return new RespBean("200","添加成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getExceptionEnum().message());
		}
	}
	@PutMapping("update")
	public RespBean Update(@RequestBody Unit unit) {
		try {
			unitService.update(unit);
			return new RespBean("200", "添加成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getExceptionEnum().message());
		}
	}
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("keywords") String keywords) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<Unit> units = unitService.getUnitByPage(page, size, keywords);
			long count = unitService.getCount(keywords);
			map.put("status", "200");
			map.put("units", units);
			map.put("count", count);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "500");
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@GetMapping("getUnitById")
	public Unit getUnitById(@RequestParam("id") String id) {
		return unitService.getUnitById(id);
	}
	
	@GetMapping("delete")
	public RespBean delete(@RequestParam("ids") String ids) {
		try {
			unitService.delete(ids);
			return new RespBean("200", "删除成功");
		} catch (Exception e) {
			return new RespBean("500", e.getMessage());
			// TODO: handle exception
		}
	}
}
