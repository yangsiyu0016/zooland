package com.zoo.service.erp.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.product.ProductBrandMapper;
import com.zoo.mapper.erp.product.ProductTypeMapper;
import com.zoo.model.erp.product.ProductBrand;
import com.zoo.model.erp.product.ProductType;
import com.zoo.model.system.user.UserInfo;

@Service
@Transactional
public class ProductBrandService {
	@Autowired
	ProductBrandMapper productBrandMapper;
	@Autowired
	ProductTypeMapper typeMapper;
	public List<ProductBrand> getBrandByPage(Integer page,Integer size){
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		int start = (page-1)*size;
		List<ProductBrand> brands = productBrandMapper.getBrandByPage(start, size, uinfo.getCompanyId());
		buildTypes(brands);
		return brands;
	}
	private void buildTypes(List<ProductBrand> brands) {
		for(ProductBrand brand:brands) {
			List<List<String>> typeIds = new ArrayList<List<String>>();
			List<ProductType> types = typeMapper.getTypeByBrandId(brand.getId());
			for(ProductType type:types) {
				List<String> typeIdList = new LinkedList<String>();
				typeIdList.add(type.getId());
				if(StringUtils.isNotBlank(type.getParentId())) {
					addParent(typeIdList,type.getParentId());
				}
				Collections.reverse(typeIdList);
				typeIds.add(typeIdList);
			}
			brand.setTypeIds(typeIds);
			
		}
		
	}
	private void addParent(List<String> typeIdList, String id) {
		ProductType type = typeMapper.getTypeById(id);
		typeIdList.add(type.getId());
		if(StringUtils.isNotBlank(type.getParentId())) {
			addParent(typeIdList,type.getParentId());
		}
	}
	public Long getCount() {
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		return productBrandMapper.getCount(uinfo.getCompanyId());
	}
	public void addProductBrand(ProductBrand brand) {
		brand.setId(UUID.randomUUID().toString());
		brand.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		productBrandMapper.addProductBrand(brand);
		//for(String typeId:typeIds) {
			//productBrandMapper.addBT(UUID.randomUUID().toString(),brand.getId(),typeId);
		//}
		
	}
	public int updateProductBrand(ProductBrand brand,String[] typeIds) {
		productBrandMapper.deleteType(brand.getId());
		for(String typeId:typeIds) {
			productBrandMapper.addBT(UUID.randomUUID().toString(),brand.getId(),typeId);
		}
		return productBrandMapper.updateProductBrand(brand);
	}
	public List<ProductBrand> getBrandByTypeId(String typeId) {

		return productBrandMapper.getBrandByTypeId(typeId);
	}
	public List<ProductBrand> getList() {
		// TODO Auto-generated method stub
		return productBrandMapper.getBrandList(LoginInterceptor.getLoginUser().getCompanyId());
	}
}
