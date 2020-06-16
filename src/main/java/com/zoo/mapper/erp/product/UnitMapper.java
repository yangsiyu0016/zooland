package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.product.Unit;

@Component
public interface UnitMapper {
	List<Unit> getUnitList();
	int addUnit(@Param("unit")Unit unit);
}
