package com.zoo.mapper.erp.warehouse;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.warehouse.Stock;

@Component
public interface StockMapper {
	List<Stock> getStockByPage(
			@Param("start")Integer start,
			@Param("size")Integer size,
			@Param("keywords")String keywords,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("warehouseId")String warehouseId,
			@Param("companyId") String companyId);
	long getStockCount(@Param("keywords")String keywords,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("warehouseId")String warehouseId,
			@Param("companyId")String companyId);
	Stock getStock(@Param("productId")String productId, @Param("warehouseId")String warehouseId);

	int addStock(@Param("stock")Stock stock);

	int updateStock(@Param("stock")Stock stock);

}
