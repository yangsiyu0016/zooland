package com.zoo.mapper.erp.purchase;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.Purchase;

@Component
public interface PurchaseMapper {

	int addPurchase(@Param("purchase")Purchase purchase);

	List<Purchase> getPurchaseByPage(@Param("start")Integer start, @Param("size")Integer size, @Param("companyId")String companyId);

	long getCount(@Param("companyId")String companyId);

	int updatePurchaseStatus(@Param("condition")Map<String, Object> condition);

	Purchase getPurchaseById(@Param("id")String id);

	int updateProcessInstanceId(@Param("id")String id, @Param("processInstanceId")String processInstanceId);
	
}
