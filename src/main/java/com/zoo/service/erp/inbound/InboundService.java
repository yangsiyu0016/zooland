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
	public List<Map<String, Object>> getInboundByPage(Integer page, Integer size) {
		Integer start = (page - 1) * size;
		
		List<Map<String, Object>> map = inboundMapper.getInboundByPage(start, size);
		for (int i = 0; i < map.size(); i++) {
			
			map.get(i).put("ctime", Date2StringUtils.Object2String(map.get(i).get("ctime")));
		}
		
		return map;
	}
	public Long getTotleCount() {
		Long totleCount = inboundMapper.getTotleCount();
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
}
