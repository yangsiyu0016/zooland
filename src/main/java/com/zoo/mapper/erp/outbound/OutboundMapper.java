package com.zoo.mapper.erp.outbound;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.outbound.Outbound;

@Component
public interface OutboundMapper {
	int addOutbound(@Param("outbound")Outbound outbound);
}
