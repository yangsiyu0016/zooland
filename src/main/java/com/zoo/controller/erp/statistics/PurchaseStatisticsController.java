package com.zoo.controller.erp.statistics;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.statistics.SearchData;
import com.zoo.service.erp.statistics.PurchaseStatisticsService;

/**
 * 采购明细统计控制层
 * @author aa
 *
 */
@RestController
@RequestMapping("/erp/purchaseStatistics")
public class PurchaseStatisticsController {

	@Autowired
	private PurchaseStatisticsService purchaseStatisticsService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		
		return purchaseStatisticsService.page(page, size);
				
	}
	
	@PostMapping("search")
	public Map<String, Object> search(@RequestBody SearchData searchData) {
		return purchaseStatisticsService.search(searchData);
	}
	
}
