package com.zoo.mapper.erp.cost;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.cost.Cost;

@Component
public interface CostMapper {
	List<Cost> getCostByForeignKey(@Param("foreignKey") String foreignKey);
	int addCost(@Param("cost")Cost cost);
	Cost getCostById(@Param("id")String id);
}
