package com.zoo.service.erp.statistics;

import java.text.ParseException;
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

	public Map<String, Object> page(SearchData searchData) throws ParseException {
		Integer start = (searchData.getPage() - 1) * searchData.getSize();
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //格式化规则
		 * if(searchData.getStartDate() != null && searchData.getEndDate() != null) {
		 * Date startDate = searchData.getStartDate(); Date endDate =
		 * searchData.getEndDate();
		 * searchData.setStartDate(sdf.parse(sdf.format(startDate)));
		 * searchData.setEndDate(sdf.parse(sdf.format(endDate)));
		 * System.out.println(sdf.parse(sdf.format(startDate))); }
		 */
		
		List<SellStatistics> list = sellStatisticsMapper.page(searchData, start, searchData.getSize(), userInfo.getCompanyId());
		
		Long count = sellStatisticsMapper.getCount(searchData, userInfo.getCompanyId());
		
		map.put("sellStatisticses", list);
		map.put("count", count);
		
		return map;
	}
}
