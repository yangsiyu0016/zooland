package com.zoo.service.erp.inbound;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.model.erp.inbound.InboundDetail;

@Service
@Transactional
public class InboundDetailService {
	@Autowired
	InboundDetailMapper inboundDetailMapper;
	public int deleteDetailByInboundId(String inboundId) {
		return inboundDetailMapper.deleteDetailByInboundId(inboundId);
		
	}
	public List<InboundDetail> getDetailByInboundForeignKey(String foreignKey) {
		// TODO Auto-generated method stub
		return inboundDetailMapper.getDetailByInboundForeignKey(foreignKey);
	}

}
