package com.zoo.service.erp.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.statistics.PurchaseStatisticsMapper;
import com.zoo.model.erp.statistics.PurchaseStatistics;
import com.zoo.model.erp.statistics.SearchData;
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
	public Map<String, Object> search(SearchData searchData) {
		// TODO Auto-generated method stub
		Integer start = (searchData.getPage() - 1) * searchData.getSize();
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		
		List<PurchaseStatistics> list = purchaseStatisticsMapper.search(searchData, start, searchData.getSize(), userInfo.getCompanyId());
		
		Long count = purchaseStatisticsMapper.getSearchCount(searchData, userInfo.getCompanyId());
		
		
		map.put("purchaseStatisticses", list);
		map.put("count", count);
		return map;
	}
	
}
