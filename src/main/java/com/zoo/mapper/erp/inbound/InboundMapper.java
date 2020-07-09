package com.zoo.mapper.erp.inbound;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.inbound.Inbound;

@Component
public interface InboundMapper {
	int addInbound(@Param("inbound")Inbound inbound);
	
	//分页获取入库信息
	List<Inbound> getInboundByPage(
			@Param("start") Integer start, 
			@Param("size") Integer size,
			@Param("sort") String sort, 
			@Param("order") String order,
			@Param("keywords") String keywords,
			@Param("code") String code,
			@Param("productCode") String productCode,
			@Param("productName") String productName,
			@Param("type") String type,
			@Param("warehouseId") String warehouseId,
			@Param("start_time") String start_time,
			@Param("end_time") String end_time);
	Long getTotleCount(
			@Param("keywords") String keywords,
			@Param("code") String code,
			@Param("productCode") String productCode,
			@Param("productName") String productName,
			@Param("type") String type,
			@Param("warehouseId") String warehouseId,
			@Param("start_time") String start_time,
			@Param("end_time") String end_time);
	
	List<Inbound> getInboundByForeignKey(@Param("foreignKey") String foreignKey);

	int deleteInboundByForeignKey(@Param("foreignKey")String foreignKey);

	Inbound getInboundById(@Param("id")String id);

	long getDetailCount(@Param("id")String id);

	int deleteById(@Param("id")String id);
}
