package com.zoo.model.erp.assembled;

import java.math.BigDecimal;

import com.zoo.model.erp.product.Product;

import lombok.Data;

@Data
public class ProductAssembledMaterial {
	private String id;
	private String productAssembledId;
	private Product product;
	private BigDecimal number;
	private BigDecimal needNumber;
	private BigDecimal notOutNumber;
}
