package com.zoo.service.erp.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.product.ProductTypeMapper;
import com.zoo.model.erp.product.ProductType;
import com.zoo.model.system.user.UserInfo;

@Service
@Transactional
public class ProductTypeService {
	@Autowired
	ProductTypeMapper productTypeMapper;
	public void addProductType(ProductType type) {
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		type.setId(UUID.randomUUID().toString());
		type.setCompanyId(uinfo.getCompanyId());
		productTypeMapper.addProductType(type);
	}
	public List<ProductType> getTreeData() {
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		List<ProductType>  types = productTypeMapper.getTreeData(uinfo.getCompanyId());
		for(ProductType type:types) {
			if(type.getChildren().size()>0) {
				setNull(type);
			}else {
				type.setChildren(null);
			}
		}
		return types;
	}
	private void setNull(ProductType type) {
		for(ProductType t:type.getChildren()) {
			if(t.getChildren().size()>0) {
				setNull(t);
			}else {
				t.setChildren(null);
			}
		}
		
	}
	public void updateProductType(ProductType type) {
		productTypeMapper.updateProductType(type);
	}
}
