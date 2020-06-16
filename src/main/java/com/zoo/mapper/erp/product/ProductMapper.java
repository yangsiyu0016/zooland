package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.Product;

@Component
public interface ProductMapper {
	List<Product> getProductByPage(@Param("start") Integer start,@Param("size") Integer size,@Param("companyId")String companyId);
	Long getCount(@Param("companyId")String companyId);
	int addProduct(@Param("product")Product product);
	int updateProduct(@Param("product")Product product);
	Product getProductById(@Param("id")String id);
}
