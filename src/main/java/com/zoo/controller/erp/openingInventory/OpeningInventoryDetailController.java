package com.zoo.controller.erp.openingInventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.service.erp.openingInventory.OpeningInventoryDetailService;

@RestController
@RequestMapping("/oi/detail")
public class OpeningInventoryDetailController {
	@Autowired
	OpeningInventoryDetailService detailService;
	@PutMapping("updatePrice")
	public void updatePrice(@RequestParam("id")String id,@RequestParam("costPrice") String costPrice,@RequestParam("totalMoney")String totalMoney) {
		detailService.updatePrice(id,costPrice,totalMoney);
	}
}
