package com.zoo.mapper.erp.productsplit;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.productsplit.ProductSplit;

@Component
public interface ProductSplitMapper {

	//分页查询
	List<ProductSplit> getProductSplitByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	//获取总数
	Long getCount(@Param("companyId") String companyId);
	
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
	
}
