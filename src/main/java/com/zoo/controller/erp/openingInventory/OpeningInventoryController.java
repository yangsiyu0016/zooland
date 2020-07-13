package com.zoo.controller.erp.openingInventory;

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
import com.zoo.filter.LoginInterceptor;
import com.zoo.model.erp.openingInventory.OpeningInventory;
import com.zoo.service.erp.openingInventory.OpeningInventoryService;
import com.zoo.vo.RespBean;
@RestController
@RequestMapping("/oi")
public class OpeningInventoryController {
	@Autowired
	OpeningInventoryService oiService;
	@GetMapping("all")
	public Map<String,Object> getOpeningInventory_all(@RequestParam("page")Integer page,
			@RequestParam("size")Integer size,
			@RequestParam(value="keywords") String keywords,
			@RequestParam(value="code") String code,
			@RequestParam(value="productCode") String productCode,
			@RequestParam(value="productName") String productName,
			@RequestParam(value="status") String status,
			@RequestParam(value="warehouseId") String warehouseId,
			@RequestParam(value="start_initDate") String start_initDate,
			@RequestParam(value="end_initDate") String end_initDate,
			@RequestParam(value="start_ctime") String start_ctime,
			@RequestParam(value="end_ctime") String end_ctime,
			@RequestParam(value="sort") String sort,
			@RequestParam(value="order") String order){
		Map<String,Object> map = new HashMap<String,Object>();
		List<OpeningInventory> ois = oiService.getOpeningInventoryByPage(page, size,null,keywords, code,productCode,productName,status,warehouseId,
				start_initDate,end_initDate,start_ctime,end_ctime,sort,order);
		long count = oiService.getCount(null,keywords, code,productCode,productName,status,warehouseId,
				start_initDate,end_initDate,start_ctime,end_ctime);
		map.put("ois", ois);
		map.put("count",count);
		return map;
	}
	@GetMapping("page")
	public Map<String,Object> getOpeningInventory_self(@RequestParam("page")Integer page,
			@RequestParam("size")Integer size,
			@RequestParam(value="keywords") String keywords,
			@RequestParam(value="code") String code,
			@RequestParam(value="productCode") String productCode,
			@RequestParam(value="productName") String productName,
			@RequestParam(value="status") String status,
			@RequestParam(value="warehouseId") String warehouseId,
			@RequestParam(value="start_initDate") String start_initDate,
			@RequestParam(value="end_initDate") String end_initDate,
			@RequestParam(value="start_ctime") String start_ctime,
			@RequestParam(value="end_ctime") String end_ctime,
			@RequestParam(value="sort") String sort,
			@RequestParam(value="order") String order){
		Map<String,Object> map = new HashMap<String,Object>();
		List<OpeningInventory> ois = oiService.getOpeningInventoryByPage(page, size,LoginInterceptor.getLoginUser().getId(),keywords, code,productCode,productName,status,warehouseId,
				start_initDate,end_initDate,start_ctime,end_ctime,sort,order);
		long count = oiService.getCount(LoginInterceptor.getLoginUser().getId(),keywords, code,productCode,productName,status,warehouseId,
				start_initDate,end_initDate,start_ctime,end_ctime);
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
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean deleteOiById(@PathVariable String ids) {
		try {
			oiService.deleteOiById(ids);
			return new RespBean("200","删除成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getMsg());
		}
	}
	@PostMapping("startFlow")
	public RespBean startFlow(@RequestParam("id")String id) {
		try {
			oiService.startProcess(id);
			return new RespBean("200","启动成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getMsg());
		}
	}
	@PostMapping("deleteFlow")
	public void deleteFlow(@RequestParam("id")String id) {
		oiService.doCallBackFlow(id);
	}
	//作废功能
	@PostMapping("destroy")
	public RespBean destroy(@RequestParam("id")String id) {
		try {
			oiService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getExceptionEnum().message());
		}
	}
	//取回
	@GetMapping("reset")
	public RespBean reject(@RequestParam("id") String id) {
		try {
			oiService.reset(id);
			return new RespBean("200", "取回成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
}
