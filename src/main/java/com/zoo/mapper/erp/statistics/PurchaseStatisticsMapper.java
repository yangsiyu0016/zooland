package com.zoo.mapper.erp.statistics;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.statistics.PurchaseStatistics;
import com.zoo.model.erp.statistics.SearchData;

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
	List<PurchaseStatistics> search(@Param("searchData") SearchData searchData, @Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	/**
	 * 采购明细统计按条件查询总条数
	 */
	Long getSearchCount(@Param("searchData") SearchData searchData, @Param("companyId") String companyId); 
}
