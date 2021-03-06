package com.zoo.mapper.system.parameter;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.parameter.SystemParameter;

@Component
public interface SystemParameterMapper {
	List<SystemParameter> getParameterList(@Param("companyId")String companyId);
	int addParameter(@Param("parameter")SystemParameter parameter);
	void update(@Param("parameter") SystemParameter parameter);
	int delete(@Param("id") String id);
	String getValueByCode(@Param("code")String code);
}
