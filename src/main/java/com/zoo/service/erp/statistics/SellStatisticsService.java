package com.zoo.service.erp.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.mapper.erp.statistics.SellStatisticsMapper;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.statistics.PurchaseStatistics;
import com.zoo.model.erp.statistics.SellStatistics;
import com.zoo.model.system.user.UserInfo;

import net.sf.json.JSONObject;

@Service
public class SellStatisticsService {

	@Autowired
	private SellStatisticsMapper sellStatisticsMapper;
	
	@Autowired
	private SpecParamMapper paramMapper;
	
	public Map<String, Object> page(Integer page, Integer size) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		Integer start = (page-1)*size;
		
		UserInfo info = LoginInterceptor.getLoginUser();
		
		List<SellStatistics> list = sellStatisticsMapper.page(start, size, info.getCompanyId());
		List<SellStatistics> retList = new ArrayList<SellStatistics>();
		
		for(SellStatistics ps: list) {
			SellStatistics buildSpec = this.buildSpec(ps);
			ps.setProductType(ps.getParentName() + "/" + ps.getName());
			retList.add(buildSpec);
		}
		Long count = sellStatisticsMapper.getCount(info.getCompanyId());
		
		map.put("sellStatisticses", retList);
		map.put("count", count);
		
		return map;
	}
	
	private SellStatistics buildSpec(SellStatistics ps) {
		
		//通用规格参数处理
		String genericSpec = ps.getGenericSpec();
		Map<String,String> map = new HashMap<String, String>();
		JSONObject obj = JSONObject.fromObject(genericSpec);
		Set<String> keySet = obj.keySet();
		for(String key: keySet) {
			SpecParam param = paramMapper.getParamById(key);
			map.put(param.getName(), StringUtils.isBlank(obj.getString(key))?"其它":obj.getString(key));
		}
		ps.setGenericSpec(map.toString());
		String ownSpec = ps.getOwnSpec();
		map = new HashMap<String,String>();
		obj  = JSONObject.fromObject(ownSpec);
		keySet = obj.keySet();
		for(String key:keySet) {
			SpecParam param = paramMapper.getParamById(key);
			map.put(param.getName(), obj.getString(key));
		}
		ps.setOwnSpec(map.toString());
		
		
		return ps;
	}
}
