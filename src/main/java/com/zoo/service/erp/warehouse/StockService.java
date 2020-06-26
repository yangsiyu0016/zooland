package com.zoo.service.erp.warehouse;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.warehouse.Stock;
@Service
@Transactional
public class StockService {
	@Autowired
	StockMapper stockMapper;
	public List<Stock> getStockByPage(Integer page,Integer size,String keywords,String productCode,String productName,String warehouseId){
		Integer start = (page-1)*size;
		List<Stock> stocks = stockMapper.getStockByPage(start, size,keywords,productCode,productName,warehouseId,LoginInterceptor.getLoginUser().getCompanyId());
		return stocks;
	}
	public long getStockCount(String keywords,String productCode,String productName,String warehouseId) {
		return stockMapper.getStockCount(keywords,productCode,productName,warehouseId,LoginInterceptor.getLoginUser().getCompanyId());
	}
	public Stock getStock(String productId, String warehouseId) {
		// TODO Auto-generated method stub
		return stockMapper.getStock(productId,warehouseId);
	}
	public int addStock(Stock stock) {
		return stockMapper.addStock(stock);	
	}
	public int updateStock(Stock stock) {
		return stockMapper.updateStock(stock);
		
	}

}
