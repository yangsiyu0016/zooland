package com.zoo.service.erp.openingInventory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.openingInventory.OpeningInventoryDetailMapper;
import com.zoo.model.erp.openingInventory.OpeningInventoryDetail;

@Service
@Transactional
public class OpeningInventoryDetailService {
	@Autowired
	OpeningInventoryDetailMapper detailMapper;
	public List<OpeningInventoryDetail> getDetailsByOpeningInventoryId(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public int updatePrice(String id, String costPrice,String totalMoney) {
		return detailMapper.updatePrice(id,new BigDecimal(costPrice),new BigDecimal(totalMoney));
		
	}
	public OpeningInventoryDetail addDetail(OpeningInventoryDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		detail.setCtime(new Date());
		detailMapper.addDetail(detail);
		return detail;
	}
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		detailMapper.deleteDetailById(split);
	}
}
