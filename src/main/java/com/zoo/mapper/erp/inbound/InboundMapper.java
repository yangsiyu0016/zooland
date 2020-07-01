package com.zoo.mapper.erp.inbound;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.inbound.Inbound;

@Component
public interface InboundMapper {
	int addInbound(@Param("inbound")Inbound inbound);
	
	//分页获取入库信息
	List<Map<String, Object>> getInboundByPage(@Param("start") Integer start, @Param("size") Integer size);
	Long getTotleCount();
	
	List<Inbound> getInboundByForeignKey(@Param("foreignKey") String foreignKey);

	int deleteInboundByForeignKey(@Param("foreignKey")String foreignKey);
}
