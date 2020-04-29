package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.ProductSku;

@Component
public interface ProductSkuMapper {
	int addSku(@Param("sku") ProductSku sku);

	List<ProductSku> getSkuByProductId(@Param("productId")String productId);

	List<ProductSku> getProductSkuByPage(@Param("start")Integer start, @Param("size")Integer size, @Param("key")String key, @Param("companyId")String companyId);
	Long getCount(@Param("key")String key, @Param("companyId")String companyId);

	ProductSku getSkuById(@Param("id")String id);
}
