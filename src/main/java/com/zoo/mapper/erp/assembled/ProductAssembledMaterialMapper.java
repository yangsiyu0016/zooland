package com.zoo.mapper.erp.assembled;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.assembled.ProductAssembledMaterial;

@Component
public interface ProductAssembledMaterialMapper {
	int addMaterial(@Param("pam")ProductAssembledMaterial pam);
	int deleteMaterialByAllembledId(@Param("productAssembledId")String productAssembledId);
}
