package com.zoo.mapper.erp.cost;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.cost.CostDetail;

@Component
public interface CostDetailMapper {
	int addCostDetail(@Param("detail")CostDetail detail);
}
