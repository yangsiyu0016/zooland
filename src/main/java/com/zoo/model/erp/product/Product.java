package com.zoo.model.erp.product;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Product {
	private String id;
	private String name;
	private String code;
	private String brandId;
	private String typeId;
	private Date ctime;
	private String companyId;
	private ProductDetail productDetail;
	private ProductBrand productBrand;
	private List<ProductSku> skus;
	
	private String typeName;
}
