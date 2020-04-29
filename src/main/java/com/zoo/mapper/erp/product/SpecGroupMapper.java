package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.SpecGroup;

@Component
public interface SpecGroupMapper {
	List<SpecGroup> getGroupByTypeId(@Param("typeId")String typeId);
	int addGroup(@Param("group") SpecGroup group);
	int updateGroup(@Param("group") SpecGroup group);
}
