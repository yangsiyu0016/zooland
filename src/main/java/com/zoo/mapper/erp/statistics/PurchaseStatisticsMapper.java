package com.zoo.mapper.erp.statistics;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.statistics.PurchaseStatistics;

/**
 * 
 * @author aa
 *
 */
@Component
public interface PurchaseStatisticsMapper {

	/**
	 * 采购明细统计分页
	 * @param start
	 * @param size
	 * @return
	 */
	List<PurchaseStatistics> page(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	/**
	 * 采购明细统计总条数
	 */
	Long getCount(@Param("companyId") String companyId); 

	/**
	 * 采购明细统计分页按条件查询
	 * @param start
	 * @param size
	 * @return
	 */
	List<PurchaseStatistics> search(@Param("start")Integer start, @Param("size")Integer size, @Param("sort")String sort, @Param("order")String order, @Param("keywords")String keywords, @Param("code")String code,
			@Param("productName")String productName, @Param("supplierName")String supplierName, @Param("start_initDate")String start_initDate, @Param("end_initDate")String end_initDate, @Param("start_ctime")String start_ctime,
			@Param("end_ctime")String end_ctime, @Param("status")String status, @Param("companyId")String companyId);
	/**
	 * 采购明细统计按条件查询总条数
	 */
	Long getSearchCount(@Param("sort")String sort, @Param("order")String order, @Param("keywords")String keywords, @Param("code")String code, @Param("productName")String productName,
			@Param("supplierName")String supplierName, @Param("start_initDate")String start_initDate, @Param("end_initDate")String end_initDate, @Param("start_ctime")String start_ctime, @Param("end_ctime")String end_ctime,
			@Param("status")String status, @Param("companyId")String companyId); 
}
