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

import com.zoo.model.erp.warehouse.GoodsAllocation;
import com.zoo.service.erp.warehouse.GoodsAllocationService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/warehouse/ga")
public class GoodsAllocationController {
	@Autowired
	GoodsAllocationService gaService;
	@GetMapping("page")
	public Map<String,Object> queryGaByPage(
			@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("warehouseId") String warehouseId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<GoodsAllocation> gas = gaService.getGoodsAllocationByPage(page, size,warehouseId);
		Long count = gaService.getCount(warehouseId);
		map.put("gas",gas);
		map.put("count",count);
		return map;
	}
	@GetMapping("getGaByWarehouseId")
	public List<GoodsAllocation> getGaByWarehouseId(@RequestParam("warehouseId")String warehouseId){
		List<GoodsAllocation> gas = gaService.getGoodsAllocationByPage(null, null, warehouseId);
		return gas;
	}
	@PostMapping("add")
	public Map<String,Object> add(@RequestBody GoodsAllocation ga){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			ga = gaService.addGoodsAllocation(ga);
			resultMap.put("status", 200);
			resultMap.put("ga", ga);
		} catch (Exception e) {
			resultMap.put("status", 500);
		}
		
		return resultMap;
	}
	@PutMapping("update")
	public RespBean update(@RequestBody GoodsAllocation ga) {
		try {
			gaService.updateGoodsAllocation(ga);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500","更新失败");
		}
	}
}
