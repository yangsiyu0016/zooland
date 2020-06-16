package com.zoo.controller.erp.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.product.ProductBrand;
import com.zoo.service.erp.product.ProductBrandService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/product/brand")
public class ProductBrandController {
	@Autowired
	ProductBrandService brandService;
	@GetMapping("page")
	public Map<String,Object> getBrandByPage(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<ProductBrand> brands = brandService.getBrandByPage(page, size);
		Long count = brandService.getCount();
		map.put("brands", brands);
		map.put("count", count);
		return map;
	}
	@GetMapping("list")
	public List<ProductBrand> getBrandList(){
		List<ProductBrand> brands = brandService.getList();
		return brands;
	}
	@GetMapping("getBrandByTypeId")
	public List<ProductBrand> getBrandByTypeId(@RequestParam("typeId")String typeId){
		List<ProductBrand> brands = brandService.getBrandByTypeId(typeId);
		return brands;
	}
	@PostMapping("add")
	public RespBean addProductBrand(ProductBrand brand) {
		try {
			brandService.addProductBrand(brand);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("update")
	public RespBean updateProductBrand(ProductBrand brand,String[] typeIds) {
		try {
			brandService.updateProductBrand(brand,typeIds);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
