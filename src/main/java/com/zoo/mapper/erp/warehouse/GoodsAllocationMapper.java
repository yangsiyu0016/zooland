package com.zoo.mapper.erp.warehouse;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.warehouse.GoodsAllocation;

@Component
public interface GoodsAllocationMapper {
	List<GoodsAllocation> getGoodsAllocationByPage(@Param("start")Integer start,@Param("size")Integer size,@Param("warehouseId")String warehouseId);
	Long getCount(@Param("warehouseId") String warehouseId);
	int addGoodsAllocation(@Param("ga")GoodsAllocation ga);
	int updateGoodsAllocation(@Param("ga") GoodsAllocation ga);
}
