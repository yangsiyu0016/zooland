package com.zoo.service.erp;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.GeneratorCodeMapper;
import com.zoo.model.erp.GeneratorCode;

@Service
@Transactional
public class GeneratorCodeService {
	@Autowired
	GeneratorCodeMapper generatorCodeMapper;
	public GeneratorCode getGCode(String prefix, int length) {
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("prefix", prefix);
		condition.put("length", length);
		condition.put("companyId", LoginInterceptor.getLoginUser().getCompanyId());
		return generatorCodeMapper.getGeneratorCodeByCondition(condition);
	}
	public void updateNumber(String id, int number) {
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", id);
		condition.put("number", number);
		generatorCodeMapper.updateNumber(condition);
	}
	public void addGeneratorCode(GeneratorCode generatorCode) {
		generatorCodeMapper.insertGeneratorCode(generatorCode);
		
	}

}
