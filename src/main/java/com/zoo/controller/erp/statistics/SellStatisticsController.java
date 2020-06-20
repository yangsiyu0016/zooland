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
import com.zoo.service.erp.statistics.SellStatisticsService;

/**
 * 销售明细统计
 * @author aa
 *
 */
@RestController
@RequestMapping("/erp/sellStatistics")
public class SellStatisticsController {

	@Autowired
	private SellStatisticsService sellStatisticsService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		
		Map<String, Object> map = sellStatisticsService.page(page, size);
		
		return map;
		
	}
	@PostMapping("search")
	public Map<String, Object> search(@RequestBody SearchData searchData) {
		return sellStatisticsService.search(searchData);
	}
	
}
