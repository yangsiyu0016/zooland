package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.ProductType;

@Component
public interface ProductTypeMapper {
	int addProductType(@Param("type") ProductType type);

	List<ProductType> getTreeData(@Param("companyId")String companyId);
	int updateProductType(@Param("type") ProductType type);

	ProductType getTypeById(@Param("id")String id);

	List<ProductType> getTypeByBrandId(@Param("brandId")String brandId);

	List<ProductType> getTypeByIds(@Param("ids")List<String> ids);
}
