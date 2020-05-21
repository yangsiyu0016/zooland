package com.zoo.mapper.erp.inventorycheck;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.inventorycheck.InventoryCheckDetail;

@Component
public interface InventoryCheckDetailMapper {

	//添加
	public int addDetail(@Param("detail")InventoryCheckDetail detail);
	
	//修改价格
	public int updatePrice(@Param("id")String id, @Param("costPrice")BigDecimal costPrice,@Param("totalMoney") BigDecimal totalMoney);
	
	//根据id修改
	public int deleteDetailById(@Param("ids")String[] ids);

	//
	public int updateDetail(@Param("detail")InventoryCheckDetail detail);
	
}
