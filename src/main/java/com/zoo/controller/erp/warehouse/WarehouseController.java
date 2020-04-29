package com.zoo.controller.erp.warehouse;

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

import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.service.erp.warehouse.WarehouseService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/warehouse")
public class WarehouseController {
	@Autowired
	WarehouseService warehouseService;
	@GetMapping("page")
	public Map<String,Object> queryWarehouseByPage(
			@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Warehouse> warehouses = warehouseService.getWarehouseByPage(page, size);
		Long count = warehouseService.getCount();
		map.put("warehouses",warehouses);
		map.put("count",count);
		return map;
	}
	@GetMapping("all")
	public List<Warehouse> getAll(){
		return warehouseService.getWarehouseByPage(null, null);
	}
	@PostMapping("add")
	public RespBean addWarehouse(@RequestBody Warehouse warehouse) {
		try {
			warehouseService.addWarehouse(warehouse);
			return new RespBean("200","保存成功");
		} catch (Exception e) {
			return new RespBean("500","保存失败");
		}
		
	}
	@PutMapping("update")
	public RespBean updateWarehouse(@RequestBody Warehouse warehouse) {
		try {
			warehouseService.updateWarehouse(warehouse);
			return new RespBean("200","保存成功");
		} catch (Exception e) {
			return new RespBean("500","保存失败");
		}
	}
}
