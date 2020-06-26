package com.zoo.controller.erp.warehouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.warehouse.Stock;
import com.zoo.service.erp.warehouse.StockService;

@RestController
@RequestMapping("/erp/stock")
public class StockController {
	@Autowired
	StockService stockService;
	@GetMapping("page")
	public Map<String,Object> page(
			@RequestParam("page")Integer page,
			@RequestParam("size")Integer size,
			@RequestParam("keywords")String keywords,
			@RequestParam("productCode")String productCode,
			@RequestParam("productName")String productName,
			@RequestParam("warehouseId")String warehouseId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Stock> stocks = stockService.getStockByPage(page, size,keywords,productCode,productName,warehouseId);
		long count = stockService.getStockCount(keywords,productCode,productName,warehouseId);
		map.put("stocks", stocks);
		map.put("count", count);
		return map;
	}
	@GetMapping("getStock")
	public Stock getStock(@RequestParam("productId") String productId,@RequestParam("warehouseId")String warehouseId) {
		Stock stock = stockService.getStock(productId, warehouseId);
		return stock;
	}
}
