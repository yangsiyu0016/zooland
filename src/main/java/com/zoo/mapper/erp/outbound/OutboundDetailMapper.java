package com.zoo.mapper.erp.outbound;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.outbound.OutboundDetail;

@Component
public interface OutboundDetailMapper {
	int addDetail(@Param("detail")OutboundDetail detail);
}
