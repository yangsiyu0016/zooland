package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.Product;

@Component
public interface ProductMapper {
	List<Product> getProductByPage(
			@Param("start") Integer start,
			@Param("size") Integer size,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("keywords") String keywords,
			@Param("typeId") String typeId,
			@Param("brandId") String brandId,
			@Param("name") String name,
			@Param("code") String code,
			@Param("mnemonic") String mnemonic,
			@Param("companyId")String companyId);
	Long getCount(
			@Param("keywords") String keywords, 
			@Param("typeId") String typeId,
			@Param("brandId") String brandId,
			@Param("name") String name,
			@Param("code") String code,
			@Param("mnemonic") String mnemonic,
			@Param("companyId")String companyId);
	int addProduct(@Param("product")Product product);
	int updateProduct(@Param("product")Product product);
	Product getProductById(@Param("id")String id);
	int deleteProductById(@Param("ids")String[] ids);
	List<Product> getProductByTypeId(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId, @Param("typeId") String typeId);
	Long getCountByTypeId(@Param("companyId") String companyId, @Param("typeId") String typeId);
	
	int updateHasBom(@Param("id")String id,@Param("hasBom") Boolean hasBom);
}
