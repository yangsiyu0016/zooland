package com.zoo.controller.erp.inbound;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
