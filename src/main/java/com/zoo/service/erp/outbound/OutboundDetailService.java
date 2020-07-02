package com.zoo.service.erp.outbound;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.outbound.OutboundDetailMapper;
import com.zoo.model.erp.outbound.OutboundDetail;

@Service
@Transactional
public class OutboundDetailService {
	@Autowired
	OutboundDetailMapper detailMapper;
	public List<OutboundDetail> getDetailByOuboundForeignKey(String foreignKey){
		return detailMapper.getDetailByOuboundForeignKey(foreignKey);
	}
	public void addDetail(OutboundDetail detail) {
		detailMapper.addDetail(detail);		
	}
}
