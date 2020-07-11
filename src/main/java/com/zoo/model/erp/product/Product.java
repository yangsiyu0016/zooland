package com.zoo.model.erp.product;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Product {
	private String id;
	private String name;
	private String code;
	private String typeId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	private String companyId;
	private ProductType productType;
	private ProductBrand productBrand;
	private Unit unit;
	private String typeName;
	
	private String spec;//规格
	private String weight;//重量
	private String color;//颜色
	private String puse;//用途
	private String description;//备注
	
	private String imageUrl;//图片地址
	
	private String deleted;//是否已删除  0未删除 1已删除
	
	private Boolean hasBom;//是否已做bom 0未做 1 已做
	
	private String mnemonic;//助记码
	
}
