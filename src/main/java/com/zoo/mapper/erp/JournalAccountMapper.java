package com.zoo.mapper.erp;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.JournalAccount;

@Component
public interface JournalAccountMapper {
	List<JournalAccount> getJournalAccountByPage(
			@Param("start")Integer start,
			@Param("size")Integer size,
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("warehouseId")String warehouseId,
			@Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime,
			@Param("companyId") String companyId,
			@Param("sort") String sort,
			@Param("order") String order);
	long getCount(
			@Param("keywords")String keywords,
			@Param("code")String code,
			@Param("productCode")String productCode,
			@Param("productName")String productName,
			@Param("warehouseId")String warehouseId,
			@Param("companyId")String companyId);
	int addJournalAccount(@Param("ja")JournalAccount ja);
	int deleteByOrderDetailId(@Param("orderDetailId")String orderDetailId);

}
