package com.zoo.controller.erp.outbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.outbound.Outbound;
import com.zoo.service.erp.outbound.OutboundService;

@RestController
@RequestMapping("/erp/outbound")
public class OutboundController {

	@Autowired
	private OutboundService outBoundService;
	
	@RequestMapping("page")
	public Map<String, Object> getOutboundsByPage(
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
			List<Outbound> list = outBoundService.getOutboundsByPage(page, size,sort,order,keywords,code,productCode,productName,type,warehouseId,start_ctime,end_ctime);
			Long count = outBoundService.getTotalCount(keywords,code,productCode,productName,type,warehouseId,start_ctime,end_ctime);
			respMap.put("outbounds", list);
			respMap.put("count", count);
			respMap.put("status", "200");
			return respMap;
		} catch (Exception e) {
			// TODO: handle exception
			respMap.put("status", "500");
			respMap.put("msg", e.getMessage());
			return respMap;
		}
	}
	
	@GetMapping("getOutboundByForeignKey")
	public List<Outbound> getOutboundByForeignKey(@RequestParam("foreignKey") String foreignKey) {
		return outBoundService.getOutboundByForeignKey(foreignKey);
	}
}
