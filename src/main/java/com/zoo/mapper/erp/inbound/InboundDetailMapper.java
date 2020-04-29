package com.zoo.mapper.erp.inbound;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.inbound.InboundDetail;

@Component
public interface InboundDetailMapper {

	int addDetail(@Param("detail")InboundDetail detail);
	
}
