package com.zoo.controller.erp.productsplit;

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

import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/split")
public class ProductSplitController {

	@Autowired
	ProductSplitService splitService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ProductSplit> list = splitService.getProductSplitByPage(page, size);
		Long count = splitService.getCount();
		map.put("productSplits", list);
		map.put("count", count);
		
		return map;
	}
	
	@GetMapping("getProducrSplitById")
	public ProductSplit getProducrSplitById(@RequestParam("id") String id) {
		return splitService.getProductSplitById(id);
	}
	
	@PostMapping("add")
	public RespBean add(@RequestBody ProductSplit split) {
		try {
			splitService.addProductSplit(split);
			return new RespBean("200","保存成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PostMapping("update")
	public RespBean update(@RequestBody ProductSplit split) {
		 try {
			splitService.updatePeoductSplit(split);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("startFlow")
	public RespBean startFlow(@RequestParam("id") String id) {
		try {
			splitService.startProcess(id);
			return new RespBean("200", "启动成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			splitService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean delete(@PathVariable String ids) {
		try {
			splitService.deleteDetailById(ids);
			return new RespBean("200", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
}
