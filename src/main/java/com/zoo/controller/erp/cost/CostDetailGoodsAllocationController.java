package com.zoo.controller.erp.cost;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.cost.CostDetailGoodsAllocation;
import com.zoo.service.erp.cost.CostDetailGoodsAllocationService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/cost/goodsAllocation")
public class CostDetailGoodsAllocationController {
	@Autowired
	CostDetailGoodsAllocationService costDetailGoodsAllocationService;
	@PostMapping("addCostDetailGoodsAllocation")
	public Map<String,Object> addCostDetailGoodsAllocation(@RequestBody CostDetailGoodsAllocation costDetailGoodsAllocation){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			costDetailGoodsAllocation = costDetailGoodsAllocationService.addCostDetailGoodsAllocation(costDetailGoodsAllocation);
			map.put("costDetailGoodsAllocation",costDetailGoodsAllocation);
			map.put("status", 200);
		} catch (Exception e) {
			map.put("status", 500);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	@DeleteMapping(value = "{id}")
	public RespBean deleteCostDetailGoodsAllocation(@PathVariable String id) {
		try {
			costDetailGoodsAllocationService.deleteCostDetailGoodsAllocation(id);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
