package com.zoo.mapper.erp.product;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.ProductDetail;

@Component
public interface ProductDetailMapper {
	int addProductDetail(@Param("productDetail")ProductDetail productDetail);

	ProductDetail getProductDetailById(@Param("id")String id);
	
}
