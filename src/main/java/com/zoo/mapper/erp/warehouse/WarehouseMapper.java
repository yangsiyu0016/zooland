package com.zoo.mapper.erp.warehouse;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.warehouse.Warehouse;

@Component
public interface WarehouseMapper {
	List<Warehouse> getWarehouseByPage(@Param("start") Integer start,@Param("size") Integer size,@Param("companyId") String companyId);
	Long getCount(@Param("companyId") String companyId);
	int addWarehouse(@Param("warehouse")Warehouse warehouse);
	int addManager(@Param("id")String id,@Param("warehouseId")String warehouseId,@Param("userId")String userId);
	int updateWarehouse(@Param("warehouse") Warehouse warehouse);
	int deleteWUByWarehouseId(@Param("warehouseId")String warehouseId);
	Warehouse getWarehouseById(@Param("id")String id);
}
