package com.zoo.mapper.erp.statistics;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.statistics.SellStatistics;

@Component
public interface SellStatisticsMapper {

	List<SellStatistics> page(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	Long getCount(@Param("companyId") String companyId);
	
}
