package com.zoo.mapper.erp.outbound;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.outbound.Outbound;

@Component
public interface OutboundMapper {
	int addOutbound(@Param("outbound")Outbound outbound);
	List<Map<String, Object>> getOutboundsByPage(@Param("start") Integer start, @Param("size") Integer size);
	Long getTotalCount();
	int deleteByCostId(@Param("costId")String costId);
	Outbound getOutboundByCostId(@Param("costId")String costId);
}
