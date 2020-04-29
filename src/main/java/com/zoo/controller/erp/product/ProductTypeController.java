package com.zoo.controller.erp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.product.ProductType;
import com.zoo.service.erp.product.ProductTypeService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/product/type")
public class ProductTypeController {
	@Autowired
	ProductTypeService typeService;
	@GetMapping("tree")
	public List<ProductType> getTreeData(){
		List<ProductType> types = typeService.getTreeData();
		return types;
	}
	@PostMapping("add")
	public RespBean addProductType(ProductType type) {
		try {
			typeService.addProductType(type);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("update")
	public RespBean updateProductType(ProductType type) {
		try {
			typeService.updateProductType(type);
			return new RespBean("200","修改成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
