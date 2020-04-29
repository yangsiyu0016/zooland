package com.zoo.service.erp.warehouse;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.model.erp.warehouse.GoodsAllocation;

@Service
@Transactional
public class GoodsAllocationService {
	@Autowired
	GoodsAllocationMapper goodsAllocationMapper;
	public List<GoodsAllocation> getGoodsAllocationByPage(Integer page,Integer size,String warehouseId){
		Integer start = null ;
		if(page!=null&&size!=null) {
			start = (page-1)*size;
		}
		return goodsAllocationMapper.getGoodsAllocationByPage(start, size, warehouseId);
	}
	public Long getCount(String warehouseId) {
		return goodsAllocationMapper.getCount(warehouseId);
	}
	public GoodsAllocation addGoodsAllocation(GoodsAllocation ga) {
		ga.setId(UUID.randomUUID().toString());
		goodsAllocationMapper.addGoodsAllocation(ga);
		return ga;
	}
	public int updateGoodsAllocation(GoodsAllocation ga) {
		return goodsAllocationMapper.updateGoodsAllocation(ga);
		
	}
}
