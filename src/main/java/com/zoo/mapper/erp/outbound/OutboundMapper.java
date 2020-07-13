package com.zoo.mapper.erp.outbound;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.outbound.Outbound;

@Component
public interface OutboundMapper {
	int addOutbound(@Param("outbound")Outbound outbound);
	List<Outbound> getOutboundsByPage(
			@Param("start") Integer start, 
			@Param("size") Integer size, 
			@Param("sort") String sort, 
			@Param("order") String order, 
			@Param("companyId") String companyId, 
			@Param("keywords") String keywords, 
			@Param("code") String code, 
			@Param("productCode") String productCode, 
			@Param("productName") String productName, 
			@Param("type") String type, 
			@Param("warehouseId") String warehouseId, 
			@Param("start_ctime") String start_ctime, 
			@Param("end_ctime") String end_ctime);
	Long getTotalCount(
			@Param("companyId") String companyId, 
			@Param("keywords") String keywords, 
			@Param("code") String code, 
			@Param("productCode") String productCode, 
			@Param("productName") String productName, 
			@Param("type") String type, 
			@Param("warehouseId") String warehouseId, 
			@Param("start_ctime") String start_ctime, 
			@Param("end_ctime") String end_ctime);
	int deleteByCostId(@Param("costId")String costId);
	Outbound getOutboundByCostId(@Param("costId")String costId);
	List<Outbound> getOutboundByForeignKey(@Param("foreignKey") String foreignKey);
	Outbound getOutboundById(@Param("id")String id);
	long getDetailCount(@Param("id")String id);
	int deleteById(@Param("id")String id);
}
