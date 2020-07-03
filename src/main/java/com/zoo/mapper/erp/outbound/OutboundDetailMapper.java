package com.zoo.mapper.erp.outbound;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.outbound.OutboundDetail;

@Component
public interface OutboundDetailMapper {
	List<OutboundDetail> getDetailByOuboundForeignKey(@Param("foreignKey")String foreignKey);
	int addDetail(@Param("detail")OutboundDetail detail);

	int deleteByOutboundId(@Param("outboundId")String outboundId);
	
	int update(@Param("detail") OutboundDetail detail);
	OutboundDetail getDetailById(@Param("id")String id);
	int outboundDetailId(@Param("id")String id);
	
}
