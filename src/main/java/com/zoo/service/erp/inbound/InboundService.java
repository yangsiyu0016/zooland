package com.zoo.service.erp.inbound;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.erp.inbound.InboundMapper;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.utils.Date2StringUtils;

@Service
@Transactional
public class InboundService {
	
	@Autowired
	private InboundMapper inboundMapper;
	@Autowired
	private InboundDetailService inbouncDetailService;
	//分页获取入库数据
	public List<Inbound> getInboundByPage(
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
			String start_time, 
			String end_time) {
		Integer start = (page - 1) * size;
		
		List<Inbound> list = inboundMapper.getInboundByPage(start, size, sort, order, keywords, code, productCode, productName, type, warehouseId, start_time, end_time);
		/*
		 * for (int i = 0; i < map.size(); i++) {
		 * 
		 * map.get(i).put("ctime",
		 * Date2StringUtils.Object2String(map.get(i).get("ctime"))); }
		 */
		
		return list;
	}
	public Long getTotleCount(
			String keywords, 
			String code, 
			String productCode, 
			String productName, 
			String type, 
			String warehouseId, 
			String start_time, 
			String end_time) {
		Long totleCount = inboundMapper.getTotleCount(keywords, code, productCode, productName, type, warehouseId, start_time, end_time);
		return totleCount;
	}
	
	public List<Inbound> getInboundByForeignKey(String id) {
		return inboundMapper.getInboundByForeignKey(id);
	}
	public void deleteInboundByForeignKey(String foreignKey) {
		
		List<Inbound> inbounds = inboundMapper.getInboundByForeignKey(foreignKey);
		for(Inbound inbound:inbounds) {
			inbouncDetailService.deleteDetailByInboundId(inbound.getId());
		}
		
		inboundMapper.deleteInboundByForeignKey(foreignKey);	
	}
	
	/**
	 * 根据id获取入库单
	 * @param id
	 * @return
	 */
	public Inbound getInboundById(String id) {
		// TODO Auto-generated method stub
		return inboundMapper.getInboundById(id);
	}
}
