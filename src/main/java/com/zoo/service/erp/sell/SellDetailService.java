package com.zoo.service.erp.sell;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.mapper.erp.sell.SellDetailMapper;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.sell.SellDetail;

import net.sf.json.JSONObject;

@Service
@Transactional
public class SellDetailService {
	@Autowired
	SellDetailMapper detailMapper;
	@Autowired
	SpecParamMapper paramMapper;
	public List<SellDetail> getSellDetailBySellId(String id) {
		// TODO Auto-generated method stub
		return detailMapper.getDetailBySellId(id);
	}
	public List<SellDetail> getNotOutDetailBySellId(String sellId) {
		List<SellDetail> details = detailMapper.getNotOutDetailBySellId(sellId);
		for(SellDetail detail:details) {
			ProductSku sku = detail.getProductSku();
			//通用规格参数处理
			String genericSpec = sku.getProduct().getProductDetail().getGenericSpec();
			Map<String,String> map = new HashMap<String,String>();
			JSONObject obj = JSONObject.fromObject(genericSpec);
			Set<String> keyset = obj.keySet();
			for(String key:keyset) {
				SpecParam param = paramMapper.getParamById(key);
				map.put(param.getName(), StringUtils.isBlank(obj.getString(key))?"其它":obj.getString(key));
			}
			sku.getProduct().getProductDetail().setGenericSpec(map.toString());
			
			
			String ownSpec = sku.getOwnSpec();
			 map = new HashMap<String,String>();
			 obj  = JSONObject.fromObject(ownSpec);
			keyset = obj.keySet();
			for(String key:keyset) {
				SpecParam param = paramMapper.getParamById(key);
				map.put(param.getName(), obj.getString(key));
			}
			sku.setOwnSpec(map.toString());
			
			detail.setProductSku(sku);
		}
		return details;
	}
	public SellDetail addDetail(SellDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		detail.setCtime(new Date());
		detailMapper.addDetail(detail);
		return detail;
	}
	public void updateDetail(SellDetail detail) {
		detailMapper.updateDetail(detail);
		
	}
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		detailMapper.deleteDetailById(split);
		
	}

}
