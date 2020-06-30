package com.zoo.mapper.erp.purchase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.Purchase;

@Component
public interface PurchaseMapper {

	int addPurchase(@Param("purchase")Purchase purchase);

	List<Purchase> getPurchaseByPage(
			@Param("start")Integer start, 
			@Param("size")Integer size, 
			@Param("companyId")String companyId,
			@Param("cuserId")String cuserId,
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("supplierName")String supplierName,
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
			@Param("supplierName")String supplierName,
			@Param("start_initDate")String start_initDate,
			@Param("end_initDate")String end_initDate,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime,
			@Param("status")String[] status);

	int updatePurchaseStatus(@Param("condition")Map<String, Object> condition);

	Purchase getPurchaseById(@Param("id")String id);

	int updateProcessInstanceId(@Param("id")String id, @Param("processInstanceId")String processInstanceId);

	int updatePurchase(@Param("purchase")Purchase purchase);
	
	void updatePurchaseIsClaimed(@Param("condition")Map<String, Object> condition);

	int deletePurchaseById(@Param("ids")String[] ids);
}
