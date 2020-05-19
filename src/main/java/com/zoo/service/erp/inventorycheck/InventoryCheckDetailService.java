package com.zoo.service.erp.inventorycheck;

import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.inventorycheck.InventoryCheckDetailMapper;
import com.zoo.model.erp.inventorycheck.InventoryCheckDetail;

@Service
@Transactional
public class InventoryCheckDetailService {
	@Autowired
	InventoryCheckDetailMapper detailMapper;
	public InventoryCheckDetail addDetail(InventoryCheckDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		detail.setCtime(new Date());
		detailMapper.addDetail(detail);
		return detail;
	}
	public void updateDetail(InventoryCheckDetail detail) {
		detailMapper.updateDetail(detail);
		
	}
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		detailMapper.deleteDetailById(split);
		
	}
}
