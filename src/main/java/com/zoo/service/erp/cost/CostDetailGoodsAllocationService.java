package com.zoo.service.erp.cost;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.cost.CostDetailGoodsAllocationMapper;
import com.zoo.model.erp.cost.CostDetailGoodsAllocation;

@Service
@Transactional
public class CostDetailGoodsAllocationService {
	@Autowired
	CostDetailGoodsAllocationMapper costDetailGoodsAllocationMapper;
	public CostDetailGoodsAllocation addCostDetailGoodsAllocation(CostDetailGoodsAllocation costDetailGoodsAllocation) {
		costDetailGoodsAllocation.setId(UUID.randomUUID().toString());
		costDetailGoodsAllocationMapper.addCostDetailGoodsAllocation(costDetailGoodsAllocation);
		return costDetailGoodsAllocation;
	}
	public void deleteCostDetailGoodsAllocation(String id) {
		costDetailGoodsAllocationMapper.deleteCostDetailGoodsAllocationById(id);
	}

}
