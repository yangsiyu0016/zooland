package com.zoo.controller.erp.outbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.service.erp.outbound.OutBoundService;

@RestController
@RequestMapping("/erp/outbound")
public class OutboundController {

	@Autowired
	private OutBoundService outBoundService;
	
	@RequestMapping("page")
	public Map<String, Object> getOutboundsByPage(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		Map<String, Object> respMap = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = outBoundService.getOutboundsByPage(page, size);
			Long count = outBoundService.getTotalCount();
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
	
}
