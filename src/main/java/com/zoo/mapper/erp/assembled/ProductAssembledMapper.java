package com.zoo.mapper.erp.assembled;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.assembled.ProductAssembled;

@Component
public interface ProductAssembledMapper {
	List<ProductAssembled> getProductAssembledByPage(@Param("start")Integer start, @Param("size")Integer size, @Param("companyId")String companyId);
	long getCount(@Param("companyId")String companyId);
	int addProductAssembled(@Param("pa")ProductAssembled pa);
	int deleteProductAssembledById(@Param("ids")String[] ids);
}
