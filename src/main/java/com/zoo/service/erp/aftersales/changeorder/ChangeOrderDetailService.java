package com.zoo.service.erp.aftersales.changeorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.aftersales.changeorder.ChangeOrderDetailMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.aftersales.changeorder.ChangeOrderDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;

import org.apache.commons.lang3.StringUtils;
import net.sf.json.JSONObject;

@Service
public class ChangeOrderDetailService {

	@Autowired
	ChangeOrderDetailMapper detailMapper;
	
	@Autowired
	SpecParamMapper paramMapper;
	
	/**
	 * 添加数据
	 * @param detail
	 * @return
	 */
	public ChangeOrderDetail addDetail(ChangeOrderDetail detail) {
		
		detail.setId(UUID.randomUUID().toString());
		
		detailMapper.addDetail(detail);
		
		return detail;
		
	}
	
	/**
	 * 更新数据
	 * @param detail
	 * @return
	 */
	public int updateDetail(ChangeOrderDetail detail) {
		
		int num = detailMapper.updateDetail(detail);
		
		return num;
		
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
	 * 更新换货数量
	 * @param id
	 * @param number
	 * @return
	 */
	public int updateNumberById(String id, BigDecimal number) {
		
		int num = detailMapper.updateNumberById(number, id);
		
		return num;
	}
	
	/**
	 * 根据换货单id获取数据
	 * @param changeOrderId
	 * @return
	 */
	public List<ChangeOrderDetail> getDetailByChangeOrderId(String changeOrderId) {
		
		List<ChangeOrderDetail> list = detailMapper.getDetailByChangeOrderId(changeOrderId);
		
		List<String> built = new ArrayList<String>();
		
		for(ChangeOrderDetail detail: list) {
			ProductSku sku = detail.getSku();
			
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
				
				detail.setSku(sku);
				built.add(sku.getProduct().getId());
			}
		}
		return list;
	}
	
}
