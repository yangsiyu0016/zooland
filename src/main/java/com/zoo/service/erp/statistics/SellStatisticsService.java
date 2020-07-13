package com.zoo.service.erp.statistics;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.statistics.SellStatisticsMapper;
import com.zoo.model.erp.statistics.SellStatistics;


@Service
public class SellStatisticsService {

	@Autowired
	private SellStatisticsMapper sellStatisticsMapper;


	public Map<String, Object> page(Integer page, Integer size, String sort, String order, String keywords, String code,
			String productName, String customerName, String start_initDate, String end_initDate, String start_ctime,
			String end_ctime, String status) {
		// TODO Auto-generated method stub
		Integer start = (page - 1) * size;
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		List<SellStatistics> list = sellStatisticsMapper.page(start, size, sort, order, keywords, code, productName, customerName, start_initDate, end_initDate, start_ctime, end_ctime, status, LoginInterceptor.getLoginUser().getCompanyId());
		Long count = sellStatisticsMapper.getCount(sort, order, keywords, code, productName, customerName, start_initDate, end_initDate, start_ctime, end_ctime, status, LoginInterceptor.getLoginUser().getCompanyId());
		//获取导出excel文件的数据
		List<SellStatistics> exportSellStatistics = sellStatisticsMapper.page(null, null, sort, order, keywords, code, productName, customerName, start_initDate, end_initDate, start_ctime, end_ctime, status, LoginInterceptor.getLoginUser().getCompanyId());
		//添加合计
		SellStatistics statistics = new SellStatistics();
		BigDecimal notOutNumber = new BigDecimal("0");
		BigDecimal number = new BigDecimal("0");
		for(SellStatistics statistic: exportSellStatistics) {
			notOutNumber = notOutNumber.add(statistic.getNotOutNumber() == null? new BigDecimal("0") : statistic.getNotOutNumber());
			number = number.add(statistic.getNumber() == null? new BigDecimal("0") : statistic.getNumber());
		}
		statistics.setTypeName("合计");
		statistics.setNotOutNumber(notOutNumber);
		statistics.setNumber(number);
		exportSellStatistics.add(statistics);
		map.put("sellStatisticses", list);
		map.put("count", count);
		map.put("exportSellStatistics", exportSellStatistics);
		return map;
	}
}
