package com.zoo.mapper.erp.inventorycheck;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.inventorycheck.InventoryCheck;

@Component
public interface InventoryCheckMapper {

	
	//分页查询
	public List<InventoryCheck> getInventoryCheckByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId, @Param("cuserId") String cuserId);
	
	//获取总数
	public Long getCount(@Param("companyId") String companyId, @Param("cuserId") String cuserId);
	
	//根据id获取单个数据
	public InventoryCheck getInventoryCheckById(@Param("id") String id);
	
	//添加数据
	public void addInventoryCheck(@Param("inventoryCheck") InventoryCheck inventoryCheck);
	
	//修改数据
	public int updateInventoryCheck(@Param("inventoryCheck") InventoryCheck inventoryCheck);
	
	//更改流程id
	public int updateProcessInstanceId(@Param("id") String id,@Param("processInstanceId")String processInstanceId);
	
	//更改订单状态
	public int updateInventoryCheckStatus(@Param("condition")Map<String, Object> condition);
}
