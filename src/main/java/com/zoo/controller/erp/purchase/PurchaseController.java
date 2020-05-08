package com.zoo.controller.erp.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.service.erp.purchase.PurchaseService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/purchase")
public class PurchaseController {
	@Autowired
	PurchaseService purchaseService;
	@GetMapping("page")
	public Map<String,Object> page(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Purchase> purchases = purchaseService.getPurchaseByPage(page,size);
		long count = purchaseService.getCount();
		map.put("purchases", purchases);
		map.put("count", count);
		return map;
	}
	@PostMapping("addPurchase")
	public RespBean addPurchase(@RequestBody Purchase  purchase) {
		try {
			purchaseService.addPurchase(purchase);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("updatePurchase")
	public RespBean updatePurchase(@RequestBody Purchase purchase) {
		try {
			purchaseService.updatePurchase(purchase);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PostMapping("startFlow")
	public RespBean startFlow(@RequestParam("id")String id) {
		
		try {
			purchaseService.startProcess(id);
			return new RespBean("200","启动成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getExceptionEnum().message());
		}
	}
	@PostMapping("destroy")
	public RespBean destroy(@RequestParam("id")String id) {
		try {
			purchaseService.destroy(id);
			return new RespBean("200","作废成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@RequestMapping(value="{id}",method=RequestMethod.GET)
	public Purchase getPurchaseById(@PathVariable String id) {
		return purchaseService.getPurchaseById(id);
	}
}
