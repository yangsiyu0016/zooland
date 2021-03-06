package com.zoo.controller.erp.inbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.service.erp.inbound.InboundDetailService;

@RestController
@RequestMapping("/erp/inbound/detail")
public class InboundDetailController {
	@Autowired
	InboundDetailService inboundDetailService;
	@GetMapping("getDetailByInboundForeignKey")
	public List<InboundDetail> getDetailByInboundForeignKey(@RequestParam("foreignKey")String foreignKey){
		return inboundDetailService.getDetailByInboundForeignKey(foreignKey);
	}
	@PutMapping("updatePrice")
	public Map<String,Object> updatePrice(@RequestParam("id")String id,@RequestParam("costPrice")String costPrice){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			InboundDetail detail = inboundDetailService.updatePrice(id, costPrice);
			resultMap.put("status", "200");
			resultMap.put("inboundDetail", detail);
		} catch (ZooException e) {
			resultMap.put("status", "500");
			resultMap.put("msg", e.getMsg());
		}
		return resultMap;
	}
	
}
