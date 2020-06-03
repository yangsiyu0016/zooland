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
import com.zoo.mapper.erp.statistics.PurchaseStatisticsMapper;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.statistics.PurchaseStatistics;
import com.zoo.model.system.user.UserInfo;

import net.sf.json.JSONObject;

@Service
public class PurchaseStatisticsService {

	@Autowired
	private PurchaseStatisticsMapper purchaseStatisticsMapper;
	
	@Autowired
	private SpecParamMapper paramMapper;
	
	public Map<String, Object> page(Integer page, Integer size) {
		Integer start = (page - 1) * size;
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		
		UserInfo userInfo = LoginInterceptor.getLoginUser();
		
		List<PurchaseStatistics> list = purchaseStatisticsMapper.page(start, size, userInfo.getCompanyId());
		
		Long count = purchaseStatisticsMapper.getCount(userInfo.getCompanyId());
		
		List<PurchaseStatistics> retList = new ArrayList<PurchaseStatistics>();
		
		for(PurchaseStatistics ps: list) {
			PurchaseStatistics buildSpec = this.buildSpec(ps);
			
			retList.add(buildSpec);
		}
		map.put("purchaseStatisticses", retList);
		map.put("count", count);
		return map;
	}
	
	private PurchaseStatistics buildSpec(PurchaseStatistics ps) {
		
		
		
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
