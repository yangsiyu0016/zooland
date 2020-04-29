package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.SpecParam;

@Component
public interface SpecParamMapper {
	int addParam(@Param("param") SpecParam param);

	List<SpecParam> getSpecParams(@Param("typeId")String typeId);

	SpecParam getParamById(@Param("id")String id);

	List<SpecParam> getParamsByGroupId(@Param("groupId")String groupId);

	int updateParam(@Param("param")SpecParam param);
}
