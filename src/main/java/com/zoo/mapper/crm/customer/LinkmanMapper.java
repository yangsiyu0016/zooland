package com.zoo.mapper.crm.customer;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.crm.Linkman;

@Component
public interface LinkmanMapper {

	int addLinkman(@Param("linkman")Linkman linkman);
	int updateLinkman(@Param("linkman")Linkman linkman);
}
