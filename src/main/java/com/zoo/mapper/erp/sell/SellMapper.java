package com.zoo.mapper.erp.sell;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.sell.Sell;

@Component
public interface SellMapper {

	int addSell(@Param("sell")Sell sell);

	List<Sell> getSellByPage(
			@Param("start")Integer start, 
			@Param("size")Integer size, 
			@Param("companyId")String companyId,
			@Param("cuserId")String cuserId,
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("customerName")String customerName,
			@Param("start_initDate")String start_initDate,
			@Param("end_initDate")String end_initDate,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime,
			@Param("status")String[] status,
			@Param("sort")String sort,
			@Param("order")String order);

	long getCount(
			@Param("companyId")String companyId,
			@Param("cuserId")String cuserId,
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("customerName")String customerName,
			@Param("start_initDate")String start_initDate,
			@Param("end_initDate")String end_initDate,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime,
			@Param("status")String[] status);
	
	Sell getSellById(@Param("id")String id);
	int updateProcessInstanceId(@Param("id") String id,@Param("processInstanceId")String processInstanceId);
	public int updateSellStatus(@Param("condition")Map<String, Object> condition);
	int deleteSellById(@Param("ids")String[] ids);

	int updateSell(@Param("sell")Sell sell);
	////更改订单是否被签收
	void updateSellIsClaimed(@Param("condition")Map<String, Object> condition);
}
