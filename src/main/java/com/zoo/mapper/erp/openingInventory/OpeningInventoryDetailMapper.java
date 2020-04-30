package com.zoo.mapper.erp.openingInventory;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.openingInventory.OpeningInventoryDetail;

@Component
public interface OpeningInventoryDetailMapper {
	int addDetail(@Param("detail")OpeningInventoryDetail detail);

	int updatePrice(@Param("id")String id, @Param("costPrice")BigDecimal costPrice,@Param("totalMoney") BigDecimal totalMoney);

	//List<OpeningInventoryDetail> getDetailsByOpeningInventoryId(@Param("openingInventoryId")String openingInventoryId);
	int deleteDetailById(@Param("ids")String[] ids);

	int updateDetail(@Param("detail")OpeningInventoryDetail detail);
}
