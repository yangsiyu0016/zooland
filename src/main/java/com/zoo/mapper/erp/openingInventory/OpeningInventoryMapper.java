package com.zoo.mapper.erp.openingInventory;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.openingInventory.OpeningInventory;

@Component
public interface OpeningInventoryMapper {
	public List<OpeningInventory> getOpeningInventoryByPage(@Param("start")Integer start,@Param("size")Integer size,@Param("companyId")String companyId,@Param("cuserId")String cuserId);
	public Long getCount(@Param("companyId")String companyId,@Param("cuserId")String cuserId);
	public OpeningInventory getOpeningInventoryById(@Param("id")String id);
	public int addOpeningInventory(@Param("oi")OpeningInventory oi);
	int updateProcessInstanceId(@Param("id") String id,@Param("processInstanceId")String processInstanceId);
	public int updateOpeningInventoryStatus(@Param("condition")Map<String, Object> condition);
	int updateOpeningInventory(@Param("oi")OpeningInventory oi);
}
