package com.zoo.service.erp.inbound;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.inbound.InboundDetailMapper;

@Service
@Transactional
public class InboundDetailService {
	@Autowired
	InboundDetailMapper inboundDetailMapper;
	public int deleteDetailByInboundId(String inboundId) {
		return inboundDetailMapper.deleteDetailByInboundId(inboundId);
		
	}

}
