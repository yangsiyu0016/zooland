package com.zoo.mapper.erp.cost;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.cost.CostDetail;

@Component
public interface CostDetailMapper {
	int addCostDetail(@Param("detail")CostDetail detail);

	List<CostDetail> getDetailByCostId(@Param("costId")String costId);

	int deleteDetailById(@Param("id")String id);

	int updateCostDetail(@Param("detail")CostDetail detail);
}
