package com.zoo.service.erp.sell;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.sell.SellDetailMapper;
import com.zoo.model.erp.sell.SellDetail;
@Service
@Transactional
public class SellDetailService {
	@Autowired
	SellDetailMapper detailMapper;
	public List<SellDetail> getSellDetailBySellId(String id) {
		List<SellDetail> details = detailMapper.getDetailBySellId(id);
		return details;
	}
	public List<SellDetail> getNotOutDetailBySellId(String sellId) {
		List<SellDetail> details = detailMapper.getNotOutDetailBySellId(sellId);
		return details;
	}
	public SellDetail addDetail(SellDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		detail.setCtime(new Date());
		detailMapper.addDetail(detail);
		return detail;
	}
	public void updateDetail(SellDetail detail) {
		detailMapper.updateDetail(detail);
		
	}
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		detailMapper.deleteDetailById(split);
		
	}

}
