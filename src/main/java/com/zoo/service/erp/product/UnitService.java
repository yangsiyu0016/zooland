package com.zoo.service.erp.product;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.product.UnitMapper;
import com.zoo.model.erp.product.Unit;


@Service
@Transactional
public class UnitService {
	@Autowired
	UnitMapper unitMapper;
	public List<Unit> getUnitList(){
		return unitMapper.getUnitList();
	}
	
	public void addUnit(Unit unit) {
		unit.setId(UUID.randomUUID().toString());
		unitMapper.addUnit(unit);
	}
}
