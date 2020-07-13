package com.zoo.controller.erp.statistics;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public Map<String, Object> page(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@RequestParam("sort") String sort,
			@RequestParam("order") String order,
			@RequestParam("keywords") String keywords,
			@RequestParam("code") String code,
			@RequestParam("productName") String productName,
			@RequestParam("customerName") String customerName,
			@RequestParam("start_initDate") String start_initDate,
			@RequestParam("end_initDate") String end_initDate,
			@RequestParam("start_ctime") String start_ctime,
			@RequestParam("end_ctime") String end_ctime,
			@RequestParam("status") String status) throws ParseException {
		//System.out.println(searchData.getStartDate());
		Map<String, Object> map = sellStatisticsService.page(page,size,sort,order,keywords,code,
				productName,customerName,start_initDate,end_initDate, start_ctime,end_ctime, status);
		
		return map;
		
	}
}
