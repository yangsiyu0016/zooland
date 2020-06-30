package com.zoo.mapper.erp.assembled;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.assembled.ProductAssembled;

@Component
public interface ProductAssembledMapper {
	List<ProductAssembled> getProductAssembledByPage(
			@Param("start")Integer start, 
			@Param("size")Integer size, 
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("status")String status,
			@Param("warehouseId")String warehouseId,
			@Param("start_assembledTime")String start_assembledTime,
			@Param("end_assembledTime")String end_assembledTime,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime,
			@Param("companyId")String companyId,
			@Param("sort")String sort,
			@Param("order")String order);
	long getCount(
			@Param("keywords")String keywords, 
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("status")String status,
			@Param("warehouseId")String warehouseId,
			@Param("start_assembledTime")String start_assembledTime,
			@Param("end_assembledTime")String end_assembledTime,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime,
			@Param("companyId")String companyId);
	ProductAssembled getProductAssembledById(@Param("id") String id);
	int addProductAssembled(@Param("pa")ProductAssembled pa);
	int updateProductAssembled(@Param("pa")ProductAssembled pa);
	int updateProcessInstanceId(@Param("id") String id,@Param("processInstanceId")String processInstanceId);
	public int updateProductAssembledStatus(@Param("condition")Map<String, Object> condition);
	int deleteProductAssembledById(@Param("ids")String[] ids);
	int updateNotInNumber(@Param("notInNumber") BigDecimal notInNumber, @Param("id") String id);
}
