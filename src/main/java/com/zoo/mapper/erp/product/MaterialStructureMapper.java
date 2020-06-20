package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.MaterialStructure;

@Component
public interface MaterialStructureMapper {
	public List<MaterialStructure> getMaterialStructureByPage(@Param("start")Integer start,@Param("size")Integer size,@Param("companyId")String companyId);
	Long getCount(@Param("companyId")String companyId);
	int addMaterialStructure(@Param("ms") MaterialStructure ms);
	int updateMaterialStructure(@Param("ms") MaterialStructure ms);
	MaterialStructure getMaterialStructureByProductId(@Param("productId")String productId);
	MaterialStructure getMaterialStructureById(@Param("id")String id);
	
	int deleteMaterialStructureById(@Param("ids")String[] ids);
}
