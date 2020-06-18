package com.zoo.mapper.erp.product;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.MaterialStructure;

@Component
public interface MaterialStructureMapper {
	int addMaterialStructure(@Param("ms") MaterialStructure ms);
}
