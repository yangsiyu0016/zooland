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
	 * 采购明细统计分页查询
	 * @param start
	 * @param size
	 * @return
	 */
	List<PurchaseStatistics> page(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	/**
	 * 采购明细统计查询总条数
	 */
	Long getCount(@Param("companyId") String companyId); 
}
