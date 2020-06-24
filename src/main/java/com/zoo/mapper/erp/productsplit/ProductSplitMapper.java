package com.zoo.mapper.erp.productsplit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.productsplit.ProductSplit;

@Component
public interface ProductSplitMapper {

	//分页查询
	//List<ProductSplit> getProductSplitByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	//获取总数
	//Long getCount(@Param("companyId") String companyId);
	
	//根据id获取拆分单
	ProductSplit getProductSplitById(@Param("id") String id);
	
	//添加数据
	void addProductSplit(@Param("productSplit") ProductSplit productSplit);
	
	//修改数据
	int updatePeoductSplit(@Param("productSplit") ProductSplit productSplit);
	
	//更改流程id
	int updateProcessInstanceId(@Param("id") String id, @Param("processInstanceId") String processInstanceId);
	
	//更改订单状态
	int updateProductSplitStatus(@Param("condition") Map<String, Object> condition);
	
	//更改是否被签收
	int updateProductSplitIsClaimed(@Param("condition") Map<String, Object> condition);
	
	//批量删除
	int deleteProductSplitByIds(@Param("ids") String[] ids);
	
	//更新未出库数量
	int updateNotOutNumberById(@Param("notOutNumber") BigDecimal notOutNumber, @Param("id") String id);

	List<ProductSplit> getProductSplitByPage(@Param("start") Integer start,
			@Param("size") Integer size, 
			@Param("keywords") String keywords, 
			@Param("code") String code,
			@Param("productCode") String productCode, 
			@Param("productName") String productName, 
			@Param("status") String status, 
			@Param("warehouseId") String warehouseId, 
			@Param("start_splitTime") String start_splitTime,
			@Param("end_splitTime") String end_splitTime, 
			@Param("start_ctime") String start_ctime, 
			@Param("end_ctime") String end_ctime, 
			@Param("companyId") String companyId, 
			@Param("sort") String sort, 
			@Param("order") String order);

	Long getCount(@Param("keywords") String keywords, 
			@Param("code") String code, 
			@Param("productCode") String productCode, 
			@Param("productName") String productName, 
			@Param("status") String status,
			@Param("warehouseId") String warehouseId, 
			@Param("start_splitTime") String start_splitTime,
			@Param("end_splitTime") String end_splitTime, 
			@Param("start_ctime") String start_ctime, 
			@Param("end_ctime") String end_ctime,
			@Param("companyId") String companyId);
	
}
