package com.zoo.service.erp.inbound;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.exception.ZooException;
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
	public void addDetail(InboundDetail detail) {
		inboundDetailMapper.addDetail(detail);
		
	}
	public InboundDetail updatePrice(String id,String price) {
		InboundDetail inboundDetail = this.getDetailById(id);
		if(inboundDetail.getFinished()) {
			throw new ZooException("已入库完成不能修改成本价");
		}
		inboundDetail.setPrice(new BigDecimal(price));
		inboundDetail.setTotalMoney(new BigDecimal(price).multiply(inboundDetail.getNumber()));
		
		inboundDetailMapper.update(inboundDetail);
		return inboundDetail;
	}
	public InboundDetail getDetailById(String id) {
		// TODO Auto-generated method stub
		return inboundDetailMapper.getDetailById(id);
	}

}
