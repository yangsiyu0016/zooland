package com.zoo.mapper.erp.statistics;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.statistics.SellStatistics;

@Component
public interface SellStatisticsMapper {


	List<SellStatistics> page(@Param("start")Integer start, @Param("size")Integer size, @Param("sort")String sort, @Param("order")String order, @Param("keywords")String keywords, @Param("code")String code,
			@Param("productName")String productName, @Param("customerName")String customerName, @Param("start_initDate")String start_initDate, @Param("end_initDate")String end_initDate, @Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime, @Param("status")String status, @Param("companyId")String companyId);

	Long getCount(@Param("sort")String sort, @Param("order")String order, @Param("keywords")String keywords, @Param("code")String code,
			@Param("productName")String productName, @Param("customerName")String customerName, @Param("start_initDate")String start_initDate, @Param("end_initDate")String end_initDate, @Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime, @Param("status")String status, @Param("companyId")String companyId);
	
}
