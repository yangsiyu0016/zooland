package com.zoo.mapper.flow;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.flow.AssembledTask;
import com.zoo.model.flow.InventoryCheckTask;
import com.zoo.model.flow.OpeningInventoryTask;
import com.zoo.model.flow.ProductSplitTask;
import com.zoo.model.flow.PurchaseTask;
import com.zoo.model.flow.SellTask;

@Component
public interface CustomTaskMapper {
	long getAssembledTaskCount(@Param("keywords")String keywords,@Param("userId")String userId);
	List<AssembledTask> getAssembledTask(@Param("start")Integer start, 
			@Param("size")Integer size, 
			@Param("sort")String sort,
			@Param("order")String order,
			@Param("keywords")String keywords,
			@Param("userId")String userId);
	List<OpeningInventoryTask> getOpeningInventoryTask(
			@Param("start")Integer start,
			@Param("size")Integer size,
			@Param("sort")String sort,
			@Param("order")String order,
			@Param("keywords")String keywords,
			@Param("userId") String userId);
	long getOpeningInventoryTaskCount(@Param("keywords")String keywords,@Param("userId") String userId);
	List<SellTask> getSellTask(
			@Param("start")Integer start, 
			@Param("size")Integer size, 
			@Param("sort")String sort,
			@Param("order")String order,
			@Param("keywords")String keywords,
			@Param("userId")String userId);
	long getSellTaskCount(@Param("keywords")String keywords,@Param("userId")String userId);
	List<PurchaseTask> getPurchaseTask(@Param("start")Integer start, 
			@Param("size")Integer size, 
			@Param("sort")String sort,
			@Param("order")String order,
			@Param("keywords")String keywords,
			@Param("userId")String userId);
	long getPurchaseTaskCount(@Param("keywords")String keywords,@Param("userId")String userId);
	
	List<InventoryCheckTask> getInventoryCheckTask(@Param("start")Integer start, @Param("size")Integer size, @Param("userId")String userId);
	long getInventoryCheckTaskCount(@Param("userId")String userId);
	
	OpeningInventoryTask getOpeningInventoryTaskById(@Param("taskId")String taskId);
	PurchaseTask getPurchaseTaskById(@Param("taskId")String taskId);
	SellTask getSellTaskById(@Param("taskId")String taskId);
	InventoryCheckTask getInventoryCheckTaskById(@Param("taskId")String taskId);
	/*--------------拆分单流程任务代码开始-----------------*/
	List<ProductSplitTask> getProductSplitTask(
			@Param("start")Integer start, 
			@Param("size")Integer size, 
			@Param("sort")String sort,
			@Param("order")String order,
			@Param("keywords")String keywords,
			@Param("userId")String userId);
	Long getProductSplitTaskCount(@Param("keywords")String keywords,@Param("userId")String userId);
	ProductSplitTask getProductSplitTaskById(@Param("taskId")String taskId);
	/*--------------拆分单流程任务代码结束-----------------*/
	
	AssembledTask getAssembledTaskById(@Param("taskId")String taskId);
}
