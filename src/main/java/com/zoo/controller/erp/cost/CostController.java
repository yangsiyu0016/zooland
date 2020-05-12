package com.zoo.controller.erp.cost;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.cost.Cost;
import com.zoo.service.erp.cost.CostService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/cost")
public class CostController {
	@Autowired
	CostService costService;
	@GetMapping("getCostByForeignKey")
	public List<Cost> getCostByForeignKey(@RequestParam("foreignKey")String foreignKey){
		return costService.getCostByForeignKey(foreignKey);
	}
	@PostMapping("addCostFromSell")
	public RespBean addCostFromSell(@RequestBody Cost cost) {
		try {
			costService.addCostFromSell(cost);
			return new RespBean("200","添加成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getExceptionEnum().message());
		}
	}
	@PostMapping("addCostFromPurchase")
	public RespBean addCostFromPurchase(@RequestBody Cost cost) {
		try {
			costService.addCostFromPurchase(cost);
			return new RespBean("200","添加成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getExceptionEnum().message());
		}
	}
	@DeleteMapping("deleteCostFromPurchase")
	public RespBean deleteCostFromPurchase(@RequestParam("id")String id) {
		try {
			costService.deleteCostFromPurchase(id);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@DeleteMapping("deleteCostFromSell")
	public RespBean deleteCostFromSell(@RequestParam("id")String id) {
		try {
			costService.deleteCostFromSell(id);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PostMapping("inboundFromPurchase")
	public RespBean inboundFromPurchase(@RequestParam("costId")String costId) {
		try {
			costService.inboundFromPurchase(costId);
			return new RespBean("200","入库成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
