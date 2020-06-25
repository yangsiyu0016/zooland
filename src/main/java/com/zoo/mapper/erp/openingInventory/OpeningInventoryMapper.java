package com.zoo.mapper.erp.openingInventory;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.openingInventory.OpeningInventory;

@Component
public interface OpeningInventoryMapper {
	public List<OpeningInventory> getOpeningInventoryByPage(
			@Param("start")Integer start,
			@Param("size")Integer size,
			@Param("companyId")String companyId,
			@Param("cuserId")String cuserId,
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("status")String status,
			@Param("warehouseId")String warehouseId,
			@Param("start_initDate")String start_initDate,
			@Param("end_initDate")String end_initDate,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime,
			@Param("sort")String sort,
			@Param("order")String order);
	public Long getCount(
			@Param("companyId")String companyId,
			@Param("cuserId")String cuserId,
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("status")String status,
			@Param("warehouseId")String warehouseId,
			@Param("start_initDate")String start_initDate,
			@Param("end_initDate")String end_initDate,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime);
	public OpeningInventory getOpeningInventoryById(@Param("id")String id);
	public int addOpeningInventory(@Param("oi")OpeningInventory oi);
	int updateProcessInstanceId(@Param("id") String id,@Param("processInstanceId")String processInstanceId);
	public int updateOpeningInventoryStatus(@Param("condition")Map<String, Object> condition);
	int updateOpeningInventory(@Param("oi")OpeningInventory oi);
	int updateOpeningInventoryIsClaimed(@Param("condition")Map<String, Object> condition);
	
	int deleteOiById(@Param("ids")String[] ids);
}
