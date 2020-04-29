package com.zoo.model.erp.product;

import lombok.Data;

@Data
public class ProductDetail {
	private String productId;
	private String description;
	private String genericSpec;//普通属性
	private String specialSpec;//特殊属性
	private String packageList;//包装清单
	private String afterService;//售后服务
}
