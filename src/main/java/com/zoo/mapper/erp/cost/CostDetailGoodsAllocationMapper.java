package com.zoo.mapper.erp.cost;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.cost.CostDetailGoodsAllocation;

@Component
public interface CostDetailGoodsAllocationMapper {
	int addCostDetailGoodsAllocation(@Param("costDetailGoodsAllocation") CostDetailGoodsAllocation costDetailGoodsAllocation);
	int deleteCostDetailGoodsAllocationById(@Param("id") String id);
}
