package com.zoo.mapper.erp.cost;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.cost.CostDetail;

@Component
public interface CostDetailMapper {
	int addCostDetail(@Param("detail")CostDetail detail);

	List<CostDetail> getDetailByCostId(@Param("costId")String costId);

	void deleteDetailById(@Param("id")String id);
}
