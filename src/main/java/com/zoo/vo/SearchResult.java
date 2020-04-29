package com.zoo.vo;

import java.util.List;
import java.util.Map;

import com.zoo.model.erp.product.ProductBrand;
import com.zoo.model.erp.product.ProductType;
import com.zoo.model.search.Goods;

public class SearchResult extends PageResult<Goods> {
	public SearchResult(Long total, Integer totalPage, List<Goods> items,List<ProductBrand> brands,List<ProductType> types,List<Map<String,Object>> specs) {
		super(total, totalPage, items);
		this.brands = brands;
		this.types = types;
		this.specs = specs;
	}


	private List<ProductBrand> brands;
	private List<ProductType> types;
	
	private List<Map<String,Object>> specs;//规格参数过滤条件

	public List<ProductBrand> getBrands() {
		return brands;
	}

	public void setBrands(List<ProductBrand> brands) {
		this.brands = brands;
	}

	public List<ProductType> getTypes() {
		return types;
	}

	public void setTypes(List<ProductType> types) {
		this.types = types;
	}

	public List<Map<String, Object>> getSpecs() {
		return specs;
	}

	public void setSpecs(List<Map<String, Object>> specs) {
		this.specs = specs;
	}
	
}
