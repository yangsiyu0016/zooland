package com.zoo.service.erp.productsplit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.mapper.erp.inbound.InboundMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitDetailMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.productsplit.ProductSplitDetail;

@Service
public class ProductSplitDetailService {

	@Autowired
	private ProductSplitDetailMapper detailMapper;
	
	@Autowired
	StockMapper stockMapper;
	@Autowired
	StockDetailMapper stockDetailMapper;
	@Autowired
	InboundMapper inboundMapper;
	@Autowired
	InboundDetailMapper inboundDetailMapper;
	@Autowired
	GoodsAllocationMapper gaMapper;
	@Autowired
	ProductSplitMapper splitMapper;
	
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

	public void updateNotInNumberById(BigDecimal notInNumber, String id) {
		// TODO Auto-generated method stub
		detailMapper.updateNumber(id, notInNumber);
	}

}
