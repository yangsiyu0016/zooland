package com.zoo.service.erp.warehouse;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.Warehouse;
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
	public List<Stock> getExportStocks(String keywords,String productCode,String productName,String warehouseId) {
		List<Stock> list = stockMapper.getStockByPage(null, null, keywords, productCode, productName, warehouseId, LoginInterceptor.getLoginUser().getCompanyId());
		Stock stock = new Stock();
		Warehouse warehouse = new Warehouse();
		warehouse.setName("合计");
		stock.setWarehouse(warehouse);
		BigDecimal usableNumber = new BigDecimal("0");//仓库可用数量
		BigDecimal lockedNumber = new BigDecimal("0");//仓库锁定数量
		for(Stock stock2: list) {
			usableNumber = usableNumber.add(stock2.getUsableNumber());
			lockedNumber = lockedNumber.add(stock2.getLockedNumber() == null? new BigDecimal("0") : stock2.getLockedNumber());
		}
		stock.setUsableNumber(usableNumber);
		stock.setLockedNumber(lockedNumber);
		Product product = new Product();
		product.setCode("");
		product.setName("");
		stock.setProduct(product);
		list.add(stock);
		return list;
	}
}
