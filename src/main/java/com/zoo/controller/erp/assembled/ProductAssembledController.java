package com.zoo.controller.erp.assembled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.service.erp.assembled.ProductAssembledService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/assembled")
public class ProductAssembledController {
	@Autowired
	ProductAssembledService paService;
	@GetMapping("page")
	public Map<String,Object> page(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<ProductAssembled> productAssembleds = paService.getProductAssembledByPage(page,size);
		long count = paService.getCount();
		map.put("productAssembleds", productAssembleds);
		map.put("count", count);
		return map;
	}
	@PostMapping("add")
	public RespBean addProductAssembled(@RequestBody ProductAssembled productAssembled) {
		try {
			paService.addProductAssembled(productAssembled);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
