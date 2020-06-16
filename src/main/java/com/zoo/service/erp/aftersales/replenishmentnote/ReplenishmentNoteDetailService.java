package com.zoo.service.erp.aftersales.replenishmentnote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.aftersales.replenishmentnote.ReplenishmentNoteDetailMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNoteDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;

import net.sf.json.JSONObject;

@Service
public class ReplenishmentNoteDetailService {

	@Autowired
	ReplenishmentNoteDetailMapper detailMapper;
	
	@Autowired
	SpecParamMapper paramMapper;
	
	/**
	 * 添加数据
	 * @param detail
	 * @return
	 */
	public ReplenishmentNoteDetail addDatail(ReplenishmentNoteDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		
		detailMapper.addDetail(detail);
		return detail;
	}
	
	/**
	 * 更新数据
	 * @param detail
	 * @return
	 */
	public int updateDetail(ReplenishmentNoteDetail detail) {
		return detailMapper.updateDetail(detail);
	}
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	public int deleteDetailById(String ids) {
		String[] split = ids.split(",");
		int num = detailMapper.deleteDetailById(split);
		return num;
	}
	
	/**
	 * 更新补货数量，单价，总额
	 * @param id
	 * @param number
	 * @return
	 */
	public int updateNumberById(String id, ReplenishmentNoteDetail detail) {
		BigDecimal number = detail.getNumber();
		BigDecimal costPrice = detail.getCostPrice();
		BigDecimal totalMoney = number.multiply(costPrice);
		return detailMapper.updateNumberById(id, number, costPrice, totalMoney);
	}
	
	/**
	 * 根据补货单id获取数据
	 * @param changeOrderId
	 * @return
	 */
	public List<ReplenishmentNoteDetail> getDetailByReplenishmentNoteId(String replenishmentNoteId) {
		List<ReplenishmentNoteDetail> list = detailMapper.getDetailByReplenishmentNoteId(replenishmentNoteId);
		
		List<String> built = new ArrayList<String>();
		
		for(ReplenishmentNoteDetail detail: list) {
			ProductSku sku = detail.getProductSku();
			
			if(!built.contains(sku.getProduct().getId())) {
				//通用规格参数
				String genericSpec = sku.getProduct().getProductDetail().getGenericSpec();
				
				Map<String,String> map = new HashMap<String, String>();
				JSONObject object = JSONObject.fromObject(genericSpec);
				Set<String> keySet = object.keySet();
				for(String key: keySet) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), StringUtils.isBlank(object.getString(key))?"其它":object.getString(key));
				}
				sku.getProduct().getProductDetail().setGenericSpec(map.toString());
				
				String ownSpec = sku.getOwnSpec();
				 map = new HashMap<String,String>();
				 object  = JSONObject.fromObject(ownSpec);
				 keySet = object.keySet();
				for(String key:keySet) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), object.getString(key));
				}
				sku.setOwnSpec(map.toString());
				
				detail.setProductSku(sku);
				built.add(sku.getProduct().getId());
			}
		}
		return list;
	}
	
}
