package com.zoo.controller.erp.openingInventory;

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


import com.zoo.filter.LoginInterceptor;
import com.zoo.model.erp.openingInventory.OpeningInventory;
import com.zoo.service.erp.openingInventory.OpeningInventoryService;
import com.zoo.vo.RespBean;
@RestController
@RequestMapping("/oi")
public class OpeningInventoryController {
	@Autowired
	OpeningInventoryService oiService;
	
	@GetMapping("page")
	public Map<String,Object> getOpeningInventory_self(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<OpeningInventory> ois = oiService.getOpeningInventoryByPage(page, size, LoginInterceptor.getLoginUser().getId());
		long count = oiService.getCount(LoginInterceptor.getLoginUser().getId());
		map.put("ois", ois);
		map.put("count",count);
		return map;
	}
	@GetMapping("getOiById")
	public OpeningInventory getOiById(@RequestParam("id")String id) {
		return oiService.getOpeningInventoryById(id);
	}
	@PostMapping("add")
	public RespBean addOpeningInventory(@RequestBody OpeningInventory oi) {
		try {
			oiService.addOpeningInventory(oi);
			return new RespBean("200","保存成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}	
	}
	@PutMapping("update")
	public RespBean updateOpeningInventory(@RequestBody OpeningInventory oi) {
		try {
			oiService.updateOpeningInventory(oi);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PostMapping("startFlow")
	public RespBean startFlow(@RequestParam("id")String id) {
		try {
			oiService.startProcess(id);
			return new RespBean("200","启动成功");
		} catch (Exception e) {
			return new RespBean("500","启动失败");
		}
	}
	@PostMapping("deleteFlow")
	public void deleteFlow(@RequestParam("id")String id) {
		oiService.doCallBackFlow(id);
	}
	//作废功能
	@PostMapping("destroy")
	public void destroy(@RequestParam("id")String id) {
		//功能未确定，暂时不识现
	}
	//取回
	@GetMapping("reset")
	public RespBean reject(@RequestParam("id") String id) {
		try {
			oiService.reset(id);
			return new RespBean("200", "取回成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
}
