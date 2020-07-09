package com.zoo.controller.erp.inbound;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.service.erp.inbound.InboundDetailService;
import com.zoo.vo.RespBean;

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
	@PostMapping("inbound")
	public RespBean inbound (@RequestParam("id")String id) {
		try {
			inboundDetailService.inbound(id);
			return new RespBean("200","操作成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getMsg());
		}
	}
}
