package com.zoo.mapper.erp.purchase;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.PurchaseDetail;

@Component
public interface PurchaseDetailMapper {

	int addDetail(@Param("detail")PurchaseDetail detail);

	PurchaseDetail getDetailById(@Param("id")String id);

}
