package com.zoo.mapper.erp.warehouse;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.warehouse.StockDetail;

@Component
public interface StockDetailMapper {

	int addDetail(@Param("detail")StockDetail detail);

	StockDetail getDetail(@Param("stockId")String stockId, @Param("goodsAllocationId")String goodsAllocationId);

	int updateStockDetail(@Param("detail")StockDetail detail);

}
