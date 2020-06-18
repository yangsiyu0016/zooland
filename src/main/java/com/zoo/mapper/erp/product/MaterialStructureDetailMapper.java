package com.zoo.mapper.erp.product;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.MaterialStructureDetail;

@Component
public interface MaterialStructureDetailMapper {
	int addMaterialStructureDetail(@Param("msd") MaterialStructureDetail msd);
}
