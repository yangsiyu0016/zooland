package com.zoo.controller.erp.sell;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.sell.SellDetail;
import com.zoo.service.erp.sell.SellDetailService;

@RestController
@RequestMapping("/erp/sell/detail")
public class SellDetailController {
	@Autowired
	SellDetailService detailService;
	
	@GetMapping("getNotOutDetailsBySellId")
	public List<SellDetail> getNotOutDetailsBySellId(String sellId){
		return detailService.getNotOutDetailBySellId(sellId);
	}
}
