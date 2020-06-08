package com.zoo.controller.erp.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.product.ProductSku;
import com.zoo.service.erp.product.ProductSkuService;
@RestController
@RequestMapping("/sku")
public class ProductSkuController {
	@Autowired
	ProductSkuService skuService;
	@RequestMapping("page")
	public Map<String,Object> getProductSkuByPage(@RequestParam("page")Integer page,@RequestParam("size")Integer size,@RequestParam("key")String key){
		Map<String,Object> map = new HashMap<String,Object>();
		List<ProductSku> skus = skuService.getProductSkuByPage(page,size,key);
		long count = skuService.getCount(key);
		map.put("skus", skus);
		map.put("count",count);
		return map;
	}
	
	@GetMapping("getProductSkuByProductId")
	public Map<String, Object> getProductSkuByProductId(@RequestParam("productId") String productId) {
		List<ProductSku> list = skuService.getProductByProductId(productId);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("skus", list);
		
		return map;
	}
	
}
