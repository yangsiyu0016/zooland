package com.zoo.model.erp.product;

import lombok.Data;

@Data
public class ProductSku {
	private String id;
	private String name;
	private String code;
	private String productId;
	private String ownSpec;// 商品特殊规格的键值对
	private String indexes;// 商品特殊规格的下标
	private String images;
	private Product product;
}
