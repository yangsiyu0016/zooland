package com.zoo.service.erp.product;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
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
		Long count = unitMapper.getCountByRepeat(unit.getName());
		if(count > 0) {//重复
			throw new ZooException(ExceptionEnum.UNIT_NAME_REPEAT);
		}else {//不重复
			unitMapper.addUnit(unit);
		}
	}

	public List<Unit> getUnitByPage(Integer page, Integer size, String keywords) {
		// TODO Auto-generated method stub
		Integer start = 0;
		if (page > 0) {
			start = (page - 1) * size;
		}
		return unitMapper.getUnitByPage(start, size, keywords);
	}
	public long getCount(String keywords) {
		return unitMapper.getCount(keywords);
	}

	/**
	 * 修改
	 * @param unit
	 */
	public void update(Unit unit) {
		// TODO Auto-generated method stub
		Long count = unitMapper.getCountByRepeatOfEdit(unit.getName(), unit.getId());
		if(count > 0) {
			throw new ZooException(ExceptionEnum.UNIT_NAME_REPEAT);
		}else {
			unitMapper.update(unit);
		}
	}
	
	public Unit getUnitById(String id) {
		return unitMapper.getUnitById(id);
	}

	public void delete(String ids) {
		// TODO Auto-generated method stub
		String[] split = ids.split(",");
		unitMapper.delete(split);
	}
}
