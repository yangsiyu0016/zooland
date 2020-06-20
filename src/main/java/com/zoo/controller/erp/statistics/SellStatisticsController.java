package com.zoo.controller.erp.statistics;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@PostMapping("page")
	public Map<String, Object> page(@RequestBody SearchData searchData) throws ParseException {
		//System.out.println(searchData.getStartDate());
		Map<String, Object> map = sellStatisticsService.page(searchData);
		
		return map;
		
	}
}
