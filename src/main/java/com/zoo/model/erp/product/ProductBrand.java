package com.zoo.model.erp.product;

import java.util.List;

import lombok.Data;

@Data
public class ProductBrand {
	private String id;
	private String name;
	private String description;
	private String companyId;
	private String status;
	private List<List<String>> typeIds;
}
