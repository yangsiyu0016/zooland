package com.zoo.mapper.flow;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.flow.OpeningInventoryTask;
import com.zoo.model.flow.PurchaseTask;
import com.zoo.model.flow.SellTask;

@Component
public interface CustomTaskMapper {
	List<OpeningInventoryTask> getOpeningInventoryTask(@Param("start")Integer start,@Param("size")Integer size,@Param("userId") String userId);
	long getOpeningInventoryTaskCount(@Param("userId") String userId);
	List<SellTask> getSellTask(@Param("start")Integer start, @Param("size")Integer size, @Param("userId")String userId);
	long getSellTaskCount(@Param("userId")String userId);
	List<PurchaseTask> getPurchaseTask(@Param("start")Integer start, @Param("size")Integer size, @Param("userId")String userId);
	long getPurchaseTaskCount(@Param("userId")String userId);
}
