package com.zoo.service.erp.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.statistics.SellStatisticsMapper;
import com.zoo.model.erp.statistics.SearchData;
import com.zoo.model.erp.statistics.SellStatistics;
import com.zoo.model.system.user.UserInfo;


@Service
public class SellStatisticsService {

	@Autowired
	private SellStatisticsMapper sellStatisticsMapper;

	public Map<String, Object> page(Integer page, Integer size) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		Integer start = (page-1)*size;
		
		UserInfo info = LoginInterceptor.getLoginUser();
		
		List<SellStatistics> list = sellStatisticsMapper.page(start, size, info.getCompanyId());
		List<SellStatistics> retList = new ArrayList<SellStatistics>();
		
		for(SellStatistics ps: list) {
			ps.setProductType(ps.getParentName() + "/" + ps.getName());
		}
		Long count = sellStatisticsMapper.getCount(info.getCompanyId());
		
		map.put("sellStatisticses", retList);
		map.put("count", count);
		
		return map;
	}

	public Map<String, Object> search(SearchData searchData) {
		// TODO Auto-generated method stub
		return null;
	}

}
