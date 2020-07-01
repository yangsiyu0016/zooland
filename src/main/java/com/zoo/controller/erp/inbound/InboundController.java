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
	public Map<String, Object> getInboundByPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		Map<String, Object> respMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> inboundList = inboundService.getInboundByPage(page, size);
			Long count = inboundService.getTotleCount();
			respMap.put("inbounds", inboundList);
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
	public Inbound getInboundByForeignKey(@RequestParam("foreignKey") String foreignKey) {
		return inboundService.getInboundByForeignKey(foreignKey);
	}
}
