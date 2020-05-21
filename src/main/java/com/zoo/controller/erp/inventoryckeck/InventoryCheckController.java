package com.zoo.controller.erp.inventoryckeck;

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
import com.zoo.filter.LoginInterceptor;
import com.zoo.model.erp.inventorycheck.InventoryCheck;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/inventoryCheck")
public class InventoryCheckController {

	
	@Autowired
	InventoryCheckService inventoryCheckService;
	
	@GetMapping("page")
	public Map<String, Object> getInventoryCheck_self(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<InventoryCheck> ics = inventoryCheckService.getInventoryCheckByPage(page, size);
		Long count = inventoryCheckService.getCount(LoginInterceptor.getLoginUser().getId());
		
		map.put("ics", ics);
		map.put("count", count);
		
		return map;
	}
	
	@GetMapping("getIcById")
	public InventoryCheck getIcById(@RequestParam("id") String id) {
		return inventoryCheckService.getInventoryCheckById(id);
	}
	
	@PostMapping("add")
	public RespBean addInventoryCheck(@RequestBody InventoryCheck ic) {
		try {
			inventoryCheckService.addInventoryCheck(ic);
			return new RespBean("200", "保存成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PutMapping("update")
	public RespBean updateIc(@RequestBody InventoryCheck ic) {
		try {
			inventoryCheckService.updateInventoryCheck(ic);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PostMapping("startFlow")
	public RespBean startFlow(@RequestParam("id") String id) {
		try {
			inventoryCheckService.startProcess(id);
			return new RespBean("200", "启动成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PostMapping("deleteFlow")
	public void deleteFlow(@RequestParam("id") String id) {
		
		inventoryCheckService.doCallBackFlow(id);
		
	}
	@PostMapping("checkStock")
	public RespBean checkStock(@RequestBody InventoryCheck check) {
		try {
			inventoryCheckService.checkStock(check);
			return new RespBean("200","");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
}
