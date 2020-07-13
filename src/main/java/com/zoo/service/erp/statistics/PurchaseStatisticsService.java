package com.zoo.service.erp.statistics;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.statistics.PurchaseStatisticsMapper;
import com.zoo.model.erp.statistics.PurchaseStatistics;
import com.zoo.model.system.user.UserInfo;


@Service
public class PurchaseStatisticsService {

	@Autowired
	private PurchaseStatisticsMapper purchaseStatisticsMapper;
	
	public Map<String, Object> page(Integer page, Integer size) {
		Integer start = (page - 1) * size;
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		
		List<PurchaseStatistics> list = purchaseStatisticsMapper.page(start, size, userInfo.getCompanyId());
		
		Long count = purchaseStatisticsMapper.getCount(userInfo.getCompanyId());
		
		map.put("purchaseStatisticses", list);
		map.put("count", count);
		return map;
	}
	
	/**
	 * 查询
	 * @param searchData
	 * @param page
	 * @param size
	 * @return
	 */
	public Map<String, Object> search(Integer page,
				Integer size,
				String sort,
				String order,
				String keywords,
				String code,
				String productName,
				String supplierName,
				String start_initDate,
				String end_initDate,
				String start_ctime,
				String end_ctime,
				String status) {
		// TODO Auto-generated method stub
		Integer start = 0;
		if(page >0) {
			start = (page - 1) * size;
		}
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		
		List<PurchaseStatistics> list = purchaseStatisticsMapper.search(start, size, sort, order, keywords, code, productName, supplierName, start_initDate, end_initDate, start_ctime, end_ctime, status, userInfo.getCompanyId());
		
		Long count = purchaseStatisticsMapper.getSearchCount(sort, order, keywords, code, productName, supplierName, start_initDate, end_initDate, start_ctime, end_ctime, status, userInfo.getCompanyId());
		//获取导出采购统计数据
		List<PurchaseStatistics> exportPurchaseStatistics = purchaseStatisticsMapper.search(null, null, sort, order, keywords, code, productName, supplierName, start_initDate, end_initDate, start_ctime, end_ctime, status, userInfo.getCompanyId());
		
		//添加合计
		PurchaseStatistics purchaseStatistics = new PurchaseStatistics();
		BigDecimal notInNumber = new BigDecimal("0");
		BigDecimal number = new BigDecimal("0");
		for(PurchaseStatistics statistic: exportPurchaseStatistics) {
			notInNumber = notInNumber.add(statistic.getNotInNumber() == null? new BigDecimal("0") : statistic.getNotInNumber());
			number = number.add(statistic.getNumber() == null? new BigDecimal("0") : statistic.getNumber());
		}
		purchaseStatistics.setProductType("合计");
		purchaseStatistics.setNotInNumber(notInNumber);
		purchaseStatistics.setNumber(number);
		exportPurchaseStatistics.add(purchaseStatistics);
		map.put("purchaseStatisticses", list);
		map.put("count", count);
		map.put("exportPurchaseStatistics", exportPurchaseStatistics);
		return map;
	}
	
}
