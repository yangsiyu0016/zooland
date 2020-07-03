package com.zoo.service.erp.outbound;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.outbound.OutboundDetailMapper;
import com.zoo.mapper.erp.outbound.OutboundMapper;
import com.zoo.model.erp.outbound.Outbound;
@Service
@Transactional
public class OutboundService {
	
	@Autowired
	private OutboundMapper outboundMapper;
	@Autowired
	private OutboundDetailMapper outboundDetailMapper;
	public List<Map<String, Object>> getOutboundsByPage(
			Integer page, 
			Integer size,
			String sort, 
			String order, 
			String keywords, 
			String code, 
			String productCode, 
			String productName, 
			String type, 
			String warehouseId, 
			String start_ctime, 
			String end_ctime) {
		Integer start = (page -1) * size;
		List<Map<String,Object>> list = outboundMapper.getOutboundsByPage(
				start, size,sort,order,LoginInterceptor.getLoginUser().getCompanyId(),
				keywords,code,productCode,productName,type,warehouseId,start_ctime,end_ctime);
		return list;
	}
	
	public Long getTotalCount(
			String keywords, 
			String code, 
			String productCode, 
			String productName, 
			String type, 
			String warehouseId, 
			String start_ctime, 
			String end_ctime) {
		return outboundMapper.getTotalCount(LoginInterceptor.getLoginUser().getCompanyId(),keywords,code,productCode,productName,type,warehouseId,start_ctime,end_ctime);
	}
	
	public void deleteByCostId(String costId) {
		Outbound outbound = outboundMapper.getOutboundByCostId(costId);
		outboundDetailMapper.deleteByOutboundId(outbound.getId());
		outboundMapper.deleteByCostId(costId);
	}
	
	public Outbound getOutboundByForeignKey(String foreignKey) {
		return outboundMapper.getOutboundByForeignKey(foreignKey);
	}

	public void addOutBound(Outbound outbound) {
		outboundMapper.addOutbound(outbound);
		
	}

	public Outbound getOutboundById(String id) {
		// TODO Auto-generated method stub
		return outboundMapper.getOutboundById(id);
	}

	public boolean checkHasDetails(String id) {
		long count = outboundMapper.getDetailCount(id);
		if(count>0) {
			return true;
		}else {
			return false;
		}
		
	}

	public void deleteById(String id) {
		outboundMapper.deleteById(id);
		outboundDetailMapper.deleteByOutboundId(id);
	}
}
