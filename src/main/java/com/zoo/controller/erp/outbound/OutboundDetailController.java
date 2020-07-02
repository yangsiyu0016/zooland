package com.zoo.controller.erp.outbound;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.outbound.OutboundDetail;
import com.zoo.service.erp.outbound.OutboundDetailService;

@RestController
@RequestMapping("/erp/outbound/detail")
public class OutboundDetailController {
	@Autowired
	OutboundDetailService outboundDetailService;
	@GetMapping("getDetailByOuboundForeignKey")
	public List<OutboundDetail> getDetailByOuboundForeignKey(@RequestParam("foreignKey")String foreignKey){
		return outboundDetailService.getDetailByOuboundForeignKey(foreignKey);
	}
}
