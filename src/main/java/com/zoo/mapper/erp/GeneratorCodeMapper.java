package com.zoo.mapper.erp;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.zoo.model.erp.GeneratorCode;

@Component
public interface GeneratorCodeMapper {

	GeneratorCode getGeneratorCodeByCondition(Map<String, Object> condition);
	public int insertGeneratorCode(GeneratorCode generatorCode);
	public int updateNumber(Map<String,Object> condtion);
}
