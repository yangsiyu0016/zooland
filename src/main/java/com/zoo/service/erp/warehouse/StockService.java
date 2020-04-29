package com.zoo.service.erp.warehouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.warehouse.Stock;

import net.sf.json.JSONObject;

@Service
@Transactional
public class StockService {
	@Autowired
	StockMapper stockMapper;
	@Autowired
	SpecParamMapper paramMapper;
	public List<Stock> getStockByPage(Integer page,Integer size){
		Integer start = (page-1)*size;
		List<Stock> stocks = stockMapper.getStockByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		buildSpec(stocks);
		return stocks;
	}
	private void buildSpec(List<Stock> stocks) {
		for(Stock stock:stocks) {
			ProductSku sku = stock.getProductSku();
			//通用规格参数处理
			String genericSpec = sku.getProduct().getProductDetail().getGenericSpec();
			Map<String,String> map = new HashMap<String,String>();
			JSONObject obj = JSONObject.fromObject(genericSpec);
			Set<String> keyset = obj.keySet();
			for(String key:keyset) {
				SpecParam param = paramMapper.getParamById(key);
				map.put(param.getName(), StringUtils.isBlank(obj.getString(key))?"其它":obj.getString(key));
			}
			sku.getProduct().getProductDetail().setGenericSpec(map.toString());
			
			
			String ownSpec = sku.getOwnSpec();
			 map = new HashMap<String,String>();
			 obj  = JSONObject.fromObject(ownSpec);
			keyset = obj.keySet();
			for(String key:keyset) {
				SpecParam param = paramMapper.getParamById(key);
				map.put(param.getName(), obj.getString(key));
			}
			sku.setOwnSpec(map.toString());
			stock.setProductSku(sku);
		}
		
	}
	public long getStockCount() {
		return stockMapper.getStockCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	public Stock getStock(String skuId, String warehouseId) {
		// TODO Auto-generated method stub
		return stockMapper.getStock(skuId,warehouseId);
	}
	public int addStock(Stock stock) {
		return stockMapper.addStock(stock);	
	}
	public int updateStock(Stock stock) {
		return stockMapper.updateStock(stock);
		
	}

}
