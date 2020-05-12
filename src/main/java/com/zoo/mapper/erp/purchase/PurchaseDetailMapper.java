package com.zoo.mapper.erp.purchase;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.PurchaseDetail;

@Component
public interface PurchaseDetailMapper {

	int addDetail(@Param("detail")PurchaseDetail detail);
	int updateDetail(@Param("detail")PurchaseDetail detail);
	PurchaseDetail getDetailById(@Param("id")String id);

	int deleteDetailById(@Param("ids")String[] ids);
	int updateNotOutNumber(@Param("id")String id,@Param("notOutNumber")BigDecimal notOutNumber);
	List<PurchaseDetail> getDetailByPurchaseId(@Param("purchaseId")String purchaseId);
	int updateNotInNumber(@Param("id")String id, @Param("notInNumber")BigDecimal notInNumber);

}
