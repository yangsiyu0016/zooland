package com.zoo.model.erp.product;

import java.util.List;

import lombok.Data;

@Data
public class ProductType {
	private String id;
	private String name;
	private String code;
	private String companyId;
	private boolean leaf;
	private String status;
	private String parentId;
	private List<ProductType> children;
}
