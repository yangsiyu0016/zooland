package com.zoo.service.erp.productsplit;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.productsplit.ProductSplitDetailMapper;
import com.zoo.model.erp.productsplit.ProductSplitDetail;

@Service
public class ProductSplitDetailService {

	@Autowired
	private ProductSplitDetailMapper detailMapper;
	
	public ProductSplitDetail addDetail(ProductSplitDetail detail) {
		String id = UUID.randomUUID().toString();
		detail.setId(id);
		detail.setCtime(new Date());
		detailMapper.addDetail(detail);
		return detail;
	}
	
	public int  updateDetail(ProductSplitDetail detail) {
		return detailMapper.updateDetail(detail);
	}
	
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		detailMapper.deleteDetailById(split);
	}
}
