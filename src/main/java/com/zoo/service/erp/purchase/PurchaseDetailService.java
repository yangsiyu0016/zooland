package com.zoo.service.erp.purchase;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.purchase.PurchaseDetailMapper;
import com.zoo.model.erp.purchase.PurchaseDetail;

@Service
@Transactional
public class PurchaseDetailService {
	@Autowired
	PurchaseDetailMapper detailMapper;
	public PurchaseDetail addDetail(PurchaseDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		detailMapper.addDetail(detail);
		return detail;
	}
	public void updateDetail(PurchaseDetail detail) {
		detailMapper.updateDetail(detail);
	}
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		detailMapper.deleteDetailById(split);
		
	}
}
