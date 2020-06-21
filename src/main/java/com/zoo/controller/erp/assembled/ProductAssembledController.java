package com.zoo.controller.erp.assembled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@RequestMapping(value="{id}",method=RequestMethod.GET)
	public ProductAssembled getProductAssembledById(@PathVariable String id) {
		return paService.getProductAssembledById(id);
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
	@PutMapping("update")
	public RespBean updateProductAssembled(@RequestBody ProductAssembled productAssembled) {
		try {
			paService.updateProductAssembled(productAssembled);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean deleteProductAssembledById(@PathVariable String ids) {
		try {
			paService.deleteProductAssembledById(ids);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
