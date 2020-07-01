package com.zoo.service.erp.cost;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.cost.CostDetailMapper;
import com.zoo.model.erp.cost.CostDetail;

@Service
@Transactional
public class CostDetailService {
	@Autowired
	CostDetailMapper detailMapper;
	@Autowired
	CostDetailGoodsAllocationService cdgaService;
	public int addCostDetail(CostDetail detail) {
		return detailMapper.addCostDetail(detail);
		
	}
	public int updateCostDetail(CostDetail detail) {
		return detailMapper.updateCostDetail(detail);
		
	}
	public List<CostDetail> getDetailByCostId(String costId) {
		// TODO Auto-generated method stub
		return detailMapper.getDetailByCostId(costId);
	}
	public void deleteDetailById(String id) {
		cdgaService.deleteCostDetailGoodsAllocationByDetailId(id);
		detailMapper.deleteDetailById(id);
	}

}
