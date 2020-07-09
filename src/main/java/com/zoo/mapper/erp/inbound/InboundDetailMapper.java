package com.zoo.mapper.erp.inbound;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.inbound.InboundDetail;

@Component
public interface InboundDetailMapper {

	int addDetail(@Param("detail")InboundDetail detail);
	
	int update(@Param("detail") InboundDetail detail);
	int deleteDetailByInboundId(@Param("inboundId")String inboundId);

	List<InboundDetail> getDetailByInboundForeignKey(@Param("foreignKey")String foreignKey);

	InboundDetail getDetailById(@Param("id")String id);

	int updateFinished(@Param("id")String id, @Param("finished")boolean finished);
	
}
