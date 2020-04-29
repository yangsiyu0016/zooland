package com.zoo.mapper.erp.inbound;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.inbound.Inbound;

@Component
public interface InboundMapper {
	int addInbound(@Param("inbound")Inbound inbound);
}
