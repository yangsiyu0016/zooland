package com.zoo.service.erp.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.product.ProductDetailMapper;
import com.zoo.mapper.erp.product.ProductMapper;
import com.zoo.mapper.erp.product.ProductSkuMapper;
import com.zoo.mapper.erp.product.ProductTypeMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.ProductType;
import com.zoo.model.erp.product.SpecParam;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Service
@Transactional
public class ProductSkuService {
	@Autowired
	ProductSkuMapper skuMapper;
	@Autowired
	SpecParamMapper paramMapper;
	@Autowired
	ProductMapper productMapper;
	@Autowired
	ProductTypeMapper typeMapper;
	@Autowired
	ProductDetailMapper detailMapper;
	public List<ProductSku> getProductSkuByPage(Integer page, Integer size, String key) {
		int start = (page-1)*size;
		String companyId = LoginInterceptor.getLoginUser().getCompanyId();
		List<ProductSku> skus = skuMapper.getProductSkuByPage(start,size,key,companyId);
		skus = buildSpecAndTypeName(skus);
		skus = buildGenericSpec(skus);
		return skus;
	}
	
	public long getCount(String key) {
		// TODO Auto-generated method stub
		return skuMapper.getCount(key, LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	private List<ProductSku> buildGenericSpec(List<ProductSku> skus){
		List<String> built = new ArrayList<String>();
		for(ProductSku sku:skus) {
			if(!built.contains(sku.getProduct().getId())){
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
				built.add(sku.getProduct().getId());
			}
			
		}
		return skus;
	}
	
	public List<ProductSku> getProductByProductId(String productId) {
		List<ProductSku> list = skuMapper.getSkuByProductId(productId);
		
		ProductSku sku = list.get(0);
		String spec = sku.getProduct().getProductDetail().getSpecialSpec();
		JSONObject object = JSONObject.fromObject(spec);
		Set<String> keySet = object.keySet();
		Map<String,Object> map = new HashMap<String, Object>();
		for(String key: keySet) {
			SpecParam param = paramMapper.getParamById(key);
			map.put(param.getName(), object.getString(key));
		}
		JSONObject fromObject = JSONObject.fromObject(map);
		String string = fromObject.toString();
		sku.getProduct().getProductDetail().setSpecialSpec(string);
		
		for(ProductSku sku1: list) {
			String ownSpec = sku1.getOwnSpec();
			Map<String,String> map1 = new HashMap<String,String>();
			JSONObject obj  = JSONObject.fromObject(sku1.getOwnSpec());
			Set<String> keyset = obj.keySet();
			for(String key:keyset) {
				SpecParam param = paramMapper.getParamById(key);
				map1.put(param.getName(), obj.getString(key));
			}
			JSONObject fromObject2 = JSONObject.fromObject(map1);
			String string2 = fromObject2.toString();
			sku1.setOwnSpec(string2);
		}
		return list;
	}
	
	private List<ProductSku>  buildSpecAndTypeName(List<ProductSku> skus) {
		for(ProductSku sku:skus) {
			//System.out.println(sku.getId());
			//特殊规格参数处理
			String ownSpec = sku.getOwnSpec();
			Map<String,String> map = new HashMap<String,String>();
			JSONObject obj  = JSONObject.fromObject(ownSpec);
			Set<String> keyset = obj.keySet();
			for(String key:keyset) {
				SpecParam param = paramMapper.getParamById(key);
				map.put(param.getName(), obj.getString(key));
			}
			sku.setOwnSpec(map.toString());
			
			//分类名称处理
			
			ProductType type = typeMapper.getTypeById(sku.getProduct().getTypeId());
			String name = buildTypeName(type.getName(),type);
			sku.getProduct().setTypeName(name);
		}
		return skus;
	}
	private String buildTypeName(String name,ProductType type) {
		if(!StringUtils.isBlank(type.getParentId())) {
			ProductType parentType = typeMapper.getTypeById(type.getParentId());
			name=parentType.getName()+"/"+name;
			buildTypeName(name,parentType);
		}
		return name;
	}
	
}
