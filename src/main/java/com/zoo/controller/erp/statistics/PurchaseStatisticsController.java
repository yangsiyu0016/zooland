package com.zoo.controller.erp.statistics;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("search")
	public Map<String, Object> search(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size,
			@RequestParam("sort") String sort,
			@RequestParam("order") String order,
			@RequestParam("keywords") String keywords,
			@RequestParam("code") String code,
			@RequestParam("productName") String productName,
			@RequestParam("supplierName") String supplierName,
			@RequestParam("start_initDate") String start_initDate,
			@RequestParam("end_initDate") String end_initDate,
			@RequestParam("start_ctime") String start_ctime,
			@RequestParam("end_ctime") String end_ctime,
			@RequestParam("status") String status) {
		return purchaseStatisticsService.search(page,size,sort,order,keywords,code,productName,supplierName,start_initDate,end_initDate,start_ctime,end_ctime,status);
	}
	
}
