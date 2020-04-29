package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.ProductBrand;

@Component
public interface ProductBrandMapper {
	List<ProductBrand> getBrandByPage(@Param("start")Integer start,@Param("size")Integer size,@Param("companyId")String companyId);
	Long getCount(@Param("companyId")String companyId);
	List<ProductBrand> getBrandByIds(@Param("ids")List<String> ids);
	int addProductBrand(@Param("brand")ProductBrand brand);
	int updateProductBrand(@Param("brand")ProductBrand brand);
	int addBT(@Param("id")String id,@Param("brandId")String brandId, @Param("typeId")String typeId);
	List<ProductBrand> getBrandByTypeId(@Param("typeId")String typeId);
	int deleteType(@Param("id")String id);
}
