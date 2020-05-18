package com.zoo.service.erp.outbound;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.erp.outbound.OutboundMapper;
import com.zoo.utils.Date2StringUtils;

@Service
@Transactional
public class OutBoundService {
	
	@Autowired
	private OutboundMapper outboundMapper;
	
	public List<Map<String, Object>> getOutboundsByPage(@Param("page") Integer page, @Param("size") Integer size) {
		Integer start = (page -1) * size;
		List<Map<String,Object>> list = outboundMapper.getOutboundsByPage(start, size);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).put("ctime", Date2StringUtils.Object2String(list.get(i).get("ctime")));
		}
		return list;
	}
	
	public Long getTotalCount() {
		return outboundMapper.getTotalCount();
	}
}
