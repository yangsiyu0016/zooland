package com.zoo.controller.erp.inbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.inbound.Inbound;
import com.zoo.service.erp.inbound.InboundService;


@RestController
@RequestMapping("/erp/inbound")
public class InboundController {

	@Autowired
	private InboundService inboundService;
	
	@GetMapping("page")
	public Map<String, Object> getInboundByPage(
			@RequestParam("page") Integer page, 
			@RequestParam("size") Integer size,
			@RequestParam("sort") String sort,
			@RequestParam("order") String order,
			@RequestParam("keywords") String keywords,
			@RequestParam("code") String code,
			@RequestParam("productCode") String productCode,
			@RequestParam("productName") String productName,
			@RequestParam("type") String type,
			@RequestParam("warehouseId") String warehouseId,
			@RequestParam("start_ctime") String start_ctime,
			@RequestParam("end_ctime") String end_ctime) {
		Map<String, Object> respMap = new HashMap<String, Object>();
		try {
			List<Inbound> list = inboundService.getInboundByPage(page, size, sort, order, keywords, code, productCode, productName, type, warehouseId, start_ctime, end_ctime);
			Long count = inboundService.getTotleCount(keywords,code, productCode, productName, type, warehouseId, start_ctime, end_ctime);
			respMap.put("inbounds", list);
			respMap.put("count", count);
			respMap.put("status", "200");
			respMap.put("msg", "获取成功");
			return respMap;
		} catch (Exception e) {
			// TODO: handle exception
			respMap.put("inbound", null);
			respMap.put("status", "500");
			respMap.put("msg", e.getMessage());
			return respMap;
		}
	}
	
	@GetMapping("getInboundByForeignKey")
	public List<Inbound> getInboundByForeignKey(@RequestParam("id") String id) {
		return inboundService.getInboundByForeignKey(id);
	}
	
	@GetMapping("getInboundById")
	public Inbound getInboundById(@RequestParam("id") String id) {
		return inboundService.getInboundById(id);
	}
}
