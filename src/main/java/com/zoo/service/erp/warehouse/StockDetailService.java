package com.zoo.service.erp.warehouse;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.model.erp.warehouse.StockDetail;

@Service
@Transactional
public class StockDetailService {
	@Autowired
	StockDetailMapper detailMapper;
	public int addStockDetail(StockDetail stockDetail) {
		return detailMapper.addDetail(stockDetail);
		
	}
	public StockDetail getStockDetail(String stockId, String goodsAllocationId) {
		// TODO Auto-generated method stub
		return detailMapper.getDetail(stockId,goodsAllocationId);
	}
	public int updateStockDetail(StockDetail stockDetail) {
		return detailMapper.updateStockDetail(stockDetail);
		
	}

}
